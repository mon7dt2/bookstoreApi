package mon7.project.bookstore.auth.controller;

import mon7.project.bookstore.admin.dao.AdminRepository;
import mon7.project.bookstore.admin.models.Admin;
import mon7.project.bookstore.admin.models.body.AdminRegisterBody;
import mon7.project.bookstore.auth.dao.AccountRespository;
import mon7.project.bookstore.auth.models.Account;
import mon7.project.bookstore.auth.models.body.CustomerRegisterBody;
import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.customer.models.data.Customer;
import mon7.project.bookstore.customer.models.view.HeaderProfile;
import mon7.project.bookstore.response_model.*;
import mon7.project.bookstore.utils.PasswordValidate;
import mon7.project.bookstore.utils.SendEmailUtils;
import mon7.project.bookstore.utils.UserDecodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auths")
public class AuthController {

    @Autowired
    AccountRespository accountRespository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AdminRepository adminRepository;

    @PostMapping("/customer/login")
    public Response CustomerLogin(@RequestHeader(HeaderConstant.AUTHORIZATION) String encodedString,
                                  @RequestBody String fcmToken) {
        Response response;
        try {
            Account account = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (accountRespository.findByUsername(account.getUsername()) == null) {
                return new NotFoundResponse("Account not exist");
            }
            if (accountRespository.isActivated(account.getUsername(), RoleConstants.CUSTOMER)) {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_VERIFIED);
            } else {
                Account login = accountRespository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
                if (login != null) {
                    Customer customer = customerRepository.findByAccount_Id(login.getId());
                    HeaderProfile profile = new HeaderProfile(customer.getId(), customer.getFullName(),
                            customer.getAvatarUrl(), customer.getEmail());
                    login.setFcmToken(fcmToken);
                    accountRespository.save(login);
                    response = new OkResponse(profile);
                } else {
                    response = new Response(HttpStatus.UNAUTHORIZED, ResponseConstant.Vi.WRONG_EMAIL_OR_PASSWORD);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @PostMapping("/admin/login")
    public Response AdminLogin(@RequestHeader(HeaderConstant.AUTHORIZATION) String encodedString) {
        Response response;
        try {
            Account account = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (accountRespository.isActivated(account.getUsername(), RoleConstants.ADMIN)) {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_VERIFIED);
            } else {
                Account login = accountRespository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
                if (login != null) {
                    response = new OkResponse();
                } else {
                    response = new Response(HttpStatus.UNAUTHORIZED, ResponseConstant.Vi.WRONG_EMAIL_OR_PASSWORD);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }




    @PostMapping("/customers/register")
    public Response register(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                             @Valid @RequestBody CustomerRegisterBody customerRegisterBody) {
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (!PasswordValidate.isPasswordValidate(u.getPassword())) {
                return new ForbiddenResponse(ResponseConstant.ErrorMessage.PASSWORD_TOO_SHORT);
            }

            Account account = accountRespository.findByUsername(u.getUsername());
            if (account != null) {
                return new ResourceExistResponse("Tai khoan da ton tai!");
            } else {
                account = new Account();
                account.setPassword(u.getPassword());
                account.setUsername(u.getUsername());
                account.setRole(RoleConstants.CUSTOMER);
                account.setActivated(true);

                accountRespository.save(account);

                Customer customer = new Customer();
                customer.setUser(account);
                customer.setFullName(customerRegisterBody.getFullName());
                customer.setPhone(customerRegisterBody.getPhone());
                customer.setEmail(customerRegisterBody.getEmail());
                customerRepository.save(customer);

                SendEmailUtils.sendEmailActiveAccount(u.getUsername());
                response = new OkResponse(u.getUsername());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @PostMapping("/admins/register")
    public Response adminRegister(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                  @Valid @RequestBody AdminRegisterBody adminRegisterBody) {
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (!PasswordValidate.isPasswordValidate(u.getPassword())) {
                return new ForbiddenResponse(ResponseConstant.ErrorMessage.PASSWORD_TOO_SHORT);
            }

            Account account = accountRespository.findByUsername(u.getUsername());
            if (account != null) {
                return new ResourceExistResponse("Tai khoan da ton tai!");
            } else {
                account = new Account();
                account.setPassword(u.getPassword());
                account.setUsername(u.getUsername());
                account.setRole(RoleConstants.ADMIN);
                account.setActivated(true);

                accountRespository.save(account);

                Admin admin = new Admin();
                admin.setFullName(adminRegisterBody.getFullName());
                admin.setPosition(adminRegisterBody.getPosition());

                adminRepository.save(admin);

//                SendEmailUtils.sendEmailrequest(u.getUsername());
                response = new OkResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/registration/confirm/{username}")
    public Response confirm_email(@PathVariable("username") String username) {
        Response response;
        try {
            username += ".com";
            if (accountRespository.findByUsername(username) == null) {
                return new NotFoundResponse("Email khong ton tai !");
            }
            accountRespository.activeAccount(true, username);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @PostMapping("/resend/registration/confirm/{username}")
    public Response resend_confirm_email(@PathVariable("username") String username) {
        Response response;
        try {
            username += ".com";
            SendEmailUtils.sendEmailActiveAccount(username);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

}

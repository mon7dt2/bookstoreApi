package mon7.project.bookstore.auth.controller;

import mon7.project.bookstore.admin.dao.AdminRepository;
import mon7.project.bookstore.admin.models.Admin;
import mon7.project.bookstore.admin.models.body.AdminRegisterBody;
import mon7.project.bookstore.admin.models.view.AdminLoginView;
import mon7.project.bookstore.auth.dao.AccountRespository;
import mon7.project.bookstore.auth.models.Account;
import mon7.project.bookstore.auth.models.body.CustomerRegisterBody;
import mon7.project.bookstore.auth.models.body.NewPassword;
import mon7.project.bookstore.auth.models.body.StaffRegisterBody;
import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.customer.models.data.Customer;
import mon7.project.bookstore.customer.models.view.HeaderProfile;
import mon7.project.bookstore.response_model.*;
import mon7.project.bookstore.staff.dao.StaffRepository;
import mon7.project.bookstore.staff.models.data.Staff;
import mon7.project.bookstore.staff.models.view.StaffLoginView;
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
    @Autowired
    StaffRepository staffRepository;

    @PostMapping("/admin/register")
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
                return new ResourceExistResponse(ResponseConstant.ErrorMessage.RESOURCE_EXIST);
            } else {
                account = new Account();
                account.setPassword(u.getPassword());
                account.setUsername(u.getUsername());
                account.setRole(RoleConstants.ADMIN);
                account.setActivated(1);

                accountRespository.save(account);

                Admin admin = new Admin();
                admin.setFullName(adminRegisterBody.getFullName());
                admin.setAccount(account);

                adminRepository.save(admin);
                response = new OkResponse();
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
            if (accountRespository.isActivated(account.getUsername(), RoleConstants.ADMIN) != 1) {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_VERIFIED);
            } else {
                Account login = accountRespository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
                if (login != null) {
                    Admin admin = adminRepository.findByAccount_Id(login.getId());
                    AdminLoginView view = new AdminLoginView(admin);
                    response = new OkResponse(view);
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

    @PostMapping("/staff/register")
    public Response staffRegister(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                  @Valid @RequestBody StaffRegisterBody staffRegisterBody) {
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (!PasswordValidate.isPasswordValidate(u.getPassword())) {
                return new ForbiddenResponse(ResponseConstant.ErrorMessage.PASSWORD_TOO_SHORT);
            }

            Account account = accountRespository.findByUsername(u.getUsername());
            if (account != null) {
                return new ResourceExistResponse(ResponseConstant.ErrorMessage.RESOURCE_EXIST);
            } else {
                account = new Account();
                account.setPassword(u.getPassword());
                account.setUsername(u.getUsername());
                account.setRole(RoleConstants.STAFF);
                account.setActivated(1);

                accountRespository.save(account);

                Staff staff = new Staff(staffRegisterBody);
                staff.setAccount(account);

                staffRepository.save(staff);
                StaffLoginView view = new StaffLoginView(staff);
                response = new OkResponse(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @PostMapping("/staff/login")
    public Response StaffLogin(@RequestHeader(HeaderConstant.AUTHORIZATION) String encodedString) {
        Response response;
        try {
            Account account = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (accountRespository.findByUsername(account.getUsername()) == null) {
                return new NotFoundResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_FOUND);
            }
            if (accountRespository.isActivated(account.getUsername(), RoleConstants.STAFF) != 1) {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_VERIFIED);
            } else {
                Account login = accountRespository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
                if (login != null) {
                    Staff staff = staffRepository.findByAccount_Id(login.getId());
                    StaffLoginView view = new StaffLoginView(staff);
                    response = new OkResponse(view);
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

    @PostMapping("/customer/register")
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
                return new ResourceExistResponse(ResponseConstant.ErrorMessage.RESOURCE_EXIST);
            } else {
                account = new Account();
                account.setPassword(u.getPassword());
                account.setUsername(u.getUsername());
                account.setRole(RoleConstants.CUSTOMER);
                account.setActivated(1);

                accountRespository.save(account);

                Customer customer = new Customer();
                customer.setAccount(account);
                customer.setFullName(customerRegisterBody.getFullName());
                customer.setPhone(customerRegisterBody.getPhone());
                customer.setEmail(customerRegisterBody.getEmail());
                customerRepository.save(customer);

                response = new OkResponse(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @PostMapping("/customer/login")
    public Response CustomerLogin(@RequestHeader(HeaderConstant.AUTHORIZATION) String encodedString,
                                  @RequestBody String fcmToken) {
        Response response;
        try {
            Account account = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (accountRespository.findByUsername(account.getUsername()) == null) {
                return new NotFoundResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_FOUND);
            }
            if (accountRespository.isActivated(account.getUsername(), RoleConstants.CUSTOMER) != 1) {
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

    @PutMapping("/{accountID}/password")
    public Response changePassword(@PathVariable("accountID") String accountID,
                                   @Valid @RequestBody NewPassword newPassword){
        Response response;
        try {
            Account account = accountRespository.getById(accountID);
            if (account == null) {
                return new NotFoundResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_FOUND);
            }else {
                if(account.getPassword().matches(newPassword.getOldPassword())){
                    account.setPassword(newPassword.getNewPassword());
                    accountRespository.save(account);
                    response = new OkResponse(ResponseConstant.MSG_OK);
                } else {
                    response = new Response(HttpStatus.CONFLICT, ResponseConstant.Vi.OLD_PASSWORD_MISMATCH);
                }
            }
        }catch (Exception e) {
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
                return new NotFoundResponse("Email không tồn tại !");
            }
            accountRespository.activeAccount(1, username);
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

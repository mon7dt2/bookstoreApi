package mon7.project.bookstore.auth.controller;

import mon7.project.bookstore.admin.dao.AdminRepository;
import mon7.project.bookstore.admin.models.Admin;
import mon7.project.bookstore.admin.models.body.AdminRegisterBody;
import mon7.project.bookstore.auth.dao.UserRespository;
import mon7.project.bookstore.auth.models.User;
import mon7.project.bookstore.auth.models.body.CustomerRegisterBody;
import mon7.project.bookstore.auth.models.body.NewPassword;
import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.customer.models.data.Customer;
import mon7.project.bookstore.customer.models.view.HeaderProfile;
import mon7.project.bookstore.response_model.*;
import mon7.project.bookstore.utils.EmailValidate;
import mon7.project.bookstore.utils.PasswordValidate;
import mon7.project.bookstore.utils.SendEmailUtils;
import mon7.project.bookstore.utils.UserDecodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auths")
@Api(value = "auth-api", description = "Nhóm API đăng nhập và cấp access token, Không yêu cầu access token")
public class AuthController {

    @Autowired
    UserRespository userRespository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AdminRepository adminRepository;

    @ApiOperation(value = "api đăng nhập cho khách hàng", response = Iterable.class)
    @PostMapping("/customer/login")
    public Response CustomerLogin(@ApiParam(name = "Authorization", value = "username+\":\"+password, lấy kết quả encode theo Base64, sau đó thêm \"Basic \" + kết quả")
                                  @RequestHeader(HeaderConstant.AUTHORIZATION) String encodedString,
                                  @RequestBody String fcmToken) {
        Response response;
        try {
            User user = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (userRespository.findByUsername(user.getUsername()) == null) {
                return new NotFoundResponse("Account not exist");
            }
            if (!userRespository.isAccountActivated(user.getUsername(), RoleConstants.CUSTOMER)) {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_VERIFIED);
            } else {
                User login = userRespository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
                if (login != null) {
                    Customer customer = customerRepository.findByUser_Id(login.getId());
                    HeaderProfile profile = new HeaderProfile(customer.getId(), customer.getFullName(),
                            customer.getAvatarUrl(), customer.getEmail());
                    login.setFcmToken(fcmToken);
                    userRespository.save(login);
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
    @ApiOperation(value = "Api đăng nhập cho admin", response = Iterable.class)
    @PostMapping("/admin/login")
    public Response AdminLogin(@ApiParam(name = "encodedString", value = "username+\":\"+password, lấy kết quả encode theo Base64, sau đó thêm \"Basic \" + kết quả")
                               @RequestHeader(HeaderConstant.AUTHORIZATION) String encodedString) {
        Response response;
        try {
            User user = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (!userRespository.isAccountActivated(user.getUsername(), RoleConstants.ADMIN)) {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_VERIFIED);
            } else {
                User login = userRespository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
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



    @ApiOperation(value = "Đăng ký tài khoản khach hang", response = Iterable.class)
    @PostMapping("/customers/register")
    public Response register(@ApiParam(name = HeaderConstant.AUTHORIZATION,
            value = "username+\":\"+password, lấy kết quả encode theo Base64, sau đó thêm \"Basic \" + kết quả", required = true)
                             @RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString, @ApiParam(name = "customerRegisterBody", value = "Tên đầy đủ KH", required = true)
                             @Valid @RequestBody CustomerRegisterBody customerRegisterBody) {
        Response response;
        try {
            User u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (!PasswordValidate.isPasswordValidate(u.getPassword())) {
                return new ForbiddenResponse(ResponseConstant.ErrorMessage.PASSWORD_TOO_SHORT);
            }

            User user = userRespository.findByUsername(u.getUsername());
            if (user != null) {
                return new ResourceExistResponse("Tai khoan da ton tai!");
            } else {
                user = new User();
                user.setPassword(u.getPassword());
                user.setUsername(u.getUsername());
                user.setRole(RoleConstants.CUSTOMER);
                user.setActived(true);

                userRespository.save(user);

                Customer customer = new Customer();
                customer.setUser(user);
                customer.setFullName(customerRegisterBody.getFullName());
                customer.setAddress(customerRegisterBody.getDateOfBirth());
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

    @ApiOperation(value = "Đăng ký tài khoản admin", response = Iterable.class)
    @PostMapping("/admins/register")
    public Response adminRegister(@ApiParam(name = HeaderConstant.AUTHORIZATION, value = "username+\":\"+password, lấy kết quả encode theo Base64, sau đó thêm \"Basic \" + kết quả", required = true)
                                  @RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString, @ApiParam(name = "adminRegisterBody", value = "Tên đầy đủ KH", required = true)
                                  @Valid @RequestBody AdminRegisterBody adminRegisterBody) {
        Response response;
        try {
            User u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (!PasswordValidate.isPasswordValidate(u.getPassword())) {
                return new ForbiddenResponse(ResponseConstant.ErrorMessage.PASSWORD_TOO_SHORT);
            }

            User user = userRespository.findByUsername(u.getUsername());
            if (user != null) {
                return new ResourceExistResponse("Tai khoan da ton tai!");
            } else {
                user = new User();
                user.setPassword(u.getPassword());
                user.setUsername(u.getUsername());
                user.setRole(RoleConstants.ADMIN);
                user.setActived(true);

                userRespository.save(user);

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

    @ApiOperation(value = "Xac nhan email", response = Iterable.class)
    @GetMapping("/registration/confirm/{username}")
    public Response confirm_email(@PathVariable("username") String username) {
        Response response;
        try {
            username += ".com";
            if (userRespository.findByUsername(username) == null) {
                return new NotFoundResponse("Email khong ton tai !");
            }
            userRespository.activeAccount(true, username);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Xac nhan email", response = Iterable.class)
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
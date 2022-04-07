package mon7.project.bookstore.customer.controller;

import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.customer.dao.FeedbackRepository;
import mon7.project.bookstore.customer.dao.RateRepository;
import mon7.project.bookstore.customer.models.body.ProfileBody;
import mon7.project.bookstore.customer.models.data.Customer;
import mon7.project.bookstore.customer.models.view.HeaderProfile;
import mon7.project.bookstore.customer.models.view.Profile;
import mon7.project.bookstore.response_model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@Api(value = "candidate-api", description = "Nhóm API Customer, Yêu cầu access token của Khách hàng")
@CrossOrigin(origins = "*")
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    RateRepository rateRepository;

    @ApiOperation(value = "Lấy Lấy avatar + email + tên Khách hàng", response = Iterable.class)
    @GetMapping("/headerProfiles/{id}")
    Response getHeaderProfile(@PathVariable("id") String customerID) {
        Response response;
        try {
            HeaderProfile headerProfile = customerRepository.getHeaderProfile(customerID);
            response = new OkResponse(headerProfile);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/profiles/{id}")
    Response getProfile(@PathVariable("id") String customerID) {
        Response response;
        try {
            Profile profile = customerRepository.getProfile(customerID);
            response = new OkResponse(profile);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }
}

package mon7.project.bookstore.customer.controller;

import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.customer.dao.FeedbackRepository;
import mon7.project.bookstore.customer.dao.RateRepository;
import mon7.project.bookstore.customer.models.view.HeaderProfile;
import mon7.project.bookstore.customer.models.view.Profile;
import mon7.project.bookstore.response_model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    RateRepository rateRepository;

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

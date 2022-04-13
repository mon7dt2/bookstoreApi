package mon7.project.bookstore.admin.controller;

import mon7.project.bookstore.admin.dao.ApplicationVersionRepository;
import mon7.project.bookstore.auth.dao.AccountRespository;
import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.customer.models.view.CustomerView;
import mon7.project.bookstore.response_model.OkResponse;
import mon7.project.bookstore.response_model.Response;
import mon7.project.bookstore.response_model.ServerErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "*")
public class AdminController implements ErrorController {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    AccountRespository accountRespository;
    @Autowired
    ApplicationVersionRepository applicationVersionRepository;
    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("/version")
    Response getVersionApp(){
        Response response;
        try {
            int version = applicationVersionRepository.findAll().get(0).getVersion();
            response = new OkResponse(version);
        }catch (Exception e){
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/customer")
    Response getAllCustomer(){
        Response response;
        try {
            List<CustomerView> customers = customerRepository.getAllCustomers();
            response = new OkResponse(customers);
        } catch (Exception e) {
            response = new ServerErrorResponse();
        }
        return response;
    }
}

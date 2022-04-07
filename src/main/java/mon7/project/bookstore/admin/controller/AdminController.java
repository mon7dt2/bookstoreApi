package mon7.project.bookstore.admin.controller;

import mon7.project.bookstore.admin.dao.ApplicationVersionRepository;
import mon7.project.bookstore.auth.dao.UserRespository;
import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.response_model.OkResponse;
import mon7.project.bookstore.response_model.Response;
import mon7.project.bookstore.response_model.ServerErrorResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
@RequestMapping("/api/admins")
@Api(value = "Admin-api", description = "Nhóm API Admin, Yêu cầu access token của Admin")
@CrossOrigin(origins = "*")
public class AdminController {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserRespository userRespository;
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
}

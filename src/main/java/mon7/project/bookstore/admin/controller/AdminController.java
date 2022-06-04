package mon7.project.bookstore.admin.controller;

import mon7.project.bookstore.admin.dao.ApplicationVersionRepository;
import mon7.project.bookstore.auth.dao.AccountRespository;
import mon7.project.bookstore.auth.models.Account;
import mon7.project.bookstore.category.dao.CategoryRepository;
import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.customer.models.view.CustomerView;
import mon7.project.bookstore.product.dao.BooksImageRepository;
import mon7.project.bookstore.product.dao.BooksRepository;
import mon7.project.bookstore.product.dao.CommentRepository;
import mon7.project.bookstore.provider.dao.ProviderRepository;
import mon7.project.bookstore.response_model.*;
import mon7.project.bookstore.staff.dao.StaffRepository;
import mon7.project.bookstore.utils.UserDecodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    AccountRespository accountRespository;
    @Autowired
    ApplicationVersionRepository applicationVersionRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProviderRepository providerRepository;
    @Autowired
    BooksRepository booksRepository;
    @Autowired
    BooksImageRepository booksImageRepository;
    @Autowired
    CommentRepository commentRepository;

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

//    @GetMapping("/total/category")
//    Response getTotalCategory(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString){
//        Response response;
//        try {
//            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
//            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
//                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
//
//            } else {
//                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
//            }
//        }catch (Exception e) {
//            response = new ServerErrorResponse();
//        }
//        return response;
//
//    }
}

package mon7.project.bookstore.order.controller;

import mon7.project.bookstore.admin.dao.ApplicationVersionRepository;
import mon7.project.bookstore.auth.dao.AccountRespository;
import mon7.project.bookstore.auth.models.Account;
import mon7.project.bookstore.category.dao.CategoryRepository;
import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.customer.models.data.Customer;
import mon7.project.bookstore.order.dao.OrderDetailRepository;
import mon7.project.bookstore.order.dao.OrderRepository;
import mon7.project.bookstore.order.model.body.CreateOrderBody;
import mon7.project.bookstore.order.model.data.Order;
import mon7.project.bookstore.order.model.data.OrderDetail;
import mon7.project.bookstore.order.model.data.OrderedProductItem;
import mon7.project.bookstore.product.dao.BooksImageRepository;
import mon7.project.bookstore.product.dao.BooksRepository;
import mon7.project.bookstore.product.model.data.Books;
import mon7.project.bookstore.provider.dao.ProviderRepository;
import mon7.project.bookstore.response_model.*;
import mon7.project.bookstore.staff.dao.StaffRepository;
import mon7.project.bookstore.utils.UserDecodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class OrderController {
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
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @PostMapping("/order")
    public Response createOrderByCustomer(
            @RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
            @RequestBody CreateOrderBody body
    ){
        Response response;
        try{
            Account u = accountRespository.findByUsername(UserDecodeUtils.decodeFromAuthorizationHeader(encodedString).getUsername());
            if(u.getRole().equals(RoleConstants.CUSTOMER)){
                Customer customer = customerRepository.findByAccount_Id(u.getId());
                if(customer != null){
                    Order order = new Order(customer);
                    order.setTotalPrice(0);
                    order.setOrderStatus(0);
                    orderRepository.save(order);
                    order.setSearchKey("dlbs-" + order.getId().substring(0,7));
                    order.setAddress(body.getAddress());
                    orderRepository.save(order);

                    for(OrderedProductItem item: body.getListProduct()){
                        if(item.getQty() > 1) {
                            Books book = booksRepository.findById(item.getProductID()).get();
                            if(book.getQuantity() > item.getQty()) {
                                OrderDetail detail = new OrderDetail(book, item.getQty());
                                detail.setOrder(order);
                                detail.setTotal((Double) (detail.getBook().getPrice() * detail.getQuantity()));
                                orderDetailRepository.save(detail);
                                order.setTotalPrice(order.getTotalPrice() + detail.getTotal());
                                orderRepository.save(order);
                                book.setQuantity(book.getQuantity() - detail.getQuantity());
                                booksRepository.save(book);
                            } else {
                                return new ForbiddenResponse(ResponseConstant.ErrorMessage.ITEM_NOT_ENOUGH);
                            }
                        }
                    }
                    response = new OkResponse(order.getId());
                } else {
                    response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
                }
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (NoSuchElementException | EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/orders")
    Response getAllOrders(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                List<Order> list = orderRepository.findAll();
                response = new OkResponse(list);
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        }  catch (NoSuchElementException | EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/order/{orderID}")
    Response getOrderById(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                          @PathVariable("orderID") String orderID){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                List<OrderDetail> list = orderDetailRepository.findByOrder_Id(orderID);
                response = new OkResponse(list);
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        }  catch (NoSuchElementException | EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }
    @PutMapping("/order/{orderID}")
    Response updateOrder(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                         @PathVariable("orderID") String orderID,
                         @RequestParam(value = "state", required = true) int state){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Order o = orderRepository.findById(orderID).get();
                o.setOrderStatus(state);
                orderRepository.save(o);
                response = new OkResponse(o.getId());
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (NoSuchElementException | EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }
    @GetMapping("/order/state/{state}")
    Response getOrderByState(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                         @PathVariable("state") int state){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                List<Order> list = orderRepository.findByOrderStatus(state);
                response = new OkResponse(list);
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (NoSuchElementException | EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/orders/users")
    Response getOrderByUser(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(!accountRespository.findByUsername(u.getUsername()).getRole().equals("")){
                Customer c = customerRepository.findByAccount_Id(u.getId());
                if(c != null){
                    List<Order> list = orderRepository.findByCustomer_Id(c.getId());
                    response = new OkResponse(list);
                }else {
                    response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
                }
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (NoSuchElementException | EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }
}

package mon7.project.bookstore.customer.controller;

import io.swagger.v3.oas.annotations.Parameter;
import mon7.project.bookstore.auth.dao.AccountRespository;
import mon7.project.bookstore.auth.models.Account;
import mon7.project.bookstore.constants.Constant;
import mon7.project.bookstore.customer.dao.AddressRepository;
import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.customer.dao.FeedbackRepository;
import mon7.project.bookstore.customer.dao.RateRepository;
import mon7.project.bookstore.customer.models.body.AddressBody;
import mon7.project.bookstore.customer.models.data.Address;
import mon7.project.bookstore.customer.models.data.Customer;
import mon7.project.bookstore.customer.models.view.AddressView;
import mon7.project.bookstore.customer.models.view.HeaderProfile;
import mon7.project.bookstore.customer.models.view.Profile;
import mon7.project.bookstore.provider.model.Provider;
import mon7.project.bookstore.response_model.*;
import mon7.project.bookstore.utils.PageAndSortRequestBuilder;
import mon7.project.bookstore.utils.UserDecodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    AccountRespository accountRespository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    RateRepository rateRepository;
    @Autowired
    AddressRepository addressRepository;

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
//*********************************************************************************************************************************************************************
//* Begin::Address Module
//*********************************************************************************************************************************************************************

    @GetMapping("/address")
    public Response getAllAddress(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                  @RequestParam(value = "customerID") String customerID,
                                  @Parameter(name = "pageIndex", description = "Index trang, mặc định là 0")
                                  @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                  @Parameter(name = "pageSize", description = "Kích thước trang, mặc đinh và tối đa là " + Constant.MAX_PAGE_SIZE)
                                  @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                  @Parameter(name = "sortBy", description = "Trường cần sort, mặc định là " + Address.NAME)
                                  @RequestParam(value = "sortBy", defaultValue = Address.NAME) String sortBy,
                                  @Parameter(name = "sortType", description = "Nhận (asc | desc), mặc định là desc")
                                  @RequestParam(value = "sortType", defaultValue = "desc") String sortType){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()) != null){
                Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PROVIDER_PAGE_SIZE);
                Page<Address> addresses = addressRepository.getAllAddressByCustomer(customerID, pageable);
                if(addresses.getTotalElements() > 0){
                    response = new OkResponse(addresses);
                } else {
                    response = new OkResponse(ResponseConstant.MSG_OK);
                }
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/address/{addressID}")
    public Response getAddress(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                               @RequestParam(value = "customerID") String customerID,
                               @PathVariable("addressID") String addressID){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()) != null){
                Customer customer = customerRepository.getById(customerID);
                Address address = addressRepository.getById(addressID);
                AddressView view = new AddressView(address.getId(), address.getAddress(),address.getIsDefault());
                response = new OkResponse(view);
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @PostMapping("/address")
    public Response addNewAddress(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                  @RequestParam(value = "customerID") String customerID,
                                  @RequestBody AddressBody body){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()) != null){
                Customer customer = customerRepository.getById(customerID);
                if(body.getIsDefault() == 1){
                    addressRepository.setAllAddressToUnDefault(customerID);
                }
                Address address = new Address(body.getAddress(), body.getIsDefault());
                address.setCustomer(customer);
                addressRepository.save(address);
                response = new OkResponse(address.getId());
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getClass() == EntityNotFoundException.class){
                response = new NotFoundResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_FOUND);
            } else {
                response = new ServerErrorResponse();
            }
        }
        return response;
    }

    @PutMapping("/address/{addressID}")
    public Response updateAddress(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                  @RequestParam(value = "customerID") String customerID,
                                  @PathVariable("addressID") String addressID,
                                  @RequestBody AddressBody body){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()) != null){
                Customer customer = customerRepository.getById(customerID);
                Address address = addressRepository.getById(addressID);
                if(body.getIsDefault() == 1){
                    addressRepository.setAllAddressToUnDefault(customerID);
                }
                address.setAddress(body.getAddress());
                address.setIsDefault(body.getIsDefault());
                addressRepository.save(address);
                response = new OkResponse(address.getId());
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getClass() == EntityNotFoundException.class){
                response = new NotFoundResponse(ResponseConstant.ErrorMessage.ADDRESS_NOT_FOUND);
            } else {
                response = new ServerErrorResponse();
            }
        }
        return response;
    }

    @DeleteMapping("/address/{addressID}")
    public Response DeleteMapping(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                  @RequestParam(value = "customerID") String customerID,
                                  @PathVariable("addressID") String addressID){
        Response response;
        try{
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()) != null){
                Customer customer = customerRepository.getById(customerID);
                Address address = addressRepository.getById(addressID);
                address.setIsDeleted(1);
                addressRepository.save(address);
                response = new OkResponse(address.getId());
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        }catch (Exception e){
            e.printStackTrace();
            if(e.getClass() == EntityNotFoundException.class){
                response = new NotFoundResponse(ResponseConstant.ErrorMessage.ADDRESS_NOT_FOUND);
            } else {
                response = new ServerErrorResponse();
            }
        }
        return response;
    }
}
//*********************************************************************************************************************************************************************
//* Begin::Address Module
//*********************************************************************************************************************************************************************

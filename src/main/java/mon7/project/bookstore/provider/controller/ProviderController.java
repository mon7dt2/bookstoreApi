package mon7.project.bookstore.provider.controller;

import io.swagger.v3.oas.annotations.Parameter;
import mon7.project.bookstore.admin.dao.AdminRepository;
import mon7.project.bookstore.auth.dao.AccountRespository;
import mon7.project.bookstore.auth.models.Account;
import mon7.project.bookstore.constants.Constant;
import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.provider.dao.ProviderRepository;
import mon7.project.bookstore.provider.model.Provider;
import mon7.project.bookstore.provider.model.body.ProviderBody;
import mon7.project.bookstore.response_model.*;
import mon7.project.bookstore.staff.dao.StaffRepository;
import mon7.project.bookstore.utils.PageAndSortRequestBuilder;
import mon7.project.bookstore.utils.UserDecodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api")
public class ProviderController {

    @Autowired
    AccountRespository accountRespository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    ProviderRepository providerRepository;

    @GetMapping("/provider")
    public Response getProviders(
            @RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
            @Parameter(name = "pageIndex", description = "Index trang, mặc định là 0")
            @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
            @Parameter(name = "pageSize", description = "Kích thước trang, mặc đinh và tối đa là " + Constant.MAX_PROVIDER_PAGE_SIZE)
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "sortBy", description = "Trường cần sort, mặc định là " + Provider.NAME)
            @RequestParam(value = "sortBy", defaultValue = Provider.NAME) String sortBy,
            @Parameter(name = "sortType", description = "Nhận (asc | desc), mặc định là desc")
            @RequestParam(value = "sortType", defaultValue = "desc") String sortType
    ){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PROVIDER_PAGE_SIZE);
                Page<Provider> page = providerRepository.getAllProvider(pageable);
                response = new OkResponse(page);
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/provider/{providerID}")
    public Response getProvider(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                @PathVariable("providerID") Long providerID){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Provider provider = providerRepository.getById(providerID);
                if(provider != null){
                    if(provider.getIsDeleted() == 0){
                        Provider view = new Provider(provider.getId(),provider.getDisplayName(), provider.getDescription(),
                                            provider.getAddress(), provider.getPhone(), provider.getEmail());
                        response = new OkResponse(view);
                    } else {
                        response = new NotFoundResponse(ResponseConstant.ErrorMessage.CATEGORY_NOT_FOUND);
                    }
                } else {
                    response = new NotFoundResponse(ResponseConstant.ErrorMessage.PROVIDER_NOT_FOUND);
                }
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (Exception e){
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @PostMapping("/provider")
    public Response addProvider(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                @RequestBody ProviderBody body){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Provider existProvider = providerRepository.findByDisplayName(body.getDisplayName());
                if(existProvider == null){
                    Provider provider = new Provider(body);
                    providerRepository.save(provider);
                    response = new OkResponse(provider);
                } else {
                    response = new ResourceExistResponse(ResponseConstant.ErrorMessage.RESOURCE_PROVIDER_EXIST);
                }
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (Exception e){
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @PutMapping("/provider/{providerID}")
    public Response updateProvider(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                   @PathVariable("providerID") Long providerID,
                                   @RequestBody ProviderBody body){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Provider existProvider = providerRepository.getById(providerID);
                if(body.getDisplayName() != null && !body.getDisplayName().equals("")) existProvider.setDisplayName(body.getDisplayName());
                if(body.getDescription() != null && !body.getDescription().equals("")) existProvider.setDescription(body.getDescription());
                if(body.getAddress() != null && !body.getAddress().equals("")) existProvider.setAddress(body.getAddress());
                if(body.getPhone() != null && !body.getPhone().equals("")) existProvider.setPhone(body.getPhone());
                if(body.getEmail() != null && !body.getEmail().equals("")) existProvider.setEmail(body.getEmail());
                providerRepository.save(existProvider);
                response = new OkResponse(providerID);
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (Exception e){
            e.printStackTrace();
            if(e.getClass() == EntityNotFoundException.class){
                response = new NotFoundResponse(ResponseConstant.ErrorMessage.PROVIDER_NOT_FOUND);
            } else {
                response = new ServerErrorResponse();
            }
        }
        return response;
    }

    @DeleteMapping("/provider/{providerID}")
    public Response deleteProvider(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                   @PathVariable("providerID") Long providerID){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Provider existProvider = providerRepository.getById(providerID);
                existProvider.setIsDeleted(1);
                providerRepository.save(existProvider);
                response = new OkResponse(providerID);
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (Exception e){
            e.printStackTrace();
            if(e.getClass() == EntityNotFoundException.class){
                response = new NotFoundResponse(ResponseConstant.ErrorMessage.PROVIDER_NOT_FOUND);
            } else {
                response = new ServerErrorResponse();
            }
        }
        return response;
    }
}

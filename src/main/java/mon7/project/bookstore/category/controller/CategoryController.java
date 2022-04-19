package mon7.project.bookstore.category.controller;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageException;
import com.google.firebase.cloud.StorageClient;
import io.swagger.v3.oas.annotations.Parameter;
import mon7.project.bookstore.admin.dao.ApplicationVersionRepository;
import mon7.project.bookstore.auth.dao.AccountRespository;
import mon7.project.bookstore.auth.models.Account;
import mon7.project.bookstore.category.dao.CategoryRepository;
import mon7.project.bookstore.category.model.Category;
import mon7.project.bookstore.category.model.view.CategoryPreview;
import mon7.project.bookstore.constants.Constant;
import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.response_model.*;
import mon7.project.bookstore.staff.dao.StaffRepository;
import mon7.project.bookstore.utils.PageAndSortRequestBuilder;
import mon7.project.bookstore.utils.UserDecodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CategoryController {

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

    @GetMapping("/category")
    public Response getCategories(
            @Parameter(name = "pageIndex", description = "Index trang, mặc định là 0")
            @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
            @Parameter(name = "pageSize", description = "Kích thước trang, mặc đinh và tối đa là " + Constant.MAX_CATEGORY_PAGE_SIZE)
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "sortBy", description = "Trường cần sort, mặc định là " + Category.NAME)
            @RequestParam(value = "sortBy", defaultValue = Category.NAME) String sortBy,
            @Parameter(name = "sortType", description = "Nhận (asc | desc), mặc định là desc")
            @RequestParam(value = "sortType", defaultValue = "desc") String sortType
    ){
        Response response;

        try {
            Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_CATEGORY_PAGE_SIZE);
            Page<Category> categoryPreview = categoryRepository.getAllCategories(pageable);
            response = new OkResponse(categoryPreview);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/category/{categoryID}")
    public Response getCategory(@PathVariable("categoryID") Long categoryID){
        Response response;

        try {
            Category category = categoryRepository.getById(categoryID);
            if(category == null){
                response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
            } else {
                if(category.getIsDeleted() == 0){
                    CategoryPreview categoryPreview = new CategoryPreview(category);
                    response = new OkResponse(categoryPreview);
                } else {
                    response = new NotFoundResponse(ResponseConstant.ErrorMessage.CATEGORY_NOT_FOUND);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @PostMapping("/category")
    public Response addCategory(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                @RequestParam(value = "displayName",required = true) String displayName,
                                @RequestParam(value = "cover",required = true) MultipartFile cover){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                if(categoryRepository.findByDisplayName(displayName) == null) {
                    Category category = new Category(displayName);
                    category.setIsDeleted(0);
                    categoryRepository.save(category);
                    if (cover != null) {
                        String coverUrl = uploadFile("category/" + category.getId(), category.getId() + "_cover.jpg",
                                cover.getBytes(), "image/jpeg");
                        category.setCoverUrl(coverUrl);
                        categoryRepository.save(category);
                    }
                    response = new OkResponse(category);
                } else {
                    response = new ResourceExistResponse(ResponseConstant.ErrorMessage.RESOURCE_CATEGORY_EXIST);
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

    @PutMapping("/category/{categoryID}")
    public Response updateCategory(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                   @PathVariable("categoryID") Long categoryID,
                                   @RequestParam(value = "displayName") String displayName,
                                   @RequestParam(value = "cover") MultipartFile cover){
        Response response;
        try{
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Category category = categoryRepository.getById(categoryID);
                if(category != null){
                    if(category.getDisplayName().equals(displayName)){
                        response = new ResourceExistResponse(ResponseConstant.ErrorMessage.RESOURCE_CATEGORY_EXIST);
                    } else {
                        category.setDisplayName(displayName);
                        if (cover != null) {
                            String coverUrl = uploadFile("category/" + category.getId(), category.getId() + "_cover.jpg",
                                    cover.getBytes(), "image/jpeg");
                            category.setCoverUrl(coverUrl);
                        }
                        categoryRepository.save(category);
                        response = new OkResponse(category.getId());
                    }
                } else {
                    response = new NotFoundResponse(ResponseConstant.ErrorMessage.CATEGORY_NOT_FOUND);
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

    @DeleteMapping("/category/{categoryID}")
    public Response deleteCategory(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                   @PathVariable("categoryID") Long categoryID){
        Response response;
        try{
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Category category = categoryRepository.getById(categoryID);
                if(category != null){
                    category.setIsDeleted(1);
                    categoryRepository.save(category);
                    response = new OkResponse("OK");
                } else {
                    response = new NotFoundResponse(ResponseConstant.ErrorMessage.CATEGORY_NOT_FOUND);
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

    public static String uploadFile(String dir, String fileName,
                                    byte[] data,
                                    String contentType) throws StorageException {
        Blob avatarFile = StorageClient.getInstance()
                .bucket()
                .create(dir+"/"+fileName, data, contentType);
        return getDownloadUrl(avatarFile.getBucket(), avatarFile.getName());
    }

    public static String getDownloadUrl(String bucketUrl, String fileName) {
        return "http://storage.googleapis.com/" + bucketUrl + "/" + fileName;
    }
}

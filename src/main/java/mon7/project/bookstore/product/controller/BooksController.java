package mon7.project.bookstore.product.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import mon7.project.bookstore.admin.dao.ApplicationVersionRepository;
import mon7.project.bookstore.auth.dao.AccountRespository;
import mon7.project.bookstore.auth.models.Account;
import mon7.project.bookstore.category.dao.CategoryRepository;
import mon7.project.bookstore.category.model.Category;
import mon7.project.bookstore.constants.Constant;
import mon7.project.bookstore.customer.dao.CustomerRepository;
import mon7.project.bookstore.product.dao.BooksImageRepository;
import mon7.project.bookstore.product.dao.BooksRepository;
import mon7.project.bookstore.product.model.data.Books;
import mon7.project.bookstore.product.model.data.BooksImage;
import mon7.project.bookstore.product.model.view.BookPreview;
import mon7.project.bookstore.provider.dao.ProviderRepository;
import mon7.project.bookstore.provider.model.Provider;
import mon7.project.bookstore.response_model.*;
import mon7.project.bookstore.staff.dao.StaffRepository;
import mon7.project.bookstore.utils.PageAndSortRequestBuilder;
import mon7.project.bookstore.utils.UploadUtils;
import mon7.project.bookstore.utils.UserDecodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BooksController {

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

    @PostMapping("/product")
    public Response addProduct(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                               @RequestParam(value = "categoryID") Long categoryID,
                               @RequestParam(value = "providerID") Long providerID,
                               @RequestParam(value = "displayName") String displayName,
                               @RequestParam(value = "description") String description,
                               @RequestParam(value = "price") Double price,
                               @RequestParam(value = "quantity") Long quantity,
                               @RequestParam(value = "author") String author,
                               @RequestParam(value = "publisher") String publisher,
                               @RequestParam(value = "avatar") MultipartFile avatar){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Category category = categoryRepository.getById(categoryID);
                Provider provider = providerRepository.getById(providerID);
                if(avatar != null){
                    Books books = new Books(displayName, description, price, quantity, author, publisher);
                    books.setCategory(category);
                    books.setProvider(provider);
                    books.setIsDeleted(0);
                    booksRepository.save(books);
                    String avatarUrl = UploadUtils.uploadFile("product/" + books.getId(), books.getId() + "_avatar.jpg",
                            avatar.getBytes(), "image/jpeg");
                    books.setAvatarUrl(avatarUrl);
                    booksRepository.save(books);
                    response = new OkResponse(books.getId());
                } else {
                    response = new ServerErrorResponse();
                }
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @PostMapping("/product/images")
    public Response uploadImage(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                @RequestParam(value = "bookID") String bookID,
                                @RequestParam(value = "images") MultipartFile[] images){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Books books = booksRepository.getById(bookID);
                List<String> arrName = new ArrayList<>();

                Arrays.asList(images).stream().forEach(file ->{
                    try {
                        BooksImage bookImage = new BooksImage();
                        bookImage.setBooks(books);
                        bookImage.setIsDeleted(0);
                        booksImageRepository.save(bookImage);
                        String url = UploadUtils.uploadFile("product/" + books.getId(), bookImage.getId() + ".jpg",
                                file.getBytes(), "image/jpeg");
                        bookImage.setUrl(url);
                        booksImageRepository.save(bookImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                response = new OkResponse(ResponseConstant.MSG_OK);
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @PatchMapping("/product/{bookID}")
    public Response updateInfoProduct(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                      @PathVariable("bookID") String bookID,
                                      @RequestParam(value = "categoryID", required = false) Long categoryID,
                                      @RequestParam(value = "providerID", required = false) Long providerID,
                                      @RequestParam(value = "displayName", required = false) String displayName,
                                      @RequestParam(value = "description", required = false) String description,
                                      @RequestParam(value = "price", required = false) Double price,
                                      @RequestParam(value = "quantity", required = false) Long quantity,
                                      @RequestParam(value = "author", required = false) String author,
                                      @RequestParam(value = "publisher", required = false) String publisher,
                                      @RequestParam(value = "avatar", required = false) MultipartFile avatar){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Books book = booksRepository.getById(bookID);
                if(categoryID != null){
                    Category category = categoryRepository.getById(categoryID);
                    book.setCategory(category);
                }
                if(providerID != null){
                    Provider provider = providerRepository.getById(providerID);
                    book.setProvider(provider);
                }
                if(displayName != null){
                    book.setDisplayName(displayName);
                }
                if(description != null){
                    book.setDescription(description);
                }
                if(price != null){
                    book.setPrice(price);
                }
                if(quantity != null){
                    book.setQuantity(quantity);
                }
                if(author != null){
                    book.setAuthor(author);
                }
                if(publisher != null){
                    book.setPublisher(publisher);
                }
                if (avatar != null){
                    String avatarUrl = UploadUtils.uploadFile("product/" + book.getId(), book.getId() + "_avatar.jpg",
                            avatar.getBytes(), "image/jpeg");
                    book.setAvatarUrl(avatarUrl);
                }
                booksRepository.save(book);
                response = new OkResponse(book.getId());
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @DeleteMapping("/product/{bookID}")
    public Response deleteProduct(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                  @PathVariable("bookID") String bookID){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Books book = booksRepository.getById(bookID);
                book.setIsDeleted(1);
                booksRepository.save(book);
                response = new OkResponse(book.getId());
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @DeleteMapping("/product/{bookID}/images")
    public Response deleteImagesProduct(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                        @PathVariable("bookID") String bookID,
                                        @RequestParam(value = "imageIDs") String[] imageIds){
        Response response;
        try {
            Account u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.STAFF) ||
                    accountRespository.findByUsername(u.getUsername()).getRole().equals(RoleConstants.ADMIN)){
                Books book = booksRepository.getById(bookID);
                Arrays.asList(imageIds).stream().forEach(imageID -> {
                    BooksImage image = booksImageRepository.getById(imageID);
                    image.setIsDeleted(1);
                    booksImageRepository.save(image);
                });
                response = new OkResponse(ResponseConstant.MSG_OK);
            } else {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
            }
        } catch (EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/product")
    public Response getProductsPreview(
            @Parameter(name = "pageIndex", description = "Index trang, mặc định là 0")
            @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
            @Parameter(name = "pageSize", description = "Kích thước trang, mặc đinh và tối đa là " + Constant.MAX_BOOK_PAGE_SIZE)
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "sortBy", description = "Trường cần sort, mặc định là " + Books.CREATED_AT)
            @RequestParam(value = "sortBy", defaultValue = Books.CREATED_AT) String sortBy,
            @Parameter(name = "sortType", description = "Nhận (asc | desc), mặc định là desc")
            @RequestParam(value = "sortType", defaultValue = "desc") String sortType){
        Response response;
        try {
            Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_BOOK_PAGE_SIZE);
            Page<BookPreview> preview = booksRepository.getBookPreview(pageable);
            response = new OkResponse(preview);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/product/{bookID}")
    public Response getProductDetail(@PathVariable("bookID") String bookID){
        Response response;
        try {
            Books book = booksRepository.findById(bookID).get();
            response = new OkResponse(book);
        } catch (EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/products/{categoryID}")
    public Response getProductByCategory(@PathVariable("categoryID") Long categoryID,
                                         @Parameter(name = "pageIndex", description = "Index trang, mặc định là 0")
                                         @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                         @Parameter(name = "pageSize", description = "Kích thước trang, mặc đinh và tối đa là " + Constant.MAX_BOOK_PAGE_SIZE)
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                         @Parameter(name = "sortBy", description = "Trường cần sort, mặc định là " + Books.CREATED_AT)
                                         @RequestParam(value = "sortBy", defaultValue = Books.CREATED_AT) String sortBy,
                                         @Parameter(name = "sortType", description = "Nhận (asc | desc), mặc định là desc")
                                         @RequestParam(value = "sortType", defaultValue = "desc") String sortType){
        Response response;
        try {
            Category category = categoryRepository.getById(categoryID);
            Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_BOOK_PAGE_SIZE);
            Page<BookPreview> preview = booksRepository.getBookPreviewByCategory(pageable, category);
            response = new OkResponse(preview);
        } catch (EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }
}

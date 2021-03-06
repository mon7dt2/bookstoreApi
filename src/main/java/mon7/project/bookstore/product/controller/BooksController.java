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
import mon7.project.bookstore.customer.models.data.Customer;
import mon7.project.bookstore.product.dao.BooksImageRepository;
import mon7.project.bookstore.product.dao.BooksRepository;
import mon7.project.bookstore.product.dao.CommentRepository;
import mon7.project.bookstore.product.model.data.Books;
import mon7.project.bookstore.product.model.data.BooksImage;
import mon7.project.bookstore.product.model.data.Comment;
import mon7.project.bookstore.product.model.view.Book;
import mon7.project.bookstore.product.model.view.BookPreview;
import mon7.project.bookstore.product.model.view.CommentView;
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
import java.util.*;

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
    @Autowired
    CommentRepository commentRepository;

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
            @Parameter(name = "pageIndex", description = "Index trang, m???c ?????nh l?? 0")
            @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
            @Parameter(name = "pageSize", description = "K??ch th?????c trang, m???c ??inh v?? t???i ??a l?? " + Constant.MAX_BOOK_PAGE_SIZE)
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "sortBy", description = "Tr?????ng c???n sort, m???c ?????nh l?? " + Books.CREATED_AT)
            @RequestParam(value = "sortBy", defaultValue = Books.CREATED_AT) String sortBy,
            @Parameter(name = "sortType", description = "Nh???n (asc | desc), m???c ?????nh l?? desc")
            @RequestParam(value = "sortType", defaultValue = "desc") String sortType){
        Response response;
        try {
            Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_BOOK_PAGE_SIZE);
            Page<Books> preview = booksRepository.getBookPreview(pageable);
            response = new OkResponse(preview);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/product/search")
    public Response searchProduct(
            @Parameter(name = "searchKey", description = "Tu khoa")
            @RequestParam(value = "searchKey") String searchKey){
        Response response;
        try {
            List<Books> preview = booksRepository.searchProduct(searchKey);
            response = new OkResponse(preview);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/products")
    public Response getProducts(){
        Response response;
        try {
            List<Books> preview = booksRepository.findAll();
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
            Books book = booksRepository.getById(bookID);
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

    @GetMapping("/products/category/{categoryID}")
    public Response getProductByCategory(@PathVariable("categoryID") Long categoryID,
                                         @Parameter(name = "pageIndex", description = "Index trang, m???c ?????nh l?? 0")
                                         @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                         @Parameter(name = "pageSize", description = "K??ch th?????c trang, m???c ??inh v?? t???i ??a l?? " + Constant.MAX_BOOK_PAGE_SIZE)
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                         @Parameter(name = "sortBy", description = "Tr?????ng c???n sort, m???c ?????nh l?? " + Books.CREATED_AT)
                                         @RequestParam(value = "sortBy", defaultValue = Books.CREATED_AT) String sortBy,
                                         @Parameter(name = "sortType", description = "Nh???n (asc | desc), m???c ?????nh l?? desc")
                                         @RequestParam(value = "sortType", defaultValue = "desc") String sortType){
        Response response;
        try {
            Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_BOOK_PAGE_SIZE);
            Page<Books> preview = booksRepository.getBookPreviewByCategory(pageable, categoryID);
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

//
// Comment module
//

    @PostMapping("/product/{bookID}/comment")
    public Response postComment(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                @PathVariable("bookID") String bookID,
                                @RequestParam("comment") String commentString){
        Response response;
        try{
            Account u = accountRespository.findByUsername(UserDecodeUtils.decodeFromAuthorizationHeader(encodedString).getUsername());
            Books book = booksRepository.findById(bookID).get();
            Customer customer = customerRepository.findByAccount_Id(u.getId());
            if(customer != null){
                if(commentString != null){
                    Comment comment = new Comment(commentString);
                    comment.setBook(book);
                    comment.setCustomer(customer);
                    comment.setIsDeleted(0);
                    commentRepository.save(comment);
                    response = new OkResponse(comment.getId());
                } else {
                    response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
                }
            } else {
                response = new NotFoundResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
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

    @PatchMapping("/product/{bookID}/comment/{commentID}")
    public Response editComment(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                @PathVariable("bookID") String bookID,
                                @PathVariable("commentID") Long commentID,
                                @RequestParam("comment") String commentString){
        Response response;
        try{
            Account u = accountRespository.findByUsername(UserDecodeUtils.decodeFromAuthorizationHeader(encodedString).getUsername());
            Books book = booksRepository.findById(bookID).get();
            if(u.getRole().equals(RoleConstants.ADMIN) || u.getRole().equals(RoleConstants.STAFF) || u.getRole().equals(RoleConstants.CUSTOMER)){
                if(commentString != null){
                    Comment comment = commentRepository.findById(commentID).get();
                    comment.setComment(commentString);
                    commentRepository.save(comment);
                    response = new OkResponse(comment.getId());
                }else {
                    response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
                }
            } else {
                response = new NotFoundResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
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

    @DeleteMapping("/product/{bookID}/comment/{commentID}")
    public Response deleteComment(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                  @PathVariable("bookID") String bookID,
                                  @PathVariable("commentID") Long commentID){
        Response response;
        try{
            Account u = accountRespository.findByUsername(UserDecodeUtils.decodeFromAuthorizationHeader(encodedString).getUsername());
            Books book = booksRepository.findById(bookID).get();
            if(u.getRole().equals(RoleConstants.ADMIN) || u.getRole().equals(RoleConstants.STAFF)){
                Comment comment = commentRepository.findById(commentID).get();
                comment.setIsDeleted(1);
                commentRepository.save(comment);
                response = new OkResponse(comment.getId());
            } else if (u.getRole().equals(RoleConstants.CUSTOMER)){
                Comment comment = commentRepository.findById(commentID).get();
                Customer customer = customerRepository.findByAccount_Id(u.getId());
                if(comment.getCustomer().getId().equals(customer.getId())){
                    comment.setIsDeleted(1);
                    commentRepository.save(comment);
                    response = new OkResponse(comment.getId());
                } else {
                    response = new NotFoundResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
                }
            }else {
                response = new NotFoundResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
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

    //for admin & staff
    @GetMapping("/comment")
    public Response getAllComments(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                   @Parameter(name = "pageIndex", description = "Index trang, m???c ?????nh l?? 0")
                                   @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                   @Parameter(name = "pageSize", description = "K??ch th?????c trang, m???c ??inh v?? t???i ??a l?? " + Constant.MAX_PAGE_SIZE)
                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                   @Parameter(name = "sortBy", description = "Tr?????ng c???n sort, m???c ?????nh l?? " + Comment.CREATED_AT)
                                   @RequestParam(value = "sortBy", defaultValue = Comment.CREATED_AT) String sortBy,
                                   @Parameter(name = "sortType", description = "Nh???n (asc | desc), m???c ?????nh l?? desc")
                                   @RequestParam(value = "sortType", defaultValue = "desc") String sortType){
        Response response;
        try {
            Account u = accountRespository.findByUsername(UserDecodeUtils.decodeFromAuthorizationHeader(encodedString).getUsername());
            if(u.getRole().equals(RoleConstants.ADMIN) || u.getRole().equals(RoleConstants.STAFF)){
                Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PAGE_SIZE);
                Page<CommentView> page = commentRepository.getAllComments(pageable);
                response = new OkResponse(page);
            } else {
                response = new NotFoundResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
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

    @GetMapping("/product/{bookID}/comment")
    public Response getCommentInProduct(@PathVariable("bookID") String bookID,
                                        @Parameter(name = "pageIndex", description = "Index trang, m???c ?????nh l?? 0")
                                        @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                        @Parameter(name = "pageSize", description = "K??ch th?????c trang, m???c ??inh v?? t???i ??a l?? " + Constant.MAX_PAGE_SIZE)
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                        @Parameter(name = "sortBy", description = "Tr?????ng c???n sort, m???c ?????nh l?? " + Comment.CREATED_AT)
                                        @RequestParam(value = "sortBy", defaultValue = Comment.CREATED_AT) String sortBy,
                                        @Parameter(name = "sortType", description = "Nh???n (asc | desc), m???c ?????nh l?? desc")
                                        @RequestParam(value = "sortType", defaultValue = "desc") String sortType){
        Response response;
        try {
                Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PAGE_SIZE);
                Page<CommentView> page = commentRepository.getAllComments(pageable);
                response = new OkResponse(page);
        } catch (NoSuchElementException | EntityNotFoundException ex){
            ex.printStackTrace();
            response = new NotFoundResponse(ResponseConstant.ErrorMessage.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    //vote
    @PostMapping("/product/{bookID}/vote")
    public Response voteProduct(@RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString,
                                @PathVariable("bookID") String bookID,
                                @RequestParam("vote") Float vote){
        Response response;
        try {
            Account u = accountRespository.findByUsername(UserDecodeUtils.decodeFromAuthorizationHeader(encodedString).getUsername());
            if(u.getRole().equals(RoleConstants.CUSTOMER)){
                Books book = booksRepository.findById(bookID).get();
                if(vote != null){
                    book.setVote(vote);
                    booksRepository.save(book);
                    response = new OkResponse(book.getId());
                } else {
                    response = new ServerErrorResponse(ResponseConstant.ErrorMessage.INVALID_INPUT);
                }
            } else {
                response = new NotFoundResponse(ResponseConstant.ErrorMessage.ACCOUNT_FORBIDDEN_ROLE);
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





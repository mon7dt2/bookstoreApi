package mon7.project.bookstore.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket postsApi() {
        ArrayList<ResponseMessage> globalResponseMessage = new ArrayList<>();
        globalResponseMessage.add(new ResponseMessageBuilder()
                .code(500)
                .message("Internal server error")
                .build());

        return new Docket(DocumentationType.SWAGGER_2)/*.configure(new DocumentationContextBuilder().)*/
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(" mon7.project.bookstore"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, globalResponseMessage);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Duc Luong BookStore API")
                .description("Duc Luong BookStore API reference")
                .termsOfServiceUrl("https://github.com/mon7dt2")
                .contact("tungvuquang1504@gmail.com").license("Mon7 License")
                .licenseUrl("https://github.com/mon7dt2").version("1.0")
                .build();
    }
}

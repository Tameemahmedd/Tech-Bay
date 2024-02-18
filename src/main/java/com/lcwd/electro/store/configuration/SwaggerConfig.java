package com.lcwd.electro.store.configuration;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@SecurityScheme(
        name="scheme1",type= SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(
        info = @Info(title = "Tech Bay Backend:APIs",description = "This is project created by Hydra",version = "1.0.0",
        contact = @Contact(name = "Hydra",email = "hydra@gmail.com",url = "https://www.google.com"),license =@License(name = "License")

        ),
        externalDocs = @ExternalDocumentation(description = "This is external docs url",url = "https://springshop.wiki.github.org/docs")
)
public class SwaggerConfig {
//    @Bean
//    public OpenAPI openAPI(){
//
//            String schemeName="bearerScheme";
//
//
//        return new OpenAPI()
//                .addSecurityItem(new SecurityRequirement().addList(schemeName))
//                .components(new Components()
//                        .addSecuritySchemes(schemeName,new io.swagger.v3.oas.models.security.SecurityScheme()
//                                .name(schemeName)
//                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
//                                .bearerFormat("JWT")
//                                .scheme("bearer")))
//                .info(new Info().title("Tech Bay Backend:APIs")
//                        .description("This is project created by Hydra")
//                        .version("1.0.0")
//                        .contact(new Contact().name("Hydra").email("hydra@gmail.com").url("https://www.google.com"))
//                        .license(new License().name("License"))
//                ).externalDocs(new ExternalDocumentation().description("This is external url").url("https://springshop.wiki.github.org/docs"))
//                ;
//    }


//@Bean
//    public Docket docket(){
//    Docket docket = new Docket(DocumentationType.SWAGGER_2);
//    docket.apiInfo(getApiInfo());
//    docket.securityContexts(Arrays.asList(getSecurityContext()));
//    docket.securitySchemes(Arrays.asList(getSchemes()));
//
//    ApiSelectorBuilder select = docket.select();
//    select.apis(RequestHandlerSelectors.any());
//    select.paths(PathSelectors.any());
//    Docket build = select.build();
//    return build;
//}
//    private SecurityContext getSecurityContext() {
//    SecurityContext context=SecurityContext.builder()
//            .securityReferences(getSecurityReferences())
//            .build();
//        return context;
//    }
//
//    private List<SecurityReference> getSecurityReferences() {
//    AuthorizationScope[] scopes={
//            new AuthorizationScope("Global","Access Everything")
//    };
//    return Arrays.asList(new SecurityReference("JWT", scopes));
//
//    }
//
//    private ApiKey getSchemes() {
//    return new ApiKey("JWT","Authorization","header");
//    }
//
//
//    private ApiInfo getApiInfo() {
//
//        ApiInfo apiInfo = new ApiInfo(
//                "Tech Bay Backend:APIs","This is project created by Hydra","1.0.0V",
//                "https://www.techbay.com",new Contact("Hydra","https://www.google.com","hydra@gmail.com"),
//                "License of APIs","https://www.techbay.com",
//                new ArrayList<>()
//        );
//        return apiInfo;
//    }
//

}

package com.lcwd.electro.store.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
@Bean
    public Docket docket(){
    Docket docket = new Docket(DocumentationType.SWAGGER_2);
    docket.apiInfo(getApiInfo());
    docket.securityContexts(Arrays.asList(getSecurityContext()));
    docket.securitySchemes(Arrays.asList(getSchemes()));

    ApiSelectorBuilder select = docket.select();
    select.apis(RequestHandlerSelectors.any());
    select.paths(PathSelectors.any());
    Docket build = select.build();
    return build;
}
    private SecurityContext getSecurityContext() {
    SecurityContext context=SecurityContext.builder()
            .securityReferences(getSecurityReferences())
            .build();
        return context;
    }

    private List<SecurityReference> getSecurityReferences() {
    AuthorizationScope[] scopes={
            new AuthorizationScope("Global","Access Everything")
    };
    return Arrays.asList(new SecurityReference("JWT", scopes));

    }

    private ApiKey getSchemes() {
    return new ApiKey("JWT","Authorization","header");
    }


    private ApiInfo getApiInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "Tech Bay Backend:APIs","This is project created by Hydra","1.0.0V",
                "https://www.techbay.com",new Contact("Hydra","https://www.google.com","hydra@gmail.com"),
                "License of APIs","https://www.techbay.com",
                new ArrayList<>()
        );
        return apiInfo;
    }


}

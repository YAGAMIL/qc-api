package com.quantumtime.qc.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Configuration
//启用swagger2功能注解
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestfulApi() {
        ParameterBuilder par = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        par.name("Authorization").description("格式为 Authorization=Bearer+空格+token(Bearer为字符串)用户token /auth/** /sms/** 路径下不需要令牌").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
        pars.add(par.build());
        //api文档实例
        //文档类型：DocumentationType.SWAGGER_2
        return new Docket(DocumentationType.SWAGGER_2)

                //api信息
                .apiInfo(apiInfo())
                //构建api选择器
                .select()
                //api选择器选择api的包
                .apis(RequestHandlerSelectors.basePackage("com.quantumtime.qc"))
                //api选择器选择包路径下任何api显示在文档中
                .paths(PathSelectors.any())
                //创建文档
                .build()
                .globalOperationParameters(pars)
                .ignoredParameterTypes(HttpServletResponse.class, HttpServletRequest.class);
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Lucifer", "", "zyf18824843235@163.com");
        return new ApiInfoBuilder()
                .title("RESTful接口 ")
                .description("接口描述")
                .contact(contact)
//                .termsOfServiceUrl("termsOfServiceUrl")
                .version("1.0")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }

}
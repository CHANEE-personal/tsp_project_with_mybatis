package com.tsp.new_tsp_project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
@Profile({"local","kang"})
public class swaggerConfiguration {

	/**
	 * <pre>
	 * 1. MethodName : apiInfo
	 * 2. ClassName  : swaggerConfiguration.java
	 * 3. Comment    : swagger api 정보
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 02. 09.
	 * </pre>
	 *
	 * @return
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Tsp Model")
				.description("Tsp Model API")
				.build();
	}

	/**
	 * <pre>
	 * 1. MethodName : commonApi
	 * 2. ClassName  : commonApi.java
	 * 3. Comment    : swagger api 경로 설정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 02. 09.
	 * </pre>
	 *
	 * @return
	 */
	@Bean
	public Docket commonApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("tsp")
				.apiInfo(this.apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.tsp.new_tsp_project.api"))
				.paths(PathSelectors.ant("/api/**"))
				.build()
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apikey()));
	}

	private ApiKey apikey() {
		return new ApiKey("JWT", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return springfox.documentation.spi.service.contexts.SecurityContext
				.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}
}

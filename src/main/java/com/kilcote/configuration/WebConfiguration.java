package com.kilcote.configuration;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.kilcote.common.constants.StringConstant;

@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH");;
	}

	@Bean(name="localeResolver")
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US);
//		//germany
//		Locale germany = new Locale("de, DE"); 
//		//czech
//		Locale czech = new Locale("cs", "CZ"); 
//		//russian
//		Locale russian = new Locale("ru", "CZ"); 
//		//english
//		Locale english = new Locale("en", "US");
//		//france
//		Locale france = new Locale("fr", "US");
//		//polish
//		Locale polish = new Locale("pl, PL");
		return slr;
	}


	@Bean(name="messageSource")
	public MessageSource getMessageResource()  {
		ReloadableResourceBundleMessageSource messageResource= new ReloadableResourceBundleMessageSource();
		messageResource.setBasename("classpath:i18n/messages");
		messageResource.setDefaultEncoding(StringConstant.UTF_8);
		return messageResource;
	}
	
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/**")
//		.addResourceLocations("classpath:/static/")
//		.resourceChain(true)
//		.addResolver(new PathResourceResolver() {
//			@Override
//			protected Resource getResource(String resourcePath, Resource location) throws IOException {
//				Resource requestedResource = location.createRelative(resourcePath);
//				return requestedResource.exists() && requestedResource.isReadable() ? requestedResource : new ClassPathResource("/static/index.html");
//			}
//		});
//	}

//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/").setViewName("forward:/index.html");
//	}


//	private Connector redirectConnector() {
//		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//		connector.setScheme("http");
//		connector.setPort(8080);
//		connector.setSecure(false);
//		connector.setRedirectPort(8443);
//		return connector;
//	}
}

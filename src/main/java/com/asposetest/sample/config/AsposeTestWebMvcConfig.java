package com.asposetest.sample.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.asposetest.sample.support.SettingCons;
import com.asposetest.sample.support.SupportUtils;

/**
 * AsposeTestWebMvcConfig is used for defining custom configuration.
 * EnableWebMvc annotation is used for initializing Spring MVC configuration.
 * 
 * @author HARIHARAN MANI
 * @since 07-01-2022
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.asposetest.sample.controller")
public class AsposeTestWebMvcConfig implements WebMvcConfigurer {

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseTrailingSlashMatch(false);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("GET", "POST", "PUT");
	}

	@Bean
	public AsposeTestHandlerInterceptor getHandlerInterceptor() {
		return new AsposeTestHandlerInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getHandlerInterceptor()).excludePathPatterns(getExcludePatterns());

		SupportUtils.printSysout("@ 04 - HandlerInterceptors added");
	}

	/**
	 * This method is used to define the excludePatterns for HandlerInterceptor
	 * 
	 * @return excludePatternList
	 */
	private List<String> getExcludePatterns() {
		List<String> excludePatterns = new ArrayList<>();
		// To Skip Static Resources
		excludePatterns.add("/js/**");
		excludePatterns.add("/css/**");
		excludePatterns.add("/resource/**");

		return excludePatterns;
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resource/**").addResourceLocations("/resource/");
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");

		SupportUtils.printSysout("@ 05 - All JS, CSS and Image resources added");
	}

	@Bean
	public InternalResourceViewResolver resolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		resolver.setContentType(SettingCons.JSON_UTF8_ENCODING);

		SupportUtils.printSysout("@ 06 - All JSP resources added");
		return resolver;
	}

}
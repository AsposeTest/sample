package com.asposetest.sample.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.asposetest.sample.support.SupportUtils;

/**
 * AsposeTestWebAppConfig is used to initialize Dispatcher SERVLET.
 * 
 * @author HARIHARAN MANI
 * @since 07-01-2022
 */
@Configuration
public class AsposeTestWebAppConfig implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TempFiles will be cleared on Server Start
		SupportUtils.printSysout(SupportUtils.reCreateTempFolder());

		SupportUtils.serverStartedStatus();

		createDispatcherServlet(servletContext); // create dispatcher context
	}

	/**
	 * createDispatcherServlet method is used to initialize the DispatcherServlet.
	 * 
	 * @param servletContext
	 */
	private void createDispatcherServlet(ServletContext servletContext) {
		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
		dispatcherContext.register(AsposeTestWebDbConfig.class);
		DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", dispatcherServlet);
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");

		SupportUtils.printSysout("@ 03 - Dispatcher Servlet initialized in AsposeTestWebAppConfig");
	}
}
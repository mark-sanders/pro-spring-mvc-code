package com.apress.prospringmvc.bookstore.web.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.apress.prospringmvc.bookstore.config.InfrastructureContextConfiguration;
import com.apress.prospringmvc.bookstore.config.TestDataContextConfiguration;

//	SpringServletContainerInitializer is registered under spring-web-*.jar!META-INF/services/
//	in a file called "javax.servlet.ServletContainerInitializer"

// 	SpringServletContainerInitializer implements javax.servlet.ServletContainerInitializer
// 	and is annotated with @HandlesTypes(WebApplicationInitializer.class)

//	On application start, a new SpringServletContainerInitializer is instantiated and 
// 	it's method onStartup() is called with the set of classes mentioned in @HandlesTypes
//  and the server context

// 	SpringServletContainerInitializer then instantiates these classes 
// 	and calls WebApplicationInitializer.onStart()

//
// 	This replaces entries in web.xml and the following entry 

/**
 * {@link WebApplicationInitializer} that will be called by Spring's {@link SpringServletContainerInitializer} as part
 * of the JEE {@link ServletContainerInitializer} pattern. This class will be called on application startup and will
 * configure our JEE and Spring configuration.
 * <p/>
 * 
 * It will first initializes our {@link AnnotationConfigWebApplicationContext} with the common {@link Configuration}
 * classes: {@link InfrastructureContextConfiguration} and {@link TestDataContextConfiguration} using a typical JEE
 * {@link ContextLoaderListener}.
 * <p/>
 * 
 * Next it creates a {@link DispatcherServlet}, being a normal JEE Servlet which will create on its turn a child
 * {@link AnnotationConfigWebApplicationContext} configured with the Spring MVC {@link Configuration} classes
 * {@link WebMvcContextConfiguration} and {@link WebflowContextConfiguration}. This Servlet will be registered using
 * JEE's programmatical API support.
 * <p/>
 * 
 * Finally it will also register a JEE listener for enabling the open entity manager in view pattern:
 * {@link OpenEntityManagerInViewFilter}
 * 
 * @author Marten Deinum
 * @author Koen Serneels
 * 
 */


public class BookstoreWebApplicationInitializer implements WebApplicationInitializer {

	private static final Class<?>[] basicConfigurationClasses = 
		{ 
			TestDataContextConfiguration.class,									// could be test-data-
			InfrastructureContextConfiguration.class, 
		};

	private static final String DISPATCHER_SERVLET_NAME = "dispatcher";

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		registerListener(servletContext);
		registerDispatcherServlet(servletContext);
		registerOpenEntityManagerInViewFilter(servletContext);
	}

	private void registerDispatcherServlet(ServletContext servletContext) {
		
		// next three statements replace the following stanza in web.xml
		//		<!-- The front controller of this Spring Web application, responsible for handling all application requests -->
		//		<servlet>
		//				<servlet-name>dispatcher</servlet-name>
		//				<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		//				<init-param>
		//					<param-name>contextConfigLocation</param-name>
		//					<param-value>
		//						/WEB-INF/spring/appServlet/web-mvc-context.xml,		<!-- instead of WebMvcContextConfiguration.class --> 
		//						/WEB-INF/spring/appServlet/webflow-context.xml,		<!-- instead of WebflowContextConfiguration.class -->
		//					</param-value>
		//				</init-param>
		//				<load-on-startup>1</load-on-startup>
		//		</servlet>
		
		AnnotationConfigWebApplicationContext dispatcherContext = createContext(
				WebMvcContextConfiguration.class,
				WebflowContextConfiguration.class);

		ServletRegistration.Dynamic dispatcher = 
				servletContext.addServlet(DISPATCHER_SERVLET_NAME,
						new DispatcherServlet(dispatcherContext));
		
		dispatcher.setLoadOnStartup(1);
		
		// replace the following stanza in web.xml
		//		<!-- Map all *.spring requests to the DispatcherServlet for handling -->
		//		<servlet-mapping>
		//			<servlet-name>dispatcher</servlet-name>
		//			<url-pattern>/</url-pattern>
		//		</servlet-mapping>
		dispatcher.addMapping("/");
	}

	private void registerListener(ServletContext servletContext) {

		// the next statement is a replacement the following stanza from web.xml
		//		<!-- The master configuration file for this Spring web application -->
		//		<context-param>
		//			<param-name>contextConfigLocation</param-name>
		//			<param-value>
		//				/WEB-INF/spring/test-data-context.xml,							<!-- config from TestDataContextConfiguration -->
		//				/WEB-INF/spring/infrastructure-context.xml,						<!-- config from InfrastructureContextConfiguration -->
		//			</param-value>
		//		</context-param>
		
		AnnotationConfigWebApplicationContext rootContext = createContext(basicConfigurationClasses);

		// 	replacement for this stanza in web.xml
		//		<listener>
		//			<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
		//		</listener>
		servletContext.addListener(new ContextLoaderListener(rootContext));
		
		// 	replacement for this stanza in web.xml
		//		<listener>
		//			<listener-class>org.springframework.web.context.request.RequestContextListener</listener-class>
		//		</listener>
		servletContext.addListener(new RequestContextListener());
	}

	private void registerOpenEntityManagerInViewFilter(ServletContext servletContext) {
		
		// filter stanza
		//
		//		<filter>
		//			<filter-name>openEntityManagerInView</filter-name>
		//			<filter-class>org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter</filter-class>
		//		</filter>
		
		FilterRegistration.Dynamic registration = 
				servletContext.addFilter("openEntityManagerInView", new OpenEntityManagerInViewFilter());
		
		// filter-mapping stanza
		//
		//		<filter-mapping>
		//			<filter-name>openEntityManagerInView</filter-name>
		//			<servlet-name>dispatcher</servlet-name>
		//			<dispatcher>REQUEST</dispatcher>
		//			<dispatcher>FORWARD</dispatcher>
		//		</filter-mapping>
		
		registration.addMappingForServletNames(
				EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), 
				true,
				DISPATCHER_SERVLET_NAME);
	}

	/**
	 * Factory method to create {@link AnnotationConfigWebApplicationContext} instances.
	 * @param annotatedClasses
	 * @return
	 */
	private AnnotationConfigWebApplicationContext createContext(final Class<?>... annotatedClasses) {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(annotatedClasses);
		return context;
	}
}
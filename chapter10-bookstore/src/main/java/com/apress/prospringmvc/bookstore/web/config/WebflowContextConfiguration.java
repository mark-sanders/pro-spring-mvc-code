package com.apress.prospringmvc.bookstore.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;

import com.apress.prospringmvc.bookstore.web.interceptor.CommonDataHandlerInterceptor;

/**
 * The glue between Web Flow and Spring MVC, registers the {@link FlowHandlerAdapter} and {@link FlowHandlerMapping}
 * which will enable the {@link DispatcherServlet} to recognize Web Flow requests and send them to the
 * {@link FlowExecutor}
 * 
 * @author Marten Deinum
 * @author Koen Serneels
 * 
 */
@Configuration
@ImportResource("classpath:/spring/webflow-config.xml")							// this is where that XML configuration gets used 
public class WebflowContextConfiguration {

	@Autowired
	private FlowExecutor flowExecutor;											// from webflow-config.xml
	
	@Autowired
	private FlowDefinitionRegistry flowRegistry;								// from webflow-config.xml
	
	@Autowired
	private CommonDataHandlerInterceptor commonDataHandlerInterceptor;			// from WebMvcContextConfiguration
	
	@Autowired
	private LocaleChangeInterceptor localeChangeInterceptor;					// from WebMvcContextConfiguration
	

	// equivalent of:
	//
	//		<!-- Dispatches requests mapped to flows to FlowHandler implementations -->
	//		<bean class="org.springframework.webflow.mvc.servlet.FlowHandlerAdapter">
	//			<property name="flowExecutor" ref="flowExecutor"/>
	//		</bean>
	
	@Bean
	public FlowHandlerAdapter flowHandlerAdapter() {
		FlowHandlerAdapter flowHandlerAdapter = new FlowHandlerAdapter();
		flowHandlerAdapter.setFlowExecutor(flowExecutor);
		return flowHandlerAdapter;
	}

	// equivalent of:
	//
	//		<!-- Maps request paths to flows in the flowRegistry; e.g. a path of /hotels/booking looks for a flow with id "hotels/booking". -->
	//		<bean class="org.springframework.webflow.mvc.servlet.FlowHandlerMapping">
	//			<property name="flowRegistry" ref="flowRegistry" />
	//			<property name="interceptors">
	//				<list>
	//					<ref bean="commonDataHandlerInterceptor"/>
	//					<ref bean="localeChangeInterceptor"/>
	//				</list>
	//			</property>
	//		</bean>

	@Bean
	public FlowHandlerMapping flowHandlerMapping() {
		FlowHandlerMapping flowHandlerMapping = new FlowHandlerMapping();
		flowHandlerMapping.setInterceptors(new Object[] { commonDataHandlerInterceptor, localeChangeInterceptor });
		flowHandlerMapping.setFlowRegistry(flowRegistry);
		return flowHandlerMapping;
	}
}

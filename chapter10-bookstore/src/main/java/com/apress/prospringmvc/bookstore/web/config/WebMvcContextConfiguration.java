package com.apress.prospringmvc.bookstore.web.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.springframework.web.servlet.view.tiles2.TilesView;

import com.apress.prospringmvc.bookstore.web.interceptor.CommonDataHandlerInterceptor;

/**
 * Spring MVC Configuration.
 * 
 * @author Marten Deinum
 * @author Koen Serneels
 * 
 */
@Configuration																	// it's like a XML configuration file but more obtuse 
@EnableWebMvc 																	// this is the equivalent of <mvc:annotation-driven />
@ComponentScan("com.apress.prospringmvc.bookstore.web")							// this is the equivalent of 
																				//	<context:component-scan
																				//		base-package="com.apress.prospringmvc.bookstore.web" />
public class WebMvcContextConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**/*").addResourceLocations("classpath:/META-INF/web-resources/");
																				// this is the equivalent of 
																				// 	<resources 
																				//		mapping="/resources/**/*" 
																				// 		location="classpath:/META-INF/web-resources/" />
	}

	// -- Start Locale Support (I18N) --//

	/**
	 * The {@link LocaleChangeInterceptor} allows for the locale to be changed. It provides a <code>paramName</code>
	 * property which sets the request parameter to check for changing the language, the default is <code>locale</code>.
	 * @return the {@link LocaleChangeInterceptor}
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	/**
	 * The {@link LocaleResolver} implementation to use. Specifies where to store the current selectd locale.
	 * 
	 * @return the {@link LocaleResolver}
	 */
	
	// equivalent to:
	//		<beans:bean 
	//			id="localeResolver"
	//			class="org.springframework.web.servlet.i18n.CookieLocaleResolver" />
	
	@Bean
	public LocaleResolver localeResolver() {
		return new CookieLocaleResolver();
	}

	/**
	 * To resolve message codes to actual messages we need a {@link MessageSource} implementation. The default
	 * implementations use a {@link java.util.ResourceBundle} to parse the property files with the messages in it.
	 * @return the {@link MessageSource}
	 */
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}

	// -- End Locale Support (I18N) --//

	// is this really needed?
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setOrder(2);
		internalResourceViewResolver.setPrefix("/WEB-INF/view/");
		internalResourceViewResolver.setSuffix(".jsp");
		internalResourceViewResolver.setViewClass(JstlView.class);
		return internalResourceViewResolver;
	}

	// equivalent to:
	//
	//		<beans:bean 
	//			id="tilesViewResolver"
	//			class="org.springframework.web.servlet.view.UrlBasedViewResolver">
	//			<beans:property 
	//				name="viewClass" 
	//				value="org.springframework.web.servlet.view.tiles2.TilesView" />
	//		</beans:bean>
	
	@Bean
	public ViewResolver tilesViewResolver() {
		UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
		urlBasedViewResolver.setOrder(1);
		urlBasedViewResolver.setViewClass(TilesView.class);
		return urlBasedViewResolver;
	}

	// equivalent to:
	//
	//		<beans:bean
	//			id="tilesConfigurer"
	//			class="org.springframework.web.servlet.view.tiles2.TilesConfigurer">
	//			<beans:property name="definitions">
	//				<beans:list>
	//					<beans:value>/WEB-INF/tiles/tiles-configuration.xml</beans:value>
	//				</beans:list>
	//			</beans:property>
	//		</beans:bean>		
	//
	// which I think will simplify to:
	// 
	//		<beans:bean
	//			id="tilesConfigurer"
	//			class="org.springframework.web.servlet.view.tiles2.TilesConfigurer"
	//			p:definitions="/WEB-INF/tiles/tiles-configuration.xml" />
	
	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setDefinitions(new String[] { "/WEB-INF/tiles/tiles-configuration.xml" });
		return tilesConfigurer;
	}

	@Bean
	public CommonDataHandlerInterceptor commonDataHandlerInterceptor() {
		return new CommonDataHandlerInterceptor();
	}

	// equivalent to:
	//
	//		<interceptors>
	//			<beans:bean class="com.apress.prospringmvc.bookstore.web.interceptor.CommonDataHandlerInterceptor" />
	//			<beans:bean class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor" p:paramName="lang" />
	//		</interceptors>
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(commonDataHandlerInterceptor());
		registry.addInterceptor(localeChangeInterceptor());
	}
}
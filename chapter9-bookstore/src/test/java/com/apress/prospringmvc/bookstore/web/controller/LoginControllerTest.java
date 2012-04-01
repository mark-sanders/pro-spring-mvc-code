package com.apress.prospringmvc.bookstore.web.controller;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.domain.support.AccountBuilder;
import com.apress.prospringmvc.bookstore.service.AccountService;
import com.apress.prospringmvc.bookstore.service.AuthenticationException;
import com.apress.prospringmvc.bookstore.web.interceptor.SecurityHandlerInterceptor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class LoginControllerTest {

	@Autowired
	private LoginController loginController;
	@Autowired
	private AccountService accountService;

	@Before
	public void setup() throws AuthenticationException {
		Account account = new AccountBuilder() {
			{
				address("Herve", "4650", "Rue de la station", "1", null, "Belgium");
				credentials("john", "secret");
				name("John", "Doe");
			}
		}.build(true);

		Mockito.when(accountService.login("john", "secret")).thenReturn(account);
	}

	@After
	public void verify() throws AuthenticationException {
		Mockito.verify(accountService, VerificationModeFactory.times(3)).login("john", "secret");
		Mockito.reset();
	}

	@Test
	public void testHandleLogin() throws AuthenticationException {

		MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
		mockHttpServletRequest.getSession().setAttribute(SecurityHandlerInterceptor.REQUESTED_URL, "someUrl");

		String view = loginController.handleLogin("john", "secret", mockHttpServletRequest);

		Account account = (Account) mockHttpServletRequest.getSession().getAttribute(
				SecurityHandlerInterceptor.ACCOUNT_ATTRIBUTE);

		assertNotNull(account);
		assertEquals("John", account.getFirstName());
		assertEquals("Doe", account.getLastName());
		assertNull(mockHttpServletRequest.getSession().getAttribute(SecurityHandlerInterceptor.REQUESTED_URL));
		assertEquals("redirect:someUrl", view);

		// Test the different view selection choices
		mockHttpServletRequest = new MockHttpServletRequest();
		view = loginController.handleLogin("john", "secret", mockHttpServletRequest);
		assertEquals("redirect:/index.htm", view);

		mockHttpServletRequest = new MockHttpServletRequest();
		mockHttpServletRequest.getSession().setAttribute(SecurityHandlerInterceptor.REQUESTED_URL, "abclogindef");
		view = loginController.handleLogin("john", "secret", mockHttpServletRequest);
		assertEquals("redirect:/index.htm", view);
	}

	@Configuration
	static class LoginControllerTestConfiguration {

		@Bean
		public AccountService accountService() {
			return Mockito.mock(AccountService.class);
		}

		@Bean
		public LoginController loginController() {
			return new LoginController();
		}
	}
}

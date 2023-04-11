package com.dws.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.WebApplicationContext;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AmountException;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.web.AccountsController;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
class ChallengeApplicationTests {
	@Autowired
	AccountsController accountsController;
	
	@MockBean
	AccountsRepository accountsRepository; 
	
	@Autowired
	AccountsRepository accountsRepository1; 
	
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;

	ObjectMapper om = new ObjectMapper();

//	@Before
//	public void setUp() {
//		MockitoAnnotations.initMocks(AccountsController.class);
//	}

	@Test
	void getAccountTest() {
		String account ="123";
		BigDecimal balance = new BigDecimal(2002);
		String emailId = "vikasv16.mishra@gmail.com";
		when(accountsRepository.getAccount(account)).thenReturn(new Account(account, balance, emailId));
		assertEquals(balance, accountsController.getAccount(account).getBalance());
		
	}
	
	@Test
	void getAccountServiceExceptionTest() {
		String str = "Account id 123 already exists!";
		Account account = new Account("123", new BigDecimal(2002), "abc@gmail.com");
		
		when(accountsController.createAccount(account)).thenThrow(new DuplicateAccountIdException(str));
		assertEquals(str, accountsController.createAccount(account).getBody().toString());
		
		
	}
	
	@Test
	void getAccountServiceTestException() {
		Account account = new Account("123", new BigDecimal(2002), "abc@gmail.com");
		ResponseEntity<Object> response= accountsController.createAccount(account);
		
		assertNotEquals(response.getStatusCode().is4xxClientError(), true);
		
		
		
	}
	/* If transfer amount is less than 0.. then it will throw invalidamountException*/
	@Test
	public void fundTransferTestException() throws Exception {
		Account account1 = new Account("123","test@gmail.com");
		account1.setBalance(new BigDecimal(2011));
		Account account2 = new Account("456","test1@gmail.com");
		account2.setBalance(new BigDecimal(2019));
		
		
		when(accountsController.fundTransfer(account1.getAccountId(), account2.getAccountId(), new BigDecimal(-1))).thenThrow(new AmountException("Invalid Amount"));
	}
	
	

}
package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AmountException;
import com.dws.challenge.exception.InvalidAccountIdException;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.web.AccountsController;

import jakarta.validation.Valid;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {
	private static final Logger log = LoggerFactory.getLogger(AccountsController.class);

	@Getter
	private final AccountsRepository accountsRepository;
	
	@Autowired
	private EmailNotificationService emailNotificationService;

	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}

//using synchronized keyword for making method- "fundTransfer" thread safe to avoid from deadlock
	@Async
	public CompletableFuture<ResponseEntity<String>> fundTransfer(@Valid String fromAccountId, @Valid String toAccountId, @Valid BigDecimal amount)
	{
		// TODO Auto-generated method stub
		log.info("Fund Transfer initiated");
		log.info("Thread executing: " + Thread.currentThread().getName());
		Account fromAct = accountsRepository.getAccount(fromAccountId);
		Account toAct = accountsRepository.getAccount(toAccountId);
		
		//fundTransfer1(fromAct,toAct,amount);
		//To check whether fromAccountId and toAccountId are not same
		if(fromAccountId.equals(toAccountId)) {
			throw new InvalidAccountIdException("From accont and to account are same");
			
		}
		// To check whether both the account exist in DB and also to check tr money should be greater than 0
		if (fromAct != null && toAct != null && amount.compareTo(BigDecimal.ZERO)>0) {
			BigDecimal from = fromAct.getBalance();
			BigDecimal to = toAct.getBalance();
			synchronized (this) {
				/*
				 * Condition to check tr. amount should be greater than available bal. if
				 * condition fails it will throw Invalid Amount exception
				 */
				if (from.compareTo(amount) >= 0) {
					to = to.add(amount);
					toAct.setBalance(to);

				}
							
			 else {
					// If transfer amount is less then 0 and if transfer amount is greater than
					// available balance
					throw new AmountException("Invalid Amount");
				}
			}
			
		} else {
			throw new InvalidAccountIdException("Invalid account");
		}
		
		BigDecimal fromBal = fromAct.getBalance();//
		fromBal = fromBal.subtract(amount);// transfer amount getting subtracted from available
		fromAct.setBalance(fromBal);//and getting set here
		log.info("Fund transfer has been done from{}", fromAccountId );
		//After successful transfer an email will be shoot out to the receiver  account
		emailNotificationService.notifyAboutTransfer(toAct,"Fund transfer done successfully");
		// Email will be sent to sender about amount deduction 
		emailNotificationService.notifydebitTransfer(fromAct, "Amount debited ", amount);
		ResponseEntity<String> resp = new ResponseEntity<String>("Fund Transfer done successfully",HttpStatus.CREATED); 
		return CompletableFuture.completedFuture(resp);
		
	}

	
}

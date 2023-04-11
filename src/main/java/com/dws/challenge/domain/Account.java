package com.dws.challenge.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/* model class */
@Data
public class Account {

	@NotNull
	@NotEmpty
	private final String accountId;

	@NotNull
	@Min(value = 0, message = "Initial balance must be positive.")
	private BigDecimal balance;
	
	@NotNull
	@NotEmpty
	private String emailId;

	public Account(String accountId,String emailId) {
		this.accountId = accountId;
		this.balance = BigDecimal.ZERO;
		this.emailId=emailId;
	}

	@JsonCreator
	public Account(@JsonProperty("accountId") String accountId, @JsonProperty("balance") BigDecimal balance,@JsonProperty("emailId") String emailId) {
		this.accountId = accountId;
		this.balance = balance;
		this.emailId =emailId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	

}

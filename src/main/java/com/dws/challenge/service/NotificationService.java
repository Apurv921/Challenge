package com.dws.challenge.service;

import java.math.BigDecimal;

import com.dws.challenge.domain.Account;

public interface NotificationService {
	void notifyAboutTransfer(Account account, String transferDescription);

	void notifydebitTransfer(Account account, String transferDescription,BigDecimal Amount);
}

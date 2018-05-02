package com.testTask.bank.dao;

import com.testTask.bank.domain.Account;
import com.testTask.bank.domain.MoneyTransfer;

public interface AccountDao {

    void transfer(MoneyTransfer moneyTransfer);

    Account getByAccountNumber(Integer accountNumber);

    void create(Account account);
}

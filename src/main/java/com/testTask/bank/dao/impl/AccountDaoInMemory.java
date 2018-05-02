package com.testTask.bank.dao.impl;

import com.testTask.bank.dao.AccountDao;
import com.testTask.bank.domain.Account;
import com.testTask.bank.domain.MoneyTransfer;
import com.testTask.bank.exception.AccountNotFoundException;
import com.testTask.bank.exception.ClientAccountException;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

@Singleton
public class AccountDaoInMemory implements AccountDao {
    private final Map<Integer, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void transfer(MoneyTransfer moneyTransfer) {
        Account accountFrom = getByAccountNumber(moneyTransfer.getAccountNumberFrom());
        Account accountTo = getByAccountNumber(moneyTransfer.getAccountNumberTo());

        Lock firstLock;
        Lock secondLock;
        //Using predefined order of Accounts to lock to avoid deadlock
        if (accountFrom.getAccountNumber() < accountTo.getAccountNumber()) {
            firstLock = accountFrom.lock();
            secondLock = accountTo.lock();
        } else {
            firstLock = accountTo.lock();
            secondLock = accountFrom.lock();
        }

        firstLock.lock();
        secondLock.lock();

        try {
            if (accountFrom.getAmount().compareTo(moneyTransfer.getAmount()) < 0) {
                throw new ClientAccountException(
                        String.format("Account From '%s' has not enough amount to transfer %s amount",
                                accountFrom, moneyTransfer));
            }

            accountFrom.subtractAmount(moneyTransfer.getAmount());
            accountTo.addAmount(moneyTransfer.getAmount());
        } finally {
            firstLock.unlock();
            secondLock.unlock();
        }
    }

    @Override
    public Account getByAccountNumber(Integer accountNumber) {
        Account account = accounts.get(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException(String.format("Account '%s' is not found", accountNumber));
        }

        return account;
    }

    @Override
    public void create(Account account) {
        Account existingAccount = accounts.putIfAbsent(account.getAccountNumber(), account);
        if (existingAccount != null) {
            throw new ClientAccountException(String.format("Account '%s' already exists",
                    account.getAccountNumber()));
        }
    }
}

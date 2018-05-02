package com.testTask.bank.config;

import com.testTask.bank.dao.AccountDao;
import com.testTask.bank.dao.impl.AccountDaoInMemory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class BankApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(AccountDaoInMemory.class).to(AccountDao.class);
    }
}

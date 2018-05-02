package com.testTask.bank.resource;

import com.testTask.bank.dao.AccountDao;
import com.testTask.bank.domain.Account;
import com.testTask.bank.domain.MoneyTransfer;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("accounts")
@Singleton
public class AccountResource {
    private final AccountDao accountDao;

    @Inject
    public AccountResource(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @GET
    @Path("account/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@PathParam("accountNumber") @NotNull @DecimalMin(value = "0") Integer accountNumber) {
        return accountDao.getByAccountNumber(accountNumber);
    }

    @POST
    @Path("account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(@Valid Account account) {
        accountDao.create(account);
        return Response.ok().build();
    }


    @POST
    @Path("transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(@Valid MoneyTransfer moneyTransfer) {
        accountDao.transfer(moneyTransfer);
        return Response.ok().build();
    }
}

package com.testTask.bank.resource;

import com.testTask.bank.Main;
import com.testTask.bank.domain.Account;
import com.testTask.bank.domain.MoneyTransfer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test for GET/PUT methods of AccountResource
 */
class AccountResourceTest {
    private HttpServer server;
    private WebTarget target;

    @BeforeEach
    void setUp() {
        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @AfterEach
    void tearDown() {
        server.shutdownNow();
    }

    @DisplayName("Test POST for accounts/account and GET for accounts/account/accountNumber")
    @ParameterizedTest(name =
            "Account Number: {0}, Amount: {1}, Expected POST Response Code: {2}")
    @CsvSource({
            "1, '50', 200",
            "1, '-100', 400"
    })
    void testCreateAndGetAccount(int accountNumber, String amount,
                                 int expectedPostResponseCode) {
        int actualPostResponseCode = createAccountAndGetResponse(accountNumber, amount);
        assertEquals(expectedPostResponseCode, actualPostResponseCode);
        if (expectedPostResponseCode == 200) {
            Account actual = getAccountAndCheckStatusOk(accountNumber);
            assertEquals(new Account(accountNumber, new BigDecimal(amount)), actual);
        }
    }

    @DisplayName("Test POST for accounts/account, same POST, and GET for accounts/account/accountNumber")
    @Test
    void testCreateAlreadyExistedAccount() {
        assertEquals(200, createAccountAndGetResponse(1, "100"));
        assertEquals(400, createAccountAndGetResponse(1, "200"));
        Account actual = getAccountAndCheckStatusOk(1);
        assertEquals(new Account(1, BigDecimal.valueOf(100L)), actual);
    }

    @DisplayName("Test POST for accounts/transfer with autogenerated JSON body")
    @ParameterizedTest(name =
            "Account From: {0}, Account To: {1}, Transfer Amount: {2}, Expected Response Code: {3}, " +
                    "Expected Amount for Account From: {4}, Expected Amount for Account To: {5}")
    @CsvSource({
            "1, 2, '10', 200, '90', '110'",
            "2, 1, '100', 200, '0', '200'",
            "1, 1, '100', 200, '100', '100'",
            "1, 2, '1.1', 200, '98.9', '101.1'",
            "1, 2, '0.0000000001', 200, '99.9999999999', '100.0000000001'",
            "1, 3, '50', 404, '100', ''",
            "5, 2, '50', 404, '', '100'",
            "5, 6, '50', 404, '', ''",
            "1, 2, '101', 400, '100', '100'",
            "1, 2, '-10', 400, '100', '100'"
    })
    void testTransfer(int accountNumberFrom, int accountNumberTo, String amount,
                      int expectedResponseCode,
                      String expectedAccountFromAmount, String expectedAccountToAmount) {
        MoneyTransfer moneyTransfer =
                new MoneyTransfer(accountNumberFrom, accountNumberTo, new BigDecimal(amount));

        assertTransfer(accountNumberFrom, accountNumberTo,
                expectedResponseCode, expectedAccountFromAmount, expectedAccountToAmount,
                Entity.json(moneyTransfer));
    }

    @DisplayName("Test POST for accounts/transfer with specified JSON body")
    @ParameterizedTest(name =
            "Account From: {0}, Account To: {1}, Expected Response Code: {2}, " +
                    "Expected Amount for Account From: {3}, Expected Amount for Account To: {4}, " +
                    "POST body: {5}")
    @CsvSource({
            "1, 2, 200, '90', '110', " +
                    "'{\"accountNumberFrom\":1,\"accountNumberTo\":2,\"amount\":10}'",
            "1, 2, 200, '90', '110', " +
                    "'{\"accountNumberFrom\":\"1\",\"accountNumberTo\":\"2\",\"amount\":\"10\"}'",
            "1, 2, 400, '100', '100', " +
                    "'{\"accountNumberFrom\":1,\"accountNumberTo\":2}'",
            "1, 2, 400, '100', '100', " +
                    "'{\"accountNumberTo\":2,\"amount\":10}'",
            "1, 2, 400, '100', '100', " +
                    "'{\"accountNumberFrom\":1,\"amount\":10}'",
            "1, 2, 500, '100', '100', " +
                    "'{\"accountNumberFrom\":1,\"accountNumberTo\":2,\"amount\":\"amount\"}'",
            "1, 2, 500, '100', '100', " +
                    "'{\"accountNumberFrom\":\"abc\",\"accountNumberTo\":2,\"amount\":10}'",
            "1, 2, 500, '100', '100', " +
                    "'{\"accountNumberFrom\":1,\"accountNumberTo\":\"abc\",\"amount\":10}'",
            "1, 2, 500, '100', '100', " +
                    "'abc'"
    })
    void testTransferWithSpecificBody(int accountNumberFrom, int accountNumberTo,
                                      int expectedResponseCode,
                                      String expectedAccountFromAmount, String expectedAccountToAmount,
                                      String body) {
        assertTransfer(accountNumberFrom, accountNumberTo,
                expectedResponseCode, expectedAccountFromAmount, expectedAccountToAmount,
                Entity.json(body));

    }

    private void assertTransfer(int accountNumberFrom, int accountNumberTo,
                                int expectedResponseCode,
                                String expectedAccountFromAmount, String expectedAccountToAmount,
                                Entity<?> body) {
        assertEquals(200, createAccountAndGetResponse(1, "100"));
        assertEquals(200, createAccountAndGetResponse(2, "100"));

        Response response = target.path("accounts/transfer").request().post(body);

        assertEquals(expectedResponseCode, response.getStatus());

        assertAccount(accountNumberFrom, expectedAccountFromAmount);
        assertAccount(accountNumberTo, expectedAccountToAmount);
    }

    private void assertAccount(int accountNumber, String expectedAmount) {
        if (expectedAmount.equals("")) {
            return;
        }

        Account account = getAccountAndCheckStatusOk(accountNumber);
        assertEquals(new Account(accountNumber, new BigDecimal(expectedAmount)), account);
    }

    private int createAccountAndGetResponse(int accountNumber, String amount) {
        Account account = new Account(accountNumber, new BigDecimal(amount));
        return target.path("accounts/account")
                .request()
                .post(Entity.json(account))
                .getStatus();
    }

    private Account getAccountAndCheckStatusOk(int accountNumber) {
        Response response = target.path(String.format("accounts/account/%d", accountNumber))
                .request().get();

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
        return response.readEntity(Account.class);
    }

}
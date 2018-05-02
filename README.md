# RESTful API for Money Transfers
Test application implementing a RESTful API for money transfers between accounts

Technologies
------------

* JAX-RS API
* Jersey
* Grizzly HTTP server
* JUnit 5
* Maven

Contents
--------

The mapping of the URI path space is presented in the following table:

URI path                                                 |  Purpose               | HTTP method         | Request and response
-------------------------------------------------------- |  --------------------- | ------------------- | ---------------------
**_/bank/accounts/account/{accountNumber}_**             |  Get account by account number | GET         | **Request:** accountNumber - non-negative number (account number identifying an Account); **Response:** accountNumber and amount of the requesting Account; **Response Statuses:** 200 - OK, 404 - Account is not found, 400 - Request is not valid
**_/bank/accounts/account_**                             |  Create account        | POST                | **Request:** accountNumber - non-negative number (account number identifying an Account), amount - non-negative decimal (account amount); **Response Statuses:** 200 - OK, 400 - Request is not valid/Account already exists
**_/bank/accounts/transfer_**                            |  Transfer amount of money from one Account to another | POST | **Request:** accountNumberFrom - non-negative number (Account From), accountNumberTo - non-negative number (Account To), amount - non-negative decimal (amount to transfer from _Account From_ to _Account To_); **Response Statuses:** 200 - OK, 400 - Request is not valid/Not enough money to transfer, 404 - either _Account From_ or _Account To_ is not found

Running the Application
-----------------------

Run the application as follows:
>     mvn clean package exec:java
This starts the application using Grizzly HTTP server.
You can access WADL at:
-   <http://localhost:8080/bank/application.wadl>


Sample Requests/Responses
-------------------------

You can access resources of this application using curl:

>     curl -H "Content-Type: application/json" -X POST -d '{"accountNumber":123,"amount":1000.12}' -i localhost:8080/bank/accounts/account
You should see:
HTTP/1.1 200 OK
Content-Length: 0

You can GET this account:
>     curl -H "Content-Type: application/json" -i localhost:8080/bank/accounts/account/123
You should see:
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 38

{"accountNumber":123,"amount":1000.12}

Finally, you can create one more account and transfer money from one account to another:
>     curl -H "Content-Type: application/json" -X POST -d '{"accountNumber":456,"amount":500.1}' -i localhost:8080/bank/accounts/account
HTTP/1.1 200 OK
Content-Length: 0
>     curl -H "Content-Type: application/json" -X POST -d '{"accountNumberFrom":123,"accountNumberTo":456,"amount":300.01}' -i localhost:8080/bank/accounts/transfer
HTTP/1.1 200 OK
Content-Length: 0

Now querying the accounts would result in:
>     curl -H "Content-Type: application/json" -i localhost:8080/bank/accounts/account/123
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 37

{"accountNumber":123,"amount":700.11}
>     curl -H "Content-Type: application/json" -i localhost:8080/bank/accounts/account/456
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 37

{"accountNumber":456,"amount":800.11}

Tests
-----

The application includes an integration test AccountResourceTest which itself starts the Grizzly HTTP Server using the base URI http://localhost:8080/bank/ and runs several test cases.

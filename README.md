# Sample Webservice

Spring Boot Java and Mysql Based. Uses MySQL for dev and Embedded H2 for running unit tests. Used spring security for basic authentication.

For write apis (POST and PUT) use username :- admin and password :- admin.
For read apis (GET) username :- user password :- user too works well.

RestTemplate is used for calling external APIs(Fixer Apis in this case).

### Swagger UI(To checkout APIs)

http://localhost:8080/swagger-ui.html

### To Run Service

./gradlew bootRun

### To Run Testcases

./gradle test

P.S. Test cases are not giving absolute code coverage but are written more as a demonstration on writing Service, Repository ,Controller and Controller Integration test.

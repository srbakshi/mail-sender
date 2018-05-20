# mail-sender

**Please change external mail service url/apikeys by modifying properties in:**
```
mail-sender/src/main/resources/app.properties
```
_Note: Email recipients need to be authorized in https://app.mailgun.com/app/account/authorized_

To compile and package into a war file target/mail-sender.war:
```
mvn clean package
```
To run:
```
mvn tomcat7:run -Dorg.slf4j.simpleLogger.defaultLogLevel=all
```
To test using curl:
```
curl -X POST \
  http://localhost:9090/mail/send \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{"from":"sbakshi@gmail.com","to":"srbakshi@gmail.com,anotheremail@gmail.com","subject":"This is the subject", "text":"This is the email body"}'
 ```

External libraries used:
```
- Spring, Spring WebMvc for REST service
- Jackson for JSON serialization/deserialization
- Apache HTTP Client
- Junit and Mockito for testing
```
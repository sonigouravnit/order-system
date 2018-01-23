Order-Sytem

    This is a order system which sends SMS, Email and invoice to a user.

REQUIREMENT: 

    1) update application.yml file (order-system/src/main/resources/application.yml)

        - mysql 
            -change path for mysql db : 
               - spring.datasource.url
               - spring.datasource.username
               - spring.datasource.password

        - kafka endpints
            - kafka bootstrap : kafkaConsumer.bootstrapServers
            
    2) Install Kafka on a server/local machine
    
    3) run query in mysql
        CREATE SCHEMA `order-system` ;


HOW TO RUN :  

    go to project directory and run below command

    mvn clean install -Dmaven.test.skip=true && java -jar target/order-test-0.0.1-SNAPSHOT.jar


API for order : 

    endpoint : http://localhost:8080/v1/order/ 
    
    method : PUT
    
    body : {
           	"userName" : "gourav",
           	"emailAddress": "gourav@gourav.com"
           }
    
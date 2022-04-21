# Messages & Metric

## How to build the project

How to run tests:
```
mvnw test
```

How to build project into .jar:
```
mvnw package
```

How to run built project:
```maven
java -jar .\target\task-0.0.1-SNAPSHOT.jar
```

## H2 web interface

H2 web interface is accessible under the link http://localhost:8080/h2-console
Please use the following parameters to connect:

- JDBC URL: jdbc:h2:mem:record
- User Name: record
- Password: record

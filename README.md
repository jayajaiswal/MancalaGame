# MancalaGame

A very interesting game of Mancala which is played by two players on a board where each player has 6 pits filled with 6 stones each.
The player with most number of stones in the end wins.

#### Restful Service uses following Technologies:

* Spring Boot 2.5
* Swagger (OpenAPI v3)

#### Front-end
* Spring Boot (Thymeleaf)
* Bootstrap
* JQuery

## Run
To run the application run the below command-
```  
mvn clean install  
```
and then run from the SpringBootApplication.java

### Swagger
```  
http://localhost:3000/documentation  
```   
### Back-end
This service has two endpoints:
```  
GET: http://localhost:3000/start (parameter: gameId)
```
returns new game board if gameId is null or not exist, otherwise returns existing game board.
```  
GET: http://localhost:3000/move (parameter: movement object)
```  
returns calculated game board if movement is proper

### Front-end
From your browser, you can access below URI and enjoy playing this game.
```  
http://localhost:3000  
```




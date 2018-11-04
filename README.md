# An adventure in the Land of Marghor
A command line Role Playing Game. CLI-RPG
## Getting Started
This is an assignment project in Java based on Maven with unit testing done in JUnit (Test coverage not nearly sufficient).
The game takes place in the land of Marghor which is ruled by the evil magician Zoram. Do you have what it takes to defeat him?

Player is represented by an 'X' and the evil sorceror by an 'Z'. Every other monster in the game has it's own marking.
A typical turn-based rpg with it's own exotic difficulty by making the player explore it's own mechanics.
### Prerequisites
* [Java](https://www.java.com/en/download/) - The programming language used
* [Maven](https://maven.apache.org/) - Dependency Management
## Installing
## Building
Since the project is based on Maven, it can be built using the following command
```
mvn clean package
```
The build project resides in the /target directory.
## JAR
To run the built project, use the following command in the /targed directory
```
java -jar rpg-cli-1.0.jar
```
## IDE
Run the `main` method in the `com.rpg.app.App` class.

## Running the tests
To run the tests made for the project, use the following command in the root directory of the project
```
mvn test
```

## Authors
Franc Bruzja

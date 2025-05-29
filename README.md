# An adventure in the Land of Marghor
A command line Role Playing Game. CLI-RPG [CURRENTLY WIP & NOT STABLE]
## Getting Started
This is an assignment project in Java based on Maven with unit testing done in JUnit (Test coverage not nearly sufficient).

The game takes place in the land of Marghor which is ruled by the evil magician Zoram. Do you have what it takes to defeat him?

Player is represented by an 'X' and the evil sorcerer by a 'Z'. Every other monster in the game has its own marking.
A typical turn-based rpg with its own exotic difficulty by making the player explore the game's mechanics and even his/her own.
### Prerequisites
* [Java](https://www.java.com/en/download/) - The programming language used
* [Maven](https://maven.apache.org/) - Dependency Management
## Installing
### Building
Since the project is based on Maven, it can be built using the following command on the root of the project
```
mvn clean package
```
The built project resides in the /target directory.
### JAR
To run the built project, use the following command in the /target directory
```
java -jar rpg-cli-1.0.jar
```
### IDE
Run the `main` method in the `com.rpg.app.App` class.

## Running the tests
To run the tests made for the project, use the following command in the root directory of the project
```
mvn test
```

## Authors
Franc Bruzja

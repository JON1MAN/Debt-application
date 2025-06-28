# Debt-application

## Prerequirements

- Java 21
- Gradle
- Docker

## How to Launch the Application (Backend)
1) update with latest remote main
 ```sh
  git pull
 ```
2) Create dev.env file for environment variables in project's root folder (If you are doing it for the first time):
 ```sh
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
SPRING_DATASOURCE_PORT=
SPRING_DATASOURCE_DATABASE_NAME=
SPRING_DATASOURCE_HOST=
SPRING_DATASOURCE_URL=jdbc:mysql://${SPRING_DATASOURCE_HOST}:${SPRING_DATASOURCE_PORT}/${SPRING_DATASOURCE_DATABASE_NAME}

LOG4J_CONFIGURATION_APPENDERS_FILE_FILENAME="your logs file path"

JWT_SECRET_KEY="your generated base 64 key"
 ```
3) Export environment variables from dev.env file into your terminal session: 
 ```sh
  exprot $(xargs < dev.env)
 ```
4) Launch docker container with mysql database
 ```sh
  docker compose up -d 
 ```
5) Build gradle project:
 ```sh
  ./gradlew build
 ```
6) Run gradle project:
 ```sh
  ./gradlew run
 ```

## How to Launch the Application (Backend)
1) update npm packages
 ```sh
  npnm install
 ```
2) run frontend
 ```sh
  npm start
 ```

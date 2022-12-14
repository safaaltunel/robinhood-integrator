# robinhood-integrator

## Endpoints

**POST("localhost:8080/sync/markets")** : Sync markets from RobinHood API


**POST("localhost:8080/sync/instruments")** : Sync instruments from RobinHood API


**GET("localhost:8080/instruments")** : Get all instruments from db


**GET("localhost:8080/instruments?symbol=< symbol >")** : Get instrument for given symbol from db

## How to run

### Docker-compose

If docker and docker-compose is installed in your system, go to src/main/docker folder then run  
``
docker-compose up
``
command

### Running locally

You need to have a postgres database with name "robinhood" in your system. Username and password is specified in application.yml file

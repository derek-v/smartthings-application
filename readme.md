# SmartThings Microservice Demo

This is Derek Vidovic's microservice coding submission.
Since I've been reading about railroading lately, I ended up making a microservice to track "locations" (rail yards, rail-served industries, etc.)
and a 2nd to store train cars.

These servies are written in Kotlin (JDK 11) and uses Gradle to build.
Spring is used to create HTTP APIs.
The services can only be run locally; I worked on Docker/Kubernetes support but didn't get it working correctly.


## Data Model

A location consists of
* An integer ID, which would be the primary key if I'd gotten around to setting up databases.
* A code, a human-readable unique identifier that is entered by users. Must consist of uppercase letters only.
* A name.
* A description.

A car consists of
* An integer ID, similar to the location ID.
* A code, similar to the location code, but in the format ABC-123.
* The ID of the location where this car is currently; this is allowed to be null (unknown).
* A type of car, which tells you what kinds of cargo it can handle.
* A mass in kilograms.

This data model is a bit weird in that there are 2 unique identifiers for each object.
I think it's relatively realistic though: railroads really do use codes like these, not database-generated integers.
But internal services may prefer to use the integers.


## Projects

This git repository contains 4 projects.

### common

This is a library project that contains everything I wanted to share between the other projects.
This contains util code, and definitions of objects that are sent to/from APIs (so everyone agrees what a location looks like, etc.)

Due to Gradle limitations and me not wanting to spend all of my time on build issues,
this project cannot function on its own, only when used by one of the other projects.
The reason is that I can't specify Gradle plugin versions in both this library project and the other projects that use it.

This project contains a very small amount of code that requires Spring; in a more realistic project you'd probably split that out.


### location

This runs the location service. All data is stored in memory; I did some preparation for connecting this to a database,
but decided to work on Docker/Kubernetes instead of database connectivity. Was that a good call? Probably not...

The location service always listens on port 8081.


### traincar

This runs the train car service. All data is stored in memory, and the service is overall very similar to the location service.
This service knows what port the location service runs on and hits some of that service's APIs
in order to enrich train car data with location information.

The train car service always listens on port 8082.


### test

This project contains system tests. They connect to both running services and verify that the APIs work correctly, data is stored correctly, etc.
Almost all of my testing is via these system tests, though there are a couple unit tests in the other projects.

This project also contains a "test" that generates demo data.
When the services start up, they initially have no data;
instead of creating the data via manual POST request, you may want to run DemoDataCreator.


## API List

There's no Swagger, but here's a list of APIs you may want to call.

For the location service:
* GET locations
* POST locations
* GET locations/{code}
* DELETE locations/{code}
* GET locations/id/{id}
* PUT locations/id/{id}
* DELETE locations/id/{id}

For the train car service:
* GET cars
* POST cars
* GET cars/{code}
* DELETE cars/{code}
* GET cars/id/{id}
* PUT cars/id/{id}
* DELETE cars/id/{id}
* GET cars/enriched
* GET cars/enriched/{code}
* GET cars/enriched/id/{id}

The "enriched" APIs contact the location service to enrich the information they return.


## Build & Deployment

### Running Locally

For either service

```
cd <dir> # either "location" or "traincar"
./gradlew bootRun
```
When the services are running locally, you can issue HTTP requests like
`GET http://localhost:8081/locations` (for the location service) or
`GET http://localhost:8082/cars` (for the traincar service).


### Creating Test Data

```
cd test
./gradlew :test --tests "com.example.smartthings.test.DemoDataCreator.createDemoData"
```


### Building .jar Files

```
cd <dir> #either "location" or "traincar"
./gradlew bootJar
```

The output is located in build/libs and is called either location-1.0-SNAPSHOT.jar or traincar-1.0-SNAPSHOT.jar


### Building Docker Images

Before doing this, you have to build the .jar files (above).

```
cd location
sudo docker build -t location .
```

and

```
cd traincar
sudo docker build -t traincar .
```


### Running in Docker

***This doesn't really work - you can access the services, but they can't talk to each other.
I wasn't able to get a docker network working correctly.***

Before doing this, you have to build the docker images (above).

Then, you have to set up a network for the containers to communicate with each other:

```docker network create -d bridge derek-microservices```

Then, to run the services (in separate consoles):

```docker run --network=derek-microservices -p 8081:8081 location```

and

```docker run --network=derek-microservices -p 8082:8082 traincar```


### Running in Kubernetes

***This doesn't work - at least on my machine, I can't connect to the running service.***

Before doing this, you have to build the docker images (above).

*Note: it may take some effort to get your Kubernetes installation to find the docker images you built previously.*

```
kubectl apply -f location.pod.yml
kubectl apply -f location.service.yml
```


## Limitations / Known Issues

* If you get a 400 response from an API, there isn't a helpful error message.
Figuring out how to send a valid body to the PUT/POST APIs may take some digging around the code, sorry.

* If some train car(s) have the IDs of locations that don't exist, errors will be thrown.
I've decided this isn't a valid use case :)
  
* Occasionally a gradle build runs into weird ClassNotFound issues. Trying again usually fixes the problem.
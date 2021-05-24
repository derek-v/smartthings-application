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

Before doing this, you have to build the docker images (above).

***This doesn't work - at least on my machine, I can't connect to the running service.***

*Note: it may take some effort to get your Kubernetes installation to find the docker images you built previously.*

```
kubectl apply -f location.pod.yml
kubectl apply -f location.service.yml
```
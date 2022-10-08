### docker-compose up -d
Start up our Docker container and our image.
### docker-compose down
Shut down the Docker container and our image.
### docker ps
Show process that's currently running
### docker inspect <pid>
Inspect pieces of an instance that are currently running 
### docker inspect <pid> | grep "IPAddress"
Inspect pieces of an instance that are currently running 
with particular IPAddress


### mysql -u root -p
'Enter password: pass'
Enter mysql from command line.

# UPDATE:

### docker ps
List currently running containers
### docker ps -a 
Get list of all containers (running or stopped)
### docker pull 
### docker run <image_name>
Combines `docker pull` and `docker start`. Creates new container.
<br>`-d` - detach, run in the background
<br>`-p HOST_PORT:CONTAINER_PORT` - bind port of your host to the container
<br>`--name <container_name>` - set container name
### docker start | stop <container_name> (or <container-id>) 
Start or stop an existing container (doesn't create new container)
### docker images
List local images
## TROUBLESHOOTING
### docker logs <container_id> (or container_name)
Show container logs
### docker exec -it <container_id> (or container_name) /bin/bash
Open a shell inside a running container

## NETWORK
### docker network ls
Show docker networks
### docker network create <network_name>
Create new docker network (for instance to connect mongo db with mongo-express)

## DOCKER COMPOSE
### docker-compose -f <filename>.yaml up
Start all the containers which are declared in the <filename>.yaml


#### example with [mongo db](https://hub.docker.com/_/mongo) creation from the [Docker course](https://youtu.be/3c-iBn73dDE?t=4825):
```bash
docker run -d \
 -p 27017:27017 \ 
 -e MONGO_INITDB_ROOT_USERNAME=admin \
 -e MONGO_INITDB_ROOT_PASSWORD=password \
 --name mongodb \
 --net mongo-network \
 mongo 
```
### and add [mongo-express](https://hub.docker.com/_/mongo-express)(see "Configuration") to the network
(port `8081` is taken by the antivirus so I use the `8082` on the host side)
```bash
docker run -d \
-p 8082:8081 \
-e ME_CONFIG_MONGODB_ADMINUSERNAME=admin \
-e ME_CONFIG_MONGODB_ADMINPASSWORD=password \
-e ME_CONFIG_MONGODB_SERVER=mongodb \
--net mongo-network \
--name mongo-express 
```

To run js project: 
```
npm install
cd ..
node app
```
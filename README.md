SASS SERVER
----------------------
Why SASS through AAPI  REST?

Sass was originally written in Ruby, but is very slow, then emerged LibSass a C/C++ port of the Sass engine. 
But LibSass is not completely ported Java, only for Linux and java8 versions. Then if you have your application
 in Java7 with Sass-Server you can have an endpoint to convert your files Sass to css.
 
 - run mvn spring-boot:run

Sass-Server run on port aplication.properties:server.port=9999
Creating docker images
----------------------
Docker images cloud be created with **Maven** 


#### <i class="icon-hdd"></i> Whit maven
- execute **mvn clean package docker:build**
- **docker run -p 9000:9000 -t sass-service**

To stop and remove and push containers

-**docker stop ID_Generated** 
-**docker rm ID_Generated**
-**docker push sass-service***


#### <i class="icon-hdd"></i> Push container to Docker Registry

-To push the image you just built to the registry from mvn, specify the pushImage flag. This push lates image
**mvn clean package docker:build -DpushImage**
-To push only specific tags of the image to the registry, specify the pushImageTag flag.
**mvn clean package docker:build -DpushImageTag**


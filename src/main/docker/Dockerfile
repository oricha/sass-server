FROM java:8

#setting build environment variables
ENV JAR_NAME="sass-service"
ENV JAR_VERSION="0.0.1-SNAPSHOT"
ENV APP_PORT=9000

VOLUME /tmp
ADD ${JAR_NAME}-${JAR_VERSION}.jar app.jar
EXPOSE ${APP_PORT}
RUN  sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

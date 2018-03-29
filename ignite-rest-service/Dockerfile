FROM openjdk:8-jre-alpine
ENV JAR_FILE ignite-rest-service-1.0-SNAPSHOT.jar
ENV APPS_HOME /usr/apps
EXPOSE 8090
COPY target/$JAR_FILE $APPS_HOME/
WORKDIR $APPS_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar -DIGNITE_REST_START_ON_CLIENT=true $JAR_FILE"]
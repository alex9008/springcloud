FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
EXPOSE 9411
ADD ./zipkin-server-3.4.4-exec.jar zipkin-server.jar

ENTRYPOINT ["java","-jar","zipkin-server.jar"]

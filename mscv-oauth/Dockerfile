FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
EXPOSE 9100
ADD ./target/mscv-oauth-0.0.1-SNAPSHOT.jar mscv-oauth.jar

ENTRYPOINT ["java","-jar","mscv-oauth.jar"]

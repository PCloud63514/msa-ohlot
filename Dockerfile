FROM openjdk:11

ARG PROFILE
ARG APPLICATION_NAME
ARG APP_VERSION
ENV EXPOSE_PORT 8761

WORKDIR workspace

COPY applications/$APPLICATION_NAME/build/libs/$APPLICATION_NAME-$APP_VERSION.jar /workspace/app.jar

#RUN apk add --no-cache tzdata
#ENV TZ Asia/Seoul

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local", "app.jar"]

EXPOSE ${EXPOSE_PORT}
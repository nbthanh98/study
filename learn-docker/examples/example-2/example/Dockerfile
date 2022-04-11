FROM openjdk:11.0-slim AS build
WORKDIR /workspace/app
COPY . /workspace/app
RUN chmod +x ./gradlew
# RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build -x test -i --stacktrace
RUN ./gradlew clean build -x test

FROM openjdk:11-jre-slim
COPY --from=build /workspace/app/build/libs/example-0.0.1-SNAPSHOT.jar app.jar
RUN /bin/sh -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
ENV TZ="Asia/Ho_Chi_Minh"
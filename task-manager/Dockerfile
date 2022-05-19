FROM maven:3.5.4-jdk-11-slim

# назначили переменную
ENV PROJECT_DIR=/opt/project
# создание директории
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

ADD ./pom.xml $PROJECT_DIR
RUN mvn dependency:resolve

ADD ./src/ $PROJECT_DIR/src
RUN mvn install -DskipTests

FROM openjdk:11-ea-11-jre-slim

ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
COPY --from=0 $PROJECT_DIR/target/task-manager.jar $PROJECT_DIR/

EXPOSE 8080

CMD ["java", "-Xms512m","-jar", "/opt/project/task-manager.jar"]
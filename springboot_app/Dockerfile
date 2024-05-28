FROM maven:3.8.5-openjdk-17

WORKDIR /springboot_app

COPY  . /springboot_app

RUN mvn dependency:go-offline

RUN mvn clean package -Dmaven.test.skip=true

CMD ["java", "-jar", "./target/skinhealth-0.0.1-SNAPSHOT.jar"]

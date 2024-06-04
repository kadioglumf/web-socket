FROM maven:3.9.6-eclipse-temurin-11 as build
WORKDIR /opt/app
COPY pom.xml .
RUN mvn clean compile package -T 1C --batch-mode -Dmaven.test.skip=true -U -fn -Daether.connector.https.securityMode=insecure

#COPY lombok.config .
COPY src src

RUN mvn clean compile package -T 1C --batch-mode -Dmaven.test.skip=true -U -Daether.connector.https.securityMode=insecure
RUN mv target/$(mvn -Daether.connector.https.securityMode=insecure -q -Dexec.executable=echo -Dexec.args='${project.artifactId}-${project.version}.jar' --non-recursive exec:exec 2>/dev/null) app.jar


FROM openjdk:11
RUN groupadd -r app -g 1000 && useradd -u 1000 -r -g app -m -d /opt/app -s /sbin/nologin -c "App user" app && chmod 755 /opt/app
COPY --from=build /opt/app/app.jar /opt/app/app.jar
RUN chown app:app /opt/app/app.jar

RUN rm /etc/localtime; ln -s /usr/share/zoneinfo/Europe/Istanbul /etc/localtime

USER app
WORKDIR /opt/app

ENTRYPOINT ["/bin/bash", "-c", "java -Dspring.profiles.active=docker -Dfile.encoding=UTF-8 -jar /opt/app/app.jar"]
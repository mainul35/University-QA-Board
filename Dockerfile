FROM tomcat:10.1.15-jdk21-temurin-jammy

WORKDIR "webapps"

RUN rm -rf ./ROOT

COPY ./target/*.war ROOT.war

WORKDIR "../bin"

ENTRYPOINT ["./catalina.sh", "run"]

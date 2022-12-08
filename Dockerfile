FROM tomcat:10-jdk17-corretto
RUN rm -rf /usr/local/tomcat/webapps/*

EXPOSE 8080:8080

COPY ./target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh","run"]


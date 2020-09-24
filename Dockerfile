FROM tomcat:latest

COPY $PWD/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war



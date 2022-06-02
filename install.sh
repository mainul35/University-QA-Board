#!/bin/bash
#rm -rf ./target/
#mvn deploy
#mvn -f ./pom.xml clean install -DskipTests
#mvn clean install -DskipTests

#mvn clean
export JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto/
mvn -f ./pom.xml clean install -DskipTests
STATUS=$?
if [ $STATUS -eq 0 ]; then
  echo "Deployment package generating Successful"
  sudo docker image rm mainul35/university-qa-board
  sudo docker image build -t mainul35/university-qa-board .
  sudo docker compose up
else
  echo "Deployment package generating Failed"
fi

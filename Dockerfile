FROM openjdk:21-jdk-oracle as openjdk_oracle

# required for jlink strip-debug to work
#RUN apk add --no-cache binutils

RUN jdeps \
    --ignore-missing-deps \
    -q --multi-release 21 \
    --print-module-deps \
    ./target/*.war > jre-deps.info

# To know about the flags, run jlink --help in your terminal
RUN jlink  \
    --verbose \
    --compress 2 \
    --strip-java-debug-attributes \
    --no-header-files \
    --no-man-pages \
    --output jre \
    --add-modules ALL-MODULE-PATH


FROM ubuntu as oracle

ENV JAVA_HOME=/usr/lib/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY --from=openjdk_oracle ./jre $JAVA_HOME

# -u flag for UID
# -D flag for DON'T CREATE A PASSWORD
# For more on other available flags: https://stackoverflow.com/questions/49955097/how-do-i-add-a-user-when-im-using-oracle-as-a-base-image
ENV USER=docker

RUN adduser --disabled-password $USER

RUN apt-get update -y
RUN apt-get upgrade -y
RUN apt-get install curl -y
RUN curl https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.13/bin/apache-tomcat-10.1.13.tar.gz  --output 'tomcat-10.1.13.tar.gz'
RUN tar -zxvf tomcat-10.1.13.tar.gz
RUN rm tomcat-10.1.13.tar.gz
#RUN apt purge curl
#RUN mvn clean install

WORKDIR "apache-tomcat-10.1.13/webapps"

RUN rm -rf ./ROOT


#RUN rm ROOT.war

COPY ./target/*.war ROOT.war

WORKDIR "../../apache-tomcat-10.1.13/bin"

ENTRYPOINT ["./catalina.sh", "run"]

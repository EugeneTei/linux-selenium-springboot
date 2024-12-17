#FROM selenium/standalone-chrome:131.0
#USER root
#RUN apt-get update && apt-get install -y openjdk-21-jdk
#USER seluser
#WORKDIR /app
#COPY target/*.jar /app/app.jar

FROM selenium/standalone-chrome:131.0

USER root
RUN apt update && apt install bash supervisor
RUN apt-get update && apt-get install -y openjdk-21-jdk

ADD ./supervisord.conf /etc/supervisor/conf.d/

WORKDIR /app
COPY target/*.jar /app/app.jar
RUN ["chmod", "+x", "/app/app.jar"]

CMD ["supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]




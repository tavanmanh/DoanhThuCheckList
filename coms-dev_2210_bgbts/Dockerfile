FROM tomcat:9.0.48-jdk8-openjdk-buster
EXPOSE 8080
ENV TZ="Asia/Ho_Chi_Minh"
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY coms-service/target/coms-service.war /usr/local/tomcat/webapps/
COPY coms-web/coms-web.war /usr/local/tomcat/webapps/



FROM tomcat:11.0-jdk21

COPY target/lab.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]
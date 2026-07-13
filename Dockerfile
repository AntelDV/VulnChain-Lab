FROM tomcat:11.0-jdk21

RUN echo '<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee" version="10.0">\
  <error-page><error-code>404</error-code><location>/404.html</location></error-page>\
  <error-page><error-code>500</error-code><location>/404.html</location></error-page>\
</web-app>' > /usr/local/tomcat/conf/web.xml.patch


COPY target/lab.war /usr/local/tomcat/webapps/ROOT.war
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

CMD ["docker-entrypoint.sh"]



#!/bin/bash
catalina.sh start

echo "Waiting for WAR extraction..."
for i in $(seq 1 30); do
    if [ -d "/usr/local/tomcat/webapps/ROOT/WEB-INF" ]; then
        echo "WAR extracted! Setting permissions..."
        chmod -R 777 /usr/local/tomcat/webapps/ROOT/
        mkdir -p /usr/local/tomcat/webapps/ROOT/uploads
        chmod 777 /usr/local/tomcat/webapps/ROOT/uploads
        echo "Done!"
        break
    fi
    sleep 1
done

tail -f /usr/local/tomcat/logs/catalina.$(date +%Y-%m-%d).log
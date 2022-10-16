FROM openjdk:11-jre

ARG JAR_FILE
RUN echo $JAR_FILE
COPY ${JAR_FILE} app.jar
COPY index.html /opt/static/
COPY manifest.json /opt/static/
COPY favicon.ico /opt/static/
COPY static /opt/static/static/
EXPOSE 8080
ENTRYPOINT java ${JAVA_OPTS:-} -Djava.security.egd=file:/dev/./urandom -jar /app.jar
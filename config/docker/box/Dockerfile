FROM anapsix/alpine-java:8
COPY server-0.1.jar server.jar
RUN sh -c 'touch /server.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/server.jar", "--server.port=8080"]
FROM openjdk:13-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
ARG JAR_FILE
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ADD target/${JAR_FILE} /usr/share/myapp.jar
EXPOSE 8080
EXPOSE 8000
ENTRYPOINT ["java","-jar","/usr/share/myapp.jar"]
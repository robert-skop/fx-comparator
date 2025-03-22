FROM eclipse-temurin:17-alpine

RUN adduser -D default

# Configure timezone
ENV TZ=Europe/Prague
RUN ln -snf /usr/share/zoneinfo/${TZ} /etc/localtime && echo ${TZ} > /etc/timezone

# Change to non-root privilege
USER default

EXPOSE 8080/tcp
WORKDIR /app

CMD java  ${JAVA_OPTS} -Dfile.encoding=UTF-8 -jar /app/app.jar

COPY fx-comparator-core/target/fx-comparator.jar app.jar

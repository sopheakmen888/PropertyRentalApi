# Dockerfile — install maven on eclipse-temurin:25 and build
FROM eclipse-temurin:25-jdk-noble AS builder

# install tools (example for Debian-based Temurin images)
RUN apt-get update && \
    apt-get install -y --no-install-recommends wget tar git ca-certificates && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

ARG MAVEN_VERSION=3.9.11
WORKDIR /opt
RUN wget https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
 && tar -xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz \
 && mv apache-maven-${MAVEN_VERSION} maven \
 && rm apache-maven-${MAVEN_VERSION}-bin.tar.gz
ENV MAVEN_HOME=/opt/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}

WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:25-jdk-noble AS runtime
ARG JAR_FILE=/workspace/target/*.jar
COPY --from=builder ${JAR_FILE} /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]

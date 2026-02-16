FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# install maven
RUN apk add --no-cache maven bash git

# copy only pom first (for caching deps)
COPY pom.xml .

RUN mvn dependency:go-offline

EXPOSE 8080

CMD ["mvn", "spring-boot:run"]

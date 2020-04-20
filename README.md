# JhipsterSampleApplication

Welcome to your new Micronaut-backed JHipster project!

## Running the App locally

Use `./mvnw` to start your server (it will run the `compile` and `exec:exec` goals), then `npm start` to serve the frontend via webpack!

## Testing

Frontend tests are run via `npm test`, and the backend via `./mvnw clean verify`

## Packaging as a FAT jar

Use `./mvnw -Pprod,-dev package` to produce a prod-profiled runnable jar, which you can run via `java -jar target/jhipster-sample-application-0.0.1-SNAPSHOT.jar`

# FX Comparator

Compares FX rates from the [Czech National Bank (CNB)](https://www.cnb.cz/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.txt)  
with the [Frankfurter API](https://frankfurter.dev/).

## Basic Information

The application exposes two secured endpoints (see the OpenAPI documentation for details), protected by **Basic Auth**.  
You can configure the username and password at application startup via provided variables.  
If no credentials are provided, default values will be used: **admin:password**.  
Health check endpoints are publicly accessible and not secured.

## Running the Application

### Prerequisites

You need to have **Docker** installed and available via the command line.

### How to Build and Run the Application

#### Step-by-Step

To build and run the application manually, use the following commands:

1. **Build the application:**  
   `./mvnw clean install`  
   *(or skip tests with:* `./mvnw clean install -DskipTests`*)*

2. **Build the Docker image:**  
   `docker build --platform linux/amd64 -t fx-comparator .`

3. **Run the Docker container:**
   ```bash
   docker run --platform linux/amd64 \
     -p 8080:8080 -p 8088:8088 \
     -e "USERNAME=CUSTOM_USERNAME" \
     -e "PASSWORD=CUSTOM_PASSWORD" \
     --name fx-comparator \
     fx-comparator
   ```
   > Replace `CUSTOM_USERNAME` and `CUSTOM_PASSWORD` with your required credentials.

### Prepared Bash Script

To build and run the application using the prepared bash script, use one of the following options:

- With default username and password:  
  `./build-and-run.sh`

- With custom username and password:  
  `./build-and-run.sh --username=VALUE1 --password=VALUE2`
  > Replace `VALUE1` and `VALUE2` with your desired credentials.

## Health Check

The health check endpoint is **not secured** and is available at:  
`http://localhost:8088/actuator/health`

## OpenAPI Documentation

The OpenAPI documentation is **not secured** and is available at:  
`http://localhost:8080/swagger-ui/index.html`

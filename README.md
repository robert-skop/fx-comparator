# FX Comparator

Compares FX rates from [CNB](https://www.cnb.cz/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.txt)
with [Frankfurter API](https://frankfurter.dev/).

# Basic information

The application has two secured endpoints (see the OpenAPI documentation for details) protected by Basic Auth. You can 
configure the username and password at application startup. If no credentials are provided, default values will be used 
(**admin:password**). Health check endpoints are not secured.

# Docker image build

Use command in project root: `docker build --platform linux/amd64 -t fx-comparator .`

Run docker image (**replace CUSTOM_USERNAME and CUSTOM_PASSWORD with your required credentials**):
`docker run --platform linux/amd64 -p 8080:8080 -p 8088:8088 -e "USERNAME=CUSTOM_USERNAME" -e "PASSWORD=CUSTOM_PASSWORD" --name fx-comparator fx-comparator`

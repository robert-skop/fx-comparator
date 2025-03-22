# FX Comparator
Compares FX rates from [CNB](https://www.cnb.cz/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.txt) with [Frankfurter API](https://frankfurter.dev/).

# Docker image build
Use command in project root: docker build --platform linux/amd64 -t fx-comparator .

Run docker image: docker run --platform linux/amd64 -p 8080:8080 -e "CUSTOM_ENV_PROPERTY1=CUSTOM_VALUE1" -e "CUSTOM_ENV_PROPERTY2=CUSTOM_VALUE2" --name fx-comparator fx-comparator

TODO vystavit este port 8088 pre actuator

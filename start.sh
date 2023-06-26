docker rm employee-service_employee_service_1
docker image rm employee-service_employee_service

./gradlew clean build
docker-compose up
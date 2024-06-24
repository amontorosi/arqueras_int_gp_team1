cd ./arqueras_docker_image_final
docker compose -f ./docker-compose-final.yml down
cd ..
docker compose -f ./docker-compose.yml up --build -d
timeout /t 20 /nobreak
cd ./arqueras_dist
java -jar arqueras_javafx.jar
start http://localhost:3000/
pause
cd ..
docker compose -f ./docker-compose.yml stop
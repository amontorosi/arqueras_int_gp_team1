# Arqueras

Proyecto integrado como propuesta de ampliación para 2ª ordinaria para los ciclos de DAM1 y DAM2.

Los requisitos del proyecto serán facilitados por el tutor y la tutora de cada grupo.

El equipo de desarrollo está compuesto por las siguientes personas:

- Nombre completo
  - [Cuenta en GitHub](enlace_a_github)
  - [Cuenta en DockerHub](enlace_a_dockerhub)

Explicación para 'dockerizar' nuestra base de datos y nuestra aplicación Web.

1. Se crea dos ficheros Dockerfile (Dockerfile-db y Dockerfile-web) que van a generar respectivamente dos imágenes personalizadas con nombre: **'mysql-arqueras:1.0'** y **'node-arqueras:1.0'**.
2. La primera imagen personalizada surge a partir de la imagen 'mysql:8.0-debian'. Esta es una imagen personalizada con nombre 'mysql-arqueras:1.0' que contendrá la base de datos (con los datos oportunos) que necesita nuestra aplicación para empezar.
3. La segunda imagen personalizada surge a partir de la imagen 'node:20.14.0-alpine3.20'. Esta es una imagen personalizada con nombre 'node-arqueras:1.0' que contendrá todos los paquetes Node.js que necesita nuestra aplicación Web y una copia del código fuente de la aplicación Web.
4. El siguente paso, es generar las dos imágenes a partir de los ficheros Dockerfile creados.
   - `docker build -f Dockerfile-db -t mysql-arqueras:1.0 .`
   - `docker build -f Dockerfile-web -t node-arqueras:1.0 .`
   1. De forma voluntaria se pueden alojar las imágenes en Dockerhub. Esto nos permite usarlas directamente con Docker Compose sin necesidad de tener el Dockerfile. En caso contario, si se decide no subir las imágenes a DockerHub, se necesitará construir las imágenes localmente cada vez que se quieran usar con Docker Compose. Previamente se habrán introducido las credenciales con `docker login`:
   - `docker tag mysql-arqueras:latest tu-usuario-dockerhub/mysql-arqueras:1.0`
   - `docker tag node-arqueras:latest tu-usuario-dockerhub/node-arqueras:1.0`
   - `docker push tu-usuario-dockerhub/mysql-arqueras:1.0`
   - `docker push tu-usuario-dockerhub/node-arqueras:1.0`

5. Finalmente, a partir de las imágenes personalizadas, desplegamos nuestra base de datos y nuestra aplicación Web 'dockerizada' haciendo uso del fichero 'docker-compose-final.yml':
   - `docker compose -f ./docker-compose-final.yml down`
   - `docker compose -f ./docker-compose.yml up --build -d`
   - `docker compose ps -a`

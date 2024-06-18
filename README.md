# web-crawler

<h2> Building </h2>

<code>
./gradlew build
</code>
<h2> Generate Docker Image </h2>
<code>
docker build web-crawler-service --file web-crawler-service/Dockerfile --tag web-crawler-service:latest
</code>

<h2> Run Docker Container </h2>
<code>
docker run -dit -p 8080:8080 --name web-crawler-service web-crawler-service:latest
</code>
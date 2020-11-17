cd ..
docker build -t labs-app:1.0-SNAPSHOT . -f k8s/Dockerfile
docker tag labs-app:1.0-SNAPSHOT localhost:5000/labs-app:1.0-SNAPSHOT
docker push localhost:5000/labs-app:1.0-SNAPSHOT
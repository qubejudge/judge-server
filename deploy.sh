echo "Creating Rabbitmq Deployment"
kubectl apply -f rabbitmq-deployment.yaml

echo "Creating Rabbitmq SVC"
kubectl apply -f rabbitmq-service.yaml

echo "Creating MySQL Deployment"
kubectl apply -f mysql-deployment.yaml

echo "Creating MySQL SVC"
kubectl apply -f mysql-service.yaml

echo "Creating Judge Server Deployment"
kubectl apply -f sender/judge-deployment.yaml

echo "Creating Judge Server SVC"
kubectl apply -f sender/judge-service.yaml

echo "Creating Judge Worker Deployment"
kubectl apply -f consumer/worker-deployment.yaml


sam package --s3-bucket s3-simple-lambda --output-template-file out_producer.yaml
sam deploy --template-file out_producer.yaml --capabilities CAPABILITY_IAM --stack-name MyStackProducer
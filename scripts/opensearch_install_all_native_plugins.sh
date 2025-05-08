#!/bin/bash

plugins=(
  'analysis-icu' 'analysis-kuromoji' 'analysis-nori' 'analysis-phonenumber'
  'analysis-phonetic' 'analysis-smartcn' 'analysis-stempel' 'analysis-ukrainian'
  'arrow-flight-rpc' 'cache-ehcache' 'crypto-kms' 'discovery-azure-classic'
  'discovery-ec2' 'discovery-gce' 'identity-shiro' 'ingest-attachment'
  'ingestion-kafka' 'ingestion-kinesis' 'mapper-annotated-text'
  'mapper-murmur3' 'mapper-size' 'repository-azure' 'repository-gcs'
  'repository-hdfs' 'repository-s3' 'store-smb' 'telemetry-otel'
  'transport-grpc' 'transport-reactor-netty4' 'workload-management'
)

for plugin in "${plugins[@]}"; do
  echo "Installing plugin: $plugin"
  ./opensearch-plugin install --batch "$plugin"
done


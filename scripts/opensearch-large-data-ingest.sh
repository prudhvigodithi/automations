#!/bin/bash

OS_URL="http://localhost:9200"
INDEX_NAME="my-index"
CHUNK_SIZE=50000  # Process 50k documents at a time
TEMP_DIR="temp_bulk_files"

# Create temporary directory
mkdir -p "$TEMP_DIR"

# 1. Delete existing index if it exists
echo "Deleting existing index..."
curl -X DELETE "$OS_URL/$INDEX_NAME"
sleep 2

# 2. Create index with multiple shards
echo "Creating index with $SHARD_COUNT shards..."
curl -X PUT "$OS_URL/$INDEX_NAME" -H "Content-Type: application/json" -d '{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 1,
    "number_of_search_only_replicas": 2,
    "replication.type": "SEGMENT",
    "index.max_result_window": 50000
  },
  "mappings": {
    "dynamic": "true",
    "properties": {
      "domainAttributes": {
        "properties": {
          "dueDate": {
            "type": "date",
            "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis||date_optional_time"
          },
          "firmId": {
            "type": "keyword"
          }
        }
      }
    }
  }
}'

sleep 2

# 3. Generate test data in chunks
echo "Generating distributed test data in chunks..."

# Function to generate bulk data
generate_chunk() {
    local start=$1
    local count=$2
    local output_file=$3
    local has_date=$4

    awk -v start="$start" -v count="$count" -v has_date="$has_date" 'BEGIN {
        srand();
        for(i=start; i<start+count; i++) {
            print "{\"index\":{\"_index\":\"'$INDEX_NAME'\",\"routing\":\"" routing_key "\"}}";
            if (has_date == "true") {
                day = (i % 30) + 1;
                date = "2022-03-" sprintf("%02d", day) "T15:40:58.324";
                print "{\"domainAttributes\":{\"firmId\":\"12345678910111213\",\"dueDate\":\"" date "\"}}";
            } else {
                print "{\"domainAttributes\":{\"firmId\":\"12345678910111213\"}}";
            }
        }
    }' > "$output_file"
}

# Generate data without dates in chunks
total_docs=3500000
for ((i=0; i<total_docs; i+=CHUNK_SIZE)); do
    chunk_size=$((CHUNK_SIZE < (total_docs-i) ? CHUNK_SIZE : (total_docs-i)))
    output_file="$TEMP_DIR/chunk_$i.json"
    echo "Generating chunk $i to $((i+chunk_size))..."
    generate_chunk $i $chunk_size "$output_file" "false"
    
    # Index the chunk
    echo "Indexing chunk $i to $((i+chunk_size))..."
    response=$(curl -s -X POST "$OS_URL/_bulk" \
        -H "Content-Type: application/json" \
        --data-binary @"$output_file")
    
    if echo "$response" | grep -q "\"errors\":true"; then
        echo "Error in bulk indexing. Response:"
        echo "$response"
        exit 1
    fi
    
    # Check progress
    curl -s "$OS_URL/$INDEX_NAME/_stats/docs?pretty"
    rm "$output_file"
    sleep 1
done

# Generate data with dates
date_docs=50000
output_file="$TEMP_DIR/dates_chunk.json"
echo "Generating dated documents..."
generate_chunk 0 $date_docs "$output_file" "true"

echo "Indexing dated documents..."
response=$(curl -s -X POST "$OS_URL/_bulk" \
    -H "Content-Type: application/json" \
    --data-binary @"$output_file")

if echo "$response" | grep -q "\"errors\":true"; then
    echo "Error in bulk indexing dates. Response:"
    echo "$response"
    exit 1
fi

# Clean up
rm -rf "$TEMP_DIR"

# Final status check
echo "Final indexing status:"
curl -s "$OS_URL/$INDEX_NAME/_stats/docs?pretty"

echo "Test completed."


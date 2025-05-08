#!/bin/bash

cd "$(dirname "$0")"

START_TIME=$(date '+%Y-%m-%d %H:%M:%S')
echo "Script started at $START_TIME"

JOBS=10
TOTAL_RUNS=100
LOG_DIR="./run_logs"
SUMMARY_FILE="$LOG_DIR/summary.log"

mkdir -p "$LOG_DIR"
rm -f "$SUMMARY_FILE"

export LOG_DIR 

run_test() {
  i=$1
  LOG_FILE="$LOG_DIR/run-$i.log"
  EXIT_FILE="$LOG_DIR/run-$i.exit"

  SEED=$(od -vAn -N8 -tx8 < /dev/urandom | tr -d ' ' | tr 'a-f' 'A-F')
  SEED=$(printf "%016s" "$SEED")

  {
    echo "[$(date '+%H:%M:%S')] Starting run $i with seed $SEED"
    ./gradlew ':server:internalClusterTest' \
      --tests "org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize" \
      -Dtests.seed="$SEED" \
      --init-script disable-xml-reports.gradle
    EXIT_CODE=$?
    echo "[$(date '+%H:%M:%S')] Completed run $i with exit code $EXIT_CODE"
    echo $EXIT_CODE > "$EXIT_FILE"
  } &> "$LOG_FILE"
}

export -f run_test

# Run all in parallel
parallel -j $JOBS run_test ::: $(seq 1 $TOTAL_RUNS)

# Summary reporting
echo -e "\n\nTest Summary (see $SUMMARY_FILE):" | tee -a "$SUMMARY_FILE"
for i in $(seq 1 $TOTAL_RUNS); do
  EXIT_FILE="$LOG_DIR/run-$i.exit"
  if [[ -f "$EXIT_FILE" ]]; then
    CODE=$(<"$EXIT_FILE")
    if [[ $CODE -eq 0 ]]; then
      echo "Run $i succeeded. Log: $LOG_DIR/run-$i.log" >> "$SUMMARY_FILE"
    else
      echo "Run $i FAILED (exit code $CODE). Log: $LOG_DIR/run-$i.log" >> "$SUMMARY_FILE"
    fi
  else
    echo "Run $i did not produce an exit code file. Log: $LOG_DIR/run-$i.log" >> "$SUMMARY_FILE"
  fi
done

END_TIME=$(date '+%Y-%m-%d %H:%M:%S')
echo "Script completed at $END_TIME"
echo "Summary saved in $SUMMARY_FILE"

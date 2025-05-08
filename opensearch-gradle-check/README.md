### Battle testing OpenSearch Gradle Tests to detect flakyness

#### Using GNU parallel CLI with gradle options

See the bash script [battletest_parallel.sh](./parallel/battletest_parallel.sh)

##### Sample output

<details>
  <summary>Tests with failures:</summary>

  <pre><code>
### Main Script output
Script started at 2025-05-09 16:26:05
Test Summary (see ./run_logs/summary.log):
Script completed at 2025-05-09 16:28:24
Summary saved in ./run_logs/summary.log

### Overall Summary 
cat ./run_logs/summary.log


Test Summary (see ./run_logs/summary.log):
Run 1 succeeded. Log: ./run_logs/run-1.log
Run 2 succeeded. Log: ./run_logs/run-2.log
Run 3 succeeded. Log: ./run_logs/run-3.log
Run 4 succeeded. Log: ./run_logs/run-4.log

### Individual Parallel Run Details 

 ls -tlr ./run_logs
total 52
-rw-r--r--. 1 ec2-user ec2-user 5936 May  9 16:27 run-2.log
-rw-r--r--. 1 ec2-user ec2-user    2 May  9 16:27 run-2.exit
-rw-r--r--. 1 ec2-user ec2-user 5936 May  9 16:27 run-1.log
-rw-r--r--. 1 ec2-user ec2-user    2 May  9 16:27 run-1.exit
-rw-r--r--. 1 ec2-user ec2-user 5873 May  9 16:28 run-4.log
-rw-r--r--. 1 ec2-user ec2-user    2 May  9 16:28 run-4.exit
-rw-r--r--. 1 ec2-user ec2-user 5873 May  9 16:28 run-3.log
-rw-r--r--. 1 ec2-user ec2-user    2 May  9 16:28 run-3.exit
-rw-r--r--. 1 ec2-user ec2-user  217 May  9 16:28 summary.log

  </code></pre>

</details>


#### Using gradle options

Following command runs the test 500 times using random seed derived from master seed (esach executions gets a random seed assigned) 

```
./gradlew ':server:internalClusterTest' --tests org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize -Dtests.iters=500 -Dtests.jvms=4 -Dorg.gradle.parallel=true -Dorg.gradle.workers.max=4 
```

```
./gradlew ':server:internalClusterTest' --tests org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize -Dtests.iters=500 -Dtests.jvms=4 -Dorg.gradle.parallel=true -Dorg.gradle.workers.max=4 
```

##### Sample output

<details>
  <summary>Tests with failures:</summary>

  <pre><code>
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:9BEE7026379743AD]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:24E94D1F0AAD504F]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:4BD9BE9E261D5B8C]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:C07E725BA280FA45]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:C806C0FDDB68B9C4]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:B56C587587490267]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:A8BF10F9B29C3C6F]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:419305AE49842028]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:41EC004E78F7D18E]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:CC28E026265CF3FB]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:7943ED3679318928]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:D3F63D66A1289652]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:14CDBB00130BBDDA]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:28DA4BEF8A6F5DDD]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:6C3887C70AE80C0]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:A337CDA67F2BB4A3]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:6278F3698CBE9FB6]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:29D03A30E90D5353]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:C4588CA2867DE75E]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:75504587D764BCEB]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:268529A64FC45A11]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:1BED4FBAB306D4F7]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:F04EB1156D1C746B]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:51B0A8BF0A3C5844]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:5A299443ECB8A8B4]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:2463C014F2CB77DE]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:DF50CB89B7D67281]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:905F43558EBE686D]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:294741AC3FE8CB17]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:4604E6815A1D5CFE]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:F3809802EDEB7C0D]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:4EA534DE2A72845C]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:66C439549F39EA4D]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:D46AC399B276B161]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:63985ED2C029D79]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:DA48DE8A6BFF5651]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:E5C5461207DBD223]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:AAE367C15AC89075]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:AC17C8E8DC002766]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:E52B728CC96CB5DF]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:B2AF405A990A78C8]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:41DB7FF4C5602B1B]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:F05F38FDC4CA0C6A]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:CA27239248183621]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:E2973D1DE6A7F478]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:1F51E6AAA0F084F8]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:AA173DDDEE3463B0]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:4FE78FD6DD512DAE]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:CE52BDE3B5C93C0B]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:EA97DE37B67C5E0C]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:55A87CCE44D2CD63]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:FEA4AFFC06A606F3]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:C92B6862B8C4B31E]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:EED9EF69749929E]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:7ED87DB51E237621]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:B6E31EA67B8DBE1A]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:1E4100526B5600A1]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:4B4A6FBB9605AE8A]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:23DEE2DF791CC024]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:EAB2B395EA0898C]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:5D76EA73BB1F06A4]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:69F4D9036AD4D716]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:E98D92573B62A563]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:E5FD8AF973EC1789]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:E09B286F0967F857]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:AF6CEF4E1C789E84]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:403C8D392CBD74AA]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:7D8B656713F8D813]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:3CA0F4D369AC480]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:C458E87A52003521]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:337D8DF3E0425082]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:33A03F54AE60A2CE]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:DDAEC87F1DD781A]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:A2517CE42A6F152F]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:D1451E25B805399B]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:FA825D5565B4F5B2]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:5A6F81CD3E21F211]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:41FC61373B6AFC51]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:8386F5B7BD6419EE]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"false"} seed=[C12AEFD51C4285BD:F9F95F12319A7089]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:9BEE7026379743AD]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:24E94D1F0AAD504F]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:4BD9BE9E261D5B8C]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:C07E725BA280FA45]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:C806C0FDDB68B9C4]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:B56C587587490267]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:A8BF10F9B29C3C6F]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:419305AE49842028]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:41EC004E78F7D18E]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:CC28E026265CF3FB]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:7943ED3679318928]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:D3F63D66A1289652]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:14CDBB00130BBDDA]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:28DA4BEF8A6F5DDD]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:6C3887C70AE80C0]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:A337CDA67F2BB4A3]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:6278F3698CBE9FB6]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:29D03A30E90D5353]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:C4588CA2867DE75E]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:75504587D764BCEB]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:268529A64FC45A11]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:1BED4FBAB306D4F7]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:F04EB1156D1C746B]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:51B0A8BF0A3C5844]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:5A299443ECB8A8B4]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:2463C014F2CB77DE]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:DF50CB89B7D67281]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:905F43558EBE686D]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:294741AC3FE8CB17]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:4604E6815A1D5CFE]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:F3809802EDEB7C0D]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:4EA534DE2A72845C]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:66C439549F39EA4D]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:D46AC399B276B161]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:63985ED2C029D79]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:DA48DE8A6BFF5651]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:E5C5461207DBD223]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:AAE367C15AC89075]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:AC17C8E8DC002766]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:E52B728CC96CB5DF]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:B2AF405A990A78C8]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:41DB7FF4C5602B1B]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:F05F38FDC4CA0C6A]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:CA27239248183621]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:E2973D1DE6A7F478]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:1F51E6AAA0F084F8]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:AA173DDDEE3463B0]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:4FE78FD6DD512DAE]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:CE52BDE3B5C93C0B]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:EA97DE37B67C5E0C]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:55A87CCE44D2CD63]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:FEA4AFFC06A606F3]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:C92B6862B8C4B31E]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:EED9EF69749929E]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:7ED87DB51E237621]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:B6E31EA67B8DBE1A]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:1E4100526B5600A1]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:4B4A6FBB9605AE8A]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:23DEE2DF791CC024]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:EAB2B395EA0898C]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:5D76EA73BB1F06A4]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:69F4D9036AD4D716]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:E98D92573B62A563]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:E5FD8AF973EC1789]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:E09B286F0967F857]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:AF6CEF4E1C789E84]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:403C8D392CBD74AA]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:7D8B656713F8D813]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:3CA0F4D369AC480]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:C458E87A52003521]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:337D8DF3E0425082]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:33A03F54AE60A2CE]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:DDAEC87F1DD781A]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:A2517CE42A6F152F]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:D1451E25B805399B]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:FA825D5565B4F5B2]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:5A6F81CD3E21F211]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:41FC61373B6AFC51]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:8386F5B7BD6419EE]}
 - org.opensearch.search.simple.SimpleSearchIT.testSimpleTerminateAfterTrackTotalHitsUpToSize {p0={"search.concurrent_segment_search.enabled":"true"} seed=[C12AEFD51C4285BD:F9F95F12319A7089]}
  </code></pre>

</details>


#### Optimizations

Add a file `disable-xml-reports.gradle` in either case to disable the reporting to speed up the runs. Use `--init-script disable-xml-reports.gradle`

```
allprojects {
  tasks.withType(Test).configureEach { 
    reports {
      junitXml.required = false
      html.required = false
    }
  }
}
```
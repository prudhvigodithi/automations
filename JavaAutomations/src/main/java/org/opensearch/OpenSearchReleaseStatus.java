/*
* Java automation to output the current release status of an OpenSearch release, example https://github.com/opensearch-project/opensearch-build/issues/4115#issuecomment-1944768141.
* This automation will help gather all the following Information:
* 1) Outputs the GitHub Search URL for all the existing Integration test failure issues for a given release.
* 2) Build Status of the OpenSerach and OpenSearch Dashboards release candidates.
* 3) Outputs the triggered Integration build of OpenSearch and OpenSearch Dashboards by looping through the top 50 integration tests jobs which are triggered by the upstream distribution build job.
* 4) Summarizes each failed plugin for each executed distribution.
* 5) Outputs pending open PR’s with release label.
* 6) Outputs pending open issues with release label.
* 7) Outputs the docker scan report URL for the generate RC.
* */

package org.opensearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenSearchReleaseStatus {

    public static void main(String[] args) {
        // The OpenSearch release version
        String releaseVersion = "v2.12.0";
        System.out.println("## Release " + releaseVersion + " Current Status");
        System.out.println("### Integration test failure issues:");
        System.out.println("Plugin teams please go through the issues and try to close them as soon as possible");
        System.out.println(String.format("https://github.com/issues?q=is%%3Aopen+is%%3Aissue+user%%3Aopensearch-project+label%%3A%s+label%%3Aautocut+%%5BAUTOCUT%%5D+in%%3Atitle+", releaseVersion));
        String jenkinsUrl = "https://build.ci.opensearch.org/";
        String dockerScanJob = "docker-scan";
        // The OpenSearch Integ test job name
        String openSearchIntegJobName = "integ-test";
        // The OpenSearch Distribution job name
        String upstreamOpenSearchJob = "distribution-build-opensearch";
        // The OpenSearch identified release candidate
        int openSearchBuildNumber = 9414;
        // The most recent 50 builds to check which are triggered by the upstream Distribution build
        int maxRecentBuildsToCheck = 50;
        System.out.println("### OpenSearch");
        buildInformation(jenkinsUrl, openSearchIntegJobName, maxRecentBuildsToCheck, openSearchBuildNumber, upstreamOpenSearchJob);
        System.out.println("### Docker Scan Results");
        extractAndSearchDockerScan(jenkinsUrl, upstreamOpenSearchJob, openSearchBuildNumber);
        // The OpenSearch Dashboards Integ test job name
        String openSearchDashboardsIntegJobName = "integ-test-opensearch-dashboards";
        // The OpenSearch Dashboards Distribution job name
        String upstreamOpenSearchDashboardsJob = "distribution-build-opensearch-dashboards";
        // The OpenSearch Dashboards identified release candidate
        int openSearchDashboardsBuildNumber = 7286;
        System.out.println("### OpenSearch Dashboards");
        buildInformation(jenkinsUrl, openSearchDashboardsIntegJobName, maxRecentBuildsToCheck, openSearchDashboardsBuildNumber, upstreamOpenSearchDashboardsJob);
        System.out.println("### Docker Scan Results");
        extractAndSearchDockerScan(jenkinsUrl, upstreamOpenSearchDashboardsJob, openSearchDashboardsBuildNumber);
        System.out.println("### Pending PR’s with release label");
        System.out.println(String.format("https://github.com/issues?q=is%%3Aopen+is%%3Apr+user%%3Aopensearch-project+label%%3A%s+", releaseVersion));
        System.out.println("### Pending Issues with release label");
        System.out.println(String.format("https://github.com/issues?q=is%%3Aopen+is%%3Aissue+user%%3Aopensearch-project+label%%3A%s+", releaseVersion));
    }

    private static void buildInformation(String jenkinsUrl, String jobName, int maxRecentBuildsToCheck, int searchBuildNumber, String upstreamJob) {
        try {
            System.out.println(getJobResult(jenkinsUrl, upstreamJob, searchBuildNumber));
            // Get the most recent build number
            int recentBuildNumber = getRecentBuildNumber(jenkinsUrl, jobName);

            // Retrieve recent builds
            for (int i = recentBuildNumber; i >= Math.max(1, recentBuildNumber - maxRecentBuildsToCheck + 1); i--) {
                int buildNumber = i;
                String buildUrl = jenkinsUrl + "job/" + jobName + "/" + buildNumber + "/";

                // Check if the build is started by the specified upstream project and build number
                boolean isMatchingBuild = checkBuild(jenkinsUrl, jobName, buildNumber, searchBuildNumber, upstreamJob);

                if (isMatchingBuild) {
                    // Extract and print failed stages
                    extractFailedStages(jenkinsUrl, jobName, buildNumber);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getJobResult(String jenkinsUrl, String jobName, int searchBuildNumber) {
        try {
            String apiUrl = jenkinsUrl + "job/" + jobName + "/" + searchBuildNumber + "/api/json?tree=result";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            String finalString = "The distribution build " + jenkinsUrl + "/blue/organizations/jenkins/" + jobName + "/detail/" + jobName + "/" + searchBuildNumber + "/pipeline" + " " + "is " + "**" + jsonObject.getString("result") + "**";
            return finalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static int getRecentBuildNumber(String jenkinsUrl, String jobName) throws Exception {
        String apiUrl = jenkinsUrl + "/job/" + jobName + "/api/json?tree=lastBuild[number]";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonObject = new JSONObject(response.toString());
        JSONObject lastBuild = jsonObject.getJSONObject("lastBuild");
        return lastBuild.getInt("number");
    }

    private static int getRecentBuildStatus(String jenkinsUrl, String jobName) throws Exception {
        String apiUrl = jenkinsUrl + "/job/" + jobName + "/api/json?tree=lastBuild[number]";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonObject = new JSONObject(response.toString());
        JSONObject lastBuild = jsonObject.getJSONObject("lastBuild");
        return lastBuild.getInt("number");
    }

    private static boolean checkBuild(String jenkinsUrl, String jobName, int buildNumber, int searchBuildNumber, String upstreamJob) throws Exception {
        String apiUrl = String.format("%s/job/%s/%d/api/json?tree=actions[causes[*]]", jenkinsUrl, jobName, buildNumber);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(apiUrl).openStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray actions = jsonObject.getJSONArray("actions");

            return IntStream.range(0, actions.length())
                    .mapToObj(actions::getJSONObject)
                    .filter(action -> action.has("causes"))
                    .map(action -> action.getJSONArray("causes"))
                    .flatMap(causes -> IntStream.range(0, causes.length())
                            .mapToObj(causes::getJSONObject)
                            .filter(cause -> cause.has("upstreamProject") && cause.has("upstreamBuild")))
                    .anyMatch(cause -> {
                        String upstreamProject = cause.optString("upstreamProject");
                        int upstreamBuild = cause.optInt("upstreamBuild");
                        return upstreamProject.equals(upstreamJob) && upstreamBuild == searchBuildNumber;
                    });
        }
    }

    private static void extractFailedStages(String jenkinsUrl, String jobName, int buildNumber) throws Exception {
        String apiUrl = jenkinsUrl + "/job/" + jobName + "/" + buildNumber + "/api/json?tree=description";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(response.toString());
        String description = json.optString("description");
        String[] parts = description.split(",");
        String buildUrl = jenkinsUrl + "/blue/organizations/jenkins/" + jobName + "/detail/" + jobName + "/" + buildNumber + "/pipeline";
        String distribution = parts.length > 5 && !parts[5].isEmpty() ? parts[2].trim() + " " + parts[5].trim() : parts[2].trim() + " " + parts[4].trim();

        // Print Build URL
        System.out.println("- Integ Test Build URL: " + buildUrl + " ");

        // Print Distribution
        System.out.println("  Distribution Type: " + "**" + distribution + "**" + " ");

        // Fetch failed stages
        apiUrl = jenkinsUrl + "/job/" + jobName + "/" + buildNumber + "/wfapi/describe";
        url = new URL(apiUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        json = new JSONObject(response.toString());
        JSONArray stages = json.getJSONArray("stages");

        StringBuilder failedStagesBuilder = new StringBuilder();
        for (int i = 0; i < stages.length(); i++) {
            JSONObject stage = stages.getJSONObject(i);
            String status = stage.getString("status");

            if (status.equals("FAILED")) {
                String stageName = stage.getString("name");
                if (failedStagesBuilder.length() > 0) {
                    failedStagesBuilder.append(", ");
                }
                failedStagesBuilder.append(stageName);
            }
        }

        // Print Failed Stages
        System.out.println("  Failed Components: " + "**" + failedStagesBuilder.toString() + "**" + " ");
    }

    private static void extractAndSearchDockerScan(String jenkinsUrl, String jobName, int buildNumber) {
        try {
            String logUrl = jenkinsUrl + "job/" + jobName + "/" + buildNumber + "/consoleText";
            URL url = new URL(logUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder consoleLogBuilder = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    consoleLogBuilder.append(line).append("\n");
                }
                String consoleLog = consoleLogBuilder.toString();
                String searchString = "Build docker-scan \\#(\\d+)";
                Pattern pattern = Pattern.compile(searchString);
                Matcher matcher = pattern.matcher(consoleLog);
                if (matcher.find()) {
                    String dockerScanNumber = matcher.group(1);
                     System.out.println("The docker scan URL is " + jenkinsUrl + "job/" + "docker-scan" + "/" + dockerScanNumber + "/artifact/scan_docker_image.txt");
                } else {
                    System.out.println("String not found in log.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

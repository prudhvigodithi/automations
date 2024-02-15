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

public class OpenSearchReleaseStatus {

    public static void main(String[] args) {
        String releaseVersion = "v2.12.0";
        System.out.println("## Release " + releaseVersion + " Current Status");
        System.out.println("### Integration test issues:");
        System.out.println("Plugin teams please go through the issues and try to close them as soon as possible");
        System.out.println(String.format("https://github.com/issues?q=is%%3Aopen+is%%3Aissue+user%%3Aopensearch-project+label%%3A%s+label%%3Aautocut+%%5BAUTOCUT%%5D+in%%3Atitle+", releaseVersion));
        String jenkinsUrl = "https://build.ci.opensearch.org/";
        String openSearchIntegJobName = "integ-test";
        String upstreamOpenSearchJob = "distribution-build-opensearch";
        int openSearchBuildNumber = 9414;
        int maxRecentBuildsToCheck = 50;
        System.out.println("### OpenSearch");
        buildInformation(jenkinsUrl, openSearchIntegJobName, maxRecentBuildsToCheck, openSearchBuildNumber, upstreamOpenSearchJob);
        String openSearchDashboardsIntegJobName = "integ-test-opensearch-dashboards";
        String upstreamOpenSearchDashboardsJob = "distribution-build-opensearch-dashboards";
        int openSearchDashboardsBuildNumber = 7286;
        System.out.println("### OpenSearch Dashboards");
        buildInformation(jenkinsUrl, openSearchDashboardsIntegJobName, maxRecentBuildsToCheck, openSearchDashboardsBuildNumber, upstreamOpenSearchDashboardsJob);
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
        System.out.println("- Integ Test Build URL: " + buildUrl);

        // Print Distribution
        System.out.println("Distribution Type: " + "**" + distribution + "**");

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
        System.out.println("Failed Components: " + "**" + failedStagesBuilder.toString() + "**");
    }



}

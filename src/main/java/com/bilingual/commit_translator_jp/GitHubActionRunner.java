package com.bilingual.commit_translator_jp;

import com.bilingual.commit_translator_jp.service.GeminiService;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GitHubActionRunner {
    public static void main(String[] args) {
        try {
            // Check for required arguments
            if (args.length < 2) {
                System.err.println("::error::GEMINI_API_KEY or GITHUB_TOKEN is missing. Please check your repo secrets.");
                System.exit(1);
            }

            String geminiKey = args[0];
            String githubToken = args[1];

            // 1. Get the PR data from the environment
            String eventPath = System.getenv("GITHUB_EVENT_PATH");
            if (eventPath == null) {
                System.err.println("::error::GITHUB_EVENT_PATH not found. This action must run in a GitHub Workflow.");
                System.exit(1);
            }
            
            String content = new String(Files.readAllBytes(Paths.get(eventPath)));
            JSONObject eventJson = new JSONObject(content);
            JSONObject pr = eventJson.getJSONObject("pull_request");

            // 2. Extract Data with Fallbacks
            // .optString handles cases where the field might be missing/null without crashing
            String prBody = pr.optString("body", "").trim();
            String prTitle = pr.optString("title", "").trim();
            String commentsUrl = pr.getString("comments_url");

            String textToTranslate = "";
            String instruction = "";

            // --- EDGE CASE LOGIC ---
            if (prBody.isEmpty()) {
                if (prTitle.isEmpty()) {
                    // Scenario: No Title and No Description
                    System.out.println("No content found to translate.");
                    postComment(commentsUrl, "⚠️ **Notice:** No title or description was provided for this PR. Please add details for a better summary.", githubToken);
                    return; 
                } else {
                    // Scenario: Title exists, but Description is empty
                    System.out.println("Description empty, translating title only.");
                    textToTranslate = prTitle;
                    instruction = "The user provided no description. Please translate this Pull Request title into professional Japanese Keigo: ";
                }
            } else {
                // Scenario: Normal operation
                textToTranslate = prBody;
                instruction = "Please summarize the following Pull Request description in professional Japanese Keigo: ";
            }

            // 3. Initialize Service and Get Translation
            GeminiService service = new GeminiService();
            service.setApiKey(geminiKey);

            // Passing the instruction + content to ensure Gemini knows what to do
            String japaneseSummary = service.getJapaneseSummary(instruction + "\n\n" + textToTranslate);

            // 4. Post the comment back to GitHub
            postComment(commentsUrl, "### Japanese PR Summary (Keigo)\n" + japaneseSummary, githubToken);

        } catch (Exception e) {
            System.err.println("::error::An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Helper method to handle posting comments to GitHub with error checking
     */
    private static void postComment(String url, String body, String token) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        JSONObject commentJson = new JSONObject();
        commentJson.put("body", body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(commentJson.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            System.out.println("Success! Action completed successfully.");
        } else {
            System.err.println("::error::GitHub API Error. Status: " + response.statusCode());
            System.err.println("Response Body: " + response.body());
            System.exit(1);
        }
    }
}
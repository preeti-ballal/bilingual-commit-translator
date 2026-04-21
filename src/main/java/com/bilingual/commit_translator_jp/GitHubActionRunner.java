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
            if (args.length < 2) {
                System.err.println("Error: Missing Gemini Key or GitHub Token");
                System.exit(1);
            }

            String geminiKey = args[0];
            String githubToken = args[1];

            // 1. Get the PR data from the environment
            String eventPath = System.getenv("GITHUB_EVENT_PATH");
            if (eventPath == null) {
                System.err.println("Error: GITHUB_EVENT_PATH not found");
                System.exit(1);
            }
            
            String content = new String(Files.readAllBytes(Paths.get(eventPath)));
            JSONObject eventJson = new JSONObject(content);

            // 2. Extract PR Body and Comments URL
            String prBody = eventJson.getJSONObject("pull_request").getString("body");
            String commentsUrl = eventJson.getJSONObject("pull_request").getString("comments_url");

            // 3. Initialize Service and Set the API Key
            GeminiService service = new GeminiService();
            service.setApiKey(geminiKey); // Setting the key manually for the GHA

            String japaneseSummary = service.getJapaneseSummary(prBody);

            // 4. Post the comment back to GitHub
            HttpClient client = HttpClient.newHttpClient();
            JSONObject commentJson = new JSONObject();
            commentJson.put("body", "### 🇯🇵 PR Summary (Keigo)\n" + japaneseSummary);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(commentsUrl))
                    .header("Authorization", "Bearer " + githubToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(commentJson.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 201) {
                System.out.println("Success! Japanese summary posted to PR.");
            } else {
                System.err.println("GitHub API Error: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
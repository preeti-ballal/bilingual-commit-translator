package com.bilingual.commit_translator_jp.service;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Part;
import com.google.genai.types.GenerateContentResponse;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    // @Value("${api.gemini.key}")
    // private String apiKey;

    // Inside GeminiService.java
    private String apiKey;

    // Add this constructor
    public GeminiService() {
        // If running in Spring, it uses @Value. 
        // For the Action, we will set it manually in the Runner.
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getJapaneseSummary(String englishText) {
        // 1. Setup the Client
        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        // 2. Prepare the prompt
        String prompt = "Please summarize the following GitHub Pull Request changes into professional business Japanese (Keigo). Use bullet points. \n\n" + englishText;

        try {
            // 3. Call Gemini 3 Flash

            Content content = Content.builder()
                                .parts(Collections.singletonList(Part.builder()
                                    .text("You are an expert software engineer. Translate GitHub commit messages into professional business Japanese (Keigo). Use bullet points.")
                                    .build()))
                                .build();

            GenerateContentConfig config = GenerateContentConfig.builder()
                .systemInstruction(content)
                .build();

            GenerateContentResponse response = client.models.generateContent("gemini-3-flash-preview", prompt, config);
            
            // 4. Return just the text
            return response.text();
        } catch (Exception e) {
            return "Error using Google SDK: " + e.getMessage();
        }

    }

    
}

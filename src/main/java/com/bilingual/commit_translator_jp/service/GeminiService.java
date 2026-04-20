package com.bilingual.commit_translator_jp.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    public String getJapaneseSummary(String englishText) {
        // 1. Setup the Client
        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        // 2. Prepare the prompt
        String prompt = "Please summarize the following GitHub Pull Request changes into professional business Japanese (Keigo). Use bullet points. \n\n" + englishText;

        try {
            // 3. Call Gemini 3 Flash
            GenerateContentResponse response = client.models.generateContent("gemini-3-flash-preview", prompt, null);
            
            // 4. Return just the text
            return response.text();
        } catch (Exception e) {
            return "Error using Google SDK: " + e.getMessage();
        }

    }

    
}

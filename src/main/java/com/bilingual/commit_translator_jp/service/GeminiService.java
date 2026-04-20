package com.bilingual.commit_translator_jp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public String getJapaneseSummary(String englishText){
        RestTemplate restTemplate = new RestTemplate();

        String fullUrl = apiUrl + "?key=" + apiKey;
        
        String prompt = "Please summarize the following GitHub Pull Request changes into professional business Japanese (Keigo). Use bullet points. \n\n" + englishText;

        // Creating the request body for Gemini
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", prompt);

        Map<String, Object> parts = new HashMap<>();
        parts.put("parts", Collections.singletonList(textPart));

        Map<String, Object> body = new HashMap<>();
        body.put("contents", Collections.singletonList(parts));

        // Sending the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(fullUrl, entity, Map.class);
            return response.getBody().toString();
        } catch (Exception e) {
            return "Error calling Gemini: " + e.getMessage();
        }

    }

    
}

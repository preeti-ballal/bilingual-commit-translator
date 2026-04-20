package com.bilingual.commit_translator_jp.controller;

import com.bilingual.commit_translator_jp.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslatorController {

    @Autowired
    private GeminiService geminiService;

    // URL: http://localhost:8080/translate?text=Hello
    @GetMapping("/translate")
    public String translate(@RequestParam String text) {
        return geminiService.getJapaneseSummary(text);
    }
    
}

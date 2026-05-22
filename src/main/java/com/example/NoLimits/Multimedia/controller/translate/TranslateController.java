package com.example.NoLimits.Multimedia.controller.translate;

import com.example.NoLimits.Multimedia.dto.translate.TranslateRequest;
import com.example.NoLimits.Multimedia.dto.translate.TranslateResponse;
import com.example.NoLimits.Multimedia.service.translate.TranslateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/translate")
public class TranslateController {

    private final TranslateService translateService;

    public TranslateController(TranslateService translateService) {
        this.translateService = translateService;
    }

    @PostMapping
    public ResponseEntity<TranslateResponse> translate(@RequestBody TranslateRequest request) {
        String translated = translateService.translateToSpanish(request.text());
        return ResponseEntity.ok(new TranslateResponse(translated));
    }
}
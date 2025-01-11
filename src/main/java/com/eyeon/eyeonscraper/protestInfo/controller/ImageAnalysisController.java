package com.eyeon.eyeonscraper.protestInfo.controller;

import com.eyeon.eyeonscraper.protestInfo.dto.ImageAnalysisResponse;
import com.eyeon.eyeonscraper.protestInfo.service.ImageAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@RestController
@RequestMapping("/api/image-analysis")
@RequiredArgsConstructor
public class ImageAnalysisController {

    private final ImageAnalysisService imageAnalysisService;

    @GetMapping
    public ResponseEntity<ImageAnalysisResponse> analyzeImage() {
        try {
            // ClassPathResource를 사용하여 리소스 폴더의 이미지에 접근
            String imagePath = new ClassPathResource("static/images/protest-image.jpg").getFile().getPath();
            String analysis = imageAnalysisService.analyzeImage(imagePath);

            ImageAnalysisResponse response = new ImageAnalysisResponse();
            response.setAnalysis(analysis);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error analyzing image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ImageAnalysisResponse());
        }
    }
}

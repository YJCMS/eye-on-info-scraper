package com.eyeon.eyeonscraper.protestInfo.controller;

import com.eyeon.eyeonscraper.protestInfo.service.ImageAnalysisService;
import com.eyeon.eyeonscraper.protestInfo.service.ProtestCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping()
public class ProtestCrawlerController {

    private final ProtestCrawlerService crawlerService;
    
    @GetMapping("/crawl")
    public ResponseEntity<String> crawlImages() {
        try {
            crawlerService.crawlPost();
            return ResponseEntity.ok("크롤링이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("크롤링 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
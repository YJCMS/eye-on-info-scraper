package com.eyeon.eyeonscraper.protestInfo.scheduler;

import com.eyeon.eyeonscraper.protestInfo.service.ImageAnalysisService;
import com.eyeon.eyeonscraper.protestInfo.service.ProtestCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

@Component
@RequiredArgsConstructor
public class ProtestInfoPostScheduler {
    private final ProtestCrawlerService protestCrawlerService;
    private final ImageAnalysisService imageAnalysisService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    String url = ""; // 컨트롤러와 동일한 URL

    @Scheduled(cron = "0 08 19 * * *")  // test 시간 설정
    public void scheduledCrawling() {
        try {
            protestCrawlerService.crawlPost(url);
            log.info("스케줄된 크롤링이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            log.error("스케줄된 크롤링 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 09 19 * * *")  // test 시간 설정
    public void scheduledImageAnalysis() {
        try {
            String imagePath = new ClassPathResource("static/images/protest-image.jpg").getFile().getPath();
            String analysis = imageAnalysisService.analyzeImage(imagePath);

            log.info("이미지 분석이 성공적으로 완료되었습니다. 분석 결과: {}", analysis);
        } catch (Exception e) {
            log.error("이미지 분석 중 오류 발생: " + e.getMessage(), e);
        }
    }

}

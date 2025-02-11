package com.eyeon.eyeonscraper.protestInfo.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;


@Slf4j
@Service
public class ProtestCrawlerService {

    // application.properties에서 설정하거나 직접 경로 지정
    @Value("${spring.webserver.content-path:src/main/resources/static/images}")
    private String imagePath;

    public void crawlPost(String url) {
        try {

            //
            Document postDoc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .timeout(5000)
                    .get();

            // 이미지 요소 선택
            Elements imageElements = postDoc.select("div.reply-content img");

            // 저장된 이미지 URL 목록
            List<String> savedImageUrls = new ArrayList<>();

            // 이미지 다운로드 및 저장
            for (Element img : imageElements) {
                String imageUrl = img.attr("abs:src");
                String fileName = "protest-image";
                String savedPath = saveImage(imageUrl, fileName);
                if (savedPath != null) {
                    savedImageUrls.add("/images/" + fileName); // 웹 접근 경로 저장
                }
            }

            log.info("저장된 이미지 수: {}", savedImageUrls.size());

        } catch (IOException e) {
            log.error("크롤링 중 오류 발생: ", e);
            throw new RuntimeException("크롤링 실패", e);
        }
    }

    private String saveImage(String imageUrl, String fileName) {
        try {
            // 저장 디렉토리 생성
            File directory = new File(imagePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 파일명이 .jpg로 끝나도록 수정
            if (!fileName.toLowerCase().endsWith(".jpg")) {
                fileName = fileName.replaceAll("\\.[^.]*$", "") + ".jpg";
            }

            // 파일 저장 경로
            String filePath = imagePath + File.separator + fileName;

            // 이미지 다운로드 및 JPG로 변환하여 저장
            URL url = new URL(imageUrl);
            try (InputStream in = url.openStream()) {
                // 이미지를 버퍼드이미지로 읽기
                BufferedImage originalImage = ImageIO.read(in);
                if (originalImage != null) {
                    // RGB로 변환 (알파 채널 제거)
                    BufferedImage jpgImage = new BufferedImage(
                            originalImage.getWidth(),
                            originalImage.getHeight(),
                            BufferedImage.TYPE_INT_RGB
                    );
                    jpgImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);

                    // JPG 형식으로 저장
                    ImageIO.write(jpgImage, "jpg", new File(filePath));
                    log.info("이미지 저장 완료: {}", filePath);
                    return filePath;
                } else {
                    log.error("이미지를 읽을 수 없습니다: {}", imageUrl);
                    return null;
                }
            }
        } catch (IOException e) {
            log.error("이미지 저장 실패: {}", imageUrl, e);
            return null;
        }
    }
}
package com.eyeon.eyeonscraper.protestInfo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ImageAnalysisService {
    @Autowired
    private ProtestInfoPostService protestInfoPostService;

    @Value("${anthropic.api-key}")
    private String apiKey;

    private final String promptTemplate;

    private static final String API_URL = "https://api.anthropic.com/v1/messages";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public ImageAnalysisService() {
        // 생성자에서 프롬프트 파일 로드
        try {
            this.promptTemplate = new String(
                    getClass().getResourceAsStream("/prompts/protest-analysis-prompt.txt")
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            log.error("Failed to load prompt template", e);
            throw new RuntimeException("Failed to load prompt template", e);
        }
    }

    public String analyzeImage(String imagePath) throws IOException {
        log.info("Starting image analysis for path: {}", imagePath);

        // 이미지 파일 읽기
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            String errorMsg = "Image file not found: " + imagePath;
            log.error(errorMsg);
            throw new IOException(errorMsg);
        }

        // 이미지를 base64로 인코딩
        byte[] fileContent = FileUtils.readFileToByteArray(imageFile);
        String base64Image = Base64.getEncoder().encodeToString(fileContent);
        log.info("Successfully encoded image to base64");

        // API 요청 본문 생성, claude-3-sonnet-20240229
        JSONObject requestBody = new JSONObject()
                .put("model", "claude-3-5-sonnet-20241022")
                .put("max_tokens", 4096)
                .put("messages", new JSONObject[]{
                        new JSONObject()
                                .put("role", "user")
                                .put("content", new JSONObject[]{
                                new JSONObject()
                                        .put("type", "text")
                                        .put("text", promptTemplate),
                                new JSONObject()
                                        .put("type", "image")
                                        .put("source", new JSONObject()
                                        .put("type", "base64")
                                        .put("media_type", "image/jpeg")
                                        .put("data", base64Image))
                        })
                });

        log.info("Prepared request body for Anthropic API");

        // API 요청 생성
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("x-api-key", apiKey)
                .addHeader("anthropic-version", "2023-06-01")
                .addHeader("content-type", "application/json")
                .post(RequestBody.create(requestBody.toString(), JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : null;

            if (!response.isSuccessful()) {
                log.error("API Error - Status: {}, Body: {}", response.code(), responseBody);
                throw new IOException("Unexpected response code: " + response.code());
            }

            JSONObject jsonResponse = new JSONObject(responseBody);
            String aiResponse = jsonResponse.getJSONArray("content")
                    .getJSONObject(0)
                    .getString("text");

            protestInfoPostService.sendProtestData(aiResponse);

            return aiResponse;
        }
    }
}

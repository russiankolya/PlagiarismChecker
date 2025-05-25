package org.example.fileanalysis.service;

import org.example.fileanalysis.entity.AnalysisResult;
import org.example.fileanalysis.repository.AnalysisResultRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpHeaders;

@Service
public class FileAnalysisService {

    private final AnalysisResultRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${remote.filestorage-service.base-url}")
    private String fileStorageBaseUrl;

    @Value("${analysis.wordcloud-path}")
    private String wordCloudPath;

    public FileAnalysisService(AnalysisResultRepository repository) {
        this.repository = repository;
    }

    public AnalysisResult analyze(Long fileId) {
        return repository.findById(fileId).orElseGet(() -> {
            try {
                String fileContent = restTemplate.getForObject(fileStorageBaseUrl + "/api/storage/" + fileId, String.class);
                var result = performAnalysis(fileContent, fileId);
                return repository.save(result);
            } catch (IOException ex) {
                throw new RuntimeException("Analysis failed", ex);
            }
        });
    }

    private AnalysisResult performAnalysis(String text, Long fileId) throws IOException {
        var wordCount = text.split("\\s+").length;
        var symbolCount = text.length();
        var lineCount = text.split("\r\n|\r|\n").length;
        var result = new AnalysisResult();
        result.setFileId(fileId);
        result.setWordCount(wordCount);
        result.setSymbolCount(symbolCount);
        result.setLineCount(lineCount);
        return result;
    }
}

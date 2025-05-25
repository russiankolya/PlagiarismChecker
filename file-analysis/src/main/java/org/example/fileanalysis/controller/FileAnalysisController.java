package org.example.fileanalysis.controller;

import org.example.fileanalysis.entity.AnalysisResult;
import org.example.fileanalysis.service.FileAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class FileAnalysisController {

    private final FileAnalysisService service;

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisResult> analyze(@PathVariable Long id) {
        AnalysisResult result = service.analyze(id);
        return ResponseEntity.ok(result);
    }
}
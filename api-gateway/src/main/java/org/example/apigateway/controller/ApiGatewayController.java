
package org.example.apigateway.controller;

import org.example.apigateway.service.ApiGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ApiGatewayController {
    @Autowired
    private ApiGatewayService apiGatewayService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return apiGatewayService.forwardFileUpload(file);
    }

    @PostMapping("/{id}/analyze")
    public ResponseEntity<String> analyze(@PathVariable Long id) {
        return apiGatewayService.forwardAnalyze(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id) {
        return apiGatewayService.forwardGetFile(id);
    }
}
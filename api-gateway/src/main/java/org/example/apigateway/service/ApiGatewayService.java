package org.example.apigateway.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


class MultipartInputStreamFileResource extends InputStreamResource {
    private final String filename;

    public MultipartInputStreamFileResource(MultipartFile multipartFile) throws IOException {
        super(multipartFile.getInputStream());
        this.filename = multipartFile.getOriginalFilename();
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() {
        return -1;
    }
}

@Service
public class ApiGatewayService {

    @Value("${remote.filestorage-service.base-url}")
    private String storageServiceBaseUrl;
    @Value("${remote.analysis-service.base-url}")
    private String analysisServiceBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<Long> forwardFileUpload(MultipartFile file) throws IOException {
        String url = storageServiceBaseUrl + "/api/storage/upload";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(url, requestEntity, Long.class);
    }

    public ResponseEntity<String> forwardAnalyze(Long id) {
        String url = analysisServiceBaseUrl + "/api/analysis/" + id ;
        return restTemplate.getForEntity(url, String.class);
    }

    public ResponseEntity<Resource> forwardGetFile(Long id) {
        String url = storageServiceBaseUrl + "/api/storage/" + id;
        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
        Resource resource = new ByteArrayResource(response.getBody());

        return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).contentType(MediaType.TEXT_PLAIN).body(resource);
    }
}

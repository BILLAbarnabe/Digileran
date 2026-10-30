package com.odc.digilearn.modules.certificate.web;

import com.odc.digilearn.modules.certificate.service.CertificateService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/verify/{code}")
    public ResponseEntity<String> verify(@PathVariable UUID code) {
        return ResponseEntity.ok("Certificate verification code: " + code);
    }

    @GetMapping("/example/{learnerName}/{trainingName}")
    public ResponseEntity<byte[]> generate(@PathVariable String learnerName, @PathVariable String trainingName) {
        var certificate = certificateService.generate(learnerName, trainingName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("certificat.pdf").build());
        return ResponseEntity.ok().headers(headers).body(certificate.pdf());
    }
}

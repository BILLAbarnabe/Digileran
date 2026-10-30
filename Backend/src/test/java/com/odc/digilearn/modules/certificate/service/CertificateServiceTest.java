package com.odc.digilearn.modules.certificate.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CertificateServiceTest {
    @Test
    void createsPdfWithPublicVerificationUrlAndUuid() {
        CertificateService service = new CertificateService("https://digilearn.example/verify");

        var certificate = service.generate("Awa Traore", "Java Backend");

        assertThat(certificate.pdf()).startsWith(new byte[]{37, 80, 68, 70});
        assertThat(certificate.verificationUrl()).startsWith("https://digilearn.example/verify/");
        assertThat(certificate.verificationCodeText()).isEqualTo(certificate.verificationCode().toString());
    }
}
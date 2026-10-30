package com.odc.digilearn.modules.certificate.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.MultiFormatWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class CertificateService {
    private final String publicVerificationUrl;

    public CertificateService(@Value("${app.certificate.public-verification-url}") String publicVerificationUrl) {
        this.publicVerificationUrl = publicVerificationUrl;
    }

    public GeneratedCertificate generate(String learnerName, String trainingName) {
        UUID code = UUID.randomUUID();
        String verificationUrl = publicVerificationUrl + "/" + code;
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(verificationUrl, BarcodeFormat.QR_CODE, 180, 180);
            ByteArrayOutputStream qrBytes = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", qrBytes);

            ByteArrayOutputStream pdfBytes = new ByteArrayOutputStream();
            Document document = new Document(new Rectangle(842, 595));
            PdfWriter.getInstance(document, pdfBytes);
            document.open();
            document.add(new Paragraph("CERTIFICAT DE REUSSITE"));
            document.add(new Paragraph("Attribue a : " + learnerName));
            document.add(new Paragraph("Formation : " + trainingName));
            document.add(new Paragraph("Code de verification : " + code));
            Image qr = Image.getInstance(qrBytes.toByteArray());
            document.add(qr);
            document.close();
            return new GeneratedCertificate(code, verificationUrl, pdfBytes.toByteArray());
        } catch (Exception exception) {
            throw new IllegalStateException("Impossible de générer le certificat", exception);
        }
    }

    public record GeneratedCertificate(UUID verificationCode, String verificationUrl, byte[] pdf) {
        public String verificationCodeText() { return verificationCode.toString(); }
    }
}

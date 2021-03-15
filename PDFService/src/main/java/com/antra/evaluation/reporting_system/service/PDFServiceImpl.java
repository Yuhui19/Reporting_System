package com.antra.evaluation.reporting_system.service;

import com.amazonaws.services.s3.AmazonS3;
import com.antra.evaluation.reporting_system.pojo.api.PDFRequest;
import com.antra.evaluation.reporting_system.pojo.report.PDFFile;
import com.antra.evaluation.reporting_system.repo.PDFRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PDFServiceImpl implements PDFService {

    private static final Logger log = LoggerFactory.getLogger(PDFServiceImpl.class);

    private final PDFRepository repository;

    private final PDFGenerator generator;

    private final AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String s3Bucket;

    public PDFServiceImpl(PDFRepository repository, PDFGenerator generator, AmazonS3 s3Client) {
        this.repository = repository;
        this.generator = generator;
        this.s3Client = s3Client;
    }

    @Override
    public PDFFile createPDF(final PDFRequest request) {
        PDFFile file = new PDFFile();
        file.setId("File-" + UUID.randomUUID().toString());
        file.setSubmitter(request.getSubmitter());
        file.setDescription(request.getDescription());
        file.setGeneratedTime(LocalDateTime.now());

        PDFFile generatedFile= generator.generate(request);

        File temp = new File(generatedFile.getFileLocation());
        log.debug("Upload temp file to s3 {}", generatedFile.getFileLocation());
        s3Client.putObject(s3Bucket,file.getId(),temp);
        log.debug("Uploaded");

        file.setFileLocation(String.join("/",s3Bucket,file.getId()));
        file.setFileSize(generatedFile.getFileSize());
        file.setFileName(generatedFile.getFileName());
        repository.saveFile(file);

        log.debug("clear tem file {}", file.getFileLocation());
        if(temp.delete()){
            log.debug("cleared");
        }

        return file;
    }

    @Override
    public InputStream getPdfBodyById(String id) throws FileNotFoundException {
        PDFFile pdfFile = repository.getFileById(id);
        String fileLocation = pdfFile.getFileLocation();
        String bucket = fileLocation.split("/")[0];
        String key = fileLocation.split("/")[1];
//        log.info(s3Client.getObject(bucket, key).toString());
        return s3Client.getObject(bucket, key).getObjectContent();
    }

    @Override
    public List<PDFFile> getPDFList() {
        return repository.getFiles();
    }

    @Override
    public PDFFile deleteFile(String id) throws FileNotFoundException {
        PDFFile pdfFile = repository.deleteFile(id);
        if (pdfFile == null) {
            throw new FileNotFoundException();
        }
        s3Client.deleteObject(s3Bucket, pdfFile.getId());
        return pdfFile;
    }

    @Override
    public PDFFile updatePDF(PDFRequest request, String id) {
        PDFFile file = new PDFFile();
//        log.debug(id);
        file.setId(id);
        file.setSubmitter(request.getSubmitter());
        file.setDescription(request.getDescription());
        file.setGeneratedTime(LocalDateTime.now());

        PDFFile generatedFile= generator.generate(request);

        File temp = new File(generatedFile.getFileLocation());
        log.debug("Update temp file to s3 {}", generatedFile.getFileLocation());
//        log.info(file.toString());
//        log.info(generatedFile.toString());
        s3Client.putObject(s3Bucket,file.getId(),temp);
        log.debug("Updated");

        file.setFileLocation(String.join("/",s3Bucket,file.getId()));
        file.setFileSize(generatedFile.getFileSize());
        file.setFileName(generatedFile.getFileName());
        repository.saveFile(file);

        log.debug("clear temp file {}", file.getFileLocation());
        if(temp.delete()){
            log.debug("cleared");
        }
        return file;
    }
}

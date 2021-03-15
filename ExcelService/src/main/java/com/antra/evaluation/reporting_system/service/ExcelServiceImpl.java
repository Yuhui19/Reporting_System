package com.antra.evaluation.reporting_system.service;

import com.amazonaws.services.s3.AmazonS3;
import com.antra.evaluation.reporting_system.exception.FileGenerationException;
import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.MultiSheetExcelRequest;
import com.antra.evaluation.reporting_system.pojo.report.ExcelData;
import com.antra.evaluation.reporting_system.pojo.report.ExcelDataHeader;
import com.antra.evaluation.reporting_system.pojo.report.ExcelDataSheet;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import com.antra.evaluation.reporting_system.repo.ExcelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelServiceImpl implements ExcelService {

    private static final Logger log = LoggerFactory.getLogger(ExcelServiceImpl.class);

    private final ExcelRepository excelRepository;

    private final ExcelGenerationService excelGenerationService;

    private final AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String s3Bucket;

    @Autowired
    public ExcelServiceImpl(ExcelRepository excelRepository, ExcelGenerationService excelGenerationService, AmazonS3 s3Client) {
        this.excelRepository = excelRepository;
        this.excelGenerationService = excelGenerationService;
        this.s3Client = s3Client;
    }

    @Override
    public InputStream getExcelBodyById(String id) throws FileNotFoundException {
//        Optional<ExcelFile> fileInfo = excelRepository.getFileById(id);
//        return new FileInputStream(fileInfo.orElseThrow(FileNotFoundException::new).getFileLocation());
        ExcelFile excelFile = excelRepository.getFileById(id);
        String fileLocation = excelFile.getFileLocation(); // this location is s3 "bucket/key"
//        log.info(fileLocation);
        String bucket = fileLocation.split("/")[0];
        String key = fileLocation.split("/")[1];
//        ExcelFile file = s3Client.getObject(bucket, excelFile.getFileId());
        log.info(s3Client.getObject(bucket, key).toString());
        return s3Client.getObject(bucket, key).getObjectContent();
    }

    @Override
    public ExcelFile generateFile(ExcelRequest request, boolean multisheet) {
        ExcelFile fileInfo = new ExcelFile();
        File generatedFile;
        fileInfo.setFileId(UUID.randomUUID().toString());
        ExcelData data = new ExcelData();
        data.setTitle(request.getDescription());
        data.setFileId(fileInfo.getFileId());
        data.setSubmitter(fileInfo.getSubmitter());
        if(multisheet){
            data.setSheets(generateMultiSheet(request));
        }else {
            data.setSheets(generateSheet(request));
        }
        try {
            generatedFile = excelGenerationService.generateExcelReport(data);
//            fileInfo.setFileLocation(generatedFile.getAbsolutePath());
            fileInfo.setFileName(generatedFile.getName());
            fileInfo.setGeneratedTime(LocalDateTime.now());
            fileInfo.setSubmitter(request.getSubmitter());
            fileInfo.setFileSize(generatedFile.length());
            fileInfo.setDescription(request.getDescription());
        } catch (IOException e) {
//            log.error("Error in generateFile()", e);
            throw new FileGenerationException(e);
        }

        log.debug("Upload temp excel file to s3 {}", generatedFile.getAbsolutePath());
        s3Client.putObject(s3Bucket, fileInfo.getFileId(), generatedFile);
        log.debug("Uploaded");
        fileInfo.setFileLocation(String.join("/", s3Bucket, fileInfo.getFileId()));
        excelRepository.saveFile(fileInfo);
        log.debug("Excel File Generated : {}", fileInfo);
        if (generatedFile.delete()) {
            log.debug("cleared");
        }
        return fileInfo;
    }

    @Override
    public List<ExcelFile> getExcelList() {
        return excelRepository.getFiles();
    }

    @Override
    public ExcelFile deleteFile(String id) throws FileNotFoundException {
        ExcelFile excelFile = excelRepository.deleteFile(id);
        if (excelFile == null) {
            throw new FileNotFoundException();
        }
//        File file = new File(excelFile.getFileLocation());
//        file.delete();
        s3Client.deleteObject(s3Bucket, excelFile.getFileId());
        return excelFile;
    }

    @Override
    public ExcelFile updateFile(ExcelRequest request, String id, boolean multisheet) {
//        log.debug(id);
        ExcelFile fileInfo = new ExcelFile();
        File generatedFile;
        fileInfo.setFileId(id);
        ExcelData data = new ExcelData();
        data.setTitle(request.getDescription());
        data.setFileId(fileInfo.getFileId());
        data.setSubmitter(fileInfo.getSubmitter());
        if(multisheet){
            data.setSheets(generateMultiSheet(request));
        }else {
            data.setSheets(generateSheet(request));
        }
        try {
            generatedFile = excelGenerationService.generateExcelReport(data);
//            fileInfo.setFileLocation(generatedFile.getAbsolutePath());
            fileInfo.setFileName(generatedFile.getName());
            fileInfo.setGeneratedTime(LocalDateTime.now());
            fileInfo.setSubmitter(request.getSubmitter());
            fileInfo.setFileSize(generatedFile.length());
            fileInfo.setDescription(request.getDescription());
        } catch (IOException e) {
//            log.error("Error in generateFile()", e);
            throw new FileGenerationException(e);
        }

        log.debug("Update temp excel file to s3 {}", generatedFile.getAbsolutePath());
//        log.info(fileInfo.toString());
//        log.info(generatedFile.toString());
        s3Client.putObject(s3Bucket, fileInfo.getFileId(), generatedFile);
        log.debug("Updated");
        fileInfo.setFileLocation(String.join("/", s3Bucket, fileInfo.getFileId()));
        excelRepository.saveFile(fileInfo);
        log.debug("Excel File Updated : {}", fileInfo);
        if (generatedFile.delete()) {
            log.debug("cleared");
        }
        return fileInfo;
    }

    private List<ExcelDataSheet> generateSheet(ExcelRequest request) {
        List<ExcelDataSheet> sheets = new ArrayList<>();
        ExcelDataSheet sheet = new ExcelDataSheet();
        sheet.setHeaders(request.getHeaders().stream().map(ExcelDataHeader::new).collect(Collectors.toList()));
        sheet.setDataRows(request.getData().stream().map(listOfString -> (List<Object>) new ArrayList<Object>(listOfString)).collect(Collectors.toList()));
        sheet.setTitle("sheet-1");
        sheets.add(sheet);
        return sheets;
    }
    private List<ExcelDataSheet> generateMultiSheet(ExcelRequest request) {
        List<ExcelDataSheet> sheets = new ArrayList<>();
        int index = request.getHeaders().indexOf(((MultiSheetExcelRequest) request).getSplitBy());
        Map<String, List<List<String>>> splittedData = request.getData().stream().collect(Collectors.groupingBy(row -> (String)row.get(index)));
        List<ExcelDataHeader> headers = request.getHeaders().stream().map(ExcelDataHeader::new).collect(Collectors.toList());
        splittedData.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(
                entry ->{
                    ExcelDataSheet sheet = new ExcelDataSheet();
                    sheet.setHeaders(headers);
                    sheet.setDataRows(entry.getValue().stream().map(listOfString -> {
                        List<Object> listOfObject = new ArrayList<>();
                        listOfString.forEach(listOfObject::add);
                        return listOfObject;
                    }).collect(Collectors.toList()));
                    sheet.setTitle(entry.getKey());
                    sheets.add(sheet);
                }
        );
        return sheets;
    }
}

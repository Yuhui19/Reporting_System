package com.antra.evaluation.reporting_system.pojo.report;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "excel")
public class ExcelFile {
    @DynamoDBHashKey
    private String fileId;
    @DynamoDBAttribute
    private String fileName;
    @DynamoDBAttribute
    private String fileLocation;
    @DynamoDBAttribute
    private String submitter;
    @DynamoDBAttribute
    private Long fileSize;
    @DynamoDBAttribute
    private String description;
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute
    private LocalDateTime generatedTime;

//    public Long getFileSize() {
//        return fileSize;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setFileSize(Long fileSize) {
//        this.fileSize = fileSize;
//    }
//
//    public String getFileId() {
//        return fileId;
//    }
//
//    public void setFileId(String fileId) {
//        this.fileId = fileId;
//    }
//
//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public String getFileLocation() {
//        return fileLocation;
//    }
//
//    public void setFileLocation(String fileLocation) {
//        this.fileLocation = fileLocation;
//    }
//
//    public String getSubmitter() {
//        return submitter;
//    }
//
//    public void setSubmitter(String submitter) {
//        this.submitter = submitter;
//    }
//
//    public LocalDateTime getGeneratedTime() {
//        return generatedTime;
//    }
//
//    public void setGeneratedTime(LocalDateTime generatedTime) {
//        this.generatedTime = generatedTime;
//    }

    @Override
    public String toString() {
        return "ExcelFile{" +
                "fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileLocation='" + fileLocation + '\'' +
                ", submitter='" + submitter + '\'' +
                ", fileSize=" + fileSize +
                ", generatedTime=" + generatedTime +
                '}';
    }

}

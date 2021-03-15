package com.antra.evaluation.reporting_system.pojo.report;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "pdf")
public class PDFFile {
    @DynamoDBHashKey
    private String id;
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

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
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
//    public Long getFileSize() {
//        return fileSize;
//    }
//
//    public void setFileSize(Long fileSize) {
//        this.fileSize = fileSize;
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
//    public LocalDateTime getGeneratedTime() {
//        return generatedTime;
//    }
//
//    public void setGeneratedTime(LocalDateTime generatedTime) {
//        this.generatedTime = generatedTime;
//    }

    @Override
    public String toString() {
        return "PDFFile{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileLocation='" + fileLocation + '\'' +
                ", submitter='" + submitter + '\'' +
                ", fileSize=" + fileSize +
                ", description='" + description + '\'' +
                ", generatedTime=" + generatedTime +
                '}';
    }
}

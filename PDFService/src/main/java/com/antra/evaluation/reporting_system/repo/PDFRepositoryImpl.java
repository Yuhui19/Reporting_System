package com.antra.evaluation.reporting_system.repo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.antra.evaluation.reporting_system.pojo.report.PDFFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PDFRepositoryImpl implements PDFRepository{

    @Autowired
    private DynamoDBMapper mapper;

    @Override
    public PDFFile getFileById(String id) {
        return mapper.load(PDFFile.class, id);
    }

    @Override
    public PDFFile saveFile(PDFFile file) {
        mapper.save(file);
        return file;
    }

    @Override
    public PDFFile deleteFile(String id) {
        PDFFile pdfFile = getFileById(id);
        mapper.delete(pdfFile);
        return pdfFile;
    }

    @Override
    public List<PDFFile> getFiles() {
        return mapper.scan(PDFFile.class, new DynamoDBScanExpression());
    }
}

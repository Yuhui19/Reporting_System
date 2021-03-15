package com.antra.evaluation.reporting_system.repo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ExcelRepositoryImpl implements ExcelRepository {

    @Autowired
    private DynamoDBMapper mapper;

    @Override
    public ExcelFile getFileById(String id) {
        return mapper.load(ExcelFile.class, id);
    }

    @Override
    public ExcelFile saveFile(ExcelFile file) {
        mapper.save(file);
        return file;
    }

    @Override
    public ExcelFile deleteFile(String id) {
        ExcelFile excelFile = getFileById(id);
        mapper.delete(excelFile);
        return excelFile;
    }

    @Override
    public List<ExcelFile> getFiles() {
        return mapper.scan(ExcelFile.class, new DynamoDBScanExpression());
    }


//    Map<String, ExcelFile> excelData = new ConcurrentHashMap<>();
//
//    @Override
//    public Optional<ExcelFile> getFileById(String id) {
//        return Optional.ofNullable(excelData.get(id));
//    }
//
//    @Override
//    public ExcelFile saveFile(ExcelFile file) {
//        return excelData.put(file.getFileId(), file);
//    }
//
//    @Override
//    public ExcelFile deleteFile(String id) {
//        return excelData.remove(id);
//    }
//
//    @Override
//    public List<ExcelFile> getFiles() {
//        return new ArrayList<>(excelData.values());
//    }

}


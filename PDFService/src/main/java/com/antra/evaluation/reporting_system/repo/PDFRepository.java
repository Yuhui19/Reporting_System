package com.antra.evaluation.reporting_system.repo;

import com.antra.evaluation.reporting_system.pojo.report.PDFFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PDFRepository {

    PDFFile getFileById(String id);

//    Optional<ExcelFile> getFileById(String id);

    PDFFile saveFile(PDFFile file);

    PDFFile deleteFile(String id);

    List<PDFFile> getFiles();
}

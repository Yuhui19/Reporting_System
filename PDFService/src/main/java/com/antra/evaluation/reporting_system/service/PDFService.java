package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.pojo.api.PDFRequest;
import com.antra.evaluation.reporting_system.pojo.report.PDFFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface PDFService {
    PDFFile createPDF(PDFRequest request);

//    PDFFile deletePDF(String reqId) throws FileNotFoundException;

    InputStream getPdfBodyById(String id) throws FileNotFoundException;

    List<PDFFile> getPDFList();

    PDFFile deleteFile(String id) throws FileNotFoundException;

    PDFFile updatePDF(PDFRequest request, String id);
}

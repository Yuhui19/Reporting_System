package com.antra.evaluation.reporting_system.endpoint;

import com.antra.evaluation.reporting_system.exception.FileGenerationException;
import com.antra.evaluation.reporting_system.pojo.api.ErrorResponse;
import com.antra.evaluation.reporting_system.pojo.api.PDFRequest;
import com.antra.evaluation.reporting_system.pojo.api.PDFResponse;
import com.antra.evaluation.reporting_system.pojo.report.PDFFile;
import com.antra.evaluation.reporting_system.service.PDFService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PDFGenerationController {

    private static final Logger log = LoggerFactory.getLogger(PDFGenerationController.class);
    private static final String DOWNLOAD_API_URI = "/pdf/{id}/content";
    private PDFService pdfService;

    @Autowired
    public PDFGenerationController(PDFService pdfService) {
        this.pdfService = pdfService;
    }


    @PostMapping("/pdf")
    @ApiOperation("Generate PDF")
    public ResponseEntity<PDFResponse> createPDF(@RequestBody @Validated PDFRequest request) {
        log.info("Got request to generate PDF: {}", request);

        PDFResponse response = new PDFResponse();
        PDFFile file = null;
        response.setReqId(request.getReqId());

        try {
            file = pdfService.createPDF(request);
            response.setFileId(file.getId());
            response.setFileLocation(file.getFileLocation());
            response.setFileSize(file.getFileSize());
            log.info("Generated: {}", file);
        } catch (Exception e) {
            response.setFailed(true);
            log.error("Error in generating pdf", e);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String generateFileDownloadLink(String fileId) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost:8080").path(DOWNLOAD_API_URI) // localhost:8080 need to be externalized as parameter
                .buildAndExpand(fileId);
        return uriComponents.toUriString();
    }

    @GetMapping("/pdf")
    @ApiOperation("List all existing pdf files")
    public ResponseEntity<List<PDFResponse>> listExcels() {
        log.debug("Got Request to List All Files");
        List<PDFFile> fileList = pdfService.getPDFList();
        var responseList = fileList.stream().map(file -> {
            PDFResponse response = new PDFResponse();
            BeanUtils.copyProperties(file, response);
            response.setFileLocation(this.generateFileDownloadLink(file.getId()));
            return response;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping(DOWNLOAD_API_URI)
    public void downloadExcel(@PathVariable String id, HttpServletResponse response) throws IOException {
        log.debug("Got Request to Download File:{}", id);
        InputStream fis = pdfService.getPdfBodyById(id);
        response.setHeader("Content-Type", "application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + id);
        FileCopyUtils.copy(fis, response.getOutputStream());
        log.debug("Downloaded File:{}", id);
    }

    @PutMapping("pdf/{id}")
    @ApiOperation("Generate PDF")
    public ResponseEntity<PDFResponse> updatePDF(@RequestBody @Validated PDFRequest request, @PathVariable String id) {
        log.info("Got request to update PDF: {}", request);
        PDFResponse response = new PDFResponse();
        PDFFile file;
        response.setReqId(request.getReqId());
        try {
            file = pdfService.updatePDF(request, id);
            response.setFileId(file.getId());
            response.setFileLocation(file.getFileLocation());
            response.setFileSize(file.getFileSize());
            log.info("Generated: {}", file);
        } catch (Exception e) {
            response.setFailed(true);
            log.error("Error in generating pdf", e);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/pdf/{id}")
    public ResponseEntity<PDFResponse> deleteExcel(@PathVariable String id) throws FileNotFoundException {
        log.debug("Got Request to Delete File:{}", id);
        var response = new PDFResponse();
        PDFFile fileDeleted = pdfService.deleteFile(id);
        BeanUtils.copyProperties(fileDeleted, response);
        response.setFileLocation(this.generateFileDownloadLink(fileDeleted.getId()));
        log.debug("File Deleted:{}", fileDeleted);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFound(Exception e) {
        log.error("The file doesn't exist", e);
        return new ResponseEntity<>(new ErrorResponse("The file doesn't exist", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileGenerationException.class)
    public ResponseEntity<ErrorResponse> handleFileGenerationExceptions(Exception e) {
        log.error("Cannot Generate Excel File", e);
        return new ResponseEntity<>(new ErrorResponse("Cannot Generate PDF File", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownExceptions(Exception e) {
        log.error("Something is wrong", e);
        return new ResponseEntity<>(new ErrorResponse("Something is wrong", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

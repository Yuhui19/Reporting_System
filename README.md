###Antra Final Project

Implemented get, delete and post api. [ReportController](https://github.com/Yuhui19/Reporting_System/blob/main/ClientService/src/main/java/com/antra/report/client/controller/ReportController.java), [ReportServiceImpl](https://github.com/Yuhui19/Reporting_System/blob/main/ClientService/src/main/java/com/antra/report/client/service/ReportServiceImpl.java), [ExcelController](https://github.com/Yuhui19/Reporting_System/blob/main/ExcelService/src/main/java/com/antra/evaluation/reporting_system/endpoint/ExcelGenerationController.java), [ExcelServiceImpl](https://github.com/Yuhui19/Reporting_System/blob/main/ExcelService/src/main/java/com/antra/evaluation/reporting_system/service/ExcelServiceImpl.java), [PdfController](https://github.com/Yuhui19/Reporting_System/blob/main/PDFService/src/main/java/com/antra/evaluation/reporting_system/endpoint/PDFGenerationController.java), [PdfServiceImpl](https://github.com/Yuhui19/Reporting_System/blob/main/PDFService/src/main/java/com/antra/evaluation/reporting_system/service/PDFServiceImpl.java)

Changed hashmap in excel service to dynamoDB. [Excel.repo](https://github.com/Yuhui19/Reporting_System/blob/main/ExcelService/src/main/java/com/antra/evaluation/reporting_system/repo/ExcelRepositoryImpl.java) 

Changed mongoDB in pdf service to dynamoDB. [Pdf.repo](https://github.com/Yuhui19/Reporting_System/blob/main/PDFService/src/main/java/com/antra/evaluation/reporting_system/repo/PDFRepositoryImpl.java) 

Save excel files into S3 bucket. [ExcelServiceImpl](https://github.com/Yuhui19/Reporting_System/blob/main/ExcelService/src/main/java/com/antra/evaluation/reporting_system/service/ExcelServiceImpl.java) line 55(get), 86(create), 110(delete), 145(update)

Save pdf files into S3 bucket. [PdfServiceImpl](https://github.com/Yuhui19/Reporting_System/blob/main/PDFService/src/main/java/com/antra/evaluation/reporting_system/service/PDFServiceImpl.java) line 51(create), 74(get), 88(delete), 107(update), 

Using multithread in sync API to send request. [ReportServiceImpl](https://github.com/Yuhui19/Reporting_System/blob/main/ClientService/src/main/java/com/antra/report/client/service/ReportServiceImpl.java) line 114-139, 267-281, 311-339

Fixed garbage files bug. [ExcelServiceImpl](https://github.com/Yuhui19/Reporting_System/blob/main/ExcelService/src/main/java/com/antra/evaluation/reporting_system/service/ExcelServiceImpl.java) line 58-95(generateFile function)
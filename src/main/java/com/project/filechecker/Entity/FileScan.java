package com.project.filechecker.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity

public class FileScan {
/**
 *  The below defines the tables for the database with its given names and value type.
 */
    @Id
  //  private  int id;
    private String fileName;
    private String filePathNoEx;
    private String fileFullPath;
    private LocalDate reportDate;
    private LocalDate fileCreatedDate;
    private String fileExtension;

    public FileScan() {

    }

    public FileScan(String fileFullPath, String fileName, LocalDate reportDate, LocalDate fileCreatedDate, String fileExtension, String filePathNoEx) {
        super();
        this.filePathNoEx = filePathNoEx;
        this.fileFullPath = fileFullPath;
        this.fileName = fileName;
        this.reportDate = reportDate;
        this.fileCreatedDate = fileCreatedDate;
        this.fileExtension = fileExtension;
    }
    public String getFilePathNoEx() {
        return filePathNoEx;
    }
    public void setPathNoEx(String filePathNoEx) {

        this.filePathNoEx = fileFullPath;
    }
    public String getFileFullPath() {
        return fileFullPath;
    }
    public void setFileFullPath(String fileFullPath) {

        this.fileFullPath = fileFullPath;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String FileName) {
        this.fileName = fileName;
    }
    public LocalDate getReportDate() {
        return reportDate;
    }
    public void setReportDate(LocalDate setReportDate) {
        this.reportDate = reportDate;
    }
    public LocalDate getFileCreatedDate() {
        return fileCreatedDate;
    }
    public void setFileCreatedDate(LocalDate FileCreatedDate) {
        this.fileCreatedDate = fileCreatedDate;
    }
    public String getFileExtension() {
        return fileExtension;
    }
    public void setFileExtension(String FileExtension) {
        this.fileExtension = fileExtension;
    }
}



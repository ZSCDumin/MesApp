package com.msw.mesapp.bean.warehouse;

public class ProductOutCheckBean {
    private String auditResult;
    private String auditor;
    private String auditNote;
    private String auditTime;

    public String getAuditNote() {
        return auditNote;
    }

    public void setAuditNote(String auditNote) {
        this.auditNote = auditNote;
    }

    public ProductOutCheckBean( String auditor,String auditResult, String auditNote, String auditTime) {
        this.auditResult = auditResult;
        this.auditor = auditor;
        this.auditNote = auditNote;
        this.auditTime = auditTime;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }
}

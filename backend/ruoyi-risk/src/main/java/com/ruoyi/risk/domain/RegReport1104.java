package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class RegReport1104 extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "报表编号")
    private String reportCode;

    @Excel(name = "报表名称")
    private String reportName;

    @Excel(name = "报表类型")
    private String reportType;

    @Excel(name = "报表期次")
    private String reportPeriod;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报表日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reportDate;

    @Excel(name = "报送频度")
    private String frequency;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报送截止日", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date submitDeadline;

    private String reportData;

    @Excel(name = "状态")
    private String status;

    @Excel(name = "生成人")
    private String generateUser;

    @Excel(name = "审核人")
    private String auditUser;

    @Excel(name = "报送人")
    private String submitUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报送时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    private String rejectReason;

    @Excel(name = "文件路径")
    private String filePath;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReportCode() { return reportCode; }
    public void setReportCode(String reportCode) { this.reportCode = reportCode; }

    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getReportPeriod() { return reportPeriod; }
    public void setReportPeriod(String reportPeriod) { this.reportPeriod = reportPeriod; }

    public Date getReportDate() { return reportDate; }
    public void setReportDate(Date reportDate) { this.reportDate = reportDate; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public Date getSubmitDeadline() { return submitDeadline; }
    public void setSubmitDeadline(Date submitDeadline) { this.submitDeadline = submitDeadline; }

    public String getReportData() { return reportData; }
    public void setReportData(String reportData) { this.reportData = reportData; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGenerateUser() { return generateUser; }
    public void setGenerateUser(String generateUser) { this.generateUser = generateUser; }

    public String getAuditUser() { return auditUser; }
    public void setAuditUser(String auditUser) { this.auditUser = auditUser; }

    public String getSubmitUser() { return submitUser; }
    public void setSubmitUser(String submitUser) { this.submitUser = submitUser; }

    public Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; }

    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}

package com.ruoyi.risk.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class RegEastReport extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "报表期次")
    private String reportPeriod;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报表日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reportDate;

    @Excel(name = "数据类型")
    private String dataType;

    @Excel(name = "数据表")
    private String dataTable;

    @Excel(name = "记录数")
    private Long recordCount;

    @Excel(name = "文件名")
    private String fileName;

    @Excel(name = "文件路径")
    private String filePath;

    @Excel(name = "文件大小")
    private Long fileSize;

    private String checkResult;

    @Excel(name = "检查状态")
    private String checkStatus;

    @Excel(name = "状态")
    private String status;

    @Excel(name = "生成人")
    private String generateUser;

    @Excel(name = "报送人")
    private String submitUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报送时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    private String rejectReason;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReportPeriod() { return reportPeriod; }
    public void setReportPeriod(String reportPeriod) { this.reportPeriod = reportPeriod; }

    public Date getReportDate() { return reportDate; }
    public void setReportDate(Date reportDate) { this.reportDate = reportDate; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }

    public String getDataTable() { return dataTable; }
    public void setDataTable(String dataTable) { this.dataTable = dataTable; }

    public Long getRecordCount() { return recordCount; }
    public void setRecordCount(Long recordCount) { this.recordCount = recordCount; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getCheckResult() { return checkResult; }
    public void setCheckResult(String checkResult) { this.checkResult = checkResult; }

    public String getCheckStatus() { return checkStatus; }
    public void setCheckStatus(String checkStatus) { this.checkStatus = checkStatus; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGenerateUser() { return generateUser; }
    public void setGenerateUser(String generateUser) { this.generateUser = generateUser; }

    public String getSubmitUser() { return submitUser; }
    public void setSubmitUser(String submitUser) { this.submitUser = submitUser; }

    public Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; }

    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
}

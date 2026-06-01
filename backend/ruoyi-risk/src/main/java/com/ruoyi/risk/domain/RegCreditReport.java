package com.ruoyi.risk.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class RegCreditReport extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "报表期次")
    private String reportPeriod;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报表日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reportDate;

    @Excel(name = "报表类型")
    private String reportType;

    @Excel(name = "数据类型")
    private String dataType;

    @Excel(name = "记录数")
    private Long recordCount;

    @Excel(name = "成功数")
    private Long successCount;

    @Excel(name = "失败数")
    private Long failCount;

    @Excel(name = "文件名")
    private String fileName;

    @Excel(name = "文件路径")
    private String filePath;

    @Excel(name = "状态")
    private String status;

    private String errorDetail;

    @Excel(name = "报送人")
    private String submitUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报送时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @Excel(name = "批次号")
    private String batchNo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReportPeriod() { return reportPeriod; }
    public void setReportPeriod(String reportPeriod) { this.reportPeriod = reportPeriod; }

    public Date getReportDate() { return reportDate; }
    public void setReportDate(Date reportDate) { this.reportDate = reportDate; }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }

    public Long getRecordCount() { return recordCount; }
    public void setRecordCount(Long recordCount) { this.recordCount = recordCount; }

    public Long getSuccessCount() { return successCount; }
    public void setSuccessCount(Long successCount) { this.successCount = successCount; }

    public Long getFailCount() { return failCount; }
    public void setFailCount(Long failCount) { this.failCount = failCount; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getErrorDetail() { return errorDetail; }
    public void setErrorDetail(String errorDetail) { this.errorDetail = errorDetail; }

    public String getSubmitUser() { return submitUser; }
    public void setSubmitUser(String submitUser) { this.submitUser = submitUser; }

    public Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }
}

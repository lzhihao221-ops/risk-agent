package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 授信额度管理实体
 */
public class LoanCreditLimit {
    private Long id;
    private Long companyId;
    private String companyName;
    private BigDecimal totalLimit;
    private BigDecimal usedLimit;
    private BigDecimal availableLimit;
    private String creditGrade;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date validFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date validTo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date reviewDate;
    private Integer status;
    private String remark;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public BigDecimal getTotalLimit() { return totalLimit; }
    public void setTotalLimit(BigDecimal totalLimit) { this.totalLimit = totalLimit; }
    public BigDecimal getUsedLimit() { return usedLimit; }
    public void setUsedLimit(BigDecimal usedLimit) { this.usedLimit = usedLimit; }
    public BigDecimal getAvailableLimit() { return availableLimit; }
    public void setAvailableLimit(BigDecimal availableLimit) { this.availableLimit = availableLimit; }
    public String getCreditGrade() { return creditGrade; }
    public void setCreditGrade(String creditGrade) { this.creditGrade = creditGrade; }
    public java.util.Date getValidFrom() { return validFrom; }
    public void setValidFrom(java.util.Date validFrom) { this.validFrom = validFrom; }
    public java.util.Date getValidTo() { return validTo; }
    public void setValidTo(java.util.Date validTo) { this.validTo = validTo; }
    public java.util.Date getReviewDate() { return reviewDate; }
    public void setReviewDate(java.util.Date reviewDate) { this.reviewDate = reviewDate; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public java.util.Date getCreateTime() { return createTime; }
    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public java.util.Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(java.util.Date updateTime) { this.updateTime = updateTime; }
}

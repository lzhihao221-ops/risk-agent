package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 放款申请实体
 */
public class LoanDisbursement {
    private Long id;
    private String loanNo;
    private String appNo;
    private Long companyId;
    private String companyName;
    private BigDecimal disburseAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date disburseDate;
    private String disburseAccount;
    private String receiveAccount;
    private String receiveBank;
    private String disburseType;
    private Integer status;
    private String operatorName;
    private String approveUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date disburseTime;
    private String remark;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLoanNo() { return loanNo; }
    public void setLoanNo(String loanNo) { this.loanNo = loanNo; }
    public String getAppNo() { return appNo; }
    public void setAppNo(String appNo) { this.appNo = appNo; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public BigDecimal getDisburseAmount() { return disburseAmount; }
    public void setDisburseAmount(BigDecimal disburseAmount) { this.disburseAmount = disburseAmount; }
    public Date getDisburseDate() { return disburseDate; }
    public void setDisburseDate(Date disburseDate) { this.disburseDate = disburseDate; }
    public String getDisburseAccount() { return disburseAccount; }
    public void setDisburseAccount(String disburseAccount) { this.disburseAccount = disburseAccount; }
    public String getReceiveAccount() { return receiveAccount; }
    public void setReceiveAccount(String receiveAccount) { this.receiveAccount = receiveAccount; }
    public String getReceiveBank() { return receiveBank; }
    public void setReceiveBank(String receiveBank) { this.receiveBank = receiveBank; }
    public String getDisburseType() { return disburseType; }
    public void setDisburseType(String disburseType) { this.disburseType = disburseType; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public String getApproveUser() { return approveUser; }
    public void setApproveUser(String approveUser) { this.approveUser = approveUser; }
    public Date getApproveTime() { return approveTime; }
    public void setApproveTime(Date approveTime) { this.approveTime = approveTime; }
    public Date getDisburseTime() { return disburseTime; }
    public void setDisburseTime(Date disburseTime) { this.disburseTime = disburseTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}

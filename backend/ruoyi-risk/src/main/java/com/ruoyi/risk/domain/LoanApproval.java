package com.ruoyi.risk.domain;

import java.math.BigDecimal;

public class LoanApproval {
    private Long id;
    private Long appId;
    private String appNo;
    private Integer approveLevel;
    private String approveRole;
    private Long approveUserId;
    private String approveUser;
    private Integer approveResult;
    private String approveOpinion;
    private java.util.Date approveTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAppId() { return appId; }
    public void setAppId(Long appId) { this.appId = appId; }
    public String getAppNo() { return appNo; }
    public void setAppNo(String appNo) { this.appNo = appNo; }
    public Integer getApproveLevel() { return approveLevel; }
    public void setApproveLevel(Integer approveLevel) { this.approveLevel = approveLevel; }
    public String getApproveRole() { return approveRole; }
    public void setApproveRole(String approveRole) { this.approveRole = approveRole; }
    public Long getApproveUserId() { return approveUserId; }
    public void setApproveUserId(Long approveUserId) { this.approveUserId = approveUserId; }
    public String getApproveUser() { return approveUser; }
    public void setApproveUser(String approveUser) { this.approveUser = approveUser; }
    public Integer getApproveResult() { return approveResult; }
    public void setApproveResult(Integer approveResult) { this.approveResult = approveResult; }
    public String getApproveOpinion() { return approveOpinion; }
    public void setApproveOpinion(String approveOpinion) { this.approveOpinion = approveOpinion; }
    public java.util.Date getApproveTime() { return approveTime; }
    public void setApproveTime(java.util.Date approveTime) { this.approveTime = approveTime; }
}

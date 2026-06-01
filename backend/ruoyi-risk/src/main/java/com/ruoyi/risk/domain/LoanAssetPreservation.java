package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 资产保全对象 loan_asset_preservation
 */
public class LoanAssetPreservation
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long loanId;
    private String loanNo;
    private Long companyId;
    private String companyName;
    private String preserveType;
    private Integer preserveStatus;
    private String courtName;
    private String caseNo;
    private BigDecimal claimAmount;
    private BigDecimal preserveAmount;
    private String preserveAsset;
    private String lawyerName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date filingDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date hearingDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date judgmentDate;
    private String judgmentResult;
    private String executionStatus;
    private BigDecimal recoveryAmount;
    private String remark;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }

    public String getLoanNo() { return loanNo; }
    public void setLoanNo(String loanNo) { this.loanNo = loanNo; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getPreserveType() { return preserveType; }
    public void setPreserveType(String preserveType) { this.preserveType = preserveType; }

    public Integer getPreserveStatus() { return preserveStatus; }
    public void setPreserveStatus(Integer preserveStatus) { this.preserveStatus = preserveStatus; }

    public String getCourtName() { return courtName; }
    public void setCourtName(String courtName) { this.courtName = courtName; }

    public String getCaseNo() { return caseNo; }
    public void setCaseNo(String caseNo) { this.caseNo = caseNo; }

    public BigDecimal getClaimAmount() { return claimAmount; }
    public void setClaimAmount(BigDecimal claimAmount) { this.claimAmount = claimAmount; }

    public BigDecimal getPreserveAmount() { return preserveAmount; }
    public void setPreserveAmount(BigDecimal preserveAmount) { this.preserveAmount = preserveAmount; }

    public String getPreserveAsset() { return preserveAsset; }
    public void setPreserveAsset(String preserveAsset) { this.preserveAsset = preserveAsset; }

    public String getLawyerName() { return lawyerName; }
    public void setLawyerName(String lawyerName) { this.lawyerName = lawyerName; }

    public Date getFilingDate() { return filingDate; }
    public void setFilingDate(Date filingDate) { this.filingDate = filingDate; }

    public Date getHearingDate() { return hearingDate; }
    public void setHearingDate(Date hearingDate) { this.hearingDate = hearingDate; }

    public Date getJudgmentDate() { return judgmentDate; }
    public void setJudgmentDate(Date judgmentDate) { this.judgmentDate = judgmentDate; }

    public String getJudgmentResult() { return judgmentResult; }
    public void setJudgmentResult(String judgmentResult) { this.judgmentResult = judgmentResult; }

    public String getExecutionStatus() { return executionStatus; }
    public void setExecutionStatus(String executionStatus) { this.executionStatus = executionStatus; }

    public BigDecimal getRecoveryAmount() { return recoveryAmount; }
    public void setRecoveryAmount(BigDecimal recoveryAmount) { this.recoveryAmount = recoveryAmount; }

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("loanId", getLoanId())
            .append("loanNo", getLoanNo())
            .append("companyId", getCompanyId())
            .append("companyName", getCompanyName())
            .append("preserveType", getPreserveType())
            .append("preserveStatus", getPreserveStatus())
            .append("courtName", getCourtName())
            .append("caseNo", getCaseNo())
            .append("claimAmount", getClaimAmount())
            .append("preserveAmount", getPreserveAmount())
            .append("preserveAsset", getPreserveAsset())
            .append("lawyerName", getLawyerName())
            .append("filingDate", getFilingDate())
            .append("hearingDate", getHearingDate())
            .append("judgmentDate", getJudgmentDate())
            .append("judgmentResult", getJudgmentResult())
            .append("executionStatus", getExecutionStatus())
            .append("recoveryAmount", getRecoveryAmount())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}

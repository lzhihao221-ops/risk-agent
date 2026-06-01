package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 贷款核销对象 loan_writeoff
 */
public class LoanWriteoff
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long loanId;
    private String loanNo;
    private Long companyId;
    private String companyName;
    private BigDecimal writeoffAmount;
    private BigDecimal writeoffPrincipal;
    private BigDecimal writeoffInterest;
    private String writeoffReason;
    private String writeoffType;
    private Integer fiveCategory;
    private String approveUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;
    private String approveOpinion;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date writeoffDate;
    private Integer status;
    private BigDecimal recoverAmount;
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

    public BigDecimal getWriteoffAmount() { return writeoffAmount; }
    public void setWriteoffAmount(BigDecimal writeoffAmount) { this.writeoffAmount = writeoffAmount; }

    public BigDecimal getWriteoffPrincipal() { return writeoffPrincipal; }
    public void setWriteoffPrincipal(BigDecimal writeoffPrincipal) { this.writeoffPrincipal = writeoffPrincipal; }

    public BigDecimal getWriteoffInterest() { return writeoffInterest; }
    public void setWriteoffInterest(BigDecimal writeoffInterest) { this.writeoffInterest = writeoffInterest; }

    public String getWriteoffReason() { return writeoffReason; }
    public void setWriteoffReason(String writeoffReason) { this.writeoffReason = writeoffReason; }

    public String getWriteoffType() { return writeoffType; }
    public void setWriteoffType(String writeoffType) { this.writeoffType = writeoffType; }

    public Integer getFiveCategory() { return fiveCategory; }
    public void setFiveCategory(Integer fiveCategory) { this.fiveCategory = fiveCategory; }

    public String getApproveUser() { return approveUser; }
    public void setApproveUser(String approveUser) { this.approveUser = approveUser; }

    public Date getApproveTime() { return approveTime; }
    public void setApproveTime(Date approveTime) { this.approveTime = approveTime; }

    public String getApproveOpinion() { return approveOpinion; }
    public void setApproveOpinion(String approveOpinion) { this.approveOpinion = approveOpinion; }

    public Date getWriteoffDate() { return writeoffDate; }
    public void setWriteoffDate(Date writeoffDate) { this.writeoffDate = writeoffDate; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public BigDecimal getRecoverAmount() { return recoverAmount; }
    public void setRecoverAmount(BigDecimal recoverAmount) { this.recoverAmount = recoverAmount; }

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
            .append("writeoffAmount", getWriteoffAmount())
            .append("writeoffPrincipal", getWriteoffPrincipal())
            .append("writeoffInterest", getWriteoffInterest())
            .append("writeoffReason", getWriteoffReason())
            .append("writeoffType", getWriteoffType())
            .append("fiveCategory", getFiveCategory())
            .append("approveUser", getApproveUser())
            .append("approveTime", getApproveTime())
            .append("approveOpinion", getApproveOpinion())
            .append("writeoffDate", getWriteoffDate())
            .append("status", getStatus())
            .append("recoverAmount", getRecoverAmount())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}

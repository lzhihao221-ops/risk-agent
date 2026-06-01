package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 催收管理对象 loan_collection
 */
public class LoanCollection
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long loanId;
    private String loanNo;
    private Long companyId;
    private String companyName;
    private BigDecimal overdueAmount;
    private Integer overdueDays;
    private String collectionType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date collectionDate;
    private String collectorName;
    private String collectionResult;
    private String nextAction;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextDate;
    private BigDecimal promiseAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date promiseDate;
    private Integer status;
    private String remark;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 催收记录列表（非数据库字段） */
    private List<LoanCollectionLog> collectionLogs;

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

    public BigDecimal getOverdueAmount() { return overdueAmount; }
    public void setOverdueAmount(BigDecimal overdueAmount) { this.overdueAmount = overdueAmount; }

    public Integer getOverdueDays() { return overdueDays; }
    public void setOverdueDays(Integer overdueDays) { this.overdueDays = overdueDays; }

    public String getCollectionType() { return collectionType; }
    public void setCollectionType(String collectionType) { this.collectionType = collectionType; }

    public Date getCollectionDate() { return collectionDate; }
    public void setCollectionDate(Date collectionDate) { this.collectionDate = collectionDate; }

    public String getCollectorName() { return collectorName; }
    public void setCollectorName(String collectorName) { this.collectorName = collectorName; }

    public String getCollectionResult() { return collectionResult; }
    public void setCollectionResult(String collectionResult) { this.collectionResult = collectionResult; }

    public String getNextAction() { return nextAction; }
    public void setNextAction(String nextAction) { this.nextAction = nextAction; }

    public Date getNextDate() { return nextDate; }
    public void setNextDate(Date nextDate) { this.nextDate = nextDate; }

    public BigDecimal getPromiseAmount() { return promiseAmount; }
    public void setPromiseAmount(BigDecimal promiseAmount) { this.promiseAmount = promiseAmount; }

    public Date getPromiseDate() { return promiseDate; }
    public void setPromiseDate(Date promiseDate) { this.promiseDate = promiseDate; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

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

    public List<LoanCollectionLog> getCollectionLogs() { return collectionLogs; }
    public void setCollectionLogs(List<LoanCollectionLog> collectionLogs) { this.collectionLogs = collectionLogs; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("loanId", getLoanId())
            .append("loanNo", getLoanNo())
            .append("companyId", getCompanyId())
            .append("companyName", getCompanyName())
            .append("overdueAmount", getOverdueAmount())
            .append("overdueDays", getOverdueDays())
            .append("collectionType", getCollectionType())
            .append("collectionDate", getCollectionDate())
            .append("collectorName", getCollectorName())
            .append("collectionResult", getCollectionResult())
            .append("nextAction", getNextAction())
            .append("nextDate", getNextDate())
            .append("promiseAmount", getPromiseAmount())
            .append("promiseDate", getPromiseDate())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}

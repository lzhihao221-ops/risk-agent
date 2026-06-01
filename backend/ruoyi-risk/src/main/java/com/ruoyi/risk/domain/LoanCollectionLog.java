package com.ruoyi.risk.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 催收记录对象 loan_collection_log
 */
public class LoanCollectionLog
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long collectionId;
    private Long loanId;
    private String loanNo;
    private String contactPerson;
    private String contactPhone;
    private String contactType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date contactTime;
    private String contactResult;
    private String borrowerResponse;
    private String operatorName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCollectionId() { return collectionId; }
    public void setCollectionId(Long collectionId) { this.collectionId = collectionId; }

    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }

    public String getLoanNo() { return loanNo; }
    public void setLoanNo(String loanNo) { this.loanNo = loanNo; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getContactType() { return contactType; }
    public void setContactType(String contactType) { this.contactType = contactType; }

    public Date getContactTime() { return contactTime; }
    public void setContactTime(Date contactTime) { this.contactTime = contactTime; }

    public String getContactResult() { return contactResult; }
    public void setContactResult(String contactResult) { this.contactResult = contactResult; }

    public String getBorrowerResponse() { return borrowerResponse; }
    public void setBorrowerResponse(String borrowerResponse) { this.borrowerResponse = borrowerResponse; }

    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("collectionId", getCollectionId())
            .append("loanId", getLoanId())
            .append("loanNo", getLoanNo())
            .append("contactPerson", getContactPerson())
            .append("contactPhone", getContactPhone())
            .append("contactType", getContactType())
            .append("contactTime", getContactTime())
            .append("contactResult", getContactResult())
            .append("borrowerResponse", getBorrowerResponse())
            .append("operatorName", getOperatorName())
            .append("createTime", getCreateTime())
            .toString();
    }
}

package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 利息计提实体
 */
public class LoanInterestAccrual {
    private Long id;
    private Long loanId;
    private String loanNo;
    private Long companyId;
    private String companyName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accrualDate;
    private BigDecimal accrualAmount;
    private BigDecimal principal;
    private BigDecimal interestRate;
    private Integer days;
    private String accrualType;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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
    public Date getAccrualDate() { return accrualDate; }
    public void setAccrualDate(Date accrualDate) { this.accrualDate = accrualDate; }
    public BigDecimal getAccrualAmount() { return accrualAmount; }
    public void setAccrualAmount(BigDecimal accrualAmount) { this.accrualAmount = accrualAmount; }
    public BigDecimal getPrincipal() { return principal; }
    public void setPrincipal(BigDecimal principal) { this.principal = principal; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }
    public String getAccrualType() { return accrualType; }
    public void setAccrualType(String accrualType) { this.accrualType = accrualType; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}

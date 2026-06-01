package com.ruoyi.risk.domain;

import java.math.BigDecimal;

public class LoanRepayment {
    private Long id;
    private Long loanId;
    private String loanNo;
    private Integer periodNo;
    private java.util.Date planDate;
    private BigDecimal planPrincipal;
    private BigDecimal planInterest;
    private BigDecimal planTotal;
    private java.util.Date actualDate;
    private BigDecimal actualPrincipal;
    private BigDecimal actualInterest;
    private BigDecimal actualTotal;
    private Integer status;
    private Integer overdueDays;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public String getLoanNo() { return loanNo; }
    public void setLoanNo(String loanNo) { this.loanNo = loanNo; }
    public Integer getPeriodNo() { return periodNo; }
    public void setPeriodNo(Integer periodNo) { this.periodNo = periodNo; }
    public java.util.Date getPlanDate() { return planDate; }
    public void setPlanDate(java.util.Date planDate) { this.planDate = planDate; }
    public BigDecimal getPlanPrincipal() { return planPrincipal; }
    public void setPlanPrincipal(BigDecimal planPrincipal) { this.planPrincipal = planPrincipal; }
    public BigDecimal getPlanInterest() { return planInterest; }
    public void setPlanInterest(BigDecimal planInterest) { this.planInterest = planInterest; }
    public BigDecimal getPlanTotal() { return planTotal; }
    public void setPlanTotal(BigDecimal planTotal) { this.planTotal = planTotal; }
    public java.util.Date getActualDate() { return actualDate; }
    public void setActualDate(java.util.Date actualDate) { this.actualDate = actualDate; }
    public BigDecimal getActualPrincipal() { return actualPrincipal; }
    public void setActualPrincipal(BigDecimal actualPrincipal) { this.actualPrincipal = actualPrincipal; }
    public BigDecimal getActualInterest() { return actualInterest; }
    public void setActualInterest(BigDecimal actualInterest) { this.actualInterest = actualInterest; }
    public BigDecimal getActualTotal() { return actualTotal; }
    public void setActualTotal(BigDecimal actualTotal) { this.actualTotal = actualTotal; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getOverdueDays() { return overdueDays; }
    public void setOverdueDays(Integer overdueDays) { this.overdueDays = overdueDays; }
}

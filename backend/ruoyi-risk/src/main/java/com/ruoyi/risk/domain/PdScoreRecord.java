package com.ruoyi.risk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class PdScoreRecord {
    private Long scoreId;
    private Long companyId;
    private String companyName;
    private Long loanId;
    private Long modelId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scoreDate;
    private Double rawScore;
    private Double probability;
    private String ratingGrade;
    private String scoreLevel;
    private String variableDetail;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getScoreId() { return scoreId; }
    public void setScoreId(Long scoreId) { this.scoreId = scoreId; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }
    public Date getScoreDate() { return scoreDate; }
    public void setScoreDate(Date scoreDate) { this.scoreDate = scoreDate; }
    public Double getRawScore() { return rawScore; }
    public void setRawScore(Double rawScore) { this.rawScore = rawScore; }
    public Double getProbability() { return probability; }
    public void setProbability(Double probability) { this.probability = probability; }
    public String getRatingGrade() { return ratingGrade; }
    public void setRatingGrade(String ratingGrade) { this.ratingGrade = ratingGrade; }
    public String getScoreLevel() { return scoreLevel; }
    public void setScoreLevel(String scoreLevel) { this.scoreLevel = scoreLevel; }
    public String getVariableDetail() { return variableDetail; }
    public void setVariableDetail(String variableDetail) { this.variableDetail = variableDetail; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}

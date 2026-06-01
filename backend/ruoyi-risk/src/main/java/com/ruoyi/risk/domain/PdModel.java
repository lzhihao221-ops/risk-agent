package com.ruoyi.risk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class PdModel {
    private Long modelId;
    private String modelName;
    private String modelType;
    private String modelVersion;
    private String targetVariable;
    private Integer sampleSize;
    private Double aucRoc;
    private Double ksStatistic;
    private Double gini;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date trainingDate;
    private Double cutoffScore;
    private String coefficientJson;
    private String status;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String remark;

    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public String getModelType() { return modelType; }
    public void setModelType(String modelType) { this.modelType = modelType; }
    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
    public String getTargetVariable() { return targetVariable; }
    public void setTargetVariable(String targetVariable) { this.targetVariable = targetVariable; }
    public Integer getSampleSize() { return sampleSize; }
    public void setSampleSize(Integer sampleSize) { this.sampleSize = sampleSize; }
    public Double getAucRoc() { return aucRoc; }
    public void setAucRoc(Double aucRoc) { this.aucRoc = aucRoc; }
    public Double getKsStatistic() { return ksStatistic; }
    public void setKsStatistic(Double ksStatistic) { this.ksStatistic = ksStatistic; }
    public Double getGini() { return gini; }
    public void setGini(Double gini) { this.gini = gini; }
    public Date getTrainingDate() { return trainingDate; }
    public void setTrainingDate(Date trainingDate) { this.trainingDate = trainingDate; }
    public Double getCutoffScore() { return cutoffScore; }
    public void setCutoffScore(Double cutoffScore) { this.cutoffScore = cutoffScore; }
    public String getCoefficientJson() { return coefficientJson; }
    public void setCoefficientJson(String coefficientJson) { this.coefficientJson = coefficientJson; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

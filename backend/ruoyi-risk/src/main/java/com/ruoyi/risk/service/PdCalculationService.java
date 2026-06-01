package com.ruoyi.risk.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.risk.domain.PdModel;
import com.ruoyi.risk.domain.PdScoreRecord;
import com.ruoyi.risk.mapper.PdModelMapper;
import com.ruoyi.risk.mapper.PdScoreRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PdCalculationService {

    @Autowired
    private PdModelMapper pdModelMapper;
    @Autowired
    private PdScoreRecordMapper pdScoreRecordMapper;

    /**
     * Logistic回归PD计算
     * PD = 1 / (1 + exp(-(b0 + b1*x1 + b2*x2 + ...)))
     */
    public double calculateLogisticPD(Map<String, Double> variables, Map<String, Double> coefficients, double intercept) {
        double z = intercept;
        for (Map.Entry<String, Double> entry : variables.entrySet()) {
            Double coef = coefficients.get(entry.getKey());
            if (coef != null) {
                z += coef * entry.getValue();
            }
        }
        return 1.0 / (1.0 + Math.exp(-z));
    }

    /**
     * 评分卡分数计算
     * Score = Offset + Factor * ln(odds)
     * 标准: PDO=20, BaseScore=600, BaseOdds=50
     */
    public int calculateScorecardScore(double pd, int pdo, int baseScore, double baseOdds) {
        if (pd <= 0) pd = 0.0001;
        if (pd >= 1) pd = 0.9999;
        double factor = pdo / Math.log(2);
        double offset = baseScore - factor * Math.log(baseOdds);
        double odds = (1 - pd) / pd;
        return (int) Math.round(offset + factor * Math.log(odds));
    }

    /**
     * WOE(Weight of Evidence)编码
     * WOE = ln(%Good / %Bad)
     */
    public Map<String, Double> calculateWOE(List<Double> values, List<Integer> defaultFlags, int bins) {
        if (values.size() != defaultFlags.size() || values.isEmpty()) return Collections.emptyMap();

        // 排序并分箱
        double min = values.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double max = values.stream().mapToDouble(Double::doubleValue).max().orElse(1);
        double binWidth = (max - min) / bins;

        int[] goodCount = new int[bins];
        int[] badCount = new int[bins];
        int totalGood = 0, totalBad = 0;

        for (int i = 0; i < values.size(); i++) {
            int binIdx = (int) ((values.get(i) - min) / binWidth);
            if (binIdx >= bins) binIdx = bins - 1;
            if (defaultFlags.get(i) == 1) {
                badCount[binIdx]++;
                totalBad++;
            } else {
                goodCount[binIdx]++;
                totalGood++;
            }
        }

        Map<String, Double> woeMap = new LinkedHashMap<>();
        for (int i = 0; i < bins; i++) {
            double goodPct = totalGood > 0 ? (double) goodCount[i] / totalGood : 0.0001;
            double badPct = totalBad > 0 ? (double) badCount[i] / totalBad : 0.0001;
            double woe = Math.log(goodPct / badPct);
            String label = String.format("%.0f-%.0f", min + i * binWidth, min + (i + 1) * binWidth);
            woeMap.put(label, woe);
        }
        return woeMap;
    }

    /**
     * IV(Information Value)信息值
     * IV = Σ (%Good - %Bad) * WOE
     */
    public double calculateIV(List<Double> values, List<Integer> defaultFlags, int bins) {
        if (values.size() != defaultFlags.size() || values.isEmpty()) return 0;

        double min = values.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double max = values.stream().mapToDouble(Double::doubleValue).max().orElse(1);
        double binWidth = (max - min) / bins;

        int[] goodCount = new int[bins];
        int[] badCount = new int[bins];
        int totalGood = 0, totalBad = 0;

        for (int i = 0; i < values.size(); i++) {
            int binIdx = (int) ((values.get(i) - min) / binWidth);
            if (binIdx >= bins) binIdx = bins - 1;
            if (defaultFlags.get(i) == 1) { badCount[binIdx]++; totalBad++; }
            else { goodCount[binIdx]++; totalGood++; }
        }

        double iv = 0;
        for (int i = 0; i < bins; i++) {
            double goodPct = totalGood > 0 ? (double) goodCount[i] / totalGood : 0.0001;
            double badPct = totalBad > 0 ? (double) badCount[i] / totalBad : 0.0001;
            iv += (goodPct - badPct) * Math.log(goodPct / badPct);
        }
        return iv;
    }

    /**
     * Merton模型PD计算
     * d1 = [ln(V/D) + (r + σ²/2)T] / (σ√T)
     * d2 = d1 - σ√T
     * PD = N(-d2)
     */
    public double calculateMertonPD(double assetValue, double debtValue, double riskFreeRate,
                                     double assetVolatility, double timeToMaturity) {
        if (debtValue <= 0 || assetValue <= 0 || assetVolatility <= 0 || timeToMaturity <= 0) return 0.5;
        double d1 = (Math.log(assetValue / debtValue) + (riskFreeRate + assetVolatility * assetVolatility / 2) * timeToMaturity)
                    / (assetVolatility * Math.sqrt(timeToMaturity));
        double d2 = d1 - assetVolatility * Math.sqrt(timeToMaturity);
        return normalCDF(-d2);
    }

    /**
     * 评级等级映射
     */
    public String mapRatingGrade(double pd) {
        if (pd < 0.001) return "AAA";
        if (pd < 0.005) return "AA";
        if (pd < 0.01) return "A";
        if (pd < 0.03) return "BBB";
        if (pd < 0.05) return "BB";
        if (pd < 0.1) return "B";
        if (pd < 0.2) return "CCC";
        if (pd < 0.5) return "CC";
        if (pd < 0.8) return "C";
        return "D";
    }

    /**
     * 分数风险等级
     */
    public String mapScoreLevel(double pd) {
        if (pd < 0.01) return "低风险";
        if (pd < 0.05) return "中风险";
        if (pd < 0.2) return "高风险";
        return "极高风险";
    }

    /**
     * 单个企业评分
     */
    public PdScoreRecord scoreCompany(Long modelId, Long companyId, String companyName, Long loanId,
                                       Map<String, Double> variables) {
        PdModel model = pdModelMapper.selectById(modelId);
        if (model == null) return null;

        JSONObject coefficients = JSON.parseObject(model.getCoefficientJson());
        double intercept = coefficients.getDoubleValue("intercept");
        Map<String, Double> coefMap = new HashMap<>();
        for (String key : coefficients.keySet()) {
            if (!"intercept".equals(key)) {
                coefMap.put(key, coefficients.getDoubleValue(key));
            }
        }

        double pd;
        int rawScore = 0;

        if ("SCORECARD".equals(model.getModelType())) {
            pd = calculateLogisticPD(variables, coefMap, intercept);
            rawScore = calculateScorecardScore(pd, 20, 600, 50);
        } else if ("MERTON".equals(model.getModelType())) {
            pd = calculateMertonPD(
                variables.getOrDefault("asset_value", 1000.0),
                variables.getOrDefault("debt_value", 500.0),
                variables.getOrDefault("risk_free_rate", 0.03),
                variables.getOrDefault("asset_volatility", 0.2),
                variables.getOrDefault("time_to_maturity", 1.0)
            );
        } else {
            pd = calculateLogisticPD(variables, coefMap, intercept);
        }

        PdScoreRecord record = new PdScoreRecord();
        record.setCompanyId(companyId);
        record.setCompanyName(companyName);
        record.setLoanId(loanId);
        record.setModelId(modelId);
        record.setScoreDate(new Date());
        record.setRawScore((double) rawScore);
        record.setProbability(pd);
        record.setRatingGrade(mapRatingGrade(pd));
        record.setScoreLevel(mapScoreLevel(pd));
        record.setVariableDetail(JSON.toJSONString(variables));
        pdScoreRecordMapper.insert(record);

        return record;
    }

    /**
     * 批量评分
     */
    public int batchScore(Long modelId, String scoreDate) {
        // 获取所有企业（简化实现，实际应从loan_application获取）
        return 0; // 需要集成企业列表
    }

    private double normalCDF(double x) {
        return 0.5 * (1 + erf(x / Math.sqrt(2)));
    }

    private double erf(double x) {
        double t = 1.0 / (1.0 + 0.3275911 * Math.abs(x));
        double y = 1.0 - (((((1.061405429 * t - 1.453152027) * t) + 1.421413741) * t - 0.284496736) * t + 0.254829592) * t * Math.exp(-x * x);
        return x >= 0 ? y : -y;
    }
}

package com.ruoyi.risk.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.risk.domain.StressTestScenario;
import com.ruoyi.risk.domain.StressTestResult;
import com.ruoyi.risk.mapper.StressTestScenarioMapper;
import com.ruoyi.risk.mapper.StressTestResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class StressTestService {

    @Autowired
    private StressTestScenarioMapper scenarioMapper;
    @Autowired
    private StressTestResultMapper resultMapper;

    // 冲击传导系数
    private static final double GDP_PD_SENSITIVITY = -0.3;      // GDP每降1%, PD升0.3%
    private static final double RATE_PD_SENSITIVITY = 0.05;      // 利率每升1bp, PD升0.05%
    private static final double HOUSE_LGD_SENSITIVITY = -0.5;    // 房价每降1%, LGD升0.5%
    private static final double UNEMPLOYMENT_PD_SENSITIVITY = 0.2; // 失业率每升1%, PD升0.2%

    /**
     * 执行压力测试
     */
    public StressTestResult runTest(Long scenarioId, String testDate, String portfolioType) {
        StressTestScenario scenario = scenarioMapper.selectById(scenarioId);
        if (scenario == null) return null;

        JSONObject params = JSON.parseObject(scenario.getParametersJson());

        // 基准损失（假设为贷款余额的2%）
        BigDecimal portfolioSize = BigDecimal.valueOf(100000000); // 1亿
        BigDecimal baselineLossRate = BigDecimal.valueOf(0.02);
        BigDecimal baselineLoss = portfolioSize.multiply(baselineLossRate);

        // 计算冲击后的损失率
        double gdpShock = params.containsKey("gdp_shock") ? params.getDouble("gdp_shock") : 0;
        double rateShock = params.containsKey("rate_shock") ? params.getDouble("rate_shock") : 0;
        double houseShock = params.containsKey("house_price_shock") ? params.getDouble("house_price_shock") : 0;
        double unemploymentShock = params.containsKey("unemployment_shock") ? params.getDouble("unemployment_shock") : 0;

        // PD冲击: 基准PD + 各因子冲击
        double basePD = 0.02;
        double shockedPD = basePD
            + (gdpShock * GDP_PD_SENSITIVITY / 100)
            + (rateShock * RATE_PD_SENSITIVITY / 10000)
            + (unemploymentShock * UNEMPLOYMENT_PD_SENSITIVITY / 100);
        shockedPD = Math.max(0.001, Math.min(0.99, shockedPD));

        // LGD冲击
        double baseLGD = 0.45;
        double shockedLGD = baseLGD + (houseShock * HOUSE_LGD_SENSITIVITY / 100);
        shockedLGD = Math.max(0.1, Math.min(0.95, shockedLGD));

        // 压力损失 = EAD × shockedPD × shockedLGD
        BigDecimal stressedLossRate = BigDecimal.valueOf(shockedPD * shockedLGD / (basePD * baseLGD))
            .multiply(baselineLossRate);
        BigDecimal stressedLoss = portfolioSize.multiply(stressedLossRate).setScale(2, RoundingMode.HALF_UP);

        // 损失增量
        BigDecimal lossIncrease = stressedLoss.subtract(baselineLoss);
        BigDecimal lossIncreaseRate = baselineLoss.compareTo(BigDecimal.ZERO) > 0 ?
            lossIncrease.divide(baselineLoss, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) : BigDecimal.ZERO;

        // 资本影响（假设资本5亿）
        BigDecimal capital = BigDecimal.valueOf(500000000);
        BigDecimal capitalAfter = capital.subtract(lossIncrease);
        BigDecimal capitalRatio = capitalAfter.divide(portfolioSize, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        // 保存结果
        StressTestResult result = new StressTestResult();
        result.setScenarioId(scenarioId);
        result.setTestDate(java.sql.Date.valueOf(testDate));
        result.setPortfolioType(portfolioType);
        result.setBaselineLoss(baselineLoss);
        result.setStressedLoss(stressedLoss);
        result.setLossIncrease(lossIncrease);
        result.setLossIncreaseRate(lossIncreaseRate);
        result.setCapitalImpact(lossIncrease);
        result.setCapitalRatioAfter(capitalRatio);
        result.setPassFail(capitalRatio.compareTo(BigDecimal.valueOf(8)) >= 0 ? "通过" : "未通过");

        resultMapper.insert(result);
        return result;
    }

    /**
     * 批量执行所有场景
     */
    public List<StressTestResult> runAllScenarios(String testDate, String portfolioType) {
        List<StressTestScenario> scenarios = scenarioMapper.selectList(new StressTestScenario());
        List<StressTestResult> results = new ArrayList<>();
        for (StressTestScenario scenario : scenarios) {
            StressTestResult result = runTest(scenario.getScenarioId(), testDate, portfolioType);
            if (result != null) results.add(result);
        }
        return results;
    }

    /**
     * 汇总
     */
    public Map<String, Object> getStressSummary(String testDate) {
        List<StressTestResult> results = resultMapper.selectByDate(testDate);
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalScenarios", results.size());
        summary.put("passed", results.stream().filter(r -> "通过".equals(r.getPassFail())).count());
        summary.put("failed", results.stream().filter(r -> "未通过".equals(r.getPassFail())).count());
        return summary;
    }
}

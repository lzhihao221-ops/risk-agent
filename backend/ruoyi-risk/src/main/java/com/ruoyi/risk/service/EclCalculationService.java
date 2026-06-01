package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.CapEcl;
import com.ruoyi.risk.mapper.CapEclMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class EclCalculationService {

    @Autowired
    private CapEclMapper eclMapper;

    public CapEcl calculateECL(Long loanId, Long companyId, String companyName,
                                int stage, BigDecimal ead, BigDecimal lgd,
                                BigDecimal pd12m, BigDecimal pdLifetime,
                                BigDecimal effectiveRate, int remainingMonths) {
        CapEcl ecl = new CapEcl();
        ecl.setLoanId(loanId);
        ecl.setCompanyId(companyId);
        ecl.setCompanyName(companyName);
        ecl.setEad(ead);
        ecl.setLgd(lgd);
        ecl.setStage(stage);

        BigDecimal pd;
        if (stage == 1) {
            pd = pd12m;
            ecl.setPd12m(pd12m);
        } else {
            pd = pdLifetime != null ? pdLifetime : estimateLifetimePD(pd12m, remainingMonths);
            ecl.setPdLifetime(pd);
        }

        BigDecimal df = calculateDiscountFactor(effectiveRate, remainingMonths);

        BigDecimal eclAmount = pd.multiply(lgd).multiply(ead).multiply(df)
                                 .divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP);
        ecl.setEclAmount(eclAmount);
        ecl.setCalcMethod("三阶段模型");

        return ecl;
    }

    public BigDecimal estimateLifetimePD(BigDecimal pdAnnual, int remainingMonths) {
        double T = remainingMonths / 12.0;
        double pd = 1.0 - Math.pow(1.0 - pdAnnual.doubleValue(), T);
        return BigDecimal.valueOf(pd).setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateDiscountFactor(BigDecimal effectiveRate, int remainingMonths) {
        double r = effectiveRate.doubleValue() / 100;
        double t = remainingMonths / 12.0;
        double df = 1.0 / Math.pow(1 + r, t);
        return BigDecimal.valueOf(df).setScale(6, RoundingMode.HALF_UP);
    }

    public int determineStage(int overdueDays, boolean isRestructured, boolean isBankrupt) {
        if (isBankrupt || overdueDays > 90 || isRestructured) return 3;
        if (overdueDays > 30) return 2;
        return 1;
    }

    public int batchCalculate(String calcDate) {
        return 0;
    }

    public Map<String, Object> getEclSummary(String calcDate) {
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("第一阶段", Map.of("count", 0, "eclSum", BigDecimal.ZERO));
        summary.put("第二阶段", Map.of("count", 0, "eclSum", BigDecimal.ZERO));
        summary.put("第三阶段", Map.of("count", 0, "eclSum", BigDecimal.ZERO));
        return summary;
    }
}

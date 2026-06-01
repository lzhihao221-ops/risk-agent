package com.ruoyi.risk.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.risk.domain.CapRwa;
import com.ruoyi.risk.mapper.RwaRiskWeightMapper;
import com.ruoyi.risk.mapper.CapRwaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class RwaCalculationService {

    @Autowired
    private RwaRiskWeightMapper weightMapper;
    @Autowired
    private CapRwaMapper rwaMapper;

    public CapRwa calculateRWA(String assetClass, String assetSubclass, String counterpartyType,
                                String ratingGrade, BigDecimal ead, BigDecimal ccf) {
        Map<String, Object> weight = findRiskWeight(assetClass, assetSubclass, counterpartyType, ratingGrade);

        CapRwa rwa = new CapRwa();
        rwa.setAssetClass(assetClass);
        rwa.setExposureAmount(ead);

        BigDecimal rw = weight != null && weight.get("riskWeight") != null ?
            new BigDecimal(weight.get("riskWeight").toString()) : BigDecimal.valueOf(100);
        rwa.setRiskWeight(rw);

        BigDecimal effectiveCcf = ccf != null ? ccf.divide(BigDecimal.valueOf(100)) : BigDecimal.ONE;
        BigDecimal rwDecimal = rw.divide(BigDecimal.valueOf(100));
        BigDecimal rwaAmount = ead.multiply(rwDecimal).multiply(effectiveCcf).setScale(2, RoundingMode.HALF_UP);
        rwa.setRwaAmount(rwaAmount);

        return rwa;
    }

    private Map<String, Object> findRiskWeight(String assetClass, String assetSubclass,
                                                String counterpartyType, String ratingGrade) {
        Map<String, Object> weight = weightMapper.selectByExact(assetClass, assetSubclass, counterpartyType, ratingGrade);
        if (weight != null) return weight;
        weight = weightMapper.selectByClass(assetClass, ratingGrade);
        if (weight != null) return weight;
        Map<String, Object> defaultWeight = new HashMap<>();
        defaultWeight.put("riskWeight", 100);
        defaultWeight.put("ccf", 100);
        return defaultWeight;
    }

    public int batchCalculate(String calcDate) {
        return 0;
    }

    public Map<String, Object> getRwaSummary(String calcDate) {
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalRwa", BigDecimal.ZERO);
        summary.put("details", new ArrayList<>());
        return summary;
    }
}

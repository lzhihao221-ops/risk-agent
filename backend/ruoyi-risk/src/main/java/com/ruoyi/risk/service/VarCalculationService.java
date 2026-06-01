package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.VarCalculation;
import com.ruoyi.risk.mapper.VarCalculationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VarCalculationService {

    @Autowired
    private VarCalculationMapper varMapper;

    public Map<String, BigDecimal> historicalVaR(List<Double> returns, BigDecimal portfolioValue,
                                                  double confidenceLevel, int timeHorizon) {
        List<Double> sorted = returns.stream().sorted().collect(Collectors.toList());
        int index = (int) Math.ceil((1 - confidenceLevel) * sorted.size()) - 1;
        index = Math.max(0, Math.min(index, sorted.size() - 1));
        double quantile = sorted.get(index);

        double dailyVaR = -quantile * portfolioValue.doubleValue();
        double adjustedVaR = dailyVaR * Math.sqrt(timeHorizon);

        double sum = 0;
        for (int i = 0; i <= index; i++) sum += sorted.get(i);
        double avgLoss = (index + 1) > 0 ? sum / (index + 1) : quantile;
        double cvar = -avgLoss * portfolioValue.doubleValue() * Math.sqrt(timeHorizon);

        Map<String, BigDecimal> result = new HashMap<>();
        result.put("var", BigDecimal.valueOf(adjustedVaR).setScale(2, RoundingMode.HALF_UP));
        result.put("cvar", BigDecimal.valueOf(cvar).setScale(2, RoundingMode.HALF_UP));
        result.put("mean", BigDecimal.valueOf(mean(returns)).setScale(6, RoundingMode.HALF_UP));
        result.put("volatility", BigDecimal.valueOf(stdDev(returns)).setScale(6, RoundingMode.HALF_UP));
        return result;
    }

    public Map<String, BigDecimal> monteCarloVaR(List<Double> historicalReturns, BigDecimal portfolioValue,
                                                  double confidenceLevel, int timeHorizon, int simulations) {
        double mu = mean(historicalReturns);
        double sigma = stdDev(historicalReturns);

        Random rand = new Random(42);
        List<Double> simulatedReturns = new ArrayList<>();
        double dt = 1.0 / 252;

        for (int i = 0; i < simulations; i++) {
            double cumulativeReturn = 0;
            for (int day = 0; day < timeHorizon; day++) {
                double z = rand.nextGaussian();
                double dailyReturn = (mu - sigma * sigma / 2) * dt + sigma * Math.sqrt(dt) * z;
                cumulativeReturn += dailyReturn;
            }
            simulatedReturns.add(cumulativeReturn);
        }

        simulatedReturns.sort(null);
        int index = (int) Math.ceil((1 - confidenceLevel) * simulations) - 1;
        index = Math.max(0, index);

        double var = -simulatedReturns.get(index) * portfolioValue.doubleValue();

        double sum = 0;
        for (int i = 0; i <= index; i++) sum += simulatedReturns.get(i);
        double cvar = -(sum / (index + 1)) * portfolioValue.doubleValue();

        Map<String, BigDecimal> result = new HashMap<>();
        result.put("var", BigDecimal.valueOf(var).setScale(2, RoundingMode.HALF_UP));
        result.put("cvar", BigDecimal.valueOf(cvar).setScale(2, RoundingMode.HALF_UP));
        result.put("simulations", BigDecimal.valueOf(simulations));
        return result;
    }

    public Map<String, BigDecimal> parametricVaR(List<Double> returns, BigDecimal portfolioValue,
                                                  double confidenceLevel, int timeHorizon) {
        double mu = mean(returns);
        double sigma = stdDev(returns);

        double z;
        if (confidenceLevel >= 0.99) z = 2.326;
        else if (confidenceLevel >= 0.95) z = 1.645;
        else if (confidenceLevel >= 0.90) z = 1.282;
        else z = 1.645;

        double dailyVaR = (z * sigma - mu) * portfolioValue.doubleValue();
        double var = dailyVaR * Math.sqrt(timeHorizon);

        double phi_z = Math.exp(-z * z / 2) / Math.sqrt(2 * Math.PI);
        double es = sigma * phi_z / (1 - confidenceLevel) * portfolioValue.doubleValue() * Math.sqrt(timeHorizon);

        Map<String, BigDecimal> result = new HashMap<>();
        result.put("var", BigDecimal.valueOf(var).setScale(2, RoundingMode.HALF_UP));
        result.put("cvar", BigDecimal.valueOf(es).setScale(2, RoundingMode.HALF_UP));
        result.put("mean", BigDecimal.valueOf(mu).setScale(6, RoundingMode.HALF_UP));
        result.put("volatility", BigDecimal.valueOf(sigma).setScale(6, RoundingMode.HALF_UP));
        result.put("skewness", BigDecimal.valueOf(skewness(returns)).setScale(4, RoundingMode.HALF_UP));
        result.put("kurtosis", BigDecimal.valueOf(kurtosis(returns)).setScale(4, RoundingMode.HALF_UP));
        return result;
    }

    public void saveVarRecord(VarCalculation record) {
        varMapper.insert(record);
    }

    private double mean(List<Double> v) { return v.stream().mapToDouble(Double::doubleValue).average().orElse(0); }
    private double stdDev(List<Double> v) { double m = mean(v); return Math.sqrt(v.stream().mapToDouble(x -> Math.pow(x - m, 2)).average().orElse(0)); }
    private double skewness(List<Double> v) { double m = mean(v), s = stdDev(v); if (s==0) return 0; double n=v.size(); return n/((n-1)*(n-2))*v.stream().mapToDouble(x->Math.pow((x-m)/s,3)).sum(); }
    private double kurtosis(List<Double> v) { double m = mean(v), s = stdDev(v); if (s==0) return 0; return v.stream().mapToDouble(x->Math.pow((x-m)/s,4)).average().orElse(0)-3; }
}

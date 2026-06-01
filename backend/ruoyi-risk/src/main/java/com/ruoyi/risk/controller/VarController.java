package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.risk.domain.VarCalculation;
import com.ruoyi.risk.mapper.VarCalculationMapper;
import com.ruoyi.risk.service.VarCalculationService;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/var")
public class VarController extends BaseController {

    @Autowired
    private VarCalculationMapper varMapper;
    @Autowired
    private VarCalculationService varService;

    @PreAuthorize("@ss.hasPermi('var:calc:list')")
    @GetMapping("/record/list")
    public TableDataInfo list(VarCalculation query) {
        startPage();
        return getDataTable(varMapper.selectList(query));
    }

    @PreAuthorize("@ss.hasPermi('var:calc:calculate')")
    @PostMapping("/historical")
    public AjaxResult historical(@RequestBody JSONObject params) {
        List<Double> returns = params.getList("returns", Double.class);
        BigDecimal portfolioValue = params.getBigDecimal("portfolioValue");
        double confidence = params.containsKey("confidenceLevel") ? params.getDouble("confidenceLevel") : 0.95;
        int horizon = params.containsKey("timeHorizon") ? params.getIntValue("timeHorizon") : 1;
        Map<String, BigDecimal> result = varService.historicalVaR(returns, portfolioValue, confidence, horizon);
        return success(result);
    }

    @PreAuthorize("@ss.hasPermi('var:calc:calculate')")
    @PostMapping("/monteCarlo")
    public AjaxResult monteCarlo(@RequestBody JSONObject params) {
        List<Double> returns = params.getList("returns", Double.class);
        BigDecimal portfolioValue = params.getBigDecimal("portfolioValue");
        double confidence = params.containsKey("confidenceLevel") ? params.getDouble("confidenceLevel") : 0.95;
        int horizon = params.containsKey("timeHorizon") ? params.getIntValue("timeHorizon") : 1;
        int simulations = params.containsKey("simulations") ? params.getIntValue("simulations") : 10000;
        Map<String, BigDecimal> result = varService.monteCarloVaR(returns, portfolioValue, confidence, horizon, simulations);
        return success(result);
    }

    @PreAuthorize("@ss.hasPermi('var:calc:calculate')")
    @PostMapping("/parametric")
    public AjaxResult parametric(@RequestBody JSONObject params) {
        List<Double> returns = params.getList("returns", Double.class);
        BigDecimal portfolioValue = params.getBigDecimal("portfolioValue");
        double confidence = params.containsKey("confidenceLevel") ? params.getDouble("confidenceLevel") : 0.95;
        int horizon = params.containsKey("timeHorizon") ? params.getIntValue("timeHorizon") : 1;
        Map<String, BigDecimal> result = varService.parametricVaR(returns, portfolioValue, confidence, horizon);
        return success(result);
    }
}

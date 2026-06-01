package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.risk.domain.StressTestScenario;
import com.ruoyi.risk.domain.StressTestResult;
import com.ruoyi.risk.mapper.StressTestScenarioMapper;
import com.ruoyi.risk.mapper.StressTestResultMapper;
import com.ruoyi.risk.service.StressTestService;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stress")
public class StressTestController extends BaseController {

    @Autowired
    private StressTestScenarioMapper scenarioMapper;
    @Autowired
    private StressTestResultMapper resultMapper;
    @Autowired
    private StressTestService stressTestService;

    @PreAuthorize("@ss.hasPermi('stress:scenario:list')")
    @GetMapping("/scenario/list")
    public TableDataInfo scenarioList(StressTestScenario query) {
        startPage();
        return getDataTable(scenarioMapper.selectList(query));
    }

    @PreAuthorize("@ss.hasPermi('stress:scenario:add')")
    @PostMapping("/scenario")
    public AjaxResult addScenario(@RequestBody StressTestScenario record) {
        return toAjax(scenarioMapper.insert(record));
    }

    @PreAuthorize("@ss.hasPermi('stress:scenario:edit')")
    @PutMapping("/scenario")
    public AjaxResult editScenario(@RequestBody StressTestScenario record) {
        return toAjax(scenarioMapper.update(record));
    }

    @PreAuthorize("@ss.hasPermi('stress:scenario:remove')")
    @DeleteMapping("/scenario/{ids}")
    public AjaxResult removeScenario(@PathVariable Long[] ids) {
        return toAjax(scenarioMapper.deleteByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('stress:result:list')")
    @PostMapping("/run")
    public AjaxResult runTest(@RequestBody JSONObject params) {
        Long scenarioId = params.getLong("scenarioId");
        String testDate = params.getString("testDate");
        String portfolioType = params.getString("portfolioType");
        StressTestResult result = stressTestService.runTest(scenarioId, testDate, portfolioType);
        return success(result);
    }

    @PreAuthorize("@ss.hasPermi('stress:result:list')")
    @PostMapping("/runAll")
    public AjaxResult runAll(@RequestBody JSONObject params) {
        String testDate = params.getString("testDate");
        String portfolioType = params.getString("portfolioType");
        return success(stressTestService.runAllScenarios(testDate, portfolioType));
    }

    @PreAuthorize("@ss.hasPermi('stress:result:list')")
    @GetMapping("/result/list")
    public TableDataInfo resultList(StressTestResult query) {
        startPage();
        return getDataTable(resultMapper.selectList(query));
    }

    @PreAuthorize("@ss.hasPermi('stress:result:list')")
    @GetMapping("/summary")
    public AjaxResult summary(@RequestParam String testDate) {
        return success(stressTestService.getStressSummary(testDate));
    }
}

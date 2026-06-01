package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.risk.domain.LoanApplication;
import com.ruoyi.risk.service.ILoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/loan/application")
public class LoanApplicationController extends BaseController {
    @Autowired
    private ILoanApplicationService service;

    @PreAuthorize("@ss.hasPermi('loan:application:list')")
    @GetMapping("/list")
    public TableDataInfo list(LoanApplication query) {
        startPage();
        List<LoanApplication> list = service.selectList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('loan:application:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(service.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('loan:application:add')")
    @PostMapping
    public AjaxResult add(@RequestBody LoanApplication app) {
        return toAjax(service.insert(app));
    }

    @PreAuthorize("@ss.hasPermi('loan:application:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody LoanApplication app) {
        return toAjax(service.update(app));
    }

    @PreAuthorize("@ss.hasPermi('loan:application:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteByIds(ids));
    }

    @GetMapping("/statusStats")
    public AjaxResult statusStats() { return success(service.selectStatusStats()); }

    @GetMapping("/typeStats")
    public AjaxResult typeStats() { return success(service.selectTypeStats()); }
}

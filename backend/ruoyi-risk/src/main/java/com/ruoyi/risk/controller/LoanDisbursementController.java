package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.risk.domain.LoanDisbursement;
import com.ruoyi.risk.service.ILoanDisbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/loan/disburse")
public class LoanDisbursementController extends BaseController {
    @Autowired
    private ILoanDisbursementService service;

    @PreAuthorize("@ss.hasPermi('loan:disburse:list')")
    @GetMapping("/list")
    public TableDataInfo list(LoanDisbursement query) {
        startPage();
        List<LoanDisbursement> list = service.selectList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('loan:disburse:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(service.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('loan:disburse:add')")
    @PostMapping
    public AjaxResult add(@RequestBody LoanDisbursement record) {
        return toAjax(service.insert(record));
    }

    @PreAuthorize("@ss.hasPermi('loan:disburse:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody LoanDisbursement record) {
        return toAjax(service.update(record));
    }

    @PreAuthorize("@ss.hasPermi('loan:disburse:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('loan:disburse:edit')")
    @PutMapping("/approve/{id}")
    public AjaxResult approve(@PathVariable Long id) {
        return toAjax(service.approve(id, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('loan:disburse:edit')")
    @PutMapping("/reject/{id}")
    public AjaxResult reject(@PathVariable Long id, @RequestParam String reason) {
        return toAjax(service.reject(id, getUsername(), reason));
    }

    @PreAuthorize("@ss.hasPermi('loan:disburse:edit')")
    @PutMapping("/disburse/{id}")
    public AjaxResult disburse(@PathVariable Long id) {
        return toAjax(service.disburse(id, getUsername()));
    }
}

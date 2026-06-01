package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.risk.domain.LoanLedger;
import com.ruoyi.risk.service.ILoanLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/loan/ledger")
public class LoanLedgerController extends BaseController {
    @Autowired
    private ILoanLedgerService service;

    @PreAuthorize("@ss.hasPermi('loan:ledger:list')")
    @GetMapping("/list")
    public TableDataInfo list(LoanLedger query) {
        startPage();
        List<LoanLedger> list = service.selectList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('loan:ledger:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(service.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('loan:ledger:add')")
    @PostMapping
    public AjaxResult add(@RequestBody LoanLedger ledger) {
        return toAjax(service.insert(ledger));
    }

    @PreAuthorize("@ss.hasPermi('loan:ledger:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody LoanLedger ledger) {
        return toAjax(service.update(ledger));
    }

    @PreAuthorize("@ss.hasPermi('loan:ledger:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteByIds(ids));
    }

    @GetMapping("/categoryStats")
    public AjaxResult categoryStats() { return success(service.selectCategoryStats()); }

    @GetMapping("/overdueStats")
    public AjaxResult overdueStats() { return success(service.selectOverdueStats()); }

    @GetMapping("/summary")
    public AjaxResult summary() { return success(service.selectSummary()); }
}

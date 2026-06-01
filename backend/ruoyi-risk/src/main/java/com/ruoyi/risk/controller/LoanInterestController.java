package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.risk.domain.LoanInterestAccrual;
import com.ruoyi.risk.service.ILoanInterestAccrualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/loan/interest")
public class LoanInterestController extends BaseController {
    @Autowired
    private ILoanInterestAccrualService service;

    @PreAuthorize("@ss.hasPermi('loan:interest:list')")
    @GetMapping("/list")
    public TableDataInfo list(LoanInterestAccrual query) {
        startPage();
        List<LoanInterestAccrual> list = service.selectList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('loan:interest:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(service.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('loan:interest:list')")
    @GetMapping("/loan/{loanId}")
    public AjaxResult getByLoanId(@PathVariable Long loanId) {
        return success(service.selectByLoanId(loanId));
    }

    @PreAuthorize("@ss.hasPermi('loan:interest:add')")
    @PostMapping
    public AjaxResult add(@RequestBody LoanInterestAccrual record) {
        return toAjax(service.insert(record));
    }

    @PreAuthorize("@ss.hasPermi('loan:interest:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteByIds(ids));
    }
}

package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.risk.domain.LoanApproval;
import com.ruoyi.risk.domain.LoanScorecard;
import com.ruoyi.risk.domain.LoanCollateral;
import com.ruoyi.risk.domain.LoanRepayment;
import com.ruoyi.risk.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanExtraController extends BaseController {

    @Autowired private LoanApprovalMapper approvalMapper;
    @Autowired private LoanScorecardMapper scorecardMapper;
    @Autowired private LoanCollateralMapper collateralMapper;
    @Autowired private LoanRepaymentMapper repaymentMapper;

    @GetMapping("/approval/{appId}")
    public AjaxResult getApprovals(@PathVariable Long appId) {
        return success(approvalMapper.selectByAppId(appId));
    }

    @PostMapping("/approval")
    public AjaxResult addApproval(@RequestBody LoanApproval approval) {
        return toAjax(approvalMapper.insert(approval));
    }

    @GetMapping("/scorecard/list")
    public AjaxResult scorecardList() {
        return success(scorecardMapper.selectList(new LoanScorecard()));
    }

    @GetMapping("/scorecard/{companyId}")
    public AjaxResult getLatestScore(@PathVariable Long companyId) {
        return success(scorecardMapper.selectLatestByCompanyId(companyId));
    }

    @PostMapping("/scorecard")
    public AjaxResult addScorecard(@RequestBody LoanScorecard sc) {
        return toAjax(scorecardMapper.insert(sc));
    }

    @GetMapping("/collateral/list")
    public AjaxResult collateralList() {
        return success(collateralMapper.selectList(new LoanCollateral()));
    }

    @GetMapping("/collateral/{companyId}")
    public AjaxResult getCollateralByCompany(@PathVariable Long companyId) {
        return success(collateralMapper.selectByCompanyId(companyId));
    }

    @PostMapping("/collateral")
    public AjaxResult addCollateral(@RequestBody LoanCollateral c) {
        return toAjax(collateralMapper.insert(c));
    }

    @GetMapping("/repayment/{loanId}")
    public AjaxResult getRepayments(@PathVariable Long loanId) {
        return success(repaymentMapper.selectByLoanId(loanId));
    }

    @PostMapping("/repayment")
    public AjaxResult addRepayment(@RequestBody LoanRepayment r) {
        return toAjax(repaymentMapper.insert(r));
    }
}

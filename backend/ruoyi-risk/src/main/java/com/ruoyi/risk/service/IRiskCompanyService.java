package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.RiskCompany;
import java.util.List;
import java.util.Map;

/**
 * 企业信息 服务接口
 */
public interface IRiskCompanyService {

    RiskCompany selectRiskCompanyById(Long id);

    List<RiskCompany> selectRiskCompanyList(RiskCompany company);

    int insertRiskCompany(RiskCompany company);

    int updateRiskCompany(RiskCompany company);

    int deleteRiskCompanyByIds(Long[] ids);

    /** 根据信用代码查询 */
    RiskCompany selectByCreditCode(String creditCode);

    /** 风险等级分布统计 */
    List<Map<String, Object>> selectRiskLevelStats();

    /** 高风险企业TOP榜 */
    List<RiskCompany> selectHighRiskCompanies(int limit);

    /** 触发企业风险扫描（调用外部数据源） */
    void scanCompanyRisk(Long companyId);
}

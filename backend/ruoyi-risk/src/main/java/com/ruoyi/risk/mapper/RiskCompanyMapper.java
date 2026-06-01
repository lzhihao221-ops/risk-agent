package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.RiskCompany;
import java.util.List;

/**
 * 企业信息Mapper接口
 */
public interface RiskCompanyMapper {

    RiskCompany selectRiskCompanyById(Long id);

    List<RiskCompany> selectRiskCompanyList(RiskCompany company);

    int insertRiskCompany(RiskCompany company);

    int updateRiskCompany(RiskCompany company);

    int deleteRiskCompanyByIds(Long[] ids);

    /** 根据信用代码查询 */
    RiskCompany selectByCreditCode(String creditCode);

    /** 统计各风险等级数量 */
    List<java.util.Map<String, Object>> selectRiskLevelStats();

    /** 查询高风险企业列表 */
    List<RiskCompany> selectHighRiskCompanies(int limit);
}

package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.RiskCompany;
import com.ruoyi.risk.mapper.RiskCompanyMapper;
import com.ruoyi.risk.service.IRiskCompanyService;
import com.ruoyi.risk.mapper.RiskCompanyRelationMapper;
import com.ruoyi.risk.mapper.RiskEventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 企业信息 服务实现
 */
@Service
public class RiskCompanyServiceImpl implements IRiskCompanyService {

    @Autowired
    private RiskCompanyMapper companyMapper;

    @Autowired
    private RiskEventMapper eventMapper;

    @Autowired
    private RiskCompanyRelationMapper relationMapper;

    @Override
    public RiskCompany selectRiskCompanyById(Long id) {
        return companyMapper.selectRiskCompanyById(id);
    }

    @Override
    public List<RiskCompany> selectRiskCompanyList(RiskCompany company) {
        return companyMapper.selectRiskCompanyList(company);
    }

    @Override
    public int insertRiskCompany(RiskCompany company) {
        return companyMapper.insertRiskCompany(company);
    }

    @Override
    public int updateRiskCompany(RiskCompany company) {
        return companyMapper.updateRiskCompany(company);
    }

    @Override
    public int deleteRiskCompanyByIds(Long[] ids) {
        return companyMapper.deleteRiskCompanyByIds(ids);
    }

    @Override
    public RiskCompany selectByCreditCode(String creditCode) {
        return companyMapper.selectByCreditCode(creditCode);
    }

    @Override
    public List<Map<String, Object>> selectRiskLevelStats() {
        return companyMapper.selectRiskLevelStats();
    }

    @Override
    public List<RiskCompany> selectHighRiskCompanies(int limit) {
        return companyMapper.selectHighRiskCompanies(limit);
    }

    @Override
    public void scanCompanyRisk(Long companyId) {
        RiskCompany company = companyMapper.selectRiskCompanyById(companyId);
        if (company == null) {
            return;
        }

        // 综合评分算法：基于企业信息 + 历史事件 + 关联关系
        int score = calculateCompanyScore(company);
        int level = scoreToLevel(score);

        company.setRiskScore(score);
        company.setRiskLevel(level);
        company.setLastScanTime(new Date());
        companyMapper.updateRiskCompany(company);
    }

    /**
     * 企业风险评分算法
     * 维度：基础信息(30%) + 事件风险(40%) + 关联风险(30%)
     */
    private int calculateCompanyScore(RiskCompany company) {
        // 1. 基础信息风险分 (0-30分)
        int baseScore = calculateBaseScore(company);

        // 2. 事件风险分 (0-40分)
        int eventScore = calculateEventScore(company.getId());

        // 3. 关联风险分 (0-30分)
        int relationScore = calculateRelationScore(company.getId());

        return Math.min(baseScore + eventScore + relationScore, 100);
    }

    /** 基础信息风险评分 */
    private int calculateBaseScore(RiskCompany company) {
        int score = 10;

        // 注册资本过小
        if (company.getRegCapital() != null) {
            if (company.getRegCapital().doubleValue() < 100) score += 8;
            else if (company.getRegCapital().doubleValue() < 500) score += 4;
        }

        // 成立时间过短
        if (company.getEstablishDate() != null) {
            long days = (System.currentTimeMillis() - company.getEstablishDate().getTime()) / (1000 * 60 * 60 * 24);
            if (days < 365) score += 10;
            else if (days < 730) score += 5;
        }

        // 行业风险
        if (company.getIndustry() != null) {
            String ind = company.getIndustry();
            if (ind.contains("投资") || ind.contains("金融") || ind.contains("地产")) {
                score += 5;
            } else if (ind.contains("贸易") || ind.contains("批发")) {
                score += 2;
            }
        }

        return Math.min(score, 30);
    }

    /** 事件风险评分：基于历史事件数量和严重程度 */
    private int calculateEventScore(Long companyId) {
        List<com.ruoyi.risk.domain.RiskEvent> events = eventMapper.selectByCompanyId(companyId);
        if (events.isEmpty()) return 0;

        int score = 0;
        for (com.ruoyi.risk.domain.RiskEvent event : events) {
            int severity = event.getSeverity() != null ? event.getSeverity() : 2;
            // 事件越近期权重越高
            int daysSince = 30;
            if (event.getEventDate() != null) {
                daysSince = (int) ((System.currentTimeMillis() - event.getEventDate().getTime()) / (1000 * 60 * 60 * 24));
            }
            int weight = daysSince <= 30 ? 3 : (daysSince <= 90 ? 2 : 1);
            score += severity * weight;
        }
        return Math.min(score, 40);
    }

    /** 关联风险评分 */
    private int calculateRelationScore(Long companyId) {
        List<com.ruoyi.risk.domain.RiskCompanyRelation> relations = relationMapper.selectByCompanyId(companyId);
        if (relations.isEmpty()) return 0;

        int score = 0;
        for (com.ruoyi.risk.domain.RiskCompanyRelation rel : relations) {
            // 担保链风险最高
            if ("担保链".equals(rel.getRelationType())) {
                score += 10;
            } else if ("股权穿透".equals(rel.getRelationType())) {
                score += 5;
            } else {
                score += 3;
            }
        }
        return Math.min(score, 30);
    }

    /** 评分转等级 */
    private int scoreToLevel(int score) {
        if (score >= 80) return 4; // 极高
        if (score >= 60) return 3; // 高
        if (score >= 40) return 2; // 中
        if (score >= 20) return 1; // 低
        return 0; // 未评估
    }
}

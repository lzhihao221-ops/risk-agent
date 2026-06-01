package com.ruoyi.risk.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 数据源管理器
 * 自动选择可用的数据源，支持降级和切换
 */
@Component
public class DataSourceManager {

    private static final Logger log = LoggerFactory.getLogger(DataSourceManager.class);

    @Autowired(required = false)
    private List<DataSourceAdapter> adapters;

    /** 获取第一个可用的数据源 */
    public DataSourceAdapter getAvailableSource() {
        if (adapters == null || adapters.isEmpty()) {
            log.warn("未配置任何数据源适配器");
            return null;
        }
        for (DataSourceAdapter adapter : adapters) {
            if (adapter.isAvailable()) {
                log.info("使用数据源: {}", adapter.getName());
                return adapter;
            }
        }
        log.warn("所有数据源均不可用，请检查 API 配置");
        return null;
    }

    /** 查询企业信息（自动选择数据源） */
    public Map<String, Object> queryCompanyInfo(String keyword) {
        DataSourceAdapter source = getAvailableSource();
        if (source == null) {
            return Map.of("error", "无可用数据源，请在系统配置中填写 API Key");
        }
        return source.queryCompanyInfo(keyword);
    }

    /** 查询被执行信息 */
    public Map<String, Object> queryExecutionInfo(String companyName) {
        DataSourceAdapter source = getAvailableSource();
        if (source == null) return Map.of("error", "无可用数据源");
        return source.queryExecutionInfo(companyName);
    }

    /** 查询失信信息 */
    public Map<String, Object> queryDishonestInfo(String companyName) {
        DataSourceAdapter source = getAvailableSource();
        if (source == null) return Map.of("error", "无可用数据源");
        return source.queryDishonestInfo(companyName);
    }

    /** 查询经营异常 */
    public Map<String, Object> queryAbnormalInfo(String companyName) {
        DataSourceAdapter source = getAvailableSource();
        if (source == null) return Map.of("error", "无可用数据源");
        return source.queryAbnormalInfo(companyName);
    }

    /** 查询诉讼信息 */
    public Map<String, Object> queryLawsuitInfo(String companyName) {
        DataSourceAdapter source = getAvailableSource();
        if (source == null) return Map.of("error", "无可用数据源");
        return source.queryLawsuitInfo(companyName);
    }

    /** 列出所有数据源状态 */
    public List<Map<String, Object>> listSources() {
        List<Map<String, Object>> result = new ArrayList<>();
        if (adapters != null) {
            for (DataSourceAdapter adapter : adapters) {
                Map<String, Object> info = new HashMap<>();
                info.put("name", adapter.getName());
                info.put("available", adapter.isAvailable());
                result.add(info);
            }
        }
        return result;
    }
}

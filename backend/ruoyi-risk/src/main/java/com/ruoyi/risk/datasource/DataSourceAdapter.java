package com.ruoyi.risk.datasource;

import java.util.Map;

/**
 * 数据源适配器接口
 * 所有外部数据源（天眼查/企查查/阿里云等）实现此接口
 */
public interface DataSourceAdapter {

    /** 数据源名称 */
    String getName();

    /** 是否可用（配置了API Key等） */
    boolean isAvailable();

    /** 查询企业基本信息 */
    Map<String, Object> queryCompanyInfo(String keyword);

    /** 查询被执行人信息 */
    Map<String, Object> queryExecutionInfo(String companyName);

    /** 查询失信被执行人信息 */
    Map<String, Object> queryDishonestInfo(String companyName);

    /** 查询经营异常信息 */
    Map<String, Object> queryAbnormalInfo(String companyName);

    /** 查询司法诉讼信息 */
    Map<String, Object> queryLawsuitInfo(String companyName);
}

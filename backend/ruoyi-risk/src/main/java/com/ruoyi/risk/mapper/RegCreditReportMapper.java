package com.ruoyi.risk.mapper;

import java.util.List;
import com.ruoyi.risk.domain.RegCreditReport;

public interface RegCreditReportMapper {
    public RegCreditReport selectById(Long id);
    public List<RegCreditReport> selectList(RegCreditReport regCreditReport);
    public int insert(RegCreditReport regCreditReport);
    public int update(RegCreditReport regCreditReport);
    public int deleteByIds(Long[] ids);
}

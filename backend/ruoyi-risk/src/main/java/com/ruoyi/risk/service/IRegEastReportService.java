package com.ruoyi.risk.service;

import java.util.List;
import com.ruoyi.risk.domain.RegEastReport;

public interface IRegEastReportService {
    public RegEastReport selectById(Long id);
    public List<RegEastReport> selectList(RegEastReport regEastReport);
    public int insert(RegEastReport regEastReport);
    public int update(RegEastReport regEastReport);
    public int deleteByIds(Long[] ids);
}

package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.RegEastReport;
import com.ruoyi.risk.mapper.RegEastReportMapper;
import com.ruoyi.risk.service.IRegEastReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegEastReportServiceImpl implements IRegEastReportService {
    @Autowired
    private RegEastReportMapper mapper;

    @Override
    public RegEastReport selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<RegEastReport> selectList(RegEastReport query) { return mapper.selectList(query); }
    @Override
    public int insert(RegEastReport record) { return mapper.insert(record); }
    @Override
    public int update(RegEastReport record) { return mapper.update(record); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
}

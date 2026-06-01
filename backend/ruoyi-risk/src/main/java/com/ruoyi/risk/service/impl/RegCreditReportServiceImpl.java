package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.RegCreditReport;
import com.ruoyi.risk.mapper.RegCreditReportMapper;
import com.ruoyi.risk.service.IRegCreditReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegCreditReportServiceImpl implements IRegCreditReportService {
    @Autowired
    private RegCreditReportMapper mapper;

    @Override
    public RegCreditReport selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<RegCreditReport> selectList(RegCreditReport query) { return mapper.selectList(query); }
    @Override
    public int insert(RegCreditReport record) { return mapper.insert(record); }
    @Override
    public int update(RegCreditReport record) { return mapper.update(record); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
}

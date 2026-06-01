package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.RegIndicator;
import com.ruoyi.risk.mapper.RegIndicatorMapper;
import com.ruoyi.risk.service.IRegIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegIndicatorServiceImpl implements IRegIndicatorService {
    @Autowired
    private RegIndicatorMapper mapper;

    @Override
    public RegIndicator selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<RegIndicator> selectList(RegIndicator query) { return mapper.selectList(query); }
    @Override
    public List<RegIndicator> selectByType(String indicatorType) { return mapper.selectByType(indicatorType); }
    @Override
    public int insert(RegIndicator record) { return mapper.insert(record); }
    @Override
    public int update(RegIndicator record) { return mapper.update(record); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
}

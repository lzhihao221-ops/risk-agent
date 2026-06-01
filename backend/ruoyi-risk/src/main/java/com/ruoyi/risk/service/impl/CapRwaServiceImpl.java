package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.CapRwa;
import com.ruoyi.risk.mapper.CapRwaMapper;
import com.ruoyi.risk.service.ICapRwaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CapRwaServiceImpl implements ICapRwaService {
    @Autowired
    private CapRwaMapper mapper;

    @Override
    public CapRwa selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<CapRwa> selectList(CapRwa query) { return mapper.selectList(query); }
    @Override
    public int insert(CapRwa record) { return mapper.insert(record); }
    @Override
    public int update(CapRwa record) { return mapper.update(record); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
    @Override
    public List<CapRwa> selectByDate(String calcDate) { return mapper.selectByDate(calcDate); }
}

package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.CapProvision;
import com.ruoyi.risk.mapper.CapProvisionMapper;
import com.ruoyi.risk.service.ICapProvisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CapProvisionServiceImpl implements ICapProvisionService {
    @Autowired
    private CapProvisionMapper mapper;

    @Override
    public CapProvision selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<CapProvision> selectList(CapProvision query) { return mapper.selectList(query); }
    @Override
    public int insert(CapProvision record) { return mapper.insert(record); }
    @Override
    public int update(CapProvision record) { return mapper.update(record); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
}

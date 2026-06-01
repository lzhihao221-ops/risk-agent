package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.CapEcl;
import com.ruoyi.risk.mapper.CapEclMapper;
import com.ruoyi.risk.service.ICapEclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CapEclServiceImpl implements ICapEclService {
    @Autowired
    private CapEclMapper mapper;

    @Override
    public CapEcl selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<CapEcl> selectList(CapEcl query) { return mapper.selectList(query); }
    @Override
    public int insert(CapEcl record) { return mapper.insert(record); }
    @Override
    public int update(CapEcl record) { return mapper.update(record); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
}

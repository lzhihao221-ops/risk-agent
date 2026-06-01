package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.CapAdequacy;
import com.ruoyi.risk.mapper.CapAdequacyMapper;
import com.ruoyi.risk.service.ICapAdequacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CapAdequacyServiceImpl implements ICapAdequacyService {
    @Autowired
    private CapAdequacyMapper mapper;

    @Override
    public CapAdequacy selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<CapAdequacy> selectList(CapAdequacy query) { return mapper.selectList(query); }
    @Override
    public int insert(CapAdequacy record) { return mapper.insert(record); }
    @Override
    public int update(CapAdequacy record) { return mapper.update(record); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
    @Override
    public CapAdequacy selectLatest() { return mapper.selectLatest(); }
}

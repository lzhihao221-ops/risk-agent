package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.RegReport1104;
import com.ruoyi.risk.mapper.RegReport1104Mapper;
import com.ruoyi.risk.service.IRegReport1104Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegReport1104ServiceImpl implements IRegReport1104Service {
    @Autowired
    private RegReport1104Mapper mapper;

    @Override
    public RegReport1104 selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<RegReport1104> selectList(RegReport1104 query) { return mapper.selectList(query); }
    @Override
    public int insert(RegReport1104 record) { return mapper.insert(record); }
    @Override
    public int update(RegReport1104 record) { return mapper.update(record); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
}

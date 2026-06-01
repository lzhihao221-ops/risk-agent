package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.ClassificationMigration;
import java.util.List;

public interface ClassificationMigrationMapper {
    List<ClassificationMigration> selectList(ClassificationMigration query);
    int insert(ClassificationMigration record);
    int deleteByIds(Long[] ids);
}

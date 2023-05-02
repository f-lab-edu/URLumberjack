package com.flab.urlumberjack.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DemoMapper {
    @Select("SELECT NOW()")
    String selectNow();

    String selectName();

}

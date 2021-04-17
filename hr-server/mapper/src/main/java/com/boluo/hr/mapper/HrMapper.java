package com.boluo.hr.mapper;

import com.boluo.hr.pojo.Hr;
import com.boluo.hr.pojo.HrExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HrMapper {
    int countByExample(HrExample example);

    int deleteByExample(HrExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Hr record);

    int insertSelective(Hr record);

    List<Hr> selectByExample(HrExample example);

    Hr selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Hr record, @Param("example") HrExample example);

    int updateByExample(@Param("record") Hr record, @Param("example") HrExample example);

    int updateByPrimaryKeySelective(Hr record);

    int updateByPrimaryKey(Hr record);

    Hr loadByUserName(String username);

    List<Hr> getRoleWithHrid(Integer id);

    List<Hr> selectAllExceptCurrentHr(@Param("id") Integer id);

    List<Hr> selectBypageSize();

    List<Hr> selectByHrName(@Param("hrName") String hrName);
}
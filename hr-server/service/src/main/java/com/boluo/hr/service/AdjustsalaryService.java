package com.boluo.hr.service;

import com.boluo.hr.mapper.AdjustsalaryMapper;
import com.boluo.hr.mapper.AppraiseMapper;
import com.boluo.hr.pojo.Adjustsalary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author @1352955539(boluo)
 * @date 2021/2/28 - 21:43
 */
@Service
public class AdjustsalaryService {

    @Autowired
    AdjustsalaryMapper adjustsalaryMapper;

    public List<Adjustsalary> getAll() {
        return adjustsalaryMapper.selectAll();
    }
}

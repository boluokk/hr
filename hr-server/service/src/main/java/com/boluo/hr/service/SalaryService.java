package com.boluo.hr.service;

import com.boluo.hr.mapper.SalaryMapper;
import com.boluo.hr.pojo.Salary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author @1352955539(boluo)
 * @date 2021/2/21 - 11:21
 */
@Service
public class SalaryService {

    @Autowired
    SalaryMapper salaryMapper;

    public List<Salary> getAllSalary() {
        return salaryMapper.selectAll();
    }

    public int delOfOne(Integer id) {
        int i = salaryMapper.deleteByPrimaryKey(id);
        return i;
    }

    public int addOfOne(Salary salary) {
        int i = salaryMapper.insertSelective(salary);
        return i;
    }

    public int editSalary(Salary salary) {
        int i = salaryMapper.updateByPrimaryKey(salary);
        return i;
    }

    public int deleteOfMany(Integer[] ids) {
        int i = salaryMapper.deleteOfMany(ids);
        return i;
    }
}

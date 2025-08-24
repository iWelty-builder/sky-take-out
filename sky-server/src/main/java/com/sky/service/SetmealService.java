package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    void add(SetmealDTO setmealDTO);

    PageResult page(SetmealPageQueryDTO pageQueryDTO);

    SetmealVO getById(Long id);

    Setmeal update(SetmealDTO setmealDTO);

    void delete(List<Long> ids);

    void startOrStop(Integer status, Long id);
}

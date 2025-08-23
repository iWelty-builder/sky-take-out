package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Transactional
    @Override
    public void add(SetmealDTO setmealDTO) {
        //使用List接收setmealDTO内的套餐菜品数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //创建Setmeal对象接收其他新增套餐传递的信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //新增的套餐默认停售
        setmeal.setStatus(StatusConstant.DISABLE);
        //调用SetmealMapper插入新增的套餐信息
        setmealMapper.addsetmeal(setmeal);

        //获取套餐的id
        Long setmealId = setmeal.getId();

        //遍历套餐菜品集合，为每一个套餐菜品设置套餐id
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
        }

        //调用SetmealDishMapper插入新增的套餐菜品数据
        setmealDishMapper.setmealadd(setmealDishes);
    }
}

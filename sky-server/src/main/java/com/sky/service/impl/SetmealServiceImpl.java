package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
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
        setmeal.setStatus(0);
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

    @Override
    public PageResult page(SetmealPageQueryDTO pageQueryDTO) {
        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());

        Setmeal setmeal =new Setmeal();
        BeanUtils.copyProperties(pageQueryDTO, setmeal);

        List<Setmeal> list =  setmealMapper.page(setmeal);

        Page<Setmeal> p = (Page<Setmeal>) list;

        return new PageResult(p.getTotal(),p.getResult());
    }

    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO =new SetmealVO();

        List<SetmealDish> list =setmealDishMapper.setmealGetById(id);

        setmealVO = setmealMapper.getById(id);
        setmealVO.setSetmealDishes(list);

        return setmealVO;
    }

    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {
        //使用List接收setmealDTO内的套餐菜品数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //创建Setmeal对象接收其他新增套餐传递的信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //调用SetmealMapper更新套餐信息
        setmealMapper.update(setmeal);

        //获取套餐的id
        Long setmealId = setmeal.getId();

        //删除对应套餐Id的菜品套餐关系数据，回归原始状态
        setmealDishMapper.deletesetmeal(setmealId);

        //遍历套餐菜品集合，为每一个套餐菜品设置套餐id
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
        }

        //调用SetmealDishMapper新增套餐菜品信息
        setmealDishMapper.setmealadd(setmealDishes);
     }
}

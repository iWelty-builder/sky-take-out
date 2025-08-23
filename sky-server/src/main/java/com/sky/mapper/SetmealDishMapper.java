package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    void setmealadd(List<SetmealDish> setmealDish);


    List<SetmealDish> setmealGetById(Long id);

    void deletesetmeal(Long setmealId);
}

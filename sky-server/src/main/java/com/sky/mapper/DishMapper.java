package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    @AutoFill(OperationType.INSERT)
    void add(Dish dish);

    /**
     * 全量查询
     * @param id
     * @return
     */
//    @Select("select * from dish")
//    Dish getById(Long id);

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    List<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    @Delete("delete from dish where id = #{id}")
    void deletebatch(Long id);

    /**
     * 单个查询
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);


    List<DishVO> getByCategoryId(Long categoryId);
}

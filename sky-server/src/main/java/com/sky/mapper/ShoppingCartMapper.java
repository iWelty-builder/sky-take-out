package com.sky.mapper;


import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    List<ShoppingCart> list(ShoppingCart shoppingCart);

    @Update("update sky_take_out.shopping_cart set number = #{number} where id = #{id}")
    void updateNumById(ShoppingCart currentShoppingCart);

    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "VALUES " +
            "(#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);


    @Delete("delete from shopping_cart where user_id=#{userId}")
    void deleteByUserId(Long userId);

    void deleteSingle(ShoppingCart shoppingCart);

    void updateNumByIdSingle(ShoppingCart cart);

    void insertBatch(List<ShoppingCart> shoppingCartList);
}

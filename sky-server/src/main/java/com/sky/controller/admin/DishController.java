package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping
    public Result add(@RequestBody DishDTO dish) {
        log.info("dish:{}", dish);
        dishService.add(dish);
        //清理缓存数据
        String key = "dish_"+ dish.getCategoryId();
        redisTemplate.delete(key);
        return Result.success();
    }

//    @GetMapping("/{id}")
//    public Result<Dish> getById(@PathVariable Long id) {
//        log.info("get dish by id:{}", id);
//        Dish dish = dishService.getById(id);
//        return Result.success(dish);
//    }

    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    public Result delete(@RequestParam List< Long> ids){
        log.info("批量删除菜品:{}", ids);
        dishService.deleteBatch(ids);
        //将所有的缓存全部清理掉
        Clean();

        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品:{}", id);
        DishVO dishVO = dishService.getByIdWithFlavors(id);
        return Result.success(dishVO);
    }

    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品:{}", dishDTO);
        dishService.update(dishDTO);
        //将所有的缓存全部清理掉
        Clean();

        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status , @RequestParam Long id ){
        log.info("SET DISH {} STATUS:{}", id ,status);
        dishService.shiftStatus(status,id);
        Clean();

        return Result.success();
    }

    private void Clean() {
        //将所有的缓存全部清理掉
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
    }

    @GetMapping("/list")
    public Result<List<DishVO>> getByCategoryId(@RequestParam Long categoryId){
        log.info("根据分类ID:{}查询菜品", categoryId);
        //调用dishService
        List<DishVO> list = dishService.getByCategoryId(categoryId);

        return Result.success(list);
    }







}

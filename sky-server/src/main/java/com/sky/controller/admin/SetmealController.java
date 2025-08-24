package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    public Result add(@RequestBody SetmealDTO setmealDTO) {
        log.info("ADD NEW SETMEAL:{}", setmealDTO);
        setmealService.add(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO pageQueryDTO){
        log.info("分页查询 {}", pageQueryDTO);
        PageResult p = setmealService.page(pageQueryDTO);
        return Result.success(p);
    }

    /**
     * 查询回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id ){
        log.info("根据id:{}查询套餐",id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);

    }

    @PutMapping
    public Result<SetmealDTO> update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐:{}",setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success(setmealDTO);
    }



}

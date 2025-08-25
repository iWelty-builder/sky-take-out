package com.sky.controller.admin;


import com.sky.result.Result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {

    public static final String KEY="Shop_Status";

    @Autowired
    RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status){
        log.info("set shop status {}",status==1?"OPEN":"CLOSE");
        redisTemplate.opsForValue().set("KEY",status);
        return Result.success();
    }

    @GetMapping("/status")
    public Result<Integer> getStatus(){
        Object status = redisTemplate.opsForValue().get("KEY");
       Integer Status = (Integer) status;
        log.info("Get Shop Status {}", Status==1?"OPEN":"CLOSE");
        return Result.success(Status);
    }

}

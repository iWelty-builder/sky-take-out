package com.sky.controller.user;


import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/shop")
@Slf4j
public class ShopControllerClient {

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/status")
    public Result<Integer> getStatus(){
        Object status = redisTemplate.opsForValue().get("Shop_Status");
       Integer Status = (Integer) status;
        log.info("Get Shop Status {}", Status==1?"OPEN":"CLOSE");
        return Result.success(Status);
    }

}

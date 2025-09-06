package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    OrderMapper orderMapper;



    /**
     * Process Time out Orders
     */
    @Scheduled(cron = "0 * * * * *")
    public void processTimeoutOrder(){
        log.info("Process Time out Orders {}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        //select * from orders where status = ? and order_time < (now - 15min)
        List<Orders> timeLT = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        if(timeLT != null && !timeLT.isEmpty()){
            for (Orders order : timeLT) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("Time out, AUTO CANCELED");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }


    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("Process Delivery Orders Timely {}",LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> timeLT = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if(timeLT != null && !timeLT.isEmpty()){
            for (Orders order : timeLT) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }




}

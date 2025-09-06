package com.sky.controller.user;

import com.google.common.primitives.Ints;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("User is Ordering Param is {}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

//    /**
//     * 订单支付
//     *
//     * @param ordersPaymentDTO
//     * @return
//     */
//    @PutMapping("/payment")
//    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
//        log.info("订单支付：{}", ordersPaymentDTO);
//        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
//        log.info("生成预支付交易单：{}", orderPaymentVO);
//        return Result.success(orderPaymentVO);
//    }
    @PutMapping("/payment")
    public Result<String> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
    log.info("订单支付：{}", ordersPaymentDTO);
    orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
    return  Result.success();
    }

    @GetMapping("/historyOrders")
    public Result<PageResult> page(int page, int pageSize, Integer status) {
        PageResult pageResult = orderService.pageQuery4User(page, pageSize, status);
        return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> detail(@PathVariable Integer id){
        OrderVO orderVO = orderService.detail(id);
        return Result.success(orderVO);
    }

    @PutMapping("/cancel/{id}")
    public Result cancelOrders(@PathVariable Integer id) throws Exception {
        orderService.cancel(id);
        return Result.success();
    }

    @PostMapping("/repetition/{id}")
    public Result onceMore(@PathVariable Long id){
        orderService.repetition(id);
        return Result.success();
    }

    @GetMapping("/reminder/{id}")
    public Result remind(@PathVariable Long id){
        orderService.remind(id);
        return Result.success();
    }



}

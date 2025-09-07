package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {
        @Autowired
        private ReportService reportService;

        @GetMapping("/turnoverStatistics")
        public Result<TurnoverReportVO> turnoverStatistics(
                @DateTimeFormat(pattern = "yyyy-MM-dd")
                @RequestParam LocalDate begin ,
                @DateTimeFormat(pattern = "yyyy-MM-dd")
                @RequestParam LocalDate end){
            TurnoverReportVO turnoverReportVO = reportService.turnover(begin,end);
            return Result.success(turnoverReportVO);
        }

        @GetMapping("/userStatistics")
        public Result<UserReportVO> userStatistics(
                @DateTimeFormat(pattern = "yyyy-MM-dd")
                @RequestParam LocalDate begin ,
                @DateTimeFormat(pattern = "yyyy-MM-dd")
                @RequestParam LocalDate end){
            log.info("User Statistics {},{}", begin,end);
            UserReportVO userReportVO =  reportService.getUserStatistics(begin,end);
            return Result.success(userReportVO);
        }

    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate end){
        log.info("用户数据统计:{},{}", begin, end);
        return Result.success(reportService.getOrderStatistics (begin, end));
    }

    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10Statistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate end){
            log.info("Top10 {},{}",begin,end);
        return Result.success(reportService.getTop10Statistics (begin, end));
    }





}

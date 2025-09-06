package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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


}

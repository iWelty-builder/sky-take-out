package com.sky.service;


import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReportService {

    TurnoverReportVO turnover(LocalDate begin, LocalDate end);
}

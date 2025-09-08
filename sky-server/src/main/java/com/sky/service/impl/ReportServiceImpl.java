package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Employee;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import com.sky.websocket.WebSocketServer;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.DataLine;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private WorkspaceService workspaceService;


    @Override
    public TurnoverReportVO turnover(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);
        while (!begin.equals(end)){
            begin= begin.plusDays(1);
            dateList.add(begin);
        }
        List<Double> turnoverlist = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map =new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status",Orders.COMPLETED);

           Double turnover =  orderMapper.sumByMap(map);
           if(turnover == null){
               turnover=0.0;
           }
           turnoverlist.add(turnover);
        }
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverlist,","))
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        dateList.add(begin);
        while (!begin.equals(end)){
            begin= begin.plusDays(1);
            dateList.add(begin);
        }

        for (LocalDate date : dateList) {
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map =new HashMap<>();
            map.put("end",endTime);
            Integer totalUser = userMapper.getTotal(map);
            totalUserList.add(totalUser);
        }

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map =new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            Integer d_newUser = userMapper.getdNewUser(map);
            newUserList.add(d_newUser);
        }

        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .build();
    }

    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);
        while (!begin.equals(end)){
            begin= begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            //查询每天订单总数 select count(*) from orders where order_time > beginTime and order_time < endTime
            orderCountList.add(getOrderCount(beginTime,endTime,null));
            //查询每天的有效订单数 select count(*) from orders where order_time > beginTime and order_time < endTime
            //and status = Orders.COMPLETED;
            validOrderCountList.add(getOrderCount(beginTime, endTime, Orders.COMPLETED));
        }

        Integer totalOrderCount=0,validOrderCount=0; Double orderCompletionRate=0.0;
        for (Integer i : orderCountList) {
            totalOrderCount=i+totalOrderCount;
        }
        for (Integer i : validOrderCountList) {
            validOrderCount=i+validOrderCount;
        }
        if (totalOrderCount != 0) {
            orderCompletionRate= validOrderCount.doubleValue() / totalOrderCount.doubleValue();
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getTop10Statistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> top10 = orderMapper.getSalesTop10(beginTime, endTime);
        List<String> name = new ArrayList<>();
        List<Integer> count = new ArrayList<>();

        for (GoodsSalesDTO goodsSalesDTO : top10) {
            name.add(goodsSalesDTO.getName());
            count.add(goodsSalesDTO.getNumber());
        }

       return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(name,","))
                .numberList(StringUtils.join(count,","))
                .build();
    }

    @Override
    public void exportBusinessData(HttpServletResponse response) {

        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end =LocalDate.now().minusDays(1);

        LocalDateTime localDateTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime localDateTime1 = LocalDateTime.of(end, LocalTime.MAX);

        BusinessDataVO data = workspaceService.getBusinessData(localDateTime, localDateTime1);

        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheet("Sheet1");

            sheet.getRow(1).getCell(1).setCellValue("TIME SPAN :"+begin + "TO" + end);

            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(data.getTurnover());
            row.getCell(4).setCellValue(data.getOrderCompletionRate());
            row.getCell(6).setCellValue(data.getNewUsers());

            row = sheet.getRow(4);
            row.getCell(2).setCellValue(data.getValidOrderCount());
            row.getCell(4).setCellValue(data.getUnitPrice());


            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);

            for(int i = 0;i < 30;i++){
                LocalDate date = begin.plusDays(i);
                workspaceService.getBusinessData(LocalDateTime.of(date,LocalTime.MIN),LocalDateTime.of(end,LocalTime.MAX));

                 row = sheet.getRow(7 + i);
                 row.getCell(1).setCellValue(date.toString());
                 row.getCell(2).setCellValue(data.getTurnover());
                 row.getCell(3).setCellValue(data.getValidOrderCount());
                 row.getCell(4).setCellValue(data.getOrderCompletionRate());
                 row.getCell(5).setCellValue(data.getUnitPrice());
                 row.getCell(6).setCellValue(data.getNewUsers());

            }

            outputStream.close();
            workbook.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }

    private Integer getOrderCount(LocalDateTime beginTime, LocalDateTime endTime, Integer status) {
        Map map1 = new HashMap();
        map1.put("begin", beginTime);
        map1.put("end", endTime);
        map1.put("status", status);
        return orderMapper.countByMap(map1);
    }


}

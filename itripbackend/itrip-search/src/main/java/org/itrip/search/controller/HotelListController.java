package org.itrip.search.controller;

import com.alibaba.druid.util.StringUtils;
import org.itrip.common.DtoUtil;
import org.itrip.common.Page;
import org.itrip.dto.Dto;
import org.itrip.search.bean.ItripHotelVO;
import org.itrip.vo.hotel.SearchHotelVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hotellist")
public class HotelListController {
    @RequestMapping(value = "/searchItripHotelPage",method = RequestMethod.POST,produces = "application/json")
    public Dto<Page<ItripHotelVO>> searchItripHotelPage(@RequestBody SearchHotelVO vo){
        try {
            //判断目的是否为空
            if(StringUtils.isEmpty(vo.getDestination())){
                return DtoUtil.returnFail("目的地不能为空！","20002");
            }
            //调用业务处理
            Page page =null;
            return DtoUtil.returnDataSuccess(page);
        }catch (Exception ex){
            ex.printStackTrace();
            return DtoUtil.returnFail("系统异常！","20001");
        }
    }
}

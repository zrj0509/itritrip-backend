package org.itrip.search.controller;

import com.alibaba.druid.util.StringUtils;
import org.itrip.common.DtoUtil;
import org.itrip.common.Page;
import org.itrip.dto.Dto;
import org.itrip.search.bean.ItripHotelVO;
import org.itrip.search.service.SearchService;
import org.itrip.vo.hotel.SearchHotCityVO;
import org.itrip.vo.hotel.SearchHotelVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/hotellist")
public class HotelListController {
    @Resource
    private SearchService searchService;
    @RequestMapping(value = "/searchItripHotelPage",method = RequestMethod.POST,produces = "application/json")
    public Dto<Page<ItripHotelVO>> searchItripHotelPage(@RequestBody SearchHotelVO vo){
        try {
            //判断目的是否为空
            if(StringUtils.isEmpty(vo.getDestination())){
                return DtoUtil.returnFail("目的地不能为空！","20002");
            }
            //调用业务处理
            Page page =searchService.SearchItripHotelPage(vo);
            return DtoUtil.returnDataSuccess(page);
        }catch (Exception ex){
            ex.printStackTrace();
            return DtoUtil.returnFail("系统异常！","20001");
        }
    }
    @RequestMapping(value = "/searchItripHotelListByHotCity",method = RequestMethod.POST,produces = "application/json")
    public Dto<Page<ItripHotelVO>> searchItripHotelListByHotCity (@RequestBody SearchHotCityVO vo){

        try {
            //判断城市id是否为空
            if(StringUtils.isEmpty(String.valueOf(vo.getCityId()))){
                return DtoUtil.returnFail("城市id不能为空！","20004");
            }

            if(StringUtils.isEmpty(String.valueOf(vo.getCount()))){
                return DtoUtil.returnFail("数目不能为空！","20005");
            }
            //调用业务处理
            return DtoUtil.returnDataSuccess(searchService.searchItripHotelListByHotCity(vo));
        }catch (Exception ex){
            ex.printStackTrace();
            return DtoUtil.returnFail("系统异常！","20003");
        }

    }
}

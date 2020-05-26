package org.itrip.biz.controller;

import org.apache.ibatis.annotations.Param;
import org.itrip.biz.service.itripHotelRoom.ItripHotelRoomService;
import org.itrip.biz.service.itripLabelDic.ItripLabelDicService;
import org.itrip.common.DateUtil;
import org.itrip.common.DtoUtil;
import org.itrip.common.EmptyUtils;
import org.itrip.dto.Dto;
import org.itrip.pojo.ItripHotelRoom;
import org.itrip.pojo.ItripLabelDic;
import org.itrip.vo.ItripLabelDicVO;
import org.itrip.vo.hotelroom.ItripHotelRoomVO;
import org.itrip.vo.hotelroom.SearchHotelRoomVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName HotelController
 * @Description: TODO
 * @Author 44401
 * @Date 2020/5/23
 * @Version V1.0
 **/
@RestController
@RequestMapping("/api/hotelroom")
public class HotelRoomController {

    //业务酒店房间接口
    @Resource
    private ItripHotelRoomService itripHotelRoomService;

    @Resource
    private ItripLabelDicService itripLabelDicService;


    /**
     *
     *查询酒店房间列表
     * @return
     */
    @PostMapping("/queryhotelroombyhotel")
    public Dto queryHotelRoomByHotel(@RequestBody SearchHotelRoomVO vo){
        //对象
        List<ItripHotelRoom> itripHotelRooms=null;
        List<ItripHotelRoomVO> itripHotelRoomVOS=null;
        try {
            //map存入参数
            Map<String,Object> param =new HashMap<>();

            //判断
            if (EmptyUtils.isEmpty(vo.getHotelId())) {
                return DtoUtil.returnFail("酒店ID不能为空", "100303");
            }
            if (EmptyUtils.isEmpty(vo.getStartDate()) || EmptyUtils.isEmpty(vo.getEndDate())) {
                return DtoUtil.returnFail("必须填写酒店入住及退房时间", "100303");
            }
            if (EmptyUtils.isNotEmpty(vo.getStartDate()) && EmptyUtils.isNotEmpty(vo.getEndDate())) {
                if (vo.getStartDate().getTime() > vo.getEndDate().getTime()) {
                    return DtoUtil.returnFail("入住时间不能大于退房时间", "100303");
                }
                List<Date> dates = DateUtil.getBetweenDates(vo.getStartDate(), vo.getEndDate());
                param.put("timesList", dates);
            }

            //设置参数
            param.put("hotelId", vo.getHotelId());
            param.put("isBook", vo.getIsBook());
            param.put("isHavingBreakfast", vo.getIsHavingBreakfast());
            param.put("isTimelyResponse", vo.getIsTimelyResponse());
            param.put("roomBedTypeId", vo.getRoomBedTypeId());
            param.put("isCancel", vo.getIsCancel());
            if(EmptyUtils.isEmpty(vo.getPayType()) || vo.getPayType()==3){
                param.put("payType", null);
            }else{
                param.put("payType", vo.getPayType());
            }
            //调用业务方法
            itripHotelRooms=itripHotelRoomService.getItripHotelRoomListByMap(param);
            ///////////////////////////

            ///////////////////////////

        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败","100304");
        }
        //返回数据
        return DtoUtil.returnDataSuccess(itripHotelRoomVOS);
    }


}

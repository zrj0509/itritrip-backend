package org.itrip.biz.controller;

import org.itrip.biz.service.itripHotelOrder.ItripHotelOrderService;
import org.itrip.common.DtoUtil;
import org.itrip.common.ValidationToken;
import org.itrip.dto.Dto;
import org.itrip.vo.order.ValidateRoomStoreVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/hotelorder")
public class HotelOrderController {
    //业务
    @Resource
    private ItripHotelOrderService hotelOrderControllerService;
    @Resource
    private ValidationToken validationToken;
    @RequestMapping(value = "/getpreorderinfo" ,method = RequestMethod.POST,produces = "application/json")
    public Dto getPreOrderInfo(@RequestBody ValidateRoomStoreVO vo, HttpServletRequest request){
        try {
            //token令牌验证
            //获取头信息
            String token= request.getHeader("token");
            String agent = request.getHeader("user-agent");
            //判断令牌验证
            if(!validationToken.valiate(agent,token)){
                return DtoUtil.returnFail("token失效，请重登录", "100000");
            }

            //表单信息的验证
            if(vo.getRoomId() == null || vo.getRoomId() == 0){
                return DtoUtil.returnFail("roomId不能为空", "100511");
            }
            if(vo.getHotelId() == null || vo.getHotelId() == 0){
                return DtoUtil.returnFail("hotelId不能为空", "100510");
            }
            if(vo.getCheckInDate().getTime() > vo.getCheckOutDate().getTime()){
                return DtoUtil.returnFail("入住时间不能大于退房时间", "100514");
            }
//          封装前端的实体对象
            Dto dto=  hotelOrderControllerService.getItripRoomStore(vo);
            return dto;
        }catch (Exception ex){
            ex.printStackTrace();
            return DtoUtil.returnFail("系统异常","100513");
        }
    }
}

package org.itrip.biz.controller;

import jdk.nashorn.internal.parser.Token;
import org.itrip.biz.service.itripHotelOrder.ItripHotelOrderService;
import org.itrip.biz.service.itripHotelRoom.ItripHotelRoomService;
import org.itrip.common.DtoUtil;
import org.itrip.common.ErrorCode;
import org.itrip.common.ValidationToken;
import org.itrip.dto.Dto;
import org.itrip.pojo.ItripHotelOrder;
import org.itrip.pojo.ItripHotelRoom;
import org.itrip.vo.hotelroom.ItripHotelRoomVO;
import org.itrip.vo.order.ItripAddHotelOrderVO;
import org.itrip.vo.order.ItripPersonalOrderRoomVO;
import org.itrip.vo.order.ValidateRoomStoreVO;
import org.springframework.web.bind.annotation.*;

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
    @Resource
    private ItripHotelRoomService itripHotelRoomService;
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
    @RequestMapping(value = "/addhotelorder",method = RequestMethod.POST,produces ="application/json" )
    public Dto addHotelOrder(@RequestBody ItripAddHotelOrderVO vo, HttpServletRequest request){
        //发生异常
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
            Dto dto=  hotelOrderControllerService.itriptxInsertItripHotelOrder(vo,token);
            return dto;

        }catch (Exception ex){
            ex.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100513");
        }
    }
    @RequestMapping(value = "/queryOrderById" ,method = RequestMethod.GET,produces = "application/json")
    public Dto queryOrderById(@RequestParam("orderId") String orderId,HttpServletRequest request){
        try {
            //token令牌验证
            //获取头信息
            String token= request.getHeader("token");
            String agent = request.getHeader("user-agent");
            //判断令牌验证
            if(!validationToken.valiate(agent,token)){
                return DtoUtil.returnFail("token失效，请重登录", "100000");
            }
            if (orderId==null || orderId.equals("")){
                return DtoUtil.returnFail("请传递参数","100525");
            }
            ItripHotelOrder itripHotelOrder =hotelOrderControllerService.getItripHotelOrderById(Long.valueOf(orderId));
            if (itripHotelOrder==null){
                return DtoUtil.returnFail("没有相关订单信息","100526");
            }
            return DtoUtil.returnDataSuccess(itripHotelOrder);
        }catch (Exception ex){
            ex.printStackTrace();
            return DtoUtil.returnFail("获取个人订单信息错误", "100527");
        }
    }
    @RequestMapping(value = "/getpersonalorderroominfo/{orderId}" ,method = RequestMethod.GET,produces = "application/json")
    public Dto getpersonalorderroominfo (@RequestParam("orderId") String orderId,HttpServletRequest request){
        try {
            //token令牌验证
            //获取头信息
            String token= request.getHeader("token");
            String agent = request.getHeader("user-agent");
            //判断令牌验证
            if(!validationToken.valiate(agent,token)){
                return DtoUtil.returnFail("token失效，请重登录", "100000");
            }
            if (orderId==null || orderId.equals("")){
                return DtoUtil.returnFail("请传递参数","100529");
            }
            ItripHotelRoom itripHotelRoom=itripHotelRoomService.getItripHotelRoomById(Long.valueOf(orderId));
            ItripPersonalOrderRoomVO itripPersonalOrderRoomVO=new ItripPersonalOrderRoomVO();
            if (itripPersonalOrderRoomVO==null){
                return DtoUtil.returnFail("没有相关订单房型信息","100530");
            }
            return DtoUtil.returnDataSuccess(itripPersonalOrderRoomVO);
        }catch (Exception ex){
            ex.printStackTrace();
            return DtoUtil.returnFail("获取个人订单房型信息错误","100531");
        }
    }
}

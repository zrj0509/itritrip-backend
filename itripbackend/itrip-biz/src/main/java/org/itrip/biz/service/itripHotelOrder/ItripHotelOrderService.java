package org.itrip.biz.service.itripHotelOrder;
import org.itrip.dto.Dto;
import org.itrip.pojo.ItripHotelOrder;

import java.util.*;
import java.util.List;
import java.util.Map;
import org.itrip.common.Page;
import org.itrip.pojo.ItripHotelTempStore;
import org.itrip.pojo.ItripUserLinkUser;
import org.itrip.vo.order.ItripAddHotelOrderVO;
import org.itrip.vo.order.ValidateRoomStoreVO;

/**
 * @Author: 
 * @Date: 2019/5/20 下午9:26
 */
public interface ItripHotelOrderService {

    public ItripHotelOrder getItripHotelOrderById(Long id)throws Exception;

    public List<ItripHotelOrder>	getItripHotelOrderListByMap(Map<String, Object> param)throws Exception;

    public Integer getItripHotelOrderCountByMap(Map<String, Object> param)throws Exception;

    public Integer itriptxAddItripHotelOrder(ItripHotelOrder itripHotelOrder)throws Exception;

    public Integer itriptxModifyItripHotelOrder(ItripHotelOrder itripHotelOrder)throws Exception;

    public Integer itriptxDeleteItripHotelOrderById(Long id)throws Exception;

    public Page<ItripHotelOrder> queryItripHotelOrderPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize)throws Exception;

    //1.获取预定信息
    Dto getItripRoomStore(ValidateRoomStoreVO vo) throws  Exception;

    //2.计算库存
    public List<ItripHotelTempStore> queryRoomStore(HashMap<String, Object> param)throws  Exception;
    //3.添加订单
    Dto itriptxInsertItripHotelOrder(ItripAddHotelOrderVO vo,String token)throws  Exception;

    //4.计算房间的总金额
    double getSumRoomPrice(long count, Double roomPrice) throws  Exception;

    //5.添加订单和入住人
    Map<String, Object> itriptxAddItripHotelOrder2(ItripHotelOrder order, List<ItripUserLinkUser> linkUser)throws  Exception;
}

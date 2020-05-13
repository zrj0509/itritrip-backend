package org.itrip.biz.service.itripHotelOrder;
import org.itrip.common.DtoUtil;
import org.itrip.dto.Dto;
import org.itrip.mapper.itripHotel.ItripHotelMapper;
import org.itrip.mapper.itripHotelOrder.ItripHotelOrderMapper;
import org.itrip.mapper.itripHotelRoom.ItripHotelRoomMapper;
import org.itrip.pojo.ItripHotel;
import org.itrip.pojo.ItripHotelOrder;
import org.itrip.common.EmptyUtils;
import org.itrip.common.Page;
import org.itrip.pojo.ItripHotelRoom;
import org.itrip.pojo.ItripHotelTempStore;
import org.itrip.vo.order.RoomStoreVO;
import org.itrip.vo.order.ValidateRoomStoreVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.itrip.common.Constants;
@Service
public class ItripHotelOrderServiceImpl implements ItripHotelOrderService {

    //酒店注入
    @Resource
    private ItripHotelMapper itripHotelMapper;

    //酒店房间注入
    @Resource
    private ItripHotelRoomMapper itripHotelRoomMapper;

    //酒店订单注入
    @Resource
    private ItripHotelOrderMapper itripHotelOrderMapper;

    public ItripHotelOrder getItripHotelOrderById(Long id)throws Exception{
        return itripHotelOrderMapper.getItripHotelOrderById(id);
    }

    public List<ItripHotelOrder>	getItripHotelOrderListByMap(Map<String,Object> param)throws Exception{
        return itripHotelOrderMapper.getItripHotelOrderListByMap(param);
    }

    public Integer getItripHotelOrderCountByMap(Map<String,Object> param)throws Exception{
        return itripHotelOrderMapper.getItripHotelOrderCountByMap(param);
    }

    public Integer itriptxAddItripHotelOrder(ItripHotelOrder itripHotelOrder)throws Exception{
            itripHotelOrder.setCreationDate(new Date());
            return itripHotelOrderMapper.insertItripHotelOrder(itripHotelOrder);
    }

    public Integer itriptxModifyItripHotelOrder(ItripHotelOrder itripHotelOrder)throws Exception{
        itripHotelOrder.setModifyDate(new Date());
        return itripHotelOrderMapper.updateItripHotelOrder(itripHotelOrder);
    }

    public Integer itriptxDeleteItripHotelOrderById(Long id)throws Exception{
        return itripHotelOrderMapper.deleteItripHotelOrderById(id);
    }

    public Page<ItripHotelOrder> queryItripHotelOrderPageByMap(Map<String,Object> param,Integer pageNo,Integer pageSize)throws Exception{
        Integer total = itripHotelOrderMapper.getItripHotelOrderCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripHotelOrder> itripHotelOrderList = itripHotelOrderMapper.getItripHotelOrderListByMap(param);
        page.setRows(itripHotelOrderList);
        return page;
    }

    /**
     * 获取预定信息
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public Dto getItripRoomStore(ValidateRoomStoreVO vo) throws Exception {
         //1.查询酒店信息ID
        ItripHotel  hotel =itripHotelMapper.getItripHotelById(vo.getHotelId());

        //2.酒店房间信息ID
        ItripHotelRoom  room = itripHotelRoomMapper.getItripHotelRoomById(vo.getRoomId());

        //3.封装对象到vo
        RoomStoreVO  roomStoreVO =new RoomStoreVO();
        //设置值
        roomStoreVO.setHotelId(vo.getHotelId());
        roomStoreVO.setRoomId(room.getId());
        roomStoreVO.setCheckInDate(vo.getCheckInDate());
        roomStoreVO.setCheckOutDate(vo.getCheckOutDate());
        roomStoreVO.setCount(vo.getCount());
        roomStoreVO.setHotelName(hotel.getHotelName());
        //类型转换
        roomStoreVO.setPrice(BigDecimal.valueOf(room.getRoomPrice()));

        //库存
//        roomStoreVO.setStore();

        //封装参数(procedure)
        HashMap<String,Object> param =new HashMap<>();
        param.put("startTime",vo.getCheckInDate());
        param.put("endTime",vo.getCheckOutDate());
        param.put("roomId",vo.getRoomId());
        param.put("hotelId",vo.getHotelId());

        //实时库存查询
        List<ItripHotelTempStore> stores=this.queryRoomStore(param);
        //判断
        if(EmptyUtils.isEmpty(stores)){
            return DtoUtil.returnFail("暂时无房","100512");
        }else{
            //保存数据!!
            roomStoreVO.setStore(stores.get(0).getStore());  //1

            //返回数据到前端
            return DtoUtil.returnDataSuccess(roomStoreVO);
        }

    }

    /**
     * 计算库存量
     * @param param
     * @return
     * @throws Exception
     */

    @Override
    public List<ItripHotelTempStore> queryRoomStore(HashMap<String, Object> param) throws Exception {
       //调用存储过程,刷新实时库存表
        itripHotelOrderMapper.flushRoomStore(param);

        //计算库存：
        List<ItripHotelTempStore> tempStores =itripHotelOrderMapper.queryRoomStore(param);
        return tempStores;
    }




}

package org.itrip.biz.service.itripHotelOrder;
import org.itrip.common.*;
import org.itrip.dto.Dto;
import org.itrip.mapper.itripHotel.ItripHotelMapper;
import org.itrip.mapper.itripHotelOrder.ItripHotelOrderMapper;
import org.itrip.mapper.itripHotelRoom.ItripHotelRoomMapper;
import org.itrip.mapper.itripOrderLinkUser.ItripOrderLinkUserMapper;
import org.itrip.pojo.*;
import org.itrip.vo.order.ItripAddHotelOrderVO;
import org.itrip.vo.order.RoomStoreVO;
import org.itrip.vo.order.ValidateRoomStoreVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //酒店订单联系注入
    @Resource
    private ItripOrderLinkUserMapper itripOrderLinkUserMapper;

    //token
    @Resource
    private ValidationToken validationToken;
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

    /**
     * 生成订单
     * @param vo
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public Dto itriptxInsertItripHotelOrder(ItripAddHotelOrderVO vo, String token) throws Exception {

        //   校验库存/////////////////
        HashMap<String,Object> param =new HashMap<>();
        param.put("startTime",vo.getCheckInDate());
        param.put("endTime",vo.getCheckOutDate());
        param.put("roomId",vo.getRoomId());
        param.put("hotelId",vo.getHotelId());

        //实时库存查询
        List<ItripHotelTempStore> stores=this.queryRoomStore(param);
        //判断
        if(EmptyUtils.isEmpty(stores)){
            return DtoUtil.returnFail("库存不足","100507");
        }
        //////////////////////////////////

        //封装订单存储对象
        ItripHotelOrder order =new ItripHotelOrder();
        //拷贝属性（vo前端给order实体）
        BeanUtils.copyProperties(vo,order);
        //vo中没有属性，order必须设置;
        //1.下单人
        ItripUser  user= validationToken.getCurrentUser(token);
        order.setCreatedBy(user.getCreatedBy());
        order.setUserId(user.getId());

        //2.设备类型 1:手机端 2:其他客户端
        if(token.startsWith("token:PC")){
            order.setBookType(0);
        }else  if(token.startsWith("token:MOBILE")){
            order.setBookType(1);
        }else{
            order.setBookType(2);
        }

        //3.入住人名称 ( 涛涛,王小果,王琳琳)
        StringBuilder builder =new StringBuilder();
        List<ItripUserLinkUser> linkUsers=vo.getLinkUser();
        //先判断是否为空
        if(EmptyUtils.isNotEmpty(linkUsers)){
            //循环
            for (int i = 0; i <linkUsers.size() ; i++) {
                //追加数据
                builder.append(linkUsers.get(i).getLinkUserName());
                //,
                if(i<linkUsers.size()-1){
                    builder.append(",");
                }
            }
            //设置值
            order.setLinkUserName(builder.toString());
        }

        //4.支付方式:1:支付宝 2:微信 3:到店付
        //酒店房间查询
        ItripHotelRoom  room = itripHotelRoomMapper.getItripHotelRoomById(vo.getRoomId());
        order.setPayType(room.getPayType());


        //5预订天数 2020.10.1~2020.10.3,实际是2天;
        long day = DateUtil.getBetweenDates(vo.getCheckInDate(),vo.getCheckOutDate()).size()-1;
        order.setBookingDays((int)day);

        //6.支付金额 ==>预定的天数*库存数量*房间价格
        double money = this.getSumRoomPrice(day*vo.getCount(),room.getRoomPrice());
        order.setPayAmount(money);

        //7.订单状态（0：待支付 1:已取消 2:支付成功 3:已消费 4：已点评）
        order.setOrderStatus(0);

        //8.订单号 机器码+订单日期+6位随机数
        // D1000001 20170630173721 44e3cd
        //随机数
        String  str=String.valueOf(Math.random()*100000);

        //日期
        SimpleDateFormat df =new SimpleDateFormat("yyyyMMddHHmmss");
        String  date =df.format(new Date()).toString();

        //对象
        StringBuilder orderNo =new StringBuilder();
        orderNo.append("D1000001").append(date).append(MD5.getMd5(str,6));
        //设置
        order.setOrderNo(orderNo.toString());


        //添加订单（订单，入住人）
        Map<String,Object>  map =this.itriptxAddItripHotelOrder2(order,vo.getLinkUser());

        return DtoUtil.returnDataSuccess(map);
    }

    /**
     * 计算金额
     * @param count
     * @param roomPrice
     * @return
     * @throws Exception
     */
    @Override
    public double getSumRoomPrice(long count, Double roomPrice) throws Exception {
        //调用计算方法
        BigDecimal money= BigDecimalUtil.OperationASMD(count,roomPrice,BigDecimalUtil.BigDecimalOprations.multiply,2,BigDecimal.ROUND_DOWN);
        return money.doubleValue();
    }

    /**
     * 添加订单和入住人
     * @param order
     * @param linkUser
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> itriptxAddItripHotelOrder2(ItripHotelOrder order, List<ItripUserLinkUser> linkUser) throws Exception {
        //1.新增订单
        if(EmptyUtils.isEmpty(order.getId())){
            //预定日期
            order.setCreationDate(new Date());
            //添加
            itripHotelOrderMapper.insertItripHotelOrder(order);
        }else {   //2.修改订单
            //先删除订单之前的所有联系人
            itripOrderLinkUserMapper.deleteItripOrderLinkUserById(order.getId());
            //   更新
            itripHotelOrderMapper.updateItripHotelOrder(order);
        }

        //3.添加联系人
        if(EmptyUtils.isNotEmpty(linkUser)){
            //循环
            for (ItripUserLinkUser linkUser0 :linkUser){
                //创建订单联系人对象
                ItripOrderLinkUser  orderLinkUser=new ItripOrderLinkUser();
                //拷贝  vo linkUser==>给订单联系orderLinkUser
                BeanUtils.copyProperties(linkUser0,orderLinkUser);
                //设置值
                orderLinkUser.setCreationDate(new Date());
                orderLinkUser.setCreatedBy(order.getCreatedBy());

                //添加订单
                itripHotelOrderMapper.insertItripHotelOrder(order);
            }
        }

        //前端vo信息
        Map<String, Object> output=new HashMap<>();
        output.put("orderNo",order.getOrderNo());
        output.put("id",order.getId());
        return output;
    }


}

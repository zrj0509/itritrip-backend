package org.itrip.biz.service.itripHotel;
import org.itrip.dto.Dto;
import org.itrip.pojo.ItripHotel;
import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.Map;
import org.itrip.common.Page;
/**
 * @Author: wangrj
 * @Date: 2019/5/20 下午9:26
 */
public interface ItripHotelService {

    public ItripHotel getItripHotelById(Long id)throws Exception;

    public List<ItripHotel>	getItripHotelListByMap(Map<String, Object> param)throws Exception;

    public Integer getItripHotelCountByMap(Map<String, Object> param)throws Exception;

    public Integer itriptxAddItripHotel(ItripHotel itripHotel)throws Exception;

    public Integer itriptxModifyItripHotel(ItripHotel itripHotel)throws Exception;

    public Integer itriptxDeleteItripHotelById(Long id)throws Exception;

    public Page<ItripHotel> queryItripHotelPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize)throws Exception;

    Dto findHotCity(Integer type)throws Exception;

    Dto findTradeArea(Integer cityId)throws Exception;
}

package org.itrip.biz.service.itripHotelTradingArea;
import org.itrip.pojo.ItripHotelTradingArea;
import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.Map;
import org.itrip.common.Page;
/**
 * @Author: wangrj
 * @Date: 2019/5/20 下午9:26
 */
public interface ItripHotelTradingAreaService {

    public ItripHotelTradingArea getItripHotelTradingAreaById(Long id)throws Exception;

    public List<ItripHotelTradingArea>	getItripHotelTradingAreaListByMap(Map<String, Object> param)throws Exception;

    public Integer getItripHotelTradingAreaCountByMap(Map<String, Object> param)throws Exception;

    public Integer itriptxAddItripHotelTradingArea(ItripHotelTradingArea itripHotelTradingArea)throws Exception;

    public Integer itriptxModifyItripHotelTradingArea(ItripHotelTradingArea itripHotelTradingArea)throws Exception;

    public Integer itriptxDeleteItripHotelTradingAreaById(Long id)throws Exception;

    public Page<ItripHotelTradingArea> queryItripHotelTradingAreaPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize)throws Exception;
}

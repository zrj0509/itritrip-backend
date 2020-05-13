package org.itrip.biz.service.itripHotelFeature;
import org.itrip.pojo.ItripHotelFeature;
import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.Map;
import org.itrip.common.Page;
/**
 * @Author: wangrj
 * @Date: 2019/5/20 下午9:26
 */
public interface ItripHotelFeatureService {

    public ItripHotelFeature getItripHotelFeatureById(Long id)throws Exception;

    public List<ItripHotelFeature>	getItripHotelFeatureListByMap(Map<String, Object> param)throws Exception;

    public Integer getItripHotelFeatureCountByMap(Map<String, Object> param)throws Exception;

    public Integer itriptxAddItripHotelFeature(ItripHotelFeature itripHotelFeature)throws Exception;

    public Integer itriptxModifyItripHotelFeature(ItripHotelFeature itripHotelFeature)throws Exception;

    public Integer itriptxDeleteItripHotelFeatureById(Long id)throws Exception;

    public Page<ItripHotelFeature> queryItripHotelFeaturePageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize)throws Exception;
}

package org.itrip.biz.service.itripImage;
import org.itrip.pojo.ItripImage;
import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.Map;
import org.itrip.common.Page;
/**
 * @Author: wangrj
 * @Date: 2019/5/20 下午9:26
 */
public interface ItripImageService {

    public ItripImage getItripImageById(Long id)throws Exception;

    public List<ItripImage>	getItripImageListByMap(Map<String, Object> param)throws Exception;

    public Integer getItripImageCountByMap(Map<String, Object> param)throws Exception;

    public Integer itriptxAddItripImage(ItripImage itripImage)throws Exception;

    public Integer itriptxModifyItripImage(ItripImage itripImage)throws Exception;

    public Integer itriptxDeleteItripImageById(Long id)throws Exception;

    public Page<ItripImage> queryItripImagePageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize)throws Exception;
}

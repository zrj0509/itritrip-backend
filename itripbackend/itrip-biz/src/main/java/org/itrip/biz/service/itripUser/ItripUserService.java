package org.itrip.biz.service.itripUser;
import org.itrip.pojo.ItripUser;
import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.Map;
import org.itrip.common.Page;
/**
 * @Author: wangrj
 * @Date: 2019/5/20 下午9:26
 */
public interface ItripUserService {

    public ItripUser getItripUserById(Long id)throws Exception;

    public List<ItripUser>	getItripUserListByMap(Map<String, Object> param)throws Exception;

    public Integer getItripUserCountByMap(Map<String, Object> param)throws Exception;

    public Integer itriptxAddItripUser(ItripUser itripUser)throws Exception;

    public Integer itriptxModifyItripUser(ItripUser itripUser)throws Exception;

    public Integer itriptxDeleteItripUserById(Long id)throws Exception;

    public Page<ItripUser> queryItripUserPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize)throws Exception;
}

package org.itrip.biz.service.itripTradeEnds;
import org.itrip.mapper.itripTradeEnds.ItripTradeEndsMapper;
import org.itrip.pojo.ItripTradeEnds;
import org.itrip.common.EmptyUtils;
import org.itrip.common.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import org.itrip.common.Constants;
@Service
public class ItripTradeEndsServiceImpl implements ItripTradeEndsService {

    @Resource
    private ItripTradeEndsMapper itripTradeEndsMapper;

    public ItripTradeEnds getItripTradeEndsById(Long id)throws Exception{
        return itripTradeEndsMapper.getItripTradeEndsById(id);
    }

    public List<ItripTradeEnds>	getItripTradeEndsListByMap(Map<String,Object> param)throws Exception{
        return itripTradeEndsMapper.getItripTradeEndsListByMap(param);
    }

    public Integer getItripTradeEndsCountByMap(Map<String,Object> param)throws Exception{
        return itripTradeEndsMapper.getItripTradeEndsCountByMap(param);
    }

    public Integer itriptxAddItripTradeEnds(ItripTradeEnds itripTradeEnds)throws Exception{
            //itripTradeEnds.setCreationDate(new Date());
            return itripTradeEndsMapper.insertItripTradeEnds(itripTradeEnds);
    }

    public Integer itriptxModifyItripTradeEnds(ItripTradeEnds itripTradeEnds)throws Exception{
       // itripTradeEnds.setModifyDate(new Date());
        return itripTradeEndsMapper.updateItripTradeEnds(itripTradeEnds);
    }

    public Integer itriptxDeleteItripTradeEndsById(Long id)throws Exception{
        return itripTradeEndsMapper.deleteItripTradeEndsById(id);
    }

    public Page<ItripTradeEnds> queryItripTradeEndsPageByMap(Map<String,Object> param,Integer pageNo,Integer pageSize)throws Exception{
        Integer total = itripTradeEndsMapper.getItripTradeEndsCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripTradeEnds> itripTradeEndsList = itripTradeEndsMapper.getItripTradeEndsListByMap(param);
        page.setRows(itripTradeEndsList);
        return page;
    }

}

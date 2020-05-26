package org.itrip.biz.service.itripHotel;
import org.itrip.common.DtoUtil;
import org.itrip.dto.Dto;
import org.itrip.mapper.itripAreaDic.ItripAreaDicMapper;
import org.itrip.mapper.itripHotel.ItripHotelMapper;
import org.itrip.pojo.ItripAreaDic;
import org.itrip.pojo.ItripHotel;
import org.itrip.common.EmptyUtils;
import org.itrip.common.Page;
import org.itrip.vo.ItripAreaDicVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

import org.itrip.common.Constants;
@Service
public class ItripHotelServiceImpl implements ItripHotelService {

    @Resource
    private ItripHotelMapper itripHotelMapper;
    @Resource
    private ItripAreaDicMapper itripAreaDicMapper;
    public ItripHotel getItripHotelById(Long id)throws Exception{
        return itripHotelMapper.getItripHotelById(id);
    }

    public List<ItripHotel>	getItripHotelListByMap(Map<String,Object> param)throws Exception{
        return itripHotelMapper.getItripHotelListByMap(param);
    }

    public Integer getItripHotelCountByMap(Map<String,Object> param)throws Exception{
        return itripHotelMapper.getItripHotelCountByMap(param);
    }

    public Integer itriptxAddItripHotel(ItripHotel itripHotel)throws Exception{
            itripHotel.setCreationDate(new Date());
            return itripHotelMapper.insertItripHotel(itripHotel);
    }

    public Integer itriptxModifyItripHotel(ItripHotel itripHotel)throws Exception{
        itripHotel.setModifyDate(new Date());
        return itripHotelMapper.updateItripHotel(itripHotel);
    }

    public Integer itriptxDeleteItripHotelById(Long id)throws Exception{
        return itripHotelMapper.deleteItripHotelById(id);
    }

    public Page<ItripHotel> queryItripHotelPageByMap(Map<String,Object> param,Integer pageNo,Integer pageSize)throws Exception{
        Integer total = itripHotelMapper.getItripHotelCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripHotel> itripHotelList = itripHotelMapper.getItripHotelListByMap(param);
        page.setRows(itripHotelList);
        return page;
    }

    @Override
    public Dto findHotCity(Integer type) throws Exception {
        //判断
        if(EmptyUtils.isEmpty(type)){
            return DtoUtil.returnFail(" hotelId不能为空","10201");
        }
        //参数
        Map params = new HashMap();
        params.put("isHot",1);
        params.put("isChina",type);

        //列表
        List<ItripAreaDic>  list=itripAreaDicMapper.getItripAreaDicListByMap(params);
        //前端数据
        List<ItripAreaDicVO> listVo=null;

        //判断列表是否为空
        if(EmptyUtils.isNotEmpty(list)){
            //实例化对象
            listVo =new ArrayList<>();

            //遍历数据
            for (ItripAreaDic itripAreaDic : list){
                //实例化vo
                ItripAreaDicVO vo = new ItripAreaDicVO();
                //拷贝到前端
                BeanUtils.copyProperties(itripAreaDic,vo);
                //添加到集合中
                listVo.add(vo);
            }

        }else{
            throw new Exception("未查询到数据");
        }

        //返回vo数据
        return DtoUtil.returnDataSuccess(listVo);
    }

    @Override
    public Dto findTradeArea(Integer cityId) throws Exception {
        //判断
        if(EmptyUtils.isEmpty(cityId)){
            return DtoUtil.returnFail("cityId不能为空","10203");
        }
        //参数
        Map params = new HashMap();
        params.put("isTradingArea",1);
        params.put("parent",cityId);
        //列表
        List<ItripAreaDic>  list=itripAreaDicMapper.getItripAreaDicListByMap(params);
        //前端数据
        List<ItripAreaDicVO> listVo=null;

        //判断列表是否为空
        if(EmptyUtils.isNotEmpty(list)){
            //实例化对象
            listVo =new ArrayList<>();

            //遍历数据
            for (ItripAreaDic itripAreaDic : list){
                //实例化vo
                ItripAreaDicVO vo = new ItripAreaDicVO();
                //拷贝到前端
                BeanUtils.copyProperties(itripAreaDic,vo);
                //添加到集合中
                listVo.add(vo);
            }

        }else{
            throw new Exception("未查询到数据");
        }

        //返回vo数据
        return DtoUtil.returnDataSuccess(listVo);
    }

}

package org.itrip.biz.controller;

import org.itrip.biz.service.itripHotel.ItripHotelService;
import org.itrip.biz.service.itripLabelDic.ItripLabelDicService;
import org.itrip.common.DtoUtil;
import org.itrip.common.EmptyUtils;
import org.itrip.dto.Dto;
import org.itrip.pojo.ItripLabelDic;
import org.itrip.vo.ItripLabelDicVO;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HotelController
 * @Description: TODO
 * @Author 44401
 * @Date 2020/5/23
 * @Version V1.0
 **/
@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    //业务接口
    @Resource
    private ItripHotelService itripHotelService;

    @Resource
    private ItripLabelDicService itripLabelDicService;


    /**
     *
     *查询酒店特色列表
     * @return
     */
    @GetMapping("/queryhotelfeature")
    public Dto queryHotelFeature(){
        List<ItripLabelDic> itripLabelDics=null;
        List<ItripLabelDicVO> vos=null;
        try {
            Map<String,Object> param =new HashMap<>();
            param.put("parentId",16);
            //调用业务
            itripLabelDics = itripLabelDicService.getItripLabelDicListByMap(param);
            //判断是否为空
            if (EmptyUtils.isNotEmpty(itripLabelDics)){
                vos =new ArrayList<>();
                //循环
                for (ItripLabelDic dic: itripLabelDics){
                    //前端数据
                    ItripLabelDicVO itripLabelDicVO =new ItripLabelDicVO();
                    //后端数据复制到vo
                    BeanUtils.copyProperties(dic,itripLabelDicVO);
                    //添加到集合中
                    vos.add(itripLabelDicVO);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败","10202");
        }
        //返回数据
        return DtoUtil.returnDataSuccess(vos);
    }


    /**
     * 查询热门城市
     * @param type:区分国内国外
     * @return
     */
    @GetMapping("/queryhotcity/{type}")
    public Dto queryHotCity(@PathVariable("type") Integer type){
        Dto dto = null;
        try {
            dto = itripHotelService.findHotCity(type);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败","10202");
        }
        return dto;
    }



    /**
     * 根据城市查询商圈
     * @param cityId:城市Id
     * @return
     */
    @GetMapping("/querytradearea/{cityId}")
    public Dto queryTradeArea(@PathVariable("cityId") Integer cityId){
        Dto dto = null;
        try {
            dto = itripHotelService.findTradeArea(cityId);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败","10202");
        }
        return dto;
    }
}

package org.itrip.search.service;

import com.alibaba.druid.util.StringUtils;
import org.itrip.common.Constants;
import org.itrip.common.EmptyUtils;
import org.itrip.common.Page;
import org.itrip.search.bean.ItripHotelVO;
import org.itrip.vo.hotel.SearchHotCityVO;
import org.itrip.vo.hotel.SearchHotelVO;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.TermsOptions;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService{
    @Resource
    private SolrTemplate solrTemplate;
    @Override
    public Page SearchItripHotelPage(SearchHotelVO vo) throws Exception {
        //创建query查询对象
        Query query =new SimpleQuery("*:*");
        //q条件
        //1>目的地；
        Criteria criteria =new Criteria("destination");
        //判断
        if (!StringUtils.isEmpty(vo.getDestination())){
            criteria=criteria.contains(vo.getDestination());
        }
        //2>关键词
        if(!StringUtils.isEmpty(vo.getKeywords())){
            //注意keyword是solr库中的，不能用前端传过来的keywords
            criteria =criteria.and("keyword").contains(vo.getKeywords());
        }

        //fq
        //3>位置（商圈）
        // "tradingAreaIds": ",3664,3669,"
        if(!StringUtils.isEmpty(vo.getTradeAreaIds())){
            //截取字符串
            String[] tradingAreaIds=vo.getTradeAreaIds().split(",");
            //循环
            for (String str:tradingAreaIds) {
                criteria =criteria.and("tradingAreaIds").contains(","+str+",");
            }
        }

        //4.价格以上 (300)->自己的工具包，int
        if(EmptyUtils.isNotEmpty(vo.getMinPrice())){
            //<=页面输出的价格
            criteria =criteria.and("minPrice").lessThanEqual(vo.getMinPrice());
        }
        //价格以下(50)
        if(EmptyUtils.isNotEmpty(vo.getMinPrice())){
            //>=页面输出的价格
            criteria =criteria.and("minPrice").greaterThanEqual(vo.getMinPrice());
        }

        //5.星级
        if(EmptyUtils.isNotEmpty(vo.getHotelLevel())){
            //>=页面输出的价格
            criteria =criteria.and("hotelLevel").is(vo.getHotelLevel());
        }

        //6.特色
        //"featureIds": ",17,115,116,117,"
        if(!StringUtils.isEmpty(vo.getFeatureIds())){
            //截取字符串
            String[] featureIds=vo.getFeatureIds().split(",");
            //循环
            for (String str:featureIds) {
                criteria =criteria.and("featureIds").contains(","+str+",");
            }
        }

        //7查询-升序
        if(!StringUtils.isEmpty(vo.getAscSort())){
            query.addSort(new Sort(Sort.Direction.ASC,vo.getAscSort()));
        }

        //查询-降序
        if(!StringUtils.isEmpty(vo.getDescSort())){
            query.addSort(new Sort(Sort.Direction.DESC,vo.getDescSort()));
        }
        //8分页
        //当前页1
        int pageNo= Constants.DEFAULT_PAGE_NO;
        //每页显示的个数 10
        int pageSize= Constants.DEFAULT_PAGE_SIZE;
        //判断不能为空
        if (EmptyUtils.isNotEmpty(vo.getPageSize())){
            pageSize =vo.getPageSize();
        }
        //设置当前第一页///////
        query.setOffset((pageNo-1)*pageSize);
        query.setRows(pageSize);
        ///////////////////
        //9.添加条件对象
        query.addCriteria(criteria);
        //10.模板查询
        ScoredPage<ItripHotelVO> vos= solrTemplate.queryForPage(query, ItripHotelVO.class);

        //11.Page封装对象
        Page page =new Page(pageNo,pageSize,new Long(vos.getTotalElements()).intValue());

        //12.设置列表List
        page.setRows(vos.getContent());

        return page;
    }

    @Override
    public  List<ItripHotelVO> searchItripHotelListByHotCity(SearchHotCityVO vo) {
        System.out.println("cityId==="+vo.getCityId());
        //创建query查询对象
        Query query =new SimpleQuery("*:*");
        Criteria criteria =new Criteria("cityId");
        //判断
        if (!StringUtils.isEmpty(String.valueOf(vo.getCityId()))){
            criteria.is(vo.getCityId());
        }
        query.setRows(vo.getCount());
        query.addCriteria(criteria);
        //10.模板查询
        ScoredPage<ItripHotelVO> vos= solrTemplate.queryForPage(query, ItripHotelVO.class);
        List<ItripHotelVO> list=vos.getContent();
        return list;
    }

}

package org.itrip.search.service;

import org.itrip.common.Page;
import org.itrip.search.bean.ItripHotelVO;
import org.itrip.vo.hotel.SearchHotCityVO;
import org.itrip.vo.hotel.SearchHotelVO;

import java.util.List;

public interface SearchService {
    Page SearchItripHotelPage(SearchHotelVO vo) throws Exception;

    List<ItripHotelVO> searchItripHotelListByHotCity(SearchHotCityVO vo) throws Exception;
}

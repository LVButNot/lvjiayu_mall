package com.lvjiayu.mall.service.impl;

import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.dao.CarouselMapper;
import com.lvjiayu.mall.entity.Carousel;
import com.lvjiayu.mall.service.CarouselService;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {
    @Resource
    private CarouselMapper carouselMapper;
    @Override
    public PageResult getCarouselPage(PageQueryUtil pageUtil) {
        //public PageResult(List<T> list, int totalCount, int pageSize, int currPage)
        List<Carousel> carousels = carouselMapper.findCarouselList(pageUtil);
        int total = carouselMapper.getTotalCarousels(pageUtil);
        return new PageResult(carousels, total, pageUtil.getLimit(), pageUtil.getPage());
    }

    @Override
    public String saveCarousel(Carousel carousel) {
        if(carouselMapper.insertSelective(carousel) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateCarousel(Carousel carousel) {
        if(carouselMapper.updateByPrimaryKeySelective(carousel) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Carousel getCarouselById(Integer id) {
        Carousel carousel = carouselMapper.selectByPrimaryKey(id);
        return carousel;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if(ids.length < 1){
            return false;
        }
        return carouselMapper.deleteBatch(ids) > 0;
    }
}

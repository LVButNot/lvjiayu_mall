package com.lvjiayu.mall.service.impl;

import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.dao.GoodsMapper;
import com.lvjiayu.mall.entity.MallGoods;
import com.lvjiayu.mall.service.GoodsService;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.PageResult;
import com.lvjiayu.mall.util.ResultGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public String insertMallGoods(MallGoods mallGoods) {
        if(goodsMapper.insertSelective(mallGoods) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateMallGoods(MallGoods mallGoods) {
        //更新之前，要校验是否存在要更改的商品
        MallGoods temp = goodsMapper.selectByPrimaryKey(mallGoods.getGoodsId());
        if(temp == null){
            return ServiceResultEnum.DATA_NOT_EXIST.name();
        }
        mallGoods.setUpdateTime(new Date());
        if(goodsMapper.updateByPrimaryKeySelective(mallGoods) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public PageResult getGoodsPage(PageQueryUtil pageUtil) {
        List<MallGoods> list = goodsMapper.findGoodsList(pageUtil);
        int total = goodsMapper.getTotalGoods(pageUtil);
        PageResult pageResult = new PageResult(list, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public boolean batchUpdateSellStatus(Long[] ids, Integer status) {
        return goodsMapper.batchUpdateSellStatus(ids, status) > 0;
    }
}

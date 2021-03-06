package com.ve.itemcenter.core.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ve.itemcenter.common.domain.qto.SkuPropertyQTO;
import com.ve.itemcenter.core.domain.SkuPropertyDO;

@Service
public interface SkuPropertyDAO {

	/**
	 * 增加SKU属性
	 * 
	 * @param skuPropertyDO
	 * @return
	 */
	Long addSkuProperty(SkuPropertyDO skuPropertyDO);

	/**
	 * 根据id,sellerId获取SKU属性
	 * 
	 * @param sellerId
	 * @param id
	 * @return
	 */
	SkuPropertyDO getSkuProperty(Long id, Long sellerId);

	/**
	 * 根据id,sellerId删除SKU属性
	 * 
	 * @param sellerId
	 * @param id
	 * @return
	 */
	int deleteSkuProperty(Long id, Long sellerId);

	/**
	 * 根据skuId,sellerId删除SKU属性
	 * 
	 * @param sellerId
	 * @param id
	 * @return
	 */
	int deleteSkuPropertyBySkuId(Long skuId, Long sellerId);

	/**
	 * 更新SKU属性信息
	 * 
	 * @param skuPropertyDO
	 * @return
	 */
	int updateSkuProperty(SkuPropertyDO skuPropertyDO);

	/**
	 * 返回SKU属性列表
	 * 
	 * @param skuPropertyQTO
	 * @return
	 */
	List<SkuPropertyDO> querySkuProperty(SkuPropertyQTO skuPropertyQTO);

}
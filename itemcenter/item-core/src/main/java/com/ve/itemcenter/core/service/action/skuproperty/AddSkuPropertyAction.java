package com.ve.itemcenter.core.service.action.skuproperty;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.ve.itemcenter.common.api.ItemResponse;
import com.ve.itemcenter.common.constant.ActionEnum;
import com.ve.itemcenter.common.constant.ResCodeNum;
import com.ve.itemcenter.common.domain.dto.SkuPropertyDTO;
import com.ve.itemcenter.common.domain.qto.SkuPropertyQTO;
import com.ve.itemcenter.core.exception.ItemException;
import com.ve.itemcenter.core.manager.ItemSkuManager;
import com.ve.itemcenter.core.manager.SkuPropertyManager;
import com.ve.itemcenter.core.service.ItemRequest;
import com.ve.itemcenter.core.service.RequestContext;
import com.ve.itemcenter.core.service.action.Action;
import com.ve.itemcenter.core.util.ResponseUtil;

/**
 * 增加商品属性Action
 * 
 * @author chen.huang
 *
 */
@Service
public class AddSkuPropertyAction implements Action {
	private static final Logger log = LoggerFactory.getLogger(AddSkuPropertyAction.class);
	@Resource
	private SkuPropertyManager skuPropertyManager;

	@Resource
	private ItemSkuManager itemSkuManager;

	@Resource
	TransactionTemplate transactionTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public ItemResponse execute(final RequestContext context) throws ItemException {
		@SuppressWarnings("rawtypes")
		ItemResponse response = transactionTemplate.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				try {
					ItemResponse response = null;
					ItemRequest request = context.getRequest();
					// 验证skuproperty列表
					if (request.getParam("skuPropertyDTOList") == null) {
						return ResponseUtil
								.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "skuPropertyDTOList is null");
					}
					// 验证skuId
					if (request.getParam("skuId") == null) {
						return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "skuId is null");
					}
					// 验证sellerId
					if (request.getParam("sellerId") == null) {
						return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "sellerId is null");
					}

					List<SkuPropertyDTO> skuPropertyDTOList = (List<SkuPropertyDTO>) request
							.getParam("skuPropertyDTOList");
					Long skuId = (Long) request.getParam("skuId");
					Long sellerId = (Long) request.getParam("sellerId");

					// 增加skuproperty列表
					List<SkuPropertyDTO> returnSkuPropertyDTOList = skuPropertyManager
							.addSkuProperty(skuId, sellerId, skuPropertyDTOList);//

					// 根据skuId和sellerId去查找sku_property表，然后将里面的属性拼成code_value更新到item_sku表中
					// 比如将 红色,17码,50g拼接成 "红色-17码-50g"
					SkuPropertyQTO skuPropertyQTO = new SkuPropertyQTO();
					skuPropertyQTO.setSkuId(skuId);
					skuPropertyQTO.setSellerId(sellerId);
					List<SkuPropertyDTO> codeValuePropertyDTOList = skuPropertyManager
							.querySkuProperty(skuPropertyQTO);
					itemSkuManager.updateItemSkuCodeValue(skuId, sellerId, codeValuePropertyDTOList);

					response = ResponseUtil.getSuccessResponse(returnSkuPropertyDTOList, returnSkuPropertyDTOList
							.size());
					return response;
				} catch (ItemException e) {
					status.setRollbackOnly();
					log.error(e.toString());
					return ResponseUtil.getErrorResponse(e.getResCodeNum(), e.getMessage());

				}
			}
		});
		return response;

	}

	@Override
	public String getName() {
		return ActionEnum.ADD_SKU_PROPERTY.getActionName();
	}
}

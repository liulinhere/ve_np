package com.ve.itemcenter.core.service.action.itemsku;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ve.itemcenter.common.api.ItemResponse;
import com.ve.itemcenter.common.constant.ActionEnum;
import com.ve.itemcenter.common.constant.ResCodeNum;
import com.ve.itemcenter.core.exception.ItemException;
import com.ve.itemcenter.core.manager.ItemSkuManager;
import com.ve.itemcenter.core.service.ItemRequest;
import com.ve.itemcenter.core.service.RequestContext;
import com.ve.itemcenter.core.service.action.Action;
import com.ve.itemcenter.core.util.ResponseUtil;

/**
 * 更新商品销售属性(ItemSku) Action
 * 
 * @author chen.huang
 *
 */

@Service
public class IncreaseItemSkuStockAction implements Action {
	private static final Logger log = LoggerFactory.getLogger(IncreaseItemSkuStockAction.class);
	@Resource
	private ItemSkuManager itemSkuManager;

	@Override
	public ItemResponse execute(RequestContext context) throws ItemException {
		ItemResponse response = null;
		ItemRequest request = context.getRequest();
		if (request.getLong("skuId") == null) {
			return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "ItemSkuID is missing");
		}
		if (request.getLong("sellerId") == null) {
			return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "sellerId is missing");
		}
		if (request.getLong("increasedNumber") == null) {
			return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "increasedNumber is missing");
		}
		long skuId = request.getLong("skuId");
		long sellerId = request.getLong("sellerId");
		long increasedNumber = request.getLong("increasedNumber");
		try {
			itemSkuManager.increaseItemSkuStock(skuId, sellerId, increasedNumber);
			response = ResponseUtil.getSuccessResponse(true);
			return response;
		} catch (ItemException e) {
			response = ResponseUtil.getErrorResponse(e.getErrorCode(), e.getMessage());
			log.error("do action:" + request.getCommand() + " occur Exception:" + e.getMessage(), e);
			return response;
		}

	}

	@Override
	public String getName() {
		return ActionEnum.INCREASE_ITEM_SKU_STOCK.getActionName();
	}
}

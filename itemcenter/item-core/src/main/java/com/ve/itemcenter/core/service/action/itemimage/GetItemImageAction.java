package com.ve.itemcenter.core.service.action.itemimage;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ve.itemcenter.common.api.ItemResponse;
import com.ve.itemcenter.common.constant.ActionEnum;
import com.ve.itemcenter.common.constant.ResCodeNum;
import com.ve.itemcenter.common.domain.dto.ItemImageDTO;
import com.ve.itemcenter.core.exception.ItemException;
import com.ve.itemcenter.core.manager.ItemImageManager;
import com.ve.itemcenter.core.service.ItemRequest;
import com.ve.itemcenter.core.service.RequestContext;
import com.ve.itemcenter.core.service.action.Action;
import com.ve.itemcenter.core.util.ResponseUtil;

/**
 * 查看商品属性模板Action
 * 
 * @author chen.huang
 *
 */
@Service
public class GetItemImageAction implements Action {
	private static final Logger log = LoggerFactory.getLogger(GetItemImageAction.class);

	@Resource
	private ItemImageManager itemImageManager;

	@Override
	public ItemResponse execute(RequestContext context) throws ItemException {
		ItemImageDTO itemImageDTO = null;
		ItemResponse response = null;
		ItemRequest request = context.getRequest();
		// 验证ID
		if (request.getLong("ID") == null) {
			return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "itemImageID is missing");
		}
		if (request.getLong("sellerId") == null) {
			return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "sellerId is missing");
		}
		Long itemImageId = request.getLong("ID");// SKU属性模板ID
		Long sellerId = request.getLong("sellerId");// 供应商ID
		try {
			itemImageDTO = itemImageManager.getItemImage(itemImageId, sellerId);
		} catch (ItemException e) {
			response = ResponseUtil.getErrorResponse(e.getErrorCode(), e.getMessage());
			log.error("do action:" + request.getCommand() + " occur Exception:" + e.getMessage(), e);
			return response;
		}
		response = ResponseUtil.getSuccessResponse(itemImageDTO);
		return response;
	}

	@Override
	public String getName() {
		return ActionEnum.GET_ITEM_IMAGE.getActionName();
	}
}

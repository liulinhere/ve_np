package com.ve.itemcenter.core.service.action.itemcategory;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ve.itemcenter.common.api.ItemResponse;
import com.ve.itemcenter.common.constant.ActionEnum;
import com.ve.itemcenter.common.constant.ResCodeNum;
import com.ve.itemcenter.common.domain.dto.ItemCategoryDTO;
import com.ve.itemcenter.core.exception.ItemException;
import com.ve.itemcenter.core.manager.ItemCategoryManager;
import com.ve.itemcenter.core.service.ItemRequest;
import com.ve.itemcenter.core.service.RequestContext;
import com.ve.itemcenter.core.service.action.Action;
import com.ve.itemcenter.core.util.ResponseUtil;

/**
 * 查看商品销售属性(ItemCategory) Action
 * 
 * @author chen.huang
 *
 */
@Service
public class GetItemCategoryAction implements Action {
	private static final Logger log = LoggerFactory.getLogger(GetItemCategoryAction.class);

	@Resource
	private ItemCategoryManager itemCategoryManager;

	@Override
	public ItemResponse execute(RequestContext context) throws ItemException {
		ItemCategoryDTO itemCategoryDTO = null;
		ItemResponse response = null;
		ItemRequest request = context.getRequest();
		// 验证ID
		if (request.getLong("ID") == null) {
			return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "itemCategoryID is missing");
		}
		Integer itemCategoryId = request.getInteger("ID");// 商品销售属性(ItemCategory)ID
		try {
			itemCategoryDTO = itemCategoryManager.getItemCategory(itemCategoryId);
		} catch (ItemException e) {
			response = ResponseUtil.getErrorResponse(e.getErrorCode(), e.getMessage());
			log.error("do action:" + request.getCommand() + " occur Exception:" + e.getMessage(), e);
			return response;
		}
		response = ResponseUtil.getSuccessResponse(itemCategoryDTO);
		return response;
	}

	@Override
	public String getName() {
		return ActionEnum.GET_ITEM_CATEGORY.getActionName();
	}
}

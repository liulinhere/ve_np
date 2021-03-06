package com.ve.itemcenter.core.service.action.itempropertytmpl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ve.itemcenter.common.api.ItemResponse;
import com.ve.itemcenter.common.constant.ActionEnum;
import com.ve.itemcenter.common.constant.ResCodeNum;
import com.ve.itemcenter.common.domain.dto.ItemPropertyTmplDTO;
import com.ve.itemcenter.core.exception.ItemException;
import com.ve.itemcenter.core.manager.ItemPropertyTmplManager;
import com.ve.itemcenter.core.service.ItemRequest;
import com.ve.itemcenter.core.service.RequestContext;
import com.ve.itemcenter.core.service.action.Action;
import com.ve.itemcenter.core.util.ResponseUtil;

/**
 * 增加商品属性模板Action
 * 
 * @author chen.huang
 *
 */
@Service
public class AddItemPropertyTmplAction implements Action {
	private static final Logger log = LoggerFactory.getLogger(AddItemPropertyTmplAction.class);
	@Resource
	private ItemPropertyTmplManager itemPropertyTmplManager;

	@Override
	public ItemResponse execute(RequestContext context) throws ItemException {
		ItemResponse response = null;
		ItemRequest request = context.getRequest();
		// 验证DTO是否为空
		if (request.getParam("itemPropertyTmplDTO") == null) {
			return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "itemPropertyTmplDTO is null");
		}
		ItemPropertyTmplDTO itemPropertyTmplDTO = (ItemPropertyTmplDTO) request.getParam("itemPropertyTmplDTO");
		try {
			itemPropertyTmplDTO = itemPropertyTmplManager.addItemPropertyTmpl(itemPropertyTmplDTO);// 新增加的itemPropertyTmplDO
			response = ResponseUtil.getSuccessResponse(itemPropertyTmplDTO);
			return response;
		} catch (ItemException e) {
			response = ResponseUtil.getErrorResponse(e.getErrorCode(), e.getMessage());
			log.error("do action:" + request.getCommand() + " occur Exception:" + e.getMessage(), e);
			return response;
		}
	}

	@Override
	public String getName() {
		return ActionEnum.ADD_ITEM_PROPERTY_TMPL.getActionName();
	}
}

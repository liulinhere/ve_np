package com.ve.itemcenter.core.service.action.skupropertytmpl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ve.itemcenter.common.api.ItemResponse;
import com.ve.itemcenter.common.constant.ActionEnum;
import com.ve.itemcenter.common.constant.ResCodeNum;
import com.ve.itemcenter.common.domain.dto.SkuPropertyTmplDTO;
import com.ve.itemcenter.core.exception.ItemException;
import com.ve.itemcenter.core.manager.SkuPropertyTmplManager;
import com.ve.itemcenter.core.service.ItemRequest;
import com.ve.itemcenter.core.service.RequestContext;
import com.ve.itemcenter.core.service.action.Action;
import com.ve.itemcenter.core.util.ResponseUtil;

/**
 * 更新商品属性模板Action
 * 
 * @author chen.huang
 *
 */

@Service
public class UpdateSkuPropertyTmplAction implements Action {
	private static final Logger log = LoggerFactory.getLogger(UpdateSkuPropertyTmplAction.class);
	@Resource
	private SkuPropertyTmplManager skuPropertyTmplManager;

	@Override
	public ItemResponse execute(RequestContext context) throws ItemException {
		ItemResponse response = null;
		ItemRequest request = context.getRequest();
		// 验证DTO是否为空
		if (request.getParam("skuPropertyTmplDTO") == null) {
			return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "skuPropertyTmplDTO is null");
		}
		SkuPropertyTmplDTO skuPropertyTmplDTO = (SkuPropertyTmplDTO) request.getParam("skuPropertyTmplDTO");
		try {
			Boolean isSuccessfullyUpdated = skuPropertyTmplManager.updateSkuPropertyTmpl(skuPropertyTmplDTO);
			response = ResponseUtil.getSuccessResponse(isSuccessfullyUpdated);
			return response;
		} catch (ItemException e) {
			response = ResponseUtil.getErrorResponse(e.getErrorCode(), e.getMessage());
			log.error("do action:" + request.getCommand() + " occur Exception:" + e.getMessage(), e);
			return response;
		}

	}

	@Override
	public String getName() {
		return ActionEnum.UPDATE_SKU_PROPERTY_TMPL.getActionName();
	}
}

package com.ve.itemcenter.core.service.action.comment;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ve.itemcenter.common.api.ItemResponse;
import com.ve.itemcenter.common.constant.ActionEnum;
import com.ve.itemcenter.common.constant.ResCodeNum;
import com.ve.itemcenter.common.domain.dto.ItemCommentDTO;
import com.ve.itemcenter.common.domain.qto.ItemCommentQTO;
import com.ve.itemcenter.core.exception.ItemException;
import com.ve.itemcenter.core.manager.ItemCommentManager;
import com.ve.itemcenter.core.service.ItemRequest;
import com.ve.itemcenter.core.service.RequestContext;
import com.ve.itemcenter.core.service.action.Action;
import com.ve.itemcenter.core.util.ResponseUtil;

/**
 * 根据商品ID查询商品评论Action
 * 
 * @author chen.huang
 *
 */
@Service
public class QueryItemCommentAction implements Action {
	private static final Logger log = LoggerFactory.getLogger(QueryItemCommentAction.class);
	@Resource
	private ItemCommentManager itemCommentManager;

	@Override
	public ItemResponse execute(RequestContext context) throws ItemException {
		ItemResponse response = null;
		ItemRequest request = context.getRequest();
		if (request.getParam("itemCommentQTO") == null) {
			return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "itemCommentQTO is null");
		}
		ItemCommentQTO itemCommentQTO = (ItemCommentQTO) request.getParam("itemCommentQTO");
		try {
			List<ItemCommentDTO> itemCommentDTOList = itemCommentManager.queryItemComment(itemCommentQTO);
			response = ResponseUtil.getSuccessResponse(itemCommentDTOList, itemCommentQTO.getTotalCount());
			return response;
		} catch (ItemException e) {
			response = ResponseUtil.getErrorResponse(e.getErrorCode(), e.getMessage());
			log.error("do action:" + request.getCommand() + " occur Exception:" + e.getMessage(), e);
			return response;
		}

	}

	@Override
	public String getName() {
		return ActionEnum.QUERY_ITEMCOMMENT.getActionName();
	}
}

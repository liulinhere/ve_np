package com.ve.tradecenter.core.service.action.cart;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ve.tradecenter.common.api.TradeResponse;
import com.ve.tradecenter.common.constant.ResponseCode;
import com.ve.tradecenter.core.exception.TradeException;
import com.ve.tradecenter.core.manager.CartItemManager;
import com.ve.tradecenter.core.service.RequestContext;
import com.ve.tradecenter.core.service.ResponseUtils;
import com.ve.tradecenter.core.service.TradeRequest;
import com.ve.tradecenter.core.service.action.Action;
import com.ve.tradecenter.core.service.action.ActionEnum;

/**
 * 清空未登入用户的购物车
 * @author cwr
 */
@Service
public class CleanCart implements Action{
	private static final Logger log = LoggerFactory.getLogger(AddCartItem.class);

	@Resource
	private CartItemManager cartItemManager;

	@Override
	public TradeResponse execute(RequestContext context) throws TradeException {
		TradeRequest request = context.getRequest();
		TradeResponse<Boolean> response = null;
		if(request.getParam("sessionId") == null){
			log.error("sessionId is null");
			return ResponseUtils.getFailResponse(ResponseCode.PARAM_E_PARAM_MISSING,"sessionId is null");
		}
		String sessionId = (String)request.getParam("sessionId");
		int result =0;
		try{
			result = this.cartItemManager.cleanCart(sessionId);
		}catch(TradeException e){
			log.error("db error: " ,e);
			return ResponseUtils.getFailResponse(ResponseCode.SYS_E_DATABASE_ERROR);
		}
		if(result > 0 ){
			return ResponseUtils.getSuccessResponse(true);
		}else{
			log.error("records doesn' exist");
			return ResponseUtils.getFailResponse(ResponseCode.BIZ_E_RECORD_NOT_EXIST,"user cart is null");
		}
	}

	@Override
	public String getName() {
		return ActionEnum.CLEAN_CART.getActionName();
	}
}

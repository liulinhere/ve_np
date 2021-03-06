package com.ve.marketingcenter.core.service.action.coupon;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ve.marketingcenter.common.api.MarketingResponse;
import com.ve.marketingcenter.common.constant.ActionEnum;
import com.ve.marketingcenter.common.constant.coupon.CouponStatus;
import com.ve.marketingcenter.common.domain.dto.coupon.OrderActivityDTO;
import com.ve.marketingcenter.core.exception.MarketingException;
import com.ve.marketingcenter.core.manager.coupon.OrderActivityManager;
import com.ve.marketingcenter.core.service.RequestContext;
import com.ve.marketingcenter.core.service.action.TransAction;
import com.ve.marketingcenter.core.util.ResponseUtil;

@Service
public class UseOrderActivity extends TransAction {
	@Resource
	OrderActivityManager orderActivityManager;

	/**
	 * 营销活动使用接口（支付完成时使用）
	 */
	@Override
	public MarketingResponse doTransaction(RequestContext context)
			throws MarketingException {
		// 获取参数
		OrderActivityDTO orderActivityDTO = (OrderActivityDTO) context
				.getRequest().getParam("orderActivityDTO");
		orderActivityManager.updateOrderActivityStatus(orderActivityDTO,
				CouponStatus.USED);
		// 返回response对象
		return ResponseUtil.getResponse(true);
	}

	@Override
	public String getName() {
		return ActionEnum.USE_ORDER_ACTIVITY.getActionName();
	}
}

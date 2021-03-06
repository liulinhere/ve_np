package com.ve.tradecenter.core.manager;

import java.util.Date;
import java.util.List;

import com.ve.tradecenter.core.domain.OrderItemDO;
import com.ve.tradecenter.core.exception.TradeException;

public interface OrderItemManager {
	
	/**
	 * 新增订单明细
	 * @param orderItem
	 * @return
	 * @throws tradeException
	 */
	public long addOrderItem(OrderItemDO orderItem)throws TradeException;
	
	/**
	 * 订单发货,更新订单发货状态
	 * @param orderItem
	 * @return
	 * @throws TradeException
	 */
	public int deliveryOrderItem(long orderItemId,long userId)throws TradeException;
	
	/**
	 * 根据userId和userId获取订单明细
	 * @param orderId
	 * @param userId
	 * @return
	 * @throws TradeException
	 */
	public List<OrderItemDO> getOrderItems(long orderId,long userId)throws TradeException;
	
	/**
	 * 更新订单退货状态
	 * @param orderId
	 * @param orderItemId
	 * @param userId
	 * @return
	 * @throws TradeException
	 */
	public int updateItemReturnStatus(int returnStatus,long orderId,long orderItemId,long userId,Date now)throws TradeException;

}

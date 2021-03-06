package com.ve.tradecenter.core.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ve.tradecenter.core.dao.OrderItemDao;
import com.ve.tradecenter.core.domain.OrderItemDO;
/**
 * 订单明细表Dao实现类
 * @author cwr
 */
public class OrderItemDaoImpl extends SqlMapClientDaoSupport implements OrderItemDao {

	@Override
	public long addOrderItem(OrderItemDO orderItem) {
		return (Long)this.getSqlMapClientTemplate().insert("order_item.addOrderItem",orderItem);
	}

	@Override
	public int deliveryOrderItem(OrderItemDO orderItem) {
		return this.getSqlMapClientTemplate().update("order_item.deliveryOrderItem",orderItem); 
	}
	
	@Override
	public List<OrderItemDO> getOrderItems(OrderItemDO orderItem){
		return (List<OrderItemDO>)this.getSqlMapClientTemplate().queryForList("order_item.getOrderItems", orderItem);
	}

	@Override
	public int updateItemReturnStatus(OrderItemDO orderItem) {
		return this.getSqlMapClientTemplate().update("order_item.updateItemReturnStatus",orderItem);
	}
	
	/*@Override
	public int updateToSubOrder(OrderItemDO orderItem){
		return this.getSqlMapClientTemplate().update("order_item.updateToSubOrder",orderItem);
	}*/

}

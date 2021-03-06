package com.ve.itemcenter.core.service.action.item;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.ve.itemcenter.common.api.ItemResponse;
import com.ve.itemcenter.common.constant.ActionEnum;
import com.ve.itemcenter.common.constant.ResCodeNum;
import com.ve.itemcenter.common.domain.dto.ItemSkuDTO;
import com.ve.itemcenter.common.domain.qto.ItemSkuQTO;
import com.ve.itemcenter.core.exception.ItemException;
import com.ve.itemcenter.core.manager.ItemImageManager;
import com.ve.itemcenter.core.manager.ItemManager;
import com.ve.itemcenter.core.manager.ItemSkuManager;
import com.ve.itemcenter.core.service.ItemRequest;
import com.ve.itemcenter.core.service.RequestContext;
import com.ve.itemcenter.core.service.action.Action;
import com.ve.itemcenter.core.util.ResponseUtil;

/**
 * 删除商品Action
 * 
 * @author chen.huang
 *
 */
@Service
public class DeleteItemAction implements Action {
	private static final Logger log = LoggerFactory.getLogger(DeleteItemAction.class);
	@Resource
	private ItemManager itemManager;

	@Resource
	TransactionTemplate transactionTemplate;

	@Resource
	private ItemSkuManager itemSkuManager;
	
	@Resource
	private ItemImageManager itemImageManager;

	@SuppressWarnings("unchecked")
	@Override
	public ItemResponse execute(final RequestContext context) throws ItemException {
		@SuppressWarnings("rawtypes")
		ItemResponse response = transactionTemplate.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				try {
					ItemResponse response = null;
					ItemRequest request = context.getRequest();
					// 验证ID
					if (request.getLong("ID") == null) {
						return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "ItemID is missing");
					}
					if (request.getLong("sellerId") == null) {
						return ResponseUtil.getErrorResponse(ResCodeNum.PARAM_E_MISSING, "sellerId is missing");
					}
					Long itemId = request.getLong("ID");// 商品品牌ID
					Long sellerId = request.getLong("sellerId");// 供应商ID
					// 获取ItemSku列表
					ItemSkuQTO itemSkuQTO = new ItemSkuQTO();
					itemSkuQTO.setItemId(itemId);
					itemSkuQTO.setSellerId(sellerId);
					List<ItemSkuDTO> itemSkuDTOList = itemSkuManager.queryItemSku(itemSkuQTO);
					for (ItemSkuDTO itemSkuDTO : itemSkuDTOList) {
						Long skuId = itemSkuDTO.getId();
						// 删除itemSku ,在manager层中先删除skuproperty列表
						itemSkuManager.deleteItemSku(skuId, sellerId);
					}
					// 删除副图列表
					itemImageManager.deleteItemImageListByItemId(itemId, sellerId);
					itemManager.deleteItem(itemId, sellerId);
					response = ResponseUtil.getSuccessResponse(true);
					return response;
				} catch (ItemException e) {
					status.setRollbackOnly();
					log.error(e.toString());
					return ResponseUtil.getErrorResponse(e.getResCodeNum(), e.getMessage());
				}
			}
		});
		return response;

	}

	@Override
	public String getName() {
		return ActionEnum.DELETE_ITEM.getActionName();
	}
}

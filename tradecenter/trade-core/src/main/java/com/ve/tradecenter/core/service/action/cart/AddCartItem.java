package com.ve.tradecenter.core.service.action.cart;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ve.tradecenter.common.api.TradeResponse;
import com.ve.tradecenter.common.constant.ResponseCode;
import com.ve.tradecenter.common.domain.CartDTO;
import com.ve.tradecenter.common.domain.CartItemDTO;
import com.ve.tradecenter.common.domain.FavorableInfoDTO;
import com.ve.tradecenter.common.domain.ItemSkuDTO;
import com.ve.tradecenter.common.domain.ItemSkuQTO;
import com.ve.tradecenter.core.constants.TradeConstants;
import com.ve.tradecenter.core.domain.CartItemDO;
import com.ve.tradecenter.core.exception.TradeException;
import com.ve.tradecenter.core.manager.CartItemManager;
import com.ve.tradecenter.core.manager.DeliveryManager;
import com.ve.tradecenter.core.manager.ItemManager;
import com.ve.tradecenter.core.manager.PromotionManager;
import com.ve.tradecenter.core.service.RequestContext;
import com.ve.tradecenter.core.service.ResponseUtils;
import com.ve.tradecenter.core.service.TradeRequest;
import com.ve.tradecenter.core.service.action.Action;
import com.ve.tradecenter.core.service.action.ActionEnum;
/**
 * 未登入用户添加购物车商品（数据库中只保存销售的商品 ，赠品和优惠都是临时计算）， 由于每次新加入商品时候优惠、赠品可能有变化
 * 1 根据新加入商品id和供应商id查询商品平台获取最新的价格、名称等信息
 * 2 新加入购物车的商品需要判断是否已经存在，如果已经存则需要更新（数量累加）
 * 3 根据所有的商品列表查询促销平台获取优惠信息（是否包邮、优惠金额、赠品）
 * 4 返回运费、优惠金额、赠品等总的购物车信息给调用方
 * 
 * @author cwr
 */
public class AddCartItem implements Action {
	private static final Logger log = LoggerFactory.getLogger(AddCartItem.class);
	
	/*
	 * 未登入用户购物车最大数目限制
	 */
	private static final int CART_MAX_NUMER = 20;

	@Resource
	private CartItemManager cartItemManager;
	
	@Resource
	private ItemManager itemManager;
	
	@Resource
	private PromotionManager promotionManager;
	
	@Resource
	private DeliveryManager deliveryManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public TradeResponse<Boolean> execute(RequestContext context){
		TradeRequest request = context.getRequest();
		if(request.getParam("cartItemDTO") == null){
			log.error("cartItemDTO is null");
			return ResponseUtils.getFailResponse(ResponseCode.PARAM_E_PARAM_MISSING,"cartItemDTO is null");
		}
		CartItemDTO cartItemDTO = (CartItemDTO)request.getParam("cartItemDTO");
		
		// 字段验证
		String errorMsg = this.cartItemManager.validateCartItemFields4Add(cartItemDTO);
		if(!StringUtils.isEmpty(errorMsg)){
			log.error(errorMsg);
			return ResponseUtils.getFailResponse(ResponseCode.PARAM_E_PARAM_MISSING,errorMsg);
		}
		
		// 必要的字段
		long itemSkuId = cartItemDTO.getItemSkuId();
		long supplierId = cartItemDTO.getSupplierId();
		int number  = cartItemDTO.getNumber();
		String sessionId = cartItemDTO.getSessionId();
		int source = cartItemDTO.getSource()==null? TradeConstants.OrderSource.PC : cartItemDTO.getSource(); // 默认来自于pc端 	
		boolean isWireless = source!=TradeConstants.OrderSource.PC ? true : false; // 判断是不是无线来源， 需要享受专享价
		// 商品平台查询商品详细信息
		ItemSkuDTO itemSku = null;
		try{
			itemSku = getItemSku(itemSkuId, supplierId);
		}catch(TradeException e){
			log.error("getItemSku error",e);
			return ResponseUtils.getFailResponse(ResponseCode.SYS_E_REMOTE_CALL_ERROR);
		}
		if(itemSku == null){
			log.error("itemSku doesn't exist : itemSkuId:  "+ itemSkuId + ", supplierId: " + supplierId);
			return ResponseUtils.getFailResponse(ResponseCode.BIZ_E_RECORD_NOT_EXIST,"itemSku doesn't exist ");
		}
		
		long result = 0;
		List<CartItemDO> cartItems = null; // 数据库购物车的商品的列表
		CartDTO cartDTO = null;
		
		try{
			// 获取已有的购物车列表
			cartItems = this.cartItemManager.queryCartItems(sessionId);
			// 判断购物车是否已经有该商品
			int index = -1;
			CartItemDO existingCartItem = null ;
			int cartSize = cartItems==null ? 0:cartItems.size(); 
			for(int i =0,len=cartItems.size() ;i<len;i++){
				CartItemDO item = cartItems.get(i);
				if(!item.getIsGift() && item.getItemSkuId().equals(itemSkuId)  && item.getSupplierId().equals(supplierId) ){
					existingCartItem = item;
					index = i;
				}
			}
			if(index > -1){ // 如果是新加入需要判断是不是超过了购物车的最大限制
				if(cartSize == CART_MAX_NUMER){
					log.error("exceed the cart max limitation: " + cartSize);
					return ResponseUtils.getFailResponse(ResponseCode.BIZ_E_CART_MAX_LIMITATION);
				}
			}
			
			Date now = new Date();
			// 拼接购物车商品参数
			CartItemDO newCartItem = new CartItemDO();
			//newCartItem.setName(itemSku.getName()); // 商品名称
			newCartItem.setItemSkuId(itemSkuId); // 
			newCartItem.setSupplierId(supplierId); // 供应商id
			newCartItem.setUserId(null); // 未登入时候无用户id
			newCartItem.setItemId(itemSku.getItemId());
			newCartItem.setSubName(itemSku.getItemName()); //
			newCartItem.setSessionId(sessionId); 
			newCartItem.setOriginPrice(itemSku.getOriginPrice()); // 市场价
			if(isWireless){ //如果是无线端  取无线价格 
				if(itemSku.getWirelessPrice() != null){
					newCartItem.setUnitPrice(itemSku.getWirelessPrice());
				}else{
					newCartItem.setUnitPrice(itemSku.getCurrentPrice()); 
				}
			}else{
				newCartItem.setUnitPrice(itemSku.getCurrentPrice());
			}
			newCartItem.setNumber(number);
			newCartItem.setTotalPrice(newCartItem.getUnitPrice() * number);
			long savePrice = newCartItem.getUnitPrice()-newCartItem.getOriginPrice();
			if(savePrice < 0){
				savePrice =0; //防止小于0
			}
			newCartItem.setSavePrice(savePrice);
			newCartItem.setTotalSave(savePrice * number);
			newCartItem.setIsGift(false); // 都不是礼品
			newCartItem.setSource(source);   
			
			newCartItem.setGmtModified(now);
			newCartItem.setGmtCreated(now);
			
			if(index > -1){  // 如果新添加的商品已经在购物车存在    则更新购物车该商品数量
				int totalNumber = existingCartItem.getNumber() + number;
				newCartItem.setId(existingCartItem.getId()); // id赋值用于更新条件
				newCartItem.setNumber(totalNumber);
				newCartItem.setTotalPrice(totalNumber * newCartItem.getUnitPrice());
				newCartItem.setTotalSave(savePrice * totalNumber);
				result = this.cartItemManager.updateCartItem(newCartItem); // 如果是购物车已经存在则更新数量和总价
				//cartItems.set(index, newCartItem);  //
			}else{
				result = cartItemManager.addCartItem(newCartItem); //如果数据库不存在 则新写入
				//cartItems.add(newCartItem);
			}
			
			/*List<ItemSkuQTO> promotionQueryList = new ArrayList<ItemSkuQTO>();
			for(CartItemDO item : cartItems){
				if(!item.getIsGift()){// 将已有的是礼品的排除在外
					ItemSkuQTO itemSkuQTO  = new ItemSkuQTO(); 
					itemSkuQTO.setNumber(item.getNumber()); //
					itemSkuQTO.setSupplierId(item.getSupplierId()); //
					itemSkuQTO.setId(item.getItemSkuId()); //
					itemSkuQTO.setPrice(item.getUnitPrice()); //单价
					promotionQueryList.add(itemSkuQTO);
				}
			}*/
			
			// 构造去促销平台查询的条件
			/*
			List<ItemSkuQTO> promotionQueryList = this.promotionManager.getPromotionQueryCondition(cartItems);
			
			// 根据商品列表、平台   去促销系统查询赠品  优惠金额  和是否包邮
			List<FavorableInfoDTO> activityList= null; // 促销活动列表
			activityList = getPromotionItems(promotionQueryList,source);
			
			cartDTO = this.cartItemManager.handlePromotionInfo(activityList, cartItems);
			*/
		}catch(TradeException e){
			log.error("db error: ",e);
			return ResponseUtils.getFailResponse(e.getResponseCode());
		}
		
		if(result >0){
			return ResponseUtils.getSuccessResponse(true);
		}else{
			return ResponseUtils.getSuccessResponse(false);
		}
	}

	@Override
	public String getName() {
		return ActionEnum.ADD_CART_ITEM.getActionName();
	}

	/**
	 * 查询商品平台获取商品的价格等信息
	 * @param itemSkuId
	 * @param supplierId
	 * @return
	 */
	private ItemSkuDTO getItemSku(Long itemSkuId,Long supplierId)throws TradeException{
		
		return this.itemManager.getItemSku(itemSkuId, supplierId);
		
	}
	
	/**
	 * 查询促销平台获取促销商品 (赠送商品)
	 * @param itemSkuId
	 * @param supplierId
	 * @return
	 */
	private List<FavorableInfoDTO> getPromotionItems(List<ItemSkuQTO> cartItems,int platform)throws TradeException{
		try{
			return this.promotionManager.getPromotionItems(cartItems);
		}catch(Exception e){
			throw new TradeException("getPromotionItems error",e);
		}
	}
	
	/**
	 * 调用运费平台计算运费
	 * @param regionId
	 * @param weight
	 * @return
	 */
	private long getDeliveryFee(Long regionId,Long weight)throws TradeException{
		long deliveryFee = 0L;
		try{
			deliveryFee = this.deliveryManager.getDeliveryFee(regionId, weight);
		}catch(Exception e){
			throw new TradeException(ResponseCode.SYS_E_REMOTE_CALL_ERROR,e);
		}
		return deliveryFee;
	}
	
}	

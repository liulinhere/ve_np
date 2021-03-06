package com.ve.usercenter.core.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ve.usercenter.core.domain.ConsigneeDO;

@Service
public interface ConsigneeDao {

	/**
	 * 获取指定用户的收货地址总数
	 * */
	int getConsigneeCountByUserId(Long userId);

	Long addConsigee(ConsigneeDO consigneeDo);

	/** 根据收货地址id，获取指定的收货地址 */
	ConsigneeDO getConsigneeById(Long userId, Long id);

	/**
	 * 删除指定的收货地址
	 * */
	int deleteConsignee(Long userId, Long consigneeId);

	/**
	 * 修改指定的收货地址
	 * */
	int updateConsigee(ConsigneeDO consigneeDo);

	/**
	 * 将指定用户的所有收货地址修改为非默认
	 * */
	int updateUserDefaultConsignee(Long userId);

	int setDefConsignee(Long userId, Long consigneeId);

	List<ConsigneeDO> queryConsignee(Long userId);

	/**
	 * 删除指定用户的所有收货地址
	 * */
	int deleteUserConsignee(Long userId);

	/**
	 * 将用户所有的收货地址还原
	 * 
	 * @param userId
	 * @return
	 */
	int restoreUserConsignee(Long userId);

}

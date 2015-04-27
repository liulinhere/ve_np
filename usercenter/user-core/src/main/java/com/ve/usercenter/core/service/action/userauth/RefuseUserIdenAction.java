package com.ve.usercenter.core.service.action.userauth;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ve.usercenter.common.action.ActionEnum;
import com.ve.usercenter.common.api.UserResponse;
import com.ve.usercenter.core.exception.UserException;
import com.ve.usercenter.core.manager.UserAuthInfoManager;
import com.ve.usercenter.core.service.RequestContext;
import com.ve.usercenter.core.service.UserRequest;
import com.ve.usercenter.core.service.action.Action;

/**
 * 买家的实名认证失败
 * */
@Service
public class RefuseUserIdenAction implements Action {

	@Resource
	private UserAuthInfoManager userAuthInfoManager;

	@Override
	public UserResponse execute(RequestContext context) throws UserException {

		UserRequest userRequest = context.getRequest();
		Long userId = (Long) userRequest.getParam("userId");
		String remark = (String) userRequest.getParam("remark");// 备注，认证实名认证不通过的原因

		userAuthInfoManager.refuseUserIden(userId, remark);

		return new UserResponse(true);
	}

	@Override
	public String getName() {
		return ActionEnum.FILEIDENTIFIED_USER_AUTH_INFO.getActionName();
	}

}
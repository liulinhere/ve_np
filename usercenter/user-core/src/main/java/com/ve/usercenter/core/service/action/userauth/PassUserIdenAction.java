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
 * 买家的实名认证通过
 * */
@Service
public class PassUserIdenAction implements Action {

	@Resource
	private UserAuthInfoManager userAuthInfoManager;

	@Override
	public UserResponse execute(RequestContext context) throws UserException {

		UserRequest userRequest = context.getRequest();
		Long userId = (Long) userRequest.getParam("userId");
		String remark = (String) userRequest.getParam("remark");// 备注
		userAuthInfoManager.passUserIden(userId, remark);
		return new UserResponse(true);
	}

	@Override
	public String getName() {
		return ActionEnum.IDENTIFIED_USER_AUTH_INFO.getActionName();
	}

}

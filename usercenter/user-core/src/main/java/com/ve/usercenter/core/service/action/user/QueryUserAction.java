package com.ve.usercenter.core.service.action.user;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ve.usercenter.common.action.ActionEnum;
import com.ve.usercenter.common.api.UserResponse;
import com.ve.usercenter.common.dto.UserDTO;
import com.ve.usercenter.common.qto.UserQTO;
import com.ve.usercenter.core.exception.UserException;
import com.ve.usercenter.core.manager.UserManager;
import com.ve.usercenter.core.service.RequestContext;
import com.ve.usercenter.core.service.UserRequest;
import com.ve.usercenter.core.service.action.Action;

@Service
public class QueryUserAction implements Action {

	@Resource
	private UserManager userManager;

	@Override
	public UserResponse execute(RequestContext context) throws UserException {

		UserRequest request = context.getRequest();
		UserQTO userQto = (UserQTO) request.getParam("userQTO");
		List<UserDTO> users = userManager.queryUser(userQto);
		Long total = userManager.getTotalCount(userQto);

		return new UserResponse(users, total);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return ActionEnum.QUERY_USER.getActionName();
	}

}

package com.ve.marketingcenter.core.dao.tool.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ve.marketingcenter.common.domain.qto.MarketingToolQTO;
import com.ve.marketingcenter.core.dao.tool.ToolDODAO;
import com.ve.marketingcenter.core.domain.ToolDO;


public class ToolDODAOImpl extends SqlMapClientDaoSupport implements ToolDODAO {

	@Override
	public Long addTool(ToolDO record) {
		return (Long)getSqlMapClientTemplate().insert("market_tool.addTool", record);
	}

	@Override
	public int updateTool(ToolDO record) {
		return getSqlMapClientTemplate().update("market_tool.updateTool", record);
	}

	@Override
	public ToolDO getTool(Long id) {
		ToolDO tdo = new ToolDO();
		tdo.setId(id);
		return (ToolDO) getSqlMapClientTemplate().queryForObject("market_tool.getTool", tdo);
	}

	@Override
	public List<ToolDO> queryTool(MarketingToolQTO baseQto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int queryToolCount(MarketingToolQTO baseQto) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
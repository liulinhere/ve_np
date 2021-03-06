package com.ve.itemcenter.core.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ve.itemcenter.common.constant.DBConst;
import com.ve.itemcenter.common.constant.ResCodeNum;
import com.ve.itemcenter.common.domain.dto.SpuPropertyDTO;
import com.ve.itemcenter.common.domain.qto.SpuPropertyQTO;
import com.ve.itemcenter.core.dao.SpuPropertyDAO;
import com.ve.itemcenter.core.domain.SpuPropertyDO;
import com.ve.itemcenter.core.exception.ItemException;
import com.ve.itemcenter.core.manager.SpuPropertyManager;
import com.ve.itemcenter.core.util.ExceptionUtil;
import com.ve.itemcenter.core.util.ItemUtil;

@Service
public class SpuPropertyManagerImpl implements SpuPropertyManager {
	@Resource
	private SpuPropertyDAO spuPropertyDAO;

	@Override
	public SpuPropertyDTO addSpuProperty(SpuPropertyDTO spuPropertyDTO) throws ItemException {
		try {
			// 验证spuPropertyDTO内的属性
			if (!verifySpuPropertyDTOProperty(spuPropertyDTO)) {
				throw ExceptionUtil.getException(ResCodeNum.PARAM_E_INVALID, "spuPropertyDTO property incorrect");
			}
			SpuPropertyDO spuPropertyDO = new SpuPropertyDO();
			ItemUtil.copyProperties(spuPropertyDTO, spuPropertyDO);// DTO转DO
			long newInsertedId = spuPropertyDAO.addSpuProperty(spuPropertyDO);// 新增的记录返回的ID
			spuPropertyDTO = getSpuProperty(newInsertedId);// 新增加的记录对应的spuPropertyDO
			return spuPropertyDTO;
		} catch (Exception e) {
			throw new ItemException(ResCodeNum.SYS_E_DEFAULT_ERROR, e);
		}
	}

	@Override
	public boolean updateSpuProperty(SpuPropertyDTO spuPropertyDTO) throws ItemException {
		// 验证参数
		if (spuPropertyDTO.getId() == null) {
			throw ExceptionUtil.getException(ResCodeNum.PARAM_E_MISSING, "spuPropertyDTO is null");
		}
		if (!verifySpuPropertyDTOProperty(spuPropertyDTO)) {
			throw ExceptionUtil.getException(ResCodeNum.PARAM_E_INVALID, "spuPropertyDTO property incorrect");
		}
		SpuPropertyDO spuPropertyDO = new SpuPropertyDO();
		ItemUtil.copyProperties(spuPropertyDTO, spuPropertyDO);
		int num = spuPropertyDAO.updateSpuProperty(spuPropertyDO);
		return num > 0 ? true : false;
	}

	@Override
	public SpuPropertyDTO getSpuProperty(Long id) throws ItemException {
		SpuPropertyDO spuPropertyDO = spuPropertyDAO.getSpuProperty(id);
		if (spuPropertyDO == null) {
			throw ExceptionUtil.getException(ResCodeNum.BASE_PARAM_E_RECORD_NOT_EXIST, "Cannot find requested record");
		}
		SpuPropertyDTO spuPropertyDTO = new SpuPropertyDTO();
		ItemUtil.copyProperties(spuPropertyDO, spuPropertyDTO);
		return spuPropertyDTO;
	}

	@Override
	public boolean deleteSpuProperty(Long id) throws ItemException {
		// TODO 验证id

		SpuPropertyDO spuPropertyDO = new SpuPropertyDO();
		spuPropertyDO.setId(id);
		spuPropertyDO.setIsDeleted(DBConst.DELETED.getCode());
		int num = spuPropertyDAO.updateSpuProperty(spuPropertyDO);
		return num > 0 ? true : false;
	}

	@Override
	public boolean verifySpuPropertyDTOProperty(SpuPropertyDTO spuPropertyDTO) throws ItemException {
		// TODO 验证SpuPropertyDTO字段属性
		return true;
	}

	public List<SpuPropertyDTO> querySpuProperty(SpuPropertyQTO spuPropertyQTO) throws ItemException {
		List<SpuPropertyDO> list = spuPropertyDAO.querySpuProperty(spuPropertyQTO);
		List<SpuPropertyDTO> spuPropertyDTOList = new ArrayList<SpuPropertyDTO>();// 需要返回的DTO列表
		for (SpuPropertyDO spuPropertyDO : list) {
			SpuPropertyDTO dto = new SpuPropertyDTO();
			ItemUtil.copyProperties(spuPropertyDO, dto);
			spuPropertyDTOList.add(dto);
		}
		return spuPropertyDTOList;
	}
}

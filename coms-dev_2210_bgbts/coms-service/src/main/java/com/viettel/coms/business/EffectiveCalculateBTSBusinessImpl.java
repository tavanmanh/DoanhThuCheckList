package com.viettel.coms.business;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.bo.EffectiveCalculateBTSBO;
import com.viettel.coms.bo.EffectiveCalculateDASCDBRBO;
import com.viettel.coms.bo.UserHolidayBO;
import com.viettel.coms.dao.AssignHandoverDAO;
import com.viettel.coms.dao.EffectiveCalculateBTSDAO;
import com.viettel.coms.dao.EffectiveCalculateDASCDBRDAO;
import com.viettel.coms.dto.*;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * @author Hoanm1
 * @version 1.0
 * @since 2020-07-16
 */
@Service("effectiveCalculateBTSBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EffectiveCalculateBTSBusinessImpl extends
		BaseFWBusinessImpl<EffectiveCalculateBTSDAO, EffectiveCalculateBTSDTO, EffectiveCalculateBTSBO> {

	@Autowired
	private EffectiveCalculateBTSDAO effectiveCalculateBTSDAO;

	public EffectiveCalculateBTSBusinessImpl() {
		tModel = new EffectiveCalculateBTSBO();
		tDAO = effectiveCalculateBTSDAO;
	}

	@Override
	public EffectiveCalculateBTSDAO gettDAO() {
		return effectiveCalculateBTSDAO;
	}

	public List<EffectiveCalculateBTSDTO> getListEffectiveCalculateBTS(SysUserRequest request) {
		return effectiveCalculateBTSDAO.getListEffectiveCalculateBTS(request);
	}
	public List<EffectiveCalculateBTSDTO> getProvince() {
		return effectiveCalculateBTSDAO.getProvince();
	}
	public List<EffectiveCalculateBTSDTO> getNameParam(EffectiveCalculateDASCDBRBTSRequest dto) {
		return effectiveCalculateBTSDAO.getNameParam(dto);
	}
	public List<EffectiveCalculateBTSDTO> getNameParamStation(EffectiveCalculateDASCDBRBTSRequest dto) {
		return effectiveCalculateBTSDAO.getNameParamStation(dto);
	}
	public Long insertEffectiveCalculateBTS(EffectiveCalculateDASCDBRBTSRequest dto) {
		return effectiveCalculateBTSDAO.insertEffectiveCalculateBTS(dto);
	}
}

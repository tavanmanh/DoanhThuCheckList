package com.viettel.coms.business;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.bo.EffectiveCalculateDASCDBRBO;
import com.viettel.coms.bo.UserHolidayBO;
import com.viettel.coms.dao.AssignHandoverDAO;
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


@Service("effectiveCalculateDASCDBRBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EffectiveCalculateDASCDBRBusinessImpl extends
		BaseFWBusinessImpl<EffectiveCalculateDASCDBRDAO, EffectiveCalculateDASCDBRDTO, EffectiveCalculateDASCDBRBO> {

	@Autowired
	private EffectiveCalculateDASCDBRDAO effectiveCalculateDASCDBRDAO;

	public EffectiveCalculateDASCDBRBusinessImpl() {
		tModel = new EffectiveCalculateDASCDBRBO();
		tDAO = effectiveCalculateDASCDBRDAO;
	}

	@Override
	public EffectiveCalculateDASCDBRDAO gettDAO() {
		return effectiveCalculateDASCDBRDAO;
	}

	public List<EffectiveCalculateDASCDBRDTO> getListEffectiveCalculateDASCDBR(SysUserRequest request) {
		return effectiveCalculateDASCDBRDAO.getListEffectiveCalculateDASCDBR(request);
	}
	public Long insertEffectiveCalculateDASCDBR(EffectiveCalculateDASCDBRBTSRequest dto) {
		return effectiveCalculateDASCDBRDAO.insertEffectiveCalculateDASCDBR(dto);
	}
}

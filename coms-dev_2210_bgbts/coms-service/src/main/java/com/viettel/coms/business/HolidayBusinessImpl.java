package com.viettel.coms.business;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.bo.UserHolidayBO;
import com.viettel.coms.dao.AssignHandoverDAO;
import com.viettel.coms.dao.HolidayDAO;
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
 * @since 2019-07-29
 */
@Service("holidayBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HolidayBusinessImpl extends
		BaseFWBusinessImpl<HolidayDAO, UserHolidayDTO, UserHolidayBO> implements
		HolidayBusiness {

	@Autowired
	private HolidayDAO holidayDAO;

	public HolidayBusinessImpl() {
		tModel = new UserHolidayBO();
		tDAO = holidayDAO;
	}

	@Override
	public HolidayDAO gettDAO() {
		return holidayDAO;
	}

	@Override
	public long count() {
		return holidayDAO.count("UserHolidayBO", null);
	}

	public List<UserHolidayDTO> getListUserHoliday(SysUserRequest request) {
		return holidayDAO.getListUserHoliday(request);
	}

	public Long insertHoliday(HolidayRequest dto) {
		return holidayDAO.insertHoliday(dto);
	}

	 public List<UserHolidayDTO> getListManagerHoliday(SysUserRequest request)
	 {
	 return holidayDAO.getListManagerHoliday(request);
	 }

	public int approveRejectHoliday(HolidayRequest request,
			ResultInfo resultInfo) throws ParseException {
		try {
			int flag = request.getSysUserRequest().getFlag();
			holidayDAO.approveRejectHoliday(request, flag);
			if (flag == 1) {
				resultInfo.setMessage("Phê duyệt thành công");
			} else {
				resultInfo.setMessage("Từ chối thành công");
			}
		} catch (Exception ex) {
			return 0;
		}
		return 1;
	}
	 public List<UserHolidayDTO> getAppVersion() {
	        List<UserHolidayDTO> appVersionItem = holidayDAO.getVersionItem();
	        return appVersionItem;
	    }
}

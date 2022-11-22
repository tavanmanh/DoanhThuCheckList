package com.viettel.coms.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.cat.business.CatProvinceBusinessImpl;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.bo.ComplainOrderRequestBO;
import com.viettel.coms.business.SysUserCOMSBusinessImpl;
import com.viettel.coms.dao.CertificateExtendDAO;
import com.viettel.coms.dao.ComplainOrderRequestDAO;
import com.viettel.coms.dao.ManageCertificateDAO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.CertificateExtendDTO;
import com.viettel.coms.dto.ComplainOrderRequestDTO;
import com.viettel.coms.dto.ComplainOrderRequestDetailLogHistoryDTO;
import com.viettel.coms.dto.ComplainOrderRequestResponse;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.TangentCustomerDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.stax2.DTDInfo;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;

@Service("complainOrdersRequestBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ComplainOrdersRequestBusinessImpl
		extends BaseFWBusinessImpl<ComplainOrderRequestDAO, ComplainOrderRequestDTO, ComplainOrderRequestBO>
		implements ComplainOrdersRequestBusiness {

	static Logger LOGGER = LoggerFactory.getLogger(ComplainOrdersRequestBusinessImpl.class);

	@Context
	HttpServletRequest request;

	@Autowired
	private ComplainOrderRequestDAO complainOrderRequestDAO;

	@Transactional
	@Override
	public List<ComplainOrderRequestDTO> doSearch(ComplainOrderRequestDTO obj) throws ParseException {
		// TODO Auto-generated method stub
		return complainOrderRequestDAO.doSearch(obj);
	}

	@SuppressWarnings("unused")
	@Transactional
	@Override
	public Long add(ComplainOrderRequestDTO obj, HttpServletRequest request) throws Exception {

		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		Date receiveAddFromCRM = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (obj.getTicketCode() != null)
			obj.setTicketCode(obj.getTicketCode());
		if (obj.getTicketId() != null)
			obj.setComplainOrderRequestId(obj.getTicketId());

		if (obj.getContent() != null)
			obj.setTitle(obj.getContent());
		if (obj.getProvinceName() == null) {
			throw new BusinessException("Vui lòng điền thông tin tỉnh.");
		} else {
			CatProvinceDTO province = complainOrderRequestDAO.findProvinceByName(obj.getProvinceName());
			if (province == null) {
				throw new BusinessException("Lỗi thông tin tỉnh không hợp lệ.");
			} else {
				obj.setProvinceId(province.getCatProvinceId());
				obj.setProvinceCode(province.getCode());
			}
		}
		if (obj.getPhone() != null)
			obj.setCustomerPhone(obj.getPhone());
		if (obj.getAddress() != null)
			obj.setCustomerAddress(obj.getAddress());
		if (obj.getComplainGroup() != null)
			obj.setComplainGroup(obj.getComplainGroup());
		if (obj.getBusiness() != null)
			obj.setBusiness(obj.getBusiness());
		if (obj.getCreateUser() != null)
			obj.setCreateUser(obj.getCreateUser());
		// get create Date from CRM
		
		if (Strings.isNullOrEmpty(obj.getPerformerName())) {
			SysUserDTO sysUser = complainOrderRequestDAO.getUserPGDHT(obj);
			if (sysUser == null) {
				throw new BusinessException("Không tồn tại PGĐ Hạ tầng CNKT/NV KD XDDD của tỉnh trên.");
			} else {
				obj.setPerformerId(sysUser.getSysUserId());
				obj.setPerformerName(sysUser.getEmployeeCode());
				obj.setPerformerfullName(sysUser.getFullName());
			}
		}
		obj.setCreateDate(new Date());
//		obj.setCreateDateString(simpleDateFormat.format(obj.getCreateDate()));
//		Calendar calGoal = Calendar.getInstance();
//		calGoal.setTime(simpleDateFormat.parse(obj.getCreateDateString()));
//		calGoal.add(Calendar.DAY_OF_MONTH, 1);
//		String date = simpleDateFormat.format(calGoal.getTime());
//		obj.setCompletedTimeExpected(simpleDateFormat.parse(date));
		// end
		obj.setStatus(1l);
		// set again create Date is now
		// obj.setCreateDate(receiveAddFromCRM);
		// obj.setCreateDateString(simpleDateFormat.format(obj.getCreateDate()));
		// end
		// return complainOrderRequestDAO.saveObject(obj.toModel());
		return complainOrderRequestDAO.saveComplainOrderRequest(obj);

	}

	@Override
	public Long updateCRM(ComplainOrderRequestDTO obj, HttpServletRequest request) throws Exception {

		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		if (obj.getTicketCode() != null)
			obj.setTicketCode(obj.getTicketCode());
		if (obj.getTicketId() != null)
			obj.setComplainOrderRequestId(obj.getTicketId());
		if (obj.getContent() != null)
			obj.setTitle(obj.getContent());
		if (obj.getPhone() != null)
			obj.setCustomerPhone(obj.getPhone());

		if (obj.getProvinceName() == null) {
			throw new BusinessException("Vui lòng điền thông tin tỉnh.");
		} else {
			CatProvinceDTO province = complainOrderRequestDAO.findProvinceByName(obj.getProvinceName());
			if (province == null) {
				throw new BusinessException("Lỗi thông tin tỉnh không hợp lệ.");
			} else {
				obj.setProvinceId(province.getCatProvinceId());
				obj.setProvinceCode(province.getCode());
			}
		}
		if (obj.getAddress() != null)
			obj.setCustomerAddress(obj.getAddress());
		if (obj.getComplainGroup() != null)
			obj.setComplainGroup(obj.getComplainGroup());
		if (obj.getBusiness() != null)
			obj.setBusiness(obj.getBusiness());
		obj.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		obj.setUpdateUser(obj.getCreateUser());
		if (obj.getStatus() == 4l)
			obj.setCompletedTimeReal((new Timestamp(System.currentTimeMillis())));
		return complainOrderRequestDAO.updateComplainOrderRequest(obj);

	}

	public List<SysUserCOMSDTO> getListPerformer(SysUserCOMSDTO sysUser) {
		// TODO Auto-generated method stub
		return complainOrderRequestDAO.getListPerformer(sysUser);
	}

	public long choosePerformer(ComplainOrderRequestDTO obj) {
		// TODO Auto-generated method stub
		return complainOrderRequestDAO.choosePerformer(obj);

	}

	public Boolean checkRoleTTHTView(HttpServletRequest request) {
		// TODO Auto-generated method stub
		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.TANGENT_CUSTOMER,
				request)) {
			return false;
		}
		return true;
	}

	public Boolean checkRoleCSKH(HttpServletRequest request) {
		// TODO Auto-generated method stub
		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVE, Constant.AdResourceKey.BUSINESS_TARGET,
				request)) {
			return false;
		}
		return true;
	}

	public Long checkRoleDeployTicket(ComplainOrderRequestDTO obj, HttpServletRequest request) {
		// TODO Auto-generated method stub
		// DUONGHV13-27122021-start
		Long IDRole = null;
		List<SysUserDTO> listUserDeploy = complainOrderRequestDAO.getListUserDeploy(obj);

		if (listUserDeploy.stream().filter(t -> t.getSysUserId().toString().equals(obj.getSysUserId().toString()))
				.findAny().isPresent() == true) {
			IDRole = 1l;
		} else
			IDRole = 0l;

		return IDRole;
	}

	public ComplainOrderRequestDTO getByTicketCode(ComplainOrderRequestDTO obj) throws Exception {
		return complainOrderRequestDAO.getByTicketCode(obj);
	}
}

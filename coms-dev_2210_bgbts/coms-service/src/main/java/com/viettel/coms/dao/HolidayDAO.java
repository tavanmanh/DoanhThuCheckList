/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.dao;

import com.viettel.coms.bo.UserHolidayBO;
import com.viettel.coms.dto.*;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author TruongBX3 Reformist CuongNV2
 * @version 1.0
 * @since 08-May-15 4:07 PM
 */
@Repository("holidayDAO")
public class HolidayDAO extends BaseFWDAOImpl<UserHolidayBO, Long> {

	public HolidayDAO() {
		this.model = new UserHolidayBO();
	}

	public HolidayDAO(Session session) {
		this.session = session;
	}

	// Service Mobile STOCK_TRANS
	// DASHBOARD phieu xuat kho

	/**
	 * GET Current TimeStamp
	 *
	 * @return String CurrentTime
	 */
	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		String res = strDate.substring(0, 4);
		Long year = Long.parseLong(res) - 1;
		StringBuilder str = new StringBuilder("01-01-").append(year.toString());
		return str.toString();
	}

	/**
	 * GET Count
	 *
	 * @param StockTransRequest
	 *            request
	 * @return CountConstructionTaskDTO
	 */
	public CountConstructionTaskDTO getCount(SysUserRequest request) {

		StringBuilder sql1 = new StringBuilder("");
		sql1.append("WITH TBL AS(SELECT syn.ORDER_CODE,syn.CODE,syn.REAL_IE_TRANS_DATE,nvl(syn.CONFIRM,0)CONFIRM  ");
		sql1.append("FROM ");
		sql1.append("SYS_USER a,USER_ROLE b,SYS_ROLE  c,USER_ROLE_DATA d, DOMAIN_DATA  e, DOMAIN_TYPE g,CAT_STATION station, CONSTRUCTION cst, SYN_STOCK_TRANS syn  ");
		sql1.append("WHERE ");
		sql1.append("a.SYS_USER_ID=b.SYS_USER_ID ");
		sql1.append("AND b.SYS_ROLE_ID=c.SYS_ROLE_ID ");
		sql1.append("AND c.CODE='COMS_GOVERNOR' ");
		sql1.append("AND b.USER_ROLE_ID=d.USER_ROLE_ID ");
		sql1.append("AND d.DOMAIN_DATA_ID=e.DOMAIN_DATA_ID ");
		sql1.append("AND e.DOMAIN_TYPE_ID=g.DOMAIN_TYPE_ID ");
		sql1.append("AND g.code='KTTS_LIST_PROVINCE' ");
		sql1.append("AND e.data_id=station.cat_province_id ");
		sql1.append("AND station.cat_station_id=cst.cat_station_id ");
		sql1.append("AND syn.construction_code=cst.code ");
		sql1.append("AND syn.type=2  ");
		sql1.append("AND syn.REAL_IE_TRANS_DATE >= to_date('"
				+ getCurrentTimeStamp() + "','dd/MM/yyyy') ");
		sql1.append("AND a.SYS_USER_ID= '" + request.getSysUserId() + "' ");

		sql1.append("UNION ALL ");

		sql1.append("SELECT ");
		sql1.append("a.ORDER_CODE,a.CODE,a.REAL_IE_TRANS_DATE,nvl(a.CONFIRM,0)CONFIRM ");
		sql1.append("FROM ");
		sql1.append("STOCK_TRANS a ");
		sql1.append("WHERE ");
		sql1.append("type      = 2 ");
		sql1.append("AND status= 2 and a.BUSINESS_TYPE =2 ");
		sql1.append("AND shipper_id= '" + request.getSysUserId() + "' ");
		sql1.append("AND a.REAL_IE_TRANS_DATE >= to_date('"
				+ getCurrentTimeStamp() + "','dd/MM/yyyy')) ");
		sql1.append("SELECT ");
		sql1.append("SUM(CASE WHEN confirm = 0 THEN 1 END) chotiepnhan, ");
		sql1.append("SUM(CASE WHEN confirm = 1 THEN 1 END) datiepnhan, ");
		sql1.append("SUM(CASE WHEN confirm = 2 THEN 1 END) datuchoi ");
		sql1.append("FROM tbl ");

		SQLQuery query1 = getSession().createSQLQuery(sql1.toString());

		query1.addScalar("chotiepnhan", new LongType());
		query1.addScalar("datiepnhan", new LongType());
		query1.addScalar("datuchoi", new LongType());

		query1.setResultTransformer(Transformers
				.aliasToBean(CountConstructionTaskDTO.class));

		return (CountConstructionTaskDTO) query1.list().get(0);
	}

	public List<UserHolidayDTO> getVersionItem() {
		StringBuilder sql = new StringBuilder(
				"SELECT NAME AS version, DESCRIPTION link " + "FROM APP_PARAM "
						+ "WHERE PAR_TYPE='HOLIDAY_VERSION'");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("version", new StringType());
		query.addScalar("link", new StringType());
		query.setResultTransformer(Transformers
				.aliasToBean(UserHolidayDTO.class));
		return query.list();
	}

	/**
	 * GET ListSysStockTrans DTO
	 *
	 * @param StockTransRequest
	 *            request
	 * @return List<SynStockTransDTO>
	 */
	public List<UserHolidayDTO> getListManagerHoliday(SysUserRequest request) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select a.full_name||'-'||a.login_name fullName,sum(case when b.TYPE_REGISTER='2' then ( ");
		sql.append(" case when to_char(b.startDate,'MM')=to_char(b.endDate,'MM') ");
		sql.append(" and to_char(b.startDate,'MM')=to_char(sysdate,'MM') then to_number(numberHoliday) else (  ");
		sql.append(" case when to_char(startDate,'MM')=to_char(sysdate,'MM') then (  ");
		sql.append(" case when startHour= '08:00' then to_number(TRUNC (LAST_DAY (to_date(sysdate)))-trunc(startDate)) +1 ");
		sql.append(" when startHour= '13:30' then to_number(TRUNC (LAST_DAY (to_date(sysdate)))-trunc(startDate)) + 0.5 end ) ");
		sql.append(" when to_char(endDate,'MM')=to_char(sysdate,'MM') then( case when endHour= '12:00' then to_number( trunc(endDate)-TRUNC (SYSDATE, 'MONTH') ) + 0.5  ");
		sql.append(" when endHour= '17:30' then to_number( trunc(endDate)-TRUNC (SYSDATE, 'MONTH')) + 1 end )end ) end) end ) totalHolidayMonth, ");
		sql.append(" sum(case when b.TYPE_REGISTER='2' and to_char(startDate,'yyyy')=to_char(sysdate,'yyyy') and to_char(endDate,'yyyy')=to_char(sysdate,'yyyy') ");
		sql.append(" then numberHoliday end)totalHolidayYear, sum(case when b.TYPE_REGISTER='1' and to_char(startDate,'MM')=to_char(sysdate,'MM')  ");
		sql.append(" then numberHoliday end)totalHolidayRegisterMonth, sum(case when b.TYPE_REGISTER='1' and to_char(startDate,'yyyy')=to_char(sysdate,'yyyy') ");
		sql.append(" then numberHoliday end)totalHolidayRegisterYear from sys_user a,user_holiday b where a.sys_user_id=b.sys_user_id and b.status=1  ");
		sql.append(" and a.PARENT_USER_ID_HOLIDAY = :sysUserId group by a.full_name||'-'||a.login_name ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("fullName", new StringType());
		query.addScalar("totalHolidayMonth", new StringType());
		query.addScalar("totalHolidayYear", new StringType());
		query.addScalar("totalHolidayRegisterMonth", new StringType());
		query.addScalar("totalHolidayRegisterYear", new StringType());
		query.setParameter("sysUserId", request.getSysUserId());
		query.setResultTransformer(Transformers
				.aliasToBean(UserHolidayDTO.class));
		return query.list();
	}

	public List<UserHolidayDTO> getListUserHoliday(SysUserRequest request) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select a.sys_user_id sysUserId,a.FULL_NAME fullName,a.LOGIN_NAME staffCode, a.email,a.PHONE_NUMBER numberPhone,");
		sql.append(" (select name from sys_group sys where sys.sys_group_id=a.sys_group_id) sysGroupName, ");
		sql.append(" b.Reason reason,b.address,b.startHour,b.endHour,b.startDate,b.endDate,b.numberHoliday,b.month,b.status,");
		sql.append(" case when a.PARENT_USER_ID_HOLIDAY is not null then 1 else 2 end type,b.reasonReject,b.user_holiday_id userHolidayId, ");
		sql.append(" b.TYPE_REGISTER typeRegister, b.TYPE_HOUR typeHour ");
		sql.append(" from sys_user a,user_holiday b where a.sys_user_id=b.sys_user_id ");
		if (request.getTypeMenu().equals("1")) {
			sql.append(" and a.sys_user_id= :sysUserId ");
		} else {
			sql.append(" and b.status =0 and a.PARENT_USER_ID_HOLIDAY= :sysUserId ");
		}
		sql.append(" order by a.FULL_NAME ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysUserId", new LongType());
		query.addScalar("fullName", new StringType());
		query.addScalar("staffCode", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("numberPhone", new StringType());
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("reason", new StringType());
		query.addScalar("address", new StringType());
		query.addScalar("startHour", new StringType());
		query.addScalar("endHour", new StringType());
		query.addScalar("startDate", new DateType());
		query.addScalar("endDate", new DateType());
		query.addScalar("numberHoliday", new StringType());
		query.addScalar("month", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("type", new StringType());
		query.addScalar("reasonReject", new StringType());
		query.addScalar("userHolidayId", new LongType());
		query.addScalar("typeRegister", new StringType());
		query.addScalar("typeHour", new StringType());
		query.setParameter("sysUserId", request.getSysUserId());
		query.setResultTransformer(Transformers
				.aliasToBean(UserHolidayDTO.class));
		return query.list();
	}

	public Long insertHoliday(HolidayRequest obj) {
		try {
			UserHolidayDTO holidayDto = new UserHolidayDTO();
			holidayDto.setSysUserId(obj.getSysUserRequest().getSysUserId());
			holidayDto.setStaffCode(obj.getHolidayDto().getStaffCode());
			holidayDto.setReason(obj.getHolidayDto().getReason());
			holidayDto.setAddress(obj.getHolidayDto().getAddress());
			holidayDto.setStartHour(obj.getHolidayDto().getStartHour());
			holidayDto.setEndHour(obj.getHolidayDto().getEndHour());
			holidayDto.setStartDate(obj.getHolidayDto().getStartDate());
			holidayDto.setEndDate(obj.getHolidayDto().getEndDate());
			holidayDto.setNumberHoliday(obj.getHolidayDto().getNumberHoliday());
			holidayDto.setCreatedDate(new Date());
			holidayDto.setTypeRegister(obj.getHolidayDto().getTypeRegister());
			if ("1".equals(obj.getHolidayDto().getTypeRegister())) {
				List <String> lstTypeHour = new ArrayList<String>();
				if("1".equals(obj.getHolidayDto().getTypeHour()) || "2".equals(obj.getHolidayDto().getTypeHour())){
					lstTypeHour.add("3");
					UserHolidayDTO checkRegisterAll=getCheckRegister(obj.getSysUserRequest().getSysUserId(),lstTypeHour,obj.getHolidayDto().getStartDate());
					if(checkRegisterAll != null){
						return -1L;
					}
					lstTypeHour = new ArrayList<String>();
					lstTypeHour.add(obj.getHolidayDto().getTypeHour());
					UserHolidayDTO checkRegister=getCheckRegister(obj.getSysUserRequest().getSysUserId(),lstTypeHour,obj.getHolidayDto().getStartDate());
					if(checkRegister != null){
						return -1L;
					}
				}else{
					lstTypeHour.add(obj.getHolidayDto().getTypeHour());
					UserHolidayDTO checkRegisterAll=getCheckRegister(obj.getSysUserRequest().getSysUserId(),lstTypeHour,obj.getHolidayDto().getStartDate());
					if(checkRegisterAll != null){
						return -1L;
					}
					lstTypeHour = new ArrayList<String>();
					lstTypeHour.add("1");
					lstTypeHour.add("2");
					UserHolidayDTO checkRegister=getCheckRegister(obj.getSysUserRequest().getSysUserId(),lstTypeHour,obj.getHolidayDto().getStartDate());
					if(checkRegister != null){
						return -1L;
					}
				}
				holidayDto.setTypeHour(obj.getHolidayDto().getTypeHour());
			}
			if ("2".equals(obj.getHolidayDto().getTypeRegister())) {
				Double numberDay=getNumberDay(obj.getSysUserRequest().getSysUserId());
				if(numberDay < Double.parseDouble(obj.getHolidayDto().getNumberHoliday())){
					return -2L;
				}
			}
			String strMonth = getCurrentTimeStampMonth();
			String strYear = getCurrentTimeStampYear();
			holidayDto.setMonth(strMonth + "/" + strYear);
			holidayDto.setStatus("0");
			Long id = this.saveObject(holidayDto.toModel());
		} catch (Exception ex) {
			ex.printStackTrace();
			this.getSession().clear();
			return 0L;
		}
		return 1L;
	}
	public UserHolidayDTO getCheckRegister( Long sysUserId,List<String> typeHour,Date startDate) {
		String sql = new String(
				"select user_holiday_id userHolidayId from user_holiday a where TYPE_REGISTER = 1 and a.status in(0,1) "
				+ " and TYPE_HOUR in( :typeHour) and STARTDATE = :startDate and SYS_USER_ID = :sysUserId ");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("sysUserId", sysUserId);
		query.setParameterList("typeHour", typeHour);
		query.setParameter("startDate", startDate);
		query.addScalar("userHolidayId", new LongType());
		query.setResultTransformer(Transformers
				.aliasToBean(UserHolidayDTO.class));
		return (UserHolidayDTO) query.uniqueResult();
	}
	 public double getNumberDay(Long sysUserId) {
		 String sql=" select nvl(sum(case when TYPE_REGISTER = 1 and a.status=1 then to_number(NUMBERHOLIDAY) else 0 end) - "
		 		+ " sum(case when TYPE_REGISTER = 2 then to_number(NUMBERHOLIDAY) else 0 end),0) numberHolidayRest "
		 		+ " from user_holiday a where  a.status in(0,1) and trunc(STARTDATE,'MM')=trunc(sysdate,'MM') and SYS_USER_ID = :sysUserId ";
			SQLQuery query = getSession().createSQLQuery(sql);
			query.setParameter("sysUserId", sysUserId);
			query.addScalar("numberHolidayRest", new DoubleType());
	        List<Double> lstNumber = query.list();
	        if (lstNumber != null && lstNumber.size() > 0) {
	            return lstNumber.get(0);
	        }
	        return -1;
	    }

	public static String getCurrentTimeStampMonth() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);

		String res = strDate.substring(5, 7);
		return res;
	}

	public static String getCurrentTimeStampYear() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		String res = strDate.substring(0, 4);
		return res;
	}

	public int approveRejectHoliday(HolidayRequest request, Integer flag) {
		if (flag == 1) {
			StringBuilder sqlChapNhan = new StringBuilder("");
			sqlChapNhan.append("UPDATE ");
			sqlChapNhan.append("user_holiday ");
			sqlChapNhan.append("SET ");
			sqlChapNhan.append("status = 1, ");
			sqlChapNhan.append("updated_user_id         = :sysUserId, ");
			sqlChapNhan.append("updated_date       = :newDate ");
			sqlChapNhan.append("WHERE ");
			sqlChapNhan.append("user_holiday_id = :userHolidayId ");
			SQLQuery queryChapNhan = getSession().createSQLQuery(
					sqlChapNhan.toString());
			queryChapNhan.setParameter("sysUserId", request.getSysUserRequest()
					.getSysUserId());
			queryChapNhan.setParameter("newDate", new Date());
			queryChapNhan.setParameter("userHolidayId", request.getHolidayDto()
					.getUserHolidayId());
			return queryChapNhan.executeUpdate();
		}
		StringBuilder sqlTuChoi = new StringBuilder("");
		sqlTuChoi.append("UPDATE ");
		sqlTuChoi.append("user_holiday ");
		sqlTuChoi.append("SET ");
		sqlTuChoi.append("status = 2, ");
		sqlTuChoi.append("updated_user_id = :sysUserId, ");
		sqlTuChoi.append("updated_date = :newDate, ");
		sqlTuChoi.append("reasonReject = :reasonReject ");
		sqlTuChoi.append("WHERE ");
		sqlTuChoi.append("user_holiday_id  = :userHolidayId ");
		SQLQuery queryTuChoi = getSession()
				.createSQLQuery(sqlTuChoi.toString());
		queryTuChoi.setParameter("sysUserId", request.getSysUserRequest()
				.getSysUserId());
		queryTuChoi.setParameter("newDate", new Date());
		queryTuChoi.setParameter("reasonReject", request.getHolidayDto()
				.getReasonReject());
		queryTuChoi.setParameter("userHolidayId", request.getHolidayDto()
				.getUserHolidayId());
		return queryTuChoi.executeUpdate();
	}
}

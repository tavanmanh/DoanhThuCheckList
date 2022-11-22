package com.viettel.coms.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.ConstructionBO;
import com.viettel.coms.dto.ApiConstructionRequest;
import com.viettel.coms.dto.ApiConstructionResponse;
import com.viettel.coms.dto.ApiListFileTrVhktDTO;
import com.viettel.coms.dto.ApiListWOInfoVhkt;
import com.viettel.coms.dto.ApiReportPeriodicMaintanceDTO;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@EnableTransactionManagement
@Transactional
@Repository("apiConstructionDAO")
public class ApiConstructionDAO extends BaseFWDAOImpl<BaseFWModelImpl, Long>{

	@Value("${folder_upload2}")
    private String folderUpload2;
	
	public ApiConstructionDAO() {
		this.model = new ConstructionBO();
	}

	public ApiConstructionDAO(Session session) {
		this.session = session;
	}
	
	int year = Calendar.getInstance().get(Calendar.YEAR);
	
	private static byte[] loadFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    long length = file.length();
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }
	    byte[] bytes = new byte[(int)length];

	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }

	    is.close();
	    return bytes;
	}
	
	public List<ApiConstructionResponse> getSolarSystemBussinessInfo(ApiConstructionRequest request) {
		StringBuilder sql = new StringBuilder(" SELECT cons.code systemCode, " + 
				" nvl(cs.LONGITUDE,0) longitude, " + 
				" nvl(cs.LATITUDE,0) latitude, " + 
				" cs.ADDRESS address, " + 
				" cons.SYSTEM_ORIGINAL_CODE systemOriginalCode, " +
				" cs.CAT_PROVINCE_ID provinceId, " +
				" cp.CODE provinceCode " +
				" FROM CONSTRUCTION cons  " + 
				" Left join CAT_STATION cs " + 
				" on cons.CAT_STATION_ID = cs.CAT_STATION_ID " + 
				" LEFT JOIN CAT_PROVINCE cp on cs.CAT_PROVINCE_ID = cp.CAT_PROVINCE_ID " + 
				" where cons.STATUS!=0 and cons.CAT_CONSTRUCTION_TYPE_ID=7 and cons.SYSTEM_ORIGINAL_CODE is not null ");
		if(StringUtils.isNotBlank(request.getSystemCode())) {
			sql.append(" and cons.code=:systemCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("systemCode", new StringType());
		query.addScalar("longitude", new StringType());
		query.addScalar("latitude", new StringType());
		query.addScalar("address", new StringType());
		query.addScalar("systemOriginalCode", new StringType());
		query.addScalar("provinceId", new LongType());
		query.addScalar("provinceCode", new StringType());
		
		if(StringUtils.isNotBlank(request.getSystemCode())) {
			query.setParameter("systemCode", request.getSystemCode());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ApiConstructionResponse.class));
		
		@SuppressWarnings("unchecked")
		List<ApiConstructionResponse> ls = query.list();
		return ls;
	}
	
	public List<ApiListFileTrVhktDTO> getConstructionAttachFile(ApiConstructionRequest request) {
		StringBuilder sql = new StringBuilder(" select wt.CONSTRUCTION_CODE systemCode,  " + 
				" cons.SYSTEM_ORIGINAL_CODE systemOriginalCode, " +
				" wma.FILE_NAME fileName, " + 
				" wma.FILE_PATH fileData " + 
				" from WO_MAPPING_ATTACH wma " + 
				" inner join wo_tr wt on wma.TR_ID = wt.id " +
				" inner join CONSTRUCTION cons on cons.CONSTRUCTION_ID = wt.CONSTRUCTION_ID " + 
				" and cons.CAT_CONSTRUCTION_TYPE_ID=7 and cons.status!=0 " +
				" where wma.STATUS!=0 "
				+ " AND wt.STATUS!=0 "
				+ " AND wt.CD_LEVEL_1 in (SELECT SYS_GROUP_ID FROM SYS_GROUP WHERE CODE='TTVHKT')");
		
		if(StringUtils.isNotBlank(request.getSystemCode())) {
			sql.append(" AND wt.CONSTRUCTION_CODE=:systemCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("systemCode", new StringType());
		query.addScalar("fileName", new StringType());
		query.addScalar("fileData", new StringType());
		query.addScalar("systemOriginalCode", new StringType());
		
		if(StringUtils.isNotBlank(request.getSystemCode())) {
			query.setParameter("systemCode", request.getSystemCode());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ApiListFileTrVhktDTO.class));
		
		@SuppressWarnings("unchecked")
		List<ApiListFileTrVhktDTO> ls = query.list();
		
		for(ApiListFileTrVhktDTO dto : ls) {
			String base64File = "";
			try {
				File file = new File(folderUpload2 + UEncrypt.decryptFileUploadPath(dto.getFileData()));
				try (FileInputStream imageInFile = new FileInputStream(file)) {
					// Reading a file from file system
					byte fileData[] = new byte[(int) file.length()];
					imageInFile.read(fileData);
					base64File = Base64.getEncoder().encodeToString(fileData);
				} catch (FileNotFoundException e) {
					System.out.println("File not found" + e);
				} catch (IOException ioe) {
					System.out.println("Exception while reading the file " + ioe);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			dto.setFileData(base64File);
		}
		
		return ls;
	}
	
	public List<ApiListWOInfoVhkt> getWOInfoByAlert(ApiConstructionRequest request) {
		StringBuilder sql = new StringBuilder(" select wo.CONSTRUCTION_CODE systemCode,   " + 
				"        cons.SYSTEM_ORIGINAL_CODE systemOriginalCode, " + 
				"				null alarmID,   " + 
				"				(select sys_user.EMPLOYEE_CODE from sys_user where sys_user.sys_user_id = wo.FT_ID) ftCode,   " + 
				"				wo.FT_NAME ftName,   " + 
				"				wo.WO_CODE woCode,   " + 
				"				wo.WO_NAME woName,   " + 
				"				wo.STATE state,  " + 
				"				CASE   " + 
				"				WHEN wo.STATE='UNASSIGN' THEN   " + 
				"				  'Mới tạo'   " + 
				"				WHEN wo.STATE='ASSIGN_CD' THEN   " + 
				"				  'Chờ CD tiếp nhận'   " + 
				"				WHEN wo.STATE='ACCEPT_CD' THEN   " + 
				"				  'CD đã tiếp nhận'   " + 
				"				WHEN wo.STATE='REJECT_CD' THEN   " + 
				"				  'CD từ chối'   " + 
				"				WHEN wo.STATE='ASSIGN_FT' THEN   " + 
				"				  'Chờ FT tiếp nhận'   " + 
				"				WHEN wo.STATE='ACCEPT_FT' THEN   " + 
				"				  'FT đã tiếp nhận'   " + 
				"				WHEN wo.STATE='REJECT_FT' THEN   " + 
				"				  'FT từ chối'   " + 
				"				WHEN wo.STATE='PROCESSING' THEN   " + 
				"				  'Đang thực hiện'   " + 
				"				WHEN wo.STATE='DONE' THEN   " + 
				"				  'Đã thực hiện'   " + 
				"				WHEN wo.STATE='OK' THEN   " + 
				"				  'Hoàn thành'   " + 
				"				WHEN wo.STATE='NG' THEN   " + 
				"				  'Chưa hoàn thành'   " + 
				"				WHEN wo.STATE='OPINION_RQ_1' THEN   " + 
				"				  'Xin ý kiến CD Level 1'   " + 
				"				WHEN wo.STATE='OPINION_RQ_2' THEN   " + 
				"				  'Xin ý kiến CD Level 2'   " + 
				"				WHEN wo.STATE='OPINION_RQ_3' THEN   " + 
				"				  'Xin ý kiến CD Level 3'   " + 
				"				WHEN wo.STATE='OPINION_RQ_4' THEN   " + 
				"				  'Xin ý kiến CD Level 4'   " + 
				"				WHEN wo.STATE='CD_OK' THEN   " + 
				"				  'Điều phối duyệt OK'   " + 
				"				WHEN wo.STATE='CD_NG' THEN   " + 
				"				  'Điều phối duyệt chưa OK'   " + 
				"				ELSE ''   " + 
				"				END stateName  " + 
				"				from wo wo  " + 
				"        left join CONSTRUCTION cons on wo.CONSTRUCTION_CODE = cons.CODE and cons.STATUS!=0 and cons.CAT_CONSTRUCTION_TYPE_ID=7 " + 
				"				where wo.STATUS!=0   " + 
				"				AND wo.CD_LEVEL_1 in (SELECT SYS_GROUP_ID FROM SYS_GROUP WHERE CODE='TTVHKT') " + 
				" ");
		
		if(StringUtils.isNotBlank(request.getSystemCode())) {
			sql.append(" AND wo.CONSTRUCTION_CODE=:systemCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("alarmID", new StringType());
		query.addScalar("ftCode", new StringType());
		query.addScalar("ftName", new StringType());
		query.addScalar("woCode", new StringType());
		query.addScalar("woName", new StringType());
		query.addScalar("state", new StringType());
		query.addScalar("stateName", new StringType());
		query.addScalar("systemCode", new StringType());
		query.addScalar("systemOriginalCode", new StringType());
		
		if(StringUtils.isNotBlank(request.getSystemCode())) {
			query.setParameter("systemCode", request.getSystemCode());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ApiListWOInfoVhkt.class));
		
		@SuppressWarnings("unchecked")
		List<ApiListWOInfoVhkt> ls = query.list();
		return ls;
	}
	
	public List<ApiListWOInfoVhkt> getTRWOBySystem(ApiConstructionRequest request) {
		StringBuilder sql = new StringBuilder(" select DISTINCT wo.CONSTRUCTION_CODE systemCode,  " + 
				"         cons.SYSTEM_ORIGINAL_CODE systemOriginalCode, " + 
				"				 tr.TR_CODE trCode,    " + 
				"				 tr.TR_NAME trName,    " + 
				"				 (select sys_user.EMPLOYEE_CODE from sys_user where sys_user.sys_user_id = wo.FT_ID) ftCode,    " + 
				"				 wo.FT_NAME ftName,    " + 
				"				 wo.WO_CODE woCode,    " + 
				"				 wo.WO_NAME woName,    " + 
				"				 wo.STATE state,   " + 
				"				 CASE    " + 
				"				 WHEN wo.STATE='UNASSIGN' THEN    " + 
				"				   'Mới tạo'    " + 
				"				 WHEN wo.STATE='ASSIGN_CD' THEN    " + 
				"				   'Chờ CD tiếp nhận'    " + 
				"				 WHEN wo.STATE='ACCEPT_CD' THEN    " + 
				"				   'CD đã tiếp nhận'    " + 
				"				 WHEN wo.STATE='REJECT_CD' THEN    " + 
				"				   'CD từ chối'    " + 
				"				 WHEN wo.STATE='ASSIGN_FT' THEN    " + 
				"				   'Chờ FT tiếp nhận'    " + 
				"				 WHEN wo.STATE='ACCEPT_FT' THEN    " + 
				"				   'FT đã tiếp nhận'    " + 
				"				 WHEN wo.STATE='REJECT_FT' THEN    " + 
				"				   'FT từ chối'    " + 
				"				 WHEN wo.STATE='PROCESSING' THEN    " + 
				"				   'Đang thực hiện'    " + 
				"				 WHEN wo.STATE='DONE' THEN    " + 
				"				   'Đã thực hiện'    " + 
				"				 WHEN wo.STATE='OK' THEN    " + 
				"				   'Hoàn thành'    " + 
				"				 WHEN wo.STATE='NG' THEN    " + 
				"				   'Chưa hoàn thành'    " + 
				"				 WHEN wo.STATE='OPINION_RQ_1' THEN    " + 
				"				   'Xin ý kiến CD Level 1'    " + 
				"				 WHEN wo.STATE='OPINION_RQ_2' THEN    " + 
				"				   'Xin ý kiến CD Level 2'    " + 
				"				 WHEN wo.STATE='OPINION_RQ_3' THEN    " + 
				"				   'Xin ý kiến CD Level 3'    " + 
				"				 WHEN wo.STATE='OPINION_RQ_4' THEN    " + 
				"				   'Xin ý kiến CD Level 4'    " + 
				"				 WHEN wo.STATE='CD_OK' THEN    " + 
				"				   'Điều phối duyệt OK'    " + 
				"				 WHEN wo.STATE='CD_NG' THEN    " + 
				"				   'Điều phối duyệt chưa OK'    " + 
				"				 ELSE ''    " + 
				"				 END stateName,   " + 
				"				 to_char(tr.START_DATE, 'yyyy-MM-dd HH24:mi:ss') trStart,    " + 
				"				 to_char(tr.FINISH_DATE, 'yyyy-MM-dd HH24:mi:ss') trEnd,    " + 
				"				 to_char(wo.START_TIME, 'yyyy-MM-dd HH24:mi:ss') woStart,    " + 
				"				 to_char(wo.END_TIME, 'yyyy-MM-dd HH24:mi:ss') woEnd,    " + 
				"				 to_char(wo.START_TIME, 'yyyy-MM-dd HH24:mi:ss') woStartStr,    " + 
				"				 to_char(wo.END_TIME, 'yyyy-MM-dd HH24:mi:ss') woEndStr,    " + 
				"				 to_char(wo.CREATED_DATE, 'yyyy-MM-dd HH24:mi:ss') woCreateStr,  " + 
				"				 to_char(wo.FINISH_DATE, 'yyyy-MM-dd HH24:mi:ss') woDeadlineStr,   " + 
				"				 tr.STATE trState,    " + 
				"				 CASE    " + 
				"				 WHEN tr.STATE='UNASSIGN' THEN    " + 
				"				   'Mới tạo'    " + 
				"				 WHEN tr.STATE='ASSIGN_CD' THEN    " + 
				"				   'Chờ CD tiếp nhận'    " + 
				"				 WHEN tr.STATE='ACCEPT_CD' THEN    " + 
				"				   'CD đã tiếp nhận'    " + 
				"				 WHEN tr.STATE='REJECT_CD' THEN    " + 
				"				   'CD đã từ chối'    " + 
				"				 WHEN tr.STATE='PROCESSING' THEN    " + 
				"				   'Đang thực hiện'    " + 
				"				 WHEN tr.STATE='DONE' THEN    " + 
				"				   'Đã thực hiện'    " + 
				"				 WHEN tr.STATE='OK' THEN    " + 
				"				   'Hoàn thành'    " + 
				"				 WHEN tr.STATE='NG' THEN    " + 
				"				   'Chưa hoàn thành'    " + 
				"				 WHEN tr.STATE='OPINION_RQ' THEN    " + 
				"				   'Xin ý kiến'    " + 
				"				 ELSE ''    " + 
				"				 END trStateName    " + 
				"				 from wo wo    " + 
				"				 inner join WO_TR tr on wo.TR_ID = tr.ID    " + 
				"         left join CONSTRUCTION cons on tr.CONSTRUCTION_ID = cons.CONSTRUCTION_ID and cons.STATUS!=0 and cons.CAT_CONSTRUCTION_TYPE_ID=7 " + 
				"				 where wo.STATUS!=0    " + 
				"				 AND tr.STATUS!=0    " + 
				"				 AND wo.CD_LEVEL_1 in (SELECT SYS_GROUP_ID FROM SYS_GROUP WHERE CODE='TTVHKT') " + 
				" ");
		
		if(StringUtils.isNotBlank(request.getSystemCode())) {
			sql.append(" AND wo.CONSTRUCTION_CODE=:systemCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("trCode", new StringType());
		query.addScalar("trName", new StringType());
		query.addScalar("woCode", new StringType());
		query.addScalar("woName", new StringType());
		query.addScalar("trStart", new StringType());
		query.addScalar("trEnd", new StringType());
		query.addScalar("woStart", new StringType());
		query.addScalar("woEnd", new StringType());
		query.addScalar("woStartStr", new StringType());
		query.addScalar("woEndStr", new StringType());
		query.addScalar("trState", new StringType());
		query.addScalar("trStateName", new StringType());
		query.addScalar("systemCode", new StringType());
		query.addScalar("ftCode", new StringType());
		query.addScalar("ftName", new StringType());
		query.addScalar("state", new StringType());
		query.addScalar("stateName", new StringType());
		query.addScalar("woCreateStr", new StringType());
		query.addScalar("woDeadlineStr", new StringType());
		query.addScalar("systemOriginalCode", new StringType());
		
		if(StringUtils.isNotBlank(request.getSystemCode())) {
			query.setParameter("systemCode", request.getSystemCode());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ApiListWOInfoVhkt.class));
		
		@SuppressWarnings("unchecked")
		List<ApiListWOInfoVhkt> ls = query.list();
		return ls;
	}
	
	public List<ApiListWOInfoVhkt> getQuickGISBySystem(ApiConstructionRequest request) {
		StringBuilder sql = new StringBuilder(" SELECT COUNT(*) numUnfinishWO,  " + 
				"wo.CONSTRUCTION_CODE systemCode, " + 
				"cons.SYSTEM_ORIGINAL_CODE systemOriginalCode " + 
				"				FROM wo   " + 
				"        LEFT JOIN construction cons on wo.CONSTRUCTION_CODE = cons.code  " + 
				"        and cons.STATUS!=0  " + 
				"        and cons.CAT_CONSTRUCTION_TYPE_ID=7 " + 
				"				WHERE wo.STATUS!=0   " + 
				"				AND wo.STATE   IN ('UNASSIGN', 'ASSIGN_CD', 'ACCEPT_CD', 'REJECT_CD')   " + 
				"				AND wo.CD_LEVEL_1 in (SELECT SYS_GROUP_ID FROM SYS_GROUP WHERE CODE='TTVHKT') ");
		
		if(StringUtils.isNotBlank(request.getSystemCode())) {
			sql.append(" AND wo.CONSTRUCTION_CODE = :systemCode ");
		}
		
		sql.append(" GROUP BY wo.CONSTRUCTION_CODE, cons.SYSTEM_ORIGINAL_CODE ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("numUnfinishWO", new LongType());
		query.addScalar("systemCode", new StringType());
		query.addScalar("systemOriginalCode", new StringType());
		
		if(StringUtils.isNotBlank(request.getSystemCode())) {
			query.setParameter("systemCode", request.getSystemCode());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ApiListWOInfoVhkt.class));
		
		return query.list();
	}
	
	//Báo cáo BD định kỳ
	@SuppressWarnings("unchecked")
	public List<ApiReportPeriodicMaintanceDTO> getDataReportMaintancePeriodic(ApiConstructionRequest obj){
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(" sg.AREA_CODE areaCode, ");
		sql.append(" sg.NAME cnkt, ");
		sql.append(" wsc.CODE taskCode, ");
		sql.append(" wsc.QUOTA_TIME monthlyNominalCapacity, ");
//		sql.append(" NULL monthlyNominalCapacity, ");
		sql.append(" :reportDate reportDate, ");
		//Nếu END_TIME nhỏ hơn ngày hiện tại thì lấy START_TIME cộng với khoảng thời gian đã chia đều cho chu kỳ (VD: END_TIME - START_TIME = 37 nhưng tính theo chu kỳ chỉ có 36 ngày thì đoạn này chia cho 36)
		sql.append(" (case when wsc.END_TIME<= sysdate then (case when wsc.CYCLE_TYPE = 1 and wsc.CYCLE_LENGTH is not null then (wsc.START_TIME + NUMTODSINTERVAL(to_number(trunc((wsc.END_TIME - wsc.START_TIME)/wsc.CYCLE_LENGTH) * wsc.CYCLE_LENGTH), 'day')) " + 
				"  when wsc.CYCLE_TYPE = 2 and wsc.CYCLE_LENGTH is not null then (wsc.START_TIME + NUMTODSINTERVAL(to_number(trunc((wsc.END_TIME - wsc.START_TIME)/7/wsc.CYCLE_LENGTH) * wsc.CYCLE_LENGTH * 7), 'day')) " + 
				"  when wsc.CYCLE_TYPE = 3 and wsc.CYCLE_LENGTH is not null then (wsc.START_TIME + NUMTODSINTERVAL(to_number(trunc((wsc.END_TIME - wsc.START_TIME)/30/wsc.CYCLE_LENGTH) * wsc.CYCLE_LENGTH * 30), 'day')) " + 
				"  else null end)  " + 
				//Nếu END_TIME lớn hơn ngày hiện tại và START_TIME nhỏ hơn ngày hiện tại thì lấy START_TIME cộng với khoảng thời gian đã chia đều cho chu kỳ
				"  WHEN wsc.START_TIME <= sysdate AND wsc.END_TIME>= sysdate then (case when wsc.CYCLE_TYPE = 1 and wsc.CYCLE_LENGTH is not null then (wsc.START_TIME + NUMTODSINTERVAL(to_number(trunc((sysdate - wsc.START_TIME)/wsc.CYCLE_LENGTH) * wsc.CYCLE_LENGTH), 'day')) " + 
				"  when wsc.CYCLE_TYPE = 2 and wsc.CYCLE_LENGTH is not null then (wsc.START_TIME + NUMTODSINTERVAL(to_number(trunc((sysdate - wsc.START_TIME)/7/wsc.CYCLE_LENGTH) * wsc.CYCLE_LENGTH * 7), 'day')) " + 
				"  when wsc.CYCLE_TYPE = 3 and wsc.CYCLE_LENGTH is not null then (wsc.START_TIME + NUMTODSINTERVAL(to_number(trunc((sysdate - wsc.START_TIME)/30/wsc.CYCLE_LENGTH) * wsc.CYCLE_LENGTH * 30), 'day')) " + 
				"  else null end) " + 
				"  else null end) nearestMaintainDate, ");
		sql.append(" tbl.WO_CODE woCode, tbl.WO_NAME woName, tbl.CONSTRUCTION_CODE systemCode, ");
		sql.append(" wsc.CYCLE_LENGTH cycleLength, ");
		sql.append(" wsc.CYCLE_TYPE cycleType, ");
		sql.append(" (case when wsc.CYCLE_TYPE =1 then 'Ngày' " + 
				"  when wsc.CYCLE_TYPE=2 then 'Tuần' " + 
				"  when wsc.CYCLE_TYPE=3 then 'Tháng' " + 
				"  ELSE '' end) cycleTypeName, ");
		sql.append(" (case when tbl.STATE='UNASSIGN' then 'Mới tạo' " + 
				"  when tbl.STATE='ASSIGN_CD' then 'Chờ CD tiếp nhận' " + 
				"  when tbl.STATE='ACCEPT_CD' then 'CD đã tiếp nhận' " + 
				"  when tbl.STATE='REJECT_CD' then 'CD từ chối' " + 
				"  when tbl.STATE='ASSIGN_FT' then 'Chờ FT tiếp nhận' " + 
				"  when tbl.STATE='ACCEPT_FT' then 'FT đã tiếp nhận' " + 
				"  when tbl.STATE='REJECT_FT' then 'FT từ chối' " + 
				"  when tbl.STATE='PROCESSING' then 'Đang thực hiện' " + 
				"  when tbl.STATE='DONE' then 'Đã thực hiện' " + 
				"  when tbl.STATE='OK' then 'Hoàn thành' " + 
				"  when tbl.STATE='NG' then 'Chưa hoàn thành' " + 
				"  when tbl.STATE='OPINION_RQ_1' then 'Xin ý kiến CD Level 1' " + 
				"  when tbl.STATE='OPINION_RQ_2' then 'Xin ý kiến CD Level 2' " + 
				"  when tbl.STATE='OPINION_RQ_3' then 'Xin ý kiến CD Level 3' " + 
				"  when tbl.STATE='OPINION_RQ_4' then 'Xin ý kiến CD Level 4' " + 
				"  when tbl.STATE='CD_OK' then 'Điều phối duyệt OK' " + 
				"  when tbl.STATE='CD_NG' then 'Điều phối duyệt chưa OK' " + 
				"  else '' end) woState ");
		if(StringUtils.isNotBlank(obj.getYear())) {
			if(obj.getYear().equals(String.valueOf(year))) {
				//Nếu là năm hiện tại thì tính số lần từ 01/01 đến ngày hiện tại.
				sql.append(" ,(case when wsc.CYCLE_TYPE =1 AND wsc.CYCLE_LENGTH is not null then trunc((select trunc(sysdate - to_date(to_char(EXTRACT(YEAR FROM sysdate)||'-'||'01'||'-'||'01'), 'YYYY-MM-DD')) from dual)/ wsc.CYCLE_LENGTH)  " + 
						"  when wsc.CYCLE_TYPE =2 AND wsc.CYCLE_LENGTH is not null then trunc((select trunc(sysdate - to_date(to_char(EXTRACT(YEAR FROM sysdate)||'-'||'01'||'-'||'01'), 'YYYY-MM-DD')) from dual)/7/ wsc.CYCLE_LENGTH)  " + 
						"  when wsc.CYCLE_TYPE =3 AND wsc.CYCLE_LENGTH is not null then trunc((select trunc(sysdate - to_date(to_char(EXTRACT(YEAR FROM sysdate)||'-'||'01'||'-'||'01'), 'YYYY-MM-DD')) from dual)/30/ wsc.CYCLE_LENGTH)  " + 
						"  else 0 end) numberMaintainTimePerYear ");
			} else {
				//Nếu khác năm hiện tại thì tính số lần từ 01/01 đến ngày 31/12 của năm đó.
				sql.append(" ,(case when wsc.CYCLE_TYPE =1 AND wsc.CYCLE_LENGTH is not null then trunc((select trunc(to_date(to_char(:year ||'-'||'12'||'-'||'31'), 'YYYY-MM-DD') - to_date(to_char(:year ||'-'||'01'||'-'||'01'), 'YYYY-MM-DD')) from dual)/ wsc.CYCLE_LENGTH) " + 
						"when wsc.CYCLE_TYPE =2 AND wsc.CYCLE_LENGTH is not null then trunc((select trunc(to_date(to_char(:year ||'-'||'12'||'-'||'31'), 'YYYY-MM-DD') - to_date(to_char(:year ||'-'||'01'||'-'||'01'), 'YYYY-MM-DD')) from dual)/7/ wsc.CYCLE_LENGTH) " + 
						"when wsc.CYCLE_TYPE =3 AND wsc.CYCLE_LENGTH is not null then trunc((select trunc(to_date(to_char(:year ||'-'||'12'||'-'||'31'), 'YYYY-MM-DD') - to_date(to_char(:year ||'-'||'01'||'-'||'01'), 'YYYY-MM-DD')) from dual)/30/ wsc.CYCLE_LENGTH) " + 
						"else 0 end) numberMaintainTimePerYear ");
			}
		}
		
		//Số lần bảo hành 3 tháng tới: Lấy ngày hiện tại cộng thêm 3 tháng rồi chia theo chu kỳ.
		sql.append(" ,(case when wsc.CYCLE_TYPE =1 and wsc.CYCLE_LENGTH is not null then trunc((select ADD_MONTHS(sysdate,3) - sysdate from dual)/wsc.cycle_length) " + 
				"  when wsc.CYCLE_TYPE =2 and wsc.CYCLE_LENGTH is not null then trunc((select ADD_MONTHS(sysdate,3) - sysdate from dual)/7/wsc.cycle_length) " + 
				"  when wsc.CYCLE_TYPE =3 and wsc.CYCLE_LENGTH is not null then trunc((select ADD_MONTHS(sysdate,3) - sysdate from dual)/30/wsc.cycle_length) " + 
				"  else 0 end) numberMaintainTimeNextThreeMonth ");
		//Số tháng bảo hành trong 3 tháng tới
		sql.append(" , (EXTRACT(MONTH FROM sysdate) + 1) ||','|| (EXTRACT(MONTH FROM sysdate) + 2) ||','|| (EXTRACT(MONTH FROM sysdate) + 3) numberMonthMaintainNextThreeMonth ");
		
		sql.append(" FROM WO_SCHEDULE_CONFIG wsc " + 
				"LEFT JOIN sys_group sg " + 
				"ON wsc.CD_LEVEL_2 = sg.SYS_GROUP_ID " + 
				"LEFT JOIN (SELECT wo.WO_CODE, " + 
				"wo.CAT_WORK_ITEM_TYPE_ID, " + 
				"wo.STATE, " + 
				"wo.FINISH_DATE, wo.CONSTRUCTION_CODE, wo.WO_NAME " + 
				"FROM wo " + 
				"WHERE wo.WO_NAME            in ('Xử lý sự cố','Sửa chữa bảo dưỡng định kỳ') " + 
				"AND wo.STATUS!              =0) tbl " + 
				"on wsc.ID=tbl.CAT_WORK_ITEM_TYPE_ID " + 
				"AND tbl.FINISH_DATE          = " + 
				"  (SELECT MAX(wo1.FINISH_DATE) " + 
				"  FROM wo wo1 " + 
				"  WHERE wo1.WO_NAME            in ('Xử lý sự cố','Sửa chữa bảo dưỡng định kỳ') " + 
				"  AND wo1.STATUS!              =0 " + 
				"  AND wo1.CAT_WORK_ITEM_TYPE_ID=wsc.ID " + 
				"  ) " + 
				"WHERE wsc.STATUS>0 ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("systemCode", new StringType());
		query.addScalar("areaCode", new StringType());
		query.addScalar("cnkt", new StringType());
		query.addScalar("taskCode", new StringType());
		query.addScalar("monthlyNominalCapacity", new StringType());
		query.addScalar("reportDate", new StringType());
		query.addScalar("nearestMaintainDate", new StringType());
		query.addScalar("woCode", new StringType());
		query.addScalar("woName", new StringType());
		query.addScalar("woState", new StringType());
//		query.addScalar("woResult", new StringType());
		if(StringUtils.isNotBlank(obj.getYear())) {
			query.addScalar("numberMaintainTimePerYear", new LongType());
		}
		query.addScalar("numberMaintainTimeNextThreeMonth", new LongType());
		query.addScalar("numberMonthMaintainNextThreeMonth", new StringType());
		query.addScalar("cycleLength", new LongType());
		query.addScalar("cycleType", new StringType());
		query.addScalar("cycleTypeName", new StringType());
		
		query.setParameter("reportDate", obj.getReportDate());
		
		if(StringUtils.isNotBlank(obj.getYear())) {
			if(!obj.getYear().equals(String.valueOf(year))) {
				query.setParameter("year", obj.getYear());
			}
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ApiReportPeriodicMaintanceDTO.class));
		
		return query.list();
	}
	
}

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
				"				  'M???i t???o'   " + 
				"				WHEN wo.STATE='ASSIGN_CD' THEN   " + 
				"				  'Ch??? CD ti???p nh???n'   " + 
				"				WHEN wo.STATE='ACCEPT_CD' THEN   " + 
				"				  'CD ???? ti???p nh???n'   " + 
				"				WHEN wo.STATE='REJECT_CD' THEN   " + 
				"				  'CD t??? ch???i'   " + 
				"				WHEN wo.STATE='ASSIGN_FT' THEN   " + 
				"				  'Ch??? FT ti???p nh???n'   " + 
				"				WHEN wo.STATE='ACCEPT_FT' THEN   " + 
				"				  'FT ???? ti???p nh???n'   " + 
				"				WHEN wo.STATE='REJECT_FT' THEN   " + 
				"				  'FT t??? ch???i'   " + 
				"				WHEN wo.STATE='PROCESSING' THEN   " + 
				"				  '??ang th???c hi???n'   " + 
				"				WHEN wo.STATE='DONE' THEN   " + 
				"				  '???? th???c hi???n'   " + 
				"				WHEN wo.STATE='OK' THEN   " + 
				"				  'Ho??n th??nh'   " + 
				"				WHEN wo.STATE='NG' THEN   " + 
				"				  'Ch??a ho??n th??nh'   " + 
				"				WHEN wo.STATE='OPINION_RQ_1' THEN   " + 
				"				  'Xin ?? ki???n CD Level 1'   " + 
				"				WHEN wo.STATE='OPINION_RQ_2' THEN   " + 
				"				  'Xin ?? ki???n CD Level 2'   " + 
				"				WHEN wo.STATE='OPINION_RQ_3' THEN   " + 
				"				  'Xin ?? ki???n CD Level 3'   " + 
				"				WHEN wo.STATE='OPINION_RQ_4' THEN   " + 
				"				  'Xin ?? ki???n CD Level 4'   " + 
				"				WHEN wo.STATE='CD_OK' THEN   " + 
				"				  '??i???u ph???i duy???t OK'   " + 
				"				WHEN wo.STATE='CD_NG' THEN   " + 
				"				  '??i???u ph???i duy???t ch??a OK'   " + 
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
				"				   'M???i t???o'    " + 
				"				 WHEN wo.STATE='ASSIGN_CD' THEN    " + 
				"				   'Ch??? CD ti???p nh???n'    " + 
				"				 WHEN wo.STATE='ACCEPT_CD' THEN    " + 
				"				   'CD ???? ti???p nh???n'    " + 
				"				 WHEN wo.STATE='REJECT_CD' THEN    " + 
				"				   'CD t??? ch???i'    " + 
				"				 WHEN wo.STATE='ASSIGN_FT' THEN    " + 
				"				   'Ch??? FT ti???p nh???n'    " + 
				"				 WHEN wo.STATE='ACCEPT_FT' THEN    " + 
				"				   'FT ???? ti???p nh???n'    " + 
				"				 WHEN wo.STATE='REJECT_FT' THEN    " + 
				"				   'FT t??? ch???i'    " + 
				"				 WHEN wo.STATE='PROCESSING' THEN    " + 
				"				   '??ang th???c hi???n'    " + 
				"				 WHEN wo.STATE='DONE' THEN    " + 
				"				   '???? th???c hi???n'    " + 
				"				 WHEN wo.STATE='OK' THEN    " + 
				"				   'Ho??n th??nh'    " + 
				"				 WHEN wo.STATE='NG' THEN    " + 
				"				   'Ch??a ho??n th??nh'    " + 
				"				 WHEN wo.STATE='OPINION_RQ_1' THEN    " + 
				"				   'Xin ?? ki???n CD Level 1'    " + 
				"				 WHEN wo.STATE='OPINION_RQ_2' THEN    " + 
				"				   'Xin ?? ki???n CD Level 2'    " + 
				"				 WHEN wo.STATE='OPINION_RQ_3' THEN    " + 
				"				   'Xin ?? ki???n CD Level 3'    " + 
				"				 WHEN wo.STATE='OPINION_RQ_4' THEN    " + 
				"				   'Xin ?? ki???n CD Level 4'    " + 
				"				 WHEN wo.STATE='CD_OK' THEN    " + 
				"				   '??i???u ph???i duy???t OK'    " + 
				"				 WHEN wo.STATE='CD_NG' THEN    " + 
				"				   '??i???u ph???i duy???t ch??a OK'    " + 
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
				"				   'M???i t???o'    " + 
				"				 WHEN tr.STATE='ASSIGN_CD' THEN    " + 
				"				   'Ch??? CD ti???p nh???n'    " + 
				"				 WHEN tr.STATE='ACCEPT_CD' THEN    " + 
				"				   'CD ???? ti???p nh???n'    " + 
				"				 WHEN tr.STATE='REJECT_CD' THEN    " + 
				"				   'CD ???? t??? ch???i'    " + 
				"				 WHEN tr.STATE='PROCESSING' THEN    " + 
				"				   '??ang th???c hi???n'    " + 
				"				 WHEN tr.STATE='DONE' THEN    " + 
				"				   '???? th???c hi???n'    " + 
				"				 WHEN tr.STATE='OK' THEN    " + 
				"				   'Ho??n th??nh'    " + 
				"				 WHEN tr.STATE='NG' THEN    " + 
				"				   'Ch??a ho??n th??nh'    " + 
				"				 WHEN tr.STATE='OPINION_RQ' THEN    " + 
				"				   'Xin ?? ki???n'    " + 
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
	
	//B??o c??o BD ?????nh k???
	@SuppressWarnings("unchecked")
	public List<ApiReportPeriodicMaintanceDTO> getDataReportMaintancePeriodic(ApiConstructionRequest obj){
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(" sg.AREA_CODE areaCode, ");
		sql.append(" sg.NAME cnkt, ");
		sql.append(" wsc.CODE taskCode, ");
		sql.append(" wsc.QUOTA_TIME monthlyNominalCapacity, ");
//		sql.append(" NULL monthlyNominalCapacity, ");
		sql.append(" :reportDate reportDate, ");
		//N???u END_TIME nh??? h??n ng??y hi???n t???i th?? l???y START_TIME c???ng v???i kho???ng th???i gian ???? chia ?????u cho chu k??? (VD: END_TIME - START_TIME = 37 nh??ng t??nh theo chu k??? ch??? c?? 36 ng??y th?? ??o???n n??y chia cho 36)
		sql.append(" (case when wsc.END_TIME<= sysdate then (case when wsc.CYCLE_TYPE = 1 and wsc.CYCLE_LENGTH is not null then (wsc.START_TIME + NUMTODSINTERVAL(to_number(trunc((wsc.END_TIME - wsc.START_TIME)/wsc.CYCLE_LENGTH) * wsc.CYCLE_LENGTH), 'day')) " + 
				"  when wsc.CYCLE_TYPE = 2 and wsc.CYCLE_LENGTH is not null then (wsc.START_TIME + NUMTODSINTERVAL(to_number(trunc((wsc.END_TIME - wsc.START_TIME)/7/wsc.CYCLE_LENGTH) * wsc.CYCLE_LENGTH * 7), 'day')) " + 
				"  when wsc.CYCLE_TYPE = 3 and wsc.CYCLE_LENGTH is not null then (wsc.START_TIME + NUMTODSINTERVAL(to_number(trunc((wsc.END_TIME - wsc.START_TIME)/30/wsc.CYCLE_LENGTH) * wsc.CYCLE_LENGTH * 30), 'day')) " + 
				"  else null end)  " + 
				//N???u END_TIME l???n h??n ng??y hi???n t???i v?? START_TIME nh??? h??n ng??y hi???n t???i th?? l???y START_TIME c???ng v???i kho???ng th???i gian ???? chia ?????u cho chu k???
				"  WHEN wsc.START_TIME <= sysdate AND wsc.END_TIME>= sysdate then (case when wsc.CYCLE_TYPE = 1 and wsc.CYCLE_LENGTH is not null then (wsc.START_TIME + NUMTODSINTERVAL(to_number(trunc((sysdate - wsc.START_TIME)/wsc.CYCLE_LENGTH) * wsc.CYCLE_LENGTH), 'day')) " + 
				"  when wsc.CYCLE_TYPE = 2 and wsc.CYCLE_LENGTH is not null then (wsc.START_TIME + NUMTODSINTERVAL(to_number(trunc((sysdate - wsc.START_TIME)/7/wsc.CYCLE_LENGTH) * wsc.CYCLE_LENGTH * 7), 'day')) " + 
				"  when wsc.CYCLE_TYPE = 3 and wsc.CYCLE_LENGTH is not null then (wsc.START_TIME + NUMTODSINTERVAL(to_number(trunc((sysdate - wsc.START_TIME)/30/wsc.CYCLE_LENGTH) * wsc.CYCLE_LENGTH * 30), 'day')) " + 
				"  else null end) " + 
				"  else null end) nearestMaintainDate, ");
		sql.append(" tbl.WO_CODE woCode, tbl.WO_NAME woName, tbl.CONSTRUCTION_CODE systemCode, ");
		sql.append(" wsc.CYCLE_LENGTH cycleLength, ");
		sql.append(" wsc.CYCLE_TYPE cycleType, ");
		sql.append(" (case when wsc.CYCLE_TYPE =1 then 'Ng??y' " + 
				"  when wsc.CYCLE_TYPE=2 then 'Tu???n' " + 
				"  when wsc.CYCLE_TYPE=3 then 'Th??ng' " + 
				"  ELSE '' end) cycleTypeName, ");
		sql.append(" (case when tbl.STATE='UNASSIGN' then 'M???i t???o' " + 
				"  when tbl.STATE='ASSIGN_CD' then 'Ch??? CD ti???p nh???n' " + 
				"  when tbl.STATE='ACCEPT_CD' then 'CD ???? ti???p nh???n' " + 
				"  when tbl.STATE='REJECT_CD' then 'CD t??? ch???i' " + 
				"  when tbl.STATE='ASSIGN_FT' then 'Ch??? FT ti???p nh???n' " + 
				"  when tbl.STATE='ACCEPT_FT' then 'FT ???? ti???p nh???n' " + 
				"  when tbl.STATE='REJECT_FT' then 'FT t??? ch???i' " + 
				"  when tbl.STATE='PROCESSING' then '??ang th???c hi???n' " + 
				"  when tbl.STATE='DONE' then '???? th???c hi???n' " + 
				"  when tbl.STATE='OK' then 'Ho??n th??nh' " + 
				"  when tbl.STATE='NG' then 'Ch??a ho??n th??nh' " + 
				"  when tbl.STATE='OPINION_RQ_1' then 'Xin ?? ki???n CD Level 1' " + 
				"  when tbl.STATE='OPINION_RQ_2' then 'Xin ?? ki???n CD Level 2' " + 
				"  when tbl.STATE='OPINION_RQ_3' then 'Xin ?? ki???n CD Level 3' " + 
				"  when tbl.STATE='OPINION_RQ_4' then 'Xin ?? ki???n CD Level 4' " + 
				"  when tbl.STATE='CD_OK' then '??i???u ph???i duy???t OK' " + 
				"  when tbl.STATE='CD_NG' then '??i???u ph???i duy???t ch??a OK' " + 
				"  else '' end) woState ");
		if(StringUtils.isNotBlank(obj.getYear())) {
			if(obj.getYear().equals(String.valueOf(year))) {
				//N???u l?? n??m hi???n t???i th?? t??nh s??? l???n t??? 01/01 ?????n ng??y hi???n t???i.
				sql.append(" ,(case when wsc.CYCLE_TYPE =1 AND wsc.CYCLE_LENGTH is not null then trunc((select trunc(sysdate - to_date(to_char(EXTRACT(YEAR FROM sysdate)||'-'||'01'||'-'||'01'), 'YYYY-MM-DD')) from dual)/ wsc.CYCLE_LENGTH)  " + 
						"  when wsc.CYCLE_TYPE =2 AND wsc.CYCLE_LENGTH is not null then trunc((select trunc(sysdate - to_date(to_char(EXTRACT(YEAR FROM sysdate)||'-'||'01'||'-'||'01'), 'YYYY-MM-DD')) from dual)/7/ wsc.CYCLE_LENGTH)  " + 
						"  when wsc.CYCLE_TYPE =3 AND wsc.CYCLE_LENGTH is not null then trunc((select trunc(sysdate - to_date(to_char(EXTRACT(YEAR FROM sysdate)||'-'||'01'||'-'||'01'), 'YYYY-MM-DD')) from dual)/30/ wsc.CYCLE_LENGTH)  " + 
						"  else 0 end) numberMaintainTimePerYear ");
			} else {
				//N???u kh??c n??m hi???n t???i th?? t??nh s??? l???n t??? 01/01 ?????n ng??y 31/12 c???a n??m ????.
				sql.append(" ,(case when wsc.CYCLE_TYPE =1 AND wsc.CYCLE_LENGTH is not null then trunc((select trunc(to_date(to_char(:year ||'-'||'12'||'-'||'31'), 'YYYY-MM-DD') - to_date(to_char(:year ||'-'||'01'||'-'||'01'), 'YYYY-MM-DD')) from dual)/ wsc.CYCLE_LENGTH) " + 
						"when wsc.CYCLE_TYPE =2 AND wsc.CYCLE_LENGTH is not null then trunc((select trunc(to_date(to_char(:year ||'-'||'12'||'-'||'31'), 'YYYY-MM-DD') - to_date(to_char(:year ||'-'||'01'||'-'||'01'), 'YYYY-MM-DD')) from dual)/7/ wsc.CYCLE_LENGTH) " + 
						"when wsc.CYCLE_TYPE =3 AND wsc.CYCLE_LENGTH is not null then trunc((select trunc(to_date(to_char(:year ||'-'||'12'||'-'||'31'), 'YYYY-MM-DD') - to_date(to_char(:year ||'-'||'01'||'-'||'01'), 'YYYY-MM-DD')) from dual)/30/ wsc.CYCLE_LENGTH) " + 
						"else 0 end) numberMaintainTimePerYear ");
			}
		}
		
		//S??? l???n b???o h??nh 3 th??ng t???i: L???y ng??y hi???n t???i c???ng th??m 3 th??ng r???i chia theo chu k???.
		sql.append(" ,(case when wsc.CYCLE_TYPE =1 and wsc.CYCLE_LENGTH is not null then trunc((select ADD_MONTHS(sysdate,3) - sysdate from dual)/wsc.cycle_length) " + 
				"  when wsc.CYCLE_TYPE =2 and wsc.CYCLE_LENGTH is not null then trunc((select ADD_MONTHS(sysdate,3) - sysdate from dual)/7/wsc.cycle_length) " + 
				"  when wsc.CYCLE_TYPE =3 and wsc.CYCLE_LENGTH is not null then trunc((select ADD_MONTHS(sysdate,3) - sysdate from dual)/30/wsc.cycle_length) " + 
				"  else 0 end) numberMaintainTimeNextThreeMonth ");
		//S??? th??ng b???o h??nh trong 3 th??ng t???i
		sql.append(" , (EXTRACT(MONTH FROM sysdate) + 1) ||','|| (EXTRACT(MONTH FROM sysdate) + 2) ||','|| (EXTRACT(MONTH FROM sysdate) + 3) numberMonthMaintainNextThreeMonth ");
		
		sql.append(" FROM WO_SCHEDULE_CONFIG wsc " + 
				"LEFT JOIN sys_group sg " + 
				"ON wsc.CD_LEVEL_2 = sg.SYS_GROUP_ID " + 
				"LEFT JOIN (SELECT wo.WO_CODE, " + 
				"wo.CAT_WORK_ITEM_TYPE_ID, " + 
				"wo.STATE, " + 
				"wo.FINISH_DATE, wo.CONSTRUCTION_CODE, wo.WO_NAME " + 
				"FROM wo " + 
				"WHERE wo.WO_NAME            in ('X??? l?? s??? c???','S???a ch???a b???o d?????ng ?????nh k???') " + 
				"AND wo.STATUS!              =0) tbl " + 
				"on wsc.ID=tbl.CAT_WORK_ITEM_TYPE_ID " + 
				"AND tbl.FINISH_DATE          = " + 
				"  (SELECT MAX(wo1.FINISH_DATE) " + 
				"  FROM wo wo1 " + 
				"  WHERE wo1.WO_NAME            in ('X??? l?? s??? c???','S???a ch???a b???o d?????ng ?????nh k???') " + 
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

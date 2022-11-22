package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.bo.DesignEstimatesBO;
import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.DesignEstimatesDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.erp.dto.CatProvincesDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Transactional
@Repository("designEstimatesDAO")
public class DesignEstimatesDAO extends BaseFWDAOImpl<DesignEstimatesBO, Long>{

	public DesignEstimatesDAO() {
        this.model = new DesignEstimatesBO();
    }

    public DesignEstimatesDAO(Session session) {
        this.session = session;
    }

	public List<DesignEstimatesDTO> doSearch(DesignEstimatesDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT ");
		sql.append(" de.DESIGN_ESTIMATES_ID designEstimatesId, de.AREA area, ");
		sql.append(" de.PROVINCE_ID provinceId, de.STATION_VTNET stationVTNET, ");
		sql.append(" de.STATION_VCC stationVCC, de.STATION_ADDRESS stationAddress, ");
		sql.append(" de.DISTRICT_NAME districtName, de.STATION_TYPE stationType, de.ENGINE_ROOM engineRoom, ");
		sql.append(" de.TERRAIN terrain, de.STATION_LONG stationLong, de.STATION_LAT stationLat, ");
		sql.append(" de.PILLAR_TYPE pillarType, de.LOCATION location, de.PILLAR_HIGHT pillarHight, ");
		sql.append(" de.TUBE tube, de.FUNDAMENTAL fundamental, de.LEVEL_ROCK_EARTH levelRockEarth, ");
		sql.append(" de.FLOOT_PASS flootPass, de.EXPLOSION_FACTORY explosionFactory, de.STATUS status, ");
		sql.append(" de.SOURCE_MACRO sourceMacro, de.SOURCE_RRU sourceRRU, de.CREATED_USER createdUser, ");
		sql.append(" de.GROUNDING_PILE groundingPile, de.GROUNDING_GEM groundingGem, de.NODE node, ");
		sql.append(" de.GROUNDING_DRILL groundingDrill, de.WIRE_2X25 wire2x25, de.UPDATE_USER updateUser, ");
		sql.append(" de.WIRE_2X35 wire2x35, de.WIRE_2X50 wire2x50, de.WIRE_2X70 wire2x70, ");
		sql.append(" de.WIRE_4X25 wire4x25, de.WIRE_4X35 wire4x35, de.PILLAR_AVAILABLE pillarAvailable, ");
		sql.append(" de.TM_PILLAR_60 tmPillar60, de.TM_PILLAR_70 tmPillar70, de.TM_PILLAR_75 tmPillar75, ");
		sql.append(" de.TM_PILLAR_80 tmPillar80, de.TM_PILLAR_85 tmPillar85, de.TM_PILLAR_100 tmPillar100, ");
		sql.append(" de.DESIGN_DATE designDate, de.DESIGN_UPDATE_DATE designUpdateDate, de.CREATED_DATE createdDate, ");
		sql.append(" de.UPDATE_DATE updateDate, de.ESTIMATING_DATE estimatingDate, de.DESIGN_USER_ID designUserId, ");
		sql.append(" de.DESIGN_UPDATE_USER designUpdateUser, de.CREAT_DESIGN_ESTIMATES_USER creatDesignEstimatesUser, ");
		sql.append(" cp.NAME provinceName, su.FULL_NAME createdUserName, su1.FULL_NAME designUserName,  ");
		sql.append(" su2.FULL_NAME designUpdateUserName, su3.FULL_NAME creatDesignEstimatesUserName ");
		sql.append(" FROM DESIGN_ESTIMATES de ");
		sql.append(" LEFT JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON de.PROVINCE_ID = cp.CAT_PROVINCE_ID ");
//		sql.append(" LEFT JOIN CTCT_IMS_OWNER.AREA a ON a.AREA_ID = de.DISTRICT_ID ");
		sql.append(" LEFT JOIN CTCT_VPS_OWNER.SYS_USER su ON su.SYS_USER_ID = de.CREATED_USER ");
		sql.append(" LEFT JOIN CTCT_VPS_OWNER.SYS_USER su1 ON su1.SYS_USER_ID = de.DESIGN_USER_ID ");
		sql.append(" LEFT JOIN CTCT_VPS_OWNER.SYS_USER su2 ON su2.SYS_USER_ID = de.DESIGN_UPDATE_USER ");
		sql.append(" LEFT JOIN CTCT_VPS_OWNER.SYS_USER su3 ON su3.SYS_USER_ID = de.CREAT_DESIGN_ESTIMATES_USER ");
		sql.append(" WHERE de.STATUS = 1 ");
		if(obj.getArea() != null && !obj.getArea().equals("")){
			sql.append(" AND de.AREA = :area ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND de.PROVINCE_ID = :provinceId ");
		}
		if(obj.getStationVTNET() != null && !obj.getStationVTNET().equals("")){
			sql.append(" AND UPPER(de.STATION_VTNET) like UPPER(:stationVTNET) ");
		}
		if(obj.getStationVCC() != null && !obj.getStationVCC().equals("")){
			sql.append(" AND UPPER(de.STATION_VCC) like UPPER(:stationVCC) ");
		}
		if(obj.getCreatedDateFrom() != null) {
			sql.append(" AND de.CREATED_DATE >= :createdDateFrom ");
		}
		if(obj.getCreatedDateTo() != null) {
			sql.append(" AND de.CREATED_DATE <= :createdDateTo ");
		}
		sql.append(" ORDER BY de.CREATED_DATE desc ");
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.setResultTransformer(Transformers.aliasToBean(DesignEstimatesDTO.class));
		query.addScalar("area", new StringType());
		query.addScalar("node", new StringType());
	    query.addScalar("stationVTNET", new StringType());
	    query.addScalar("stationVCC", new StringType());
	    query.addScalar("stationAddress", new StringType());
	    query.addScalar("stationType", new StringType());
	    query.addScalar("terrain", new StringType());
	    query.addScalar("stationLong", new StringType());
	    query.addScalar("stationLat", new StringType());
	    query.addScalar("pillarType", new StringType());
	    query.addScalar("location", new StringType());
	    query.addScalar("pillarHight", new StringType());
	    query.addScalar("tube", new StringType());
	    query.addScalar("fundamental", new StringType());
	    query.addScalar("levelRockEarth", new StringType());
	    query.addScalar("engineRoom", new StringType());
	    query.addScalar("flootPass", new StringType());
	    query.addScalar("explosionFactory", new StringType());
	    query.addScalar("sourceMacro", new StringType());
	    query.addScalar("sourceRRU", new StringType());
	    query.addScalar("groundingPile", new StringType());
	    query.addScalar("groundingGem", new StringType());
	    query.addScalar("groundingDrill", new StringType());
	    query.addScalar("wire2x25", new StringType());
	    query.addScalar("wire2x35", new StringType());
	    query.addScalar("wire2x50", new StringType());
	    query.addScalar("wire2x70", new StringType());
	    query.addScalar("wire4x25", new StringType());
	    query.addScalar("wire4x35", new StringType());
	    query.addScalar("pillarAvailable", new StringType());
	    query.addScalar("tmPillar60", new StringType());
	    query.addScalar("tmPillar70", new StringType());
	    query.addScalar("tmPillar75", new StringType());
	    query.addScalar("tmPillar80", new StringType());
	    query.addScalar("tmPillar85", new StringType());
	    query.addScalar("tmPillar100", new StringType());
	    query.addScalar("provinceName", new StringType());
	    query.addScalar("districtName", new StringType());
	    query.addScalar("designUserId", new LongType());
	    query.addScalar("designUpdateUser", new LongType());
	    query.addScalar("creatDesignEstimatesUser", new LongType());
	    query.addScalar("createdUser", new LongType());
	    query.addScalar("updateUser", new LongType());
	    query.addScalar("provinceId", new LongType());
//	    query.addScalar("districtId", new LongType());
		query.addScalar("designEstimatesId", new LongType());
	    query.addScalar("status", new LongType());
	    query.addScalar("designDate", new DateType());
	    query.addScalar("designUpdateDate", new DateType());
	    query.addScalar("createdDate", new DateType());
	    query.addScalar("updateDate", new DateType());
	    query.addScalar("estimatingDate", new DateType());
	    query.addScalar("createdUserName", new StringType());
	    query.addScalar("designUserName", new StringType());
	    query.addScalar("designUpdateUserName", new StringType());
	    query.addScalar("creatDesignEstimatesUserName", new StringType());
	    
		if(obj.getArea() != null && !obj.getArea().equals("")){
			query.setParameter("area", obj.getArea());
			queryCount.setParameter("area", obj.getArea());
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
			queryCount.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getStationVTNET() != null && !obj.getStationVTNET().equals("")){
			query.setParameter("stationVTNET", "%" + obj.getStationVTNET() + "%");
			queryCount.setParameter("stationVTNET", "%" + obj.getStationVTNET() + "%");
		}
		if(obj.getStationVCC() != null && !obj.getStationVCC().equals("")){
			query.setParameter("stationVCC", "%" + obj.getStationVCC() + "%");
			queryCount.setParameter("stationVCC", "%" + obj.getStationVCC() + "%");
		}
		if(obj.getCreatedDateFrom() != null) {
			query.setParameter("createdDateFrom", obj.getCreatedDateFrom());
			queryCount.setParameter("createdDateFrom", obj.getCreatedDateFrom());
		}
		if(obj.getCreatedDateTo() != null) {
			query.setParameter("createdDateTo", obj.getCreatedDateTo());
			queryCount.setParameter("createdDateTo", obj.getCreatedDateTo());
		}

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		  	
		    obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		    
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<CatProvincesDTO> doSearchArea(CatProvincesDTO obj) {
		StringBuilder sql = new StringBuilder(
				"select distinct AREA_NAME_LEVEL3 districtName, AREA_ID districtId from CTCT_IMS_OWNER.AREA where AREA_NAME_LEVEL3 is not null AND AREA_LEVEL = 3 ");
		if (obj.getProvinceId() != null) {
			sql.append(" AND PROVINCE_ID = :provinceId ");
		}
		if(obj.getDistrictName() != null && !obj.getDistrictName().equals("")) {
			sql.append(" AND UPPER(AREA_NAME_LEVEL3) like UPPER(:districtName) ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("districtName", new StringType());
		query.addScalar("districtId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(CatProvincesDTO.class));
		if (obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getDistrictName() != null && !obj.getDistrictName().equals("")) {
			query.setParameter("districtName", "%"+obj.getDistrictName()+"%");
		}
		List<CatProvincesDTO> lst = query.list();

		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<SysUserDTO> doSearchUser(SysUserDTO obj) {
		StringBuilder sql = new StringBuilder(
				"select SYS_USER_ID userId, FULL_NAME fullName, EMPLOYEE_CODE employeeCode from SYS_USER where ROWNUM < 50 ");
		if (obj.getName() != null) {
			sql.append(" AND (UPPER(FULL_NAME) like UPPER(:name) OR UPPER(EMPLOYEE_CODE) like UPPER(:name) OR UPPER(EMAIL) like UPPER(:name)) ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("fullName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("userId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
		if (obj.getName() != null) {
			query.setParameter("name", "%" + obj.getName() + "%");
		}
		List<SysUserDTO> lst = query.list();

		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<CatStationDTO> doSearchStationVCC(CatStationDTO obj) {
		StringBuilder sql = new StringBuilder("select CAT_STATION_ID catStationId, CODE code from CTCT_CAT_OWNER.CAT_STATION where ROWNUM < 50 ");
		if(obj.getCode() != null && !obj.getCode().equals("")) {
			sql.append(" AND (UPPER(CODE) like UPPER(:code) OR UPPER(NAME) like UPPER(:code))");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("catStationId", new LongType());
		query.addScalar("code", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));
		if (obj.getCode() != null && !obj.getCode().equals("")) {
			query.setParameter("code", "%" + obj.getCode() + "%");
		}
		List<CatStationDTO> lst = query.list();
		return lst;
	}
	
//	@SuppressWarnings("unchecked")
//	public UtilAttachDocumentDTO getFile(String code) {
//		StringBuilder sql = new StringBuilder("");
//		sql.append(" select wma.FILE_NAME name, wma.FILE_PATH filePath  from CTCT_COMS_OWNER.WO w ");
//		sql.append(" LEFT JOIN CTCT_COMS_OWNER.WO_MAPPING_ATTACH wma ON w.ID = wma.WO_ID ");
//		sql.append(" where w.STATION_CODE = :code AND w.WO_TYPE_ID = 261 AND wma.STATUS = 1 AND wma.TYPE = 1 ");
//		
//		SQLQuery query = getSession().createSQLQuery(sql.toString());
//		query.setParameter("code", code);		
//		query.addScalar("name", new StringType());
//		query.addScalar("filePath", new StringType());
//		query.setResultTransformer(Transformers.aliasToBean(UtilAttachDocumentDTO.class));
//		List<UtilAttachDocumentDTO> lst = query.list();
//		if(lst.size() > 0) return lst.get(0);
//		return null;
//	}
	
	@SuppressWarnings("unchecked")
	public DesignEstimatesDTO getFile(String code) {
		StringBuilder sql = new StringBuilder("select w.STATION_CODE stationVCC, cp.CAT_PROVINCE_ID provinceId,");
		sql.append(" cp.NAME provinceName, cs.DISTRICT_CODE districtName , cps.CAT_PARTNER_STATION_CODE stationVTNET, ");
		sql.append(" (CASE WHEN ccwit.STATION_TYPE = 0 THEN 'Macro' WHEN ccwit.STATION_TYPE = 1 THEN 'RRU' WHEN ccwit.STATION_TYPE = 2 THEN 'SMC' ELSE NULL END ) stationType, ");
		sql.append(" (CASE WHEN ccwit.PLACEMENT = 0 THEN 'Trên mái' WHEN ccwit.PLACEMENT = 1 THEN 'Dưới đất'  ELSE NULL END ) location, ");
		sql.append(" (CASE WHEN ccwit.PILLAR_TYPE = 1 THEN 'Cột cóc'  ");
		sql.append(" WHEN ccwit.PILLAR_TYPE = 2 THEN 'Cột ngụy trang'  ");
		sql.append(" WHEN ccwit.PILLAR_TYPE = 3 THEN 'Cột dây co'  ");
		sql.append(" WHEN ccwit.PILLAR_TYPE = 4 THEN 'Cột tự đứng'  ");
		sql.append(" WHEN ccwit.PILLAR_TYPE = 5 THEN 'Cột tự đứng (đốt 600x600)'  ");
	    sql.append(" WHEN ccwit.PILLAR_TYPE = 6 THEN 'Cột monopole' ");
	    sql.append(" WHEN ccwit.PILLAR_TYPE = 7 THEN 'Cột tự đứng thanh giằng'  ");
	    sql.append(" WHEN ccwit.PILLAR_TYPE = 8 THEN 'Cột thân thiện (cây dừa/cây thông)'  ");
	    sql.append(" WHEN ccwit.PILLAR_TYPE = 9 THEN 'Cột thân thiện (lồng đèn/cánh sen)' ");
	    sql.append(" WHEN ccwit.PILLAR_TYPE = 10 THEN 'Cột bê tông ly tâm (BTLT)' ");
	    sql.append(" WHEN ccwit.PILLAR_TYPE = 11 THEN 'Có cột sẵn/Không có cột' ");
	    sql.append(" ELSE NULL END ) pillarType, ");
	    sql.append(" ccwit.HIGHT_PILLAR pillarHight,ccwit.REGION terrain,ccwit.FOUNDATION fundamental, cs.ADDRESS stationAddress, ");
		sql.append(" cp.AREA_CODE area, wma.FILE_NAME name, wma.FILE_PATH filePath  from CTCT_COMS_OWNER.WO w ");
		sql.append(" LEFT JOIN CTCT_COMS_OWNER.WO_MAPPING_ATTACH wma ON w.ID = wma.WO_ID ");
		sql.append(" LEFT JOIN CTCT_CAT_OWNER.CAT_STATION cs ON w.STATION_CODE = cs.CODE ");
		sql.append(" left join CTCT_CAT_OWNER.CAT_PROVINCE cp ON cs.CAT_PROVINCE_ID = cp.CAT_PROVINCE_ID ");
		sql.append(" LEFT JOIN CTCT_CAT_OWNER.CAT_STATION_HOUSE csh ON cs.CAT_STATION_HOUSE_ID = csh.CAT_STATION_HOUSE_ID ");
		sql.append(" LEFT JOIN CTCT_CAT_OWNER.CAT_PARTNER_STATION cps ON cps.CAT_STATION_HOUSE_CODE = csh.CODE ");
		sql.append(" left join CTCT_IMS_OWNER.CNT_CONTRACT cnt on cnt.STATION_CODE_VCC=cs.CODE ");
		sql.append(" left join CTCT_IMS_OWNER.CNT_CONSTR_WORK_ITEM_TASK ccwit on cnt.CNT_CONTRACT_ID=ccwit.CNT_CONTRACT_ID ");
		sql.append(" where w.STATION_CODE = :code AND w.WO_TYPE_ID = 261 AND wma.STATUS = 1 AND wma.TYPE = 1 ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("code", code);	
		query.addScalar("stationVCC", new StringType());
		query.addScalar("provinceId", new LongType());
		query.addScalar("provinceName", new StringType());
		query.addScalar("districtName", new StringType());
		query.addScalar("stationVTNET", new StringType());
		query.addScalar("stationType", new StringType());
		query.addScalar("area", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("filePath", new StringType());
		query.addScalar("location", new StringType());
		query.addScalar("pillarType", new StringType());
		query.addScalar("pillarHight", new StringType());
		query.addScalar("terrain", new StringType());
		query.addScalar("fundamental", new StringType());
		query.addScalar("stationAddress", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(DesignEstimatesDTO.class));
		List<DesignEstimatesDTO> lst = query.list();
		if(lst.size() > 0) return lst.get(0);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CatStationDTO> doSearchStationVTNET(CatStationDTO obj) {
		StringBuilder sql = new StringBuilder("select CAT_PARTNER_STATION_ID catStationId, CAT_PARTNER_STATION_CODE code from CTCT_CAT_OWNER.CAT_PARTNER_STATION where ROWNUM < 50 ");
		if(obj.getCode() != null && !obj.getCode().equals("")) {
			sql.append(" AND (UPPER(CAT_PARTNER_STATION_CODE) like UPPER(:code))");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("catStationId", new LongType());
		query.addScalar("code", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));
		if (obj.getCode() != null && !obj.getCode().equals("")) {
			query.setParameter("code", "%" + obj.getCode() + "%");
		}
		List<CatStationDTO> lst = query.list();
		return lst;
	}

	public List<CatStationDTO> doSearchFile(UtilAttachDocumentDTO obj) {
		// TODO Auto-generated method stub
		return null;
	}
}

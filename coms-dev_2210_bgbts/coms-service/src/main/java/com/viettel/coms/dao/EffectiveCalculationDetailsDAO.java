package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.EffectiveCalculationDetailsBO;
import com.viettel.coms.dto.EffectiveCalculationDetailsDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.utils.ValidateUtils;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("effectiveCalculationDetailsDAO")
public class EffectiveCalculationDetailsDAO extends BaseFWDAOImpl<EffectiveCalculationDetailsBO, Long>{

	public EffectiveCalculationDetailsDAO() {
        this.model = new EffectiveCalculationDetailsBO();
    }

    public EffectiveCalculationDetailsDAO(Session session) {
        this.session = session;
    }
    
    public void deleteRecordEffective(Session session, Long woId) {
    	StringBuilder sql = new StringBuilder(" UPDATE EFFECTIVE_CALCULATION_DETAILS set STATUS=0 where WO_ID = :woId ");
    	StringBuilder sql2 = new StringBuilder(" UPDATE WO_MAPPING_ATTACH set STATUS=0 where WO_ID = :woId ");
    	SQLQuery query = session.createSQLQuery(sql.toString());
    	SQLQuery query2 = session.createSQLQuery(sql2.toString());
    	query.setParameter("woId", woId);
    	query2.setParameter("woId", woId);
    	query.executeUpdate();
    	query2.executeUpdate();
    	session.flush();
    	session.clear();
    }
    
    public List<EffectiveCalculationDetailsDTO> doSearch(EffectiveCalculationDetailsDTO obj) {
    	StringBuilder sql = new StringBuilder(" select ");
    	sql.append("EFFECTIVE_CALCULATION_DETAILS_ID effectiveCalculationDetailsId, ")
    	.append("AREA_CODE areaCode, ")
    	.append("PROVINCE_CODE provinceCode, ")
    	.append("STATION_CODE_VTN stationCodeVtn, ")
    	.append("STATION_CODE_VCC stationCodeVcc, ")
    	.append("ADDRESS address, ")
    	.append("STATION_TYPE stationType, ")
    	.append("MAI_DAT maiDat, ")
    	.append("DO_CAO_COT doCaoCot, ")
    	.append("LOAI_COT loaiCot, ")
    	.append("MONG_CO mongCo, ")
    	.append("LOAI_NHA loaiNha, ")
    	.append("TIEP_DIA tiepDia, ")
    	.append("DIEN_CNKT dienCnkt, ")
    	.append("SO_COT_DIEN soCotDien, ")
    	.append("VAN_CHUYEN_BO vanChuyenBo, ")
    	.append("THUE_ACQUY thueAcquy, ")
    	.append("GIA_THUE_MB_THUC_TE giaThueMbThucTe, ")
    	.append("GIA_THUE_MB_THEO_DINH_MUC giaThueMbTheoDinhMuc, ")
    	.append("CAPEX_COT capexCot, ")
    	.append("CAPEX_TIEP_DIA capexTiepDia, ")
    	.append("CAPEX_AC capexAc, ")
    	.append("CAPEX_PHONG_MAY capexPhongMay, ")
    	.append("TONG_CAPEX_HT tongCapexHt, ")
    	.append("VCC_CHAO_GIA_HT vccChaoGiaHt, ")
    	.append("VCC_CHAO_GIA_ACQUY vccChaoGiaAcquy, ")
    	.append("TONG_CONG tongCong, ")
    	.append("DESCRIPTION description, ")
    	.append("NGAY_GUI ngayGui, ")
    	.append("NGAY_HOAN_THANH ngayHoanThanh, ")
    	.append("NGUOI_LAP nguoiLap, ")
    	.append("CAPEX_HT_VCC capexHtVcc, ")
    	.append("NPV npv, ")
    	.append("IRR irr, ")
    	.append("THOI_GIAN_HV thoiGianHv, ")
    	.append("LNST_DT lnstDt, ")
    	.append("NPV_STRING npvString, ")
    	.append("IRR_STRING irrString, ")
    	.append("THOI_GIAN_HV_STRING thoiGianHvString, ")
    	.append("LNST_DT_STRING lnstDtString, ")
    	.append("CAPEX_TRUOC_VAT_STRING capexTruocVatString, ")
    	.append("CONCLUDE conclude, ")
    	.append("WO_ID woId, ")
    	.append("WO_CODE woCode, ")
    	.append("STATUS status ")
    	.append("FROM EFFECTIVE_CALCULATION_DETAILS WHERE STATUS !=0 AND CONCLUDE IS NOT NULL ");
    	
    	if(obj.getWoId()!=null) {
    		sql.append(" AND WO_ID = :woId ");
    	}
    	
    	if(obj.getFromDate()!=null) {
    		sql.append(" AND NGAY_HOAN_THANH >= :fromDate ");
    	}
    	
    	if(obj.getToDate()!=null) {
    		sql.append(" AND NGAY_HOAN_THANH <= :toDate ");
    	}
    	
    	if(StringUtils.isNotBlank(obj.getKeySearch())) {
    		sql.append(" AND ( UPPER(STATION_CODE_VTN) LIKE UPPER(:keySearch)"
    				+ " OR UPPER(STATION_CODE_VCC) LIKE UPPER(:keySearch)"
    				+ " OR UPPER(WO_CODE) LIKE UPPER(:keySearch)"
    				+ " OR UPPER(ADDRESS) LIKE UPPER(:keySearch) escape '&' ) ");
    	}
    	
    	if(StringUtils.isNotBlank(obj.getStatus())) {
    		if(obj.getStatus().equals("1")) {
    			sql.append(" AND UPPER(CONCLUDE) = UPPER('HIỆU QUẢ') ");
    		} else {
        		sql.append(" AND UPPER(CONCLUDE) = UPPER('KHÔNG HIỆU QUẢ') ");
        	}
    	} 
    	
    	if(StringUtils.isNotBlank(obj.getProvinceCode())) {
    		sql.append(" AND PROVINCE_CODE = :provinceCode ");
    	}
    	
    	StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
    	
        query.addScalar("effectiveCalculationDetailsId", new LongType());
        query.addScalar("areaCode", new StringType());
        query.addScalar("provinceCode", new StringType());
        query.addScalar("stationCodeVtn", new StringType());
        query.addScalar("stationCodeVcc", new StringType());
        query.addScalar("address", new StringType());
        query.addScalar("stationType", new StringType());
        query.addScalar("maiDat", new StringType());
        query.addScalar("doCaoCot", new DoubleType());
        query.addScalar("loaiCot", new StringType());
        query.addScalar("mongCo", new StringType());
        query.addScalar("loaiNha", new StringType());
        query.addScalar("tiepDia", new StringType());
        query.addScalar("dienCnkt", new StringType());
        query.addScalar("soCotDien", new StringType());
        query.addScalar("vanChuyenBo", new StringType());
        query.addScalar("thueAcquy", new StringType());
        query.addScalar("giaThueMbThucTe", new LongType());
        query.addScalar("giaThueMbTheoDinhMuc", new LongType());
        query.addScalar("capexCot", new LongType());
        query.addScalar("capexTiepDia", new LongType());
        query.addScalar("capexAc", new LongType());
        query.addScalar("capexPhongMay", new LongType());
        query.addScalar("tongCapexHt", new LongType());
        query.addScalar("vccChaoGiaHt", new DoubleType());
        query.addScalar("vccChaoGiaAcquy", new DoubleType());
        query.addScalar("tongCong", new LongType());
        query.addScalar("description", new StringType());
        query.addScalar("ngayGui", new DateType());
        query.addScalar("ngayHoanThanh", new DateType());
        query.addScalar("nguoiLap", new StringType());
        query.addScalar("capexHtVcc", new LongType());
        query.addScalar("npv", new LongType());
        query.addScalar("irr", new DoubleType());
        query.addScalar("thoiGianHv", new DoubleType());
        query.addScalar("lnstDt", new DoubleType());
        query.addScalar("conclude", new StringType());
        query.addScalar("woId", new LongType());
        query.addScalar("status", new StringType());
        query.addScalar("woCode", new StringType());
        query.addScalar("npvString", new StringType());
        query.addScalar("irrString", new StringType());
        query.addScalar("thoiGianHvString", new StringType());
        query.addScalar("lnstDtString", new StringType());
        query.addScalar("capexTruocVatString", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(EffectiveCalculationDetailsDTO.class));

        if(obj.getWoId()!=null) {
    		query.setParameter("woId", obj.getWoId());
    		queryCount.setParameter("woId", obj.getWoId());
    	}
        
        if(obj.getFromDate()!=null) {
    		query.setParameter("fromDate", obj.getFromDate());
    		queryCount.setParameter("fromDate", obj.getFromDate());
    	}
    	
    	if(obj.getToDate()!=null) {
    		query.setParameter("toDate", obj.getToDate());
    		queryCount.setParameter("toDate", obj.getToDate());
    	}
    	
    	if(StringUtils.isNotBlank(obj.getKeySearch())) {
    		query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
    		queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
    	}
        
    	if(StringUtils.isNotBlank(obj.getProvinceCode())) {
    		query.setParameter("provinceCode", obj.getProvinceCode());
    		queryCount.setParameter("provinceCode", obj.getProvinceCode());
    	}
    	
        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }

        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        List<WoDTO> lst = query.list();
        return query.list();
    }
    
    public void updateTthqResultWoMappingChecklist(Session session, Long woId, String result) {
    	StringBuilder sql = new StringBuilder(" UPDATE WO_MAPPING_CHECKLIST set TTHQ_RESULT=:result where WO_ID = :woId ");
    	SQLQuery query = session.createSQLQuery(sql.toString());
    	query.setParameter("woId", woId);
    	query.setParameter("result", result);
    	query.executeUpdate();
    	session.flush();
    	session.clear();
    }
    
    //Huypq-29122021-start
    public List<EffectiveCalculationDetailsDTO> getDetailStationByContract(Session session, EffectiveCalculationDetailsDTO obj) {
    	StringBuilder sql = new StringBuilder("SELECT " + 
    			"    province.AREA_CODE areaCode, " + 
    			"    province.code provinceCode, " + 
    			"    T6.CODE stationCodeVcc, " + 
    			"    T1.STATION_HTCT stationCodeVtn, " + 
    			"    T6.ADDRESS address, " + 
    			"    case T1.STATION_TYPE when '0' then 'Trạm Macro' "
    			+ " when '1' then 'Trạm RRU' "
    			+ " when '2' then 'Trạm SMC' "
    			+ " else '' end stationType, " + 
    			"    case T1.PLACEMENT when '0' then 'Trên mái' "
    			+ " when '1' then 'Dưới đất' "
    			+ " else '' end maiDat, " + 
    			"    T1.HIGHT_PILLAR doCaoCot, " + 
    			"    case T1.PILLAR_TYPE when '1' then 'Cột cóc' "
    			+ " when '2' then 'Cột ngụy trang' "
    			+ " when '3' then 'Cột dây co' "
    			+ " when '4' then 'Cột tự đứng' "
    			+ " when '5' then 'Cột tự đứng (đốt 600x600)' "
    			+ " when '6' then 'Cột monopole' "
    			+ " when '7' then 'Cột tự đứng thanh giằng' "
    			+ " when '8' then 'Cột thân thiện (cây dừa/cây thông)' "
    			+ " when '9' then 'Cột thân thiện (lồng đèn/cánh sen)' "
    			+ " when '10' then 'Cột Bê tông ly tâm (BTLT)' "
    			+ " when '11' then 'Cột có sẵn/Không có cột' "
    			+ " else '' end loaiCot, " + 
    			"    T1.FOUNDATION mongCo, " + 
    			"    case T1.HOUSE_TYPE when '1' then 'PM C04' "
    			+ " when '2' then 'PM C05' "
    			+ " when '3' then 'PM X04' "
    			+ " when '4' then 'PM Minishelter' "
    			+ " when '5' then 'PM Cải tạo (quây tôn/khung kính..)' "
    			+ " when '6' then 'Không có PM' "
    			+ " else '' end loaiNha, " + 
    			"    T1.EARTHING tiepDia, " + 
    			"    T1.POWER_LINE_LENGTH dienCnkt, " + 
    			"    T1.COUNT_PILLAR soCotDien, " + 
    			"    T1.DISTANCE_TRANSPORT vanChuyenBo, " + 
    			"    case T1.RENT_ACQUY when '0' then 'Có nguồn' "
    			+ " when '1' then 'Không nguồn' "
    			+ " else '' end thueAcquy, " + 
    			"    T1.PRICE_RENT_MB giaThueMbThucTe " + 
    			" FROM " + 
    			"    CNT_CONSTR_WORK_ITEM_TASK T1 " + 
    			"    INNER JOIN CONSTRUCTION T2 ON T1.CONSTRUCTION_ID = T2.CONSTRUCTION_ID AND T2.STATUS!=0 " + 
    			"    INNER JOIN CAT_STATION T6 ON T2.CAT_STATION_ID = T6.CAT_STATION_ID AND T6.STATUS!=0 " + 
    			"    INNER JOIN CAT_PROVINCE province ON T6.CAT_PROVINCE_ID = province.CAT_PROVINCE_ID AND province.STATUS!=0 " +
    			"	 INNER join WO wo on T6.CODE = wo.STATION_CODE " +
    			" WHERE " + 
    			"    1 = 1 " + 
    			"    AND T1.STATUS = 1" +
    			"    AND wo.ID=:woId ");
    	
    	StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = session.createSQLQuery(sql.toString());
        SQLQuery queryCount = session.createSQLQuery(sqlCount.toString());
    	
        query.addScalar("areaCode", new StringType());
        query.addScalar("provinceCode", new StringType());
        query.addScalar("stationCodeVtn", new StringType());
        query.addScalar("stationCodeVcc", new StringType());
        query.addScalar("address", new StringType());
        query.addScalar("stationType", new StringType());
        query.addScalar("maiDat", new StringType());
        query.addScalar("doCaoCot", new DoubleType());
        query.addScalar("loaiCot", new StringType());
        query.addScalar("mongCo", new StringType());
        query.addScalar("loaiNha", new StringType());
        query.addScalar("tiepDia", new StringType());
        query.addScalar("dienCnkt", new StringType());
        query.addScalar("soCotDien", new StringType());
        query.addScalar("vanChuyenBo", new StringType());
        query.addScalar("thueAcquy", new StringType());
        query.addScalar("giaThueMbThucTe", new LongType());
        
        query.setResultTransformer(Transformers.aliasToBean(EffectiveCalculationDetailsDTO.class));

		query.setParameter("woId", obj.getWoId());
		queryCount.setParameter("woId", obj.getWoId());

        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }

        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        List<WoDTO> lst = query.list();
        session.flush();
        return query.list();
    }
    //Huy-end
}

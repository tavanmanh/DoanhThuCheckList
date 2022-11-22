package com.viettel.coms.dao;

//Duonghv13 start-16/08/2021//
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.bo.TotalMonthPlanHTCTBO;
import com.viettel.coms.bo.TotalMonthPlanOSBO;
import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.RpKHBTSDTO;
import com.viettel.coms.dto.TotalMonthPlanHTCTDTO;
import com.viettel.coms.dto.TotalMonthPlanOSDTO;
import com.viettel.coms.dto.TotalMonthPlanOSSimpleDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;

@Repository("totalMonthPlanHTCTDAO")
public class TotalMonthPlanHTCTDAO extends BaseFWDAOImpl<TotalMonthPlanHTCTBO, Long>{
	public TotalMonthPlanHTCTDAO() {
        this.model = new TotalMonthPlanHTCTBO();
    }

    public TotalMonthPlanHTCTDAO(Session session) {
        this.session = session;
    }
    
    public List<TotalMonthPlanHTCTDTO> doSearch(TotalMonthPlanHTCTDTO obj) {
        StringBuilder sql = new StringBuilder("SELECT T1.TOTAL_MONTH_PLAN_DTHT_ID totalMonthPlanDTHTId, " 
        		+ "T1.MONTH month, "
        		+ "T1.YEAR year, " 
        		+ "T1.MONTH || '/' || T1.YEAR monthYear, " 
        		+ "T1.PROVINCE_CODE provinceCode, "
        		
				+ "T1.STATION_CODE_VCC stationCodeVCC, "
				+ "T1.STATION_CODE_VTNET stationCodeVTNET, "
        		
        		+ "T1.KHOI_CONG soluong_KC, "
        		+ "T1.DONG_BO soluong_DB, "
        		+ "T1.PHAT_SONG soluong_PS, "
        		+ "T1.THUE_MAT_BANG soluong_TMB, "
        		+ "T1.HSHC soluong_HSHC, "
        		+ "T1.HSHC_DTHT soluong_HSHC_DTHT, "
        		+ "T1.DOANH_THU tram_toDoanhThu, "
        		+ "T1.TKDT soluong_TKDT, "
                + "T1.INSERT_TIME insertTime, "
                + "T2.SYS_USER_ID createdBy, "
        		+ "T2.EMAIL createdByName, "  
        		+ "T1.UPDATED_TIME updatedTime, " 
        		+ "T3.SYS_USER_ID updatedBy, "
        		+ "T3.EMAIL updatedByName "  
        		+ "FROM TOTAL_MONTH_PLAN_DTHT T1 "
        		+ "LEFT JOIN SYS_USER T2 ON T1.CREATED_BY = T2.SYS_USER_ID "
        		+ "LEFT JOIN SYS_USER T3 ON T1.UPDATED_BY = T3.SYS_USER_ID "
        		+ "WHERE 1=1");

        if (StringUtils.isNotBlank(obj.getProvinceCode())) {
            sql.append(
                    " and upper(T1.PROVINCE_CODE) LIKE upper(:provinceCode)");
        }
        if (obj.getMonthYear() != null) {
        	sql.append(" AND upper(T1.MONTH || '/' || T1.YEAR) LIKE upper(:monthYear)");
		}
        if (StringUtils.isNotBlank(obj.getCreatedByName())){
        	sql.append(" AND upper(T2.EMAIL)  LIKE upper(:createdByName) ");
		}
        if (StringUtils.isNotBlank(obj.getStationCodeVCC())) {
            sql.append(" and upper(T1.STATION_CODE_VCC) LIKE upper(:stationCodeVCC)");
        }
        
        if (null != obj.getStatus()) {
        	sql.append("AND T1.STATUS like (:status)");
		}

        sql.append(" ORDER BY T1.PROVINCE_CODE");
        
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        
        StringBuilder sqlSum = new StringBuilder("SELECT ");
		sqlSum.append(" sum(soluong_KC) sumsoluong_KC, ");
		sqlSum.append(" sum(soluong_DB) sumsoluong_DB, ");
		sqlSum.append(" sum(soluong_PS) sumsoluong_PS, ");
		sqlSum.append(" sum(soluong_TMB) sumsoluong_TMB, ");
		sqlSum.append(" sum(soluong_HSHC) sumsoluong_HSHC, ");
		sqlSum.append(" sum(soluong_HSHC_DTHT) sumsoluong_HSHC_DTHT, ");
		sqlSum.append(" sum(tram_toDoanhThu) sumtram_toDoanhThu,  ");
		sqlSum.append(" sum(soluong_TKDT) sumsoluong_TKDT FROM (");
		sqlSum.append(sql.toString());
		sqlSum.append(")");
		
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery querySum = getSession().createSQLQuery(sqlSum.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if (StringUtils.isNotBlank(obj.getProvinceCode())) {
            query.setParameter("provinceCode", "%" + obj.getProvinceCode() + "%");
            queryCount.setParameter("provinceCode", "%" + obj.getProvinceCode() + "%");
            querySum.setParameter("provinceCode", "%" + obj.getProvinceCode() + "%");
        }
        if (null != obj.getMonthYear()) {
			query.setParameter("monthYear","%" + obj.getMonthYear()+"%");
			queryCount.setParameter("monthYear","%" + obj.getMonthYear()+"%");
			querySum.setParameter("monthYear","%" + obj.getMonthYear()+"%");
		}
        if (StringUtils.isNotBlank(obj.getCreatedByName())){
			query.setParameter("createdByName","%" +obj.getCreatedByName()+"%");
			queryCount.setParameter("createdByName","%" +obj.getCreatedByName()+"%");
			querySum.setParameter("createdByName","%" +obj.getCreatedByName()+"%");
		}
        if (null != obj.getStatus()) {
			query.setParameter("status","%" + obj.getStatus() +"%");
			queryCount.setParameter("status","%" + obj.getStatus() +"%");
			querySum.setParameter("status","%" + obj.getStatus() +"%");
		}
        if (StringUtils.isNotBlank(obj.getStationCodeVCC())) {
			query.setParameter("stationCodeVCC","%" +obj.getStationCodeVCC()+"%");
			queryCount.setParameter("stationCodeVCC","%" +obj.getStationCodeVCC()+"%");
			querySum.setParameter("stationCodeVCC","%" +obj.getStationCodeVCC()+"%");
        }
        query.addScalar("totalMonthPlanDTHTId", new LongType());
        query.addScalar("month", new StringType());
        query.addScalar("year", new LongType());
        query.addScalar("monthYear", new StringType());
        
        query.addScalar("provinceCode", new StringType());
        query.addScalar("stationCodeVCC", new StringType());
        query.addScalar("stationCodeVTNET", new StringType());
 
        query.addScalar("soluong_KC", new LongType());
        query.addScalar("soluong_DB", new LongType());
        query.addScalar("soluong_PS", new LongType());
        query.addScalar("soluong_TMB", new LongType());
        query.addScalar("soluong_HSHC", new LongType());
        query.addScalar("soluong_HSHC_DTHT", new LongType());
        query.addScalar("tram_toDoanhThu", new LongType());
        query.addScalar("soluong_TKDT", new LongType());
        query.addScalar("insertTime", new DateType());
        query.addScalar("createdBy", new LongType());
        query.addScalar("createdByName", new StringType());
        query.addScalar("updatedTime", new DateType());
        query.addScalar("updatedBy", new LongType());
        query.addScalar("updatedByName", new StringType());
        
         //query sum//
        
		 querySum.addScalar("sumsoluong_KC", new LongType());
		 querySum.addScalar("sumsoluong_DB", new LongType());
		 querySum.addScalar("sumsoluong_PS", new LongType());
		 querySum.addScalar("sumsoluong_TMB", new LongType());
		 querySum.addScalar("sumsoluong_HSHC", new LongType());
		 querySum.addScalar("sumsoluong_HSHC_DTHT", new LongType());
		 querySum.addScalar("sumtram_toDoanhThu", new LongType());
		 querySum.addScalar("sumsoluong_TKDT", new LongType());
       
        
        query.setResultTransformer(Transformers.aliasToBean(TotalMonthPlanHTCTDTO.class));
        if (obj.getPage() != null && obj.getPageSize() != null) {
        	query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
        	query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        List<TotalMonthPlanHTCTDTO> lstDto = query.list();
        
        if(lstDto.size() > 0) {
        	List<Object[]> rs = querySum.list();
            for (Object[] objects : rs) {
            	lstDto.get(0).setSumsoluong_KC((Long) objects[0]);
            	lstDto.get(0).setSumsoluong_DB((Long) objects[1]);
            	lstDto.get(0).setSumsoluong_PS((Long) objects[2]);
            	lstDto.get(0).setSumsoluong_TMB((Long) objects[3]);
            	lstDto.get(0).setSumsoluong_HSHC((Long) objects[4]);
            	lstDto.get(0).setSumsoluong_HSHC_DTHT((Long) objects[5]);
            	lstDto.get(0).setSumtram_toDoanhThu((Long) objects[6]);
            	lstDto.get(0).setSumsoluong_TKDT((Long) objects[7]);	
            }
        }
		return lstDto;
    }
    
    public TotalMonthPlanHTCTDTO findbyProvinceCodeAndMonth(String code,String month,String year,String stationCodeVCC) {
		StringBuilder stringBuilder = new StringBuilder("SELECT T1.TOTAL_MONTH_PLAN_DTHT_ID totalMonthPlanDTHTId, " 
		        		+ "T1.MONTH month, "
		        		+ "T1.YEAR year, " 
		        		+ "T1.MONTH || '/' || T1.YEAR monthYear, " 
		        		+ "T1.PROVINCE_CODE provinceCode, "
		        		+ "T1.STATION_CODE_VCC stationCodeVCC, "
						+ "T1.STATION_CODE_VTNET stationCodeVTNET, "
		        		+ "T1.KHOI_CONG soluong_KC, "
		        		+ "T1.DONG_BO soluong_DB, "
		        		+ "T1.PHAT_SONG soluong_PS, "
		        		+ "T1.THUE_MAT_BANG soluong_TMB, "
		        		+ "T1.HSHC soluong_HSHC, "
		        		+ "T1.HSHC_DTHT soluong_HSHC_DTHT, "
		        		+ "T1.DOANH_THU tram_toDoanhThu, "
		        		+ "T1.TKDT soluong_TKDT, "
		                + "T1.INSERT_TIME insertTime, "
		                + "T2.SYS_USER_ID createdBy, "
		        		+ "T2.EMAIL createdByName, "  
		        		+ "T1.UPDATED_TIME updatedTime, " 
		        		+ "T3.SYS_USER_ID updatedBy, "
		        		+ "T3.EMAIL updatedByName "  
		        		+ "FROM TOTAL_MONTH_PLAN_DTHT T1 "
		        		+ "LEFT JOIN SYS_USER T2 ON T1.CREATED_BY = T2.SYS_USER_ID "
		        		+ "LEFT JOIN SYS_USER T3 ON T1.UPDATED_BY = T3.SYS_USER_ID "
		        		+ "WHERE 1=1 AND T1.STATUS LIKE '1' "
						+ "AND upper(T1.PROVINCE_CODE) like upper(:provinceCode) " 
						+ "AND upper(T1.MONTH ) LIKE upper(:month) "
						+ "AND TO_CHAR(T1.YEAR) LIKE upper(:year) "
						+ "AND upper(T1.STATION_CODE_VCC) like upper(:stationCodeVCC) " );
    	
    	SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
    	
    	 query.addScalar("totalMonthPlanDTHTId", new LongType());
         query.addScalar("month", new StringType());
         query.addScalar("year", new LongType());
         query.addScalar("monthYear", new StringType());
         query.addScalar("provinceCode", new StringType());
         query.addScalar("stationCodeVCC", new StringType());
         query.addScalar("stationCodeVTNET", new StringType());
         query.addScalar("soluong_KC", new LongType());
         query.addScalar("soluong_DB", new LongType());
         query.addScalar("soluong_PS", new LongType());
         query.addScalar("soluong_TMB", new LongType());
         query.addScalar("soluong_HSHC", new LongType());
         query.addScalar("soluong_HSHC_DTHT", new LongType());
         query.addScalar("tram_toDoanhThu", new LongType());
         query.addScalar("soluong_TKDT", new LongType());
         query.addScalar("insertTime", new DateType());
         query.addScalar("createdBy", new LongType());
         query.addScalar("createdByName", new StringType());
         query.addScalar("updatedTime", new DateType());
         query.addScalar("updatedBy", new LongType());
         query.addScalar("updatedByName", new StringType());
        
		query.setParameter("provinceCode", "%" + code + "%");  
		query.setParameter("month", "%" + month + "%"); 
		query.setParameter("year", "%" + year + "%" ); 
		query.setParameter("stationCodeVCC", "%" + stationCodeVCC + "%");  
		query.setResultTransformer(Transformers.aliasToBean(TotalMonthPlanHTCTDTO.class));    	

		return (TotalMonthPlanHTCTDTO) query.uniqueResult();
	}
    
    @SuppressWarnings("unchecked")
	public TotalMonthPlanHTCTDTO getById(Long id) {
    	StringBuilder stringBuilder = new StringBuilder("SELECT T1.TOTAL_MONTH_PLAN_DTHT_ID totalMonthPlanDTHTId, " 
    	        		+ "T1.MONTH month, "
    	        		+ "T1.YEAR year, " 
    	        		+ "T1.PROVINCE_CODE provinceCode, "
    	        		+ "T1.STATION_CODE_VCC stationCodeVCC, "
    					+ "T1.STATION_CODE_VTNET stationCodeVTNET, "
    	        		+ "T1.KHOI_CONG soluong_KC, "
    	        		+ "T1.DONG_BO soluong_DB, "
    	        		+ "T1.PHAT_SONG soluong_PS, "
    	        		+ "T1.THUE_MAT_BANG soluong_TMB, "
    	        		+ "T1.HSHC soluong_HSHC, "
    	        		+ "T1.HSHC_DTHT soluong_HSHC_DTHT, "
    	        		+ "T1.DOANH_THU tram_toDoanhThu, "
    	        		+ "T1.TKDT soluong_TKDT, "
    	                + "T1.INSERT_TIME insertTime, "
    	        		+ "T2.EMAIL createdBy, "  
    	        		+ "T1.UPDATED_TIME updatedTime, " 
    	        		+ "T3.EMAIL updatedBy "  
    	        		+ "FROM TOTAL_MONTH_PLAN_DTHT T1 "
    	        		+ "LEFT JOIN SYS_USER T2 ON T1.CREATED_BY = T2.SYS_USER_ID "
    	        		+ "LEFT JOIN SYS_USER T3 ON T1.UPDATED_BY = T3.SYS_USER_ID "
    	        		+ "WHERE 1=1 AND T1.STATUS LIKE '1' AND "	
		    	        + "T1.TOTAL_MONTH_PLAN_DTHT_ID = :totalMonthPlanDTHTId ");
    	
    	SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
    	query.addScalar("totalMonthPlanDTHTId", new LongType());
        query.addScalar("month", new StringType());
        query.addScalar("year", new LongType());
        query.addScalar("provinceCode", new StringType());
        query.addScalar("stationCodeVCC", new StringType());
        query.addScalar("stationCodeVTNET", new StringType());
        query.addScalar("soluong_KC", new LongType());
        query.addScalar("soluong_DB", new LongType());
        query.addScalar("soluong_PS", new LongType());
        query.addScalar("soluong_TMB", new LongType());
        query.addScalar("soluong_HSHC", new LongType());
        query.addScalar("soluong_HSHC_DTHT", new LongType());
        query.addScalar("tram_toDoanhThu", new LongType());
        query.addScalar("soluong_TKDT", new LongType());
        query.addScalar("insertTime", new DateType());
        query.addScalar("createdBy", new LongType());
        query.addScalar("updatedTime", new DateType());
        query.addScalar("updatedBy", new LongType());
        
		query.setParameter("totalMonthPlanDTHTId", id);
		query.setResultTransformer(Transformers.aliasToBean(TotalMonthPlanHTCTDTO.class));
		return (TotalMonthPlanHTCTDTO) query.uniqueResult();
	}

	public List<TotalMonthPlanHTCTDTO> getAllTotalMonthPlan(TotalMonthPlanHTCTDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT T1.TOTAL_MONTH_PLAN_DTHT_ID totalMonthPlanDTHTId, " 
		        		+ "T1.MONTH || '/' || T1.YEAR monthYear, " 
		        		+ "T1.PROVINCE_CODE provinceCode, "
		        		+ "UPPER(T1.STATION_CODE_VCC) stationCodeVCC "
		        		+ "FROM TOTAL_MONTH_PLAN_DTHT T1 "
		        		+ "WHERE 1=1 AND  T1.STATUS LIKE '1'");
		if (null != obj.getListmonthYear() && obj.getListmonthYear().size()>0) {
			sql.append(" AND T1.MONTH || '/' || T1.YEAR  in (:listmonthYear) ");
    	}
		if (null != obj.getListprovinceCode() && obj.getListprovinceCode().size()>0) {
			sql.append(" AND T1.PROVINCE_CODE  in (:listprovinceCode) ");
    	}

        sql.append("ORDER BY T1.PROVINCE_CODE");
        
        SQLQuery query = getSession().createSQLQuery(sql.toString());
     
        query.addScalar("totalMonthPlanDTHTId", new LongType()); 
        query.addScalar("monthYear", new StringType());
        query.addScalar("provinceCode", new StringType());
        query.addScalar("stationCodeVCC", new StringType());
        
        if (null != obj.getListmonthYear() && obj.getListmonthYear().size()>0 ) {
			query.setParameterList("listmonthYear",obj.getListmonthYear());
		}
        if (null != obj.getListprovinceCode() && obj.getListprovinceCode().size()>0) {
        	query.setParameterList("listprovinceCode",obj.getListprovinceCode());
    	}
        
        query.setResultTransformer(Transformers.aliasToBean(TotalMonthPlanHTCTDTO.class));
        return query.list();
	}

	
	public List<CatStationDTO> getAllStationVCCHTCT(TotalMonthPlanHTCTDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder("SELECT CT.CAT_STATION_ID catStationId, "
                + "CT.NAME name, "
                + "CT.STATUS status, "
                + "CT.CODE code,CT.ADDRESS address,CP.CODE catProvinceName"
                + " FROM CAT_STATION CT"
                + " LEFT JOIN CAT_PROVINCE CP ON CT.CAT_PROVINCE_ID = CP.CAT_PROVINCE_ID"
                + " where CT.status = 1 and CT.type_htct = 1");
		
		if (null != obj.getListprovinceCode() && obj.getListprovinceCode().size()>0) {
			stringBuilder.append(" AND CP.CODE  in (:listprovinceCode) ");
    	}
		
        if (null != obj.getStationCodeVCC()) {
            stringBuilder.append(" AND (upper(CT.NAME) like upper(:stationCodeVCC) or upper(CT.CODE) like upper(:stationCodeVCC) escape '&')");
        }
        stringBuilder.append(" ORDER BY CP.CODE ");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM ( ");
        sqlCount.append(stringBuilder.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("catStationId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("address", new StringType());
        query.addScalar("catProvinceName", new StringType());

        if (null != obj.getStationCodeVCC()) {
            query.setParameter("stationCodeVCC", "%" + obj.getStationCodeVCC() + "%");
            queryCount.setParameter("stationCodeVCC", "%" + obj.getStationCodeVCC() + "%");
        }
        if (null != obj.getListprovinceCode() && obj.getListprovinceCode().size()>0) {
        	query.setParameterList("listprovinceCode",obj.getListprovinceCode());
        	queryCount.setParameterList("listprovinceCode", obj.getListprovinceCode());
    	}

        query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));

        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
	}

    
  //Duonghv13 end-16/08/2021//
}

package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.CatStationBO;
import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.ConstructionDetailDTO;
import com.viettel.coms.dto.WoSimpleConstructionDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hailh10
 */
@Repository("catStationDAO")
@Transactional
public class CatStationDAO extends BaseFWDAOImpl<CatStationBO, Long> {

    public CatStationDAO() {
        this.model = new CatStationBO();
    }

    public CatStationDAO(Session session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public List<CatStationDTO> doSearch(CatStationDTO criteria) {
        StringBuilder stringBuilder = new StringBuilder("select totalRecord ");
        stringBuilder.append(",T1.TYPE type ");
        stringBuilder.append(",T1.IS_SYNONIM isSynonim ");
        stringBuilder.append(",T1.DESCRIPTION description ");
        stringBuilder.append(",T1.CR_NUMBER crNumber ");
        stringBuilder.append(",T1.LATITUDE latitude ");
        stringBuilder.append(",T1.LONGITUDE longitude ");
        stringBuilder.append(",T1.CAT_STATION_ID catStationId ");
        stringBuilder.append(",nvl(T1.NAME,'') name ");
        stringBuilder.append(",T1.CODE code ");
        stringBuilder.append(",T1.ADDRESS address ");
        stringBuilder.append(",T1.STATUS status ");
        stringBuilder.append(",T1.START_POINT_ID startPointId ");
        stringBuilder.append(",T1.END_POINT_ID endPointId ");
        stringBuilder.append(",T1.LINE_TYPE_ID lineTypeId ");
        stringBuilder.append(",T1.LINE_LENGTH lineLength ");
        stringBuilder.append(",T1.EMISSION_DATE emissionDate ");
        stringBuilder.append(",T1.SCOPE scope ");
        stringBuilder.append(",T1.SCOPE_NAME scopeName ");
        stringBuilder.append(",T1.START_POINT_TYPE startPointType ");
        stringBuilder.append(",T1.END_POINT_TYPE endPointType ");
        stringBuilder.append(",T1.PARENT_ID parentId ");
        stringBuilder.append(",T1.DISTANCE_ODD distanceOdd ");
        stringBuilder.append(",T1.AREA_LOCATION areaLocation ");
        stringBuilder.append(",T1.CREATED_DATE createdDate ");
        stringBuilder.append(",T1.UPDATED_DATE updatedDate ");
        stringBuilder.append(",T1.CREATED_USER createdUser ");
        stringBuilder.append(",T1.UPDATED_USER updatedUser ");
        stringBuilder.append(",T1.CAT_STATION_TYPE_ID catStationTypeId ");
        stringBuilder.append(",T1.CAT_PROVINCE_ID catProvinceId ");
        stringBuilder.append(",T1.CAT_STATION_HOUSE_ID catStationHouseId ");

        stringBuilder.append("FROM CAT_STATION T1 ");
        stringBuilder.append("WHERE T1.STATUS != 0 ");

        if (StringUtils.isNotEmpty(criteria.getType())) {
            stringBuilder.append(
                    "AND( UPPER(T1.Code) LIKE UPPER(:keySearch) or UPPER(T1.Name) LIKE UPPER(:keySearch)   ESCAPE '\\' )");
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(stringBuilder.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("type", new StringType());
        query.addScalar("isSynonim", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("crNumber", new StringType());
        query.addScalar("latitude", new DoubleType());
        query.addScalar("longitude", new DoubleType());
        query.addScalar("catStationId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("address", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("startPointId", new LongType());
        query.addScalar("endPointId", new LongType());
        query.addScalar("lineTypeId", new LongType());
        query.addScalar("lineLength", new LongType());
        query.addScalar("emissionDate", new DateType());
        query.addScalar("scope", new LongType());
        query.addScalar("scopeName", new StringType());
        query.addScalar("startPointType", new StringType());
        query.addScalar("endPointType", new StringType());
        query.addScalar("parentId", new LongType());
        query.addScalar("distanceOdd", new LongType());
        query.addScalar("areaLocation", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("updatedDate", new DateType());
        query.addScalar("createdUser", new LongType());
        query.addScalar("updatedUser", new LongType());
        query.addScalar("catStationTypeId", new LongType());
        query.addScalar("catProvinceId", new LongType());
        query.addScalar("catStationHouseId", new LongType());

        if (StringUtils.isNotEmpty(criteria.getType())) {
            query.setParameter("keySearch", "%" + criteria.getKeySearch() + "%");
            queryCount.setParameter("keySearch", "%" + criteria.getKeySearch() + "%");
        }

        query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));

        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            query.setFirstResult((criteria.getPage().intValue() - 1) * criteria.getPageSize().intValue());
            query.setMaxResults(criteria.getPageSize().intValue());
        }
        criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }

    public CatStationDTO findByValue(String value) {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        stringBuilder.append("T1.TYPE type ");
        stringBuilder.append(",T1.IS_SYNONIM isSynonim ");
        stringBuilder.append(",T1.DESCRIPTION description ");
        stringBuilder.append(",T1.CR_NUMBER crNumber ");
        stringBuilder.append(",T1.LATITUDE latitude ");
        stringBuilder.append(",T1.LONGITUDE longitude ");
        stringBuilder.append(",T1.CAT_STATION_ID catStationId ");
        stringBuilder.append(",T1.NAME name ");
        stringBuilder.append(",T1.CODE code ");
        stringBuilder.append(",T1.ADDRESS address ");
        stringBuilder.append(",T1.STATUS status ");
        stringBuilder.append(",T1.START_POINT_ID startPointId ");
        stringBuilder.append(",T1.END_POINT_ID endPointId ");
        stringBuilder.append(",T1.LINE_TYPE_ID lineTypeId ");
        stringBuilder.append(",T1.LINE_LENGTH lineLength ");
        stringBuilder.append(",T1.EMISSION_DATE emissionDate ");
        stringBuilder.append(",T1.SCOPE scope ");
        stringBuilder.append(",T1.SCOPE_NAME scopeName ");
        stringBuilder.append(",T1.START_POINT_TYPE startPointType ");
        stringBuilder.append(",T1.END_POINT_TYPE endPointType ");
        stringBuilder.append(",T1.PARENT_ID parentId ");
        stringBuilder.append(",T1.DISTANCE_ODD distanceOdd ");
        stringBuilder.append(",T1.AREA_LOCATION areaLocation ");
        stringBuilder.append(",T1.CREATED_DATE createdDate ");
        stringBuilder.append(",T1.UPDATED_DATE updatedDate ");
        stringBuilder.append(",T1.CREATED_USER createdUser ");
        stringBuilder.append(",T1.UPDATED_USER updatedUser ");
        stringBuilder.append(",T1.CAT_STATION_TYPE_ID catStationTypeId ");
        stringBuilder.append(",T1.CAT_PROVINCE_ID catProvinceId ");
        stringBuilder.append(",T1.CAT_STATION_HOUSE_ID catStationHouseId ");

        stringBuilder.append("FROM CAT_STATION T1 ");
        stringBuilder.append("WHERE T1.IS_DELETED = 'N' AND upper(T1.VALUE) = upper(:value)");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        query.addScalar("type", new StringType());
        query.addScalar("isSynonim", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("crNumber", new StringType());
        query.addScalar("latitude", new DoubleType());
        query.addScalar("longitude", new DoubleType());
        query.addScalar("catStationId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("address", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("startPointId", new LongType());
        query.addScalar("endPointId", new LongType());
        query.addScalar("lineTypeId", new LongType());
        query.addScalar("lineLength", new LongType());
        query.addScalar("emissionDate", new DateType());
        query.addScalar("scope", new LongType());
        query.addScalar("scopeName", new StringType());
        query.addScalar("startPointType", new StringType());
        query.addScalar("endPointType", new StringType());
        query.addScalar("parentId", new LongType());
        query.addScalar("distanceOdd", new LongType());
        query.addScalar("areaLocation", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("updatedDate", new DateType());
        query.addScalar("createdUser", new LongType());
        query.addScalar("updatedUser", new LongType());
        query.addScalar("catStationTypeId", new LongType());
        query.addScalar("catProvinceId", new LongType());
        query.addScalar("catStationHouseId", new LongType());

        query.setParameter("value", value);
        query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));

        return (CatStationDTO) query.uniqueResult();
    }

    public List<CatStationDTO> getForAutoComplete(CatStationDTO obj) {
        StringBuilder sql = new StringBuilder(
                "SELECT ct.CAT_STATION_ID catStationId, ct.NAME name, ct.CODE code, ct.ADDRESS address "
                        + " FROM CAT_STATION ct WHERE STATUS != 0");

        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            sql.append(" AND (upper(ct.NAME) LIKE upper(:keySearch) OR upper(ct.CODE) LIKE upper(:keySearch)) ");
        }

        //VietNT_20181207_start
        if (null != obj.getCatStationHouseId()) {
            sql.append(" AND ct.CAT_STATION_HOUSE_ID LIKE :catStationHouseId ");
        }
        //VietNT_20190105_start
        if (null != obj.getCatProvinceId()) {
            sql.append("AND ct.CAT_PROVINCE_ID = :catProvinceId ");
        }
        //VietNT_end

        sql.append(" ORDER BY ct.CODE");

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("catStationId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("address", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));

        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
            queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
        }

        //VietNT_20181207_start
        if (null != obj.getCatStationHouseId()) {
            query.setParameter("catStationHouseId", obj.getCatStationHouseId());
            queryCount.setParameter("catStationHouseId", obj.getCatStationHouseId());
        }
        //VietNT_20190105_start
        if (null != obj.getCatProvinceId()) {
            query.setParameter("catProvinceId", obj.getCatProvinceId());
            queryCount.setParameter("catProvinceId", obj.getCatProvinceId());
        }
        //VietNT_end

        query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
        query.setMaxResults(obj.getPageSize().intValue());
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public CatStationDTO getById(Long id) {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");

        stringBuilder.append("T1.CAT_STATION_ID catStationId ");
        stringBuilder.append(",T1.NAME name ");
        stringBuilder.append(",T1.CODE code ");

        stringBuilder.append("FROM CAT_STATION T1 ");
        stringBuilder.append("WHERE T1.CAT_STATION_ID = :catStationId ");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("catStationId", new LongType());

        query.setParameter("catStationId", id);
        query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));

        return (CatStationDTO) query.uniqueResult();
    }

    //VietNT_20181206_start
    public List<CatStationDTO> getCatStationHouseForAutoComplete(CatStationDTO obj) {
        StringBuilder sql = new StringBuilder(
                "SELECT  ch.CAT_STATION_HOUSE_ID catStationHouseId, " +
                        "ch.CODE code, " +
                        "ch.ADDRESS address " +
                        "FROM CAT_STATION_HOUSE ch WHERE ch.STATUS != 0");

        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            sql.append(" AND (" +
                    "upper(ch.ADDRESS) LIKE upper(:keySearch) " +
                    "OR upper(ch.CODE) LIKE upper(:keySearch) escape '&') ");
        }

        sql.append(" ORDER BY ch.CODE");

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());


        query.addScalar("catStationHouseId", new LongType());
        query.addScalar("address", new StringType());
        query.addScalar("code", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));

        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
            queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
        }

        query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize());
        query.setMaxResults(obj.getPageSize());
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }
    //VietNT_end

    //HuyPQ-20190314
    public List<ConstructionDetailDTO> getForAutoCompleteHouse(ConstructionDetailDTO obj) {
        StringBuilder sql = new StringBuilder(
                "SELECT ct.CAT_STATION_HOUSE_ID catStationHouseId, ct.CODE catStationHouseCode, ct.ADDRESS addressStationHouse "
                        + " FROM CAT_STATION_HOUSE ct WHERE STATUS != 0");

        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            sql.append(" AND (upper(ct.CODE) LIKE upper(:keySearch)) ");
        }

        sql.append(" ORDER BY ct.CODE");

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("catStationHouseId", new LongType());
        query.addScalar("catStationHouseCode", new StringType());
        query.addScalar("addressStationHouse", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(ConstructionDetailDTO.class));

        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
            queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
        }

        query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
        query.setMaxResults(obj.getPageSize().intValue());
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }
    //Huy-end

    //Thinh-start
    public int checkStationTypeByTypeCode(Long stationId, String code) {
        StringBuilder sqlCount = new StringBuilder(
                "SELECT COUNT(*) FROM CAT_STATION CS " +
                        "INNER JOIN CAT_STATION_TYPE CT ON CS.CAT_STATION_TYPE_ID=CT.CAT_STATION_TYPE_ID " +
                        "WHERE CS.CAT_STATION_ID=:stationId AND CT.CODE=:code");
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        queryCount.setParameter("stationId", stationId);
        queryCount.setParameter("code", code);
        return ((BigDecimal) queryCount.uniqueResult()).intValue();
    }
    //Thinh-end

    public CatStationBO getByStationCode(String code) {
        try {
            String sql = "select CAT_STATION_ID catStationId from cat_station where code = :code ";

            SQLQuery query = getSession().createSQLQuery(sql);
            query.setParameter("code", code);
            query.addScalar("catStationId", new LongType());

            query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));
            List<CatStationDTO> result = query.list();

            if (result.size() > 0) return getOneRaw(result.get(0).getCatStationId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public CatStationBO getByStationId(Long stationId) {
        try {
            String sql = "select CAT_STATION_ID catStationId from cat_station where CAT_STATION_ID = :stationId ";

            SQLQuery query = getSession().createSQLQuery(sql);
            query.setParameter("stationId", stationId);
            query.addScalar("catStationId", new LongType());

            query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));
            List<CatStationDTO> result = query.list();

            if (result.size() > 0) return getOneRaw(result.get(0).getCatStationId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public CatStationBO getOneRaw(long catStationId) {
        return this.get(CatStationBO.class, catStationId);
    }
    
    public List<CatStationDTO> getCodeStationForImport(List<String> lstStation) {
		StringBuilder sql = new StringBuilder(
				" SELECT DISTINCT cs.CAT_STATION_ID catStationId, "
				+ "cs.CODE code, "
				+ "nvl(TYPE_HTCT,'0') typeHtct, "
				+ "nvl(RENT_STATUS,0) rentStatus "
				+ "FROM CAT_STATION cs "
				+ " WHERE cs.TYPE =1 "
				+ "AND cs.STATUS = 1 "
				+ "AND nvl(type_htct,0) = 1 "
				+ "AND RENT_STATUS in (0,2) ");
		if (lstStation.size() > 0) {
			sql.append(" and cs.CODE in :lstStation ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("catStationId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("typeHtct", new StringType());
		query.addScalar("rentStatus", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));
		if (lstStation.size() > 0) {
			query.setParameterList("lstStation", lstStation);
		}
		return query.list();
	}
    
    public List<CatStationDTO> checkDataDbImportTrStation(List<String> lstStationProvince) {
		StringBuilder sql = new StringBuilder(
				" select (cs.code || '-' || sg.code) code from CAT_STATION cs\r\n" + 
				" left join WO_TR_MAPPING_STATION wtms\r\n" + 
				" on cs.CAT_STATION_ID = wtms.CAT_STATION_ID\r\n" + 
				" LEFT JOIN SYS_GROUP sg\r\n" + 
				" on wtms.SYS_GROUP_ID = sg.SYS_GROUP_ID "
				+ " WHERE (cs.code || '-' || sg.code) in (:lstStationProvince) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("code", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));
		query.setParameterList("lstStationProvince", lstStationProvince);
		return query.list();
	}
    
    public void updateRentStatusStation(Long stationId, String rentStatus) {
        StringBuilder sqlCount = new StringBuilder(
                "UPDATE CAT_STATION SET RENT_STATUS=:rentStatus WHERE CAT_STATION_ID=:stationId ");
        SQLQuery query = getSession().createSQLQuery(sqlCount.toString());
        query.setParameter("stationId", stationId);
        query.setParameter("rentStatus", rentStatus);
        query.executeUpdate();
    }
    
    //Huypq-12072021-start
    public Map<String, Long> getWorkItemConfigByStationId(List<String> lstStationCode) {
        String sql = " SELECT " + 
        		"    config.CAT_STATION_ID catStationId, cs.CODE code " + 
        		"FROM " + 
        		"    CAT_STATION_WO_CONFIG_VALUE config " + 
        		"    LEFT JOIN CAT_STATION cs on config.CAT_STATION_ID = cs.CAT_STATION_ID AND cs.STATUS=1 " + 
        		"    where cs.CODE is not null and cs.CODE in (:lstStationCode) ";
        SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("catStationId", new LongType());
		query.addScalar("code", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));
		query.setParameterList("lstStationCode", lstStationCode);
		List<CatStationDTO> ls =  query.list();
		HashMap<String, Long> mapStation = new HashMap<>();
		for(CatStationDTO dto : ls) {
			mapStation.put(dto.getCode(), dto.getCatStationId());
		}
		return mapStation;
    }
    //Huy-end
    //Huypq-10122021-start
    public Boolean checkStationConstruction(Long consId) {
		StringBuilder sql = new StringBuilder("select CAT_STATION_ID from CONSTRUCTION where STATUS!=0 AND CONSTRUCTION_ID = :consId");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("CAT_STATION_ID", new LongType());
		query.setParameter("consId", consId);
		if(query.uniqueResult()!=null) {
			return true;
		}
		return false;
	}
    //Huy-end
    
    //Huypq-10122021-start
    public Long getContractShareRevenueById(Long contractId) {
    	StringBuilder sql = new StringBuilder(" select CNT_CONTRACT_REVENUE from CNT_CONTRACT where STATUS!=0 AND CNT_CONTRACT_ID=:contractId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("CNT_CONTRACT_REVENUE", new LongType());
    	query.setParameter("contractId", contractId);
    	
    	if(query.uniqueResult()!=null) {
    		return (Long)query.uniqueResult();
    	} else {
    		return 0l;
    	}
    }
    
    public void updateBranchWorkItem(Long constructionId, Long catWorkItemTypeId, String branch) {
        StringBuilder sqlCount = new StringBuilder(
                "UPDATE WORK_ITEM SET BRANCH=:branch where status>0 and construction_id = :constructionId and cat_work_item_type_id = :catWorkItemTypeId ");
        SQLQuery query = getSession().createSQLQuery(sqlCount.toString());
        query.setParameter("constructionId", constructionId);
        query.setParameter("catWorkItemTypeId", catWorkItemTypeId);
        query.setParameter("branch", branch);
        query.executeUpdate();
    }
    //Huy-end
}

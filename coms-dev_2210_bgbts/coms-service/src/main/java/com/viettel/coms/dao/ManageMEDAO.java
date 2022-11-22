package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.cat.dto.CatManufacturerDTO;
import com.viettel.coms.bo.SendEmailBO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.BatteryDTO;
import com.viettel.coms.dto.CabinetsSourceACDTO;
import com.viettel.coms.dto.CabinetsSourceDCDTO;
import com.viettel.coms.dto.DeviceStationElectricalDTO;
import com.viettel.coms.dto.DocManagementDTO;
import com.viettel.coms.dto.ElectricATSDTO;
import com.viettel.coms.dto.ElectricAirConditioningACDTO;
import com.viettel.coms.dto.ElectricAirConditioningDCDTO;
import com.viettel.coms.dto.ElectricDetailDTO;
import com.viettel.coms.dto.ElectricEarthingSystemDTO;
import com.viettel.coms.dto.ElectricExplosionFactoryDTO;
import com.viettel.coms.dto.ElectricFireExtinguisherDTO;
import com.viettel.coms.dto.ElectricHeatExchangerDTO;
import com.viettel.coms.dto.ElectricLightningCutFilterDTO;
import com.viettel.coms.dto.ElectricNotificationFilterDustDTO;
import com.viettel.coms.dto.ElectricRectifierDTO;
import com.viettel.coms.dto.ElectricWarningSystemDTO;
import com.viettel.coms.dto.GeneratorDTO;
import com.viettel.coms.dto.ManageCareerDTO;
import com.viettel.coms.dto.StationElectricalDTO;
import com.viettel.coms.dto.StationInformationDTO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.UnitListDTO;
import com.viettel.coms.dto.UserDirectoryDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Repository("manageMEDAO")
@Transactional
public class ManageMEDAO extends BaseFWDAOImpl<BaseFWModelImpl, Long> {


    public ManageMEDAO() {
        
    }

    public ManageMEDAO(Session session) {
        this.session = session;
    }
    
    @Autowired
   	private SendEmailDAO sendEmailDAO;
    
    @SuppressWarnings("unchecked")
    public List<StationElectricalDTO> doSearch(StationElectricalDTO criteria, Long sysUserId) {
        StringBuilder stringBuilder = new StringBuilder(
				" SELECT DISTINCT " + 
				"            T1.STATION_ELECTRICAL_ID stationElectricalId, " + 
				"            T1.STATION_ID stationId, " + 
				"            T1.STATION_CODE stationCode, " + 
				"            T1.AREA_CODE areaCode, " + 
				"            T2.CAT_PROVINCE_ID provinceId, " + 
				"            T2.ADDRESS stationAddress, " + 
				"            T1.MANAGE_USER_ID manageUserId, " + 
				"            T1.MANAGE_USER_CODE manageUserCode, " + 
				"            T1.MANAGE_USER_NAME manageUserName, " + 
				"            T1.MANAGE_USER_EMAIL manageUserEmail, " + 
				"            su.PHONE_NUMBER manageUserPhone, " + 
//				"            T1.STATION_TYPE stationType, " + 
//				"            cons.BROADCASTING_DATE broadcastingDate, " + 

				"            T1.FORMED_ASSET_DATE formedAssetDate, " + 
				"            T1.LAST_MAINTENANCE lastMaintenance, " + 
				"            T1.STATUS status, " + 
				"            cps.CAT_PARTNER_STATION_CODE stationCodeVtnet, " + 
				"            tbl.LOAI_TRAM stationType " + 
				"        FROM " + 
				"            STATION_ELECTRICAL T1  "
				+ "		LEFT JOIN CTCT_VPS_OWNER.SYS_USER su ON su.SYS_USER_ID = T1.MANAGE_USER_ID" + 
				"        LEFT JOIN " + 
				"            CAT_STATION T2  " + 
				"                ON T2.CAT_STATION_ID = T1.STATION_ID AND T2.STATUS!=0 " + 
				"        LEFT JOIN " + 
				"            CAT_STATION_HOUSE csh  " + 
				"                on T2.CAT_STATION_HOUSE_ID = csh.CAT_STATION_HOUSE_ID AND csh.STATUS!=0 " + 
//				"		 LEFT JOIN CONSTRUCTION cons on T1.STATION_ID = cons.CAT_STATION_ID AND cons.STATUS!=0 " +
				"        LEFT JOIN " + 
				"            CAT_PARTNER_STATION cps  " + 
				"                on csh.CODE = cps.CAT_STATION_HOUSE_CODE and cps.STATUS = 1 " + 
				"        LEFT JOIN (SELECT DISTINCT cnt.STATION_CODE_VCC, " + 
				"            (CASE  " + 
				"                   when cwit.PLACEMENT=0 then 'Trên mái' " + 
				"                   when cwit.PLACEMENT=1 then 'Dưới đất' " + 
				"                   else null " + 
				"                  end) LOAI_TRAM " + 
				"        FROM CNT_CONSTR_WORK_ITEM_TASK cwit  " + 
				"        left join CNT_CONTRACT cnt on cwit.CNT_CONTRACT_ID=cnt.CNT_CONTRACT_ID AND cnt.STATUS!=0 " + 
				"        where cnt.CONTRACT_TYPE=8 and cwit.STATUS!=0 and cnt.CNT_CONTRACT_APPROVE =1 ) tbl on T1.STATION_CODE = tbl.STATION_CODE_VCC " +
						" where 1=1 ");
//                        + "and T2.TYPE_HTCT = 1 ");
        if (StringUtils.isNotBlank(criteria.getAreaCode())) {
            stringBuilder.append("AND (upper(T1.AREA_CODE) like upper(:areaCode) escape '&') ");
        }
        if(null != criteria.getStatus()) {
        	stringBuilder.append("AND T1.STATUS = :status ");
        	
        }
        if (null != criteria.getProvinceId()) {
            stringBuilder.append("AND T2.CAT_PROVINCE_ID = :provinceId ");
        }
        if(sysUserId!=null) {
        	stringBuilder.append("AND T1.MANAGE_USER_ID = :sysUserId ");
        }
        
        if(StringUtils.isNotBlank(criteria.getStationCode())) {
        	stringBuilder.append("AND (upper(T1.STATION_CODE) like upper(:stationCode) escape '&') ");
        }
//        stringBuilder.append("AND ROWNUM <= 100 ");
        stringBuilder.append(" ORDER BY T1.STATION_ID DESC ");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(stringBuilder.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("stationElectricalId", new LongType());
        query.addScalar("stationId", new LongType());
        query.addScalar("stationCode", new StringType());
        query.addScalar("stationAddress", new StringType());
        query.addScalar("areaCode", new StringType());
        query.addScalar("provinceId", new LongType());

		query.addScalar("manageUserId", new LongType());
        query.addScalar("manageUserCode", new StringType());
        query.addScalar("manageUserName", new StringType());
        query.addScalar("manageUserEmail", new StringType());
        query.addScalar("manageUserPhone", new LongType());
        query.addScalar("stationType", new StringType());
        
        query.addScalar("formedAssetDate", new DateType());
        query.addScalar("lastMaintenance", new DateType());
        query.addScalar("status", new LongType());
        query.addScalar("stationCodeVtnet", new StringType());

        if (StringUtils.isNotBlank(criteria.getAreaCode())) {
            query.setParameter("areaCode", "%" + criteria.getAreaCode() + "%");
            queryCount.setParameter("areaCode", "%" + criteria.getAreaCode() + "%");
        }
        if(null != criteria.getStatus()) {
        	 query.setParameter("status",criteria.getStatus());
             queryCount.setParameter("status",criteria.getStatus());
        }
        if (null != criteria.getProvinceId()) {
            query.setParameter("provinceId", criteria.getProvinceId());
            queryCount.setParameter("provinceId",criteria.getProvinceId());
        }
        if(sysUserId!=null) {
        	query.setParameter("sysUserId", sysUserId);
            queryCount.setParameter("sysUserId", sysUserId);
        }
        if(StringUtils.isNotBlank(criteria.getStationCode())) {
        	query.setParameter("stationCode", "%" + criteria.getStationCode() + "%");
            queryCount.setParameter("stationCode", "%" + criteria.getStationCode() + "%");
        }
        query.setResultTransformer(Transformers
                .aliasToBean(StationElectricalDTO.class));
        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            query.setFirstResult((criteria.getPage().intValue() - 1)
                    * criteria.getPageSize().intValue());
            query.setMaxResults(criteria.getPageSize().intValue());
        }

        List ls = query.list();
        criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult())
                .intValue());
        return ls;
    }
    
    @SuppressWarnings("unchecked")
    public List<DeviceStationElectricalDTO> getDevices(DeviceStationElectricalDTO criteria) {
        StringBuilder stringBuilder = new StringBuilder(
                "SELECT T1.DEVICE_ID deviceId,"
                		+ " T1.DEVICE_CODE deviceCode," + "T1.DEVICE_NAME deviceName,"
                        + " T1.TYPE type," + "T1.SERIAL serial," 
                		+ " T1.STATUS status," + " T1.STATE state,"
                        + " T1.CREATE_DATE createDate," + "T1.CREATE_USER createUser,"
                		+ " T1.STATION_ID stationId, "
                		+ " T1.FAILURE_STATUS failureStatus, "
						+ " T1.CREATED_USER createdUser, "
						+ " T1.CREATED_DATE createdDate, "
                		+ " T1.APPROVED_USER approvedUser, "
                		+ " T1.APPROVED_DATE approvedDate, "
                		+ " T1.DESCRIPTION_FAILURE descriptionFailure, "
                		+ " T1.FAILURE failure, "
                		+ " apr1.NAME failureString, "
                        + " apr.NAME stateStr "
                        + " FROM  DEVICE_STATION_ELECTRICAL T1"
                        + " LEFT JOIN APP_PARAM apr on T1.STATE = apr.PAR_ORDER AND apr.PAR_TYPE='DEVICE_ELECTRIC_STATE' "
                        + " LEFT JOIN APP_PARAM apr1 on T1.FAILURE = apr1.APP_PARAM_ID "
                        + " where 1=1 ");

        if (StringUtils.isNotBlank(criteria.getDeviceName())) {
            stringBuilder.append("AND (upper(T1.DEVICE_NAME) like upper(:deviceName)) ");
        }
        
        if(StringUtils.isNotBlank(criteria.getType())) {
        	stringBuilder.append("AND T1.TYPE = :type ");
        }
        
        if(StringUtils.isNotBlank(criteria.getStatus())) {
        	stringBuilder.append("AND T1.STATUS LIKE :status ");
        }
        
        if(StringUtils.isNotBlank(criteria.getState())) {
        	stringBuilder.append("AND T1.STATE LIKE :state ");
        }
        
        if (null != criteria.getStationId()) {
            stringBuilder.append("AND T1.STATION_ID = :stationId ");
        }
        stringBuilder.append(" ORDER BY T1.DEVICE_ID DESC ");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(stringBuilder.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("deviceId", new LongType());
        query.addScalar("deviceCode", new StringType());
        query.addScalar("deviceName", new StringType());
        query.addScalar("type", new StringType());
        query.addScalar("serial", new StringType());

        query.addScalar("status", new StringType());
        query.addScalar("state", new StringType());
        
        query.addScalar("createDate", new DateType());
        query.addScalar("createUser", new LongType());
        query.addScalar("stationId", new LongType());
        query.addScalar("stateStr", new StringType());

		query.addScalar("createdDate", new DateType());
		query.addScalar("createdUser", new LongType());
        query.addScalar("failureStatus", new LongType());
        query.addScalar("approvedUser", new LongType());
        query.addScalar("approvedDate", new DateType());
        query.addScalar("descriptionFailure", new StringType());
        query.addScalar("failure", new StringType());
        query.addScalar("failureString", new StringType());

        if (StringUtils.isNotBlank(criteria.getDeviceName())) {
            
            query.setParameter("deviceName", "%" + criteria.getDeviceName() + "%");
            queryCount.setParameter("deviceName", "%" + criteria.getDeviceName() + "%");
        }
        
        if(StringUtils.isNotBlank(criteria.getType())) {
        	query.setParameter("type", criteria.getType() );
            queryCount.setParameter("type", criteria.getType() );
        }
        
        if(StringUtils.isNotBlank(criteria.getStatus())) {
        	query.setParameter("status","%" + criteria.getStatus() + "%");
            queryCount.setParameter("status","%" + criteria.getStatus() + "%");
        }
        
        if(StringUtils.isNotBlank(criteria.getState())) {
        	query.setParameter("state","%" + criteria.getState() + "%");
            queryCount.setParameter("state","%" + criteria.getState() + "%");
        }
        
        if (null != criteria.getStationId()) {
            query.setParameter("stationId", criteria.getStationId() );
            queryCount.setParameter("stationId",criteria.getStationId());
        }
        
        query.setResultTransformer(Transformers
                .aliasToBean(DeviceStationElectricalDTO.class));
        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            query.setFirstResult((criteria.getPage().intValue() - 1)
                    * criteria.getPageSize().intValue());
            query.setMaxResults(criteria.getPageSize().intValue());
        }

        List ls = query.list();
        criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult())
                .intValue());
        return ls;
    }

	public List<AppParamDTO> getEquipments(AppParamDTO obj) {
		// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			StringBuilder sql = new StringBuilder("SELECT APP_PARAM_ID appParamId," + "CODE code," + "NAME name,"
					+ "PAR_TYPE parType," + "STATUS status " + " FROM APP_PARAM WHERE 1=1 ");

			if (StringUtils.isNotBlank(obj.getParType())) {
				sql.append(" AND PAR_TYPE  like (:parType) ");
			}
			
			sql.append(" ORDER BY APP_PARAM_ID ASC ");

			StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
			sqlCount.append(sql.toString());
			sqlCount.append(")");

			SQLQuery query = getSession().createSQLQuery(sql.toString());
			SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

			query.addScalar("appParamId", new LongType());
			query.addScalar("code", new StringType());
			query.addScalar("name", new StringType());
			query.addScalar("parType", new StringType());
			query.addScalar("status", new StringType());

			query.setResultTransformer(Transformers.aliasToBean(AppParamDTO.class));

			if (StringUtils.isNotBlank(obj.getParType())) {
				query.setParameter("parType","%" + obj.getParType()+ "%");
				queryCount.setParameter("parType","%" + obj.getParType()+ "%");
			}
			obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

			return query.list();
		
	}

	public List<ElectricDetailDTO> getDeviceDetails(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
			StringBuilder sql = new StringBuilder("SELECT ed.ID electricDetailId, ed.ID id, ed.DEVICE_ID deviceId, ed.ELECTRIC electric, ed.DISTANCE distance, ");
			sql.append(" ed.ELECTRIC_QUOTA_CB_ELECTRIC_METER_A electricQuotaCBElectricMeterA, ");
			sql.append(" ed.ELECTRIC_QUOTA_CB_STATION_A electricQuotaCBStationA, ed.WIRE_TYPE wireType, ");
			sql.append(" ed.SUPPILER suppiler, ed.VOLTAGE_AC voltageAC, ed.HOST_OPINION hostOpinion, ");
			sql.append(" ed.RATE_CAPACITY_STATION rateCapacityStation, ed.PRICE price, ");
			sql.append(" ed.SECTION section, ed.CONDUTOR_QUALITY condutorQuality, ed.SUPERFICIES superficies, dse.STATE state, ed.MAX_SIZE maxSize ");
			sql.append(" FROM ELECTRIC_DETAIL ed LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON ed.DEVICE_ID = dse.DEVICE_ID");
			sql.append(" WHERE ed.DEVICE_ID = :deviceId ");
			
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("electricDetailId", new LongType());
			query.addScalar("id", new LongType());
			query.addScalar("deviceId", new LongType());
			query.addScalar("electric", new LongType());
			query.addScalar("distance", new LongType());
			query.addScalar("electricQuotaCBElectricMeterA", new LongType());
			query.addScalar("electricQuotaCBStationA", new LongType());
			query.addScalar("wireType", new LongType());
			query.addScalar("voltageAC", new LongType());
			query.addScalar("rateCapacityStation", new LongType());
			query.addScalar("price", new LongType());
			query.addScalar("section", new LongType());
			query.addScalar("condutorQuality", new LongType());
			query.addScalar("superficies", new LongType());
			query.addScalar("suppiler", new StringType());
			query.addScalar("hostOpinion", new StringType());
			query.addScalar("state", new LongType());
			query.addScalar("maxSize", new StringType());
			query.setResultTransformer(Transformers.aliasToBean(ElectricDetailDTO.class));

			query.setParameter("deviceId",obj.getDeviceId());
			return query.list();
	}

	public void approve(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		String sql = ("UPDATE DEVICE_STATION_ELECTRICAL SET STATE = 3 WHERE DEVICE_ID =:deviceId");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("deviceId", obj.getDeviceId());
		query.executeUpdate();
		
	}
	
	public void reject(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		String sql = ("UPDATE DEVICE_STATION_ELECTRICAL SET STATE = 4, REASON = :reason WHERE DEVICE_ID =:deviceId");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("deviceId", obj.getDeviceId());
		query.setParameter("reason", obj.getReason());
		query.executeUpdate();
		
	}

	public List<CabinetsSourceACDTO> getCabinetsSourceAC(DeviceStationElectricalDTO obj) {
		StringBuilder sql = new StringBuilder("select csa.ID id,csa.DEVICE_ID deviceId, ");
		sql.append(" csa.CABINETS_SOURCE_NAME cabinetsSourceName, csa.PHASE_NUMBER phaseNumber, csa.STATUS status, dse.STATE state ");
		sql.append(" FROM CABINETS_SOURCE_AC csa LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON csa.DEVICE_ID = dse.DEVICE_ID");
		sql.append(" WHERE csa.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("phaseNumber", new LongType());
		query.addScalar("cabinetsSourceName", new StringType());
		query.addScalar("status", new LongType());
		query.addScalar("state", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(CabinetsSourceACDTO.class));
		query.setParameter("deviceId",obj.getDeviceId());
		return query.list();
	}
	
	public List<CabinetsSourceDCDTO> getCabinetsSourceDC(DeviceStationElectricalDTO obj) {
		StringBuilder sql = new StringBuilder("select csd.ID cabinetsDCId, csd.ID id,csd.DEVICE_ID deviceId, csd.RECFITER_NUMBER recfiterNumber,");
		sql.append(" csd.PREVENTIVE preventtive, csd.CABINETS_SOURCE_DC_NAME cabinetsSourceDCName, ");
		sql.append(" csd.STATE_CABINETS_SOURCE_DC stateCabinetsSourceDC, csd.POWER_CABINET_MONITORING powerCabinetMonitoring, ");
		sql.append(" csd.NOT_CHARGE_THE_BATTERY notChargeTheBattery, csd.CHARGE_THE_BATTERY chargeTheBattery, ");
		sql.append(" csd.CB_NUMBER_LESS_THAN_30A_UNUSED cbNumberLessThan30AUnused, csd.CB_NUMBER_GREATER_THAN_30A_UNUSED cbNumberGreaterThan30AUnused, ");
		sql.append(" csd.CB_NYMBER_ADDITION cbNymberAddition, csd.STATE_RECTIFER stateRectifer, csd.QUANTITY_USE quantityUse, ");
		sql.append(" csd.QUANTITY_ADDITION quantityAddition, csd.SERIAL sireal, ");
		sql.append(" csd.DEVICE_MODEL numberDeviceModel, csd.STATE_MODULE stateModule,  dse.STATE state ");
		sql.append(" FROM CABINETS_SOURCE_DC csd LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON csd.DEVICE_ID = dse.DEVICE_ID");
		sql.append(" WHERE csd.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("cabinetsDCId", new LongType());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("cabinetsSourceDCName", new StringType());
		query.addScalar("notChargeTheBattery", new StringType());
		query.addScalar("chargeTheBattery", new StringType());
		query.addScalar("preventtive", new LongType());
		query.addScalar("stateCabinetsSourceDC", new LongType());
		query.addScalar("powerCabinetMonitoring", new LongType());
		query.addScalar("cbNumberLessThan30AUnused", new LongType());
		query.addScalar("cbNumberGreaterThan30AUnused", new LongType());
		query.addScalar("cbNymberAddition", new LongType());
		query.addScalar("stateRectifer", new LongType());
		query.addScalar("quantityUse", new LongType());
		query.addScalar("quantityAddition", new LongType());
		query.addScalar("sireal", new StringType());
		query.addScalar("numberDeviceModel", new StringType());
		query.addScalar("stateModule", new LongType());
		query.addScalar("state", new LongType());
		query.addScalar("recfiterNumber", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(CabinetsSourceDCDTO.class));
		query.setParameter("deviceId",obj.getDeviceId());
		return query.list();
	}

	public List<ElectricAirConditioningACDTO> getElectricAirConditioningAC(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.ELECTRIC_AIR_CONDITIONING_AC_NAME electricAirConditioningACName, ");
		sql.append(" t1.STATE_AIR_CONDITIONING stateAirConditioning, t1.SERI_N_L seriNL, ");
		sql.append(" t1.MAX_FIX_TME maxFixTme, t1.MANUFACTURER manufacturer, ");
		sql.append(" t1.MODEL model, t1.GOOD_NAME goodName, t1.GOOD_CODE goodCode, ");
		sql.append(" t1.GOOD_CODE_KTTS goodCodeKTTS, t1.TIME_INTO_USE timeIntoUse, ");
		sql.append(" t1.LAST_MAJOR_MAINTENANCE_TIME lastMajorMaintenanceTime, ");
		sql.append(" t1.MODEL_COLD_UNIT modelColdUnit, t1.MANUFACTURER_COLD_UNIT manufacturerColdUnit, ");
		sql.append(" t1.GOOD_CODE_COLD_UNIT goodCodeColdUnit, t1.TYPE_COLD_UNIT typeCodeUnit, ");
		sql.append(" t1.WATTAGE_DH_BTU wattageDHBTU, t1.WATTAGE_ELICTRONIC wattageElictronic, ");
		sql.append(" t1.MODEL_HOT_UNIT modelHotUnit, t1.MANUFACTURER_HOT_UNIT manufacturerHotUnit, ");
		sql.append(" t1.TOTAL_REPAIR_COST totalRepairCost, t1.TYPE_OF_GAS typeOfGas, ");
		sql.append(" t1.TOTAL_NUMBER_FAILURES totalNumberFailures, dse.STATE state ");
		sql.append(" FROM ELECTRIC_AIR_CONDITIONING_AC t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("electricAirConditioningACName", new StringType());
		query.addScalar("manufacturer", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("goodName", new StringType());
		query.addScalar("modelColdUnit", new StringType());
		query.addScalar("manufacturerColdUnit", new StringType());
		query.addScalar("goodCodeColdUnit", new StringType());
		query.addScalar("modelHotUnit", new StringType());
		query.addScalar("manufacturerHotUnit", new StringType());
		query.addScalar("typeOfGas", new StringType());
		query.addScalar("stateAirConditioning", new LongType());
		query.addScalar("seriNL", new StringType());
		query.addScalar("goodCode", new StringType());
		query.addScalar("goodCodeKTTS", new StringType());
		query.addScalar("typeCodeUnit", new LongType());
		query.addScalar("wattageDHBTU", new LongType());
		query.addScalar("wattageElictronic", new LongType());
		query.addScalar("totalRepairCost", new LongType());
		query.addScalar("totalNumberFailures", new LongType());
		query.addScalar("state", new LongType());
		query.addScalar("maxFixTme", new DateType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("lastMajorMaintenanceTime", new DateType());
		
		query.setResultTransformer(Transformers.aliasToBean(ElectricAirConditioningACDTO.class));
		query.setParameter("deviceId",obj.getDeviceId());
		return query.list();
	}

	public DeviceStationElectricalDTO findByTypeAndSerial(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder("Select T1.DEVICE_ID deviceId,"
				+ " T1.DEVICE_CODE deviceCode," + "T1.DEVICE_NAME deviceName, "
				+ " T1.TYPE type," + "T1.SERIAL serial, " 
                + " T1.STATUS status "
                + " FROM DEVICE_STATION_ELECTRICAL T1 "
                + " WHERE T1.STATUS LIKE '1' ");
		
		if(obj.getDeviceId() != null) {
			stringBuilder.append(" AND T1.DEVICE_ID <> :id "); 
		}
		
		if(StringUtils.isNotBlank(obj.getType())) {
        	stringBuilder.append("AND T1.TYPE = :type ");
        }
        
        if(StringUtils.isNotBlank(obj.getDeviceCode())) {
        	stringBuilder.append("AND T1.DEVICE_CODE = :code ");
        }
        if(obj.getStationId() != null) {
        	stringBuilder.append("AND T1.STATION_ID = :station ");
        }
        
        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        query.addScalar("deviceId", new LongType());
        query.addScalar("deviceCode", new StringType());
        query.addScalar("deviceName", new StringType());
        query.addScalar("type", new StringType());
        query.addScalar("serial", new StringType());
        query.addScalar("status", new StringType());
        if(obj.getDeviceId() != null) {
        	query.setParameter("id", obj.getDeviceId());
        }
        if(StringUtils.isNotBlank(obj.getType())) {
        	query.setParameter("type", obj.getType() );  
        }
        if(StringUtils.isNotBlank(obj.getDeviceCode())) {
        	query.setParameter("code",obj.getDeviceCode());
        }
        if(obj.getStationId() != null) {
        	query.setParameter("station",obj.getStationId());
        }
        query.setResultTransformer(Transformers
                .aliasToBean(DeviceStationElectricalDTO.class));
        return (DeviceStationElectricalDTO) query.uniqueResult();
	}

	public List<ElectricHeatExchangerDTO> getNHIET(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" select t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.DIVICE_NAME deviceName, t1.STATE_EHE stateEHE, t1.SERIAL serial, ");
		sql.append(" t1.WATTAGE wattage, t1.MANUFACTURER manufacturer, t1.MODEL model,  ");
		sql.append(" t1.GOOD_CODE goodCode, t1.GOOD_NAME goodName, ");
		sql.append(" t1.GOOD_CODE_KTTS goodCodeKtts, t1.TIME_INTO_USE timeIntoUse, ");
		sql.append(" t1.LAST_MAJOR_MAINTENANCE_TIME lastMajorMaintenanceTime, dse.STATE state ");
		sql.append(" from ELECTRIC_HEAT_EXCHANGER t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("deviceName", new StringType());
		query.addScalar("stateEHE", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("wattage", new LongType());
		query.addScalar("manufacturer", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("goodCode", new StringType());
		query.addScalar("goodName", new StringType());
		query.addScalar("goodCodeKtts", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("lastMajorMaintenanceTime", new DateType());
		query.addScalar("state", new StringType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(ElectricHeatExchangerDTO.class));
		return query.list();
		
	}
	
	public List<ElectricNotificationFilterDustDTO> getTHONGGIO(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" select t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.ELECTRIC_NOTIFICATION_FILTER_DUST_NAME electricNotificationFilterDustName, ");
		sql.append(" t1.SERIAL serial, t1.STATE_SERIAL stateSerial, t1.MANUFACTURER manufacturer,   ");
		sql.append(" t1.MODEL model, t1.GOOD_CODE goodCode, t1.MAXIMUM_CAPACITY maximumCapacity, ");
		sql.append(" t1.STATE_KTTS stateKtts, t1.TIME_INTO_USE timeIntoUse, t1.LAST_MAINTENANCE_TIME lastMaintenanceTime, ");
		sql.append(" t1.NEAREST_REPAIR_TIME nearestRepairTime, t1.TOTAL_REPAIR_COST totalRepairCost, ");
		sql.append(" t1.TOTAL_NUMBER_FAILURES totalNumberFailures,t1.STATE_ENFD stateENFD, dse.STATE state ");
		sql.append(" from ELECTRIC_NOTIFICATION_FILTER_DUST t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("serial", new StringType());
		query.addScalar("stateSerial", new StringType());
		query.addScalar("goodCode", new StringType());
		query.addScalar("maximumCapacity", new LongType());
		query.addScalar("state", new LongType());
		query.addScalar("stateENFD", new LongType());
		query.addScalar("totalRepairCost", new LongType());
		query.addScalar("totalNumberFailures", new LongType());
		query.addScalar("electricNotificationFilterDustName", new StringType());
		query.addScalar("manufacturer", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("stateKtts", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("lastMaintenanceTime", new DateType());
		query.addScalar("nearestRepairTime", new DateType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(ElectricNotificationFilterDustDTO.class));
		return query.list();
		
	}
	
	public List<ElectricAirConditioningDCDTO> getDHDC(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.ELECTRIC_AIR_CONDITIONING_DC_NAME electricAirConditioningDcName, ");
		sql.append(" t1.SERIAL serial, t1.TIME_INTO_USE timeIntoUse, t1.TYPE_GAS typeGas, ");
		sql.append(" t1.STATE_EACD stateEACD, t1.MANUFACTURER manufacturer, ");
		sql.append(" t1.MODEL model, t1.REFRIGERATION_CAPACITY refrigerationCapacity, ");
		sql.append(" t1.POWER_CAPACITY powerCapacity, t1.GOOD_NAME goodName, ");
		sql.append(" t1.GOOD_CODE goodCode, t1.GOOD_CODE_KTTS goodCodeKtts, dse.STATE state, ");
		sql.append(" t1.LAST_MAINTENANCE_TIME lastMaintenanceTime, t1.NEAREST_REPAIR_TIME nearestRepairTime, ");
		sql.append(" t1.TOTAL_REPAIR_COST totalRepairCost, t1.TOTAL_NUMBER_FAILURES totalNumberFailures ");
		sql.append(" from ELECTRIC_AIR_CONDITIONING_DC t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("electricAirConditioningDcName", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("state", new LongType());
		query.addScalar("manufacturer", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("refrigerationCapacity", new LongType());
		query.addScalar("powerCapacity", new LongType());
		query.addScalar("goodName", new StringType());
		query.addScalar("goodCode", new StringType());
		query.addScalar("goodCodeKtts", new StringType());
		query.addScalar("lastMaintenanceTime", new DateType());
		query.addScalar("nearestRepairTime", new DateType());
		query.addScalar("totalRepairCost", new LongType());
		query.addScalar("totalNumberFailures", new LongType());
		query.addScalar("typeGas", new StringType());
		query.addScalar("stateEACD", new LongType());
		query.addScalar("timeIntoUse", new DateType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(ElectricAirConditioningDCDTO.class));
		return query.list();
		
	}
	
	public List<GeneratorDTO> getMayPhat(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.GENERATOR_NAME generatorName, t1.MODEL model, ");
		sql.append(" t1.MANUFACTURER manufactuber, t1.FUEL_TYPE fuelType, ");
		sql.append(" t1.SERIAL serial, t1.RATED_POWER ratedPower, dse.STATE state,");
		sql.append(" t1.STATION_CODE_BEFORE_TRANSFER stationCodeBeforeTransfer, ");
		sql.append(" t1.WATTAGE_MAX wattageMax, t1.WORKSTATION_STATUS workstationStatus, ");
		sql.append(" t1.DISTANCE_GENERATOR_STATION distanceGeneratorStation, ");
		sql.append(" t1.STATUS status, t1.TOTAL_RUNNING_TIME totalRunningTime, ");
		sql.append(" t1.TOTAL_REPAIR_COST totalRepairCost,t1.TOTAL_NUMBER_FAILURES totalNumberFailures, ");
		sql.append(" t1.FT_CONFIRM_GENERATOR_OVER_CAPACITY ftConfirmGeneratorOverCapacity, ");
		sql.append(" t1.GENERATOR_OVER_CAPACITY generatorOverCapacity,t1.TIME_INTO_USE timeIntoUse, ");
		sql.append(" t1.TIME_LAST_MAINTENANCE timeLastMaintenance, t1.LAST_REPAIR_TIME lastRepairTime ");
		sql.append(" from GENERATOR t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("state", new LongType());
		query.addScalar("ratedPower", new LongType());
		query.addScalar("wattageMax", new LongType());
		query.addScalar("workstationStatus", new LongType());
		query.addScalar("distanceGeneratorStation", new LongType());
		query.addScalar("status", new LongType());
		query.addScalar("totalRunningTime", new LongType());
		query.addScalar("totalRepairCost", new LongType());
		query.addScalar("totalNumberFailures", new LongType());
		query.addScalar("generatorOverCapacity", new StringType());
		query.addScalar("ftConfirmGeneratorOverCapacity", new LongType());
		query.addScalar("generatorName", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("manufactuber", new StringType());
		query.addScalar("fuelType", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("stationCodeBeforeTransfer", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("timeLastMaintenance", new DateType());
		query.addScalar("lastRepairTime", new DateType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(GeneratorDTO.class));
		return query.list();
		
	}
	
	public List<BatteryDTO> getAcquy(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.BATTERY_NAME batteryName, t1.MODEL model, dse.STATE state, ");
		sql.append(" t1.PRODUCTION_TECHNOLOGY productionTechnology, ");
		sql.append(" t1.GOOD_CODE goodCode, t1.MANUFACTURER manufactuber, ");
		sql.append(" t1.BATTERY_TYPE batteryType, t1.CAPACITY capacity, ");
		sql.append(" t1.STATION_OUTPUT_TIME_AFTER_RECOVERY stationOutputTimeAfterRecover, ");
		sql.append(" t1.TIME_INTO_USE timeIntoUse,t1.LAST_MAINTENANCE_TIME lastMaintenanceTime ");
		sql.append(" from BATTERY t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("state", new LongType());
		query.addScalar("model", new StringType());
		query.addScalar("capacity", new LongType());
		query.addScalar("batteryType", new StringType());
		query.addScalar("manufactuber", new StringType());
		query.addScalar("goodCode", new StringType());
		query.addScalar("productionTechnology", new StringType());
		query.addScalar("batteryName", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("stationOutputTimeAfterRecover", new DateType());
		query.addScalar("lastMaintenanceTime", new DateType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(BatteryDTO.class));
		return query.list();
		
	}
	public List<ElectricFireExtinguisherDTO> getCuuHoa(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.ELECTRIC_FIRE_EXTINGUISHER_NAME electricFireExtinguisherName, ");
		sql.append(" t1.ELECTRIC_FIRE_EXTINGUISHER_TYPE electricFireExtinguisherType, ");
		sql.append(" t1.ELECTRIC_FIRE_EXTINGUISHER_STATE electricFireExtinguisherState, ");
		sql.append(" t1.LAST_MAINTENANCE_TIME lastMaintenanceTime, ");
		sql.append(" t1.ELECTRIC_FIRE_EXTINGUISHER_LOCATION electricFireExtinguisherLocation, ");
		sql.append(" t1.WEIGHT weight,t1.TIME_INTO_USE timeIntoUse, dse.STATE state ");
		sql.append(" from ELECTRIC_FIRE_EXTINGUISHER t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("weight", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("electricFireExtinguisherName", new StringType());
		query.addScalar("electricFireExtinguisherType", new StringType());
		query.addScalar("electricFireExtinguisherState", new LongType());
		query.addScalar("electricFireExtinguisherLocation", new StringType());
		query.addScalar("state", new LongType());
		query.addScalar("lastMaintenanceTime", new DateType());
		query.addScalar("timeIntoUse", new DateType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(ElectricFireExtinguisherDTO.class));
		return query.list();
		
	}
	
	public List<ElectricWarningSystemDTO> getCanhBao(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.STATE_AC_BOX stateACBox, t1.STATUS_AC_BOX statusACBox, ");
		sql.append(" t1.STATE_LOW_BATTERY stateLowBattery, t1.STATUS_LOW_BATTERY statusLowBattery, ");
		sql.append(" t1.STATE_TEMPERATURE_WARNING stateTemperatureWarning, dse.STATE state, ");
		sql.append(" t1.STATUS_TEMPERATURE_WARNING statusTemperatureWarning, ");
		sql.append(" t1.STATE_SMOKE_WARNING stateSmokeWarning, t1.STATUS_SMOKE_WARNING statusSmokeWarning, ");
		sql.append(" t1.STATE_POWER_CABINET_MALFUNCTION_WARNING statePowerCabinetMalfuntionWarning, ");
		sql.append(" t1.STATUS_POWER_CABINET_MALFUNCTION_WARNING statusPowerCabinetMalfuntionWarning, ");
		sql.append(" t1.STATE_EXPLOSIVE_FACTORY_OPEN_WARNING stateExplosiveFactoryOpenWarning, ");
		sql.append(" t1.STATUS_EXPLOSIVE_FACTORY_OPEN_WARNING statusExplosiveFactoryOpenWarning, ");
		sql.append(" t1.STATUS_STATION_OPEN_WARNING statusStationOpenWarning, ");
		sql.append(" t1.STATE_STATION_OPEN_WARNING stateStationOpenWarning, ");
		sql.append(" t1.STATE_LOW_AC stateLowAC,t1.STATUS_LOW_AC statusLowAC ");
		sql.append(" from ELECTRIC_WARNING_SYSTEM t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("stateACBox", new LongType());
		query.addScalar("statusACBox", new LongType());
		query.addScalar("stateLowBattery", new LongType());
		query.addScalar("statusLowBattery", new LongType());
		query.addScalar("stateTemperatureWarning", new LongType());
		query.addScalar("statusTemperatureWarning", new LongType());
		query.addScalar("stateSmokeWarning", new LongType());
		query.addScalar("statusSmokeWarning", new LongType());
		query.addScalar("statePowerCabinetMalfuntionWarning", new LongType());
		query.addScalar("statusPowerCabinetMalfuntionWarning", new LongType());
		query.addScalar("stateExplosiveFactoryOpenWarning", new LongType());
		query.addScalar("statusExplosiveFactoryOpenWarning", new LongType());
		query.addScalar("stateStationOpenWarning", new LongType());
		query.addScalar("statusStationOpenWarning", new LongType());
		query.addScalar("stateLowAC", new LongType());
		query.addScalar("statusLowAC", new LongType());
		query.addScalar("state", new LongType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(ElectricWarningSystemDTO.class));
		return query.list();
	}
	
	public List<ElectricATSDTO> getATS(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.GOOD_CODE_KTTS goodCodeKTTS, t1.PHASE_NUMBER phaseNumber, ");
		sql.append(" t1.GOOD_NAME goodName,t1.GOOD_ELECTRIC_NAME goodElectricName,t1.STATE_EA stateEA, ");
		sql.append(" t1.SERIAL, t1.MODEL model, t1.SERIAL serial, dse.STATE state, t1.MANUFACTURER manufacturer,");
		sql.append(" t1.TIME_INTO_USE timeIntoUse,t1.LAST_MAINTENANCE_TIME lastMaintenanceTime, t1.ELECTRIC_QUOTA electricQuota ");
		sql.append(" from ELECTRIC_ATS t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("goodCodeKTTS", new StringType());
		query.addScalar("phaseNumber", new LongType());
		query.addScalar("stateEA", new LongType());
		query.addScalar("lastMaintenanceTime", new DateType());
		query.addScalar("state", new LongType());
		query.addScalar("goodName", new StringType());
		query.addScalar("goodElectricName", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("manufacturer", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("electricQuota", new LongType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(ElectricATSDTO.class));
		return query.list();
	}
	
	public List<ElectricExplosionFactoryDTO> getNMN(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.ELECTRIC_EXPLOSION_FACTORY_NAME electricExplosionName, ");
		sql.append(" t1.HOUSE_TYPE houseType, t1.TIME_INTO_USE timeIntoUse, ");
		sql.append(" t1.IGNITER_SETTING_STATUS igniterSettingStatus, dse.STATE state");
		sql.append(" from ELECTRIC_EXPLOSION_FACTORY t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("igniterSettingStatus", new LongType());
		query.addScalar("state", new LongType());
		query.addScalar("electricExplosionName", new StringType());
		query.addScalar("houseType", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(ElectricExplosionFactoryDTO.class));
		return query.list();
	}
	
	public List<ElectricLightningCutFilterDTO> getLocSet(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.ELECTRIC_LIGHTNING_CUT_FILTER_NAME electricLightningCutFilterName, ");
		sql.append(" t1.PRIMARY_STATUS primaryStatus, t1.PRIMARY_QUANTITY primaryQuantity, ");
		sql.append(" t1.PRIMARY_CONDITION primaryCondition,t1.PRIMARY_SPECIES primarySpecies, ");
		sql.append(" t1.RESISTOR resistor, t1.SECONDARY_STATUS secondaryStatus, ");
		sql.append(" t1.SECONDARY_QUANTITY secondaryQuantity,t1.SECONDARY_CONDITION secondaryCodition,");
		sql.append(" t1.SECONDARY_SPECIES secondarySpecies, t1.OTHER_LIGHTNING_CUT_FILTER_NAME otherLightningCutFilterName,");
		sql.append(" t1.OTHER_LIGHTNING_CUT_FILTER_STATUS otherLightningCutFilterStatus, ");
		sql.append(" t1.TIME_INTO_USE timeIntoUse,LAST_MAINTENANCE_TIME lastMaintenanceTime, ");
		sql.append(" dse.STATE state");
		sql.append(" from ELECTRIC_LIGHTNING_CUT_FILTER t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("electricLightningCutFilterName", new StringType());
		query.addScalar("primaryStatus", new LongType());
		query.addScalar("primaryQuantity", new LongType());
		query.addScalar("primaryCondition", new LongType());
		query.addScalar("primarySpecies", new StringType());
		query.addScalar("resistor", new LongType());
		query.addScalar("secondaryStatus", new LongType());
		query.addScalar("secondaryQuantity", new LongType());
		query.addScalar("secondaryCodition", new LongType());
		query.addScalar("state", new LongType());
		query.addScalar("otherLightningCutFilterStatus", new LongType());
		query.addScalar("secondarySpecies", new StringType());
		query.addScalar("otherLightningCutFilterName", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("lastMaintenanceTime", new DateType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(ElectricLightningCutFilterDTO.class));
		return query.list();
	}
	
	public List<ElectricEarthingSystemDTO> getTD(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.ELECTRIC_EARTHING_SYSTEM_NAME electricEarthingSystemName, ");
		sql.append(" t1.GROUNDING_STATUS groundStatus,t1.PRIMARY_CONDITION primaryCondition, ");
		sql.append(" t1.LAST_MAINTENANCE_TIME lastMaintenanceTime, ");
		sql.append(" t1.GROUND_RESISTANCE groundResistance, dse.STATE state");
		sql.append(" from ELECTRIC_EARTHING_SYSTEM t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("electricEarthingSystemName", new StringType());
		query.addScalar("groundResistance", new LongType());
		query.addScalar("groundStatus", new LongType());
		query.addScalar("primaryCondition", new LongType());
		query.addScalar("state", new LongType());
		query.addScalar("lastMaintenanceTime", new DateType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(ElectricEarthingSystemDTO.class));
		return query.list();
	}
	
	public List<ElectricRectifierDTO> getR(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.SERIAL serial, t1.KTTS_STATUS kttsStatus, t1.GOOD_CODE goodCode, ");
		sql.append(" t1.GOOD_NAME goodName, t1.MODEL model, t1.MANUFACTURER manufacturer, ");
		sql.append(" t1.QUANTITY_CAN_ADDED quantitycanAdded, t1.QUANTITY_IN_USE quantityInUse, ");
		sql.append(" t1.RATED_POWER ratedPower, t1.TIME_INTO_USE timeIntoUse,  ");
		sql.append(" t1.STATE_ER stateER, dse.STATE state");
		sql.append(" from ELECTRIC_RECTIFIER t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("serial", new StringType());
		query.addScalar("kttsStatus", new LongType());
		query.addScalar("goodCode", new StringType());
		query.addScalar("goodName", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("manufacturer", new StringType());
		query.addScalar("ratedPower", new LongType());
		query.addScalar("quantityInUse", new LongType());
		query.addScalar("quantitycanAdded", new LongType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("stateER", new LongType());
		query.addScalar("state", new LongType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(ElectricRectifierDTO.class));
		return query.list();
	}
	
	public List<StationInformationDTO> getNhaTram(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT t1.ID id, t1.DEVICE_ID deviceId, ");
		sql.append(" t1.MANAGER manager, t1.ADDRESS address, t1.STATION_TYPE stationType, ");
		sql.append(" t1.STATION_HOUSE_TYPE stationHouseType, t1.STATION_HOUSE_SIZE stationHouseSize, ");
		sql.append(" t1.WATTAGE_MAX wattageMax, t1.WATTAGE_MAX_ACQUY wattageMaxAcquy, t1.WATTAGE_ACQUY wattageAcquy, ");
		sql.append(" t1.WATTAGE_AIR_CONDITIONING wattageAirConditioning, t1.WATTAGE_DUST_FILTER wattageDustFilter, ");
		sql.append(" t1.WATTAGE_HEAD_EXCHANGERS wattageHeadExchangers, t1.VT_WATTAGE_TV vtWattageTV, ");
		sql.append(" t1.VT_WATTAGE_TRANSMISSION vtWattageTransmission, t1.VT_WATTAGE_IP vtWattageIP, ");
		sql.append(" t1.VT_WATTAGE_CDBR vtWattageCDBR, t1.VT_WATTAGE_PSTN vtWattagePSTN, ");
		sql.append(" t1.VNP_WATTAGE_TV vnpWattageTV, t1.VNP_WATTAGE_TRANSMISSION vnpWattageTransmission, ");
		sql.append(" t1.VNP_WATTAGE_IP vnpWattageIP, t1.VNP_WATTAGE_CDBR vnpWattageCDBR, ");
		sql.append(" t1.VNP_WATTAGE_PSTN vnpWattagePSTN, t1.VNM_WATTAGE_TV vnmWattageTV, ");
		sql.append(" t1.VNM_WATTAGE_TRANSMISSION vnmWattageTransmission, t1.VNM_WATTAGE_IP vnmWattageIP, ");
		sql.append(" t1.VNM_WATTAGE_CDBR vnmWattageCDBR, t1.VNM_WATTAGE_PSTN vnmWattagePSTN, ");
		sql.append(" t1.MBP_WATTAGE_TV mbpWattageTV, t1.MBP_WATTAGE_TRANSMISSION mbpWattageTransmission, ");
		sql.append(" t1.MBP_WATTAGE_IP mbpWattageIP, t1.MBP_WATTAGE_CDBR mbpWattageCDBR, ");
		sql.append(" t1.MBP_WATTAGE_PSTN mbpWattagePSTN,dse.STATE state");
		sql.append(" from STATION_INFORMATION t1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON t1.DEVICE_ID = dse.DEVICE_ID ");
		sql.append(" WHERE t1.DEVICE_ID = :deviceId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id", new LongType());
		query.addScalar("deviceId", new LongType());
		query.addScalar("manager", new StringType());
		query.addScalar("address", new StringType());
		query.addScalar("stationType", new StringType());
		query.addScalar("stationHouseType", new StringType());
		query.addScalar("stationHouseSize", new StringType());
		query.addScalar("wattageMax", new LongType());
		query.addScalar("wattageMaxAcquy", new LongType());
		query.addScalar("wattageAcquy", new LongType());
		query.addScalar("wattageAirConditioning", new LongType());
		query.addScalar("wattageDustFilter", new LongType());
		query.addScalar("wattageHeadExchangers", new LongType());
		query.addScalar("vtWattageTV", new LongType());
		query.addScalar("vtWattageTransmission", new LongType());
		query.addScalar("vtWattageIP", new LongType());
		query.addScalar("vtWattageCDBR", new LongType());
		query.addScalar("vtWattagePSTN", new LongType());
		query.addScalar("vnpWattageTV", new LongType());
		query.addScalar("vnpWattageTransmission", new LongType());
		query.addScalar("vnpWattageIP", new LongType());
		query.addScalar("vnpWattageCDBR", new LongType());
		query.addScalar("vnpWattagePSTN", new LongType());
		query.addScalar("vnmWattageTV", new LongType());
		query.addScalar("vnmWattageTransmission", new LongType());
		query.addScalar("vnmWattageIP", new LongType());
		query.addScalar("vnmWattageCDBR", new LongType());
		query.addScalar("vnmWattagePSTN", new LongType());
		query.addScalar("mbpWattageTV", new LongType());
		query.addScalar("mbpWattageTransmission", new LongType());
		query.addScalar("mbpWattageIP", new LongType());
		query.addScalar("mbpWattageCDBR", new LongType());
		query.addScalar("mbpWattagePSTN", new LongType());
		query.addScalar("state", new LongType());
		
		query.setParameter("deviceId", obj.getDeviceId());
		query.setResultTransformer(Transformers.aliasToBean(StationInformationDTO.class));
		return query.list();
	}

	public Long getProvinceId(Long sysGroupId) {
		// TODO Auto-generated method stub
		String sql = "SELECT PROVINCE_ID FROM SYS_GROUP WHERE SYS_GROUP_ID = :sysGroupId";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("sysGroupId", sysGroupId);
		Long id = ((BigDecimal) query.uniqueResult()).longValue();
		return id;
	}

	public List<SysUserDTO> doSearchUser(SysUserDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("select su.SYS_USER_ID userId, su.FULL_NAME fullName, su.EMPLOYEE_CODE employeeCode, su.EMAIL email ");
		sql.append(" from CTCT_VPS_OWNER.SYS_USER su ");
		sql.append(" left join CTCT_CAT_OWNER.SYS_GROUP sg ON su.SYS_GROUP_ID = sg.SYS_GROUP_ID ");
		sql.append(" where su.STATUS = 1 AND su.EMAIL is not null AND ");
		sql.append(" sg.PATH like '%'||(select REGEXP_SUBSTR(PATH, '[^/]+', 1, 2) from SYS_GROUP where SYS_GROUP_ID= :sysGroupId)||'%' ");
		if(obj.getName() != null && !obj.getName().equalsIgnoreCase("")) {
			sql.append(" AND (UPPER(su.FULL_NAME) like UPPER(:name) OR UPPER(su.EMPLOYEE_CODE) like UPPER(:name) OR UPPER(su.EMAIL) like UPPER(:name)) ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("sysGroupId", obj.getSysGroupId());
		if(obj.getName() != null && !obj.getName().equalsIgnoreCase("")) {
			query.setParameter("name", "%" + obj.getName() + "%");
		}
		
		query.addScalar("userId", new LongType());
		query.addScalar("fullName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("email", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
		return query.list();
	}

	public String updateManageStation(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		try {
			StringBuilder sql= new StringBuilder("UPDATE STATION_ELECTRICAL set ");
			sql.append(" MANAGE_USER_ID = :userId,");
			sql.append(" MANAGE_USER_CODE = :employeeCode,");
			sql.append(" MANAGE_USER_NAME = :fullName,");
			sql.append(" MANAGE_USER_EMAIL = :mail");
			sql.append(" WHERE STATION_ELECTRICAL_ID = :stationElectricalId ");
			
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.setParameter("userId", obj.getManageUserId());
			query.setParameter("employeeCode", obj.getManageUserCode());
			query.setParameter("fullName", obj.getManageUserName());
			query.setParameter("mail", obj.getManageUserEmail());
			query.setParameter("stationElectricalId", obj.getStationElectricalId());
			query.executeUpdate();
			return obj.getStationElectricalId().toString();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public List<StationElectricalDTO> getInforDashboard(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(" nvl(sum(CASE WHEN dse.STATE=1 THEN 1 ELSE 0 END),0) pheDuyet, ");
		sql.append(" nvl(sum(CASE WHEN ed.ELECTRIC=3 THEN 1 ELSE 0 END),0) khongDien, ");
		sql.append(" nvl(sum(CASE WHEN ed.ELECTRIC=1 THEN 1 ELSE 0 END),0) motPha, ");
		sql.append(" nvl(sum(CASE WHEN ed.ELECTRIC=2 THEN 1 ELSE 0 END),0) baPha, ");
		sql.append(" nvl(sum(CASE WHEN ed.SUPERFICIES=0 THEN 1 ELSE 0 END),0) muaEVN, ");
		sql.append(" nvl(sum(CASE WHEN ed.SUPERFICIES=2 THEN 1 ELSE 0 END),0) muaNgoaiEVN, ");
		sql.append(" nvl(sum(CASE WHEN r.QUANTITY_IN_USE = 1 THEN 1 ELSE 0 END),0) tram1Rectifiter, ");
		sql.append(" nvl(sum(CASE WHEN dse.TYPE='ATS' THEN 1 ELSE 0 END),0) tramcoATS, ");
		sql.append(" nvl(sum(CASE WHEN g.STATUS=1 THEN 1 ELSE 0 END),0) mayphatCoDinh, ");
		sql.append(" nvl(sum(CASE WHEN g.STATUS =2 THEN 1 ELSE 0 END),0) mayPhatHong ");
		sql.append(" FROM CTCT_COMS_OWNER.DEVICE_STATION_ELECTRICAL dse ");
		sql.append(" LEFT JOIN CTCT_COMS_OWNER.STATION_ELECTRICAL se ON dse.STATION_ID=se.STATION_ID ");
		sql.append(" LEFT JOIN CTCT_COMS_OWNER.ELECTRIC_DETAIL ed ON dse.DEVICE_ID =ed.DEVICE_ID ");
		sql.append(" LEFT JOIN CTCT_COMS_OWNER.ELECTRIC_RECTIFIER r on dse.DEVICE_ID=r.DEVICE_ID ");
		sql.append(" LEFT JOIN CTCT_COMS_OWNER.GENERATOR g on dse.DEVICE_ID=g.DEVICE_ID ");
		sql.append(" LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP sg ON sg.SYS_GROUP_ID = se.SYS_GROUP_ID ");
		sql.append(" WHERE 1=1 ");
		if(!obj.getCheckUserKCQTD()) {
			sql.append("  AND sg.PROVINCE_ID = (SELECT PROVINCE_ID FROM SYS_GROUP where SYS_GROUP_ID = :sysGroupId) ");
		}
		if(obj.getCheckUserKCQTD() && obj.getProvinceId() != null) {
			sql.append(" AND sg.PROVINCE_ID = :province ");
		}
		if(obj.getStationCode() != null && !obj.getStationCode().equals("")) {
			sql.append(" AND se.STATION_CODE like :stationCode ");
		}
		if(obj.getManageUserCode() != null && !obj.getManageUserCode().equals("")) {
			sql.append(" AND se.MANAGE_USER_CODE = :manageUserCode ");
		}
		if(obj.getAreaCode() != null && !obj.getAreaCode().equals("")) {
			sql.append(" AND se.AREA_CODE = :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(!obj.getCheckUserKCQTD()) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if(obj.getCheckUserKCQTD() && obj.getProvinceId() != null) {
			query.setParameter("province", obj.getProvinceId());
		}
		if(obj.getStationCode() != null && !obj.getStationCode().equals("")) {
			query.setParameter("stationCode", "%" +obj.getStationCode()+ "%");
		}
		if(obj.getManageUserCode() != null && !obj.getManageUserCode().equals("")) {
			query.setParameter("manageUserCode", obj.getManageUserCode());
		}
		if(obj.getAreaCode() != null && !obj.getAreaCode().equals("")) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.setResultTransformer(Transformers.aliasToBean(StationElectricalDTO.class));
		query.addScalar("pheDuyet", new LongType());
		query.addScalar("khongDien", new LongType());
		query.addScalar("motPha", new LongType());
		query.addScalar("baPha", new LongType());
		query.addScalar("muaEVN", new LongType());
		query.addScalar("muaNgoaiEVN", new LongType());
		query.addScalar("tram1Rectifiter", new LongType());
		query.addScalar("tramcoATS", new LongType());
		query.addScalar("mayphatCoDinh", new LongType());
		query.addScalar("mayPhatHong", new LongType());
		
		return query.list();
	}

	public List<StationElectricalDTO> getInforDashboardStation(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(" nvl(sum(CASE when cwit.PLACEMENT =0 then 1 else 0 end),0) tramTrenMai, ");
		sql.append(" nvl(sum(CASE when cwit.PLACEMENT =1 then 1 else 0 end),0) tramDuoiDat ");
		sql.append(" FROM CTCT_IMS_OWNER.CNT_CONTRACT cnt ");
		sql.append(" LEFT JOIN CTCT_IMS_OWNER.CNT_CONSTR_WORK_ITEM_TASK cwit ON cnt.CNT_CONTRACT_ID=cwit.CNT_CONTRACT_ID ");
		sql.append(" LEFT JOIN CTCT_COMS_OWNER.STATION_ELECTRICAL se ON cnt.STATION_CODE_VCC =se.STATION_CODE ");
		sql.append(" LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP sg ON sg.SYS_GROUP_ID = se.SYS_GROUP_ID ");
		sql.append(" WHERE cnt.CNT_CONTRACT_EDITOR_TYPE=8 ");
		if(!obj.getCheckUserKCQTD()) {
			sql.append(" AND sg.PROVINCE_ID = (SELECT PROVINCE_ID FROM SYS_GROUP where SYS_GROUP_ID = :sysGroupId) ");
		}
		if(obj.getCheckUserKCQTD() && obj.getProvinceId() != null) {
			sql.append(" AND sg.PROVINCE_ID = :province ");
		}
		if(obj.getStationCode() != null && !obj.getStationCode().equals("")) {
			sql.append(" AND se.STATION_CODE like :stationCode ");
		}
		if(obj.getManageUserCode() != null && !obj.getManageUserCode().equals("")) {
			sql.append(" AND se.MANAGE_USER_CODE = :manageUserCode ");
		}
		if(obj.getAreaCode() != null && !obj.getAreaCode().equals("")) {
			sql.append(" AND se.AREA_CODE = :areaCode ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(!obj.getCheckUserKCQTD()) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if(obj.getCheckUserKCQTD() && obj.getProvinceId() != null) {
			query.setParameter("province", obj.getProvinceId());
		}
		if(obj.getStationCode() != null && !obj.getStationCode().equals("")) {
			query.setParameter("stationCode", "%" +obj.getStationCode()+ "%");
		}
		if(obj.getManageUserCode() != null && !obj.getManageUserCode().equals("")) {
			query.setParameter("manageUserCode", obj.getManageUserCode());
		}
		if(obj.getAreaCode() != null && !obj.getAreaCode().equals("")) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.setResultTransformer(Transformers.aliasToBean(StationElectricalDTO.class));
		query.addScalar("tramTrenMai", new LongType());
		query.addScalar("tramDuoiDat", new LongType());
		
		return query.list();
	}
	
	public List<StationElectricalDTO> getInforDashboardWo(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(" nvl(sum(CASE WHEN wo.finish_date < sysdate THEN 1 ELSE 0 END),0) woQuaHan, ");
		sql.append(" nvl(sum(CASE WHEN wo.finish_date > sysdate THEN 1 ELSE 0 END),0) woDangThucHien ");
		sql.append(" from CTCT_COMS_OWNER.WO wo ");
		sql.append(" LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP sg ON sg.SYS_GROUP_ID = wo.CD_LEVEL_2 ");
		sql.append(" LEFT JOIN CTCT_COMS_OWNER.STATION_ELECTRICAL se ON wo.STATION_CODE=se.STATION_CODE ");
		sql.append(" where  wo.WO_TYPE_ID=421 ");
		if(!obj.getCheckUserKCQTD()) {
			sql.append(" AND sg.PROVINCE_ID = (SELECT PROVINCE_ID FROM SYS_GROUP where SYS_GROUP_ID = :sysGroupId) ");
		}
		if(obj.getCheckUserKCQTD() && obj.getProvinceId() != null) {
			sql.append(" AND sg.PROVINCE_ID = :province ");
		}
		if(obj.getStationCode() != null && !obj.getStationCode().equals("")) {
			sql.append(" AND se.STATION_CODE like :stationCode ");
		}
		if(obj.getManageUserCode() != null && !obj.getManageUserCode().equals("")) {
			sql.append(" AND se.MANAGE_USER_CODE = :manageUserCode ");
		}
		if(obj.getAreaCode() != null && !obj.getAreaCode().equals("")) {
			sql.append(" AND se.AREA_CODE = :areaCode ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(!obj.getCheckUserKCQTD()) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if(obj.getCheckUserKCQTD() && obj.getProvinceId() != null) {
			query.setParameter("province", obj.getProvinceId());
		}
		if(obj.getStationCode() != null && !obj.getStationCode().equals("")) {
			query.setParameter("stationCode", "%" +obj.getStationCode()+ "%");
		}
		if(obj.getManageUserCode() != null && !obj.getManageUserCode().equals("")) {
			query.setParameter("manageUserCode", obj.getManageUserCode());
		}
		if(obj.getAreaCode() != null && !obj.getAreaCode().equals("")) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.setResultTransformer(Transformers.aliasToBean(StationElectricalDTO.class));
		query.addScalar("woQuaHan", new LongType());
		query.addScalar("woDangThucHien", new LongType());
		
		return query.list();
	}

	public List<StationElectricalDTO> getStation(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT se.STATION_CODE stationCode,cp.CODE AS provinceCode,cp.AREA_CODE areaCode,cs.ADDRESS address ");
		if(obj.getType() == 3 || obj.getType() == 8) {
			sql.append(" ,(CASE WHEN cwit.PLACEMENT= 1 THEN 'Dưới đất' WHEN cwit.PLACEMENT= 0 THEN 'Trên mái' ELSE NULL END ) location ");
			sql.append(" FROM CTCT_IMS_OWNER.CNT_CONTRACT cnt  ");
			sql.append(" LEFT JOIN CTCT_IMS_OWNER.CNT_CONSTR_WORK_ITEM_TASK cwit ON cnt.CNT_CONTRACT_ID=cwit.CNT_CONTRACT_ID ");
			sql.append(" LEFT JOIN CTCT_COMS_OWNER.STATION_ELECTRICAL se ON cnt.STATION_CODE_VCC =se.STATION_CODE ");
			sql.append(" LEFT JOIN CTCT_CAT_OWNER.CAT_STATION cs ON cs.CAT_STATION_ID = se.STATION_ID ");
			sql.append(" LEFT JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID = cs.CAT_PROVINCE_ID ");
			sql.append(" WHERE cnt.CNT_CONTRACT_EDITOR_TYPE=8 ");
		}else {
			sql.append(" FROM CTCT_COMS_OWNER.DEVICE_STATION_ELECTRICAL dse ");
			sql.append(" LEFT JOIN CTCT_COMS_OWNER.STATION_ELECTRICAL se ON dse.STATION_ID=se.STATION_ID ");
			sql.append(" LEFT JOIN CTCT_COMS_OWNER.ELECTRIC_DETAIL ed ON dse.DEVICE_ID =ed.DEVICE_ID ");
			sql.append(" LEFT JOIN CTCT_COMS_OWNER.ELECTRIC_RECTIFIER r ON dse.DEVICE_ID=r.DEVICE_ID ");
			sql.append(" LEFT JOIN CTCT_COMS_OWNER.GENERATOR g ON dse.DEVICE_ID=g.DEVICE_ID ");
			sql.append(" LEFT JOIN CTCT_CAT_OWNER.CAT_STATION cs ON cs.CAT_STATION_ID =se.STATION_ID ");
			sql.append(" LEFT JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID = cs.CAT_PROVINCE_ID ");
			sql.append(" WHERE 1=1 ");
			if(obj.getType() == 23) {
				sql.append(" AND dse.STATE=1 ");
			}
		}
		
		if(!obj.getCheckUserKCQTD()) {
			sql.append(" AND sg.PROVINCE_ID = (SELECT PROVINCE_ID FROM SYS_GROUP where SYS_GROUP_ID = :sysGroupId) ");
		}
		if(obj.getCheckUserKCQTD() && obj.getProvinceId() != null) {
			sql.append(" AND sg.PROVINCE_ID = :province ");
		}
		if(obj.getStationCode() != null && !obj.getStationCode().equals("")) {
			sql.append(" AND se.STATION_CODE like :stationCode ");
		}
		if(obj.getManageUserCode() != null && !obj.getManageUserCode().equals("")) {
			sql.append(" AND se.MANAGE_USER_CODE = :manageUserCode ");
		}
		if(obj.getAreaCode() != null && !obj.getAreaCode().equals("")) {
			sql.append(" AND se.AREA_CODE = :areaCode ");
		}
		
		if(obj.getType() == 3) {
			sql.append(" AND cwit.PLACEMENT = 0 ");
		}
		if(obj.getType() == 4) {
			sql.append(" AND ed.ELECTRIC = 3 ");
		}
		if(obj.getType() == 5) {
			sql.append(" AND ed.SUPERFICIES=2 ");
		}
		if(obj.getType() == 6) {
			sql.append(" AND r.QUANTITY_IN_USE = 1 ");
		}
		if(obj.getType() == 8) {
			sql.append(" AND cwit.PLACEMENT = 1 ");
		}
		if(obj.getType() == 10) {
			sql.append(" AND ed.SUPERFICIES=0 ");
		}
		if(obj.getType() == 11) {
			sql.append(" AND dse.TYPE='ATS' ");
		}
		if(obj.getType() == 13) {
			sql.append(" AND g.STATUS=1 ");
		}
		if(obj.getType() == 18) {
			sql.append(" AND g.STATUS=0 ");
		}
		if(obj.getType() == 24) {
			sql.append(" AND ed.ELECTRIC = 1 ");
		}
		if(obj.getType() == 25) {
			sql.append(" AND ed.ELECTRIC = 2 ");
		}
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        if(!obj.getCheckUserKCQTD()) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		}
        if(obj.getCheckUserKCQTD() && obj.getProvinceId() != null) {
        	query.setParameter("province", obj.getProvinceId());
			queryCount.setParameter("province", obj.getProvinceId());
		}
		if(obj.getStationCode() != null && !obj.getStationCode().equals("")) {
			query.setParameter("stationCode", "%" +obj.getStationCode()+ "%");
			queryCount.setParameter("stationCode", "%" +obj.getStationCode()+ "%");
		}
		if(obj.getManageUserCode() != null && !obj.getManageUserCode().equals("")) {
			query.setParameter("manageUserCode", obj.getManageUserCode());
			queryCount.setParameter("manageUserCode", obj.getManageUserCode());
		}
		if(obj.getAreaCode() != null && !obj.getAreaCode().equals("")) {
			query.setParameter("areaCode", obj.getAreaCode());
			queryCount.setParameter("areaCode", obj.getAreaCode());
		}
		query.setResultTransformer(Transformers.aliasToBean(StationElectricalDTO.class));
		query.addScalar("stationCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("areaCode", new StringType());
		query.addScalar("address", new StringType());
		if(obj.getType() == 3 || obj.getType() == 8) {
			query.addScalar("location", new StringType());
		}
		 if (obj.getPage() != null && obj.getPageSize() != null) {
	            query.setFirstResult((obj.getPage().intValue() - 1)* obj.getPageSize().intValue());
	            query.setMaxResults(obj.getPageSize().intValue());
	        }

	        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}

	
	public List<WoDTO> doSearchWo(WoDTO woDto) {
	    StringBuilder sql = new StringBuilder("select woTbl.WO_CODE as woCode, woTbl.WO_NAME as woName , woTbl.type as type , apr.NAME as apWorkSrcName, ");
	    sql.append(" woTbl.TR_CODE as trCode, woTbl.CONTRACT_CODE as contractCode, woTbl.STATION_CODE as stationCode, woTbl.CONSTRUCTION_CODE as constructionCode, ");
	    sql.append(" woTbl.MONEY_VALUE as moneyValue, wi.NAME as itemName, woTbl.FT_NAME as ftName, su.EMAIL ftEmailSysUser, su.POSITION_NAME ftPositionName, ");
	    sql.append(" woTbl.STATUS as status, woTbl.FINISH_DATE as finishDate ");
	    sql.append(", (select wc.CHECKLIST_NAME from wo_checklist wc left join wo w ON wc.WO_ID = w.ID  where wc.wo_id = woTbl.ID AND  w.WO_TYPE_ID = 261 AND wc.CHECKLIST_ORDER = (select MIN(CHECKLIST_ORDER) from wo_checklist where STATE = 'NEW' AND wo_id = woTbl.ID)) jobUnfinished ");
	    sql.append(" from WO woTbl ");
	    sql.append(" left join WO_TYPE wt on woTbl.WO_TYPE_ID = wt.ID " +
	            " left join CNT_CONTRACT cc on woTbl.contract_id = cc.CNT_CONTRACT_ID and cc.status > 0 " +
	            " left join APP_PARAM apr on to_char(woTbl.AP_WORK_SRC) = apr.CODE AND apr.PAR_TYPE = 'AP_WORK_SRC' " +
	            " left join APP_PARAM apr2 on to_char(woTbl.AP_CONSTRUCTION_TYPE) = apr2.CODE AND apr2.PAR_TYPE = 'AP_CONSTRUCTION_TYPE' " +
				" left join APP_PARAM apr3 on to_char(woTbl.AP_CONSTRUCTION_TYPE) = apr3.CODE AND apr3.PAR_TYPE = 'AVG_CHECKLIST' " +
	            " left join WORK_ITEM wi on woTbl.CONSTRUCTION_ID = wi.CONSTRUCTION_ID and woTbl.CAT_WORK_ITEM_TYPE_ID = wi.CAT_WORK_ITEM_TYPE_ID ");
	    sql.append(" LEFT JOIN WO_TR woTr ON wotbl.TR_ID = woTr.ID "); 
	    sql.append(" left join CAT_WORK_ITEM_TYPE cwit on woTbl.CAT_WORK_ITEM_TYPE_ID = cwit.CAT_WORK_ITEM_TYPE_ID ");
	    sql.append(" left join SYS_USER su on woTbl.FT_ID = su.SYS_USER_ID ");
	    sql.append(" LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP sg ON sg.SYS_GROUP_ID = woTbl.CD_LEVEL_2 ");
	    sql.append("  LEFT JOIN CTCT_COMS_OWNER.STATION_ELECTRICAL se ON woTbl.STATION_CODE=se.STATION_CODE  ");
	    sql.append(" WHERE woTbl.STATUS>0 AND  woTbl.WO_TYPE_ID=421");
	    
	    if(!woDto.getCheckUserKCQTD()) {
			sql.append(" AND sg.PROVINCE_ID = (SELECT PROVINCE_ID FROM SYS_GROUP where SYS_GROUP_ID = :sysGroupId) ");
		}
	    if(woDto.getCheckUserKCQTD() && woDto.getProvinceId() != null) {
			sql.append(" AND sg.PROVINCE_ID = :province ");
		}
		if(woDto.getStationCode() != null && !woDto.getStationCode().equals("")) {
			sql.append(" AND se.STATION_CODE like :stationCode ");
		}
		if(woDto.getManageUserCode() != null && !woDto.getManageUserCode().equals("")) {
			sql.append(" AND se.MANAGE_USER_CODE = :manageUserCode ");
		}
		if(woDto.getAreaCode() != null && !woDto.getAreaCode().equals("")) {
			sql.append(" AND se.AREA_CODE = :areaCode ");
		}
	    if(woDto.getType() != null && woDto.getType().equals("1")) {
	    	sql.append(" AND woTbl.finish_date < sysdate ");
	    }
	    if(woDto.getType() != null && woDto.getType().equals("2")) {
	    	sql.append(" AND woTbl.finish_date > sysdate ");
	    }
	    SQLQuery query = getSession().createSQLQuery(sql.toString());
	    StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
	    sqlCount.append(sql.toString());
	    sqlCount.append(")");
	    SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
	    if(!woDto.getCheckUserKCQTD()) {
			query.setParameter("sysGroupId", woDto.getSysGroupId());
			queryCount.setParameter("sysGroupId", woDto.getSysGroupId());
		}
	    if(woDto.getCheckUserKCQTD() && woDto.getProvinceId() != null) {
	    	query.setParameter("province", woDto.getProvinceId());
			queryCount.setParameter("province", woDto.getProvinceId());
		}
		if(woDto.getStationCode() != null && !woDto.getStationCode().equals("")) {
			query.setParameter("stationCode", "%" +woDto.getStationCode()+ "%");
			queryCount.setParameter("stationCode", "%" +woDto.getStationCode()+ "%");
		}
		if(woDto.getManageUserCode() != null && !woDto.getManageUserCode().equals("")) {
			query.setParameter("manageUserCode", woDto.getManageUserCode());
			queryCount.setParameter("manageUserCode", woDto.getManageUserCode());
		}
		if(woDto.getAreaCode() != null && !woDto.getAreaCode().equals("")) {
			query.setParameter("areaCode", woDto.getAreaCode());
			queryCount.setParameter("areaCode", woDto.getAreaCode());
		}
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		query.addScalar("woCode", new StringType());
	    query.addScalar("woName", new StringType());
	    query.addScalar("type", new StringType());
	    query.addScalar("apWorkSrcName", new StringType());
	    query.addScalar("trCode", new StringType());
	    query.addScalar("contractCode", new StringType());
	    query.addScalar("stationCode", new StringType());
	    query.addScalar("constructionCode", new StringType());
	    query.addScalar("moneyValue", new DoubleType());
	    query.addScalar("itemName", new StringType());
	    query.addScalar("ftName", new StringType());
	    query.addScalar("ftEmailSysUser", new StringType());
	    query.addScalar("ftPositionName", new StringType());
	    query.addScalar("status", new LongType());
	    query.addScalar("finishDate", new DateType());
	    query.addScalar("jobUnfinished", new StringType());
	    if (woDto.getPage() != null && woDto.getPageSize() != null) {
	        query.setFirstResult((woDto.getPage().intValue() - 1) * woDto.getPageSize().intValue());
	        query.setMaxResults(woDto.getPageSize().intValue());
	    }
	    woDto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
	    return query.list();
	}
	
	public Boolean checkUserKCQTD(String employeeCode) {
		StringBuilder sql = new StringBuilder(
				"SELECT sg.SYS_GROUP_ID sysGroupId, sg.CODE code, sg.NAME name, sg.PATH path, sg.GROUP_LEVEL groupLevel "
						+ "FROM CTCT_CAT_OWNER.SYS_GROUP sg "
						+ "INNER JOIN SYS_USER su ON su.SYS_GROUP_ID = sg.SYS_GROUP_ID "
						+ "WHERE (sg.PATH like '%/166571/280501/' "
						+ "OR sg.PATH like'%/166571/242656%' OR sg.PATH LIKE  '%/166571/280483%' OR sg.PATH LIKE '%/166571/166677%' "
						+ "OR sg.PATH LIKE '%/166571/270120%' OR sg.PATH LIKE '%166571/9006003%' OR sg.PATH LIKE '%/166571/166572/%' OR sg.PATH LIKE '%/166571/9006211/%' OR sg.PATH LIKE '%/166571/9006204/%'"
						+ "OR sg.PATH LIKE '%/166571/280501/%' OR sg.PATH LIKE '%/166571/9006211/%' OR sg.PATH LIKE '%/166571/9006203/%'"
						+ "OR sg.PATH LIKE '%/166571/9006198/%' OR sg.PATH LIKE '%/166571/9008861/%' OR sg.PATH LIKE '%/166571/9006197/%'"
						+ "OR sg.PATH LIKE '%/166571/9006204/%' OR sg.PATH LIKE '%/166571/9006210/%' OR sg.PATH LIKE '%/166571/9008188/%'"
						+ ") AND su.EMPLOYEE_CODE =:employeeCode ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("path", new StringType());
		query.addScalar("groupLevel", new StringType());
		query.setParameter("employeeCode", employeeCode);
		query.setResultTransformer(Transformers.aliasToBean(SysGroupDTO.class));
		List<SysGroupDTO> ls = query.list();
		if (ls.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<SysUserDTO> doSearchUserSysGroup(SysUserDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("select su.SYS_USER_ID userId, su.FULL_NAME fullName, su.EMPLOYEE_CODE employeeCode, su.EMAIL email ");
		sql.append(" from CTCT_VPS_OWNER.SYS_USER su ");
		sql.append(" left join CTCT_CAT_OWNER.SYS_GROUP sg ON su.SYS_GROUP_ID = sg.SYS_GROUP_ID ");
		sql.append(" where su.STATUS = 1 AND su.EMAIL is not null AND ROWNUM <50 ");
		if(obj.getName() != null && !obj.getName().equalsIgnoreCase("")) {
			sql.append(" AND (UPPER(su.FULL_NAME) like UPPER(:name) OR UPPER(su.EMPLOYEE_CODE) like UPPER(:name) OR UPPER(su.EMAIL) like UPPER(:name)) ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getName() != null && !obj.getName().equalsIgnoreCase("")) {
			query.setParameter("name", "%" + obj.getName() + "%");
		}
		
		query.addScalar("userId", new LongType());
		query.addScalar("fullName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("email", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
		return query.list();
	}
	
	public List<ElectricDetailDTO> getLuoiDienForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" select cp.NAME provinceName, cs.CODE stationCode, T1.ELECTRIC electric, T1.SUPPILER suppiler,  ");
		sql.append(" T1.ELECTRIC_QUOTA_CB_ELECTRIC_METER_A electricQuotaCBElectricMeterA,  ");
		sql.append(" T1.ELECTRIC_QUOTA_CB_STATION_A electricQuotaCBStationA, T1.DISTANCE distance,    ");
		sql.append(" T1.RATE_CAPACITY_STATION rateCapacityStation, T1.WIRE_TYPE wireType, T1.SECTION section  ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='LUOI_DIEN' ");
		sql.append(" left join ELECTRIC_DETAIL T1  ON  dse.DEVICE_ID =T1.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1  ");
		
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("provinceName", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("electric", new LongType());
		query.addScalar("suppiler", new StringType());
		query.addScalar("electricQuotaCBElectricMeterA", new LongType());
		query.addScalar("electricQuotaCBStationA", new LongType());
		query.addScalar("distance", new LongType());
		query.addScalar("rateCapacityStation", new LongType());
		query.addScalar("wireType", new LongType());
		query.addScalar("section", new LongType());
		
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.setResultTransformer(Transformers.aliasToBean(ElectricDetailDTO.class));
		return query.list();
	}
	
	public List<ElectricNotificationFilterDustDTO> getTHONGGIOForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" select cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" dse.DEVICE_NAME deviceName, t1.MANUFACTURER manufacturer, T1.MAXIMUM_CAPACITY maximumCapacity, ");
		sql.append(" case when T1.GOOD_CODE=1 then '2x2,5mm2' when T1.GOOD_CODE=2 then '2x4mm2' when T1.GOOD_CODE=3 then '2x6mm2' ");
		sql.append(" when T1.GOOD_CODE=4 then '2x10mm2' when T1.GOOD_CODE=5 then '2x16mm2' when T1.GOOD_CODE=6 then '2x25mm2' ");
		sql.append(" when T1.GOOD_CODE=7 then '2x35mm2' when T1.GOOD_CODE=8 then '2x50mm2' when T1.GOOD_CODE=9 then '2x70mm2' ");
		sql.append(" when T1.GOOD_CODE=10 then '3x10+6mm2' when T1.GOOD_CODE=11 then '3x16+10mm2' when T1.GOOD_CODE=12 then '3x25+16mm2' ");
		sql.append(" when T1.GOOD_CODE=13 then '3x35+16mm2' when T1.GOOD_CODE=14 then '3x50+25mm2' when T1.GOOD_CODE=15 then '3x70+35mm2' ");
		sql.append(" when T1.GOOD_CODE=16 then '4x10mm2' when T1.GOOD_CODE=17 then '4x11mm2' when T1.GOOD_CODE=18 then '4x16mm2' ");
		sql.append(" when T1.GOOD_CODE=19 then '4x25mm2' when T1.GOOD_CODE=20 then '4x35mm2' when T1.GOOD_CODE=21 then '4x50mm2' ");
		sql.append(" when T1.GOOD_CODE=22 then '4x70mm2' end goodCode, ");
		sql.append(" case when T1.GOOD_CODE=1 then '2x2,5mm2' when T1.GOOD_CODE=2 then '2x4mm2' when T1.GOOD_CODE=3 then '2x6mm2' ");
		sql.append(" when T1.GOOD_CODE=4 then '2x10mm2' when T1.GOOD_CODE=5 then '2x16mm2' when T1.GOOD_CODE=6 then '2x25mm2' ");
		sql.append("  when T1.GOOD_CODE=7 then '2x35mm2' when T1.GOOD_CODE=8 then '2x50mm2' when T1.GOOD_CODE=9 then '2x70mm2' ");
		sql.append(" when T1.GOOD_CODE=10 then '3x10+6mm2' when T1.GOOD_CODE=11 then '3x16+10mm2' when T1.GOOD_CODE=12 then '3x25+16mm2' ");
		sql.append(" when T1.GOOD_CODE=13 then '3x35+16mm2' when T1.GOOD_CODE=14 then '3x50+25mm2' when T1.GOOD_CODE=15 then '3x70+35mm2' ");
		sql.append(" when T1.GOOD_CODE=16 then '4x10mm2' when T1.GOOD_CODE=17 then '4x11mm2' when T1.GOOD_CODE=18 then '4x16mm2' ");
		sql.append(" when T1.GOOD_CODE=19 then '4x25mm2' when T1.GOOD_CODE=20 then '4x35mm2' when T1.GOOD_CODE=21 then '4x50mm2' ");
		sql.append(" when T1.GOOD_CODE=22 then '4x70mm2' end goodName,T1.STATE_KTTS stateKtts, T1.SERIAL serial, T1.STATE_ENFD stateENFD, T1.TIME_INTO_USE timeIntoUse, ");   
		sql.append(" T1.LAST_MAINTENANCE_TIME lastMaintenanceTime, T1.NEAREST_REPAIR_TIME nearestRepairTime,  ");
		sql.append(" T1.TOTAL_REPAIR_COST totalRepairCost, T1.TOTAL_NUMBER_FAILURES totalNumberFailures  ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='THONG_GIO' ");
		sql.append(" left join ELECTRIC_NOTIFICATION_FILTER_DUST T1  ON  dse.DEVICE_ID =T1.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1  ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("deviceName", new StringType());
		query.addScalar("manufacturer", new StringType());
		query.addScalar("maximumCapacity", new LongType());
		query.addScalar("goodCode", new StringType());
		query.addScalar("goodName", new StringType());
		query.addScalar("stateKtts", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("stateENFD", new LongType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("lastMaintenanceTime", new DateType());
		query.addScalar("nearestRepairTime", new DateType());
		query.addScalar("totalRepairCost", new LongType());
		query.addScalar("totalNumberFailures", new LongType());
		
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.setResultTransformer(Transformers.aliasToBean(ElectricNotificationFilterDustDTO.class));
		return query.list();
		
	}
	
	public List<ElectricExplosionFactoryDTO> getNMNForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" t1.HOUSE_TYPE houseType, t1.TIME_INTO_USE timeIntoUse,  ");
		sql.append(" t1.IGNITER_SETTING_STATUS igniterSettingStatus  ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='MAY_NO' ");
		sql.append(" left join ELECTRIC_EXPLOSION_FACTORY T1  ON  dse.DEVICE_ID =T1.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1  ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("igniterSettingStatus", new LongType());
		query.addScalar("houseType", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		query.setResultTransformer(Transformers.aliasToBean(ElectricExplosionFactoryDTO.class));
		return query.list();
	}
	
	public List<CabinetsSourceACDTO> getCabinetsSourceACForExport(StationElectricalDTO obj) {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" select cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" csa.CABINETS_SOURCE_NAME cabinetsSourceName, csa.PHASE_NUMBER phaseNumber, csa.STATUS status  ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='TU_NGUON_AC' ");
		sql.append(" left join CABINETS_SOURCE_AC csa  ON  dse.DEVICE_ID =csa.DEVICE_ID  ");
		sql.append(" WHERE 1=1 ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("phaseNumber", new LongType());
		query.addScalar("cabinetsSourceName", new StringType());
		query.addScalar("status", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(CabinetsSourceACDTO.class));
		return query.list();
	}
	
	public List<GeneratorDTO> getMayPhatForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode, ");
		sql.append(" t1.GENERATOR_NAME generatorName,t1.MANUFACTURER manufactuber, t1.WATTAGE_MAX wattageMax, ");
		sql.append(" t1.RATED_POWER ratedPower,t1.FUEL_TYPE fuelType, t1.SERIAL serial, ");
		sql.append(" t1.WORKSTATION_STATUS workstationStatus,  t1.DISTANCE_GENERATOR_STATION distanceGeneratorStation, ");
		sql.append(" t1.STATUS status, t1.TIME_INTO_USE timeIntoUse, t1.TOTAL_RUNNING_TIME totalRunningTime, ");
		sql.append(" t1.TIME_LAST_MAINTENANCE timeLastMaintenance , t1.LAST_REPAIR_TIME lastRepairTime, ");
		sql.append(" t1.TOTAL_REPAIR_COST totalRepairCost, t1.STATION_CODE_BEFORE_TRANSFER stationCodeBeforeTransfer, ");
		sql.append(" t1.TOTAL_NUMBER_FAILURES totalNumberFailures, t1.GENERATOR_OVER_CAPACITY generatorOverCapacity, ");
		sql.append(" t1.FT_CONFIRM_GENERATOR_OVER_CAPACITY ftConfirmGeneratorOverCapacity ");
		sql.append(" from STATION_ELECTRICAL a ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='MAY_PHAT'");
		sql.append(" left join GENERATOR T1  ON  dse.DEVICE_ID =T1.DEVICE_ID ");
		sql.append(" WHERE 1 = 1 ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.setResultTransformer(Transformers.aliasToBean(GeneratorDTO.class));
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("ratedPower", new LongType());
		query.addScalar("wattageMax", new LongType());
		query.addScalar("workstationStatus", new LongType());
		query.addScalar("distanceGeneratorStation", new LongType());
		query.addScalar("status", new LongType());
		query.addScalar("totalRunningTime", new LongType());
		query.addScalar("totalRepairCost", new LongType());
		query.addScalar("totalNumberFailures", new LongType());
		query.addScalar("generatorOverCapacity", new StringType());
		query.addScalar("ftConfirmGeneratorOverCapacity", new LongType());
		query.addScalar("generatorName", new StringType());
		query.addScalar("manufactuber", new StringType());
		query.addScalar("fuelType", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("stationCodeBeforeTransfer", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("timeLastMaintenance", new DateType());
		query.addScalar("lastRepairTime", new DateType());
		
		return query.list();
		
	}
	
	public List<ElectricLightningCutFilterDTO> getLocSetForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" t1.ELECTRIC_LIGHTNING_CUT_FILTER_NAME electricLightningCutFilterName,  ");
		sql.append(" t1.PRIMARY_STATUS primaryStatus, t1.PRIMARY_QUANTITY primaryQuantity,  ");
		sql.append(" t1.PRIMARY_CONDITION primaryCondition,t1.PRIMARY_SPECIES primarySpecies,  ");
		sql.append(" t1.RESISTOR resistor, t1.SECONDARY_STATUS secondaryStatus,  ");
		sql.append(" t1.SECONDARY_QUANTITY secondaryQuantity,t1.SECONDARY_CONDITION secondaryCodition, ");
		sql.append(" t1.SECONDARY_SPECIES secondarySpecies, t1.OTHER_LIGHTNING_CUT_FILTER_NAME otherLightningCutFilterName, ");
		sql.append(" t1.OTHER_LIGHTNING_CUT_FILTER_STATUS otherLightningCutFilterStatus,  ");
		sql.append(" t1.TIME_INTO_USE timeIntoUse,t1.LAST_MAINTENANCE_TIME lastMaintenanceTime, dse.STATE state  ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='LOC_SET' ");
		sql.append(" left join ELECTRIC_LIGHTNING_CUT_FILTER T1  ON  dse.DEVICE_ID =T1.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1  ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setResultTransformer(Transformers.aliasToBean(ElectricLightningCutFilterDTO.class));
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("electricLightningCutFilterName", new StringType());
		query.addScalar("primaryStatus", new LongType());
		query.addScalar("primaryQuantity", new LongType());
		query.addScalar("primaryCondition", new LongType());
		query.addScalar("primarySpecies", new StringType());
		query.addScalar("resistor", new LongType());
		query.addScalar("secondaryStatus", new LongType());
		query.addScalar("secondaryQuantity", new LongType());
		query.addScalar("secondaryCodition", new LongType());
		query.addScalar("state", new LongType());
		query.addScalar("otherLightningCutFilterStatus", new LongType());
		query.addScalar("secondarySpecies", new StringType());
		query.addScalar("otherLightningCutFilterName", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("lastMaintenanceTime", new DateType());
		
		return query.list();
	}
	
	public List<CabinetsSourceDCDTO> getCabinetsSourceDCForExport(StationElectricalDTO obj) {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" csd.RECFITER_NUMBER recfiterNumber, csd.PREVENTIVE preventtive, csd.CABINETS_SOURCE_DC_NAME cabinetsSourceDCName,  ");
		sql.append(" csd.STATE_CABINETS_SOURCE_DC stateCabinetsSourceDC, csd.POWER_CABINET_MONITORING powerCabinetMonitoring,  ");
		sql.append(" csd.NOT_CHARGE_THE_BATTERY notChargeTheBattery, csd.CHARGE_THE_BATTERY chargeTheBattery,  ");
		sql.append(" csd.CB_NUMBER_LESS_THAN_30A_UNUSED cbNumberLessThan30AUnused, csd.CB_NUMBER_GREATER_THAN_30A_UNUSED cbNumberGreaterThan30AUnused,  ");
		sql.append(" csd.CB_NYMBER_ADDITION cbNymberAddition, csd.STATE_RECTIFER stateRectifer, csd.QUANTITY_USE quantityUse,  ");
		sql.append(" csd.QUANTITY_ADDITION quantityAddition, csd.SERIAL sireal,  ");
		sql.append(" csd.DEVICE_MODEL numberDeviceModel, csd.STATE_MODULE stateModule,  dse.STATE state  ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='TU_NGUON_DC' ");
		sql.append(" left join CABINETS_SOURCE_DC csd  ON  dse.DEVICE_ID =csd.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1  ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("cabinetsSourceDCName", new StringType());
		query.addScalar("notChargeTheBattery", new StringType());
		query.addScalar("chargeTheBattery", new StringType());
		query.addScalar("preventtive", new LongType());
		query.addScalar("stateCabinetsSourceDC", new LongType());
		query.addScalar("powerCabinetMonitoring", new LongType());
		query.addScalar("cbNumberLessThan30AUnused", new LongType());
		query.addScalar("cbNumberGreaterThan30AUnused", new LongType());
		query.addScalar("cbNymberAddition", new LongType());
		query.addScalar("stateRectifer", new LongType());
		query.addScalar("quantityUse", new LongType());
		query.addScalar("quantityAddition", new LongType());
		query.addScalar("sireal", new StringType());
		query.addScalar("numberDeviceModel", new StringType());
		query.addScalar("stateModule", new LongType());
		query.addScalar("state", new LongType());
		query.addScalar("recfiterNumber", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(CabinetsSourceDCDTO.class));
		return query.list();
	}
	
	public List<BatteryDTO> getAcquyForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" t1.BATTERY_NAME batteryName, t1.GOOD_CODE goodCode, t1.MANUFACTURER manufactuber,  ");
		sql.append(" t1.MODEL model, t1.PRODUCTION_TECHNOLOGY productionTechnology, t1.CAPACITY capacity,  ");
		sql.append(" t1.TIME_INTO_USE timeIntoUse, t1.LAST_MAINTENANCE_TIME lastMaintenanceTime,  ");
		sql.append(" t1.STATION_OUTPUT_TIME_AFTER_RECOVERY stationOutputTimeAfterRecover  ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='AC_QUY' ");
		sql.append(" left join BATTERY T1  ON  dse.DEVICE_ID =T1.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1  ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("capacity", new LongType());
		query.addScalar("manufactuber", new StringType());
		query.addScalar("goodCode", new StringType());
		query.addScalar("productionTechnology", new StringType());
		query.addScalar("batteryName", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("stationOutputTimeAfterRecover", new DateType());
		query.addScalar("lastMaintenanceTime", new DateType());
		
		query.setResultTransformer(Transformers.aliasToBean(BatteryDTO.class));
		return query.list();
		
	}
	
	public List<ElectricEarthingSystemDTO> getTDForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" t1.ELECTRIC_EARTHING_SYSTEM_NAME electricEarthingSystemName,  ");
		sql.append(" t1.GROUNDING_STATUS groundStatus,t1.PRIMARY_CONDITION primaryCondition,  ");
		sql.append(" t1.LAST_MAINTENANCE_TIME lastMaintenanceTime,  ");
		sql.append(" t1.GROUND_RESISTANCE groundResistance, dse.STATE state ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='TIEP_DIA' ");
		sql.append(" left join ELECTRIC_EARTHING_SYSTEM T1  ON  dse.DEVICE_ID =T1.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1  ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("electricEarthingSystemName", new StringType());
		query.addScalar("groundResistance", new LongType());
		query.addScalar("groundStatus", new LongType());
		query.addScalar("primaryCondition", new LongType());
		query.addScalar("state", new LongType());
		query.addScalar("lastMaintenanceTime", new DateType());
		
		query.setResultTransformer(Transformers.aliasToBean(ElectricEarthingSystemDTO.class));
		return query.list();
	}
	
	public List<ElectricAirConditioningACDTO> getElectricAirConditioningACForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" t1.ELECTRIC_AIR_CONDITIONING_AC_NAME electricAirConditioningACName,  ");
		sql.append(" t1.STATE_AIR_CONDITIONING stateAirConditioning, t1.SERI_N_L seriNL,  ");
		sql.append(" t1.MAX_FIX_TME maxFixTme, t1.MANUFACTURER manufacturer,  ");
		sql.append(" t1.MODEL model, t1.GOOD_NAME goodName, t1.GOOD_CODE goodCode,  ");
		sql.append(" t1.GOOD_CODE_KTTS goodCodeKTTS, t1.TIME_INTO_USE timeIntoUse,  ");
		sql.append(" t1.LAST_MAJOR_MAINTENANCE_TIME lastMajorMaintenanceTime,  ");
		sql.append(" t1.MODEL_COLD_UNIT modelColdUnit, t1.MANUFACTURER_COLD_UNIT manufacturerColdUnit,  ");
		sql.append(" t1.GOOD_CODE_COLD_UNIT goodCodeColdUnit, t1.TYPE_COLD_UNIT typeCodeUnit,  ");
		sql.append(" t1.WATTAGE_DH_BTU wattageDHBTU, t1.WATTAGE_ELICTRONIC wattageElictronic,  ");
		sql.append(" t1.MODEL_HOT_UNIT modelHotUnit, t1.MANUFACTURER_HOT_UNIT manufacturerHotUnit,  ");
		sql.append(" t1.TOTAL_REPAIR_COST totalRepairCost, t1.TYPE_OF_GAS typeOfGas,  ");
		sql.append(" t1.TOTAL_NUMBER_FAILURES totalNumberFailures, dse.STATE state  ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='DIEU_HOA_AC' ");
		sql.append(" left join ELECTRIC_AIR_CONDITIONING_AC T1  ON  dse.DEVICE_ID =T1.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1  ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("electricAirConditioningACName", new StringType());
		query.addScalar("manufacturer", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("goodName", new StringType());
		query.addScalar("modelColdUnit", new StringType());
		query.addScalar("manufacturerColdUnit", new StringType());
		query.addScalar("goodCodeColdUnit", new StringType());
		query.addScalar("modelHotUnit", new StringType());
		query.addScalar("manufacturerHotUnit", new StringType());
		query.addScalar("typeOfGas", new StringType());
		query.addScalar("stateAirConditioning", new LongType());
		query.addScalar("seriNL", new StringType());
		query.addScalar("goodCode", new StringType());
		query.addScalar("goodCodeKTTS", new StringType());
		query.addScalar("typeCodeUnit", new LongType());
		query.addScalar("wattageDHBTU", new LongType());
		query.addScalar("wattageElictronic", new LongType());
		query.addScalar("totalRepairCost", new LongType());
		query.addScalar("totalNumberFailures", new LongType());
		query.addScalar("state", new LongType());
		query.addScalar("maxFixTme", new DateType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("lastMajorMaintenanceTime", new DateType());
		
		query.setResultTransformer(Transformers.aliasToBean(ElectricAirConditioningACDTO.class));
		return query.list();
	}
	
	public List<ElectricFireExtinguisherDTO> getCuuHoaForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode, ");
		sql.append(" t1.ELECTRIC_FIRE_EXTINGUISHER_NAME electricFireExtinguisherName, ");
		sql.append(" t1.ELECTRIC_FIRE_EXTINGUISHER_TYPE electricFireExtinguisherType, ");
		sql.append(" t1.ELECTRIC_FIRE_EXTINGUISHER_STATE electricFireExtinguisherState, ");
		sql.append(" t1.LAST_MAINTENANCE_TIME lastMaintenanceTime, ");
		sql.append(" t1.ELECTRIC_FIRE_EXTINGUISHER_LOCATION electricFireExtinguisherLocation, ");
		sql.append(" t1.WEIGHT weight,t1.TIME_INTO_USE timeIntoUse, dse.STATE state ");
		sql.append(" from STATION_ELECTRICAL a ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='CUU_HOA' ");
		sql.append(" left join ELECTRIC_FIRE_EXTINGUISHER T1  ON  dse.DEVICE_ID =T1.DEVICE_ID ");
		sql.append(" WHERE 1 = 1 ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("weight", new LongType());
		query.addScalar("electricFireExtinguisherName", new StringType());
		query.addScalar("electricFireExtinguisherType", new StringType());
		query.addScalar("electricFireExtinguisherState", new LongType());
		query.addScalar("electricFireExtinguisherLocation", new StringType());
		query.addScalar("state", new LongType());
		query.addScalar("lastMaintenanceTime", new DateType());
		query.addScalar("timeIntoUse", new DateType());
		
		query.setResultTransformer(Transformers.aliasToBean(ElectricFireExtinguisherDTO.class));
		return query.list();
		
	}
	
	public List<ElectricRectifierDTO> getRForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode, ");
		sql.append(" t1.SERIAL serial, t1.KTTS_STATUS kttsStatus, t1.GOOD_CODE goodCode, ");
		sql.append(" t1.GOOD_NAME goodName, t1.MODEL model, t1.MANUFACTURER manufacturer, ");
		sql.append(" t1.QUANTITY_CAN_ADDED quantitycanAdded, t1.QUANTITY_IN_USE quantityInUse, ");
		sql.append(" t1.RATED_POWER ratedPower, t1.TIME_INTO_USE timeIntoUse,  ");
		sql.append(" t1.STATE_ER stateER, dse.STATE state");
		sql.append(" from STATION_ELECTRICAL a ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='RECTIFITER' ");
		sql.append(" left join ELECTRIC_RECTIFIER T1  ON  dse.DEVICE_ID =T1.DEVICE_ID ");
		sql.append(" WHERE 1 = 1 ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("kttsStatus", new LongType());
		query.addScalar("goodCode", new StringType());
		query.addScalar("goodName", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("manufacturer", new StringType());
		query.addScalar("ratedPower", new LongType());
		query.addScalar("quantityInUse", new LongType());
		query.addScalar("quantitycanAdded", new LongType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("stateER", new LongType());
		query.addScalar("state", new LongType());
		
		query.setResultTransformer(Transformers.aliasToBean(ElectricRectifierDTO.class));
		return query.list();
	}
	
	public List<ElectricAirConditioningDCDTO> getDHDCforExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" t1.ELECTRIC_AIR_CONDITIONING_DC_NAME electricAirConditioningDcName,  ");
		sql.append(" t1.SERIAL serial, t1.TIME_INTO_USE timeIntoUse, t1.TYPE_GAS typeGas,  ");
		sql.append(" t1.STATE_EACD stateEACD, t1.MANUFACTURER manufacturer,  ");
		sql.append(" t1.MODEL model, t1.REFRIGERATION_CAPACITY refrigerationCapacity,  ");
		sql.append(" t1.POWER_CAPACITY powerCapacity, t1.GOOD_NAME goodName,  ");
		sql.append(" t1.GOOD_CODE goodCode, t1.GOOD_CODE_KTTS goodCodeKtts, dse.STATE state,  ");
		sql.append(" t1.LAST_MAINTENANCE_TIME lastMaintenanceTime, t1.NEAREST_REPAIR_TIME nearestRepairTime,  ");
		sql.append(" t1.TOTAL_REPAIR_COST totalRepairCost, t1.TOTAL_NUMBER_FAILURES totalNumberFailures  ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='DIEU_HOA_DC' ");
		sql.append(" left join ELECTRIC_AIR_CONDITIONING_DC T1  ON  dse.DEVICE_ID =T1.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1  ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("electricAirConditioningDcName", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("state", new LongType());
		query.addScalar("manufacturer", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("refrigerationCapacity", new LongType());
		query.addScalar("powerCapacity", new LongType());
		query.addScalar("goodName", new StringType());
		query.addScalar("goodCode", new StringType());
		query.addScalar("goodCodeKtts", new StringType());
		query.addScalar("lastMaintenanceTime", new DateType());
		query.addScalar("nearestRepairTime", new DateType());
		query.addScalar("totalRepairCost", new LongType());
		query.addScalar("totalNumberFailures", new LongType());
		query.addScalar("typeGas", new StringType());
		query.addScalar("stateEACD", new LongType());
		query.addScalar("timeIntoUse", new DateType());
		
		query.setResultTransformer(Transformers.aliasToBean(ElectricAirConditioningDCDTO.class));
		return query.list();
	}
	
	public List<ElectricWarningSystemDTO> getCanhBaoForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" t1.STATE_AC_BOX stateACBox, t1.STATUS_AC_BOX statusACBox,  ");
		sql.append(" t1.STATE_LOW_BATTERY stateLowBattery, t1.STATUS_LOW_BATTERY statusLowBattery,  ");
		sql.append(" t1.STATE_TEMPERATURE_WARNING stateTemperatureWarning, dse.STATE state,  ");
		sql.append(" t1.STATUS_TEMPERATURE_WARNING statusTemperatureWarning,  ");
		sql.append(" t1.STATE_SMOKE_WARNING stateSmokeWarning, t1.STATUS_SMOKE_WARNING statusSmokeWarning,  ");
		sql.append(" t1.STATE_POWER_CABINET_MALFUNCTION_WARNING statePowerCabinetMalfuntionWarning,  ");
		sql.append(" t1.STATUS_POWER_CABINET_MALFUNCTION_WARNING statusPowerCabinetMalfuntionWarning,  ");
		sql.append(" t1.STATE_EXPLOSIVE_FACTORY_OPEN_WARNING stateExplosiveFactoryOpenWarning,  ");
		sql.append(" t1.STATUS_EXPLOSIVE_FACTORY_OPEN_WARNING statusExplosiveFactoryOpenWarning,  ");
		sql.append(" t1.STATUS_STATION_OPEN_WARNING statusStationOpenWarning, ");
		sql.append(" t1.STATE_STATION_OPEN_WARNING stateStationOpenWarning,  ");
		sql.append(" t1.STATE_LOW_AC stateLowAC,t1.STATUS_LOW_AC statusLowAC  ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='CANH_BAO' ");
		sql.append(" left join ELECTRIC_WARNING_SYSTEM T1  ON  dse.DEVICE_ID =T1.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1  ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("stateACBox", new LongType());
		query.addScalar("statusACBox", new LongType());
		query.addScalar("stateLowBattery", new LongType());
		query.addScalar("statusLowBattery", new LongType());
		query.addScalar("stateTemperatureWarning", new LongType());
		query.addScalar("statusTemperatureWarning", new LongType());
		query.addScalar("stateSmokeWarning", new LongType());
		query.addScalar("statusSmokeWarning", new LongType());
		query.addScalar("statePowerCabinetMalfuntionWarning", new LongType());
		query.addScalar("statusPowerCabinetMalfuntionWarning", new LongType());
		query.addScalar("stateExplosiveFactoryOpenWarning", new LongType());
		query.addScalar("statusExplosiveFactoryOpenWarning", new LongType());
		query.addScalar("stateStationOpenWarning", new LongType());
		query.addScalar("statusStationOpenWarning", new LongType());
		query.addScalar("stateLowAC", new LongType());
		query.addScalar("statusLowAC", new LongType());
		query.addScalar("state", new LongType());
		
		query.setResultTransformer(Transformers.aliasToBean(ElectricWarningSystemDTO.class));
		return query.list();
	}
	
	public List<StationInformationDTO> getNhaTramForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" t1.MANAGER manager, t1.ADDRESS address, t1.STATION_TYPE stationType,  ");
		sql.append(" t1.STATION_HOUSE_TYPE stationHouseType, t1.STATION_HOUSE_SIZE stationHouseSize,  ");
		sql.append(" t1.WATTAGE_MAX wattageMax, t1.WATTAGE_MAX_ACQUY wattageMaxAcquy, t1.WATTAGE_ACQUY wattageAcquy,  ");
		sql.append(" t1.WATTAGE_AIR_CONDITIONING wattageAirConditioning, t1.WATTAGE_DUST_FILTER wattageDustFilter,  ");
		sql.append(" t1.WATTAGE_HEAD_EXCHANGERS wattageHeadExchangers, t1.VT_WATTAGE_TV vtWattageTV,  ");
		sql.append(" t1.VT_WATTAGE_TRANSMISSION vtWattageTransmission, t1.VT_WATTAGE_IP vtWattageIP,  ");
		sql.append(" t1.VT_WATTAGE_CDBR vtWattageCDBR, t1.VT_WATTAGE_PSTN vtWattagePSTN,  ");
		sql.append(" t1.VNP_WATTAGE_TV vnpWattageTV, t1.VNP_WATTAGE_TRANSMISSION vnpWattageTransmission,  ");
		sql.append(" t1.VNP_WATTAGE_IP vnpWattageIP, t1.VNP_WATTAGE_CDBR vnpWattageCDBR,  ");
		sql.append(" t1.VNP_WATTAGE_PSTN vnpWattagePSTN, t1.VNM_WATTAGE_TV vnmWattageTV,  ");
		sql.append(" t1.VNM_WATTAGE_TRANSMISSION vnmWattageTransmission, t1.VNM_WATTAGE_IP vnmWattageIP,  ");
		sql.append(" t1.VNM_WATTAGE_CDBR vnmWattageCDBR, t1.VNM_WATTAGE_PSTN vnmWattagePSTN,  ");
		sql.append(" t1.MBP_WATTAGE_TV mbpWattageTV, t1.MBP_WATTAGE_TRANSMISSION mbpWattageTransmission,  ");
		sql.append(" t1.MBP_WATTAGE_IP mbpWattageIP, t1.MBP_WATTAGE_CDBR mbpWattageCDBR,  ");
		sql.append(" t1.MBP_WATTAGE_PSTN mbpWattagePSTN,dse.STATE state ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='NHA_TRAM' ");
		sql.append(" left join STATION_INFORMATION T1  ON  dse.DEVICE_ID =T1.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1  ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("manager", new StringType());
		query.addScalar("address", new StringType());
		query.addScalar("stationType", new StringType());
		query.addScalar("stationHouseType", new StringType());
		query.addScalar("stationHouseSize", new StringType());
		query.addScalar("wattageMax", new LongType());
		query.addScalar("wattageMaxAcquy", new LongType());
		query.addScalar("wattageAcquy", new LongType());
		query.addScalar("wattageAirConditioning", new LongType());
		query.addScalar("wattageDustFilter", new LongType());
		query.addScalar("wattageHeadExchangers", new LongType());
		query.addScalar("vtWattageTV", new LongType());
		query.addScalar("vtWattageTransmission", new LongType());
		query.addScalar("vtWattageIP", new LongType());
		query.addScalar("vtWattageCDBR", new LongType());
		query.addScalar("vtWattagePSTN", new LongType());
		query.addScalar("vnpWattageTV", new LongType());
		query.addScalar("vnpWattageTransmission", new LongType());
		query.addScalar("vnpWattageIP", new LongType());
		query.addScalar("vnpWattageCDBR", new LongType());
		query.addScalar("vnpWattagePSTN", new LongType());
		query.addScalar("vnmWattageTV", new LongType());
		query.addScalar("vnmWattageTransmission", new LongType());
		query.addScalar("vnmWattageIP", new LongType());
		query.addScalar("vnmWattageCDBR", new LongType());
		query.addScalar("vnmWattagePSTN", new LongType());
		query.addScalar("mbpWattageTV", new LongType());
		query.addScalar("mbpWattageTransmission", new LongType());
		query.addScalar("mbpWattageIP", new LongType());
		query.addScalar("mbpWattageCDBR", new LongType());
		query.addScalar("mbpWattagePSTN", new LongType());
		query.addScalar("state", new LongType());
		
		query.setResultTransformer(Transformers.aliasToBean(StationInformationDTO.class));
		return query.list();
	}
	
	public List<ElectricHeatExchangerDTO> getNHIETForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" select cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" t1.DIVICE_NAME deviceName, t1.STATE_EHE stateEHE, t1.SERIAL serial,  ");
		sql.append(" t1.WATTAGE wattage, t1.MANUFACTURER manufacturer, t1.MODEL model,   ");
		sql.append(" t1.GOOD_CODE goodCode, t1.GOOD_NAME goodName,  ");
		sql.append(" t1.GOOD_CODE_KTTS goodCodeKtts, t1.TIME_INTO_USE timeIntoUse,  ");
		sql.append(" t1.LAST_MAJOR_MAINTENANCE_TIME lastMajorMaintenanceTime, dse.STATE state  ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='NHIET' ");
		sql.append(" left join ELECTRIC_HEAT_EXCHANGER T1  ON  dse.DEVICE_ID =T1.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1  ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("deviceName", new StringType());
		query.addScalar("stateEHE", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("wattage", new LongType());
		query.addScalar("manufacturer", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("goodCode", new StringType());
		query.addScalar("goodName", new StringType());
		query.addScalar("goodCodeKtts", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("lastMajorMaintenanceTime", new DateType());
		query.addScalar("state", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(ElectricHeatExchangerDTO.class));
		return query.list();
		
	}
	
	public List<ElectricATSDTO> getATSForExport(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT cp.AREA_CODE areaCode, cp.CODE provinceCode, null districtCode, cs.CODE stationCode,  ");
		sql.append(" t1.GOOD_CODE_KTTS goodCodeKTTS, t1.PHASE_NUMBER phaseNumber,  ");
		sql.append(" t1.GOOD_NAME goodName,t1.GOOD_ELECTRIC_NAME goodElectricName,t1.STATE_EA stateEA,  ");
		sql.append(" t1.MODEL model, t1.SERIAL serial, dse.STATE state, t1.MANUFACTURER manufacturer, ");
		sql.append(" t1.TIME_INTO_USE timeIntoUse,t1.LAST_MAINTENANCE_TIME lastMaintenanceTime, t1.ELECTRIC_QUOTA electricQuota  ");
		sql.append(" from STATION_ELECTRICAL a  ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ON a.STATION_ID = cs.CAT_STATION_ID and cs.status=1 ");
		sql.append(" inner JOIN CTCT_CAT_OWNER.CAT_PROVINCE cp ON cp.CAT_PROVINCE_ID  =cs.CAT_PROVINCE_ID and cp.status=1 ");
		sql.append(" LEFT JOIN DEVICE_STATION_ELECTRICAL dse ON a.STATION_ID = dse.STATION_ID and dse.type='ATS' ");
		sql.append(" left join ELECTRIC_ATS T1  ON  dse.DEVICE_ID =T1.DEVICE_ID  ");
		sql.append(" WHERE 1 = 1 ");
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			sql.append(" AND cs.CODE like :code ");
		}
		if(obj.getProvinceId() != null) {
			sql.append(" AND cs.CAT_PROVINCE_ID = :provinceId ");
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			sql.append(" AND cp.AREA_CODE like :areaCode ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(obj.getStationCode() != null && !(obj.getStationCode().equals(""))) {
			query.setParameter("code", "%" + obj.getStationCode() + "%");
		}
		if(obj.getProvinceId() != null) {
			query.setParameter("provinceId", obj.getProvinceId());
		}
		if(obj.getAreaCode() != null && !(obj.getAreaCode().equals(""))) {
			query.setParameter("areaCode", obj.getAreaCode());
		}
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("goodCodeKTTS", new StringType());
		query.addScalar("phaseNumber", new LongType());
		query.addScalar("stateEA", new LongType());
		query.addScalar("lastMaintenanceTime", new DateType());
		query.addScalar("state", new LongType());
		query.addScalar("goodName", new StringType());
		query.addScalar("goodElectricName", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("model", new StringType());
		query.addScalar("manufacturer", new StringType());
		query.addScalar("timeIntoUse", new DateType());
		query.addScalar("electricQuota", new LongType());
		
		query.setResultTransformer(Transformers.aliasToBean(ElectricATSDTO.class));
		return query.list();
	}
	
	
	//ducpm
	public List<WoDTO> getManagerList(WoDTO obj){
		String sqlString = "SELECT" + 
				" se.STATION_CODE stationCode,cs.ADDRESS address,se.MANAGE_USER_NAME manageUserName, w.WO_CODE woCode, dse.DEVICE_NAME deviceName, dse.SERIAL serial," + 
				"  w.CREATED_DATE createdDate, w.FINISH_DATE finishDate, w.UPDATE_CD_LEVEL3_RECEIVE_WO updateCdLevel3ReceiveWo, w.UPDATE_FT_RECEIVE_WO updateFtReceiveWo," + 
				"  w.UPDATE_CD_APPROVE_WO updateCdApproveWo, w.END_TIME endTime, w.STATE state," + 
				"  (CASE" + 
				"  WHEN w.STATE = 'NG'" + 
				"  THEN (select wl.CONTENT from WO_WORKLOGS wl where w.ID=wl.WO_ID)" + 
				"  ELSE NULL" + 
				"  END) overdueReason" + 
				" FROM STATION_ELECTRICAL se left join DEVICE_STATION_ELECTRICAL dse on se.STATION_ID=dse.STATION_ID" + 
				" left join CTCT_CAT_OWNER.CAT_STATION cs on se.STATION_ID=cs.CAT_STATION_ID" + 
				" left join WO w on se.STATION_CODE=w.STATION_CODE" + 
				" left join WO_TYPE wt on w.WO_TYPE_ID=wt.ID"+
				" where 1=1";
//				"where wt.WO_TYPE_CODE='BDTHTCT';";
		
		StringBuilder sql = new StringBuilder(sqlString);
		
		if(obj.getStationCode() != null && !obj.getStationCode().equals("")) {
			sql.append(" AND se.STATION_CODE like :stationCode ");
		}
		
		if(obj.getWoCode() != null && !obj.getWoCode().equals("")) {
			sql.append(" AND w.WO_CODE LIKE :woCode");
		}
		if(obj.getDeviceName() != null && !obj.getDeviceName().equals("")) {
			sql.append(" AND dse.DEVICE_NAME LIKE :deviceName");
		}
		if(obj.getManageUserName() != null) {
			sql.append(" AND se.MANAGE_USER_NAME = :manageUserName");
		}
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT (*) FROM ( ");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		//System.out.println(sqlCount.toString());
		// Thực hiện query để lấy dữ liệu record
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		// Thực hiện query để lấy số bản ghi
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		if(obj.getStationCode() != null && !obj.getStationCode().equals("")) {
			query.setParameter("stationCode", "%" + obj.getStationCode() + "%");
			queryCount.setParameter("stationCode", "%" + obj.getStationCode() + "%");
		} 
		
		if(obj.getWoCode() != null && !obj.getWoCode().equals("")) {
			query.setParameter("woCode", "%" + obj.getWoCode()+"%");
			queryCount.setParameter("woCode", "%" + obj.getWoCode()+"%");
		}
		
		
		if(obj.getDeviceName() != null && !obj.getDeviceName().equals("")) {
			query.setParameter("deviceName", "%" + obj.getDeviceName()+"%");
			queryCount.setParameter("deviceName", "%" + obj.getDeviceName()+"%");
		}
		
		if(obj.getManageUserName() != null) {
			query.setParameter("manageUserName", obj.getManageUserName());
			queryCount.setParameter("manageUserName", obj.getManageUserName());
		}
		
		query.addScalar("stationCode", new StringType());
		query.addScalar("address", new StringType());
		query.addScalar("deviceName", new StringType());
		query.addScalar("manageUserName", new StringType());
		query.addScalar("woCode", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("finishDate", new DateType());
		query.addScalar("updateCdLevel3ReceiveWo", new DateType());
		query.addScalar("updateFtReceiveWo", new DateType());
		query.addScalar("updateCdApproveWo", new DateType());
		query.addScalar("endTime", new DateType());
		query.addScalar("state", new StringType());
		query.addScalar("overdueReason", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		
		// set kết quả trả về theo phân trang
		if(obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		return query.list();
	}
	
	public void sendMailApprove(DeviceStationElectricalDTO obj, Long userId) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("select su.EMAIL email, cs.CODE name ");
		sql.append(" from DEVICE_STATION_ELECTRICAL dse ");
		sql.append(" LEFT JOIN CTCT_VPS_OWNER.SYS_USER su ON dse.CREATE_USER = su.SYS_USER_ID ");
		sql.append(" LEFT JOIN CTCT_CAT_OWNER.CAT_STATION cs ON dse.STATION_ID = cs.CAT_STATION_ID ");
		sql.append(" where dse.DEVICE_ID = :deviceId ");
		

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("email", new StringType());
		query.addScalar("name", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
		query.setParameter("deviceId", obj.getDeviceId());
		List<SysUserDTO> lst = query.list();
		
		if(lst.size() > 0) {
			SendEmailBO mailBO = new SendEmailBO();
			mailBO.setStatus(0l);
			mailBO.setSubject("Phê duyệt thiết bị cơ điện");
			mailBO.setContent("Yêu cầu phê duyệt thiết bị" + obj.getType() + ", trạm " + lst.get(0).getName() + " của đồng chí đã được phê duyệt" + ". Phần mềm COMS");
			mailBO.setReceiveEmail(lst.get(0).getEmail());
			mailBO.setCreatedDate(new Date());
			mailBO.setCreatedUserId(userId);
			sendEmailDAO.saveObject(mailBO);
		}
	}

	public void sendMailReject(DeviceStationElectricalDTO obj, Long userId) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("select su.EMAIL email, cs.CODE name ");
		sql.append(" from DEVICE_STATION_ELECTRICAL dse ");
		sql.append(" LEFT JOIN CTCT_VPS_OWNER.SYS_USER su ON dse.CREATE_USER = su.SYS_USER_ID ");
		sql.append(" LEFT JOIN CTCT_CAT_OWNER.CAT_STATION cs ON dse.STATION_ID = cs.CAT_STATION_ID ");
		sql.append(" where dse.DEVICE_ID = :deviceId ");
		

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("email", new StringType());
		query.addScalar("name", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
		query.setParameter("deviceId", obj.getDeviceId());
		List<SysUserDTO> lst = query.list();
		
		if(lst.size() > 0) {
			SendEmailBO mailBO = new SendEmailBO();
			mailBO.setStatus(0l);
			mailBO.setSubject("Phê duyệt thiết bị cơ điện");
			mailBO.setContent("Yêu cầu phê duyệt thiết bị" + obj.getType() + ", trạm " + lst.get(0).getName() + " của đồng chí đã bị từ chối" + ". Phần mềm COMS");
			mailBO.setReceiveEmail(lst.get(0).getEmail());
			mailBO.setCreatedDate(new Date());
			mailBO.setCreatedUserId(userId);
			sendEmailDAO.saveObject(mailBO);
		}
	}

	public List<UserDirectoryDTO> getUserDirectoryList(UserDirectoryDTO obj){
		String basicQuery = "SELECT" + 
							" ud.ID id, ud.LOGIN_NAME loginName, ud.FULL_NAME fullName, ud.PROVINCE_CODE provinceCode, ud.POSITION position ,ud.UNIT_NAME unitName, ud.PHONE phone"+
							" FROM USER_DIRECTORY ud" +
							" WHERE 1=1";
		StringBuilder sql = new StringBuilder(basicQuery);
		//Them dieu kiem tim kiem
		if(obj.getLoginName() != null && !obj.getLoginName().equals("")) {
			sql.append(" AND UPPER(ud.LOGIN_NAME) like UPPER(:loginName)");
		}
		
		if(obj.getFullName() != null && !obj.getFullName().equals("")) {
			sql.append(" AND UPPER(ud.FULL_NAME) like UPPER(:fullName)");
		}
		
		if(obj.getPhone() != null) {
			sql.append(" AND TO_CHAR(PHONE) like :phone");
		}
		
		if(obj.getUnitName() != null && !obj.getUnitName().equals("")) {
			sql.append(" AND UPPER(ud.UNIT_NAME) like UPPER(:unitName)");
		}
		
		sql.append(" ORDER BY ud.ID DESC");
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT (*) FROM ( ");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		
		//tao SQL
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery countQuery = getSession().createSQLQuery(sqlCount.toString());
		
		// add param
		if(obj.getLoginName() != null && !obj.getLoginName().equals("")) {
			query.setParameter("loginName", "%"+obj.getLoginName()+"%");
			countQuery.setParameter("loginName", "%"+obj.getLoginName()+"%");
		}
		
		if(obj.getFullName() != null && !obj.getFullName().equals("")) {
			query.setParameter("fullName", "%"+obj.getFullName()+"%");
			countQuery.setParameter("fullName", "%"+obj.getFullName()+"%");
		}
		
		if(obj.getPhone() != null) {
			query.setParameter("phone", "%"+obj.getPhone()+"%");
			countQuery.setParameter("phone", "%"+obj.getPhone()+"%");
		}
		
		if(obj.getUnitName() != null && !obj.getUnitName().equals("")) {
			query.setParameter("unitName", "%"+obj.getUnitName()+"%");
			countQuery.setParameter("unitName", "%"+obj.getUnitName()+"%");
		}
		
		
		//add Scalar
		query.addScalar("id", new LongType());
		query.addScalar("loginName", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("position", new StringType());
		query.addScalar("unitName", new StringType());
		query.addScalar("phone", new LongType());
		
		query.setResultTransformer(Transformers.aliasToBean(UserDirectoryDTO.class));
		obj.setTotalRecord(((BigDecimal) countQuery.uniqueResult()).intValue());
		
		// set kết quả trả về theo phân trang
		if(obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		return query.list();		
	}
	
	public List<UserDirectoryDTO>getDoSearchUserDirectoryList(UserDirectoryDTO obj){
		String basicQuery = "SELECT" + 
				" su.LOGIN_NAME loginName, su.FULL_NAME fullName, su.EMAIL email, nvl(sg.GROUP_NAME_LEVEL3,sg.GROUP_NAME_LEVEL2) unitName, su.PHONE_NUMBER phone, sg.PROVINCE_CODE provinceCode"+
				" FROM CTCT_VPS_OWNER.SYS_USER su" +
				" LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP sg ON su.SYS_GROUP_ID = sg.SYS_GROUP_ID"+
				" WHERE su.STATUS = 1 AND su.EMAIL is not null  AND su.EMAIL != 'null' AND ROWNUM < 50";
		StringBuilder sql = new StringBuilder(basicQuery);
		//Them dieu kiem tim kiem
		if(obj.getLoginName() != null && !obj.getLoginName().equals("")) {
			sql.append(" AND su.LOGIN_NAME= :loginName");
		}
		if(obj.getEmail() != null && !obj.getEmail().equals("")) {
			sql.append(" AND (UPPER(su.LOGIN_NAME) like UPPER(:email) OR UPPER(su.EMAIL) like UPPER(:email)) ");
		}
		
		//tao SQL
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		// add param
		if(obj.getLoginName() != null && !obj.getLoginName().equals("")) {
			query.setParameter("loginName", obj.getLoginName());
		}
		
		if(obj.getEmail() != null && !obj.getEmail().equals("")) {
			query.setParameter("email", "%"+obj.getEmail()+"%");
		}
		
		//add Scalar
		query.addScalar("loginName", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("unitName", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("phone", new LongType());
		
		query.setResultTransformer(Transformers.aliasToBean(UserDirectoryDTO.class));
		
		return query.list();	
	}
	
	public void removeUserDirectory(UserDirectoryDTO obj) {
		String sql = "DELETE FROM USER_DIRECTORY"
					+" WHERE ID= :id";
	
		SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", obj.getId());
        query.executeUpdate();
	}
	
	public List<UnitListDTO> getUnitList(UnitListDTO obj){
		String basicQuery = "SELECT" + 
							" ul.ID id, ul.UNIT_CODE unitCode, ul.UNIT_NAME unitName"+
							" FROM UNIT_LIST ul" +
							" WHERE 1=1";
		StringBuilder sql = new StringBuilder(basicQuery);
		
		//Thêm điều kiện tìm kiếm
		if(obj.getUnitName() != null && !obj.getUnitName().equals("")) {
			sql.append(" AND UPPER(ul.UNIT_NAME) like UPPER(:unitName) OR UPPER(ul.UNIT_CODE) LIKE UPPER(:unitName)");
		}
		
		if(obj.getUnitCode() != null && !obj.getUnitCode().equals("")) {
			sql.append(" AND ul.UNIT_CODE = :unitCode");
		}
		sql.append(" ORDER BY ul.ID DESC");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT (*) FROM ( ");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		
		//tao SQL
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery countQuery = getSession().createSQLQuery(sqlCount.toString());
		
		// add param
		if(obj.getUnitName() != null && !obj.getUnitName().equals("")) {
			query.setParameter("unitName", "%"+obj.getUnitName()+"%");
			countQuery.setParameter("unitName", "%"+obj.getUnitName()+"%");
		}
		
		if(obj.getUnitCode() != null && !obj.getUnitCode().equals("")) {
			query.setParameter("unitCode", obj.getUnitCode());
			countQuery.setParameter("unitCode", obj.getUnitCode());
		}
		
		//add Scalar
		query.addScalar("id", new LongType());
		query.addScalar("unitCode", new StringType());
		query.addScalar("unitName", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(UnitListDTO.class));
		obj.setTotalRecord(((BigDecimal) countQuery.uniqueResult()).intValue());
		
		// set kết quả trả về theo phân trang
		if(obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		return query.list();		
	}
	
	public List<UnitListDTO> doSearchUnit(UnitListDTO obj){
		String basicSql = "SELECT "
						+ " cat.AREA_CODE areaCode, cat.GROUP_NAME_LEVEL3 unitName, cat.CODE unitCode"
						+ " FROM CTCT_CAT_OWNER.SYS_GROUP cat"
						+ " WHERE ROWNUM < 50 AND cat.GROUP_NAME_LEVEL3 IS NOT NULL";
		StringBuilder sql = new StringBuilder(basicSql);
		
		if(obj.getAreaCode() != null) {
			sql.append(" AND cat.AREA_CODE = :areaCode");
		}
		
		if(obj.getUnitName() != null && !obj.getUnitName().equals("")) {
			sql.append(" AND UPPER(cat.GROUP_NAME_LEVEL3) LIKE UPPER(:unitName) OR UPPER(cat.CODE) LIKE UPPER(:unitName)");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		

		if(obj.getAreaCode() != null) {
			query.setParameter("areaCode", "%"+obj.getAreaCode()+"%");
		}
		
		if(obj.getUnitName() != null && !obj.getUnitName().equals("")) {
			query.setParameter("unitName", "%"+obj.getUnitName()+"%");
		}
		
		//add Scalar
		query.addScalar("areaCode", new StringType());
		query.addScalar("unitCode", new StringType());
		query.addScalar("unitName", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(UnitListDTO.class));
		return query.list();
	}
	
	public void removeUnitList(UnitListDTO obj) {
		String sql = "DELETE FROM UNIT_LIST"
					+" WHERE ID= :id";
	
		SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", obj.getId());
        query.executeUpdate();
	}
	
	public List<DocManagementDTO> getDocManagementList(DocManagementDTO obj){
		String basicSql = "SELECT "
				+ " dm.ID id, dm.FIELD field, dm.DOCUMENT_TYPE documentType, dm.DOCUMENT_CODE documentCode,"
				+ " dm.DATE_ISSUED dateIssued, dm.DESCRIPTION description, dm.ISSUING_UNIT issuingUnit"
				+ " FROM DOCUMENT_MANAGEMENT dm "
				+ " WHERE 1 = 1 ";
		StringBuilder sql = new StringBuilder(basicSql);
		// add condition
		if(obj.getDocumentCode() != null && !obj.getDocumentCode().equals("")) {
			sql.append(" AND dm.DOCUMENT_CODE = :documentCode ");
		}
		sql.append(" ORDER BY dm.ID DESC");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM( ");
		sqlCount.append(sql);
		sqlCount.append(")");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery countQuery = getSession().createSQLQuery(sqlCount.toString());
		
		//add praram
		if(obj.getDocumentCode() != null && !obj.getDocumentCode().equals("")) {
			query.setParameter("documentCode", obj.getDocumentCode());
			countQuery.setParameter("documentCode", obj.getDocumentCode());
		}
		
		query.addScalar("id", new LongType());
		query.addScalar("field", new StringType());
		query.addScalar("documentType", new StringType());
		query.addScalar("documentCode", new StringType());
		query.addScalar("description", new StringType());
		query.addScalar("issuingUnit", new StringType());
		query.addScalar("dateIssued", new DateType());
		
		query.setResultTransformer(Transformers.aliasToBean(DocManagementDTO.class));
		obj.setTotalRecord(((BigDecimal) countQuery.uniqueResult()).intValue());
		
		// set kết quả trả về theo phân trang
		if(obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		return query.list();			
	}
	
	public void removeDocManagement(DocManagementDTO obj) {
		String sql = "DELETE FROM DOCUMENT_MANAGEMENT"
					+" WHERE ID= :id";
	
		SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", obj.getId());
        query.executeUpdate();
        
		sql = "DELETE FROM CTCT_CAT_OWNER.UTIL_ATTACH_DOCUMENT ad"
				+" WHERE ad.TYPE = 'DOCUMENT_MANAGEMENT' AND ad.OBJECT_ID=:id";

		 query = getSession().createSQLQuery(sql.toString());
		 query.setParameter("id", obj.getId());
		 query.executeUpdate();
	}
	//ducpm-end
}

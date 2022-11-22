package com.viettel.coms.dao;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.viettel.coms.bo.EffectiveCalculateBTSBO;
import com.viettel.coms.bo.EffectiveCalculateDASCDBRBO;
import com.viettel.coms.bo.EffectiveCalculateDasBO;
import com.viettel.coms.dto.EffectiveCalculateBTSDTO;
import com.viettel.coms.dto.EffectiveCalculateDASCDBRDTO;
import com.viettel.coms.dto.EffectiveCalculateDASCDBRBTSRequest;
import com.viettel.coms.dto.EffectiveCalculateDasDTO;
import com.viettel.coms.dto.HolidayRequest;
import com.viettel.coms.dto.SysUserRequest;
import com.viettel.coms.dto.UserHolidayDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Repository("effectiveCalculateBTSDAO")
public class EffectiveCalculateBTSDAO extends BaseFWDAOImpl<EffectiveCalculateBTSBO, Long> {
	
	public EffectiveCalculateBTSDAO() {
		this.model = new EffectiveCalculateBTSBO();
	}

	public EffectiveCalculateBTSDAO(Session session) {
		this.session = session;
	}
  
	public List<EffectiveCalculateBTSDTO> getListEffectiveCalculateBTS(SysUserRequest request) {
		StringBuilder stringBuilder = new StringBuilder("select CAT_PROVINCE_ID catProvinceId,CAT_PROVINCE_CODE catProvinceCode,CAT_PROVINCE_NAME catProvinceName,LATITUDE latitude, ");
		stringBuilder.append(" LONGITUDE longitude,HIGHT_BTS hightBts,COLUMN_TYPE columnType,TOPOGRAPHIC topographic,LOCATION location,ADDRESS address, ");
		stringBuilder.append(" TYPE_STATION typeStation,MNO_VIETTEL mnoViettel,MNO_VINA mnoVina,MNO_MOBILE mnoMobi,OURCE_DEPLOYMENT ourceDeployment, ");
		stringBuilder.append(" LECT_DEPRECIATION_PERIOD lectDepreciationPeriod,SILK_ENTER_PRICE SilkEnterPrice,PRICE price,COST_MB costMB,COLUMN_FOUNDATION_ITEMS columnFoundationItems, ");
		stringBuilder.append(" COST_COLUMN_FOUNDATION_ITEMS costColumnFoundationItems,HOUSE_FOUNDATION_ITEMS houseFoundationItems,COST_HOUSE_FOUNDATION_ITEMS costHouseFoundationItems, ");
		stringBuilder.append(" COLUMN_BODY_CATEGORY columnBodyCategory,COST_COLUMN_BODY_CATEGORY costColumnBodyCategory,MACHINE_ROOM_ITEMS machineRoomItems,COST_MACHINE_ROOM_ITEMS costMachineRoomItems,");
		stringBuilder.append(" GROUNDING_ITEMS groundingItems,COST_GROUNDING_ITEMS costGroundingItems,ELECTRIC_TOWING_ITEMS electricTowingItems,COST_ELECTRIC_TOWING_ITEMS costElectricTowingItems,");
		stringBuilder.append(" COLUMN_MOUNTING_ITEM columnMountingItem,COST_COLUMN_MOUNTING_ITEM costColumnMountingItem,INSTALLATION_HOUSES installationHouses,COST_INSTALLATION_HOUSES costInstallationHouses,");
		stringBuilder.append(" ELECTRICAL_ITEMS electricalItems,COST_ELECTRICAL_ITEMS costElectricalItems,MOTORIZED_TRANSPORT_ITEMS motorizedTransportItems,COST_MOTORIZED_TRANSPORT_ITEMS costMotorizedTransportItems,");
		stringBuilder.append(" ITEM_MANUAL_SHIPPING itemManualShipping,COST_ITEM_MANUAL_SHIPPING costItemManualShipping,ITEM_SHIPPING itemShipping,COST_ITEM_SHIPPING costItemShipping,");
		stringBuilder.append(" COST_ITEMS_OTHER_EXPENSES costItemsOtherExpenses,OWER_CABINET_COOLING_SYSTEM owerCabinetCoolingSystem,RECTIFIER_3000 rectifier3000,BATTERY_LITHIUM batteryLithium,OIL_GENERATOR oilGenerator,");
		stringBuilder.append(" ATS ats,SUPERVISION_CONTROL supervisionControl,OTHER_AUXILIARY_SYSTEM otherAuxiliarySystem,PUBLIC_INSTALLATION_POWER publicInstallationPower,EFFECTIVE effective ");
		stringBuilder.append(" from Effective_Calculate_BTS a where a.Created_user_id=:sysUserId ");
		stringBuilder.append(" order by Effective_Calculate_BTS_ID desc "); 	
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("catProvinceCode", new StringType());
		query.addScalar("catProvinceName", new StringType());
		query.addScalar("latitude", new StringType());
		query.addScalar("longitude", new StringType());
		query.addScalar("hightBts", new DoubleType());
		query.addScalar("columnType", new StringType());
		query.addScalar("topographic", new StringType());
		query.addScalar("location", new StringType());
		query.addScalar("address", new StringType());
		query.addScalar("typeStation", new StringType());
		query.addScalar("mnoViettel", new StringType());
		query.addScalar("mnoVina", new StringType());
		query.addScalar("mnoMobi", new StringType());
		query.addScalar("ourceDeployment", new LongType());
		query.addScalar("lectDepreciationPeriod", new StringType());
		query.addScalar("SilkEnterPrice", new StringType());
		query.addScalar("price", new StringType());
		query.addScalar("costMB", new StringType());
		query.addScalar("columnFoundationItems", new StringType());
		query.addScalar("costColumnFoundationItems", new DoubleType());
		query.addScalar("houseFoundationItems", new StringType());
		query.addScalar("costHouseFoundationItems", new DoubleType());
		query.addScalar("columnBodyCategory", new StringType());
		query.addScalar("costColumnBodyCategory", new DoubleType());
		query.addScalar("machineRoomItems", new StringType());
		query.addScalar("costMachineRoomItems", new DoubleType());
		query.addScalar("groundingItems", new StringType());
		query.addScalar("costGroundingItems", new DoubleType());
		query.addScalar("electricTowingItems", new StringType());
		query.addScalar("costElectricTowingItems", new DoubleType());
		query.addScalar("columnMountingItem", new StringType());
		query.addScalar("costColumnMountingItem", new DoubleType());
		query.addScalar("installationHouses", new StringType());
		query.addScalar("costInstallationHouses", new DoubleType());
		query.addScalar("electricalItems", new StringType());
		query.addScalar("costElectricalItems", new DoubleType());
		query.addScalar("motorizedTransportItems", new StringType());
		query.addScalar("costMotorizedTransportItems", new DoubleType());
		query.addScalar("itemManualShipping", new StringType());
		query.addScalar("costItemManualShipping", new DoubleType());
		query.addScalar("itemShipping", new StringType());
		query.addScalar("costItemShipping", new DoubleType());
		query.addScalar("costItemsOtherExpenses", new DoubleType());
		query.addScalar("owerCabinetCoolingSystem", new DoubleType());
		query.addScalar("rectifier3000", new DoubleType());
		query.addScalar("batteryLithium", new DoubleType());
		query.addScalar("oilGenerator", new DoubleType());
		query.addScalar("ats", new DoubleType());
		query.addScalar("supervisionControl", new DoubleType());
		query.addScalar("otherAuxiliarySystem", new DoubleType());
		query.addScalar("publicInstallationPower", new DoubleType());
		query.addScalar("effective", new StringType());
		query.setParameter("sysUserId", request.getSysUserId());
		query.setResultTransformer(Transformers
				.aliasToBean(EffectiveCalculateBTSDTO.class));
		return query.list();
	}
	public List<EffectiveCalculateBTSDTO> getProvince() {
		StringBuilder stringBuilder = new StringBuilder("select CODE catProvinceCode,name catProvinceName from cat_province where status=1 order by name ");	
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("catProvinceCode", new StringType());
		query.addScalar("catProvinceName", new StringType());
		query.setResultTransformer(Transformers
				.aliasToBean(EffectiveCalculateBTSDTO.class));
		return query.list();
	}
	public List<EffectiveCalculateBTSDTO> getNameParam(EffectiveCalculateDASCDBRBTSRequest dto) {
		StringBuilder stringBuilder = new StringBuilder("select name nameParam from App_param where Par_type = :parType order by PAR_ORDER ");	
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("nameParam", new StringType());		
		query.setParameter("parType", dto.getEffectiveCalculateBTSDTO().getParType());
		query.setResultTransformer(Transformers
				.aliasToBean(EffectiveCalculateBTSDTO.class));
		return query.list();
	}
	public List<EffectiveCalculateBTSDTO> getNameParamStation(EffectiveCalculateDASCDBRBTSRequest dto) {
		StringBuilder stringBuilder = new StringBuilder("select work nameParam from Item_Capex where 1=1 ");
		if("mong_cot".equals(dto.getEffectiveCalculateBTSDTO().getParType())){
			stringBuilder.append(" and item_type='Móng cột (giá trị trước VAT)' ");
		}else if("mong_nha".equals(dto.getEffectiveCalculateBTSDTO().getParType())){
			stringBuilder.append(" and item_type='Móng nhà' ");
		}else if("than_cot".equals(dto.getEffectiveCalculateBTSDTO().getParType())){
			stringBuilder.append(" and item_type='Sản xuất cột' ");
		}else if("phong_may".equals(dto.getEffectiveCalculateBTSDTO().getParType())){
			stringBuilder.append(" and item_type='Sản xuất nhà' ");
		}else if("tiep_dia".equals(dto.getEffectiveCalculateBTSDTO().getParType())){
			stringBuilder.append(" and item_type='Tiếp địa' ");
		}else if("keo_dien".equals(dto.getEffectiveCalculateBTSDTO().getParType())){
			stringBuilder.append(" and item_type='Điện AC' ");
		}else if("lap_cot".equals(dto.getEffectiveCalculateBTSDTO().getParType())){
			stringBuilder.append(" and item_type='Lắp dựng cột' ");
		}else if("lap_nha".equals(dto.getEffectiveCalculateBTSDTO().getParType())){
			stringBuilder.append(" and item_type='Lắp dựng phòng máy' ");
		}else if("dau_dien".equals(dto.getEffectiveCalculateBTSDTO().getParType())){
			stringBuilder.append(" and item_type='Chi phí xin phép (Theo QĐ 1648 năm 2018) CTCT ban hành' ");
		}else if("van_chuyen_co_gioi".equals(dto.getEffectiveCalculateBTSDTO().getParType())){
			stringBuilder.append(" and item_type='Phần vận chuyển' ");
		}else if("van_chuyen_thu_cong".equals(dto.getEffectiveCalculateBTSDTO().getParType())){
			stringBuilder.append(" and item_type='Phần vận chuyển' ");
		}else if("van_chuyen".equals(dto.getEffectiveCalculateBTSDTO().getParType())){
			stringBuilder.append(" and item_type='Phần vận chuyển' ");
		}
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("nameParam", new StringType());		
		query.setResultTransformer(Transformers
				.aliasToBean(EffectiveCalculateBTSDTO.class));
		return query.list();
	}
	public Long insertEffectiveCalculateBTS(EffectiveCalculateDASCDBRBTSRequest obj) {
		try {
			EffectiveCalculateBTSDTO effectiveDto = new EffectiveCalculateBTSDTO();
			effectiveDto=obj.getEffectiveCalculateBTSDTO();
			effectiveDto.setCreatedDate(new Date());
			effectiveDto.setCreatedUserId(obj.getSysUserRequest().getSysUserId());
			Long id = this.saveObject(effectiveDto.toModel());
		} catch (Exception ex) {
			ex.printStackTrace();
			this.getSession().clear();
			return 0L;
		}
		return 1L;
	}
	
}

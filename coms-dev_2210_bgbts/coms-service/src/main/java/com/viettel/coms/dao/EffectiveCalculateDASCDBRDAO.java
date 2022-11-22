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

import com.viettel.coms.bo.EffectiveCalculateDASCDBRBO;
import com.viettel.coms.bo.EffectiveCalculateDasBO;
import com.viettel.coms.dto.EffectiveCalculateDASCDBRDTO;
import com.viettel.coms.dto.EffectiveCalculateDASCDBRBTSRequest;
import com.viettel.coms.dto.EffectiveCalculateDasDTO;
import com.viettel.coms.dto.HolidayRequest;
import com.viettel.coms.dto.SysUserRequest;
import com.viettel.coms.dto.UserHolidayDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Repository("effectiveCalculateDASCDBRDAO")
public class EffectiveCalculateDASCDBRDAO extends BaseFWDAOImpl<EffectiveCalculateDASCDBRBO, Long> {

	public EffectiveCalculateDASCDBRDAO() {
		this.model = new EffectiveCalculateDASCDBRBO();
	}

	public EffectiveCalculateDASCDBRDAO(Session session) {
		this.session = session;
	}

	public List<EffectiveCalculateDASCDBRDTO> getListEffectiveCalculateDASCDBR(SysUserRequest request) {
		StringBuilder stringBuilder = new StringBuilder("select a.Effective_Calculate_DAS_CDBR_ID  effectiveCalculateDASCDBRId,a.Das_type dasType,a.Cdbr_type cdbrType,a.House_name houseName,a.Total_area totalArea, ");
		stringBuilder.append(" a.Total_apartments totalApartment,a.Cost_Das costDas,a.Cost_engine_room_CDBR costEngineRoomCDBR,a.Cost_CDBR costCDBR,a.Ratio_rate ratioRate, ");
		stringBuilder.append(" a.Engine_room_Das engineRoomDas,a.Feeder_anten_Das feederAntenDas,a.Cost_other_Das costOtherDas,a.Axis_Cdbr axisCdbr, ");
		stringBuilder.append(" a.Apartments_all_Cdbr apartmentsAllCdbr,a.Apartments_Cdbr apartmentsCdbr,a.Cost_other_Cdbr costOtherCdbr, ");
		stringBuilder.append(" a.Engine_room_cdbr engineRoomCdbr,a.Engine_room_cable_cdbr engineRoomCableCdbr,a.effective ");
		stringBuilder.append(" from Effective_Calculate_DAS_CDBR a where a.Created_user_id=:sysUserId ");
		stringBuilder.append(" order by Effective_Calculate_DAS_CDBR_ID desc ");
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("effectiveCalculateDASCDBRId", new LongType());
		query.addScalar("dasType", new LongType());
		query.addScalar("cdbrType", new LongType());
		query.addScalar("houseName", new StringType());
		query.addScalar("totalArea", new DoubleType());
		query.addScalar("totalApartment", new DoubleType());
		query.addScalar("costDas", new StringType());
		query.addScalar("costEngineRoomCDBR", new StringType());
		query.addScalar("costCDBR", new StringType());
		query.addScalar("ratioRate", new DoubleType());
		query.addScalar("engineRoomDas", new LongType());
		query.addScalar("feederAntenDas", new LongType());
		query.addScalar("costOtherDas", new LongType());
		query.addScalar("axisCdbr", new LongType());
		query.addScalar("apartmentsAllCdbr", new LongType());
		query.addScalar("apartmentsCdbr", new LongType());
		query.addScalar("costOtherCdbr", new LongType());
		query.addScalar("engineRoomCdbr", new LongType());
		query.addScalar("engineRoomCableCdbr", new LongType());
		query.addScalar("effective", new StringType());
		query.setParameter("sysUserId", request.getSysUserId());
		query.setResultTransformer(Transformers
				.aliasToBean(EffectiveCalculateDASCDBRDTO.class));
		return query.list();
	}
	public Long insertEffectiveCalculateDASCDBR(EffectiveCalculateDASCDBRBTSRequest obj) {
		try {
			EffectiveCalculateDASCDBRDTO effectiveDto = obj.getEffectiveCalculateDASCDBRDTO();
			effectiveDto.setCreatedDate(new Date());
			effectiveDto.setCreatedUserId(obj.getSysUserRequest().getSysUserId());
			return this.saveObject(effectiveDto.toModel());
		} catch (Exception ex) {
			ex.printStackTrace();
			this.getSession().clear();
			return 0L;
		}
	}

}

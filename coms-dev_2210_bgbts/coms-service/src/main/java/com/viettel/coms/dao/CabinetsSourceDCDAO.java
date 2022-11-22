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
	import org.hibernate.type.LongType;
	import org.hibernate.type.StringType;
	import org.springframework.stereotype.Repository;
	import org.springframework.transaction.annotation.Transactional;
	import com.viettel.coms.bo.CabinetsSourceDCBO;
	import com.viettel.cat.dto.CatManufacturerDTO;
	import com.viettel.coms.dto.AppParamDTO;
	import com.viettel.coms.dto.CabinetsSourceACDTO;
	import com.viettel.coms.dto.CabinetsSourceDCDTO;
	import com.viettel.coms.dto.DeviceStationElectricalDTO;
	import com.viettel.coms.dto.ElectricAirConditioningACDTO;
	import com.viettel.coms.dto.ElectricDetailDTO;
	import com.viettel.coms.dto.ManageCareerDTO;
	import com.viettel.coms.dto.StationElectricalDTO;
	import com.viettel.erp.utils.JsonDateDeserializer;
	import com.viettel.erp.utils.JsonDateSerializerDate;
	import com.viettel.service.base.dao.BaseFWDAOImpl;
	import com.viettel.service.base.model.BaseFWModelImpl;

@Repository("cabinetsSourceDCDAO")
@Transactional
public class CabinetsSourceDCDAO extends BaseFWDAOImpl<CabinetsSourceDCBO, Long>{

	public CabinetsSourceDCDAO() {
        this.model = new CabinetsSourceDCBO();
    }

    public CabinetsSourceDCDAO(Session session) {
        this.session = session;
    }
}
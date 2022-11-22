package com.viettel.coms.business;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.coms.bo.EffectiveCalculationDetailsBO;
import com.viettel.coms.dao.EffectiveCalculationDetailsDAO;
import com.viettel.coms.dao.SignVofficeDAO;
import com.viettel.coms.dto.EffectiveCalculationDetailsDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;


@Service("effectiveCalculationDetailsBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EffectiveCalculationDetailsBusinessImpl extends
		BaseFWBusinessImpl<EffectiveCalculationDetailsDAO, EffectiveCalculationDetailsDTO, EffectiveCalculationDetailsBO>
		implements EffectiveCalculationDetailsBusiness {

	@Override
	public EffectiveCalculationDetailsDAO gettDAO() {
		return effectiveCalculationDetailsDAO;
	}
	
	@Autowired
	EffectiveCalculationDetailsDAO effectiveCalculationDetailsDAO;
	
	public DataListDTO getDataTableTTTHQ(EffectiveCalculationDetailsDTO obj) {
//		List<EffectiveCalculationDetailsDTO> ls = effectiveCalculationDetailsDAO.doSearch(obj);
//		Session session = effectiveCalculationDetailsDAO.getSessionFactory().openSession();
//		List<EffectiveCalculationDetailsDTO> ls = effectiveCalculationDetailsDAO.getDetailStationByContract(session, obj);
		List<EffectiveCalculationDetailsDTO> ls = new ArrayList<EffectiveCalculationDetailsDTO>();
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setSize(obj.getTotalRecord());
		data.setStart(1);
		data.setTotal(obj.getTotalRecord());
		return data;
	}
}

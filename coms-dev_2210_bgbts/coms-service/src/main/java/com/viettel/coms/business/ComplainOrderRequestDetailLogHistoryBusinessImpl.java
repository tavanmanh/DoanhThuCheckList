package com.viettel.coms.business;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.ComplainOrderRequestBO;
import com.viettel.coms.bo.ComplainOrderRequestDetailLogHistoryBO;
import com.viettel.coms.dao.ComplainOrderRequestDAO;
import com.viettel.coms.dao.ComplainOrderRequestDetailLogHistoryDAO;
import com.viettel.coms.dto.ComplainOrderRequestDTO;
import com.viettel.coms.dto.ComplainOrderRequestDetailLogHistoryDTO;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;

@Service("complainOrderRequestDetailLogHistoryBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ComplainOrderRequestDetailLogHistoryBusinessImpl extends BaseFWBusinessImpl<ComplainOrderRequestDetailLogHistoryDAO, ComplainOrderRequestDetailLogHistoryDTO, ComplainOrderRequestDetailLogHistoryBO> implements ComplainOrderRequestDetailLogHistoryBusiness {

    static Logger LOGGER = LoggerFactory.getLogger(ComplainOrderRequestDetailLogHistoryBusinessImpl.class);
    
    @Context
	HttpServletRequest request;
	
	
	@Autowired
	private ComplainOrderRequestDetailLogHistoryDAO complainOrderRequestDetailLogHistoryDAO;
	
	@Override
	public List<ComplainOrderRequestDetailLogHistoryDTO> doSearch(ComplainOrderRequestDetailLogHistoryDTO obj) {
		// TODO Auto-generated method stub
		return complainOrderRequestDetailLogHistoryDAO.doSearch(obj);
	}
	@Transactional
	@Override
	public Long add(ComplainOrderRequestDetailLogHistoryDTO obj, HttpServletRequest request) throws Exception {
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		return complainOrderRequestDetailLogHistoryDAO.saveObject(obj.toModel());
	}

}

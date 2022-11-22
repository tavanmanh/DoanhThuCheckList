package com.viettel.coms.business;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.coms.bo.DetailTangentCustomerBO;
import com.viettel.coms.bo.OrdersBO;
import com.viettel.coms.bo.TangentCustomerBO;
import com.viettel.coms.dao.DetailTangentCustomerDAO;
import com.viettel.coms.dao.OrdersDAO;
import com.viettel.coms.dao.TangentCustomerDAO;
import com.viettel.coms.dto.DetailTangentCustomerDTO;
import com.viettel.coms.dto.TangentCustomerDTO;
import com.viettel.coms.dto.TotalMonthPlanHTCTDTO;
import com.viettel.service.base.business.BaseFWBusinessImpl;

@Service("detailTangentCustomerBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DetailTangentCustomerBusinessImpl extends BaseFWBusinessImpl<DetailTangentCustomerDAO, DetailTangentCustomerDTO, DetailTangentCustomerBO>
implements DetailTangentCustomerBusiness{
	
	@Autowired
	private DetailTangentCustomerDAO detailTangentCustomerDAO;
	
	@Context
	HttpServletRequest request;
	
	public DetailTangentCustomerBusinessImpl() {
        tModel = new DetailTangentCustomerBO();
        tDAO = detailTangentCustomerDAO;
    }

    @Override
    public DetailTangentCustomerDAO gettDAO() {
        return detailTangentCustomerDAO;
    }
    
    @Override
	public Long createDetailTangentCustomer(DetailTangentCustomerDTO obj) throws Exception {
		// TODO Auto-generated method stub
		
		return detailTangentCustomerDAO.saveObject(obj.toModel());
	}

	@Override
	public Long updateDetailTangentCustomer(DetailTangentCustomerDTO obj) throws Exception {
		// TODO Auto-generated method stub
		return detailTangentCustomerDAO.updateObject(obj.toModel());
	}


}

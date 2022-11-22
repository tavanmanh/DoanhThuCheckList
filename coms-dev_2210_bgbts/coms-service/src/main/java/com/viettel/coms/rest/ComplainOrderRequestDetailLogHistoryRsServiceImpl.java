package com.viettel.coms.rest;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.coms.business.ComplainOrderRequestDetailLogHistoryBusinessImpl;
import com.viettel.coms.business.ComplainOrdersRequestBusinessImpl;
import com.viettel.coms.dto.ComplainOrderRequestDTO;
import com.viettel.coms.dto.ComplainOrderRequestDetailLogHistoryDTO;
import com.viettel.erp.dto.DataListDTO;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.wms.business.UserRoleBusinessImpl;

public class ComplainOrderRequestDetailLogHistoryRsServiceImpl implements ComplainOrderRequestDetailLogHistoryRsService {

    protected final Logger LOGGER = Logger.getLogger(ComplainOrderRequestDetailLogHistoryRsService.class);

    @Autowired
    private ComplainOrderRequestDetailLogHistoryBusinessImpl complainOrderRequestDetailLogHistoryBusinessImpl;
    
    @Autowired
    private ComplainOrdersRequestBusinessImpl complainOrdersRequestBusinessImpl;

    @Autowired
    private UserRoleBusinessImpl userRoleBusinessImpl;

    @Context
    private HttpServletRequest request;

    @Override
    public Response doSearch(ComplainOrderRequestDetailLogHistoryDTO obj) {
        List<ComplainOrderRequestDetailLogHistoryDTO>  ls = complainOrderRequestDetailLogHistoryBusinessImpl.doSearch(obj);
        if (ls == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			DataListDTO data = new DataListDTO();
			data.setData(ls);
			data.setTotal(obj.getTotalRecord());
			data.setSize(obj.getPageSize());
			data.setStart(1);
			return Response.ok(data).build();
		}
        
    }

//    @Override
//    public Response add(ComplainOrderRequestDetailLogHistoryDTO obj) throws Exception {
//    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
//       
//		try {
//			
//			obj.setCreateDate(new Timestamp(System.currentTimeMillis()));
//			obj.setCreateUser(objUser.getVpsUserInfo().getSysUserId().toString());
//			Long id = complainOrderRequestDetailLogHistoryBusinessImpl.add(obj,request);
//			if (id == 0l) {
//	            return Response.status(Response.Status.BAD_REQUEST).build();
//	        }else {
//                return Response.status(Response.Status.OK).build();
//            }
//		} catch (Exception e) {
//			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
//		}	
//    }

}

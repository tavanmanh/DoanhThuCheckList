package com.viettel.coms.business;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.viettel.coms.dto.ComplainOrderRequestDTO;
public interface ComplainOrdersRequestBusiness {
	
	List<ComplainOrderRequestDTO> doSearch(ComplainOrderRequestDTO obj) throws ParseException;
	Long add(ComplainOrderRequestDTO obj,HttpServletRequest request) throws Exception;

	Long updateCRM(ComplainOrderRequestDTO obj, HttpServletRequest request) throws Exception;

}

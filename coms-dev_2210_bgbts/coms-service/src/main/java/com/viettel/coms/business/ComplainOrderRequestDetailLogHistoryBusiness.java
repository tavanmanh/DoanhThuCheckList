package com.viettel.coms.business;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.viettel.coms.dto.ComplainOrderRequestDetailLogHistoryDTO;


public interface ComplainOrderRequestDetailLogHistoryBusiness {
	List<ComplainOrderRequestDetailLogHistoryDTO> doSearch(ComplainOrderRequestDetailLogHistoryDTO obj);
	Long add(ComplainOrderRequestDetailLogHistoryDTO obj,HttpServletRequest request) throws Exception;

}

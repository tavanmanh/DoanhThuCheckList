package com.viettel.erp.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viettel.erp.business.TCTCTSettlementDebtBusinessImpl;

public class TCTCTSettlementDebtRsServiceImpl implements TCTCTSettlementDebtRsService{

	protected final Logger log = Logger.getLogger(TCTCTSettlementDebtRsServiceImpl.class);
	
	@Autowired
	TCTCTSettlementDebtBusinessImpl tCTCTSettlementDebtBusinessImpl;

	@Context
	HttpServletRequest request;

	@Value("${folder_upload2}")
	private String folderUpload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	@Value("${allow.file.ext}")
	private String allowFileExt;
	@Value("${allow.folder.dir}")
	private String allowFolderDir;
	@Value("${folder_upload}")
	private String folderTemp;
}

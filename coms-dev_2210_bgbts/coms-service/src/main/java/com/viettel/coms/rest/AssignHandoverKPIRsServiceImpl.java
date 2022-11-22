package com.viettel.coms.rest;

import com.viettel.coms.business.AssignHandoverBusinessImpl;
import com.viettel.coms.business.ConstructionBusinessImpl;
import com.viettel.coms.dto.*;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.wms.business.UserRoleBusinessImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

//hienvd: created 3/7/2019
public class AssignHandoverKPIRsServiceImpl implements AssignHandoverKPIRsService {

    protected final Logger log = Logger.getLogger(RpHSHCRsServiceImpl.class);

    @Autowired
    private AssignHandoverBusinessImpl assignHandoverBusinessImpl;

    @Autowired
    private UserRoleBusinessImpl userRoleBusinessImpl;

    @Autowired
    private ConstructionBusinessImpl constructionBusiness;

    @Context
    private HttpServletRequest request;

    @Value("${folder_upload2}")
    private String folderUpload;

    @Value("${allow.file.ext}")
    private String allowFileExt;

    @Value("${allow.folder.dir}")
    private String allowFolderDir;

    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;


    //hienvd:
    @Override
    public Response doSearch(AssignHandoverDTO obj) {
        DataListDTO data = assignHandoverBusinessImpl.doSearchKPI(obj);
        return Response.ok(data).build();
    }

}

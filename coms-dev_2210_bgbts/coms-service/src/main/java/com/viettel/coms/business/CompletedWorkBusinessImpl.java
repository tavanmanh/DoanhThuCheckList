package com.viettel.coms.business;

import com.viettel.coms.bo.WorkItemBO;
import com.viettel.coms.dao.*;
import com.viettel.coms.dto.*;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.coms.utils.PermissionUtils;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("completedWorkBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CompletedWorkBusinessImpl extends
        BaseFWBusinessImpl<CompletedWorkDAO, WorkItemDTO, WorkItemBO> implements CompletedWorkBusiness {
    @Autowired
    CompletedWorkDAO completedWorkDAO;
    @Autowired
    UtilAttachDocumentDAO utilAttachDocumentDAO;
    @Autowired
    WorkItemDAO workItemDAO;
    @Autowired
    ConstructionTaskDAO constructionTaskDAO;
    @Autowired
    ConstructionTaskDailyDAO constructionTaskDailyDAO;
    protected final Logger log = Logger.getLogger(CompletedWorkBusinessImpl.class);

    public CompletedWorkBusinessImpl() {
        tModel = new WorkItemBO();
        tDAO = completedWorkDAO;
    }
    @Value("${folder_upload2}")
    private String folder2Upload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;

    @Override
    public CompletedWorkDAO gettDAO() {
        return completedWorkDAO;
    }

    @Override
    public long count() {
        return completedWorkDAO.count("WorkItemBO", null);
    }

    public DataListDTO doSearch(WorkItemDetailDTO obj) {
        List<WorkItemDetailDTO> ls = new ArrayList<WorkItemDetailDTO>();
        ls = completedWorkDAO.doSearchWork(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
    public void approveCompletedWork(WorkItemDetailDTO obj, HttpServletRequest request) {
        KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        Long userId = objUser.getVpsUserInfo().getSysUserId();
        obj.setUpdatedUserId(userId);
        obj.setApproveUserId(userId);
        completedWorkDAO.updateCompletedWork(obj);
    }

    public void rejectCompletedWork(WorkItemDetailDTO obj, HttpServletRequest request) {
        KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        Long userId = objUser.getVpsUserInfo().getSysUserId();
        obj.setUpdatedUserId(userId);
        obj.setApproveUserId(userId);
        completedWorkDAO.rejectCompletedWork(obj);
    }


}

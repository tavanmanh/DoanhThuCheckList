package com.viettel.coms.rest;

import com.viettel.coms.bo.WO_DOANHTHU_GPTH_CHECKLIST_BO;
import com.viettel.coms.bo.WoTypeBO;
import com.viettel.coms.business.WoBusinessImpl;
import com.viettel.coms.dao.DoahThuCheckListDAO;
import com.viettel.coms.dao.WoDAO;
import com.viettel.coms.dto.WO_DOANHTHU_GPTH_CHECKLIST_DTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoTypeDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.utils.BaseResponseOBJ;
import com.viettel.ktts2.common.UConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.core.Response;
import java.util.List;

public class DoanhThuCheckListServiceImpl implements DoanhThuCheckListService{
    private final int SUCCESS_CODE = 1;
    private final int ERROR_CODE = -1;
    private final String SUCCESS_MSG = "SUCCESS";
    private final String ERROR_MSG = "Có lỗi xảy ra.";
    private final String NOTFOUND_MSG = "NOT FOUND";
    private final String CREATE_NEW_MSG = "CREATED";
    private final String UPDATED_MSG = "UPDATED";
    private final String DELETE_MSG = "DELETE";
    private final String TOO_MANY_AIO_WO = "TR từ AIO này chỉ có thể tạo 1 WO.";

    private final String UNASSIGN = "UNASSIGN";
    private final String ASSIGN_CD = "ASSIGN_CD";
    private final String ACCEPT_CD = "ACCEPT_CD";
    private final String REJECT_CD = "REJECT_CD";
    private final String ASSIGN_FT = "ASSIGN_FT";
    private final String ACCEPT_FT = "ACCEPT_FT";
    private final String REJECT_FT = "REJECT_FT";
    private final String PROCESSING = "PROCESSING";
    private final String DONE = "DONE";
    private final String CD_OK = "CD_OK";
    private final String CD_NG = "CD_NG";
    private final String OK = "OK";
    private final String NG = "NG";
    private final String OPINION_RQ = "OPINION_RQ";
    private final String WAIT_TC_BRANCH = "WAIT_TC_BRANCH";
    private final String WAIT_TC_TCT = "WAIT_TC_TCT";
    private final String WAIT_PQT = "WAIT_PQT";
    private final String WAIT_TTDTHT = "WAIT_TTDTHT";
    private final String CD_PAUSE = "CD_PAUSE";
    private final String TTHT_PAUSE = "TTHT_PAUSE";
    private final String DTHT_PAUSE = "DTHT_PAUSE";
    private final String CD_PAUSE_REJECT = "CD_PAUSE_REJECT";
    private final String TTHT_PAUSE_REJECT = "TTHT_PAUSE_REJECT";
    private final String DTHT_PAUSE_REJECT = "DTHT_PAUSE_REJECT";
    private final String RECEIVED_PQT = "RECEIVED_PQT";
    private final String REJECT_PQT = "REJECT_PQT";
    private final String RECEIVED_TTDTHT = "RECEIVED_TTDTHT";
    private final String REJECT_TTDTHT = "REJECT_TTDTHT";
    private final String PQT_NG = "PQT_NG";
    private final String TTDTHT_NG = "TTDTHT_NG";

    private final String ALL_TYPE = "ALL TYPE";
    private final String CREATED_TYPE = "CREATED TYPE";
    private final String ASSIGNED_CD_TYPE = "ASSIGNED CD TYPE";
    private final String ASSIGNED_FT_TYPE = "ASSIGNED FT TYPE";
    private final String REPORT_TYPE = "REPORT_TYPE";

    private final String AP_CONSTRUCTION_TYPE = "AP_CONSTRUCTION_TYPE";
    private final String AP_WORK_SRC = "AP_WORK_SRC";

    private final String CANNOT_DELETE = "Không thể sửa/xóa do có dữ liệu liên quan.";
    private final String CANNOT_DELETE_SCHEDULE_WI_CHECKLIST = "Không thể xóa do có dữ liệu liên quan.";

    private final String TTHT_ID = "242656";
    private final String TTVHKT_ID = "270120";
    private final String TTXDDTHT_ID = "166677";
    private final String TTGPTH_ID = "280483";
    private final String TTCNTT_ID = "280501";

    private final String NEW = "NEW";
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
    @Value("${allow.file.ext}")
    private String allowFileExt;
    @Value("${folder_upload2}")
    private String folderUpload;
    @Value("${allow.folder.dir}")
    private String allowFolderDir;
    @Value("${input_image_sub_folder_upload}")
    private String input_image_sub_folder_upload;
    @Value("${input_image_folder_upload_wo}")
    private String input_image_folder_upload_wo;

    private final String getOrderGoodsDetail = UConfig.get("ktts.ams.service") + "amsOrderGoodsRsService/doOrderGoodsDetail";

    @Autowired
    DoahThuCheckListDAO doahThuCheckListDAO;



    @Override
    public Response getNameProgress(WO_DOANHTHU_GPTH_CHECKLIST_DTO dto) throws Exception {
        String result = "";

        if (dto.getId() != null ) {
            result = doahThuCheckListDAO.getNameProgress(dto.getId());
        }
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, result);
        return Response.ok(resp).build();
    }

    @Override
    public Response updateNameProgress(WO_DOANHTHU_GPTH_CHECKLIST_DTO dto) throws Exception {
        BaseResponseOBJ resp;
        boolean isEditable = doahThuCheckListDAO.updateNameProgress(dto.getId(),dto.getNAME());
        if (isEditable == true) {
            resp = new BaseResponseOBJ(SUCCESS_CODE, UPDATED_MSG, "true");
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, CANNOT_DELETE,"false" );
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response deleteDoanhThuCheckList(WO_DOANHTHU_GPTH_CHECKLIST_DTO dto) throws Exception {
        BaseResponseOBJ resp;
        boolean isEditable = doahThuCheckListDAO.deleteDoanhThuCheckList(dto.getId());
        if (isEditable == true) {
            resp = new BaseResponseOBJ(SUCCESS_CODE, DELETE_MSG, "true");
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, CANNOT_DELETE,"false" );
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response createDoanhThuCheckList(WO_DOANHTHU_GPTH_CHECKLIST_DTO dto) throws Exception {
        BaseResponseOBJ resp;
        boolean isEditable = doahThuCheckListDAO.createDoanhThuCheckList(dto.getId(),dto.getNAME());
        if (isEditable == true) {
            resp = new BaseResponseOBJ(SUCCESS_CODE, "INSERT", "true");
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, "CANNOT_INSERT","false" );
        }
        return Response.ok(resp).build();
    }
    @Override
    public Response createDTO(WO_DOANHTHU_GPTH_CHECKLIST_DTO dto) throws Exception {
        String returnStr = doahThuCheckListDAO.save(dto.toModel());
        return Response.ok(returnStr).build();
    }


    @Override
    public Response getWOTypeById(WO_DOANHTHU_GPTH_CHECKLIST_DTO dto) throws Exception {
        long id = dto.getId();

        WO_DOANHTHU_GPTH_CHECKLIST_BO bo = doahThuCheckListDAO.getOneRaw(id);
        if (bo == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        WO_DOANHTHU_GPTH_CHECKLIST_DTO dto1 = bo.toDTO();
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, dto1);

        return Response.ok(resp).build();
    }


}

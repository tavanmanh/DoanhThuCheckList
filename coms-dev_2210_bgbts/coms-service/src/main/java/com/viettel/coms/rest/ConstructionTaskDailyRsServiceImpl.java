/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viettel.coms.business.ConstructionTaskDailyBusinessImpl;
import com.viettel.coms.business.YearPlanBusinessImpl;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ConstructionTaskDailyDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;

/**
 * @author HungLQ9
 */
public class ConstructionTaskDailyRsServiceImpl implements ConstructionTaskDailyRsService {

    protected final Logger log = Logger.getLogger(ConstructionTaskDailyBusinessImpl.class);
    @Autowired
    ConstructionTaskDailyBusinessImpl constructionTaskDailyBusinessImpl;
    @Autowired
    YearPlanBusinessImpl yearPlanBusinessImpl;
    @Context
    HttpServletRequest request;
    
    @Value("${allow.file.ext}")
    private String allowFileExt;
    @Value("${allow.folder.dir}")
    private String allowFolderDir;
    
    @Context
    HttpServletResponse response;
    @Value("${folder_upload2}")
    private String folderUpload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;

    @Override
    public Response getConstructionTaskDaily() {
        List<ConstructionTaskDailyDTO> ls = constructionTaskDailyBusinessImpl.getAll();
        if (ls == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            DataListDTO data = new DataListDTO();
            data.setData(ls);
            data.setTotal(ls.size());
            data.setSize(ls.size());
            data.setStart(1);
            return Response.ok(data).build();
        }
    }

    @Override
    public Response getConstructionTaskDailyById(Long id) {
        ConstructionTaskDailyDTO obj = (ConstructionTaskDailyDTO) constructionTaskDailyBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(obj).build();
        }
    }

    @Override
    public Response updateConstructionTaskDaily(ConstructionTaskDailyDTO obj) {
        Long id = constructionTaskDailyBusinessImpl.update(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok().build();
        }

    }

    @Override
    public Response addConstructionTaskDaily(ConstructionTaskDailyDTO obj) {
        Long id = constructionTaskDailyBusinessImpl.save(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(Response.Status.CREATED).build();
        }
    }

    @Override
    public Response deleteConstructionTaskDaily(Long id) {
        ConstructionTaskDailyDTO obj = (ConstructionTaskDailyDTO) constructionTaskDailyBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            constructionTaskDailyBusinessImpl.delete(obj);
            return Response.ok(Response.Status.NO_CONTENT).build();
        }
    }
    /**Hoangnh start 18022019**/
    @Override
    public Response doSearch(ConstructionTaskDailyDTO obj) {
    	KttsUserSession objUsers = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("DOSEARCH_CONSTRUCTION_TASK_DAILY");
        objKpiLog.setDescription("Phê duyệt sản lượng ngoài OS theo ngày");
        objKpiLog.setCreateUserId(objUsers.getVpsUserInfo().getSysUserId());
//        hoanm1_20190528_start
        DataListDTO data = constructionTaskDailyBusinessImpl.doSearch(obj,request);
//        hoanm1_20190528_end
        Date dEnd = new Date();
        objKpiLog.setEndTime(dEnd);
        objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
        objKpiLog.setStatus("1");
        yearPlanBusinessImpl.addKpiLog(objKpiLog);
        return Response.ok(data).build();//end
    }
    
    @Override
    public Response getListImage(ConstructionTaskDailyDTO obj) throws Exception {
        return Response.ok(constructionTaskDailyBusinessImpl.getListImage(obj)).build();
    }
    
    @Override
    public Response rejectQuantityByDay(ConstructionTaskDailyDTO obj) {
        try {
        	constructionTaskDailyBusinessImpl.rejectQuantityByDay(obj, request);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
    
    @Override
    public Response cancelApproveQuantityByDay(ConstructionTaskDailyDTO obj) {
        try {
            // TODO Auto-generated method stub
        	constructionTaskDailyBusinessImpl.cancelApproveQuantityByDay(obj, request);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
    
    @Override
    public Response approveQuantityDayChecked(List<ConstructionTaskDailyDTO> lstObj) {
    	//tanqn 20181113 start
    	KttsUserSession objUsers = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
//        objKpiLog.setFunctionCode("ACCEPT_DETAIL_MONTH_PLAN_OS");
//        objKpiLog.setDescription("Phê duyệt sản lượng theo ngày ngoài OS");
        objKpiLog.setCreateUserId(objUsers.getVpsUserInfo().getSysUserId());
        try {
            for (ConstructionTaskDailyDTO obj : lstObj) {
            	constructionTaskDailyBusinessImpl.approveQuantityByDay(obj, request);
            }
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
    
    @Override
    public Response approveQuantityByDay(ConstructionTaskDailyDTO obj) {
        try {
        	constructionTaskDailyBusinessImpl.approveQuantityByDay(obj, request);
           
            return Response.ok(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
    
    @Override
    public Response exportConstructionTaskDaily(ConstructionTaskDailyDTO obj) throws Exception {
    	//tanqn 20181113 start
    	KttsUserSession objUsers = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("EXPORT_WORK_ITEM_DETAIL_OS");
        objKpiLog.setDescription("Phê duyệt sản lượng theo ngày ngoài OS");
        objKpiLog.setCreateUserId(objUsers.getVpsUserInfo().getSysUserId());
        try {
            String strReturn = constructionTaskDailyBusinessImpl.exportConstructionTaskDaily(obj, request);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
        }
        return null;
    }
    /**Hoangnh end 18022019**/

    //Huypq-20191009-start
	@Override
	public Response doSearchCompleteManage(WorkItemDTO obj) {
		DataListDTO data = constructionTaskDailyBusinessImpl.doSearchCompleteManage(obj,request);
		return Response.ok(data).build();
	}
	
	private boolean isFolderAllowFolderSave(String folderDir) {
		return UString.isFolderAllowFolderSave(folderDir, allowFolderDir);
	}

	private boolean isExtendAllowSave(String fileName) {
		return UString.isExtendAllowSave(fileName, allowFileExt);
	}
	
    @Override
    public Response importCompleteWorkItem(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePathReturn;
        String filePath;
        String folderParam = UString.getSafeFileName(request.getParameter("folder"));
        String sysGroupId = UString.getSafeFileName(request.getParameter("sysGroupId"));
        String sysGroupName = UString.getSafeFileName(request.getParameter("sysGroupName"));
        String typeImport = UString.getSafeFileName(request.getParameter("typeImport"));
        
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        
        if (UString.isNullOrWhitespace(folderParam)) {
            folderParam = defaultSubFolderUpload;
        } else {
            if (!isFolderAllowFolderSave(folderParam)) {
                throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
            }
        }

        DataHandler dataHandler = attachments.getDataHandler();

        // get filename to be uploaded
        MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
        String fileName = UFile.getFileName(multivaluedMap);

        if (!isExtendAllowSave(fileName)) {
            throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
        }

        // write & upload file to server
        try (InputStream inputStream = dataHandler.getInputStream();) {
            filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
            filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
        } catch (Exception ex) {
            throw new BusinessException("Loi khi save file", ex);
        }

        try {

            try {
                java.util.List<WorkItemDTO> result = constructionTaskDailyBusinessImpl.importCompleteWorkItem(filePath, typeImport);
				if (result != null && !result.isEmpty()
						&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
					DetailMonthPlanDTO monthPlan = constructionTaskDailyBusinessImpl.getDetailMonthPlanId(Long.parseLong(sysGroupId));
					if(monthPlan==null) {
						throw new BusinessException("Kế hoạch tháng "+ month+"/"+year+" của đơn vị "+ sysGroupName+" chưa tồn tại");
					}
					
					if(typeImport.equals("1")) {
						//Huypq-20200227-start
						HashMap<String, Long> mapTask = new HashMap<>();
						HashMap<Long, Long> checkConsId = new HashMap<>();
						List<ConstructionTaskDTO> lstWiInMonthPlan = constructionTaskDailyBusinessImpl.getConsTaskByDetailMonthPlan(monthPlan.getDetailMonthPlanId());
						if(lstWiInMonthPlan.size()==0) {
							throw new BusinessException("Kế hoạch tháng "+ month+"/"+year+" của đơn vị "+ sysGroupName+" không có hạng mục để hoàn thành");
						}
						for(ConstructionTaskDTO task : lstWiInMonthPlan) {
							mapTask.put(task.getConstructionId().toString() + "|" + task.getWorkItemId().toString(), task.getConstructionId());
							checkConsId.put(task.getConstructionId(), task.getConstructionId());
						}
						
						for(WorkItemDTO work : result) {
							DetailMonthPlanDTO plan = new DetailMonthPlanDTO();
							plan.setSysGroupId(Long.parseLong(sysGroupId));
							plan.setDetailMonthPlanId(monthPlan.getDetailMonthPlanId());
							plan.setConstructionCode(work.getConstructionCode());
							plan.setWorkItemName(work.getWorkItemName());
							plan.setQuantity(work.getQuantity()*1000000);
							constructionTaskDailyBusinessImpl.updateConstructionTaskWork(plan);
							constructionTaskDailyBusinessImpl.updateWorkItem(plan);
//							hoanm1_20200415_start
//							String key = work.getConstructionId().toString() + "|" + work.getWorkItemId().toString();
//							if(mapTask.get(key)!=null) {
//								mapTask.remove(key);
//							}
							Double res = constructionTaskDailyBusinessImpl.avgStatus(work.getConstructionId());
							if (res.compareTo(3.0) == 0) {
								constructionTaskDailyBusinessImpl.updateStatusCons(work.getConstructionId());
							}else{
								constructionTaskDailyBusinessImpl.updateStatusConsProcess(work.getConstructionId());
							}
						}
//						for(Entry<String, Long> entry: mapTask.entrySet()) {
//							Long checkId = checkConsId.get(entry.getValue());
//							if(checkId==null) {
//								constructionTaskDailyBusinessImpl.updateStatusCons(entry.getValue());
//							}
//					    }
//						hoanm1_20200415_end
						//Huy-end
					} else {
						for(WorkItemDTO work : result) {
							DetailMonthPlanDTO plan = new DetailMonthPlanDTO();
							plan.setSysGroupId(Long.parseLong(sysGroupId));
							plan.setDetailMonthPlanId(monthPlan.getDetailMonthPlanId());
							plan.setConstructionCode(work.getConstructionCode());
							plan.setQuantity(work.getQuantity()*1000000);
							if(typeImport.equals("2")) {
								constructionTaskDailyBusinessImpl.updateConstructionTaskQuyLuong(plan);
							} else if(typeImport.equals("3")) {
								constructionTaskDailyBusinessImpl.updateConstructionTaskDoanhThu(plan);
							}
						}
					}
					
					return Response.ok(result).build();
				} else if (result == null || result.isEmpty()) {
                    return Response.ok().entity(Response.Status.NO_CONTENT).build();
                } else {
                    return Response.ok(result).build();
                }
            } catch (Exception e) {
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }

        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
	//Huy-end

}

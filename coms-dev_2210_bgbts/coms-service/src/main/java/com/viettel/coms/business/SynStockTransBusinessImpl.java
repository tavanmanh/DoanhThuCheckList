package com.viettel.coms.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.viettel.coms.dto.*;
import com.viettel.wms.dto.StockTransDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.bo.SynStockTransBO;
import com.viettel.coms.dao.AIOMerEntityDAO;
import com.viettel.coms.dao.AIOStockGoodsTotalDAO;
import com.viettel.coms.dao.AIOStockTransDetailDAO;
import com.viettel.coms.dao.AIOStockTransDetailSerialDAO;
import com.viettel.coms.dao.AssignHandoverDAO;
import com.viettel.coms.dao.ConstructionDAO;
import com.viettel.coms.dao.SynStockTransDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dao.WorkItemDAO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.ChuyenTienRaChu;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.utils.ImageUtil;
import com.viettel.wms.dao.AppParamDAO;
import com.viettel.wms.dto.AppParamDTO;
import com.viettel.wms.dto.StockDTO;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.springframework.web.client.RestTemplate;

/**
 * @author CuongNV2
 * @version 1.0
 * @since 2018-06-15
 */
@Service("synStockTransBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SynStockTransBusinessImpl extends BaseFWBusinessImpl<SynStockTransDAO, SynStockTransDTO, SynStockTransBO>
        implements SynStockTransBusiness {

    private static final String SYN_STOCK_TRAIN = "A";
    //VietNT_20190125_start
    private final String REJECT_NOTE_PERMISSION = "REJECT NOTE";

    @Autowired
    private AssignHandoverDAO assignHandoverDAO;
    //VietNT_end
    @Autowired
    private ConstructionDAO constructionDAO;
    
    @Autowired
    private AppParamDAO appParamDAO;
    
    @Value("${input_image_sub_folder_upload}")
    private String input_image_sub_folder_upload;

 	private Logger LOGGER = Logger.getLogger(SynStockTransBusinessImpl.class);
 
    @Value("${folder_upload2}")
    private String folderUpload;
    
    
    @Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
    
    @Autowired
    private SynStockTransDAO synStockTransDAO;

    @Autowired
	private WorkItemDAO workItemDAO;
    
    @Autowired
	private UtilAttachDocumentDAO utilAttachDocumentDAO;
//    hoanm1_20200523_start
    @Autowired
    private AIOStockTransDetailDAO aioStockTransDetailDAO;
    
    @Autowired
    private AIOStockTransDetailSerialDAO aioStockTransDetailSerialDAO;
    
    @Autowired
    private AIOStockGoodsTotalDAO aioStockGoodsTotalDAO;
    
    @Autowired
    private AIOMerEntityDAO aioMerEntityDAO;

    @Value("${wms_service_base_url}")
    private String wmsServiceBaseUrl;
//    hoanm1_20200523_end
    public SynStockTransBusinessImpl() {
        tModel = new SynStockTransBO();
        tDAO = synStockTransDAO;
    }

    @Override
    public SynStockTransDAO gettDAO() {
        return synStockTransDAO;
    }

    @Override
    public long count() {
        return synStockTransDAO.count("SynStockTransBO", null);
    }

    /**
     * getCountContructionTask
     *
     * @param request
     * @return CountConstructionTaskDTO
     */
    public CountConstructionTaskDTO getCountContructionTask(SysUserRequest request) {
        return synStockTransDAO.getCount(request);
    }

    /**
     * getListSysStockTransDTO
     *
     * @param request
     * @return List<SynStockTransDTO>
     */
    //huypq-20190907-start
//    public List<SynStockTransDTO> getListSysStockTransDTO(StockTransRequest request) {
//        return synStockTransDAO.getListSysStockTransDTO(request);
//    }

    public DataListDTO getListSysStockTransDTO(StockTransRequest request) {
    	List<SynStockTransDTO> ls = synStockTransDAO.getListSysStockTransDTO(request);
        DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(request.getTotalRecord());
		data.setSize(request.getPageSize());
		data.setStart(1);
		return data;
    }
    //Huy-end
    /**
     * getListSynStockTransDetail
     *
     * @param st
     * @return List<SynStockTransDetailDTO>
     */
    public List<SynStockTransDetailDTO> getListSynStockTransDetail(SynStockTransDTO st) {
        return synStockTransDAO.getListSysStockTransDetailDTO(st);
    }

    /**
     * getListMerEntityDto
     *
     * @param request
     * @return List<MerEntityDTO>
     */
    public List<MerEntityDTO> getListMerEntityDto(StockTransRequest request) {
        return synStockTransDAO.getListMerEntity(request);
    }

    /**
     * updateStockTrans
     *
     * @param request
     * @return int result
     */
    public int updateStockTrans(StockTransRequest request) {
        return synStockTransDAO.updateStockTrans(request);
    }

    /**
     * updateSynStockTrans
     *
     * @param request
     * @return int result
     */
    public int updateSynStockTrans(StockTransRequest request) {
        return synStockTransDAO.updateSynStockTrans(request);
    }

    /**
     * getCongNo
     *
     * @param request
     * @return List<MerEntityDTO>
     */
    public List<MerEntityDTO> getCongNo(SysUserRequest request) {
        return synStockTransDAO.getCongNo(request);
    }

    /**
     * count Materials
     *
     * @param request
     * @return CountConstructionTaskDTO
     */
    public CountConstructionTaskDTO countMaterials(SysUserRequest request) {
        return synStockTransDAO.countMaterials(request);
    }

    /**
     * DeliveryMaterials
     *
     * @param request
     * @return int result
     * @throws ParseException
     */
    public int DeliveryMaterials(StockTransRequest request, ResultInfo resultInfo) throws ParseException {
        boolean isInvestor = SYN_STOCK_TRAIN.equals(request.getSynStockTransDto().getStockType());
        int flag = request.getSysUserRequest().getFlag();
        Long userId = request.getSysUserRequest().getSysUserId();
        Long receiver = request.getSynStockTransDto().getReceiverId();
        Long lastShipperId = request.getSynStockTransDto().getLastShipperId();
        String confirm = request.getSynStockTransDto().getConfirm().trim();
        String state = request.getSynStockTransDto().getState().trim();
        String stockType = request.getSynStockTransDto().getStockType().trim();
        SynStockTransDetailDTO newestTransactionId = synStockTransDAO.getNewestTransactionId(request);

        if (flag == 1 && userId.compareTo(lastShipperId) == 0 && "0".equals(confirm) && receiver == null) {
            if ("A".equals(stockType)) {
                synStockTransDAO.updateSynStockTrans(request);
            } else {
                synStockTransDAO.updateStockTrans(request);
            }
            //VietNT_20190128_start
//            SynStockTransDTO dto = request.getSynStockTransDto();
            // check: shipperId = lastShipperId && type = 2 && businessType = 1
//            if (dto.getShipperId().compareTo(dto.getLastShipperId()) == 0
//                    && dto.getType().equals("2")
//                    && dto.getBussinessType().equals("1")) {
//                synStockTransDAO.updateConfirmDateFirstTime(dto.getStockTransId());
//            }
//            if (StringUtils.isNotEmpty(dto.getCode())) {
//                synStockTransDAO.updateSynStockDailyImportExport(dto.getCode());
//            }
            //VietNT_end
            resultInfo.setMessage("Xác nhận thành công");
            return 1;

        } else if (flag == 0 && userId.compareTo(lastShipperId) == 0 && "0".equals(confirm)) {
            if ("A".equals(stockType)) {
                synStockTransDAO.updateSynStockTrans(request);
                //hoanm1_20190919_start
                if(request.getSynStockTransDto().getConfirmDescription().equals("Sai đơn vị nhận")){
	                if (userId.compareTo(lastShipperId) == 0 && receiver == null) {
	                	SynStockTransDTO userInfo = synStockTransDAO.getUserTTKTProvince(lastShipperId, request.getSynStockTransDto().getConsCode());
	                	if (userInfo != null && userInfo.getSysUserId() != null) {
	                		synStockTransDAO.updateLastShipperSynStockTrans(userInfo.getSysUserId(), request.getSynStockTransDto().getSynStockTransId());
	                		Double statusConstruction = synStockTransDAO.getConstructionStatus(request.getSynStockTransDto().getConsCode());
	                		if (statusConstruction <= 1.0) {
	                			synStockTransDAO.updateSysGroupIdConstruction(userInfo.getSysGroupIdConstruction(),request.getSynStockTransDto().getConsCode());
	                		}
	                	}
	                }
                }
//                hoanm1_20190919_start
//                this.sendSmsReject(request.getSynStockTransDto(), userId);
//                hoanm1_20190524_comment
    	        //VietNT_end
            } else {
                synStockTransDAO.updateStockTrans(request);
            }
           
            resultInfo.setMessage("Đã từ chối");
            return 1;

        } else {
            SysUserRequest sysUserReceiver = request.getSysUserReceiver();
            if ((sysUserReceiver != null && flag == 2 && lastShipperId.compareTo(userId) == 0 && receiver == null
                    && "1".equals(confirm.trim()))
                    || (sysUserReceiver != null && flag == 2 && receiver != null && "2".equals(state)
                    && lastShipperId.compareTo(userId) == 0)
                    || (sysUserReceiver != null && flag == 2 && lastShipperId.compareTo(receiver) == 0
                    && lastShipperId.compareTo(userId) == 0 && "1".equals(state))) {

                synStockTransDAO.UpdateStockTransState(request);
                synStockTransDAO.SaveStTransaction(request);
                resultInfo.setMessage("Thực hiện bàn giao thành công");
                return 1;

            } else if (flag == 0 && lastShipperId.compareTo(receiver) != 0 && userId.compareTo(receiver) == 0
                    && "0".equals(state)) {
                synStockTransDAO.UpdateStocktrainByReceiver(request);
                synStockTransDAO.UpdateStocktrainHistoryByRefusedByReceiver(request, isInvestor, newestTransactionId);
                resultInfo.setMessage("Đã từ chối");
                return 1;

            } else if (flag == 1 && lastShipperId.compareTo(receiver) != 0 && userId.compareTo(receiver) == 0
                    && "0".equals(state)) {
                synStockTransDAO.UpdateStocktrainConfirmByReceiver(request);
                synStockTransDAO.UpdateStocktrainConfirmByLastShipper(request, isInvestor, newestTransactionId);
                resultInfo.setMessage("Đã tiếp nhận");
                return 1;
            }
        }
        return 0;
    }
    //VietNT_20190125_start
    private void sendSmsReject(SynStockTransDTO dto, Long rejectUserId) {
    	if (dto == null || dto.getLastShipperId() == null || rejectUserId == null) {
    		return;
    	}

        List<SysUserDTO> users = synStockTransDAO.findUsersWithPermission(REJECT_NOTE_PERMISSION, null);
        if (users == null || users.isEmpty()) {
        	return;
        }

        SynStockTransDTO lastShipperInfo = synStockTransDAO.getRejectorInfo(dto.getLastShipperId());

        String subject = "Thông báo từ chối nhận phiếu xuất kho A cấp";
        String content = lastShipperInfo.getSysUserName() + " - " + lastShipperInfo.getCustomField() +
                " đã từ chối phiếu xuất kho \"" + dto.getCode() +
                "\" của công trình: \"" + dto.getConsCode() + "\".";
        Date today = new Date();

        for (SysUserDTO user : users) {
            assignHandoverDAO.insertIntoSendSmsEmailTable(user, subject, content, rejectUserId, today, null);
        }
    }
    //VietNT_end

    //VietNT_20190116_start
    public DataListDTO doSearch(SynStockTransDTO obj) {
        List<SynStockTransDTO> dtos = synStockTransDAO.doSearch(obj);
        DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(dtos);
        dataListDTO.setTotal(obj.getTotalRecord());
        dataListDTO.setSize(obj.getPageSize());
        dataListDTO.setStart(1);
        return dataListDTO;
    }

    /**
     * Forward SynStockTrans to group
     *
     * @param dto       dto.listToForward: list stockTrans data
     *                  dto.sysGroupId: sysGroupId forwarding to
     * @param sysUserId sysUserId execute forward
     * @throws Exception
     */
    public void doForwardGroup(SynStockTransDTO dto, Long sysUserId) throws Exception {
        Long forwardToSysGroupId = dto.getSysGroupId();
        List<SynStockTransDTO> listForward = dto.getListToForward();
        if (listForward == null || listForward.isEmpty()) {
            throw new BusinessException("Chưa chọn PXK");
        }

        Date today = new Date();
        for (SynStockTransDTO stockTrans : listForward) {
            SynStockTransDTO provinceChiefInfo = this.getProvinceChiefInfo(forwardToSysGroupId, stockTrans.getConstructionId());
            // add info to update
            provinceChiefInfo.setSynStockTransId(stockTrans.getSynStockTransId());
            provinceChiefInfo.setUpdatedBy(sysUserId);
            provinceChiefInfo.setUpdatedDate(today);
            int result = synStockTransDAO.updateForwardSynStockTrans(provinceChiefInfo);
            if (result < 1) {
                throw new BusinessException("Có lỗi xảy ra khi Chuyển đơn vị");
            }
            result = synStockTransDAO.updateConstructionForwardSynStockTrans(forwardToSysGroupId, stockTrans.getConstructionId());
            if (result < 1) {
                throw new BusinessException("Có lỗi xảy ra khi cập nhật dữ liệu Điều chuyển công trình");
            }
        }
    }

    private SynStockTransDTO getProvinceChiefInfo(Long sysGroupId, Long constructionId) throws Exception {
        SynStockTransDTO provinceChief;
        if (null != sysGroupId && null != constructionId) {
            provinceChief = synStockTransDAO.getProvinceChiefId(sysGroupId, constructionId);
            if (null == provinceChief
                    || provinceChief.getSysUserId() == null
                    || StringUtils.isEmpty(provinceChief.getSysUserName())) {
                throw new BusinessException("Không tìm thấy tỉnh trưởng! Thực hiện chọn lại đơn vị");
            }
        } else {
            throw new BusinessException("Không đủ thông tin để tìm kiếm tỉnh trưởng");
        }
        return provinceChief;
    }


    //VietNT_end
    //HuyPQ-08042019-start
    public DataListDTO reportDetailAWaitReceive(SynStockTransDTO obj) {
		List<SynStockTransDTO> ls = synStockTransDAO.reportDetailAWaitReceive(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
    //HuyPQ-end
    
    //Huypq-20190904-start
    public DataListDTO doSearchAcceptManage(SynStockTransDTO obj, HttpServletRequest request) {
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        obj.setLastShipperId(user.getVpsUserInfo().getSysUserId());
        obj.setReceiverId(user.getVpsUserInfo().getSysUserId());
        
        String provinceId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> provinceIdList = ConvertData.convertStringToList(provinceId, ",");
		if(provinceIdList !=null) {
			obj.setProvinceIdLst(provinceIdList);
		}
    	List<SynStockTransDTO> dtos = synStockTransDAO.doSearchAcceptManage(obj);
        DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(dtos);
        dataListDTO.setTotal(obj.getTotalRecord());
        dataListDTO.setSize(obj.getPageSize());
        dataListDTO.setStart(1);
        return dataListDTO;
    }
    
    //Tiếp nhận
    public Long updateAcceptPXK(SynStockTransDTO obj, HttpServletRequest request) throws ParseException{
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	Long id=null;
    	for(SynStockTransDTO dto : obj.getListSynStockTrans()) {
//    		boolean isInvestor = SYN_STOCK_TRAIN.equals(dto.getStockType());
//    		SynStockTransDetailDTO newestTransactionId = synStockTransDAO.getNewestTransactionId(dto);
//            synStockTransDAO.UpdateStocktrainConfirmByLastShipper(dto, isInvestor, newestTransactionId);
//            
    		dto.setSysUserId(user.getVpsUserInfo().getSysUserId());
    		id = synStockTransDAO.updateSynStockTransAccept(dto);
    	}
    	return id;
    }
    
    //Xác nhận bàn giao
    public Long updateAcceptAssignPXK(SynStockTransDTO obj, HttpServletRequest request) throws ParseException{
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	Long id=null;
    	for(SynStockTransDTO dto : obj.getListSynStockTrans()) {
    		//
        	for(UtilAttachDocumentDTO file : obj.getListFileData()) {
        		file.setObjectId(dto.getSynStockTransId());
        		file.setStatus("1");
        		file.setCreatedDate(new Date());
        		file.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
        		file.setCreatedUserName(user.getFullName());
        		file.setType("XNBG_PXK_A");
        		utilAttachDocumentDAO.saveObject(file.toModel());
        	}
        	//
    		
    		boolean isInvestor = SYN_STOCK_TRAIN.equals(dto.getStockType());
    		SynStockTransDetailDTO newestTransactionId = synStockTransDAO.getNewestTransactionId(dto);
            synStockTransDAO.UpdateStocktrainConfirmByLastShipper(dto, isInvestor, newestTransactionId);
    		dto.setSysUserId(user.getVpsUserInfo().getSysUserId());
    		id = synStockTransDAO.updateSynStockTransAcceptAssign(dto);
    	}
    	return id;
    }
    
    //Từ chối
    public Long updateDenyPXK(SynStockTransDTO obj, HttpServletRequest request) {
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	Long id=null;
    	for(SynStockTransDTO dto : obj.getListSynStockTrans()) {
    		dto.setSysUserId(user.getVpsUserInfo().getSysUserId());
    		dto.setConfirmDescription(obj.getConfirmDescription());
    		if (user.getVpsUserInfo().getSysUserId().compareTo(dto.getLastShipperId()) == 0 && dto.getConfirm().trim().equals("0")) {
    			id = synStockTransDAO.updateSynStockTransDeny(dto);
    			if(!obj.getConfirmDescription().equals("Sai số lượng, chủng loại vật tư")) {
    				SynStockTransDTO userInfo = synStockTransDAO.getUserTTKTProvince(dto.getLastShipperId(), dto.getConstructionCode());
                	if (userInfo != null && userInfo.getSysUserId() != null) {
                		synStockTransDAO.updateLastShipperSynStockTrans(userInfo.getSysUserId(), dto.getSynStockTransId());
                		Double statusConstruction = synStockTransDAO.getConstructionStatus(dto.getConstructionCode());
                		if (statusConstruction <= 1.0) {
                			synStockTransDAO.updateSysGroupIdConstruction(userInfo.getSysGroupIdConstruction(), dto.getConstructionCode());
                		}
                	}
    			}
    		} else {
    			synStockTransDAO.updateSynStockTransDenyAsign(dto);
    		}
    	}
    	return id;
    }
    
    //Bàn giao
    public Long updateAssignPXK(SynStockTransDTO obj, HttpServletRequest request)  throws ParseException {
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	Long id=null;
    	for(SynStockTransDTO dto : obj.getListSynStockTrans()) {
    		dto.setSysUserId(user.getVpsUserInfo().getSysUserId());
	    	dto.setSysUserIdRecieve(obj.getSysUserIdRecieve());
	    	dto.setConfirmDescription(null);
	    	id = synStockTransDAO.UpdateStockTransState(dto);
	    	synStockTransDAO.SaveStTransaction(dto);
    	}
    	return id;
    }
    
    public String  exportFile(SynStockTransDTO obj, String filePath, HttpServletRequest request) throws Exception{
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	boolean checkRoleTTHT = VpsPermissionChecker.hasPermission(Constant.OperationKey.RULE,
				Constant.AdResourceKey.TTHT, request);
        obj.setLastShipperId(user.getVpsUserInfo().getSysUserId());
        obj.setReceiverId(user.getVpsUserInfo().getSysUserId());
    	SynStockTransDTO data = synStockTransDAO.getAcceptManageById(obj, checkRoleTTHT);
    	if(data==null) {
    		throw new BusinessException("Không phải người nhận không được tải phiếu xuất !");
    	}
    	String exportType = "docx";
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String createdDate = formatter.format(data.getCreatedDate());
			String[] arrDate = createdDate.split("/");
			
			String realIEDate = formatter.format(data.getRealIeTransDate());
			String[] arrRealIEDate = createdDate.split("/");
			
			data.setDayPXK(arrDate[0]);
			data.setMonthPXK(arrDate[1]);
			data.setYearPXK(arrDate[2]);
			
			data.setDayTX(arrRealIEDate[0]);
			data.setMonthTX(arrRealIEDate[1]);
			data.setYearTX(arrRealIEDate[2]);
			
			Double sumTotalMoney = 0d;
			//get data tttb
			SynStockTransDetailSerialDTO detailSerial = new SynStockTransDetailSerialDTO();
			detailSerial.setIdDetail(obj.getSynStockTransDetailId());
			detailSerial.setTypeExport(1l);
			detailSerial.setPage(1l);
			detailSerial.setPageSize(10);
			List<SynStockTransDetailSerialDTO> lsTttb = workItemDAO.GoodsListDetail(detailSerial);
			List<SynStockTransDetailSerialDTO> tttbi = new ArrayList<SynStockTransDetailSerialDTO>();
			for(SynStockTransDetailSerialDTO seri : lsTttb) {
				tttbi.add(seri);
			}
			for (int i = 0; i < tttbi.size(); i++) {
				tttbi.get(i).setStt(i + 1);
				tttbi.get(i).setTotalMoney((tttbi.get(i).getAmountReal()!=null? tttbi.get(i).getAmountReal() : 0d)
						*(tttbi.get(i).getUnitPrice()!=null?tttbi.get(i).getUnitPrice():0d));
				sumTotalMoney = sumTotalMoney + tttbi.get(i).getTotalMoney();
			}
			//get data ttvt
			SynStockTransDetailDTO detail = new SynStockTransDetailDTO();
			detail.setIdTable(obj.getSynStockTransId());
			detail.setTypeExport(1l);
			detail.setPage(1l);
			detail.setPageSize(10);
			List<SynStockTransDetailDTO> lsTtvt = workItemDAO.GoodsListTable(detail);
			List<SynStockTransDetailDTO> ttvt = new ArrayList<SynStockTransDetailDTO>();
			for(SynStockTransDetailDTO seri : lsTtvt) {
				ttvt.add(seri);
			}
			for (int i = 0; i < ttvt.size(); i++) {
				ttvt.get(i).setStt(i + 1);
				ttvt.get(i).setTotalMoney((ttvt.get(i).getAmountReal()!=null? ttvt.get(i).getAmountReal() : 0d)
						*(ttvt.get(i).getTotalPrice()!=null?ttvt.get(i).getTotalPrice():0d));
				sumTotalMoney = sumTotalMoney + ttvt.get(i).getTotalMoney();
			}
			InputStream in = new FileInputStream(new File(filePath + "/BM-PXK-A-CAP.docx"));
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);
			FieldsMetadata metadata = report.createFieldsMetadata();
			// 3) Create context Java model
			IContext context = report.createContext();

			data.setSumTotalMoney(Double.parseDouble(String.valueOf(Math.round(sumTotalMoney))));
			data.setTienBangChu(ChuyenTienRaChu.chuyenThanhChu2(String.valueOf(Math.round(sumTotalMoney))));
			
			context.put("item", data);
			context.put("tttbi", tttbi);
			context.put("ttvt", ttvt);
			
			metadata.load("tttbi", SynStockTransDetailSerialDTO.class, true);
			metadata.load("ttvt", SynStockTransDetailDTO.class, true);
			
			File fout = new File(folder2Upload + "/" + obj.getSynStockTransId() + "-BM-PXK-A-CAP."+exportType.trim().toLowerCase());
			OutputStream out = new FileOutputStream(fout);
			if("pdf".equals(exportType)){
				Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
				report.convert(context, options, out);
			}else{
				report.process(context, out);
			}
			out.flush();
			out.close();
			
			return obj.getSynStockTransId() + "-BM-PXK-A-CAP."+exportType;
		
	}
    //Huy-end
    
    //Huypq-20191002-start
    public int DeliveryMaterialsNew(StockTransRequest request, ResultInfo resultInfo) throws ParseException {
        boolean isInvestor = SYN_STOCK_TRAIN.equals(request.getSynStockTransDto().getStockType());
        int flag = request.getSysUserRequest().getFlag();
        Long userId = request.getSysUserRequest().getSysUserId();
        Long receiver = request.getSynStockTransDto().getReceiverId();
        Long lastShipperId = request.getSynStockTransDto().getLastShipperId();
        String confirm = request.getSynStockTransDto().getConfirm().trim();
        String state = request.getSynStockTransDto().getState().trim();
        String stockType = request.getSynStockTransDto().getStockType().trim();
        SynStockTransDetailDTO newestTransactionId = synStockTransDAO.getNewestTransactionId(request);

        if ((flag == 1 && userId.compareTo(lastShipperId) == 0 && "0".equals(confirm) && receiver == null)
        		|| flag == 1 && userId.compareTo(lastShipperId) == 0 && "0".equals(confirm) && "0".equals(state)) {
            if ("A".equals(stockType)) {
                synStockTransDAO.updateSynStockTrans(request);
            } else {
                synStockTransDAO.updateStockTrans(request);
            }
            resultInfo.setMessage("Xác nhận thành công");
            return 1;

        } else if (flag == 0 && userId.compareTo(lastShipperId) == 0 && "0".equals(confirm)) {
            if ("A".equals(stockType)) {
                synStockTransDAO.updateSynStockTrans(request);
                //hoanm1_20190919_start
                if(request.getSynStockTransDto().getConfirmDescription().equals("Sai đơn vị nhận")){
	                if ((userId.compareTo(lastShipperId) == 0 && receiver == null) || (userId.compareTo(lastShipperId) == 0 && "0".equals(state))) {
	                	SynStockTransDTO userInfo = synStockTransDAO.getUserTTKTProvince(lastShipperId, request.getSynStockTransDto().getConsCode());
	                	if (userInfo != null && userInfo.getSysUserId() != null) {
	                		synStockTransDAO.updateLastShipperSynStockTrans(userInfo.getSysUserId(), request.getSynStockTransDto().getSynStockTransId());
	                		Double statusConstruction = synStockTransDAO.getConstructionStatus(request.getSynStockTransDto().getConsCode());
	                		if (statusConstruction <= 1.0) {
	                			synStockTransDAO.updateSysGroupIdConstruction(userInfo.getSysGroupIdConstruction(),request.getSynStockTransDto().getConsCode());
	                		}
	                	}
	                }
                }
    	        //VietNT_end
            } else {
                synStockTransDAO.updateStockTrans(request);
            }
           
            resultInfo.setMessage("Đã từ chối");
            return 1;

        } else {
            SysUserRequest sysUserReceiver = request.getSysUserReceiver();
            if ((sysUserReceiver != null && flag == 2 && lastShipperId.compareTo(userId) == 0 && receiver == null)
                    || (sysUserReceiver != null && flag == 2 && receiver != null && "2".equals(state)
                    && lastShipperId.compareTo(userId) == 0)
                    || (sysUserReceiver != null && flag == 2 && lastShipperId.compareTo(receiver) == 0
                    && lastShipperId.compareTo(userId) == 0 && "1".equals(state))
                    || (lastShipperId.compareTo(receiver) != 0 && userId.compareTo(receiver) == 0 && "0".equals(state))) {

                synStockTransDAO.UpdateStockTransState(request);
                synStockTransDAO.SaveStTransaction(request);
                resultInfo.setMessage("Thực hiện bàn giao thành công");
                return 1;

            } else if (flag == 0 && lastShipperId.compareTo(receiver) != 0 && userId.compareTo(receiver) == 0
                    && "0".equals(state)) {
                synStockTransDAO.UpdateStocktrainByReceiver(request);
                synStockTransDAO.UpdateStocktrainHistoryByRefusedByReceiver(request, isInvestor, newestTransactionId);
                resultInfo.setMessage("Đã từ chối");
                return 1;

            } else if (flag == 1 && lastShipperId.compareTo(receiver) != 0 && userId.compareTo(receiver) == 0
                    && "0".equals(state)) {
                synStockTransDAO.UpdateStocktrainConfirmByReceiver(request);
                synStockTransDAO.UpdateStocktrainConfirmByLastShipper(request, isInvestor, newestTransactionId);
                resultInfo.setMessage("Đã tiếp nhận");
                return 1;
            }
        }
        return 0;
    }
    //Huy-end
//    hoanm1_20191012_start
    public int DeliveryMaterialsUpdate(StockTransRequest request, ResultInfo resultInfo) throws ParseException {
        boolean isInvestor = SYN_STOCK_TRAIN.equals(request.getSynStockTransDto().getStockType());
        int flag = request.getSysUserRequest().getFlag();
        Long userId = request.getSysUserRequest().getSysUserId();
        Long receiver = request.getSynStockTransDto().getReceiverId();
//        Long receiver = request.getSysUserReceiver().getSysUserId();
        Long lastShipperId = request.getSynStockTransDto().getLastShipperId();
        String confirm = request.getSynStockTransDto().getConfirm().trim();
        String state = request.getSynStockTransDto().getState().trim();
        String stockType = request.getSynStockTransDto().getStockType().trim();
        SynStockTransDetailDTO newestTransactionId = synStockTransDAO.getNewestTransactionId(request);

//        confirm=0,recived_id=null,flag == 0
        if (flag == 0 && userId.compareTo(lastShipperId) == 0 && "0".equals(confirm) && (receiver == null|| receiver == 0)) {
            if ("A".equals(stockType)) {
                synStockTransDAO.updateSynStockTrans(request);
                if(request.getSynStockTransDto().getFilePaths() != null && request.getSynStockTransDto().getFilePaths().size() > 0) {
                 	  this.saveImages(request.getSynStockTransDto().getFilePaths(), request.getSynStockTransDto().getSynStockTransId());
                   }
                if(request.getSynStockTransDto().getConfirmDescription().equals("Sai đơn vị nhận")){
	                if (userId.compareTo(lastShipperId) == 0 && receiver == null) {
	                	SynStockTransDTO userInfo = synStockTransDAO.getUserTTKTProvince(lastShipperId, request.getSynStockTransDto().getConsCode());
	                	if (userInfo != null && userInfo.getSysUserId() != null) {
	                		synStockTransDAO.updateLastShipperSynStockTrans(userInfo.getSysUserId(), request.getSynStockTransDto().getSynStockTransId());
	                		Double statusConstruction = synStockTransDAO.getConstructionStatus(request.getSynStockTransDto().getConsCode());
	                		if (statusConstruction <= 1.0) {
	                			synStockTransDAO.updateSysGroupIdConstruction(userInfo.getSysGroupIdConstruction(),request.getSynStockTransDto().getConsCode());
	                		}
	                	}
	                }
                }
            } else {
                synStockTransDAO.updateStockTrans(request);
            }
            // call api aio
            try {
                String url = "/service/vsmartWorkOrderWebService/service/autoCreateImportOrderWrong";
//                StockTransRequest dto = new StockTransRequest();
                StockTransRequestForRejectExport dto = new StockTransRequestForRejectExport();
                dto.setStockTransCode(request.getSynStockTransDto().getCode());
                HttpEntity<StockTransRequestForRejectExport> entity = new HttpEntity<>(dto, this.getDefaultHeader());
                RestTemplate restTemplate = new RestTemplate();
                System.out.println(entity);
                restTemplate.postForObject(wmsServiceBaseUrl + url, entity, Object.class);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new BusinessException("Có lỗi xảy ra khi cập nhật đơn hàng");
            }
            resultInfo.setMessage("Đã từ chối xác nhận phiếu");
            return 1;

        }
//      confirm=0,recived_id=null,flag == 1
      if (flag == 1 && userId.compareTo(lastShipperId) == 0 && "0".equals(confirm) && (receiver == null|| receiver == 0)) {
          if ("A".equals(stockType)) {
              synStockTransDAO.updateSynStockTrans(request);
              if(request.getSynStockTransDto().getListSynStockTransDetailDto().size() > 0) {
            	  request.getSynStockTransDto().getListSynStockTransDetailDto().forEach( dto ->{
            		  dto.setSynStockTransId(request.getSynStockTransDto().getSynStockTransId());
            		  synStockTransDAO.updateSynStockTransDetail(dto);
            		  
            	  });
              }
              if(request.getSynStockTransDto().getFilePaths() != null && request.getSynStockTransDto().getFilePaths().size() > 0) {
            	  this.saveImages(request.getSynStockTransDto().getFilePaths(), request.getSynStockTransDto().getSynStockTransId());
              }
              
          } else {
              synStockTransDAO.updateStockTrans(request);
//            hoanm1_20200523_vsmart_start
            if("18".equals(request.getSynStockTransDto().getBusinessType())){
                StockDTO stockDTO = synStockTransDAO.getStockById(request.getSynStockTransDto().getStockReceiveId());
                if (stockDTO == null) {
                    throw new BusinessException("Không tìm thấy kho nhận");
                }
                this.insertBillImportToStock(request, stockDTO);
            }
//            hoanm1_20200523_vsmart_end
          }
          resultInfo.setMessage("Xác nhận phiếu thành công");
          return 1;

      }
//        confirm=0,recived_id=null,flag == 2
        else if (flag == 2 && userId.compareTo(lastShipperId) == 0 && "0".equals(confirm) && (receiver == null|| receiver == 0) && "A".equals(stockType)) {
        	 synStockTransDAO.UpdateStockTransState(request);
             synStockTransDAO.SaveStTransaction(request);
             resultInfo.setMessage("Thực hiện bàn giao thành công");
             return 1;
        }
       // confirm=0,recived_id != null,flag == 0
        else if (flag == 0 && "0".equals(confirm) && (receiver != null|| receiver != 0) && "A".equals(stockType)) {
        	 synStockTransDAO.UpdateStocktrainByReceiver(request);
             synStockTransDAO.UpdateStocktrainHistoryByRefusedByReceiver(request, isInvestor, newestTransactionId);
             if(request.getSynStockTransDto().getFilePaths() != null && request.getSynStockTransDto().getFilePaths().size() > 0) {
           	  this.saveImages(request.getSynStockTransDto().getFilePaths(), request.getSynStockTransDto().getSynStockTransId());
             }
             resultInfo.setMessage("Đã từ chối nhận bàn giao");
             return 1;
        }
       // confirm=0,recived_id != null,flag == 1
        else if (flag == 1 && "0".equals(confirm) && (receiver != null|| receiver != 0) && "A".equals(stockType)) {
        	synStockTransDAO.UpdateStocktrainConfirmByReceiver(request);
            synStockTransDAO.UpdateStocktrainConfirmByLastShipper(request, isInvestor, newestTransactionId);
            resultInfo.setMessage("Đã tiếp nhận bàn giao");
            return 1;
        }
//        confirm=0,recived_id != null,flag == 2
        else if (flag == 2 && "0".equals(confirm) && (receiver != null|| receiver != 0) && "A".equals(stockType)) {
        	synStockTransDAO.UpdateStockTransState(request);
            synStockTransDAO.SaveStTransaction(request);
            resultInfo.setMessage("Thực hiện bàn giao thành công");
            return 1;
        }
//      confirm=1,recived_id !=null,flag == 0
        else if (flag == 0 && lastShipperId.compareTo(receiver) != 0 && userId.compareTo(receiver) == 0
                && "1".equals(confirm)&& "0".equals(state)) {
            synStockTransDAO.UpdateStocktrainByReceiver(request);
            synStockTransDAO.UpdateStocktrainHistoryByRefusedByReceiver(request, isInvestor, newestTransactionId);
            if(request.getSynStockTransDto().getFilePaths() != null && request.getSynStockTransDto().getFilePaths().size() > 0) {
          	  this.saveImages(request.getSynStockTransDto().getFilePaths(), request.getSynStockTransDto().getSynStockTransId());
            }
            resultInfo.setMessage("Đã từ chối nhận bàn giao");
            return 1;
        }
//      confirm=1,recived_id !=null,flag == 1
        else if (flag == 1 && lastShipperId.compareTo(receiver) != 0 && userId.compareTo(receiver) == 0
                && "1".equals(confirm)&& "0".equals(state)) {
        	synStockTransDAO.UpdateStocktrainConfirmByReceiver(request);
        	LOGGER.info("Giá trị nàyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy: " + newestTransactionId.getMaxTransactionId() + "__" + request.getSynStockTransDto().getSynStockTransId());
            synStockTransDAO.UpdateStocktrainConfirmByLastShipper(request, isInvestor, newestTransactionId);
            resultInfo.setMessage("Đã tiếp nhận bàn giao");
            return 1;
        }
//     confirm=1,recived_id !=null,flag == 2
        else if (flag == 2 && lastShipperId.compareTo(receiver) != 0 && (receiver != null|| receiver != 0)
                && "1".equals(confirm)) {
//    	 synStockTransDAO.UpdateStockTransState(request);
//         synStockTransDAO.SaveStTransaction(request);
         resultInfo.setMessage("Tính năng này đã bị khóa");
         return 1;
        }
      
//      
//        else if (flag == 0 && userId.compareTo(lastShipperId) == 0 && "0".equals(confirm)) {
//            if ("A".equals(stockType)) {
//                synStockTransDAO.updateSynStockTrans(request);
//                if(request.getSynStockTransDto().getConfirmDescription().equals("Sai đơn vị nhận")){
//	                if (userId.compareTo(lastShipperId) == 0 && receiver == null) {
//	                	SynStockTransDTO userInfo = synStockTransDAO.getUserTTKTProvince(lastShipperId, request.getSynStockTransDto().getConsCode());
//	                	if (userInfo != null && userInfo.getSysUserId() != null) {
//	                		synStockTransDAO.updateLastShipperSynStockTrans(userInfo.getSysUserId(), request.getSynStockTransDto().getSynStockTransId());
//	                		Double statusConstruction = synStockTransDAO.getConstructionStatus(request.getSynStockTransDto().getConsCode());
//	                		if (statusConstruction <= 1.0) {
//	                			synStockTransDAO.updateSysGroupIdConstruction(userInfo.getSysGroupIdConstruction(),request.getSynStockTransDto().getConsCode());
//	                		}
//	                	}
//	                }
//                }
//            } else {
//                synStockTransDAO.updateStockTrans(request);
//            }
//            resultInfo.setMessage("Đã từ chối");
//            return 1;
//
//        } else {
//            SysUserRequest sysUserReceiver = request.getSysUserReceiver();
//            if ((sysUserReceiver != null && flag == 2 && lastShipperId.compareTo(userId) == 0 && receiver == null
//                    && "1".equals(confirm.trim()))
//                    || (sysUserReceiver != null && flag == 2 && receiver != null && "2".equals(state)
//                    && lastShipperId.compareTo(userId) == 0)
//                    || (sysUserReceiver != null && flag == 2 && lastShipperId.compareTo(receiver) == 0
//                    && lastShipperId.compareTo(userId) == 0 && "1".equals(state))) {
//
//                synStockTransDAO.UpdateStockTransState(request);
//                synStockTransDAO.SaveStTransaction(request);
//                resultInfo.setMessage("Thực hiện bàn giao thành công");
//                return 1;
//
//            } else if (flag == 0 && lastShipperId.compareTo(receiver) != 0 && userId.compareTo(receiver) == 0
//                    && "0".equals(state)) {
//                synStockTransDAO.UpdateStocktrainByReceiver(request);
//                synStockTransDAO.UpdateStocktrainHistoryByRefusedByReceiver(request, isInvestor, newestTransactionId);
//                resultInfo.setMessage("Đã từ chối");
//                return 1;
//
//            } else if (flag == 1 && lastShipperId.compareTo(receiver) != 0 && userId.compareTo(receiver) == 0
//                    && "0".equals(state)) {
//                synStockTransDAO.UpdateStocktrainConfirmByReceiver(request);
//                synStockTransDAO.UpdateStocktrainConfirmByLastShipper(request, isInvestor, newestTransactionId);
//                resultInfo.setMessage("Đã tiếp nhận");
//                return 1;
//            }
//        }
        return 0;
    }
//    hoanm1_20191012_end 
     
    
    
    
    //Huypq-20200206-start
    public Long updateAcceptAssign(SynStockTransDetailDTO obj, HttpServletRequest request) throws ParseException{
    	Long id = 0l;
    	SynStockTransDTO synDto = new SynStockTransDTO();
    	synDto.setListSynStockTrans(obj.getListSynStockTrans());
    	synDto.setListFileData(obj.getListFileData());
    	this.updateAcceptAssignPXK(synDto, request);
    	for(SynStockTransDetailDTO dto : obj.getListDataMaterial()) {
    		id = synStockTransDAO.updateAcceptSynStockTrans(dto);
    	}
    	return id;
    }
    
    public DataListDTO getDataFilePXK(SynStockTransDetailDTO obj){
    	List<UtilAttachDocumentDTO> ls = synStockTransDAO.getDataFilePXK(obj);
    	DataListDTO data = new DataListDTO();
    	data.setData(ls);
    	data.setSize(obj.getPageSize());
    	data.setTotal(obj.getTotalRecord());
    	data.setStart(1);
    	return data;
    }
    
    public List<SysUserDTO> checkRolePGD(HttpServletRequest request) {
//		ASSIGN_PXK_A
		String provinceId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.RULE,
					Constant.AdResourceKey.TTHT, request);
		List<String> provinceIdList = ConvertData.convertStringToList(provinceId, ",");

		List<SysUserDTO> lstSys = synStockTransDAO.getUserIdByRoleProvince(provinceIdList);
		return lstSys;
	}

    //Huy-end
    //tatph-start-11/2/2020
    private void saveImages(List<UtilAttachDocumentDTO> listImage, Long productInfoId) {
        for (UtilAttachDocumentDTO dto : listImage) {
           
            try {
                InputStream inputStream = ImageUtil.convertBase64ToInputStream(dto.getBase64String());
                String imagePath = UFile.writeToFileServerATTT2(inputStream, dto.getName(),
                        input_image_sub_folder_upload, folderUpload);
                dto.setFilePath(imagePath);

                dto.setObjectId(productInfoId);
                dto.setDescription("Ảnh thông tin sản phẩm");
                dto.setStatus("1");
                dto.setCreatedDate(new Date());

                utilAttachDocumentDAO.saveObject(dto.toModel());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

        }
    }
    public List<Integer> DeliveryMaterialsUpdateV2(StockTransRequest request, ResultInfo resultInfo) throws ParseException {
    	List<Integer> result = new ArrayList<>();
    	if(request.getLstSynStockTransDto() != null && request.getLstSynStockTransDto().size() > 0) {
    		for(SynStockTransDTO dto : request.getLstSynStockTransDto()){
    			request.setSynStockTransDto(dto);
    			boolean isInvestor = SYN_STOCK_TRAIN.equals(request.getSynStockTransDto().getStockType());
		        int flag = request.getSysUserRequest().getFlag();
		        Long userId = request.getSysUserRequest().getSysUserId();
		        Long receiver = request.getReceiverId();
		        request.getSynStockTransDto().setDescription(request.getConfirmDescription());
//		        Long receiver = request.getSysUserReceiver().getSysUserId();
		        Long lastShipperId = request.getSynStockTransDto().getLastShipperId();
		        String confirm = request.getSynStockTransDto().getConfirm().trim();
		        String state = request.getSynStockTransDto().getState().trim();
		        String stockType = request.getSynStockTransDto().getStockType().trim();
		        SynStockTransDetailDTO newestTransactionId = synStockTransDAO.getNewestTransactionId(request);

//		        confirm=0,recived_id=null,flag == 0
		        if (flag == 0 && userId.compareTo(lastShipperId) == 0 && "0".equals(confirm) && (receiver == null|| receiver == 0)) {
		            if ("A".equals(stockType)) {
		                synStockTransDAO.updateSynStockTrans(request);
		                if(request.getSynStockTransDto().getConfirmDescription().equals("Sai đơn vị nhận")){
			                if (userId.compareTo(lastShipperId) == 0 && receiver == null) {
			                	SynStockTransDTO userInfo = synStockTransDAO.getUserTTKTProvince(lastShipperId, request.getSynStockTransDto().getConsCode());
			                	if (userInfo != null && userInfo.getSysUserId() != null) {
			                		synStockTransDAO.updateLastShipperSynStockTrans(userInfo.getSysUserId(), request.getSynStockTransDto().getSynStockTransId());
			                		Double statusConstruction = synStockTransDAO.getConstructionStatus(request.getSynStockTransDto().getConsCode());
			                		if (statusConstruction <= 1.0) {
			                			synStockTransDAO.updateSysGroupIdConstruction(userInfo.getSysGroupIdConstruction(),request.getSynStockTransDto().getConsCode());
			                		}
			                	}
			                }
		                }
		            } else {
		                synStockTransDAO.updateStockTrans(request);
		            }
		            resultInfo.setMessage("Đã từ chối xác nhận phiếu");
		            result.add(1);
		            continue;

		        }
//		      confirm=0,recived_id=null,flag == 1
		      if (flag == 1 && userId.compareTo(lastShipperId) == 0 && "0".equals(confirm) && (receiver == null|| receiver == 0)) {
		          if ("A".equals(stockType)) {
		              synStockTransDAO.updateSynStockTrans(request);
		          } else {
		              synStockTransDAO.updateStockTrans(request);
		          }
		          resultInfo.setMessage("Xác nhận phiếu thành công");
		          result.add(1);
		          continue;

		      }
//		        confirm=0,recived_id=null,flag == 2
		        else if (flag == 2 && userId.compareTo(lastShipperId) == 0 && "0".equals(confirm) && (receiver == null|| receiver == 0) && "A".equals(stockType)) {
		        	 synStockTransDAO.UpdateStockTransState(request);
		             synStockTransDAO.SaveStTransaction(request);
		             resultInfo.setMessage("Thực hiện bàn giao thành công");
		             result.add(1);
		             continue;
		        }
		       // confirm=0,recived_id != null,flag == 0
		        else if (flag == 0 && "0".equals(confirm) && (receiver != null|| receiver != 0) && "A".equals(stockType)) {
		        	 synStockTransDAO.UpdateStocktrainByReceiver(request);
		             synStockTransDAO.UpdateStocktrainHistoryByRefusedByReceiver(request, isInvestor, newestTransactionId);
		             resultInfo.setMessage("Đã từ chối nhận bàn giao");
		             result.add(1);
		             continue;
		        }
		       // confirm=0,recived_id != null,flag == 1
		        else if (flag == 1 && "0".equals(confirm) && (receiver != null|| receiver != 0) && "A".equals(stockType)) {
		        	synStockTransDAO.UpdateStocktrainConfirmByReceiver(request);
		            synStockTransDAO.UpdateStocktrainConfirmByLastShipper(request, isInvestor, newestTransactionId);
		            resultInfo.setMessage("Đã tiếp nhận bàn giao");
		            result.add(1);
		            continue;
		        }
//		        confirm=0,recived_id != null,flag == 2
		        else if (flag == 2 && "0".equals(confirm) && (receiver != null|| receiver != 0) && "A".equals(stockType)) {
		        	synStockTransDAO.UpdateStockTransState(request);
		            synStockTransDAO.SaveStTransaction(request);
		            resultInfo.setMessage("Thực hiện bàn giao thành công");
		            result.add(1);
		            continue;
		        }
//		      confirm=1,recived_id !=null,flag == 0
		        else if (flag == 0 && lastShipperId.compareTo(receiver) != 0 && userId.compareTo(receiver) == 0
		                && "1".equals(confirm)&& "0".equals(state)) {
		            synStockTransDAO.UpdateStocktrainByReceiver(request);
		            synStockTransDAO.UpdateStocktrainHistoryByRefusedByReceiver(request, isInvestor, newestTransactionId);
		            resultInfo.setMessage("Đã từ chối nhận bàn giao");
		            result.add(1);
		            continue;
		        }
//		      confirm=1,recived_id !=null,flag == 1
		        else if (flag == 1 && lastShipperId.compareTo(receiver) != 0 && userId.compareTo(receiver) == 0
		                && "1".equals(confirm)&& "0".equals(state)) {
		        	synStockTransDAO.UpdateStocktrainConfirmByReceiver(request);
		            synStockTransDAO.UpdateStocktrainConfirmByLastShipper(request, isInvestor, newestTransactionId);
		            resultInfo.setMessage("Đã tiếp nhận bàn giao");
		            result.add(1);
		            continue;
		        }
//		     confirm=1,recived_id !=null,flag == 2
		        else if (flag == 2 && lastShipperId.compareTo(receiver) != 0 && (receiver != null|| receiver != 0)
		                && "1".equals(confirm)) {
		    	 synStockTransDAO.UpdateStockTransState(request);
		         synStockTransDAO.SaveStTransaction(request);
		         resultInfo.setMessage("Thực hiện bàn giao thành công");
		         result.add(1);
		         continue;
		        }
		      
		      result.add(0);
		      continue;
    		}
    	}else {
    		return result;
    	}
    	return result;
    }
    
    public List<AppParamDTO> getAppParam(StockTransRequest request) {
        List<AppParamDTO> ls = appParamDAO.getAppParam(request);
        return ls;
    }
    
    
    public List<UtilAttachDocumentDTO> getListAttachmentByIdAndType(StockTransRequest request) {
        try {
        	Long objectId = request.getSynStockTransDto().getSynStockTransId();
        	String type = request.getSynStockTransDto().getSynTransType();
            List<UtilAttachDocumentDTO> dtos = utilAttachDocumentDAO.getListAttachmentByIdAndType(Collections.singletonList(objectId), Collections.singletonList(type));
            for (UtilAttachDocumentDTO dto : dtos) {
                String base64Image = ImageUtil.convertImageToBase64(folderUpload+dto.getFilePath());
                dto.setBase64String(base64Image);
                dto.setFilePath(UEncrypt.encryptFileUploadPath(dto.getFilePath()));
            }
            return dtos;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
	public Long saveKpiLogTimeProcess(StockTransRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String s = sdf.format(date);
		KpiLogTimeProcessDTO dto = new KpiLogTimeProcessDTO();
		dto.setAppCode(request.getAppCode());
		dto.setFuncCode(request.getFuncCode());
		dto.setServiceCode(request.getServiceCode());
		dto.setUserName(request.getUserName());
		try {
			dto.setStartTime(sdf.parse(s));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dto.setStartTimeProcess(System.currentTimeMillis());
		Long id = constructionDAO.saveKpiLogTimeProcess(dto);
		return id;
	}
	
	public void updateKpiLogTimeProcess(StockTransRequest request) {
		KpiLogTimeProcessDTO data= constructionDAO.getKpiLogTimeProcessById(request.getKpiLogTimeProcessId());
		KpiLogTimeProcessDTO dto = new KpiLogTimeProcessDTO();
		dto.setId(request.getKpiLogTimeProcessId());
		dto.setEndTime(new Date());
		dto.setEndTimeProcess(System.currentTimeMillis());
		Long processTime = dto.getEndTimeProcess() - data.getStartTimeProcess();
		dto.setProcessTime(processTime);
		if(request.getAppCode() != null) {
			if("COMS".equals(request.getAppCode())) {
				if(processTime < 20000) {
					constructionDAO.updateKpiLogTimeProcessById(dto);
				}
//				else {
//					constructionDAO.deleteKpiLogTimeProcessById(dto);
//				}
			}else if("IMS".equals(request.getAppCode())){
				if(processTime < 30000) {
					constructionDAO.updateKpiLogTimeProcessById(dto);
				}
//				else {
//					constructionDAO.deleteKpiLogTimeProcessById(dto);
//				}
			}else {
				constructionDAO.updateKpiLogTimeProcessById(dto);
			}
		}else {
			constructionDAO.updateKpiLogTimeProcessById(dto);
		}
	}
    //tatph-end-11/2/2020
	
//  hoanm1_20200523_vsmart_start
   private void insertBillImportToStock(StockTransRequest obj, StockDTO stockDTO) {
       Long stockTransId = this.createStockTrans(obj, stockDTO);
       if (stockTransId < 1) {
           throw new BusinessException("Lưu thất bại");
       }
       if (obj.getSynStockTransDto().getListSynStockTransDetailDto() != null && !obj.getSynStockTransDto().getListSynStockTransDetailDto().isEmpty()) {
           this.createStockTransDetails(stockTransId, obj.getSynStockTransDto().getListSynStockTransDetailDto(), stockDTO);
       } else {
           throw new BusinessException("Không tìm thấy PXK ");
       }
   }
   private Long createStockTrans(StockTransRequest obj, StockDTO stockDTO) {
       SysUserDTO userInfo = synStockTransDAO.getUserInfo(obj.getSysUserRequest().getSysUserId());
       if (userInfo == null) {
           throw new BusinessException("Không có quyền");
       }
       String userName = userInfo.getSysUserName();
       String sysGroupName = userInfo.getSysGroupName();

       AIOSynStockTransDTO stockTransDto = new AIOSynStockTransDTO();
       // stockTransDto.setCode(obj.getSynStockTransDto().getCode());
       Long sequence = synStockTransDAO.getSequenceStock();
       stockTransDto.setCode("PNK_" + stockDTO.getCode() + "/20/" + sequence);
       stockTransDto.setType("1");
       stockTransDto.setStockId(stockDTO.getStockId());
       stockTransDto.setStockCode(stockDTO.getCode());
       stockTransDto.setStockName(stockDTO.getName());
       stockTransDto.setStatus("2");
       stockTransDto.setSignState("3");
       stockTransDto.setFromStockTransId(obj.getSynStockTransDto().getSynStockTransId());
       stockTransDto.setDescription("Nhập kho nhân viên");
       stockTransDto.setCreatedByName(userName);
       stockTransDto.setCreatedDeptId(obj.getSysUserRequest().getDepartmentId());
       stockTransDto.setCreatedDeptName(sysGroupName);
       stockTransDto.setRealIeTransDate(new Date());
       stockTransDto.setRealIeUserId(String.valueOf(obj.getSysUserRequest().getSysUserId()));
       stockTransDto.setRealIeUserName(userName);
       stockTransDto.setShipperId(obj.getSysUserRequest().getSysUserId());
       stockTransDto.setShipperName(userName);
       stockTransDto.setCreatedBy(obj.getSysUserRequest().getSysUserId());
       stockTransDto.setCreatedDate(new Date());
       stockTransDto.setBusinessTypeName("Nhập kho nhân viên");
       stockTransDto.setDeptReceiveName(sysGroupName);
       stockTransDto.setDeptReceiveId(obj.getSysUserRequest().getDepartmentId());
       stockTransDto.setBussinessType("19");
       return synStockTransDAO.saveObject(stockTransDto.toModel());
   }
   
   private void createStockTransDetails(Long stockTransId, List<AIOSynStockTransDetailDTO> stockTransDetailDTOS,StockDTO stockDTO) {
		
		for (AIOSynStockTransDetailDTO detailDTO : stockTransDetailDTOS) {
			AIOSynStockTransDetailDTO dtoDetail = this.toAIOSynStockTransDetailDTO(stockTransId, detailDTO);
			Long idDetail = aioStockTransDetailDAO.saveObject(dtoDetail.toModel());
			if (idDetail < 1) {
	            throw new BusinessException("Lưu thất bại PXK");
	        }
			List<AIOStockTransDetailSerialDTO> getListDetailSerial = synStockTransDAO.getListDetailSerial(detailDTO.getSynStockTransDetailId());
			int resultUpdate;
			for (AIOStockTransDetailSerialDTO bo : getListDetailSerial) {
				bo.setStockTransId(stockTransId);
				bo.setStockTransDetailId(idDetail);
				Long idDetailSerial = aioStockTransDetailSerialDAO.saveObject(bo.toModel());
				if (idDetailSerial < 1) {
		            throw new BusinessException("Lưu thất bại PXK serial");
		        }
				
				// update merEntity
				resultUpdate = synStockTransDAO.updateMerStockId(bo.getMerEntityId(), stockDTO.getStockId());
				if (resultUpdate < 1) {
		            throw new BusinessException("Cập nhật Mer thất bại");
		        }
			}
			
			// tinh lai apply_price
			resultUpdate = synStockTransDAO.recalculateApplyPrice(detailDTO.getGoodsId(), stockDTO.getStockId());
			if (resultUpdate < 1) {
	            throw new BusinessException("Cập nhật apply price thất bại");
	        }
			
			// check exit stock_goods_total
			AIOSynStockTransDTO stockTotal = synStockTransDAO.getStockGoodTotal(stockDTO.getStockId(), detailDTO.getGoodsId());
			if (stockTotal != null) {
				resultUpdate = synStockTransDAO.updateStockGoodsTotalAmount(
				stockTotal.getStockGoodsTotalId(),
				stockTotal.getAmount(),
				stockTotal.getAmountIssue(),
				detailDTO.getAmountOrder());
				if (resultUpdate < 1) {
		            throw new BusinessException("Cập nhật stockGoodsTotal thất bại");
		        }
			} else {
					AIOStockGoodsTotalDTO dtoTotal = this.toAioStockGoodsTotalDTO(stockDTO, detailDTO);
					Long idTotal = aioStockGoodsTotalDAO.saveObject(dtoTotal.toModel());
					if (idTotal < 1) {
			            throw new BusinessException("Cập nhật hàng tồn kho thất bại");
			        }
			}
		}
	}
   private AIOSynStockTransDetailDTO toAIOSynStockTransDetailDTO(Long stockTransId, AIOSynStockTransDetailDTO dto) {
       AIOSynStockTransDetailDTO dtoDetail = new AIOSynStockTransDetailDTO();
       dtoDetail.setStockTransId(stockTransId);
       dtoDetail.setOrderId(dto.getOrderId());
       dtoDetail.setGoodsType(dto.getGoodsType());
       dtoDetail.setGoodsTypeName(dto.getGoodsTypeName());
       dtoDetail.setGoodsId(dto.getGoodsId());
       dtoDetail.setGoodsCode(dto.getGoodsCode());
       dtoDetail.setGoodsIsSerial(dto.getGoodsIsSerial());
       dtoDetail.setGoodsState(dto.getGoodsState());
       dtoDetail.setGoodsStateName(dto.getGoodsStateName());
       dtoDetail.setGoodsName(dto.getGoodsNameImport());
       dtoDetail.setGoodsUnitId(dto.getGoodsUnitId());
       dtoDetail.setGoodsUnitName(dto.getGoodsUnitName());
       dtoDetail.setAmountOrder(dto.getAmountOrder());
       dtoDetail.setAmountReal(dto.getAmountOrder());
       dtoDetail.setTotalPrice(dto.getTotalPrice());
       return dtoDetail;
   }
   private AIOStockGoodsTotalDTO toAioStockGoodsTotalDTO(StockDTO stockDTO, AIOSynStockTransDetailDTO detailDTO) {
       AIOStockGoodsTotalDTO dtoTotal = new AIOStockGoodsTotalDTO();
       dtoTotal.setStockId(stockDTO.getStockId());
       dtoTotal.setStockCode(stockDTO.getCode());
       dtoTotal.setGoodsId(detailDTO.getGoodsId());
       dtoTotal.setGoodsState(detailDTO.getGoodsState());
       dtoTotal.setGoodsStateName(detailDTO.getGoodsStateName());
       dtoTotal.setGoodsCode(detailDTO.getGoodsCode());
       dtoTotal.setGoodsName(detailDTO.getGoodsNameImport());
       dtoTotal.setStockName(stockDTO.getName());
       dtoTotal.setGoodsTypeName(detailDTO.getGoodsTypeName());
       dtoTotal.setGoodsType(Long.parseLong(detailDTO.getGoodsType()));
       dtoTotal.setGoodsIsSerial(detailDTO.getGoodsIsSerial());
       dtoTotal.setGoodsUnitId(detailDTO.getGoodsUnitId());
       dtoTotal.setGoodsUnitName(detailDTO.getGoodsUnitName());
       dtoTotal.setAmount(detailDTO.getAmountOrder());
       dtoTotal.setChangeDate(new Date());
       dtoTotal.setAmountIssue(detailDTO.getAmountOrder());
       return dtoTotal;
   }
//   hoanm1_20200523_vsmart_end

    HttpHeaders HEADERS;
    private HttpHeaders getDefaultHeader() {
        if (HEADERS == null) {
            HEADERS = new HttpHeaders();
            HEADERS.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HEADERS.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
//            HEADERS.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
        }
        return HEADERS;
    }

}


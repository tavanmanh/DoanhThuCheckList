package com.viettel.coms.business;

import com.viettel.coms.dao.TangentCustomerCallbotContentDAO;
import com.viettel.coms.dao.TangentCustomerNoticeDAO;
import com.viettel.coms.dto.TangentCustomerNoticeDTO;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.viettel.cat.dto.CatPartnerDTO;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.cat.dto.ConstructionImageInfo;
import com.viettel.coms.bo.ResultSolutionBO;
import com.viettel.coms.bo.ResultTangentBO;
import com.viettel.coms.bo.ResultTangentGPTHBO;
import com.viettel.coms.bo.TangentCustomerBO;
import com.viettel.coms.dao.DetailTangentCustomerGPTHDAO;
import com.viettel.coms.dao.ResultSolutionGPTHDAO;
import com.viettel.coms.dao.ResultTangentDetailNoDAO;
import com.viettel.coms.dao.ResultTangentDetailYesDAO;
import com.viettel.coms.dao.ResultTangentGPTHDAO;
import com.viettel.coms.dao.TangentCustomerGPTHDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.coms.dto.DetailTangentCustomerGPTHDTO;
import com.viettel.coms.dto.ResultSolutionGPTHDTO;
import com.viettel.coms.dto.ResultTangentGPTHDTO;
import com.viettel.coms.dto.ResultTangentDetailYesDTO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.coms.dto.SysUserRequest;
import com.viettel.coms.dto.TangentCustomerGPTHDTO;
import com.viettel.coms.dto.TangentCustomerRequest;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.utils.CallApiUtils;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.coms.utils.InputSlotsObject;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.utils.ImageUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Service("tangentCustomerGPTHBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TangentCustomerGPTHBusinessImpl
		extends BaseFWBusinessImpl<TangentCustomerGPTHDAO, TangentCustomerGPTHDTO, TangentCustomerBO>
		implements TangentCustomerBusiness {

	@Autowired
	private TangentCustomerGPTHDAO tangentCustomerGPTHDAO;

	@Autowired
	private ResultTangentGPTHDAO resultTangentGPTHDAO;

	@Autowired
	private UtilAttachDocumentDAO utilAttachDocumentDAO;

	@Autowired
	private ResultTangentDetailYesDAO resultTangentDetailYesDAO;

	@Autowired
	private ResultTangentDetailNoDAO resultTangentDetailNoDAO;

	@Autowired
	private TangentCustomerNoticeDAO tangentCustomerNoticeDAO;

	@Autowired
	private TangentCustomerCallbotContentDAO tangentCustomerCallbotContentDAO;

	@Context
	HttpServletRequest request;

	@Value("${folder_upload2}")
	private String folder2Upload;
	// hoanm1_20200718_start
	// @Value("${input_image_sub_folder_upload}")
	@Value("${input_sub_folder_upload}")
	// hoanm1_20200718_end
	private String input_image_sub_folder_upload;

	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;

	// Huypq-24062021-start
	@Value("${url_crm_service}")
	private String url_crm_service;
	// Huy-end
	// Huypq-04042022-start
	@Value("${call_bot.url}")
	private String call_bot_url;
	@Value("${call_bot.api_key}")
	private String call_bot_api_key;
	@Value("${call_bot.key}")
	private String call_bot_key;
	@Value("${call_bot.value}")
	private String call_bot_value;
	@Value("${url_aio}")
	private String url_aio;
	// Huy-end

	@Autowired
	private ResultSolutionGPTHDAO resultSolutionGPTHDAO;

	@Autowired
	private DetailTangentCustomerGPTHDAO detailTangentCustomerGPTHDAO;

	private final String GROUP_ID = "9008143";

	public DataListDTO doSearch(TangentCustomerGPTHDTO obj, HttpServletRequest request) throws ParseException {
		List<TangentCustomerGPTHDTO> ls = new ArrayList<>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> provinceIdLst = ConvertData.convertStringToList(groupId, ",");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		if (provinceIdLst != null && provinceIdLst.size() > 0) {
			ls = tangentCustomerGPTHDAO.doSearch(obj, provinceIdLst);
			for (TangentCustomerGPTHDTO tangentCustomer : ls) {
				ResultTangentGPTHDTO tangent = new ResultTangentGPTHDTO();
				tangent.setTangentCustomerGPTHId(tangentCustomer.getTangentCustomerGPTHId());
				if ("1".equals(tangentCustomer.getStatus()) == false) {
					tangent = resultTangentGPTHDAO.getMaxAppointmentDateByTangentCustomerId(tangent);

				}

				if ("1".equals(tangentCustomer.getStatus()) == true || "2".equals(tangentCustomer.getStatus()) == true
						|| "3".equals(tangentCustomer.getStatus()) == true
						|| "4".equals(tangentCustomer.getStatus()) == true
						|| "8".equals(tangentCustomer.getStatus()) == true) {

					if ("1".equals(tangentCustomer.getStatus()) == true) {
						tangentCustomer.setNextTangentTime(tangentCustomer.getSuggestTime());
					} else if ("2".equals(tangentCustomer.getStatus()) == true) {
						tangentCustomer.setResultTangentGPTHDTO(tangent);
						tangentCustomer.setNextTangentTime(tangent.getAppointmentDate());
					} else if ("4".equals(tangentCustomer.getStatus()) == true) {
						tangentCustomer.setResultTangentGPTHDTO(tangent);

					} else {
						tangentCustomer.setResultTangentGPTHDTO(tangent);
						ResultSolutionGPTHDTO resultSolution = resultSolutionGPTHDAO.getResultSolutionFieldByTargentCustomerId(tangentCustomer.getTangentCustomerGPTHId());
						tangentCustomer.setResultSolutionGPTHDTO(resultSolution);
						if (resultSolution != null) {
							tangentCustomer.setResultSolutionGPTHDTO(resultSolution);

							if ("3".equals(tangentCustomer.getStatus()) == true) {
								if (resultSolution.getPresentSolutionDate() != null)
									tangentCustomer.setNextTangentTime(resultSolution.getPresentSolutionDate());
							}

						} else {
							tangentCustomer.setNextTangentTime(null);
							tangentCustomer.setResultSolutionGPTHDTO(null);
						}
					}
				} else if ("5".equals(tangentCustomer.getStatus()) == true
						|| "6".equals(tangentCustomer.getStatus()) == true
						|| "7".equals(tangentCustomer.getStatus()) == true
						|| "9".equals(tangentCustomer.getStatus()) == true) {
					ResultSolutionGPTHDTO resultSolution = resultSolutionGPTHDAO
							.getResultSolutionFieldByTargentCustomerId(tangentCustomer.getTangentCustomerGPTHId());
					if ("5".equals(tangentCustomer.getStatus()) == true) {
						tangentCustomer.setResultTangentGPTHDTO(tangent);
						if (resultSolution != null) {
							tangentCustomer.setResultSolutionGPTHDTO(resultSolution);
							if (resultSolution.getSignDate() != null)
								tangentCustomer.setNextTangentTime(resultSolution.getSignDate());
						} else
							tangentCustomer.setNextTangentTime(null);
					} else if ("6".equals(tangentCustomer.getStatus()) == true) {
						tangentCustomer.setResultTangentGPTHDTO(tangent);
						if (resultSolution != null) {
							tangentCustomer.setResultSolutionGPTHDTO(resultSolution);
							if (resultSolution.getPresentSolutionDate() != null)
								tangentCustomer.setNextTangentTime(resultSolution.getPresentSolutionDate());
						} else
							tangentCustomer.setNextTangentTime(null);
					} else {
						tangentCustomer.setResultTangentGPTHDTO(tangent);
						if (null != resultSolution) {
							tangentCustomer.setResultSolutionGPTHDTO(resultSolution);
						} else
							tangentCustomer.setResultSolutionGPTHDTO(null);
					}

				}
			}
		}

		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setStart(1);
		data.setSize(obj.getPageSize());
		data.setTotal(obj.getTotalRecord());
		return data;
	}

	public Long save(TangentCustomerGPTHDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setCreatedDate(new Date());
		obj.setCreatedUser(user.getVpsUserInfo().getSysUserId());
		obj.setStatus("1");
		if (obj.getIsInternalSource() != null) {
			if (obj.getIsInternalSource() == 1l)
				obj.setSource("1");
		}
		if (obj.getIsSocialSource() != null) {
			if (1l == obj.getIsSocialSource())
				obj.setSource("2");
		}
		if (obj.getIsCustomerServiceSource() != null) {
			if (1l == obj.getIsCustomerServiceSource())
				obj.setSource("3");
		}
		if (obj.getApartmentNumber() != null)
			obj.setAddress(obj.getApartmentNumber() + ", " + obj.getCommuneName() + ", " + obj.getDistrictName() + ", "
					+ obj.getProvinceCode());
		else
			obj.setAddress(obj.getCommuneName() + ", " + obj.getDistrictName() + ", " + obj.getProvinceCode());
//		List<TangentCustomerGPTHDTO> listCustomerValidate = tangentCustomerGPTHDAO.getDataTangentFollowPhoneNumber(obj.getCustomerPhone());
//		if (listCustomerValidate.size() == 0) {
			if (obj.getPartnerType() == 1) {
				obj.setPartnerCode("KHCN" + "-" + obj.getProvinceCode() + "-" + obj.getCustomerPhone());
			} else
				obj.setPartnerCode("KHDN" + "-" + obj.getProvinceCode() + "-" + obj.getCustomerPhone());

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String content = "Đ/c được giao nhiệm vụ tiếp xúc khách hàng " + obj.getCustomerName() + " - "
					+ obj.getCustomerPhone() + ", đề nghị triển khai trước ngày "
					+ simpleDateFormat.format(obj.getSuggestTime());
			tangentCustomerGPTHDAO.insertSendSMS(obj.getPerformerEmail(), obj.getPerformerPhoneNumber(),
					user.getVpsUserInfo().getSysUserId(), content);
			tangentCustomerGPTHDAO.insertSendEmail(obj.getPerformerEmail(), user.getVpsUserInfo().getSysUserId(), content);

			return tangentCustomerGPTHDAO.saveObject(obj.toModel());
//		} else {
//			throw new BusinessException("SĐT đã thuộc về 1 khách hàng/đối tác khác.");
//		}

	}

	public Long update(TangentCustomerGPTHDTO obj, HttpServletRequest request) throws Exception {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			// Boolean checkRoleUpdate =
			// VpsPermissionChecker.hasPermission(Constant.OperationKey.UPDATE,
			// Constant.AdResourceKey.TANGENT_CUSTOMER, request);
			// if(!checkRoleUpdate) {
			// throw new BusinessException("Bạn không có quyền cập nhật bản ghi !");
			// }
			boolean isUpdatePartnerTTHT = false;
			CatPartnerDTO origin = new CatPartnerDTO();
//			if (obj.getOldPhone().equals(obj.getCustomerPhone()) == false) {
//				List<TangentCustomerGPTHDTO> listCustomerValidate = tangentCustomerGPTHDAO
//						.getDataTangentFollowPhoneNumber(obj.getCustomerPhone());
//				if (listCustomerValidate.size() > 0) {
//					throw new BusinessException("SĐT đã thuộc về 1 khách hàng khác.");
//				} else {
					if (null != obj.getPartnerType()) {
						TangentCustomerGPTHDTO search = new TangentCustomerGPTHDTO();search.setTangentCustomerGPTHId(obj.getTangentCustomerGPTHId());
						origin = (CatPartnerDTO) tangentCustomerGPTHDAO.getPartnerTTHT(search);
						if (null != origin) {
								String[] codeSplit = origin.getCode().split("-", 3);
								origin.setPhone(obj.getCustomerPhone());
								origin.setCode(codeSplit[0] + "-" + codeSplit[1] + "-" + origin.getPhone());
								origin.setUpdatedDate(new Date());
								origin.setUpdatedUserId(user.getVpsUserInfo().getSysUserId());
								obj.setPartnerCode(origin.getCode());
								isUpdatePartnerTTHT = true;
						}else {
							if (obj.getPartnerType() == 1) {
								obj.setPartnerCode("KHCN" + "-" + obj.getProvinceCode() + "-" + obj.getCustomerPhone());
							} else
								obj.setPartnerCode("KHDN" + "-" + obj.getProvinceCode() + "-" + obj.getCustomerPhone());
						}
						
					}
//				}
//			}

			obj.setUpdatedDate(new Date());
			if(user != null && user.getVpsUserInfo() != null) {
				obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
			}
			
			if (obj.getApartmentNumber() != null)
				obj.setAddress(obj.getApartmentNumber() + ", " + obj.getCommuneName() + ", " + obj.getDistrictName() + ", "
						+ obj.getProvinceCode());
			else
				obj.setAddress(obj.getCommuneName() + ", " + obj.getDistrictName() + ", " + obj.getProvinceCode());

			SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			try {
				obj.setCreatedDate(formatDate.parse(obj.getCreatedDateDb()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			Long tangentCustomerId = tangentCustomerGPTHDAO.updateObject(obj.toModel());
			if (tangentCustomerId == 0l) {
				throw new BusinessException("Lỗi xảy ra khi cập nhật thông tin khách hàng và yêu cầu tiếp xúc.");
			} else {
				if (isUpdatePartnerTTHT == false)
					return tangentCustomerId;
				else
					return tangentCustomerGPTHDAO.updatePartnerTTHT(origin);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
		
	}

	public DataListDTO doSearchDistrictByProvinceCode(TangentCustomerGPTHDTO obj) {
		List<TangentCustomerGPTHDTO> ls = new ArrayList<TangentCustomerGPTHDTO>();
		ls = tangentCustomerGPTHDAO.doSearchDistrictByProvinceCode(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	public DataListDTO doSearchCommunneByDistrict(TangentCustomerGPTHDTO obj) {
		List<TangentCustomerGPTHDTO> ls = new ArrayList<TangentCustomerGPTHDTO>();
		ls = tangentCustomerGPTHDAO.doSearchCommunneByDistrict(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	public ConfigStaffTangentDTO getUserConfigTagent(ConfigStaffTangentDTO obj) {
		return tangentCustomerGPTHDAO.getUserConfigTagent(obj.getCatProvinceId(), obj.getType());
	}

	public Long deleteRecord(TangentCustomerGPTHDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		return tangentCustomerGPTHDAO.deleteRecord(obj.getTangentCustomerGPTHId(), user.getVpsUserInfo().getSysUserId());
	}

	public void saveDetailAppoinmentDate(TangentCustomerGPTHDTO obj, ResultTangentGPTHDTO ResultTangentGPTHDTO,
			KttsUserSession user) {
		// update tangent customer
		obj.setStatus("2");
		obj.setUpdatedDate(new Date());
		obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
		// Lưu ResultTangent
		List<ResultTangentGPTHBO> resultBo = resultTangentGPTHDAO.getResultTangentByTangentCustomerId(ResultTangentGPTHDTO);
		if (resultBo.size() == 0) {
			ResultTangentGPTHDTO.setOrderResultTangent("1");
		} else {
			ResultTangentGPTHDTO.setOrderResultTangent(String.valueOf(resultBo.size() + 1));
		}
		if (ResultTangentGPTHDTO.getResultTangentType() != null) {
			ResultTangentGPTHDTO.setRealityTangentDate(new Date());
		}
		// ResultTangentGPTHDTO.setApprovedStatus("0");
		ResultTangentGPTHDTO.setCreatedDate(new Date());
		ResultTangentGPTHDTO.setCreatedUser(user.getVpsUserInfo().getSysUserId());
		ResultTangentGPTHDTO.setPerformerId(user.getVpsUserInfo().getSysUserId());
		Long resultTangentId = resultTangentGPTHDAO.saveObject(ResultTangentGPTHDTO.toModel());

		// gui toi nhan vien thuc hien
		String content = "Yêu cầu tiếp xúc khách hàng " + obj.getCustomerName() + " của Đ/c đã được nhân viên: "
				+ obj.getPerformerCode() + "-" + obj.getPerformerName() + " xác nhận thời gian triển khai tiếp xúc ";
		tangentCustomerGPTHDAO.insertSendEmail(obj.getCreatedEmail(), user.getVpsUserInfo().getSysUserId(), content);
		tangentCustomerGPTHDAO.insertSendSMS(obj.getCreatedEmail(), obj.getCreatedPhoneNumber(),
				user.getVpsUserInfo().getSysUserId(), content);
		// gui toi quan ly
		List<TangentCustomerGPTHDTO> lstManager = tangentCustomerGPTHDAO.getListManager(obj.getProvinceId());
		if (lstManager.size() > 0) {
			for (TangentCustomerGPTHDTO dto : lstManager) {
				content = "Yêu cầu tiếp xúc khách hàng " + obj.getCustomerName()
						+ " của CNKT Tỉnh/TP đã được xác nhận thời gian triển khai ";
				tangentCustomerGPTHDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(), content);
				tangentCustomerGPTHDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
						user.getVpsUserInfo().getSysUserId(), content);
			}
		}

		// Có nhu cầu
		if (ResultTangentGPTHDTO.getResultTangentType() != null && ResultTangentGPTHDTO.getResultTangentType().equals("1")) {
			if (obj.getStatus().equals("2") == true && obj.getResultSolutionGPTHDTO() != null) {
				Long solutionId = saveResultSolution(obj, request);
				tangentCustomerGPTHDAO.getSession().flush();
				tangentCustomerGPTHDAO.getSession().clear();
				System.out.println("" + solutionId);
			}
			obj.setStatus("3");
			obj.setUpdatedDate(new Date());
			obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
			// gui ket qua tiep xuc toi quan ly
			// List<TangentCustomerGPTHDTO> lstManager =
			// tangentCustomerGPTHDAO.getListManager(obj.getProvinceId());
			if (obj.getOldStatus().equals(obj.getStatus()) == false) {
				if (lstManager.size() > 0) {
					for (TangentCustomerGPTHDTO dto : lstManager) {
						content = "Khách hàng " + obj.getCustomerName() + " đã được nhân viên " + obj.getPerformerCode()
								+ "-" + obj.getPerformerName()
								+ " tiếp xúc thành công, kết quả tiếp xúc: Khách hàng có nhu cầu";
						tangentCustomerGPTHDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(),
								content);
						tangentCustomerGPTHDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
								user.getVpsUserInfo().getSysUserId(), content);
					}
				}
				// gui ket qua tiep xuc toi TTHT
				List<TangentCustomerGPTHDTO> lstUserTTHT = tangentCustomerGPTHDAO.getListUserTTHT();
				if (lstUserTTHT.size() > 0) {
					for (TangentCustomerGPTHDTO dto : lstUserTTHT) {
						content = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
								+ obj.getProvinceCode()
								+ " tiếp xúc thành công, kết quả tiếp xúc: Khách hàng có nhu cầu";
						tangentCustomerGPTHDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(),
								content);
						tangentCustomerGPTHDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
								user.getVpsUserInfo().getSysUserId(), content);
					}
				}
			}

			// if (ResultTangentGPTHDTO.getFileReceipts() != null) {
			//
			// List<UtilAttachDocumentDTO> lstAttach = ResultTangentGPTHDTO.getFileReceipts();
			// for(UtilAttachDocumentDTO attach : lstAttach) {
			// attach.setStatus("1");
			// attach.setObjectId(resultTangentId);
			// attach.setType("KQTX");
			// attach.setCreatedDate(new Date());
			// attach.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
			// attach.setCreatedUserName(user.getVpsUserInfo().getFullName());
			// attach.setDescription("File đính kèm phiếu thu thập thông tin khách hàng");
			// utilAttachDocumentDAO.saveObject(attach.toModel());
			// }
			// }

			// Có thiết kế
			// if (ResultTangentGPTHDTO.getContructionDesign()!=null &&
			// ResultTangentGPTHDTO.getContructionDesign().equals("1")) {
			// Save Result Tangent Yes
			ResultTangentDetailYesDTO resultTangentYesDTO = obj.getResultTangentDetailYesDTO();
			// ResultTangentDetailYesDTO yesDb = null;
			// if(ResultTangentGPTHDTO.getResultTangentId()!=null) {
			// resultTangentDetailYesDAO.getListResultTangentYesByTangentCustomerId(ResultTangentGPTHDTO.getResultTangentId());
			// }
			if (resultTangentYesDTO != null) {
				resultTangentYesDTO.setResultTangentId(resultTangentId);
				// if(yesDb!=null) {
				// resultTangentYesDTO.setUpdatedDate(new Date());
				// resultTangentYesDTO.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
				// resultTangentDetailYesDAO.updateObject(resultTangentYesDTO.toModel());
				// } else {
				resultTangentYesDTO.setCreatedDate(new Date());
				resultTangentYesDTO.setCreatedUser(user.getVpsUserInfo().getSysUserId());
				Long objectId = resultTangentDetailYesDAO.saveObject(resultTangentYesDTO.toModel());

				if (resultTangentYesDTO.getLstImageDesign() != null) {
					List<UtilAttachDocumentDTO> lstImageDesign = resultTangentYesDTO.getLstImageDesign();
					for (UtilAttachDocumentDTO imageDesign : lstImageDesign) {
						imageDesign.setObjectId(objectId);
						imageDesign.setType("YCTX_DESIGN_CONSTRUCTION");
						imageDesign.setDescription("File mô tả phong cách thiết kế yêu cầu tiếp xúc khách hàng");
						utilAttachDocumentDAO.saveObject(imageDesign.toModel());
					}
				}
				// }
				// end
			}

			// }

			// Chưa có thiết kế
			// if (ResultTangentGPTHDTO.getContructionDesign()!=null &&
			// ResultTangentGPTHDTO.getContructionDesign().equals("0")) {
			// ResultTangentDetailNoDTO detailNo = obj.getResultTangentDetailNoDTO();
			// detailNo.setResultTangentId(resultTangentId);
			// Long noId = 0l;
			// detailNo.setCreatedDate(new Date());
			// detailNo.setCreatedUser(user.getVpsUserInfo().getSysUserId());
			// noId = resultTangentDetailNoDAO.saveObject(detailNo.toModel());
			//
			// if(ResultTangentGPTHDTO.getFileRedBook()!=null) {
			// List<UtilAttachDocumentDTO> lstAttachSoDo =
			// ResultTangentGPTHDTO.getFileRedBook();
			// for(UtilAttachDocumentDTO attachSoDo : lstAttachSoDo) {
			// attachSoDo.setStatus("1");
			// attachSoDo.setObjectId(noId);
			// attachSoDo.setType("SODO");
			// attachSoDo.setDescription("File hình ảnh sổ đỏ");
			// utilAttachDocumentDAO.saveObject(attachSoDo.toModel());
			// }
			// }
			//
			// if(ResultTangentGPTHDTO.getFileTopographic()!=null) {
			// List<UtilAttachDocumentDTO> lstAttachDiaHinh =
			// ResultTangentGPTHDTO.getFileTopographic();
			// for(UtilAttachDocumentDTO attachDiaHinh : lstAttachDiaHinh) {
			// attachDiaHinh.setStatus("1");
			// attachDiaHinh.setObjectId(noId);
			// attachDiaHinh.setType("DHTD");
			// attachDiaHinh.setDescription("File địa hình thửa đất");
			// utilAttachDocumentDAO.saveObject(attachDiaHinh.toModel());
			// }
			// }
			// }

		}
	}

	public void updateResultTangent(ResultTangentGPTHDTO ResultTangentGPTHDTO, TangentCustomerGPTHDTO obj,
			HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		Transaction tx = null;
		Session session = null;
		boolean isDetailBegin = false;
		// Có nhu cầu
		try {
			if (ResultTangentGPTHDTO.getResultTangentGPTHId() != null) {
				session = tangentCustomerGPTHDAO.getSessionFactory().openSession();
				tx = session.beginTransaction();
				String validateStatus = obj.getStatus();
				if (ResultTangentGPTHDTO.getResultTangentType() != null
						&& ResultTangentGPTHDTO.getResultTangentType().equals("1")) {
					obj.setStatus("3");
					obj.setUpdatedDate(new Date());
					obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
					// gui ket qua tiep xuc toi quan ly
					if (obj.getOldStatus().equals(obj.getStatus()) == false) {
						List<TangentCustomerGPTHDTO> lstManager = tangentCustomerGPTHDAO.getListManager(obj.getProvinceId());
						if (lstManager.size() > 0) {
							for (TangentCustomerGPTHDTO dto : lstManager) {
								String content = "Khách hàng " + obj.getCustomerName() + " đã được nhân viên "
										+ obj.getPerformerCode() + "-" + obj.getPerformerName()
										+ " tiếp xúc thành công, kết quả tiếp xúc: Khách hàng có nhu cầu";
								tangentCustomerGPTHDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(),
										content);
								tangentCustomerGPTHDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
										user.getVpsUserInfo().getSysUserId(), content);
							}
						}
						// gui ket qua tiep xuc toi TTHT
						List<TangentCustomerGPTHDTO> lstUserTTHT = tangentCustomerGPTHDAO.getListUserTTHT();
						if (lstUserTTHT.size() > 0) {
							for (TangentCustomerGPTHDTO dto : lstUserTTHT) {
								String content = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
										+ obj.getProvinceCode()
										+ " tiếp xúc thành công, kết quả tiếp xúc: Khách hàng có nhu cầu";
								tangentCustomerGPTHDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(),
										content);
								tangentCustomerGPTHDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
										user.getVpsUserInfo().getSysUserId(), content);
							}
						}
					}

//					ResultTangentDetailYesDTO resultTangentYesDTO = obj.getResultTangentDetailYesDTO();
//					ResultTangentDetailYesDTO yesDb = resultTangentDetailYesDAO
//							.getListResultTangentYesByTangentCustomerId(ResultTangentGPTHDTO.getResultTangentGPTHId());
//					resultTangentYesDTO.setResultTangentId(ResultTangentGPTHDTO.getResultTangentGPTHId());
//					if (yesDb != null) {
//						resultTangentYesDTO.setUpdatedDate(new Date());
//						resultTangentYesDTO.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
//
//						SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//						try {
//							resultTangentYesDTO
//									.setCreatedDate(formatDate.parse(resultTangentYesDTO.getCreatedDateDb()));
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							//e.printStackTrace();
//						}
//
//						Long resultTangentDetailYesId = resultTangentDetailYesDAO
//								.updateObject(resultTangentYesDTO.toModel());
//						if (resultTangentDetailYesId == 0l) {
//							if (tx != null)
//								tx.rollback();
//						} else {
//							if (resultTangentYesDTO.getLstImageDesign() != null) {
//								utilAttachDocumentDAO.deleteExtend(resultTangentYesDTO.getResultTangentDetailYesId(),
//										"YCTX_DESIGN_CONSTRUCTION");
//								List<UtilAttachDocumentDTO> lstImageDesign = resultTangentYesDTO.getLstImageDesign();
//								for (UtilAttachDocumentDTO imageDesign : lstImageDesign) {
//									imageDesign.setObjectId(resultTangentYesDTO.getResultTangentDetailYesId());
//									imageDesign.setType("YCTX_DESIGN_CONSTRUCTION");
//									imageDesign.setDescription(
//											"File mô tả phong cách thiết kế yêu cầu tiếp xúc khách hàng");
//									utilAttachDocumentDAO.saveObject(imageDesign.toModel());
//								}
//							}
//							resultTangentYesDTO.getDetailCustomerGPTH()
//									.setTangentCustomerGPTHId(ResultTangentGPTHDTO.getResultTangentGPTHId());
//							isDetailBegin = true;
//						}
//
//					} else {
//						resultTangentYesDTO.setCreatedDate(new Date());
//						resultTangentYesDTO.setCreatedUser(user.getVpsUserInfo().getSysUserId());
//						Long objectId = resultTangentDetailYesDAO.saveObject(resultTangentYesDTO.toModel());
//
//						if (objectId == 0l) {
//							if (tx != null)
//								tx.rollback();
//						} else {
//							if (resultTangentYesDTO.getLstImageDesign() != null) {
//								List<UtilAttachDocumentDTO> lstImageDesign = resultTangentYesDTO.getLstImageDesign();
//								for (UtilAttachDocumentDTO imageDesign : lstImageDesign) {
//									imageDesign.setObjectId(objectId);
//									imageDesign.setType("YCTX_DESIGN_CONSTRUCTION");
//									imageDesign.setDescription(
//											"File mô tả phong cách thiết kế yêu cầu tiếp xúc khách hàng");
//									utilAttachDocumentDAO.saveObject(imageDesign.toModel());
//								}
//							}
//							resultTangentYesDTO.getDetailCustomerGPTH().setTangentCustomerGPTHId(ResultTangentGPTHDTO.getResultTangentGPTHId());
//							isDetailBegin = true;
//
//						}
//
//					}
					if (obj.getStatus() != null &&  !obj.getStatus().equals("1")) {
						if (null == obj.getDetailTangentCustomerGPTH().getDetailTangentCustomerGPTHId()) {
							obj.getDetailTangentCustomerGPTH().setTangentCustomerGPTHId(obj.getTangentCustomerGPTHId());
							Long detailTangentCustomerId = detailTangentCustomerGPTHDAO.saveObject(obj.getDetailTangentCustomerGPTH().toModel());
							if (detailTangentCustomerId == 0l) {
								if (tx != null)
									tx.rollback();
							}
						} else {
							Long detailTangentCustomerId = detailTangentCustomerGPTHDAO.updateObject(obj.getDetailTangentCustomerGPTH().toModel());
							if (detailTangentCustomerId == 0l) {
								if (tx != null)
									tx.rollback();
							}
						}

					}
				}

				ResultTangentGPTHDTO.setUpdatedDate(new Date());
				ResultTangentGPTHDTO.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
				ResultTangentGPTHDTO tang = resultTangentGPTHDAO.getResultTangentTypeByResultTangentId(ResultTangentGPTHDTO.getResultTangentGPTHId());
				if (tang.getResultTangentType() == null
						&& StringUtils.isNotBlank(ResultTangentGPTHDTO.getResultTangentType())) {
					ResultTangentGPTHDTO.setRealityTangentDate(new Date());
				}

				SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				try {
					ResultTangentGPTHDTO.setCreatedDate(formatDate.parse(ResultTangentGPTHDTO.getCreatedDateDb()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}

				Long resultTangentId = resultTangentGPTHDAO.updateObject(ResultTangentGPTHDTO.toModel());
				if (resultTangentId == 0l) {
					if (tx != null)
						tx.rollback();
				} else {
					if (validateStatus.equals("2") == true && obj.getResultSolutionGPTHDTO() != null) {
						Long solutionId = saveResultSolution(obj, request);
						if (solutionId != 0l) {
							tangentCustomerGPTHDAO.getSession().flush();
							tangentCustomerGPTHDAO.getSession().clear();
							System.out.println("" + solutionId);
						}
					}
					tx.commit();
				}
			} else {
				saveDetailAppoinmentDate(obj, ResultTangentGPTHDTO, user);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			if (session != null) {
				if (session.isOpen())
					session.close();
			}
		}
	}

	public Long saveDetail(TangentCustomerGPTHDTO obj, HttpServletRequest request) throws Exception {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		// Save Result Tangent
		ResultTangentGPTHDTO resultTangentGPTHDTO = obj.getResultTangentGPTHDTO();
		resultTangentGPTHDTO.setTangentCustomerGPTHId(obj.getTangentCustomerGPTHId());

		updateResultTangent(resultTangentGPTHDTO, obj, request);

		// end
		SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			obj.setCreatedDate(formatDate.parse(obj.getCreatedDateDb()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		// huypq-24062021-start
		String linkCall = url_crm_service + "/misv.php?com=api&elem=Survey&func=Update";
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("opportunityId", obj.getCrmId());
		personJsonObject.put("surveyStatus", obj.getStatus());
		personJsonObject.put("reason", obj.getReasonRejection());
		String responseCallApiCrm = CallApiUtils.callApiCrm(linkCall, personJsonObject);
		System.out.println(responseCallApiCrm);
		// Huy-end
		Long tangentCustomerId = tangentCustomerGPTHDAO.updateObject(obj.toModel());
		tangentCustomerGPTHDAO.getSession().flush();
		tangentCustomerGPTHDAO.getSession().clear();
		return tangentCustomerId;
	}

	// Save lúc Lý do khách hàng từ chối
	public Long saveNotDemain(ResultTangentGPTHDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		Long id = 0l;
		if (obj.getResultTangentGPTHId() == null) {
			tangentCustomerGPTHDAO.updateStatusTangent(obj.getTangentCustomerGPTHDTO().getTangentCustomerGPTHId(), "4", null,
					user.getVpsUserInfo().getSysUserId());
			// tangentCustomerGPTHDAO.updateStatusTangent(obj.getTangentCustomerId(), "11",null,
			// user.getVpsUserInfo().getSysUserId());// duonghv13-29122021-edit
			obj.setApprovedStatus("0");
			obj.setPerformerId(obj.getTangentCustomerGPTHDTO().getPerformerId());
			obj.setOrderResultTangent("1");
			obj.setCreatedDate(new Date());
			obj.setCreatedUser(user.getVpsUserInfo().getSysUserId());
			id = resultTangentGPTHDAO.saveObject(obj.toModel());
		} else {
			tangentCustomerGPTHDAO.updateStatusTangent(obj.getTangentCustomerGPTHId(), "4", null,
					user.getVpsUserInfo().getSysUserId());
			// tangentCustomerGPTHDAO.updateStatusTangent(obj.getTangentCustomerId(), "11",null,
			// user.getVpsUserInfo().getSysUserId());// duonghv13-29122021-edit
			obj.setApprovedStatus("0");

			SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			try {
				obj.setCreatedDate(formatDate.parse(obj.getCreatedDateDb()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}

			obj.setUpdatedDate(new Date());
			obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());

			id = resultTangentGPTHDAO.updateObject(obj.toModel());
		}

		// gui ket qua tiep xuc toi quan ly
		List<TangentCustomerGPTHDTO> lstManager = tangentCustomerGPTHDAO
				.getListManager(obj.getTangentCustomerGPTHDTO().getProvinceId());
		if (lstManager.size() > 0) {
			for (TangentCustomerGPTHDTO dto : lstManager) {
				String content = "Khách hàng " + obj.getTangentCustomerGPTHDTO().getCustomerName() + " đã được nhân viên "
						+ obj.getTangentCustomerGPTHDTO().getPerformerCode() + "-"
						+ obj.getTangentCustomerGPTHDTO().getPerformerName()
						+ " tiếp xúc thành công, kết quả tiếp xúc: Khách hàng không có nhu cầu";
				tangentCustomerGPTHDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(), content);
				tangentCustomerGPTHDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
						user.getVpsUserInfo().getSysUserId(), content);
			}
		}
		// gui ket qua tiep xuc toi TTHT
		List<TangentCustomerGPTHDTO> lstUserTTHT = tangentCustomerGPTHDAO.getListUserTTHT();
		if (lstUserTTHT.size() > 0) {
			for (TangentCustomerGPTHDTO dto : lstUserTTHT) {
				String content = "Khách hàng " + obj.getTangentCustomerGPTHDTO().getCustomerName()
						+ " đã được CNKT Tỉnh/TP: " + obj.getTangentCustomerGPTHDTO().getProvinceCode()
						+ " tiếp xúc thành công, kết quả tiếp xúc: Khách hàng không có nhu cầu";
				tangentCustomerGPTHDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(), content);
				tangentCustomerGPTHDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
						user.getVpsUserInfo().getSysUserId(), content);
			}
		}
		return id;
	}

	// Save lúc phê duyệt / từ chối kết quả tiếp xúc
	public Long saveApproveOrReject(ResultTangentGPTHDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		Long id = 0l;
		// huypq-24062021-start
		/*String linkCall = url_crm_service + "/misv.php?com=api&elem=Survey&func=Update";
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("opportunityId",
				tangentCustomerGPTHDAO.getCrmIdByTangentCustomerId(obj.getTangentCustomerGPTHId()));
		personJsonObject.put("reason", obj.getReasonRejection());*/
		// Huy-end
		if (obj.getApprovedStatus() != null && obj.getApprovedStatus().equals("1")) {
			obj.setApprovedDate(new Date());
			obj.setApprovedUserId(user.getVpsUserInfo().getSysUserId());
			tangentCustomerGPTHDAO.updateStatusTangent(obj.getTangentCustomerGPTHId(), "9", null,
					user.getVpsUserInfo().getSysUserId());
			// tangentCustomerGPTHDAO.updateStatusTangent(obj.getTangentCustomerId(), "11",null,
			// user.getVpsUserInfo().getSysUserId());// duonghv13-29122021-edit
			SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			try {
				obj.setCreatedDate(formatDate.parse(obj.getCreatedDateDb()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}

			obj.setUpdatedDate(new Date());
			obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());

			id = resultTangentGPTHDAO.updateObject(obj.toModel());

			// huypq-24062021-start
//			personJsonObject.put("surveyStatus", "9");
			// personJsonObject.put("surveyStatus", "11");// duonghv13-29122021-edit
			// Huy-end
		} else if (obj.getApprovedStatus() != null && obj.getApprovedStatus().equals("2")) {
			obj.setApprovedDate(new Date());
			obj.setApprovedUserId(user.getVpsUserInfo().getSysUserId());
			tangentCustomerGPTHDAO.updateStatusTangent(obj.getTangentCustomerGPTHId(), "1", obj.getApprovedPerformerId(),
					user.getVpsUserInfo().getSysUserId());

			SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			try {
				obj.setCreatedDate(formatDate.parse(obj.getCreatedDateDb()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}

			obj.setUpdatedDate(new Date());
			obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());

			resultTangentGPTHDAO.updateObject(obj.toModel());
			// Lưu ResultTangent
			ResultTangentGPTHDTO ResultTangentGPTHDTO = new ResultTangentGPTHDTO();
			ResultTangentGPTHDTO.setTangentCustomerGPTHId(obj.getTangentCustomerGPTHId());
			List<ResultTangentGPTHBO> resultBo = resultTangentGPTHDAO.getResultTangentByTangentCustomerId(ResultTangentGPTHDTO);
			if (resultBo.size() == 0) {
				ResultTangentGPTHDTO.setOrderResultTangent("1");
			} else {
				ResultTangentGPTHDTO.setOrderResultTangent(String.valueOf(resultBo.size() + 1));
			}
			ResultTangentGPTHDTO.setApprovedStatus(null);
			ResultTangentGPTHDTO.setCreatedDate(new Date());
			ResultTangentGPTHDTO.setCreatedUser(user.getVpsUserInfo().getSysUserId());
			ResultTangentGPTHDTO.setPerformerId(obj.getApprovedPerformerId());
			ResultTangentGPTHDTO.setPerformerName(obj.getApprovedPerformerName());
			id = resultTangentGPTHDAO.saveObject(ResultTangentGPTHDTO.toModel());
			// huypq-24062021-start
//			personJsonObject.put("surveyStatus", "1");
			// Huy-end
			// Gửi sms and email
			ResultTangentGPTHDTO result = resultTangentGPTHDAO.getListResultTangentJoinSysUserByResultTangentId(obj);
			String contentGiao = "Đồng chí được giao tiếp xúc khách hàng " + obj.getCustomerName() + " do nhân viên "
					+ ResultTangentGPTHDTO.getPerformerName() + " không hoàn thành";
			String contentTuChoi = "Yêu cầu tiếp xúc khách hàng " + obj.getCustomerName()
					+ " đã được chuyển cho đồng chí " + obj.getApprovedPerformerName()
					+ " do đồng chí tiếp xúc không đạt";
			// bị từ chối
			tangentCustomerGPTHDAO.insertSendSMS(result.getPerformerEmail(), result.getPerformerPhone(),
					user.getVpsUserInfo().getSysUserId(), contentTuChoi);
			tangentCustomerGPTHDAO.insertSendEmail(result.getPerformerEmail(), user.getVpsUserInfo().getSysUserId(),
					contentTuChoi);
			// được giao tiếp xúc
			tangentCustomerGPTHDAO.insertSendSMS(obj.getApprovedPerformerEmail(), obj.getApprovedPerformerPhone(),
					user.getVpsUserInfo().getSysUserId(), contentGiao);
			tangentCustomerGPTHDAO.insertSendEmail(obj.getApprovedPerformerEmail(), user.getVpsUserInfo().getSysUserId(),
					contentGiao);
		}
		// huypq-24062021-start
//		String responseCallApiCrm = CallApiUtils.callApiCrm(linkCall, personJsonObject);
//		System.out.println(responseCallApiCrm);
		// Huy-end
		return id;
	}

	public Long checkRoleApproved(HttpServletRequest request) {
		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED, Constant.AdResourceKey.TANGENT_CUSTOMER,
				request)) {
			return 0l;
		}
		return 1l;
	}

	public TangentCustomerGPTHDTO getListResultTangentByTangentCustomerId(TangentCustomerGPTHDTO obj) {
		TangentCustomerGPTHDTO tangent = new TangentCustomerGPTHDTO();
		List<ResultTangentGPTHDTO> ls = tangentCustomerGPTHDAO.getListResultTangentJoinSysUserByTangentCustomerGPTHId(obj);
		DetailTangentCustomerGPTHDTO detailCustomer = new DetailTangentCustomerGPTHDTO();
		if (ls.size() > 0) {
			List<UtilAttachDocumentDTO> util = resultTangentGPTHDAO .getFileAttachByResultTangentId(ls.get(0).getResultTangentGPTHId(), "KQTX");
//			ResultTangentDetailYesDTO yesDto = resultTangentDetailYesDAO .getResultTangentYesByResultTangentId(ls.get(0).getResultTangentGPTHId());

			tangent.setTangentCustomerGPTHId(obj.getTangentCustomerGPTHId());
			if (null != obj.getPartnerType()) {
				tangent.setPartnerType(obj.getPartnerType());
				detailCustomer = tangentCustomerGPTHDAO.getDetailsTangentCustomer(tangent);
//				if (null != detailCustomer && null != detailCustomer.getDetailTangentCustomerGPTHId())
//					yesDto.setDetailCustomerGPTH(detailCustomer);
			}
//			List<UtilAttachDocumentDTO> lstImageDesign = resultTangentGPTHDAO.getFileAttachByResultTangentId(ls.get(0).getResultTangentGPTHId(), "YCTX_DESIGN_CONSTRUCTION");
//			if (lstImageDesign != null && lstImageDesign.size() > 0)
//				yesDto.setLstImageDesign(lstImageDesign);
//
//			ls.get(0).setResultTangentDetailYesDTO(yesDto); // Lấy dữ liệu Có thiết kế
			tangent.setResultTangentGPTHDTO(ls.get(0));
				tangent.setDetailTangentCustomerGPTH(detailCustomer);
		}

		List<ResultSolutionGPTHDTO> lstSolution = tangentCustomerGPTHDAO.getResultSolutionJoinSysUserByTangentCustomerId(obj.getTangentCustomerGPTHId());
		if (lstSolution.size() > 0) {
			List<UtilAttachDocumentDTO> utilGp = resultTangentGPTHDAO
					.getFileAttachByResultTangentId(lstSolution.get(0).getResultSolutionGPTHId(), "GP");
			lstSolution.get(0).setFileResultSolution(utilGp);
			tangent.setResultSolutionGPTHDTO(lstSolution.get(0));
		}

		// Lấy list lịch sử tiếp xúc
		for (ResultTangentGPTHDTO result : ls) {
			List<UtilAttachDocumentDTO> util = resultTangentGPTHDAO.getFileAttachByResultTangentId(result.getResultTangentGPTHId(), "KQTX");
			result.setFileReceipts(util);
			/*if (null != result.getResultTangentType()) {
				if (result.getResultTangentType().equals("1") == true) {
					if (null != obj.getPartnerType())
						result.setPartnerType(obj.getPartnerType());
					ResultTangentDetailYesDTO yesDetailDto = resultTangentDetailYesDAO.getResultTangentYesByResultTangentId(result.getResultTangentGPTHId());
					if (null != detailCustomer && null != detailCustomer.getDetailTangentCustomerGPTHId())
						yesDetailDto.setDetailCustomerGPTH(detailCustomer);
					List<UtilAttachDocumentDTO> lstYesImageDesign = resultTangentGPTHDAO
							.getFileAttachByResultTangentId(result.getResultTangentGPTHId(), "YCTX_DESIGN_CONSTRUCTION");
					if (lstYesImageDesign != null && lstYesImageDesign.size() > 0)
						yesDetailDto.setLstImageDesign(lstYesImageDesign);
					result.setResultTangentDetailYesDTO(yesDetailDto);
				}
			}*/

		}

		for (ResultSolutionGPTHDTO solution : lstSolution) {
			List<UtilAttachDocumentDTO> util = resultTangentGPTHDAO
					.getFileAttachByResultTangentId(solution.getResultSolutionGPTHId(), "GP");
			solution.setFileResultSolution(util);
		}

		tangent.setListResultTangentGPTH(ls);
		tangent.setListResultSolutionGPTH(lstSolution);
		return tangent;
	}

	// hoanm1_20200612_start
	public TangentCustomerGPTHDTO getCountStateTangentCustomer(SysUserRequest request) {
		TangentCustomerGPTHDTO dto = tangentCustomerGPTHDAO.getCountStateTangentCustomer(request);
		return dto;
	}

	public List<TangentCustomerGPTHDTO> getListTangentCustomer(SysUserRequest request) {
		return tangentCustomerGPTHDAO.getListTangentCustomer(request);
	}

	public int updateTangentCustomer(TangentCustomerRequest request) throws ParseException {
		return tangentCustomerGPTHDAO.updateTangentCustomer(request);
	}

	public List<ConstructionImageInfo> getImagesResultSolution(TangentCustomerRequest request) {
		List<ConstructionImageInfo> listImageResponse = new ArrayList<>();
		List<ConstructionImageInfo> listImage = tangentCustomerGPTHDAO
				.getImagesResultSolutionId(request.getTangentCustomerGPTHDTO().getResultSolutionId());
		listImageResponse = getResultSolutionImages(listImage);
		return listImageResponse;
	}

	public List<ConstructionImageInfo> getResultSolutionImages(List<ConstructionImageInfo> lstImages) {
		List<ConstructionImageInfo> result = new ArrayList<>();
		for (ConstructionImageInfo packageImage : lstImages) {
			try {
				// hoanm1_20200718_start
				String imagePath = UEncrypt.decryptFileUploadPath(packageImage.getImagePath());
				String fullPath = folder2Upload + File.separator + imagePath;
				// hoanm1_20200718_end
				String base64Image = ImageUtil.convertImageToBase64(fullPath);
				ConstructionImageInfo obj = new ConstructionImageInfo();
				obj.setImageName(packageImage.getImageName());
				obj.setBase64String(base64Image);
				obj.setImagePath(fullPath);
				obj.setStatus(1L);
				obj.setUtilAttachDocumentId(packageImage.getUtilAttachDocumentId());
				result.add(obj);
			} catch (Exception e) {
				continue;
			}
		}

		return result;
	}

	public List<TangentCustomerGPTHDTO> getListContract(SysUserRequest request) {
		return tangentCustomerGPTHDAO.getListContract(request);
	}

	public Long addTangentCustomer(TangentCustomerRequest dto) {
		return tangentCustomerGPTHDAO.addTangentCustomer(dto);
	}

	public Long updateTangentCustomerCreate(TangentCustomerRequest request) {
		return tangentCustomerGPTHDAO.updateTangentCustomerCreate(request);
	}

	public List<TangentCustomerGPTHDTO> getDataProvinceCity() {
		return tangentCustomerGPTHDAO.getDataProvinceCity();
	}

	public List<TangentCustomerGPTHDTO> getDataDistrict(TangentCustomerGPTHDTO obj) {
		return tangentCustomerGPTHDAO.getDataDistrict(obj.getParentId());
	}

	public List<TangentCustomerGPTHDTO> getDataWard(TangentCustomerGPTHDTO obj) {
		return tangentCustomerGPTHDAO.getDataWard(obj.getParentId());
	}

	// hoanm1_20200612_end
	// huypq-16062020-start
	public DataListDTO getAllContractXdddSuccess(ResultSolutionGPTHDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		List<ResultSolutionGPTHDTO> ls = resultSolutionGPTHDAO.getAllContractXdddSuccess(obj,
				user.getVpsUserInfo().getSysGroupId());
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setStart(1);
		data.setSize(obj.getPageSize());
		data.setTotal(obj.getTotalRecord());
		return data;
	}

	public ResultSolutionGPTHDTO getResultSolutionByContractId(Long contractId) {
		ResultSolutionGPTHDTO dto = resultSolutionGPTHDAO.getResultSolutionByContractId(contractId);
		return dto;
	}

	public Long saveResultSolution(TangentCustomerGPTHDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		boolean isFailSign = false;
		// List<ResultSolutionGPTHDTO> haveDb =
		// resultSolutionGPTHDAO.getResultSolutionJoinSysUserByTangentCustomerId(obj.getTangentCustomerId());
		// huypq-24062021-start
//		String linkCall = url_crm_service + "/misv.php?com=api&elem=Survey&func=Update";
//		JSONObject personJsonObject = new JSONObject();
//		personJsonObject.put("opportunityId",
//				tangentCustomerGPTHDAO.getCrmIdByTangentCustomerId(obj.getTangentCustomerGPTHId()));
//		personJsonObject.put("reason", "");
		// Huy-end
		Long id = 0l;
		ResultSolutionGPTHDTO solutionDto = obj.getResultSolutionGPTHDTO();
		if (solutionDto != null) {
			if (solutionDto.getContractId() == null) {
				solutionDto.setSignDate(null);
				solutionDto.setGuaranteeTime(null);
				solutionDto.setConstructTime(null);
			}
			if (solutionDto.getResultSolutionGPTHId() == null) {
				solutionDto.setCreatedDate(new Date());
				solutionDto.setCreatedUser(user.getVpsUserInfo().getSysUserId());

				solutionDto.setResultSolutionOrder(1l);
				solutionDto.setTangentCustomerGPTHId(obj.getTangentCustomerGPTHId());
				solutionDto.setPerformerId(obj.getPerformerId());
				Long solutionId = resultSolutionGPTHDAO.saveObject(solutionDto.toModel());
				if (solutionDto.getFileResultSolution() != null && solutionDto.getFileResultSolution().size() > 0) {
					List<UtilAttachDocumentDTO> lstAttachGiaiPhap = solutionDto.getFileResultSolution();
					for (UtilAttachDocumentDTO attachGiaiPhap : lstAttachGiaiPhap) {
						attachGiaiPhap.setStatus("1");
						attachGiaiPhap.setObjectId(solutionId);
						attachGiaiPhap.setType("GP");
						attachGiaiPhap.setDescription("File trình bày giải pháp với khách hàng");
						utilAttachDocumentDAO.saveObject(attachGiaiPhap.toModel());
					}
				}

				String content = "";
				String contentTTHT = "";
				if (solutionDto.getResultSolutionType() != null) {
					if (solutionDto.getResultSolutionType().equals("1")) {
						if (solutionDto.getContractId() == null) {
							obj.setStatus("5");
//							personJsonObject.put("surveyStatus", "5");
							content = "Công việc của khách hàng " + obj.getCustomerName() + " - " + obj.getCustomerPhone() + " đang chờ lên hợp đồng";
							contentTTHT = "Công việc của khách hàng " + obj.getCustomerName() + " - " + obj.getCustomerPhone() + " đang chờ lên hợp đồng";
						} else {
							obj.setStatus("8");
//							personJsonObject.put("surveyStatus", "8");
							content = "Khách hàng " + obj.getCustomerName()
									+ " đã được trình bày giải pháp, kết quả khách hàng đồng ý ký hợp đồng số: "
									+ solutionDto.getContractCode();
							contentTTHT = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
									+ obj.getProvinceCode()
									+ " trình bày giải pháp, kết quả khách hàng đồng ý ký hợp đồng số: "
									+ solutionDto.getContractCode();
						}
					}

					if (solutionDto.getResultSolutionType().equals("2")) {
						obj.setStatus("7");
						// obj.setStatus("11");
//						personJsonObject.put("surveyStatus", "7");
						// personJsonObject.put("surveyStatus", "11");
						content = "Khách hàng " + obj.getCustomerName()
								+ " đã được trình bày giải pháp, kết quả khách hàng từ chối do: "
								+ solutionDto.getContentResultSolution();
						contentTTHT = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
								+ obj.getProvinceCode() + " trình bày giải pháp, kết quả khách hàng từ chối do: "
								+ solutionDto.getContentResultSolution();
						//Huypq-04042022-start call api call bot Từ chối giải pháp và phê duyệt
						List<InputSlotsObject> lstMapCallBot = new ArrayList<>();
						InputSlotsObject object = new InputSlotsObject();
						object.setKey(call_bot_key);
						object.setValue(obj.getCustomerName());
						lstMapCallBot.add(object);
						String urlCallbot = call_bot_url + "/api/partner/v1/campaign/1045/createCall";
						JSONObject callbotRequest = new JSONObject();
						callbotRequest.put("api_key", call_bot_api_key);
						callbotRequest.put("customer_phone", obj.getCustomerPhone());
						callbotRequest.put("input_slots", lstMapCallBot);
//						Boolean connectStatus = CallApiUtils.connectionCallbot(urlCallbot, callbotRequest);
						JSONObject requestCallBot = new JSONObject();
						requestCallBot.put("urlCallbot", urlCallbot);
						requestCallBot.put("callbotRequest", callbotRequest);
						CallApiUtils.callApiResponseString(url_aio + "service/aioCallBotWsService/createCallBot", requestCallBot);
						//Huy-end
					}

					if (solutionDto.getResultSolutionType().equals("3")) {
						obj.setStatus("6");
//						personJsonObject.put("surveyStatus", "6");
						content = "Khách hàng " + obj.getCustomerName()
								+ " đã được trình bày giải pháp, kết quả khách hàng yêu cầu điều chỉnh giải pháp: "
								+ solutionDto.getContentResultSolution();
						contentTTHT = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
								+ obj.getProvinceCode()
								+ " trình bày giải pháp, kết quả khách hàng yêu cầu điều chỉnh giải pháp: "
								+ solutionDto.getContentResultSolution();
					}
				}

			} else {
				List<ResultSolutionGPTHDTO> dto = resultSolutionGPTHDAO
						.getResultSolutionByTangentCustomerId(obj.getTangentCustomerGPTHId());
				List<TangentCustomerGPTHDTO> lstManager = tangentCustomerGPTHDAO.getListManager(obj.getProvinceId());
				List<TangentCustomerGPTHDTO> lstUserTTHT = tangentCustomerGPTHDAO.getListUserTTHT();
				String content = "";
				String contentTTHT = "";
				if (dto.size() == 0) {
					solutionDto.setCreatedDate(new Date());
					solutionDto.setCreatedUser(user.getVpsUserInfo().getSysUserId());
					solutionDto.setTangentCustomerGPTHId(obj.getTangentCustomerGPTHId());
					solutionDto.setResultSolutionOrder((long) dto.size() + 1l);
					if (solutionDto.getResultSolutionType() != null) {
						if (solutionDto.getResultSolutionType().equals("1")) {
							if (solutionDto.getContractId() == null) {
								obj.setStatus("5");
//								personJsonObject.put("surveyStatus", "5");
								content = "Công việc của khách hàng " + obj.getCustomerName() + " - " + obj.getCustomerPhone() + " đang chờ lên hợp đồng";
								contentTTHT = "Công việc của khách hàng " + obj.getCustomerName() + " - " + obj.getCustomerPhone() + " đang chờ lên hợp đồng";

							} else {
								obj.setStatus("8");

//								personJsonObject.put("surveyStatus", "8");
								content = "Khách hàng " + obj.getCustomerName()
										+ " đã được trình bày giải pháp, kết quả khách hàng đồng ý ký hợp đồng số: "
										+ solutionDto.getContractCode();
								contentTTHT = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
										+ obj.getProvinceCode()
										+ " trình bày giải pháp, kết quả khách hàng đồng ý ký hợp đồng số: "
										+ solutionDto.getContractCode();
							}
						}

						if (solutionDto.getResultSolutionType().equals("2")) {
							obj.setStatus("7");
							// obj.setStatus("11");
//							personJsonObject.put("surveyStatus", "7");
							// personJsonObject.put("surveyStatus", "11");
							content = "Khách hàng " + obj.getCustomerName()
									+ " đã được trình bày giải pháp, kết quả khách hàng từ chối do: "
									+ solutionDto.getContentResultSolution();
							contentTTHT = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
									+ obj.getProvinceCode() + " trình bày giải pháp, kết quả khách hàng từ chối do: "
									+ solutionDto.getContentResultSolution();
							//Huypq-04042022-start call api call bot Từ chối giải pháp và phê duyệt
							List<InputSlotsObject> lstMapCallBot = new ArrayList<>();
							InputSlotsObject object = new InputSlotsObject();
							object.setKey(call_bot_key);
							object.setValue(obj.getCustomerName());
							lstMapCallBot.add(object);
							String urlCallbot = call_bot_url + "/api/partner/v1/campaign/1045/createCall";
							JSONObject callbotRequest = new JSONObject();
							callbotRequest.put("api_key", call_bot_api_key);
							callbotRequest.put("customer_phone", obj.getCustomerPhone());
							callbotRequest.put("input_slots", lstMapCallBot);
//							Boolean connectStatus = CallApiUtils.connectionCallbot(urlCallbot, callbotRequest);
							JSONObject requestCallBot = new JSONObject();
							requestCallBot.put("urlCallbot", urlCallbot);
							requestCallBot.put("callbotRequest", callbotRequest);
							CallApiUtils.callApiResponseString(url_aio + "service/aioCallBotWsService/createCallBot", requestCallBot);
							//Huy-end
						}

						if (solutionDto.getResultSolutionType().equals("3")) {
							obj.setStatus("6");
//							personJsonObject.put("surveyStatus", "6");
							content = "Khách hàng " + obj.getCustomerName()
									+ " đã được trình bày giải pháp, kết quả khách hàng yêu cầu điều chỉnh giải pháp: "
									+ solutionDto.getContentResultSolution();
							contentTTHT = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
									+ obj.getProvinceCode()
									+ " trình bày giải pháp, kết quả khách hàng yêu cầu điều chỉnh giải pháp: "
									+ solutionDto.getContentResultSolution();
						}
						solutionDto.setRealitySolutionDate(new Date());
					}

					solutionDto.setPerformerId(user.getVpsUserInfo().getSysUserId());
					Long solutionId = resultSolutionGPTHDAO.saveObject(solutionDto.toModel());
					if (solutionDto.getFileResultSolution() != null) {
						utilAttachDocumentDAO.deleteExtend(solutionId, "GP");
						List<UtilAttachDocumentDTO> lstAttachGiaiPhap = solutionDto.getFileResultSolution();
						for (UtilAttachDocumentDTO attachGiaiPhap : lstAttachGiaiPhap) {
							attachGiaiPhap.setStatus("1");
							attachGiaiPhap.setObjectId(solutionId);
							attachGiaiPhap.setType("GP");
							attachGiaiPhap.setDescription("File trình bày giải pháp với khách hàng");
							utilAttachDocumentDAO.saveObject(attachGiaiPhap.toModel());
						}
					}
				} else {
					ResultSolutionGPTHDTO maxResult = dto.get(0);
					maxResult.setPresentSolutionDate(solutionDto.getPresentSolutionDate());
					maxResult.setResultSolutionType(solutionDto.getResultSolutionType());
					maxResult.setUpdatedDate(new Date());
					maxResult.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
					maxResult.setContractId(solutionDto.getContractId());
					maxResult.setContractCode(solutionDto.getContractCode());
					maxResult.setSignDate(solutionDto.getSignDate());
					maxResult.setGuaranteeTime(solutionDto.getGuaranteeTime());
					maxResult.setConstructTime(solutionDto.getConstructTime());
					maxResult.setContractRose(solutionDto.getContractRose());
					maxResult.setContentResultSolution(solutionDto.getContentResultSolution());
					// maxResult.setPresentSolutionDateNext(solutionDto.getPresentSolutionDateNext());
					maxResult.setContractPrice(solutionDto.getContractPrice());

					maxResult.setSignStatus(solutionDto.getSignStatus());
					maxResult.setUnsuccessfullReason(solutionDto.getUnsuccessfullReason());

					// List<String> lsType = new ArrayList<>();
					// lsType.add("GP");
					// resultTangentDetailNoDAO.deleteFileAttachByObjectId(maxResult.getResultSolutionId(),
					// lsType);

					if (solutionDto.getFileResultSolution() != null) {
						utilAttachDocumentDAO.deleteExtend(maxResult.getResultSolutionGPTHId(), "GP");
						List<UtilAttachDocumentDTO> lstAttachGiaiPhap = solutionDto.getFileResultSolution();
						for (UtilAttachDocumentDTO attachGiaiPhap : lstAttachGiaiPhap) {
							attachGiaiPhap.setStatus("1");
							attachGiaiPhap.setObjectId(maxResult.getResultSolutionGPTHId());
							attachGiaiPhap.setType("GP");
							attachGiaiPhap.setDescription("File trình bày giải pháp với khách hàng");
							utilAttachDocumentDAO.saveObject(attachGiaiPhap.toModel());
						}
					}

					if (solutionDto.getResultSolutionType() != null) {
						if (solutionDto.getResultSolutionType().equals("1")) {
							if (solutionDto.getContractId() == null) {
								obj.setStatus("5");
								maxResult.setSignStatus(solutionDto.getSignStatus());

								if (null != maxResult.getSignStatus() && maxResult.getSignStatus() == 0) {
									maxResult.setUnsuccessfullReason(solutionDto.getUnsuccessfullReason());
									obj.setStatus("9");
									isFailSign = true;
									//Huypq-04042022-start call api call bot Từ chối kí hợp đồng và chờ phê duyệt
									List<InputSlotsObject> lstMapCallBot = new ArrayList<>();
									InputSlotsObject object = new InputSlotsObject();
									object.setKey(call_bot_key);
									object.setValue(obj.getCustomerName());
									lstMapCallBot.add(object);
									String urlCallbot = call_bot_url + "/api/partner/v1/campaign/1071/createCall";
									JSONObject callbotRequest = new JSONObject();
									callbotRequest.put("api_key", call_bot_api_key);
									callbotRequest.put("customer_phone", obj.getCustomerPhone());
									callbotRequest.put("input_slots", lstMapCallBot);
//									Boolean connectStatus = CallApiUtils.connectionCallbot(urlCallbot, callbotRequest);
									JSONObject requestCallBot = new JSONObject();
									requestCallBot.put("urlCallbot", urlCallbot);
									requestCallBot.put("callbotRequest", callbotRequest);
									CallApiUtils.callApiResponseString(url_aio + "service/aioCallBotWsService/createCallBot", requestCallBot);
									//Huy-end
								}
//								personJsonObject.put("surveyStatus", obj.getStatus());
								content = "Công việc của khách hàng " + obj.getCustomerName() + " - " + obj.getCustomerPhone() + " đang chờ lên hợp đồng";
								contentTTHT = "Công việc của khách hàng " + obj.getCustomerName() + " - " + obj.getCustomerPhone() + " đang chờ lên hợp đồng";
							} else {
								obj.setStatus("8");
//								personJsonObject.put("surveyStatus", "8");
								content = "Khách hàng " + obj.getCustomerName()
										+ " đã được trình bày giải pháp, kết quả khách hàng đồng ý ký hợp đồng số: "
										+ solutionDto.getContractCode();
								contentTTHT = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
										+ obj.getProvinceCode()
										+ " trình bày giải pháp, kết quả khách hàng đồng ý ký hợp đồng số: "
										+ solutionDto.getContractCode();
							}
						}

						if (solutionDto.getResultSolutionType().equals("2")) {
							obj.setStatus("7");
							// obj.setStatus("11");
//							personJsonObject.put("surveyStatus", "7");
							// personJsonObject.put("surveyStatus", "11");
							content = "Khách hàng " + obj.getCustomerName()
									+ " đã được trình bày giải pháp, kết quả khách hàng từ chối do: "
									+ solutionDto.getContentResultSolution();
							contentTTHT = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
									+ obj.getProvinceCode() + " trình bày giải pháp, kết quả khách hàng từ chối do: "
									+ solutionDto.getContentResultSolution();
							//Huypq-04042022-start call api call bot Từ chối giải pháp và phê duyệt
							List<InputSlotsObject> lstMapCallBot = new ArrayList<>();
							InputSlotsObject object = new InputSlotsObject();
							object.setKey(call_bot_key);
							object.setValue(obj.getCustomerName());
							lstMapCallBot.add(object);
							String urlCallbot = call_bot_url + "/api/partner/v1/campaign/1045/createCall";
							JSONObject callbotRequest = new JSONObject();
							callbotRequest.put("api_key", call_bot_api_key);
							callbotRequest.put("customer_phone", obj.getCustomerPhone());
							callbotRequest.put("input_slots", lstMapCallBot);
//							Boolean connectStatus = CallApiUtils.connectionCallbot(urlCallbot, callbotRequest);
							JSONObject requestCallBot = new JSONObject();
							requestCallBot.put("urlCallbot", urlCallbot);
							requestCallBot.put("callbotRequest", callbotRequest);
							CallApiUtils.callApiResponseString(url_aio + "service/aioCallBotWsService/createCallBot", requestCallBot);
							//Huy-end
						}

						if (solutionDto.getResultSolutionType().equals("3")) {
							obj.setStatus("6");
//							personJsonObject.put("surveyStatus", "6");

							maxResult.setApprovedPerformerId(solutionDto.getApprovedPerformerId());
							maxResult.setApprovedDescription(solutionDto.getApprovedDescription());

							solutionDto.setCreatedDate(new Date());
							solutionDto.setCreatedUser(user.getVpsUserInfo().getSysUserId());
							solutionDto.setResultSolutionOrder((long) dto.size() + 1l);
							solutionDto.setPresentSolutionDate(solutionDto.getPresentSolutionDateNext());
							solutionDto.setPresentSolutionDateNext(null);
							solutionDto.setTangentCustomerGPTHId(obj.getTangentCustomerGPTHId());
							solutionDto.setPerformerId(obj.getPerformerId());
							solutionDto.setResultSolutionType(null);
							Long solutionId = resultSolutionGPTHDAO.saveObject(solutionDto.toModel());
							content = "Khách hàng " + obj.getCustomerName()
									+ " đã được trình bày giải pháp, kết quả khách hàng yêu cầu điều chỉnh giải pháp: "
									+ solutionDto.getContentResultSolution();
							contentTTHT = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
									+ obj.getProvinceCode()
									+ " trình bày giải pháp, kết quả khách hàng yêu cầu điều chỉnh giải pháp: "
									+ solutionDto.getContentResultSolution();
						}

						maxResult.setRealitySolutionDate(new Date());
					}
					maxResult.setPresentSolutionDate(maxResult.getPresentSolutionDate());

					SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					try {
						maxResult.setCreatedDate(formatDate.parse(maxResult.getCreatedDateDb()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}

					resultSolutionGPTHDAO.updateObject(maxResult.toModel());
					if (isFailSign == true) {
						CatPartnerDTO origin = new CatPartnerDTO();
						origin.setStatus(0l);
						origin.setUpdatedDate(new Date());
						origin.setUpdatedUserId(user.getVpsUserInfo().getSysUserId());
						origin.setTangentCustomerGPTHId(obj.getTangentCustomerGPTHId());
						tangentCustomerGPTHDAO.updatePartnerTTHT(origin);
					}
				}

				if (!"".equals(content)) {
					for (TangentCustomerGPTHDTO userDto : lstManager) {
						tangentCustomerGPTHDAO.insertSendEmail(userDto.getEmail(), user.getVpsUserInfo().getSysUserId(),
								content);
						tangentCustomerGPTHDAO.insertSendSMS(userDto.getEmail(), userDto.getPhoneNumber(),
								user.getVpsUserInfo().getSysUserId(), content);
					}
				}

				if (!"".equals(contentTTHT)) {
					for (TangentCustomerGPTHDTO dtoTTHT : lstUserTTHT) {
						tangentCustomerGPTHDAO.insertSendEmail(dtoTTHT.getEmail(), user.getVpsUserInfo().getSysUserId(),
								contentTTHT);
						tangentCustomerGPTHDAO.insertSendSMS(dtoTTHT.getEmail(), dtoTTHT.getPhoneNumber(),
								user.getVpsUserInfo().getSysUserId(), contentTTHT);
					}
				}
			}
		}

		SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			obj.setCreatedDate(formatDate.parse(obj.getCreatedDateDb()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		obj.setUpdatedDate(new Date());
		obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
		id = tangentCustomerGPTHDAO.updateObject(obj.toModel());
//		String responseCallApiCrm = CallApiUtils.callApiCrm(linkCall, personJsonObject);
//		System.out.println(responseCallApiCrm);
		return id;
	}

	// Save lúc phê duyệt / từ chối kết quả giải pháp
	public Long saveApproveOrRejectGiaiPhap(ResultSolutionGPTHDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		// List<ResultSolutionGPTHDTO> lstSolution =
		// resultSolutionGPTHDAO.getResultSolutionByTangentCustomerId(obj.getTangentCustomerId());
		Long id = 0l;
		String contentStaff = "";
		String contentManage = "";

		ResultSolutionGPTHDTO result = resultSolutionGPTHDAO
				.getResultSolutionJoinSysUserByResultSolutionId(obj.getResultSolutionGPTHId());

		// huypq-24062021-start
//		String linkCall = url_crm_service + "/misv.php?com=api&elem=Survey&func=Update";
//		JSONObject personJsonObject = new JSONObject();
//		personJsonObject.put("opportunityId",
//				tangentCustomerGPTHDAO.getCrmIdByTangentCustomerId(obj.getTangentCustomerGPTHId()));
//		personJsonObject.put("reason", obj.getContentResultSolution());
		// Huy-end

		if (obj.getApprovedStatus() != null && obj.getApprovedStatus().equals("1")) {
			obj.setApprovedDate(new Date());
			obj.setApprovedUserId(user.getVpsUserInfo().getSysUserId());
			obj.setApprovedStatus(obj.getApprovedStatus());
			obj.setApprovedPerformerId(obj.getApprovedPerformerId());
			obj.setApprovedDescription(obj.getApprovedDescription());
			tangentCustomerGPTHDAO.updateStatusTangent(obj.getTangentCustomerGPTHId(), "9", null,
					user.getVpsUserInfo().getSysUserId());
			// tangentCustomerGPTHDAO.updateStatusTangent(obj.getTangentCustomerId(), "11",null,
			// user.getVpsUserInfo().getSysUserId());
			SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			try {
				obj.setCreatedDate(formatDate.parse(obj.getCreatedDateDb()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}

			obj.setUpdatedDate(new Date());
			obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());

			id = resultSolutionGPTHDAO.updateObject(obj.toModel());
			contentStaff = "TTHT đã phê duyệt đề nghị đóng kết quả trình bầy giải pháp với khách hàng "
					+ obj.getCustomerName();
			contentManage = "TTHT đã phê duyệt đề nghị đóng kết quả trình bầy giải pháp với khách hàng "
					+ obj.getCustomerName();

//			personJsonObject.put("surveyStatus", "9");
			// personJsonObject.put("surveyStatus", "11");
		} else if (obj.getApprovedStatus() != null && obj.getApprovedStatus().equals("2")) {
			obj.setApprovedDate(new Date());
			obj.setApprovedUserId(user.getVpsUserInfo().getSysUserId());
			obj.setApprovedStatus(obj.getApprovedStatus());
			obj.setApprovedPerformerId(obj.getApprovedPerformerId());
			obj.setApprovedDescription(obj.getApprovedDescription());
			tangentCustomerGPTHDAO.updateStatusTangent(obj.getTangentCustomerGPTHId(), "3", obj.getApprovedPerformerId(),
					user.getVpsUserInfo().getSysUserId());

			SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			try {
				obj.setCreatedDate(formatDate.parse(obj.getCreatedDateDb()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}

			obj.setUpdatedDate(new Date());
			obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());

			id = resultSolutionGPTHDAO.updateObject(obj.toModel());

			// Lưu ResultTangent
			ResultSolutionGPTHDTO ResultSolutionGPTHDTO = new ResultSolutionGPTHDTO();
			ResultSolutionGPTHDTO.setTangentCustomerGPTHId(obj.getTangentCustomerGPTHId());
			List<ResultSolutionBO> resultBo = resultSolutionGPTHDAO.getResultSolutionByTangentCustomerId(ResultSolutionGPTHDTO);
			if (resultBo.size() == 0) {
				ResultSolutionGPTHDTO.setResultSolutionOrder(1l);
			} else {
				ResultSolutionGPTHDTO.setResultSolutionOrder((long) resultBo.size() + 1l);
			}
			ResultSolutionGPTHDTO.setApprovedStatus("0");
			ResultSolutionGPTHDTO.setCreatedDate(new Date());
			ResultSolutionGPTHDTO.setCreatedUser(user.getVpsUserInfo().getSysUserId());
			// ResultSolutionGPTHDTO.setPerformerId(obj.getApprovedPerformerId());
			// ResultSolutionGPTHDTO.setPerformerName(obj.getApprovedPerformerName());
			id = resultSolutionGPTHDAO.saveObject(ResultSolutionGPTHDTO.toModel());

			// Gửi sms and email
			String contentGiao = "Đồng chí được giao trình bày giải pháp cho khách hàng " + obj.getCustomerName()
					+ " do nhân viên " + result.getPerformerName() + " không hoàn thành";
			// String contentTuChoi = "Yêu cầu trình bày giải pháp cho khách hàng " +
			// obj.getCustomerName() + " đã được chuyển cho đồng chí " +
			// obj.getApprovedPerformerName() + " do đồng chí trình bày giải pháp không
			// đạt";
			// bị từ chối
			// tangentCustomerGPTHDAO.insertSendSMS(result.getPerformerEmail(),
			// result.getPerformerPhone(), user.getVpsUserInfo().getSysUserId(),
			// contentTuChoi);
			// tangentCustomerGPTHDAO.insertSendEmail(result.getPerformerEmail(),
			// user.getVpsUserInfo().getSysUserId(), contentTuChoi);
			// được giao tiếp xúc
			tangentCustomerGPTHDAO.insertSendSMS(obj.getApprovedPerformerEmail(), obj.getApprovedPerformerPhone(),
					user.getVpsUserInfo().getSysUserId(), contentGiao);
			tangentCustomerGPTHDAO.insertSendEmail(obj.getApprovedPerformerEmail(), user.getVpsUserInfo().getSysUserId(),
					contentGiao);

			contentStaff = "TTHT đã từ chối đề nghị đóng yêu cầu trình bầy giải pháp khách hàng "
					+ obj.getCustomerName() + " của đồng chí";
			contentManage = "TTHT đã từ chối đề nghị đóng yêu cầu trình bầy giải pháp khách hàng "
					+ obj.getCustomerName() + " của nhân viên " + result.getPerformerName()
					+ ", Đề nghị Đ/c kiểm tra chỉ đạo triển khai theo nội dung bổ sung của TTHT";

//			personJsonObject.put("surveyStatus", "3");
		}
		// huypq-24062021-start
//		String responseCallApiCrm = CallApiUtils.callApiCrm(linkCall, personJsonObject);
//		System.out.println(responseCallApiCrm);
		// Huy-end

		tangentCustomerGPTHDAO.insertSendSMS(result.getPerformerEmail(), result.getPerformerPhone(),
				user.getVpsUserInfo().getSysUserId(), contentStaff);
		tangentCustomerGPTHDAO.insertSendEmail(result.getPerformerEmail(), user.getVpsUserInfo().getSysUserId(),
				contentStaff);

		List<TangentCustomerGPTHDTO> lstManager = tangentCustomerGPTHDAO.getListManager(obj.getProvinceId());
		for (TangentCustomerGPTHDTO userDto : lstManager) {
			tangentCustomerGPTHDAO.insertSendEmail(userDto.getEmail(), user.getVpsUserInfo().getSysUserId(), contentManage);
			tangentCustomerGPTHDAO.insertSendSMS(userDto.getEmail(), userDto.getPhoneNumber(),
					user.getVpsUserInfo().getSysUserId(), contentManage);
		}

		return id;
	}

	public List<ResultTangentGPTHDTO> getListResultTangentJoinSysUserByTangentCustomerId(ResultTangentGPTHDTO obj) {
		return resultTangentGPTHDAO.getListResultTangentJoinSysUserByTangentCustomerId(obj);
	}

	public List<ResultSolutionGPTHDTO> getResultSolutionJoinSysUserByTangentCustomerId(Long customerId) {
		return resultSolutionGPTHDAO.getResultSolutionJoinSysUserByTangentCustomerId(customerId);
	}

	public ResultTangentGPTHDTO getListResultTangentByResultTangentId(ResultTangentGPTHDTO obj) {
		ResultTangentGPTHDTO ls = resultTangentGPTHDAO.getListResultTangentJoinSysUserByResultTangentId(obj);
		if (ls != null) {
			// List<UtilAttachDocumentDTO> util =
			// resultTangentGPTHDAO.getFileAttachByResultTangentId(ls.getResultTangentId(),
			// "KQTX");
			ResultTangentDetailYesDTO yesDto = resultTangentDetailYesDAO
					.getResultTangentYesByResultTangentId(ls.getResultTangentGPTHId());
			TangentCustomerGPTHDTO tangent = new TangentCustomerGPTHDTO();
			tangent.setTangentCustomerGPTHId(obj.getTangentCustomerGPTHId());
			DetailTangentCustomerGPTHDTO detailCustomer = new DetailTangentCustomerGPTHDTO();
			if (obj.getPartnerType() != null) {
				tangent.setPartnerType(obj.getPartnerType());
				detailCustomer = detailTangentCustomerGPTHDAO.getDetailsTangentCustomer(tangent);
				yesDto.setDetailCustomerGPTH(detailCustomer);
			}
			List<UtilAttachDocumentDTO> lstImageDesign = resultTangentGPTHDAO
					.getFileAttachByResultTangentId(ls.getResultTangentGPTHId(), "YCTX_DESIGN_CONSTRUCTION");
			if (lstImageDesign != null && lstImageDesign.size() > 0)
				yesDto.setLstImageDesign(lstImageDesign);
			// ResultTangentDetailNoDTO noDto =
			// resultTangentDetailNoDAO.getResultTangentNoByResultTangentId(ls.getResultTangentId());
			// List<UtilAttachDocumentDTO> utilSoDo = new ArrayList<>();
			// List<UtilAttachDocumentDTO> utilDiaHinh = new ArrayList<>();
			// if(noDto!=null) {
			// utilSoDo =
			// resultTangentGPTHDAO.getFileAttachByResultTangentId(noDto.getResultTangentDetailNoId(),
			// "SODO");
			// utilDiaHinh =
			// resultTangentGPTHDAO.getFileAttachByResultTangentId(noDto.getResultTangentDetailNoId(),
			// "DHTD");
			// }
			ls.setResultTangentDetailYesDTO(yesDto);
			// ls.setResultTangentDetailNoDTO(noDto);
			// ls.setFileReceipts(util);
			// ls.setFileRedBook(utilSoDo);
			// ls.setFileTopographic(utilDiaHinh);
		}

		List<UtilAttachDocumentDTO> util = resultTangentGPTHDAO.getFileAttachByResultTangentId(ls.getResultTangentGPTHId(),
				"KQTX");
		ls.setFileReceipts(util);

		return ls;
	}

	public Long checkRoleUpdate(HttpServletRequest request) {
		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.UPDATE, Constant.AdResourceKey.TANGENT_CUSTOMER,
				request)) {
			return 0l;
		}
		return 1l;
	}

	public Double getContractRose() {
		return tangentCustomerGPTHDAO.getContractRose();
	}

	public Long approveRose(ResultSolutionGPTHDTO obj, HttpServletRequest request) throws Exception {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setUserApprovedId(user.getVpsUserInfo().getSysUserId());
		;
		return tangentCustomerGPTHDAO.approveRose(obj, request);
	}
	// Huy-end

	// Huypq-21052021-start
	public DataListDTO doSearchProvince(CatProvinceDTO obj) {
		List<CatProvinceDTO> ls = new ArrayList<CatProvinceDTO>();
		ls = tangentCustomerGPTHDAO.doSearchProvince(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	// Huy-end

	public Long checkRoleSourceYCTX(TangentCustomerGPTHDTO obj, HttpServletRequest request) {
		// TODO Auto-generated method stub
		// DUONGHV13-27122021-start
		Long IDRole = null;
		List<SysGroupDTO> listGroupTangent = tangentCustomerGPTHDAO.getListGroupTangent(Long.parseLong(GROUP_ID));
		SysUserDTO sysUser = tangentCustomerGPTHDAO.getTypeUserTangentForSource(obj);
		if (sysUser == null) {
			throw new BusinessException("Không tìm thấy user trong hệ thông/User không hợp lệ.");
		} else {
			obj.setSysUserId(sysUser.getSysUserId());
			if ("2".equals(sysUser.getTypeUser()) || "1".equals(sysUser.getTypeUser())) {
				IDRole = 2l;
			} else if (listGroupTangent.stream()
					.filter(t -> t.getSysGroupId().toString().equals(sysUser.getSysGroupId().toString())).findAny()
					.isPresent() == true) {
				IDRole = 3l;
			} else
				IDRole = 1l;
			return IDRole;
		}

		// DUONGHV13-27122021-end
	}

	public DataListDTO getAllContractXdddSuccessInternal(ResultSolutionGPTHDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		List<ResultSolutionGPTHDTO> ls = resultSolutionGPTHDAO.getAllContractXdddSuccessInternal(obj,
				user.getVpsUserInfo().getSysGroupId());
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setStart(1);
		data.setSize(obj.getPageSize());
		data.setTotal(obj.getTotalRecord());
		return data;
	}

	// Duonghv13-11022022-start
	public Boolean checkRoleCreated(HttpServletRequest request) {
		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATED, Constant.AdResourceKey.YCTX, request)) {
			return false;
		}
		return true;
	}

	public DataListDTO getChannel(AppParamDTO obj) {
		List<AppParamDTO> ls = new ArrayList<AppParamDTO>();
		ls = tangentCustomerGPTHDAO.getChannel(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	// Duonghv13-11022022-end

	public String exportFile(DataListDTO data) throws Exception {
		// TODO Auto-generated method stub
		String folderParam = defaultSubFolderUpload;
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();

		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Baocaoyeucautiepxuc_GPTH.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		file.close();

		String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload);

		String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload);
		File udir = new File(uploadPath);
		if (!udir.exists()) {
			udir.mkdirs();
		}
		OutputStream outFile = new FileOutputStream(
				udir.getAbsolutePath() + File.separator + "Baocaoyeucautiepxuc_GPTH.xlsx");
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFCellStyle stt = ExcelUtils.styleText(sheet);
		stt.setAlignment(HorizontalAlignment.CENTER);
		XSSFCellStyle styleTitle = ExcelUtils.styleBold(sheet);

		XSSFFont fontStyle = workbook.createFont();
		fontStyle.setFontHeightInPoints((short) 10);
		fontStyle.setFontName("Arial");
		fontStyle.setBold(false);
		fontStyle.setItalic(false);

		styleTitle.setAlignment(HorizontalAlignment.CENTER);
		styleTitle.setFont(fontStyle);
		styleTitle.setWrapText(false);

		if (data.getData() != null && !data.getData().isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			style.setAlignment(HorizontalAlignment.CENTER);
			AtomicInteger ordinal = new AtomicInteger(2);
			style.setFont(fontStyle);
			AtomicInteger no = new AtomicInteger(1);
			List<TangentCustomerGPTHDTO> lstTangent = data.getData();
			for (TangentCustomerGPTHDTO obj : lstTangent) {
				Row row = sheet.createRow(ordinal.getAndIncrement());
				Cell cell = row.createCell(0, CellType.NUMERIC);
				cell.setCellValue(no.getAndIncrement());
				cell.setCellStyle(style);
				cell = row.createCell(1, CellType.STRING);
				if(obj.getPartnerType() == null)	cell.setCellValue(obj.getProvinceKeyCode() + " - "+ obj.getCustomerPhone());
				else if(obj.getPartnerType() == 1)	cell.setCellValue("KHCN - " + obj.getProvinceKeyCode() + " - "+ obj.getCustomerPhone());
				else cell.setCellValue("KHDN - " + obj.getProvinceKeyCode() + " - "+ obj.getCustomerPhone());
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getProvinceCode()) ? obj.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getProvinceKeyCode()) ? obj.getProvinceKeyCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getDistrictName()) ? obj.getDistrictName() : "");
				cell.setCellStyle(style);

				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getCustomerName()) ? obj.getCustomerName() : "");
				cell.setCellStyle(style);

				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getBciCode()) ? obj.getBciCode() : "");
				cell.setCellStyle(style);

				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getCustomerPhone()) ? obj.getCustomerPhone() : "");
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getAddress()) ? obj.getAddress() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getListsources().get(obj.getSource())) ? obj.getListsources().get(obj.getSource()) : "");
				cell.setCellStyle(style);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getReceptionChannel()) ? obj.getReceptionChannel() : "");
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getCustomerResources()) ? obj.getCustomerResources() : "");
				cell.setCellStyle(style);

				cell = row.createCell(12, CellType.STRING);
				String[] split = obj.getCreatedUserName().split("-", 2);
				cell.setCellValue(StringUtils.isNotEmpty(split[0]) ? split[0] : "");
				cell.setCellStyle(style);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(split[1]) ? split[1] : "");
				cell.setCellStyle(style);

				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue(dateFormat.format(obj.getCreatedDate()));
				cell.setCellStyle(style);

				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue(dateFormat.format(obj.getSuggestTime()));
				cell.setCellStyle(style);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getPerformerName()) ? obj.getPerformerName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getPerformerCode()) ? obj.getPerformerCode() : "");
				cell.setCellStyle(style);

				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(Constant.TANGENT_STATUS.get(obj.getStatus()))
						? Constant.TANGENT_STATUS.get(obj.getStatus())
						: "");
				cell.setCellStyle(style);
				if (obj.getStatus().equals("2") == true || obj.getStatus().equals("4") == true) {
					cell = row.createCell(19, CellType.STRING);
					if(obj.getResultTangentGPTHDTO()!=null) {
						if (null == obj.getResultTangentGPTHDTO().getCreatedDateDb())
							cell.setCellValue("");
						else	cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentGPTHDTO().getCreatedDateDb())
									? obj.getResultTangentGPTHDTO().getCreatedDateDb()
									: "");
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(style);
					
					cell = row.createCell(20, CellType.STRING);
					cell.setCellValue("");
					cell.setCellStyle(style);
					
					cell = row.createCell(21, CellType.STRING);
					cell.setCellValue("");
					cell.setCellStyle(style);
					
					cell = row.createCell(22, CellType.STRING);
					cell.setCellValue("");
					cell.setCellStyle(style);
					
				} else if (obj.getStatus().equals("3") == true || obj.getStatus().equals("5") == true
						|| obj.getStatus().equals("6") == true || obj.getStatus().equals("7") == true) {
					cell = row.createCell(19, CellType.STRING);
					if(obj.getResultTangentGPTHDTO()!=null) {
						cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentGPTHDTO().getCreatedDateDb())
								? obj.getResultTangentGPTHDTO().getCreatedDateDb()
								: "");
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(style);
					cell = row.createCell(20, CellType.STRING);
					if(obj.getResultTangentGPTHDTO()!=null) {
						cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentGPTHDTO().getCreatedDateYes())
								? obj.getResultTangentGPTHDTO().getCreatedDateYes()
								: "");
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(style);
					if (obj.getStatus().equals("5") == true) {
						if (null != obj.getResultSolutionGPTHDTO()) {
							cell = row.createCell(21, CellType.STRING);
							cell.setCellValue(StringUtils.isNotEmpty(obj.getResultSolutionGPTHDTO().getUpdatedDateDb())
									? obj.getResultSolutionGPTHDTO().getUpdatedDateDb()
									: "");
							cell.setCellStyle(style);
						}else {
							cell = row.createCell(21, CellType.STRING);
							cell.setCellValue("");
							cell.setCellStyle(style);
						}
					}else {
						cell = row.createCell(21, CellType.STRING);
						cell.setCellValue("");
						cell.setCellStyle(style);
					}
					cell = row.createCell(22, CellType.STRING);
					cell.setCellValue("");
					cell.setCellStyle(style);

				} else if (obj.getStatus().equals("9") == true) {
					cell = row.createCell(19, CellType.STRING);
					if(obj.getResultTangentGPTHDTO()!=null) {
						cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentGPTHDTO().getCreatedDateDb())
								? obj.getResultTangentGPTHDTO().getCreatedDateDb()
								: "");
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(style);
					if (null == obj.getResultSolutionGPTHDTO()) {
						cell = row.createCell(20, CellType.STRING);
						if(obj.getResultTangentGPTHDTO()!=null) {
							cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentGPTHDTO().getUpdatedDateDb())
									? obj.getUpdatedDateDb()
									: "");
						} else {
							cell.setCellValue("");
						}
						cell.setCellStyle(style);
						cell = row.createCell(21, CellType.STRING);
						cell.setCellValue("");
						cell.setCellStyle(style);
						cell = row.createCell(22, CellType.STRING);
						cell.setCellValue("");
						cell.setCellStyle(style);
					} else {
						cell = row.createCell(20, CellType.STRING);
						if(obj.getResultTangentGPTHDTO()!=null) {
							cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentGPTHDTO().getCreatedDateYes())
									? obj.getResultTangentGPTHDTO().getCreatedDateYes()
									: "");
						} else {
							cell.setCellValue("");
						}
						cell.setCellStyle(style);
						cell = row.createCell(21, CellType.STRING);
						cell.setCellValue(StringUtils.isNotEmpty(obj.getResultSolutionGPTHDTO().getUpdatedDateDb())
								? obj.getResultSolutionGPTHDTO().getUpdatedDateDb()
								: "");
						cell.setCellStyle(style);
						cell = row.createCell(22, CellType.STRING);
						cell.setCellValue("");
						cell.setCellStyle(style);
					}

				} else if (obj.getStatus().equals("8") == true) {
					cell = row.createCell(19, CellType.STRING);
					if(obj.getResultTangentGPTHDTO()!=null) {
						cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentGPTHDTO().getCreatedDateDb())
								? obj.getResultTangentGPTHDTO().getCreatedDateDb()
								: "");
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(style);
					cell = row.createCell(20, CellType.STRING);
					if(obj.getResultTangentGPTHDTO()!=null) {
						cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentGPTHDTO().getCreatedDateYes())
								? obj.getResultTangentGPTHDTO().getCreatedDateYes()
								: "");
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(style);
					cell = row.createCell(21, CellType.STRING);
					cell.setCellValue(StringUtils.isNotEmpty(obj.getResultSolutionGPTHDTO().getUpdatedDateDb())
							? obj.getResultSolutionGPTHDTO().getUpdatedDateDb()
							: "");
					cell.setCellStyle(style);
					cell = row.createCell(22, CellType.STRING);
					cell.setCellValue(StringUtils.isNotEmpty(obj.getResultSolutionGPTHDTO().getUpdatedDateDb())
							? obj.getResultSolutionGPTHDTO().getUpdatedDateDb()
							: "");
					cell.setCellStyle(style);

				}else {
					cell = row.createCell(19, CellType.STRING);
					cell.setCellValue("");
					cell.setCellStyle(style);
					cell = row.createCell(20, CellType.STRING);
					cell.setCellValue("");
					cell.setCellStyle(style);
					cell = row.createCell(21, CellType.STRING);
					cell.setCellValue("");
					cell.setCellStyle(style);
					cell = row.createCell(22, CellType.STRING);
					cell.setCellValue("");
					cell.setCellStyle(style);
				}
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getContentCustomer())
						? obj.getContentCustomer()
						: "");
				cell.setCellStyle(style);
			}

		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Baocaoyeucautiepxuc_GPTH.xlsx");
		return path;
	}

	
	//Huypq-24062021-start
	public ConfigStaffTangentDTO getUserConfigTagentByProvince(ConfigStaffTangentDTO obj) {
		return tangentCustomerGPTHDAO.getUserConfigTagentByProvince(obj.getCatProvinceId());
	}
	
	public Boolean checkRoleUserAssignYctx(HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		return tangentCustomerGPTHDAO.checkRoleUserAssignYctx(user.getSysUserId());
	}
	//Huy-end

	// 20210425 aeg start
	public List<TangentCustomerNoticeDTO> findCallBotConversation (Long tangentCustomerId) {
		List<TangentCustomerNoticeDTO> tangentCustomerNoticeBOS = tangentCustomerNoticeDAO.findByTangentCustomerId(tangentCustomerId);
		tangentCustomerNoticeBOS.forEach(tangentCustomerNoticeBO -> {
			tangentCustomerNoticeBO.setContent(tangentCustomerCallbotContentDAO.findByTangentCustomerNoticeId(tangentCustomerNoticeBO.getId()));
		});
		return tangentCustomerNoticeBOS;
	}
	// 20210425 aeg end
}

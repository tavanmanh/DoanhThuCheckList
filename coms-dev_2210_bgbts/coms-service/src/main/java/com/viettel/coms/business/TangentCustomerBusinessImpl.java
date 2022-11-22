package com.viettel.coms.business;

import com.viettel.coms.bo.TangentCustomerCallbotContentBO;
import com.viettel.coms.bo.TangentCustomerNoticeBO;
import com.viettel.coms.dao.TangentCustomerCallbotContentDAO;
import com.viettel.coms.dao.TangentCustomerNoticeDAO;
import com.viettel.coms.dto.TangentCustomerNoticeDTO;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
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

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
import com.viettel.cat.dto.CatPartnerDTO;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.cat.dto.ConstructionImageInfo;
import com.viettel.coms.bo.ResultSolutionBO;
import com.viettel.coms.bo.ResultTangentBO;
import com.viettel.coms.bo.TangentCustomerBO;
import com.viettel.coms.dao.DetailTangentCustomerDAO;
import com.viettel.coms.dao.ResultSolutionDAO;
import com.viettel.coms.dao.ResultTangentDAO;
import com.viettel.coms.dao.ResultTangentDetailNoDAO;
import com.viettel.coms.dao.ResultTangentDetailYesDAO;
import com.viettel.coms.dao.TangentCustomerDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.CallBotDTO;
import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.coms.dto.DetailTangentCustomerDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.ResultSolutionDTO;
import com.viettel.coms.dto.ResultTangentDTO;
import com.viettel.coms.dto.ResultTangentDetailNoDTO;
import com.viettel.coms.dto.ResultTangentDetailYesDTO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.coms.dto.SysUserRequest;
import com.viettel.coms.dto.TangentCustomerDTO;
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

@Service("tangentCustomerBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TangentCustomerBusinessImpl
		extends BaseFWBusinessImpl<TangentCustomerDAO, TangentCustomerDTO, TangentCustomerBO>
		implements TangentCustomerBusiness {

	@Autowired
	private TangentCustomerDAO tangentCustomerDAO;

	@Autowired
	private ResultTangentDAO resultTangentDAO;

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
	@Value("${url_cim_service}")
	private String url_cim_service;
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
	private ResultSolutionDAO resultSolutionDAO;

	@Autowired
	private DetailTangentCustomerDAO detailTangentCustomerDAO;

	private final String GROUP_ID = "9008143";

	public DataListDTO doSearch(TangentCustomerDTO obj, HttpServletRequest request) throws ParseException {
		List<TangentCustomerDTO> ls = new ArrayList<>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> provinceIdLst = ConvertData.convertStringToList(groupId, ",");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		if (provinceIdLst != null && provinceIdLst.size() > 0) {
			ls = tangentCustomerDAO.doSearch(obj, provinceIdLst);
			for (TangentCustomerDTO tangentCustomer : ls) {
				ResultTangentDTO tangent = new ResultTangentDTO();
				tangent.setTangentCustomerId(tangentCustomer.getTangentCustomerId());
				if ("1".equals(tangentCustomer.getStatus()) == false) {
					tangent = resultTangentDAO.getMaxAppointmentDateByTangentCustomerId(tangent);

				}
				if(tangent != null) {
				if ("1".equals(tangentCustomer.getStatus()) == true || "2".equals(tangentCustomer.getStatus()) == true
						|| "3".equals(tangentCustomer.getStatus()) == true
						|| "4".equals(tangentCustomer.getStatus()) == true
						|| "8".equals(tangentCustomer.getStatus()) == true) {

					if ("1".equals(tangentCustomer.getStatus()) == true) {
						tangentCustomer.setNextTangentTime(tangentCustomer.getSuggestTime());
						// Date createdDate = sdf.parse(tangentCustomer.getCreatedDateDb());
						// String sysDateStr = sdf.format(calendar.getTime());Date sysDate =
						// sdf.parse(sysDateStr);
						// Date diff = new Date(sysDate.getTime() - createdDate.getTime());
						// calendar.setTime(diff);
						// int hours = calendar.get(Calendar.HOUR_OF_DAY);
						// if(hours < 20) tangentCustomer.setOverdueStatus(1L);
						// else if(20 <= hours && hours < 24) tangentCustomer.setOverdueStatus(2L);
						// else tangentCustomer.setOverdueStatus(3L);
					} else if ("2".equals(tangentCustomer.getStatus()) == true) {
						tangentCustomer.setResultTangentDTO(tangent);
						tangentCustomer.setNextTangentTime(tangent.getAppointmentDate());
						// Date appointmentDate = sdf.parse(resultTangent.getAppointmentDateDb());
						// String sysDateStr = sdf.format(calendar.getTime());Date sysDate =
						// sdf.parse(sysDateStr);
						// Date diff = new Date(appointmentDate.getTime()- sysDate.getTime());
						// calendar.setTime(diff);
						// int hours = calendar.get(Calendar.HOUR_OF_DAY);
						// if(hours > 24) tangentCustomer.setOverdueStatus(1L);
						// else if(10 <= hours && hours < 12) tangentCustomer.setOverdueStatus(2L);
						// else if(hours < 0)tangentCustomer.setOverdueStatus(3L);
					} else if ("4".equals(tangentCustomer.getStatus()) == true) {
						tangentCustomer.setResultTangentDTO(tangent);

					} else {
						tangentCustomer.setResultTangentDTO(tangent);
						ResultSolutionDTO resultSolution = resultSolutionDAO
								.getResultSolutionFieldByTargentCustomerId(tangentCustomer.getTangentCustomerId());
						tangentCustomer.setResultSolutionDTO(resultSolution);
						if (resultSolution != null) {
							tangentCustomer.setResultSolutionDTO(resultSolution);

							if ("3".equals(tangentCustomer.getStatus()) == true) {
								if (resultSolution.getPresentSolutionDate() != null)
									tangentCustomer.setNextTangentTime(resultSolution.getPresentSolutionDate());
							}

						} else {
							tangentCustomer.setNextTangentTime(null);
							tangentCustomer.setResultSolutionDTO(null);
						}
						// if(resultSolution != null) {
						// tangentCustomer.setSuggestTime(resultSolution.getSignDate());
						// }else tangentCustomer.setSuggestTime(null);
						// String createdReturn = resultSolution.getCreatedDateDb();
						// Date createdDate = sdf.parse(createdReturn);
						// String sysDateStr = sdf.format(calendar.getTime());Date sysDate =
						// sdf.parse(sysDateStr);
						// Date diff = new Date(sysDate.getTime() - createdDate.getTime());
						// calendar.setTime(diff);
						// int hours = calendar.get(Calendar.HOUR_OF_DAY);
						// if(hours < 96) tangentCustomer.setOverdueStatus(1L);
						// else if(96 <= hours && hours < 120) tangentCustomer.setOverdueStatus(2L);
						// else tangentCustomer.setOverdueStatus(3L);
					}
				} else if ("5".equals(tangentCustomer.getStatus()) == true
						|| "6".equals(tangentCustomer.getStatus()) == true
						|| "7".equals(tangentCustomer.getStatus()) == true
						|| "9".equals(tangentCustomer.getStatus()) == true) {
					ResultSolutionDTO resultSolution = resultSolutionDAO
							.getResultSolutionFieldByTargentCustomerId(tangentCustomer.getTangentCustomerId());
					if ("5".equals(tangentCustomer.getStatus()) == true) {
						tangentCustomer.setResultTangentDTO(tangent);
						if (resultSolution != null) {
							tangentCustomer.setResultSolutionDTO(resultSolution);
							if (resultSolution.getSignDate() != null)
								tangentCustomer.setNextTangentTime(resultSolution.getSignDate());
						} else
							tangentCustomer.setNextTangentTime(null);
					} else if ("6".equals(tangentCustomer.getStatus()) == true) {
						tangentCustomer.setResultTangentDTO(tangent);
						if (resultSolution != null) {
							tangentCustomer.setResultSolutionDTO(resultSolution);
							if (resultSolution.getPresentSolutionDate() != null)
								tangentCustomer.setNextTangentTime(resultSolution.getPresentSolutionDate());
						} else
							tangentCustomer.setNextTangentTime(null);
						// Date createdDate = sdf.parse(resultSolution.getCreatedDateDb());
						// String sysDateStr = sdf.format(calendar.getTime());Date sysDate =
						// sdf.parse(sysDateStr);
						// Date diff = new Date(sysDate.getTime() - createdDate.getTime());
						// calendar.setTime(diff);
						// int hours = calendar.get(Calendar.HOUR_OF_DAY);
						// if(hours < 24) tangentCustomer.setOverdueStatus(1L);
						// else if(24 <= hours && hours < 48) tangentCustomer.setOverdueStatus(2L);
						// else tangentCustomer.setOverdueStatus(3L);
					} else {
						tangentCustomer.setResultTangentDTO(tangent);
						if (null != resultSolution) {
							tangentCustomer.setResultSolutionDTO(resultSolution);
						} else
							tangentCustomer.setResultSolutionDTO(null);
					}

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

	public Long save(TangentCustomerDTO obj, HttpServletRequest request) {
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
		List<TangentCustomerDTO> listCustomerValidate = tangentCustomerDAO
				.getDataTangentFollowPhoneNumber(obj.getCustomerPhone());
		if (listCustomerValidate.size() == 0) {
			if (obj.getPartnerType() == 1) {
				obj.setPartnerCode("KHCN" + "-" + obj.getProvinceCode() + "-" + obj.getCustomerPhone());
			} else
				obj.setPartnerCode("KHDN" + "-" + obj.getProvinceCode() + "-" + obj.getCustomerPhone());

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String content = "Đ/c được giao nhiệm vụ tiếp xúc khách hàng " + obj.getCustomerName() + " - "
					+ obj.getCustomerPhone() + ", đề nghị triển khai trước ngày "
					+ simpleDateFormat.format(obj.getSuggestTime());
			tangentCustomerDAO.insertSendSMS(obj.getPerformerEmail(), obj.getPerformerPhoneNumber(),
					user.getVpsUserInfo().getSysUserId(), content);
			tangentCustomerDAO.insertSendEmail(obj.getPerformerEmail(), user.getVpsUserInfo().getSysUserId(), content);

			return tangentCustomerDAO.saveObject(obj.toModel());
		} else {
			throw new BusinessException("SĐT đã thuộc về 1 khách hàng/đối tác khác.");
		}

	}

	public Long update(TangentCustomerDTO obj, HttpServletRequest request) throws Exception {
		try {
			KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
			// Boolean checkRoleUpdate =
			// VpsPermissionChecker.hasPermission(Constant.OperationKey.UPDATE,
			// Constant.AdResourceKey.TANGENT_CUSTOMER, request);
			// if(!checkRoleUpdate) {
			// throw new BusinessException("Bạn không có quyền cập nhật bản ghi !");
			// }
			boolean isUpdatePartnerTTHT = false;
			CatPartnerDTO origin = new CatPartnerDTO();
			if (obj.getOldPhone().equals(obj.getCustomerPhone()) == false) {
				List<TangentCustomerDTO> listCustomerValidate = tangentCustomerDAO
						.getDataTangentFollowPhoneNumber(obj.getCustomerPhone());
				if (listCustomerValidate.size() > 0) {
					throw new BusinessException("SĐT đã thuộc về 1 khách hàng khác.");
				} else {
					if (null != obj.getPartnerType()) {
						TangentCustomerDTO search = new TangentCustomerDTO();search.setTangentCustomerId(obj.getTangentCustomerId());
						origin = (CatPartnerDTO) tangentCustomerDAO.getPartnerTTHT(search);
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
				}
			}

			obj.setUpdatedDate(new Date());
			if(user != null && user.getVpsUserInfo()!= null) {
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
			Long tangentCustomerId = tangentCustomerDAO.updateObject(obj.toModel());
			if (tangentCustomerId == 0l) {
				throw new BusinessException("Lỗi xảy ra khi cập nhật thông tin khách hàng và yêu cầu tiếp xúc.");
			} else {
				if (isUpdatePartnerTTHT == false)
					return tangentCustomerId;
				else
					return tangentCustomerDAO.updatePartnerTTHT(origin);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public DataListDTO doSearchDistrictByProvinceCode(TangentCustomerDTO obj) {
		List<TangentCustomerDTO> ls = new ArrayList<TangentCustomerDTO>();
		ls = tangentCustomerDAO.doSearchDistrictByProvinceCode(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	public DataListDTO doSearchCommunneByDistrict(TangentCustomerDTO obj) {
		List<TangentCustomerDTO> ls = new ArrayList<TangentCustomerDTO>();
		ls = tangentCustomerDAO.doSearchCommunneByDistrict(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	public ConfigStaffTangentDTO getUserConfigTagent(ConfigStaffTangentDTO obj) {
		return tangentCustomerDAO.getUserConfigTagent(obj.getCatProvinceId(), obj.getType());
	}

	public Long deleteRecord(TangentCustomerDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		return tangentCustomerDAO.deleteRecord(obj.getTangentCustomerId(), user.getVpsUserInfo().getSysUserId());
	}

	public void saveDetailAppoinmentDate(TangentCustomerDTO obj, ResultTangentDTO resultTangentDTO,
			KttsUserSession user) {
		// update tangent customer
		obj.setStatus("2");
		obj.setUpdatedDate(new Date());
		obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
		// Lưu ResultTangent
		List<ResultTangentBO> resultBo = resultTangentDAO.getResultTangentByTangentCustomerId(resultTangentDTO);
		if (resultBo.size() == 0) {
			resultTangentDTO.setOrderResultTangent("1");
		} else {
			resultTangentDTO.setOrderResultTangent(String.valueOf(resultBo.size() + 1));
		}
		if (resultTangentDTO.getResultTangentType() != null) {
			resultTangentDTO.setRealityTangentDate(new Date());
		}
		// resultTangentDTO.setApprovedStatus("0");
		resultTangentDTO.setCreatedDate(new Date());
		resultTangentDTO.setCreatedUser(user.getVpsUserInfo().getSysUserId());
		resultTangentDTO.setPerformerId(user.getVpsUserInfo().getSysUserId());
		Long resultTangentId = resultTangentDAO.saveObject(resultTangentDTO.toModel());

		// gui toi nhan vien thuc hien
		String content = "Yêu cầu tiếp xúc khách hàng " + obj.getCustomerName() + " của Đ/c đã được nhân viên: "
				+ obj.getPerformerCode() + "-" + obj.getPerformerName() + " xác nhận thời gian triển khai tiếp xúc ";
		tangentCustomerDAO.insertSendEmail(obj.getCreatedEmail(), user.getVpsUserInfo().getSysUserId(), content);
		tangentCustomerDAO.insertSendSMS(obj.getCreatedEmail(), obj.getCreatedPhoneNumber(),
				user.getVpsUserInfo().getSysUserId(), content);
		// gui toi quan ly
		List<TangentCustomerDTO> lstManager = tangentCustomerDAO.getListManager(obj.getProvinceId());
		if (lstManager.size() > 0) {
			for (TangentCustomerDTO dto : lstManager) {
				content = "Yêu cầu tiếp xúc khách hàng " + obj.getCustomerName()
						+ " của CNKT Tỉnh/TP đã được xác nhận thời gian triển khai ";
				tangentCustomerDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(), content);
				tangentCustomerDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
						user.getVpsUserInfo().getSysUserId(), content);
			}
		}

		// Có nhu cầu
		if (resultTangentDTO.getResultTangentType() != null && resultTangentDTO.getResultTangentType().equals("1")) {
			if (obj.getStatus().equals("2") == true && obj.getResultSolutionDTO() != null) {
				Long solutionId = saveResultSolution(obj, request);
				tangentCustomerDAO.getSession().flush();
				tangentCustomerDAO.getSession().clear();
				System.out.println("" + solutionId);
			}
			obj.setStatus("3");
			obj.setUpdatedDate(new Date());
			obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
			// gui ket qua tiep xuc toi quan ly
			// List<TangentCustomerDTO> lstManager =
			// tangentCustomerDAO.getListManager(obj.getProvinceId());
			if (obj.getOldStatus().equals(obj.getStatus()) == false) {
				if (lstManager.size() > 0) {
					for (TangentCustomerDTO dto : lstManager) {
						content = "Khách hàng " + obj.getCustomerName() + " đã được nhân viên " + obj.getPerformerCode()
								+ "-" + obj.getPerformerName()
								+ " tiếp xúc thành công, kết quả tiếp xúc: Khách hàng có nhu cầu";
						tangentCustomerDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(),
								content);
						tangentCustomerDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
								user.getVpsUserInfo().getSysUserId(), content);
					}
				}
				// gui ket qua tiep xuc toi TTHT
				List<TangentCustomerDTO> lstUserTTHT = tangentCustomerDAO.getListUserTTHT();
				if (lstUserTTHT.size() > 0) {
					for (TangentCustomerDTO dto : lstUserTTHT) {
						content = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
								+ obj.getProvinceCode()
								+ " tiếp xúc thành công, kết quả tiếp xúc: Khách hàng có nhu cầu";
						tangentCustomerDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(),
								content);
						tangentCustomerDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
								user.getVpsUserInfo().getSysUserId(), content);
					}
				}
			}

			// if (resultTangentDTO.getFileReceipts() != null) {
			//
			// List<UtilAttachDocumentDTO> lstAttach = resultTangentDTO.getFileReceipts();
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
			// if (resultTangentDTO.getContructionDesign()!=null &&
			// resultTangentDTO.getContructionDesign().equals("1")) {
			// Save Result Tangent Yes
			ResultTangentDetailYesDTO resultTangentYesDTO = obj.getResultTangentDetailYesDTO();
			// ResultTangentDetailYesDTO yesDb = null;
			// if(resultTangentDTO.getResultTangentId()!=null) {
			// resultTangentDetailYesDAO.getListResultTangentYesByTangentCustomerId(resultTangentDTO.getResultTangentId());
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
			// if (resultTangentDTO.getContructionDesign()!=null &&
			// resultTangentDTO.getContructionDesign().equals("0")) {
			// ResultTangentDetailNoDTO detailNo = obj.getResultTangentDetailNoDTO();
			// detailNo.setResultTangentId(resultTangentId);
			// Long noId = 0l;
			// detailNo.setCreatedDate(new Date());
			// detailNo.setCreatedUser(user.getVpsUserInfo().getSysUserId());
			// noId = resultTangentDetailNoDAO.saveObject(detailNo.toModel());
			//
			// if(resultTangentDTO.getFileRedBook()!=null) {
			// List<UtilAttachDocumentDTO> lstAttachSoDo =
			// resultTangentDTO.getFileRedBook();
			// for(UtilAttachDocumentDTO attachSoDo : lstAttachSoDo) {
			// attachSoDo.setStatus("1");
			// attachSoDo.setObjectId(noId);
			// attachSoDo.setType("SODO");
			// attachSoDo.setDescription("File hình ảnh sổ đỏ");
			// utilAttachDocumentDAO.saveObject(attachSoDo.toModel());
			// }
			// }
			//
			// if(resultTangentDTO.getFileTopographic()!=null) {
			// List<UtilAttachDocumentDTO> lstAttachDiaHinh =
			// resultTangentDTO.getFileTopographic();
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

	public void updateResultTangent(ResultTangentDTO resultTangentDTO, TangentCustomerDTO obj,
			HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		Transaction tx = null;
		Session session = null;
		boolean isDetailBegin = false;
		// Có nhu cầu
		try {
			if (resultTangentDTO.getResultTangentId() != null) {
				session = tangentCustomerDAO.getSessionFactory().openSession();
				tx = session.beginTransaction();
				String validateStatus = obj.getStatus();
				if (resultTangentDTO.getResultTangentType() != null
						&& resultTangentDTO.getResultTangentType().equals("1")) {
					obj.setStatus("3");
					obj.setUpdatedDate(new Date());
					obj.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
					// gui ket qua tiep xuc toi quan ly
					if (obj.getOldStatus().equals(obj.getStatus()) == false) {
						List<TangentCustomerDTO> lstManager = tangentCustomerDAO.getListManager(obj.getProvinceId());
						if (lstManager.size() > 0) {
							for (TangentCustomerDTO dto : lstManager) {
								String content = "Khách hàng " + obj.getCustomerName() + " đã được nhân viên "
										+ obj.getPerformerCode() + "-" + obj.getPerformerName()
										+ " tiếp xúc thành công, kết quả tiếp xúc: Khách hàng có nhu cầu";
								tangentCustomerDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(),
										content);
								tangentCustomerDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
										user.getVpsUserInfo().getSysUserId(), content);
							}
						}
						// gui ket qua tiep xuc toi TTHT
						List<TangentCustomerDTO> lstUserTTHT = tangentCustomerDAO.getListUserTTHT();
						if (lstUserTTHT.size() > 0) {
							for (TangentCustomerDTO dto : lstUserTTHT) {
								String content = "Khách hàng " + obj.getCustomerName() + " đã được CNKT Tỉnh/TP: "
										+ obj.getProvinceCode()
										+ " tiếp xúc thành công, kết quả tiếp xúc: Khách hàng có nhu cầu";
								tangentCustomerDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(),
										content);
								tangentCustomerDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
										user.getVpsUserInfo().getSysUserId(), content);
							}
						}
					}

					// List<String> fileType = new ArrayList<>();
					// fileType.add("KQTX");
					// resultTangentDetailNoDAO.deleteFileAttachByObjectId(resultTangentDTO.getResultTangentId(),
					// fileType);
					// if (resultTangentDTO.getFileReceipts() != null) {
					// if(resultTangentDTO.getContructionDesign()!=null &&
					// !resultTangentDTO.getContructionDesign().equals("0")) {
					// obj.setStatus("3");
					// }
					// List<UtilAttachDocumentDTO> lstAttach = resultTangentDTO.getFileReceipts();
					// for(UtilAttachDocumentDTO attach : lstAttach) {
					// attach.setStatus("1");
					// attach.setObjectId(resultTangentDTO.getResultTangentId());
					// attach.setType("KQTX");
					// attach.setCreatedDate(new Date());
					// attach.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
					// attach.setCreatedUserName(user.getVpsUserInfo().getFullName());
					// attach.setDescription("File đính kèm phiếu thu thập thông tin khách hàng");
					// utilAttachDocumentDAO.saveObject(attach.toModel());
					// }
					// }

					// Có thiết kế
					// if (resultTangentDTO.getContructionDesign() != null
					// && resultTangentDTO.getContructionDesign().equals("1")
					// && obj.getResultTangentDetailYesDTO() != null) {
					// Save Result Tangent Yes
					ResultTangentDetailYesDTO resultTangentYesDTO = obj.getResultTangentDetailYesDTO();
					ResultTangentDetailYesDTO yesDb = resultTangentDetailYesDAO
							.getListResultTangentYesByTangentCustomerId(resultTangentDTO.getResultTangentId());
					resultTangentYesDTO.setResultTangentId(resultTangentDTO.getResultTangentId());
					if (yesDb != null) {
						resultTangentYesDTO.setUpdatedDate(new Date());
						resultTangentYesDTO.setUpdatedUser(user.getVpsUserInfo().getSysUserId());

						SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						try {
							resultTangentYesDTO
									.setCreatedDate(formatDate.parse(resultTangentYesDTO.getCreatedDateDb()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}

						// try {
						// Date date = DateUtils.parseDate(resultTangentYesDTO.getCreatedDateDb(),
						// "dd/MM/yyyy HH:mm:ss");
						// resultTangentYesDTO.setCreatedDate(date);
						// } catch (ParseException e) {
						// // TODO Auto-generated catch block
						// //e.printStackTrace();
						// }

						Long resultTangentDetailYesId = resultTangentDetailYesDAO
								.updateObject(resultTangentYesDTO.toModel());
						if (resultTangentDetailYesId == 0l) {
							if (tx != null)
								tx.rollback();
						} else {
							if (resultTangentYesDTO.getLstImageDesign() != null) {
								utilAttachDocumentDAO.deleteExtend(resultTangentYesDTO.getResultTangentDetailYesId(),
										"YCTX_DESIGN_CONSTRUCTION");
								List<UtilAttachDocumentDTO> lstImageDesign = resultTangentYesDTO.getLstImageDesign();
								for (UtilAttachDocumentDTO imageDesign : lstImageDesign) {
									imageDesign.setObjectId(resultTangentYesDTO.getResultTangentDetailYesId());
									imageDesign.setType("YCTX_DESIGN_CONSTRUCTION");
									imageDesign.setDescription(
											"File mô tả phong cách thiết kế yêu cầu tiếp xúc khách hàng");
									utilAttachDocumentDAO.saveObject(imageDesign.toModel());
								}
							}
							resultTangentYesDTO.getDetailCustomer()
									.setTangentCustomerId(resultTangentDTO.getTangentCustomerId());
							isDetailBegin = true;
						}

					} else {
						resultTangentYesDTO.setCreatedDate(new Date());
						resultTangentYesDTO.setCreatedUser(user.getVpsUserInfo().getSysUserId());
						Long objectId = resultTangentDetailYesDAO.saveObject(resultTangentYesDTO.toModel());

						if (objectId == 0l) {
							if (tx != null)
								tx.rollback();
						} else {
							if (resultTangentYesDTO.getLstImageDesign() != null) {
								List<UtilAttachDocumentDTO> lstImageDesign = resultTangentYesDTO.getLstImageDesign();
								for (UtilAttachDocumentDTO imageDesign : lstImageDesign) {
									imageDesign.setObjectId(objectId);
									imageDesign.setType("YCTX_DESIGN_CONSTRUCTION");
									imageDesign.setDescription(
											"File mô tả phong cách thiết kế yêu cầu tiếp xúc khách hàng");
									utilAttachDocumentDAO.saveObject(imageDesign.toModel());
								}
							}
							resultTangentYesDTO.getDetailCustomer()
									.setTangentCustomerId(resultTangentDTO.getTangentCustomerId());
							isDetailBegin = true;

						}

					}
					if (isDetailBegin == true) {
						if (null == resultTangentYesDTO.getDetailCustomer().getDetailTangentCustomerId()) {
							Long detailTangentCustomerId = detailTangentCustomerDAO
									.saveObject(resultTangentYesDTO.getDetailCustomer().toModel());
							if (detailTangentCustomerId == 0l) {
								if (tx != null)
									tx.rollback();
							}
						} else {
							Long detailTangentCustomerId = detailTangentCustomerDAO
									.updateObject(resultTangentYesDTO.getDetailCustomer().toModel());
							if (detailTangentCustomerId == 0l) {
								if (tx != null)
									tx.rollback();
							}
						}

					}
					// end
					// }

					// Chưa có thiết kế
					// if (resultTangentDTO.getContructionDesign() != null
					// && resultTangentDTO.getContructionDesign().equals("0")
					// && (obj.getResultTangentDetailNoDTO() != null ||
					// resultTangentDTO.getFileRedBook()!=null
					// || resultTangentDTO.getFileTopographic() != null)) {
					// ResultTangentDetailNoDTO noDb = resultTangentDetailNoDAO
					// .getListResultTangentNoByTangentCustomerId(resultTangentDTO.getResultTangentId());
					//
					// Long noId = 0l;
					// ResultTangentDetailNoDTO detailNo = obj.getResultTangentDetailNoDTO();
					// if(detailNo==null) {
					// ResultTangentDetailNoDTO detailNoNew = new ResultTangentDetailNoDTO();
					// detailNoNew.setResultTangentId(resultTangentDTO.getResultTangentId());
					// detailNoNew.setCreatedDate(new Date());
					// detailNoNew.setCreatedUser(user.getVpsUserInfo().getSysUserId());
					// noId = resultTangentDetailNoDAO.saveObject(detailNoNew.toModel());
					// } else {
					// detailNo.setResultTangentId(resultTangentDTO.getResultTangentId());
					// if (noDb != null) {
					// detailNo.setUpdatedDate(new Date());
					// detailNo.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
					//
					// SimpleDateFormat formatDate=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					// try {
					// detailNo.setCreatedDate(formatDate.parse(detailNo.getCreatedDateDb()));
					// } catch (ParseException e) {
					// // TODO Auto-generated catch block
					// //e.printStackTrace();
					// }
					//
					// resultTangentDetailNoDAO.updateObject(detailNo.toModel());
					// noId = detailNo.getResultTangentDetailNoId();
					// List<String> lsType = new ArrayList<>();
					// lsType.add("SODO");
					// lsType.add("DHTD");
					// resultTangentDetailNoDAO.deleteFileAttachByObjectId(detailNo.getResultTangentDetailNoId(),
					// lsType);
					// } else {
					// detailNo.setCreatedDate(new Date());
					// detailNo.setCreatedUser(user.getVpsUserInfo().getSysUserId());
					// noId = resultTangentDetailNoDAO.saveObject(detailNo.toModel());
					// }
					// }
					//
					// if (resultTangentDTO.getFileRedBook() != null) {
					// List<UtilAttachDocumentDTO> lstAttachSoDo =
					// resultTangentDTO.getFileRedBook();
					// for(UtilAttachDocumentDTO attachSoDo : lstAttachSoDo) {
					// attachSoDo.setStatus("1");
					// attachSoDo.setObjectId(noId);
					// attachSoDo.setType("SODO");
					// attachSoDo.setDescription("File hình ảnh sổ đỏ");
					// utilAttachDocumentDAO.saveObject(attachSoDo.toModel());
					// }
					// }
					//
					// if (resultTangentDTO.getFileTopographic() != null) {
					// List<UtilAttachDocumentDTO> lstAttachDiaHinh =
					// resultTangentDTO.getFileTopographic();
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
				// else {
				// if(resultTangentDTO.getAppointmentDate()!=null) {
				// obj.setStatus("2");
				// }
				// }

				resultTangentDTO.setUpdatedDate(new Date());
				resultTangentDTO.setUpdatedUser(user.getVpsUserInfo().getSysUserId());
				ResultTangentDTO tang = resultTangentDAO
						.getResultTangentTypeByResultTangentId(resultTangentDTO.getResultTangentId());
				if (tang.getResultTangentType() == null
						&& StringUtils.isNotBlank(resultTangentDTO.getResultTangentType())) {
					resultTangentDTO.setRealityTangentDate(new Date());
				}

				SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				try {
					resultTangentDTO.setCreatedDate(formatDate.parse(resultTangentDTO.getCreatedDateDb()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}

				Long resultTangentId = resultTangentDAO.updateObject(resultTangentDTO.toModel());
				if (resultTangentId == 0l) {
					if (tx != null)
						tx.rollback();
				} else {
					if (validateStatus.equals("2") == true && obj.getResultSolutionDTO() != null) {
						Long solutionId = saveResultSolution(obj, request);
						if (solutionId != 0l) {
							tangentCustomerDAO.getSession().flush();
							tangentCustomerDAO.getSession().clear();
							System.out.println("" + solutionId);
						}
					}
					if(tx != null) {
						tx.commit();
					}
				}
			} else {
				saveDetailAppoinmentDate(obj, resultTangentDTO, user);
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

	public Long saveDetail(TangentCustomerDTO obj, HttpServletRequest request) throws Exception {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		// Save Result Tangent
		ResultTangentDTO resultTangentDTO = obj.getResultTangentDTO();
		resultTangentDTO.setTangentCustomerId(obj.getTangentCustomerId());

		updateResultTangent(resultTangentDTO, obj, request);

		// end
		SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			obj.setCreatedDate(formatDate.parse(obj.getCreatedDateDb()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		// huypq-24062021-start
		String linkCall = url_cim_service + "/opportunity/update-status";
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("id", obj.getCrmId());
		personJsonObject.put("surveyStatus", obj.getStatus());
		personJsonObject.put("reason", obj.getReasonRejection());
		String responseCallApiCrm = CallApiUtils.callApiCrm(linkCall, personJsonObject);
		System.out.println(responseCallApiCrm);
		// Huy-end
		Long tangentCustomerId = tangentCustomerDAO.updateObject(obj.toModel());
		tangentCustomerDAO.getSession().flush();
		tangentCustomerDAO.getSession().clear();
		return tangentCustomerId;
	}

	// Save lúc Lý do khách hàng từ chối
	public Long saveNotDemain(ResultTangentDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		Long id = 0l;
		if (obj.getResultTangentId() == null) {
			tangentCustomerDAO.updateStatusTangent(obj.getTangentCustomerId(), "4", null,
					user.getVpsUserInfo().getSysUserId());
			// tangentCustomerDAO.updateStatusTangent(obj.getTangentCustomerId(), "11",null,
			// user.getVpsUserInfo().getSysUserId());// duonghv13-29122021-edit
			obj.setApprovedStatus("0");
			obj.setPerformerId(obj.getTangentCustomerDTO().getPerformerId());
			obj.setOrderResultTangent("1");
			obj.setCreatedDate(new Date());
			obj.setCreatedUser(user.getVpsUserInfo().getSysUserId());
			id = resultTangentDAO.saveObject(obj.toModel());
		} else {
			tangentCustomerDAO.updateStatusTangent(obj.getTangentCustomerId(), "4", null,
					user.getVpsUserInfo().getSysUserId());
			// tangentCustomerDAO.updateStatusTangent(obj.getTangentCustomerId(), "11",null,
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

			id = resultTangentDAO.updateObject(obj.toModel());
		}

		//Huypq-04042022-start call api call bot Từ chối tiếp xúc và chờ phê duyệt
		List<InputSlotsObject> lstMapCallBot = new ArrayList<>();
		InputSlotsObject object = new InputSlotsObject();
		object.setKey(call_bot_key);
		object.setValue(obj.getTangentCustomerDTO().getCustomerName());
		lstMapCallBot.add(object);
		String urlCallbot = call_bot_url + "/api/partner/v1/campaign/1035/createCall";
		JSONObject callbot = new JSONObject();
		callbot.put("api_key", call_bot_api_key);
		callbot.put("customer_phone", obj.getTangentCustomerDTO().getCustomerPhone());
		callbot.put("input_slots", lstMapCallBot);
//		Boolean connectStatus = CallApiUtils.connectionCallbot(urlCallbot, callbotRequest);
		JSONObject requestCallBot = new JSONObject();
		requestCallBot.put("urlCallbot", urlCallbot);
		requestCallBot.put("callbotRequest", callbot);
		CallApiUtils.callApiResponseString(url_aio + "service/aioCallBotWsService/createCallBot", requestCallBot);
		//Huy-end
		
		// huypq-24062021-start
		String linkCall = url_cim_service + "/opportunity/update-status";
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("id",
				tangentCustomerDAO.getCrmIdByTangentCustomerId(obj.getTangentCustomerId()));
		personJsonObject.put("surveyStatus", "4");
		// personJsonObject.put("surveyStatus", "11");// duonghv13-29122021-edit
		personJsonObject.put("reason", obj.getReasonRejection());
		String responseCallApiCrm = CallApiUtils.callApiCrm(linkCall, personJsonObject);
		System.out.println(responseCallApiCrm);
		// Huy-end

		// gui ket qua tiep xuc toi quan ly
		List<TangentCustomerDTO> lstManager = tangentCustomerDAO
				.getListManager(obj.getTangentCustomerDTO().getProvinceId());
		if (lstManager.size() > 0) {
			for (TangentCustomerDTO dto : lstManager) {
				String content = "Khách hàng " + obj.getTangentCustomerDTO().getCustomerName() + " đã được nhân viên "
						+ obj.getTangentCustomerDTO().getPerformerCode() + "-"
						+ obj.getTangentCustomerDTO().getPerformerName()
						+ " tiếp xúc thành công, kết quả tiếp xúc: Khách hàng không có nhu cầu";
				tangentCustomerDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(), content);
				tangentCustomerDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
						user.getVpsUserInfo().getSysUserId(), content);
			}
		}
		// gui ket qua tiep xuc toi TTHT
		List<TangentCustomerDTO> lstUserTTHT = tangentCustomerDAO.getListUserTTHT();
		if (lstUserTTHT.size() > 0) {
			for (TangentCustomerDTO dto : lstUserTTHT) {
				String content = "Khách hàng " + obj.getTangentCustomerDTO().getCustomerName()
						+ " đã được CNKT Tỉnh/TP: " + obj.getTangentCustomerDTO().getProvinceCode()
						+ " tiếp xúc thành công, kết quả tiếp xúc: Khách hàng không có nhu cầu";
				tangentCustomerDAO.insertSendEmail(dto.getEmail(), user.getVpsUserInfo().getSysUserId(), content);
				tangentCustomerDAO.insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
						user.getVpsUserInfo().getSysUserId(), content);
			}
		}
		return id;
	}

	// Save lúc phê duyệt / từ chối kết quả tiếp xúc
	public Long saveApproveOrReject(ResultTangentDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		Long id = 0l;
		// huypq-24062021-start
		String linkCall = url_cim_service + "/opportunity/update-status";
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("id",
				tangentCustomerDAO.getCrmIdByTangentCustomerId(obj.getTangentCustomerId()));
		personJsonObject.put("reason", obj.getReasonRejection());
		// Huy-end
		if (obj.getApprovedStatus() != null && obj.getApprovedStatus().equals("1")) {
			obj.setApprovedDate(new Date());
			obj.setApprovedUserId(user.getVpsUserInfo().getSysUserId());
			tangentCustomerDAO.updateStatusTangent(obj.getTangentCustomerId(), "9", null,
					user.getVpsUserInfo().getSysUserId());
			// tangentCustomerDAO.updateStatusTangent(obj.getTangentCustomerId(), "11",null,
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

			id = resultTangentDAO.updateObject(obj.toModel());

			// huypq-24062021-start
			personJsonObject.put("surveyStatus", "9");
			// personJsonObject.put("surveyStatus", "11");// duonghv13-29122021-edit
			// Huy-end
		} else if (obj.getApprovedStatus() != null && obj.getApprovedStatus().equals("2")) {
			obj.setApprovedDate(new Date());
			obj.setApprovedUserId(user.getVpsUserInfo().getSysUserId());
			tangentCustomerDAO.updateStatusTangent(obj.getTangentCustomerId(), "1", obj.getApprovedPerformerId(),
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

			resultTangentDAO.updateObject(obj.toModel());
			// Lưu ResultTangent
			ResultTangentDTO resultTangentDTO = new ResultTangentDTO();
			resultTangentDTO.setTangentCustomerId(obj.getTangentCustomerId());
			List<ResultTangentBO> resultBo = resultTangentDAO.getResultTangentByTangentCustomerId(resultTangentDTO);
			if (resultBo.size() == 0) {
				resultTangentDTO.setOrderResultTangent("1");
			} else {
				resultTangentDTO.setOrderResultTangent(String.valueOf(resultBo.size() + 1));
			}
			resultTangentDTO.setApprovedStatus(null);
			resultTangentDTO.setCreatedDate(new Date());
			resultTangentDTO.setCreatedUser(user.getVpsUserInfo().getSysUserId());
			resultTangentDTO.setPerformerId(obj.getApprovedPerformerId());
			resultTangentDTO.setPerformerName(obj.getApprovedPerformerName());
			id = resultTangentDAO.saveObject(resultTangentDTO.toModel());
			// huypq-24062021-start
			personJsonObject.put("surveyStatus", "1");
			// Huy-end
			// Gửi sms and email
			ResultTangentDTO result = resultTangentDAO.getListResultTangentJoinSysUserByResultTangentId(obj);
			String contentGiao = "Đồng chí được giao tiếp xúc khách hàng " + obj.getCustomerName() + " do nhân viên "
					+ resultTangentDTO.getPerformerName() + " không hoàn thành";
			String contentTuChoi = "Yêu cầu tiếp xúc khách hàng " + obj.getCustomerName()
					+ " đã được chuyển cho đồng chí " + obj.getApprovedPerformerName()
					+ " do đồng chí tiếp xúc không đạt";
			// bị từ chối
			tangentCustomerDAO.insertSendSMS(result.getPerformerEmail(), result.getPerformerPhone(),
					user.getVpsUserInfo().getSysUserId(), contentTuChoi);
			tangentCustomerDAO.insertSendEmail(result.getPerformerEmail(), user.getVpsUserInfo().getSysUserId(),
					contentTuChoi);
			// được giao tiếp xúc
			tangentCustomerDAO.insertSendSMS(obj.getApprovedPerformerEmail(), obj.getApprovedPerformerPhone(),
					user.getVpsUserInfo().getSysUserId(), contentGiao);
			tangentCustomerDAO.insertSendEmail(obj.getApprovedPerformerEmail(), user.getVpsUserInfo().getSysUserId(),
					contentGiao);
		}
		// huypq-24062021-start
		String responseCallApiCrm = CallApiUtils.callApiCrm(linkCall, personJsonObject);
		System.out.println(responseCallApiCrm);
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

	public TangentCustomerDTO getListResultTangentByTangentCustomerId(ResultTangentDTO obj) {
		TangentCustomerDTO tangent = new TangentCustomerDTO();
		List<ResultTangentDTO> ls = resultTangentDAO.getListResultTangentJoinSysUserByTangentCustomerId(obj);
		DetailTangentCustomerDTO detailCustomer = new DetailTangentCustomerDTO();
		if (ls.size() > 0) {
			// List<UtilAttachDocumentDTO> utilSoDo = new ArrayList<>();
			// List<UtilAttachDocumentDTO> utilDiaHinh = new ArrayList<>();
			List<UtilAttachDocumentDTO> util = resultTangentDAO
					.getFileAttachByResultTangentId(ls.get(0).getResultTangentId(), "KQTX");
			ResultTangentDetailYesDTO yesDto = resultTangentDetailYesDAO
					.getResultTangentYesByResultTangentId(ls.get(0).getResultTangentId());

			tangent.setTangentCustomerId(obj.getTangentCustomerId());
			if (null != obj.getPartnerType()) {
				tangent.setPartnerType(obj.getPartnerType());
				detailCustomer = detailTangentCustomerDAO.getDetailsTangentCustomer(tangent);
				if (null != detailCustomer && null != detailCustomer.getDetailTangentCustomerId())
					yesDto.setDetailCustomer(detailCustomer);
			}
			List<UtilAttachDocumentDTO> lstImageDesign = resultTangentDAO
					.getFileAttachByResultTangentId(ls.get(0).getResultTangentId(), "YCTX_DESIGN_CONSTRUCTION");
			if (lstImageDesign != null && lstImageDesign.size() > 0)
				yesDto.setLstImageDesign(lstImageDesign);
			// ResultTangentDetailNoDTO noDto =
			// resultTangentDetailNoDAO.getResultTangentNoByResultTangentId(ls.get(0).getResultTangentId());
			// if(noDto!=null) {
			// utilSoDo =
			// resultTangentDAO.getFileAttachByResultTangentId(noDto.getResultTangentDetailNoId(),
			// "SODO");
			// utilDiaHinh =
			// resultTangentDAO.getFileAttachByResultTangentId(noDto.getResultTangentDetailNoId(),
			// "DHTD");
			// }

			ls.get(0).setResultTangentDetailYesDTO(yesDto); // Lấy dữ liệu Có thiết kế
			// ls.get(0).setResultTangentDetailNoDTO(noDto); //Lấy dữ liệu không thiết kế
			ls.get(0).setFileReceipts(util);
			// ls.get(0).setFileRedBook(utilSoDo); //Lấy file sổ đỏ
			// ls.get(0).setFileTopographic(utilDiaHinh); //Lấy địa hình
			tangent.setResultTangentDTO(ls.get(0));
			
			// Lấy list lịch sử tiếp xúc
			for (ResultTangentDTO result : ls) {
				List<UtilAttachDocumentDTO> utils = resultTangentDAO
						.getFileAttachByResultTangentId(result.getResultTangentId(), "KQTX");
				result.setFileReceipts(utils);
				if (null != result.getResultTangentType()) {
					if (result.getResultTangentType().equals("1") == true) {
						if (null != obj.getPartnerType())
							result.setPartnerType(obj.getPartnerType());
						ResultTangentDetailYesDTO yesDetailDto = resultTangentDetailYesDAO
								.getResultTangentYesByResultTangentId(result.getResultTangentId());
						if(null!=yesDetailDto) {
							if (null != detailCustomer && null != detailCustomer.getDetailTangentCustomerId())
								yesDetailDto.setDetailCustomer(detailCustomer);
							List<UtilAttachDocumentDTO> lstYesImageDesign = resultTangentDAO
									.getFileAttachByResultTangentId(result.getResultTangentId(), "YCTX_DESIGN_CONSTRUCTION");
							if (lstYesImageDesign != null && lstYesImageDesign.size() > 0)
								yesDetailDto.setLstImageDesign(lstYesImageDesign);
							result.setResultTangentDetailYesDTO(yesDetailDto);
						}
					}
				}

			}
		}

		List<ResultSolutionDTO> lstSolution = resultSolutionDAO
				.getResultSolutionJoinSysUserByTangentCustomerId(obj.getTangentCustomerId());
		if (lstSolution.size() > 0) {
			List<UtilAttachDocumentDTO> utilGp = resultTangentDAO
					.getFileAttachByResultTangentId(lstSolution.get(0).getResultSolutionId(), "GP");
			lstSolution.get(0).setFileResultSolution(utilGp);
			tangent.setResultSolutionDTO(lstSolution.get(0));
			for (ResultSolutionDTO solution : lstSolution) {
				List<UtilAttachDocumentDTO> util = resultTangentDAO
						.getFileAttachByResultTangentId(solution.getResultSolutionId(), "GP");
				solution.setFileResultSolution(util);
			}
		}


		tangent.setListResultTangent(ls);
		tangent.setListResultSolution(lstSolution);
		return tangent;
	}

	// hoanm1_20200612_start
	public TangentCustomerDTO getCountStateTangentCustomer(SysUserRequest request) {
		TangentCustomerDTO dto = tangentCustomerDAO.getCountStateTangentCustomer(request);
		return dto;
	}

	public List<TangentCustomerDTO> getListTangentCustomer(SysUserRequest request) {
		return tangentCustomerDAO.getListTangentCustomer(request);
	}

	public int updateTangentCustomer(TangentCustomerRequest request) throws ParseException {
		return tangentCustomerDAO.updateTangentCustomer(request);
	}

	public List<ConstructionImageInfo> getImagesResultSolution(TangentCustomerRequest request) {
		List<ConstructionImageInfo> listImageResponse = new ArrayList<>();
		List<ConstructionImageInfo> listImage = tangentCustomerDAO
				.getImagesResultSolutionId(request.getTangentCustomerDTO().getResultSolutionId());
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

	public List<TangentCustomerDTO> getListContract(SysUserRequest request) {
		return tangentCustomerDAO.getListContract(request);
	}

	public Long addTangentCustomer(TangentCustomerRequest dto) {
		return tangentCustomerDAO.addTangentCustomer(dto);
	}

	public Long updateTangentCustomerCreate(TangentCustomerRequest request) {
		return tangentCustomerDAO.updateTangentCustomerCreate(request);
	}

	public List<TangentCustomerDTO> getDataProvinceCity() {
		return tangentCustomerDAO.getDataProvinceCity();
	}

	public List<TangentCustomerDTO> getDataDistrict(TangentCustomerDTO obj) {
		return tangentCustomerDAO.getDataDistrict(obj.getParentId());
	}

	public List<TangentCustomerDTO> getDataWard(TangentCustomerDTO obj) {
		return tangentCustomerDAO.getDataWard(obj.getParentId());
	}

	// hoanm1_20200612_end
	// huypq-16062020-start
	public DataListDTO getAllContractXdddSuccess(ResultSolutionDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		List<ResultSolutionDTO> ls = resultSolutionDAO.getAllContractXdddSuccess(obj,
				user.getVpsUserInfo().getSysGroupId());
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setStart(1);
		data.setSize(obj.getPageSize());
		data.setTotal(obj.getTotalRecord());
		return data;
	}

	public ResultSolutionDTO getResultSolutionByContractId(Long contractId) {
		ResultSolutionDTO dto = resultSolutionDAO.getResultSolutionByContractId(contractId);
		return dto;
	}

	public Long saveResultSolution(TangentCustomerDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		boolean isFailSign = false;
		// List<ResultSolutionDTO> haveDb =
		// resultSolutionDAO.getResultSolutionJoinSysUserByTangentCustomerId(obj.getTangentCustomerId());
		// huypq-24062021-start
		String linkCall = url_cim_service + "/opportunity/update-status";
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("id",
				tangentCustomerDAO.getCrmIdByTangentCustomerId(obj.getTangentCustomerId()));
		personJsonObject.put("reason", "");
		// Huy-end
		Long id = 0l;
		ResultSolutionDTO solutionDto = obj.getResultSolutionDTO();
		if (solutionDto != null) {
			if (solutionDto.getContractId() == null) {
				solutionDto.setSignDate(null);
				solutionDto.setGuaranteeTime(null);
				solutionDto.setConstructTime(null);
			}
			if (solutionDto.getResultSolutionId() == null) {
				solutionDto.setCreatedDate(new Date());
				solutionDto.setCreatedUser(user.getVpsUserInfo().getSysUserId());

				solutionDto.setResultSolutionOrder(1l);
				solutionDto.setTangentCustomerId(obj.getTangentCustomerId());
				solutionDto.setPerformerId(obj.getPerformerId());
				Long solutionId = resultSolutionDAO.saveObject(solutionDto.toModel());
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
							personJsonObject.put("surveyStatus", "5");
							content = "Công việc của khách hàng " + obj.getCustomerName() + " - " + obj.getCustomerPhone() + " đang chờ lên hợp đồng";
							contentTTHT = "Công việc của khách hàng " + obj.getCustomerName() + " - " + obj.getCustomerPhone() + " đang chờ lên hợp đồng";
						} else {
							obj.setStatus("8");
							personJsonObject.put("surveyStatus", "8");
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
						personJsonObject.put("surveyStatus", "7");
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
						personJsonObject.put("surveyStatus", "6");
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
				List<ResultSolutionDTO> dto = resultSolutionDAO
						.getResultSolutionByTangentCustomerId(obj.getTangentCustomerId());
				List<TangentCustomerDTO> lstManager = tangentCustomerDAO.getListManager(obj.getProvinceId());
				List<TangentCustomerDTO> lstUserTTHT = tangentCustomerDAO.getListUserTTHT();
				String content = "";
				String contentTTHT = "";
				if (dto.size() == 0) {
					solutionDto.setCreatedDate(new Date());
					solutionDto.setCreatedUser(user.getVpsUserInfo().getSysUserId());
					solutionDto.setTangentCustomerId(obj.getTangentCustomerId());
					solutionDto.setResultSolutionOrder((long) dto.size() + 1l);
					if (solutionDto.getResultSolutionType() != null) {
						if (solutionDto.getResultSolutionType().equals("1")) {
							if (solutionDto.getContractId() == null) {
								obj.setStatus("5");
								personJsonObject.put("surveyStatus", "5");
								content = "Công việc của khách hàng " + obj.getCustomerName() + " - " + obj.getCustomerPhone() + " đang chờ lên hợp đồng";
								contentTTHT = "Công việc của khách hàng " + obj.getCustomerName() + " - " + obj.getCustomerPhone() + " đang chờ lên hợp đồng";

							} else {
								obj.setStatus("8");

								personJsonObject.put("surveyStatus", "8");
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
							personJsonObject.put("surveyStatus", "7");
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
							personJsonObject.put("surveyStatus", "6");
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
					Long solutionId = resultSolutionDAO.saveObject(solutionDto.toModel());
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
					ResultSolutionDTO maxResult = dto.get(0);
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
						utilAttachDocumentDAO.deleteExtend(maxResult.getResultSolutionId(), "GP");
						List<UtilAttachDocumentDTO> lstAttachGiaiPhap = solutionDto.getFileResultSolution();
						for (UtilAttachDocumentDTO attachGiaiPhap : lstAttachGiaiPhap) {
							attachGiaiPhap.setStatus("1");
							attachGiaiPhap.setObjectId(maxResult.getResultSolutionId());
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
								personJsonObject.put("surveyStatus", obj.getStatus());
								content = "Công việc của khách hàng " + obj.getCustomerName() + " - " + obj.getCustomerPhone() + " đang chờ lên hợp đồng";
								contentTTHT = "Công việc của khách hàng " + obj.getCustomerName() + " - " + obj.getCustomerPhone() + " đang chờ lên hợp đồng";
							} else {
								obj.setStatus("8");
								personJsonObject.put("surveyStatus", "8");
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
							personJsonObject.put("surveyStatus", "7");
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
							personJsonObject.put("surveyStatus", "6");

							maxResult.setApprovedPerformerId(solutionDto.getApprovedPerformerId());
							maxResult.setApprovedDescription(solutionDto.getApprovedDescription());

							solutionDto.setCreatedDate(new Date());
							solutionDto.setCreatedUser(user.getVpsUserInfo().getSysUserId());
							solutionDto.setResultSolutionOrder((long) dto.size() + 1l);
							solutionDto.setPresentSolutionDate(solutionDto.getPresentSolutionDateNext());
							solutionDto.setPresentSolutionDateNext(null);
							solutionDto.setTangentCustomerId(obj.getTangentCustomerId());
							solutionDto.setPerformerId(obj.getPerformerId());
							solutionDto.setResultSolutionType(null);
							Long solutionId = resultSolutionDAO.saveObject(solutionDto.toModel());
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

					resultSolutionDAO.updateObject(maxResult.toModel());
					if (isFailSign == true) {
						CatPartnerDTO origin = new CatPartnerDTO();
						origin.setStatus(0l);
						origin.setUpdatedDate(new Date());
						origin.setUpdatedUserId(user.getVpsUserInfo().getSysUserId());
						origin.setTangentCustomerId(obj.getTangentCustomerId());
						tangentCustomerDAO.updatePartnerTTHT(origin);
					}
				}

				if (!"".equals(content)) {
					for (TangentCustomerDTO userDto : lstManager) {
						tangentCustomerDAO.insertSendEmail(userDto.getEmail(), user.getVpsUserInfo().getSysUserId(),
								content);
						tangentCustomerDAO.insertSendSMS(userDto.getEmail(), userDto.getPhoneNumber(),
								user.getVpsUserInfo().getSysUserId(), content);
					}
				}

				if (!"".equals(contentTTHT)) {
					for (TangentCustomerDTO dtoTTHT : lstUserTTHT) {
						tangentCustomerDAO.insertSendEmail(dtoTTHT.getEmail(), user.getVpsUserInfo().getSysUserId(),
								contentTTHT);
						tangentCustomerDAO.insertSendSMS(dtoTTHT.getEmail(), dtoTTHT.getPhoneNumber(),
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
		id = tangentCustomerDAO.updateObject(obj.toModel());
		String responseCallApiCrm = CallApiUtils.callApiCrm(linkCall, personJsonObject);
		System.out.println(responseCallApiCrm);
		return id;
	}

	// Save lúc phê duyệt / từ chối kết quả giải pháp
	public Long saveApproveOrRejectGiaiPhap(ResultSolutionDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		// List<ResultSolutionDTO> lstSolution =
		// resultSolutionDAO.getResultSolutionByTangentCustomerId(obj.getTangentCustomerId());
		Long id = 0l;
		String contentStaff = "";
		String contentManage = "";

		ResultSolutionDTO result = resultSolutionDAO
				.getResultSolutionJoinSysUserByResultSolutionId(obj.getResultSolutionId());

		// huypq-24062021-start
		String linkCall = url_cim_service + "/opportunity/update-status";
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("id",
				tangentCustomerDAO.getCrmIdByTangentCustomerId(obj.getTangentCustomerId()));
		personJsonObject.put("reason", obj.getContentResultSolution());
		// Huy-end

		if (obj.getApprovedStatus() != null && obj.getApprovedStatus().equals("1")) {
			obj.setApprovedDate(new Date());
			obj.setApprovedUserId(user.getVpsUserInfo().getSysUserId());
			obj.setApprovedStatus(obj.getApprovedStatus());
			obj.setApprovedPerformerId(obj.getApprovedPerformerId());
			obj.setApprovedDescription(obj.getApprovedDescription());
			tangentCustomerDAO.updateStatusTangent(obj.getTangentCustomerId(), "9", null,
					user.getVpsUserInfo().getSysUserId());
			// tangentCustomerDAO.updateStatusTangent(obj.getTangentCustomerId(), "11",null,
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

			id = resultSolutionDAO.updateObject(obj.toModel());
			contentStaff = "TTHT đã phê duyệt đề nghị đóng kết quả trình bầy giải pháp với khách hàng "
					+ obj.getCustomerName();
			contentManage = "TTHT đã phê duyệt đề nghị đóng kết quả trình bầy giải pháp với khách hàng "
					+ obj.getCustomerName();

			personJsonObject.put("surveyStatus", "9");
			// personJsonObject.put("surveyStatus", "11");
		} else if (obj.getApprovedStatus() != null && obj.getApprovedStatus().equals("2")) {
			obj.setApprovedDate(new Date());
			obj.setApprovedUserId(user.getVpsUserInfo().getSysUserId());
			obj.setApprovedStatus(obj.getApprovedStatus());
			obj.setApprovedPerformerId(obj.getApprovedPerformerId());
			obj.setApprovedDescription(obj.getApprovedDescription());
			tangentCustomerDAO.updateStatusTangent(obj.getTangentCustomerId(), "3", obj.getApprovedPerformerId(),
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

			id = resultSolutionDAO.updateObject(obj.toModel());

			// Lưu ResultTangent
			ResultSolutionDTO resultSolutionDTO = new ResultSolutionDTO();
			resultSolutionDTO.setTangentCustomerId(obj.getTangentCustomerId());
			List<ResultSolutionBO> resultBo = resultSolutionDAO.getResultSolutionByTangentCustomerId(resultSolutionDTO);
			if (resultBo.size() == 0) {
				resultSolutionDTO.setResultSolutionOrder(1l);
			} else {
				resultSolutionDTO.setResultSolutionOrder((long) resultBo.size() + 1l);
			}
			resultSolutionDTO.setApprovedStatus("0");
			resultSolutionDTO.setCreatedDate(new Date());
			resultSolutionDTO.setCreatedUser(user.getVpsUserInfo().getSysUserId());
			// resultSolutionDTO.setPerformerId(obj.getApprovedPerformerId());
			// resultSolutionDTO.setPerformerName(obj.getApprovedPerformerName());
			id = resultSolutionDAO.saveObject(resultSolutionDTO.toModel());

			// Gửi sms and email
			String contentGiao = "Đồng chí được giao trình bày giải pháp cho khách hàng " + obj.getCustomerName()
					+ " do nhân viên " + result.getPerformerName() + " không hoàn thành";
			// String contentTuChoi = "Yêu cầu trình bày giải pháp cho khách hàng " +
			// obj.getCustomerName() + " đã được chuyển cho đồng chí " +
			// obj.getApprovedPerformerName() + " do đồng chí trình bày giải pháp không
			// đạt";
			// bị từ chối
			// tangentCustomerDAO.insertSendSMS(result.getPerformerEmail(),
			// result.getPerformerPhone(), user.getVpsUserInfo().getSysUserId(),
			// contentTuChoi);
			// tangentCustomerDAO.insertSendEmail(result.getPerformerEmail(),
			// user.getVpsUserInfo().getSysUserId(), contentTuChoi);
			// được giao tiếp xúc
			tangentCustomerDAO.insertSendSMS(obj.getApprovedPerformerEmail(), obj.getApprovedPerformerPhone(),
					user.getVpsUserInfo().getSysUserId(), contentGiao);
			tangentCustomerDAO.insertSendEmail(obj.getApprovedPerformerEmail(), user.getVpsUserInfo().getSysUserId(),
					contentGiao);

			contentStaff = "TTHT đã từ chối đề nghị đóng yêu cầu trình bầy giải pháp khách hàng "
					+ obj.getCustomerName() + " của đồng chí";
			contentManage = "TTHT đã từ chối đề nghị đóng yêu cầu trình bầy giải pháp khách hàng "
					+ obj.getCustomerName() + " của nhân viên " + result.getPerformerName()
					+ ", Đề nghị Đ/c kiểm tra chỉ đạo triển khai theo nội dung bổ sung của TTHT";

			personJsonObject.put("surveyStatus", "3");
		}
		// huypq-24062021-start
		String responseCallApiCrm = CallApiUtils.callApiCrm(linkCall, personJsonObject);
		System.out.println(responseCallApiCrm);
		// Huy-end

		tangentCustomerDAO.insertSendSMS(result.getPerformerEmail(), result.getPerformerPhone(),
				user.getVpsUserInfo().getSysUserId(), contentStaff);
		tangentCustomerDAO.insertSendEmail(result.getPerformerEmail(), user.getVpsUserInfo().getSysUserId(),
				contentStaff);

		List<TangentCustomerDTO> lstManager = tangentCustomerDAO.getListManager(obj.getProvinceId());
		for (TangentCustomerDTO userDto : lstManager) {
			tangentCustomerDAO.insertSendEmail(userDto.getEmail(), user.getVpsUserInfo().getSysUserId(), contentManage);
			tangentCustomerDAO.insertSendSMS(userDto.getEmail(), userDto.getPhoneNumber(),
					user.getVpsUserInfo().getSysUserId(), contentManage);
		}

		return id;
	}

	public List<ResultTangentDTO> getListResultTangentJoinSysUserByTangentCustomerId(ResultTangentDTO obj) {
		return resultTangentDAO.getListResultTangentJoinSysUserByTangentCustomerId(obj);
	}

	public List<ResultSolutionDTO> getResultSolutionJoinSysUserByTangentCustomerId(Long customerId) {
		return resultSolutionDAO.getResultSolutionJoinSysUserByTangentCustomerId(customerId);
	}

	public ResultTangentDTO getListResultTangentByResultTangentId(ResultTangentDTO obj) {
		ResultTangentDTO ls = resultTangentDAO.getListResultTangentJoinSysUserByResultTangentId(obj);
		if (ls != null) {
			// List<UtilAttachDocumentDTO> util =
			// resultTangentDAO.getFileAttachByResultTangentId(ls.getResultTangentId(),
			// "KQTX");
			ResultTangentDetailYesDTO yesDto = resultTangentDetailYesDAO
					.getResultTangentYesByResultTangentId(ls.getResultTangentId());
			TangentCustomerDTO tangent = new TangentCustomerDTO();
			tangent.setTangentCustomerId(obj.getTangentCustomerId());
			DetailTangentCustomerDTO detailCustomer = new DetailTangentCustomerDTO();
			if (obj.getPartnerType() != null) {
				tangent.setPartnerType(obj.getPartnerType());
				detailCustomer = detailTangentCustomerDAO.getDetailsTangentCustomer(tangent);
				yesDto.setDetailCustomer(detailCustomer);
			}
			List<UtilAttachDocumentDTO> lstImageDesign = resultTangentDAO
					.getFileAttachByResultTangentId(ls.getResultTangentId(), "YCTX_DESIGN_CONSTRUCTION");
			if (lstImageDesign != null && lstImageDesign.size() > 0)
				yesDto.setLstImageDesign(lstImageDesign);
			// ResultTangentDetailNoDTO noDto =
			// resultTangentDetailNoDAO.getResultTangentNoByResultTangentId(ls.getResultTangentId());
			// List<UtilAttachDocumentDTO> utilSoDo = new ArrayList<>();
			// List<UtilAttachDocumentDTO> utilDiaHinh = new ArrayList<>();
			// if(noDto!=null) {
			// utilSoDo =
			// resultTangentDAO.getFileAttachByResultTangentId(noDto.getResultTangentDetailNoId(),
			// "SODO");
			// utilDiaHinh =
			// resultTangentDAO.getFileAttachByResultTangentId(noDto.getResultTangentDetailNoId(),
			// "DHTD");
			// }
			ls.setResultTangentDetailYesDTO(yesDto);
			// ls.setResultTangentDetailNoDTO(noDto);
			// ls.setFileReceipts(util);
			// ls.setFileRedBook(utilSoDo);
			// ls.setFileTopographic(utilDiaHinh);
		}

		List<UtilAttachDocumentDTO> util = resultTangentDAO.getFileAttachByResultTangentId(ls.getResultTangentId(),
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
		return tangentCustomerDAO.getContractRose();
	}

	public Long approveRose(ResultSolutionDTO obj, HttpServletRequest request) throws Exception {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setUserApprovedId(user.getVpsUserInfo().getSysUserId());
		;
		return tangentCustomerDAO.approveRose(obj, request);
	}
	// Huy-end

	// Huypq-21052021-start
	public DataListDTO doSearchProvince(CatProvinceDTO obj) {
		List<CatProvinceDTO> ls = new ArrayList<CatProvinceDTO>();
		ls = tangentCustomerDAO.doSearchProvince(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	// Huy-end

	public Long checkRoleSourceYCTX(TangentCustomerDTO obj, HttpServletRequest request) {
		// TODO Auto-generated method stub
		// DUONGHV13-27122021-start
		Long IDRole = null;
		List<SysGroupDTO> listGroupTangent = tangentCustomerDAO.getListGroupTangent(Long.parseLong(GROUP_ID));
		SysUserDTO sysUser = tangentCustomerDAO.getTypeUserTangentForSource(obj);
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

	public DataListDTO getAllContractXdddSuccessInternal(ResultSolutionDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		List<ResultSolutionDTO> ls = resultSolutionDAO.getAllContractXdddSuccessInternal(obj,
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
		ls = tangentCustomerDAO.getChannel(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	// Duonghv13-11022022-end

	public String exportExcelqlyctx(DataListDTO data) throws Exception {
		// TODO Auto-generated method stub
		
		try {
		String folderParam = defaultSubFolderUpload;
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();

		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Bao_cao_yeu_cau_tiep_xuc.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		file.close();

		String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload);

		String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload);
		File udir = new File(uploadPath);
		if (!udir.exists()) {
			udir.mkdirs();
		}
		OutputStream outFile = new FileOutputStream(
				udir.getAbsolutePath() + File.separator + "Bao_cao_yeu_cau_tiep_xuc.xlsx");
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
			List<TangentCustomerDTO> lstTangent = data.getData();
			for (TangentCustomerDTO obj : lstTangent) {
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
				cell.setCellValue(StringUtils.isNotEmpty(obj.getBirthYear()) ? obj.getBirthYear() : "");
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
				cell.setCellValue(obj.getSuggestTime() != null ? dateFormat.format(obj.getSuggestTime()) : "");
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
					if (null == obj.getResultTangentDTO() || null == obj.getResultTangentDTO().getCreatedDateDb())
						cell.setCellValue("");
					else	cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentDTO().getCreatedDateDb()) ? obj.getResultTangentDTO().getCreatedDateDb() : "");
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
					
				} else if (obj.getResultTangentDTO() != null && (obj.getStatus().equals("3") == true || obj.getStatus().equals("5") == true || obj.getStatus().equals("6") == true || obj.getStatus().equals("7") == true)) {
					cell = row.createCell(19, CellType.STRING);
					cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentDTO().getCreatedDateDb()) ? obj.getResultTangentDTO().getCreatedDateDb() : "");
					cell.setCellStyle(style);
					cell = row.createCell(20, CellType.STRING);
					cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentDTO().getCreatedDateYes()) ? obj.getResultTangentDTO().getCreatedDateYes() : "");
					cell.setCellStyle(style);
					if (obj.getStatus().equals("5") == true) {
						if (null != obj.getResultSolutionDTO()) { cell = row.createCell(21, CellType.STRING); 
						cell.setCellValue(StringUtils.isNotEmpty(obj.getResultSolutionDTO().getUpdatedDateDb()) ? obj.getResultSolutionDTO().getUpdatedDateDb() : "");
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

				} else if (obj.getStatus().equals("9") == true  && obj.getResultTangentDTO() != null) {
					cell = row.createCell(19, CellType.STRING);
					cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentDTO().getCreatedDateDb()) ? obj.getResultTangentDTO().getCreatedDateDb() : "");
					cell.setCellStyle(style);
					if (null == obj.getResultSolutionDTO()) {
						cell = row.createCell(20, CellType.STRING);
						cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentDTO().getUpdatedDateDb())
								? obj.getUpdatedDateDb()
								: "");
						cell.setCellStyle(style);
						cell = row.createCell(21, CellType.STRING);
						cell.setCellValue("");
						cell.setCellStyle(style);
						cell = row.createCell(22, CellType.STRING);
						cell.setCellValue("");
						cell.setCellStyle(style);
					} else {
						cell = row.createCell(20, CellType.STRING);
						cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentDTO().getCreatedDateYes()) ? obj.getResultTangentDTO().getCreatedDateYes() : "");
						cell.setCellStyle(style);
						cell = row.createCell(21, CellType.STRING);
						cell.setCellValue(StringUtils.isNotEmpty(obj.getResultSolutionDTO().getUpdatedDateDb()) ? obj.getResultSolutionDTO().getUpdatedDateDb() : "");
						cell.setCellStyle(style);
						cell = row.createCell(22, CellType.STRING);
						cell.setCellValue("");
						cell.setCellStyle(style);
					}

				} else if (obj.getStatus().equals("8") == true && obj.getResultTangentDTO() != null) {
					cell = row.createCell(19, CellType.STRING);
					cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentDTO().getCreatedDateDb()) ? obj.getResultTangentDTO().getCreatedDateDb() : "");
					cell.setCellStyle(style);
					cell = row.createCell(20, CellType.STRING);
					cell.setCellValue(StringUtils.isNotEmpty(obj.getResultTangentDTO().getCreatedDateYes()) ? obj.getResultTangentDTO().getCreatedDateYes() : "");
					cell.setCellStyle(style);
					cell = row.createCell(21, CellType.STRING);
					cell.setCellValue(StringUtils.isNotEmpty(obj.getResultSolutionDTO().getUpdatedDateDb()) ? obj.getResultSolutionDTO().getUpdatedDateDb() : "");
					cell.setCellStyle(style);
					cell = row.createCell(22, CellType.STRING);
					cell.setCellValue(StringUtils.isNotEmpty(obj.getResultSolutionDTO().getUpdatedDateDb()) ? obj.getResultSolutionDTO().getUpdatedDateDb() : "");
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

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Bao_cao_yeu_cau_tiep_xuc.xlsx");
		return path;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	
	//Huypq-24062021-start
	public ConfigStaffTangentDTO getUserConfigTagentByProvince(ConfigStaffTangentDTO obj) {
		return tangentCustomerDAO.getUserConfigTagentByProvince(obj.getCatProvinceId());
	}
	
	public Boolean checkRoleUserAssignYctx(HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		return tangentCustomerDAO.checkRoleUserAssignYctx(user.getSysUserId());
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

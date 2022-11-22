package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.viettel.asset.bo.SysGroup;
import com.viettel.coms.bo.WoBO;
import com.viettel.coms.bo.WoMappingChecklistBO;
import com.viettel.coms.bo.WoTypeBO;
import com.viettel.coms.dao.WoDAO;
import com.viettel.coms.dao.WoMappingChecklistDAO;
import com.viettel.coms.dao.WoTypeDAO;
import com.viettel.coms.dto.WoDTO;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.org.eclipse.jdt.core.dom.SwitchCase;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.coms.bo.DeviceStationElectricalBO;
import com.viettel.coms.bo.SendEmailBO;
import com.viettel.coms.bo.StationElectricalBO;
import com.viettel.coms.bo.SysGroupBO;
import com.viettel.coms.dao.AttachElectronicStationDAO;
import com.viettel.coms.dao.BatteryDAO;
import com.viettel.coms.dao.CabinetsSourceACDAO;
//import com.viettel.coms.dao.CabinetsSourceAcDAO;
import com.viettel.coms.dao.CabinetsSourceDCDAO;
import com.viettel.coms.dao.ElectricATSDAO;
import com.viettel.coms.dao.ElectricAirConditioningACDAO;
import com.viettel.coms.dao.ElectricAirConditioningDCDAO;
import com.viettel.coms.dao.ElectricDetailDAO;
import com.viettel.coms.dao.ElectricEarthingSystemDAO;
import com.viettel.coms.dao.ElectricExplosionFactoryDAO;
import com.viettel.coms.dao.ElectricFireExtinguisherDAO;
import com.viettel.coms.dao.ElectricHeatExchangerDAO;
import com.viettel.coms.dao.ElectricLightningCutFilterDAO;
import com.viettel.coms.dao.ElectricNotificationFilterDustDAO;
import com.viettel.coms.dao.ElectricRectifierDAO;
import com.viettel.coms.dao.ElectricWarningSystemDAO;
import com.viettel.coms.dao.GeneratorDAO;
import com.viettel.coms.dao.ManageMEDAO;
import com.viettel.coms.dao.SendEmailDAO;
import com.viettel.coms.dao.StationInformationDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dao.WoDAO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.AttachElectronicStationDTO;
import com.viettel.coms.dto.BatteryDTO;
import com.viettel.coms.dto.CabinetsSourceACDTO;
import com.viettel.coms.dto.CabinetsSourceDCDTO;
import com.viettel.coms.dto.DeviceStationElectricalDTO;
import com.viettel.coms.dto.DocManagementDTO;
import com.viettel.coms.dto.ElectricATSDTO;
import com.viettel.coms.dto.ElectricAirConditioningACDTO;
import com.viettel.coms.dto.ElectricAirConditioningDCDTO;
import com.viettel.coms.dto.ElectricDetailDTO;
import com.viettel.coms.dto.ElectricEarthingSystemDTO;
import com.viettel.coms.dto.ElectricExplosionFactoryDTO;
import com.viettel.coms.dto.ElectricFireExtinguisherDTO;
import com.viettel.coms.dto.ElectricHeatExchangerDTO;
import com.viettel.coms.dto.ElectricLightningCutFilterDTO;
import com.viettel.coms.dto.ElectricNotificationFilterDustDTO;
import com.viettel.coms.dto.ElectricRectifierDTO;
import com.viettel.coms.dto.ElectricWarningSystemDTO;
import com.viettel.coms.dto.GeneratorDTO;
import com.viettel.coms.dto.StationElectricalDTO;
import com.viettel.coms.dto.StationElectricalRequest;
import com.viettel.coms.dto.StationInformationDTO;
import com.viettel.coms.dto.UnitListDTO;
import com.viettel.coms.dto.UserDirectoryDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ImageUtil;
import com.viettel.wms.dao.AppParamDAO;
import org.springframework.transaction.annotation.Transactional;

@Service("manageMEBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ManageMEBusinessImpl extends BaseFWBusinessImpl<ManageMEDAO, StationElectricalDTO, DeviceStationElectricalBO>
		implements ManageMEBusiness {

	static Logger LOGGER = LoggerFactory.getLogger(ManageMEBusinessImpl.class);

	@Autowired
	private ManageMEDAO manageMEDAO;
	
	@Autowired
	private AppParamDAO appParamDAO;
	
	@Autowired
	private ElectricDetailDAO electricDetailDAO;
	
	@Autowired
	private CabinetsSourceACDAO cabinetsSourceACDAO;
	
	@Autowired
	private CabinetsSourceDCDAO cabinetsSourceDCDAO;
	@Autowired
    UtilAttachDocumentDAO utilAttachDocumentDAO;
	@Autowired
	private ElectricAirConditioningACDAO electricAirConditioningACDAO;
	
	@Autowired
	private AttachElectronicStationDAO attachElectronicStationDAO;
	
	@Autowired
	private ElectricHeatExchangerDAO electricHeatExchangerDAO;
	
	@Autowired
	private ElectricNotificationFilterDustDAO electricNotificationFilterDustDAO;
	
	@Autowired
	private ElectricAirConditioningDCDAO electricAirConditioningDCDAO;
	
	@Autowired
	private GeneratorDAO generatorDAO;
	
	@Autowired
	private BatteryDAO batteryDAO;
	
	@Autowired
	private ElectricFireExtinguisherDAO electricFireExtinguisherDAO;
	
	@Autowired
	private ElectricWarningSystemDAO electricWarningSystemDAO;
	
	@Autowired
	private ElectricATSDAO electricATSDAO;
	
	@Autowired
	private ElectricExplosionFactoryDAO electricExplosionFactoryDAO;
	
	@Autowired
	private ElectricLightningCutFilterDAO electricLightningCutFilterDAO;
	
	@Autowired
	private ElectricEarthingSystemDAO electricEarthingSystemDAO;
	
	@Autowired
	private ElectricRectifierDAO electricRectifierDAO;
	
	@Autowired
	private StationInformationDAO stationInformationDAO;

	@Autowired
	private WoTypeDAO woTypeDAO;

	@Autowired
	private WoDAO woDAO;

	@Autowired
	private WoMappingChecklistDAO woMappingChecklistDAO;
	
	 @Autowired
		private SendEmailDAO sendEmailDAO;

	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	@Value("${input_image_sub_folder_upload}")
	private String input_image_sub_folder_upload;

	public ManageMEBusinessImpl() {
		tDAO = manageMEDAO;
	}

	@Override
	public ManageMEDAO gettDAO() {
		return manageMEDAO;
	}

	@Override
	public List<StationElectricalDTO> doSearch(StationElectricalDTO obj, Long sysUserId) {
		// TODO Auto-generated method stub
		return manageMEDAO.doSearch(obj, sysUserId);
	}

	@Override
	public List<DeviceStationElectricalDTO> getDevices(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		List<DeviceStationElectricalDTO> listDevices =  manageMEDAO.getDevices(obj);
		for(DeviceStationElectricalDTO device : listDevices) {
			UtilAttachDocumentDTO attachFile  = new UtilAttachDocumentDTO();
	        attachFile.setType("ELECTRIC_FILE");
			attachFile.setObjectId(device.getDeviceId());
			attachFile = utilAttachDocumentDAO.getAttachFile(attachFile);
			device.setAttachFile(attachFile);
			if(null!=attachFile)device.setAttachFileName(attachFile.getName());
		}
		return listDevices;
	}

	public DataListDTO getEquipments(AppParamDTO obj) {
		// TODO Auto-generated method stub
		List<AppParamDTO> ls = manageMEDAO.getEquipments(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public DataListDTO getDeviceDetails(DeviceStationElectricalDTO obj, Boolean isMobile) {
		// TODO Auto-generated method stub
		DataListDTO data = new DataListDTO();
		List<AttachElectronicStationDTO> listImageMobile = new ArrayList<>();
		switch (obj.getType()) {    
		case "LUOI_DIEN":
			List<ElectricDetailDTO> ls = manageMEDAO.getDeviceDetails(obj);
			
			AttachElectronicStationDTO dto = new AttachElectronicStationDTO();
			dto.setObjectId(obj.getDeviceId());
			dto.setType("LUOI_DIEN");
			if(ls.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(dto);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst4 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst5 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst6 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst7 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst8 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst9 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst10 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst11 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst12 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst13 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("1")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("2")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("3")) {
							lst3.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("4")) {
							lst4.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("5")) {
							lst5.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("6")) {
							lst6.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("7")) {
							lst7.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("8")) {
							lst8.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("9")) {
							lst9.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("10")) {
							lst10.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("11")) {
							lst11.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("12")) {
							lst12.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("13")) {
							lst13.add(file);
						}
					}
				}
				ls.get(0).setListImage1(lst1);
				ls.get(0).setListImage2(lst2);
				ls.get(0).setListImage3(lst3);
				ls.get(0).setListImage4(lst4);
				ls.get(0).setListImage5(lst5);
				ls.get(0).setListImage6(lst6);
				ls.get(0).setListImage7(lst7);
				ls.get(0).setListImage8(lst8);
				ls.get(0).setListImage9(lst9);
				ls.get(0).setListImage10(lst10);
				ls.get(0).setListImage11(lst11);
				ls.get(0).setListImage12(lst12);
				ls.get(0).setListImage13(lst13);
				ls.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(ls);
		    break; 
		case "TU_NGUON_AC":
			List<CabinetsSourceACDTO> lsCabinetsSourceAC = manageMEDAO.getCabinetsSourceAC(obj);
			
			AttachElectronicStationDTO fileDto = new AttachElectronicStationDTO();
			fileDto.setObjectId(obj.getDeviceId());
			fileDto.setType("TU_NGUON_AC");
			if(lsCabinetsSourceAC.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileDto);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("1")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("2")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("3")) {
							lst3.add(file);
						}
					}
				}
				lsCabinetsSourceAC.get(0).setListImage1(lst1);
				lsCabinetsSourceAC.get(0).setListImage2(lst2);
				lsCabinetsSourceAC.get(0).setListImage3(lst3);
				lsCabinetsSourceAC.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lsCabinetsSourceAC);
		    break; 
		case "TU_NGUON_DC":
			List<CabinetsSourceDCDTO> lsCabinetsSourceDC = manageMEDAO.getCabinetsSourceDC(obj);
			AttachElectronicStationDTO fileDtoDC = new AttachElectronicStationDTO();
			fileDtoDC.setObjectId(obj.getDeviceId());
			fileDtoDC.setType("TU_NGUON_DC");
			if(lsCabinetsSourceDC.size() > 0) {
				List<AttachElectronicStationDTO> fileDC = utilAttachDocumentDAO.getAttachFileImageNew(fileDtoDC);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst4 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst5 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst6 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst7 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst8 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst9 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst10 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst11 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst12 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst13 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst14 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst15 = new ArrayList<AttachElectronicStationDTO>();
				
				for(AttachElectronicStationDTO file : fileDC) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("1")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("2")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("3")) {
							lst3.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("5")) {
							lst4.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("6")) {
							lst5.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("7")) {
							lst6.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("8")) {
							lst7.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("9")) {
							lst8.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("10")) {
							lst9.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("11")) {
							lst10.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("12")) {
							lst11.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("13")) {
							lst12.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("14")) {
							lst13.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("15")) {
							lst14.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("16")) {
							lst15.add(file);
						}
					}
				}
				lsCabinetsSourceDC.get(0).setListImage1(lst1);
				lsCabinetsSourceDC.get(0).setListImage2(lst2);
				lsCabinetsSourceDC.get(0).setListImage3(lst3);
				lsCabinetsSourceDC.get(0).setListImage4(lst4);
				lsCabinetsSourceDC.get(0).setListImage5(lst5);
				lsCabinetsSourceDC.get(0).setListImage6(lst6);
				lsCabinetsSourceDC.get(0).setListImage7(lst7);
				lsCabinetsSourceDC.get(0).setListImage8(lst8);
				lsCabinetsSourceDC.get(0).setListImage9(lst9);
				lsCabinetsSourceDC.get(0).setListImage10(lst10);
				lsCabinetsSourceDC.get(0).setListImage11(lst11);
				lsCabinetsSourceDC.get(0).setListImage12(lst12);
				lsCabinetsSourceDC.get(0).setListImage13(lst13);
				lsCabinetsSourceDC.get(0).setListImage14(lst14);
				lsCabinetsSourceDC.get(0).setListImage15(lst15);
				lsCabinetsSourceDC.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lsCabinetsSourceDC);
		    break; 
		case "DIEU_HOA_AC":
			List<ElectricAirConditioningACDTO> lsDHAC = manageMEDAO.getElectricAirConditioningAC(obj);
			AttachElectronicStationDTO fileDHAC = new AttachElectronicStationDTO();
			fileDHAC.setObjectId(obj.getDeviceId());
			fileDHAC.setType("DIEU_HOA_AC");
			if(lsDHAC.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileDHAC);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst4 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst5 = new ArrayList<AttachElectronicStationDTO>();
				
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("2")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("3")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("9")) {
							lst3.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("11")) {
							lst4.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("12")) {
							lst5.add(file);
						}
					}
				}
				lsDHAC.get(0).setListImage1(lst1);
				lsDHAC.get(0).setListImage2(lst2);
				lsDHAC.get(0).setListImage3(lst3);
				lsDHAC.get(0).setListImage4(lst4);
				lsDHAC.get(0).setListImage5(lst1);
				lsDHAC.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lsDHAC);
		    break;
		case "NHIET":
			List<ElectricHeatExchangerDTO> lsNHIET = manageMEDAO.getNHIET(obj);
			AttachElectronicStationDTO fileNhiet = new AttachElectronicStationDTO();
			fileNhiet.setObjectId(obj.getDeviceId());
			fileNhiet.setType("NHIET");
			if(lsNHIET.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileNhiet);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("1")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("2")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("3")) {
							lst3.add(file);
						}
					}
				}
				lsNHIET.get(0).setListImage1(lst1);
				lsNHIET.get(0).setListImage2(lst2);
				lsNHIET.get(0).setListImage3(lst3);
				lsNHIET.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lsNHIET);
		    break;
		case "THONG_GIO":
			List<ElectricNotificationFilterDustDTO> lstTHONGGIO = manageMEDAO.getTHONGGIO(obj);
			AttachElectronicStationDTO fileTG = new AttachElectronicStationDTO();
			fileTG.setObjectId(obj.getDeviceId());
			fileTG.setType("THONG_GIO");
			if(lstTHONGGIO.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileTG);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("9")) {
							lst1.add(file);
						}
					}
				}
				lstTHONGGIO.get(0).setListImage1(lst1);
				lstTHONGGIO.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lstTHONGGIO);
		    break;
		case "DIEU_HOA_DC":
			List<ElectricAirConditioningDCDTO> lstDHDC = manageMEDAO.getDHDC(obj);
			AttachElectronicStationDTO fileDHDC = new AttachElectronicStationDTO();
			fileDHDC.setObjectId(obj.getDeviceId());
			fileDHDC.setType("DIEU_HOA_DC");
			if(lstDHDC.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileDHDC);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("1")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("3")) {
							lst2.add(file);
						}
					}
				}
				lstDHDC.get(0).setListImage1(lst1);
				lstDHDC.get(0).setListImage2(lst2);
				lstDHDC.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lstDHDC);
		    break;
		case "MAY_PHAT":
			List<GeneratorDTO> lstMayPhat = manageMEDAO.getMayPhat(obj);
			AttachElectronicStationDTO fileMP = new AttachElectronicStationDTO();
			fileMP.setObjectId(obj.getDeviceId());
			fileMP.setType("MAY_PHAT");
			if(lstMayPhat.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileMP);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("8")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("9")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("10")) {
							lst3.add(file);
						}
					}
				}
				lstMayPhat.get(0).setListImage1(lst1);
				lstMayPhat.get(0).setListImage2(lst2);
				lstMayPhat.get(0).setListImage3(lst3);
				lstMayPhat.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lstMayPhat);
		    break;
		case "AC_QUY":
			List<BatteryDTO> lstAcquy = manageMEDAO.getAcquy(obj);
			AttachElectronicStationDTO fileAQ = new AttachElectronicStationDTO();
			fileAQ.setObjectId(obj.getDeviceId());
			fileAQ.setType("AC_QUY");
			if(lstAcquy.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileAQ);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst4 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("1")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("2")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("7")) {
							lst3.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("8")) {
							lst4.add(file);
						}
					}
				}
				lstAcquy.get(0).setListImage1(lst1);
				lstAcquy.get(0).setListImage2(lst2);
				lstAcquy.get(0).setListImage3(lst3);
				lstAcquy.get(0).setListImage4(lst4);
				lstAcquy.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lstAcquy);
		    break;
		case "CUU_HOA":
			List<ElectricFireExtinguisherDTO> lstCuuHoa = manageMEDAO.getCuuHoa(obj);
			AttachElectronicStationDTO fileCH = new AttachElectronicStationDTO();
			fileCH.setObjectId(obj.getDeviceId());
			fileCH.setType("CUU_HOA");
			if(lstCuuHoa.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileCH);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst4 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("1")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("2")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("3")) {
							lst3.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("4")) {
							lst4.add(file);
						}
					}
				}
				lstCuuHoa.get(0).setListImage1(lst1);
				lstCuuHoa.get(0).setListImage2(lst2);
				lstCuuHoa.get(0).setListImage3(lst3);
				lstCuuHoa.get(0).setListImage4(lst4);
				lstCuuHoa.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lstCuuHoa);
		    break;
		case "CANH_BAO":
			List<ElectricWarningSystemDTO> lstCanhBao = manageMEDAO.getCanhBao(obj);
			AttachElectronicStationDTO fileCB = new AttachElectronicStationDTO();
			fileCB.setObjectId(obj.getDeviceId());
			fileCB.setType("CANH_BAO");
			if(lstCanhBao.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileCB);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst4 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst5 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst6 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst7 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst8 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst9 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst10 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst11 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst12 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst13 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst14 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst15 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst16 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("1")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("2")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("3")) {
							lst3.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("4")) {
							lst4.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("5")) {
							lst5.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("6")) {
							lst6.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("7")) {
							lst7.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("8")) {
							lst8.add(file);
						}if(file.getAppParamCode()!=null && file.getAppParamCode().equals("9")) {
							lst9.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("10")) {
							lst10.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("11")) {
							lst11.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("12")) {
							lst12.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("13")) {
							lst13.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("14")) {
							lst14.add(file);
						}if(file.getAppParamCode()!=null && file.getAppParamCode().equals("15")) {
							lst15.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("16")) {
							lst16.add(file);
						}
					}
				}
				lstCanhBao.get(0).setListImage1(lst1);
				lstCanhBao.get(0).setListImage2(lst2);
				lstCanhBao.get(0).setListImage3(lst3);
				lstCanhBao.get(0).setListImage4(lst4);
				lstCanhBao.get(0).setListImage5(lst5);
				lstCanhBao.get(0).setListImage6(lst6);
				lstCanhBao.get(0).setListImage7(lst7);
				lstCanhBao.get(0).setListImage8(lst8);
				lstCanhBao.get(0).setListImage9(lst9);
				lstCanhBao.get(0).setListImage10(lst10);
				lstCanhBao.get(0).setListImage11(lst11);
				lstCanhBao.get(0).setListImage12(lst12);
				lstCanhBao.get(0).setListImage13(lst13);
				lstCanhBao.get(0).setListImage14(lst14);
				lstCanhBao.get(0).setListImage15(lst15);
				lstCanhBao.get(0).setListImage15(lst16);
				lstCanhBao.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lstCanhBao);
		    break;
		case "ATS":
			List<ElectricATSDTO> lstATS = manageMEDAO.getATS(obj);
			AttachElectronicStationDTO fileATS = new AttachElectronicStationDTO();
			fileATS.setObjectId(obj.getDeviceId());
			fileATS.setType("ATS");
			if(lstATS.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileATS);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("7")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("8")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("9")) {
							lst3.add(file);
						}
					}
				}
				lstATS.get(0).setListImage1(lst1);
				lstATS.get(0).setListImage2(lst2);
				lstATS.get(0).setListImage3(lst3);
				lstATS.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lstATS);
		    break;
		case "MAY_NO":
			List<ElectricExplosionFactoryDTO> lstNMN = manageMEDAO.getNMN(obj);
			AttachElectronicStationDTO fileNMN = new AttachElectronicStationDTO();
			fileNMN.setObjectId(obj.getDeviceId());
			fileNMN.setType("MAY_NO");
			if(lstNMN.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileNMN);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("1")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("3")) {
							lst2.add(file);
						}
					}
				}
				lstNMN.get(0).setListImage1(lst1);
				lstNMN.get(0).setListImage2(lst2);
				lstNMN.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lstNMN);
		    break;
		case "LOC_SET":
			List<ElectricLightningCutFilterDTO> lstLocSet = manageMEDAO.getLocSet(obj);
			AttachElectronicStationDTO fileLS = new AttachElectronicStationDTO();
			fileLS.setObjectId(obj.getDeviceId());
			fileLS.setType("LOC_SET");
			if(lstLocSet.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileLS);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst4 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst5 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst6 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst7 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst8 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst9 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst10 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst11 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("2")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("3")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("4")) {
							lst3.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("5")) {
							lst4.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("6")) {
							lst5.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("7")) {
							lst6.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("8")) {
							lst7.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("9")) {
							lst8.add(file);
						}if(file.getAppParamCode()!=null && file.getAppParamCode().equals("10")) {
							lst9.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("11")) {
							lst10.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("12")) {
							lst11.add(file);
						}
					}
				}
				lstLocSet.get(0).setListImage1(lst1);
				lstLocSet.get(0).setListImage2(lst2);
				lstLocSet.get(0).setListImage3(lst3);
				lstLocSet.get(0).setListImage4(lst4);
				lstLocSet.get(0).setListImage5(lst5);
				lstLocSet.get(0).setListImage6(lst6);
				lstLocSet.get(0).setListImage7(lst7);
				lstLocSet.get(0).setListImage8(lst8);
				lstLocSet.get(0).setListImage9(lst9);
				lstLocSet.get(0).setListImage10(lst10);
				lstLocSet.get(0).setListImage11(lst11);
				lstLocSet.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lstLocSet);
		    break;
		case "TIEP_DIA":
			List<ElectricEarthingSystemDTO> lstTD = manageMEDAO.getTD(obj);
			AttachElectronicStationDTO fileTD = new AttachElectronicStationDTO();
			fileTD.setObjectId(obj.getDeviceId());
			fileTD.setType("TIEP_DIA");
			if(lstTD.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileTD);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("1")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("3")) {
							lst2.add(file);
						}
					}
				}
				lstTD.get(0).setListImage1(lst1);
				lstTD.get(0).setListImage2(lst2);
				lstTD.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lstTD);
		    break;
		case "RECTIFITER":
			List<ElectricRectifierDTO> lstR = manageMEDAO.getR(obj);
			AttachElectronicStationDTO fileR = new AttachElectronicStationDTO();
			fileR.setObjectId(obj.getDeviceId());
			fileR.setType("RECTIFITER");
			if(lstR.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileR);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("7")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("9")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("11")) {
							lst3.add(file);
						}
					}
				}
				lstR.get(0).setListImage1(lst1);
				lstR.get(0).setListImage2(lst2);
				lstR.get(0).setListImage3(lst3);
				lstR.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lstR);
		    break;
		case "NHA_TRAM":
			List<StationInformationDTO> lstNT = manageMEDAO.getNhaTram(obj);
			AttachElectronicStationDTO fileNT = new AttachElectronicStationDTO();
			fileNT.setObjectId(obj.getDeviceId());
			fileNT.setType("NHA_TRAM");
			if(lstNT.size() > 0) {
				List<AttachElectronicStationDTO> fileDTO = utilAttachDocumentDAO.getAttachFileImageNew(fileNT);
				List<AttachElectronicStationDTO> lst1 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst2 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst3 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst4 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst5 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst6 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst7 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst8 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst9 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst10 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst11 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst12 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst13 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst14 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst15 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst16 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst17 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst18 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst19 = new ArrayList<AttachElectronicStationDTO>();
				List<AttachElectronicStationDTO> lst20 = new ArrayList<AttachElectronicStationDTO>();
				
				for(AttachElectronicStationDTO file : fileDTO) {
					file.setBase64String(
							ImageUtil.convertImageToBase64(folder2Upload + file.getFilePath()));
					listImageMobile.add(file);
					if(!isMobile) {
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("12")) {
							lst1.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("13")) {
							lst2.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("14")) {
							lst3.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("15")) {
							lst4.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("16")) {
							lst5.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("17")) {
							lst6.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("18")) {
							lst7.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("19")) {
							lst8.add(file);
						}if(file.getAppParamCode()!=null && file.getAppParamCode().equals("20")) {
							lst9.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("21")) {
							lst10.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("22")) {
							lst11.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("23")) {
							lst12.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("24")) {
							lst13.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("25")) {
							lst14.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("26")) {
							lst15.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("27")) {
							lst16.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("28")) {
							lst17.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("29")) {
							lst18.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("30")) {
							lst19.add(file);
						}
						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("31")) {
							lst20.add(file);
						}
//						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("19")) {
//							lst19.add(file);
//						}
//						if(file.getAppParamCode()!=null && file.getAppParamCode().equals("20")) {
//							lst20.add(file);
//						}
					}
				}
				lstNT.get(0).setListImage1(lst1);
				lstNT.get(0).setListImage2(lst2);
				lstNT.get(0).setListImage3(lst3);
				lstNT.get(0).setListImage4(lst4);
				lstNT.get(0).setListImage5(lst5);
				lstNT.get(0).setListImage6(lst6);
				lstNT.get(0).setListImage7(lst7);
				lstNT.get(0).setListImage8(lst8);
				lstNT.get(0).setListImage9(lst9);
				lstNT.get(0).setListImage10(lst10);
				lstNT.get(0).setListImage11(lst11);
				lstNT.get(0).setListImage12(lst12);
				lstNT.get(0).setListImage13(lst13);
				lstNT.get(0).setListImage14(lst14);
				lstNT.get(0).setListImage15(lst15);
				lstNT.get(0).setListImage16(lst16);
				lstNT.get(0).setListImage17(lst17);
				lstNT.get(0).setListImage18(lst18);
				lstNT.get(0).setListImage19(lst19);
				lstNT.get(0).setListImage20(lst20);
				lstNT.get(0).setListFileAttach(listImageMobile);
			}
			data.setData(lstNT);
		    break;
		    
		default:     
		}
		data.setStart(1);
		return data;
	}

	public void approve(DeviceStationElectricalDTO obj, Long userId) {
		// TODO Auto-generated method stub
		manageMEDAO.approve(obj);
		manageMEDAO.sendMailApprove(obj,userId);
	}
	
	public void reject(DeviceStationElectricalDTO obj, Long userId) {
		// TODO Auto-generated method stub
		manageMEDAO.reject(obj);
		manageMEDAO.sendMailReject(obj,userId);
	}

	public String saveDevice(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		obj.setCreateDate(new Date());
		obj.setStatus("1");
		obj.setState("1");
		String result = manageMEDAO.save(obj.toModel());
        return result;
	}
	
	public String updateDevice(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		String result = manageMEDAO.update(obj.toModel());
        return result;
	}
	
	public Long save(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		return manageMEDAO.saveObject(obj.toModel());
	}
	
	public Long update(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		return manageMEDAO.updateObject(obj.toModel());
	}
	
	public DeviceStationElectricalDTO findByTypeAndSerial(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		return manageMEDAO.findByTypeAndSerial(obj);
	}
	
	public String saveDeviceLD(ElectricDetailDTO obj) {
		// TODO Auto-generated method stub
		 String result = manageMEDAO.update(obj.toModel());
	        return result;
	}
	
	public String saveDeviceTuNguonAC(CabinetsSourceACDTO obj) {
		// TODO Auto-generated method stub
		String result = manageMEDAO.update(obj.toModel());
        return result;
	}

	public String saveDeviceTuNguonDC(CabinetsSourceDCDTO obj) {
		// TODO Auto-generated method stub
		cabinetsSourceDCDAO.update(obj.toModel());
        return null;
	}
	
	public String saveDeviceNHIET(ElectricHeatExchangerDTO obj) {
		// TODO Auto-generated method stub
		manageMEDAO.update(obj.toModel());
        return null;
	}
	
	public String saveDeviceDHAC(ElectricAirConditioningACDTO obj) {
		// TODO Auto-generated method stub
		manageMEDAO.update(obj.toModel());
        return null;
	}
	
	public String saveDeviceTG(ElectricNotificationFilterDustDTO obj) {
		// TODO Auto-generated method stub
		manageMEDAO.update(obj.toModel());
        return null;
	}
	
	public String saveDeviceDHDC(ElectricAirConditioningDCDTO obj) {
		// TODO Auto-generated method stub
		manageMEDAO.update(obj.toModel());
        return null;
	}

	@Override
	public List<com.viettel.wms.dto.AppParamDTO> getAppParamByParType(String parType) {
		// TODO Auto-generated method stub
		return appParamDAO.getAppParamByParType(parType);
	}

	private void saveAttachFileStationElectric(List<AttachElectronicStationDTO> lstAttach, Long objectId, Long userId, String type, String description) {
		for (AttachElectronicStationDTO attach : lstAttach) {
			attach.setObjectId(objectId);
			attach.setType(type);
			attach.setDescription(description);
			attach.setStatus("1");
			attach.setCreatedDate(new Date());
			attach.setCreatedUserId(userId);
			attach.setName(attach.getImageName());
			InputStream inputStream = ImageUtil.convertBase64ToInputStream(attach.getBase64String());
			try {
				String imagePath = UFile.writeToFileServer(inputStream, attach.getName(), input_image_sub_folder_upload,
						folder2Upload);

				attach.setFilePath(imagePath);
				attachElectronicStationDAO.saveObject(attach.toModel());
			} catch (Exception e) {
//				e.printStackTrace();
				continue;
			}
		}
	}
	
	@Override
	public void saveDeviceDetail(StationElectricalRequest request) {
		DeviceStationElectricalDTO deviceStation = request.getDeviceStationElectricalDTO();
		String deviceType = deviceStation.getType();
		Long userId = request.getSysUserRequest().getSysUserId();
		if (Constant.DeviceType.LUOI_DIEN.equalsIgnoreCase(deviceType)) {
			ElectricDetailDTO elec = request.getDeviceStationElectricalDTO().getElectricDetailDTO();
			if(elec.getId()!=null) {
				elec.setElectricDetailId(elec.getId());
				electricDetailDAO.updateObject(elec.toModel());
			} else {
				electricDetailDAO.saveObject(elec.toModel());
			}
			
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị lưới điện. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.LUOI_DIEN, "File ảnh chi tiết thiết bị Lưới điện");
		}
		if (Constant.DeviceType.TU_NGUON_AC.equalsIgnoreCase(deviceType)) {
			CabinetsSourceACDTO elec = request.getDeviceStationElectricalDTO().getCabinetsSourceACDTO();
			if(elec.getId()!=null) {
				cabinetsSourceACDAO.updateObject(elec.toModel());
			} else {
				cabinetsSourceACDAO.saveObject(elec.toModel());
			}
			
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị tủ nguồn AC. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.TU_NGUON_AC, "File ảnh chi tiết thiết bị Tủ nguồn AC");
		}
		if (Constant.DeviceType.TU_NGUON_DC.equalsIgnoreCase(deviceType)) {
			CabinetsSourceDCDTO elec = request.getDeviceStationElectricalDTO().getCabinetsSourceDCDTO();
			if(elec.getId()!=null) {
				elec.setCabinetsDCId(elec.getId());
				cabinetsSourceDCDAO.updateObject(elec.toModel());
			} else {
				cabinetsSourceDCDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị tủ nguồn DC. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.TU_NGUON_DC, "File ảnh chi tiết thiết bị Tủ nguồn DC");
		}
		if (Constant.DeviceType.NHIET.equalsIgnoreCase(deviceType)) {
			ElectricHeatExchangerDTO elec = request.getDeviceStationElectricalDTO().getElectricHeatExchangerDTO();
			elec.setStateEHE(elec.getState());
			if(elec.getId()!=null) {
				electricHeatExchangerDAO.updateObject(elec.toModel());
			} else {
				electricHeatExchangerDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị bộ trao đổi nhiệt. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.NHIET, "File ảnh chi tiết thiết bị Bộ trao đổi nhiệt");
		}
		if (Constant.DeviceType.DIEU_HOA_AC.equalsIgnoreCase(deviceType)) {
			ElectricAirConditioningACDTO elec = request.getDeviceStationElectricalDTO()
					.getElectricAirConditioningACDTO();
			if(elec.getId()!=null) {
				electricAirConditioningACDAO.updateObject(elec.toModel());
			} else {
				electricAirConditioningACDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị điều hòa AC. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.DIEU_HOA_AC, "File ảnh chi tiết thiết bị Điều hòa Ac");
		}
		if (Constant.DeviceType.THONG_GIO.equalsIgnoreCase(deviceType)) {
			ElectricNotificationFilterDustDTO elec = request.getDeviceStationElectricalDTO().getElectricNotificationFilterDustDTO();
			if(elec.getId()!=null) {
				electricNotificationFilterDustDAO.updateObject(elec.toModel());
			} else {
				electricNotificationFilterDustDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị thông gió lọc bụi. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.THONG_GIO, "File ảnh chi tiết thiết bị Thông gió lọc bụi");
		}
		if (Constant.DeviceType.DIEU_HOA_DC.equalsIgnoreCase(deviceType)) {
			ElectricAirConditioningDCDTO elec = request.getDeviceStationElectricalDTO().getElectricAirConditioningDCDTO();
			if(elec.getId()!=null) {
				electricAirConditioningDCDAO.updateObject(elec.toModel());
			} else {
				electricAirConditioningDCDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị điều hòa DC. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.DIEU_HOA_DC, "File ảnh chi tiết thiết bị Điều hòa DC");
		}
		if (Constant.DeviceType.MAY_PHAT.equalsIgnoreCase(deviceType)) {
			GeneratorDTO elec = request.getDeviceStationElectricalDTO().getGeneratorDTO();
			if(elec.getId()!=null) {
				generatorDAO.updateObject(elec.toModel());
			} else {
				generatorDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị máy phát điện. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.MAY_PHAT, "File ảnh chi tiết thiết bị Máy phát điện");
		}
		if (Constant.DeviceType.AC_QUY.equalsIgnoreCase(deviceType)) {
			BatteryDTO elec = request.getDeviceStationElectricalDTO().getBatteryDTO();
			if(elec.getId()!=null) {
				batteryDAO.updateObject(elec.toModel());
			} else {
				batteryDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị acquy. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.AC_QUY, "File ảnh chi tiết thiết bị Ắc quy");
		}
		if (Constant.DeviceType.CUU_HOA.equalsIgnoreCase(deviceType)) {
			ElectricFireExtinguisherDTO elec = request.getDeviceStationElectricalDTO().getElectricFireExtinguisherDTO();
			if(elec.getId()!=null) {
				electricFireExtinguisherDAO.updateObject(elec.toModel());
			} else {
				electricFireExtinguisherDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị bình cứu hỏa. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.CUU_HOA, "File ảnh chi tiết thiết bị Bình cứu hỏa");
		}
		if (Constant.DeviceType.CANH_BAO.equalsIgnoreCase(deviceType)) {
			ElectricWarningSystemDTO elec = request.getDeviceStationElectricalDTO().getElectricWarningSystemDTO();
			if(elec.getId()!=null) {
				electricWarningSystemDAO.updateObject(elec.toModel());
			} else {
				electricWarningSystemDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị hệ thống cảnh báo. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.CANH_BAO, "File ảnh chi tiết thiết bị Hệ thống cảnh bảo");
		}
		if (Constant.DeviceType.ATS.equalsIgnoreCase(deviceType)) {
			ElectricATSDTO elec = request.getDeviceStationElectricalDTO().getElectricATSDTO();
			if(elec.getId()!=null) {
				electricATSDAO.updateObject(elec.toModel());
			} else {
				electricATSDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị ATS/ATS Timer/GSĐK. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.ATS, "File ảnh chi tiết thiết bị ATS/ATS Timer/GSĐK");
		}
		if (Constant.DeviceType.MAY_NO.equalsIgnoreCase(deviceType)) {
			ElectricExplosionFactoryDTO elec = request.getDeviceStationElectricalDTO().getElectricExplosionFactoryDTO();
			if(elec.getId()!=null) {
				electricExplosionFactoryDAO.updateObject(elec.toModel());
			} else {
				electricExplosionFactoryDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị nhà máy nổ. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.MAY_NO, "File ảnh chi tiết thiết bị Nhà máy nổ");
		}
		if (Constant.DeviceType.LOC_SET.equalsIgnoreCase(deviceType)) {
			ElectricLightningCutFilterDTO elec = request.getDeviceStationElectricalDTO().getElectricLightningCutFilterDTO();
			if(elec.getId()!=null) {
				electricLightningCutFilterDAO.updateObject(elec.toModel());
			} else {
				electricLightningCutFilterDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị bộ lọc cắt sét. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.LOC_SET, "File ảnh chi tiết thiết bị Bộ lọc cắt sét");
		}
		if (Constant.DeviceType.TIEP_DIA.equalsIgnoreCase(deviceType)) {
			ElectricEarthingSystemDTO elec = request.getDeviceStationElectricalDTO().getElectricEarthingSystemDTO();
			if(elec.getId()!=null) {
				electricEarthingSystemDAO.updateObject(elec.toModel());
			} else {
				electricEarthingSystemDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị hệ thống tiếp địa. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.TIEP_DIA, "File ảnh chi tiết thiết bị Hệ thống tiếp địa");
		}
		if (Constant.DeviceType.RECTIFITER.equalsIgnoreCase(deviceType)) {
			ElectricRectifierDTO elec = request.getDeviceStationElectricalDTO().getElectricRectifierDTO();
			if(elec.getId()!=null) {
				electricRectifierDAO.updateObject(elec.toModel());
			} else {
				electricRectifierDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị Rectifiter. Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.RECTIFITER, "File ảnh chi tiết thiết bị Thiết bị Rectifiter");
		}
		if (Constant.DeviceType.NHA_TRAM.equalsIgnoreCase(deviceType)) {
			StationInformationDTO elec = request.getDeviceStationElectricalDTO().getStationInformationDTO();
			if(elec.getId()!=null) {
				stationInformationDAO.updateObject(elec.toModel());
			} else {
				stationInformationDAO.saveObject(elec.toModel());
			}
			electricDetailDAO.updateDeviceStation(elec.getDeviceId(), "2");
//			List<SysUserDTO> lstUser= electricDetailDAO.sendMailCD(elec.getDeviceId());
//			if(lstUser.size() > 0) {
//				for(SysUserDTO dto: lstUser) {
//					SendEmailBO mailBO = new SendEmailBO();
//					mailBO.setStatus(0l);
//					mailBO.setSubject("Phê duyệt thiết bị cơ điện");
//					mailBO.setContent("Đồng chí nhận được yêu cầu phê duyệt thiết bị" + deviceType + ". Trạm " + dto.getName() + ", phần mềm COMS");
//					mailBO.setReceiveEmail(dto.getEmail());
//					mailBO.setCreatedDate(new Date());
//					mailBO.setCreatedUserId(userId);
//					sendEmailDAO.saveObject(mailBO);
//				}
//			}
			saveAttachFileStationElectric(elec.getListFileAttach(), elec.getDeviceId(), userId,
					Constant.DeviceType.NHA_TRAM, "File ảnh chi tiết thiết bị Thông tin nhà trạm");
		}
	}

	public Long getProvinceId(Long sysGroupId) {
		// TODO Auto-generated method stub
		Long id = manageMEDAO.getProvinceId(sysGroupId);
		return id;
	}

	public List<SysUserDTO> doSearchUser(SysUserDTO obj) {
		// TODO Auto-generated method stub
		return manageMEDAO.doSearchUser(obj);
	}

	public String saveManageStation(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
        return manageMEDAO.updateManageStation(obj);
	}

	public StationElectricalDTO getInforDashboard(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		StationElectricalDTO dto = new StationElectricalDTO();
		List<StationElectricalDTO> lst = manageMEDAO.getInforDashboard(obj);
		List<StationElectricalDTO> lst2 = manageMEDAO.getInforDashboardStation(obj);
		List<StationElectricalDTO> lst3 = manageMEDAO.getInforDashboardWo(obj);
		
		if(lst.size() > 0) {
			dto = lst.get(0);
		}
		dto.setTramTrenMai(lst2.get(0).getTramTrenMai());
		dto.setTramDuoiDat(lst2.get(0).getTramDuoiDat());
		dto.setWoQuaHan(lst3.get(0).getWoQuaHan());
		dto.setWoDangThucHien(lst3.get(0).getWoDangThucHien());
		return dto;
	}

	public List<StationElectricalDTO> getStation(StationElectricalDTO obj) {
		// TODO Auto-generated method stub
		return manageMEDAO.getStation(obj);
	}

	public List<WoDTO> doSearchWo(WoDTO obj) {
		// TODO Auto-generated method stub
		return manageMEDAO.doSearchWo(obj);
	}

	public Boolean checkUserKCQTD(String employeeCode) {
		// TODO Auto-generated method stub
		return manageMEDAO.checkUserKCQTD(employeeCode);
	}
	public List<SysUserDTO> doSearchUserSysGroup(SysUserDTO obj) {
		// TODO Auto-generated method stub
		return manageMEDAO.doSearchUserSysGroup(obj);
	}

	public String exportExcel(StationElectricalDTO obj, HttpServletRequest request) throws Exception{
		// TODO Auto-generated method stub

		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "EXPORT_ELECTICAL_BY_DEVICE.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		file.close();
		Calendar cal = Calendar.getInstance();
		String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
				+ File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
				+ File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
				+ cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
				+ cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		File udir = new File(uploadPath);
		if (!udir.exists()) {
			udir.mkdirs();
		}
		OutputStream outFile = new FileOutputStream(
				udir.getAbsolutePath() + File.separator + "EXPORT_ELECTICAL_BY_DEVICE.xlsx");
		
		for(Long index: obj.getListType()) {
			if(index == 0l) {
				List<ElectricDetailDTO> data = manageMEDAO.getLuoiDienForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(0);
				setDataForSheet0(sheet,workbook,data);
			}
			else if(index == 1l) {
				List<ElectricNotificationFilterDustDTO> data = manageMEDAO.getTHONGGIOForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(1);
				 setDataForSheet1(sheet,workbook,data);
			}
			else if(index == 2l) {
				List<ElectricExplosionFactoryDTO> data = manageMEDAO.getNMNForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(2);
				 setDataForSheet2(sheet,workbook,data);
			}
			else if(index == 3l) {
				List<CabinetsSourceACDTO> data = manageMEDAO.getCabinetsSourceACForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(3);
				 setDataForSheet3(sheet,workbook,data);
			}
			else if(index == 4l) {
				List<GeneratorDTO> data = manageMEDAO.getMayPhatForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(4);
				 setDataForSheet4(sheet,workbook,data);
			}
			else if(index == 5l) {
				List<ElectricLightningCutFilterDTO> data = manageMEDAO.getLocSetForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(5);
				 setDataForSheet5(sheet,workbook,data);
			}
			else if(index == 6l) {
				List<CabinetsSourceDCDTO> data = manageMEDAO.getCabinetsSourceDCForExport(obj);;
				
				XSSFSheet sheet = workbook.getSheetAt(6);
				 setDataForSheet6(sheet,workbook,data);
			}
			else if(index == 7l) {
				List<BatteryDTO> data = manageMEDAO.getAcquyForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(7);
				 setDataForSheet7(sheet,workbook,data);
			}
			else if(index == 8l) {
				List<ElectricEarthingSystemDTO> data = manageMEDAO.getTDForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(8);
				setDataForSheet8(sheet,workbook,data);
			}
			else if(index == 9l) {
				List<ElectricAirConditioningACDTO> data = manageMEDAO.getElectricAirConditioningACForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(9);
				setDataForSheet9(sheet,workbook,data);
			}
			else if(index == 10l) {
				List<ElectricFireExtinguisherDTO> data = manageMEDAO.getCuuHoaForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(10);
				 setDataForSheet10(sheet,workbook,data);
			}
			else if(index == 11l) {
				List<ElectricRectifierDTO> data = manageMEDAO.getRForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(11);
				setDataForSheet11(sheet,workbook,data);
			}
			else if(index == 12l) {
				List<ElectricAirConditioningDCDTO> data = manageMEDAO.getDHDCforExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(12);
				setDataForSheet12(sheet,workbook,data);
			}
			else if(index == 13l) {
				List<ElectricWarningSystemDTO> data = manageMEDAO.getCanhBaoForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(13);
				setDataForSheet13(sheet,workbook,data);
			}
			else if(index == 14l) {
				List<StationInformationDTO> data = manageMEDAO.getNhaTramForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(14);
				setDataForSheet14(sheet,workbook,data);
			}
			else if(index == 15l) {
				List<ElectricHeatExchangerDTO> data = manageMEDAO.getNHIETForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(15);
				setDataForSheet15(sheet,workbook,data);
			}
			else {
				List<ElectricATSDTO> data = manageMEDAO.getATSForExport(obj);
				XSSFSheet sheet = workbook.getSheetAt(16);
				setDataForSheet16(sheet,workbook,data);
			}
			
		}
		List<Long> lstDelete = new ArrayList<>();
		Long[] lst = {16l,15l,14l,13l,12l,11l,10l,9l,8l,7l,6l,5l,4l,3l,2l,1l,0l};
		List<Long> listLong = Arrays.asList(lst);
		for(Long def: listLong){
			Boolean check = false;
			for(Long select: obj.getListType()) {
				
				if(def == select) {
					check = true;
					break;
				}
			}
			if(!check) {
				lstDelete.add(def);
			}
		}
		
		for(Long index: lstDelete) {
			workbook.removeSheetAt(Integer.parseInt(index.toString()));
		}
		
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "EXPORT_ELECTICAL_BY_DEVICE.xlsx");
		return path;
	}
	
	private void Switch() {
		// TODO Auto-generated method stub
		
	}

	public XSSFWorkbook setDataForSheet0(XSSFSheet sheet, XSSFWorkbook workbook, List<ElectricDetailDTO> data) {
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (ElectricDetailDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getProvinceName() != null) ? dto.getProvinceName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				//cell.setCellValue((dto.getElectric() != null) ? dto.getElectric() : 0d);
				if(dto.getElectric() != null) {
					switch (dto.getElectric().intValue()) {
					case 1:
						cell.setCellValue("Lưới điện 1 pha");
						break;
					case 2:
						cell.setCellValue("Lưới điện 3 pha");
						break;
					case 3:
						cell.setCellValue("Trạm không điện");
						break;
					default:
						cell.setCellValue("");
						break;
					}
				}else {
					cell.setCellValue("");
				}
				
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				//cell.setCellValue((dto.getSuppiler() != null) ? dto.getSuppiler() : "");
				if(dto.getSuppiler() != null) {
					switch (dto.getSuppiler()) {
					case "1":
						cell.setCellValue("EVN");
						break;
					case "2":
						cell.setCellValue("Ngoài EVN");
						break;
					default:
						cell.setCellValue("");
						break;
					}
				}else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getElectricQuotaCBElectricMeterA() != null) ? dto.getElectricQuotaCBElectricMeterA() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getElectricQuotaCBStationA() != null) ? dto.getElectricQuotaCBStationA() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getDistance() != null) ? dto.getDistance() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getRateCapacityStation() != null) ? dto.getRateCapacityStation() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(9, CellType.STRING);
				//cell.setCellValue((dto.getWireType() != null) ? dto.getWireType() : 0d);
				if(dto.getWireType() != null) {
					switch (dto.getWireType().intValue()) {
					case 1:
						cell.setCellValue("Nhôm");
						break;
					case 2:
						cell.setCellValue("Đồng");
						break;
					default:
						cell.setCellValue("");
						break;
					}
				}else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				
				cell = row.createCell(10, CellType.STRING);
				//cell.setCellValue((dto.getSection() != null) ? dto.getSection() : 0d);
				if(dto.getSection() != null) {
				switch (dto.getSection().intValue()) {
				case 1:
					cell.setCellValue("2x2.5");
					break;
				case 2:
					cell.setCellValue("2x4");
					break;
				case 3:
					cell.setCellValue("2x6");
					break;
				case 4:
					cell.setCellValue("2x10");
					break;
				case 5:
					cell.setCellValue("2x16");
					break;
				case 6:
					cell.setCellValue("2x25");
					break;
				case 7:
					cell.setCellValue("2x35");
					break;
				case 8:
					cell.setCellValue("2x50");
					break;
				case 9:
					cell.setCellValue("3x10+6");
					break;
				case 10:
					cell.setCellValue("3x16+10");
					break;
				case 11:
					cell.setCellValue("3x25+16");
					break;
				case 12:
					cell.setCellValue("3x50+25");
					break;
				case 13:
					cell.setCellValue("3x70+35");
					break;
				case 14:
					cell.setCellValue("4x10");
					break;
				case 15:
					cell.setCellValue("4x11");
					break;
				case 16:
					cell.setCellValue("4x16");
					break;
				case 17:
					cell.setCellValue("4x25");
					break;
				case 18:
					cell.setCellValue("4x35");
					break;
				case 19:
					cell.setCellValue("4x50");
					break;
				case 20:
					cell.setCellValue("4x70");
					break;
				default:
					cell.setCellValue("");
					break;
				}
			}else {
				cell.setCellValue("");
			}
				cell.setCellStyle(style);
			}
		}
		return workbook;
		
	}
	
	public XSSFWorkbook setDataForSheet1(XSSFSheet sheet, XSSFWorkbook workbook, List<ElectricNotificationFilterDustDTO> data) {
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (ElectricNotificationFilterDustDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getDeviceName() != null) ? dto.getDeviceName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getManufacturer() != null) ? dto.getManufacturer() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getMaximumCapacity() != null) ? dto.getMaximumCapacity() : 0l);
				cell.setCellStyle(style);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getGoodCode() != null) ? dto.getGoodCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getGoodName() != null) ? dto.getGoodName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getStateKtts() != null) ? dto.getStateKtts() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getSerial() != null) ? dto.getSerial() : "");
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(12, CellType.STRING);
				String stateStr = "";
				if(dto.getStateENFD() != null) {
					if(dto.getStateENFD() == 1) {
						stateStr = "Tốt";
					}else {
						stateStr = "Hỏng";
					}
				}
				cell.setCellValue(stateStr);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getTimeIntoUse() != null) ? dto.getTimeIntoUse() : null );
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getLastMaintenanceTime() != null) ? dto.getLastMaintenanceTime() : null );
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getNearestRepairTime() != null) ? dto.getNearestRepairTime() : null );
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getTotalRepairCost() != null) ? dto.getTotalRepairCost() : 0l);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getTotalNumberFailures() != null) ? dto.getTotalNumberFailures() : 0l);
				cell.setCellStyle(styleNumber);
			}
			
//			query.addScalar("totalRepairCost", new LongType());
//			query.addScalar("totalNumberFailures", new LongType());
		}
		return workbook;
		
	}
	
	private XSSFWorkbook setDataForSheet2(XSSFSheet sheet, XSSFWorkbook workbook, List<ElectricExplosionFactoryDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (ElectricExplosionFactoryDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				String houseType = "";
				if(dto.getHouseType() != null) {
					switch (dto.getHouseType()) {
					case "1":
						houseType = "Bệ bê tông";
						break;
					case "2":
						houseType = "Cabin";
						break;
					case "3":
						houseType = "Nhà cải tạo";
						break;
					case "4":
						houseType = "Nhà B40";
						break;
					case "5":
						houseType = "Nhà thiết kế chung với trạm";
						break;
					case "6":
						houseType = "Nhà vượt lũ";
						break;
					case "7":
						houseType = "Xây độc lập";
						break;
					case "8":
						houseType = "Nhà trạm";
						break;
					default:
						houseType = "";
						break;
					}
				}
				cell.setCellValue(houseType);
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				String status = "";
				if(dto.getIgniterSettingStatus() != null) {
					switch (dto.getIgniterSettingStatus().toString()) {
					case "1":
						status = "Không đặt máy nổ (do NMN trên đồi cao, sông nước)";
						break;
					case "2":
						status = "Không đặt nhà máy nổ ( do hạ tầng NMN không đảm bảo)";
						break;
					case "3":
						status = "Không đặt nhà máy nổ ( do không có nhà máy nổ)";
						break;
					case "4":
						status = "Không đặt nhà máy nổ ( do không  đảm bảo an ninh)";
						break;
					case "5":
						status = "Không đặt nhà máy nổ do nguyên nhân khác";
						break;

					default:
						status = "Đã đặt nhà máy nổ";
						break;
					}
				}
				cell.setCellValue(status);
				cell.setCellStyle(style);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getTimeIntoUse() != null) ? dto.getTimeIntoUse() : null);
				cell.setCellStyle(styleCenter);
				
			}
		}
		return workbook;
		
	
	}
	
	private XSSFWorkbook setDataForSheet3(XSSFSheet sheet, XSSFWorkbook workbook, List<CabinetsSourceACDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (CabinetsSourceACDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				String cabinetsSourceName = "";
				if(dto.getCabinetsSourceName() != null) {
					switch (dto.getCabinetsSourceName()) {
					case "1":
						cabinetsSourceName = "AC box-1P";
						break;
					case "2":
						cabinetsSourceName = "AC box-2P";
						break;
					case "3":
						cabinetsSourceName = "AC box-3P";
						break;
					case "4":
						cabinetsSourceName = "Cầu dao đảo-1P";
						break;
					case "5":
						cabinetsSourceName = "Cầu dao đảo-3P";
						break;
					case "6":
						cabinetsSourceName = "Tủ outdoor-1P";
						break;
					case "7":
						cabinetsSourceName = "Tủ outdoor-3P";
						break;
					case "8":
						cabinetsSourceName = "V1-1P";
						break;
					case "9":
						cabinetsSourceName = "V1-3P";
						break;
					case "10":
						cabinetsSourceName = "V2-1P";
						break;
					case "11":
						cabinetsSourceName = "V2-3P";
						break;
					case "12":
						cabinetsSourceName = "V3-1P";
						break;
					case "13":
						cabinetsSourceName = "V3-3P";
						break;
					case "14":
						cabinetsSourceName = "V5-1P";
						break;
					case "15":
						cabinetsSourceName = "V5-3P";
						break;
					case "16":
						cabinetsSourceName = "Khác";
						break;
					default:
						cabinetsSourceName = "";
						break;
					}
				}
				cell.setCellValue(cabinetsSourceName);
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getPhaseNumber() != null) ? (dto.getPhaseNumber() == 1 ? "1P" : "3P") : "");
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getStatus() != null) ? (dto.getStatus() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
			}
		}
		return workbook;
		
	
	}
	
	private XSSFWorkbook setDataForSheet4(XSSFSheet sheet, XSSFWorkbook workbook, List<GeneratorDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (GeneratorDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getGeneratorName() != null) ? dto.getGeneratorName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getManufactuber() != null) ? dto.getManufactuber() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getWattageMax() != null) ? dto.getWattageMax() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getRatedPower() != null) ? dto.getRatedPower() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getFuelType() != null) ? dto.getFuelType() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getSerial() != null) ? dto.getSerial() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getWorkstationStatus() != null) ? (dto.getWorkstationStatus() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getDistanceGeneratorStation() != null) ? dto.getDistanceGeneratorStation() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getStatus() != null) ? (dto.getStatus() == 1 ? "Cố định" : "Lưu động") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getTimeIntoUse() != null) ? dto.getTimeIntoUse() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getTotalRunningTime() != null) ? dto.getTotalRunningTime() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getTimeLastMaintenance() != null) ? dto.getTimeLastMaintenance() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getLastRepairTime() != null) ? dto.getLastRepairTime() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getTotalRepairCost() != null) ? dto.getTotalRepairCost() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getStationCodeBeforeTransfer() != null) ? dto.getStationCodeBeforeTransfer() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue((dto.getTotalNumberFailures() != null) ? dto.getTotalNumberFailures() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue((dto.getGeneratorOverCapacity() != null) ? dto.getGeneratorOverCapacity() : "");
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue((dto.getFtConfirmGeneratorOverCapacity() != null) ? (dto.getStatus() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
			}
		}
		return workbook;
		
	
	}
	
	private XSSFWorkbook setDataForSheet5(XSSFSheet sheet, XSSFWorkbook workbook, List<ElectricLightningCutFilterDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 7;
			for (ElectricLightningCutFilterDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-7));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getElectricLightningCutFilterName() != null) ? dto.getElectricLightningCutFilterName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getPrimaryStatus() != null) ? (dto.getPrimaryStatus() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(7, CellType.STRING);
				String primarySpecies = "";
				if(dto.getPrimarySpecies() != null) {
					switch (dto.getPrimarySpecies()) {
					case "1":
						primarySpecies="GZ220";
						break;
					case "2":
						primarySpecies="GZ250";
						break;
					case "3":
						primarySpecies="GZ50";
						break;
					case "4":
						primarySpecies="GZ500";
						break;
					case "5":
						primarySpecies="SPARK_GAP_DEHN";
						break;
					case "6":
						primarySpecies="SPARK_GAP_POUSURGE";
						break;
					case "7":
						primarySpecies="SPARK_GAP_POUSURGE";
						break;
					case "8":
						primarySpecies="Khác";
						break;
					default:
						break;
					}
				}
				cell.setCellValue(primarySpecies);
				cell.setCellStyle(style);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getPrimaryQuantity() != null) ? dto.getPrimaryQuantity() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getPrimaryCondition() != null) ? (dto.getPrimaryCondition() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getResistor() != null) ? dto.getResistor() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getSecondaryStatus() != null) ? (dto.getSecondaryStatus() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(12, CellType.STRING);
				String secondarySpecies = "";
				if(dto.getSecondarySpecies() != null) {
					switch (dto.getPrimarySpecies()) {
					case "1":
						secondarySpecies="M3C85";
						break;
					case "2":
						secondarySpecies="MH2200";
						break;
					case "3":
						secondarySpecies="Khác";
						break;
					default:
						secondarySpecies="";
						break;
					}
				}
				cell.setCellValue(secondarySpecies);
				cell.setCellStyle(style);
				
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getSecondaryQuantity() != null) ? dto.getSecondaryQuantity() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getSecondaryCodition() != null) ? (dto.getSecondaryCodition() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getOtherLightningCutFilterName() != null) ? dto.getOtherLightningCutFilterName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getOtherLightningCutFilterStatus() != null) ? (dto.getOtherLightningCutFilterStatus() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getTimeIntoUse() != null) ? dto.getTimeIntoUse() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getLastMaintenanceTime() != null) ? dto.getLastMaintenanceTime() : null);
				cell.setCellStyle(styleCenter);
			}
		}
		return workbook;
		
	
	}
	
	private XSSFWorkbook setDataForSheet6(XSSFSheet sheet, XSSFWorkbook workbook, List<CabinetsSourceDCDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 7;
			for (CabinetsSourceDCDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-7));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getCabinetsSourceDCName() != null) ? (dto.getCabinetsSourceDCName().equals("1") ? "DC1" : "DC2") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getStateCabinetsSourceDC() != null) ? (dto.getStateCabinetsSourceDC() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getNotChargeTheBattery() != null) ? dto.getNotChargeTheBattery() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getChargeTheBattery() != null) ? dto.getChargeTheBattery() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getCbNumberLessThan30AUnused() != null) ? dto.getCbNumberLessThan30AUnused() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getCbNumberGreaterThan30AUnused() != null) ? dto.getCbNumberGreaterThan30AUnused() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getCbNymberAddition() != null) ? dto.getCbNymberAddition() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getQuantityUse() != null) ? dto.getQuantityUse() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getQuantityAddition() != null) ? dto.getQuantityAddition() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getRecfiterNumber() != null) ? dto.getRecfiterNumber() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getStateRectifer() != null) ? (dto.getStateRectifer() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getSerial() != null) ? dto.getSerial() : "");
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getNumberDeviceModel() != null) ? dto.getNumberDeviceModel() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getStateModule() != null) ? (dto.getStateModule() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getPowerCabinetMonitoring() != null) ? (dto.getPowerCabinetMonitoring() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
			}
		}
		return workbook;
		
	
	}
	
	private XSSFWorkbook setDataForSheet7(XSSFSheet sheet, XSSFWorkbook workbook, List<BatteryDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (BatteryDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				///
				cell = row.createCell(5, CellType.STRING);
				if(dto.getBatteryName() != null) {
					switch (dto.getBatteryName()) {
					case "1":
						cell.setCellValue("ACCU1");
						break;
					case "2":
						cell.setCellValue("ACCU2");
						break;
					case "3":
						cell.setCellValue("ACCU3");
						break;
					case "4":
						cell.setCellValue("ACCCU4");
						break;
					default:
						cell.setCellValue("");
						break;
					}
				}else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				

				cell = row.createCell(6, CellType.STRING);
				if(dto.getGoodCode() != null) {
					switch (dto.getGoodCode()) {
					case "1":
						cell.setCellValue("Lithium_ZTT_4850_P_82018");
						break;
					case "2":
						cell.setCellValue("Lithium_ZTT_4850");
						break;
					default:
						cell.setCellValue("");
						break;
					}
				}else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getManufactuber() != null) ? dto.getManufactuber() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getModel() != null) ? dto.getModel() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(9, CellType.STRING);
				if(dto.getProductionTechnology() != null) {
					switch (dto.getProductionTechnology()) {
					case "1":
						cell.setCellValue("Chì");
						break;
					case "2":
						cell.setCellValue("Lithium");
						break;
					default:
						cell.setCellValue("");
						break;
					}
				}else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getCapacity() != null) ? dto.getCapacity() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getTimeIntoUse() != null) ? dto.getTimeIntoUse() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getLastMaintenanceTime() != null) ? dto.getLastMaintenanceTime() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getStationOutputTimeAfterRecover() != null) ? dto.getStationOutputTimeAfterRecover() : null);
				cell.setCellStyle(styleCenter);
				
			}
		}
		return workbook;
	}
	
	private XSSFWorkbook setDataForSheet8(XSSFSheet sheet, XSSFWorkbook workbook, List<ElectricEarthingSystemDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (ElectricEarthingSystemDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getElectricEarthingSystemName() != null) ? dto.getElectricEarthingSystemName() : "");
				cell.setCellStyle(style);
				
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getGroundResistance() != null) ? dto.getGroundResistance() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(7, CellType.STRING);
				String status = "";
				if(dto.getGroundStatus() != null) {
					switch(dto.getGroundStatus().toString()) {
					case "1":
						status = "Bị hỏng, đứt gãy tiếp địa ";
						break;
					case "2":
						status = "Bị mất trộm dây tiếp địa";
						break;
					case "3":
						status = "Trạm không có bãi tiếp địa";
						break;
					default: 
						status = "Trạm tiếp địa đảm bảo";
						break;
					}
				}
				cell.setCellValue(status);
				cell.setCellStyle(style);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getLastMaintenanceTime() != null) ? dto.getLastMaintenanceTime() : null);
				cell.setCellStyle(styleCenter);
				
			}
		}
		return workbook;
	}
	
	private XSSFWorkbook setDataForSheet9(XSSFSheet sheet, XSSFWorkbook workbook, List<ElectricAirConditioningACDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 7;
			for (ElectricAirConditioningACDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getElectricAirConditioningACName() != null) ? dto.getElectricAirConditioningACName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getWattageDHBTU() != null) ? dto.getWattageDHBTU() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getWattageElictronic() != null) ? dto.getWattageElictronic() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getTypeCodeUnit() != null) ? (dto.getTypeCodeUnit() == 1 ? "Inverter" : "Thường") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getSeriNL() != null) ? dto.getSeriNL() : "");
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getGoodName() != null) ? dto.getGoodName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getGoodCode() != null) ? dto.getGoodCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getGoodCodeKTTS() != null) ? dto.getGoodCodeKTTS() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getTimeIntoUse() != null) ? dto.getTimeIntoUse() :null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getModelColdUnit() != null) ? dto.getModelColdUnit() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getManufacturerColdUnit() != null) ? dto.getManufacturerColdUnit() : "");
				cell.setCellStyle(style);
				
				
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getModelHotUnit() != null) ? dto.getModelHotUnit() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getManufacturerHotUnit() != null) ? dto.getManufacturerHotUnit() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getStateAirConditioning() != null) ? (dto.getStateAirConditioning() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getTypeOfGas() != null) ? dto.getTypeOfGas() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getLastMajorMaintenanceTime() != null) ? dto.getLastMajorMaintenanceTime() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue((dto.getMaxFixTme() != null) ? dto.getMaxFixTme() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue((dto.getTotalRepairCost() != null) ? dto.getTotalRepairCost() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue((dto.getTotalNumberFailures() != null) ? dto.getTotalNumberFailures() : 0d);
				cell.setCellStyle(styleNumber);
			}
		}
		return workbook;
	}
	
	private XSSFWorkbook setDataForSheet10(XSSFSheet sheet, XSSFWorkbook workbook, List<ElectricFireExtinguisherDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (ElectricFireExtinguisherDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getElectricFireExtinguisherName() != null) ? dto.getElectricFireExtinguisherName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				String tempStr = "";
				if(dto.getElectricFireExtinguisherType() != null) {
					switch (dto.getElectricFireExtinguisherType()) {
					case "1":
						tempStr="Bình CO2 MT2";
						break;
					case "2":
						tempStr="Bình CO2 MT24";
						break;
					case "3":
						tempStr="Bình CO2 MT3";
						break;
					case "4":
						tempStr="Bình CO2 MT5";
						break;
					case "5":
						tempStr="Bình bột MFZ1";
						break;
					case "6":
						tempStr="Bình bột MFZ2";
						break;
					case "7":
						tempStr="Bình bột MFZ3";
						break;
					case "8":
						tempStr="Bình bột MFZ4";
						break;
					case "9":
						tempStr="Bình bột MFZ5";
						break;
					case "10":
						tempStr="Quả cầu chữa cháy";
						break;
					default:
						tempStr="";
						break;
					}
				}
				cell.setCellValue(tempStr);
				cell.setCellStyle(style);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getWeight() != null) ? dto.getWeight() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getElectricFireExtinguisherState() != null) ? (dto.getElectricFireExtinguisherState() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getTimeIntoUse() != null) ? dto.getTimeIntoUse() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getElectricFireExtinguisherLocation() != null) ? dto.getElectricFireExtinguisherLocation() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getLastMaintenanceTime() != null) ? dto.getLastMaintenanceTime() : null);
				cell.setCellStyle(styleCenter);
			}
		}
		return workbook;
	}
	
	private XSSFWorkbook setDataForSheet11(XSSFSheet sheet, XSSFWorkbook workbook, List<ElectricRectifierDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (ElectricRectifierDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getGoodName() != null) ? dto.getGoodName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getGoodCode() != null) ? dto.getGoodCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getSerial() != null) ? dto.getSerial() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getStateER() != null) ? (dto.getStateER() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getTimeIntoUse() != null) ? dto.getTimeIntoUse() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getManufacturer() != null) ? dto.getManufacturer() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getModel() != null) ? dto.getModel() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getRatedPower() != null) ? dto.getRatedPower() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getQuantityInUse() != null) ? dto.getQuantityInUse() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getQuantitycanAdded() != null) ? dto.getQuantitycanAdded() : 0d);
				cell.setCellStyle(styleNumber);
			}
		}
		return workbook;
	}
	
	private XSSFWorkbook setDataForSheet12(XSSFSheet sheet, XSSFWorkbook workbook, List<ElectricAirConditioningDCDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (ElectricAirConditioningDCDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getElectricAirConditioningDcName() != null) ? dto.getElectricAirConditioningDcName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getRefrigerationCapacity() != null) ? dto.getRefrigerationCapacity() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getPowerCapacity() != null) ? dto.getPowerCapacity() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getGoodName() != null) ? dto.getGoodName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getGoodCode() != null) ? dto.getGoodCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getGoodCodeKtts() != null) ? dto.getGoodCodeKtts() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getSerial() != null) ? dto.getSerial() : "");
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getModel() != null) ? dto.getModel() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getManufacturer() != null) ? dto.getManufacturer() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getStateEACD() != null) ? (dto.getStateEACD() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getTypeGas() != null) ? dto.getTypeGas() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getTimeIntoUse() != null) ? dto.getTimeIntoUse() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getLastMaintenanceTime() != null) ? dto.getLastMaintenanceTime() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getNearestRepairTime() != null) ? dto.getNearestRepairTime() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getTotalNumberFailures() != null) ? dto.getTotalNumberFailures() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getTotalRepairCost() != null) ? dto.getTotalRepairCost() : 0d);
				cell.setCellStyle(styleNumber);
			}
		}
		return workbook;
	}
	
	private XSSFWorkbook setDataForSheet13(XSSFSheet sheet, XSSFWorkbook workbook, List<ElectricWarningSystemDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (ElectricWarningSystemDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getStateACBox() != null) ? (dto.getStateACBox() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getStatusACBox() != null) ? (dto.getStatusACBox() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getStateTemperatureWarning() != null) ? (dto.getStateTemperatureWarning() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getStatusTemperatureWarning() != null) ? (dto.getStatusTemperatureWarning() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getStateSmokeWarning() != null) ? (dto.getStateSmokeWarning() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getStatusSmokeWarning() != null) ? (dto.getStatusSmokeWarning() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getStateStationOpenWarning() != null) ? (dto.getStateStationOpenWarning() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getStatusStationOpenWarning() != null) ? (dto.getStatusStationOpenWarning() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getStateExplosiveFactoryOpenWarning() != null) ? (dto.getStateExplosiveFactoryOpenWarning() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getStatusExplosiveFactoryOpenWarning() != null) ? (dto.getStatusExplosiveFactoryOpenWarning() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getStatePowerCabinetMalfuntionWarning() != null) ? (dto.getStatePowerCabinetMalfuntionWarning() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getStatusPowerCabinetMalfuntionWarning() != null) ? (dto.getStatusPowerCabinetMalfuntionWarning() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getStateLowBattery() != null) ? (dto.getStateLowBattery() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getStatusLowBattery() != null) ? (dto.getStatusLowBattery() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getStateLowAC() != null) ? (dto.getStateLowAC() == 1 ? "Có" : "Không") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getStatusLowAC() != null) ? (dto.getStatusLowAC() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
			}
		}
		return workbook;
	}
	
	private XSSFWorkbook setDataForSheet14(XSSFSheet sheet, XSSFWorkbook workbook, List<StationInformationDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 7;
			for (StationInformationDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getAddress() != null) ? dto.getAddress() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getManager() != null) ? dto.getManager() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getWattageMax() != null) ? dto.getWattageMax() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getWattageMaxAcquy() != null) ? dto.getWattageMaxAcquy() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getWattageAcquy() != null) ? dto.getWattageAcquy() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getWattageAirConditioning() != null) ? dto.getWattageAirConditioning() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getWattageDustFilter() != null) ? dto.getWattageDustFilter() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getWattageHeadExchangers() != null) ? dto.getWattageHeadExchangers() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getVtWattageTV() != null) ? dto.getVtWattageTV() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getVtWattageTransmission() != null) ? dto.getVtWattageTransmission() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getVtWattageCDBR() != null) ? dto.getVtWattageCDBR() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getVtWattageIP() != null) ? dto.getVtWattageIP() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getVtWattagePSTN() != null) ? dto.getVtWattagePSTN() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getVnpWattageTV() != null) ? dto.getVnpWattageTV() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getVnpWattageTransmission() != null) ? dto.getVnpWattageTransmission() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getVnpWattageCDBR() != null) ? dto.getVnpWattageCDBR() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue((dto.getVnpWattageIP() != null) ? dto.getVnpWattageIP() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue((dto.getVnpWattagePSTN() != null) ? dto.getVnpWattagePSTN() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue((dto.getVnmWattageTV() != null) ? dto.getVnmWattageTV() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(24, CellType.STRING);
				cell.setCellValue((dto.getVnmWattageTransmission() != null) ? dto.getVnmWattageTransmission() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(25, CellType.STRING);
				cell.setCellValue((dto.getVnmWattageCDBR() != null) ? dto.getVnmWattageCDBR() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(26, CellType.STRING);
				cell.setCellValue((dto.getVnmWattageIP() != null) ? dto.getVnmWattageIP() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(27, CellType.STRING);
				cell.setCellValue((dto.getVnmWattagePSTN() != null) ? dto.getVnmWattagePSTN() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(28, CellType.STRING);
				cell.setCellValue((dto.getMbpWattageTV() != null) ? dto.getMbpWattageTV() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(29, CellType.STRING);
				cell.setCellValue((dto.getMbpWattageTransmission() != null) ? dto.getMbpWattageTransmission() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(30, CellType.STRING);
				cell.setCellValue((dto.getMbpWattageCDBR() != null) ? dto.getMbpWattageCDBR() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(31, CellType.STRING);
				cell.setCellValue((dto.getMbpWattageIP() != null) ? dto.getMbpWattageIP() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(32, CellType.STRING);
				cell.setCellValue((dto.getMbpWattagePSTN() != null) ? dto.getMbpWattagePSTN() : 0d);
				cell.setCellStyle(styleNumber);
			}
		}
		return workbook;
	}
	
	private XSSFWorkbook setDataForSheet15(XSSFSheet sheet, XSSFWorkbook workbook, List<ElectricHeatExchangerDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (ElectricHeatExchangerDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getDeviceName() != null) ? dto.getDeviceName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getWattage() != null) ? dto.getWattage() : 0d);
				cell.setCellStyle(styleNumber);
				
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getGoodName() != null) ? dto.getGoodName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getGoodCode() != null) ? dto.getGoodCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getGoodCodeKtts() != null) ? dto.getGoodCodeKtts() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getSerial() != null) ? dto.getSerial() : "");
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getModel() != null) ? dto.getModel() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getManufacturer() != null) ? dto.getManufacturer() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getStateEHE() != null) ? (dto.getStateEHE() == "1" ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getTimeIntoUse() != null) ? dto.getTimeIntoUse() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getLastMajorMaintenanceTime() != null) ? dto.getLastMajorMaintenanceTime() : null);
				cell.setCellStyle(styleCenter);
				
			}
		}
		return workbook;
	}
	
	private XSSFWorkbook setDataForSheet16(XSSFSheet sheet, XSSFWorkbook workbook, List<ElectricATSDTO> data) {
		// TODO Auto-generated method stub

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (ElectricATSDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i-6));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getGoodCodeKTTS() != null) ? dto.getGoodCodeKTTS() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getGoodName() != null) ? dto.getGoodName() : "");
				cell.setCellStyle(style);
				
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getGoodElectricName() != null) ? dto.getGoodElectricName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getSerial() != null) ? dto.getSerial() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getStateEA() != null) ? (dto.getStateEA() == 1 ? "Tốt" : "Hỏng") : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getManufacturer() != null) ? dto.getManufacturer() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getModel() != null) ? dto.getModel() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(12, CellType.STRING);
//				cell.setCellValue((dto.getPhaseNumber() != null) ? dto.getPhaseNumber() : 0d);
				if(dto.getPhaseNumber() != null) {
					cell.setCellValue( dto.getPhaseNumber());
				}else {
					cell.setCellValue("");
				}
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(13, CellType.STRING);
				if(dto.getElectricQuota() != null) {
					cell.setCellValue( dto.getElectricQuota());
				}else {
					cell.setCellValue("");
				}
//				cell.setCellValue((dto.getElectricQuota() != null) ? dto.getElectricQuota() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getTimeIntoUse() != null) ? dto.getTimeIntoUse() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getLastMaintenanceTime() != null) ? dto.getLastMaintenanceTime() : null);
				cell.setCellStyle(styleCenter);
			}
		}
		return workbook;
	}
	
//ducpm23
	public List<WoDTO> getUpdateElectric(WoDTO obj){
		return woDAO.getListUpdateElectric(obj);
	}
	
	public List<WoDTO> getManager(WoDTO obj){
		return manageMEDAO.getManagerList(obj);
	}
//ducpm
	@Override
	public DeviceStationElectricalDTO createBroken(DeviceStationElectricalDTO obj) {
		// TODO Auto-generated method stub
		DeviceStationElectricalBO bo = manageMEDAO.get(DeviceStationElectricalBO.class ,obj.getDeviceId());
		bo.setFailureStatus(1L);
		bo.setCreatedDate(new Date());
		bo.setCreatedUser(obj.getCreatedUser());
		bo.setDescriptionFailure(obj.getDescriptionFailure());
		bo.setFailure(obj.getFailure());
		for(UtilAttachDocumentDTO dto : obj.getBrokenImages()) {
			dto.setType("ELECTRIC_BROKEN_IMAGE");
			dto.setObjectId(obj.getDeviceId());
			utilAttachDocumentDAO.save(dto.toModel());
		}
		manageMEDAO.updateObject(bo);
		return obj;
	}

	@Override
	@Transactional
	public DeviceStationElectricalDTO updateBroken(DeviceStationElectricalDTO obj) {
		DeviceStationElectricalBO bo = manageMEDAO.get(DeviceStationElectricalBO.class ,obj.getDeviceId());
		bo.setCreatedDate(new Date());
		bo.setApprovedDate(new Date());
		bo.setApprovedUser(obj.getApprovedUser());
		bo.setFailureStatus(obj.getFailureStatus());
		manageMEDAO.updateObject(bo);
		if (obj.getFailureStatus() == 3) return obj;
		Long woTypeId = woTypeDAO.getIdByCode("SUA_CHUA_HT_CO_DIEN");
		WoTypeBO woTypeBO = manageMEDAO.get(WoTypeBO.class, woTypeId);
		StationElectricalBO stationElectricalBO = manageMEDAO.get(StationElectricalBO.class, bo.getStationId());
		SysGroupBO sysGroupCnkt = manageMEDAO.get(SysGroupBO.class, stationElectricalBO.getSysGroupId());
		// todo
		// create wo
		WoBO woBO = new WoBO();
		woBO.setWoTypeId(woTypeId);
		woBO.setWoName(woTypeBO.getWoTypeName());

		woBO.setWoCode(woTypeBO.getWoTypeCode());
		woBO.setStationCode(stationElectricalBO.getStationCode());
		woBO.setCreatedDate(new Date());
		woBO.setFinishDate(obj.getFinishDate());
		woBO.setQoutaTime(obj.getQoutaTime().intValue());
		woBO.setState ("ASSIGN_CD");
		woBO.setStatus(1L);
		// dau viec chua biet luu vao dau

		SysGroupBO sysGroupVhkt = manageMEDAO.get(SysGroupBO.class, 270120L);
		woBO.setCdLevel1(sysGroupVhkt.getSysGroupId().toString());
		woBO.setCdLevel1Name(sysGroupVhkt.getName());
		// van hanh khai thac chua biet lay nhu nao
		WoDTO aioWoTrDTO2 = woDAO.getCDLevel(sysGroupCnkt.getSysGroupId());
		if (aioWoTrDTO2 != null) {
			woBO.setCdLevel2(aioWoTrDTO2.getCdLevel3());
			woBO.setCdLevel2Name(aioWoTrDTO2.getCdLevel3Name());
			woBO.setCdLevel3(aioWoTrDTO2.getCdLevel3());
			woBO.setCdLevel3Name(aioWoTrDTO2.getCdLevel3Name());
		}
		woBO.setMoneyValue(0d);
		woBO.setDescribeAfterMath(obj.getDescribeAfterMath());
		Long id = woDAO.saveObject(woBO);
		String code = "VNM_PMXL_2_";
		Long nextSq = woDAO.getNextSeqVal();
		Long countIdType = woDAO.getCountWoTypeAIO(woTypeId);
		code += woTypeId + "_" + countIdType + "_" + nextSq;
		woBO.setWoCode(code);
		WoMappingChecklistBO woMappingChecklistBO = new WoMappingChecklistBO();
		woMappingChecklistBO.setWoId(id);
		woMappingChecklistBO.setState("NEW");
		woMappingChecklistBO.setStatus(1L);
		woMappingChecklistBO.setName(obj.getDescriptionFailure());
		woMappingChecklistDAO.saveObject(woMappingChecklistBO);

		return obj;
	}


	
	public List<UserDirectoryDTO>getListUserDirectory(UserDirectoryDTO obj){
		return manageMEDAO.getUserDirectoryList(obj);
	}
	
	public List<UserDirectoryDTO>getDoSearchUserDirectoryList(UserDirectoryDTO obj){
		return manageMEDAO.getDoSearchUserDirectoryList(obj);
	}
	
	public Long saveUserDirectory(UserDirectoryDTO obj) {
		Long resutl = manageMEDAO.saveObject(obj.toModel());
		return resutl;
	}
	
	public void removeUserDirectory(UserDirectoryDTO obj) {
		 manageMEDAO.removeUserDirectory(obj);
	}
	
	public void updateUserDirectory(UserDirectoryDTO obj) {
 		manageMEDAO.updateObject(obj.toModel());
	}
	
	public List<UnitListDTO> getUnitList(UnitListDTO obj){
		return manageMEDAO.getUnitList(obj);
	}
	
	public List<UnitListDTO>doSearchUnitList(UnitListDTO obj){
		return manageMEDAO.doSearchUnit(obj);
	}
	public Long saveUnitList(UnitListDTO obj) {
		Long resutl = manageMEDAO.saveObject(obj.toModel());
		return resutl;
	}
	
	public void removeUnitList(UnitListDTO obj) {
		 manageMEDAO.removeUnitList(obj);
	}
	
	public void updateUnitList(UnitListDTO obj) {
 		manageMEDAO.updateObject(obj.toModel());
	}
	public List<DocManagementDTO>getDocManagementList(DocManagementDTO obj){
		List<DocManagementDTO> docManagementList = manageMEDAO.getDocManagementList(obj);
		for(DocManagementDTO doc : docManagementList) {
			UtilAttachDocumentDTO attachFile = new UtilAttachDocumentDTO();
			attachFile.setType("DOCUMENT_MANAGEMENT");
			attachFile.setObjectId(doc.getId());
			attachFile = utilAttachDocumentDAO.getAttachFile(attachFile);
			doc.setAttachFile(attachFile);
			if(null!=attachFile)doc.setAttachFileName(attachFile.getName());
		}
		return docManagementList;
	}

	public Long save(DocManagementDTO obj) {
		return manageMEDAO.saveObject(obj.toModel());
	}
	
	public void removeDocManagement(DocManagementDTO obj) {
		 manageMEDAO.removeDocManagement(obj);
	}

	public void updateDocManagement(DocManagementDTO obj) {
		if(obj.getAttachFile() != null) {
			manageMEDAO.updateObject(obj.toModel());
			utilAttachDocumentDAO.updateObject(obj.getAttachFile().toModel());
		}
	}
	
//ducpm23
}

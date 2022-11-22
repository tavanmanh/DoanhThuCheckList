package com.viettel.coms.business;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.coms.bo.AIOSysUserBO;
import com.viettel.coms.dao.AIOSysUserDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dto.AIOSysGroupDTO;
import com.viettel.coms.dto.AIOSysUserDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.utils.CryptoUtils;
import com.viettel.utils.ImageUtil;
import com.viettel.wms.business.UserRoleBusinessImpl;

@Service("aioSysUserBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AIOSysUserBusinessImpl extends BaseFWBusinessImpl<AIOSysUserDAO, AIOSysUserDTO, AIOSysUserBO> implements AIOSysUserBusiness {

    @Autowired
    private AIOSysUserDAO sysUserDAO;
    
    @Autowired
    private UserRoleBusinessImpl userRoleBusiness;

    @Value("${folder_upload2}")
    private String folderUpload;

    @Value("${allow.file.ext}")
    private String allowFileExt;

    @Value("${allow.folder.dir}")
    private String allowFolderDir;

    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;

    @Value("${input_image_sub_folder_upload}")
    private String input_image_sub_folder_upload;

    @Value("${folder_uploadAio}")
    private String folderUploadAio;
    
    private static final String COLLABORATOR_TYPE_IMG = "121";
    private static final String COLLABORATOR_TYPE_ATTACH = "122";

    private static final String BASE_PATH = "/166571";
    private static final String DIVIDER = "/";
    private static final String PATTERN_LIKE = "%";
    
    @Autowired
    public AIOSysUserBusinessImpl(AIOSysUserDAO aioSysUserDAO) {
        tDAO = aioSysUserDAO;
        tModel = new AIOSysUserBO();
    }
    @Autowired
    public UtilAttachDocumentDAO utilAttachDocumentDAO;

    @Context
    HttpServletRequest request;

    public DataListDTO doSearch(AIOSysUserDTO criteria, HttpServletRequest request) {
    	List<AIOSysUserDTO> dtos = new ArrayList<>();
    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty()) {
			dtos = sysUserDAO.doSearch(criteria, groupIdList);
		}
        DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(dtos);
        dataListDTO.setTotal(criteria.getTotalRecord());
        dataListDTO.setSize(criteria.getPageSize());
        dataListDTO.setStart(1);
        return dataListDTO;
    }
    
	public Long saveRegisterCtv(AIOSysUserDTO obj) throws Exception {
			AIOSysUserDTO existUser = sysUserDAO.getSysUserByTaxCodeAndPhoneNum(obj.getTaxCode());
			if (existUser != null) {
				if (existUser.getTaxCode() != null && existUser.getTaxCode().equals(obj.getTaxCode())) {
					throw new BusinessException("Đã tồn tại số CMT/ĐKKD của cộng tác viên trên hệ thống");
				}
			}

			// validate parent phone
			AIOSysUserDTO parent = sysUserDAO.getUserIdByPhone(obj.getParentPhone());
			if (parent == null) {
				throw new BusinessException("Không tìm thấy người giới thiệu!");
			}

			obj.setParentUserId(parent.getSysUserId());
//			obj.setSysGroupId(parent.getSysGroupId());
			obj.setStatus(2L);
			obj.setSaleChannel("VCC");
			obj.setLoginName(obj.getPhoneNumber());
			if(isPasswordValid(obj.getPassword())) {
				obj.setPassword(CryptoUtils.hashPassword(encryptText(obj.getPassword())));
			} else {
				throw new BusinessException("Mật khẩu phải có ít nhất 8 ký tự chữ hoa, thường, số và ký tự đặc biệt(không nhập khoảng trắng)");
			}
			
			obj.setCreatedDate(new Date());
			obj.setTypeUser(1l);
			obj.setFieldType("2");
			Long id = sysUserDAO.saveObject(obj.toModel());

			List<UtilAttachDocumentDTO> listImages = new ArrayList<UtilAttachDocumentDTO>();
			listImages.addAll(obj.getListImageCmt());
			listImages.addAll(obj.getListImageHk());
			listImages.addAll(obj.getListImageHd());
			for (UtilAttachDocumentDTO util : listImages) {
				util.setObjectId(id);
				util.setStatus("1");
				util.setCreatedDate(new Date());
				if (StringUtils.isNoneBlank(util.getType())) {
					if (util.getType().equals("121")) {
						util.setDescription("IMAGE CMT ATTACH REGISTRY PARTNER");
					}

					if (util.getType().equals("HK")) {
						util.setDescription("IMAGE HK ATTACH REGISTRY PARTNER");
					}

					if (util.getType().equals("HĐ")) {
						util.setDescription("IMAGE HĐ ATTACH REGISTRY PARTNER");
					}
				}
				utilAttachDocumentDAO.saveObject(util.toModel());
			}

			return id;
	}
	
	public Long updateRegisterCtv(AIOSysUserDTO obj) throws Exception {
			AIOSysUserDTO userDb = sysUserDAO.getDataById(obj.getSysUserId());
			
			// validate parent phone
			AIOSysUserDTO parent = sysUserDAO.getUserIdByPhone(obj.getParentPhone());
			if (parent == null) {
				throw new BusinessException("Không tìm thấy người giới thiệu!");
			}

			userDb.setParentUserId(parent.getSysUserId());
			userDb.setSysGroupId(obj.getSysGroupId());
			userDb.setSaleChannel("VCC");
			userDb.setLoginName(obj.getPhoneNumber());
			
			userDb.setFullName(obj.getFullName());
			userDb.setEmployeeCode(obj.getEmployeeCode());
			userDb.setEmail(obj.getEmail());
			userDb.setAddress(obj.getAddress());
			userDb.setPhoneNumber(obj.getPhoneNumber());
			userDb.setUserBirthday(obj.getUserBirthday());
			userDb.setAccountNumber(obj.getAccountNumber());
			userDb.setTaxCodeUser(obj.getTaxCodeUser());
			userDb.setContractCode(obj.getContractCode());
			if(!userDb.getPassword().equals(obj.getPassword())) {
				if(isPasswordValid(obj.getPassword())) {
					userDb.setPassword(CryptoUtils.hashPassword(encryptText(obj.getPassword())));
				} else {
					throw new BusinessException("Mật khẩu phải có ít nhất 8 ký tự chữ hoa, thường, số và ký tự đặc biệt(không nhập khoảng trắng)");
				}
			}
			userDb.setProvinceIdXddd(obj.getProvinceIdXddd());
			userDb.setProvinceNameXddd(obj.getProvinceNameXddd());
			
			SimpleDateFormat formatDate=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			userDb.setCreatedDate(formatDate.parse(userDb.getCreatedDateDb()));
			userDb.setProvinceIdCtvXddd(obj.getProvinceIdCtvXddd());
			userDb.setProvinceNameCtvXddd(obj.getProvinceNameCtvXddd());
			userDb.setDistrictName(obj.getDistrictName());
			userDb.setCommuneName(obj.getCommuneName());
			Long id = sysUserDAO.updateObject(userDb.toModel());
			
			//Xoá ảnh
			List<String> lstType = new ArrayList<>();
			lstType.add("121");
			lstType.add("HK");
			lstType.add("HĐ");
			utilAttachDocumentDAO.deleteUtilAttachDocument(obj.getSysUserId(), lstType);
			
			//Lưu ảnh
			List<UtilAttachDocumentDTO> listImages = new ArrayList<UtilAttachDocumentDTO>();
			listImages.addAll(obj.getListImageCmt());
			listImages.addAll(obj.getListImageHk());
			listImages.addAll(obj.getListImageHd());
			for (UtilAttachDocumentDTO util : listImages) {
				util.setObjectId(userDb.getSysUserId());
				util.setStatus("1");
				util.setCreatedDate(new Date());
				if (StringUtils.isNoneBlank(util.getType())) {
					if (util.getType().equals("121")) {
						util.setDescription("IMAGE CMT ATTACH REGISTRY PARTNER");
					}

					if (util.getType().equals("HK")) {
						util.setDescription("IMAGE HK ATTACH REGISTRY PARTNER");
					}

					if (util.getType().equals("HĐ")) {
						util.setDescription("IMAGE HĐ ATTACH REGISTRY PARTNER");
					}
				}
				utilAttachDocumentDAO.saveObject(util.toModel());
			}

			return id;
	}
	
	public DataListDTO getSysGroupTree(AIOSysGroupDTO dto) {
        StringBuilder queryPath = new StringBuilder(BASE_PATH);
        List<Long> groupLv = Arrays.asList(1L, 2L);
        if (dto.getSysGroupId() != null) {
            queryPath.append(DIVIDER).append(dto.getSysGroupId()).append(PATTERN_LIKE);
            groupLv = Collections.singletonList(3L);
        } else {
            queryPath.append(DIVIDER).append(PATTERN_LIKE);
        }
        List<AIOSysGroupDTO> ls = sysUserDAO.getGroupTree(dto, queryPath.toString(), groupLv);
        
        DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(ls);
        dataListDTO.setTotal(dto.getTotalRecord());
        dataListDTO.setSize(dto.getPageSize());
        dataListDTO.setStart(1);
        return dataListDTO;
    }
	
	public AIOSysUserDTO getImageById(AIOSysUserDTO obj) throws Exception{
		AIOSysUserDTO data = new AIOSysUserDTO();
		List<UtilAttachDocumentDTO> listImageCmt = new ArrayList<>();
		List<UtilAttachDocumentDTO> listImageHk = new ArrayList<>();
		List<UtilAttachDocumentDTO> listImageHd = new ArrayList<>();
		List<Long> listId = new ArrayList<Long>();
		listId.add(obj.getSysUserId());
		List<String> lstType = new ArrayList<String>();
		lstType.add("121");
		lstType.add("HK");
		lstType.add("HĐ");
		List<UtilAttachDocumentDTO> listImageWorkItem = utilAttachDocumentDAO.getListAttachmentByIdAndType(listId, lstType);
		for(UtilAttachDocumentDTO dto : listImageWorkItem) {
//			dto.setFilePath(UEncrypt.encryptFileUploadPath(dto.getFilePath()));
			dto.setBase64String(
					ImageUtil.convertImageToBase64(folderUploadAio + dto.getFilePath()));
			if(dto.getType().equals("121")) {
				listImageCmt.add(dto);
			}
			
			if(dto.getType().equals("HK")) {
				listImageHk.add(dto);
			}
			
			if(dto.getType().equals("HĐ")) {
				listImageHd.add(dto);
			}
			
		}
		
		data.setListImageCmt(listImageCmt);
		data.setListImageHk(listImageHk);
		data.setListImageHd(listImageHd);
		
		return data;
	}
	
	public Long removeRecord(Long id) {
		return sysUserDAO.removeRecord(id);
	}
	
	public static boolean isPasswordValid(String password) {
		// Regex to check valid password. 
        String regex = "^(?=.*[0-9])"
                       + "(?=.*[a-z])(?=.*[A-Z])"
                       + "(?=.*[@#$%^&+=])"
                       + "(?=\\S+$).{8,20}$"; 
  
        // Compile the ReGex 
        Pattern p = Pattern.compile(regex); 
  
        // If the password is empty 
        // return false 
        if (password == null) { 
            return false; 
        } 
  
        // Pattern class contains matcher() method 
        // to find matching between given password 
        // and regular expression. 
        Matcher matcher = p.matcher(password); 
  
        // Return if the password 
        // matched the ReGex 
	    if(matcher.matches())
	        return true;
	    else
	        return false;
	}
	
	public static String encryptText(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
}

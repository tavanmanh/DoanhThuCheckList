package com.viettel.coms.rest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Lists;
import com.viettel.asset.bo.SysGroup;
import com.viettel.asset.dto.ReportIncreaseDecreaseDto;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.business.TangentCustomerBusinessImpl;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.coms.dto.DetailTangentCustomerDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.ResultSolutionDTO;
import com.viettel.coms.dto.ResultTangentDTO;
import com.viettel.coms.dto.TangentCustomerDTO;
import com.viettel.erp.rest.QualityCableMeaResultRsServiceImpl;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UDate;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;

import fr.opensagres.xdocreport.core.io.internal.ByteArrayOutputStream;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

public class TangentCustomerRsServiceImpl implements TangentCustomerRsService {

	@Context
	HttpServletRequest request;
	
	static Logger LOGGER = LoggerFactory.getLogger(TangentCustomerRsServiceImpl.class);

	@Autowired
	TangentCustomerBusinessImpl tangentCustomerBusinessImpl;

	@Value("${folder_upload2}")
	private String folderUpload;
	
	@Override
	public Response doSearch(TangentCustomerDTO obj) throws ParseException {
		DataListDTO data = tangentCustomerBusinessImpl.doSearch(obj, request);
		return Response.ok(data).build();
	}

	@Override
	public Response save(TangentCustomerDTO obj) throws Exception {
		try {	
			Long id = tangentCustomerBusinessImpl.save(obj, request);
			if (id == 0) {
				return Response.ok(Response.Status.BAD_REQUEST).build();
			} else 
				return Response.ok(Response.Status.CREATED).build();
		}catch (BusinessException e) {
			e.printStackTrace();
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}catch (Exception e) {
			e.printStackTrace();
			return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
		}
	}

	@Override
	public Response update(TangentCustomerDTO obj) throws Exception {
		try {
			Long id = tangentCustomerBusinessImpl.update(obj, request);
			if (id == 0) {
				return Response.ok(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.CREATED).build();
			}
		} catch (BusinessException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
		}
	}

	@Override
	public Response doSearchDistrictByProvinceCode(TangentCustomerDTO obj) {
		DataListDTO data = tangentCustomerBusinessImpl.doSearchDistrictByProvinceCode(obj);
		return Response.ok(data).build();
	}

	@Override
	public Response doSearchCommunneByDistrict(TangentCustomerDTO obj) {
		DataListDTO data = tangentCustomerBusinessImpl.doSearchCommunneByDistrict(obj);
		return Response.ok(data).build();
	}

	@Override
	public Response getUserConfigTagent(ConfigStaffTangentDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerBusinessImpl.getUserConfigTagent(obj)).build();
	}

	@Override
	public Response deleteRecord(TangentCustomerDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerBusinessImpl.deleteRecord(obj, request)).build();
	}

	@Override
	public Response saveDetail(TangentCustomerDTO obj) {
		try {
			Long id = tangentCustomerBusinessImpl.saveDetail(obj, request);
			if (id == 0) {
				return Response.ok(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.CREATED).build();
			}
		}
		catch (BusinessException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
       }catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra khi cập nhật")).build();
		}
		
	}

	@Override
	public Response saveApproveOrReject(ResultTangentDTO obj) {
		Long id = tangentCustomerBusinessImpl.saveApproveOrReject(obj, request);
		if (id == 0) {
			return Response.ok(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(Response.Status.CREATED).build();
		}
	}

	@Override
	public Response saveNotDemain(ResultTangentDTO obj) {
		Long id = tangentCustomerBusinessImpl.saveNotDemain(obj, request);
		if (id == 0) {
			return Response.ok(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(Response.Status.CREATED).build();
		}
	}

	@Override
	public Response checkRoleApproved() {
		Long check = tangentCustomerBusinessImpl.checkRoleApproved(request);
		return Response.ok(check).build();
	}

	@Override
	public Response getListResultTangentByTangentCustomerId(ResultTangentDTO obj) {
		TangentCustomerDTO ls = tangentCustomerBusinessImpl.getListResultTangentByTangentCustomerId(obj);
		return Response.ok(ls).build();
	}

	@Override
	public Response getAllContractXdddSuccess(ResultSolutionDTO obj) {
		DataListDTO data = tangentCustomerBusinessImpl.getAllContractXdddSuccess(obj, request);
		return Response.ok(data).build();
	}

	@Override
	public Response getResultSolutionByContractId(Long contractId) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerBusinessImpl.getResultSolutionByContractId(contractId)).build();
	}

	@Override
	public Response saveResultSolution(TangentCustomerDTO obj) {
		Long id = tangentCustomerBusinessImpl.saveResultSolution(obj, request);
		if (id == 0) {
			return Response.ok(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(Response.Status.CREATED).build();
		}
	}

	@Override
	public Response saveApproveOrRejectGiaiPhap(ResultSolutionDTO obj) {
		Long id = tangentCustomerBusinessImpl.saveApproveOrRejectGiaiPhap(obj, request);
		if (id == 0) {
			return Response.ok(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(Response.Status.CREATED).build();
		}
	}

	@Override
	public Response getListResultTangentJoinSysUserByTangentCustomerId(ResultTangentDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerBusinessImpl.getListResultTangentJoinSysUserByTangentCustomerId(obj)).build();
	}

	@Override
	public Response getResultSolutionJoinSysUserByTangentCustomerId(Long id) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerBusinessImpl.getResultSolutionJoinSysUserByTangentCustomerId(id)).build();
	}

	@Override
	public Response getListResultTangentByResultTangentId(ResultTangentDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerBusinessImpl.getListResultTangentByResultTangentId(obj)).build();
	}

	@Override
	public Response checkRoleUpdate() {
		Long check = tangentCustomerBusinessImpl.checkRoleUpdate(request);
		return Response.ok(check).build();
	}

	@Override
	public Response getContractRose() {
		Double check = tangentCustomerBusinessImpl.getContractRose();
		return Response.ok(check).build();
	}

	@Override
	public Response doSearchProvince(CatProvinceDTO obj) {
		DataListDTO data = tangentCustomerBusinessImpl.doSearchProvince(obj);
		return Response.ok(data).build();
	}

	@Override
	public Response approveRose(ResultSolutionDTO obj) throws Exception {
		// TODO Auto-generated method stub
		try {
			Long id = tangentCustomerBusinessImpl.approveRose(obj, request);
			if (id == 0) {
				return Response.ok(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.OK).build();
			}
		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}
	
	@Override
	public Response getUserConfigTagentByProvince(ConfigStaffTangentDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerBusinessImpl.getUserConfigTagentByProvince(obj)).build();
	}

	@Override
	public Response checkRoleSourceYCTX(TangentCustomerDTO obj) throws Exception {
		// TODO Auto-generated method stub
		try {
			Long IDRole = tangentCustomerBusinessImpl.checkRoleSourceYCTX(obj, request);
			if (IDRole == null) {
				return Response.ok(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(IDRole).build();
			}
		}catch (BusinessException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}catch (Exception e) {
			return Response.ok().entity(Collections.singletonMap("error", "Không tìm thấy user trong hệ thông/User không hợp lệ")).build();
		}
	}

	@Override
	public Response getAllContractXdddSuccessInternal(ResultSolutionDTO obj) {
		DataListDTO data = tangentCustomerBusinessImpl.getAllContractXdddSuccessInternal(obj, request);
		return Response.ok(data).build();
	}

	// Duonghv13-11022022-start

	@Override
	public Response checkRoleCreated(){
		Boolean checkRoleCreated = tangentCustomerBusinessImpl.checkRoleCreated(request);
		if (checkRoleCreated) {
			return Response.ok(Status.OK).build();
		}
		return Response.ok().build();
	}
	
	
	@Override
	public Response getChannel(AppParamDTO obj) {
		List<String> lstParType = Lists.newArrayList();
		lstParType.add("RECEPTION_CHANNEL");
		lstParType.add("CUSTOMER_RESOURCES");
		lstParType.add("FIELD_WORK");
		lstParType.add("REGISTERED_CUSTOMER_SERVICE");
		lstParType.add("BUILDER_MODEL");
		obj.setLstParType(lstParType);
		DataListDTO data = tangentCustomerBusinessImpl.getChannel(obj);
		return Response.ok(data).build();
		
	}
	
	@Override
	public Response exportExcelqlyctx(TangentCustomerDTO obj) throws Exception {
		// TODO Auto-generated method stub
		try {
			DataListDTO data = tangentCustomerBusinessImpl.doSearch(obj,request);
            String strReturn = tangentCustomerBusinessImpl.exportExcelqlyctx(data);
    		return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        }
        return null;
	}

	@Override
	public Response exportExcelDetailData(TangentCustomerDTO obj) throws Exception {
		// TODO Auto-generated method stub
		//Get Data
        
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String exportTime = sdf.format(new Timestamp(System.currentTimeMillis()));
        ResultTangentDTO resultTangent = obj.getResultTangentDTO();
        ResultSolutionDTO resultSolution = new ResultSolutionDTO();
        obj.getResultSolutionDTO();
        DetailTangentCustomerDTO  detailCustomer= new DetailTangentCustomerDTO();
        String utf8EncodedString_fileExport  = new String();
        InputStream is;
        try {

            Map beans = new HashMap();
            beans.put("exportTime", exportTime);
            beans.put("exportUser", user.getVpsUserInfo().getFullName());
            beans.put("customer", obj);
            beans.put("resultTangent", resultTangent);
           
            if(Long.parseLong(obj.getStatus()) > 2 ){
            	if(null != resultTangent.getResultTangentType()) {
                	if(resultTangent.getResultTangentType().equals("1")==true) {
                		beans.put("resultTangentYes", resultTangent.getResultTangentDetailYesDTO());
                		if(null != resultTangent.getResultTangentDetailYesDTO().getDetailCustomer()) {
                			detailCustomer  = resultTangent.getResultTangentDetailYesDTO().getDetailCustomer();	
                			
                		}
                		if(null != resultTangent.getResultSolutionDTO()) {
                			resultSolution  = resultTangent.getResultSolutionDTO();	
                		}
                		beans.put("detailCustomer", detailCustomer);
                		beans.put("resultSolution", resultSolution);
                		
                	}else {
                		beans.put("resultTangentNo", resultTangent.getResultTangentDetailNoDTO());
                	}
                }
            }
            
            
            
//            iS = new FileInputStream(filePath + "/CHI_TIET_THONG_TIN_KHACH_HANG.xlsx");
            is = new BufferedInputStream(new FileInputStream(filePath + "CHI_TIET_THONG_TIN_KHACH_HANG.xlsx"));
            XLSTransformer transformer = new XLSTransformer();
            // long startTime = System.nanoTime();
            Workbook resultWorkbook = transformer.transformXLS(is, beans);
            // long endTime = System.nanoTime();
            is.close();
            saveWorkbook(resultWorkbook, folderUpload + "/CHI_TIET_THONG_TIN_KHACH_HANG.xlsx");
        } catch (FileNotFoundException fe) {
            LOGGER.error(fe.getMessage(), fe);
        } catch (ParsePropertyException pe) {
            LOGGER.error(pe.getMessage(), pe);
        } catch (InvalidFormatException formate) {
            LOGGER.error(formate.getMessage(), formate);
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage(), ioe);
        }
        
        
        String path = UEncrypt.encryptFileUploadPath("CHI_TIET_THONG_TIN_KHACH_HANG.xlsx");
        return Response.ok(Collections.singletonMap("fileName", path)).build();
	}
	
	private void saveWorkbook(Workbook resultWorkbook, String fileName) throws IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(fileName));
        resultWorkbook.write(os);
        os.flush();
        os.close();
    }
	
	// Duonghv13-11022022-end
	
	@Override
	public Response checkRoleUserAssignYctx() {
		Long result = 0l;
		Boolean check = tangentCustomerBusinessImpl.checkRoleUserAssignYctx(request);
		if(check) {
			result = 1l;
		}
		return Response.ok(result).build();
	}

	@Override
	public Response getCallbotConversation(TangentCustomerDTO obj) {
		return Response.ok(tangentCustomerBusinessImpl.findCallBotConversation(obj.getTangentCustomerId())).build();
	}
}

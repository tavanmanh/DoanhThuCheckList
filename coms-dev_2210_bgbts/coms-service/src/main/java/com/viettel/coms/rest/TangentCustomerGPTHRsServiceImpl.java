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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.google.common.collect.Lists;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.business.TangentCustomerGPTHBusinessImpl;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.coms.dto.DetailTangentCustomerGPTHDTO;
import com.viettel.coms.dto.ResultSolutionGPTHDTO;
import com.viettel.coms.dto.ResultSolutionGPTHDTO;
import com.viettel.coms.dto.ResultTangentGPTHDTO;
import com.viettel.coms.dto.ResultTangentGPTHDTO;
import com.viettel.coms.dto.TangentCustomerGPTHDTO;
import com.viettel.coms.dto.TangentCustomerGPTHDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

public class TangentCustomerGPTHRsServiceImpl implements TangentCustomerGPTHRsService {

	@Context
	HttpServletRequest request;
	
	static Logger LOGGER = LoggerFactory.getLogger(TangentCustomerGPTHRsServiceImpl.class);

	@Autowired
	TangentCustomerGPTHBusinessImpl tangentCustomerGPTHBusinessImpl;

	@Value("${folder_upload2}")
	private String folderUpload;
	
	@Override
	public Response doSearch(TangentCustomerGPTHDTO obj) throws ParseException {
		DataListDTO data = tangentCustomerGPTHBusinessImpl.doSearch(obj, request);
		return Response.ok(data).build();
	}

	@Override
	public Response save(TangentCustomerGPTHDTO obj) throws Exception {
		try {	
			Long id = tangentCustomerGPTHBusinessImpl.save(obj, request);
			if (id == 0) {
				return Response.ok(Response.Status.BAD_REQUEST).build();
			} else 
				return Response.ok(Response.Status.CREATED).build();
		}catch (BusinessException e) {
			//e.printStackTrace();
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}catch (Exception e) {
			//e.printStackTrace();
			return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
		}
	}

	@Override
	public Response update(TangentCustomerGPTHDTO obj) throws Exception {
		try {
			Long id = tangentCustomerGPTHBusinessImpl.update(obj, request);
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
	public Response doSearchDistrictByProvinceCode(TangentCustomerGPTHDTO obj) {
		DataListDTO data = tangentCustomerGPTHBusinessImpl.doSearchDistrictByProvinceCode(obj);
		return Response.ok(data).build();
	}

	@Override
	public Response doSearchCommunneByDistrict(TangentCustomerGPTHDTO obj) {
		DataListDTO data = tangentCustomerGPTHBusinessImpl.doSearchCommunneByDistrict(obj);
		return Response.ok(data).build();
	}

	@Override
	public Response getUserConfigTagent(ConfigStaffTangentDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerGPTHBusinessImpl.getUserConfigTagent(obj)).build();
	}

	@Override
	public Response deleteRecord(TangentCustomerGPTHDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerGPTHBusinessImpl.deleteRecord(obj, request)).build();
	}

	@Override
	public Response saveDetail(TangentCustomerGPTHDTO obj) {
		try {
			Long id = tangentCustomerGPTHBusinessImpl.saveDetail(obj, request);
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
	public Response saveApproveOrReject(ResultTangentGPTHDTO obj) {
		Long id = tangentCustomerGPTHBusinessImpl.saveApproveOrReject(obj, request);
		if (id == 0) {
			return Response.ok(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(Response.Status.CREATED).build();
		}
	}

	@Override
	public Response saveNotDemain(ResultTangentGPTHDTO obj) {
		try {
			Long id = tangentCustomerGPTHBusinessImpl.saveNotDemain(obj, request);
			if (id == 0) {
				return Response.ok(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(Response.Status.CREATED).build();
			}
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}

	@Override
	public Response checkRoleApproved() {
		Long check = tangentCustomerGPTHBusinessImpl.checkRoleApproved(request);
		return Response.ok(check).build();
	}

	@Override
	public Response getListResultTangentByTangentCustomerId(TangentCustomerGPTHDTO obj) {
		TangentCustomerGPTHDTO ls = tangentCustomerGPTHBusinessImpl.getListResultTangentByTangentCustomerId(obj);
		return Response.ok(ls).build();
	}

	@Override
	public Response getAllContractXdddSuccess(ResultSolutionGPTHDTO obj) {
		DataListDTO data = tangentCustomerGPTHBusinessImpl.getAllContractXdddSuccess(obj, request);
		return Response.ok(data).build();
	}

	@Override
	public Response getResultSolutionByContractId(Long contractId) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerGPTHBusinessImpl.getResultSolutionByContractId(contractId)).build();
	}

	@Override
	public Response saveResultSolution(TangentCustomerGPTHDTO obj) {
		Long id = tangentCustomerGPTHBusinessImpl.saveResultSolution(obj, request);
		if (id == 0) {
			return Response.ok(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(Response.Status.CREATED).build();
		}
	}

	@Override
	public Response saveApproveOrRejectGiaiPhap(ResultSolutionGPTHDTO obj) {
		Long id = tangentCustomerGPTHBusinessImpl.saveApproveOrRejectGiaiPhap(obj, request);
		if (id == 0) {
			return Response.ok(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(Response.Status.CREATED).build();
		}
	}

	@Override
	public Response getListResultTangentJoinSysUserByTangentCustomerId(ResultTangentGPTHDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerGPTHBusinessImpl.getListResultTangentJoinSysUserByTangentCustomerId(obj)).build();
	}

	@Override
	public Response getResultSolutionJoinSysUserByTangentCustomerId(Long id) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerGPTHBusinessImpl.getResultSolutionJoinSysUserByTangentCustomerId(id)).build();
	}

	@Override
	public Response getListResultTangentByResultTangentId(ResultTangentGPTHDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(tangentCustomerGPTHBusinessImpl.getListResultTangentByResultTangentId(obj)).build();
	}

	@Override
	public Response checkRoleUpdate() {
		Long check = tangentCustomerGPTHBusinessImpl.checkRoleUpdate(request);
		return Response.ok(check).build();
	}

	@Override
	public Response getContractRose() {
		Double check = tangentCustomerGPTHBusinessImpl.getContractRose();
		return Response.ok(check).build();
	}

	@Override
	public Response doSearchProvince(CatProvinceDTO obj) {
		DataListDTO data = tangentCustomerGPTHBusinessImpl.doSearchProvince(obj);
		return Response.ok(data).build();
	}

	@Override
	public Response approveRose(ResultSolutionGPTHDTO obj) throws Exception {
		// TODO Auto-generated method stub
		try {
			Long id = tangentCustomerGPTHBusinessImpl.approveRose(obj, request);
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
		return Response.ok(tangentCustomerGPTHBusinessImpl.getUserConfigTagentByProvince(obj)).build();
	}

	@Override
	public Response checkRoleSourceYCTX(TangentCustomerGPTHDTO obj) throws Exception {
		// TODO Auto-generated method stub
		try {
			Long IDRole = tangentCustomerGPTHBusinessImpl.checkRoleSourceYCTX(obj, request);
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
	public Response getAllContractXdddSuccessInternal(ResultSolutionGPTHDTO obj) {
		DataListDTO data = tangentCustomerGPTHBusinessImpl.getAllContractXdddSuccessInternal(obj, request);
		return Response.ok(data).build();
	}

	// Duonghv13-11022022-start

	@Override
	public Response checkRoleCreated(){
		Boolean checkRoleCreated = tangentCustomerGPTHBusinessImpl.checkRoleCreated(request);
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
		DataListDTO data = tangentCustomerGPTHBusinessImpl.getChannel(obj);
		return Response.ok(data).build();
		
	}
	
	@Override
	public Response exportFile(TangentCustomerGPTHDTO obj) throws Exception {
		// TODO Auto-generated method stub
		try {
			DataListDTO data = tangentCustomerGPTHBusinessImpl.doSearch(obj,request);
            String strReturn = tangentCustomerGPTHBusinessImpl.exportFile(data);
    		return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        	e.printStackTrace();
        	LOGGER.error(e.getMessage());
        }
        return null;
	}

	@Override
	public Response exportExcelDetailData(TangentCustomerGPTHDTO obj) throws Exception {
		// TODO Auto-generated method stub
		//Get Data
        
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String exportTime = sdf.format(new Timestamp(System.currentTimeMillis()));
        ResultTangentGPTHDTO resultTangent = obj.getResultTangentGPTHDTO();
        ResultSolutionGPTHDTO resultSolution = new ResultSolutionGPTHDTO();
        obj.getResultSolutionGPTHDTO();
        DetailTangentCustomerGPTHDTO  detailCustomer= new DetailTangentCustomerGPTHDTO();
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
                			detailCustomer  = resultTangent.getResultTangentDetailYesDTO().getDetailCustomerGPTH();
                			
                		}
                		if(null != resultTangent.getResultSolutionGPTHDTO()) {
                			resultSolution  = resultTangent.getResultSolutionGPTHDTO();	
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
		Boolean check = tangentCustomerGPTHBusinessImpl.checkRoleUserAssignYctx(request);
		if(check) {
			result = 1l;
		}
		return Response.ok(result).build();
	}

	@Override
	public Response getCallbotConversation(TangentCustomerGPTHDTO obj) {
		return Response.ok(tangentCustomerGPTHBusinessImpl.findCallBotConversation(obj.getTangentCustomerGPTHId())).build();
	}
}

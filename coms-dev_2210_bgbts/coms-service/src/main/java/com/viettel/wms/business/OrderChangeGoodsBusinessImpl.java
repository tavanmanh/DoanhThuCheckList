package com.viettel.wms.business;

import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.wms.bo.OrderChangeGoodsBO;
import com.viettel.wms.constant.Constants;
import com.viettel.wms.dao.OrderChangeGoodsDAO;
import com.viettel.wms.dao.OrderChangeGoodsDetailDAO;
import com.viettel.wms.dto.*;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service("orderChangeGoodsBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OrderChangeGoodsBusinessImpl
        extends
        BaseFWBusinessImpl<OrderChangeGoodsDAO, OrderChangeGoodsDTO, OrderChangeGoodsBO>
        implements OrderChangeGoodsBusiness {
    protected final Logger log = Logger.getLogger(OrderChangeGoodsBusinessImpl.class);
    @Autowired
    private OrderChangeGoodsDAO orderChangeGoodsDAO;

    @Autowired
    private OrderChangeGoodsDetailDAO orderChangeGoodsDetailDAO;

    @Autowired
    OrderChangeGoodsDetailBusinessImpl businessImpl;

    @Autowired
    private GoodsBusinessImpl goodsBusinessImpl;

    List<OrderGoodsExelDTO> lstErrorOrder;

    @Value("${folder_upload}")
    private String folder2Upload;

    public OrderChangeGoodsBusinessImpl() {
        tDAO = orderChangeGoodsDAO;
        tModel = new OrderChangeGoodsBO();
    }

    @Override
    public OrderChangeGoodsDAO gettDAO() {
        return orderChangeGoodsDAO;
    }

    @Override
    public long count() {
        return orderChangeGoodsDAO.count("OrderChangeGoodsBO", null);
    }

    /*--Begin
        T??m ki???m b???n ghi y??u c???u thay ?????i
     */
    public DataListDTO doSearch(OrderChangeGoodsDTO obj) {
        List<OrderChangeGoodsDTO> ls = orderChangeGoodsDAO.doSearch(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getTotalRecord());
        data.setStart(1);
        return data;
    }

    /*
     * k???t th??c t??m ki???m -- End
     */

    /*--Begin
      T???o m???i y??u c???u thay ?????i, b???n ghi ???? import t??? file excel v??o b???ng WMS_OWNER.ORDER_CHANGE_GOODS v?? WMS_OWNER.ORDER_CHANGE_GOODS_DETAIL
     */
    @Override
    @Transactional
    public Long saveImportChange(OrderChangeGoodsDTO obj, HttpServletRequest request) throws Exception {
        if (!VpsPermissionChecker.checkPermissionOnDomainData(Constants.OperationKey.CREATE, Constants.AdResourceKey.CHANGE_ORDER, obj.getStockId(), request)) {
            throw new IllegalArgumentException("B???n kh??ng c?? quy???n th??m m???i y??u c???u thay ?????i !");
        }
        if (checkInsertUpdate(obj)) {
            GoodsDTO dto = new GoodsDTO();
            List<GoodsDTO> lstGoods = goodsBusinessImpl.getGoodsForOrder(dto);
            List<String> lstCode = new ArrayList<>();
            for (int j = 0; j < lstGoods.size(); j++) {
                lstCode.add(lstGoods.get(j).getCode());
            }
            Long ids = orderChangeGoodsDAO.saveObject(obj.toModel());
            if (ids != null) {
                for (int i = 0; i < obj.listorderChangeGoodsDetailDTO().size(); i++) {
                    GoodsDTO goodsDTO1 = orderChangeGoodsDetailDAO.checkIsSerial(obj.listorderChangeGoodsDetailDTO().get(i).getGoodsCode());
                    GoodsDTO goodsDTO2 = orderChangeGoodsDAO.selectNameByCode(obj.listorderChangeGoodsDetailDTO().get(i).getGoodsCode());
                    GoodsDTO goodsDTO = orderChangeGoodsDAO.selectNameByCode(obj.listorderChangeGoodsDetailDTO().get(i).getNewGoodsCode());
                    if (lstCode.contains(obj.listorderChangeGoodsDetailDTO().get(i).getNewGoodsCode().trim())) {
                        obj.listorderChangeGoodsDetailDTO().get(i).setNewGoodsId(goodsDTO.getGoodsId());
                        obj.listorderChangeGoodsDetailDTO().get(i).setOrderChangeGoodsId(ids);
                        obj.listorderChangeGoodsDetailDTO().get(i).setGoodsUnitId(goodsDTO2.getGoodsUnitId());
                        obj.listorderChangeGoodsDetailDTO().get(i).setGoodsType(goodsDTO2.getGoodsType());
                        obj.listorderChangeGoodsDetailDTO().get(i).setGoodsTypeName(goodsDTO2.getGoodsTypeName());
                        obj.listorderChangeGoodsDetailDTO().get(i).setGoodsId(goodsDTO2.getGoodsId());
                        obj.listorderChangeGoodsDetailDTO().get(i).setGoodsState("1");
                        obj.listorderChangeGoodsDetailDTO().get(i).setGoodsStateName("B??nh Th?????ng");
                        obj.listorderChangeGoodsDetailDTO().get(i).setNewGoodsName(goodsDTO.getName());
                        obj.listorderChangeGoodsDetailDTO().get(i).setNewGoodsCode(obj.listorderChangeGoodsDetailDTO().get(i).getNewGoodsCode().trim());
                        checkInsert(obj, i);
                        businessImpl.save(obj.listorderChangeGoodsDetailDTO().get(i));
                    }

                }
            }
            return ids;
        }
        return 1l;
    }

    public void checkInsert(OrderChangeGoodsDTO obj, int i) {
        GoodsDTO goodsDTO1 = new GoodsDTO();
        goodsDTO1 = orderChangeGoodsDetailDAO.checkIsSerial(obj.listorderChangeGoodsDetailDTO().get(i).getGoodsCode());
        if (Integer.parseInt(goodsDTO1.getIsSerial()) == 1) {
            obj.listorderChangeGoodsDetailDTO().get(i).setAmountChange((double) 1);
            obj.listorderChangeGoodsDetailDTO().get(i).setGoodsIsSerial("1");
        } else if (Integer.parseInt(goodsDTO1.getIsSerial()) == 0) {
            StockGoodsTotalDTO stockGoodsTotalDTO = orderChangeGoodsDAO.sumAmountChange(obj.listorderChangeGoodsDetailDTO().get(i).getGoodsCode());
            obj.listorderChangeGoodsDetailDTO().get(i).setAmountChange(stockGoodsTotalDTO.getAmount());
        }
    }

    /*
     * k???t th??c t???o m???i -- End
     */

    /*
     * Begin -- Update y??u c???u thay ?????i, b???n ghi ???? import t??? file excel v??o
     * b???ng WMS_OWNER.ORDER_CHANGE_GOODS v?? WMS_OWNER.ORDER_CHANGE_GOODS_DETAIL
     */
    @Transactional
    public Long updateImportChange(OrderChangeGoodsDTO obj, KttsUserSession objUser) {
        if (!obj.getCreatedBy().equals(objUser.getSysUserId())) {
            throw new IllegalArgumentException("Ng?????i d??ng hi???n t???i kh??ng c?? quy???n s???a b???n ghi n??y !");
        }
        if (checkInsertUpdate(obj)) {
            Long delete = orderChangeGoodsDAO.deleteObj(obj);
            Long ids = orderChangeGoodsDAO.updateObject(obj.toModel());
            if (ids != null && delete != null) {
                GoodsDTO dto = new GoodsDTO();
                List<GoodsDTO> lstGoods = goodsBusinessImpl.getGoodsForOrder(dto);
                List<String> lstCode = new ArrayList<>();
                for (int j = 0; j < lstGoods.size(); j++) {
                    lstCode.add(lstGoods.get(j).getCode());
                }
                for (int i = 0; i < obj.listorderChangeGoodsDetailDTO().size(); i++) {
                    GoodsDTO goodsDTO1 = new GoodsDTO();
                    goodsDTO1 = orderChangeGoodsDetailDAO.checkIsSerial(obj.listorderChangeGoodsDetailDTO().get(i).getGoodsCode());
                    GoodsDTO goodsDTO2 = orderChangeGoodsDAO.selectNameByCode(obj.listorderChangeGoodsDetailDTO().get(i).getGoodsCode());
                    GoodsDTO goodsDTO = orderChangeGoodsDAO.selectNameByCode(obj.listorderChangeGoodsDetailDTO().get(i).getNewGoodsCode());
                    if (lstCode.contains(obj.listorderChangeGoodsDetailDTO().get(i).getNewGoodsCode().trim())) {
                        obj.listorderChangeGoodsDetailDTO().get(i).setOrderChangeGoodsId(obj.getOrderChangeGoodsId());
                        obj.listorderChangeGoodsDetailDTO().get(i).setNewGoodsName(goodsDTO.getName());
                        obj.listorderChangeGoodsDetailDTO().get(i).setGoodsType(goodsDTO2.getGoodsType());
                        obj.listorderChangeGoodsDetailDTO().get(i).setGoodsUnitId(goodsDTO2.getGoodsUnitId());
                        obj.listorderChangeGoodsDetailDTO().get(i).setGoodsTypeName(goodsDTO2.getGoodsTypeName());
                        obj.listorderChangeGoodsDetailDTO().get(i).setGoodsId(goodsDTO2.getGoodsId());
                        obj.listorderChangeGoodsDetailDTO().get(i).setGoodsState("1");
                        obj.listorderChangeGoodsDetailDTO().get(i).setGoodsStateName("B??nh Th?????ng");
                        obj.listorderChangeGoodsDetailDTO().get(i).setNewGoodsCode(obj.listorderChangeGoodsDetailDTO().get(i).getNewGoodsCode().trim());
                        checkInsert(obj, i);
                        businessImpl.save(obj.listorderChangeGoodsDetailDTO().get(i));
                    }
                }
            }
            return ids;
        }
        return 1l;
    }

    /* k???t th??c update */
    /* End */
    @Override
    public List<OrderChangeGoodsDetailDTO> doSearchForAutoImport(
            OrderChangeGoodsDetailDTO obj) {
        return orderChangeGoodsDAO.doSearchForAutoImport(obj);
    }


    /**
     * Fill d??? li???u khi v??o grid popup update khi click theo id
     *
     * @param obj
     * @return
     */
    @Override
    public DataListDTO doSearchGoodsForImportReq(OrderChangeGoodsDTO obj) {
        // TODO Auto-generated method stub
        List<OrderChangeGoodsDTO> ls = orderChangeGoodsDAO
                .doSearchGoodsForImportReq(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getTotalRecord());
        data.setStart(1);
        return data;
    }

    public List<OrderChangeGoodsDTO> getOrderChangeById(Long id) {
        // TODO Auto-generated method stub
        return orderChangeGoodsDAO.getOrderChangeById(id);
    }

    public boolean checkInsertUpdate(OrderChangeGoodsDTO obj) {
        GoodsDTO dto = new GoodsDTO();
        int flag = 1;
        List<GoodsDTO> lstGoods = goodsBusinessImpl.getGoodsForOrder(dto);

        List<String> lstCode = new ArrayList<>();
        for (int j = 0; j < lstGoods.size(); j++) {
            lstCode.add(lstGoods.get(j).getCode().trim());
        }

        if (obj.listorderChangeGoodsDetailDTO().size() <= 0) {
            throw new IllegalArgumentException("Kh??ng c?? d??? li???u chi ti???t h??ng h??a thay ?????i");
        } else {
            for (int i = 0; i < obj.listorderChangeGoodsDetailDTO().size(); i++) {
                OrderChangeGoodsDetailDTO oderchange = new OrderChangeGoodsDetailDTO();
                List<OrderChangeGoodsDetailDTO> lstDTO = orderChangeGoodsDetailDAO.checkSerial(oderchange);
                List<String> lstSerial1 = new ArrayList<>();
                for (int p = 0; p < lstDTO.size(); p++) {
                    lstSerial1.add(lstDTO.get(p).getSerial());
                }
                boolean check = false;
                boolean check1 = false;
                GoodsDTO goodsDTO = new GoodsDTO();
                GoodsDTO goodsDTO1 = orderChangeGoodsDetailDAO.checkIsSerial(obj.listorderChangeGoodsDetailDTO().get(i).getGoodsCode().trim());
                StockGoodsTotalDTO stockGoodsTotalDTO = new StockGoodsTotalDTO();
                stockGoodsTotalDTO = orderChangeGoodsDAO.sumAmountChange(obj.listorderChangeGoodsDetailDTO().get(i).getGoodsCode());
                if (obj.listorderChangeGoodsDetailDTO().get(i).getNewGoodsCode() != null) {
                    goodsDTO = orderChangeGoodsDetailDAO.checkIsSerial(obj.listorderChangeGoodsDetailDTO().get(i).getNewGoodsCode().trim());
                }
                if (Integer.parseInt(goodsDTO1.getIsSerial()) == 1) {
                    if (obj.listorderChangeGoodsDetailDTO().get(i).getSerial() == null) {
                        throw new IllegalArgumentException("C???n nh???p Serial thay ?????i (h??ng th???" + (i + 1) + ")");
                    } else if (obj.listorderChangeGoodsDetailDTO().get(i).getSerial() != null) {
                        check = orderChangeGoodsDetailDAO.checkDuplicateSerial(obj.getListorderChangeGoodsDetailDTO().get(i).getGoodsCode().toUpperCase().trim(), obj.getListorderChangeGoodsDetailDTO().get(i).getSerial().toUpperCase().trim());
                        if (!check) {
                            throw new IllegalArgumentException("Serial " + obj.listorderChangeGoodsDetailDTO().get(i).getSerial() + " kh??ng ph?? h???p v???i m?? h??ng " + obj.listorderChangeGoodsDetailDTO().get(i).getGoodsCode());
                        }
                        for (int k = i + 1; k < obj.listorderChangeGoodsDetailDTO().size(); k++) {
                            if (obj.listorderChangeGoodsDetailDTO().get(i).getSerial().trim().equals(obj.listorderChangeGoodsDetailDTO().get(k).getSerial() != null ? (obj.listorderChangeGoodsDetailDTO().get(k).getSerial().trim()) : null)) {
                                throw new IllegalArgumentException("Tr??ng serial thay ?????i trong danh s??ch");
                            }
                        }


                    }
                    if (obj.getListorderChangeGoodsDetailDTO().get(i).getNewGoodsCode() != null && obj.getListorderChangeGoodsDetailDTO().get(i).getNewGoodsCode() != "") {
                        if (goodsDTO != null && Integer.parseInt(goodsDTO.getIsSerial()) == 0) {
                            throw new IllegalArgumentException("Kh??ng th??? thay ?????i h??ng h??a t??? c?? qu???n l?? serial sang kh??ng qu???n l?? serial");
                        }
                    }
                } else if (Integer.parseInt(goodsDTO1.getIsSerial()) == 0) {
                    if (obj.listorderChangeGoodsDetailDTO().get(i).getSerial() != null && obj.listorderChangeGoodsDetailDTO().get(i).getSerial() != "") {
                        throw new IllegalArgumentException("M?? " + obj.listorderChangeGoodsDetailDTO().get(i).getGoodsCode() + " kh??ng c?? qu???n l?? serial, kh??ng ???????c nh???p serial thay ?????i ");
                    }
                }
                if (obj.listorderChangeGoodsDetailDTO().get(i).getNewGoodsCode() == null) {
                    throw new IllegalArgumentException("C???n nh???p m?? h??ng sau thay ?????i");
                } else if (obj.listorderChangeGoodsDetailDTO().get(i).getNewGoodsCode() != null) {
                    if (obj.getListorderChangeGoodsDetailDTO().get(i).getNewSerial() != null) {
                        check1 = orderChangeGoodsDetailDAO.checkDuplicateSerial(obj.getListorderChangeGoodsDetailDTO().get(i).getNewGoodsCode().trim(), obj.getListorderChangeGoodsDetailDTO().get(i).getNewSerial().trim());
                    }
//						if(goodsDTO!=null&&Integer.parseInt(goodsDTO.getIsSerial())==1){
//							if(obj.getListorderChangeGoodsDetailDTO().get(i).getNewGoodsCode()!=null&&obj.getListorderChangeGoodsDetailDTO().get(i).getNewGoodsCode()!=""){
//								throw new IllegalArgumentException("Kh??ng th??? thay ?????i h??ng h??a t??? kh??ng qu???n l?? serial sang c?? qu???n l?? serial");
//							}
//						}
                }
                if (stockGoodsTotalDTO.getAmount() == null) {
                    throw new IllegalArgumentException("M?? h??ng " + obj.getListorderChangeGoodsDetailDTO().get(i).getGoodsCode().trim() + " kh??ng t???n t???i trong kho t???n!");
                }
                if (!lstCode.contains(obj.getListorderChangeGoodsDetailDTO().get(i).getGoodsCode().trim())) {
                    throw new IllegalArgumentException("M?? h??ng " + obj.getListorderChangeGoodsDetailDTO().get(i).getGoodsCode().trim() + " kh??ng t???n t???i!");
                }
                if (obj.listorderChangeGoodsDetailDTO().get(i).getNewGoodsCode() != null) {
                    if (!lstCode.contains(obj.listorderChangeGoodsDetailDTO().get(i).getNewGoodsCode().trim())) {
                        throw new IllegalArgumentException("M?? h??ng sau thay ?????i kh??ng t???n t???i");
                    }
                    flag = 0;
                }

                if (goodsDTO != null && Integer.parseInt(goodsDTO.getIsSerial()) == 1) {
                    if ((obj.listorderChangeGoodsDetailDTO().get(i).getNewSerial() != null && obj.listorderChangeGoodsDetailDTO().get(i).getNewSerial() != "") && flag == 0) {
                        if (check1) {
                            throw new IllegalArgumentException("Serial sau thay ?????i c???a m?? h??ng sau thay ?????i ???? t???n t???i");
                        }
                        for (int k = i + 1; k < obj.listorderChangeGoodsDetailDTO().size(); k++) {
                            if (obj.listorderChangeGoodsDetailDTO().get(i).getNewSerial().trim().equals(obj.listorderChangeGoodsDetailDTO().get(k).getNewSerial() != null ? (obj.listorderChangeGoodsDetailDTO().get(k).getNewSerial().trim()) : null)) {
                                throw new IllegalArgumentException("Tr??ng serial sau thay ?????i trong danh s??ch");
                            }
                        }


                    } else {
                        throw new IllegalArgumentException("M?? h??ng sau thay ?????i c?? qu???n l?? serial c???n nh???p serial sau thay ?????i");
                    }
                } else if (goodsDTO != null && Integer.parseInt(goodsDTO.getIsSerial()) == 0) {
                    if ((obj.listorderChangeGoodsDetailDTO().get(i).getNewSerial() != null && obj.listorderChangeGoodsDetailDTO().get(i).getNewSerial() != "") && flag == 0) {
                        throw new IllegalArgumentException("M?? h??ng sau thay ?????i kh??ng c?? qu???n l?? serial kh??ng ???????c nh???p serial sau thay ?????i");
                    }
                }
						/*else{
							obj.listorderChangeGoodsDetailDTO().get(i).setNewSerial(obj.listorderChangeGoodsDetailDTO().get(i).getSerial().trim());
							boolean checkDuplicateSerial=orderChangeGoodsDetailDAO.checkDuplicateSerial(obj.getListorderChangeGoodsDetailDTO().get(i).getNewGoodsCode().trim(),
									obj.getListorderChangeGoodsDetailDTO().get(i).getNewSerial().trim());
							if(checkDuplicateSerial){
								throw new IllegalArgumentException("Serial sau thay ?????i c???a m?? h??ng sau thay ?????i ?????i ???? t???n t???i");
							}
					}*/


            }
            return true;
        }

    }


    public String exportOrderChangeExcelError(OrderChangeGoodsDetailDTO errorObj) throws Exception {
        String filePath = UEncrypt.decryptFileUploadPath(errorObj.getFilePathError());
        InputStream file = new BufferedInputStream(new FileInputStream(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 0; i < errorObj.getLstErrorOrderGoods().size(); i++) {
            XSSFRow row = sheet.getRow(Integer.parseInt(errorObj.getLstErrorOrderGoods().get(i).getLineError()) - 1);
            if (row == null) {
                row = sheet.createRow(Integer.parseInt(errorObj.getLstErrorOrderGoods().get(i).getLineError()) - 1);
            }
            XSSFCell cell = row.getCell(7);

            if (cell == null) {
                cell = row.createCell(7);
            }
            if (!cell.getStringCellValue().isEmpty()) {
                cell.setCellValue(cell.getStringCellValue() + "," + errorObj.getLstErrorOrderGoods().get(i).getDetailError());
            } else {
                cell.setCellValue(errorObj.getLstErrorOrderGoods().get(i).getDetailError());
            }
        }
        file.close();
        File out = new File(folder2Upload + File.separatorChar + "ThayDoiHangHoa.xlsx");

        FileOutputStream outFile = new FileOutputStream(out);
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt.encryptFileUploadPath("ThayDoiHangHoa.xlsx");
        return path;

    }

    @Override
    public List<OrderChangeGoodsDTO> doSearchForCheckAll(OrderChangeGoodsDTO obj) {
        // TODO Auto-generated method stub
        return orderChangeGoodsDAO.doSearchForCheckAll(obj);
    }

}

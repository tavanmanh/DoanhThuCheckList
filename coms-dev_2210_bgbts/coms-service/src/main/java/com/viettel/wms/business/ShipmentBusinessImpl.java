package com.viettel.wms.business;

import com.google.common.collect.Lists;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.wms.bo.ShipmentBO;
import com.viettel.wms.constant.Constants;
import com.viettel.wms.dao.ICntContractDAO;
import com.viettel.wms.dao.ShipmentDAO;
import com.viettel.wms.dto.*;
import com.viettel.wms.utils.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service("shipmentBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShipmentBusinessImpl extends
        BaseFWBusinessImpl<ShipmentDAO, ShipmentDTO, ShipmentBO> implements
        ShipmentBusiness {

    static Logger LOGGER = LoggerFactory.getLogger(ShipmentBusinessImpl.class);
    List<OrderGoodsExelDTO> lstErrorOrder;
    @Autowired
    private ShipmentDAO shipmentDAO;
    @Autowired
    ICntContractDAO icntContractDao;
    @Autowired
    private GoodsBusinessImpl goodsBusinessImpl;
    @Autowired
    private ShipmentGoodsBusinessImpl shipmentGoodsBusinessImpl;

    @Autowired
    private ManufacturerBusinessImpl manufacturerBusinessImpl;

    @Autowired
    private ProducingCountryBusinessImpl producingCountryBusinessImpl;

    @Autowired
    private ShipmentTaxBusinessImpl shipmentTaxBusinessImpl;

    @Autowired
    private TaxBusinessImpl taxBusinessImpl;
    @Autowired
    private CommonBusiness commonBusiness;
    @Autowired
    private ShipmentGoodsDetailBusinessImpl shipmentGoodsDetailBusinessImpl;

    @Value("${folder_upload}")
    private String folder2Upload;

    public ShipmentBusinessImpl() {
        tModel = new ShipmentBO();
        tDAO = shipmentDAO;
    }

    @Override
    public ShipmentDAO gettDAO() {
        return shipmentDAO;
    }

    @Override
    public long count() {
        return shipmentDAO.count("ShipmentBO", null);
    }

    // H??m check m?? l?? h??ng
    public Boolean checkCode(String code, Long shipmentId) {
        ShipmentDTO obj = shipmentDAO.getbycode(code);

        if (shipmentId == null) {
            if (obj == null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (obj == null) {
                return true;
            } else if (obj != null
                    && obj.getShipmentId().longValue() == shipmentId) {
                return true;
            } else {
                return false;
            }
        }

    }

    //End
    // H??m t??m ki???m danh s??ch l?? h??ng trong Qu???n l?? danh s??ch l?? h??ng
    public DataListDTO doSearch(ShipmentDTO criteria) {
        List<ShipmentDTO> ls = shipmentDAO.doSearch(criteria);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(criteria.getTotalRecord());
        data.setSize(criteria.getTotalRecord());
        data.setStart(1);
        return data;
    }

    // End
    // H??m t??m ki???m danh s??ch l?? h??ng trong ?????nh l?????ng t??? tr???ng k??? thu???t
    public DataListDTO doSearchQuantity(ShipmentDTO criteria,
                                        HttpServletRequest request) throws Exception {
        String err = "";
        if (criteria.getCreatedDeptId() == null) {
            List<Long> listId = commonBusiness.getListDomainData(
                    Constants.OperationKey.ESTIMATE,
                    Constants.AdResourceKey.SHIPMENT, request);
            criteria.setLstCreatedDeptId(listId);
            List<ShipmentDTO> ls = shipmentDAO.doSearchQuantity(criteria);
            DataListDTO data = new DataListDTO();
            data.setData(ls);
            data.setTotal(criteria.getTotalRecord());
            data.setSize(criteria.getTotalRecord());
            data.setStart(1);
            return data;
        }
        if (!VpsPermissionChecker.checkPermissionOnDomainData(
                Constants.OperationKey.ESTIMATE,
                Constants.AdResourceKey.SHIPMENT, criteria.getCreatedDeptId(),
                request)) {
            err = StringUtils.isNotEmpty(err) ? (err + ";" + criteria
                    .getCreatedDeptId())
                    : ("B???n kh??ng c?? quy???n xem th??ng tin l?? h??ng t???i ????n v???  " + criteria
                    .getCreatedDeptName());
        }

        if (StringUtils.isNotEmpty(err)) {
            throw new IllegalArgumentException(err);
        }
        if (criteria.getLstCreatedDeptId().size() == 0) {
            List<Long> listId = commonBusiness.getListDomainData(Constants.OperationKey.ESTIMATE, Constants.AdResourceKey.SHIPMENT, request);
            criteria.setLstCreatedDeptId(listId);
        }
        List<ShipmentDTO> ls = shipmentDAO.doSearchQuantity(criteria);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(criteria.getTotalRecord());
        data.setSize(criteria.getTotalRecord());
        data.setStart(1);
        return data;
    }

    // End
    // Begin
    // T??m ki???m ?????nh gi?? theo mi???n d??? li???u
    public DataListDTO doSearchPrice(ShipmentDTO criteria,
                                     HttpServletRequest request) throws Exception {
        String err = "";
        if (criteria.getCreatedDeptId() == null) {
            List<Long> listId = commonBusiness.getListDomainData(
                    Constants.OperationKey.VALUE,
                    Constants.AdResourceKey.SHIPMENT, request);
            criteria.setLstCreatedDeptId(listId);
            List<ShipmentDTO> ls = shipmentDAO.doSearchQuantity(criteria);
            DataListDTO data = new DataListDTO();
            data.setData(ls);
            data.setTotal(criteria.getTotalRecord());
            data.setSize(criteria.getTotalRecord());
            data.setStart(1);
            return data;
        }
        if (!VpsPermissionChecker.checkPermissionOnDomainData(
                Constants.OperationKey.VALUE, Constants.AdResourceKey.SHIPMENT,
                criteria.getCreatedDeptId(), request)) {
            err = StringUtils.isNotEmpty(err) ? (err + ";" + criteria
                    .getCreatedDeptId())
                    : ("B???n kh??ng c?? quy???n xem th??ng tin l?? h??ng t???i ????n v???  " + criteria
                    .getCreatedDeptName());
        }

        if (StringUtils.isNotEmpty(err)) {
            throw new IllegalArgumentException(err);
        }
        if (criteria.getLstCreatedDeptId().size() == 0) {
            List<Long> listId = commonBusiness.getListDomainData(Constants.OperationKey.VALUE, Constants.AdResourceKey.SHIPMENT, request);
            criteria.setLstCreatedDeptId(listId);
        }
        List<ShipmentDTO> ls = shipmentDAO.doSearchQuantity(criteria);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(criteria.getTotalRecord());
        data.setSize(criteria.getTotalRecord());
        data.setStart(1);
        return data;
    }

    // End
    //
    @Override
    public List<String> searchListShipmentCode(String code) {
        // TODO Auto-generated method stub
        return shipmentDAO.searchListShipmentCode(code);
    }

    //End
    // H??m Import D??? li???u t??? Excel v??o Grid
    @Override
    public List<GoodsDTO> importGoods(String fileInput) throws Exception {
        lstErrorOrder = new ArrayList<>();
        List<GoodsDTO> workLst = Lists.newArrayList();
        try {
            File f = new File(fileInput);

            XSSFWorkbook workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();
            OrderGoodsExelDTO orderErrorFormat = new OrderGoodsExelDTO();
            int count = 0;
            for (Row row : sheet) {
                count++;
                if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {

                    boolean checkColumn1 = true;
                    boolean checkColumn3 = true;
                    boolean checkColumn4 = true;
                    boolean checkColumn6 = true;
                    boolean checkColumn7 = true;
                    String code = "";
                    String amount = "";
                    String name = "";
                    String partNumber = "";
                    String serial = "";
                    String manufacturerId = "";
                    String producingCountryId = "";

                    for (Cell cell : row) {

                        // Check format file exel
                        if (cell.getColumnIndex() == 1) {
                            code = formatter.formatCellValue(cell);
                        } else if (cell.getColumnIndex() == 2) {
                            name = formatter.formatCellValue(cell);
                        } else if (cell.getColumnIndex() == 3) {
                            manufacturerId = formatter.formatCellValue(cell);
                        } else if (cell.getColumnIndex() == 4) {
                            producingCountryId = formatter
                                    .formatCellValue(cell);
                        } else if (cell.getColumnIndex() == 5) {
                            partNumber = formatter.formatCellValue(cell);
                        } else if (cell.getColumnIndex() == 6) {
                            serial = formatter.formatCellValue(cell);
                        } else if (cell.getColumnIndex() == 7) {
                            amount = formatter.formatCellValue(cell);
                        }
                    }
                    if (code == "" && name == "" && amount == ""
                            && serial == "" && partNumber == ""
                            && producingCountryId == "" && manufacturerId == "") {
                        if (count == 3) {
                            for (Cell cell : row) {
                                orderErrorFormat = new OrderGoodsExelDTO();
                                if (cell.getColumnIndex() == 1) {
                                    code = formatter.formatCellValue(cell);
                                    checkColumn1 = checkDataFromFileExel(code,
                                            count, 1, orderErrorFormat);
                                } else if (cell.getColumnIndex() == 2) {
                                    name = formatter.formatCellValue(cell);
                                } else if (cell.getColumnIndex() == 3) {
                                    manufacturerId = formatter
                                            .formatCellValue(cell);
                                    checkColumn3 = checkDataFromFileExel(
                                            manufacturerId, count, 3,
                                            orderErrorFormat);
                                } else if (cell.getColumnIndex() == 4) {
                                    producingCountryId = formatter
                                            .formatCellValue(cell);
                                    checkColumn4 = checkDataFromFileExel(
                                            producingCountryId, count, 4,
                                            orderErrorFormat);
                                } else if (cell.getColumnIndex() == 5) {
                                    partNumber = formatter
                                            .formatCellValue(cell);
                                } else if (cell.getColumnIndex() == 6) {
                                    serial = formatter.formatCellValue(cell);
                                    checkColumn6 = checkDataFromFileExel(
                                            serial, count, 6, orderErrorFormat);
                                } else if (cell.getColumnIndex() == 7) {
                                    amount = formatter.formatCellValue(cell);
                                    checkColumn7 = checkDataFromFileExel(
                                            amount, count, 7, orderErrorFormat);
                                }
                            }
                        } else {
                            break;
                        }
                    } else {
                        for (Cell cell : row) {
                            orderErrorFormat = new OrderGoodsExelDTO();
                            if (cell.getColumnIndex() == 1) {
                                code = formatter.formatCellValue(cell);
                                checkColumn1 = checkDataFromFileExel(code,
                                        count, 1, orderErrorFormat);
                            } else if (cell.getColumnIndex() == 2) {
                                name = formatter.formatCellValue(cell);
                            } else if (cell.getColumnIndex() == 3) {
                                manufacturerId = formatter
                                        .formatCellValue(cell);
                                checkColumn3 = checkDataFromFileExel(
                                        manufacturerId, count, 3,
                                        orderErrorFormat);
                            } else if (cell.getColumnIndex() == 4) {
                                producingCountryId = formatter
                                        .formatCellValue(cell);
                                checkColumn4 = checkDataFromFileExel(
                                        producingCountryId, count, 4,
                                        orderErrorFormat);
                            } else if (cell.getColumnIndex() == 5) {
                                partNumber = formatter.formatCellValue(cell);
                            } else if (cell.getColumnIndex() == 6) {
                                serial = formatter.formatCellValue(cell);
                                checkColumn6 = checkDataFromFileExel(serial,
                                        count, 6, orderErrorFormat);
                            } else if (cell.getColumnIndex() == 7) {
                                amount = formatter.formatCellValue(cell);
                                checkColumn7 = checkDataFromFileExel(amount,
                                        count, 7, orderErrorFormat);
                            }
                        }
                    }
                    if (checkColumn1 && checkColumn7 && checkColumn6 && checkColumn3 && checkColumn4) {
                        GoodsDTO newObj = new GoodsDTO();
                        List<GoodsDTO> lstGoods = goodsBusinessImpl.getGoodsForOrder(newObj);
                        DecimalFormat df = new DecimalFormat("#.##");
                        for (int i = 0; i < lstGoods.size(); i++) {
                            if (lstGoods.get(i).getCode().equals(formatter.formatCellValue(row.getCell(1)).toUpperCase().trim())) {
                                newObj.setName(lstGoods.get(i).getName());
                                newObj.setOriginPrice(lstGoods.get(i).getOriginPrice());
                                newObj.setCode(formatter.formatCellValue(row.getCell(1)).toUpperCase().trim());
                                if (manufacturerId != "") {
                                    newObj.setManufacturerId(Math.round(row.getCell(3).getNumericCellValue()));
                                    newObj.setManufacturerName(manufacturerBusinessImpl.getAllNameById(Long.parseLong(manufacturerId)).getName());
                                } else {
                                    newObj.setManufacturerId(null);
                                    newObj.setManufacturerName(null);
                                }
                                if (producingCountryId != "") {
                                    newObj.setProducingCountryId(Math.round(row
                                            .getCell(4).getNumericCellValue()));
                                    newObj.setProducingCountryName(producingCountryBusinessImpl
                                            .getAllNameById(
                                                    Long.parseLong(producingCountryId))
                                            .getName());
                                } else {
                                    newObj.setProducingCountryId(null);
                                    newObj.setProducingCountryName(null);
                                }
                                newObj.setPartNumber(formatter
                                        .formatCellValue(row.getCell(5))
                                        .toUpperCase().trim());
                                newObj.setSerial(formatter.formatCellValue(
                                        row.getCell(6)).trim());
                                if (isDouble(formatter
                                        .formatCellValue(row.getCell(7)))) {
                                    newObj.setAmount(Double.parseDouble(df
                                            .format(row.getCell(7)
                                                    .getNumericCellValue())));
                                }
                                newObj.setGoodsId(lstGoods.get(i).getGoodsId());
                                newObj.setIsSerial(lstGoods.get(i)
                                        .getIsSerial());
                                newObj.setGoodsUnitName(lstGoods.get(i)
                                        .getGoodsUnitName());
                                newObj.setUnitType(lstGoods.get(i)
                                        .getGoodsUnitId());
                                break;
                            }
                        }
                        workLst.add(newObj);
                    }

                }
            }
            workbook.close();
        } catch (NullPointerException pointerException) {
            // pointerException.printStackTrace();
            LOGGER.error(pointerException.getMessage(), pointerException);
        } catch (Exception e) {
            // e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
        }

        String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
        GoodsDTO objError = new GoodsDTO();
        objError.setLstErrorGoods(lstErrorOrder);
        objError.setFilePathError(filePathError);

        workLst.add(objError);
        return workLst;
    }

    // End
    // Kiem tra du lieu file Import
    boolean check = false;
    String goodCode = "";

    public boolean checkDataFromFileExel(String data, int rowIndex,
                                         int columnIndex, OrderGoodsExelDTO orderErrorFormat) {
        GoodsDTO dto = new GoodsDTO();
        List<GoodsDTO> lstGoods = goodsBusinessImpl.getGoodsForOrder(dto);
        if (columnIndex == 1) {
            List<String> lstCode = new ArrayList<>();
            for (int i = 0; i < lstGoods.size(); i++) {
                lstCode.add(lstGoods.get(i).getCode().toUpperCase().trim());
            }
            if (data.isEmpty()) {
                goodCode = "";
                orderErrorFormat.setLineError(String.valueOf(rowIndex));
                orderErrorFormat
                        .setColumnError(String.valueOf(columnIndex + 1));
                orderErrorFormat.setDetailError("M?? h??ng ??ang ????? tr???ng");
                lstErrorOrder.add(orderErrorFormat);
                return false;
            }
            if (!lstCode.contains(data.toUpperCase().trim())) {
                check = false;
                goodCode = "";
                orderErrorFormat.setLineError(String.valueOf(rowIndex));
                orderErrorFormat
                        .setColumnError(String.valueOf(columnIndex + 1));
                orderErrorFormat.setDetailError("M?? h??ng kh??ng t???n t???i");
                lstErrorOrder.add(orderErrorFormat);
                return false;
            } else {
                goodCode = data.toUpperCase().trim();
                check = true;
            }
        } else if (columnIndex == 3) {
            List<ManufacturerDTO> lst = manufacturerBusinessImpl
                    .getAllNameAndId();
            List<Long> lstMnId = new ArrayList<>();
            for (int i = 0; i < lst.size(); i++) {
                lstMnId.add(lst.get(i).getManufacturerId());
            }
            if (!(lstMnId.toString()).contains(data.trim())) {
                orderErrorFormat.setLineError(String.valueOf(rowIndex));
                orderErrorFormat
                        .setColumnError(String.valueOf(columnIndex + 1));
                orderErrorFormat
                        .setDetailError("ID h??ng s???n xu???t kh??ng t???n t???i");
                lstErrorOrder.add(orderErrorFormat);
                return false;
            }
        } else if (columnIndex == 4) {
            List<ProducingCountryDTO> lstp = producingCountryBusinessImpl
                    .getAllNameAndId();
            List<Long> lstPrId = new ArrayList<>();
            for (int i = 0; i < lstp.size(); i++) {
                lstPrId.add(lstp.get(i).getProducingCountryId());
            }
            if (!(lstPrId.toString()).contains(data.trim())) {
                orderErrorFormat.setLineError(String.valueOf(rowIndex));
                orderErrorFormat
                        .setColumnError(String.valueOf(columnIndex + 1));
                orderErrorFormat
                        .setDetailError("ID n?????c s???n xu???t kh??ng t???n t???i");
                lstErrorOrder.add(orderErrorFormat);
                return false;
            }
        } else if (columnIndex == 6) {
            if (check && goodCode != "") {
                checkSerialFileImport(data, rowIndex, columnIndex, orderErrorFormat, lstGoods, check, goodCode);
            }

        } else if (columnIndex == 7) {
            if (check && goodCode != "") {
                checkAmount(data, rowIndex, columnIndex, orderErrorFormat, lstGoods, check, goodCode);
            }
        }
        return true;

    }

    //Hamf check so luong trong file import
    public boolean checkAmount(String data, int rowIndex,
                               int columnIndex, OrderGoodsExelDTO orderErrorFormat, List<GoodsDTO> lstGoods, boolean check, String goodsCode) {
        for (int i = 0; i < lstGoods.size(); i++) {
            if (lstGoods.get(i).getCode().toUpperCase().trim()
                    .contains(goodCode.toUpperCase().trim())) {
                if (data.isEmpty()) {
                    orderErrorFormat.setLineError(String
                            .valueOf(rowIndex));
                    orderErrorFormat.setColumnError(String
                            .valueOf(columnIndex + 1));
                    orderErrorFormat
                            .setDetailError("S??? l?????ng ??ang ????? tr???ng");
                    lstErrorOrder.add(orderErrorFormat);
                    return false;
                } else if (!isDouble(data)) {
                    orderErrorFormat.setLineError(String
                            .valueOf(rowIndex));
                    orderErrorFormat.setColumnError(String
                            .valueOf(columnIndex + 1));
                    orderErrorFormat
                            .setDetailError("S??? l?????ng kh??ng ????ng ?????nh d???ng");
                    lstErrorOrder.add(orderErrorFormat);
                    return false;
                } else if (Double.parseDouble(data) <= 0) {
                    orderErrorFormat.setLineError(String
                            .valueOf(rowIndex));
                    orderErrorFormat.setColumnError(String
                            .valueOf(columnIndex + 1));
                    orderErrorFormat
                            .setDetailError("S??? l?????ng nh??? h??n 0");
                    lstErrorOrder.add(orderErrorFormat);
                    return false;
                } else if ("1".equals(lstGoods.get(i)
                        .getIsSerial())
                        && Double.parseDouble(data) != 1) {
                    orderErrorFormat.setLineError(String
                            .valueOf(rowIndex));
                    orderErrorFormat.setColumnError(String
                            .valueOf(columnIndex + 1));
                    orderErrorFormat
                            .setDetailError("H??ng h??a l?? thi???t b??? ch??? ???????c ph??p nh???p s??? l?????ng =1");
                    lstErrorOrder.add(orderErrorFormat);
                    return false;
                } else if (Double.parseDouble(data) > 9999999999d) {
                    orderErrorFormat.setLineError(String
                            .valueOf(rowIndex));
                    orderErrorFormat.setColumnError(String
                            .valueOf(columnIndex + 1));
                    orderErrorFormat
                            .setDetailError("S??? l?????ng b???n nh???p qu?? maxLength!");
                    lstErrorOrder.add(orderErrorFormat);
                    return false;
                }
            }
        }
        return true;
    }

    //End
    //Ham check Serial trong file import
    public boolean checkSerialFileImport(String data, int rowIndex, int columnIndex, OrderGoodsExelDTO orderErrorFormat, List<GoodsDTO> lstGoods, boolean check, String goodsCode) {
        for (int i = 0; i < lstGoods.size(); i++) {
            if (lstGoods.get(i).getCode().toUpperCase().trim()
                    .contains(goodCode.toUpperCase().trim())) {
                if ("1".equals(lstGoods.get(i).getIsSerial())
                        && data.isEmpty()) {
                    orderErrorFormat.setLineError(String
                            .valueOf(rowIndex));
                    orderErrorFormat.setColumnError(String
                            .valueOf(columnIndex + 1));
                    orderErrorFormat
                            .setDetailError("H??ng c?? qu???n l?? Serial kh??ng ???????c tr???ng");
                    lstErrorOrder.add(orderErrorFormat);
                    return false;
                } else if (!"1".equals(lstGoods.get(i)
                        .getIsSerial().toUpperCase().trim())
                        && data != "") {
                    orderErrorFormat.setLineError(String
                            .valueOf(rowIndex));
                    orderErrorFormat.setColumnError(String
                            .valueOf(columnIndex + 1));
                    orderErrorFormat
                            .setDetailError("H??ng kh??ng qu???n l?? Serial kh??ng ???????c ph??p nh???p Serial");
                    lstErrorOrder.add(orderErrorFormat);
                    return false;
                }
            }
        }
        return true;

    }
    //End

    // H??m check Double
    public boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    // End

    // H??m Export ra file Excel l???i
    @Override
    public String exportExcelError(GoodsDTO errorObj) throws Exception {
        // TODO Auto-generated method stub
        String filePath = UEncrypt.decryptFileUploadPath(errorObj
                .getFilePathError());
        InputStream file = new BufferedInputStream(
                new FileInputStream(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 0; i < errorObj.getLstErrorGoods().size(); i++) {
            XSSFRow row = sheet.getRow(Integer.parseInt(errorObj
                    .getLstErrorGoods().get(i).getLineError()) - 1);
            if (row == null) {
                row = sheet.createRow(Integer.parseInt(errorObj
                        .getLstErrorGoods().get(i).getLineError()) - 1);
            }
            XSSFCell cell = row.getCell(8);

            if (cell == null) {
                cell = row.createCell(8);
            }
            if (!cell.getStringCellValue().isEmpty()) {
                cell.setCellValue(cell.getStringCellValue() + ","
                        + errorObj.getLstErrorGoods().get(i).getDetailError());
            } else {
                cell.setCellValue(errorObj.getLstErrorGoods().get(i)
                        .getDetailError());
            }
        }
        file.close();
        File out = new File(folder2Upload + File.separatorChar
                + "Import_HangHoa_Err.xlsx");

        FileOutputStream outFile = new FileOutputStream(out);
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt.encryptFileUploadPath("Import_HangHoa_Err.xlsx");
        return path;
    }

    // End
    // Export file bi???u m???u
    @Override
    public String exportExcelTemplate() throws Exception {
        // TODO Auto-generated method stub
        List<ManufacturerDTO> lstManufacturer = manufacturerBusinessImpl
                .getAllNameAndId();
        List<ProducingCountryDTO> lstProducingCountry = producingCountryBusinessImpl
                .getAllNameAndId();

        ClassLoader classloader = Thread.currentThread()
                .getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template")
                .getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath
                + "Import_HangHoa.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(1);

        XSSFFont defaultFont = workbook.createFont();
        defaultFont.setFontHeightInPoints((short) 10);
        defaultFont.setFontName("Times New Roman");
        defaultFont.setColor(IndexedColors.BLACK.getIndex());
        defaultFont.setBold(false);
        defaultFont.setItalic(false);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFont(defaultFont);
        cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);

        for (int i = 0; i < lstProducingCountry.size(); i++) {
            XSSFRow row = sheet.getRow(i + 1);
            XSSFCell cell1 = row.createCell(0);
            cell1.setCellValue(lstProducingCountry.get(i)
                    .getProducingCountryId());
            cell1.setCellStyle(cellStyle);
            XSSFCell cell2 = row.createCell(1);
            cell2.setCellValue(lstProducingCountry.get(i).getName());
            cell2.setCellStyle(cellStyle);

        }
        for (int i = 0; i < lstManufacturer.size(); i++) {
            XSSFRow row = sheet.getRow(i + 1);
            XSSFCell cell3 = row.createCell(2);
            cell3.setCellValue(lstManufacturer.get(i).getManufacturerId());
            cell3.setCellStyle(cellStyle);
            XSSFCell cell4 = row.createCell(3);
            cell4.setCellValue(lstManufacturer.get(i).getName());
            cell4.setCellStyle(cellStyle);
        }

        file.close();
        File out = new File(folder2Upload + File.separatorChar
                + "Import_HangHoa.xlsx");

        FileOutputStream outFile = new FileOutputStream(out);
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt.encryptFileUploadPath("Import_HangHoa.xlsx");
        return path;
    }


    //H??m c???p nh???t status cho l?? h??ng
    @Override
    public void updateStatus(ShipmentDTO obj) {
        shipmentDAO.updateStatus(obj);

    }

    //End
    // begin
    // luu vao 2 bang SHIPMENT_GOODS va SHIPMENT_GOODS_DETAILS
    @Override
    public Long saveDetailShipment(ShipmentDTO obj, HttpServletRequest request)
            throws Exception {
        if (!VpsPermissionChecker.checkPermissionOnDomainData(
                Constants.OperationKey.VALUE, Constants.AdResourceKey.SHIPMENT,
                obj.getCreatedDeptId(), request)) {
            throw new IllegalArgumentException(
                    "B???n kh??ng c?? quy???n ?????nh gi?? cho l?? h??ng n??y !");
        }
        Long id = update(obj);

        for (int k = 0; k < obj.getLstShipmentTax().size(); k++) {
            obj.getLstShipmentTax().get(k).setShipmentId(obj.getShipmentId());
            if (obj.getStatus() == "3") {
                shipmentTaxBusinessImpl.save(obj.getLstShipmentTax().get(k));
            } else {
                Long idtax = shipmentDAO.deleteShipmentTax(obj);
                if (idtax != null) {
                    shipmentTaxBusinessImpl.save(obj.getLstShipmentTax().get(k));
                }
            }

        }
        for (int i = 0; i < obj.getLstShipmentGoods().size(); i++) {
            Long idShipmentGoods = shipmentDAO.deleteShipmentGoods(obj);
            Long shipmentGoodsId = shipmentGoodsBusinessImpl.save(obj.getLstShipmentGoods().get(i));
            if (idShipmentGoods != null) {
                obj.getLstShipmentGoods().get(i).setShipmentId(obj.getShipmentId());
                Long idShipmentGoodsDetail = shipmentDAO.deleteShipmentGoodsDetail(obj);
                if (idShipmentGoodsDetail != null) {
                    if (obj.getLstShipmentDetail() != null) {
                        obj.getLstShipmentDetail().get(i).setShipmentGoodsId(shipmentGoodsId);
                        shipmentGoodsDetailBusinessImpl.save(obj.getLstShipmentDetail().get(i));
                    }
                }
            }
        }

        return id;
    }

    // end
    // H??m X??? l?? Map h??ng h??a cho l?? h??ng
    public Long addShipmentGoods(List<ShipmentGoodsDTO> lobj) throws Exception {
        Long id = (long) 0;
        ShipmentDTO s = new ShipmentDTO();
        ShipmentGoodsDTO smg = new ShipmentGoodsDTO();
        ShipmentGoodsDetailDTO smgd = new ShipmentGoodsDetailDTO();
        smg.setShipmentId(lobj.get(0).getShipmentId());
        smgd.setShipmentId(lobj.get(0).getShipmentId());
        List<ShipmentGoodsDTO> lstObj = shipmentGoodsBusinessImpl
                .doSearchMap(smg);
        if (lstObj.size() != 0) {
            for (ShipmentGoodsDTO o : lstObj) {
                shipmentGoodsBusinessImpl.deleteGoods(o.getShipmentGoodsId());
            }
        }
        List<ShipmentGoodsDetailDTO> lstdObj = shipmentGoodsDetailBusinessImpl
                .getDoMapDetail(smgd);
        if (lstdObj.size() != 0) {
            for (ShipmentGoodsDetailDTO o : lstdObj) {
                shipmentGoodsDetailBusinessImpl.deleteGoods(o
                        .getShipmentGoodsDetailId());
            }
        }
        for (ShipmentGoodsDTO obj : lobj) {
            GoodsDTO g = new GoodsDTO();
            g.setId(obj.getGoodsId());
            GoodsDTO lstGoods = goodsBusinessImpl.getGoodById(g);
            ShipmentGoodsDetailDTO sgd = new ShipmentGoodsDetailDTO();
            sgd.setGoodsId(obj.getGoodsId());
            sgd.setSerial(obj.getSerial());
            sgd.setShipmentId(obj.getShipmentId());
            if (sgd.getSerial() != null && sgd.getGoodsId() != null) {
                ShipmentGoodsDetailDTO data = shipmentGoodsDetailBusinessImpl
                        .checkSerial(sgd);
                if (data != null) {
                    throw new IllegalArgumentException(
                            "Serial c???a h??ng h??a ???? t???n t???i trong DB");
                }
            }
            if ("1".equals(lstGoods.getIsSerial()) && sgd.getSerial() == null) {
                throw new IllegalArgumentException(
                        "H??ng qu???n l?? Serial kh??ng ???????c ????? tr???ng tr?????ng Serial");
            }
            if (!"1".equals(lstGoods.getIsSerial()) && sgd.getSerial() != null) {
                throw new IllegalArgumentException(
                        "H??ng kh??ng qu???n l?? Serial kh??ng ???????c nh???p tr?????ng Serial");
            }
            obj.setOriginPrice(lstGoods.getOriginPrice());
            obj.setTotalOriginPrice(lstGoods.getOriginPrice() * obj.getAmount());
            id = shipmentGoodsBusinessImpl.save(obj);
            ShipmentGoodsDetailDTO objS = new ShipmentGoodsDetailDTO();
            if (id == 0)
                break;
            else {
                objS.setShipmentGoodsId(id);
                objS.setGoodsId(obj.getGoodsId());
                objS.setGoodsName(obj.getGoodsName());
                objS.setGoodsCode(obj.getGoodsCode());
                if (obj.getManufacturerId() != null) {
                    objS.setManufacturerName(obj.getManufacturerName());
                    objS.setManufacturerId(obj.getManufacturerId());
                } else {
                    objS.setManufacturerId(lstGoods.getManufacturerId());
                    objS.setManufacturerName(lstGoods.getManufacturerName());
                }
                if (obj.getProducingCountryId() != null) {
                    objS.setProducingCountryId(obj.getProducingCountryId());
                    objS.setProducingCountryName(obj.getProducingCountryName());
                } else {
                    objS.setProducingCountryId(lstGoods.getProducingCountryId());
                    objS.setProducingCountryName(lstGoods
                            .getProducingCountryName());
                }
                objS.setUnitTypeId(obj.getUnitTypeId());
                objS.setUnitTypeName(obj.getUnitTypeName());
                objS.setSerial(obj.getSerial());
                objS.setShipmentId(obj.getShipmentId());
                objS.setPartNumber(obj.getPartNumber());
                objS.setAmount(obj.getAmount());
                shipmentGoodsDetailBusinessImpl.save(objS);
            }
        }
        s.setShipmentId(lobj.get(0).getShipmentId());
        s.setStatus("2");
        this.updateStatus(s);
        if (id == 0)
            return 1l;
        else
            return 2l;
    }

    // End
    // H??m x??? l?? th??m m???i l?? h??ng
    public Long addShipment(ShipmentDTO obj) throws Exception {
        boolean check = checkCode(obj.getCode(), null);
        if (!check) {
            throw new IllegalArgumentException("M?? l?? h??ng ???? t???n t???i");
        }
        return shipmentDAO.saveObject(obj.toModel());
    }

    // End
    // H??m x??? l?? c???p nh???t l?? h??ng
    public Long updateShipment(ShipmentDTO obj, KttsUserSession objUser)
            throws Exception {
        if (!obj.getCreatedBy().equals(objUser.getSysUserId())) {
            throw new IllegalArgumentException(
                    "Ng?????i d??ng hi???n t???i kh??ng c?? quy???n s???a b???n ghi n??y");
        }
        boolean check = checkCode(obj.getCode(), obj.getShipmentId());
        if (!check) {
            throw new IllegalArgumentException(obj.getCode().toUpperCase());
        }
        return shipmentDAO.updateObject(obj.toModel());
    }

    // End
    // H??m x??? l?? H???y l?? h??ng
    public Long remove(ShipmentDTO obj, KttsUserSession objUser) {
        if (!obj.getCreatedBy().equals(objUser.getSysUserId())) {
            throw new IllegalArgumentException(
                    "Ng?????i d??ng hi???n t???i kh??ng c?? quy???n s???a b???n ghi n??y");
        }
        return shipmentDAO.updateObject(obj.toModel());
    }

    // End
    // H??m x??? l?? ?????nh l?????ng h??ng h??a cho l?? h??ng
    public Long updateShipmentGoods(ShipmentDTO obj, HttpServletRequest request)
            throws Exception {
        if (!VpsPermissionChecker.checkPermissionOnDomainData(
                Constants.OperationKey.ESTIMATE,
                Constants.AdResourceKey.SHIPMENT, obj.getCreatedDeptId(),
                request)) {
            throw new IllegalArgumentException(
                    "B???n kh??ng c?? quy???n ?????nh l?????ng cho l?? h??ng n??y ");
        }
        Long ids = update(obj);
        if (ids != null) {
            for (int i = 0; i < obj.getLstShipmentGoods().size(); i++) {
                shipmentGoodsBusinessImpl.update(obj.getLstShipmentGoods().get(
                        i));
            }
        }
        return ids;
    }
    // End
}

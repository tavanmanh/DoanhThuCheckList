package com.viettel.asset.rest;

import com.viettel.asset.business.CatAssetCodeBusiness;
import com.viettel.asset.business.LongTermAssetBusiness;
import com.viettel.asset.business.MerHandOverInfoBusiness;
import com.viettel.asset.dto.AutocompleteSearchDto;
import com.viettel.asset.dto.LongTermAssetCostDto;
import com.viettel.asset.dto.LongTermAssetDto;
import com.viettel.asset.dto.MerEntityDto;
import com.viettel.asset.dto.search.AssetHandOverSearchDto;
import com.viettel.asset.dto.service.PagingDto;
import com.viettel.asset.filter.session.UserSession;
import com.viettel.erp.rest.FileServiceImpl;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.MULTIPART_FORM_DATA})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.MULTIPART_FORM_DATA})
public class LongTermAssetRsRService extends AbstractRsService {

    @Inject
    private UserSession userSession;
    @Autowired
    LongTermAssetBusiness longTermAssetBusiness;
    @Autowired
    CatAssetCodeBusiness catAssetCodeBusiness;
    @Autowired
    MerHandOverInfoBusiness merHandOverInfoBusiness;
    // @Value("${folder_upload2}")
    private String folderUpload;
    static Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * Lay lotaIndex tiep theo
     *
     * @param obj
     * @return
     * @throws Exception
     */
    @POST
    @Path("/getNewLotaIndex/")
    public Response getNewLotaIndex(LongTermAssetDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.getCaacFullCodeTemp(obj.getCatAssetCodeId())).build();
    }

    /*
     * tim kiem autocomplete tai san
     */
    @POST
    @Path("/getForAutoComplete/")
    public Response getForAutoComplete(LongTermAssetDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.getForAutoComplete(obj)).build();
    }

    /*
     * Lay cong trinh cho autocomplete Hien tai chua su dung o module tscd
     */
    @Deprecated
    @POST
    @Path("/getConstrConstructionsForAutoComplete/")
    public Response getConstrConstructionsAutoComplete(AutocompleteSearchDto query) throws Exception {
        return Response.ok(longTermAssetBusiness.getConstrConstructionForAutoComplete(query)).build();
    }

    /*
     * lay tram cho autocomplete hien tai chua su dung o module tscd
     */
    @Deprecated
    @POST
    @Path("/getStationForAutoComplete/")
    public Response getStationForAutoComplete(AutocompleteSearchDto query) throws Exception {
        return Response.ok(longTermAssetBusiness.getStationForAutoComplete(query)).build();
    }

    @POST
    @Path("/getSysGroupForAutoComplete/")
    public Response getSysGroupForAutoComplete(AutocompleteSearchDto query) throws Exception {
        return Response.ok(longTermAssetBusiness.getSysGroupForAutoComplete(query)).build();
    }

    /**
     * Tim kiem tai san khong phan trang (co the phuc vu su dung export excel
     * sau
     *
     * @param obj
     * @return
     * @throws Exception
     */
    @Deprecated
    @POST
    @Path("/search/")
    public Response search(LongTermAssetDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.getAllAsset(obj)).build();
    }

    /**
     * T??m ki???m t??i s???n
     *
     * @param pagingInfo
     * @param obj
     * @return
     * @throws Exception
     */
    @POST
    @Path("/searchLtaPaginate/")
    public Response searchLtaPaginate(@BeanParam PagingDto pagingInfo, LongTermAssetDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.searchLtaPaginate(obj, pagingInfo)).build();
    }

    /**
     * Export danh sach tai san
     *
     * @param obj
     * @return
     * @throws Exception
     */

    @Deprecated
    @POST
    @Path("/exportExcel/")
    public Response exportExcel(LongTermAssetDto obj) throws Exception {
        Map beans = new HashMap();
        List<LongTermAssetDto> list = longTermAssetBusiness.getAllAsset(obj);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRownum(i + 1);
            if (list.get(i).getLotaType() != null && list.get(i).getLotaType() == 1l) {
                list.get(i).setLotaTypeText("T??i s???n c??? ?????nh");
            } else if (list.get(i).getLotaType() != null && list.get(i).getLotaType() == 2l) {
                list.get(i).setLotaTypeText("C??ng c??? d???ng c???");
            }

            if (list.get(i).getIsSentErp() != null && list.get(i).getIsSentErp() == 0l) {
                list.get(i).setIsSentErpText("Ch??a ?????ng b???");
            } else if (list.get(i).getIsSentErp() != null && list.get(i).getIsSentErp() == 1l) {
                list.get(i).setIsSentErpText("???? ?????ng b???");
            }

            if (list.get(i).getLotaStatus() != null && list.get(i).getLotaStatus() == 0l) {
                list.get(i).setLotaStatusText("T???m t??ng");
            } else if (list.get(i).getLotaStatus() != null && list.get(i).getLotaStatus() == 1l) {
                list.get(i).setLotaStatusText("???? quy???t to??n");
            }

            if (list.get(i).getDepreciationStartDate() != null) {
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String datetext = format.format(list.get(i).getDepreciationStartDate());
                list.get(i).setStringDate(datetext);
            }
        }

        beans.put("assets", list);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream is = new BufferedInputStream(new FileInputStream(filePath + "ExportAsset.xlsx"));
        XLSTransformer transformer = new XLSTransformer();
        Workbook resultWorkbook = transformer.transformXLS(is, beans);
        is.close();
        saveWorkbook(resultWorkbook, folderUpload + "/ExportAsset.xlsx");

        return Response.ok(Collections.singletonMap("fileName", "ExportAsset.xlsx")).build();
    }

    private void saveWorkbook(Workbook resultWorkbook, String fileName) throws IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(fileName));
        resultWorkbook.write(os);
        os.flush();
        os.close();
    }

    /**
     * G???i t??i ch??nh
     *
     * @param id: id long_term_asset
     * @return response ok -> th??nh c??ng
     * @throws Exception
     */
    @GET
    @Path("/send/{id}")
    public Response send(@PathParam("id") Long id) throws Exception {
        longTermAssetBusiness.sendToErp(id);
        return Response.ok().build();
    }

    /**
     * X??a t??i s???n khi ch??a g???i sang t??i ch??nh th??nh c??ng
     *
     * @param id
     * @return
     * @throws Exception
     */
    @POST
    @Path("/remove/{id}/")
    public Response remove(@PathParam("id") Long id) throws Exception {
        longTermAssetBusiness.delete(id);
        return Response.ok().build();
    }

    @Deprecated
    @POST
    @Path("/getMerEntity/")
    public Response getMerEntity(MerEntityDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.getMerEntity(obj)).build();
    }

    @Deprecated
    @POST
    @Path("/getConstructionAcceptance/")
    public Response getConstructionAcceptance(MerEntityDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.getConstructionAcceptance(obj)).build();
    }

    /*
     * L???y th??ng tin bi??n b???n b??n giao kh??ng qua x??y l??p cho vi???c h??nh th??nh t??i
     * s???n c??? ?????nh
     */
    @GET
    @Path("/getConstructionAcceptanceById/{id}")
    public Response getConstructionAcceptanceById(@PathParam(value = "id") Long id) throws Exception {
        return Response.ok(longTermAssetBusiness.getConstructionAcceptanceById(id)).build();
    }

    @GET
    @Path("/getFolder")
    public Response getFolder() throws Exception {
        return Response.ok().entity(java.util.Collections.singletonMap("folder", folderUpload)).build();
    }

    @POST
    @Path("/add/")
    public Response addNew(LongTermAssetDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.addNew(obj)).build();
    }

    @POST
    @Path("/update/put/")
    public Response update(LongTermAssetDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.update(obj)).build();
    }

    @GET
    @Path("/getSerial/{id}")
    public Response getSerial(@PathParam("id") Long id) throws Exception {
        return Response.ok(longTermAssetBusiness.getSerial(id)).build();
    }

    @GET
    @Path("/getNoneSerial/{id}")
    public Response getNoneSerial(@PathParam("id") Long id) throws Exception {
        return Response.ok(longTermAssetBusiness.getNoneSerial(id)).build();
    }

    @GET
    @Path("/getDistri/{id}")
    public Response getDistri(@PathParam("id") Long id) throws Exception {
        return Response.ok(longTermAssetBusiness.getDistri(id)).build();
    }

    /*
     * T??m ki???m bi??n b???n b??n giao (c??? qua x??y l???p l???n ko qua x??y l???p
     */
    @POST
    @Path("/searchHandOver/")
    public Response searchHandover(AssetHandOverSearchDto handOverDto) throws Exception {
        return Response.ok(longTermAssetBusiness.searchHandover(handOverDto)).build();
    }

    /*
     * T??m ki???m bi??n b???n b??n giao qua x??y l???p
     */
    @POST
    @Path("/searchHandOverByConstruction/")
    public Response searchHandOverByConstruction(AssetHandOverSearchDto handOverDto) throws Exception {
        return Response.ok(longTermAssetBusiness.searchHandoverByConstruction(handOverDto)).build();
    }

    /*
     * T??m ki???m bi??n b???n b??n giao kh??ng qua x??y l???p
     */
    @POST
    @Path("/searchHandOverNotByConstruction/")
    public Response searchHandOverNotByConstruction(AssetHandOverSearchDto handOverDto) throws Exception {
        handOverDto.setUserSession(getUserSession());
        return Response.ok(longTermAssetBusiness.searchHandoverNotByConstruction(handOverDto)).build();
    }

    @POST
    @Path("/searchAssetCost/")
    public Response searchAssetCost(LongTermAssetCostDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.searchAssetCost(obj)).build();
    }

    @POST
    @Path("/getDetail")
    public Response getDetailAsset(LongTermAssetDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.getDetailAsset(obj.getLongTermAssetId())).build();
    }

    /// hanhls1 _lay du lieu cho merHandOver
    // @GET
    // @Path("/getMerhandOverToCreateLTAById/{id}")
    // public Response getMerhandOverToCreateLTAById(@PathParam(value="id") Long
    // id) throws Exception {
    // return
    // Response.ok(longTermAssetBusiness.getMerhandOverToCreateLTAById(id)).build();
    // }

    // Hanhls1 20170411: kiem tra ma tai san co thuoc loai bat buoc hinh thanh
    // tai san kong
    @GET
    @Path("/isRequiredToBeLTA")
    public Response isRequiredToBeLTA(@QueryParam("caacLevel") Long caacLevel,
                                      @QueryParam("catAssetCodeId") Long catAssetCodeId) throws Exception {
        return Response.ok(catAssetCodeBusiness.isRequiredToBeLTA(caacLevel, catAssetCodeId)).build();
    }

    /*
     * T??m ki???m bi??n b???n b??n giao n??ng c???p tr???m
     */
    @POST
    @Path("/searchHandOverUpgradeConstr/")
    public Response searchHandOverUpgradeConstr(AssetHandOverSearchDto handOverDto) throws Exception {
        handOverDto.setUserSession(getUserSession());
        return Response.ok(longTermAssetBusiness.searchHandOverUpgradeConstructionPaginate(handOverDto)).build();
    }

    /**
     * L???y th??ng tin bi??n b???n b??n giao, k??m th??ng tin t??i s???n c??? ?????nh
     *
     * @param handOverId
     * @return
     * @throws Exception
     */
    @POST
    @Path("/loadMerHandOverNotByConstrForUpgrade/")
    public Response loadMerHandOverNotByConstrForUpgrade(Long handOverId) throws Exception {
        return Response.ok(merHandOverInfoBusiness.loadMerHandOverNotByConstrForUpgrade(handOverId)).build();
    }

    ;

    /**
     * H???y n??ng c???p t??i s???n kh??ng qua x??y l???p
     *
     * @param longTermAssetId
     * @return
     * @throws Exception
     */
    @POST
    @Path("/cancelUpgradeLta/")
    public Response cancelUpgradeNotByConstr(LongTermAssetDto obj) throws Exception {
        UserSession userSession = getUserSession();
        return Response.ok(longTermAssetBusiness.cancelUpgradeByConstr(obj, userSession)).build();
    }

    ;

    /**
     * Upgrade Ts
     *
     * @param obj
     * @return
     * @throws Exception
     */
    @POST
    @Path("/upgradeLta/")
    public Response upgradeLta(LongTermAssetDto obj) throws Exception {
        UserSession s = getUserSession();
        return Response.ok(longTermAssetBusiness.upgradeLta(obj, s)).build();
    }

    /**
     * Autocomplete t??m ki???m t??i s???n
     *
     * @param autoSearchForm
     * @return
     */
    @POST
    @Path("/autocompleteConstrFromOfferSlip/")
    public Response autocompleteConstrFromOfferSlip(AutocompleteSearchDto autoSearchForm) {
        return Response.ok(longTermAssetBusiness.autocompleteConstrFromOfferSlip(autoSearchForm)).build();
    }

    /**
     * T???o m???i
     *
     * @param obj
     * @return
     * @throws Exception
     */
    @POST
    @Path("/createLtaFromOfferSlip/")
    public Response createLtaFromOfferSlip(LongTermAssetDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.createLtaFromOfferSlip(obj)).build();
    }

    /**
     * Update
     *
     * @param obj
     * @return
     * @throws Exception
     */
    @POST
    @Path("/updateLtaFromOfferSlip/")
    public Response updateLtaFromOfferSlip(LongTermAssetDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.updateLtaFromOfferSlip(obj)).build();
    }

    @POST
    @Path("/getLtaAttachDetail/")
    public Response getLtaAttachDetail(LongTermAssetCostDto obj) throws Exception {
        return Response.ok(longTermAssetBusiness.getLtaAttachDetail(obj)).build();
    }

}

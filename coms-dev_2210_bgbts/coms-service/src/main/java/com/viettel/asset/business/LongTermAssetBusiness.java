package com.viettel.asset.business;

import com.google.gson.Gson;
import com.viettel.asset.bo.*;
import com.viettel.asset.dao.*;
import com.viettel.asset.dto.*;
import com.viettel.asset.dto.search.AssetHandOverDto;
import com.viettel.asset.dto.search.AssetHandOverSearchDto;
import com.viettel.asset.dto.search.ConstructionAcceptanceDto;
import com.viettel.asset.dto.search.VConstructionOfferSlip;
import com.viettel.asset.dto.service.PagingDto;
import com.viettel.asset.filter.session.UserSession;
import com.viettel.erp.business.UtilAttachedDocumentsBusinessImpl;
import com.viettel.erp.dao.UtilAttachedDocumentsDAO;
import com.viettel.erp.dto.UtilAttachedDocumentsDTO;
import com.viettel.ktts2.common.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class LongTermAssetBusiness {

    @Value("${folder_upload2}")
    private String folderUpload;
    @Value("${asset.lstStatusAllowEdit}")
    private String lstStatusAllowEdit;

    private List<String> getLtaStatusAllowEdit() {
        return Arrays.asList(lstStatusAllowEdit.split(","));

    }

    @Value("${linkErp}")
    private String linkErp;

    @Value("${catAsset.attachTypeKey}")
    private Long attachAssetTypeKey;

    @Value("${catAssetVourcher.attachTypeKey}")
    private Long attachVourcherTypeKey;

    @Value("${ltaAttachDetail.attachTypeKey}")
    private Long longTermAssetAttachDetailTypeKey;

    @Autowired
    LongTermAssetDao longTermAssetDao;

    @Autowired
    CatAssetCodeDao catAssetCodeDao;

    @Autowired
    LongTermAssetHistoryDao longTermAssetHistoryDao;

    @Autowired
    LongTermAssetEntityDao longTermAssetEntityDao;
    @Autowired
    LongTermAssetAttachDetailDao longTermAssetAttachDetailDao;

    @Autowired
    LongTermAssetCostDao longTermAssetCostDao;

    @Autowired
    LongTermAssetVoucherDao longTermAssetVoucherDao;

    @Autowired
    BusinessLogBusiness businessLogBusiness;

    @Autowired
    MerEntityDao merEntityDao;

    @Autowired
    MerHandOverInfoDao merHandOverInfoDao;

    @Autowired
    SysGroupDao sysGroupDao;

    @Autowired
    UtilAttachedDocumentsBusinessImpl utilAttachedDocumentsBusinessImpl;
    @Autowired
    UtilAttachedDocumentsDAO utilAttachedDocumentsDAO;

    private static final Long LONG_TERM_ASSET_UPGRADE_STATUS_CREATED = 2l;
    private static final Long LONG_TERM_ASSET_UPGRADE_STATUS_NOT_CREATED_BEFORE = 1l;
    private static final Long LTA_ENTITY_STATUS_NOT_SEND_ERP = 1l;
    private static final Long LONG_TERM_ASSET_UPGRADE_STATUS_SENT_ERP = 3l;
    private static final Long LONG_TERM_ASSET_UPGRADE_STATUS_SENT_ERP_FAIL = 4l;
    private static final Long LONG_TERM_ASSET_UPGRADE_APPROVAL = null;


    /**
     * PhuongH1 - Cap nhat thong tin khau hao tu ERP
     *
     * @param dto
     * @throws Exception
     */
    public void updateDepreciationFromErp(LongTermAssetHistoryDto dto) throws Exception {
        LongTermAsset longTermAsset = longTermAssetDao.getByLotaCode(dto.getLotaCode());
        if (longTermAsset == null) {
            throw new BusinessException(String.format("Kh??ng t???n t???i t??i s???n c?? m??: %s", dto.getLotaCode()));
        }
        if (dto.getDepreciatedYear() == null) {
            throw new BusinessException("depreciatedYear kh??ng ???????c ????? tr???ng");
        }
        if (dto.getDepreciatedMonth() == null) {
            throw new BusinessException("depreciatedMonth kh??ng ???????c ????? tr???ng");
        }

        Map<String, Object> mapValue = new HashMap<>();
        String oldValue;
        mapValue.put(LongTermAsset.Constants.TABLE_NAME, longTermAsset);

        LongTermAssetHistory longTermAssetHistory = longTermAssetHistoryDao.getDepreciatedRow(
                longTermAsset.getLongTermAssetId(), dto.getDepreciatedMonth(), dto.getDepreciatedYear());

        boolean isNew = false;
        if (longTermAssetHistory == null) {
            /* Insert m???i b???n ghi */
            isNew = true;
            longTermAssetHistory = new LongTermAssetHistory();

            longTermAssetHistory.setDepreciatedDate(dto.getDepreciatedDate());
            longTermAssetHistory.setDepreciatedMonth(dto.getDepreciatedMonth());
            longTermAssetHistory.setDepreciatedMonthValue(dto.getDepreciatedMonthValue());
            longTermAssetHistory.setDepreciatedTime(dto.getDepreciatedTime());
            longTermAssetHistory.setDepreciatedValue(dto.getDepreciatedValue());
            longTermAssetHistory.setDepreciatedYear(dto.getDepreciatedYear());
            longTermAssetHistory.setLtahDate(dto.getDepreciatedDate());
            longTermAssetHistory.setCreatedDate(Calendar.getInstance().getTime());
        } else {
            mapValue.put(LongTermAssetHistory.Constants.TABLE_NAME, longTermAssetHistory);
        }

        Gson gson = new Gson();
        oldValue = gson.toJson(mapValue);

        if (isNew) {
            longTermAssetHistory.setLtahType(LongTermAssetHistory.Constants.LTAH_TYPE_DEPRECIATION);
            longTermAssetHistory.setGroupId(longTermAsset.getGroupId());
            longTermAssetHistory.setUseGroupId(longTermAsset.getUseGroupId());
            longTermAssetHistory.setLongTermAssetId(longTermAsset.getLongTermAssetId());
            longTermAssetHistory.setLotaCode(longTermAsset.getLotaCode());
            longTermAssetHistory.setLtahNewValue(dto.getDepreciatedMonthValue());
        }
        longTermAssetHistoryDao.insertOrUpdate(longTermAssetHistory);

        longTermAsset.setDepreciatedTime(dto.getDepreciatedTime());
        longTermAsset.setDepreciatiedValue(dto.getDepreciatedValue());
        longTermAsset.setResidualPrice(longTermAsset.getOriginalPrice() - longTermAsset.getDepreciatiedValue());
        longTermAsset.setIsSentErp(2l);// cap nhat ve status 2
        longTermAssetDao.update(longTermAsset);

        /* Insert log business */
        BusinessLogDto businessLogDto = new BusinessLogDto();
        businessLogDto
                .setBulAction(isNew ? BusinessLog.Constants.BulAction.INSERT : BusinessLog.Constants.BulAction.UPDATE);
        businessLogDto.setOldValue(oldValue);
        businessLogDto.setNewValue(gson.toJson(mapValue));
        businessLogDto.setDescription("Update Depreciation Info from ERP System");
        businessLogDto.setCreatedDate(new Date());
        List<String> lstDbTable = businessLogDto.getLstDbTable();
        lstDbTable.add(LongTermAssetHistory.Constants.TABLE_NAME);
        lstDbTable.add(LongTermAsset.Constants.TABLE_NAME);
        businessLogDto.setMainId(longTermAssetHistory.getLongTermAssetHistoryId());
        businessLogBusiness.insert(businessLogDto);

    }

    /**
     * PhuongH1 - Cap nhat thong tin khau hao tu ERP
     *
     * @param lstDto
     * @throws Exception
     */
    public void updateDepreciationFromErp(List<LongTermAssetHistoryDto> lstDto) throws Exception {
        for (LongTermAssetHistoryDto longTermAssetHistoryDto : lstDto) {
            updateDepreciationFromErp(longTermAssetHistoryDto);
        }
    }

    public void updateInfoFromErp(LongTermAssetDto dto) throws Exception {
        LongTermAsset longTermAsset = longTermAssetDao.getByLotaCode(dto.getLotaCode());
        if (longTermAsset == null) {
            throw new BusinessException(String.format("Kh??ng t???n t???i t??i s???n c?? m??: %s", dto.getLotaCode()));
        }
        Gson gson = new Gson();
        String oldValue = gson.toJson(longTermAsset);

        longTermAsset.setDepreciationTime(dto.getDepreciationTime());
        if (null != longTermAsset.getDepreciatedTime()) {
            longTermAsset.setDepreciationRate(100 / dto.getDepreciationTime());
        }
        if (1l != dto.getLotaType() && 2l != dto.getLotaType()) {
            throw new BusinessException(String.format("lota_type khong hop le lota_type=%d", dto.getLotaType()));
        }
        longTermAsset.setLotaType(dto.getLotaType());
        longTermAssetDao.update(longTermAsset);

        /* Insert log business */
        BusinessLogDto businessLogDto = new BusinessLogDto();
        businessLogDto.setBulAction(BusinessLog.Constants.BulAction.UPDATE);
        businessLogDto.setOldValue(oldValue);
        businessLogDto.setNewValue(gson.toJson(longTermAsset));
        businessLogDto.setDescription("Update DepreciationTime from ERP System");
        businessLogDto.setCreatedDate(new Date());
        businessLogDto.getLstDbTable().add(LongTermAsset.Constants.TABLE_NAME);
        businessLogDto.setMainId(longTermAsset.getLongTermAssetId());
        businessLogBusiness.insert(businessLogDto);
    }

    public void updateLongTermAssetFailFromErp(LongTermAssetDto dto) throws Exception {
        LongTermAsset longTermAsset = longTermAssetDao.getByLotaCode(dto.getLotaCode());
        if (longTermAsset == null) {
            throw new BusinessException(String.format("Kh??ng t???n t???i t??i s???n c?? m??: %s", dto.getLotaCode()));
        }
        longTermAsset.setIsSentErp(dto.getIsSentErp());
        longTermAssetDao.update(longTermAsset);
    }

    /*
     * public LongTermAssetDto getNewLotaIndex(LongTermAssetDto obj) throws
     * Exception { return longTermAssetDao.getNewLotaIndex(obj); }
     */

    /*
     * Lay dto voi full_code va lotaIndex
     */
    public LongTermAssetDto getCaacFullCodeTemp(Long catAssetCodeId) {
        // Lay mac dinh la 300001
        Long maxLotaIndex = longTermAssetDao.getNextLotaIndex(catAssetCodeId, null);
        CatAssetCode caacFullCode = catAssetCodeDao.find(catAssetCodeId);
        LongTermAssetDto dto = new LongTermAssetDto();
        dto.setLotaIndex(maxLotaIndex);
        dto.setAssetGroupCode(caacFullCode.getCaacFullCode());
        return dto;
    }

    public List<LongTermAssetDto> getForAutoComplete(LongTermAssetDto obj) throws Exception {
        return longTermAssetDao.getForAutoComplete(obj);
    }

    public List<ConstrConstructionDto> getConstrConstructionForAutoComplete(AutocompleteSearchDto obj)
            throws Exception {
        return longTermAssetDao.getConstrConstructionForAutoComplete(obj);
    }

    public List<CatStationDto> getStationForAutoComplete(AutocompleteSearchDto obj) throws Exception {
        return longTermAssetDao.getStationForAutoComplete(obj);
    }

    public List<SysGroupDto> getSysGroupForAutoComplete(AutocompleteSearchDto obj) throws Exception {

        return longTermAssetDao.getSysGroupForAutoComplete(obj);
    }

    public List<LongTermAssetDto> getAllAsset(LongTermAssetDto obj) throws Exception {
        return longTermAssetDao.getAllAsset(obj);
    }

    public Page<LongTermAssetDto> searchLtaPaginate(LongTermAssetDto searchForm, PagingDto pageInfo) throws Exception {
        Page<LongTermAssetDto> pageEntity = longTermAssetDao.searchLtaPaginate(searchForm, pageInfo);
        return pageEntity;
    }

    public void delete(Long longTermAssetId) throws Exception {
        LongTermAsset obj = longTermAssetDao.getByLongTermAssetId(longTermAssetId);
        if (getLtaStatusAllowEdit().contains(obj.getIsSentErp())) {
            List<LongTermAssetCost> costlst = longTermAssetCostDao.getByLongTermAssetId(longTermAssetId);
            List<LongTermAssetEntity> entlst = longTermAssetEntityDao.getByLongTermAssetId(longTermAssetId);
            List<MerEntity> merlst = merEntityDao.find(MerEntity.Columns.LONG_TERM_ASSET_ID, longTermAssetId);
            if (merlst.size() > 0) {
                for (MerEntity mer : merlst) {
                    mer.setLongTermAssetId(null);
                    merEntityDao.update(mer);
                }
            }
            if (costlst.size() > 0) {
                for (LongTermAssetCost cost : costlst) {
                    List<LongTermAssetVoucher> voulst = longTermAssetVoucherDao
                            .getByLongTermAssetId(cost.getLongTermAssetCostId());
                    for (LongTermAssetVoucher vou : voulst) {
                        longTermAssetVoucherDao.delete(vou);
                    }
                    longTermAssetCostDao.delete(cost);
                }
            }
            if (entlst.size() > 0) {
                for (LongTermAssetEntity ent : entlst) {
                    longTermAssetEntityDao.delete(ent);
                }
            }
            obj.setIsActive(0L);
            longTermAssetDao.update(obj);
            if (1 == obj.getVoucherType()) {
                // cap nhat bien ban ban giao qua xay lap
                // CONSTRUCTION_ACCEPTANCE
                longTermAssetDao.updateConstructonAcceptToStatusNotAssetCreated(longTermAssetId);
            } else if (2 == obj.getVoucherType()) {
                // Cap nhat bien ban giao hinh hinh thanh khong qua xay lap
                // MER_HAND_OVER_INFO
                longTermAssetDao.updateMerhandOverInfoToStatusNotAssetCreated(obj.getHandoverCode());
            }
        } else {
            throw new BusinessException("Kh??ng th??? x??a b???n ghi ???? ???????c g???i qua ERP ");
        }
    }

    /**
     * Gui tai chinh
     *
     * @param longTermAssetId
     * @throws Exception
     */

    public void sendToErp(Long longTermAssetId) throws Exception {

        LongTermAsset obj = longTermAssetDao.getByLongTermAssetId(longTermAssetId);
        SysGroup sysGroup = sysGroupDao.find(obj.getGroupId());
        SysGroup sysGroupUser = sysGroupDao.find(obj.getUseGroupId());
        CatAssetCode asset = catAssetCodeDao.find(obj.getCatAssetCodeId());
        CatAssetCode assetSource = catAssetCodeDao.find(asset.getCaacParentId());
        CatAssetCode assetType = catAssetCodeDao.find(assetSource.getCaacParentId());
        CatAssetCode assetGroup = catAssetCodeDao.find(assetType.getCaacParentId());

        if (getLtaStatusAllowEdit().contains(obj.getIsSentErp().toString())) {
            obj.setIsSentErp(1L);

            AAssetDTO erp = new AAssetDTO();
            //Hanhls1: check gia tri organizationId c???a ????n v??? s??? d???ng v?? ????n v??? h???ch to??n
            if (sysGroup == null || sysGroup.getOrganizationId() == null) {
                throw new BusinessException("????n v??? h???ch to??n ch??a t???n t???i ho???c ch??a ???????c map v???i c??y ????n v??? t???p ??o??n");
            }
            if (sysGroupUser == null || sysGroupUser.getOrganizationId() == null) {
                throw new BusinessException("????n v??? s??? d???ng ch??a t???n t???i ho???c ch??a ???????c map v???i c??y ????n v??? t???p ??o??n");
            }

            //Chuy???n sang organizeId
            erp.setOrgValue(sysGroup.getOrganizationId().toString());
            //erp.setOrgValue(sysGroup.getGroupCode());

            erp.setCreateDate(new Date());
            //Chuy???n sang organize Id
            erp.setDepartmentValue(sysGroupUser.getOrganizationId().toString());
            //erp.setDepartmentValue(sysGroupUser.getGroupCode());
            // erp.setDepartmentValue(departmentValue);
            erp.setDocumentType(obj.getVoucherType()); // document_type ->
            // voucher_type -> loaij
            // chung tu
            erp.setSourceValue(obj.getCreatedSource()); // Ngu???n h??nh th??nh
            erp.setDepreciationAmount(0l);
            erp.setGroupValue(assetGroup.getCaacFullCode());// nh??m TS
            erp.setTypeValue(assetSource.getCaacFullCode());// Lo???i TS

            erp.setMonthDepreciation(obj.getDepreciationTime());// T???ng th???i
            // gian kh???u hao

            erp.setName(asset.getCaacName()); // t??n TS
            erp.setValue(obj.getLotaCode());// m?? TS
            erp.setBaseAmount(obj.getOriginalPrice());
            erp.setDateused(obj.getDepreciationStartDate());
            // Bien ban ban giao
            erp.setDocBbbg(obj.getHandoverCode() != null ? obj.getHandoverCode() : "");
            // Neu la bien ban khong qua xay lap thi lay them thong tin cong
            // tr??nh
            if (1 == obj.getVoucherType()) {
                ConstructionAcceptanceDto dto = longTermAssetDao
                        .getConstructionInfoFromConstructionAcceptanceCode(obj.getHandoverCode());
                if (dto != null) {
                    erp.setConstructionValue(dto.getConstrCode() != null ? dto.getConstrCode() : "");
                    erp.setProjectValue(dto.getInvestProjectCode() != null ? dto.getInvestProjectCode() : "");
                }
            }
            erp.setType(obj.getLotaType() != null ? obj.getLotaType().toString() : null);
            erp.setTypeEstimate(obj.getLotaStatus() + 1);// tang 1

            // @TODO: log ban tin di
            URestService.getInstance().post(linkErp, erp);
            // End gui ban tin sang ERp

            longTermAssetDao.update(obj);

            LongTermAssetVoucher vou = new LongTermAssetVoucher();

            vou.setVoucherType(2L);// voucher type l?? h??nh th??nh
            vou.setObjectId(longTermAssetId);
            vou.setVoucherCode(obj.getHandoverCode());
            vou.setVoucherDate(new Date());

            Long vouId = longTermAssetVoucherDao.insert(vou);

            LongTermAssetHistory his = new LongTermAssetHistory();

            his.setGroupId(obj.getGroupId());
            his.setUseGroupId(obj.getUseGroupId());
            his.setLongTermAssetId(obj.getLongTermAssetId());
            his.setDepreciatedTime(obj.getDepreciatedTime());
            his.setLtahNewValue(obj.getOriginalPrice());
            his.setLtahOldValue(0l);
            his.setLtahValue(obj.getOriginalPrice());
            his.setDepreciatedValue(obj.getDepreciatiedValue());
            his.setLotaCode(obj.getLotaCode());
            his.setLtahType(1L);
            his.setHandoverCode(obj.getHandoverCode());
            his.setCreatedDate(new Date());
            his.setLongTermAssetVoucherId(vouId);
            his.setLtahDate(new Date());
            // Fix sonal
            // Long hisId = longTermAssetHistoryDao.insert(his);
            longTermAssetHistoryDao.insert(his);
        } else {
            throw new BusinessException("B???n ghi ???? ???????c g???i qua ERP");
        }
    }

    public List<MerEntityDto> getMerEntity(MerEntityDto obj) throws Exception {
        return merEntityDao.doSearch(obj);
    }

    @Deprecated
    public List<MerEntityDto> getConstructionAcceptance(MerEntityDto obj) throws Exception {
        return longTermAssetDao.getConstructionAcceptance(obj);
    }

    // Hanhls1: lay danh sach cac TODO:
    public ConstructionAcceptanceDto getConstructionAcceptanceById(Long constructionAcceptanceId) throws Exception {
        // Lay thong tin constructionAcceptance
        ConstructionAcceptanceDto dto = longTermAssetDao.getConstructionAcceptanceById(constructionAcceptanceId);
        // Lay thong tin merEntity thuoc constructionAcceptance nay

        // List<MerEntityDto> lst=
        // longTermAssetEntityDao.getListMerEntityByConstructId(dto.getConstructId(),null);
        List<MerEntityDto> lst = longTermAssetEntityDao.getListConstrAccepMerListByConstructId(dto.getConstructId(),
                null);
        dto.setLstMerEntity(lst);
        dto.setLstDataConstructionAcceptance(
                longTermAssetDao.getChiPhiDataConstructionAcceptance(dto.getConstructionAcceptanceId()));

        return dto;
    }

    /**
     * upgrade t??i s???n kh??ng qua x??y l???p
     *
     * @param obj
     * @param userInfo
     * @return
     * @throws Exception
     */
    public LongTermAssetDto upgradeLta(LongTermAssetDto obj, UserSession userInfo) throws Exception {
        LongTermAsset longTermAssetBO;
        MerHandOverInfo mm = null;
        List<MerHandOverEntityDto> ltsMerHandOverEntity = null;
        Long totalIncrease = 0l;
        mm = merHandOverInfoDao.find(obj.getHandOverId());
        ltsMerHandOverEntity = merHandOverInfoDao.getMerHandOverEntitybyId(obj.getHandOverId());
        if (obj.getLongTermAssetId() != null) {
            longTermAssetBO = longTermAssetDao.find(obj.getLongTermAssetId());
            if (!LONG_TERM_ASSET_UPGRADE_STATUS_NOT_CREATED_BEFORE.equals(longTermAssetBO.getUpgradeStatus())) {
                longTermAssetBO.setUpgradeStatus(LONG_TERM_ASSET_UPGRADE_STATUS_CREATED);
            }

        } else {
            longTermAssetBO = obj.toEntity();
            longTermAssetBO.setOriginalPrice(0l);

            mm.setIsToAsset(1l);
            merHandOverInfoDao.update(mm);
            // entity.set
            CatAssetCodeDto dongTS = catAssetCodeDao.getDongTSByChildIdAndLevel(longTermAssetBO.getCatAssetCodeId(), 4l);
            if (dongTS.getDepreciationTime() != null && dongTS.getDepreciationTime() != 0) {
                longTermAssetBO.setDepreciationTime(dongTS.getDepreciationTime());
                longTermAssetBO.setDepreciationRate(100 / dongTS.getDepreciationTime());
            }
            longTermAssetBO.setDepreciationStartDate(mm.getHandOverDate());
            longTermAssetBO.setIsActive(1l);
            longTermAssetBO.setLotaStatus(1l);
            longTermAssetBO.setIsSentErp(2l);
            longTermAssetBO.setHandoverCode(mm.getCode());
            longTermAssetBO.setCreatorDate(Calendar.getInstance().getTime());
            longTermAssetBO.setCreatorId(userInfo.getUserId());
            longTermAssetBO.setUpdatorDate(Calendar.getInstance().getTime());
            longTermAssetBO.setCreatedGroupId(userInfo.getGroupId());
            longTermAssetBO.setConstructId(mm.getConstructionId());

            longTermAssetBO.setUpgradeStatus(LONG_TERM_ASSET_UPGRADE_STATUS_NOT_CREATED_BEFORE);
//			entity.setStationId(mm.get);
        }
        for (MerHandOverEntityDto result : ltsMerHandOverEntity) {
            Long total = (long) (result.getCount().doubleValue() * result.getOriginalPrice().longValue());
            totalIncrease = totalIncrease + total;
        }
        longTermAssetBO.setOriginalPrice(longTermAssetBO.getOriginalPrice() + totalIncrease);


        //Truong hop t???o m???i t??i s???n m???i th??m file ????nh k??m
        if (obj.getLongTermAssetId() == null) {
            Long id = longTermAssetDao.insert(longTermAssetBO);
            longTermAssetDao.setLtaIndexAndCode(id, longTermAssetBO.getCatAssetCodeId());
            if (StringUtils.isNotEmpty(obj.getHasUpload()) && StringUtils.isNotEmpty(obj.getAttachName())) {
                String documentPath = UEncrypt.decryptFileUploadPath(obj.getAttachName());
                String documentName = UString.extractFileNameFromPath(documentPath);
                utilAttachedDocumentsBusinessImpl.insert(documentName, longTermAssetBO.getLongTermAssetId(), documentPath,
                        attachAssetTypeKey);
            }
        } else {
            longTermAssetDao.update(longTermAssetBO);
        }
        for (MerHandOverEntityDto result : ltsMerHandOverEntity) {
            MerEntity merEntity = merEntityDao.find(result.getMerEntityId());
            merEntity.setLongTermAssetId(result.getLongTermAssetId());
            merEntityDao.update(merEntity);
            LongTermAssetEntity longTermAssetEntity = new LongTermAssetEntity();
            longTermAssetEntity.setLongTermAssetId(longTermAssetBO.getLongTermAssetId());
            longTermAssetEntity.setMerEntityId(result.getMerEntityId());
            longTermAssetEntity.setOriginalPrice(result.getOriginalPrice());
            longTermAssetEntity.setQuantity(result.getCount());
            longTermAssetEntity.setUpgradeStatus(LONG_TERM_ASSET_UPGRADE_STATUS_NOT_CREATED_BEFORE);//gia tri LONG_TERM_ASSET_UPGRADE_STATUS_CREATED=2 l?? ????
            longTermAssetEntity.setHandOverUpgradeId(mm.getHandOverId());
            longTermAssetEntityDao.insert(longTermAssetEntity);
        }

        return obj;
    }


    /**
     * Cancel
     *
     * @param obj
     * @param userInfo
     * @return
     * @throws Exception
     */
    public LongTermAssetDto cancelUpgradeByConstr(LongTermAssetDto obj, UserSession userInfo) throws Exception {
        LongTermAsset longTermAssetBO = longTermAssetDao.find(obj.getLongTermAssetId());
        if (longTermAssetBO == null) {
            throw new BusinessException("T??i s???n kh??ng t???n t???i");
        }

        if (longTermAssetBO.getUpgradeStatus() == null || longTermAssetBO.getUpgradeStatus() == LONG_TERM_ASSET_UPGRADE_STATUS_SENT_ERP) {
            throw new BusinessException("T??i s???n n??ng c???p ???? ???????c g???i t??i ch??nh th??nh c??ng");
        }
        //Tr?????ng h???p h??nh th??nh m?? t??i s???n m???i trong vi???c n??ng c???p x??a t??i s???n
        if (LONG_TERM_ASSET_UPGRADE_STATUS_NOT_CREATED_BEFORE.equals(longTermAssetBO.getUpgradeStatus())) {
            longTermAssetBO.setIsActive(0l);
        } else if (LONG_TERM_ASSET_UPGRADE_STATUS_CREATED.equals(longTermAssetBO.getUpgradeStatus())) {
            longTermAssetBO.setUpgradeStatus(null);
        } else {
            throw new BusinessException("Upgrade t??i s???n ???? ???????c g???i sang t??i ch??nh, kh??ng ???????c h???y");
        }

        longTermAssetDao.update(longTermAssetBO);

        Long[] lstLTANotSendToErp = {LTA_ENTITY_STATUS_NOT_SEND_ERP};

        //Lay cac id bien ban nang cap tai san
        List<Long> lstHandOverId = longTermAssetDao.getListMerHandOverUpgradeLtaByLtaId(obj.getLongTermAssetId(), Arrays.asList(lstLTANotSendToErp));
        //Cap nhat trang thai handOver ve chua hinh thanh tai s???n c??? ?????nh
        longTermAssetDao.updateMerhandOverInfoToStatusNotAssetCreatedByHandOverId(lstHandOverId);
        //xoa tia san kem theo
        longTermAssetDao.deleteListLTAEntityInUpgradeProcess(obj.getLongTermAssetId(), lstHandOverId);
        return null;
    }

    /**
     * T???o t??i s???n qua x??y lwps m???i
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public LongTermAssetDto addNew(LongTermAssetDto obj) throws Exception {
        LongTermAsset entity = obj.toEntity();
        // Luu thong tin chung vao bang LONG_TERM_ASSET
        entity.setVoucherType(1l);
        entity.setLotaIndex(null);

        // entity.setLotaIndex(null);
        CatAssetCodeDto dongTS = catAssetCodeDao.getDongTSByChildIdAndLevel(entity.getCatAssetCodeId(), 4l);
        if (dongTS.getDepreciationTime() != null && dongTS.getDepreciationTime() != 0) {
            entity.setDepreciationTime(dongTS.getDepreciationTime());
            entity.setDepreciationRate(100 / dongTS.getDepreciationTime());
        }

        Long id = longTermAssetDao.insert(entity);
        longTermAssetDao.setLtaIndexAndCode(id, entity.getCatAssetCodeId());
        if (StringUtils.isNotEmpty(obj.getHasUpload()) && StringUtils.isNotEmpty(obj.getAttachName())) {

            String documentPath = UEncrypt.decryptFileUploadPath(obj.getAttachName());
            String documentName = UString.extractFileNameFromPath(documentPath);
            utilAttachedDocumentsBusinessImpl.insert(documentName, id, documentPath, attachAssetTypeKey);
        }

        // Luu thong tin vao bang LONG_TERM_ASSET_ENTITIES??
        List<MerEntityDto> listMerEntities = obj.getListAssetEntities();
        for (MerEntityDto merEntities : listMerEntities) {
            LongTermAssetEntity assetEntity = new LongTermAssetEntity();

            assetEntity.setLongTermAssetId(id);
            assetEntity.setMerEntityId(merEntities.getMerEntityId());
            assetEntity.setQuantity(merEntities.getMerWeight());
            assetEntity.setOriginalPrice(merEntities.getVndUnitPrice());

            longTermAssetEntityDao.insert(assetEntity);

            // Cap nhat cot LONG_TERM_ASSET_ID trong bang MER_ENTITY
            MerEntity objUpdate = merEntityDao.find(merEntities.getMerEntityId());
            objUpdate.setLongTermAssetId(id);
            merEntityDao.update(objUpdate);
        }

        // Luu thong tin vao bang LONG_TERM_ASSET_COSTS??
        List<LongTermAssetCostDto> listAssetCost = obj.getListAssetCost();
        for (LongTermAssetCostDto assetCost : listAssetCost) {
            assetCost.setLongTermAssetId(id);
            LongTermAssetCost assetCostEntity = assetCost.toEntity();
            Long assetCostId = longTermAssetCostDao.insert(assetCostEntity);

            // Luu thong tin vao bang LONG_TERM_ASSET_VOUCHERS??
            LongTermAssetVoucher voucher = new LongTermAssetVoucher();
            voucher.setVoucherType(1l);
            voucher.setVoucherDate(assetCost.getVoucherDate());
            voucher.setVoucherValue(assetCost.getLoacValue());
            voucher.setVoucherCode(assetCost.getVoucherCode());
            voucher.setObjectId(assetCostId);

            Long voucherId = longTermAssetVoucherDao.insert(voucher);

            // Cap nhat du lieu vao bang UTIL_ATTACHED_DOCUMENTS
            if (StringUtils.isNotEmpty(assetCost.getHasUpload()) && StringUtils.isNotEmpty(assetCost.getAttachName())) {
                String assetCostAttachPath = UEncrypt.decryptFileUploadPath(assetCost.getAttachName());
                String assetCostAttachName = UString.extractFileNameFromPath(assetCostAttachPath);
                utilAttachedDocumentsBusinessImpl.insert(assetCostAttachName, voucherId, assetCostAttachPath,
                        attachVourcherTypeKey);
            }
        }

        // cap nhat trang thai hinh thanh tai san
        longTermAssetDao.updateConstructonAcceptToStatusAssetCreated(entity.getConstructId(),
                entity.getLongTermAssetId());

        obj.setLongTermAssetId(id);
        return obj;
    }

    public LongTermAssetDto update(LongTermAssetDto obj) throws Exception {
        LongTermAsset entity = obj.toEntity();
        // Luu thong tin chung vao bang LONG_TERM_ASSET
        entity.setVoucherType(1l);
        CatAssetCodeDto dongTS = catAssetCodeDao.getDongTSByChildIdAndLevel(entity.getCatAssetCodeId(), 4l);
        if (dongTS.getDepreciationTime() != null && dongTS.getDepreciationTime() != 0) {
            entity.setDepreciationTime(dongTS.getDepreciationTime());
            entity.setDepreciationRate(1200 / dongTS.getDepreciationTime());
        }
        longTermAssetDao.update(entity);
        if (StringUtils.isNotEmpty(obj.getHasUpload()) && StringUtils.isNotEmpty(obj.getAttachName())) {
            String documentPath = UEncrypt.decryptFileUploadPath(obj.getAttachName());
            String documentName = UString.extractFileNameFromPath(documentPath);

            UtilAttachedDocumentsDTO objAtt = utilAttachedDocumentsBusinessImpl
                    .findByParentIdAndType(obj.getLongTermAssetId(), attachAssetTypeKey);
            if (objAtt != null) {
                utilAttachedDocumentsBusinessImpl.updateUtilByParentIdAndType(documentName, obj.getLongTermAssetId(),
                        documentPath, attachAssetTypeKey);
            } else {
                utilAttachedDocumentsBusinessImpl.insert(documentName, obj.getLongTermAssetId(), documentPath,
                        attachAssetTypeKey);
            }
        }

        // Xoa thong tin cu trong LONG_TERM_ASSET_ENTITIES
        List<LongTermAssetEntity> listMerEntitiesSearch = longTermAssetEntityDao
                .find(LongTermAssetEntity.Columns.LONG_TERM_ASSET_ID, obj.getLongTermAssetId());
        for (LongTermAssetEntity merEntitiesSearch : listMerEntitiesSearch) {
            longTermAssetEntityDao.delete(merEntitiesSearch);
        }

        // Luu thong tin vao bang LONG_TERM_ASSET_ENTITIES
        List<MerEntityDto> listMerEntities = obj.getListAssetEntities();
        for (MerEntityDto merEntities : listMerEntities) {
            LongTermAssetEntity assetEntity = new LongTermAssetEntity();

            assetEntity.setLongTermAssetId(entity.getLongTermAssetId());
            assetEntity.setMerEntityId(merEntities.getMerEntityId());
            assetEntity.setQuantity(merEntities.getMerWeight());
            assetEntity.setOriginalPrice(merEntities.getVndUnitPrice());

            longTermAssetEntityDao.insert(assetEntity);

            // Cap nhat cot LONG_TERM_ASSET_ID trong bang MER_ENTITY
            MerEntity objUpdate = merEntityDao.find(merEntities.getMerEntityId());
            objUpdate.setLongTermAssetId(obj.getLongTermAssetId());
            merEntityDao.update(objUpdate);
        }

        // Xoa thong tin cu trong LONG_TERM_ASSET_COSTS
        List<LongTermAssetCost> listAssetCostSearch = longTermAssetCostDao
                .find(LongTermAssetCost.Columns.LONG_TERM_ASSET_ID, obj.getLongTermAssetId());
        for (LongTermAssetCost assetCostSearch : listAssetCostSearch) {
            // Xoa thong tin cu trong LONG_TERM_ASSET_VOUCHERS
            List<LongTermAssetVoucher> listVoucherSearch = longTermAssetVoucherDao
                    .find(LongTermAssetVoucher.Columns.OBJECT_ID, assetCostSearch.getLongTermAssetCostId());
            for (LongTermAssetVoucher voucherSearch : listVoucherSearch) {
                longTermAssetVoucherDao.delete(voucherSearch);
            }

            longTermAssetCostDao.delete(assetCostSearch);
        }

        // Luu thong tin vao bang LONG_TERM_ASSET_COSTS
        List<LongTermAssetCostDto> listAssetCost = obj.getListAssetCost();
        for (LongTermAssetCostDto assetCost : listAssetCost) {
            assetCost.setLongTermAssetId(entity.getLongTermAssetId());
            LongTermAssetCost assetCostEntity = assetCost.toEntity();
            Long assetCostId = longTermAssetCostDao.insert(assetCostEntity);

            // Luu thong tin vao bang LONG_TERM_ASSET_VOUCHERS??
            LongTermAssetVoucher voucher = new LongTermAssetVoucher();
            voucher.setVoucherType(1l);
            voucher.setVoucherDate(assetCost.getVoucherDate());
            voucher.setVoucherValue(assetCost.getLoacValue());
            voucher.setVoucherCode(assetCost.getVoucherCode());
            voucher.setObjectId(assetCostId);

            Long voucherId = longTermAssetVoucherDao.insert(voucher);

            // Cap nhat du lieu vao bang UTIL_ATTACHED_DOCUMENTS
            if (UString.isNotNullAndWhitespace(assetCost.getAttachIdEncrypted())) {
                String attachId = UEncrypt.decryptFileUploadPath(assetCost.getAttachIdEncrypted());
                utilAttachedDocumentsDAO.updateParentIdByAttachId(voucherId, Long.parseLong(attachId));

            } else if (StringUtils.isNotEmpty(assetCost.getHasUpload())
                    && StringUtils.isNotEmpty(assetCost.getAttachName())) {
                String documentPath = UEncrypt.decryptFileUploadPath(assetCost.getAttachName());
                String documentName = UString.extractFileNameFromPath(documentPath);

                if (assetCost.getLongTermAssetCostId() != null) {
                    UtilAttachedDocumentsDTO objAttVourcher = utilAttachedDocumentsBusinessImpl
                            .findByParentIdAndType(assetCost.getLongTermAssetCostId(), attachVourcherTypeKey);
                    if (objAttVourcher != null) {
                        objAttVourcher.setDocumentName(documentName);
                        objAttVourcher.setDocumentPath(documentPath);
                        objAttVourcher.setParentId(voucherId);

                        utilAttachedDocumentsBusinessImpl.update(objAttVourcher);
                    } else {
                        utilAttachedDocumentsBusinessImpl.insert(documentName, voucherId, documentPath,
                                attachVourcherTypeKey);
                    }
                } else {
                    utilAttachedDocumentsBusinessImpl.insert(documentName, voucherId, documentPath,
                            attachVourcherTypeKey);
                }
            } else if (StringUtils.isNotEmpty(assetCost.getAttachName())) {
                String documentPath = UEncrypt.decryptFileUploadPath(assetCost.getAttachName());
                String documentName = UString.extractFileNameFromPath(documentPath);
                if (assetCost.getLongTermAssetCostId() != null) {
                    UtilAttachedDocumentsDTO objAttVourcher = utilAttachedDocumentsBusinessImpl
                            .findByParentIdAndType(assetCost.getLongTermAssetCostId(), attachVourcherTypeKey);
                    if (objAttVourcher != null) {
                        objAttVourcher.setDocumentName(documentName);
                        objAttVourcher.setDocumentPath(documentPath);
                        objAttVourcher.setParentId(voucherId);

                        utilAttachedDocumentsBusinessImpl.update(objAttVourcher);
                    } else {
                        utilAttachedDocumentsBusinessImpl.insert(documentName, voucherId, documentPath,
                                attachVourcherTypeKey);
                    }
                }
            }
        }

        return new LongTermAssetDto(entity);
    }

    public List<LongTermAssetEntityDto> getNoneSerial(Long longTermAssetId) throws Exception {
        return longTermAssetEntityDao.getNoneSerial(longTermAssetId);
    }

    public List<LongTermAssetEntityDto> getSerial(Long longTermAssetId) throws Exception {
        return longTermAssetEntityDao.getSerial(longTermAssetId);
    }

    public List<LongTermAssetVoucherDto> getDistri(Long longTermAssetId) throws Exception {
        return longTermAssetVoucherDao.getDistri(longTermAssetId);
    }

    public List<LongTermAssetCostDto> searchAssetCost(LongTermAssetCostDto obj) throws Exception {
        List<LongTermAssetCostDto> list = longTermAssetCostDao.doSearch(obj);
        for (LongTermAssetCostDto item : list) {
            if (item.getAttachId() != null) {
                item.setAttachIdEncrypted(UEncrypt.encryptFileUploadPath(item.getAttachId().toString()));
            }
            if (UString.isNotNullAndWhitespace(item.getAttachName())) {
                item.setAttachDisplayName(item.getAttachName());
            }
        }
        return list;
    }

    public LongTermAssetDto getDetailAsset(Long id) throws Exception {
        LongTermAssetDto dto = longTermAssetDao.getDetailAsset(id);

        List<MerEntityDto> lst = longTermAssetEntityDao.getListConstrAccepMerListByConstructId(dto.getConstructId(),
                null);
        dto.setListAssetEntities(lst);
        return dto;
    }

    public Page<AssetHandOverDto> searchHandover(AssetHandOverSearchDto searchForm) throws Exception {
        Page<AssetHandOverDto> pageEntity = longTermAssetDao.searchHandOverPaginate(searchForm);
        return pageEntity;

    }

    public Page<AssetHandOverDto> searchHandoverByConstruction(AssetHandOverSearchDto searchForm) throws Exception {
        Page<AssetHandOverDto> pageEntity = longTermAssetDao.searchHandOverByConstructionPaginate(searchForm);
        return pageEntity;

    }

    /**
     * T??m ki???m
     *
     * @param searchForm
     * @return
     * @throws Exception
     */
    public Page<AssetHandOverDto> searchHandOverUpgradeConstructionPaginate(AssetHandOverSearchDto searchForm) throws Exception {
        Page<AssetHandOverDto> pageEntity = longTermAssetDao.searchHandOverUpgradeConstructionPaginate(searchForm);
        return pageEntity;

    }

    /**
     * T??m ki???m t??i s???n kh??ng qua x??y l???p
     *
     * @param searchForm: form t??m ki???m
     * @return danh s??ch th??ng tin bi??n b??n b??n giao t??i s???n ???????c paging
     * @throws Exception
     */
    public Page<AssetHandOverDto> searchHandoverNotByConstruction(AssetHandOverSearchDto searchForm) throws Exception {
        Page<AssetHandOverDto> pageEntity = longTermAssetDao.searchHandOverNotByConstructionPaginate(searchForm);
        return pageEntity;

    }

    public Object getMerhandOverToCreateLTAById(Long merHandOverId) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Th???c hi???n vi???c c???p nh???t tr???ng th??i c???a t??i s???n t??? t??i ch??nh
     *
     * @param longTermAssetDto
     * @throws DatabaseException
     */
    public void updateLongTermAssetStatusFromErp(LongTermAssetDto dto) throws DatabaseException {
        // TODO Auto-generated method stub

        Long status = dto.getIsSentErp();
        LongTermAsset lta = longTermAssetDao.getByLotaCode(dto.getLotaCode());
        if (lta == null) {
            throw new BusinessException(String.format("Kh??ng t???n t???i t??i s???n c?? m??: %s", dto.getLotaCode()));
        }
        if (2l == lta.getIsSentErp() || 3l == lta.getIsSentErp()) {
            throw new BusinessException(String.format("Ta?? s???n c?? m??: %s ???? ??? tr???ng th??i duy???t ho???c t??? ch???i, m?? tr???ng th??i: %d", dto.getLotaCode(), lta.getIsSentErp()));
        }
        if ((lta.getIsSentErp() != null && lta.getIsActive().equals(status)) || (2l != status && 3l != status)) {
            throw new BusinessException(String.format("T??i s???n hi???n ??ang c?? tr???ng th??i g???i t??i ch??nh l?? %s, tr???ng th??i ?????ng b??? sang l?? %s", lta.getIsSentErp(), dto.getIsSentErp()));
        }
        lta.setIsSentErp(status);
        if (3l == status) {
            if (dto.getErpFailReason() != null || dto.getErpFailReason().length() > 2000) {
                throw new BusinessException(String.format("L?? do t??? ch???i qu?? k??ch th?????c 2000, n???i dung:%s", lta.getErpFailReason()));
            }
            lta.setErpFailReason(dto.getErpFailReason());
        }
        lta.setUpdatorDate(new Date());
        longTermAssetDao.update(lta);


    }

    /**
     * T??m ki???m autocomplete
     *
     * @param autoSearchForm
     * @return
     * @throws DatabaseException
     */
    public List<VConstructionOfferSlip> autocompleteConstrFromOfferSlip(AutocompleteSearchDto autoSearchForm) {
        // TODO Auto-generated method stub
        return longTermAssetDao.autocompleteConstrFromOfferSlip(autoSearchForm);
    }

    /**
     * T???o m???i
     *
     * @param obj
     * @return
     * @throws Exception
     */

    public LongTermAssetDto createLtaFromOfferSlip(LongTermAssetDto obj) throws Exception {
        LongTermAsset entity = obj.toEntity();
        // Luu thong tin chung vao bang LONG_TERM_ASSET
        entity.setVoucherType(1l);
        entity.setLotaIndex(null);
        entity.setCreatorDate(new Date());
        entity.setIsOfferSlip(1l);
        entity.setUpdatorDate(new Date());

        // entity.setLotaIndex(null);
        CatAssetCodeDto dongTS = catAssetCodeDao.getDongTSByChildIdAndLevel(entity.getCatAssetCodeId(), 4l);
        if (dongTS.getDepreciationTime() != null && dongTS.getDepreciationTime() != 0) {
            entity.setDepreciationTime(dongTS.getDepreciationTime());
            entity.setDepreciationRate(100 / dongTS.getDepreciationTime());
        }

        Long id = longTermAssetDao.insert(entity);
        longTermAssetDao.setLtaIndexAndCode(id, entity.getCatAssetCodeId());

        // Luu thong tin vao bang LONG_TERM_ASSET_COSTS??
        List<LongTermAssetCostDto> listAssetCost = obj.getListAssetCost();
        for (LongTermAssetCostDto assetCost : listAssetCost) {
            assetCost.setLongTermAssetId(id);
            LongTermAssetCost assetCostEntity = assetCost.toEntity();
            Long assetCostId = longTermAssetCostDao.insert(assetCostEntity);

            // Luu thong tin vao bang LONG_TERM_ASSET_VOUCHERS??
            LongTermAssetVoucher voucher = new LongTermAssetVoucher();
            voucher.setVoucherType(1l);
            voucher.setVoucherDate(assetCost.getVoucherDate());
            voucher.setVoucherValue(assetCost.getLoacValue());
            voucher.setVoucherCode(assetCost.getVoucherCode());
            voucher.setObjectId(assetCostId);

            Long voucherId = longTermAssetVoucherDao.insert(voucher);

            // Cap nhat du lieu vao bang UTIL_ATTACHED_DOCUMENTS
            if (StringUtils.isNotEmpty(assetCost.getAttachName())) {
                String assetCostAttachPath = UEncrypt.decryptFileUploadPath(assetCost.getAttachName());
                String assetCostAttachName = UString.extractFileNameFromPath(assetCostAttachPath);
                utilAttachedDocumentsBusinessImpl.insert(assetCostAttachName, voucherId, assetCostAttachPath, attachVourcherTypeKey
                );
            }
        }
        if (obj.getLstAttachDetailDto() != null) {
            for (LongTermAssetAttachDetailDto attachDto : obj.getLstAttachDetailDto()) {
                attachDto.setLongTermAssetId(id);
                Long longTermAssetAttachId = longTermAssetAttachDetailDao.insert(attachDto.toEntity());
                if (StringUtils.isNotEmpty(attachDto.getAttachName())) {
                    String assetCostAttachPath = UEncrypt.decryptFileUploadPath(attachDto.getAttachName());
                    String assetCostAttachName = UString.extractFileNameFromPath(assetCostAttachPath);
                    utilAttachedDocumentsBusinessImpl.insert(assetCostAttachName, longTermAssetAttachId, assetCostAttachPath,
                            longTermAssetAttachDetailTypeKey);
                }
            }
        }

        // cap nhat trang thai hinh thanh tai san
		/*longTermAssetDao.updateConstructonAcceptToStatusAssetCreated(entity.getConstructId(),
				entity.getLongTermAssetId());*/

        obj.setLongTermAssetId(id);
        return obj;
    }

    public LongTermAssetDto updateLtaFromOfferSlip(LongTermAssetDto obj) throws Exception {
        LongTermAsset entity = obj.toEntity();
        // Luu thong tin chung vao bang LONG_TERM_ASSET
        entity.setVoucherType(1l);
        entity.setIsOfferSlip(1l);//Caapj nhat la hinh thanh tu de nghi
        CatAssetCodeDto dongTS = catAssetCodeDao.getDongTSByChildIdAndLevel(entity.getCatAssetCodeId(), 4l);
        if (dongTS.getDepreciationTime() != null && dongTS.getDepreciationTime() != 0) {
            entity.setDepreciationTime(dongTS.getDepreciationTime());
            entity.setDepreciationRate(1200 / dongTS.getDepreciationTime());
        }
        longTermAssetDao.update(entity);
        //B??? ph???n
	/*	if (StringUtils.isNotEmpty(obj.getHasUpload()) && StringUtils.isNotEmpty(obj.getAttachName())) {
			String documentPath = UEncrypt.decryptFileUploadPath(obj.getAttachName());
			String documentName = UString.extractFileNameFromPath(documentPath);

			UtilAttachedDocumentsDTO objAtt = utilAttachedDocumentsBusinessImpl
					.findByParentIdAndType(obj.getLongTermAssetId(), attachAssetTypeKey);
			if (objAtt != null) {
				utilAttachedDocumentsBusinessImpl.updateUtilByParentIdAndType(documentName, obj.getLongTermAssetId(),
						documentPath, attachAssetTypeKey);
			} else {
				utilAttachedDocumentsBusinessImpl.insert(documentName, obj.getLongTermAssetId(), documentPath,
						attachAssetTypeKey);
			}
		}*/

        // Xoa thong tin cu trong LONG_TERM_ASSET_COSTS
        List<LongTermAssetCost> listAssetCostSearch = longTermAssetCostDao
                .find(LongTermAssetCost.Columns.LONG_TERM_ASSET_ID, obj.getLongTermAssetId());
        List<LongTermAssetAttachDetail> listLtaAttachDetail = longTermAssetAttachDetailDao.find(LongTermAssetCost.Columns.LONG_TERM_ASSET_ID, obj.getLongTermAssetId());
        for (LongTermAssetCost assetCostSearch : listAssetCostSearch) {
            // Xoa thong tin cu trong LONG_TERM_ASSET_VOUCHERS
            List<LongTermAssetVoucher> listVoucherSearch = longTermAssetVoucherDao
                    .find(LongTermAssetVoucher.Columns.OBJECT_ID, assetCostSearch.getLongTermAssetCostId());
            for (LongTermAssetVoucher voucherSearch : listVoucherSearch) {
                longTermAssetVoucherDao.delete(voucherSearch);
            }

            longTermAssetCostDao.delete(assetCostSearch);
        }
        for (LongTermAssetAttachDetail detail : listLtaAttachDetail) {
            longTermAssetAttachDetailDao.delete(detail);
        }

        // Luu thong tin vao bang LONG_TERM_ASSET_COSTS
        List<LongTermAssetCostDto> listAssetCost = obj.getListAssetCost();
        for (LongTermAssetCostDto assetCost : listAssetCost) {
            assetCost.setLongTermAssetId(entity.getLongTermAssetId());
            LongTermAssetCost assetCostEntity = assetCost.toEntity();
            Long assetCostId = longTermAssetCostDao.insert(assetCostEntity);

            // Luu thong tin vao bang LONG_TERM_ASSET_VOUCHERS??
            LongTermAssetVoucher voucher = new LongTermAssetVoucher();
            voucher.setVoucherType(1l);
            voucher.setVoucherDate(assetCost.getVoucherDate());
            voucher.setVoucherValue(assetCost.getLoacValue());
            voucher.setVoucherCode(assetCost.getVoucherCode());
            voucher.setObjectId(assetCostId);

            Long voucherId = longTermAssetVoucherDao.insert(voucher);

            // Cap nhat du lieu vao bang UTIL_ATTACHED_DOCUMENTS
            if (UString.isNotNullAndWhitespace(assetCost.getAttachIdEncrypted())) {
                String attachId = UEncrypt.decryptFileUploadPath(assetCost.getAttachIdEncrypted());
                utilAttachedDocumentsDAO.updateParentIdByAttachId(voucherId, Long.parseLong(attachId));

            } else if (StringUtils.isNotEmpty(assetCost.getAttachName())) {
                String documentPath = UEncrypt.decryptFileUploadPath(assetCost.getAttachName());
                String documentName = UString.extractFileNameFromPath(documentPath);

                if (assetCost.getLongTermAssetCostId() != null) {
                    UtilAttachedDocumentsDTO objAttVourcher = utilAttachedDocumentsBusinessImpl
                            .findByParentIdAndType(assetCost.getLongTermAssetCostId(), attachVourcherTypeKey);
                    if (objAttVourcher != null) {
                        objAttVourcher.setDocumentName(documentName);
                        objAttVourcher.setDocumentPath(documentPath);
                        objAttVourcher.setParentId(voucherId);

                        utilAttachedDocumentsBusinessImpl.update(objAttVourcher);
                    } else {
                        utilAttachedDocumentsBusinessImpl.insert(documentName, voucherId, documentPath,
                                attachVourcherTypeKey);
                    }
                } else {
                    utilAttachedDocumentsBusinessImpl.insert(documentName, voucherId, documentPath,
                            attachVourcherTypeKey);
                }
            } else if (StringUtils.isNotEmpty(assetCost.getAttachName())) {
                String documentPath = UEncrypt.decryptFileUploadPath(assetCost.getAttachName());
                String documentName = UString.extractFileNameFromPath(documentPath);
                if (assetCost.getLongTermAssetCostId() != null) {
                    UtilAttachedDocumentsDTO objAttVourcher = utilAttachedDocumentsBusinessImpl
                            .findByParentIdAndType(assetCost.getLongTermAssetCostId(), attachVourcherTypeKey);
                    if (objAttVourcher != null) {
                        objAttVourcher.setDocumentName(documentName);
                        objAttVourcher.setDocumentPath(documentPath);
                        objAttVourcher.setParentId(voucherId);

                        utilAttachedDocumentsBusinessImpl.update(objAttVourcher);
                    } else {
                        utilAttachedDocumentsBusinessImpl.insert(documentName, voucherId, documentPath,
                                attachVourcherTypeKey);
                    }
                }
            }
        }

        return new LongTermAssetDto(entity);
    }

    public List<LongTermAssetAttachDetailDto> getLtaAttachDetail(LongTermAssetCostDto obj) throws Exception {
        // TODO Auto-generated method stub
        List<LongTermAssetAttachDetailDto> list = longTermAssetAttachDetailDao.getLtaAttachDetail(obj);
        for (LongTermAssetAttachDetailDto item : list) {
            if (item.getAttachId() != null) {
                item.setAttachIdEncrypted(UEncrypt.encryptFileUploadPath(item.getAttachId().toString()));
            }
            if (UString.isNotNullAndWhitespace(item.getAttachName())) {
                item.setAttachDisplayName(item.getAttachName());
            }
        }
        return list;
    }

}

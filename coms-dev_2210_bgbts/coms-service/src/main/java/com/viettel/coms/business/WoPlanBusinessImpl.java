package com.viettel.coms.business;

import com.viettel.coms.bo.WoPlanBO;
import com.viettel.coms.dao.WoDAO;
import com.viettel.coms.dao.WoMappingPlanDAO;
import com.viettel.coms.dao.WoPlanDAO;
import com.viettel.coms.dto.*;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("woPlanBusinessImpl")
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WoPlanBusinessImpl extends BaseFWBusinessImpl<WoPlanDAO, WoPlanDTO, WoPlanBO>
        implements WoPlanBusiness {
    @Autowired
    private WoPlanDAO woPlanDAO;
    @Autowired
    private WoMappingPlanDAO woMappingPlanDAO;
    @Autowired
    WoBusinessImpl woBusiness;
    @Autowired
    private WoDAO woDAO;

    public WoPlanBusinessImpl() {
        tModel = new WoPlanBO();
        tDAO = woPlanDAO;
    }

    @Override
    public WoPlanDAO gettDAO() {
        return woPlanDAO;
    }

    @Override
    public List<WoPlanDTO> list(long ftId) {
        List<WoPlanDTO> lst = woPlanDAO.getListWOPlan(ftId);
        for (WoPlanDTO iPlan : lst) {
            int countOk = 0;
            List<WoDTO> data = getListWosByPlanId(iPlan);
            int woOfPlan = data.size();
            for (WoDTO iDto : data) {
                if ("OK".equalsIgnoreCase(iDto.getState())) {
                    countOk++;
                }
            }
            iPlan.setWoOkNumber(countOk + "/" + woOfPlan);
        }
        return lst;
    }

    @Override
    public void insert(WoPlanDTO dto, List<WoDTO> lstWosOfPlan) throws Exception {
        boolean check = check(dto.getName(), dto.getFtId());
        if (!check) {
            throw new IllegalArgumentException("Tên kế hoạch đã tồn tại !");
        }
        if (dto != null) {
            // Insert plan
            dto.setStatus(1L);
            dto.setCreatedDate(new Date());
            WoPlanBO woPlanBO = dto.toModel();
            woPlanBO.setCreatedDate(new Date());
            if (Integer.parseInt(woPlanBO.getPlanType()) == 1) {
                woPlanBO.setToDate(DateTimeUtils.addDays(woPlanBO.getFromDate(), 7));
            } else if (Integer.parseInt(woPlanBO.getPlanType()) == 2) {
                woPlanBO.setToDate(DateTimeUtils.addDays(woPlanBO.getFromDate(), 30));
            } else if (Integer.parseInt(woPlanBO.getPlanType()) == 3) {
                woPlanBO.setToDate(DateTimeUtils.addDays(woPlanBO.getFromDate(), 90));
            }
            woPlanBO.setCode("PLAN_" + dto.getFtId() + "_" + woPlanDAO.getNextValSequence("WO_PLAN_SEQ"));
            woPlanDAO.saveObject(woPlanBO);

            // Insert mapping
            for (WoDTO wo : lstWosOfPlan) {
                WoMappingPlanDTO woMappingPlanDTO = new WoMappingPlanDTO();
                woMappingPlanDTO.setWoId(wo.getWoId());
                woMappingPlanDTO.setWoPlanId(woPlanBO.getId());
                woMappingPlanDTO.setStatus(1L);
                woMappingPlanDAO.save(woMappingPlanDTO.toModel());
            }
        }

    }

    @Override
    public void update(WoPlanDTO dto, List<WoDTO> lstWosOfPlan) throws Exception {
        if (dto != null) {
            WoPlanBO woPlanBO = dto.toModel();
            if (Integer.parseInt(woPlanBO.getPlanType()) == 1) {
                woPlanBO.setToDate(DateTimeUtils.addDays(woPlanBO.getFromDate(), 7));
            } else if (Integer.parseInt(woPlanBO.getPlanType()) == 2) {
                woPlanBO.setToDate(DateTimeUtils.addDays(woPlanBO.getFromDate(), 30));
            } else if (Integer.parseInt(woPlanBO.getPlanType()) == 3) {
                woPlanBO.setToDate(DateTimeUtils.addDays(woPlanBO.getFromDate(), 90));
            }
            woPlanDAO.update(woPlanBO);
            woMappingPlanDAO.removeWoMappingPlan(dto);
            for (WoDTO wo : lstWosOfPlan) {
                WoMappingPlanDTO woMappingPlanDTO = new WoMappingPlanDTO();
                woMappingPlanDTO.setWoId(wo.getWoId());
                woMappingPlanDTO.setWoPlanId(woPlanBO.getId());
                woMappingPlanDTO.setStatus(1L);
                woMappingPlanDAO.save(woMappingPlanDTO.toModel());
            }
        }
    }

    @Override
    public boolean deletePlan(WoPlanDTO woPlanDTO) throws Exception {
        List<Long> lstWos = woDAO.getListWosByPlanId(woPlanDTO);
        boolean deletable = true;

        if (lstWos != null && lstWos.size() > 0) {
            WoDTORequest request = new WoDTORequest();
            SysUserRequest sysUserRequest = new SysUserRequest();
            sysUserRequest.setSysUserId(woPlanDTO.getFtId());
            request.setSysUserRequest(sysUserRequest);
            List<WoDTO> woList = woBusiness.list(request, lstWos, null);
            for (WoDTO wo : woList) {
                if (!"ACCEPT_FT".equalsIgnoreCase(wo.getState())) deletable = false;
            }
        }

        if (deletable) {
            woPlanDAO.delete(woPlanDTO.getId());
            return true;
        }

        return false;
    }

    @Override
    public List<WoDTO> getListWosByPlanId(WoPlanDTO woPlanDTO) {
        List<Long> lstWos = woDAO.getListWosByPlanId(woPlanDTO);
        if (lstWos == null || lstWos.size() == 0) {
            return new ArrayList<>();
        }
        return woDAO.getListWoByLstWos(lstWos);
    }

    @Override
    public List<WoPlanDTO> getDataForPlanChart(Long loginUserId) {
        List<WoPlanDTO> lstDataForPlanChart = woPlanDAO.getDataForPlanChart(loginUserId);
        return lstDataForPlanChart;
    }

    private Boolean check(String name, Long ftId) {
        WoPlanDTO obj = woPlanDAO.findByName(name, ftId);
        if (name == null) {
            if (obj == null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (obj != null && obj.getName().equals(name)) {
                return false;
            } else {
                return true;
            }
        }
    }

}

package com.viettel.coms.business;

import com.viettel.coms.dao.WoDAO;
import com.viettel.coms.dto.WoMappingChecklistDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("woMappingChecklistBusinessImpl")
public class WoMappingChecklistBusinessImpl implements WoMappingChecklistBusiness {

    @Autowired
    WoDAO woDao;


    @Override
    public String update(List<WoMappingChecklistDTO> lstWoMappingChecklists) {
        return null;
    }
}

package com.viettel.coms.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.viettel.coms.bo.WorkItemBO;
import com.viettel.coms.dao.RpBTSDAO;
import com.viettel.coms.dao.StationConstructionDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dto.RpBTSDTO;
import com.viettel.coms.dto.StationConstructionDTO;
import com.viettel.coms.dto.StationConstructionOverviewDTO;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;

@Service("stationConstructionBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StationConstructionBusinessImpl  implements StationConstructionBusiness {

	@Autowired
    private StationConstructionDAO stationConstructionDAO;
	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	

    public DataListDTO doSearch(StationConstructionDTO obj) {
        List<StationConstructionOverviewDTO> ls = stationConstructionDAO.doSearch(obj);
        if(ls == null ) {
        	ls = Lists.newArrayList();
        	obj.setTotalRecord(0);
        }
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

}

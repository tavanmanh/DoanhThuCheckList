package com.viettel.coms.business;

import com.viettel.coms.dto.CertificateExtendDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;

import java.util.List;

public interface UtilAttachDocumentBusiness {

    long count();

    List<UtilAttachDocumentDTO> doSearch(UtilAttachDocumentDTO obj);
    UtilAttachDocumentDTO getAttachFile(UtilAttachDocumentDTO obj);
    void deleteExtend(CertificateExtendDTO obj);
}

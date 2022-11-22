/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.dao;

import com.viettel.coms.bo.StockTransDetailSerialVsmartBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 * @author HOANM1
 * @version 1.0
 * @since 2019-03-10
 */
@Repository("aioStockTransDetailSerialDAO")
public class AIOStockTransDetailSerialDAO extends BaseFWDAOImpl<StockTransDetailSerialVsmartBO, Long> {

    public AIOStockTransDetailSerialDAO() {
        this.model = new StockTransDetailSerialVsmartBO();
    }

    public AIOStockTransDetailSerialDAO(Session session) {
        this.session = session;
    }
}

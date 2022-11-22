/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.wms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author thangdd8@viettel.com.vn
 * @version 1.0
 * @since Apr 12, 2010
 */
public class ValidateUtils {

    /**
     * private contructor
     */
    private ValidateUtils() {
    }

    /**
     * <P>Check is Integer or not</P>
     *
     * @param str String to check
     * @param str
     * @return @boolean true if valid, false if not valid
     */
    public static boolean isInteger(String str) {
        if (str == null || !str.matches("[0-9]+$")) {
            return false;
        }
        return true;
    }

    public static String validateKeySearch(String str) {
        return str.replaceAll("&", "&&").replaceAll("%", "&%").replaceAll("_", "&_");
    }

    public static boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }
    
    public static Date isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
            return date;
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

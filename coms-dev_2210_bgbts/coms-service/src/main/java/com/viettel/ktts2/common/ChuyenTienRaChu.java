package com.viettel.ktts2.common;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;

/**
 *
 * @author Phong Nha
 */
public class ChuyenTienRaChu {
    
    
	static String khong=UConfig.get("common.number.0");
	static String mot=UConfig.get("common.number.1");
	static String hai=UConfig.get("common.number.2");
	static String ba=UConfig.get("common.number.3");
	static String bon=UConfig.get("common.number.4");
	static String nam=UConfig.get("common.number.5");
	static String sau=UConfig.get("common.number.6");
	static String bay=UConfig.get("common.number.7");
	static String tam=UConfig.get("common.number.8");
	static String chin=UConfig.get("common.number.9");
	
	static String dong=UConfig.get("common.donvi.dong");
	static String muoi=UConfig.get("common.donvi.muoi");
	static String tram=UConfig.get("common.donvi.tram");
	static String nghin=UConfig.get("common.donvi.nghin");
	static String trieu=UConfig.get("common.donvi.trieu");
	static String ty=UConfig.get("common.donvi.ty");
	
	
	static String mot_1=UConfig.get("common.donvi.mot_1");//
	static String muoi_1=UConfig.get("common.donvi.muoi_1");//
	static String le_1=UConfig.get("common.donvi.le_1");//
	
	
	
	protected static final HashMap<String, String> hm_tien=new HashMap<String,String>(){{
//		put("0","không");
		put("0",khong);
		put("1",mot);put("2",hai);put("3",ba);put("4",bon);put("5",nam);put("6",sau);put("7",bay);put("8",tam);put("9",chin);}};
	protected static final HashMap<String, String> hm_hanh=new HashMap<String,String>(){{put("1",dong);put("2",muoi);put("3",tram);put("4",nghin);put("5",muoi);put("6",tram);put("7",trieu);put("8",muoi);put("9",tram);put("10",ty);put("11",muoi);put("12",tram);put("13",nghin);put("14",muoi);put("15",tram);

	}};

	public static void main(String[] args) throws ParseException {
		String tien = "1023045";
		String kq = chuyenThanhChu2(tien);
		// System.out.println(currencyFormat(tien));
		System.out.println(kq);

	}
	@Deprecated
	public static String chuyenThanhChu(String x) {
        String kq = "";
        x = x.replace(".", "");
        String arr_temp[] = x.split(",");
        
        if (!UData.isNumber(arr_temp[0])) {
            return "";
        }
        String m = arr_temp[0];
        int dem = m.length();
        String dau ;
        int flag10 = 1;
        while (!"".equals(m)) {
            if (m.length() <= 3 && m.length() > 1 && Long.parseLong(m) == 0) {

            } else {
                dau = m.substring(0, 1);
                if (dem % 3 == 1 && m.startsWith("1") && flag10 == 0) {
                    kq += mot_1;
                    flag10 = 0;
                } else if (dem % 3 == 2 && m.startsWith("1")) {
                    kq += muoi_1;
                    flag10 = 1;
                } else if (dem % 3 == 2 && m.startsWith("0") && m.length() >= 2 && !"0".equals(m.substring(1, 2))) {
                    //System.out.println("a  "+m.substring(1, 2));
                    kq += le_1;
                    flag10 = 1;
                } else {
                    if (!m.startsWith("0")) {
                        kq += hm_tien.get(dau) + " ";
                        flag10 = 0;
                    }
                }
                if (dem%3!=1 &&m.startsWith("0") && m.length()>1) {
                } else {
                    if (dem % 3 == 2 && (m.startsWith("1") || m.startsWith("0"))) {//muoi
                    } else {
                        kq += hm_hanh.get(dem + "") + " ";
                    }
                }
            }
            m = m.substring(1);
            dem = m.length();
        }
        kq=kq.substring(0, kq.length() - 1);
        return kq;
    }
	
	public static String chuyenThanhChu2(String x) {
        String kq = "";
        x = x.replace(".", "");
        String arr_temp[] = x.split(",");
        
        if (!UData.isNumber(arr_temp[0])) {
            return "";
        }
        String m = arr_temp[0];
        int dem = m.length();
        String dau ;
        int flag10 = 1;
        while (!"".equals(m)) {
            if (m.length() <= 3 && m.length() > 1 && Long.parseLong(m) == 0) {

            } else {
                dau = m.substring(0, 1);
                if (dem % 3 == 1 && m.startsWith("1") && flag10 == 0) {
                    kq += mot_1;
                    flag10 = 0;
                } else if (dem % 3 == 2 && m.startsWith("1")) {
                    kq += muoi_1;
                    flag10 = 1;
                } else if (dem % 3 == 2 && m.startsWith("0") && m.length() >= 2 && !"0".equals(m.substring(1, 2))) {
                    //System.out.println("a  "+m.substring(1, 2));
                    kq += le_1;
                    flag10 = 1;
                } else {
                    if (!m.startsWith("0")) {
                        kq += hm_tien.get(dau) + " ";
                        flag10 = 0;
                    }else{
                    	 kq += hm_tien.get(dau) + " ";
                         flag10 = 0;
                    }
                }
                if (dem%3!=1 &&m.startsWith("0") && m.length()>1) {
                	kq += hm_hanh.get(dem + "") +" ";
                } else {
                    if (dem % 3 == 2 && (m.startsWith("1") || m.startsWith("0"))) {//muoi
                    	
                    } else {
                    	if(dem%3==1 &&dem>1){
                    		kq += hm_hanh.get(dem + "") +", ";
                    	}else{
                    		kq += hm_hanh.get(dem + "") +" ";
                    	}
                    }
                }
            }
            m = m.substring(1);
            dem = m.length();
        }
        String returnStr=kq.substring(1, kq.length() - 1);
        String firstChar=kq.substring(0,1);
        return firstChar.toUpperCase()+returnStr;
//        return kq;
    }
	
	
	public static String chuyenThanhChu3(String x) {
        String kq = "";
        x = x.replace(".", "");
        String arr_temp[] = x.split(",");
        
        if (!UData.isNumber(arr_temp[0])) {
            return "";
        }
        String m = arr_temp[0];
        int dem = m.length();
        String dau ;
        int flag10 = 1;
        while (!"".equals(m)) {
            if (m.length() <= 3 && m.length() > 1 && Long.parseLong(m) == 0) {

            } else {
                dau = m.substring(0, 1);
                if (dem % 3 == 1 && m.startsWith("1") && flag10 == 0) {
                    kq += mot_1;
                    flag10 = 0;
                } else if (dem % 3 == 2 && m.startsWith("1")) {
                    kq += muoi_1;
                    flag10 = 1;
                } else if (dem % 3 == 2 && m.startsWith("0") && m.length() >= 2 && !"0".equals(m.substring(1, 2))) {
                    //System.out.println("a  "+m.substring(1, 2));
                    kq += le_1;
                    flag10 = 1;
                } else {
                    if (!m.startsWith("0")) {
                        kq += hm_tien.get(dau) + " ";
                        flag10 = 0;
                    }
                }
                if (dem%3!=1 &&m.startsWith("0") && m.length()>1) {
                } else {
                    if (dem % 3 == 2 && (m.startsWith("1") || m.startsWith("0"))) {//muoi
                    } else {
                        kq += hm_hanh.get(dem + "") + ", ";
                    }
                }
            }
            m = m.substring(1);
            dem = m.length();
        }
        kq=kq.substring(0, kq.length() - 1);
        return kq;
    }


	public static String currencyFormat(String curr) throws Exception{
       
            double vaelue = Double.parseDouble(curr);
            String pattern = "###,###";
            DecimalFormat myFormatter = new DecimalFormat(pattern);
            String output = myFormatter.format(vaelue);
            return output;
        
    }
}
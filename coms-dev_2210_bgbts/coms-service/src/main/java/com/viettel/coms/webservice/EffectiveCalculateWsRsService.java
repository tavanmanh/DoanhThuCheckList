package com.viettel.coms.webservice;

import com.ctc.wstx.util.StringUtil;
import com.viettel.asset.business.AuthenticateWsBusiness;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.cat.business.CatProvinceBusinessImpl;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.business.*;
import com.viettel.coms.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Irr;
import com.viettel.coms.dto.CapexSourceHTCTDTO;
import com.viettel.coms.business.CapexSourceHTCTBusinessImpl;
import com.viettel.coms.business.CapexBtsHtctBusinessImpl;
import com.viettel.coms.business.WaccHTCTBusinessImpl;
import com.viettel.coms.dto.CapexBtsHtctDTO;
import com.viettel.coms.business.RatioDeliveryHtctBusinessImpl;
import com.viettel.coms.dto.RatioDeliveryHtctDTO;
import com.viettel.coms.business.OfferHtctBusinessImpl;
import com.viettel.coms.dto.OfferHtctDTO;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML})
@Path("/service")
public class EffectiveCalculateWsRsService {
  private Logger LOGGER = Logger.getLogger(EffectiveCalculateWsRsService.class);

  @Autowired
  EffectiveCalculateDASCDBRBusinessImpl effectiveCalculateDASCDBRBusiness;
  @Autowired
  AuthenticateWsBusiness authenticateWsBusiness;
  @Autowired
  EffectiveCalculateBTSBusinessImpl effectiveCalculateBTSBusiness;

  @Autowired
  EffectiveCalculateDasBusinessImpl effectiveCalculateDasBusiness;

  @Autowired
  EffectiveCalculateDasCapexBusinessImpl effectiveCalculateDasCapexBusiness;

  @Autowired
  CalculateEfficiencyHtctBusinessImpl calculateEfficiencyHtctBusiness;

  @Autowired
  WaccHTCTBusinessImpl waccHTCTBusiness;

  @Autowired
  CapexSourceHTCTBusinessImpl capexSourceHTCTBusiness;

  @Autowired
  CapexBtsHtctBusinessImpl capexBtsHtctBusiness;

  @Autowired
  RatioDeliveryHtctBusinessImpl ratioDeliveryHtctBusiness;

  @Autowired
  OfferHtctBusinessImpl offerHtctBusiness;

  @Autowired
  Cost1477HtctBusinessImpl cost1477HtctBusiness;

  @Autowired
  CatProvinceBusinessImpl catProvinceBusiness;

  @Autowired
  GpmbHtctBusinessImpl gpmbHtctBusiness;
  // CONSTANT DAS + CDBR
  final String RATE = "Lãi suất chiết khấu (WACC)";

  final String BANK_LOAN = "Vốn vay ngân hàng";

  final String TIME_LOAN = "Thời gian vay vốn";

  final String TIME_RENT = "Thời gian dự kiến cho thuê";

  final String ENGINE_ROOM_DAS = "Suất đầu tư phòng máy";

  final String FEEDER_ANTENNA_DAS = "Suất đầu tư feeder/anten trung bình/m2";

  final String COST_OTHER_DAS = "Chi phí khác (Chi phí QLDA, CP tư vấn,…)";

  final String AXIS_CDBR = "Đầu tư phần trục";

  final String APARTMENTS_ALL_CDBR = "Đầu tư phần trong căn hộ ( Đầu tư toàn bộ-1 điểm đấu nối, mặt hạt)";

  final String APARTMENTS_CDBR = "Đầu tư phần trong căn hộ ( Đầu tư mỗi dây cáp)";

  final String COST_OTHER_CDBR = "Chi phí khác (Chi phí QLDA, CP tư vấn,…)";

  final String ENGINE_ROOM_CDBR = "Chi phí phòng máy (rack, ODF + OLT)";

  final String ENGINE_ROOM_CABLE_CDBR = "Đầu tư thang máng cáp";

  final String TIME_DEPRECIATION = "Thời gian khấu hao";

  final String PERCENTAGE_COST_BUSINESS = "Chi phí kinh doanh";

  final String COST_VHKT = "Chi phí VHKT";

  final String PERCENTAGE_MANAGEMENT_COST = "CPQL";

  final String PERCENTAGE_REPLACE = "Chi phí bảo dưỡng, thay thế, UCTT";

  final String PERCENTAGE_INTEREST = "Chi phí vốn vay (Lãi vay)";

  final String PERCENTAGE_SHARE_REVENUE_DAS = "Tỉ lệ chia sẻ doanh thu DAS cho CĐT";

  final String TAX_INCOME = "Thuế suất Thuế TNDN";

  // Ti le lap dau qua cac nam
  final double[] rateOil = {0, 0.35, 0.45, 0.6, 0.75, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9};
  final long priceHTDAS = 1050;
  final long priceElectricity = 1200;
  final int MONTH = 12;
  final long priceInternet = 200000;
  final long priceTV = 40000;
  final double kpi = 0.95;
  final double percentageDiscount = 0.05;
  final long costApplicationDAS = 700;
  final long costApplicationCDBR = 70000;
  final double percentageIncrease = 1.02;
  final int timeDiscount = 2;

  //                        CONSTANT BTS
  // chi phí vốn tự có
  final String EQUITY_COST = "Chi phí vốn tự có";

  final String PROJECT_LOANS_RATIO = "Tỷ lệ vốn vay dự án";

  final String LOAN_EXPENSES = "Chi phí vốn vay";

  final String RATE_OF_PROJECT_EQUITY = "Tỷ lệ vốn tự có dự án";

  final String ON_THE_ROOF = "trên mái";

  final String DELTA = "đồng bằng";

  final String MOUNTAIN = "miền núi";

  final String SELF_INCLUDED_RENTAL_PRICE = "Tự nhập giá thuê";

  final String MACRO_STATION = "trạm bts";

  final String RRU_STATION = "trạm rru";

  final String SMALLCELL_STATION = "trạm small cell";

  // capex nguon

  final String COST_PUBLIC_INSTALLATION_POWER = "Công lắp đặt hệ thống nguồn";

  final String COST_OTHER_AUXILIARY_SYSTEM = "Hệ thống phụ trợ khác";

  final String COST_SUPERVISION_CONTROL = "Giám sát điều khiển";

  final String COST_ATS = "ATS";

  final String COST_OIL_GENERATOR = "Máy phát điện dầu 8kVA/12KVA";

  final String COST_BATTERY_LITHIUM = "Battery Lithium";

  final String COST_RECTIFIER3000 = "Rectifier 3000W";

  final String COST_POWER_CABINET_COOLING_SYSTEM = "Tủ nguồn và hệ thống làm mát";

  final String COST_SURVEY_GEOLOGICAL = "Chi phí khảo sát địa chất";

  final String COST_PROJECT_PLANNING = "Chi phí lập dự án";

  final String COST_PROJECT_EXAMINATION = "Chi phí thẩm tra dự án";

  final String COST_DESIGN = "Chi phí thiết kế bản vẽ thi công";

  final String COST_DESIGN_EXAMINATION = "Thẩm tra thiết kế bản vẽ thi công";

  final String COST_VERIFICATION_PROJECTS = "Thẩm tra dự toán";

  final String COST_HSMT_HSDT_CONSTRUCTION = "Chi phí lập HSMT, đánh giá HSDT thi công xây dựng";

  final String COST_HSMT_HSDT_SHOPPING_EQUIPMENT = "Chi phí lập HSMT, đánh giá HSDT mua sắm thiết bị";

  final String COST_INSTALLATION_MONITORING = "Chi phí giám sát lắp đặt thiết bị";

  final String COST_CONSTRUCTION_MONITORING = "Chi phí giám sát thi công xây dựng công trình";

  final String COST_GENERAL_CATEGORY = "Chi phí hạng mục chung ";

  final String COST_INVESTMENT_PROJECTS = "Phí thẩm định dự án đầu tư";

  final String COST_AUDIT = "Chi phí kiểm toán";

  final String COST_EXAMINE_APPROVAL = "Phí thẩm tra, phê duyệt quyết toán";

  final String COST_LICENSE_CONSTRUCTION = "Chi phí xin phép thi công";

  final String COST_ALLOW_ELECTRICAL_CONNECTION = "Chi phi xin phép đấu nối điện";

  final String COST_DESIGN_EVALUATION = "Phí thẩm định thiết kế";

  final String COST_VALUATION_ESTIMATION = "Phí thẩm định dự toán";

  final String COST_PREVENTIVE = "Chi phí dự phòng phát sinh";

  final String COST_RISK = "Chi phí rủi ro";

  // Capex
  final double costOtherStationInfrastructure = 9600000;

  // OFFER
  final String COST_MAINTAINER = "Chi phí sửa chữa, bảo trì hệ thống cột nhà trạm";

  final String COST_MAINTAINER_ELECTRIC = "Chi phí bảo trì bảo dưỡng hệ thống cơ điện";

  final String COST_PROJECT_MANAGER = "Chi phí quản lý dự án";

  final String TIME_RENT_BTS = "Thời gian dự kiến cho thuê";

  final double costLabor = 601635.44;

  final long costRoof = 6235865;

  final long costLand = 4411720;

  //final int maxTimeRentBts = 15;

  final double percentBTS = 1.1;

  final double rentalRate = 0.97;

  final double timeDepreciationBTS = 6;
  // doanh thu
  final long UNIT_RENT_MOBIPHONE = 6000000;

  @POST
  @Path("/getListEffectiveCalculateDASCDBR/")
  public EffectiveCalculateDASCDBRBTSResponse getListEffectiveCalculateDASCDBR(SysUserRequest request) {
    EffectiveCalculateDASCDBRBTSResponse response = new EffectiveCalculateDASCDBRBTSResponse();
    try {
      List<EffectiveCalculateDASCDBRDTO> data = effectiveCalculateDASCDBRBusiness.getListEffectiveCalculateDASCDBR(request);
      response.setLstEffectiveCalculateDASCDBRDTO(data);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_OK);
      response.setResultInfo(resultInfo);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_NOK);
      resultInfo.setMessage(e.getMessage());
      response.setResultInfo(resultInfo);
    }
    return response;
  }

  @POST
  @Path("/addEffectiveCalculateDASCDBR/")
  public EffectiveCalculateDASCDBRBTSResponse addEffectiveCalculateDASCDBR(EffectiveCalculateDASCDBRBTSRequest request) throws Exception {
    EffectiveCalculateDASCDBRBTSResponse response = new EffectiveCalculateDASCDBRBTSResponse();
    try {
      Long result = effectiveCalculateDASCDBRBusiness.insertEffectiveCalculateDASCDBR(request);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(result != 0 ? ResultInfo.RESULT_OK : ResultInfo.RESULT_NOK);
      response.setResultInfo(resultInfo);
    } catch (Exception e) {
    	e.printStackTrace();
      LOGGER.error(e.getMessage(), e);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_NOK);
      resultInfo.setMessage("Có lỗi xảy ra");
      response.setResultInfo(resultInfo);
    }
    return response;
  }

  @POST
  @Path("/calculateDASCDBR/")
  public double calculateDASCDBR(EffectiveCalculateDASCDBRBTSRequest request) throws Exception {
    EffectiveCalculateDASCDBRBTSResponse response = new EffectiveCalculateDASCDBRBTSResponse();
    long result = 0L;
    try {
      // get du lieu nhap tu man hinh
      EffectiveCalculateDASCDBRDTO effectiveCalculateDASCDBRDTO = request.getEffectiveCalculateDASCDBRDTO();
      Long dasType = effectiveCalculateDASCDBRDTO.getDasType() != null ? effectiveCalculateDASCDBRDTO.getDasType() : 0; // 1 or 0
      Long cdbrType = effectiveCalculateDASCDBRDTO.getCdbrType() != null ? effectiveCalculateDASCDBRDTO.getCdbrType() : 0;
      Double totalArea = effectiveCalculateDASCDBRDTO.getTotalArea() != null ? effectiveCalculateDASCDBRDTO.getTotalArea() : 0;
      Double totalApartment = effectiveCalculateDASCDBRDTO.getTotalApartment() != null ? effectiveCalculateDASCDBRDTO.getTotalApartment() : 0;

      String costDasStr = StringUtils.isNotBlank(effectiveCalculateDASCDBRDTO.getCostDas()) ? effectiveCalculateDASCDBRDTO.getCostDas() : "0";
      double costDasValue = ToDouble(costDasStr);

      String costEngineRoomCDBRStr = StringUtils.isNotBlank(effectiveCalculateDASCDBRDTO.getCostEngineRoomCDBR()) ? effectiveCalculateDASCDBRDTO.getCostEngineRoomCDBR() : "0";
      double costEngineRoomCDBRValue = ToDouble(costEngineRoomCDBRStr);

      //if(costEngineRoomCDBRValue == 0) return result;
      String costCDBRStr = StringUtils.isNotBlank(effectiveCalculateDASCDBRDTO.getCostCDBR()) ? effectiveCalculateDASCDBRDTO.getCostCDBR() : "0";
      double costCDBRValue = ToDouble(costCDBRStr);
      // if(costEngineRoomCDBRValue == 0) return result;
      Double ratioRate = effectiveCalculateDASCDBRDTO.getRatioRate() != null ? effectiveCalculateDASCDBRDTO.getRatioRate() : 0;
      Long engineRoomDasType = effectiveCalculateDASCDBRDTO.getEngineRoomDas() != null ? effectiveCalculateDASCDBRDTO.getEngineRoomDas() : 0;
      Long feederAntennaDasType = effectiveCalculateDASCDBRDTO.getFeederAntenDas() != null ? effectiveCalculateDASCDBRDTO.getFeederAntenDas() : 0;
      Long costOtherDasType = effectiveCalculateDASCDBRDTO.getCostOtherDas() != null ? effectiveCalculateDASCDBRDTO.getCostOtherDas() : 0;
      Long axisCdbrType = effectiveCalculateDASCDBRDTO.getAxisCdbr() != null ? effectiveCalculateDASCDBRDTO.getAxisCdbr() : 0;
      Long apartmentsAllCdbrType = effectiveCalculateDASCDBRDTO.getApartmentsAllCdbr() != null ? effectiveCalculateDASCDBRDTO.getApartmentsAllCdbr() : 0;
      Long apartmentsCdbrType = effectiveCalculateDASCDBRDTO.getApartmentsCdbr() != null ? effectiveCalculateDASCDBRDTO.getApartmentsCdbr() : 0;
      Long costOtherCdbrType = effectiveCalculateDASCDBRDTO.getCostOtherCdbr() != null ? effectiveCalculateDASCDBRDTO.getCostOtherCdbr() : 0;
      Long engineRoomCdbrType = effectiveCalculateDASCDBRDTO.getEngineRoomCdbr() != null ? effectiveCalculateDASCDBRDTO.getEngineRoomCdbr() : 0;
      Long engineRoomCableCdbrType = effectiveCalculateDASCDBRDTO.getEngineRoomCableCdbr() != null ? effectiveCalculateDASCDBRDTO.getEngineRoomCableCdbr() : 0;
      // 1. get du lieu gia_dinh trong bang ASSUMPTIONS
      List<EffectiveCalculateDasDTO> assumptions = effectiveCalculateDasBusiness.getAssumptions(new EffectiveCalculateDasDTO());
      // 2.  get du lieu gia_dinh_capex trong bang ASSUMPTIONS_CAPEX
      List<EffectiveCalculateDasCapexDTO> assumptionsCapex = effectiveCalculateDasCapexBusiness.getAssumptionsCapex(new EffectiveCalculateDasCapexDTO());
      // 3.  ap dung cong thuc tinh do hieu qua
      // 3.1 get rate
      double rate = getValueObject(assumptions, RATE);
      double bankLoan = getValueObject(assumptions, BANK_LOAN);
      int timeLoan = (int) getValueObject(assumptions, TIME_LOAN);
      int timeRent = (int) getValueObject(assumptions, TIME_RENT) + 1;
      int timeDepreciation = (int) getValueObject(assumptions, TIME_DEPRECIATION);
      // das
      double engineRoomDas = getValue(assumptionsCapex, ENGINE_ROOM_DAS);
      double feederAntennaDas = getValue(assumptionsCapex, FEEDER_ANTENNA_DAS);
      double costOtherDas = feederAntennaDas * 0.1;
      // cdbr
      double apartmentsCdbr = getValue(assumptionsCapex, APARTMENTS_CDBR);
      double axisCdbr = getValue(assumptionsCapex, AXIS_CDBR);
      double apartmentAllCdbr = getValue(assumptionsCapex, APARTMENTS_ALL_CDBR);
      double engineRoomCdbr = getValue(assumptionsCapex, ENGINE_ROOM_CDBR);
      double engineRoomCableCdbr = getValue(assumptionsCapex, ENGINE_ROOM_CABLE_CDBR);
      double costOtherCdbr = axisCdbr * axisCdbrType * 0.1 + apartmentsCdbrType * apartmentsCdbr * 0.1 + apartmentAllCdbr * apartmentsAllCdbrType * 0.1
          + (engineRoomCdbr / totalApartment) * 0.1 + engineRoomCableCdbr * engineRoomCableCdbrType * 0.1;
      // percentage
      double percentageCostBusiness = getValueObject(assumptions, PERCENTAGE_COST_BUSINESS);
      double percentageVHKT = getValueObject(assumptions, COST_VHKT);
      double percentageCPQL = getValueObject(assumptions, PERCENTAGE_MANAGEMENT_COST);
      double percentageReplace = getValueObject(assumptions, PERCENTAGE_REPLACE);
      double percentageInterest = getValueObject(assumptions, PERCENTAGE_INTEREST);
      double percentageShareRevenueDAS = getValueObject(assumptions, PERCENTAGE_SHARE_REVENUE_DAS);
      double taxIncome = getValueObject(assumptions, TAX_INCOME);
      // tinh capex DAS
      double capexDas = (engineRoomDas * engineRoomDasType
          + feederAntennaDas * feederAntennaDasType * totalArea
          + costOtherDas * costOtherDasType * totalArea) * dasType;
      // tinh capex CDBR
      double capexCdbr = axisCdbr * axisCdbrType * totalApartment
          + apartmentAllCdbr * apartmentsAllCdbrType * totalApartment
          + apartmentsCdbr * apartmentsCdbrType * totalApartment * cdbrType
          + costOtherCdbr * costOtherCdbrType * totalApartment
          + engineRoomCdbr * engineRoomCdbrType * cdbrType
          + engineRoomCableCdbr * engineRoomCableCdbrType * totalApartment;
      // tinh capex
      double capex = capexCdbr * cdbrType + capexDas * dasType;
      // chi phi lay DA DAS
      double costDAS = costApplicationDAS * totalArea * dasType;
      //tinh chi phi lay DA CDBR
      double costCdbr = costApplicationCDBR * totalApartment * cdbrType;
      //Chi phi lay DA phan bo
      double costDasCdbr = costCdbr + costDAS;
      // get so tien vay
      double totalLoan = bankLoan * capex;
      // get so tien tra hang thang
      double monthlyPayments = totalLoan / timeLoan;

      // 3.2 get dong tien
      double[] cashFlow = new double[timeRent];
//      double[] depreciation = new double[timeRent];
//      double[] costBusinessDas = new double[timeRent];
//      double[] costBusinessCdbr = new double[timeRent];
//      double[] revenue = new double[timeRent];
//      double[] revenueDas = new double[timeRent];
//      double[] revenueCdbr = new double[timeRent];

      double[] arrayPrice = new double[timeRent];
      double[] debitBalance = new double[timeRent];
      double[] arrayCashFlowDiscount = new double[timeRent];
      double[] arrayCashFlowDiscountCumulative = new double[timeRent];
      double[] amountFttx = new double[timeRent];
      double[] arrayCostHostPrivateDAS = new double[timeRent];
      double[] arrayCostHostPrivateCDBR = new double[timeRent];
      double[] arrayCostElectricityDAS = new double[timeRent];
      double[] arrayCostElectricityCDBR = new double[timeRent];
      double costSell = 0;
      double timePayBack = 0;
      double ratioRateOLT = engineRoomCdbrType == 1 ? 0.3 : 0.2;
      for (int index = 0; index < timeRent; index++) {
        if (index == 0) {
          // set capex first
          double capexFirst = -(capex + costDasCdbr);
          cashFlow[index] = capexFirst;
          // get doanh thu nha mang
          arrayPrice[index] = priceInternet * cdbrType + priceTV * cdbrType * 0.7;
          // get du no cuoi ky
          debitBalance[index] = totalLoan;
          // dong tien chiet khau
          arrayCashFlowDiscount[index] = capexFirst;
          // dong tien chiet khau luy ke
          arrayCashFlowDiscountCumulative[index] = capexFirst;
        } else {
          // get don gia DT nha mang
          double price = index != 1 && ((index + 1) % timeDiscount == 0)
              ? arrayPrice[index - 1] * (1 - this.percentageDiscount)
              : arrayPrice[index - 1];
          arrayPrice[index] = price;
          //Tinh khau hao
          double depreciation = index <= timeDepreciation ? (capex / timeDepreciation) : 0;
          //Tinh doanh thu Das
          double revenueDAS = getRevenueDAS(index, dasType, totalArea, timeLoan);
          // Tinh Doanh thu VCCC
          amountFttx[index] = Math.floor(rateOil[index] * totalApartment * cdbrType);
          double revenueCDBR = ratioRateOLT * Math.floor(amountFttx[index]) * price * MONTH;
          // Tong doanh thu
          double revenue = revenueDAS + revenueCDBR;
          // Tinh Chi phi
          // get du no cuoi ky
          debitBalance[index] = index + 1 <= timeLoan ? debitBalance[index - 1] - monthlyPayments : 0;
          // Chi phi kinh doanh
          double costBusinessDAS = percentageCostBusiness * revenue * dasType;
          double costBusinessCDBR = costSell * (amountFttx[index] - amountFttx[index - 1]);
          // Chi phi phong may
          double rentalEngineDas = index == 1
              ? costDasValue * MONTH * dasType
              : (arrayCostHostPrivateDAS[index - 1] * percentageIncrease);
          double rentalEngineCdbr = index == 1
              ? MONTH * costEngineRoomCDBRValue * cdbrType * engineRoomCdbrType
              : (arrayCostHostPrivateCDBR[index - 1] * percentageIncrease);
          arrayCostHostPrivateDAS[index] = rentalEngineDas;
          arrayCostHostPrivateCDBR[index] = rentalEngineCdbr;
          // Chi phi quan ly
          double costCPQL = percentageCPQL * revenue;
          //Chi phi bao duong thay the
          double costReplacement = percentageReplace * capex;
          // Chi phi dien
          double powerDas = index == 1
              ? priceElectricity * dasType * totalArea : (arrayCostElectricityDAS[index - 1] * percentageIncrease);
          double powerCdbr = index == 1
              ? costCDBRValue * cdbrType * engineRoomCdbrType : (arrayCostElectricityCDBR[index - 1] * percentageIncrease);
          arrayCostElectricityDAS[index] = powerDas;
          arrayCostElectricityCDBR[index] = powerCdbr;
          // Chi phi lai vay trong ky
          double costDebitBalance = index <= timeLoan ? debitBalance[index - 1] * percentageInterest : 0;
          // Chia se doanh thu
          double costShareRevenueDAS = percentageShareRevenueDAS * revenueDAS;
          double costShareRevenueCDBR = ratioRate * revenueCDBR / 100;
          //  Tong chi phi
          double cost = depreciation + (costBusinessDAS + costBusinessCDBR) + (percentageVHKT * revenue) + (rentalEngineDas + rentalEngineCdbr)
              + costCPQL + costReplacement
              + (powerCdbr + powerDas) + costDebitBalance + (costShareRevenueCDBR + costShareRevenueDAS);
          // loi nhuan truoc thue
          double profit = revenue - cost;
          // Lọi nhuan sau thue
          double profitAfterTaxes = profit > 0 ? profit - (profit * taxIncome) : profit;
          // Dong tien
          cashFlow[index] = profitAfterTaxes + depreciation;
          // Dong tien chiet khau
          double percentage = (1 / (Math.pow(1 + rate, index)));
          arrayCashFlowDiscount[index] = cashFlow[index] * percentage;
          //Dong tien chiet khau luy ke
          arrayCashFlowDiscountCumulative[index] = arrayCashFlowDiscount[index] + arrayCashFlowDiscountCumulative[index - 1];
          // so nam hoan von
          timePayBack = getTimePayBack(timePayBack, arrayCashFlowDiscountCumulative, arrayCashFlowDiscount, index);
        }
      }

      // 3.3 get NPV

      // Initialize net present value
//      double npvProject = getNPV(cashFlow, rate);
//      double irrProject = Irr.irr(cashFlow, rate);

      // 3.5 return response
      return calculateEfficiency(cashFlow, rate, timePayBack, timeDepreciation);
    } catch (Exception e) {
    	e.printStackTrace();
      LOGGER.error(e.getMessage(), e);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_NOK);
      resultInfo.setMessage("Có lỗi xảy ra");
      response.setResultInfo(resultInfo);
    }
    return result;
  }

  @POST
  @Path("/getListEffectiveCalculateBTS/")
  public EffectiveCalculateDASCDBRBTSResponse getListEffectiveCalculateBTS(SysUserRequest request) {
    EffectiveCalculateDASCDBRBTSResponse response = new EffectiveCalculateDASCDBRBTSResponse();
    try {
      List<EffectiveCalculateBTSDTO> data = effectiveCalculateBTSBusiness.getListEffectiveCalculateBTS(request);
      response.setLstEffectiveCalculateBTSDTO(data);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_OK);
      response.setResultInfo(resultInfo);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_NOK);
      resultInfo.setMessage(e.getMessage());
      response.setResultInfo(resultInfo);
    }
    return response;
  }

  @POST
  @Path("/getProvince/")
  public EffectiveCalculateDASCDBRBTSResponse getProvince() {
    EffectiveCalculateDASCDBRBTSResponse response = new EffectiveCalculateDASCDBRBTSResponse();
    try {
      List<EffectiveCalculateBTSDTO> data = effectiveCalculateBTSBusiness.getProvince();
      response.setLstEffectiveCalculateBTSDTO(data);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_OK);
      response.setResultInfo(resultInfo);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_NOK);
      resultInfo.setMessage(e.getMessage());
      response.setResultInfo(resultInfo);
    }
    return response;
  }

  @POST
  @Path("/getNameParam/")
  public EffectiveCalculateDASCDBRBTSResponse getNameParam(EffectiveCalculateDASCDBRBTSRequest request) {
    EffectiveCalculateDASCDBRBTSResponse response = new EffectiveCalculateDASCDBRBTSResponse();
    try {
      List<EffectiveCalculateBTSDTO> data = effectiveCalculateBTSBusiness.getNameParam(request);
      response.setLstEffectiveCalculateBTSDTO(data);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_OK);
      response.setResultInfo(resultInfo);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_NOK);
      resultInfo.setMessage(e.getMessage());
      response.setResultInfo(resultInfo);
    }
    return response;
  }

  @POST
  @Path("/getNameParamStation/")
  public EffectiveCalculateDASCDBRBTSResponse getNameParamStation(EffectiveCalculateDASCDBRBTSRequest request) {
    EffectiveCalculateDASCDBRBTSResponse response = new EffectiveCalculateDASCDBRBTSResponse();
    try {
      List<EffectiveCalculateBTSDTO> data = effectiveCalculateBTSBusiness.getNameParamStation(request);
      response.setLstEffectiveCalculateBTSDTO(data);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_OK);
      response.setResultInfo(resultInfo);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_NOK);
      resultInfo.setMessage(e.getMessage());
      response.setResultInfo(resultInfo);
    }
    return response;
  }

  @POST
  @Path("/addEffectiveCalculateBTS/")
  public EffectiveCalculateDASCDBRBTSResponse addEffectiveCalculateBTS(EffectiveCalculateDASCDBRBTSRequest request) throws Exception {
    EffectiveCalculateDASCDBRBTSResponse response = new EffectiveCalculateDASCDBRBTSResponse();
    try {
      Long result = effectiveCalculateBTSBusiness.insertEffectiveCalculateBTS(request);
      if (result == -1L) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setStatus(ResultInfo.RESULT_NOK);
        resultInfo.setMessage("Thời gian bạn đăng ký đi làm đã tồn tại");
        response.setResultInfo(resultInfo);
      } else if (result == -2L) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setStatus(ResultInfo.RESULT_NOK);
        resultInfo.setMessage("Đăng ký vượt quá số ngày nghỉ cho phép");
        response.setResultInfo(resultInfo);
      } else if (result == 1L) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setStatus(ResultInfo.RESULT_OK);
        response.setResultInfo(resultInfo);
      } else {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setStatus(ResultInfo.RESULT_NOK);
        resultInfo.setMessage("Có lỗi xảy ra");
        response.setResultInfo(resultInfo);
      }

    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_NOK);
      resultInfo.setMessage("Có lỗi xảy ra");
      response.setResultInfo(resultInfo);
    }
    return response;
  }

  @POST
  @Path("/calculateBTS/")
  public double calculateBTS(EffectiveCalculateDASCDBRBTSRequest request) throws Exception {
    EffectiveCalculateDASCDBRBTSResponse response = new EffectiveCalculateDASCDBRBTSResponse();
    long result = 0L;
    try {
      // get du lieu nhap tu man hinh
      EffectiveCalculateBTSDTO effectiveCalculateBTSDTO = request.getEffectiveCalculateBTSDTO();
      //                MÀN HÌNH THÔNG TIN CHUNG
      // mã code tỉnh
      //hàm
      String catProvinceCode = effectiveCalculateBTSDTO.getCatProvinceCode();
      CatProvinceDTO catProvince = catProvinceBusiness.findByCode(catProvinceCode);
      if (catProvince == null) {
        return result;
      }
      // kinh độ
      String longitude = effectiveCalculateBTSDTO.getLongitude();
      // vĩ độ
      String latitude = effectiveCalculateBTSDTO.getLatitude();
      // độ cao yêu cầu
      Double hightBts = effectiveCalculateBTSDTO.getHightBts();
      // loại cột
      String columnType = effectiveCalculateBTSDTO.getColumnType();
      // địa hình
      String topographic = effectiveCalculateBTSDTO.getTopographic();
      // vị trí
      String location = effectiveCalculateBTSDTO.getLocation();
      // địa điểm cụ thể
      String address = effectiveCalculateBTSDTO.getAddress();
      // loại trạm
      String typeStation = effectiveCalculateBTSDTO.getTypeStation();
      // nhà mạng
      String mnoViettel = effectiveCalculateBTSDTO.getMnoViettel();
      String mnoMobi = effectiveCalculateBTSDTO.getMnoMobi();
      String mnoVina = effectiveCalculateBTSDTO.getMnoVina();
      List<String> listMno = new ArrayList<>();
      if (StringUtils.isNotBlank(mnoViettel)) listMno.add(mnoViettel);
      if (StringUtils.isNotBlank(mnoMobi)) listMno.add(mnoMobi);
      if (StringUtils.isNotBlank(mnoVina)) listMno.add(mnoVina);
      // triển khai nguồn
      Long sourceDeploymentType = effectiveCalculateBTSDTO.getOurceDeployment();
      // thời gian khấu hao
      int lectDepreciationPeriod = Integer.parseInt(effectiveCalculateBTSDTO.getLectDepreciationPeriod());
      // lựa chọn nhập giá
      String silkEnterPrice = effectiveCalculateBTSDTO.getSilkEnterPrice() == null ? "" : effectiveCalculateBTSDTO.getSilkEnterPrice().trim();
      // giá thuê
      String price = effectiveCalculateBTSDTO.getPrice();
      // đơn giá thuê MB thực tế
      double costMB = ToDouble(effectiveCalculateBTSDTO.getCostMB());
      //                MÀN HÌNH HT NHÀ TRẠM
      // móng cột
      String columnFoundationItems = effectiveCalculateBTSDTO.getColumnFoundationItems();
      // chi phí móng cột
      Double costColumnFoundationItems = effectiveCalculateBTSDTO.getCostColumnFoundationItems();
      // móng nhà
      String houseFoundationItems = effectiveCalculateBTSDTO.getHouseFoundationItems();
      // chi phí móng nhà
      Double costHouseFoundationItems = effectiveCalculateBTSDTO.getCostHouseFoundationItems();
      // thân cột
      String columnBodyCategory = effectiveCalculateBTSDTO.getColumnBodyCategory();
      // chi phí thân cột
      Double costColumnBodyCategory = effectiveCalculateBTSDTO.getCostColumnBodyCategory();
      //int costColumnBodyCategory = listMno.size();
      // phòng máy
      String machineRoomItems = effectiveCalculateBTSDTO.getMachineRoomItems();
      // chi phí phòng máy
      // Double costMachineRoomItems = effectiveCalculateBTSDTO.getCostMachineRoomItems();
      int costMachineRoomItems = listMno.size();
      // tiếp địa
      String groundingItems = effectiveCalculateBTSDTO.getGroundingItems();
      // chi phí tiếp địa
      Double costGroundingItems = effectiveCalculateBTSDTO.getCostGroundingItems();
      // kéo điện
      String electricTowingItems = effectiveCalculateBTSDTO.getElectricTowingItems();
      // chi phí kéo điện
      Double costElectricTowingItems = effectiveCalculateBTSDTO.getCostElectricTowingItems();
      // lắp cột
      String columnMountingItem = effectiveCalculateBTSDTO.getColumnMountingItem();
      // chi phí lắp cột
      Double costColumnMountingItem = effectiveCalculateBTSDTO.getCostColumnMountingItem();
      // lắp nhà
      String installationHouses = effectiveCalculateBTSDTO.getInstallationHouses();
      // chi phí lắp nhà
      //Double costInstallationHouses = effectiveCalculateBTSDTO.getCostInstallationHouses();
      int costInstallationHouses = listMno.size();
      // đấu điện
      String electricalItems = effectiveCalculateBTSDTO.getElectricalItems();
      // chi phí đấu điện
      Double costElectricalItems = effectiveCalculateBTSDTO.getCostElectricalItems();
      // vận chuyển cơ giới
      String motorizedTransportItems = effectiveCalculateBTSDTO.getMotorizedTransportItems();
      // chi phí vận chuyển cơ giới
      Double costMotorizedTransportItems = effectiveCalculateBTSDTO.getCostMotorizedTransportItems();
      // vận chuyển thu công
      String itemManualShipping = effectiveCalculateBTSDTO.getItemManualShipping();
      // chi phí thủ công
      Double costItemManualShipping = effectiveCalculateBTSDTO.getCostItemManualShipping();
      // vận chuyển
      String itemShipping = effectiveCalculateBTSDTO.getItemShipping();
      // chi phí vận chuyển
      Double costItemShipping = effectiveCalculateBTSDTO.getCostItemShipping();
      // chi phí khác
      Double costItemsOtherExpenses = effectiveCalculateBTSDTO.getCostItemsOtherExpenses();
      //                  MÀN HÌNH HT NGUỒN
      // tủ nguồn và hệ thống làm mát
      Double powerCabinetCoolingSystem = effectiveCalculateBTSDTO.getOwerCabinetCoolingSystem();
      // rectifier 3000W
      Double rectifier3000 = effectiveCalculateBTSDTO.getRectifier3000();
      // battery lithium
      Double batteryLithium = effectiveCalculateBTSDTO.getBatteryLithium();
      // máy phát điện dầu
      Double oilGenerator = effectiveCalculateBTSDTO.getOilGenerator();
      // Át
      Double ats = effectiveCalculateBTSDTO.getAts();
      // giám sát điều khiển
      Double supervisionControl = effectiveCalculateBTSDTO.getSupervisionControl();
      // hệ thống phụ trợ khác
      Double otherAuxiliarySystem = effectiveCalculateBTSDTO.getOtherAuxiliarySystem();
      // công lắp đặt hệ thống nguồn
      Double publicInstallationPower = effectiveCalculateBTSDTO.getPublicInstallationPower();
      // get du lieu gia_dinh trong bang WACCHTCT
      List<WaccHtctDTO> waccHtctData = waccHTCTBusiness.getData2(new WaccHtctDTO());
      // get du lieu capex nguon
      List<CapexSourceHTCTDTO> capexSourceBts = capexSourceHTCTBusiness.getCapexSource(new CapexSourceHTCTDTO());
      // get du lieu capex
      List<CapexBtsHtctDTO> capexBtsHtct = capexBtsHtctBusiness.findByProvince(catProvinceCode);
      // get du lieu
      RatioDeliveryHtctDTO ratioDelivery = ratioDeliveryHtctBusiness.findByProvince(catProvinceCode);
      // get du lieu
      List<OfferHtctDTO> offer = offerHtctBusiness.getData8(new OfferHtctDTO());
      // get tinh toan hieu qua
      List<CalculateEfficiencyHtctDTO> calculateEfficiencyHtct = calculateEfficiencyHtctBusiness.getData9(new CalculateEfficiencyHtctDTO());
      // get thoi gian du kien cho thue
      int maxTimeRentBts = (int) getCalculateEfficiencyHtct(calculateEfficiencyHtct, TIME_RENT_BTS);
      // get rate WACC
      double rate = getRexWacc(waccHtctData, EQUITY_COST) / 100;
      double projectLoansRaito = getRexWacc(waccHtctData, PROJECT_LOANS_RATIO) / 100;
      double loanExpenses = getRexWacc(waccHtctData, LOAN_EXPENSES) / 100;
      // 1 get dòng tiền dự án
      // 1.2 get lợi nhuận sau thuế = lợi nhuận trước thuế - Thuế TNDN 20%
      // 1.2.1 get Lợi nhuận trước thuế = doanh thu - tổng chi phí

      // 1.2.1b get Tổng chi phí = D35 * D6
      // tỷ lệ vốn tự có (D6)
      double rateProjectEquity = getRexWacc(waccHtctData, RATE_OF_PROJECT_EQUITY);
      double costPublicInstallationPower = getCapexSource(capexSourceBts, COST_PUBLIC_INSTALLATION_POWER) * sourceDeploymentType * publicInstallationPower;
      double costOtherAuxiliarySystem = getCapexSource(capexSourceBts, COST_OTHER_AUXILIARY_SYSTEM) * sourceDeploymentType * otherAuxiliarySystem;
      double costOilGenerator = getCapexSource(capexSourceBts, COST_OIL_GENERATOR) * sourceDeploymentType * oilGenerator;
      double costBatteryLithium = getCapexSource(capexSourceBts, COST_BATTERY_LITHIUM) * sourceDeploymentType * batteryLithium;
      double costRectifier3000 = getCapexSource(capexSourceBts, COST_RECTIFIER3000) * sourceDeploymentType * rectifier3000;
      double costPowerCabinetCoolingSystem = getCapexSource(capexSourceBts, COST_POWER_CABINET_COOLING_SYSTEM) * sourceDeploymentType * powerCabinetCoolingSystem;
      double costAts = getCapexSource(capexSourceBts, COST_ATS) * sourceDeploymentType * ats;
      double costSupervisionControl = getCapexSource(capexSourceBts, COST_SUPERVISION_CONTROL) * sourceDeploymentType * supervisionControl;

      // Lay don gia bang Capex
      double priceColumnFoundationItems = getCapex(capexBtsHtct, columnFoundationItems, catProvinceCode); // móng cột
      double priceHouseFoundationItems = getCapex(capexBtsHtct, houseFoundationItems, catProvinceCode); // móng nhà
      double priceColumnBodyCategory = getCapex(capexBtsHtct, columnBodyCategory, catProvinceCode); // thân cột
      double priceMachineRoomItems = getCapex(capexBtsHtct, machineRoomItems, catProvinceCode); // phòng máy
      double priceGroundingItems = getCapex(capexBtsHtct, groundingItems, catProvinceCode); // tiếp địa
      double priceElectricTowingItems = getCapex(capexBtsHtct, electricTowingItems, catProvinceCode); // kéo điện
      double priceInstallationHouses = getCapex(capexBtsHtct, installationHouses, catProvinceCode); // lắp nhà
      double priceColumnMountingItem = getCapex(capexBtsHtct, columnMountingItem, catProvinceCode); // lắp cột
      double priceElectricalItems = getCapex(capexBtsHtct, electricalItems, catProvinceCode); // đấu điện
      double priceMotorizedTransportItems = getCapex(capexBtsHtct, motorizedTransportItems, catProvinceCode); // vận chuyển cơ giới
      double priceItemManualShipping = getCapex(capexBtsHtct, itemManualShipping, catProvinceCode); // vận chuyển thủ công
      double priceItemShipping = getCapex(capexBtsHtct, itemShipping, catProvinceCode); // vận chuyển

      // get ha tang nguon (D36) (Capex Cơ điện)
      double sourceInfrastructure = (costPublicInstallationPower
          + costOtherAuxiliarySystem
          + costOilGenerator
          + costBatteryLithium
          + costRectifier3000
          + costPowerCabinetCoolingSystem
          + costAts
          + costSupervisionControl) * sourceDeploymentType;
      double priceSourceInfrastructure = sourceInfrastructure / (7 * MONTH);
      // get tổng chi phí xây dựng (Capex hạ tầng)
      double totalConstructionCost = costColumnFoundationItems * priceColumnFoundationItems
          + costHouseFoundationItems * priceHouseFoundationItems
          + costColumnBodyCategory * priceColumnBodyCategory
          + costMachineRoomItems * priceMachineRoomItems
          + costGroundingItems * priceGroundingItems
          + costElectricTowingItems * priceElectricTowingItems
          + costColumnMountingItem * priceColumnMountingItem
          + costInstallationHouses * priceInstallationHouses
          + costElectricalItems * priceElectricalItems
          + costMotorizedTransportItems * priceMotorizedTransportItems
          + costItemManualShipping * priceItemManualShipping
          + costItemShipping * priceItemShipping
          + costItemsOtherExpenses * costOtherStationInfrastructure;
      double priceTotalConstructionCost = totalConstructionCost / (lectDepreciationPeriod * MONTH);
      // giá trị chi phí sửa chữa, bảo trì hệ thống cột nhà trạm
      double costMaintainerHomeStationColumn = totalConstructionCost * getUnitOffer(offer, COST_MAINTAINER);
      // giá trị chi phí bảo trì bảo dưỡng hệ thống cơ điện
      double costMaintainerElecctric = sourceInfrastructure * getUnitOffer(offer, COST_MAINTAINER_ELECTRIC);
      // giá trị Chi Phí Bảo Dưỡng
      double costMaintainer = costMaintainerHomeStationColumn + costMaintainerElecctric;
      // giá chí phí bảo dưỡng
      double priceCostMaintainer = costMaintainer / MONTH;

      // 1. get Chi phí vận hành khai thác
      // 1.1 get chi phí thuê trạm
      // 1.1.1 get đơn giá thuê mặt bằng theo tờ trình 1477
      // get type group from capex)
      Cost1477HtctDTO cost1477HtctDTO = cost1477HtctBusiness.findByGroup(catProvince.getGroupName(), address, location, typeStation);
      double costGround = cost1477HtctDTO != null ? cost1477HtctDTO.getCost1477() : 0;
      double priceCostRentStation = (costMB > costGround ? costMB : costGround); // chi phí thuê trạm
      double costOperating = priceCostRentStation + costLabor; // Chi phí vận hành khai thác
      // get Chi phí đầu tư XDCT = Capex hạ tầng + Capex Cơ điện
      double investmentCosts = sourceInfrastructure + totalConstructionCost;
      // get ti le giao khoan xuong chi nhanh
      double ratioDeliveryBts = getRatioDelivery(ratioDelivery, typeStation, topographic);
      // Chi phi khao sat dia hinh
      double costSurveyTopographic = location.toLowerCase().equals(ON_THE_ROOF) ? costRoof : costLand;
      // chi phí khảo sát địa chất
      double costSurveyGeological = 0;
      // chi phí lập dự án = Ký Hiệu * Chi Phí đầu tư XDCT
      double costProjectPlanning = getUnitOffer(offer, COST_PROJECT_PLANNING) * investmentCosts;
      // chi phi tham tra thiet ke ban ve
      double costDesignExamination = getUnitOffer(offer, COST_DESIGN_EXAMINATION) * totalConstructionCost;
      // tham tra du an
      double costVerificationProjects = getUnitOffer(offer, COST_VERIFICATION_PROJECTS) * totalConstructionCost * 1.2;
      // Chi phí lập HSMT, đánh giá HSDT thi công xây dựng
      double costHMTSHSDTConstruction = getUnitOffer(offer, COST_HSMT_HSDT_CONSTRUCTION) * totalConstructionCost;
      // Chi phí lập HSMT, đánh giá HSDT mua sắm thiết bị
      double costHMTSHSDTConstructionShoppingEquipment = getUnitOffer(offer, COST_HSMT_HSDT_SHOPPING_EQUIPMENT) * sourceInfrastructure;
      // Chi phí giám sát thi công xây dựng công trình
      double costConstructionMonitoring = getUnitOffer(offer, COST_CONSTRUCTION_MONITORING) * totalConstructionCost;
      // Chi phí giám sát lắp đặt thiết bị
      double costInstallationMonitoring = getUnitOffer(offer, COST_INSTALLATION_MONITORING) * sourceInfrastructure;
      // Chi phi tu van dau tu xay dung
      double costConstructionInvestment = costSurveyTopographic
          + costSurveyGeological
          + costProjectPlanning
          + costDesignExamination
          + costVerificationProjects
          + costHMTSHSDTConstruction
          + costHMTSHSDTConstructionShoppingEquipment
          + costInstallationMonitoring
          + costConstructionMonitoring;
      // Chi phí hạng mục chung
      double costGeneralCategory = getUnitOffer(offer, COST_GENERAL_CATEGORY) * totalConstructionCost;
      // Phí thẩm định dự án đầu tư
      double costInvestmentProject = getUnitOffer(offer, COST_INVESTMENT_PROJECTS) * investmentCosts;
      // Chi phí kiểm toán
      double costAudit = getUnitOffer(offer, COST_AUDIT) * investmentCosts;
      // Phí thẩm tra, phê duyệt quyết toán
      double costExamineApproval = getUnitOffer(offer, COST_EXAMINE_APPROVAL) * investmentCosts;
      // get GPMB
      GpmbHtctDTO gpmbHtctDTO = gpmbHtctBusiness.findByProvince(catProvinceCode);
      // Chi phí xin phép thi công
      double costLicenseConstruction = gpmbHtctDTO != null ? gpmbHtctDTO.getCostGpmb() : 0;
      // Chi phí xin phép đấu nối điện
      double costAllowElectricalConnection = gpmbHtctDTO != null ? gpmbHtctDTO.getCostNcdn() : 0;
      // chi phí khác
      double costOther = costGeneralCategory
          + costInvestmentProject
          + costAudit
          + costExamineApproval
          + costLicenseConstruction
          + costAllowElectricalConnection * publicInstallationPower;
      // giá chi phí khác
      double priceCostOther = costOther / (MONTH * lectDepreciationPeriod);
      //Chi phí dự phòng phát sinh
      double costPreventive = getUnitOffer(offer, COST_PREVENTIVE) * investmentCosts;
      // Chi phí rủi ro
      double costRisk = getUnitOffer(offer, COST_RISK) * investmentCosts;
      // chi phí quản lý dự án
      double costProjectManager = getUnitOffer(offer, COST_PROJECT_MANAGER) * investmentCosts;
      // => get giá IV. Chi phí khác
      double costCPk = (costProjectManager
          + costConstructionInvestment
          + costOther
          + costPreventive
          + costRisk) / (MONTH * lectDepreciationPeriod);
      // get chi phí quản lý chung
      double priceCostGeneralManagement = (priceTotalConstructionCost
          + priceSourceInfrastructure
          + priceCostMaintainer
          + costCPk
          + costOperating) * 0.05;

      // 1. get chi phí lãi vay = F44 / 12
      // 1.1 get giá trị chi phí lãi vay (F44)
      double interestExpenses = 0;
      double[] interestExprensesArray = new double[maxTimeRentBts];
      for (int i = 0; i < maxTimeRentBts; i++) {
        if (i == 0) {
          interestExprensesArray[i] = investmentCosts;
        } else {
          interestExprensesArray[i] = i < lectDepreciationPeriod
              ? interestExprensesArray[i - 1] - (investmentCosts / lectDepreciationPeriod)
              : 0;
        }
        interestExpenses += interestExprensesArray[i] * 0.1;
      }
      // giá chi phí lãi vay
      double priceInterestExpenses = (interestExpenses / 10) / MONTH;

      double profitValue = priceSourceInfrastructure
          + priceTotalConstructionCost
          + priceCostMaintainer
          + costOperating
          + costCPk
          + priceCostGeneralManagement
          + priceInterestExpenses;
      // get giá lợi nhuận
      double profit = profitValue * 0.1;
      // get giá thuê = (profitValue + profit) * H88
      double costRent = (profitValue + profit) * 1;
      // get Đơn giá thuê VTNet 1 tháng/trạm
      double priceVTNet = silkEnterPrice != null && silkEnterPrice.equals(SELF_INCLUDED_RENTAL_PRICE)
          ? ToDouble(price)
          : costRent * rentalRate;
      // get Doanh Thu

      double revenue = calculateRevenue(priceVTNet, listMno, mnoViettel, mnoMobi, mnoVina);
      // Chi phi quan ly du an
      double costProjectManagement = (sourceInfrastructure * 2 + totalConstructionCost) * 1.335 / 100 * 0.8 * 1.1;

      // CHI PHÍ TƯ VẤN ĐẦU TƯ XÂY DỰNG
      double costConstructionInvestmentSource = calculateCostConstructionInvestment(totalConstructionCost, sourceInfrastructure, costSurveyTopographic);
      // CHI PHÍ KHÁC
      double costProjectManagementSource = costProjectManagement + costConstructionInvestmentSource + investmentCosts;
      double costOtherSource = calculateCostOtherSource(
          totalConstructionCost, costProjectManagementSource,
          costLicenseConstruction, costAllowElectricalConnection * publicInstallationPower
      );
      // CHI PHÍ DỰ PHÒNG
      double costRedundancy = 5 * (investmentCosts + costProjectManagement + costConstructionInvestmentSource + costOtherSource) / 100;
      // get ha tang _ cot nha trạm (D35)
      double infrastructureStationHouse = totalConstructionCost
          - (1 - (ratioDeliveryBts / 100)) * (priceHouseFoundationItems
          + priceGroundingItems
          + priceElectricTowingItems
          + priceInstallationHouses)
          + costProjectManagement
          + costConstructionInvestmentSource
          + costOtherSource
          + costRedundancy;
      // get TỔNG MỨC ĐẦU TƯ
      double totalInvestment = sourceInfrastructure + infrastructureStationHouse;
      // Tổng chi phí năm 1(E66) = SUM OPEX
      // 1. Tính Chi phí lãi vay + trả nợ vay
      int lenghtArray = maxTimeRentBts + 1;
      double[] interestExpensesAndLoanRepayment = new double[lenghtArray]; // E62
      double[] totalCost = new double[lenghtArray];
      double[] profitBeforeTax = new double[lenghtArray]; // lợi nhuận trước thuế
      double[] profitAfterTax = new double[lenghtArray]; // lợi nhuận sau thuế
      double[] projectCashFlow = new double[lenghtArray]; // dòng tiền dự án
      double costManagerTCT = 0.05 * revenue; // Chi phí quản lý TCT 5%
      double costPerformMining = 0.04 * revenue; // Chi phí vân hành khai thác 4% * DT\
      double costMaintenance = 0.25 / 100 * totalInvestment; // Bảo dưỡng 0.25% * (XD + TB)
      double[] rentLand = new double[lenghtArray]; // thue dat sau 5 nam tang 10%
      double[] debtStartOfTerm = new double[lenghtArray]; // dư nợ đầu kỳ
      double debtIncurred = 0; // Nợ phát sinh trong kỳ (I9)
      double[] debtEndOfTerm = new double[lenghtArray]; // dư nợ cuối kỳ
      double[] cumulativeCashFlow = new double[lenghtArray]; // dòng tiền lũy kế
      double timePayBack = 0; // thời gian hoàn vốn
      // Doanh thu (E64)
      int timeDepreciation = hightBts <= 21 ? 5 : 10;
      double taxInCome = getCalculateEfficiencyHtct(calculateEfficiencyHtct, TAX_INCOME);

      for (int year = 0; year < lenghtArray; year++) {
        double interest;
        // get khau hao
        double depreciation = countDepreciation(year, calculateEfficiencyHtct, infrastructureStationHouse, timeDepreciation,
            costPowerCabinetCoolingSystem, costRectifier3000, costOilGenerator, costAts,
            costSupervisionControl, costOtherAuxiliarySystem, costPublicInstallationPower, costBatteryLithium);
        if (year == 0) {
          // LỢI NHUẬN TRƯỚC THUẾ NĂM 0 = D35 * D6
          profitBeforeTax[year] = -(infrastructureStationHouse * (rateProjectEquity / 100));
          // du no dau ky
          debtStartOfTerm[year] = projectLoansRaito * totalInvestment;
          // lãi vay trả trong kỳ
          interest = loanExpenses * debtStartOfTerm[year];
          // Dư nợ cuối kỳ
          debtEndOfTerm[year] = debtStartOfTerm[year] + interest;
          // get LỢI NHUẬN SAU THUẾ
          profitAfterTax[year] = profitBeforeTax[year];
          // get DÒNG TIỀN DỰ ÁN
          projectCashFlow[year] = depreciation + profitAfterTax[year];
          // get Dòng tiền lũy kế
          cumulativeCashFlow[year] = 0;
        } else {
          // tính Chi phí sửa chữa thay thế lớn TB = nhóm tài sản 4 năm = Battery Lithium
          double repairCost = (year == 6 || year == 11) ? costBatteryLithium : 0;
          //  START: TÍNH BẢNG KẾ HOẠCH TRẢ NỢ
          // Thuê đất sau năm 5 tăng 10%
          rentLand[year] = getRentLand(year, costMB);
          // dư nợ đầu kỳ
          debtStartOfTerm[year] = debtEndOfTerm[year - 1]; // I8
          // lãi vay trả trong kỳ
          interest = loanExpenses * debtStartOfTerm[year];
          // Tổng mức trả nợ mỗi kỳ
          double totalLiabilities = debtEndOfTerm[year - 1] > 0
              ? (revenue - costManagerTCT - costPerformMining - costMaintenance - rentLand[year] - repairCost)
              : 0;
          // Nợ gốc trả trong kỳ(I10)
          double originalDebt = totalLiabilities > (debtEndOfTerm[year - 1] + interest)
              ? debtEndOfTerm[year - 1]
              : totalLiabilities - interest;
          if (year == 4) {
            originalDebt = debtStartOfTerm[year];
            totalLiabilities = debtStartOfTerm[year] * loanExpenses + originalDebt;
          }
          // Dư nợ cuối kỳ = I8 + I9 - I10
          debtEndOfTerm[year] = debtStartOfTerm[year] + debtIncurred - originalDebt;
          //Chi phí lãi vay + trả nợ vay
          interestExpensesAndLoanRepayment[year] = totalLiabilities;
          // TÍNH TỔNG CHI PHÍ = E62-E61+E60+E58+E57+E55+E63+E56+E59
          totalCost[year] = interestExpensesAndLoanRepayment[year] // chi phí lãi vay + trả nợ vay (E62)
              + depreciation // khẩu hao (E60)
              + rentLand[year] // Thuê đất sau năm 5 tăng 10% (E58)
              + costMaintenance // E57
              + costPerformMining // E56
              + costManagerTCT // E55
              + repairCost;
          // LỢI NHUẬN TRƯỚC THUẾ NĂM 1 = E66 - E64
          profitBeforeTax[year] = revenue - totalCost[year];
          // get LỢI NHUẬN SAU THUẾ
          profitAfterTax[year] = profitBeforeTax[year] - (profitBeforeTax[year] >= 0 ? profitBeforeTax[year] * taxInCome : 0);
          // get DÒNG TIỀN DỰ ÁN
          projectCashFlow[year] = depreciation + profitAfterTax[year];
          // get Dòng tiền lũy kế
          cumulativeCashFlow[year] = getCumulativeCashFlow(year, projectCashFlow);
          //tính thời gian hoàn vốn
          timePayBack = getTimePayBack(timePayBack, cumulativeCashFlow, projectCashFlow, year);
        }
      }
      // 4. tính Hiệu quả
      double npvProject = getNPV_BTS(projectCashFlow, rate);
      double irrProject = Irr.irr(projectCashFlow, rate);
      return (npvProject > 0) && (irrProject > rate) && (timePayBack <= timeDepreciationBTS) ? 1 : 0;
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      ResultInfo resultInfo = new ResultInfo();
      resultInfo.setStatus(ResultInfo.RESULT_NOK);
      resultInfo.setMessage("Có lỗi xảy ra");
      response.setResultInfo(resultInfo);
    }
    return result;
  }

  /**
   * get data import from WaccHTCT
   *
   * @param waccData
   * @param filterValue
   * @return rex
   */
  private double getRexWacc(List<WaccHtctDTO> waccData, String filterValue) {
    if (waccData.size() > 0) {
      for (WaccHtctDTO wacc : waccData) {
        if (wacc.getWaccName().trim().toLowerCase().equals(filterValue.trim().toLowerCase())) {
          return wacc.getWaccRex();
        }
      }
    }
    return 0;
  }

  /**
   * get data import from OfferHTCT
   *
   * @param offerData
   * @param filterValue
   * @return
   */
  private double getUnitOffer(List<OfferHtctDTO> offerData, String filterValue) {
    if (offerData.size() > 0) {
      for (OfferHtctDTO offer : offerData) {
        if (offer.getCategoryOffer().trim().toLowerCase().equals(filterValue.trim().toLowerCase())) {
          return ToDouble(offer.getSymbol()) / 100;
        }
      }
    }
    return 0;
  }

  /**
   * @param assumptions
   * @param filterValue
   * @return
   */
  private double getValueObject(@NotNull List<EffectiveCalculateDasDTO> assumptions, String filterValue) {
    if (assumptions.size() > 0) {
      for (EffectiveCalculateDasDTO assumption : assumptions) {
        String contentContentAssumptions = assumption.getContentAssumptions();
        if (contentContentAssumptions.trim().toLowerCase().equals(filterValue.trim().toLowerCase())) {
          String unit = assumption.getUnit();
          if(StringUtils.isNotBlank(unit)){
            if (unit.equals("%")) return assumption.getCostAssumptions() / 100;
          }
          return assumption.getCostAssumptions();
        }
      }
    }
    return 0;
  }

  /**
   * @param assumptionsCapex
   * @param filterValue
   * @return
   */
  private double getValue(List<EffectiveCalculateDasCapexDTO> assumptionsCapex, String filterValue) {
    if (assumptionsCapex.size() > 0) {
      for (EffectiveCalculateDasCapexDTO assumptionCapex : assumptionsCapex) {
        String itemContent = assumptionCapex.getItem();
        if(StringUtils.isNotBlank(itemContent)){
          if (itemContent.trim().toLowerCase().equals(filterValue.trim().toLowerCase())) {
            return assumptionCapex.getCost();
          }
        }
      }
    }
    return 0;
  }

  /**
   * @param capexSourceBts
   * @param filterValue
   * @return
   */
  private double getCapexSource(List<CapexSourceHTCTDTO> capexSourceBts, String filterValue) {
    if (capexSourceBts.size() > 0) {
      for (CapexSourceHTCTDTO capexSource : capexSourceBts) {
        String contentCapex = capexSource.getContentCapex();
        if (contentCapex.trim().toLowerCase().equals(filterValue.trim().toLowerCase())) {
          String unit = capexSource.getUnit();
          if(StringUtils.isNotBlank(unit)){
            if (unit.equals("%")) return capexSource.getCostCapex() / 100;
          }
          return capexSource.getCostCapex();
        }
      }
    }
    return 0;
  }

  private double getCapex(List<CapexBtsHtctDTO> capexBts, String workCapex, String provinceCode) {
    if (capexBts.size() > 0) {
      for (CapexBtsHtctDTO capex : capexBts) {
        String capexProvinceCode = capex.getProvinceCode();
        String capexWorkCapex = capex.getWorkCapex();
        if(StringUtils.isNotBlank(capexProvinceCode) && StringUtils.isNotBlank(capexWorkCapex)){
          if (capexProvinceCode.equals(provinceCode) && capexWorkCapex.equals(workCapex)) {
            return capex.getCostCapexBts();
          }
        }
      }
    }
    return 0;
  }

  private double getCalculateEfficiencyHtct(List<CalculateEfficiencyHtctDTO> calculateEfficiencyHtcts, String filterValue) {
    if (calculateEfficiencyHtcts.size() > 0) {
      for (CalculateEfficiencyHtctDTO calculateEfficiencyHtct : calculateEfficiencyHtcts) {
        String contentCalculateEfficiency = calculateEfficiencyHtct.getContentCalEff();
        if (contentCalculateEfficiency.trim().toLowerCase().equals(filterValue.trim().toLowerCase())) {
          String unit = calculateEfficiencyHtct.getUnit();
          if(StringUtils.isNotBlank(unit)){
            if (unit.equals("%")) return calculateEfficiencyHtct.getCostCalEff() / 100;
          }
          return calculateEfficiencyHtct.getCostCalEff();
        }
      }
    }
    return 0;
  }

  /**
   * @param ratioDelivery
   * @param typeStation
   * @param topographic
   * @return
   */
  private double getRatioDelivery(RatioDeliveryHtctDTO ratioDelivery, String typeStation, String topographic) {
    if (ratioDelivery == null) return 0;
    switch (typeStation.toLowerCase()) {
      case MACRO_STATION:
        if (topographic.toLowerCase().equals(DELTA)) return ratioDelivery.getCostDeliveryBts();
        if (topographic.toLowerCase().equals(MOUNTAIN)) return ratioDelivery.getCostMountainsBts();
        if (topographic.toLowerCase().equals(ON_THE_ROOF)) return ratioDelivery.getCostRoofBts();
      case RRU_STATION:
        if (topographic.toLowerCase().equals(DELTA)) return ratioDelivery.getCostDeliveryPru();
        if (topographic.toLowerCase().equals(MOUNTAIN)) return ratioDelivery.getCostMountainsPru();
        if (topographic.toLowerCase().equals(ON_THE_ROOF)) return ratioDelivery.getCostRoofPru();
      case SMALLCELL_STATION:
        if (topographic.toLowerCase().equals(DELTA)) return ratioDelivery.getCostDeliverySmallcell();
        if (topographic.toLowerCase().equals(MOUNTAIN)) return ratioDelivery.getCostMountainsSmallcell();
        if (topographic.toLowerCase().equals(ON_THE_ROOF)) return ratioDelivery.getCostRoofSmallcell();
      default:
        return 0;
    }
  }

  /**
   * tinh doanh thu nhà mạng
   *
   * @param priceVTNet
   * @param mnoViettel
   * @param mnoMobi
   * @return
   */
  private double calculateRevenue(double priceVTNet, List<String> listMno, String mnoViettel, String mnoMobi, String mnoVina) {
    final double PARAMETER = 0.8;
    final int MAX_NETWORK = 3;
    if (listMno.size() > 0) {
      switch (listMno.size()) {
        case MAX_NETWORK: {
          return (priceVTNet + 2 * UNIT_RENT_MOBIPHONE) * MONTH * PARAMETER;
        }
        case MAX_NETWORK - 1: {
          return (priceVTNet + UNIT_RENT_MOBIPHONE) * MONTH * PARAMETER;
        }
        case MAX_NETWORK - 2: {
          String firstItem = listMno.get(0);
          if (firstItem.equals(mnoViettel)) {
            System.out.println("mnoViettel");
            return priceVTNet * MONTH;
          } else if (firstItem.equals(mnoMobi)) {
            return UNIT_RENT_MOBIPHONE * MONTH;
          } else if (firstItem.equals(mnoVina)) { //Chua co cach tinh cho nha mang Vina
            return 0;
          } else {
            return priceVTNet * MONTH;
          }
        }
        default:
          return 0;
      }
    }
    return 0;
  }

  /**
   * @param year
   * @param dasType
   * @param totalArea
   * @param timeLoan
   * @return
   */
  public double getRevenueDAS(int year, double dasType, double totalArea, double timeLoan) {
    double revenueDAS = (priceHTDAS * dasType) *
        (totalArea * dasType) * (kpi) * MONTH;
    return revenueDAS * (year > timeLoan ? (1 - percentageDiscount) : 1);
  }

  /**
   * @param year
   * @param costMB
   * @return
   */
  public double getRentLand(int year, double costMB) {
    double rentLand = costMB * MONTH;
    return year < 5
        ? rentLand
        : rentLand * (year < 11
        ? percentBTS
        : percentBTS * percentBTS);
  }

  /**
   * @param args
   * @param rate
   * @return
   */
  public double getNPV(double[] args, double rate) {
    // Initialize net present value
    double value = 0;

    // Loop on all values
    for (int j = 1; j <= args.length; j++) {
      value += args[j - 1] / Math.pow(1 + rate, j);
    }

    // Return net present value
    return value;
  }


  public double getNPV_BTS(double[] args, double rate) {
    // Initialize net present value
    double value = 0;
    // Loop on all values
    for (int j = 0; j < args.length; j++) {
      value += args[j] / Math.pow(1 + rate, j);
    }

    // Return net present value
    return value;
  }

  /**
   * @param arrayCashFlowDiscountCumulative
   * @param i
   * @return
   */
  public double getTimePayBack(double value, double[] arrayCashFlowDiscountCumulative, double[] arrayCashFlowDiscount, int i) {
    value += arrayCashFlowDiscountCumulative[i - 1] < 0 && arrayCashFlowDiscountCumulative[i] < 0
        ? 1
        : arrayCashFlowDiscountCumulative[i - 1] < 0 && arrayCashFlowDiscountCumulative[i] > 0
        ? -arrayCashFlowDiscountCumulative[i - 1] / arrayCashFlowDiscount[i]
        : 0;
    return value;
  }

  /**
   * @param cashFlow
   * @param rate
   * @param timePayBack
   * @param timeDepreciationBTS
   * @return
   */
  public double calculateEfficiency(double[] cashFlow, double rate, double timePayBack, double timeDepreciationBTS) {
    double npvProject = getNPV(cashFlow, rate);
    double irrProject = Irr.irr(cashFlow, rate);
    System.out.println(npvProject + "," + irrProject + "," + rate + "," + timePayBack + "," + timeDepreciationBTS);
    return (npvProject > 0) && (irrProject > rate) && (timePayBack <= timeDepreciationBTS) ? 1 : 0;
  }

  public double calculateCostConstructionInvestment(double totalConstructionCost, double sourceInfrastructure, double costSurveyTopographic) {
    // chi phí khảo sát địa chất
    double costSurveyGeological = 0;
    // chi phí lập dự án = Ký Hiệu * Chi Phí đầu tư XDCT
    double costProjectPlanning = 0.245 * 0.65 * totalConstructionCost / 100;
    // tham tra du an
    double costVerificationProjects = 0.048 * 0.5 * totalConstructionCost / 100;
    // chi phi thiet ke ban ve
    double costDesignExamination = 1.121 * totalConstructionCost / 100;
    // chi phi tham tra thiet ke ban ve
    double costVerificationDesignExamination = 0.073 * totalConstructionCost / 100;
    // chi phi Thẩm tra dự toán
    double costVerificationProject = 1.2 * 0.069 * totalConstructionCost / 100;
    // Chi phí lập HSMT, đánh giá HSDT thi công xây dựng
    double costHMTSHSDTConstruction = 0;
    // Chi phí lập HSMT, đánh giá HSDT mua sắm thiết bị
    double costHMTSHSDTConstructionShoppingEquipment = 0;
    // Chi phí giám sát thi công xây dựng công trình
    double costConstructionMonitoring = 0.102 * totalConstructionCost / 100;
    // Chi phí giám sát lắp đặt thiết bị
    double costInstallationMonitoring = 0.381 * sourceInfrastructure / 100; // sai
    // Chi phi tu van dau tu xay dung
    double costConstructionInvestment = costSurveyTopographic
        + costSurveyGeological
        + costProjectPlanning
        + costDesignExamination
        + costVerificationProjects
        + costVerificationDesignExamination
        + costVerificationProject
        + costHMTSHSDTConstruction
        + costHMTSHSDTConstructionShoppingEquipment
        + costInstallationMonitoring
        + costConstructionMonitoring;
    return costConstructionInvestment;
  }

  public double calculateCostOtherSource(
      double totalConstructionCost, double costProjectManagement,
      double costLicenseConstruction, double costAllowElectricalConnection
  ) {
    // Chi phí hạng mục chung
    double costGeneralCategory = 3 * totalConstructionCost / 100;
    // Phí thẩm định dự án đầu tư
    double costInvestmentProject = 0.0076 * costProjectManagement / 100; // sai
    // Phí thẩm định thiết kế
    double costDesigin = 0.0144 * totalConstructionCost / 100;
    // Phí thẩm định dự toán
    double costEstimate = 0.0136 * totalConstructionCost / 100;
    // Chi phí kiểm toán (nếu được yêu cầu)
    double costAudit = 0.331 * costProjectManagement / 100;
    // Phí thẩm tra, phê duyệt quyết toán vốn đầu tư [Nếu có k5 thì mục này tính 50%]
    double costInvestment = 0.229 * costProjectManagement * 0.5 / 100;
    // chi phí khác
    double costOther = costGeneralCategory
        + costInvestmentProject
        + costDesigin
        + costAudit
        + costEstimate
        + costInvestment
        + costLicenseConstruction
        + costAllowElectricalConnection;
    return costOther;
  }

  public double countDepreciation(
      int year, List<CalculateEfficiencyHtctDTO> calculateEfficiencyHtct,
      double infrastructureStationHouse, double timeDepreciation,
      double costPowerCabinetCoolingSystem, double costRectifier3000, double costOilGenerator, double costAts, double costSupervisionControl,
      double costOtherAuxiliarySystem, double costPublicInstallationPower, double costBatteryLithium
  ) {
    int timeDepreciation4 = 4;
    int timeDepreciation7 = 7;
    if (year == 0) return 0;

    double totalDepreciation = 0;
    double totalDepreciation4 = 0;
    double depreciation = (infrastructureStationHouse / timeDepreciation);
    Map<String, Double> array = new HashMap();
    array.put(COST_POWER_CABINET_COOLING_SYSTEM, costPowerCabinetCoolingSystem);
    array.put(COST_PUBLIC_INSTALLATION_POWER, costPublicInstallationPower);
    array.put(COST_OIL_GENERATOR, costOilGenerator);
    array.put(COST_BATTERY_LITHIUM, costBatteryLithium);
    array.put(COST_RECTIFIER3000, costRectifier3000);
    array.put(COST_ATS, costAts);
    array.put(COST_OTHER_AUXILIARY_SYSTEM, costOtherAuxiliarySystem);
    array.put(COST_SUPERVISION_CONTROL, costSupervisionControl);
    List<String> myList = new ArrayList<>(array.keySet());
    for (String key : myList) {
      double timeObject = getCalculateEfficiencyHtct(calculateEfficiencyHtct, key);
      if (timeObject == timeDepreciation7) {
        totalDepreciation += array.get(key);
      } else if (timeObject == timeDepreciation4) {
        totalDepreciation4 += array.get(key);
      }
    }

    if (year <= timeDepreciation4)
      depreciation += ((totalDepreciation / 7) + ((totalDepreciation + totalDepreciation4) / 4)); //Công thức tính khấu hao
    if (year > timeDepreciation4 && year <= timeDepreciation7) depreciation += (totalDepreciation / 7);
    if (year > timeDepreciation) depreciation = 0;
    return depreciation;
  }

  public double getCumulativeCashFlow(int year, double[] projectCashFlow) {
    double calculateCumulative = 0;
    for (int i = 0; i <= year; i++) {
      calculateCumulative += projectCashFlow[i];
    }
    return calculateCumulative;
  }

  public double ToDouble(String input) {
    double values;
    if (!StringUtils.isNotBlank(input)) return 0;
    try {
      values = Double.valueOf(input.trim());
    } catch (NumberFormatException nfe) {
      return 0;
    }
    return values;
  }
}

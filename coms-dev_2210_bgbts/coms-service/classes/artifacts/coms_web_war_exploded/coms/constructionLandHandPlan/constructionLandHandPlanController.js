(function() {
    'use strict';
    var controllerId = 'constructionLandHandPlanController';

    angular.module('MetronicApp').controller(controllerId, constructionLandHandPlanController);

    function constructionLandHandPlanController($scope, $rootScope, $timeout, gettextCatalog,$filter,
                                                kendoConfig, $kWindow,constructionLandHandPlanService,htmlCommonService,
                                                CommonService, PopupConst, Restangular, RestEndpoint,Constant) {
        initCommonFunction($scope, $rootScope,$filter );
        var vm = this;
        vm.showSearch = true;
        vm.isCreateNew = false;
        vm.showDetail = false;
        vm.constructionLandHandPlanSearch={};
        vm.constructionLandHandPlan={};
        vm.String="Quản lý công trình > Quản lý công trình >Quản lý kế hoạch BGMB";
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            fillDataTable([]);
     // fillDataTablecatPartNer([]);
     //       fillconstructionLandHandPlanDetailTable();
            initDropDownList();
        }
        function initDropDownList(){
            vm.yearDataList=[];
            vm.monthDataList=[];
            var currentYear = (new Date()).getFullYear();
            for(var i =currentYear-5;i<currentYear+6;i++){
                vm.yearDataList.push({
                    id:i,
                    name:i

                })
            }
            /*
			 * for (var i = 1; i < 13; i++) { vm.monthDataList.push({ id: i,
			 * name: i }) }
			 */
            vm.monthDataList = [
                {id:1,name:'01'},
                {id:2,name:'02'},
                {id:3,name:'03'},
                {id:4,name:'04'},
                {id:5,name:'05'},
                {id:6,name:'06'},
                {id:7,name:'07'},
                {id:8,name:'08'},
                {id:9,name:'09'},
                {id:10,name:'10'},
                {id:11,name:'11'},
                {id:12,name:'12'}
            ];
            vm.yearDownListOptions = {
                dataSource: vm.yearDataList,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
            vm.monthDownListOptions = {
                dataSource: vm.monthDataList,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
            vm.catPartnerOptions = {
                dataTextField: "code",
                dataValueField: "id",
                select: function (e) {
                    vm.selectedStation = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.constructionLandHandPlan.catPartnerCode = dataItem.code;
                    vm.constructionLandHandPlan.catPartnerId = dataItem.id;
                },
                pageSize: 10,

                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function(options) {
                            vm.selectedDept1=false;
                            return Restangular.all("departmentRsService/department/" + 'getAutocompleteLanHan').post({name:vm.constructionLandHandPlan.catPartnerCode,pageSize:vm.catPartnerOptions.pageSize}).then(function(response){
                                options.success(response);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
                change: function(e) {
                    if (e.sender.value() === '') {
                        vm.constructionLandHandPlan.catPartnerCode = null;
                        vm.constructionLandHandPlan.catPartnerId = null;
                    }
                },
                ignoreCase: false
            }
            /*
			 * vm.catPartnerOptions = { dataTextField: "code", dataValueField:
			 * "id", select: function (e) { vm.selectedStation = true; var
			 * dataItem = this.dataItem(e.item.index());
			 * vm.constructionLandHandPlan.catPartnerCode = dataItem.code;
			 * vm.constructionLandHandPlan.catPartnerId = dataItem.id; } }
			 */
        }
        vm.validatorOptions = kendoConfig.get('validatorOptions');
        vm.formatAction=function(dataItem){
            var template=
                '<div class="text-center #=constructionLandHandPlanId#"">'
            template+='<button type="button"'+
            'class="btn btn-default padding-button box-shadow  #=constructionLandHandPlanId#"'+
            'disble="" ng-click=vm.edit(#=constructionLandHandPlanId#)>'+
            '<div class="action-button edit" uib-tooltip="Sửa" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>'+
            '</button>'+
            '<button type="button"'+
            'class="btn btn-default padding-button box-shadow #=constructionLandHandPlanId#"'+
            'ng-click=vm.send(#=constructionLandHandPlanId#)>'+
            '<div class="action-button export" uib-tooltip="Gửi tài chính" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>'+
            '</button>'+
            '<button type="button"'+
            'class="btn btn-default padding-button box-shadow #=constructionLandHandPlanId#"'+
            'ng-click=vm.remove(#=constructionLandHandPlanId#)>'+
            '<div class="action-button del" uib-tooltip="Xóa" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>'+
            '</button>'
            +
            '<button type="button" class="btn btn-default padding-button box-shadow #=constructionLandHandPlanId#"'+
            'ng-click=vm.cancelUpgradeLta(#=constructionLandHandPlanId#)>'+
            '<div class="action-button cancelUpgrade" uib-tooltip="Hủy nâng cấp" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>'+
            '</button>';
            template+='</div>';
            return dataItem.groupId;
        }
        setTimeout(function(){
            $("#keySearch").focus();
        },15);
        /*
		 * setTimeout(function(){ $("#appIds1").focus(); },15);
		 */
        var record=0;

        function fillDataTable(data) {

            vm.gridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template:
                        '<div class=" pull-left ">'+
                        '<button class="btn btn-qlk padding-search-right addQLK"'+
                        'ng-click="vm.add()" uib-tooltip="Tạo mới" translate>Tạo mới</button>'+

                        '<button class="btn btn-qlk padding-search-right excelQLK"'+
                        'ng-click="vm.importHanPlan()" uib-tooltip="Import" translate>Import</button>'+
                        '</div>'
                        +
                        '<div class="btn-group pull-right margin_top_button margin10">'+
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'+
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'+
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
                        '<label ng-repeat="column in vm.constructionLandHandPlanGrid.columns| filter: vm.gridColumnShowHideFilter">'+
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
                        '</label>'+
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCount").text("" + response.total);
                            vm.count = response.total;
                            return response.total; // total is returned in
                            // the "total" field of
                            // the response
                        },
                        data: function (response) {
                            var list = response.data;
                            return response.data; // data is returned in
                            // the "data" field of
                            // the response
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "contructionLandHandoverPlan/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.constructionLandHandPlanSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.constructionLandHandPlanSearch.page = options.page
                            vm.constructionLandHandPlanSearch.pageSize = options.pageSize
                            return JSON.stringify(vm.constructionLandHandPlanSearch)

                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                save: function(e) {
                    vm.constructionLandHandPlanGrid.refresh();
                },
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSize: 10,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                columns: [
                    {
                        title: "TT",
                        field:"stt",
                        template: function () {
                            return ++record;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Tên kế hoạch",
                        field: 'name',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Đơn vị nhận bàn giao",
                        field: 'sysGroupName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Mã trạm",
                        field: 'catStationCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Mã công trình",
                        field: 'contructionCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Đối tác bàn giao",
                        field: 'catPartnerCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Ngày bàn giao dự kiến",
                        field: 'groundPlanDate',
                        /*template : function(data){
                            return formatDate(data.groundPlanDate);
                        },*/
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Ghi chú",
                        field: 'description',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Thao tác",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template: dataItem =>
                        '<div class="text-center">'
                        +'<button style=" border: none; background-color: Orange;" id="updateId" ng-hide="dataItem.status==0" ng-click="vm.edit(dataItem)" class=" icon_table "'+
                        '   uib-tooltip="Sửa" translate>'+
                        '<i class="fa fa-pencil" style="color:Orange"  ng-hide="dataItem.status==0"   aria-hidden="true"></i>'+
                        '</button>'

                        +'<button style=" border: none; background-color: white;" id=""'+
                        'class=" icon_table" ng-click="vm.remove(dataItem)" uib-tooltip="Xóa" translate'+'>'+
                        '<i class="fa fa-trash" style="color: #337ab7;" ng-hide="dataItem.status==0"  aria-hidden="true"></i>'+
                        '</button>'
                        +'</div>',
                        width: '15%'
                    }
                ]
            });
        }
        function formatDate(date) {
            var newdate = new Date(date);
            return kendo.toString(newdate, "dd/MM/yyyy");
        }
       /*
		 * function fillDataTablecatPartNer(data) {
		 * 
		 * vm.deliveryUnitOptions = kendoConfig.getGridOptions({ autoBind: true,
		 * scrollable: false, resizable: true, editable: false, dataBinding:
		 * function () { record = (this.dataSource.page() - 1) *
		 * this.dataSource.pageSize(); }, reorderable: true,
		 * 
		 * dataSource: { serverPaging: true, schema: { total: function
		 * (response) { vm.countCons = response.total; return response.total; //
		 * total is returned in // the "total" field of // the response }, data:
		 * function (response) { return response.data; // data is returned in //
		 * the "data" field of // the response } }, transport: { read: { // Thuc
		 * hien viec goi service url: Constant.BASE_SERVICE_URL +
		 * "constructionService/doSearch", contentType: "application/json;
		 * charset=utf-8", type: "POST" }, parameterMap: function (options,
		 * type) { // vm.constructionLandHandPlan.employeeId = //
		 * Constant.user.srvUser.catEmployeeId; vm.constructionLandHandPlan.page =
		 * options.page vm.constructionLandHandPlan.pageSize = options.pageSize
		 * 
		 * return JSON.stringify(vm.constructionLandHandPlan) } }, pageSize: 10 },
		 * noRecords: true, columnMenu: false, messages: { noRecords:
		 * gettextCatalog.getString("<div style='margin:5px'>Không có kết quả
		 * hiển thị</div>") }, pageable: { refresh: false, pageSizes: [10, 15,
		 * 20, 25], messages: { display: "{0}-{1} của {2} kết quả",
		 * itemsPerPage: "kết quả/trang", empty: "<div style='margin:5px'>Không
		 * có kết quả hiển thị</div>" } }, columns: [ { title: "TT", field:
		 * "stt", template: function () { return ++record; }, width: '5%',
		 * columnMenu: false, headerAttributes: {style:
		 * "text-align:center;font-weight: bold;"}, attributes: { style:
		 * "text-align:center;" } }, { title: "Loại đối tác", field: 'code',
		 * width: '20%', headerAttributes: {style:
		 * "text-align:center;font-weight: bold;"}, attributes: { style:
		 * "text-align:left;" } }, { title: "Mã đối tác", field: 'name', width:
		 * '30%', headerAttributes: {style: "text-align:center;font-weight:
		 * bold;"}, attributes: { style: "text-align:left;" } }, { title: "Tên
		 * đối tác", field: 'catContructionTypeName', width: '10%',
		 * headerAttributes: {style: "text-align:center;font-weight: bold;"},
		 * attributes: { style: "text-align:left;" } }, { title: "Trạng thái",
		 * field: 'status', width: '10%', template: function (dataItem) { if
		 * (dataItem.status == 0) { return "<span name='status' font-weight:
		 * bold;'>Hết hiệu lực</span>" } else if (dataItem.status == 1) {
		 * return "<span name='status' font-weight: bold;'>Hiệu lực</span>" } },
		 * headerAttributes: {style: "text-align:center;font-weight: bold;"},
		 * attributes: { style: "text-align:left;" } }, { title: "Chọn",
		 * template: '<div class="text-center "> ' + ' <a type="button" class="
		 * icon_table" uib-tooltip="Chọn" translate>'+ ' <i id="#=code#"
		 * ng-click=save(dataItem) class="fa fa-check color-green"
		 * aria-hidden="true"></i> '+ ' </a>' +'</div>', width: "15%",
		 * field:"stt" } ] }); }
		 */

        function pushDataToconstructionLandHandPlanTable(data){
            var grid = vm.constructionLandHandPlanDetailGrid;
            if(grid){
                grid.dataSource.data(data);
                grid.refresh();
            }
        }
       /* function fillconstructionLandHandPlanDetailTable(){
            vm.constructionLandHandPlanDetailGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
                reorderable: true,
                noRecords: true,
                save: function () {
                    var grid = this;
                    setTimeout(function () {
                        grid.refresh();
                    })
                },
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSize: 10,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                columns: [
                    { field: "sysGroupId",
                        // footerTemplate: "#=aggregates.source.sum#",
                        hidden:true,
                        groupHeaderTemplate: '{{dataItem.items[0].sysGroupName}}</td>'+
                        '<td class="text-right">#= aggregates.source.sum  #</td>'+
                        '<td class="text-right">#= aggregates.quantity.sum #</td>'+
                        '<td class="text-right">#= aggregates.complete.sum #</td>'+
                        '<td class="text-right">#= aggregates.revenue.sum #</td>'+
                        '<td class="text-center"><button class="icon_table text-center" style=" margin-left:6px;border: none; background-color: white;" ng-click="vm.removeconstructionLandHandPlanPerSysGroup(#= value #)" uib-tooltip="Xóa" translate> <i style="color: darkblue" aria-hidden="true" class="fa fa-trash"></i>'+
                        '</button>'
                    },
                    {
                        title: "TT",
                        field:"stt",
                        template: function () {
                            return ++record;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        editable: false
                    },
                    {
                        title: "Đơn vị",
                        field: 'month',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        editable: false

                    },
                    {
                        title: "Nguồn việc",
                        field: 'source',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: "#=sum#"
                    },
                    {
                        title: "Sản lượng",
                        field: 'quantity',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: "#=sum#"
                    },
                    {
                        title: "HSHC",
                        field: 'complete',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: "#=sum#"
                    },
                    {
                        title: "Doanh thu",
                        field: 'revenue',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: "#=sum#"
                    },
                    {
                        title: "Thao tác",
                        template: dataItem =>
                        '<div class="text-center">'
                        +
                        '<button style=" border: none; background-color: white;" id="removeId"'+
                        'class=" icon_table" ng-click="vm.removeconstructionLandHandPlanDetail($event)" uib-tooltip="Xóa" translate'+
                        '>'+
                        '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>'+
                        '</button>'
                        +'</div>',
                        field:'action',
                        width: '10%',
                    }
                ],
                dataSource: {
                    schema: {
                        model: {
                            id: "constructionLandHandPlanId",
                            fields: {
                                stt:{ editable: false, nullable: true },
                                month:{ editable: false, nullable: true },
                                source:{ editable: true, type: "number",nullable: true },
                                complete:{ editable: true,type: "number", nullable: true },
                                revenue:{ editable: true,type: "number", nullable: true },
                                quantity:{ editable: true,type: "number", nullable: true },
                                action:{ editable: false, nullable: true }
                            }
                        }
                    },sort: { field: "month", dir: "asc" },
                    group: { field: "sysGroupId", aggregates: [
                        { field: "sysGroupName", aggregate: "count" },
                        { field: "source", aggregate: "sum" },
                        { field: "quantity", aggregate: "sum" },
                        { field: "complete", aggregate: "sum" },
                        { field: "revenue", aggregate: "sum" }
                    ]
                    },
                    aggregate: [
                        { field: "source", aggregate: "sum" },
                        { field: "quantity", aggregate: "sum" },
                        { field: "complete", aggregate: "sum" },
                        { field: "revenue", aggregate: "sum" }]
                },
                dataBound: function(e){
                    var firstCell = e.element.find(".k-grouping-row td:first-child");
                    firstCell.attr("colspan", 3);
                }
            });


        }*/
        vm.listRemove=[{
            title: "Thao tác"
        }]
        vm.listConvert=[{
            field:"status",
            data:{
                1:'Hiệu lực',
                0:'Hết Hiệu lực'
            }
        },{
            field:"signState",
            data:{
                1:'Chưa trình ký',
                2:'Đã trình ký',
                3:'Đã ký duyệt',
                4:'Từ chối ký duyệt'

            }
        }
        ]

        vm.exportFile= function(){
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  return Restangular.all("contructionLandHandoverPlan/exportHanPlan").post(vm.constructionLandHandPlanSearch).then(function (d) {
        	                var data = d.plain();
        	                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	                kendo.ui.progress(element, false);
        	            }).catch(function (e) {
        	            	kendo.ui.progress(element, false);
        	                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	                return;
        	            });
        		});
        			
        	  }
        		displayLoading(".tab-content");
            

        }
        vm.importHanPlan = importHanPlan;
         function importHanPlan(){
             var data = vm.constructionLandHandPlan;
             return Restangular.all("contructionLandHandoverPlan/checkImport").post(data).then(function (d) {
                 if(d.error){
                     toastr.error(d.error);
                     return;
                 }
             }).catch(function (e) {
                 var teamplateUrl = "coms/constructionLandHandPlan/imPortHanPlan.html";
                 var title = "Import từ file excel";
                 var windowId = "CONTRUCTION_LAND_HANDOVER_PLAN";
                 CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '700', 'auto', "code");
             });
        }

        vm.getExcelTemplate = function () {
            $('#loadding').show();
            constructionLandHandPlanService.downloadTemplate({}).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                $('#loadding').hide();
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                return;
                $('#loadding').hide();
            });
        }

        vm.onSelectFile = onSelectFile;
        function onSelectFile() {
            if ($("#fileImport")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImport")[0].files[0].name.split('.').pop() != 'xlsx') {
                toastr.warning("Sai định dạng file");
                setTimeout(function () {
                    $(".k-upload-files.k-reset").find("li").remove();
                    $(".k-upload-files").remove();
                    $(".k-upload-status").remove();
                    $(".k-upload.k-header").addClass("k-upload-empty");
                    $(".k-upload-button").removeClass("k-state-focused");
                }, 10);
                return;
            }
            else {
                if (104857600 < $("#fileImport")[0].files[0].size) {
                    toastr.warning("Dung lượng file vượt quá 100MB! ");
                    return;
                }
                $("#fileName")[0].innerHTML = $("#fileImport")[0].files[0].name
            }
        }

        vm.saveImportFile = saveImportFile;
        function saveImportFile(data) {
            var ajax_sendding = false;
            if (ajax_sendding == true){
                alert('Dang Load Ajax');
                return false;
            }
            if ($("#fileImport")[0].files[0] == null) {
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($("#fileImport")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImport")[0].files[0].name.split('.').pop() != 'xlsx') {
                toastr.warning("Sai định dạng file");
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', $('#fileImport')[0].files[0]);
            $('#loadding').show();
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "contructionLandHandoverPlan/importHanPlanDetail",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if(data.error){
                        toastr.warning(data.error);
                        cancelImport();
                        return;
                    }
                    else{
                        if (data.length === 0) {
                            toastr.warning("File imp" +
                            "ort không có dữ liệu");
                            return;
                        } else if (data[data.length - 1].errorFilePath != null) {
                            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                            toastr.warning("File import không hợp lệ");
                            return;
                        }
                        else {
                            // data[0].groundPlanDate = undefined;
                            //constructionLandHandPlanService.insertImport(data[0]).then(function (dto){
                            doSearch();
                            // })
                            /* var sysGroup  Name = '';
                             var detailList = [];
                             var constructionLandHandPlanGrid = vm.constructionLandHandPlanGrid;
                             if (constructionLandHandPlanGrid != undefined && constructionLandHandPlanGrid.dataSource != undefined) {
                             constructionLandHandPlanGrid.dataSource.data(data);
                             constructionLandHandPlanGrid.dataSource.sync();
                             constructionLandHandPlanGrid.refresh();
                             }*/
                            toastr.success("Import file thành công");
                            cancelImport();
                        }
                    }
                }
            }).always(function(){
                ajax_sendding = false;
                $('#loadding').hide();
            });
        }
       /*
		 * vm.saveImportfile = saveImportfile; function saveImportfile(data){
		 * if($("#files")[0].files[0] == null){ toastr.warning("Bạn chưa chọn
		 * file để import"); return; }
		 * if($("#files")[0].files[0].name.split('.').pop() !='xls' &&
		 * $("#files")[0].files[0].name.split('.').pop() !='xlsx' ){
		 * toastr.warning("Sai định dạng file"); return; } var formData = new
		 * FormData(); formData.append('multipartFile',
		 * $('#files')[0].files[0]); return $.ajax({
		 * url:Constant.BASE_SERVICE_URL+"constructionLandHandPlanDetailRsServiceRest/constructionLandHandPlanDetail/importGoods?folder="+
		 * vm.folder, type: "POST", data: formData, enctype:
		 * 'multipart/form-data', processData: false, contentType: false, cache:
		 * false, success:function(data) { var dem=0; if(data[data.length -
		 * 1].lstErrorGoods!=null && data[data.length -
		 * 1].lstErrorGoods.length!=0){ vm.lstErrImport = data[data.length -
		 * 1].lstErrorGoods; vm.objectErr = data[data.length - 1]; var
		 * teamplateUrl="coms/constructionLandHandPlan/constructionLandHandPlanPopUp.html";
		 * var title="Kết quả Import"; var windowId="ERR_IMPORT";
		 * 
		 * CommonService.populatePopupCreate(teamplateUrl,title,vm.lstErrImport,vm,windowId,false,'60%','60%');
		 * fillDataImportErrTable(vm.lstErrImport); return ; } else
		 * if(data.length===1){ toastr.warning("File imp" + "ort không có dữ
		 * liệu"); return; }else{
		 * 
		 * //danglam
		 * $("#constructionLandHandPlanDetail").data("kendoGrid").dataSource.read();
		 * $("#constructionLandHandPlanDetail").data("kendoGrid").refresh();
		 * data.splice(data.length - 1, 1); var grid =
		 * $("#constructionLandHandPlanDetail").data("kendoGrid"); for(var i =
		 * 0; i<data.length;i++){ data[i].id = i+1;
		 * data[i].sysGroupId=data[i].sysGroupId; data[i].month = data[i].month;
		 * data[i].source = data[i].source; data[i].complete = data[i].complete;
		 * data[i].revenue = data[i].revenue;
		 * 
		 *//*
			 * data[i].partNumber = data[i].partNumber; data[i].serial =
			 * data[i].serial; data[i].isSerial = data[i].isSerial;
			 * data[i].unitTypeName = data[i].goodsUnitName; data[i].unitTypeId =
			 * data[i].unitType; data[i].originPrice = data[i].originPrice;
			 * data[i].amount=data[i].amount;
			 *//*
				 * if(data[i].partNumber.length>100){ toastr.warning("PartNumber
				 * không được vượt quá maxlength!"); return; }
				 * if(data[i].serial.length>100){ toastr.warning("Serial không
				 * được vượt quá maxlength!"); return; }
				 * if(data[i].amount.length>10){ toastr.warning("Số lượng không
				 * vượt quá maxlength!"); return; } else{
				 * grid.dataSource.add(data[i]); } } toastr.success("Import
				 * thành công!"); } console.log(data); } }); }
				 */

        function refreshGrid(d) {
            var grid = vm.constructionLandHandPlanGrid;
            if(grid){
                grid.dataSource.data(d);
                grid.refresh();
            }
        }
        function validatekendodatepicker(){
            $('#groundPlanDate2').click(function () {
                var todayDate = kendo.toString(kendo.parseDate(new Date()), 'dd/MM/yyyy');
                $("#date1").data("kendoDatePicker").value(todayDate);
            });
            /*$('#createdDate').click(function () {
                var todayDate = kendo.toString(kendo.parseDate(new Date()), 'dd/MM/yyyy');
                $("#date2").data("kendoDatePicker").value(todayDate);
            });
            $('#endDate').click(function () {
                var todayDate = kendo.toString(kendo.parseDate(new Date()), 'dd/MM/yyyy');
                $("#date3").data("kendoDatePicker").value(todayDate);
            });*/
        }
        vm.add=add;
        function add(){
            var data = vm.constructionLandHandPlan;
            return Restangular.all("contructionLandHandoverPlan/checkPermissionsAdd").post(data).then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                validatekendodatepicker();
                var teamplateUrl = "coms/constructionLandHandPlan/constructionLandHandPlanPopup.html";
                var title = "Thêm mới kế hoạch BGMB";
                vm.constructionLandHandPlan = {};
                //        vm.constructionLandHandPlanDetailTemp = {};
                var windowId = "CONSTRUCTION_LAND_HANDOVER_PLAN";
                CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', 'auto', "code");
            });
        }

        vm.edit=edit;
        function edit(dataItem){
            vm.String="Quản lý công trình > Quản lý kế hoạch BGMB > Chỉnh sửa kế hoạch BGMB";
            var teamplateUrl="coms/constructionLandHandPlan/constructionLandHandPlanPopup.html";
            var title="Chỉnh sửa kế hoạch BGMB";
            var windowId="CONSTRUCTION_LAND_HANDOVER_PLAN";
            CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','auto',"code");
            vm.constructionLandHandPlan =dataItem;
            constructionLandHandPlanService.getconstructionLandHandPlanById(dataItem.contructionLandHanPlanId).then(function(data){
                validatekendodatepicker();
                constructionLandHandPlanService.getLstConstruction(data.catStationCode).then(function(data1) {
                    vm.workCode=data1;
                    vm.constructionLandHandPlan =data;
                });
            },function(error){
                toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
            });
        }
        function refreshconstructionLandHandPlanDetailList(data){
            var grid = vm.constructionLandHandPlanDetailGrid;
            if(grid){
                grid.dataSource.data(data);
                grid.refresh();
            }
        }
        vm.removeconstructionLandHandPlanDetail = removeconstructionLandHandPlanDetail;
        function removeconstructionLandHandPlanDetail(e){
            var grid = vm.constructionLandHandPlanDetailGrid;
            var row = $(e.target).closest("tr");
            var dataItem = grid.dataItem(row);
            grid.removeRow(dataItem); // just gives alert message
            grid.dataSource.remove(dataItem); // removes it actually from the
												// grid
            grid.dataSource.sync();
            grid.refresh();
        }
        vm.removeconstructionLandHandPlanPerSysGroup = removeconstructionLandHandPlanPerSysGroup;
        function removeconstructionLandHandPlanPerSysGroup(sysGroupId){
            var  constructionLandHandPlanDetailGrid = vm.constructionLandHandPlanDetailGrid;
            var listRemove=[];
            if(constructionLandHandPlanDetailGrid!=undefined && constructionLandHandPlanDetailGrid.dataSource!=undefined) {
                var detailList = constructionLandHandPlanDetailGrid.dataSource._data;
                angular.forEach(detailList,function(dataItem){
                    if(sysGroupId==dataItem.sysGroupId){
                        listRemove.push(dataItem);
                    }
                })
            }
            angular.forEach(listRemove,function(dataItem){
                constructionLandHandPlanDetailGrid.removeRow(dataItem); // just
																		// gives
																		// alert
																		// message
                constructionLandHandPlanDetailGrid.dataSource.remove(dataItem); // removes
																				// it
																				// actually
																				// from
																				// the
																				// grid
                constructionLandHandPlanDetailGrid.dataSource.sync();
                constructionLandHandPlanDetailGrid.refresh();
            })
            constructionLandHandPlanDetailGrid.dataSource.page(1);
        }
        vm.addconstructionLandHandPlan = addconstructionLandHandPlan;
        function addconstructionLandHandPlan(){
            if(validateTemp()){
                var grid = vm.constructionLandHandPlanDetailGrid;
                if(grid){
                    var detailList = grid.dataSource._data;
                    var valid = true;
                    angular.forEach(detailList,function(dataItem){
                        if(vm.constructionLandHandPlan.sysGroupId==dataItem.sysGroupId&&vm.constructionLandHandPlan.month==dataItem.month){
                            valid = false;
                        }
                    })
                    if(valid) {
                        grid.dataSource.add(vm.constructionLandHandPlan);
                        grid.dataSource.sync();
                        grid.refresh();
                    }else {
                        toastr.warning("Dữ liệu tháng " + vm.constructionLandHandPlan.month + " của đơn vị "+vm.constructionLandHandPlan+" đã tồn tại!");
                    }
                }
            }

        }
        function validateLanHanPlan(){
            if(vm.constructionLandHandPlan.month== undefined || vm.constructionLandHandPlan.month == null||vm.constructionLandHandPlan.month ==""){
                toastr.warning("Bạn phải chọn tháng!");
                return false;
            }
            if(vm.constructionLandHandPlan.year== undefined || vm.constructionLandHandPlan.year == null||vm.constructionLandHandPlan.year ==""){
                toastr.warning("Bạn phải chọn năm");
                return false;
            }
            if(vm.constructionLandHandPlan.catStationCode== undefined || vm.constructionLandHandPlan.catStationCode == null||vm.constructionLandHandPlan.catStationCode ==""){
                toastr.warning("Bạn phải nhập mã trạm");
                return false;
            }
            if(vm.constructionLandHandPlan.sysGroupName== undefined || vm.constructionLandHandPlan.sysGroupName == null||vm.constructionLandHandPlan.sysGroupName ==""){
                toastr.warning("Bạn chọn đơn vị nhận bàn giao");
                return false;
            }
            if(vm.constructionLandHandPlan.constructionId== undefined || vm.constructionLandHandPlan.constructionId == null||vm.constructionLandHandPlan.constructionId ==""){
                toastr.warning("Bạn phải chọn mã công trình");
                return false;
            }
            if(vm.constructionLandHandPlan.groundPlanDate== undefined || vm.constructionLandHandPlan.groundPlanDate == null||vm.constructionLandHandPlan.groundPlanDate ==""){
                toastr.warning("Bạn phải chọn ngày bàn giao dự kiến");
                return false;
            }
            if(vm.constructionLandHandPlan.catPartnerCode== undefined || vm.constructionLandHandPlan.catPartnerCode == null||vm.constructionLandHandPlan.catPartnerCode ==""){
                toastr.warning("Bạn phải chọn đơn vị bàn giao");
                return false;
            }
            return true;
        }

        vm.save= function() {
            if (validateLanHanPlan()) {
                var data = populateDataToSave();
                if (data.constructionLandHandPlanId == null || data.constructionLandHandPlanId == '') {
                    constructionLandHandPlanService.createconstructionLandHandPlan(data).then(function (result) {
                        if (result.error) {
                            $('#year').focus();
                            toastr.error(result.error);
                            return;
                        }
                        toastr.success("Thêm mới thành công!");
                        vm.cancel();
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi thêm mới"));
                    });
                }
                else {
                    constructionLandHandPlanService.updateconstructionLandHandPlan(data).then(function (result) {
                        if (result.error) {
                            $('#year').focus();
                            toastr.error(result.error);
                            return;
                        }
                        toastr.success("Chỉnh sửa thành công!");
                        vm.cancel();
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi chỉnh sửa"));
                    });
                }
            }
        }
// validate mỗi đơn vị phải đủ 12 tháng
        function validateYearDetail(){
            var isValid= true;
            var sysGroupName = '';
            var detailList =[];
            var  constructionLandHandPlanGrid = vm.constructionLandHandPlanGrid;
            if(constructionLandHandPlanGrid!=undefined && constructionLandHandPlanGrid.dataSource!=undefined) {
                detailList = constructionLandHandPlanGrid.dataSource._data;
                var result=_.chain(detailList).groupBy("sysGroupId").map(function(v, i) {
                    return {
                        sysGroupId: i,
                        sysGroupName: (_.find(v, 'sysGroupName')).sysGroupName,
                        monthList: _.map(v, 'month')
                    }
                }).value();
                if(result!=undefined && result.length>0){
                    angular.forEach(result,function(item){
                        if(item.monthList==null||item.monthList.length!=12){
                            isValid = false;
                            sysGroupName = item.sysGroupName;
                        }
                    })
                }
            }
            if(!isValid){
                toastr.warning("Kế hoạch năm của đơn vị"+sysGroupName+" không hợp lệ!");
            }
            return isValid;
        }
        function populateDataToSave(){
            var data = vm.constructionLandHandPlan;
            var detailList=[]
            var  constructionLandHandPlanGrid = vm.constructionLandHandPlanGrid;
            if(constructionLandHandPlanGrid!=undefined && constructionLandHandPlanGrid.dataSource!=undefined)
                data.detailList=constructionLandHandPlanGrid.dataSource._data;
            return data;
        }
        vm.cancel= cancel ;
        function cancel(){
            vm.showDetail = false;
            vm.constructionLandHandPlan={}
            vm.constructionLandHandPlan={};
            vm.constructionLandHandPlanDetailGrid.dataSource.data([]);
            vm.constructionLandHandPlanDetailGrid.refresh();
            vm.doSearch();
        }
        vm.remove=remove;
        function remove(dataItem){
            confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function(){
                constructionLandHandPlanService.remove(dataItem).then(
                    function(d) {
                        toastr.success("Xóa bản ghi thành công!");
                        var sizePage = $("#constructionLandHandPlanGrid").data("kendoGrid").dataSource.total();
                        var pageSize = $("#constructionLandHandPlanGrid").data("kendoGrid").dataSource.pageSize();
                        if(sizePage % pageSize === 1){
                            var currentPage = $("#constructionLandHandPlanGrid").data("kendoGrid").dataSource.page();
                            if (currentPage > 1) {
                                $("#constructionLandHandPlanGrid").data("kendoGrid").dataSource.page(currentPage - 1);
                            }
                        }
                        $("#constructionLandHandPlanGrid").data('kendoGrid').dataSource.read();
                        $("#constructionLandHandPlanGrid").data('kendoGrid').refresh();

                    }, function(errResponse) {
                        toastr.error("Lỗi không xóa được!");
                    });
            } )
        }



        vm.cancelDoSearch= function (){
            vm.showDetail = false;
            vm.constructionLandHandPlanSearch={
            };
            doSearch();
        }

        vm.doSearch= doSearch;
        function doSearch(){
            vm.showDetail = false;
            var grid =vm.constructionLandHandPlanGrid;
            if(grid){
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }

        vm.showHideColumn=function(column){
            if (angular.isUndefined(column.hidden)) {
                vm.constructionLandHandPlanGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.constructionLandHandPlanGrid.showColumn(column);
            } else {
                vm.constructionLandHandPlanGrid.hideColumn(column);
            }


        }
        /*
		 * * Filter các cột của select
		 */

        vm.gridColumnShowHideFilter = function (item) {
            return item.type==null||item.type !==1;
        };


        vm.exportpdf= function(){
            var obj={};
            constructionLandHandPlanService.exportpdf(obj);
        }


        /*
		 * var specialChars = "<>@!#$%^&*()+[]{}?:;|'\"\\,./~`-="; var check =
		 * function(string){ for(var i = 0; i < specialChars.length;i++){
		 * if(string.indexOf(specialChars[i]) > -1){ return true; } }
		 * vm.listApp.mess=""; return false; } var check1 = function(string){
		 * for(var i = 0; i < specialChars.length;i++){
		 * if(string.indexOf(specialChars[i]) > -1){ return true; } }
		 * vm.listApp.mess1=""; return false; } var check2 = function(string){
		 * for(var i = 0; i < specialChars.length;i++){
		 * if(string.indexOf(specialChars[i]) > -1){ return true; } }
		 * vm.listApp.mess2=""; return false; }
		 * 
		 * vm.checkErr = checkErr; function checkErr() { var parType =
		 * $('#parType').val(); var code = $('#code').val(); var
		 * name=$('#name').val(); if(check(parType) === true){ vm.listApp.mess =
		 * "Loại tham số chứa ký tự đặc biệt"; } if(check1(code) === true){
		 * vm.listApp.mess1 = "Mã tham số chứa ký tự đặc biệt"; }
		 * if(check2(name) === true){ vm.listApp.mess2 = "Tên Tham số ký tự đặc
		 * biệt"; } return vm.listApp; }
		 */
        vm.errNumber="";
        vm.checkNumber=checkNumber;
        function checkNumber(){
            var val=$('#parOder').val();
            if(val===0){
                if(val===0){
                    if(val===""){
                        vm.errNumber="";
                    }else{
                        vm.errNumber="Phải nhập kiểu số nguyên từ 1-99";
                        return false;
                    }

                }
            }else{
                var isNaN = function(val) {
                    if(Number.isNaN(Number(val))){
                        return false;
                    }
                    return true;
                }
                if(isNaN(val)===false){
                    vm.errNumber="Phải nhập kiểu số nguyên từ 1-99";
                }else{
                    vm.errNumber="";
                }

            }


        }
        vm.deliveryUnit = deliveryUnit
        function deliveryUnit(popUp){
            vm.obj = {};
            vm.departmentpopUp = popUp;
            var templateUrl = 'coms/constructionLandHandPlan/deliveryUnitPopup.html';
            var title = gettextCatalog.getString("Danh sách đơn vị đối tác");
            CommonService.deliveryUnitPopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '80%', '90%');
        }

        vm.openDepartmentTo=openDepartmentTo
        function openDepartmentTo(popUp){
            vm.obj={};
            vm.departmentpopUp=popUp;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
        }
        vm.onSave=onSave;
    // vm.constructionLandHandPlanDetailTemp={}
        function onSave(data){
            if(vm.departmentpopUp === 'dept'){
                vm.constructionLandHandPlanSearch.sysGroupName = data.text;
                vm.constructionLandHandPlanSearch.sysGroupId = data.id;
            }
            else if (vm.departmentpopUp === 'dept1'){
                vm.constructionLandHandPlan.name= data.name;
                vm.constructionLandHandPlan.sysGroupName = data.text;
                vm.constructionLandHandPlan.sysGroupId = data.id;
            }
            else if (vm.departmentpopUp === 'dept2'){
                vm.constructionLandHandPlan.catPartnerCode= data.code;
                vm.constructionLandHandPlan.catPartnerId= data.catPartnerId;
            }
        }
        function resetForm(){
            blurInput('groundPlanDate');
        }
        function blurInput(id){
            if(document.getElementById(id)!=null) {
                document.getElementById(id).focus();
                document.getElementById(id).blur();
            }
        }
// clear data
        vm.changeDataAuto=changeDataAuto
        function changeDataAuto(id){
            switch(id){
                case 'dept':{
                    if(processSearch(id,vm.selectedDept1)){
                        vm.constructionLandHandPlanSearch.sysGroupId= null;
                        vm.constructionLandHandPlanSearch.sysGroupName= null;
                        vm.selectedDept1=false;
                    }
                    break;
                }
                case 'dept1':
                {
                    if (processSearch(id, vm.selectedDept1)) {
                        vm.constructionLandHandPlan.sysGroupName = null;
                        vm.constructionLandHandPlan.sysGroupName = null;
                        vm.selectedDept1 = false;
                    }
                    break;
                }
                case 'dept2':
                {
                    if (processSearch(id, vm.selectedStation)) {
                        vm.constructionLandHandPlan.catStationCode = null;
                        vm.selectedStation = false;
                    }
                    break;
                }
            }}
        vm.cancelInput = function(param){
            if(param == 'dept'){
                vm.constructionLandHandPlanSearch.sysGroupId= null;
                vm.constructionLandHandPlanSearch.sysGroupName= null;
            }
            else if (param == 'dept1') {
                vm.constructionLandHandPlan.sysGroupId = null;
                vm.constructionLandHandPlan.sysGroupName = null;
            }
        }
        vm.clearConstructionIdwhenCatStationCodechange=clearConstructionIdwhenCatStationCodechange;
        function clearConstructionIdwhenCatStationCodechange(){
        	 vm.constructionLandHandPlan.contructionCode = '';
        	 vm.constructionLandHandPlan.constructionId = [];
        	 vm.workCode=[];
        };
        vm.cancelStationInput = cancelStationInput;
        function cancelStationInput(type,form) {
            if(type=='constructionId'){
                vm.constructionLandHandPlan.constructionId = ''
            }
            if(type=='name'){
                vm.constructionLandHandPlan.name = ''
            }
            if(type=='groundPlanDate'){
                vm.constructionLandHandPlan.groundPlanDate =  '';
                $rootScope.validateDate(vm.constructionLandHandPlan.groundPlanDate,null,null,form)
            }
            if(type=='catStationCode'){
                vm.constructionLandHandPlan.catStationCode = '';
                vm.constructionLandHandPlan.constructionId = [];
            }
            if(type=='catPartnerId'){
                vm.constructionLandHandPlan.catPartnerCode = ''
            }
            if(type=='constructionId'){
                vm.constructionLandHandPlan.constructionId = []
            }
        }
        /*function validateDate(dateValue)
        {
            var selectedDate = dateValue;
            if(selectedDate == '')
                return false;

            var regExp = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/; //Declare Regex
            var dateArray = selectedDate.match(regExp); // is format OK?

            if (dateArray == null){
                return false;
            }
            var day = dateArray[1];
            var month= dateArray[3];
            var year = dateArray[5];

            if (month < 1 || month > 12){
                return false;
            }else if (day < 1 || day> 31){
                return false;
            }else if ((month==4 || month==6 || month==9 || month==11) && day ==31){
                return false;
            }else if (month == 2){
                var isLeapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
                if (day> 29 || (day ==29 && !isLeapYear)){
                    return false
                }
            }
            return true;
        }*/
// 8.2 Search SysGroup
        vm.selectedDept1=false;
        vm.deprtOptions1 = {
            dataTextField: "text",
            dataValueField:"id",
            select: function(e) {
                vm.selectedDept1=true;
                var dataItem = this.dataItem(e.item.index());
                vm.constructionLandHandPlan.sysGroupName = dataItem.text;;
                vm.constructionLandHandPlan.sysGroupId = dataItem.id;
            },
            pageSize: 10,
            open: function(e) {
                vm.selectedDept1 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        vm.selectedDept1=false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({name:vm.constructionLandHandPlan.sysGroupName,pageSize:vm.deprtOptions1.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
            change: function(e) {
                if (e.sender.value() === '') {
                    vm.constructionLandHandPlan.sysGroupName = null;// thành
																	// name
                    vm.constructionLandHandPlan.sysGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.deprtOptions2 = {
            dataTextField: "text",
            dataValueField:"id",
        	placeholder:"Nhập mã hoặc tên đơn vị",
            select: function(e) {
                vm.selectedDept1=true;
                var dataItem = this.dataItem(e.item.index());
                vm.constructionLandHandPlanSearch.sysGroupName = dataItem.text;;
                vm.constructionLandHandPlanSearch.sysGroupId = dataItem.id;
            },
            pageSize: 10,
            open: function(e) {
                vm.selectedDept1 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        vm.selectedDept1=false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({name:vm.constructionLandHandPlanSearch.sysGroupName,pageSize:vm.deprtOptions2.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
            change: function(e) {
                if (e.sender.value() === '') {
                    vm.constructionLandHandPlanSearch.sysGroupName = null;// thành name
                    vm.constructionLandHandPlanSearch.sysGroupId = null;
                }
            },
            ignoreCase: false
        }

       /* vm.selectedStation=false;
        vm.catStationOptions = {
            dataTextField: "code",
            dataValueField:"id",
            select: function(e) {
                vm.selectedStation=true;
                var dataItem = this.dataItem(e.item.index());
                vm.constructionLandHandPlan.catStationCode = dataItem.code;;
                vm.constructionLandHandPlan.catStationId = dataItem.id;
            },
            pageSize: 10,
            open: function(e) {
                vm.selectedStation = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        vm.selectedDept1=false;
                        return Restangular.all("contructionLandHandoverPlan/contructionLandHandoverPlan/" + 'getCatStation').post({name:vm.constructionLandHandPlan.sysGroupName,pageSize:vm.deprtOptions1.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
            change: function(e) {
                if (e.sender.value() === '') {
                    vm.constructionLandHandPlan.sysGroupName = null;// thành name
                    vm.constructionLandHandPlan.sysGroupId = null;
                }
            },
            ignoreCase: false
        }*/
        vm.selectedStation=false;
       vm.workCode=[];
        vm.catStationOptions = {
            dataTextField: "code",
            dataValueField: "id",
            select: function (e) {
                vm.selectedStation = true;
                var dataItem = this.dataItem(e.item.index());
                vm.constructionLandHandPlan.catStationCode = dataItem.code;
                vm.constructionLandHandPlan.catStationId = dataItem.id;
                vm.constructionLandHandPlan.name = dataItem.address;
              // alert(vm.constructionLandHandPlan.catStationCode);
                vm.constructionLandHandPlan.constructionId = '';
                constructionLandHandPlanService.getLstConstruction(vm.constructionLandHandPlan.catStationCode).then(function(data) {
                    vm.workCode=data;
                    console.log(vm.workCode);
                }).catch( function(){
                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                });
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedStation = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedStation = false;
                        return Restangular.all("contructionLandHandoverPlan/contructionLandHandoverPlan/" + 'getCatStation').post({
                            code: vm.constructionLandHandPlan.catStationCode,
                            pageSize: vm.catStationOptions.pageSize,
                            type: 1
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div></div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.constructionLandHandPlan.catStationCode = null;// thành
																		// name
                    vm.constructionLandHandPlan.catStationId = null;
                }
            },
            ignoreCase: false
        }

        vm.cancelPopup=cancelPopup;
        function cancelPopup(){
            $(".k-icon.k-i-close").click();
        }
        vm.cancelImport = cancelImport;
        function cancelImport() {
            $(".k-icon.k-i-close").click();
        //    cancelPopup();
        };

        vm.saveHanPlan = saveHanPlan;
        function saveHanPlan(data3) {
            vm.constructionLandHandPlan = data3;
            if (validateLanHanPlan()) {
            if (vm.constructionLandHandPlan.contructionLandHanPlanId == null || vm.constructionLandHandPlan.contructionLandHanPlanId == ''){
                constructionLandHandPlanService.createconstructionLandHandPlan(vm.constructionLandHandPlan).then(function (data) {
                    // close popup and refresh table list
                    if(data.error){
                        toastr.error(data.error);
                        return;
                    }
                    toastr.success("Thêm mới dữ liệu thành công.");
                    cancelPopup();
                    CommonService.dismissPopup1();
                    doSearch();
                    $("#constructionLandHandPlanGrid").data('kendoGrid').dataSource.read();
                    $("#constructionLandHandPlanGrid").data('kendoGrid').refresh();
                }, function (e) {
                    toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
                });
            } else{
                constructionLandHandPlanService.updateconstructionLandHandPlan(vm.constructionLandHandPlan).then(function(data){
                    if(data.error){
                        toastr.error(data.error);
                        return;
                    }
                    toastr.success("Sửa dữ liệu thành công.");
                    cancelPopup();
                    CommonService.dismissPopup1();
                    doSearch();
                    $("#constructionLandHandPlanGrid").data('kendoGrid').dataSource.read();
                    $("#constructionLandHandPlanGrid").data('kendoGrid').refresh();

                },function(error){
                    toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
                });
            }
            }
    }

        vm.cancelHandover = cancelHandover;
        function cancelHandover(type) {
            if(type=='month'){
                vm.constructionLandHandPlanSearch.month = undefined;
            }
            if(type=='year'){
                vm.constructionLandHandPlanSearch.year = undefined;
            }
            if(type=='month'){
                vm.constructionLandHandPlan.month = []
            }
            if(type=='year'){
                vm.constructionLandHandPlan.year = []
            }

        }
        vm.onSelect = function(e) {
            if($("#files")[0].files[0].name.split('.').pop() !='xls' && $("#files")[0].files[0].name.split('.').pop() !='xlsx'){
                toastr.warning("Sai định dạng file");
                setTimeout(function() {
                    $(".k-upload-files.k-reset").find("li").remove();
                    $(".k-upload-files").remove();
                    $(".k-upload-status").remove();
                    $(".k-upload.k-header").addClass("k-upload-empty");
                    $(".k-upload-button").removeClass("k-state-focused");
                },10);
                return;
            }
            else{
                $("#fileName").innerHTML=$("#files")[0].files[0].name
            }
        }
        vm.searchItem={}
        vm.deliveryUnitOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        $("#appCount1").text("" + response.total);
                        vm.count = response.total;
                        return response.total; // total is returned in
                        // the "total" field of
                        // the response
                    },
                    data: function (response) {
                        var list = response.data;
                        return response.data; // data is returned in
                        // the "data" field of
                        // the response
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "contructionLandHandoverPlan/doSearchPartner",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        // vm.yearPlanSearch.employeeId =
                        // Constant.user.srvUser.catEmployeeId;
                        vm.searchItem.page = options.page
                        vm.searchItem.pageSize = options.pageSize

                        return JSON.stringify(vm.searchItem)

                    }
                },
                pageSize: 10
            },
            noRecords: true,
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSizes: [10, 15, 20, 25],
                messages: {
                    display: "{0}-{1} của {2} kết quả",
                    itemsPerPage: "kết quả/trang",
                    empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                }
            },
            columns:[{
                title: "TT",
                field: "stt",
                template: function () {
                    return ++record;
                },
                width: '5%',
                columnMenu: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:center;"
                }
            },
                {
                    title: "Loại đối tác",
                    field: "partnerTypeName",
                    width: "18%",
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                }, {
                    title: "Mã đối tác",
                    field: "code",
                    width: "30%",
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                }, {
                    title: "Tên dối tác",
                    field: "name",
                    width: "30%",
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                },{
                    title: "Trạng thái",
                    field: "statusName",
                    width: "20%",
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                },{
                    title: "Chọn",
                    template:
                    '<div class="text-center "> '	+
                    '		<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate>'+
                    '			<i id="#=code#" ng-click=save(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> '+
                    '		</a>'
                    +'</div>',
                    width: "15%",
                    field:"stt"
                }]
        });

    	vm.openCatProvincePopup = openCatProvincePopup;
    	vm.onSaveCatProvince = onSaveCatProvince;
    	vm.clearProvince = clearProvince;
        function openCatProvincePopup(){
    		var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }
        function onSaveCatProvince(data){
            vm.constructionLandHandPlanSearch.catProvinceId = data.catProvinceId;
            vm.constructionLandHandPlanSearch.catProvinceCode = data.code;
    		vm.constructionLandHandPlanSearch.catProvinceName = data.name;
            htmlCommonService.dismissPopup();
            $("#provincename").focus();
        };
    	function clearProvince (){
    		vm.constructionLandHandPlanSearch.catProvinceId = null;
    		vm.constructionLandHandPlanSearch.catProvinceCode = null;
    		vm.constructionLandHandPlanSearch.catProvinceName = null;
    		$("#provincename").focus();
    	}
        vm.provinceOptions = {
            dataTextField: "name",
            dataValueField: "id",
    		placeholder:"Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.constructionLandHandPlanSearch.catProvinceId = dataItem.catProvinceId;
                vm.constructionLandHandPlanSearch.catProvinceCode = dataItem.code;
    			vm.constructionLandHandPlanSearch.catProvinceName = dataItem.name;
            },
            pageSize: 10,
            open: function (e) {
                vm.isSelect = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelect = false;
                        return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
                            name: vm.constructionLandHandPlanSearch.catProvinceName,
                            pageSize: vm.provinceOptions.pageSize,
                            page: 1
                        }).then(function (response) {
                            options.success(response.data);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
    		headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
    		'<p class="col-md-6 text-header-auto border-right-ccc">Mã tỉnh</p>' +
    		'<p class="col-md-6 text-header-auto">Tên tỉnh</p>' +
    		'</div>',
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
            	if (!vm.isSelect) {
                    vm.constructionLandHandPlanSearch.catProvinceId = null;
                    vm.constructionLandHandPlanSearch.catProvinceCode = null;
    				vm.constructionLandHandPlanSearch.catProvinceName = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.constructionLandHandPlanSearch.catProvinceId = null;
                    vm.constructionLandHandPlanSearch.catProvinceCode = null;
    				vm.constructionLandHandPlanSearch.catProvinceName = null;
                }
            }
        }

        
    }
})();

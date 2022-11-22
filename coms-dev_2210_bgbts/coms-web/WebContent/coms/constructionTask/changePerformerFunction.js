function changePerformerFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
    kendoConfig, $kWindow, constructionService,
    CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm){
	
	vm.updateList = [];
	vm.updateConstructionTask = function(){
		if(validateChangingGrid()){
			var obj = {};
			obj.childDTOList = vm.updateList;
			constructionService.updatePerformer(obj).then(function(response){
				 if (response.error) {
                    toastr.error(response.error);
                    return;
                }
				vm.clearInput('7');
				vm.clearInput('8');
				refreshGrid();
                toastr.success("Cập nhật dữ liệu thành công")
			});
		}
	}
	
	//tìm người thực hiện
	vm.changePerformer = {};
	vm.changePerformer.priority=1
	   vm.performerOption = {
	            dataTextField: "fullName",
	            dataValueField: "sysUserId",
	            select: function (e) {
	                vm.selectedDept1 = true;
	                var data = this.dataItem(e.item.index());
	                vm.changePerformer.perFormerName = data.fullName;
	                vm.changePerformer.performerId = data.sysUserId;
	                refreshGrid();
	            },
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function (options) {
	                    	var constructionId = vm.changePerformer.priority==2 ? null : vm.changePerformer.constructionId;
	                        if(vm.changePerformer.priority==1 && !vm.changePerformer.constructionCode){
	                        	constructionId= -1;
	                        	toastr.error("Bạn phải chọn công trình trước");
	                        }
	                        return Restangular.all("constructionTaskService/getPerformerAutocomplete").post({
	                            keySearch: vm.changePerformer.performerName,
	                            pageSize: vm.performerOption.pageSize*5,
	                            constructionId: constructionId,
	                            month : $scope.monthGrantt,
	                            year : $scope.yearGrantt,
	                            page: 1
	                        }).then(function (response) {
	                            options.success(response);
	                        }).catch(function (err) {
	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                        });
	                    }
	                }
	            },
	            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.loginName #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
	            change: function (e) {
	                if (e.sender.value() === '') {
	                    vm.obj.performerName = null;
	                    vm.obj.performerIdDetail = null;
	                }
	            },
	            ignoreCase: false
	        }
	   
	   
	   vm.altPerformerOption = {
	            dataTextField: "fullName",
	            dataValueField: "sysUserId",
	            select: function (e) {
	                vm.selectedDept1 = true;
	                var data = this.dataItem(e.item.index());
	                vm.changePerformer.altPerformerName = data.fullName;
	                vm.changePerformer.altPerformerId = data.sysUserId;
	            },
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function (options) {
	                        if(!vm.changePerformer.constructionCode){
	                        	toastr.error("Bạn phải chọn công trình trước");
	                        }
	                        return Restangular.all("constructionTaskService/construction/searchPerformer").post({
	                            keySearch: vm.changePerformer.altPerformerName,
	                            pageSize: vm.altPerformerOption.pageSize,
	                            sysGroupId: vm.performerSearch.sysGroupId,
	                            page: 1
	                        }).then(function (response) {
	                            options.success(response);
	                        }).catch(function (err) {
	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                        });
	                    }
	                }
	            },
	            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.loginName #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
	            change: function (e) {
	                if (e.sender.value() === '') {
	                    vm.obj.altPerformerName = null;
	                    vm.obj.altPerformerName = null;
	                }
	            },
	            ignoreCase: false
	        }
	   
	   //autocomplete tìn kiếm công trình
	   vm.changePer= {};
	   vm.changePer.constructionOptions = {
	            dataTextField: "code",
	            dataValueField: "constructionId",
	            select: function (e) {
	                vm.selectedDept1 = true;
	                var data = this.dataItem(e.item.index());
	                vm.changePerformer.constructionCode = data.code;
	                vm.changePerformer.constructionId = data.constructionId;
	                if(vm.changePerformer.priority==1)
	                	getFirstFound();
	                refreshGrid();
	            },
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function (options) {
	                    	var performerId = vm.changePerformer.priority==1 ? null : vm.changePerformer.performerId;
	                    	 if(vm.changePerformer.priority==2 && !vm.changePerformer.performerId){
	                    		 performerId = -1;
		                        	toastr.error("Bạn phải chọn người thực hiện trước");
	                        }
//	                    	var performerid = vm.changePerformer.priority==2 ? vm.changePerformer.performerId : null;
	                        return Restangular.all("constructionTaskService/getForChangePerformerAutocomplete").post({
	                            keySearch: vm.changePerformer.constructionCode,
//	                            taskType: 1,
	                            pageSize: vm.constructionOptions.pageSize,
	                            month : $scope.monthGrantt,
						    	year : $scope.yearGrantt,
	                            page: 1,
	                            performerId:performerId
	                        }).then(function (response) {
	                            options.success(response);
	                        }).catch(function (err) {
	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                        });
	                    }
	                }
	            },
	            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.name #</div> </div>',
	            change: function (e) {
	                if (e.sender.value() === '') {
	                	vm.changePerformer.constructionCode = null;
	 	                vm.changePerformer.constructionId = null;
	                }
	            },
	            ignoreCase: false
	        }
	   
	   //Grid tìm kiếm công trình
	   vm.findConstructionPopupOptions = kendoConfig.getGridOptions({
           autoBind: true,
           scrollable: false,
           resizable: true,
           editable: false,
           dataBinding: function () {
               record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
           },
           reorderable: true,
           dataSource: {
               serverPaging: true,
               schema: {
                   total: function (response) {
                       vm.countCons = response.total;
                       return response.total; // total is returned in the "total" field of the response
                   },
                   data: function (response) {
                       return response.data; // data is returned in the "data" field of the response
                   }
               },
               transport: {
                   read: {
                       url: Constant.BASE_SERVICE_URL + "constructionTaskService/getConstrForChangePerformer",
                       contentType: "application/json; charset=utf-8",
                       type: "POST"
                   },
                   parameterMap: function (options, type) {
                	   var performerId =vm.changePerformer.performerId;
                	   if(vm.changePerformer.priority==1){
                		   performerId = null;
                	   }
                       vm.constructionSearch.page = options.page
                       vm.constructionSearch.pageSize = options.pageSize
                       vm.constructionSearch.keySearch = vm.keySearch;
                       vm.constructionSearch.performerId = performerId;
                       vm.constructionSearch.month = $scope.monthGrantt;
                       vm.constructionSearch.year = $scope.yearGrantt;
                       vm.constructionSearch.taskType = vm.obj.type;
                       return JSON.stringify(vm.constructionSearch)
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
           columns: [
               {
                   title: "TT",
                   field: "stt",
                   template: function () {
                       return ++record;
                   },
                   width: '5%',
                   columnMenu: false,
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   attributes: {
                       style: "text-align:center;"
                   }
               },{
                   title: "Loại công trình",
                   field: 'catContructionTypeName',
                   width: '10%',
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   attributes: {
                       style: "text-align:left;"
                   }
               },{
                   title: "Mã công trình",
                   field: 'code',
                   width: '20%',
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   attributes: {
                       style: "text-align:left;"
                   }
               },{
                   title: "Tên công trình",
                   field: 'name',
                   width: '35%',
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   attributes: {
                       style: "text-align:left;"
                   }
               },{
                   title: "Trạng thái",
                   field: 'status',
                   width: '10%',
                   template: function (dataItem) {
                       if (dataItem.status == 1) {
                           return "<span name='status' font-weight: bold;'>Chờ bàn giao mặt bằng</span>"
                       } else if (dataItem.status == 2) {
                           return "<span name='status' font-weight: bold;'>Chờ khởi công</span>"
                       } else if (dataItem.status == 3) {
                           return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                       } else if (dataItem.status == 4) {
                           return "<span name='status' font-weight: bold;'>Đã tạm dừng</span>"
                       } else if (dataItem.status == 5) {
                           return "<span name='status' font-weight: bold;'>Đã hoàn thành</span>"
                       } else if (dataItem.status == 6) {
                           return "<span name='status' font-weight: bold;'>Đã nghiệm thu</span>"
                       } else if (dataItem.status == 7) {
                           return "<span name='status' font-weight: bold;'>Đã hoàn công</span>"
                       } else if (dataItem.status == 8) {
                           return "<span name='status' font-weight: bold;'>Đã quyết toán</span>"
                       } else if (dataItem.status == 0) {
                           return "<span name='status' font-weight: bold;'>Đã hủy</span>"
                       }
                   },
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   attributes: {
                       style: "text-align:left;"
                   }
               },{
                   title: "Chọn",
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   template: dataItem =>
                       '<div class="text-center">' +
                       '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                       '<i id="#=code#"  ng-click="caller.chooseConstructionForChanging(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                       '</a>'
                       + '</div>',
                   width: '10%'
               },
           ]
       });
	   
	   //popup tim kiem nguoi thuc hien
	   vm.performerFindOptions = kendoConfig.getGridOptions({
           autoBind: true,
           scrollable: false,
           resizable: true,
           editable: false,
           dataSource: {
               serverPaging: true,
               schema: {
                   total: function (response) {
                       return response.total;
                   },
                   data: function (response) {
                       return response.data;
                   }
               },
               transport: {
                   read: {
                       url: Constant.BASE_SERVICE_URL + "constructionTaskService/getPerformerForChanging",
                       contentType: "application/json; charset=utf-8",
                       type: "POST"
                   },
                   parameterMap: function (options, type) {
                	   var constructionId = vm.changePerformer.constructionId;
                	   if(vm.changePerformer.priority==2)
                		   constructionId = null;
                       vm.performerSearch.page = options.page
                       vm.performerSearch.pageSize = options.pageSize
                       vm.performerSearch.keySearch = vm.keySearch;
                       vm.performerSearch.month = $scope.monthGrantt;
                       vm.performerSearch.year = $scope.yearGrantt;
                       vm.performerSearch.constructionId = vm.changePerformer.constructionId;
                       return JSON.stringify(vm.performerSearch)
                   }
               },
               pageSize: 10
           },
           dataBinding: function () {
               record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
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
                   field: "stt",
                   template: function () {
                       return ++record;
                   },
                   width: '10%',
                   columnMenu: false,
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   attributes: {
                       style: "text-align:center;"
                   }
               },{
                   title: "Tên đăng nhập",
                   field: 'loginName',
                   width: '15%',
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   attributes: {
                       style: "text-align:left;"
                   }
               },{
                   title: "Mã nhân viên",
                   field: 'employeeCode',
                   width: '15%',
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   attributes: {
                       style: "text-align:left;"
                   }
               },{
                   title: "Họ tên",
                   field: 'fullName',
                   width: '20%',
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   attributes: {
                       style: "text-align:left;"
                   }
               },{
                   title: "Email",
                   field: 'email',
                   width: '15%',
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   attributes: {
                       style: "text-align:right;"
                   }
               },{
                   title: "Số điện thoại",
                   field: 'phoneNumber',
                   width: '15%',
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   attributes: {
                       style: "text-align:right;"
                   }
               },{
                   title: "Chọn",
                   headerAttributes: { style: "text-align:center;font-weight: bold;" },
                   template: dataItem =>
                       '<div class="text-center">' +
                       '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                       '<i id="#=code#"  ng-click="caller.choosePerformerForChanging(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                       '</a>'
                       + '</div>',
                   width: '10%'
               },{
                   field: 'sysUserId',
                   hidden: true
               }
           ]
       });
	   
	   //lấy ra danh sách workitem cần update
	   vm.workItemTaskGridOptions = kendoConfig.getGridOptions({
       	editable:true,
			autoBind: true,
			scrollable: false, 
			resizable: true,
			dataBinding: function() {
               record = (this.dataSource.page() -1) * this.dataSource.pageSize();
           },
           reorderable: true,
			dataSource: {
	        	autoSync: true,    
				serverPaging: false,
				schema: {
					type:"json",
					 model: {
		                 	fields: {
		                 		stt: {editable: false},
		                 		name: {editable: false},
		                 		quantity:  { type: "number", format: "{0:c}", editable: true },
		                 		startDate: {editable: true},
		                 		endDate: {editable: true},
		                 	}
		                 },
					 total: function (response) {
						    $("#cntCount").text(""+response.total);
							return response.total; 
						},
					data: function (response) {				
						for(var x in response.data){
							response.data[x].sysUserId=Constant.userInfo.vpsUserToken.sysUserId
						}
						return response.data; 
					},
	            },
				transport: {
					read: {
						url: Constant.BASE_SERVICE_URL +"constructionTaskService/findForChangePerformer",
						contentType: "application/json; charset=utf-8",
						type: "POST"
					},					
					parameterMap: function (options, type) {
						vm.isCheckAll = false;
						vm.workItemSearch.performerId =  !!vm.changePerformer.performerId ? vm.changePerformer.performerId : -1;
					    vm.workItemSearch.constructionId = !!vm.changePerformer.constructionId ? vm.changePerformer.constructionId : -1;
					    vm.workItemSearch.month = $scope.monthGrantt;
				    	vm.workItemSearch.year = $scope.yearGrantt;
						return JSON.stringify(vm.workItemSearch)
					}
				},					 
				pageSize: 10
			},
			noRecords: true,
			columnMenu: false,
			messages: {
				noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
			},
			pageable: {
				refresh: false,
				 pageSizes: [10, 15, 20, 25],
				messages: {
	                display: "{0}-{1} của {2} kết quả",
	                itemsPerPage: "<span translate>kết quả/trang</span>",
	                empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
	            }
			},
			columns: [{
				title: "<input type='checkbox' id='checkAllWorkItem' name='ckAll' ng-click='caller.handleCheckAllWorkItem()' ng-model='caller.isCheckAll'/>",
				width: '10%',
				template: "<input type='checkbox' id='checkWorkItem' name='gridcheckbox' ng-click='caller.handleCheckForWorkItem(dataItem)' ng-model='dataItem.selected'/>",
		        headerAttributes: {style: "text-align:center;"},
				attributes: {style: "text-align:center;"},
			},{
				title: "Hạng mục",
		        field: 'workItemName',
		        width: '30%',
		        headerAttributes: {
					style: "text-align:center; font-weight: bold;",
					translate:"",
				},
				attributes: {
					style: "text-align:left;",
					readonly:"readonly"
				},
			},
			{
				title: "Đối tác",
		        field: 'partnerName',
		        width: '25%',
		        headerAttributes: {
					style: "text-align:center; font-weight: bold;",
					translate:"",
				},
				attributes: {
					style: "text-align:left;",
					readonly:"readonly"
				},
			},
			{
				title: "Sản lượng <br>(Triệu VND)",
		        field: 'quantity',
		        width: '20%',
		        headerAttributes: {
		        	style: "text-align:center; font-weight: bold;"
				},
				attributes: {style: "text-align:left;"},
				format: "{0:n0}"
			},{
				title: "Từ ngày",
		        field: 'startDate',
		        width: '20%',
		        template: dataItem =>!!dataItem.startDate ? kendo.toString(kendo.parseDate(dataItem.startDate,'dd/MM/yyyy'), 'dd/MM/yyyy') : "",
		        headerAttributes: {
		        	style: "text-align:center; font-weight: bold;",
				},
				attributes: {style: "text-align:left;"},
				editor: function (container, options) {
				    var input = $("<input id = 'kendoDate' ng-field = 'startDate' ng-blur ='caller.validateDateCell(dataItem)'/>");
				    input.attr("name", options.field);
				    input.appendTo(container);
				    input.kendoDatePicker({format:"dd/MM/yyyy"});
			    },
			},{
				title: "Đến ngày",
		        field: 'endDate',
		        template: dataItem =>!!dataItem.endDate ? kendo.toString(kendo.parseDate(dataItem.endDate,'dd/MM/yyyy'), 'dd/MM/yyyy') : "",
		        width: '20%',
		        headerAttributes: {
		        	style: "text-align:center; font-weight: bold;",
				},
				attributes: {style: "text-align:center;"},
				editor: function (container, options) {
				    var input = $("<input id = 'kendoDate'  ng-field = 'endDate' ng-blur ='caller.validateDateCell(dataItem)'/>");
				    input.attr("name", options.field);
				    input.appendTo(container);
				    input.kendoDatePicker({format:"dd/MM/yyyy"});
			    },
			}]
		});
	   
	   function refreshGrid(){
		   if(!!vm.changePerformer.constructionId){
		       $('#workItemTaskGrid').data('kendoGrid').dataSource.read().then(function(){
		    	   $('#workItemTaskGrid').data('kendoGrid').refresh()
		       });
		       $('#workItemTaskGrid').data('kendoGrid').refresh();
		   }
       }   
	   
	   vm.chooseConstructionForChanging = function(data){
		   vm.changePerformer.constructionCode = data.code;
           vm.changePerformer.constructionId = data.constructionId;
           if(vm.changePerformer.priority==1)
        	   getFirstFound();
           CommonService.dismissPopup1();
           refreshGrid();
	   }
	   
	   vm.choosePerformerForChanging = function(data){
		   vm.changePerformer.performerName = data.fullName;
           vm.changePerformer.performerId = data.sysUserId;
           CommonService.dismissPopup1();
           refreshGrid();
	   }
	   
	   function validateChangingGrid(){
		   if(!vm.changePerformer.altPerformerId){
			   toastr.warning("Người được nhận không được để trống!");
			   return false;
		   }
		   vm.updateList =[];
		   var table = $("#workItemTaskGrid  table")[0];
		   var isQuantityValid = true;
		   var isStartDateValid = true;
		   var isEndDateValid = true;
		   var listWorkItem = $("#workItemTaskGrid").data().kendoGrid.dataSource.data();
		   for(var i = 0; i<listWorkItem.length; i++){
				if(listWorkItem[i].selected == true){
					var row = i+1;
					if ((listWorkItem[i].quantity == undefined || listWorkItem[i].quantity === '')) {
						$(table.rows[row].cells[3]).addClass("k-state-focused");
						$(table.rows[row]).addClass("k-state-selected");
						isQuantityValid = false;
	                }
					if ((listWorkItem[i].startDate == undefined || listWorkItem[i].startDate == '')) {
						$(table.rows[row].cells[4]).addClass("k-state-focused");
						$(table.rows[row]).addClass("k-state-selected");
						isStartDateValid = false;
	                }
					if ((listWorkItem[i].endDate == undefined || listWorkItem[i].endDate == '')) {
						$(table.rows[row].cells[5]).addClass("k-state-focused");
						$(table.rows[row]).addClass("k-state-selected");
						isEndDateValid = false;
	                }
					listWorkItem[i].oldPerformerId = vm.changePerformer.performerId;
					listWorkItem[i].performerId = vm.changePerformer.altPerformerId;
					listWorkItem[i].performerWorkItemId = vm.changePerformer.altPerformerId;
					listWorkItem[i].quantity = listWorkItem[i].quantity * 1000000;
					listWorkItem[i].endDate =kendo.toString(kendo.parseDate(listWorkItem[i].endDate,'dd/MM/yyyy'), 'dd/MM/yyyy');
					listWorkItem[i].startDate =kendo.toString(kendo.parseDate(listWorkItem[i].startDate,'dd/MM/yyyy'), 'dd/MM/yyyy')
		        	vm.updateList.push(listWorkItem[i]);
				}
			}
		   
		   if(!vm.updateList || vm.updateList == 0){
				toastr.warning("Chưa có hạng mục nào được chọn!")
				return false;
			}
		   
		   	if(!isStartDateValid)
				toastr.warning("Thời gian từ ngày không được để trống!")
			if(!isEndDateValid)
				toastr.warning("Thời gian từ ngày không được để trống!")
			if(!isQuantityValid)
				toastr.warning("Sản lượng không được để trống!")
			return isQuantityValid && isStartDateValid && isEndDateValid;
	   }
	   
	   vm.handleCheckAllWorkItem = function(){
		   var listWorkItem = $("#workItemTaskGrid").data().kendoGrid.dataSource.data();
		   if(vm.isCheckAll){
			   for(var i = 0; i<listWorkItem.length; i++){
					listWorkItem[i].selected = true;
					$('#workItemTaskGrid').data('kendoGrid').refresh();
				}
		   }else{
			   for(var i = 0; i<listWorkItem.length; i++){
					listWorkItem[i].selected = false;
					$('#workItemTaskGrid').data('kendoGrid').refresh();
				}
		   }
	   }
	   
	   function getFirstFound(){
//		   Restangular.all("constructionTaskService/getPerformerAutocomplete").post({
//               keySearch: null,
//               pageSize: vm.performerOption.pageSize,
//               constructionId: vm.changePerformer.constructionId,
//               month : $scope.monthGrantt,
//               year : $scope.yearGrantt,
//               page: 1
//           }).then(function (response) {
//        	   vm.changePerformer.perFormerName = response[0].fullName;
//               vm.changePerformer.performerId = response[0].sysUserId;
//           })
	   }
}
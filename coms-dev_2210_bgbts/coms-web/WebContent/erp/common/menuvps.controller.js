(function() {
	
	'use strict';

	var controllerId = 'MenuController';

	angular.module('MetronicApp').controller(controllerId, MenuController);

	/* @ngInject */
	function MenuController($scope, $rootScope, Constant, $http, Restangular,
			CommonService) {
		var vm = this;
		vm.listMenuKey = {};
		$scope.Constant = Constant;

		$scope.$watch(function() {
			return $rootScope.casUser;
		}, function(casUser) {
			if (casUser == null) {
				return;
			}
			for (var i = 0; i < casUser.parentMenu.length; i++) {
				switch (casUser.parentMenu[i].code) {
				//menu danh muc
				case "CAT_PARENT": {
					casUser.parentMenu[i].classIcon = "dmgl";
					var menu = getChildMenu(casUser.parentMenu[i].childMenu);
					vm.listMenuKey.listCatMenu = menu;
					break;
				}
				//menu hop dong
				case "CONTRACT": {
					casUser.parentMenu[i].classIcon = "qlhopdong";
					var menu = getChildMenu(casUser.parentMenu[i].childMenu);
					vm.listMenuKey.listImsMenu = menu;
					break;
				}
				//menu cong trinh
				case "CAT_CONTRUCTION_MENU": {
					casUser.parentMenu[i].classIcon = "qlcongtrinh";
					var menu = getChildMenu(casUser.parentMenu[i].childMenu);
					vm.listMenuKey.listComsMenu = menu;
					break;
				}
				case "WMS_SHIPMENT": {
					casUser.parentMenu[i].classIcon = "qlLohang";
					break;
				}
				case "WMS_INPUT": {
					casUser.parentMenu[i].classIcon = "qlNhapKho";
					break;
				}
				//menu kho
				case "WMS": {
					casUser.parentMenu[i].classIcon = "qlKho";
					var menu = getChildMenu(casUser.parentMenu[i].childMenu);
					vm.listMenuKey.listWmsMenu = menu;
					break;
				}
				case "WMS_INVENTORY": {
					casUser.parentMenu[i].classIcon = "qlTonkho";
					break;
				}
				case "WMS_TOOL": {
					casUser.parentMenu[i].classIcon = "qlTienIch";
					break;
				}
				case "WMS_REPORT": {
					casUser.parentMenu[i].classIcon = "qlBaoCao";
					break;
				}
				//menu kcs
				case "KCS": {
					casUser.parentMenu[i].classIcon = "qlKcs";
					var menu = getChildMenu(casUser.parentMenu[i].childMenu);
					vm.listMenuKey.listKcsMenu = menu;
					break;
				}
				
				//menu hcqt
				case "HCQT": {
					casUser.parentMenu[i].classIcon = "qlHcqt";
					var menu = getChildMenu(casUser.parentMenu[i].childMenu);
					vm.listMenuKey.listHcqtMenu = menu;
					break;
				}
				
				//Huypq-20190913-start
				case "ALL_IN_ONE": {
					casUser.parentMenu[i].classIcon = "dmgl";
					var menu = getChildMenu(casUser.parentMenu[i].childMenu);
					vm.listMenuKey.listAIOMenu = menu;
					break;
				}
				case "AMS": {
					casUser.parentMenu[i].classIcon = "qlts";
					var menu = getChildMenu(casUser.parentMenu[i].childMenu);
					vm.listMenuKey.listAmsMenu = menu;
					break;
				}
				//Huy-end
				
				}
			}
//			vm.menuObjects = casUser.parentMenu;
			for(var i in casUser.parentMenu){
				if(casUser.parentMenu[i].code=="CAT_CONTRUCTION_MENU"){
					vm.menuObjects = [casUser.parentMenu[i]];
				}
			}
//			vm.menuObjects = [casUser.parentMenu[2]];//nhantv menu coms only
		})

		vm.goTo = goTo;
		vm.goToDefault = goToDefault;

		/*
		 * get menu text - neu vsa tra ve null thi
		 */
		vm.getMenuText = function(menuObject) {
			try {
				if (menuObject.url == null || menuObject.url == undefined) {
					return CommonService.translate(menuObject.name);
					/* return menuObject.name; */
				}

				var template = Constant.getTemplateUrl(menuObject.code);
				if (template == null) {
					if (menuObject.name != '') {
						return CommonService.translate(menuObject.name);

					}
					return "N/A";
				}
				return template.title;
			} catch (err) {
				console.debug(err);
				return "N/A";
			}
		}

		/* Handle action client on a menu item */
		function goTo(menuKey) {
			var template = Constant.getTemplateUrl(menuKey);
			var lstOpenMenu = ['APP_PARAM', 'CAT_UNIT', 'GOODS','CAT_PROVINCE','CAT_PRODUCING_COUNTRY','CAT_MANUFACTURER'];
			var lstWmsMenu = vm.listMenuKey.listWmsMenu;
			var lstCatMenu = vm.listMenuKey.listCatMenu;
			var lstImsMenu = vm.listMenuKey.listImsMenu;
			var lstComsMenu = vm.listMenuKey.listComsMenu;
			var lstHcqtMenu = vm.listMenuKey.listHcqtMenu;
			var lstKcsMenu = vm.listMenuKey.listKcsMenu;
			
			var listAIOMenu = vm.listMenuKey.listAIOMenu;
			var listAmsMenu = vm.listMenuKey.listAmsMenu;
			
			//Huypq-start
//			var lstHcqtMenu = vm.listMenuKey.listHcqtMenu;
//			if (lstHcqtMenu.indexOf(menuKey) !== -1) {
//				postal.publish({
//					channel : "Tab",
//					topic : "open",
//					data : template
//				});
//			}
			//Huy-end
			
			if (lstComsMenu.indexOf(menuKey) !== -1) {
				postal.publish({
					channel : "Tab",
					topic : "open",
					data : template
				});
			} else {
				var menuOpenToUrl;
				var urlCallMenu = "http://coms.congtrinhviettel.com.vn";

				if(lstCatMenu!=null && lstCatMenu.indexOf(menuKey) !== -1)
					{
					menuOpenToUrl = 'http://cat.congtrinhviettel.com.vn';
					var urlCallService= 'http://cat.congtrinhviettel.com.vn';
					callUrlFromMenu(menuOpenToUrl,urlCallMenu,urlCallService,menuKey);

					}
				else if(lstWmsMenu!=null && lstWmsMenu.indexOf(menuKey) !== -1)
				{
					menuOpenToUrl = 'http://wms.congtrinhviettel.com.vn';
					var urlCallService= 'http://wms.congtrinhviettel.com.vn';
					callUrlFromMenu(menuOpenToUrl,urlCallMenu,urlCallService,menuKey);
				}
				
				else if(lstImsMenu!=null && lstImsMenu.indexOf(menuKey) !== -1)
				{
					menuOpenToUrl = 'http://ims.congtrinhviettel.com.vn';
					var urlCallService= 'http://ims.congtrinhviettel.com.vn';
					callUrlFromMenu(menuOpenToUrl,urlCallMenu,urlCallService,menuKey);
				}
				//Huypq-20190913-start
				else if(lstHcqtMenu!=null && lstHcqtMenu.indexOf(menuKey) !== -1)
				{
					menuOpenToUrl = 'http://hcqt.congtrinhviettel.com.vn';
					var urlCallService= 'http://hcqt.congtrinhviettel.com.vn';
					callUrlFromMenu(menuOpenToUrl,urlCallMenu,urlCallService,menuKey);
				}
				
				else if(listAIOMenu!=null && listAIOMenu.indexOf(menuKey) !== -1)
				{
					menuOpenToUrl = 'http://aio.congtrinhviettel.com.vn';
					var urlCallService= 'http://aio.congtrinhviettel.com.vn';
					callUrlFromMenu(menuOpenToUrl,urlCallMenu,urlCallService,menuKey);
				}
				else if(listAmsMenu!=null && listAmsMenu.indexOf(menuKey) !== -1)
				{
					menuOpenToUrl = 'http://ams.congtrinhviettel.com.vn';
					var urlCallService= 'http://ams.congtrinhviettel.com.vn';
					callUrlFromMenu(menuOpenToUrl,urlCallMenu,urlCallService,menuKey);
				}
				//Huy-end
				//hoanm1_20180522_start
				/*
				var obj={
			            menuCode:menuKey,
			            urlCallMenu:"http://10.30.145.25:8102/wms-web/"
			        }
					CommonService.requestMenuCode(obj).then(function(result) {
						var win = window.open('http://localhost:8084/wms-web/', '_blank');
														win.focus();
					});
				
				if(menuOpenToUrl !=undefined && menuOpenToUrl !=null)
					{
					var win = window.open(menuOpenToUrl, '_blank');
					win.focus();
					}*/
				//	hoanm1_20180522_end
			}
		}
		vm.goToDash=goToDash;
		function goToDash(menuKey) {
			var hasPerm = true;
			if (hasPerm) {
				var template = Constant
					.getTemplateUrl(menuKey);

				postal.publish({
					channel: "Tab",
					topic: "open",
					data: template
				});
			} else {
				// toastr.error(gettextCatalog.getString("Tài
				// khoản đăng nhập
				// hiện tại không được phép truy cập vào
				// chức năng này!"));
			}

		}
		
		function callUrlFromMenu(menuOpenToUrl, urlCallMenu,urlCallService,menuKey)
		{
			if($("#wait").length == 0){
				var loading = '<div id="wait" style="position:fixed;top:50%;left:50%;z-index:20003;width:100%;height:100%">'+
					'<img src="assets/global/kendoui/styles/Bootstrap/loading-image.gif"> '+
				'</div>';
	    		$("body").append(loading);
	    	}
			
			var isOpen = false;
			var countOpen = 0;
			$('iframe#comsFrameLoadMenu').attr('src', menuOpenToUrl);
			$('iframe#comsFrameLoadMenu').on('load',function() {
				var obj={
			            menuCode:menuKey,
			            urlCallMenu:urlCallMenu,
			            urlCallService: urlCallService
			        }
				if(!isOpen)
					{
					CommonService.requestMenuCode(obj).then(function(result) {
						isOpen = true;	
						var win = window.open(menuOpenToUrl, '_blank');
						if(win !=null && typeof(win) !='undefined'){								
							win.focus();
							$("#wait").remove();
							}
							setTimeout(function(){
															$('iframe#comsFrameLoadMenu').attr('src', '');
															$('iframe#comsFrameLoadMenu').empty();
														},30000);														
					})
					.catch(function(data){
						if(countOpen < 8)
							{
								$('iframe#comsFrameLoadMenu').attr('src',menuOpenToUrl);
								countOpen ++;
							}else
								{
								var win = window.open(menuOpenToUrl, '_blank');
								if(win !=null && typeof(win) !='undefined'){								
									win.focus();
									$("#wait").remove();}
								}
					})
					;
					}	
				});
		}

		
		
		function goToDefault(menuKey) {
			if(menuKey == 'GOODS'){
				var template = Constant.getTemplateUrl(menuKey);
				postal.publish({
					channel : "Tab",
					topic : "open",
					data : template
				});
			}
		}
		
		vm.activeHomePage = activeHomePage;
		function activeHomePage() {
			postal.publish({
				channel : "Tab",
				topic : "active"

			});
		}
		
		function getChildMenu(menu, listReturn){
			listReturn = !!listReturn ? listReturn : [];
			menu.forEach(function(element){
				if(!!element.childMenu && element.childMenu.length>0){
					getChildMenu(element.childMenu, listReturn);
				}else{
					listReturn.push(element.code);
				}
			});
			return listReturn;
		}
	}
})();
(function() {
	'use strict';

	var controllerId = 'MenuController';

	angular.module('MetronicApp').controller(controllerId, MenuController);

	/* @ngInject */
	function MenuController($scope, $rootScope, Constant, $http,Restangular,CommonService) {
		var vm = this;
		$scope.Constant = Constant;

		$scope.$watch(function() {
	        return $rootScope.casUser;
	    },  function(casUser){
	    	if(casUser==null){
	    		return ;
	    	}
			console.log(casUser);
			vm.menuObjects=casUser.parentMenu;
			
		})

		/*
		 * get menu text - neu vsa tra ve null thi
		 */
		vm.getMenuText=function(menuObject){
            try {
            	if(menuObject.url==null){
            		return CommonService.translate(menuObject.name);
            		/*return menuObject.name;*/
            	}
                var template = Constant.getTemplateUrl(menuObject.url);
                if(template==null){
                	if(menuObject.name!=''){
                		return CommonService.translate(menuObject.code);
                		
                	}
        			return "N/A";
                }
                return template.title;
            }catch(err){
				console.debug(err);
                return "N/A";
            }
        }
		
		/* Handle action client on a menu item */
//		function goTo(menuKey) {
//			var template = Constant.getTemplateUrl(menuKey);
//			console.debug("postal", postal);
//			postal.publish({
//				channel : "Tab",
//				topic   : "open",
//				data    : template
//			});
//		}
		vm.goTo = goTo;
		function goTo(menuKey) {
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
		vm.activeHomePage=activeHomePage;
		function activeHomePage() {
			CommonService.goToMenu("DASH_BOARD");
			postal.publish({
				channel : "Tab",
				topic   : "active"
				
			});
		}
		
	}
})();
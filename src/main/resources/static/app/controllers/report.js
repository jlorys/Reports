angular.module('Reports')
.controller('ReportController', function($http, $scope, AuthenticationService) {
	$scope.buttonText = 'Dodaj';
	var init = function() {
		$http.get('api/userreports', {headers: AuthenticationService.createAuthorizationTokenHeader()}).success(function(res) {
			$scope.reportList = res;
			$scope.uploading = false;
			$scope.reportForm.$setPristine();
			$scope.report = null;
			
		}).error(function(error) {
			$scope.message = error.message;
		});
	};
	$scope.deleteReport = function(report) {
		$http.delete('api/report/'+report.id, {headers: AuthenticationService.createAuthorizationTokenHeader()}).success(function(res) {
			$scope.deleteMessage ="Sukces!";
			init(); 
		}).error(function(error) {
			$scope.deleteMessage = error.message;
		});
	};
	$scope.initDownload = function(report) {
        $http({
            url: 'api/userReport/file/'+report.id,
            method: "GET",
            responseType: 'arraybuffer',
            headers: AuthenticationService.createAuthorizationTokenHeader()
        }).then(function(response) {

			var file = new Blob([response.data]);
			var filename = report.name;
			var extension = report.extension;
            saveAs(file, filename.concat(extension));
            $scope.deleteMessage ="Sukces!";

        }).catch(function(error) {
			$scope.deleteMessage = error.message;
		});
	};
	var addReport = function(){
		$scope.uploading = true;
		var f = document.getElementById('file').files[0];
		
		var n = document.getElementById('file').files[0].name;
		var extension = n.substr(n.lastIndexOf('.'));
		var name = n.substring(0, n.length - extension.length);
		var description = document.getElementById('description').value;
		  
		var report = {extension: extension, name: name, description: description}
		  
		$http.post('api/report/uploadfile', f, {headers: AuthenticationService.createAuthorizationTokenHeader()}).success(function(res) {
			  $http.put('api/report/uploaddetails', report, {headers: AuthenticationService.createAuthorizationTokenHeader()}).success(function(res) {
				  init();
			   }).error(function(error) {
					$scope.message = error.message;
				});
			  
				$scope.confirmPassword = null;
				$scope.reportForm.$setPristine();
				$scope.message = "Sprawozdanie dodane";
	
			}).error(function(error) {
				$scope.message = error.message;
			});
		  
    };
	$scope.submit = function() {
			addReport();	
	};
	init();
});

angular.module('Reports').directive('validFile',function(){
	  return {
	    require:'ngModel',
	    link:function(scope,el,attrs,ngModel){
	      //change event is fired when file is selected
	      el.bind('change',function(){
	        scope.$apply(function(){
	          ngModel.$setViewValue(el.val());
	          ngModel.$render();
	        });
	      });
	    }
	  }
	});


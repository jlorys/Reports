angular.module('Reports')
.controller('AllReportsController', function($http, $scope, AuthenticationService) {

	var init = function() {
		$http.get('api/reports', {headers: AuthenticationService.createAuthorizationTokenHeader()}).success(function(res) {
			
			$scope.reportList = res;
			$scope.buttonGradeText = 'Wystaw ocenę';
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
            url: 'api/report/file/'+report.id,
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
	$scope.initAddGrade = function(report) {
		$scope.report = report;

	};
	var addReportGrade = function(){
	   $http.put('api/report/uploadgrade', $scope.report, {headers: AuthenticationService.createAuthorizationTokenHeader()}).success(function(res) {
    	    $scope.message = "Ocena dodana";
    	    $scope.report = null;
			$scope.confirmPassword = null;
			$scope.reportForm.$setPristine();
			init();
		}).error(function(error) {
			$scope.message = error.message;
		});
      
	};
	$scope.submit = function() {
			addReportGrade();	
	};
    init();
});



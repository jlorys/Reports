angular.module('Reports')
.controller('LogController', function($http, $scope, $state, AuthenticationService, $rootScope) {

	$scope.login = function() {
		var base64Credential = btoa($scope.username + ':' + $scope.password);

		$http.get('user', {
			headers : {
				'Authorization' : 'Basic ' + base64Credential
			}
		}).success(function(res) {
			$scope.password = null;
			if (res.authenticated) {
				$scope.message = '';
				// setting equal headers for all requests
                $http.defaults.headers.common['Authorization'] = 'Basic ' + base64Credential;
				AuthenticationService.user = res;
				$rootScope.$broadcast('LoginSuccessful');
				$state.go('report');
			} else {
				$scope.message = 'Logowanie nie powiodło się!';
			}
		}).error(function(error) {
			$scope.message = 'Logowanie nie powiodło się!';
		});
	};
});

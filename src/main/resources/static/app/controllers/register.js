angular.module('Reports')
.controller('RegisterController', function($http, $scope, AuthenticationService) {
	$scope.submit = function() {
		$http.post('register', $scope.appUser, {headers: AuthenticationService.createAuthorizationTokenHeader()}).success(function(res) {
			$scope.appUser = null;
			$scope.confirmPassword = null;
			$scope.register.$setPristine();
			$scope.message = "Rejestracja przebiegła pomyślnie!";
		}).error(function(error) {
			$scope.message = error.message;
		});
	};
});

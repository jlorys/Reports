angular.module('Reports')
.controller('NavbarController', function($http, $scope, AuthenticationService, $state, $rootScope) {
	$scope.$on('LoginSuccessful', function() {
		$scope.user = AuthenticationService.user;
	});
	$scope.$on('LogoutSuccessful', function() {
		$scope.user = null;
	});
	$scope.logout = function() {
		AuthenticationService.user = null;
		AuthenticationService.removeJwtToken();
		$rootScope.$broadcast('LogoutSuccessful');
		$state.go('log');
        $scope.authenticated = false;
	};
});

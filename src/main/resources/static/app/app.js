// Creating angular Application with module "Reports"
angular.module('Reports', [ 'ui.router' ])

//To avoid browser showing alerts to get user credentials
.config([ '$httpProvider', function($httpProvider) {
	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
} ])
//This method runs one time when module start
.run(function(AuthenticationService, $rootScope, $state) {
    // Every state change and the ui-router module will transmit $stateChangeStart
	$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
		// if user is logged or isn't
		if (!AuthenticationService.user) {
			if (toState.name != 'log' && toState.name != 'register') {
				event.preventDefault();
				$state.go('log');
			}
		} else {
			// if user is authorized to view some views then show him
			if (toState.data && toState.data.role) {
				if (toState.data.role != AuthenticationService.user.principal.role) {
					event.preventDefault();
					$state.go('accessing-fault');
				}
			}
		}
	});
});
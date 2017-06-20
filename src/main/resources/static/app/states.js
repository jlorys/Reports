angular.module('Reports').config(function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/page-not-found');
	$stateProvider.state('navbar', {
		abstract : true,
		url : '',
		views : {
			'navbar@' : {
				templateUrl : 'app/views/navbar.html',
				controller : 'NavbarController'
			}
		}
	}).state('log', {
		parent : 'navbar',
		url : '/log',
		views : {
			'content@' : {
				templateUrl : 'app/views/log.html',
				controller : 'LogController'
			}
		}
	}).state('users', {
		parent : 'navbar',
		url : '/users',
		data : {
			role : 'ADMIN'
		},
		views : {
			'content@' : {
				templateUrl : 'app/views/users.html',
				controller : 'UsersController',
			}
		}
	}).state('report', {
		parent : 'navbar',
		url : '/',
		views : {
			'content@' : {
				templateUrl : 'app/views/report.html',
				controller : 'ReportController'
			}
		}
	}).state('page-not-found', {
		parent : 'navbar',
		url : '/page-not-found',
		views : {
			'content@' : {
				templateUrl : 'app/views/page-not-found.html',
				controller : 'PageNotFoundController'
			}
		}
	}).state('accessing-fault', {
		parent : 'navbar',
		url : '/accessing-fault',
		views : {
			'content@' : {
				templateUrl : 'app/views/accessing-fault.html',
				controller : 'AccessingFaultController'
			}
		}
	}).state('register', {
		parent : 'navbar',
		url : '/register',
		views : {
			'content@' : {
				templateUrl : 'app/views/register.html',
				controller : 'RegisterController'
			}
		}
	}).state('allreports', {
		parent : 'navbar',
		url : '/allreports',
		data : {
			role : 'ADMIN'
		},
		views : {
			'content@' : {
				templateUrl : 'app/views/allreports.html',
				controller : 'AllReportsController'
			}
		}
	});
});

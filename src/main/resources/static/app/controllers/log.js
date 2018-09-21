angular.module('Reports')
.controller('LogController', ['$scope', '$rootScope', '$http', '$state', 'AuthenticationService',
    function($scope, $rootScope, $http, $state, authenticationService) {
        $scope.error = false;

        $scope.credentials = {};

        $scope.login = function() {
            // We are using formLogin in our backend, so here we need to serialize our form data
            $http({
                url: 'auth/login',
                method: 'POST',
                data: $scope.credentials,
                headers: authenticationService.createAuthorizationTokenHeader()
            })
                .success(function(res) {
                    $scope.message = '';
                    $rootScope.authenticated = true;
                    authenticationService.setJwtToken(res.access_token);
                    authenticationService.user = res.user;
                    authenticationService.roles = res.roles.toString();
                    $rootScope.$broadcast('LoginSuccessful');
                    $state.go("report");
                    $scope.error = false;
                })
                .catch(function() {
                    $scope.message = "Nieprawidłowy login lub hasło";
                    authenticationService.removeJwtToken();
                    $rootScope.authenticated = false;
                    $scope.error = true;
                });
        };
    }]);

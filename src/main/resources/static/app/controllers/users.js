angular.module('Reports')
.controller('UsersController', function($http, $scope, AuthenticationService) {
	var edit = false;
	$scope.buttonText = 'Dodaj';
	var init = function() {
		$http.get('api/users').success(function(res) {
			$scope.users = res;
			
			$scope.userForm.$setPristine();
			$scope.appUser = null;
			$scope.buttonText = 'Dodaj';
			
		}).error(function(error) {
			$scope.message = error.message;
		});
	};
	$scope.initEdit = function(appUser) {
		edit = true;
		$scope.appUser = appUser;
		$scope.message='';
		$scope.buttonText = 'Aktualizuj';
	};
	$scope.initAddUser = function() {
		edit = false;
		$scope.appUser = null;
		$scope.userForm.$setPristine();
		$scope.message='';
		$scope.buttonText = 'Dodaj';
	};
	$scope.deleteUser = function(appUser) {
		$http.delete('api/users/'+appUser.id).success(function(res) {
			$scope.deleteMessage ="Sukces!";
			init();
		}).error(function(error) {
			$scope.deleteMessage = error.message;
		});
	};
	var editUser = function(){
		$http.put('api/users', $scope.appUser).success(function(res) {
			$scope.appUser = null;
			$scope.confirmPassword = null;
			$scope.userForm.$setPristine();
			$scope.message = "Aktualizacja zakończona sukcesem";
			init();
		}).error(function(error) {
			$scope.message =error.message;
		});
	};
	var addUser = function(){
		$http.post('api/users', $scope.appUser).success(function(res) {
			$scope.appUser = null;
			$scope.confirmPassword = null;
			$scope.userForm.$setPristine();
			$scope.message = "Użytkownik dodany";
			init();
		}).error(function(error) {
			$scope.message = error.message;
		});
	};
	$scope.submit = function() {
		if(edit){
			editUser();
		}else{
			addUser();	
		}
	};
	init();

});

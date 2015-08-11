var fotiloApp = angular.module('fotiloApp', []);

fotiloApp.controller('FotiloMainController', function ($scope, $http) {
    $http.get('/api/cameras').success(function(data, status, headers, config) {
        $scope.cameras = data;
    });
});

fotiloApp.controller('FotiloCameraController', function ($scope, $http) {
    $scope.selectedCamera = "inside";

    $http.get('/api/cameras').success(function(data, status, headers, config) {
        $scope.cameras = data;
    });

    $http.get('/api/camera/' + $scope.selectedCamera).success(function(data, status, headers, config) {
        $scope.camera = data;
    });
});


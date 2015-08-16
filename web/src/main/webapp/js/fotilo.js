var fotiloApp = angular.module('fotiloApp', []);

fotiloApp.factory('CameraService', function () {

})

fotiloApp.controller('FotiloMainController', function ($scope, $http) {
    $http.get('/api/cameras').success(function (data, status, headers, config) {
        $scope.cameras = data;
    });
});

var DIRECTIONS = Object.freeze([
    {name: "left", symbol: '←'},
    {name: "right", symbol: '→'},
    {name: "stop", symbol: 'stop'},
    {name: "up", symbol: '↑'},
    {name: "down", symbol: '↓'}
]);

var CameraController = function ($http, $scope) {
    return {
        changeFrameRate: function (newFrameRate, oldFrameRate) {
            $http.post('/api/camera/' + $scope.selectedCamera + '/settings/frame-rate/' + newFrameRate);
        },
        changeResolution: function (newResolution, oldResolution) {
            $http.post('/api/camera/' + $scope.selectedCamera + '/settings/resolution/' + newResolution);
        },
        move: function (directionName, duration) {
            $http.post('/api/camera/' + $scope.selectedCamera + '/control/move/' + directionName);
        },
        stop: function () {
            $http.post('/api/camera/' + $scope.selectedCamera + '/control/stop');
        }
    };
}

fotiloApp.controller('FotiloCameraController', function ($scope, $http) {
    $scope.directions = DIRECTIONS;
    $scope.selectedCamera = "inside";
    $scope.cameraController = CameraController($http, $scope);

    $http.get('/api/cameras').success(function (data, status, headers, config) {
        $scope.cameras = data;
    });

    $http.get('/api/camera/' + $scope.selectedCamera).success(function (data, status, headers, config) {
        $scope.camera = data;

        $scope.$watch(
            'camera.settings.resolution',
            $scope.cameraController.changeResolution,
            true
        );
    });

    $scope.move = function (directionIndex, duration) {
        var direction = $scope.directions[directionIndex];
        if (direction.name === 'stop') {
            $scope.cameraController.stop();
        } else {
            $scope.cameraController.move(direction.name, duration);
        }
    }
});


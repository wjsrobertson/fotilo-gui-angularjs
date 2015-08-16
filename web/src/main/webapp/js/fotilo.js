var fotiloApp = angular.module('fotiloApp', ['ui.router']);

var fotiloRouteConfig = function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/cameras");

    $stateProvider
        .state('camera-list', {
            url: "/cameras",
            templateUrl: "views/camera-list.view.html",
            controller: MainController
        })
        .state('camera', {
            url: "/camera/:cameraId",
            templateUrl: "views/camera.view.html",
            controller: FotiloCameraController
        })
}

var MainController = function ($scope, $http) {
    $http.get('/api/cameras').success(function (data, status, headers, config) {
        $scope.cameras = data;
    });
};

fotiloApp.config(fotiloRouteConfig);

//fotiloApp.controller('MainController',);

var DIRECTIONS = Object.freeze([
    {name: "left", symbol: '←'},
    {name: "right", symbol: '→'},
    {name: "stop", symbol: 'stop'},
    {name: "up", symbol: '↑'},
    {name: "down", symbol: '↓'}
]);

// TODO - redo as an angular dependency
var CameraService = function ($http, $scope) {
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

var FotiloCameraController = function ($scope, $http, $stateParams) {
    $scope.directions = DIRECTIONS;
    $scope.selectedCamera = $stateParams.cameraId;
    $scope.cameraService = CameraService($http, $scope);

    $http.get('/api/cameras').success(function (data, status, headers, config) {
        $scope.cameras = data;
    });

    $http.get('/api/camera/' + $scope.selectedCamera).success(function (data, status, headers, config) {
        $scope.camera = data;

        $scope.$watch(
            'camera.settings.resolution',
            $scope.cameraService.changeResolution,
            true
        );
    });

    $scope.move = function (directionIndex, duration) {
        var direction = $scope.directions[directionIndex];
        if (direction.name === 'stop') {
            $scope.cameraService.stop();
        } else {
            $scope.cameraService.move(direction.name, duration);
        }
    }
}

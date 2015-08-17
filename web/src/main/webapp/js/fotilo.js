var fotiloApp = angular.module('fotiloApp', ['ui.router', 'ui.slider']);

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
        changeBrightness: function (newBrightness, oldBrightness) {
            $http.post('/api/camera/' + $scope.selectedCamera + '/settings/brightness/' + newBrightness);
        },
        changeFrameRate: function (newFrameRate, oldFrameRate) {
            $http.post('/api/camera/' + $scope.selectedCamera + '/settings/frame-rate/' + newFrameRate);
        },
        changeResolution: function (newResolution, oldResolution) {
            $http.post('/api/camera/' + $scope.selectedCamera + '/settings/resolution/' + newResolution);
        },
        changeContrast: function (newContrast, oldContrast) {
            $http.post('/api/camera/' + $scope.selectedCamera + '/settings/contrast/' + newContrast);
        },
        changePanTiltSpeed: function (newPanTiltSpeed, oldPanTiltSpeed) {
            $http.post('/api/camera/' + $scope.selectedCamera + '/settings/pan-tilt-speed/' + newPanTiltSpeed);
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
        $scope.$watch(
            'camera.settings.frameRate',
            $scope.cameraService.changeFrameRate,
            true
        );
        $scope.$watch(
            'camera.settings.contrast',
            $scope.cameraService.changeContrast,
            true
        );
        $scope.$watch(
            'camera.settings.brightness',
            $scope.cameraService.changeBrightness,
            true
        );
        $scope.$watch(
            'camera.settings.panTiltSpeed',
            $scope.cameraService.changePanTiltSpeed,
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

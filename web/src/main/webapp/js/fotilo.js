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

fotiloApp.config(fotiloRouteConfig);

var MainController = function ($scope, $http) {
    $http.get('/api/cameras').success(function (data, status, headers, config) {
        $scope.cameras = data;
    });
};

var CameraService = function ($http, $scope) {

    var timeToWaitBeforeChangingSettingMs = 50;
    var settingsChangeLog = {};

    function changeSetting(newValue, oldValue, settingName) {
        if (newValue === oldValue) {
            return;
        }

        settingsChangeLog[settingName] = newValue;
        setTimeout(
            function () {
                changeSettingIfValueHasntChanged(settingName, newValue)
            }
            , timeToWaitBeforeChangingSettingMs
        );
    }

    function changeSettingIfValueHasntChanged(settingName, newValue) {
        if (newValue === settingsChangeLog[settingName]) {
            var settingUrlPathPart = settingName;
            $http.post('/api/camera/' + $scope.selectedCamera + '/settings/' + settingUrlPathPart + '/' + newValue);
        }
    }

    return {
        changeBrightness: function (newBrightness, oldBrightness) {
            changeSetting(newBrightness, oldBrightness, 'brightness');
        },
        changeFrameRate: function (newFrameRate, oldFrameRate) {
            changeSetting(newFrameRate, oldFrameRate, 'frame-rate');
        },
        changeResolution: function (newResolution, oldResolution) {
            changeSetting(newResolution, oldResolution, 'resolution');
        },
        changeContrast: function (newContrast, oldContrast) {
            changeSetting(newContrast, oldContrast, 'contrast');
        },
        changePanTiltSpeed: function (newPanTiltSpeed, oldPanTiltSpeed) {
            changeSetting(newPanTiltSpeed, oldPanTiltSpeed, 'pan-tilt-speed');
        },
        move: function (directionName, duration) {
            $http.post('/api/camera/' + $scope.selectedCamera + '/control/move/' + directionName);
        },
        stop: function () {
            $http.post('/api/camera/' + $scope.selectedCamera + '/control/stop');
        }
    };
}

var DIRECTIONS = Object.freeze([
    {name: "left", symbol: '←'},
    {name: "right", symbol: '→'},
    {name: "stop", symbol: 'stop'},
    {name: "up", symbol: '↑'},
    {name: "down", symbol: '↓'}
]);

var FotiloCameraController = function ($scope, $http, $stateParams) {
    $scope.directions = DIRECTIONS;
    $scope.selectedCamera = $stateParams.cameraId;
    $scope.cameraService = CameraService($http, $scope);

    $http.get('/api/cameras').success(function (data, status, headers, config) {
        $scope.cameras = data;
    });

    $http.get('/api/camera/' + $scope.selectedCamera).success(function (data, status, headers, config) {
        $scope.camera = data;

        var propertiesToWatch = {
            'camera.settings.brightness': $scope.cameraService.changeBrightness,
            'camera.settings.resolution': $scope.cameraService.changeResolution,
            'camera.settings.frameRate': $scope.cameraService.changeFrameRate,
            'camera.settings.contrast': $scope.cameraService.changeContrast,
            'camera.settings.panTiltSpeed': $scope.cameraService.changePanTiltSpeed
        }
        jQuery.each(propertiesToWatch, function (propertyToWatch, watchFunction) {
            $scope.$watch(propertyToWatch, watchFunction, true);
        });
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

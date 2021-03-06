var fotiloApp = angular.module('fotiloApp', ['ui.router', 'ui.slider', 'ui.bootstrap', 'ngCookies']);

var fotiloRouteConfig = function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/cameras");

    $stateProvider
        .state('cameras', {
            url: "/cameras",
            views: {
                'camera-list': {
                    templateUrl: "views/camera-list.view.html",
                    controller: MainController
                },
                'camera': {}
            }
        })
        .state('camera', {
            url: "/camera/:cameraId",
            views: {
                'camera-list': {
                    templateUrl: "views/camera-list.view.html",
                    controller: MainController
                },
                'camera': {
                    templateUrl: "views/camera.view.html",
                    controller: FotiloCameraController
                }
            }
        });
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

        function changeSettingIfValueHasntChanged(settingName, newValue) {
            if (newValue === settingsChangeLog[settingName]) {
                var settingUrlPathPart = settingName;
                $http.post('/api/camera/' + $scope.selectedCamera + '/settings/' + settingUrlPathPart + '/' + newValue);
            }
        }

        settingsChangeLog[settingName] = newValue;
        setTimeout(
            function () {
                changeSettingIfValueHasntChanged(settingName, newValue)
            }
            , timeToWaitBeforeChangingSettingMs
        );
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
        changeOrientation: function (newOrientation, oldOrientation) {
            changeSetting(newOrientation, oldOrientation, 'orientation');
        },
        flip: function (newRotation, oldRotation) {
            changeSetting(newRotation, oldRotation, 'flip');
        },
        setInfraRedLightOn: function (newValue, oldValue) {
            newValue = newValue === true ? 'on' : 'false';
            oldValue = oldValue === true ? 'on' : 'false';
            changeSetting(newValue, oldValue, 'infra-red-light-on');
        },
        move: function (directionName, duration) {
            if (duration) {
                $http.post('/api/camera/' + $scope.selectedCamera + '/control/move/' + directionName + '?duration=' + duration);
            } else {
                $http.post('/api/camera/' + $scope.selectedCamera + '/control/move/' + directionName);
            }
        },
        stop: function () {
            $http.post('/api/camera/' + $scope.selectedCamera + '/control/stop');
        }
    };
}

var FotiloCameraController = function ($scope, $http, $stateParams, $cookies) {
    $scope.selectedCamera = $stateParams.cameraId;
    $scope.cameraService = CameraService($http, $scope);
    $scope.volatileValues = {
        incrementalMovement: false,
        stepMoveDuration: 1
    }

    if (typeof $cookies.get('incrementalMovement') !== 'undefined') {
        $scope.volatileValues.incrementalMovement = ('true' === $cookies.get('incrementalMovement'));
    }
    $scope.$watch('volatileValues.incrementalMovement', function (newincrementalMovementValue, oldValue) {
        console.log("incremental movement: " + newincrementalMovementValue);
        $cookies.put('incrementalMovement', String(newincrementalMovementValue));
    });

    if (typeof $cookies.get('stepMoveDuration') !== 'undefined') {
        $scope.volatileValues.stepMoveDuration = parseInt($cookies.get('stepMoveDuration'));
    }
    $scope.$watch('volatileValues.stepMoveDuration', function (newStepMoveDuration, oldValue) {
        console.log("step move duration: " + newStepMoveDuration);
        $cookies.put('stepMoveDuration', String(newStepMoveDuration));
    });

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
            'camera.settings.panTiltSpeed': $scope.cameraService.changePanTiltSpeed,
            'camera.settings.infrRedCutEnabled': $scope.cameraService.setInfraRedLightOn
        }

        $.each(propertiesToWatch, function (propertyToWatch, watchFunction) {
            $scope.$watch(propertyToWatch, watchFunction, true);
        });
    });

    $scope.move = function (direction, duration) {
        if (direction === 'stop') {
            $scope.cameraService.stop();
        } else {
            $scope.cameraService.move(direction, duration);
        }
    }

    $scope.flip = function (rotation) {
        $scope.cameraService.flip(rotation);
    }

    $scope.orientation = function (orientation) {
        $scope.cameraService.changeOrientation(orientation);
    }
}

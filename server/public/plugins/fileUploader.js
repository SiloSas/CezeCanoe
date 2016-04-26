angular.module('uploader', []).directive('appFilereader', function ($q, $parse, ImageFactory) {
    return {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, element, attrs) {

            var model = $parse(attrs.appFilereader);
            var modelSetter = model.assign;
            element.bind('change', function () {
                scope.$apply(function () {
                    ImageFactory.postImage(element[0].files[0]).then(function(success) {
                        console.log(modelSetter)
                        console.log(model)
                        modelSetter(scope, success + "?0");
                    })
                });
            });

        } //link
    }; //return
}).factory("ImageFactory", function($q, $http) {
    return {
        postImage: function (image) {
            var fd = new FormData();
            var deferred = $q.defer();
            fd.append('picture', image);
            $http.post('/upload', fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).success(function (success) {
                console.log(success)
                deferred.resolve(success)
            }).error(function (error) {
                console.log(error)
            });
            return deferred.promise
        }
    }
}).directive('compileTemplate', function($compile, $parse){
    return {
        link: function(scope, element, attr){
            var parsed = $parse(attr.ngBindHtml);
            function getStringValue() { return (parsed(scope) || '').toString(); }

            //Recompile if the template changes
            scope.$watch(getStringValue, function() {
                $compile(element, null, -9999)(scope);  //The -9999 makes it skip directives so that we do not recompile ourselves
            });
        }
    }
});
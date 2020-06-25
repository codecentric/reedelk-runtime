let VersionController = (function () {

    let version = function () {
        $.get(Constants.HealthApiPath, function (data) {
            $('#runtime-version').text(data.version);
            $('#runtime-qualifier').text(data.qualifier);
        });
    };

    return {
        Version: version,
    }

})();

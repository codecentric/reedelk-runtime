let IndexController = (function () {

    const REFRESH_WAIT = 1000; // ms

    let listModules = function () {
        $.get(Constants.ModuleApiPath, function (data) {
            Utilities.SortByModuleName(data.modules);
            ModulesTableRenderer.Render(data.modules);
        });
    };

    let deployModule = function (formData) {
        $.ajax({
            url: Constants.ModuleDeployApiPath,
            data: formData,
            processData: false,
            contentType: false,
            type: 'POST',
            success: function (response) {

                Messages.Success('Module deployed, updating modules list ...');

                clearTableBody();
                showSpinner();

                setTimeout(function () {
                    clearTableBody();
                    listModules();
                }, REFRESH_WAIT);

            },
            error: function (error) {
                showError(moduleName, 'deployed', error);
            }
        });
    };

    let updateModule = function (moduleName, moduleFilePath) {
        let body = JSON.stringify({moduleFilePath: moduleFilePath});
        $.ajax({
            url: Constants.ModuleApiPath,
            type: 'PUT',
            contentType: "application/json",
            data: body,
            success: function (result) {

                Messages.Success('Module "' + moduleName + '" updated, updating modules list ...');

                clearTableBody();
                showSpinner();

                setTimeout(function () {
                    clearTableBody();
                    listModules();
                }, REFRESH_WAIT);

            },
            error: function (error) {
                showError(moduleName, 'updated', error);
            }
        });
    };

    let removeModule = function (moduleName, moduleFilePath) {
        let body = JSON.stringify({moduleFilePath: moduleFilePath});
        $.ajax({
            url: Constants.ModuleApiPath,
            type: 'DELETE',
            contentType: "application/json",
            data: body,
            success: function (result) {

                Messages.Success('Module "' + moduleName + '" removed, updating modules list ...');

                clearTableBody();
                showSpinner();

                setTimeout(function () {
                    clearTableBody();
                    listModules();
                }, REFRESH_WAIT);

            },
            error: function (error) {
                showError(moduleName, 'removed', error);
            }
        });
    };

    let clearTableBody = function () {
        $("#deployed-modules tbody tr").remove();
    };

    let showSpinner = function () {
        let table = document.getElementById("deployed-modules");
        let tableBody = $(table).find('tbody');
        let content = Template.Row(Template.Column('<p class="refreshing-list">' + Template.Bold('Updating modules &hellip;') + '</p>', '', 8));
        tableBody.append(content);
    };

    let showError = function (moduleName, actionName, error) {
        if (error.responseText) {
            Messages.Error('Module "' + moduleName + '" could not be ' + actionName + ': ' + error.responseText);
        } else {
            Messages.Error('Module "' + moduleName + '" could not be ' + actionName + '. Please check that the Runtime is up and running.');
        }
    };

    return {
        ListModules: listModules,
        DeployModule: deployModule,
        UpdateModule: updateModule,
        RemoveModule: removeModule
    }

})();

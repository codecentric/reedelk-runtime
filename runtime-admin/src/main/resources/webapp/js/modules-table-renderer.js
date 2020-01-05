var ModulesTableRenderer = (function() {

	var notUpdatableOrRemovableModules = [
		'module-rest'	 // NAME_CONVENTION
	];

	var excludedModulesFromList = [
		'runtime-admin',  // NAME_CONVENTION
	];

	var render = function(modules) {
		var table = document.getElementById("deployed-modules");

		for (var index in modules) {

        	var module = modules[index];

        	var name = module.name;

        	// Exclude modules which should not be in the list (e.g admin-console)
        	if (excludedModulesFromList.includes(name)) {
        		continue;
			}

        	var id = module.moduleId;
        	var version = module.version;
        	var path = module.moduleFilePath;
            var status = Utilities.IconByModuleStatus(module.state);

            var infoBtn = Template.CollapseButton('Info', 'icon-folder-open', '#' + module.moduleId, 'collapseExample');

            var enabled = true;
            if (notUpdatableOrRemovableModules.includes(name)) {
            	enabled = false;
			}
            var updateBtn = Template.ActionButton('Update', 'icon-redo2', 'onUpdate(\'' + name + '\',\'' + path + '\')', "btn-secondary", enabled);
            var removeBtn = Template.ActionButton('Remove', 'icon-bin', 'onRemove(\'' + name + '\',\'' + path + '\')', "btn-danger", enabled);

            var allColumns = 
                Template.Column(status, 'align-center') +
                Template.Column(id) + 
                Template.Column(name) + 
                Template.Column(version) + 
                Template.Column(path) + 
                Template.Column(infoBtn, 'align-right') +
                Template.Column(updateBtn, 'align-center') +
                Template.Column(removeBtn, 'align-left');

            var tableBody = $(table).find('tbody');
            tableBody.append(Template.Row(allColumns));

            var detailsContent = ModuleDetailsRenderer.Render(module);
            var content = Template.Div(detailsContent, id, 'collapse', 'data-parent="#modulesTableContainer"');
        	tableBody.append(Template.Row(Template.Column('', 'details-column', 1) + Template.Column(content, 'details-column', 8)));
        }
	};

	return {
		Render: render
	}

})();
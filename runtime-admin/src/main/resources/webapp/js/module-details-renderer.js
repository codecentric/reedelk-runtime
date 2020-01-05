let ModuleDetailsRenderer = (function() {

	let renderFlows = function(flows) {
    		Utilities.SortByFlowTitle(flows);
    		var flowsContent = flows.map(function(flow) {
    			return flow.title ?
					Template.Bold(flow.title) + ' (id: ' + flow.id + ')':
					Template.Bold('ID: ') + flow.id;
    		}).join('<br>');
		return flowsContent ? Template.DetailsRowItem('Flows:', flowsContent) : '';
	};

	let render = function(module) {

		let detailsContent = '';

		let state = Utilities.Capitalize(module.state);
		if (state) {
    	    detailsContent += Template.DetailsRowItem('State:', state);
    	}

    	let flows = module.flows;
    	if (flows) {
    		detailsContent += renderFlows(flows);
    	}

		let resolved = module.resolvedComponents.join('<br>');
		if (resolved) {
    	    detailsContent += Template.DetailsRowItem('Resolved components:', resolved);
    	}

		let unresolved = module.unresolvedComponents.join('<br>');
		if (unresolved) {
    	    detailsContent += Template.DetailsRowItem('Unresolved components:', unresolved);
    	}

    	// The JSON containing the exceptions string contains new lines. 
    	// We replace '\n' with the HTML '<br/>' whenever a new line is found.
    	let errors = module.errors.map(function(error){ return error.replace(/\n/g, "<br/>")}).join('<hr>');
    	if (errors) {
    	    detailsContent += Template.DetailsRowItem('Errors:', errors);
    	}

    	return Template.DetailsContainer(detailsContent);

	};

	return {
		Render: render
	}

})();
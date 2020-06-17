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

		let errorMessages = module.errors.map(function(error){ return error.message.replace(/\n/g, "<br/>")}).join('<hr>');
		if (errorMessages) {
			detailsContent += Template.DetailsRowItem('Error messages:', errorMessages);
		}

    	// The JSON containing the Error contains new lines in the stacktrace.
    	// We replace '\n' with the HTML '<br/>' whenever a new line is found.
    	let errorStacktraces = module.errors.map(function(error){ return error.stacktrace.replace(/\n/g, "<br/>")}).join('<hr>');
    	if (errorStacktraces) {
    	    detailsContent += Template.DetailsRowItem('Error Stacktraces:', errorStacktraces);
    	}

    	return Template.DetailsContainer(detailsContent);

	};

	return {
		Render: render
	}

})();

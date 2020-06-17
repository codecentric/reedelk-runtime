var Template = (function() {

	const Bold = ({text}) =>
		`<b>${text}</b>`;

	const Row = ({content}) => 
		`<tr>${content}</tr>`;

	const Column = ({content, additionalAttributes }) => 
		`<td ${additionalAttributes}>${content}</td>`;

	const Div = ({content, additionalAttributes}) => 
		`<div ${additionalAttributes}>${content}</div>`;

	const DetailsContainer = ({ content }) => 
		`<div class="container-fluid module-details-container">
    		${content}
    	</div>`;

	const DetailsRowItem = ({ title, content}) => 
		`<div class="row">
        	<div class="col-sm-1 module-details-row-item-title"><b>${title}</b></div>
        	<div class="col-sm-11 module-details-row-item-content">${content}</div>
    	</div>`;

	const ActionButton = ({ text, icon, action, type, enabled }) =>
		`<button type="button" class="btn btn-sm btn-labeled ${type}" onclick=${action} ${enabled}>
			<span class="btn-label"><i class="${icon}"></i></span>
			${text}
		</button>`;

	const CollapseButton = ({ text, icon, dataTarget, ariaControl }) => 
		`<button type="button" class="btn btn-sm btn-labeled btn-secondary" 
	  		data-toggle="collapse" data-target="${dataTarget}" 
	  		aria-expanded="false" aria-controls="${ariaControl}">
	  		<span class="btn-label"><i class="${icon}"></i></span>
	  		${text}
  		</button>`;


  	return {
  		
  		Row: function(content) {
			return [{ content:content }].map(Row).join('');
		},

		Column: function(content, classes, colspan) {
			var additionalAttributes = '';
			if (classes) additionalAttributes += ' class=' + classes;
			if (colspan) additionalAttributes += ' colspan=' + colspan;
			return [{ content:content, additionalAttributes:additionalAttributes }].map(Column).join('');
		},

		Div: function(content, id, classes, additionalAttributes) {
			var realAdditionalAttributes = '';
			if (id) realAdditionalAttributes += ' id=' + id;
			if (classes) realAdditionalAttributes += ' class=' + classes;
			if (additionalAttributes) realAdditionalAttributes += ' ' + additionalAttributes;
			// noinspection JSConstructorReturnsPrimitive
			return [{ content:content, additionalAttributes:realAdditionalAttributes }].map(Div).join('');
  		},

		DetailsContainer: function(content) {
			// noinspection JSConstructorReturnsPrimitive
			return [{ content:content }].map(DetailsContainer).join('');
		},

		DetailsRowItem: function(title, content) {
			// noinspection JSConstructorReturnsPrimitive
			return [{ title: title, content:content }].map(DetailsRowItem).join('');
		},

    	ActionButton: function(text, icon, action, type, enabled) {
        	// noinspection JSConstructorReturnsPrimitive
			return [{ text:text, icon:icon, action:action, type:type, enabled:enabled ? '' : 'disabled' }].map(ActionButton).join('');
    	},

		CollapseButton: function(text, icon, dataTarget, ariaControl) {
			// noinspection JSConstructorReturnsPrimitive
        	return [{ text:text, icon:icon, dataTarget:dataTarget, ariaControl:ariaControl }].map(CollapseButton).join('');
    	},

		Bold: function(text) {
			// noinspection JSConstructorReturnsPrimitive
  			return [{text:text}].map(Bold).join('');
		}
	}
})();
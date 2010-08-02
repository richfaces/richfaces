/**
*  Client-side implementation of command script for commadd links, buttons etc.
*  
*/
A4J_Command = Class.create();
A4J_Command.prototype = {
	/**
	* Object is usually created on click event.
	*/
	initialize: function(e, params, target) {
		this.form = Event.findElement(e, 'form');
		this.target = target;
		this.objectsCreated = new Array();
		this.oldValuesOfExistingInputs = {};
		
		this.appendParameters(params);
		
		this.processClick();
		
		this.cleanUp();
		
	},
	/**
	* store form;'s target
	* append temporary parameter nodes
	* submit form
	*/
	processClick: function() {
		var form = this.form;
		var targ = form.target;
		
		$A(this.objectsCreated).each(
			function(node) {
				form.appendChild(node);
			}
		);
		
		if (this.target){
			form.target = this.target;
		}
		
		form.submit();
		
		form.target = targ;
		
	},
	/**
	iterate over set of parameters, setting values of hidden inputs
	*/
	appendParameters: function(params) {
		var dis = this;
		$H(params).each(
			function(parameter) {
				dis.createOrInitHiddenInput(parameter.key, parameter.value);
			}
		);
	},
	/**
	*	remove created hidden inputs, and restore values of previously existed
	*/
	cleanUp: function(){
		var form = this.form;
		
		$H(this.oldValuesOfExistingInputs).each(
			function(input){
				($(input.key) || form[input.key]).value = input.value;
			}
		
		);
		
		$A(this.objectsCreated).each(
			function(node) {
				form.removeChild(node);
			}
		);
		
		delete (this.objectsCreated);
		
	},
		
	/**
	*  init hidden inputs with parameter values, creating inputs if necessary
	*/
	createOrInitHiddenInput : function(name, value) {
		var hiddenObj = $(name) || this.form[name];
		
		if (!hiddenObj) {
			hiddenObj = document.createElement('input');
			hiddenObj.setAttribute('type', 'hidden');
			hiddenObj.setAttribute('name', name);
			hiddenObj.setAttribute('id', name);
			this.objectsCreated.push(hiddenObj);
		} else {
			this.oldValuesOfExistingInputs[name] = hiddenObj.value;
		}
		hiddenObj.value = value;
	}
};

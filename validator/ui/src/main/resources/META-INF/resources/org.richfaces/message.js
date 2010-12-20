(function($, rf) {

	rf.ui = rf.ui || {};
	
	// Constructor definition
	rf.ui.Base = function(componentId, options, defaultOptions) {
		this.namespace = "."+rf.Event.createNamespace(this.name, componentId);
		// call constructor of parent class
		$super.constructor.call(this, componentId);
		this.options = $.extend(this.options, defaultOptions, options);
		this.attachToDom();
		this.__bindEventHandlers();
	};

	// Extend component class and add protected methods from parent class to our container
	rf.BaseComponent.extend(rf.ui.Base);

	// define super class link
	var $super = rf.ui.Base.$super;

	$.extend(rf.ui.Base.prototype, {
		__bindEventHandlers: function () {
		},
		destroy: function () {
			rf.Event.unbindById(this.id, this.namespace);
			$super.destroy.call(this);
		}
	});
	
})(jQuery, window.RichFaces || (window.RichFaces={}));

(function($, rf) {
	
	// Constructor definition
	rf.ui.Message = function(componentId, options) {
		// call constructor of parent class
		$super.constructor.call(this, componentId, options, defaultOptions);
	};

	// Extend component class and add protected methods from parent class to our container
	rf.ui.Base.extend(rf.ui.Message);

	// define super class link
	var $super = rf.ui.Message.$super;

	var defaultOptions = {

	};
	
	var componentHash = {};
	var componentIndex = 0;
	
	var onMessage = function (event, element, data) {
		if (!this.options.forComponentId) {
			var index = componentHash[data.sourceId];
			if (typeof index != undefined) {
				$(rf.getDomElement(this.id+index)).remove();
			}
			var content = content = $(rf.getDomElement(this.id));

			componentIndex ++;
			if (data.message) content.append('<li id="'+this.id+componentIndex+'">'+data.message.summary+'</li>');
			componentHash[data.sourceId] = componentIndex;
			
		} else if (this.options.forComponentId==data.sourceId) {
			rf.getDomElement(this.id).innerHTML = data.message ? '<li>'+data.message.summary+'</li>' : '';
		}
	}

	$.extend(rf.ui.Message.prototype, {
		name: "Message",
		__bindEventHandlers: function () {
			rf.Event.bind(window.document, rf.Event.MESSAGE_EVENT_TYPE+this.namespace, onMessage, this);
		}
	});
	
})(jQuery, window.RichFaces || (window.RichFaces={}));
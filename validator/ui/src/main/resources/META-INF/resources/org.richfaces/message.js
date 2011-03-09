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
		showSummary:true,
		level:0,
		isMessages: false
	};
	
	var messageSeverityClasses = ["rf-msg-inf", "rf-msg-wrn", "rf-msg-err", "rf-msg-ftl"];
	
	var messagesSeverityClasses = ["rf-msgs-inf", "rf-msgs-wrn", "rf-msgs-err", "rf-msgs-ftl"];

	var messageSummaryClass = "rf-msg-sum";
	
	var messagesSummaryClass = "rf-msgs-sum";
	
	var messageDetailClass = "rf-msg-dtl";

	var messagesDetailClass = "rf-msgs-dtl";

	var onMessage = function (event, element, data) {
		var content = $(rf.getDomElement(this.id));
		var sourceId = data.sourceId;
		var message = data.message;
		if (!this.options.forComponentId) {
			if (!message) {
				// rf.csv.clearMessage
				$(rf.getDomElement(this.id+':'+sourceId)).remove();
			} else {
				renderMessage.call(this,sourceId,message);
			}
		} else if (this.options.forComponentId === sourceId) {
			content.empty();
			renderMessage.call(this,sourceId,message);
		}
	}
	
	var renderMessage = function(index,message){
		if(message && message.severity >= this.options.level){
			var isMessages = this.options.isMessages;
			
			var content = $(rf.getDomElement(this.id));
			var msgContent = "<span class='"+(isMessages ? messagesSeverityClasses : messageSeverityClasses)[message.severity]+"' id='"+this.id+':'+index+"'";
			if(message.summary){
				if(this.options.tooltip){
					msgContent = msgContent+" title='"+message.summary+"'>";
				} else if(this.options.showSummary ){
					msgContent = msgContent + "><span class='"+(isMessages ? messagesSummaryClass : messageSummaryClass)+"'>"+message.summary+"</span>";
				} else {
					msgContent = msgContent+">";
				}
			} else {
				msgContent = msgContent+">";
			}
			if(this.options.showDetail && message.detail){
				msgContent = msgContent + "<span class='"+(isMessages ? messagesDetailClass : messageDetailClass)+"'>"+message.detail+"</span>";
			}
			msgContent = msgContent+"</span>"
			content.append(msgContent);
		}
	}

	var bindEventHandlers = function () {
		rf.Event.bind(window.document, rf.Event.MESSAGE_EVENT_TYPE+this.namespace, onMessage, this);
	};
	
	$.extend(rf.ui.Message.prototype, {
		name: "Message",
		__bindEventHandlers: bindEventHandlers
	});
	
})(jQuery, window.RichFaces || (window.RichFaces={}));
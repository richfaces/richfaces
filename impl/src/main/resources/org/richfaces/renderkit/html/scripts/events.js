if (!window.Richfaces) {
	window.Richfaces = {};
}
Richfaces.SYNTHETIC_EVENT = "Richfaces.SYNTHETIC_EVENT";

Richfaces.createEvent = function (type, component, baseEvent, props) {
	var eventObj;
	
	if (document.createEventObject) {
		if (baseEvent) {
			eventObj = document.createEventObject(baseEvent);
		} else {
			eventObj = document.createEventObject();
		}
	} else {
		var bubbles = baseEvent && baseEvent.bubbles || false;
		var cancelable = baseEvent && baseEvent.cancelable || true;		
		
		switch (type) {
			case 'abort':
			case 'blur':
			case 'change':
			case 'error':
			case 'focus':
			case 'load':
			case 'reset':
			case 'resize':
			case 'scroll':
			case 'select':
			case 'submit':
			case 'unload':

				eventObj = document.createEvent('HTMLEvents');
				eventObj.initEvent(type, bubbles, cancelable);
			
			break;
			
			case 'DOMActivate':
			case 'DOMFocusIn':
			case 'DOMFocusOut': 
			case 'keydown':
			case 'keypress':
			case 'keyup':

				eventObj = document.createEvent('UIEvents');
				if (baseEvent) {
					eventObj.initUIEvent(type, bubbles, cancelable, baseEvent.windowObject, 
						baseEvent.detail);
				} else {
					eventObj.initEvent(type, bubbles, cancelable);
				}
			
			break;
	
			case 'click':
			case 'mousedown':
			case 'mousemove':
			case 'mouseout':
			case 'mouseover':
			case 'mouseup':
			
				eventObj = document.createEvent('MouseEvents');
				if (baseEvent) {
					eventObj.initMouseEvent(type, bubbles, cancelable, 
						baseEvent.windowObject, 
						baseEvent.detail, 
						baseEvent.screenX, 
						baseEvent.screenY, 
						baseEvent.clientX, 
						baseEvent.clientY, 
						baseEvent.ctrlKey, 
						baseEvent.altKey, 
						baseEvent.shiftKey, 
						baseEvent.metaKey, 
						baseEvent.button, 
						baseEvent.relatedTarget);
				} else {
					eventObj.initEvent(type, bubbles, cancelable) 
				}
			
			break;
			
			case 'DOMAttrModified':
			case 'DOMNodeInserted':
			case 'DOMNodeRemoved':
			case 'DOMCharacterDataModified':
			case 'DOMNodeInsertedIntoDocument':
			case 'DOMNodeRemovedFromDocument':
			case 'DOMSubtreeModified':
				
				eventObj = document.createEvent('MutationEvents');
				if (baseEvent) {
					eventObj.initMutationEvent(type, bubbles, cancelable, 
						baseEvent.relatedNode, 
						baseEvent.prevValue, 
						baseEvent.newValue, 
						baseEvent.attrName, 
						baseEvent.attrChange
						);
				} else {
					eventObj.initEvent(type, bubbles, cancelable) 
				}

			break;
			
			default:
				eventObj = document.createEvent('Events');
				eventObj.initEvent(type, bubbles, cancelable);
		}
	}

	if (props) {
		Object.extend(eventObj, props);
	}
 
	eventObj[Richfaces.SYNTHETIC_EVENT] = true;
			
	return {
		event: eventObj,
		
		fire: function() {
			if (component.fireEvent) {
				component.fireEvent("on" + type, this.event);
			} else {
				component.dispatchEvent(this.event);
			}
		},
		
		destroy: function() {
			if (props) {
				for (var name in props) {
					this.event[name] = undefined;
				}
			}
		}
	};
}

Richfaces.eventIsSynthetic = function (event) {
	if (event) {
		return new Boolean(event[Richfaces.SYNTHETIC_EVENT]).valueOf();
	}
	
	return false;
}
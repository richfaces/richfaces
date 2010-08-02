/**
 * @author Maksim Kaszynski
 * base class for draggable component.
 */
DnD.Draggable = function() {};

DnD.ieReleaseCapture = function() {
	if (document.releaseCapture) {
		document.releaseCapture();
	}
};

DnD.DragEndListener = Class.create();
DnD.DragEndListener.prototype = {
	
	initialize: function(callback) {
		this.callback = callback;
		this.onmoveBound = this.onmove.bindAsEventListener(this);
		this.onupBound = this.onup.bindAsEventListener(this);
	},
	
	activate: function(event) {		
		Event.observe(document, "mousemove", this.onmoveBound);
		Event.observe(document, "mouseup", this.onupBound);
		if (event.type == "mousemove") {
			this.onmoveBound(event);
		}
    	// prevent text selection in IE
    	this.onSelectStartHandler = document.onselectstart;
    	this.onDragStartHandler = document.ondragstart;

    	document.onselectstart = function () { return false; };
    	document.ondragstart = function () { DnD.ieReleaseCapture(); return false; };
	
		if (document.releaseCapture) {
    		Event.observe(document, "mousemove", DnD.ieReleaseCapture);
		}		
	},
	
	onmove: function(event) {
		if ("mousemove" == event.type) {
	    	if (!this.mouseMoveProvidesButtonChecked) {
				this.mouseMoveProvidesButtonChecked = true;
		    	if (!this.mouseMoveProvidesButton) {
			    	this.mouseMoveProvidesButton = event.button != 0;
		    	}
			}

			// 	The best over the world browser IE6 thinks that we stop holding
			// a left mouse button when cursor is over <select> elements. 
			// So we have to not stop dragging if Event.isLeftClick() returns false.
			if (this.mouseMoveProvidesButton && !Event.isLeftClick(event) &&
					RichFaces.getIEVersion() != 6) {
				this.endDrag(event);
			}
		}
	},
	
	onup: function(event) {
		this.endDrag(event);
	},
	
	endDrag: function(event) {
		this.deactivate();
		this.callback(event);
	},	
	
	deactivate: function() {
		Event.stopObserving(document, "mousemove", this.onmoveBound);
		Event.stopObserving(document, "mouseup", this.onupBound);
		
        document.onselectstart = this.onSelectStartHandler;
        document.ondragstart = this.onDragStartHandler;

    	if (document.releaseCapture) {
	    	Event.stopObserving(document, "mousemove", DnD.ieReleaseCapture);
    	}
	}

};

DnD.Cursor = Class.create();
DnD.Cursor.prototype = {
	
	initialize: function(element,cursor) {
		this.element = element;
		this.cursor = cursor;
		this.visible = false;
		if (this.element.style.cursor && this.element.style.cursor != "") {
			//save cursor if exist
			this.oldcursor = this.element.style.cursor;
		}	
	},
	
	showCursor: function() {
		var parent = this.element;
		this.element.style.cursor = this.cursor;
		this.visible = true;
	},
	
	hideCursor: function() {
		var parent = this.element;
		parent.style.cursor = "" ;
		this.visible = false;
		if (this.oldcursor) {
			//restore saved cursor
			this.element.style.cursor = this.oldcursor;
		} 
	}
}; 

DnD.Draggable.prototype = {

	getElement: function() {
		return $(this.id);
	},

	getDraggableOptions: function() {
		return null;
	},

	getDnDDefaultParams: function() {
		return DnD.getDnDDefaultParams(this.getElement());
	},

	getDnDDragParams: function() {
		return DnD.getDnDDragParams(this.getElement());
	},

	/**
	 * @return type of draggable content
	 */
	getContentType: function() {
		return null;
	},
	/**
	 * implementations are responsible for getting drag indicator
	 * @return DnD.Indicator
	 */
	getIndicator: function() {
		return null;
	},

	getOrCreateDefaultIndicator: function() {
        var dragDiv = $("_rfDefaultDragIndicator");
        if (!dragDiv) {        		
	   		dragDiv = document.createElement("div");
	        dragDiv.id = "_rfDefaultDragIndicatorLeft";
	        Element.setStyle(dragDiv, {"fontSize": "0px", "lineHeight": "0px", "zIndex": 1000});
	        document.body.appendChild(dragDiv);
	        
	        dragDiv = document.createElement("div");
	        dragDiv.id = "_rfDefaultDragIndicatorRight";
	        Element.setStyle(dragDiv, {"fontSize": "0px", "lineHeight": "0px", "zIndex": 1000});
	        document.body.appendChild(dragDiv);
	        
	        dragDiv = document.createElement("div");
	        dragDiv.id = "_rfDefaultDragIndicatorBottom";
	        Element.setStyle(dragDiv, {"fontSize": "0px", "lineHeight": "0px", "zIndex": 1000});
	        document.body.appendChild(dragDiv);
        	
            dragDiv = document.createElement("div");
            dragDiv.id = "_rfDefaultDragIndicator";
            Element.setStyle(dragDiv, {"fontSize": "0px", "lineHeight": "0px", "zIndex": 1000});
            Object.extend(dragDiv, DefaultDragIndicator);            	
            document.body.appendChild(dragDiv);
        }
        DefaultDragIndicator.changeIndicatorColor(dragDiv, "black");
        
        return dragDiv;
    },

    setIndicator: function(event) {
		var indicator = this.getIndicator();

		if (indicator) {
			var dndParams = this.getDnDDragParams();

			DnD.setDefaultDnDParams(dndParams);

            if (this.getDraggableItems && this.getDraggableItems() > 1) {
	            indicator.setContent("default", false, dndParams);
            } else {
	            indicator.setContent("default", true, dndParams);
            }
		}
	},

	moveDrag: function(event) {
		//TODO handle mouseover to update coords
		var x = Event.pointerX(event);
		var y = Event.pointerY(event);
		
		if (!window.drag && (Math.abs(this.lastDragX - x) + Math.abs(this.lastDragY - y)) > 2) {
			this.updateDrag(event);
		}
	},

	isDragEnabled: function() {
		return !!this.getContentType();
	},

	startDrag : function(event) {
		if (this.isDragEnabled()) {
			if(this.grabbingCursor) {
				if(this.grabCursor && this.grabCursor.visible) {
					this.grabCursor.hideCursor();
				}
				this.grabbingCursor.showCursor();		
			} 

			if (!this.endDragListener) {
				this.dragTrigger = this.moveDrag.bindAsEventListener(this);
			
				this.endDragListener = new DnD.DragEndListener(function(localEvent) {
	
					Event.stopObserving(document, "mousemove", this.dragTrigger);
					DnD.endDrag(localEvent, window.drag);
	
				}.bind(this));
			}
			
			this.endDragListener.activate(event);
			Event.observe(document, "mousemove", this.dragTrigger);

			this.lastDragX = Event.pointerX(event);
			this.lastDragY = Event.pointerY(event);		
		}
    },

	updateDrag : function(event) {
		var type = this.getContentType();
		var indicator = this.getIndicator();
		var drag = new DnD.Drag(this, indicator, type);
			
		if (indicator.id.indexOf("_rfDefaultDragIndicator") != -1) {			
			var target = drag.source.getElement();
			var offSets = Position.cumulativeOffset(target);
			indicator.indicatorWidth = Element.getWidth(target);
		    indicator.indicatorHeight = Element.getHeight(target);
			indicator.position(offSets[0], offSets[1]);
			indicator.removalX = Event.pointerX(event) - offSets[0];
			indicator.removalY = Event.pointerY(event) - offSets[1];			
		}
		
		DnD.startDrag(drag);
		DnD.updateDrag(event);
		this.ondragstart(event, drag);
		if (indicator) {			 
			indicator.show();
		}	
	
		var options = this.getDraggableOptions();
		if (options && options.ondragstart) {
			options.ondragstart(event);
		}
	
	        // cancel out any text selections
	        //document.body.focus();
	
    },
	/**
	 *
	 * @param {DnD.Drag} drag
	 */
	endDrag: function(event, drag) {
		DnD.endDrag(event);
		
		this.lastDragX = undefined;
		this.lastDragY = undefined;
		
		if (this.endDragListener) {
			this.endDragListener.deactivate();
		}

        if (drag) {
	        var indicator = drag.indicator;
			if (indicator) {
				indicator.hide();
			}
	
			this.ondragend(event, drag);
			
		}
		
		var grabbingCursor = this.getCurrentGrabbingCursor();
					
		if (grabbingCursor) {
			if (grabbingCursor.visible) {
				grabbingCursor.hideCursor();
			}	
		}
		
		var options = this.getDraggableOptions();
		if (options && options.ondragend) {
			options.ondragend(event);
		}
    },
     
    attachCursor: function () {
    	this.cursor = new DnD.Cursor(); 
    },
    
	/**
	 * cubclasses may define custom behavior
	 * @param {Object} drag
	 */
	ondragstart: function(event, drag) {
	
	},

	ondragend: function (event, drag) {

	},
	
	ondragover: function(event) {
		var grabCursor = this.getCurrentGrabCursor();
		if(!document.body.style.cursor) {
			if(grabCursor) {
				if(!grabCursor.visible) {
					grabCursor.showCursor();
				}	
			}
		}
	},
	
	ondragout: function(event) {
		var grabCursor = this.getCurrentGrabCursor();
		if (grabCursor) {
			if(grabCursor.visible) {
				grabCursor.hideCursor();
			}  
		}
	}, 
	
	getCurrentGrabbingCursor: function() {
		var drag = window.drag;
		var grabbingCursor = this.grabbingCursor;
		if(drag) {
			grabbingCursor = drag.source.grabbingCursor;
		} 
		return 	grabbingCursor;	
	},
	
	getCurrentGrabCursor: function() {
		var drag = window.drag;
		var grabCursor = this.grabCursor
		if(drag) {
			grabCursor = drag.source.grabCursor;
		} 
		return grabCursor;
	},
	
	onmouseup: function (event) {
		var grabbingCursor = this.getCurrentGrabbingCursor(); 
		var grabCursor = this.grabCursor;
		
		if(grabbingCursor && grabbingCursor.visible) {
			grabbingCursor.hideCursor();
		}
		
		if(grabCursor) {
			grabCursor.showCursor();
		}
	},
	
	ondropover: function(event, drag) {
		var options = this.getDraggableOptions();
		if (options && options.ondropover) {
			event.drag = drag;
			options.ondropover(event);
		}
	},
	
	ondropout: function(event,drag) {
		var options = this.getDraggableOptions();
		if (options && options.ondropout) {
			event.drag = drag;
			options.ondropout(event);
		}
	},
	
	enableDraggableCursors: function(grab,grabbing) {
			 
		var element = this.getElement();
				
		if (grab) {
			this.dragOutBound = this.ondragout.bindAsEventListener(this);
			this.dragOverBound = this.ondragover.bindAsEventListener(this);
			this.dragUpBound = this.onmouseup.bindAsEventListener(this);
				
			Event.observe(element, "mouseout", this.dragOutBound);
			Event.observe(element, "mouseover", this.dragOverBound);
			Event.observe(element, "mouseup", this.dragUpBound);
					
			this.grabCursor = new DnD.Cursor(element,grab);
		} 
		
		if (grabbing) {
			this.grabbingCursor = new DnD.Cursor(document.body,grabbing);
		}
		
	},
	
	disableDraggableCursors: function() {
		var element = this.getElement();
		if(this.dragOutBound && this.dragOverBound) {
			Event.stopObserving(element, "mouseover", this.dragOutBound);
			Event.stopObserving(element, "mouseout", this.dragOverBound);
		} else {
			return false;
		}
		return true;
	},
	
	isDraggableCursorsEnabled: function() {
		if (this.isCursorsEnabled) {
			this.isCursorsEnabled = true;
		} else {
			this.isCursorsEnabled = false;
		}
		return this.isCursorsEnabled;
	}
	

};

DefaultDragIndicator = {

    setContent: function(name, single, params) {
	},

	show: function() {
        if (window.drag && window.drag.source) {
            var target = window.drag.source.getElement();

            Element.setStyle(this, {"width": Element.getWidth(target) + "px", "height": "1px"});
            Element.show(this);
            this.style.position = 'absolute';
            
            var elt = $("_rfDefaultDragIndicatorLeft");
    		if (elt) {
    			Element.setStyle(elt, {"width": "1px", "height": Element.getHeight(target) + "px"});
    			Element.show(elt);
		           elt.style.position = 'absolute';
    		}
    		elt = $("_rfDefaultDragIndicatorRight");
    		if (elt) {
    			Element.setStyle(elt, {"width": "1px", "height": Element.getHeight(target) + "px"});
    			Element.show(elt);
		    	elt.style.position = 'absolute';
    		}    			
    		elt = $("_rfDefaultDragIndicatorBottom");
    		if (elt) {
    			Element.setStyle(elt, {"width": Element.getWidth(target) + "px", "height": "1px"});
    			Element.show(elt);
		        elt.style.position = 'absolute';    			
    		}            
        }
    },

	hide: function() {
		Element.hide(this);
		this.style.position = '';
		
		var elt = $("_rfDefaultDragIndicatorLeft");
    	if (elt) {
    		Element.hide(elt);
			elt.style.position = '';
    	}
    	elt = $("_rfDefaultDragIndicatorRight");
    	if (elt) {
    		Element.hide(elt);
			elt.style.position = '';
    	}    			
    	elt = $("_rfDefaultDragIndicatorBottom");
    	if (elt) {
    		Element.hide(elt);
			elt.style.position = '';
    	} 
	},

    position: function(x, y) {
   		if (this.removalX && this.removalY) {
				x -= (this.removalX + 5);
				y -= (this.removalY + 14);
		} 
        Element.setStyle(this, {"left": x + "px", "top": y + "px"});
        
        var elt = $("_rfDefaultDragIndicatorLeft");
    	if (elt) {    				
    		Element.setStyle(elt, {"left": x + "px", "top": y + "px"});
    	}
    	x += this.indicatorWidth;				
    	elt = $("_rfDefaultDragIndicatorRight");
    	if (elt) {
    		Element.setStyle(elt, {"left": x + "px", "top": y + "px"});
    	} 
    	x -= this.indicatorWidth;				
    	y += this.indicatorHeight;   			
    	elt = $("_rfDefaultDragIndicatorBottom");
    	if (elt) {
    		Element.setStyle(elt, {"left": x + "px", "top": y + "px"});
    	}
    },

    accept: function() {
    	this.changeIndicatorColor(this, "green");
    },

    reject: function() {
	    this.changeIndicatorColor(this, "red");
    },

    leave: function() {
    	this.changeIndicatorColor(this, "black");
    },
    
    changeIndicatorColor: function(indicator, color) {
    	Element.setStyle(indicator, {"borderTop" : "1px dashed "+color});
    			
		var elt = $("_rfDefaultDragIndicatorLeft");
		if (elt) {
			Element.setStyle(elt, {"borderLeft" : "1px dashed "+color});
		}
		elt = $("_rfDefaultDragIndicatorRight");
		if (elt) {
			Element.setStyle(elt, {"borderRight" : "1px dashed "+color});
		}
		elt = $("_rfDefaultDragIndicatorBottom");
		if (elt) {
			Element.setStyle(elt, {"borderBottom" : "1px dashed "+color});
		}
    }
};

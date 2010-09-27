(function ($, richfaces) {
    
    richfaces.ui = richfaces.ui || {};
    
	var initButtons = function(buttons, css, component) {
		var id;

		var fn = function(e) {
			e.data.fn.call(e.data.component, e);
		}
		
    	var data = {};
		data.component = component;

		for(id in buttons) {
			var element = $(document.getElementById(id));
        	
			data.id = id;
    		data.page = buttons[id];
    		data.element = element;
    		data.fn = component.processClick;

    		element.bind('click', copy(data), fn);
		
    		if(css) {
        		data.fn = component.processStyles;
        		data.css =  css.mousedown;
        		element.bind('mousedown', copy(data), fn);
        		
        		data.css =  css.mouseup;
        		element.bind('mouseup', copy(data), fn);
        		
        		data.css =  css.mouseout;
        		element.bind('mouseout', copy(data), fn);
        		
        		data.css = css.mouseover;
        		element.bind('mouseover', copy(data), fn);
    		}
    	}
	};
	
 	var copy = function(data) {
   		var key;
		var eventData = {};
		
		for (key in data) {
			eventData[key] = data[key];
		}
		
		return eventData;
	};
		
 	richfaces.ui.DataScroller =  function(id, submit, options) {
    	
    	$super.constructor.call(this,id);
    	
    	this.attachToDom(id);
    	
    	this.options = options; 
        this.currentPage = options.currentPage;
    	var buttons = options.buttons;
        var digitals = options.digitals;
        
		if (submit && typeof submit == 'function') {
			RichFaces.Event.bindById(id, this.getScrollEventName(), submit);
		}	
        
        var css = {};
        
        if(buttons) {
           	var leftButtons = buttons.left;
           	css.mouseover = "rf-ds-btn rf-ds-lft";
           	css.mouseup = "rf-ds-btn rf-ds-lft";
           	css.mouseout = "rf-ds-btn rf-ds-lft";
           	css.mousedown = "rf-ds-btn rf-ds-lft rf-ds-hov";
        	initButtons(leftButtons,css, this);
        	
        	var rightButtons = buttons.right;
        	css.mouseover = "rf-ds-btn rf-ds-rgh";
           	css.mouseup = "rf-ds-btn rf-ds-rgh";
           	css.mouseout = "rf-ds-btn rf-ds-rgh";
           	css.mousedown = "rf-ds-btn rf-ds-rgh rf-ds-hov";
        	initButtons(rightButtons,css, this);
        }
        
        if(digitals) {
        	css.mouseover= "rf-ds-nmb-btn rf-ds-hov";
           	css.mouseup= "rf-ds-nmb-btn rf-ds-hov";
           	css.mouseout = "rf-ds-nmb-btn";
           	css.mousedown="rf-ds-nmb-btn rf-ds-press";
           	initButtons(digitals, css,this);
        }
    };
    
    var $super = richfaces.BaseComponent.extend(richfaces.ui.DataScroller);
    var $p = richfaces.BaseComponent.extend(richfaces.ui.DataScroller, {});
	var $super = richfaces.ui.DataScroller.$super;
    
    $.extend(richfaces.ui.DataScroller.prototype, (function (options) {
   	
      	var scrollEventName = "rich:datascroller:onscroll";
    	           
        return {
        	
        	name: "RichFaces.ui.DataScroller",

        	processClick: function(event) {
        		var data = event.data;
        		if(data) {
        			var page = data.page;
        			if(page) {
        				this.switchToPage(page);
        			}
        		}
        	}, 
        	
        	processStyles: function(event) {
        		var data = event.data;
        		
        		if(data && (data.page != this.currentPage)) {
        			var element = data.element;
        			var css = data.css;
        		    		    			
        			if(element && css) {
        				element.attr('class', css);
        			}
        		}
        	},
        	
        	switchToPage: function(page) {
        		if (typeof page != 'undefined' && page != null) {
        			RichFaces.Event.fireById(this.id, this.getScrollEventName(), {'page' : page});
        		}	
        	},
        	
        	fastForward: function() {
        		this.switchToPage("fastforward");
        	}, 
        	
        	fastRewind: function() {
        		this.switchToPage("fastrewind");
        	}, 
        	
	    	next: function() {
	    		this.switchToPage("next");
	    	},
	    	
	    	previous: function() {
	    		this.switchToPage("previous");
	    	},
	    	
	    	first: function() {
	    		this.switchToPage("first");
	    	},
	    	
	    	last: function() {
	    		this.switchToPage("last");
	    	},
	    	
	    	getScrollEventName: function() {
	    		return scrollEventName;
	    	}
	    }
    
    })());

})(jQuery, window.RichFaces);
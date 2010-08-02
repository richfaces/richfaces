(function ($, richfaces) {
    
    richfaces.ui = richfaces.ui || {};
    var selectionEventHandler = function(event){
    	event.stopPropagation();
    	event.preventDefault();
    };

	var disableSelection = function (element)
	{
		if (typeof element.onselectstart!="undefined") //IE
		{
			jQuery(element).bind( 'selectstart', selectionEventHandler);
		}
		else //All other (ie: Opera)
		{
			jQuery(element).bind( 'mousedown', selectionEventHandler);
		}
	}

	var enableSelection = function (element)
	{
		if (typeof element.onselectstart!="undefined") //IE
		{
			jQuery(element).unbind( 'selectstart', selectionEventHandler);
		}
		else //All other (ie: Opera)
		{
			jQuery(element).unbind( 'mousedown', selectionEventHandler);
		}
	}
		
 	richfaces.ui.PopupPanel =  function(id, options) {
    	
    	$super.constructor.call(this,id);
    	this.markerId = id;
    	this.attachToDom(id);
    	id = "#" + id;
    	this.options = options; 

		this.id = $(id);
		this.minWidth = this.getMinimumSize(this.options.minWidth);
		this.minHeight = this.getMinimumSize(this.options.minHeight);
		this.maxWidth = this.options.maxWidth;
		this.maxHeight = this.options.maxHeight;
		this.options = options;

		this.baseZIndex = this.options.zindex ? this.options.zindex : 100;
		
		this.div = id;
		this.cdiv = id + "_container";
		this.contentDiv = id + "_content";
		this.shadowDiv = id + "_shadow";
		this.scrollerDiv = id + "_content_scroller"

		this.borders = new Array();
		this.firstHref = id + "FirstHref";
		this.lastHref = id + "LastHref";
		if (this.options.resizeable) {
			this.borders.push(new richfaces.ui.PopupPanel.Border(id + "ResizerN", this, "N-resize", richfaces.ui.PopupPanel.Sizer.N));
			this.borders.push(new richfaces.ui.PopupPanel.Border(id + "ResizerE", this, "E-resize", richfaces.ui.PopupPanel.Sizer.E));
			this.borders.push(new richfaces.ui.PopupPanel.Border(id + "ResizerS", this, "S-resize", richfaces.ui.PopupPanel.Sizer.S));
			this.borders.push(new richfaces.ui.PopupPanel.Border(id + "ResizerW", this, "W-resize", richfaces.ui.PopupPanel.Sizer.W));

			this.borders.push(new richfaces.ui.PopupPanel.Border(id + "ResizerNW", this, "NW-resize", richfaces.ui.PopupPanel.Sizer.NW));
			this.borders.push(new richfaces.ui.PopupPanel.Border(id + "ResizerNE", this, "NE-resize", richfaces.ui.PopupPanel.Sizer.NE));
			this.borders.push(new richfaces.ui.PopupPanel.Border(id + "ResizerSE", this, "SE-resize", richfaces.ui.PopupPanel.Sizer.SE));
			this.borders.push(new richfaces.ui.PopupPanel.Border(id + "ResizerSW", this, "SW-resize", richfaces.ui.PopupPanel.Sizer.SW));
		}

		if (this.options.moveable && $(id + "_header")) {
			this.header = new richfaces.ui.PopupPanel.Border(id + "_header", this, "move", richfaces.ui.PopupPanel.Sizer.Header);
		} else{
			$(id + "_header").css('cursor', 'default');
		}

    };
    
    var $super = richfaces.BaseComponent.extend(richfaces.ui.PopupPanel);
    var $p = richfaces.BaseComponent.extend(richfaces.ui.PopupPanel, {});
	var $super = richfaces.ui.PopupPanel.$super;
    $.extend(richfaces.ui.PopupPanel.prototype, (function (options) {
    	           
        return {
        	
        	name: "RichFaces.ui.PopupPanel",
			saveInputValues: function(element) {
				/* Fix for RF-3856 - Checkboxes in modal panel does not hold their states after modal was closed and opened again */
				if ($.browser.msie /* reproducible for checkbox/radio in IE6, radio in IE 7/8 beta 2 */) {
					$('input[type=checkbox], input[type=radio]', element).each(function(index) {
    					$(this).defaultChecked = $(this).checked;
  					});
				}
			},
	
			width: function() {
				return this.getContentElement()[0].clientWidth;//TODO
			},

			height: function() {
				return this.getContentElement()[0].clientHeight;//TODO
			},
			
			getLeft : function (){
				return $(this.cdiv).css('left');
			},
			
			getTop : function (){
				return $(this.cdiv).css('top');
			},
			
			getInitialSize : function(){
				if(this.options.autosized){
					return 15;
				} else{
					return $(this.div + "_header_content").height();
				}
			},
			
			getContentElement: function() {
				if (!this._contentElement) {
					this._contentElement =  $(this.cdiv);
				}

				return this._contentElement;
			},
			getSizeElement : function() {
				return document.body;
			},

			getMinimumSize : function(size) {
				return Math.max(size, 2*this.getInitialSize() + 2);
			},
			destroy: function() {
		 
				this._contentElement = null;
				this.firstOutside = null;
				this.lastOutside = null;
				this.firstHref = null;
        		this.parent = null;
        		if (this.header) {
        			this.header.destroy();
					this.header=null;        	
        		}

				for (var k = 0; k < this.borders.length; k++ ) {
					this.borders[k].destroy();
				}
				this.borders = null;

				if (this.domReattached) {
					var element = this.id;
					var parent = $(element).parent();
					if (parent) {
						parent.remove(element);
					}
				}
				this.markerId = null;
		    	this.options = null; 
		
				this.id = null;
				
				this.div  = null;
				this.cdiv = null;
				this.contentDiv = null;
				this.shadowDiv = null;
				this.scrollerDiv = null;
				this.userOptions = null;
				this.eIframe= null;
		
			},

			initIframe : function() {
        		if (this.contentWindow) {
					$(this.contentWindow.document.body).css("margin", "0px 0px 0px 0px");
				} else {
					//TODO opera etc.

				}

				if("transparent" == $(document.body).css("background-color")) {
					$(this).css('filter', "alpha(opacity=0)");
					$(this).css('opacity', "0");
				}
			},
	
			setLeft: function(pos) {
				if(!isNaN(pos)){
					$(this.cdiv).css('left', pos + "px");
					var depth = this.options.shadowDepth ? this.options.shadowDepth : 2;
					$(this.shadowDiv).css('left', pos + depth  + "px");
				}
			},

			setTop: function(pos) {
				if(!isNaN(pos)){
					$(this.cdiv).css('top', pos + "px");
					var depth = this.options.shadowDepth ? this.options.shadowDepth : 2;
					$(this.shadowDiv).css('top', pos + depth +"px");
				}
			},

			show: function(event, opts) {
				if(!this.shown && this.invokeEvent("beforeshow",event,null,element)) {
					this.preventFocus();
					var element = this.id;
			
	        		if (!this.domReattached) {
						this.parent = $(element).parent();
				
						var domElementAttachment;
						if (opts) {
							domElementAttachment = opts.domElementAttachment;
						} 
				
						if (!domElementAttachment) {
							domElementAttachment = this.options.domElementAttachment;
						}
				
						var newParent;
						if ('parent' == domElementAttachment) {
							newParent = this.parent;
						} else if ('form' == domElementAttachment) {
							newParent = this.findForm(element) || document.body;
						} else {
							//default - body
							newParent = document.body;
						}
				
						if (newParent != this.parent) {
							this.saveInputValues(element);
							element.insertBefore(newParent.firstChild);
							this.domReattached = true;
						} else {
							$(this.parent).show();
						}
					}
	
					var forms = $("form", element);
	
					if (this.options.keepVisualState && forms) {
						for (var i = 0; i < forms.length; i++) {
							var popup = this;
							$(forms[i]).bind( "submit", {popup:popup}, this.setStateInput); 
						}
					}
	
					
	
					var options = {};
					this.userOptions = {};
					if (!element.mpSet) {
						$.extend(options, this.options);
					}
	
					if (opts) {
						$.extend(options, opts);
						$.extend(this.userOptions, opts);
					}
			
					this.currentMinHeight = this.getMinimumSize((options.minHeight || options.minHeight == 0) ? options.minHeight : this.minHeight); 
					this.currentMinWidth = this.getMinimumSize((options.minWidth || options.minWidth == 0) ? options.minWidth : this.minWidth);
			
					var eContentElt = this.getContentElement();
	
					if (!this.options.autosized) {
						if (options.width && options.width == -1) 
							options.width = 300;
						if (options.height && options.height == -1) 
							options.height = 200;
					} else{
						//options.width = $(this.div+"_headerSpan").width() +20;
					}
				
					if (options.width && options.width != -1) {
						if (this.currentMinWidth > options.width) {
							options.width = this.currentMinWidth;
						}
						if (options.width > this.maxWidth) {
							options.width = this.maxWidth;
						}
						$(eContentElt).css('width', options.width + (/px/.test(options.width) ? '' : 'px'));
						$(this.shadowDiv).css('width', options.width + 4 + (/px/.test(options.width) ? '' : 'px'));
						$(this.scrollerDiv).css('width', options.width + (/px/.test(options.width) ? '' : 'px'));
						
						
					}
	
					if (options.height && options.height != -1) {
						if (this.currentMinHeight > options.height) {
							options.height = this.currentMinHeight;
						}
						if (options.height > this.maxHeight) {
							options.height = this.maxHeight;
						}
						$(eContentElt).css('height', options.height + (/px/.test(options.height) ? '' : 'px'));
						$(this.shadowDiv).css('height', options.height + 4 + (/px/.test(options.height) ? '' : 'px'));
						var headerHeight = $(this.div +"_header")[0] ? $(this.div +"_header")[0].clientHeight : 0;
						$(this.scrollerDiv).css('height', options.height - headerHeight + (/px/.test(options.height) ? '' : 'px'));
						
						
					}
					var eIframe;
					if (this.options.overlapEmbedObjects && !this.iframe) {
	                        		this.iframe = this.markerId + "IFrame";
	            		$("<iframe src=\"javascript:''\" frameborder=\"0\" scrolling=\"no\" id=\"" + this.iframe + "\" " +								
						"class=\"mp-iframe\" style=\"width:" +this.options.width + "px; height:" + this.options.height + "px;\">" +
						"</iframe>").insertBefore($(':first-child', $(this.cdiv))[0]);
				
						eIframe = jQuery("#"+this.iframe); 
	
						$(eIframe).bind('load', this.initIframe);
						this.eIframe = eIframe;
					}
					element.mpSet = true;
	
					var eDiv = $(this.div);
	
					if (options.left) {
						var _left;
						if (options.left != "auto") {
							_left = parseInt(options.left, 10);
						} else {
							var cw = this.getSizeElement().clientWidth;
				 			var _width = this.width(); 
							if (cw >= _width) {
					 			_left = (cw - _width) / 2;
							} else {
								_left = 0;
							}
						}
	
						this.setLeft(Math.round(_left));
					}
	
					if (options.top) {
						var _top;
						if (options.top != "auto") {
							_top = parseInt(options.top, 10);
						} else {
							var cw = this.getSizeElement().clientHeight;
							var _height = this.height();
							if (cw >= _height) {
								_top = (cw - _height) / 2;
							} else {
								_top = 0;
							}
						}
	
						this.setTop(Math.round(_top));
					}

					var opacity = options.shadowOpacity ? options.shadowOpacity : 0.1;
					$(this.shadowDiv).css('opacity', opacity);
					$(this.shadowDiv).css('filter ', 'alpha(opacity='+opacity*100 +');');
	    			$(element).css('visibility', '');
	    			$(element).css('display', 'block');
	    			var event = {};
	    			event.parameters = opts || {};
	    			this.shown = true;
	    			this.invokeEvent("show",event,null,element);
				}	
			},	
	
			startDrag: function(border) {
				//for (var k = 0; k < this.borders.length; k++ ) {
					//this.borders[k].hide();
				//}
				disableSelection(document.body);
			},
			firstOnfocus: function(event) {
		var e = $(event.data.popup.firstHref)
		if (e) {
			e.focus();
		}
	},

	
	formElements: "|a|input|select|button|textarea|",
	
	processAllFocusElements: function(root, callback) {
		var idx = -1;
		var tagName;
		
		if (root.focus && root.nodeType == 1 && (tagName = root.tagName) &&
			// Many not visible elements have focus method, we is had to avoid processing them.
			(idx = this.formElements.indexOf(tagName.toLowerCase())) != -1 &&
			this.formElements.charAt(idx - 1) === '|' && 
			this.formElements.charAt(idx + tagName.length) === '|' &&
			!root.disabled && root.type!="hidden") {
				callback.call(this, root);
		} else {
			if (root != this.id) {
				var child = root.firstChild;
				while (child) {
					if (!child.style || child.style.display != 'none') {
						this.processAllFocusElements(child, callback);
					}
					child = child.nextSibling;
				}
			}
		}
	},

	processTabindexes:	function(input) {
		if (!this.firstOutside) {
			this.firstOutside = input;
		}
		if (input.tabIndex && !input.prevTabIndex) {
			input.prevTabIndex = input.tabIndex;
		}
		input.tabIndex = undefined;
		if (input.accesskey  && !input.prevAccesskey) {
			input.prevAccesskey = input.accesskey;
		}
		input.accesskey = undefined;
	},

	restoreTabindexes:	function(input) {
		if (input.prevTabIndex) {
			input.tabIndex = input.prevTabIndex;
			input.prevTabIndex = undefined;
		}
		if (input.prevAccesskey) {
			input.accesskey = input.prevAccesskey;
			input.prevAccesskey = undefined;
		}
	},

	preventFocus:	function() {
		if(this.options.modal){
			this.processAllFocusElements(document, this.processTabindexes);
			var popup = this;
			if (this.firstOutside) {
			
				jQuery(this.firstOutside).bind("focus", {popup: popup}, this.firstOnfocus); 
			}
		}
	},

	restoreFocus: function() {
		if(this.options.modal){
			this.processAllFocusElements(document, this.restoreTabindexes);
		
			if (this.firstOutside) {
				jQuery(this.firstOutside).unbind("focus", this.firstOnfocus);
				this.firstOutside = null;
			}
		}
	},
	
			endDrag: function(border) {
				for (var k = 0; k < this.borders.length; k++ ) {
					this.borders[k].show();
					this.borders[k].doPosition();
				}
				enableSelection(document.body);
			},

			hide: function(event, opts) {
				var element = this.id;
				this.restoreFocus();
				if (this.shown && this.invokeEvent("beforehide",event,null,element)) {

					this.currentMinHeight = undefined; 
					this.currentMinWidth = undefined;			
	
					$(this.id).hide();
	
					if (this.parent) {
						if (this.domReattached) {
							this.saveInputValues(element);

							this.parent.append(element);

							this.domReattached = false;
						} else {
							$(this.parent).hide();
						}
					}
			
					var event = {};
					event.parameters = opts || {};
					if (this.options && this.options.onhide) {
						this.options.onhide(event);
					}
			
					var forms = $("form", element);
					if (this.options.keepVisualState && forms) {
						for (var i = 0; i < forms.length; i++) {
							$(forms[i]).unbind( "submit", this.setStateInput);
						}
					}
	
					this.shown = false;
			
				}
			},

			getStyle: function(elt, name) {
				return parseInt($(elt).css(name).replace("px", ""), 10);
			},
	
			doResizeOrMove: function(diff) {
				var vetoes = {};
				var shadowHash = {};
				var cssHash = {};
				var cssHashWH = {};
				var shadowHashWH = {};
				var contentHashWH = {};
				var scrollerHashWH = {};

				var vetoeChange = false;
				var newSize;
				var shadowDepth = this.options.shadowDepth? this.options.shadowDepth: 4;
				var scrollerHeight = 22;
				var scrollerWidth = 0;
				var eContentElt = this.getContentElement();
		
				newSize = this.getStyle(eContentElt, "width");

				var oldSize = newSize;
				newSize += diff.deltaWidth || 0;

				
				
				if (newSize >= this.currentMinWidth || this.options.autosized) {
					if (diff.deltaWidth) {
						cssHashWH.width = newSize + 'px';
						shadowHashWH.width = newSize + shadowDepth + 'px';
						contentHashWH.width = newSize - scrollerWidth + 'px';
						scrollerHashWH.width = newSize - scrollerWidth + 'px';
					}
				} else {
					if (diff.deltaWidth) {
						cssHashWH.width = this.currentMinWidth + 'px';
						shadowHashWH.width = this.currentMinWidth + shadowDepth + 'px';
						contentHashWH.width = this.currentMinWidth - scrollerWidth + 'px';
						scrollerHashWH.width = this.currentMinWidth - scrollerWidth + 'px';
						vetoes.vx = oldSize - this.currentMinWidth;
					}

					vetoes.x = true;
				}
				
				if (newSize >= this.options.maxWidth) {
					if (diff.deltaWidth) {
						cssHashWH.width = this.currentMaxWidth + 'px';
						shadowHashWH.width = this.currentMaxWidth + shadowDepth + 'px';
						contentHashWH.width = this.currentMaxWidth - scrollerWidth + 'px';
						scrollerHashWH.width = this.currentMaxWidth - scrollerWidth + 'px';
						vetoes.vx = oldSize - this.currentMaxWidth;
					}

					vetoes.x = true;
				}

				if (vetoes.vx && diff.deltaX) {
					diff.deltaX = -vetoes.vx;
				}
		
				var eCdiv = $(this.cdiv); 

				if (diff.deltaX && (vetoes.vx || !vetoes.x)) {
					if (vetoes.vx) {
						diff.deltaX = vetoes.vx;
					}
					var newPos;
			
					newPos = this.getStyle(eCdiv, "left");
					newPos += diff.deltaX;
					cssHash.left = newPos + 'px';
					
					shadowHash.left = newPos + shadowDepth + "px";
				}

				newSize = this.getStyle(eContentElt, "height")

				var oldSize = newSize;
				newSize += diff.deltaHeight || 0;

				if (newSize >= this.currentMinHeight || this.options.autosized) {
					if (diff.deltaHeight) {
						cssHashWH.height = newSize + 'px';
						shadowHashWH.height = newSize + shadowDepth + 'px';
						scrollerHashWH.height = newSize - scrollerHeight + 'px';
					}
				} else {
					if (diff.deltaHeight) {
						cssHashWH.height = this.currentMinHeight + 'px';
						shadowHashWH.height = this.currentMinHeight + shadowDepth + 'px';
						scrollerHashWH.height = this.currentMinHeight - scrollerHeight + 'px';
						vetoes.vy = oldSize - this.currentMinHeight;
					}

					vetoes.y = true;
				}
				
				if (newSize >= this.options.maxHeight) {
					if (diff.deltaHeight) {
						cssHashWH.height = this.currentMaxHeight + 'px';
						shadowHashWH.height = this.currentMaxHeight + shadowDepth + 'px';
						scrollerHashWH.height = this.currentMaxHeight - scrollerHeight + 'px';
						vetoes.vy = oldSize - this.currentMaxHeight;
					}

					vetoes.y = true;
				}

				if (vetoes.vy && diff.deltaY) {
					diff.deltaY = -vetoes.vy;
				}

				if (diff.deltaY && (vetoes.vy || !vetoes.y)) {
					if (vetoes.vy) {
						diff.deltaY = vetoes.vy;
					}

				}
				if (diff.deltaY && (vetoes.vy || !vetoes.y)) {
					if (vetoes.vy) {
						diff.deltaY = vetoes.vy;
					}
					var newPos;
			
					newPos = this.getStyle(eCdiv, "top");
					newPos += diff.deltaY;
					cssHash.top = newPos + 'px';
					shadowHash.top = newPos + shadowDepth + "px";
				}
				$(eContentElt).css(cssHashWH);
				$(this.scrollerDiv).css(scrollerHashWH);
				if(this.eIframe)$(this.eIframe).css(scrollerHashWH);
				$(this.shadowDiv).css(shadowHashWH);

				$(eCdiv).css(cssHash);
				$(this.shadowDiv).css(shadowHash);
				//if(this.eIframe)$(this.eIframe).css(cssHash);
				$.extend(this.userOptions, cssHash);
				$.extend(this.userOptions, cssHashWH);
				var w = this.width();
				var h = this.height();

				this.reductionData = null;

				if (w <= 2*this.getInitialSize()) {
					this.reductionData = {};
					this.reductionData.w = w;
				}

				if (h <= 2*this.getInitialSize()) {
					if (!this.reductionData) {
						this.reductionData = {};
					}

					this.reductionData.h = h;
				}

				if (this.header) {
					this.header.doPosition();
				}
		
				return vetoes;
			},
			
			setSize : function (width, height){
				var w = width - this.width() ;
				var h = height -this.height();
				var diff = new richfaces.ui.PopupPanel.Sizer.Diff(0,0, w, h);
				this.doResizeOrMove(diff);
			},
			
			moveTo : function (top, left){
				var shadowDepth = this.options.shadowDepth? this.options.shadowDepth: 4;
				$(this.cdiv).css('top', top);
				$(this.cdiv).css('left', left);
				$(this.shadowDiv).css('top', top + shadowDepth);
				$(this.shadowDiv).css('left', left + shadowDepth);
			},
			
			move : function (dx, dy){
				var diff = new richfaces.ui.PopupPanel.Sizer.Diff(dx,dy, 0, 0);
				this.doResizeOrMove(diff);
			},
			
			resize : function (dx, dy){
				var diff = new richfaces.ui.PopupPanel.Sizer.Diff(0,0, dx, dy);
				this.doResizeOrMove(diff);
			},

			findForm: function(elt) {
				var target = elt;
				while (target) {
					if (!target.tagName /* document node doesn't have tagName */ 
							|| target.tagName.toLowerCase() != "form") {
				
						target = $(target).parent();
					} else {
						break;
					}
				}
		
				return target;
			},
	
			setStateInput: function(event) {
					// Concret input but not entire form is a target element for onsubmit in FF
					var popup = event.data.popup;
					target = $(popup.findForm(event.currentTarget));
			
					var input = document.createElement("input");
					input.type = "hidden";
					input.id = popup.markerId + "OpenedState";
					input.name = popup.markerId + "OpenedState";
					input.value = popup.shown ? "true" : "false";
					target.append(input);

					$.each(popup.userOptions, function(key, value) {
	  					input = document.createElement("input");
						input.type = "hidden";
						input.id = popup.markerId + "StateOption_" + key;
						input.name = popup.markerId + "StateOption_" + key;
						input.value = value;
						target.append(input);
					});
			
					return true;
			},
	
			invokeEvent: function(eventName, event, value, element) {
	
				var eventFunction = this.options['on'+eventName];
				var result;

				if (eventFunction) {
					var eventObj;
					if (event) {
						eventObj = event;
					}
					else if(document.createEventObject) {
						eventObj = document.createEventObject();
					}
					else if( document.createEvent ) {
						eventObj = document.createEvent('Events');
						eventObj.initEvent( eventName, true, false );
					}
			
					eventObj.rich = {component:this};
					eventObj.rich.value = value;

					try	{
						result = eventFunction.call(element, eventObj);
					}
					catch (e) { LOG.warn("Exception: "+e.Message + "\n[on"+eventName + "]"); }
				}
		
				if (result!=false) {
					 result = true;
				}	 
				return result;
			}
			
        	
	    }
    
    })());
    $.extend(richfaces.ui.PopupPanel, {
    	
    	showPopupPanel : function (id, opts, event) {
	
			$(document).ready(richfaces.$(id).show());
		},

		hidePopupPanel : function (id, opts, event) {
			$(document).ready(richfaces.$(id).hide());
		}		
    });

})(jQuery, window.RichFaces);

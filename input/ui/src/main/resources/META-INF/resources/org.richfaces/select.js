//TODO anton -> Cache impl should be moved to the utils  

(function ($, rf) {
	rf.utils = rf.utils || {};

	rf.utils.Cache = function (key, items, values, useCache) {
		this.key = key.toLowerCase();
		this.cache = {}
		this.cache[this.key] = items || [];
		this.originalValues = typeof values == "function" ? values(items) : values || this.cache[this.key];
		this.values = processValues(this.originalValues);
		this.useCache = useCache || checkValuesPrefix.call(this);
	};
	
	var processValues = function (values) {
		var processedValues = [];
		for (var i = 0; i<values.length; i++) {
			processedValues.push(values[i].toLowerCase());
		}
		return processedValues;
	}
	
	var checkValuesPrefix = function () {
		var result = true;
		for (var i = 0; i<this.values.length; i++) {
			if (this.values[i].indexOf(this.key)!=0) {
				result = false; break;
			}
		}
		return result;
	}

	var getItems = function (key, filterFunction) {
		key = key.toLowerCase();
		var newCache = [];
		
		if (key.length < this.key.length) {
			return newCache;
		}

		if (this.cache[key]) {
			newCache = this.cache[key];
		} else {
			
			var useCustomFilterFunction = typeof filterFunction == "function";
			var itemsCache = this.cache[this.key];

			for (var i = 0; i<this.values.length; i++) {
				var value = this.values[i];
				
				if (useCustomFilterFunction && filterFunction(key, value)) {
					newCache.push(itemsCache[i]);
				} else {
					var p = value.indexOf(key);
					if (p == 0) {
						newCache.push(itemsCache[i]);
					}
				}
			}

			if ((!this.lastKey || key.indexOf(this.lastKey)!=0) && newCache.length > 0) {
				this.cache[key] = newCache;
				
				if (newCache.length==1) {
					this.lastKey = key;
				}
			}
		}

		return newCache;
	};
	
	var getItemValue = function (item) {
		return this.originalValues[this.cache[this.key].index(item)];
	};
	
	var isCached = function (key) {
		key = key.toLowerCase();
		return this.cache[key] || this.useCache && key.indexOf(this.key)==0;
	};

	$.extend(rf.utils.Cache.prototype, (function () {
		return  {
			getItems: getItems,
			getItemValue: getItemValue,
			isCached: isCached
		};
	})());
	

})(jQuery, RichFaces);

//-->

(function ($, rf) {
	
	rf.ui = rf.ui || {};
		
        rf.ui.Select =  function(id, options) {
        	this.id = id;
        	var mergedOptions = $.extend({}, defaultOptions, options);
           	mergedOptions['attachTo'] = id;
           	mergedOptions['scrollContainer'] = $(document.getElementById(id + "Items")).parent()[0];

           	$super.constructor.call(this, id, mergedOptions);

           	this.options = mergedOptions;
           	this.defaultLabel = mergedOptions.defaultLabel;
            var inputLabel = this.getValue() ;
            this.initialValue = (inputLabel != this.defaultLabel) ? inputLabel : "";
            this.selValueInput = $(document.getElementById(id+"selValue"));
            this.field = $(document.getElementById(id+"Field"));
            this.clientItems = mergedOptions.items;
            

            if(mergedOptions.showControl) {
        		this.btn = $(document.getElementById(id+"Button"));
        	   	this.btn.bind("click", $.proxy(this.__clickHandler, this));
        	   	this.btn.mousedown($.proxy(this, function(){
        	   		this.focusin = false;
        	   	}));
        	}
            
        	this.selectFirst = mergedOptions.selectFirst;
        	this.popupList = new rf.ui.PopupList((id+"List"), this, mergedOptions);
        	this.listElem =  $(document.getElementById(id+"List"));
        	this.listElem.bind("click", $.proxy(this.__onListClick, this));
    		this.items = this.popupList.__getItems();
        	this.enableManualInput = mergedOptions.enableManualInput; 

        	if (this.items.length>0 && this.enableManualInput) {
    			this.cache = new rf.utils.Cache("", this.items, getData, true);
    		}
    		this.changeDelay = mergedOptions.changeDelay; 
        };
        
    	rf.ui.InputBase.extend(rf.ui.Select);
    	var $super = rf.ui.Select.$super;
    	
    	var defaultOptions = {
    		defaultLabel: "",
    		selectFirst: true,
    		showControl: true,
    		enableManualInput: false, 
    		itemCss: "rf-sel-opt",
    		selectItemCss: "rf-sel-sel", 
    		listCss: "rf-sel-lst-cord",
    		changeDelay: 8
  		};
    	
    	var REGEXP_TRIM = /^[\n\s]*(.*)[\n\s]*$/;

		var getData = function (nodeList) {
			var data = [];
			nodeList.each(function () {;
				data.push($(this).text().replace(REGEXP_TRIM, "$1"));
			});
			return data;
		}
        
    	$.extend(rf.ui.Select.prototype, ( function () {
    		return{
    			name : "select", 
    			
    			__clickHandler: function(e) {
    				if(!this.popupList.isVisible()) {
						this.__updateItems();
    					this.showPopup();
    				} else {
    					this.hidePopup();
    				}
    				this.__setInputFocus();
           			window.clearTimeout(this.timeoutId);
    			}, 
    			
    			__focusHandler: function(e) {
    				if (!this.focused) {
	    				if(this.getValue() == this.defaultLabel) {
	    					this.setValue("");
	    				}
	    				this.focusValue = this.selValueInput.val();
	    				this.focused = true;
	    				this.invokeEvent.call(this, "focus", document.getElementById(this.id + 'Input'), e);
    				}
    			},
    			
    			__keydownHandler: function(e) {
    				var code; 

    				if(e.keyCode) {
    					code = e.keyCode;
    				} else if(e.which) {
    					code = e.which;
    				}
           			
    				var visible = this.popupList.isVisible();
    				
    				switch(code) {
    					case rf.KEYS.DOWN: 
    						e.preventDefault();
    						if(!visible) {
    							this.__updateItems();
    							this.showPopup();
    						} else {
    							this.popupList.__selectNext() ;
    						}	
    						break;
    	       				
    					case rf.KEYS.UP:
    						e.preventDefault();
    						if(visible) {
	    						this.popupList.__selectPrev();
    						}
    						break;
    	       				
    					case rf.KEYS.RETURN:
    						e.preventDefault();
    						if(visible) {
	    						this.popupList.__selectCurrent();
    						}
    						return false;
    						break;
    					
    					case rf.KEYS.TAB:
    						break;
    						
    					case rf.KEYS.ESC:
    						e.preventDefault();
    						if(visible) {
    							this.hidePopup();
    						}
    						break;

    					default:
    	    				var _this = this;
    	       				window.clearTimeout(this.changeTimerId);
    	       				this.changeTimerId = window.setTimeout(function(){_this.__onChangeValue(e);}, this.changeDelay);
    	       				break;
    				}
    			}, 
    			
    			__onChangeValue: function(e) {
    				this.popupList.__selectByIndex();
    				var newValue = this.getValue();
    				if(this.cache && this.cache.isCached(newValue)) {
    					this.__updateItems();
    					
    					var items = this.popupList.__getItems();
    					if(items.length != 0) {
    						this.field.removeClass("rf-sel-fld-err");
    					} else {
    						this.field.addClass("rf-sel-fld-err");
    					}
    					
    					if(!this.popupList.isVisible()) {
    						this.showPopup();
    					}
    				}	
    			},
    			
    			__blurHandler: function(e) {
    				this.timeoutId = window.setTimeout($.proxy(function(){
        					this.onblur(e); 
    				}, this), 200);
    			},
           		
           		__onListClick: function(e) {
           			window.clearTimeout(this.timeoutId);
           		},
           		
           		__updateItems: function() {
					var newValue = this.getValue();
					newValue = (newValue != this.defaultLabel) ? newValue : "";
					this.__updateItemsFromCache(newValue);

					if(this.selectFirst) {
						this.popupList.__selectByIndex(0);
					}
           		}, 
           		
    			__updateItemsFromCache: function(value) {
					if(this.items.length>0 && this.enableManualInput) {
						var newItems = this.cache.getItems(value);
						var items = $(newItems);
	           			this.popupList.__setItems(items);
						$(document.getElementById(this.id+"Items")).empty().append(items);
					}
    			},
    			
    			showPopup: function() {
    				this.popupList.show();
    			},
    			
    			hidePopup: function() {
    				this.popupList.hide();
    			},
           		
    			processItem: function(item) {
    				var key = $(item).attr("id");
    				var label;
    				$.each(this.clientItems, function() {
    					if(this.id == key) {
    						label = this.label;
    						return false;
    					}
    				});
    				this.setValue(label);
               		this.hidePopup();
               		this.__setInputFocus();
    			}, 
    			
    			onblur: function(e) {
    				this.hidePopup();
					var value = "";
					var label = this.defaultLabel;
					var inputLabel = this.getValue();
					if(inputLabel && inputLabel != "") {
						var items = this.cache.getItems(inputLabel);					
						if(items.length > 0) {
							var first = $(items[0]);
							$.each(this.clientItems, function() {
								if(this.id == first.attr("id")) {
									label = this.label;
									value = this.value;
									return false;
								}
							});
						} else {
    						this.field.removeClass("rf-sel-fld-err");
							
    						var prevValue =	this.selValueInput.val();
							if(prevValue && prevValue != "") {
								$.each(this.clientItems, function() {
									if(this.value == prevValue) {
										label = this.label;
										value = this.value
										return false;
									}
								});		
							} 
						}
					} 
					
					this.setValue(label);
					this.selValueInput.val(value);
					this.focused = false;
					this.invokeEvent.call(this,"blur", document.getElementById(this.id + 'Input'), e);
					if(this.focusValue != this.selValueInput.val() ) {
						this.invokeEvent.call(this, "change", document.getElementById(this.id + 'Input'), e);
					}
    			}
    		}
    	})());

})(jQuery, window.RichFaces);

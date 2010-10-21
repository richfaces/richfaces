//TODO:  to the utils? <--
(function (rf) {
	rf.KEYS = {
		BACKSPACE: 8,	
		TAB: 9,
		RETURN: 13,
		ESC: 27,
		PAGEUP: 33,
		PAGEDOWN: 34,
		LEFT: 37,
		UP: 38,
		RIGHT: 39,
		DOWN: 40,
		DEL: 46
	};
})(RichFaces);


//TODO: remove when cache will be moved to utils <-- 
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
				result = false;
				break;
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
        	$super.constructor.call(this, id);
        	this.id = id;
           	var mergedOptions = $.extend({}, defaultOptions, options);
           	mergedOptions['attachTo'] = id;
      
        	this.defaultLabel = mergedOptions.defaultLabel;
            var inputLabel = this.getValue() ;
            this.initialValue = (inputLabel != this.defaultLabel) ? inputLabel : "";
            
            this.selValueInput = $(document.getElementById(id+"selValue"));
            this.clientItems = mergedOptions.items;

            if(mergedOptions.showControl) {
        		this.btn = $(document.getElementById(id+"Button"));
            	this.btn.bind("click", $.proxy(this.__clickHandler, this));
        	}
        	this.selectFirst = mergedOptions.selectFirst;
        	this.popupList = new rf.ui.SelectList((id+"List"), this, mergedOptions);
        	
    		var items = this.popupList.__getItems();
        	var enableManualInput = mergedOptions.enableManualInput; 
    		if (items.length>0 && enableManualInput) {
    			this.cache = new rf.utils.Cache("", items, getData, true);
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
    				e.preventDefault();
    				if(!this.popupList.isVisible()) {
    					this.popupList.show();
        				this.__setInputFocus();
    				} else {
    					this.popupList.hide();
    				}
    			}, 
    			
    			__focusHandler: function(e) {
    				if(this.getValue() == this.defaultLabel) {
    					this.setValue("");
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
    							this.popupList.show();
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
    					
    					if(this.initialValue !=newValue) {
    						var newItems = this.cache.getItems(newValue);
    						var items = $(newItems);
    	           			this.popupList.__setItems(items);
    						$(document.getElementById(this.id+"Items")).empty().append(items);
    					}	
    					    	    				
    					if(!this.popupList.isVisible()) {
    						this.popupList.show();
    					}
    	           			
    					if(this.selectFirst) {
    						this.popupList.__selectByIndex(0);
    					}
    				}	
    			},
    			
    			__blurHandler: function(e) {
    				var target = $(e.originalEvent.explicitOriginalTarget);
    				if(!this.popupList.isPopupList(target)) {
    					var inputLabel = this.getValue();
    					if(!inputLabel || inputLabel == "") {
    						this.setValue(this.defaultLabel);
    					}
    					this.popupList.hide();
    					return true;
    				} 				
           			return false;
           		},
    			
    			processItem: function(item) {
    				var key = $(item).attr("id");
    				var value = this.getItemValue(key);
    				this.saveItemValue(value);
    				var label = this.getItemLabel(key);
    				this.setValue(label);
               		this.popupList.hide();
               		this.__setInputFocus();
    			}, 
    			
    			getItemValue: function(key) {
    				for(var i in this.clientItems) {
    					var item = this.clientItems[i];
    					if(item && item.id == key) {
    						return item.value;
    					}
    				}
    			}, 
    			
    			getItemLabel: function(key) {
    				for(var i in this.clientItems) {
    					var item = this.clientItems[i];
    					if(item && item.id == key) {
    						return item.label;
    					}
    				}
    			}, 
    			
    			saveItemValue: function(value) {
    				this.selValueInput.val(value);
    			}
    		}
    		
    	})());

})(jQuery, window.RichFaces);

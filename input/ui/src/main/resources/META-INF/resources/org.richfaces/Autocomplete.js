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

(function ($, rf) {

	/*
	 * TODO: add user's event handlers call from options
	 * TODO: add fire events
	 */
	
	rf.ui = rf.ui || {};
	// Constructor definition
	rf.ui.Autocomplete = function(componentId, fieldId, options) {
		this.namespace = "."+rf.Event.createNamespace(this.name, componentId);
		this.options = {};
		// call constructor of parent class
		$super.constructor.call(this, componentId, componentId+ID.SELECT, fieldId, options);
		this.attachToDom(componentId);
		this.options = $.extend(this.options, defaultOptions, options);
		this.value = this.__getSubValue();
		this.index = -1;
		this.isFirstAjax = true;
		updateTokenOptions.call(this);
		bindEventHandlers.call(this);
		updateItemsList.call(this, "");
	};

	// Extend component class and add protected methods from parent class to our container
	rf.ui.AutocompleteBase.extend(rf.ui.Autocomplete);

	// define super class link
	var $super = rf.ui.Autocomplete.$super;

	var defaultOptions = {
		itemClass:'rf-au-itm',
		selectedItemClass:'rf-au-itm-sel',
		subItemClass:'rf-au-opt',
		selectedSubItemClass:'rf-au-opt-sel',
		autofill:true,
		minChars:1,
		selectFirst:true,
		ajaxMode:true,
		lazyClientMode:false,
		isCachedAjax:true,
		tokens: "",
		attachToBody:true,
		filterFunction: undefined
	};

	var ID = {
		SELECT:'List',
		ITEMS:'Items',
		VALUE:'Value'
	};
	
	var REGEXP_TRIM = /^[\n\s]*(.*)[\n\s]*$/;
	var REGEXP_TOKEN_LEFT;
	var REGEXP_TOKEN_RIGHT;
	
	var getData = function (nodeList) {
		var data = [];
		nodeList.each(function () {;
			data.push($(this).text().replace(REGEXP_TRIM, "$1"));
		});
		return data;
	}
	
	var updateTokenOptions = function () {
		this.useTokens = (typeof this.options.tokens == "string" && this.options.tokens.length>0);
		if (this.useTokens) {
			var escapedTokens = this.options.tokens.split('').join("\\");
			REGEXP_TOKEN_LEFT = new RegExp('[^'+escapedTokens+']+$','i');
			REGEXP_TOKEN_RIGHT = new RegExp('['+escapedTokens+']','i');
			this.hasSpaceToken = this.options.tokens.indexOf(' ')!=-1;
		};
	};

	var bindEventHandlers = function () {
		rf.Event.bind(rf.getDomElement(this.id+ID.ITEMS).parentNode, "click"+this.namespace+" mouseover"+this.namespace, onMouseAction, this);
	};

	var onMouseAction = function(event) {
		var element = $(event.target).closest("."+this.options.itemClass, event.currentTarget).get(0);

		if (element) {
			if (event.type=="mouseover") {
				var index = this.items.index(element);
				selectItem.call(this, event, index);
			} else {
				this.__onEnter(event);
				rf.Selection.setCaretTo(rf.getDomElement(this.fieldId));
				this.hide(event);
			}
		}
	};

	var updateItemsList = function (value, fetchValues) {
		this.items = $(rf.getDomElement(this.id+ID.ITEMS)).find("."+this.options.itemClass);
		if (this.items.length>0) {
			this.cache = new rf.utils.Cache((this.options.ajaxMode ? value : ""), this.items, fetchValues || getData, !this.options.ajaxMode);
		}
	};

	var scrollToSelectedItem = function() {
        var offset = 0;
        this.items.slice(0, this.index).each(function() {
			offset += this.offsetHeight;
		});
        var parentContainer = $(rf.getDomElement(this.id+ID.ITEMS)).parent();
        if(offset < parentContainer.scrollTop()) {
        	parentContainer.scrollTop(offset);
        } else {
        	offset+=this.items.get(this.index).offsetHeight;
        	if(offset - parentContainer.scrollTop() > parentContainer.get(0).clientHeight) {
        		parentContainer.scrollTop(offset - parentContainer.innerHeight());
            }
        }
	};

	var autoFill = function (inputValue, value) {
		if( this.options.autofill && value.toLowerCase().indexOf(inputValue)==0) {
			var field = rf.getDomElement(this.fieldId);
			var start = rf.Selection.getStart(field);
			this.setInputValue(inputValue + value.substring(inputValue.length));
			var end = start+value.length - inputValue.length;
			rf.Selection.set(field, start, end);
		}
	};

	var callAjax = function(event, value, callback) {
		
		clearItems.call(this);
		
		rf.getDomElement(this.id+ID.VALUE).value = value;
		
		var _this = this;
		var _event = event;
		var ajaxSuccess = function (event) {
			updateItemsList.call(_this, _this.value, event.componentData && event.componentData[_this.id]);
			if (_this.options.lazyClientMode && _this.value.length!=0) {
				updateItemsFromCache.call(_this, _this.value);
			}
			if (_this.focused && _this.items.length!=0 && callback) {
				callback.call(_this, _event);
			}
			if (!callback && _this.isVisible && _this.options.selectFirst) {
				selectItem.call(_this, _event, 0);
			}
		}
		
		var ajaxError = function (event) {
			//alert("error");
		}
		
		this.isFirstAjax = false;
		//caution: JSF submits inputs with empty names causing "WARNING: Parameters: Invalid chunk ignored." in Tomcat log
		var params = {};
		params[this.id + ".ajax"] = "1";
		rf.ajax(this.id, event, {parameters: params, error: ajaxError, complete:ajaxSuccess});
	};
	
	var selectItem = function(event, index, isOffset) {
		if (this.items.length==0 || (!isOffset && this.index == index)) return;
	
		if (this.index!=-1) {
			var element = this.items.eq(this.index)
			if (element.removeClass(this.options.selectedItemClass).hasClass(this.options.subItemClass)){
				element.removeClass(this.options.selectedSubItemClass);
			}
		}

		if (index==undefined) {
			this.index = -1;
			return;
		}

		if (isOffset) {
			this.index += index;
			if ( this.index<0 ) {
				this.index = this.items.length - 1;
			} else if (this.index >= this.items.length) {
				this.index = 0;
			}
		} else {
			if (index<0) {
				index = 0;
			} else if (index>=this.items.length) {
				index = this.items.length - 1;
			}
			this.index = index;
		}
		var item = this.items.eq(this.index);
		if (item.addClass(this.options.selectedItemClass).hasClass(this.options.subItemClass)) {
			item.addClass(this.options.selectedSubItemClass);
		}
		scrollToSelectedItem.call(this);
		if (event &&
			event.which != rf.KEYS.BACKSPACE &&
			event.which != rf.KEYS.DEL &&
			event.which != rf.KEYS.LEFT &&
			event.which != rf.KEYS.RIGHT) {
				autoFill.call(this, this.value, getSelectedItemValue.call(this));
		}
	};
	
	var updateItemsFromCache = function (value) {
		var newItems = this.cache.getItems(value, this.options.filterFunction);
		this.items = $(newItems);
		//TODO: works only with simple markup, not with <tr>
		$(rf.getDomElement(this.id+ID.ITEMS)).empty().append(this.items);
		window.console && console.log && console.log("updateItemsFromCache");
	};
	
	var clearItems = function () {
		$(rf.getDomElement(this.id+ID.ITEMS)).removeData().empty();
		this.items = [];
	};
	
	var onChangeValue = function (event, value, callback) {
		selectItem.call(this, event);
		
		// value is undefined if called from AutocompleteBase onChange
		var subValue = (typeof value == "undefined") ? this.__getSubValue() : value;
		var oldValue = this.value;
		this.value = subValue;
		
		if ((this.options.isCachedAjax || !this.options.ajaxMode) &&
			this.cache && this.cache.isCached(subValue)) {
			if (oldValue!=subValue) {
				updateItemsFromCache.call(this, subValue);
			}
			if (this.items.length!=0 && callback) {
				callback.call(this, event);
			}
			if (event.which == rf.KEYS.RETURN || event.type == "click") {
				this.setInputValue(subValue);
			} else if (this.options.selectFirst) {
				selectItem.call(this, event, 0);
			}
		} else {
			if (event.which == rf.KEYS.RETURN || event.type == "click") {
				this.setInputValue(subValue);
			}
			if (subValue.length>=this.options.minChars) {
				if ((this.options.ajaxMode || this.options.lazyClientMode) && oldValue!=subValue) {
					callAjax.call(this, event, subValue, callback);
				}
			} else {
				if (this.options.ajaxMode) {
					clearItems.call(this);
				}
			}
		}

	};
	
	var getSelectedItemValue = function () {
		if ( this.index>=0) {
			var element = this.items.eq(this.index);
			return this.cache.getItemValue(element);
		}
		return undefined;
	};
	
	var getSubValue = function () {
		//TODO: add posibility to use space chars before and after tokens if space not a token char
		if (this.useTokens) {
			var field = rf.getDomElement(this.fieldId);
			var value = field.value;
			var cursorPosition = rf.Selection.getStart(field);
			var beforeCursorStr = value.substring(0, cursorPosition);
			var afterCursorStr = value.substring(cursorPosition);
			var r = REGEXP_TOKEN_LEFT.exec(beforeCursorStr);
			var result = "";
			if (r) {
				result = r[0];
			}
			r = afterCursorStr.search(REGEXP_TOKEN_RIGHT);
			if (r==-1) r = afterCursorStr.length;
			result += afterCursorStr.substring(0, r);

			return result;
		} else {
			return this.getInputValue();
		}
	};
	
	var updateInputValue = function (value) {
		var field = rf.getDomElement(this.fieldId);
		var inputValue = field.value;
		
		var cursorPosition = rf.Selection.getStart(field);
		var beforeCursorStr = inputValue.substring(0, cursorPosition);
		var afterCursorStr = inputValue.substring(cursorPosition);
		
		var pos = beforeCursorStr.search(REGEXP_TOKEN_LEFT);
		var startPos = pos!=-1 ? pos : beforeCursorStr.length;
		pos = afterCursorStr.search(REGEXP_TOKEN_RIGHT);
		var endPos = pos!=-1 ? pos : afterCursorStr.length;
		
		var beginNewValue = inputValue.substring(0, startPos) + value;
		cursorPosition = beginNewValue.length;
		field.value = beginNewValue + afterCursorStr.substring(endPos);
		field.focus();
		rf.Selection.setCaretTo(field, cursorPosition);
		return field.value;
	};
	
	/*
	 * Prototype definition
	 */
	$.extend(rf.ui.Autocomplete.prototype, (function () {
		return {
			/*
			 * public API functions
			 */
 			name:"Autocomplete",
 			/*
 			 * Protected methods
 			 */
 			__updateState: function (event) {
				var subValue = this.__getSubValue();
				// called from AutocompleteBase when not actually value changed
				if (this.items.length==0 && this.isFirstAjax) {
					if ((this.options.ajaxMode && subValue.length>=this.options.minChars) || this.options.lazyClientMode) {
						callAjax.call(this, event, subValue);
					}
				}
				return;
			},
 			__getSubValue: getSubValue,
 			__updateInputValue: function (value) {
				if (this.useTokens) {
					return updateInputValue.call(this, value);
				} else {
					return $super.__updateInputValue.call(this, value);
				}
 			},
 			__onChangeValue: onChangeValue,
 			/*
 			 * Override abstract protected methods
 			 */
 			__onKeyUp: function (event) {
 				selectItem.call(this, event, -1, true);
 			},
 			__onKeyDown: function (event) {
 				selectItem.call(this, event, 1, true);
 			},
 			__onPageUp: function (event) {

 			},
 			__onPageDown: function (event) {

 			},
 			__onBeforeShow: function (event) {
 			},
 			__onEnter: function (event) {
 				var value = getSelectedItemValue.call(this);
 				this.__onChangeValue(event, value);
 				this.invokeEvent("selectitem", rf.getDomElement(this.fieldId), event, value);
 			},
 			__onShow: function (event) {
 				if (this.options.selectFirst) {
 					selectItem.call(this, event, 0);
				}
 			},
 			__onHide: function (event) {
 				selectItem.call(this, event);
 			},
 			/*
 			 * Destructor
 			 */
 			destroy: function () {
				//TODO: add all unbind
				this.items = null;
				this.cache = null;
				var itemsContainer = rf.getDomElement(this.id+ID.ITEMS);
				$(itemsContainer).removeData();
				rf.Event.unbind(itemsContainer.parentNode, this.namespace);
				$super.destroy.call(this);
 			}
		};
	})());
})(jQuery, RichFaces);
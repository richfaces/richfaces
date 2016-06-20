(function ($, rf) {

    /*
     * TODO: add user's event handlers call from options
     * TODO: add fire events
     */

    rf.ui = rf.ui || {};
    /**
     * Backing object for rich:autocomplete
     * 
     * @extends RichFaces.ui.AutocompleteBase
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.Autocomplete
     * 
     * @param componentId {string} component id
     * @param fieldId {string} id of the input box
     * @param options {Object} autocomplete options
     */
    // Constructor definition
    rf.ui.Autocomplete = function(componentId, fieldId, options) {
        this.namespace = "." + rf.Event.createNamespace(this.name, componentId);
        this.options = {};
        // call constructor of parent class
        $super.constructor.call(this, componentId, componentId + ID.SELECT, fieldId, options);
        this.attachToDom();
        this.options = $.extend(this.options, defaultOptions, options);
        this.value = "";
        this.index = null;
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
        //nothingLabel = "Nothing";
    };

    var ID = {
        SELECT:'List',
        ITEMS:'Items',
        VALUE:'Value'
    };

    var REGEXP_TRIM = /^[\n\s]*(.*)[\n\s]*$/;

    var getData = function (nodeList) {
        var data = [];
        nodeList.each(function () {
            data.push($(this).text().replace(REGEXP_TRIM, "$1"));
        });
        return data;
    }

    var updateTokenOptions = function () {
        this.useTokens = (typeof this.options.tokens == "string" && this.options.tokens.length > 0);
        if (this.useTokens) {
            var escapedTokens = this.options.tokens.split('').join("\\");
            this.REGEXP_TOKEN_RIGHT = new RegExp('[' + escapedTokens + ']', 'i');
            this.getLastTokenIndex = function(value) {
                return RichFaces.ui.Autocomplete.__getLastTokenIndex(escapedTokens, value);
            }
        }
    };

    var bindEventHandlers = function () {
        var list = $(rf.getDomElement(this.id + ID.ITEMS).parentNode);
        list.on("click" + this.namespace, "."+this.options.itemClass, $.proxy(onMouseClick, this));
        // The mouseenter event is available only natively in Internet Explorer, however jQuery emulates it in other browsers
        list.on("mouseenter" + this.namespace, "."+this.options.itemClass, $.proxy(onMouseEnter, this));
    };

    var onMouseEnter = function(event) {
        var element = $(event.target);

        if (element && !element.hasClass(this.options.itemClass)) {
            element = element.parents("." + this.options.itemClass).get(0);
        }

        if (element) {
            var index = this.items.index(element);
            selectItem.call(this, event, index);
        }
    };

    var onMouseClick = function(event) {
        var element = $(event.target);

        if (element && !element.hasClass(this.options.itemClass)) {
            element = element.parents("." + this.options.itemClass).get(0);
        }

        if (element) {
            this.__onEnter(event);
            rf.Selection.setCaretTo(rf.getDomElement(this.fieldId));
            this.__hide(event);
        }
    };

    var updateItemsList = function (value, fetchValues) {
        var itemsContainer = $(rf.getDomElement(this.id + ID.ITEMS));
        this.items = itemsContainer.find("." + this.options.itemClass);
        var componentData = itemsContainer.data("componentData");
        itemsContainer.removeData("componentData");
        if (this.items.length > 0) {
            this.cache = new rf.utils.Cache((this.options.ajaxMode ? value : ""), this.items, fetchValues || componentData || getData, !this.options.ajaxMode);
        }
    };

    var scrollToSelectedItem = function() {
        var offset = 0;
        this.items.slice(0, this.index).each(function() {
            offset += this.offsetHeight;
        });
        var parentContainer = $(rf.getDomElement(this.id + ID.ITEMS)).parent();
        if (offset < parentContainer.scrollTop()) {
            parentContainer.scrollTop(offset);
        } else {
            offset += this.items.eq(this.index).outerHeight();
            if (offset - parentContainer.scrollTop() > parentContainer.innerHeight()) {
                parentContainer.scrollTop(offset - parentContainer.innerHeight());
            }
        }
    };

    var autoFill = function (inputValue, value) {
        if (this.options.autofill && value.toLowerCase().indexOf(inputValue.toLowerCase()) == 0) {
            var field = rf.getDomElement(this.fieldId);
            var start = rf.Selection.getStart(field);
            this.__setInputValue(inputValue + value.substring(inputValue.length));
            var end = start + value.length - inputValue.length;
            rf.Selection.set(field, start, end);
        }
    };

    var callAjax = function(event, callback) {

        rf.getDomElement(this.id + ID.VALUE).value = this.value;

        var _this = this;
        var _event = event;
        var ajaxSuccess = function (event) {
            if (_this.options.minChars <= _this.value.length) {
                updateItemsList.call(_this, _this.value, event.componentData && event.componentData[_this.id]);
            }
            if (_this.options.lazyClientMode && _this.value.length != 0) {
                updateItemsFromCache.call(_this, _this.value);
            }
            if (_this.items.length != 0) {
                if (callback) {
                    (_this.focused || _this.isMouseDown) && callback.call(_this, _event);
                } else {
                    _this.isVisible && _this.options.selectFirst && selectItem.call(_this, _event, 0);
                }
            } else {
                _this.__hide(_event);
            }
        };

        var ajaxError = function (event) {
            _this.__hide(_event);
            clearItems.call(_this);
        };

        this.isFirstAjax = false;
        //caution: JSF submits inputs with empty names causing "WARNING: Parameters: Invalid chunk ignored." in Tomcat log
        var params = {};
        params[this.id + ".ajax"] = "1";
        var opts = {
            parameters: params,
            error: ajaxError,
            complete:ajaxSuccess,
            queueId: _this.options.queueId,
            begin: _this.options.onbegin,
            status: _this.options.status
        };
        rf.ajax(this.id, event, opts);
    };

    var clearSelection = function () {
        if (this.index != null) {
            var element = this.items.eq(this.index);
            if (element.removeClass(this.options.selectedItemClass).hasClass(this.options.subItemClass)) {
                element.removeClass(this.options.selectedSubItemClass);
            }
            this.index = null;
        }
    };

    var selectItem = function(event, index, isOffset) {
        if (this.items.length == 0 || (!isOffset && index == this.index)) return;

        if (index == null || index == undefined) {
            clearSelection.call(this);
            return;
        }

        if (isOffset) {
            if (this.index == null) {
                index = 0;
            } else {
                index = this.index + index;
            }
        }
        if (index < 0) {
            index = 0;
        } else if (index >= this.items.length) {
            index = this.items.length - 1;
        }
        if (index == this.index) return;

        clearSelection.call(this);
        this.index = index;

        var item = this.items.eq(this.index);
        if (item.addClass(this.options.selectedItemClass).hasClass(this.options.subItemClass)) {
            item.addClass(this.options.selectedSubItemClass);
        }
        scrollToSelectedItem.call(this);
        if (event &&
            event.keyCode != rf.KEYS.BACKSPACE &&
            event.keyCode != rf.KEYS.DEL &&
            event.keyCode != rf.KEYS.LEFT &&
            event.keyCode != rf.KEYS.RIGHT) {
            autoFill.call(this, this.value, getSelectedItemValue.call(this));
        }
    };

    var updateItemsFromCache = function (value) {
        var newItems = this.cache.getItems(value, this.options.filterFunction);
        this.items = $(newItems);
        //TODO: works only with simple markup, not with <tr>
        $(rf.getDomElement(this.id + ID.ITEMS)).empty().append(this.items);
    };

    var clearItems = function () {
        $(rf.getDomElement(this.id + ID.ITEMS)).removeData().empty();
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
            if (oldValue != subValue) {
                updateItemsFromCache.call(this, subValue);
            }
            if (this.items.length != 0) {
                callback && callback.call(this, event);
            } else {
                this.__hide(event);
            }
            if (event.keyCode == rf.KEYS.RETURN || event.type == "click") {
                this.__setInputValue(subValue);
            } else if (this.options.selectFirst) {
                selectItem.call(this, event, 0);
            }
        } else {
            if (event.keyCode == rf.KEYS.RETURN || event.type == "click") {
                this.__setInputValue(subValue);
            }
            if (subValue.length >= this.options.minChars) {
                if ((this.options.ajaxMode || this.options.lazyClientMode) && (oldValue != subValue || (oldValue === '' && subValue === ''))) {
                    callAjax.call(this, event, callback);
                }
            } else {
                if (this.options.ajaxMode) {
                    clearItems.call(this);
                    this.__hide(event);
                }
            }
        }

    };

    var getSelectedItemValue = function () {
        if (this.index != null) {
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
            var result = beforeCursorStr.substring(this.getLastTokenIndex(beforeCursorStr));
            r = afterCursorStr.search(this.REGEXP_TOKEN_RIGHT);
            if (r == -1) r = afterCursorStr.length;
            result += afterCursorStr.substring(0, r);

            return result;
        } else {
            return this.getValue();
        }
    };
    
    var getCursorPosition = function(field) {
        var pos = rf.Selection.getStart(field);
        if (pos <= 0) {
            // when cursorPosition is not determined (input is not focused),
            // use position of last token occurence) 
            pos = this.getLastTokenIndex(field.value);
        }
        return pos;
    }

    var updateInputValue = function (value) {
        var field = rf.getDomElement(this.fieldId);
        var inputValue = field.value;

        var cursorPosition = this.__getCursorPosition(field);
        var beforeCursorStr = inputValue.substring(0, cursorPosition);
        var afterCursorStr = inputValue.substring(cursorPosition);
        var pos = this.getLastTokenIndex(beforeCursorStr);
        var startPos = pos != -1 ? pos : beforeCursorStr.length;
        pos = afterCursorStr.search(this.REGEXP_TOKEN_RIGHT);
        var endPos = pos != -1 ? pos : afterCursorStr.length;

        var beginNewValue = inputValue.substring(0, startPos) + value;
        cursorPosition = beginNewValue.length;
        field.value = beginNewValue + afterCursorStr.substring(endPos);
        field.focus();
        rf.Selection.setCaretTo(field, cursorPosition);
        return field.value;
    };

    var getPageLastItem = function() {
        if (this.items.length == 0) return -1;
        var parentContainer = $(rf.getDomElement(this.id + ID.ITEMS)).parent();
        var h = parentContainer.scrollTop() + parentContainer.innerHeight() + this.items[0].offsetTop;
        var item;
        var i = (this.index != null && this.items[this.index].offsetTop <= h) ? this.index : 0;
        for (i; i < this.items.length; i++) {
            item = this.items[i];
            if (item.offsetTop + item.offsetHeight > h) {
                i--;
                break;
            }
        }
        if (i != this.items.length - 1 && i == this.index) {
            h += this.items[i].offsetTop - parentContainer.scrollTop();
            for (++i; i < this.items.length; i++) {
                item = this.items[i];
                if (item.offsetTop + item.offsetHeight > h) {
                    break;
                }
            }
        }
        return i;
    };

    var getPageFirstItem = function() {
        if (this.items.length == 0) return -1;
        var parentContainer = $(rf.getDomElement(this.id + ID.ITEMS)).parent();
        var h = parentContainer.scrollTop() + this.items[0].offsetTop;
        var item;
        var i = (this.index != null && this.items[this.index].offsetTop >= h) ? this.index - 1 : this.items.length - 1;
        for (i; i >= 0; i--) {
            item = this.items[i];
            if (item.offsetTop < h) {
                i++;
                break;
            }
        }
        if (i != 0 && i == this.index) {
            h = this.items[i].offsetTop - parentContainer.innerHeight();
            if (h < this.items[0].offsetTop) h = this.items[0].offsetTop;
            for (--i; i >= 0; i--) {
                item = this.items[i];
                if (item.offsetTop < h) {
                    i++;
                    break;
                }
            }
        }
        return i;
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
                if (this.items.length == 0 && this.isFirstAjax) {
                    if ((this.options.ajaxMode && subValue.length >= this.options.minChars) || this.options.lazyClientMode) {
                        this.value = subValue;
                        callAjax.call(this, event, this.__show);
                        return true;
                    }
                }
                return false;
            },
            __getSubValue: getSubValue,
            __getCursorPosition: getCursorPosition,
            __updateInputValue: function (value) {
                if (this.useTokens) {
                    return updateInputValue.call(this, value);
                } else {
                    return $super.__updateInputValue.call(this, value);
                }
            },
            __setInputValue: function (value) {
                this.currentValue = this.__updateInputValue(value);
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
                selectItem.call(this, event, getPageFirstItem.call(this));
            },
            __onPageDown: function (event) {
                selectItem.call(this, event, getPageLastItem.call(this));
            },
            __onKeyHome: function (event) {
                selectItem.call(this, event, 0);
            },
            __onKeyEnd: function (event) {
                selectItem.call(this, event, this.items.length - 1);
            },
            __onBeforeShow: function (event) {
            },
            __onEnter: function (event) {
                var value = getSelectedItemValue.call(this);
                this.__onChangeValue(event, value);
                this.invokeEvent("selectitem", rf.getDomElement(this.id), event, value);
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
                var itemsContainer = rf.getDomElement(this.id + ID.ITEMS);
                $(itemsContainer).removeData();
                rf.Event.unbind(itemsContainer.parentNode, this.namespace);
                this.__conceal();
                $super.destroy.call(this);
            }
        };
    })());

    $.extend(rf.ui.Autocomplete, {
            setData: function (id, data) {
                $(rf.getDomElement(id)).data("componentData", data);
            },
            
            __getLastTokenIndex:  function (tokens, value) {
                var LAST_TOKEN_OCCURENCE = new RegExp("[" + tokens + "][^" + tokens + "]*$", "i");
                var AFTER_LAST_TOKEN_WITH_SPACES = new RegExp("[^" + tokens + " ]", "i");
                
                var value = value || "";

                var lastTokenIndex = value.search(LAST_TOKEN_OCCURENCE);
                if (lastTokenIndex < 0) {
                    return 0;
                }
                var beforeToken = value.substring(lastTokenIndex);
                var afterLastTokenIndex = beforeToken.search(AFTER_LAST_TOKEN_WITH_SPACES);
                if (afterLastTokenIndex <= 0) {
                    afterLastTokenIndex = beforeToken.length;
                }
            
                return lastTokenIndex + afterLastTokenIndex;
            }
        });

})(RichFaces.jQuery, RichFaces);
(function ($, rf) {

    rf.ui = rf.ui || {};

    /**
     * Backing object of the rich:select
     * 
     * @extends RichFaces.ui.InputBase
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.Select
     * 
     * @param id
     * @param options
     */
    rf.ui.Select = function(id, options) {
        this.id = id;
        this.element = this.attachToDom();
        var mergedOptions = $.extend({}, defaultOptions, options);
        mergedOptions['attachTo'] = id;
        mergedOptions['scrollContainer'] = $(document.getElementById(id + "Items")).parent()[0];
        mergedOptions['focusKeeperEnabled'] = false;
        $super.constructor.call(this, id, mergedOptions);

        this.options = mergedOptions;
        this.defaultLabel = mergedOptions.defaultLabel;
        var inputLabel = this.__getValue();
        this.initialValue = (inputLabel != this.defaultLabel) ? inputLabel : "";
        this.selValueInput = $(document.getElementById(id + "selValue"));
        this.container = this.selValueInput.parent();
        this.clientSelectItems = mergedOptions.clientSelectItems;
        this.filterFunction = mergedOptions.filterFunction;


        if (mergedOptions.showControl && !mergedOptions.disabled) {
            this.container.bind("mousedown", $.proxy(this.__onBtnMouseDown, this))
                .bind("mouseup", $.proxy(this.__onMouseUp, this));
        }

        this.isFirstAjax = true;
        this.previousValue = this.__getValue();
        this.selectFirst = mergedOptions.selectFirst;
        this.popupList = new rf.ui.PopupList((id + "List"), this, mergedOptions);
        this.list = this.popupList.__getList();
        this.listElem = $(document.getElementById(id + "List"));

        this.listElem.bind("mousedown", $.proxy(this.__onListMouseDown, this));
        this.listElem.bind("mouseup", $.proxy(this.__onMouseUp, this));

        var listEventHandlers = {};
        listEventHandlers["listshow" + this.namespace] = $.proxy(this.__listshowHandler, this);
        listEventHandlers["listhide" + this.namespace] = $.proxy(this.__listhideHandler, this);
        listEventHandlers["change" + this.namespace] = $.proxy(this.__onInputChangeHandler, this);
        rf.Event.bind(this.element, listEventHandlers, this);

        this.originalItems = this.list.__getItems();  // initialize here for non-autocomplete use cases
        this.enableManualInput = mergedOptions.enableManualInput || mergedOptions.isAutocomplete;

        if (this.enableManualInput) {
            updateItemsList.call(this, "", this.clientSelectItems);
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
        changeDelay: 8,
        disabled: false,
        filterFunction : undefined,
        isAutocomplete: false,
        ajaxMode:true,
        lazyClientMode:false,
        isCachedAjax:true
    };

    var REGEXP_TRIM = /^[\n\s]*(.*)[\n\s]*$/;

    var updateItemsList = function (value, clientSelectItems) {
        if (!clientSelectItems) {
            clientSelectItems = [];
        }
        if (clientSelectItems.length || (!this.options.isAutocomplete && !this.options.isCachedAjax)) {
            // do no empty if autocomplete/caching is on
            this.clientSelectItems = clientSelectItems;
        }

        this.originalItems = this.list.__updateItemsList();
        this.list.__storeClientSelectItems(clientSelectItems);
        if (this.originalItems.length > 0) {
            this.cache = new rf.utils.Cache((this.options.ajaxMode ? value : ""), this.originalItems, getData, !this.options.ajaxMode);
        }

    };

    var getData = function (nodeList) {
        var data = [];
        nodeList.each(function () {
            data.push($(this).text().replace(REGEXP_TRIM, "$1"));
        });
        return data;
    }

    $.extend(rf.ui.Select.prototype, ( function () {
        return{
            name : "select",
            defaultLabelClass : "rf-sel-dflt-lbl",

            __listshowHandler: function(e) {
                if (this.originalItems.length == 0 && this.isFirstAjax) {
                    this.callAjax(e);
                }
            },

            __listhideHandler: function(e) {
            },

            __onInputChangeHandler: function(e) {
                this.__setValue(this.input.val());
            },

            __onBtnMouseDown: function(e) {
                if (!this.popupList.isVisible() && !this.options.isAutocomplete) {
                    this.__updateItems();
                    this.__showPopup();
                } else {
                    this.__hidePopup();
                }
                this.isMouseDown = true;
            },

            __focusHandler: function(e) {
                if (!this.focused) {
                    if (this.__getValue() == this.defaultLabel) {
                        this.__setValue("");
                    }
                    this.focusValue = this.selValueInput.val();
                    this.focused = true;
                    this.invokeEvent.call(this, "focus", document.getElementById(this.id), e);
                }
            },

            __keydownHandler: function(e) {
                var code;

                if (e.keyCode) {
                    code = e.keyCode;
                } else if (e.which) {
                    code = e.which;
                }

                var visible = this.popupList.isVisible();

                switch (code) {
                    case rf.KEYS.DOWN:
                        e.preventDefault();
                        e.stopPropagation();
                        if (!visible) {
                            this.__updateItems();
                            this.__showPopup();
                        } else {
                            this.list.__selectNext();
                        }
                        break;

                    case rf.KEYS.UP:
                        e.preventDefault();
                        e.stopPropagation();
                        if (visible) {
                            this.list.__selectPrev();
                        }
                        break;

                    case rf.KEYS.TAB:
                    case rf.KEYS.RETURN:
                        if (code == rf.KEYS.TAB && !visible) {
                            break;
                        }
                        e.preventDefault();
                        if (visible) {
                            this.list.__selectCurrent();
                        }
                        return false;
                        break;

                    case rf.KEYS.ESC:
                        e.preventDefault();
                        if (visible) {
                            this.__hidePopup();
                        }
                        break;

                    default:
                        if (this.__selectItemByLabel(code)) {
                            break;
                        }
                        var _this = this;
                        window.clearTimeout(this.changeTimerId);
                        this.changeTimerId = window.setTimeout(function() {
                            _this.__onChangeValue(e);
                        }, this.changeDelay);
                        break;
                }
            },

            __onChangeValue: function(e) {
                var newValue = this.__getValue();
                if (newValue === this.previousValue) {
                    return;
                }
                this.previousValue = newValue;
                if (!this.options.isAutocomplete ||
                    (this.options.isCachedAjax || !this.options.ajaxMode) && this.cache && this.cache.isCached(newValue)) {
                    this.__updateItems();
                    if (this.isAutocomplete) {
                        this.originalItems = this.list.__getItems();
                    }

                    if (this.list.__getItems().length != 0) {
                        this.container.removeClass("rf-sel-fld-err");
                    } else {
                        this.container.addClass("rf-sel-fld-err");
                    }

                    if (!this.popupList.isVisible()) {
                        this.__showPopup();
                    }
                } else {
                    if (newValue.length >= this.options.minChars) {
                        if ((this.options.ajaxMode || this.options.lazyClientMode)) {
                            this.callAjax(e);
                        }
                    } else {
                        if (this.options.ajaxMode) {
                            this.clearItems();
                            this.__hidePopup();
                        }
                    }
                }
            },

            clearItems: function() {
                this.list.removeAllItems();
            },

            callAjax: function(event) {
                var _this = this;
                var _event = event;
                var ajaxSuccess = function (event) {
                    updateItemsList.call(_this, _this.__getValue(), event.componentData && event.componentData[_this.id]);

                    if (_this.clientSelectItems && _this.clientSelectItems.length) {
                        _this.__updateItems();
                        _this.__showPopup();
                    } else {
                        _this.__hidePopup();
                    }
                };

                var ajaxError = function (event) {
                    _this.__hidePopup();
                    _this.clearItems();
                };

                this.isFirstAjax = false;
                //caution: JSF submits inputs with empty names causing "WARNING: Parameters: Invalid chunk ignored." in Tomcat log
                var params = {};
                params[this.id + ".ajax"] = "1";
                var opts = {
                    parameters: params,
                    error: ajaxError,
                    complete:ajaxSuccess,
                    begin: _this.options.onbegin,
                    status: _this.options.status
                };
                rf.ajax(this.id, event, opts);

            },

            __blurHandler: function(e) {
                if (!this.isMouseDown) {
                    var that = this;
                    this.timeoutId = window.setTimeout(function() {
                        if (that.input !== null) {
                            that.onblur(e);
                        }
                    }, 200);
                } else {
                    this.__setInputFocus();
                    this.isMouseDown = false;
                }
            },

            __onListMouseDown: function(e) {
                this.isMouseDown = true;
            },

            __onMouseUp: function(e) {
                this.isMouseDown = false;
                this.__setInputFocus();
            },

            __updateItems: function() {
                var newValue = this.__getValue();
                newValue = (newValue != this.defaultLabel) ? newValue : "";
                this.__updateItemsFromCache(newValue);

                if (this.selectFirst && this.enableManualInput && !this.__isValueSelected(newValue)) {
                    this.list.__selectByIndex(0);
                }
                
                if (!this.options.enableManualInput || this.__isValueSelected(this.getLabel())) {
                    if (this.originalItems.length > this.popupList.list.items.length) {
                        this.popupList.list.__unselectPrevious();
                        this.popupList.list.__setItems(this.originalItems);
                        $(document.getElementById(this.id + "Items")).children().detach();
                        $(document.getElementById(this.id + "Items")).append(this.originalItems);
                    }
                    this.list.__selectItemByValue(this.getValue());
                }
            },

            __updateItemsFromCache: function(value) {
                if (this.originalItems.length > 0 && (this.enableManualInput || this.isAutocomplete)
                    && !this.__isValueSelected(value)) {
                    var newItems = this.cache.getItems(value, this.filterFunction);
                    var items = $(newItems);
                    this.list.__unselectPrevious();
                    this.list.__setItems(items);
                    $(document.getElementById(this.id + "Items")).children().detach();
                    $(document.getElementById(this.id + "Items")).append(items);
                }
            },

            __getClientItemFromCache: function(inputLabel) {
                var empty = {'label': '', 'value': ''};
                if (!this.cache) {
                    return empty;
                }
                var value;
                var label;
                if (this.enableManualInput) {
                    var items = this.cache.getItems(inputLabel, this.filterFunction);
                    if (items && items.length > 0) {
                        var cachedItems = [];
                        for (var i = 0; i < items.length; i++) {
                            currItem = items[i];
                            $.each(this.clientSelectItems, function() {
                                if (this.id == currItem.id) {
                                    cachedItems.push({label: this.label, value: this.value});
                                }
                            });
                        }
                        return cachedItems;
                    } else {
                        label = inputLabel;
                        value = "";
                    }
                }

                if (label) {
                    return {'label': label, 'value': value};
                }

                return empty;
            },

            __getClientItem: function(inputLabel) {
                var value;
                var label = inputLabel;
                $.each(this.clientSelectItems, function() {
                    if (label == this.label) {
                        value = this.value;
                    }
                });

                if (label && value) {
                    return {'label': label, 'value': value};
                }
            },

            __isValueSelected: function(label) {
                this.lastSearched = this.lastSearched || {};
                var current = {
                        label: label,
                        value: this.getValue()
                    };

                if (current.label === this.lastSearched.label
                        && current.value == this.lastSearched.value) {
                    return true;
                }

                var item = this.__getClientItemFromCache(label);
                if (item.label && item.label === current.label && item.value == current.value && current.value != "") {
                    this.lastSearched = current;
                    return true;
                }
                if (item.length) {
                    for (var i = 0; i < item.length; i++) {
                        if (item[i].label === current.label && item[i].value == current.value) {
                            this.lastSearched = current;
                            return true;
                        }
                    }
                }
                return false;
            },

            __selectItemByLabel: function(code) {
                // only 0-9 and a-z
                if (this.enableManualInput || code < 48 || (code > 57 && code < 65) || code > 90) {
                    return false;
                }

                if (!this.popupList.isVisible()) {
                    this.__updateItems();
                    this.__showPopup();
                }

                var matchingItemIndexes = new Array();

                $.each(this.clientSelectItems, function(index) {
                    if(this.label[0].toUpperCase().charCodeAt(0) == code) {
                        matchingItemIndexes.push( index );
                    }
                });

                if (matchingItemIndexes.length)
                {
                    var keyCodeCount = 0;
                    if (this.lastKeyCode && this.lastKeyCode == code)
                    {
                        keyCodeCount = this.lastKeyCodeCount + 1;
                        if (keyCodeCount >= matchingItemIndexes.length)
                        {
                            keyCodeCount = 0;
                        }
                    }
                    this.lastKeyCode = code;
                    this.lastKeyCodeCount = keyCodeCount;

                    this.list.__selectByIndex( matchingItemIndexes[keyCodeCount] );

                }

                return false;
            },

            __showPopup: function() {
                if (this.originalItems.length > 0) {
                    this.popupList.show();
                    if (!this.options.enableManualInput || this.__isValueSelected(this.getLabel())) {
                        this.list.__selectItemByValue(this.getValue());
                    }
                }
                this.invokeEvent.call(this, "listshow", document.getElementById(this.id));
            },

            __hidePopup: function() {
                this.popupList.hide();
                this.invokeEvent.call(this, "listhide", document.getElementById(this.id));
            },

            /**
             * Show the popup list of options
             * 
             * @method
             * @name RichFaces.ui.Select#showPopup
             */
            showPopup: function() {
                if (!this.popupList.isVisible()) {
                    this.__updateItems();
                    this.__showPopup();
                }
                this.__setInputFocus();
                if (!this.focused) {
                    if (this.__getValue() == this.defaultLabel) {
                        this.__setValue("");
                    }
                    this.focusValue = this.selValueInput.val();
                    this.focused = true;
                    this.invokeEvent.call(this, "focus", document.getElementById(this.id));
                }
            },

            /**
             * Hide the popup list
             * 
             * @method
             * @name RichFaces.ui.Select#hidePopup
             */
            hidePopup: function() {
                if (this.popupList.isVisible()) {
                    this.__hidePopup();
                    var inputLabel = this.__getValue();

                    if (!inputLabel || inputLabel == "") {
                        this.__setValue(this.defaultLabel);
                        this.selValueInput.val("");
                    }

                    this.focused = false;
                    this.invokeEvent.call(this, "blur", document.getElementById(this.id));
                    if (this.focusValue != this.selValueInput.val()) {
                        this.invokeEvent.call(this, "change", document.getElementById(this.id));
                    }
                }
            },

            processItem: function(item) {
                var key = $(item).attr("id");
                var label, value;
                $.each(this.clientSelectItems, function() {
                    if (this.id == key) {
                        label = this.label;
                        value = this.value;
                        return false;
                    }
                });
                this.__setValue(label);
                this.previousValue = label;
                this.selValueInput.val(value);
                this.__hidePopup();
                this.__setInputFocus();

                this.invokeEvent.call(this, "selectitem", document.getElementById(this.id));
            },

            onblur: function(e) {
                this.__hidePopup();
                var inputLabel = this.__getValue();

                if (!inputLabel || inputLabel == "") {
                    this.__setValue(this.defaultLabel);
                    this.selValueInput.val("");
                }

                this.focused = false;
                this.invokeEvent.call(this, "blur", document.getElementById(this.id), e);
                if (this.focusValue != this.selValueInput.val()) {
                    this.invokeEvent.call(this, "change", document.getElementById(this.id), e);
                }
            },

            /**
             * Get the current value
             * 
             * @method
             * @name RichFaces.ui.Select#getValue
             * @return {string} current value
             */
            getValue: function() {
                return this.selValueInput.val();
            },

            /**
             * Set new value
             * 
             * @method
             * @name RichFaces.ui.Select#setValue
             * @param value {string} new value
             */
            setValue: function(value) {
                if (value == null || value == '') {
                    this.__setValue('');
                    this.selValueInput.val('');
                    this.__updateItems();
                    return;
                }
                var item;
                for (var i = 0; i < this.clientSelectItems.length; i++) {
                    item = this.clientSelectItems[i];
                    if (item.value == value && !item.disabled) {
                        this.__setValue(item.label);
                        this.selValueInput.val(item.value);
                        this.list.__selectByIndex(i);
                        return;
                    }
                }
            },

            /**
             * Get the value of the default label
             * 
             * @method
             * @name RichFaces.ui.Select#getLabel
             */
            getLabel: function() {
                return this.__getValue();
            },

            destroy: function() {
                this.popupList.destroy();
                this.popupList = null;
                $super.destroy.call(this);
            }
        }
    })());

    // client-side validation
    rf.csv = rf.csv || {};
    rf.csv.validateSelectLabelValue = function (input, id, params, msg) {
        var value = $(document.getElementById(id + 'selValue')).val();
        var label = $(document.getElementById(id + 'Input')).val();

        var defaultLabel = RichFaces.component(id).defaultLabel;

        if (!value && label && (label != defaultLabel)) {
            throw rf.csv.getMessage(null, null, [id, ""], 'UISELECTONE_INVALID');
        }
    };

})(RichFaces.jQuery, window.RichFaces);

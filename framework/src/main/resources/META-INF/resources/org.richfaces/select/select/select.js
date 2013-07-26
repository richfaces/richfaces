/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.Select = function(id, options) {
        this.id = id;
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
        rf.Event.bind(this.input, listEventHandlers, this);

        this.originalItems = this.list.__getItems();
        this.enableManualInput = mergedOptions.enableManualInput;

        if (this.originalItems.length > 0 && this.enableManualInput) {
            this.cache = new rf.utils.Cache("", this.originalItems, getData, true);
        }
        this.changeDelay = mergedOptions.changeDelay;
        
        if (this.defaultLabel == "") {
            var firstItem = this.clientSelectItems[0];
            var key = $(firstItem).attr("id");
            var label;
            $.each(this.clientSelectItems, function() {
                if (this.id == key) {
                    label = this.label;
                    return false;
                }
            });
            this.__setValue(label);
            this.__save();
        }
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
        filterFunction : undefined
    };

    var REGEXP_TRIM = /^[\n\s]*(.*)[\n\s]*$/;

    var getData = function (nodeList) {
        var data = [];
        nodeList.each(function () {
            ;
            data.push($(this).text().replace(REGEXP_TRIM, "$1"));
        });
        return data;
    }

    $.extend(rf.ui.Select.prototype, ( function () {
        return{
            name : "select",
            defaultLabelClass : "rf-sel-dflt-lbl",

            __listshowHandler: function(e) {
            },

            __listhideHandler: function(e) {
            },
            
            __onInputChangeHandler: function(e) {
                this.__setValue(this.input.val());
            },

            __onBtnMouseDown: function(e) {
                if (!this.popupList.isVisible()) {
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
                        if (!visible) {
                            this.__updateItems();
                            this.__showPopup();
                        } else {
                            this.list.__selectNext();
                        }
                        break;

                    case rf.KEYS.UP:
                        e.preventDefault();
                        if (visible) {
                            this.list.__selectPrev();
                        }
                        break;

                    case rf.KEYS.RETURN:
                        e.preventDefault();
                        if (visible) {
                            this.list.__selectCurrent();
                        }
                        return false;
                        break;

                    case rf.KEYS.TAB:
                        break;

                    case rf.KEYS.ESC:
                        e.preventDefault();
                        if (visible) {
                            this.__hidePopup();
                        }
                        break;

                    default:
                        var _this = this;
                        window.clearTimeout(this.changeTimerId);
                        this.changeTimerId = window.setTimeout(function() {
                            _this.__onChangeValue(e);
                        }, this.changeDelay);
                        break;
                }
            },

            __onChangeValue: function(e) {
                this.list.__selectByIndex();
                var newValue = this.__getValue();
                if (this.cache && this.cache.isCached(newValue)) {
                    this.__updateItems();

                    if (this.list.__getItems().length != 0) {
                        this.container.removeClass("rf-sel-fld-err");
                    } else {
                        this.container.addClass("rf-sel-fld-err");
                    }

                    if (!this.popupList.isVisible()) {
                        this.__showPopup();
                    }
                }
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

                if (this.selectFirst) {
                    this.list.__selectByIndex(0);
                }
            },

            __updateItemsFromCache: function(value) {
                if (this.originalItems.length > 0 && this.enableManualInput) {
                    var newItems = this.cache.getItems(value, this.filterFunction);
                    var items = $(newItems);
                    this.list.__setItems(items);
                    $(document.getElementById(this.id + "Items")).empty().append(items);
                }
            },

            __getClientItemFromCache: function(inputLabel) {
    			var value;
				var label;
				if (this.enableManualInput) {
					var items = this.cache.getItems(inputLabel, this.filterFunction);
					if (items && items.length > 0) {
					    var first = $(items[0]);
                        $.each(this.clientSelectItems, function() {
                            if (this.label == inputLabel) {
                                label = this.label;
                                value = this.value;
                                return false;
                            }
                        });
                    
                    } else {
                        this.container.removeClass("rf-sel-fld-err");
                    
                        var prevValue = this.selValueInput.val();
                        if (prevValue && prevValue != "") {
                            $.each(this.clientSelectItems, function() {
                                if (this.value == prevValue) {
                                    label = this.label;
                                    value = this.value;
                                    return false;
                                }
                            });
                        }
                    }
                }
            
                if (label && value) {
                    return {'label': label,'value': value};
                }
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

            __showPopup: function() {
                this.popupList.show();
                this.invokeEvent.call(this, "listshow", document.getElementById(this.id));
            },

            __hidePopup: function() {
                this.popupList.hide();
                this.invokeEvent.call(this, "listhide", document.getElementById(this.id));
            },

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
                var label;
                $.each(this.clientSelectItems, function() {
                    if (this.id == key) {
                        label = this.label;
                        return false;
                    }
                });
                this.__setValue(label);
                this.__hidePopup();
                this.__setInputFocus();
                this.__save();

                this.invokeEvent.call(this, "selectitem", document.getElementById(this.id));
            },

            __save: function() {
                var value = "";
                var label = "";
                var inputLabel = this.__getValue();
                var clientSelectItem;

                if (inputLabel && inputLabel != "") {
                    if (this.enableManualInput) {
                        clientSelectItem = this.__getClientItemFromCache(inputLabel);
                    } else {
                        clientSelectItem = this.__getClientItem(inputLabel);
                    }

                    if (clientSelectItem) {
                        label = clientSelectItem.label;
                        value = clientSelectItem.value;
                    }
                }

                this.__setValue(label);
                this.selValueInput.val(value);
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

            getValue: function() {
                return this.selValueInput.val();
            },

            setValue: function(value) {
                if (value == null || value == '') {
                    this.__setValue('');
                    this.__save();
                    this.__updateItems();
                    return;
                }
                var item;
                for (var i = 0; i < this.clientSelectItems.length; i++) {
                    item = this.clientSelectItems[i];
                    if (item.value == value) {
                        this.__setValue(item.label);
                        this.__save();
                        this.list.__selectByIndex(i);
                        return;
                    }
                }
            },

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
        
        var defaultLabel = RichFaces.$(id).defaultLabel;
        
        if (!value && label && (label != defaultLabel)) {
            throw rf.csv.getMessage(null, 'UISELECTONE_INVALID', [id, ""]);
        }
    };

})(jQuery, window.RichFaces);

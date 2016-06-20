(function ($, rf) {

    rf.ui = rf.ui || {};

    /**
     * @extends RichFaces.BaseComponent
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.List
     * 
     * @param id
     * @param options
     */
    rf.ui.List = function(id, options) {
        $super.constructor.call(this, id);
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
        this.attachToDom();
        var mergedOptions = $.extend({}, defaultOptions, options);
        this.list = $(document.getElementById(id));
        this.selectListener = mergedOptions.selectListener;
        this.selectItemCss = mergedOptions.selectItemCss;
        this.selectItemCssMarker = mergedOptions.selectItemCss.split(" ", 1)[0];
        this.scrollContainer = $(mergedOptions.scrollContainer);
        this.parentContainer = mergedOptions.parentContainer ? $(mergedOptions.parentContainer) : this.scrollContainer;
        this.itemCss = mergedOptions.itemCss.split(" ", 1)[0]; // we only need one of the item css classes to identify the item
        this.listCss = mergedOptions.listCss;
        this.itemDisabledCss = this.itemCss + "-dis";
        this.clickRequiredToSelect = mergedOptions.clickRequiredToSelect;
        this.index = -1;
        this.disabled = mergedOptions.disabled;

        this.focusKeeper = $(document.getElementById(id + "FocusKeeper"));
        this.focusKeeper.focused = false;

        this.isMouseDown = false;
        this.list
            .bind("mousedown", $.proxy(this.__onMouseDown, this))
            .bind("mouseup", $.proxy(this.__onMouseUp, this));


        bindEventHandlers.call(this);
        if (mergedOptions.focusKeeperEnabled) {
            bindFocusEventHandlers.call(this);
        }

        this.__updateItemsList(); // initialize this.items
        if (mergedOptions.clientSelectItems !== null) {
            this.__storeClientSelectItems(mergedOptions.clientSelectItems);
        }
    };

    rf.BaseComponent.extend(rf.ui.List);
    var $super = rf.ui.List.$super;

    var defaultOptions = {
        clickRequiredToSelect: false,
        disabled : false,
        selectListener : false,
        clientSelectItems : null,
        focusKeeperEnabled : true
    };

    var bindEventHandlers = function () {
        var handlers = {};
        handlers["click" + this.namespace] = $.proxy(this.onClick, this);
        handlers["dblclick" + this.namespace] = $.proxy(this.onDblclick, this);
        this.list.on("mouseover" + this.namespace, "."+this.itemCss, $.proxy(onMouseOver, this));
        rf.Event.bind(this.list, handlers, this);
    };

    var bindFocusEventHandlers = function () {
        var focusEventHandlers = {};
        focusEventHandlers["keydown" + this.namespace] = $.proxy(this.__keydownHandler, this);
        focusEventHandlers["blur" + this.namespace] = $.proxy(this.__blurHandler, this);
        focusEventHandlers["focus" + this.namespace] = $.proxy(this.__focusHandler, this);
        rf.Event.bind(this.focusKeeper, focusEventHandlers, this);
    };

    var onMouseOver = function(e) {
        var item = $(e.target);
        if (item && !this.clickRequiredToSelect && !this.disabled) {
            this.__select(item);
        }
    };

    $.extend(rf.ui.List.prototype, ( function () {

        return{

            name : "list",

            processItem: function(item) {
                if ($(item).hasClass(this.itemDisabledCss)) {
                    return;
                }
                if (this.selectListener.processItem && typeof this.selectListener.processItem == 'function') {
                    this.selectListener.processItem(item);
                }
            },

            isSelected: function(item) {
                return item.hasClass(this.selectItemCssMarker);
            },

            selectItem: function(item) {
                if (this.selectListener.selectItem && typeof this.selectListener.selectItem == 'function') {
                    this.selectListener.selectItem(item);
                } else {
                    if (item.hasClass(this.itemDisabledCss)) {
                        return;
                    }
                    item.addClass(this.selectItemCss);
                    rf.Event.fire(this, "selectItem", item);
                }
                this.__scrollToSelectedItem(this);
            },

            unselectItem: function(item) {
                if (this.selectListener.unselectItem && typeof this.selectListener.unselectItem == 'function') {
                    this.selectListener.unselectItem(item);
                } else {
                    item.removeClass(this.selectItemCss);
                    rf.Event.fire(this, "unselectItem", item);
                }
            },

            __focusHandler : function(e) {
                if (! this.focusKeeper.focused) {
                    this.focusKeeper.focused = true;
                    rf.Event.fire(this, "listfocus" + this.namespace, e);
                }
            },

            __blurHandler: function(e) {
                if (!this.isMouseDown) {
                    var that = this;
                    this.timeoutId = window.setTimeout(function() {
                        that.focusKeeper.focused = false;
                        that.invokeEvent.call(that, "blur", document.getElementById(that.id), e);
                        rf.Event.fire(that, "listblur" + that.namespace, e);
                    }, 200);
                } else {
                    this.isMouseDown = false;
                }
            },

            __onMouseDown: function(e) {
                this.isMouseDown = true;
            },

            __onMouseUp: function(e) {
                this.isMouseDown = false;
            },

            __keydownHandler: function(e) {
                if (e.isDefaultPrevented()) return;
                if (e.metaKey || e.ctrlKey) return;

                var code;
                if (e.keyCode) {
                    code = e.keyCode;
                } else if (e.which) {
                    code = e.which;
                }

                switch (code) {
                    case rf.KEYS.DOWN:
                        e.preventDefault();
                        this.__selectNext();
                        break;

                    case rf.KEYS.UP:
                        e.preventDefault();
                        this.__selectPrev();
                        break;

                    case rf.KEYS.HOME:
                        e.preventDefault();
                        this.__selectByIndex(0);
                        break;

                    case rf.KEYS.END:
                        e.preventDefault();
                        this.__selectByIndex(this.items.length - 1);
                        break;

                    default:
                        break;
                }
            },

            onClick: function(e) {
                this.setFocus();
                var item = this.__getItem(e);
                if (!item || $(item).hasClass(this.itemDisabledCss)) return;
                this.processItem(item);
                var clickModified = e.metaKey || e.ctrlKey;
                if (!this.disabled) {
                    this.__select(item, clickModified && this.clickRequiredToSelect);
                }
            },

            onDblclick: function(e) {
                this.setFocus();
                var item = this.__getItem(e);
                if (!item) return;
                this.processItem(item);
                if (!this.disabled) {
                    this.__select(item, false);
                }
            },

            currentSelectItem: function() {
                if (this.items && this.index != -1) {
                    return $(this.items[this.index]);
                }
            },

            getSelectedItemIndex: function() {
                return this.index;
            },

            removeItems: function(items) {
                $(items).detach();
                this.__updateItemsList();
                rf.Event.fire(this, "removeitems", items);
            },

            removeAllItems: function() {
                var items = this.__getItems();
                this.removeItems(items);
                return items;
            },

            addItems: function(items) {
                var container = this.parentContainer;
                container.append(items);
                this.__updateItemsList();
                rf.Event.fire(this, "additems", items);
            },

            move: function(items, step) {
                if (step === 0) {
                    return;
                }
                var that = this;
                if (step > 0) {
                    items = $(items.get().reverse());
                }
                items.each(function(i) {
                    var index = that.items.index(this);
                    var indexNew = index + step;
                    var existingItem = that.items[indexNew];
                    if (step < 0) {
                        $(this).insertBefore(existingItem);
                    } else {
                        $(this).insertAfter(existingItem);
                    }
                    that.index = that.index + step;
                    that.__updateItemsList();
                });
                rf.Event.fire(this, "moveitems", items);
            },

            getItemByIndex: function(i) {
                if (i >= 0 && i < this.items.length) {
                    return this.items[i];
                }
            },

            getClientSelectItemByIndex: function(i) {
                if (i >= 0 && i < this.items.length) {
                    return $(this.items[i]).data('clientSelectItem');
                }
            },

            resetSelection: function() {
                var item = this.currentSelectItem();
                if (item) {
                    this.unselectItem($(item));
                }
                this.index = -1;
            },

            isList: function(target) {
                var parentId = target.parents("." + this.listCss).attr("id");
                return (parentId && (parentId == this.getId()));
            },

            length: function() {
                return this.items.length;
            },

            __updateIndex: function(item) {
                if (item === null) {
                    this.index = -1;
                } else {
                    var index = this.items.index(item);
                    if (index < 0) {
                        index = 0;
                    } else if (index >= this.items.length) {
                        index = this.items.length - 1;
                    }
                    this.index = index;
                }
            },

            __updateItemsList: function () {
                return (this.items = this.list.find("." + this.itemCss));
            },

            __storeClientSelectItems: function(clientSelectItems) {
                var clientSelectItemsMap = [];
                $.each(clientSelectItems, function(i) {
                    clientSelectItemsMap[this.id] = this;
                });
                this.items.each(function (i)  {
                    var item = $(this);
                    var id = item.attr("id");
                    var clientSelectItem = clientSelectItemsMap[id];
                    item.data('clientSelectItem', clientSelectItem);
                })
            },

            __select: function(item, clickModified) {
                var index = this.items.index(item);
                this.__selectByIndex(index, clickModified);
            },

            __selectByIndex: function(index, clickModified) {
                if (! this.__isSelectByIndexValid(index)) {
                    return;
                }

                if (!this.clickRequiredToSelect && this.index == index) {
                    return; // do nothing if re-selecting the same item
                }

                var oldIndex = this.__unselectPrevious();

                if (this.clickRequiredToSelect && oldIndex == index) {
                    return; //do nothing after unselecting item
                }

                this.index = this.__sanitizeSelectedIndex(index);

                var item = this.items.eq(this.index);
                if (this.isSelected(item)) {
                    this.unselectItem(item);
                } else {
                    this.selectItem(item);
                }
            },

            __isSelectByIndexValid: function(index) {
                if (this.items.length == 0) {
                    return false;
                }
                if (index == undefined) {
                    this.index = -1;
                    return false;
                }
                return true;
            },

            __sanitizeSelectedIndex: function(index) {
                var sanitizedIndex;
                if (index < 0) {
                    sanitizedIndex = 0;
                } else if (index >= this.items.length) {
                    sanitizedIndex = this.items.length - 1;
                } else {
                    sanitizedIndex = index;
                }
                return sanitizedIndex;
            },

            __unselectPrevious: function() {
                var oldIndex = this.index;
                if (oldIndex != -1) {
                    var item = this.items.eq(oldIndex);
                    this.unselectItem(item);
                    this.index = -1;
                }
                return oldIndex;
            },

            __selectItemByValue: function(value) {
                var item = null;
                this.resetSelection();
                var that = this;
                this.__getItems().each(function( i ) {
                    if ($(this).data('clientSelectItem').value == value) {
                        that.__selectByIndex(i);
                        item = $(this);
                        return false; //break
                    }
                });
                return item;
            },

            csvEncodeValues: function() {
                var encoded = new Array();
                this.__getItems().each(function( index ) {
                    encoded.push($(this).data('clientSelectItem').value);
                });
                return encoded.join("\",'");
            },

            __selectCurrent: function() {
                var item;
                if (this.items && this.index >= 0) {
                    item = this.items.eq(this.index);
                    this.processItem(item);
                }
            },

            __getAdjacentIndex: function(offset) {
                var index = this.index + offset;
                if (index < 0) {
                    index = this.items.length - 1;
                } else if (index >= this.items.length) {
                    index = 0;
                }
                return index;
            },

            __selectPrev: function() {
                this.__selectByIndex(this.__getAdjacentIndex(-1));
            },

            __selectNext: function() {
                this.__selectByIndex(this.__getAdjacentIndex(1));
            },

            __getItem: function(e) {
                return $(e.target).closest("." + this.itemCss, e.currentTarget).get(0);
            },

            __getItems: function () {
                return this.items;
            },

            __setItems: function(items) {
                this.items = items;
            },

            __scrollToSelectedItem : function() {
                if (this.scrollContainer) {
                    var contClientRect = this.scrollContainer[0].getBoundingClientRect(),
                        itemClientRect = this.items.get(this.index).getBoundingClientRect();
                    
                    if (contClientRect.top < itemClientRect.top && 
                        itemClientRect.bottom < contClientRect.bottom) {
                        // the item is within the visible area, do not scroll
                        return;
                    }
                    
                    var iTop = itemClientRect.top, cTop = contClientRect.top,
                        scrollTop = this.scrollContainer.scrollTop() + iTop - cTop;
                    
                    this.scrollContainer.scrollTop(scrollTop);
                }
            },

            setFocus : function() {
    		    this.focusKeeper.focus();
	        }

        }
    })());

})(RichFaces.jQuery, window.RichFaces);

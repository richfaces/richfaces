(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.List = function(id, listener, options) {
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, id);
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        this.list = $(document.getElementById(id));
        this.selectListener = listener;
        this.selectItemCss = mergedOptions.selectItemCss;
        this.scrollContainer = $(mergedOptions.scrollContainer);
        this.itemCss = mergedOptions.itemCss;
        this.listCss = mergedOptions.listCss;
        this.clickRequiredToSelect = mergedOptions.clickRequiredToSelect;
        this.multipleSelect = mergedOptions.multipleSelect;
        this.index = -1;
        this.disabled = mergedOptions.disabled;

        this.lastMouseX = null;
        this.lastMouseY = null;
        bindEventHandlers.call(this);
        this.__updateItemsList();
    };

    rf.BaseComponent.extend(rf.ui.List);
    var $super = rf.ui.List.$super;

    var defaultOptions = {
        clickRequiredToSelect: false,
        multipleSelect: false,
        disabled : false
    };

    var bindEventHandlers = function () {
        var handlers = {};
        handlers["click" + this.namespace] = onClick;
        handlers["mouseover" + this.namespace] = onMouseOver;
        if (!$.browser.msie && !$.browser.opera) {
            handlers["mouseenter" + this.namespace] = onMouseEnter;
            handlers["mouseleave" + this.namespace] = onMouseLeave;
        }
        rf.Event.bind(this.list, handlers, this);
    };

    var onMouseLeave = function(e) {
        rf.Event.unbind(this.list, "mousemove" + this.namespace);
        this.lastMouseX = null;
        this.lastMouseY = null;
    };

    var onMouseMove = function(e) {
        this.lastMouseX = e.pageX;
        this.lastMouseY = e.pageY;
    };

    var onMouseEnter = function(e) {
        this.lastMouseX = e.pageX;
        this.lastMouseY = e.pageY;
        rf.Event.bind(this.list, "mousemove" + this.namespace, onMouseMove, this);
    };

    var onMouseOver = function(e) {
        if (this.lastMouseX == null || this.lastMouseX != e.pageX || this.lastMouseY != e.pageY) {
            var item = this.__getItem(e);
            if (item && !this.clickRequiredToSelect && !this.disabled) {
                this.__select(item);
            }
        }
    };

    var onClick = function(e) {
        var item = this.__getItem(e);
        this.processItem(item);
        var clickModified = e.metaKey;
        if (!this.disabled) {
            this.__select(item, clickModified && this.clickRequiredToSelect);
        }
    };

    $.extend(rf.ui.List.prototype, ( function () {

        return{

            name : "list",

            processItem: function(item) {
                if (this.selectListener.processItem && typeof this.selectListener.processItem == 'function') {
                    this.selectListener.processItem(item);
                }
            },

            isSelected: function(item) {
                return item.hasClass(this.selectItemCss);
            },

            selectItem: function(item) {
                if (this.selectListener.selectItem && typeof this.selectListener.selectItem == 'function') {
                    this.selectListener.selectItem(item);
                } else {
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

            currentSelectItem: function() {
                if (this.items && this.index != -1) {
                    return $(this.items[this.index]);
                }
            },

            getSelectedItemIndex: function() {
                return this.index;
            },

            getSelectedItems: function() {
                return this.list.find("." + this.selectItemCss);;
            },

            removeSelectedItems: function() {
                var items = this.getSelectedItems().detach();
                this.__updateItemsList();
                return items;
            },

            removeAllItems: function() {
                var items = this.__getItems().detach();
                this.__updateItemsList();
                rf.Event.fire(this, "removeitems", items);
                return items;
            },

            addItems: function(items) {
                var parentContainer = this.scrollContainer;
                parentContainer.append(items);
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
                    that.__updateItemsList();
                });
                rf.Event.fire(this, "moveitems", items);
            },

            getItemByIndex: function(i) {
                if (i >= 0 && i < this.items.length) {
                    return this.items[i];
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

            __select: function(item, clickModified) {
                var index = this.items.index(item);
                this.__selectByIndex(index, clickModified);
            },

            __selectByIndex: function(index, clickModified) {
                if (this.items.length == 0) return;

                if (!this.multipleSelect) {
                    if (!this.clickRequiredToSelect && this.index == index) return;

                    if (this.index != -1) {
                        var item = this.items.eq(this.index);
                        this.unselectItem(item);
                        var oldIndex = this.index;
                        this.index = -1;
                    }

                    if (this.clickRequiredToSelect && oldIndex == index) {
                        return;
                    }
                }

                if (index == undefined) {
                    this.index = -1;
                    return;
                }

                if (index < 0) {
                    index = 0;
                } else if (index >= this.items.length) {
                    index = this.items.length - 1;
                }
                this.index = index;

                item = this.items.eq(this.index);
                if (this.multipleSelect && ! clickModified) {
                    var that = this;
                    this.getSelectedItems().each( function(index) {
                        that.unselectItem($(this))
                    });
                    this.selectItem(item);
                } else {
                    if (this.isSelected(item)) {
                        this.unselectItem(item);
                    } else {
                        this.selectItem(item);
                    }
                }
            },

            __selectItemByValue: function(value) {
                var item;
                for (var i = 0; i < this.items.length; i++) {
                    item = this.items[i];
                    if (item.value == value) {
                        this.__selectByIndex(i);
                        return;
                    }
                }
                this.resetSelection();
                return item;
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
                    var offset = 0;

                    this.items.slice(0, this.index).each(function() {
                        offset += this.offsetHeight;
                    });

                    var parentContainer = this.scrollContainer;
                    if (offset < parentContainer.scrollTop()) {
                        parentContainer.scrollTop(offset);
                    } else {
                        offset += this.items.get(this.index).offsetHeight;
                        if (offset - parentContainer.scrollTop() > parentContainer.get(0).clientHeight) {
                            parentContainer.scrollTop(offset - parentContainer.innerHeight());
                        }
                    }
                }
            }
        }
    })());

})(jQuery, window.RichFaces);
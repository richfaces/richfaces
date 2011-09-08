(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.PickList = function(id, options) {
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        mergedOptions['scrollContainer'] = $(document.getElementById(id + "SourceItems")).parent()[0];
        this.sourceList = new rf.ui.ListMulti(id+ "SourceList", mergedOptions);
        mergedOptions['scrollContainer'] = $(document.getElementById(id + "TargetItems")).parent()[0];
        this.selectItemCss = mergedOptions['selectItemCss'];
        var hiddenId = id + "SelValue";
        this.hiddenValues = $(document.getElementById(hiddenId));
        mergedOptions['hiddenId'] = hiddenId;
        this.orderable = mergedOptions['orderable'];

        if (this.orderable) {
            this.orderingList = new rf.ui.OrderingList(id+ "Target", mergedOptions);
            this.targetList = this.orderingList.list;
        } else {
            this.targetList = new rf.ui.ListMulti(id+ "TargetList", mergedOptions);
        }
        this.pickList = $(document.getElementById(id));

        this.addButton = $('.rf-pick-add', this.pickList);
        this.addButton.bind("click", $.proxy(this.add, this));
        this.addAllButton = $('.rf-pick-add-all', this.pickList);
        this.addAllButton.bind("click", $.proxy(this.addAll, this));
        this.removeButton = $('.rf-pick-rem', this.pickList);
        this.removeButton.bind("click", $.proxy(this.remove, this));
        this.removeAllButton = $('.rf-pick-rem-all', this.pickList);
        this.removeAllButton.bind("click", $.proxy(this.removeAll, this));
        this.disabled = mergedOptions.disabled;

        if (mergedOptions['onadditems'] && typeof mergedOptions['onadditems'] == 'function') {
            rf.Event.bind(this.targetList, "additems", mergedOptions['onadditems']);
        }
        rf.Event.bind(this.targetList, "additems", $.proxy(this.toggleButtons, this));

        this.focused = false;
        this.keepingFocus = false;
        bindFocusEventHandlers.call(this, mergedOptions);

        // Adding items to the source list happens after removing them from the target list
        if (mergedOptions['onremoveitems'] && typeof mergedOptions['onremoveitems'] == 'function') {
            rf.Event.bind(this.sourceList, "additems", mergedOptions['onremoveitems']);
        }
        rf.Event.bind(this.sourceList, "additems", $.proxy(this.toggleButtons, this));

        rf.Event.bind(this.sourceList, "selectItem", $.proxy(this.toggleButtons, this));
        rf.Event.bind(this.sourceList, "unselectItem", $.proxy(this.toggleButtons, this));
        rf.Event.bind(this.targetList, "selectItem", $.proxy(this.toggleButtons, this));
        rf.Event.bind(this.targetList, "unselectItem", $.proxy(this.toggleButtons, this));

        // TODO: Is there a "Richfaces way" of executing a method after page load?
        $(document).ready($.proxy(this.toggleButtons, this));
    };
    rf.BaseComponent.extend(rf.ui.PickList);
    var $super = rf.ui.PickList.$super;

    var defaultOptions = {
        defaultLabel: "",
        itemCss: "rf-pick-opt",
        selectItemCss: "rf-pick-sel",
        listCss: "rf-pick-lst-cord",
        clickRequiredToSelect: true,
        disabled : false
    };

    var bindFocusEventHandlers = function (options) {
        // source list
        if (options['onsourcefocus'] && typeof options['onsourcefocus'] == 'function') {
            rf.Event.bind(this.sourceList, "listfocus" + this.sourceList.namespace, options['onsourcefocus']);
        }

        if (options['onsourceblur'] && typeof options['onsourceblur'] == 'function') {
            rf.Event.bind(this.sourceList, "listblur" + this.sourceList.namespace, options['onsourceblur']);
        }

        // target list
        if (options['ontargetfocus'] && typeof options['ontargetfocus'] == 'function') {
            rf.Event.bind(this.targetList, "listfocus" + this.targetList.namespace, options['ontargetfocus']);
        }
        if (options['ontargetblur'] && typeof options['ontargetblur'] == 'function') {
            rf.Event.bind(this.targetList, "listblur" + this.targetList.namespace, options['ontargetblur']);
        }

        // pick list
        if (options['onfocus'] && typeof options['onfocus'] == 'function') {
            rf.Event.bind(this, "focus" + this.namespace, options['onfocus']);
        }
        if (options['onblur'] && typeof options['onblur'] == 'function') {
            rf.Event.bind(this, "blur" + this.namespace, options['onblur']);
        }

        var focusEventHandlers = {};
        focusEventHandlers["listfocus" + this.sourceList.namespace] = $.proxy(this.__focusHandler, this);
        focusEventHandlers["listblur" + this.sourceList.namespace] = $.proxy(this.__blurHandler, this);
        rf.Event.bind(this.sourceList, focusEventHandlers, this);

        focusEventHandlers = {};
        focusEventHandlers["listfocus" + this.targetList.namespace] = $.proxy(this.__focusHandler, this);
        focusEventHandlers["listblur" + this.targetList.namespace] = $.proxy(this.__blurHandler, this);
        rf.Event.bind(this.targetList, focusEventHandlers, this);

        focusEventHandlers = {};
        focusEventHandlers["focus"] = $.proxy(this.__focusHandler, this);
        focusEventHandlers["blur"] = $.proxy(this.__blurHandler, this);
        rf.Event.bind(this.addButton, focusEventHandlers, this);
        rf.Event.bind(this.addAllButton, focusEventHandlers, this);
        rf.Event.bind(this.removeButton, focusEventHandlers, this);
        rf.Event.bind(this.removeAllButton, focusEventHandlers, this);
    };

    $.extend(rf.ui.PickList.prototype, (function () {

        return {
            name : "pickList",
            defaultLabelClass : "rf-pick-dflt-lbl",

            getName: function() {
                return this.name;
            },
            getNamespace: function() {
                return this.namespace;
            },

            __focusHandler: function(e) {
                this.keepingFocus = this.focused;
                if (! this.focused) {
                    this.focused = true;
                    rf.Event.fire(this, "focus" + this.namespace, e);
                }
            },

            __blurHandler: function(e) {
                var that = this;
                this.timeoutId = window.setTimeout(function() {
                    if (!that.keepingFocus) { // If no other pickList "sub" component has grabbed the focus during the timeout
                        that.focused = false;
                        rf.Event.fire(that, "blur" + that.namespace, e);
                    }
                    that.keepingFocus = false;
                }, 200);
            },


            add: function() {
                this.targetList.setFocus();
                var items = this.sourceList.removeSelectedItems();
                this.targetList.addItems(items);
                this.encodeHiddenValues();
            },

            remove: function() {
                this.sourceList.setFocus();
                var items = this.targetList.removeSelectedItems();
                this.sourceList.addItems(items);
                this.encodeHiddenValues();
            },

            addAll: function() {
                this.targetList.setFocus();
                var items = this.sourceList.removeAllItems();
                this.targetList.addItems(items);
                this.encodeHiddenValues();
            },

            removeAll: function() {
                this.sourceList.setFocus();
                var items = this.targetList.removeAllItems();
                this.sourceList.addItems(items);
                this.encodeHiddenValues();
            },

            encodeHiddenValues: function() {
                this.hiddenValues.val(this.targetList.csvEncodeValues());
            },

            toggleButtons: function() {
                this.__toggleButton(this.addButton, this.sourceList.__getItems());
                this.__toggleButton(this.removeButton, this.targetList.__getItems());
                if (this.orderable) {
                    this.orderingList.toggleButtons();
                }
            },

            __toggleButton: function(button, list) {
                if (this.disabled || list.filter('.' + this.selectItemCss).length == 0) {
                    if (! button.hasClass('rf-pick-btn-dis')) {
                        button.addClass('rf-pick-btn-dis')
                    }
                    if (! button.attr('disabled')) {
                        button.attr('disabled', true);
                    }
                } else {
                    if (button.hasClass('rf-pick-btn-dis')) {
                        button.removeClass('rf-pick-btn-dis')
                    }
                    if (button.attr('disabled')) {
                        button.attr('disabled', false);
                    }
                }
            }
        };
    })());

})(jQuery, window.RichFaces);

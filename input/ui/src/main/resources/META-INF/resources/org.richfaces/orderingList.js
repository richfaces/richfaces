(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.OrderingList = function(id, options) {
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        this.orderingList = $(document.getElementById(id));
        mergedOptions['scrollContainer'] = $(document.getElementById(id + "Items")).parent()[0];
        this.list = new rf.ui.ListMulti(id+ "List", mergedOptions);
        var hiddenId = mergedOptions['hiddenId'] ===null ? id + "SelValue" : mergedOptions['hiddenId'];
        this.hiddenValues = $(document.getElementById(hiddenId));
        this.selectItemCss = mergedOptions['selectItemCss'];
        this.disabled = mergedOptions.disabled;

        this.upButton = $('.rf-ord-up', this.orderingList);
        this.upButton.bind("click", $.proxy(this.up, this));
        this.upTopButton = $('.rf-ord-up-top', this.orderingList);
        this.upTopButton.bind("click", $.proxy(this.upTop, this));
        this.downButton = $('.rf-ord-down', this.orderingList);
        this.downButton.bind("click", $.proxy(this.down, this));
        this.downBottomButton = $('.rf-ord-down-bottom', this.orderingList);
        this.downBottomButton.bind("click", $.proxy(this.downBottom, this));

        if (mergedOptions['onmoveitems'] && typeof mergedOptions['onmoveitems'] == 'function') {
            rf.Event.bind(this.list, "moveitems", mergedOptions['onmoveitems']);
        }
        rf.Event.bind(this.list, "moveitems", $.proxy(this.toggleButtons, this));

        rf.Event.bind(this.list, "selectItem", $.proxy(this.toggleButtons, this));
        rf.Event.bind(this.list, "unselectItem", $.proxy(this.toggleButtons, this));

        rf.Event.bind(this.list, "keydown" + this.list.namespace, $.proxy(this.__keydownHandler, this));

        // TODO: Is there a "Richfaces way" of executing a method after page load?
        $(document).ready($.proxy(this.toggleButtons, this));
    };
    rf.BaseComponent.extend(rf.ui.OrderingList);
    var $super = rf.ui.OrderingList.$super;

    var defaultOptions = {
        defaultLabel: "",
        itemCss: "rf-ord-opt",
        selectItemCss: "rf-ord-sel",
        listCss: "rf-ord-lst-cord",
        clickRequiredToSelect: true,
        disabled : false,
        hiddenId : null
    };

    $.extend(rf.ui.OrderingList.prototype, (function () {

        return {
            name : "ordList",
            defaultLabelClass : "rf-ord-dflt-lbl",

            getName: function() {
                return this.name;
            },
            getNamespace: function() {
                return this.namespace;
            },

            __focusHandler: function(e) {
            },

            __keydownHandler: function(e) {
                if (e.isDefaultPrevented()) return;
                if (! e.metaKey) return;

                var code;
                if (e.keyCode) {
                    code = e.keyCode;
                } else if (e.which) {
                    code = e.which;
                }

                switch (code) {
                    case rf.KEYS.DOWN:
                        e.preventDefault();
                        this.down();
                        break;

                    case rf.KEYS.UP:
                        e.preventDefault();
                        this.up();
                        break;

                    case rf.KEYS.HOME:
                        e.preventDefault();
                        this.upTop();
                        break;

                    case rf.KEYS.END:
                        e.preventDefault();
                        this.downBottom();
                        break;

                    default:
                        break;
                }
                return;
            },

            up: function() {
                var items = this.list.getSelectedItems();
                this.list.move(items, -1);
                this.encodeHiddenValues();
            },

            down: function() {
                var items = this.list.getSelectedItems();
                this.list.move(items, 1);
                this.encodeHiddenValues();
            },

            upTop: function() {
                var selectedItems = this.list.getSelectedItems();
                var index = this.list.items.index(selectedItems.first());
                this.list.move(selectedItems, -index);
                this.encodeHiddenValues();
            },

            downBottom: function() {
                var selectedItems = this.list.getSelectedItems();
                var index = this.list.items.index(selectedItems.last());
                this.list.move(selectedItems, (this.list.items.length -1) - index);
                this.encodeHiddenValues();
            },

            encodeHiddenValues: function() {
                var oldHiddenValues = this.hiddenValues.val();
                var newHiddenValues = this.list.csvEncodeValues();
                if (oldHiddenValues !== newHiddenValues)  {
                    this.hiddenValues.val(this.list.csvEncodeValues());
                    this.invokeEvent.call(this, "change", document.getElementById(this.id));
                }
            },

            toggleButtons: function() {
                var list = this.list.__getItems();
                if (this.disabled || this.list.getSelectedItems().length === 0) {
                    this.__disableButton(this.upButton);
                    this.__disableButton(this.upTopButton);
                    this.__disableButton(this.downButton);
                    this.__disableButton(this.downBottomButton);
                } else {
                    if (this.list.items.index(this.list.getSelectedItems().first()) === 0) {
                        this.__disableButton(this.upButton);
                        this.__disableButton(this.upTopButton);
                    } else {
                        this.__enableButton(this.upButton);
                        this.__enableButton(this.upTopButton);
                    }
                    if (this.list.items.index(this.list.getSelectedItems().last()) === (this.list.items.length - 1)) {
                        this.__disableButton(this.downButton);
                        this.__disableButton(this.downBottomButton);
                    } else {
                        this.__enableButton(this.downButton);
                        this.__enableButton(this.downBottomButton);
                    }
                }
            },

            __disableButton: function (button) {
                 if (! button.hasClass('rf-ord-btn-dis')) {
                    button.addClass('rf-ord-btn-dis')
                }
                if (! button.attr('disabled')) {
                    button.attr('disabled', true);
                }
            },

            __enableButton: function(button) {
                if (button.hasClass('rf-ord-btn-dis')) {
                    button.removeClass('rf-ord-btn-dis')
                }
                if (button.attr('disabled')) {
                    button.attr('disabled', false);
                }
            }
        };
    })());

})(jQuery, window.RichFaces);

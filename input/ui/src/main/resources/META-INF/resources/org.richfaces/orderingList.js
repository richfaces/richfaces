(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.OrderingList = function(id, options) {
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        this.orderingList = $(document.getElementById(id));
        mergedOptions['scrollContainer'] = $(document.getElementById(id + "Items")).parent()[0];
        this.list = new rf.ui.List(id+ "List", this, mergedOptions);
        this.hiddenValues = $(document.getElementById(id + "SelValue"));
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
        disabled : false
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
                alert("focus");
            },

            up: function() {
                var item = this.list.currentSelectItem();
                this.list.moveUp(item);
                this.encodeHiddenValues();
            },

            down: function() {
                var item = this.list.currentSelectItem();
                this.list.moveDown(item);
                this.encodeHiddenValues();
            },

            upTop: function() {
                var item = this.list.currentSelectItem();
                this.list.moveToTop(item);
                this.encodeHiddenValues();
            },

            downBottom: function() {
                var item = this.list.currentSelectItem();
                this.list.moveToBottom(item);
                this.encodeHiddenValues();
            },

            encodeHiddenValues: function() {
                var encoded = new Array();
                this.list.__getItems().each(function( index ) {
                    encoded.push($(this).attr('value'));
                });
                this.hiddenValues.val(encoded.join(","));
            },

            toggleButtons: function() {
                var list = this.list.__getItems();
                if (this.disabled || list.filter('.' + this.selectItemCss).length == 0) {
                    this.__disableButton(this.upButton);
                    this.__disableButton(this.upTopButton);
                    this.__disableButton(this.downButton);
                    this.__disableButton(this.downBottomButton);
                } else {
                    this.__enableButton(this.upButton);
                    this.__enableButton(this.upTopButton);
                    this.__enableButton(this.downButton);
                    this.__enableButton(this.downBottomButton);
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

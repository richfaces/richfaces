(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.PickList = function(id, options) {
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        mergedOptions['scrollContainer'] = $(document.getElementById(id + "SourceItems")).parent()[0];
        this.sourceList = new rf.ui.List(id+ "Source", this, mergedOptions);
        mergedOptions['scrollContainer'] = $(document.getElementById(id + "TargetItems")).parent()[0];
        this.selectItemCss = mergedOptions['selectItemCss'];
        this.targetList = new rf.ui.List(id+ "Target", this, mergedOptions);
        this.pickList = $(document.getElementById(id));
        this.hiddenValues = $(document.getElementById(id + "SelValue"));

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
        multipleSelect: true,
        disabled : false
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
                alert("focus");
            },

            add: function() {
                var items = this.sourceList.removeSelectedItems();
                this.targetList.addItems(items);
                this.encodeHiddenValues();
            },

            remove: function() {
                var items = this.targetList.removeSelectedItems();
                this.sourceList.addItems(items);
                this.encodeHiddenValues();
            },

            addAll: function() {
                var items = this.sourceList.removeAllItems();
                this.targetList.addItems(items);
                this.encodeHiddenValues();
            },

            removeAll: function() {
                var items = this.targetList.removeAllItems();
                this.sourceList.addItems(items);
                this.encodeHiddenValues();
            },

            encodeHiddenValues: function() {
                var encoded = new Array();
                this.targetList.__getItems().each(function( index ) {
                    encoded.push($(this).attr('value'));
                });
                this.hiddenValues.val(encoded.join(","));
            },

            toggleButtons: function() {
                this.__toggleButton(this.addButton, this.sourceList.__getItems());
                this.__toggleButton(this.removeButton, this.targetList.__getItems());
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

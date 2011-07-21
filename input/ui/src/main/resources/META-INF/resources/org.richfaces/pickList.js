(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.PickList = function(id, options) {
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        mergedOptions['scrollContainer'] = $(document.getElementById(id + "SourceItems")).parent()[0];
        this.sourceList = new rf.ui.List(id+ "Source", this, mergedOptions);
        mergedOptions['scrollContainer'] = $(document.getElementById(id + "TargetItems")).parent()[0];
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
    };
    rf.BaseComponent.extend(rf.ui.PickList);
    var $super = rf.ui.PickList.$super;

    var defaultOptions = {
        defaultLabel: "",
//        selectFirst: true,
//        showControl: true,
//        enableManualInput: false,
        itemCss: "rf-pick-opt",
        selectItemCss: "rf-pick-sel",
        listCss: "rf-pick-lst-cord",
        clickRequiredToSelect: true,
        multipleSelect: true
//        changeDelay: 8,
//        disabled: false
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
            }
        };
    })());

})(jQuery, window.RichFaces);

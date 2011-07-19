(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.PickList = function(id, options) {
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        this.pickList = $(document.getElementById(id + "PickList"));
        this.sourceList = $(document.getElementById(id + "Source"));
        this.targetList = $(document.getElementById(id + "Target"));
        this.hiddenValues = $(document.getElementById(id + "Hidden"));
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

            add: function() {
                $('option:selected', this.sourceList).remove().appendTo(this.targetList);
                this.encodeHiddenValues();
            },

            remove: function() {
                $('option:selected', this.targetList).remove().appendTo(this.sourceList);
                this.encodeHiddenValues();
            },

            addAll: function() {
                $('option', this.sourceList).remove().appendTo(this.targetList);
                this.encodeHiddenValues();
            },

            removeAll: function() {
                $('option', this.targetList).remove().appendTo(this.sourceList);
                this.encodeHiddenValues();
            },

            encodeHiddenValues: function() {
                var encoded = new Array();
                $('option', this.targetList).each(function( index ) {
                    encoded.push($(this).val());
                });
                this.hiddenValues.val(encoded.join(","));
            }
        };
    })());

})(jQuery, window.RichFaces);

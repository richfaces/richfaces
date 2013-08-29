(function ($, rf) {

    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

    rf.rf4.ui.ListMulti = function(id, options) {
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, id);
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        this.disabled = mergedOptions.disabled;
    };

    rf.rf4.ui.List.extend(rf.rf4.ui.ListMulti);
    var $super = rf.rf4.ui.ListMulti.$super;

    var defaultOptions = {
        clickRequiredToSelect: true
    };

    $.extend(rf.rf4.ui.ListMulti.prototype, ( function () {

        return{

            name : "listMulti",

            getSelectedItems: function() {
                return this.list.find("." + this.selectItemCssMarker);
            },

            removeSelectedItems: function() {
                var items = this.getSelectedItems();
                this.removeItems(items);
                return items;
            },

            __selectByIndex: function(index, clickModified) {
                if (! this.__isSelectByIndexValid(index)) {
                    return;
                }

                this.index = this.__sanitizeSelectedIndex(index);

                var item = this.items.eq(this.index);
                if (! clickModified) {
                    var that = this;
                    this.getSelectedItems().each( function() {
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
            }
        }
    })());

})(RichFaces.jQuery, window.RichFaces);
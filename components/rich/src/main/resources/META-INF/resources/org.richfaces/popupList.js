(function ($, rf) {

    rf.ui = rf.ui || {};

    /**
     * @extends RichFaces.ui.Popup
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.PopupList
     * 
     * @param id
     * @param listener
     * @param options
     */
    rf.ui.PopupList = function(id, listener, options) {
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, id);
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        mergedOptions['selectListener']=listener;
        this.list = new rf.ui.List(id, mergedOptions);
    };

    rf.ui.Popup.extend(rf.ui.PopupList);
    var $super = rf.ui.PopupList.$super;

    var defaultOptions = {
        attachToBody: true,
        positionType: "DROPDOWN",
        positionOffset: [0,0]
    };

    $.extend(rf.ui.PopupList.prototype, ( function () {

        return {

            name : "popupList",

            __getList: function() {
                return this.list;
            },

            destroy: function() {
                this.list.destroy();
                this.list = null;
                $super.destroy.call(this);
            }
        }
    })());

})(RichFaces.jQuery, window.RichFaces);
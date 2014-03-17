(function ($, rf) {

    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

    rf.rf4.ui.PopupList = function(id, listener, options) {
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, id);
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        mergedOptions['selectListener']=listener;
        this.list = new rf.rf4.ui.List(id, mergedOptions);
    };

    rf.rf4.ui.Popup.extend(rf.rf4.ui.PopupList);
    var $super = rf.rf4.ui.PopupList.$super;

    var defaultOptions = {
        attachToBody: true,
        positionType: "DROPDOWN",
        positionOffset: [0,0]
    };

    $.extend(rf.rf4.ui.PopupList.prototype, ( function () {

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
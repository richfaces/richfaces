(function ($, rf) {

    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

    rf.rf4.ui.ComponentControl = rf.rf4.ui.ComponentControl || {};

    $.extend(rf.rf4.ui.ComponentControl, {

            execute: function(event, parameters) {
                var targetList = parameters.target;
                var selector = parameters.selector;
                var callback = parameters.callback;

                if (parameters.onbeforeoperation && typeof parameters.onbeforeoperation == "function") {
                    var result = parameters.onbeforeoperation(event);
                    if (result == "false" || result == 0) return;
                }

                if (targetList) {
                    for (var i = 0; i < targetList.length; i++) {
                        var component = document.getElementById(targetList[i]);
                        if (component) {
                            rf.rf4.ui.ComponentControl.invokeOnComponent(event, component, callback);
                        }
                    }
                }

                if (selector) {
                    rf.rf4.ui.ComponentControl.invokeOnComponent(event, selector, callback);
                }
            },

            invokeOnComponent : function(event, target, callback) {
                if (callback && typeof callback == 'function') {
                    $(target).each(function() {
                        var component = rf.component(this);
                        if (component) {
                            callback(event, component);
                        }
                    });
                }
            }
        });

})(RichFaces.jQuery, window.RichFaces);
(function ($, rf) {

    rf.ui = rf.ui || {};

    /**
     * Backing object for rich:componentControl
     * 
     * @memberOf! RichFaces.ui
     * @static
     * @alias RichFaces.ui.ComponentControl
     */
    rf.ui.ComponentControl = rf.ui.ComponentControl || {};

    $.extend(rf.ui.ComponentControl, {

            /**
             * @ignore
             * @memberOf! RichFaces.ui.ComponentControl
             * 
             * @param event
             * @param parameters
             */
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
                            rf.ui.ComponentControl.invokeOnComponent(event, component, callback);
                        }
                    }
                }

                if (selector) {
                    rf.ui.ComponentControl.invokeOnComponent(event, selector, callback);
                }
            },

            invokeOnComponent : function(event, target, callback) {
                if (callback && typeof callback == 'function') {
                    $(target).each(function() {
                        var component = rf.component(this) || this;
                        callback(event, component);
                    });
                }
            }
        });

})(RichFaces.jQuery, window.RichFaces);
(function ($, richfaces) {

    richfaces.ui = richfaces.ui || {};

    richfaces.ui.ComponentControl = richfaces.ui.ComponentControl || {};

    $.extend(richfaces.ui.ComponentControl, {

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
                            richfaces.ui.ComponentControl.invokeOnComponent(event, component, callback);
                        }
                    }
                }

                if (selector) {
                    richfaces.ui.ComponentControl.invokeOnComponent(event, selector, callback);
                }
            },

            invokeOnComponent : function(event, target, callback) {
                if (callback && typeof callback == 'function') {
                    $(target).each(function() {
                        var component = richfaces.component(this);
                        if (component) {
                            callback(event, component);
                        }
                    });
                }
            }
        });

})(jQuery, window.RichFaces);
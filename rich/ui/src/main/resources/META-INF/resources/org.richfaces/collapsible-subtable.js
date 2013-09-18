(function ($, rf) {

    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

    rf.rf4.ui.CollapsibleSubTable = function(id, f, options) {
        this.id = id;
        this.stateInput = options.stateInput;
        this.optionsInput = options.optionsInput;
        this.expandMode = options.expandMode || rf.rf4.ui.CollapsibleSubTable.MODE_CLNT;
        this.eventOptions = options.eventOptions;
        this.formId = f;

        this.attachToDom();
    };

    $.extend(rf.rf4.ui.CollapsibleSubTable, {
            MODE_AJAX: "ajax",
            MODE_SRV: "server",
            MODE_CLNT: "client",
            collapse: 0,
            expand: 1
        });

    rf.BaseComponent.extend(rf.rf4.ui.CollapsibleSubTable);
    var $super = rf.rf4.ui.CollapsibleSubTable.$super;

    $.extend(rf.rf4.ui.CollapsibleSubTable.prototype, (function () {

        var element = function() {
            //use parent tbody as parent dom elem
            return $(document.getElementById(this.id)).parent();
        };

        var stateInputElem = function() {
            return $(document.getElementById(this.stateInput));
        };

        var optionsInputElem = function() {
            return $(document.getElementById(this.optionsInput));
        };

        var ajax = function(e, options) {
            this.__switchState();
            rf.ajax(this.id, e, options);
        };

        var server = function(options) {
            this.__switchState();
            $(document.getElementById(this.formId)).submit();
        };

        var client = function(options) {
            if (this.isExpanded()) {
                this.collapse(options);
            } else {
                this.expand(options);
            }
        };


        return {

            name: "CollapsibleSubTable",

            switchState: function(e, options) {
                if (this.expandMode == rf.rf4.ui.CollapsibleSubTable.MODE_AJAX) {
                    ajax.call(this, e, this.eventOptions, options);
                } else if (this.expandMode == rf.rf4.ui.CollapsibleSubTable.MODE_SRV) {
                    server.call(this, options);
                } else if (this.expandMode == rf.rf4.ui.CollapsibleSubTable.MODE_CLNT) {
                    client.call(this, options);
                }
            },

            collapse: function(options) {
                this.setState(rf.rf4.ui.CollapsibleSubTable.collapse);
                element.call(this).hide();
            },

            expand: function(options) {
                this.setState(rf.rf4.ui.CollapsibleSubTable.expand);
                element.call(this).show();
            },

            isExpanded: function() {
                return (parseInt(this.getState()) == rf.rf4.ui.CollapsibleSubTable.expand);
            },

            __switchState: function(options) {
                var state = this.isExpanded() ? rf.rf4.ui.CollapsibleSubTable.collapse : rf.rf4.ui.CollapsibleSubTable.expand;
                this.setState(state);
            },

            getState: function() {
                return stateInputElem.call(this).val();
            },

            setState: function(state) {
                stateInputElem.call(this).val(state)
            },

            setOption: function(option) {
                optionsInputElem.call(this).val(option);
            },

            getMode: function() {
                return this.expandMode;
            },
            destroy: function() {
                $super.destroy.call(this);
            }
        };

    })());

})(RichFaces.jQuery, window.RichFaces);
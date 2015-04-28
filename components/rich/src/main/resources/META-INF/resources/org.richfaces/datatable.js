(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.DataTable = function(id, options) {
        $super.constructor.call(this, id);
        this.options = $.extend(this.options, options || {});
        this.element = this.attachToDom();
        var self = this;
        var header = $(this.element).find('.rf-dt-thd');
        header.find(".rf-dt-c-srt").each(function() {
            $(this).bind("click", {sortHandle: this}, $.proxy(self.sortHandler, self));
        });
        header.find(".rf-dt-flt-i").each(function() {
            $(this).bind("blur", {filterHandle: this}, $.proxy(self.filterHandler, self));
        });
        $(this.element).trigger("rich:ready", this);
    };

    rf.BaseComponent.extend(rf.ui.DataTable);
    var $super = rf.ui.DataTable.$super;

    $.extend(rf.ui.DataTable, {
            SORTING: "rich:sorting",
            FILTERING: "rich:filtering",
            SUBTABLE_SELECTOR:".rf-cst"
        });

    $.extend(rf.ui.DataTable.prototype, ( function () {

        var invoke = function(event, attributes) {
            rf.ajax(this.id, event, {"parameters" : attributes});
        };

        var createParameters = function(type, id, arg1, arg2) {
            var parameters = {};
            var key = this.id + type;
            parameters[key] = (id + ":" + (arg1 || "") + ":" + arg2);

            var eventOptions = this.options.ajaxEventOption;
            for (key in eventOptions) {
                if (eventOptions.hasOwnProperty(key) && !parameters[key]) {
                    parameters[key] = eventOptions[key];
                }
            }
            return parameters;
        };


        return {

            name : "RichFaces.ui.DataTable",

            sort: function(columnId, direction, isClear) {
                invoke.call(this, null, createParameters.call(this, rf.ui.DataTable.SORTING, columnId, direction, isClear));
            },

            clearSorting: function() {
                this.sort("", "", true);
            },

            sortHandler: function(event) {
                var sortHandle = $(event.data.sortHandle);
                var button = sortHandle.find('.rf-dt-srt-btn');
                var columnId = button.data('columnid');
                var sortOrder = button.hasClass('rf-dt-srt-asc') ? 'descending' : 'ascending';
                this.sort(columnId, sortOrder, false);
            },

            filter: function(columnId, filterValue, isClear) {
                invoke.call(this, null, createParameters.call(this, rf.ui.DataTable.FILTERING, columnId, filterValue, isClear));
            },

            clearFiltering: function() {
                this.filter("", "", true);
            },

            filterHandler: function(event) {
                var filterHandle = $(event.data.filterHandle);
                var columnId = filterHandle.data('columnid');
                var filterValue = filterHandle.val();
                this.filter(columnId, filterValue, false);
            },

            expandAllSubTables: function() {
                this.invokeOnSubTables('expand');
            },

            collapseAllSubTables: function() {
                this.invokeOnSubTables('collapse');
            },

            switchSubTable: function(id) {
                this.getSubTable(id).switchState();
            },

            getSubTable: function(id) {
                return rf.component(id);
            },

            invokeOnSubTables: function(funcName) {
                var elements = $(document.getElementById(this.id)).children(rf.ui.DataTable.SUBTABLE_SELECTOR);
                var invokeOnComponent = this.invokeOnComponent;
                elements.each(
                    function() {
                        if (this.firstChild && this.firstChild[rf.RICH_CONTAINER] && this.firstChild[rf.RICH_CONTAINER].component) {
                            var component = this.firstChild[rf.RICH_CONTAINER].component;
                            if (component instanceof RichFaces.ui.CollapsibleSubTable) {
                                invokeOnComponent(component, funcName);
                            }
                        }
                    }
                );
            },

            invokeOnSubTable: function(id, funcName) {
                var subtable = this.getSubTable(id);
                this.invokeOnComponent(subtable, funcName);
            },

            invokeOnComponent: function(component, funcName) {
                if (component) {
                    var func = component[funcName];
                    if (typeof func == 'function') {
                        func.call(component);
                    }
                }
            },
            
            __getMenuSelector: function (menu) {
                var selector = "[id='" + this.element.id + "'] ";
                selector += (typeof menu.options.targetSelector === 'undefined')
                    ?  ".rf-dt-b td" : menu.options.targetSelector;
                selector = $.trim(selector);
                return selector;
            },
            
            contextMenuAttach: function (menu) {
                var selector = this.__getMenuSelector(menu);
                rf.Event.bind(selector, menu.options.showEvent, $.proxy(menu.__showHandler, menu), menu);
            },
            
            contextMenuDetach: function (menu) {
                var selector = this.__getMenuSelector(menu);
                rf.Event.unbind(selector, menu.options.showEvent);
            },
            
            destroy: function() {
                $super.destroy.call(this);
            }
        }

    })());

})(RichFaces.jQuery, window.RichFaces);


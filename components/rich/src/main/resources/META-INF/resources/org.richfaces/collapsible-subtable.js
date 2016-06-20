(function ($, rf) {

    rf.ui = rf.ui || {};

    /**
     * Backing object for rich:collapsibleSubTable
     * 
     * @extends RichFaces.BaseComponent
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.CollapsibleSubTable
     * 
     * @param id
     * @param f
     * @param options
     */
    rf.ui.CollapsibleSubTable = function(id, f, options) {
        this.id = id;
        this.options = $.extend(this.options, options || {});
        this.stateInput = options.stateInput;
        this.optionsInput = options.optionsInput;
        this.expandMode = options.expandMode || rf.ui.CollapsibleSubTable.MODE_CLNT;
        this.eventOptions = options.eventOptions;
        this.formId = f;
        this.isNested = options.isNested;

        if (! this.isNested) {
            var self = this;
            var tbody = $(document.getElementById(this.id)).parent();
            tbody.find(".rf-dt-c-srt").each(function() {
                $(this).bind("click", {sortHandle: this}, $.proxy(self.sortHandler, self));
            });
            tbody.find(".rf-dt-flt-i").each(function() {
                $(this).bind("blur", {filterHandle: this}, $.proxy(self.filterHandler, self));
                $(this).bind("keyup", {filterHandle: this}, $.proxy(self.filterHandler, self));
            });

        }

        this.attachToDom();
    };

    $.extend(rf.ui.CollapsibleSubTable, {
            MODE_AJAX: "ajax",
            MODE_SRV: "server",
            MODE_CLNT: "client",
            collapse: 0,
            expand: 1,
            SORTING: "rich:sorting",
            FILTERING: "rich:filtering"
        });

    rf.BaseComponent.extend(rf.ui.CollapsibleSubTable);
    var $super = rf.ui.CollapsibleSubTable.$super;

    $.extend(rf.ui.CollapsibleSubTable.prototype, (function () {

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

        var element = function() {
                // use parent tbody as parent dom elem
            return $(document.getElementById(this.id)).parent();
        };
        
        var nestedElements = function() {
                // return children rows - id contains the id of parent
            var childrenRegex = new RegExp("^" + this.id + "\\:\\d+\\:");
            return $(document.getElementById(this.id)).parent().find("tr").filter(function() {
                return this.id.match(childrenRegex);
            });
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

            /**
             * Sort a table column
             * 
             * @method
             * @name RichFaces.ui.CollapsibleSubDataTable#sort
             * @param columnId {string} short column id
             * @param [direction] {string} sort direction ("ascending", "descending", "unsorted")
             *          if not provided and the column is sorted the direction switches, if it's unsorted it will be sorted in ascending sirection
             * @param [isClear] {boolean} whether or not to clear the previous sort, default - false
             */
            sort: function(columnId, direction, isClear) {
                invoke.call(this, null, createParameters.call(this, rf.ui.CollapsibleSubTable.SORTING, columnId, direction, isClear));
            },

            /**
             * Clear any sorting currently applied to the table
             * 
             * @method
             * @name RichFaces.ui.CollapsibleSubDataTable#clearSorting
             */
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

            /**
             * Filter a table column
             * 
             * @method
             * @name RichFaces.ui.CollapsibleSubDataTable#filter
             * @param columnId {string} short column id
             * @param filterValue {string} value to filter by
             * @param [isClear] {boolean} whether or not to clear the previous filter, default - false
             */
            filter: function(columnId, filterValue, isClear) {
                invoke.call(this, null, createParameters.call(this, rf.ui.CollapsibleSubTable.FILTERING, columnId, filterValue, isClear));
            },

            /**
             * Clear any filtering currently applied to the table
             * 
             * @method
             * @name RichFaces.ui.CollapsibleSubDataTable#clearFiltering
             */
            clearFiltering: function() {
                this.filter("", "", true);
            },

            filterHandler: function(event) {
                if (event.type == "keyup" && event.keyCode != 13) {
                    return;
                }
                var filterHandle = $(event.data.filterHandle);
                var columnId = filterHandle.data('columnid');
                var filterValue = filterHandle.val();
                this.filter(columnId, filterValue, false);
            },

            switchState: function(e, options) {
                if (this.expandMode == rf.ui.CollapsibleSubTable.MODE_AJAX) {
                    ajax.call(this, e, this.eventOptions, options);
                } else if (this.expandMode == rf.ui.CollapsibleSubTable.MODE_SRV) {
                    server.call(this, options);
                } else if (this.expandMode == rf.ui.CollapsibleSubTable.MODE_CLNT) {
                    client.call(this, options);
                }
            },

            /**
             * Collapse this subtable
             * 
             * @method
             * @name RichFaces.ui.CollapsibleSubDataTable#collapse
             */
            collapse: function(options) {
                this.setState(rf.ui.CollapsibleSubTable.collapse);
                if (this.isNested) {
                    nestedElements.call(this).hide();
                }
                else {
                    element.call(this).hide();
                }
            },

            /**
             * Expand this subtable
             * 
             * @method
             * @name RichFaces.ui.CollapsibleSubDataTable#expand
             */
            expand: function(options) {
                this.setState(rf.ui.CollapsibleSubTable.expand);
                
                if (!this.isNested) {
                    element.call(this).show();
                }
                else {
                        // return first level children only
                    var subtableRegex = new RegExp("^" + this.id + "\\:\\d+\\:[^\\:]+$");
                    nestedElements.call(this).filter(function () {
                        return this.id.match(subtableRegex);
                    }).each(function() {
                        if (this.rf) { // is a nested subtable, show content if necessary
                            if (this.rf.component.isExpanded()) {
                                this.rf.component.expand();
                            }
                        }
                        else { // is a subtable toggler, always show
                            $(this).show();
                        }
                    });
                }
            },

            /**
             * Returns true if this subtable is expanded
             * 
             * @method
             * @name RichFaces.ui.CollapsibleSubDataTable#isExpanded
             * @return {boolean} true if this subtable is expanded
             */
            isExpanded: function() {
                return (parseInt(this.getState()) == rf.ui.CollapsibleSubTable.expand);
            },

            __switchState: function(options) {
                var state = this.isExpanded() ? rf.ui.CollapsibleSubTable.collapse : rf.ui.CollapsibleSubTable.expand;
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

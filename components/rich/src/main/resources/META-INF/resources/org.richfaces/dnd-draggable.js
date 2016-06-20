/*
 * code review by Pavel Yaschenko
 * 
 * No event's unbindings when component would be destroyed 
 * Hint: easy way to unbind - use namespaces when bind event handlers
 * 
 */

(function ($, rf) {

    rf.ui = rf.ui || {};

    var defaultIndicatorClasses = {
        rejectClass : "rf-ind-rejt",
        acceptClass : "rf-ind-acpt",
        draggingClass : "rf-ind-drag"
    };

    /**
     * Backing object for rich:dragSource
     * 
     * @extends RichFaces.BaseNonVisualComponent
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.Draggable
     * 
     * @param id
     * @param options
     */
    rf.ui.Draggable = function(id, options) {
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $super.constructor.call(this, id);

        this.id = id;

        this.namespace = this.namespace || "."
            + rf.Event.createNamespace(this.name, this.id);

        this.parentId = this.options.parentId;
        this.attachToDom(this.parentId);
        this.dragElement = $(document.getElementById(this.options.parentId));
        this.dragElement.draggable();
        
        if (options.dragOptions) {
            this.dragElement.draggable("option", options.dragOptions);
        }

        if (options.dragOptions && options.dragOptions.helper) {
            this.dragElement.data("indicator", false);
        } else if (options.indicator) {
            var element = $(document.getElementById(options.indicator));
            var clone = element.clone();
            $("*[id]", clone).andSelf().each(function() {
                $(this).removeAttr("id");
            });
            if (element.attr("id")) {
                clone.attr("id", element.attr("id") + "Clone");
            }
            
            this.dragElement.data("indicator", true);
            this.dragElement.draggable("option", "helper", function() {
                return clone;
            });
        } else {
            this.dragElement.data("indicator", false);
            this.dragElement.draggable("option", "helper", 'clone');
        }

        this.dragElement.draggable("option", "addClasses", false);
        this.dragElement.draggable( "option", "appendTo", "body" );
        

        this.dragElement.data('type', this.options.type);
        this.dragElement.data("init", true);
        this.dragElement.data("id", this.id);
        rf.Event.bind(this.dragElement, 'dragstart' + this.namespace, this.dragStart, this);
        rf.Event.bind(this.dragElement, 'drag' + this.namespace, this.drag, this);
    };

    rf.BaseNonVisualComponent.extend(rf.ui.Draggable);

    // define super class link
    var $super = rf.ui.Draggable.$super;

    var defaultOptions = {
    };

    $.extend(rf.ui.Draggable.prototype, ( function () {
        return {
            name : "Draggable",
            dragStart: function(e) {
                var ui = e.rf.data;
                var element = ui.helper[0];
                this.parentElement = element.parentNode;

                if (this.__isCustomDragIndicator()) {
                    ui.helper.detach().appendTo("body").show();
                    
                    // move cursor to the center of custom dragIndicator;
                    var left = (ui.helper.width() / 2);
                    var top = (ui.helper.height() / 2);
                    this.dragElement.data('ui-draggable').offset.click.left = left;
                    this.dragElement.data('ui-draggable').offset.click.top = top;
                    
                }
            },

            drag: function(e) {
                var ui = e.rf.data;
                if (this.__isCustomDragIndicator()) {
                    var indicator = rf.component(this.options.indicator);
                    if (indicator) {
                        ui.helper.addClass(indicator.getDraggingClass());
                    } else {
                        ui.helper.addClass(defaultIndicatorClasses.draggingClass);
                    }
                }
                this.__clearDraggableCss(ui.helper);
            },

            __isCustomDragIndicator: function() {
                return this.dragElement.data("indicator");
            },

            __clearDraggableCss: function(element) {
                if (element && element.removeClass) {
                    //draggable 'addClasses: false' doesn't work so clear jQuery style
                    element.removeClass("ui-draggable-dragging");
                }
            },

            destroy : function() {
                // clean up code here
                this.detach(this.parentId);
                rf.Event.unbind(this.dragElement, this.namespace);
                // call parent's destroy method
                $super.destroy.call(this);
            }
        }
    })());
})(RichFaces.jQuery, window.RichFaces);

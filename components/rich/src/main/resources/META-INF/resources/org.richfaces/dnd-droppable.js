/*
 * code review by Pavel Yaschenko
 * 
 * No event's unbindings when component would be destroyed Hint: easy way to
 * unbind - use namespaces when bind event handlers
 * 
 */

(function($, rf) {

    rf.ui = rf.ui || {};

    var defaultIndicatorClasses = {
        rejectClass : "rf-ind-rejt",
        acceptClass : "rf-ind-acpt",
        draggingClass : "rf-ind-drag"
    };

    var defaultOptions = {
    };

    /**
     * Backing object for rich:dropTarget
     * 
     * @extends RichFaces.BaseNonVisualComponent
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.Droppable
     * 
     * @param id
     * @param options
     */
    rf.ui.Droppable = function(id, options) {
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $super.constructor.call(this, id);

        this.namespace = this.namespace || "."
            + rf.Event.createNamespace(this.name, this.id);

        this.id = id;

        this.parentId = this.options.parentId;

        this.attachToDom(this.parentId);

        this.dropElement = $(document.getElementById(this.parentId));
        this.dropElement.droppable({
                addClasses : false
            });
        this.dropElement.data("init", true);

        rf.Event.bind(this.dropElement, 'drop' + this.namespace, this.drop, this);
        rf.Event.bind(this.dropElement, 'dropover' + this.namespace, this.dropover, this);
        rf.Event.bind(this.dropElement, 'dropout' + this.namespace, this.dropout, this);

    };

    rf.BaseNonVisualComponent.extend(rf.ui.Droppable);

    var $super = rf.ui.Droppable.$super;

    $.extend(rf.ui.Droppable.prototype, (function() {
        return {
            name: "Droppable",
            drop : function(e) {
                var ui = e.rf.data;
                if (this.accept(ui.draggable)) {
                    this.__callAjax(e, ui);
                }
                var dragIndicatorObj;
                if (ui.draggable.data('indicator')) {
                    dragIndicatorObj = this.__getIndicatorObject(ui.helper);
                }
                if (dragIndicatorObj) {
                    ui.helper.removeClass(dragIndicatorObj.getAcceptClass());
                    ui.helper.removeClass(dragIndicatorObj.getRejectClass());
                } else {
                    ui.helper.removeClass(defaultIndicatorClasses.acceptClass);
                    ui.helper.removeClass(defaultIndicatorClasses.rejectClass);
                }
            },

            dropover : function(e) {
                var ui = e.rf.data;
                var draggable = ui.draggable;
                var dragIndicatorObj;
                if (ui.draggable.data('indicator')) {
                    dragIndicatorObj = this.__getIndicatorObject(ui.helper);
                }
                this.dropElement.addClass("rf-drp-hvr");
                if (dragIndicatorObj) {
                    if (this.accept(draggable)) {
                        ui.helper.removeClass(dragIndicatorObj.getRejectClass());
                        ui.helper.addClass(dragIndicatorObj.getAcceptClass());
                        this.dropElement.addClass("rf-drp-hlight");
                    } else {
                        ui.helper.removeClass(dragIndicatorObj.getAcceptClass());
                        ui.helper.addClass(dragIndicatorObj.getRejectClass());
                        this.dropElement.removeClass("rf-drp-hlight");
                    }
                } else {
                    if (this.accept(draggable)) {
                        ui.helper.removeClass(defaultIndicatorClasses.rejectClass);
                        ui.helper.addClass(defaultIndicatorClasses.acceptClass);
                        this.dropElement.addClass("rf-drp-hlight");
                    } else {
                        ui.helper.removeClass(defaultIndicatorClasses.acceptClass);
                        ui.helper.addClass(defaultIndicatorClasses.rejectClass);
                        this.dropElement.removeClass("rf-drp-hlight");
                    }
                }
            },

            dropout : function(e) {
                var ui = e.rf.data;
                var draggable = ui.draggable;
                var dragIndicatorObj;
                if (ui.draggable.data('indicator')) {
                    dragIndicatorObj = this.__getIndicatorObject(ui.helper);
                }
                this.dropElement.removeClass("rf-drp-hvr rf-drp-hlight");
                if (dragIndicatorObj) {
                    ui.helper.removeClass(dragIndicatorObj.getAcceptClass());
                    ui.helper.removeClass(dragIndicatorObj.getRejectClass());
                } else {
                    ui.helper.removeClass(defaultIndicatorClasses.acceptClass);
                    ui.helper.removeClass(defaultIndicatorClasses.rejectClass);
                }
            },

            accept : function(draggable) {
                var accept = false;
                var acceptType = draggable.data("type");
                if (acceptType && this.options.acceptedTypes) {
                    $.each(this.options.acceptedTypes, function() {
                        if (this == "@none") {
                            return false;
                        }

                        if (this == acceptType || this == "@all") {
                            accept = true;
                            return false;
                        }
                    });
                }
                return accept;
            },

            __getIndicatorObject: function(helper) {
                var indicatorCloneId = helper.attr('id');
                if (indicatorCloneId) {
                    var indicatorId = indicatorCloneId.match(/(.*)Clone$/)[1];
                    return rf.component(indicatorId);
                }
            },

            __callAjax : function(e, ui) {
                if (ui.draggable) {
                    var dragSource = ui.draggable.data("id");
                    var ajaxFunc = this.options.ajaxFunction;
                    if (ajaxFunc && typeof ajaxFunc == 'function') {
                        ajaxFunc.call(this, e, dragSource);
                    }
                }
            },

            destroy : function() {
                // clean up code here
                this.detach(this.parentId);
                rf.Event.unbind(this.dropElement, this.namespace);

                // call parent's destroy method
                $super.destroy.call(this);

            }


        }
    })());

})(RichFaces.jQuery, window.RichFaces);
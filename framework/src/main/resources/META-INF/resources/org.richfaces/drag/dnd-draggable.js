/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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

        if (options.indicator) {
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
                    this.dragElement.data('draggable').offset.click.left = left;
                    this.dragElement.data('draggable').offset.click.top = top;
                    
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
})(RichFaces.jQuery, RichFaces);

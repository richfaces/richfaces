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

/**
 * @author Ilya Shaikovsky
 * @author Lukas Fryc
 */

(function($, rf) {

    rf.ui = rf.ui || {};

    var defaultOptions = {
        enabledInInput : false,
        preventDefault : true
    };
    
    var types = [ 'keydown', 'keyup' ];

    rf.ui.HotKey = function(componentId, options) {
        $super.constructor.call(this, componentId);
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
        this.attachToDom(this.componentId);
        this.options = $.extend({}, defaultOptions, options);
        this.__handlers = {};
        
        this.options.selector = (this.options.selector) ? this.options.selector : document;

        $(document).ready($.proxy(function() {
            this.__bindDefinedHandlers();
        }, this));
    };

    rf.BaseComponent.extend(rf.ui.HotKey);

    var $super = rf.ui.HotKey.$super;

    $.extend(rf.ui.HotKey.prototype, {

        name : "HotKey",
        
        __bindDefinedHandlers : function() {
            for (var i = 0; i < types.length; i++) {
                if (this.options['on' + types[i]]) {
                    this.__bindHandler(types[i]);
                }
            }
        },
        
        __bindHandler : function(type) {
            this.__handlers[type] = $.proxy(function(event) {
                var result = this.invokeEvent.call(this, type, document.getElementById(this.id), event);
                if (this.options.preventDefault) {
                    event.stopPropagation();
                    event.preventDefault();
                    return false;
                }
                return result;
            }, this);
            $(this.options.selector).bind(type + this.namespace, this.options, this.__handlers[type]);
        },

        destroy : function() {
            rf.Event.unbindById(this.id, this.namespace);

            for (var type in this.__handlers) {
                if (this.__handlers.hasOwnProperty(type)) {
                    $(this.options.selector).unbind(type + this.namespace, this.__handlers[type]);
                }
            }

            $super.destroy.call(this);
        }
    });

})(RichFaces.jQuery, RichFaces);
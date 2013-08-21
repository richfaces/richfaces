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
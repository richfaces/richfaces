/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
(function($, rf) {

    var getGlobalStatusNameVariable = function() {
        return rf.statusName;
    }

    var RICHFACES_AJAX_STATUS = "richfaces:ajaxStatus";

    var getStatusDataAttributeName = function(statusName) {
        return statusName ? (RICHFACES_AJAX_STATUS + "@" + statusName) : RICHFACES_AJAX_STATUS;
    };

    var statusAjaxEventHandler = function(data, methodName) {
        if (methodName) {
            //global status name
            var statusName = getGlobalStatusNameVariable();
            var source = data.source;

            var statusApplied = false;
            var statusDataAttribute = getStatusDataAttributeName(statusName);

            var statusContainers;
            if (statusName) {
                statusContainers = [$(document)];
            } else {
                // the element reference will be stale if it was rerendered
                // we need to find the current element in the DOM
                var currentSource = document.getElementById(source.id);

                statusContainers = [$(currentSource).parents('form'), $(document)];
            }

            for (var containerIdx = 0; containerIdx < statusContainers.length && !statusApplied;
                 containerIdx++) {

                var statusContainer = statusContainers[containerIdx];
                var statuses = statusContainer.data(statusDataAttribute);
                if (statuses) {
                    for (var statusId in statuses) {
                        if (statuses.hasOwnProperty(statusId)) {
                            var status = statuses[statusId];
                            var result = status[methodName].apply(status, arguments);
                            if (result) {
                                statusApplied = true;
                            } else {
                                delete statuses[statusId];
                            }
                        }
                    }

                    if (!statusApplied) {
                        statusContainer.removeData(statusDataAttribute);
                    }
                }
            }
        }
    };

    var initializeStatuses = function() {
        var thisFunction = arguments.callee;
        if (!thisFunction.initialized) {
            thisFunction.initialized = true;

            var jsfEventsListener = rf.createJSFEventsAdapter({
                    begin: function(event) {
                        statusAjaxEventHandler(event, 'start');
                    },
                    error: function(event) {
                        statusAjaxEventHandler(event, 'error');
                    },
                    success: function(event) {
                        statusAjaxEventHandler(event, 'success');
                    },
                    complete: function() {
                        rf.setGlobalStatusNameVariable(null);
                    }
                });

            jsf.ajax.addOnEvent(jsfEventsListener);
            //TODO blocks default alert error handler
            jsf.ajax.addOnError(jsfEventsListener);
        }
    };

    rf.ui = rf.ui || {};
    
    rf.ui.Status = rf.BaseComponent.extendClass({

            name: "Status",
            
            /**
             * Backing object for a4j:status
             * 
             * @extends RichFaces.BaseComponent
             * @memberOf! RichFaces.ui
             * @constructs RichFaces.ui.Status
             * 
             * @param id {string} component id
             * @param [options] {Object} status options
             */
            //TODO - support for parallel requests
            init: function(id, options) {
                this.id = id;
                this.attachToDom();
                this.options = options || {};
                this.register();
            },

            register: function() {
                initializeStatuses();

                var statusName = this.options.statusName;
                var dataStatusAttribute = getStatusDataAttributeName(statusName);

                var container;
                if (statusName) {
                    container = $(document);
                } else {
                    container = $(rf.getDomElement(this.id)).parents('form');
                    if (container.length == 0) {
                        container = $(document);
                    }
                    ;
                }

                var statuses = container.data(dataStatusAttribute);
                if (!statuses) {
                    statuses = {};
                    container.data(dataStatusAttribute, statuses);
                }

                statuses[this.id] = this;
            },

            /**
             * Switches status to the stop state.
             * 
             * @method
             * @name RichFaces.ui.Status#start
             */
            start: function() {
                if (this.options.onstart) {
                    this.options.onstart.apply(this, arguments);
                }

                return this.__showHide('.rf-st-start');
            },

            /**
             * Switches status to the stop state.
             * 
             * @method
             * @name RichFaces.ui.Status#stop
             */
            stop: function() {
                this.__stop();
                return this.__showHide('.rf-st-stop');
            },

            success: function() {
                if (this.options.onsuccess) {
                    this.options.onsuccess.apply(this, arguments);
                }
                return this.stop();
            },

            /**
             * Switches status to the error state.
             * 
             * @method
             * @name RichFaces.ui.Status#error
             */
            error: function() {
                if (this.options.onerror) {
                    this.options.onerror.apply(this, arguments);
                }
                this.__stop();

                return this.__showHide(':not(.rf-st-error) + .rf-st-stop, .rf-st-error');
            },

            __showHide: function(selector) {
                var element = $(rf.getDomElement(this.id));
                if (element) {
                    var statusElts = element.children();
                    statusElts.each(function() {
                        var t = $(this);
                        t.css('display', t.is(selector) ? '' : 'none');
                    });

                    return true;
                }
                return false;
            },

            __stop: function () {
                if (this.options.onstop) {
                    this.options.onstop.apply(this, arguments);
                }
            }
        });
}(RichFaces.jQuery, window.RichFaces));

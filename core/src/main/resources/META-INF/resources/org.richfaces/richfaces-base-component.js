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
 * @author Pavel Yaschenko
 */

window.RichFaces = window.RichFaces || {};
RichFaces.jQuery = RichFaces.jQuery || window.jQuery;


(function ($, rf, params) {

    rf.blankFunction = function () {
    }; //TODO: add it to global library

    /**
     * Base class for all components.
     * All RichFaces components should use this class as base or another RichFaces class which based on it.
     *
     <pre><code>
     //Inheritance example:
     (function ($, richfaces, params) {

         // Constructor definition
         richfaces.MyComponent = function(componentId, [options]) {
             // call constructor of parent class
             $super.constructor.call(this, componentId, [options]);

             <span style="color:red">
             // call this.attachToDom method to attach component to dom element
             // its required for the client side API calls 
             // and to clean up after ajax request or page unload:
             // destroy method will be called if component attached to dom
             this.attachToDom(componentId);
             </span>
             };

             // define private method
             var myPrivateMethod = function () {
             }

             // Extend component class and add protected methods from parent class to our container
             richfaces.BaseComponent.extend(richfaces.BaseComponent, richfaces.MyComponent);

             // define super class link
             var $super = richfaces.MyComponent.$super;

             // Add new properties and methods
             $.extend(richfaces.MyComponent.prototype, (function (params) {
                 return {
                     name:"MyComponent",
                     f:function (){alert("hello"),
                     // destroy method definition for clean up
                     destroy: function () {
                         // clean up code here
                    
                         // call parent's destroy method
                         $super.destroy.call(this);
                     }
                 }
             };
         })(params));
     })(jQuery, RichFaces);
     </code></pre>
     *
     * @class
     * @alias RichFaces.BaseComponent
     * @param {string} componentId - component id
     * */
    rf.BaseComponent = function(componentId) {
        this.id = componentId;
        this.options = this.options || {};
    };

    var $p = {};

    var extend = function (parent, child, h) {
        h = h || {};
        var F = rf.blankFunction;
        F.prototype = parent.prototype;
        child.prototype = new F();
        child.prototype.constructor = child;
        child.$super = parent.prototype;
        if (child.$super == rf.BaseComponent.prototype) {
            var r = jQuery.extend({}, $p, h || {});
        }

        var _parent = child;

        // create wrapper with protected methods and variables
        child.extend = function (_child, _h) {
            _h = _h || {};
            var _r = jQuery.extend({}, r || h || {}, _h || {});
            return extend(_parent, _child, _r);
        }
        return r || h;
    };

    /**
     * Method extends child class prototype with parent prototype
     * and return the object with parent's protected methods
     *
     * @method
     * @name RichFaces.BaseComponent.extend
     *
     * @return {object}
     * */
    rf.BaseComponent.extend = function(child, h) {
        return extend(rf.BaseComponent, child, h);
    };


    /**
     * Easy way to create a subclass.
     *
     * Example:
     *
     * RichFaces.ui.MyClass = RichFaces.BaseComponent.extendClass({
     *     // Class name
     *     name: "MyClass",
     *
     *     // Constructor
     *     init : function (...) {
     *         // ...
     *     },
     *
     *     // public api
     *     publicFunction : function () {
     *         // ...
     *     },
     *
     *     // private api
     *     // names of private methods should start with '__' (2 underscore symbols)
     *     __privateFunction : function () {
     *         // ...
     *     },
     *
     *     __overrideMethod : function () {
     *         // if you need to use method from parent class use link to parent prototype
     *         // like in previous solution with extend method
     *         $super.__overrideMethod.call(this, ...params...);
     *
     *         //...
     *     }
     *
     * });
     *
     * RichFaces.ui.MySecondClass = RichFaces.ui.MyClass({
     *     //
     *     name : "MySecondClass",
     *
     *     // Constructor
     *     init : function (...) {
     *         // ...
     *     }
     *
     * })
     *
     * */
    rf.BaseComponent.extendClass = function (methods) {
        var DerivedClass = methods.init || rf.blankFunction;
        var SupperClass = this;

        SupperClass.extend(DerivedClass);

        DerivedClass.extendClass = SupperClass.extendClass;

        $.extend(DerivedClass.prototype, methods);

        return DerivedClass;
    };

    $.extend(rf.BaseComponent.prototype, (function (params) {
        return {
            /**
             * Component name.
             *
             * @name RichFaces.BaseComponent#name
             * @type {string}
             * */
            name: "BaseComponent",

            /**
             * Method for converting object to string
             *
             * @method
             * @name RichFaces.BaseComponent#toString
             *
             * @return {string}
             * */
            toString: function() {
                var result = [];
                if (this.constructor.$super) {
                    result[result.length] = this.constructor.$super.toString();
                }
                result[result.length] = this.name;
                return result.join(', ');
            },

            getValue: function() {
                return;
            },

            /**
             * Method returns element's id for event handlers binding.
             * Event API calls this method when binding by component object as selector was used.
             *
             * @method
             * @name RichFaces.BaseComponent#getEventElement
             *
             * @return {string}
             * */
            getEventElement: function() {
                return this.id;
            },

            /**
             * Attach component object to DOM element by component id, DOM element or jQuery object and returns the element
             * Its required for the client side API calls and to clean up after ajax request or document unload by
             * calling destroy method
             *
             * @method
             * @name RichFaces.BaseComponent#attachToDom
             * @param {string|DOMElement|jQuery} source - component id, DOM element or DOM elements wrapped by jQuery
             *
             * @return {DOMElement}
             * */
            attachToDom: function(source) {
                source = source || this.id;
                var element = rf.getDomElement(source);
                if (element) {
                    var container = element[rf.RICH_CONTAINER] = element[rf.RICH_CONTAINER] || {};
                    container.component = this;
                }
                return element;
            },

            /**
             * Detach component object from DOM element by component id, DOM element or jQuery object
             *
             * @method
             * @name RichFaces.BaseComponent#detach
             * @param {string|DOMElement|jQuery} source - component id, DOM element or DOM elements wrapped by jQuery
             *
             * */
            detach: function(source) {
                source = source || this.id;
                var element = rf.getDomElement(source);
                element && element[rf.RICH_CONTAINER] && (element[rf.RICH_CONTAINER].component = null);
            },

            /**
             * Invokes event on on the DOM element
             * @method
             * @name RichFaces.BaseComponent#invokeEvent
             * @param eventType {string} event type, e.g. "click"
             * @param element {DOMElement} DOM element object
             * @param event {Event} jQuery Event
             * @param data {Object} additional data used for event handler
             * @return {boolean} true if an event is successfully invoked
             */
            invokeEvent: function(eventType, element, event, data) {
                var handlerResult, result;
                var eventObj = $.extend({}, event, {type: eventType});

                if (!eventObj) {
                    if (document.createEventObject) {
                        eventObj = document.createEventObject();
                        eventObj.type = eventType;
                    }
                    else if (document.createEvent) {
                        eventObj = document.createEvent('Events');
                        eventObj.initEvent(eventType, true, false);
                    }
                }
                eventObj[rf.RICH_CONTAINER] = {component:this, data: data};

                var eventHandler = this.options['on' + eventType];

                if (typeof eventHandler == "function") {
                    handlerResult = eventHandler.call(element, eventObj);
                }

                if (rf.Event) {
                    result = rf.Event.callHandler(this, eventType, data);
                }

                if (result != false && handlerResult != false) result = true;

                return result;
            },

            /**
             * Destroy method. Will be called before remove component from the page
             *
             * @method
             * @name RichFaces.BaseComponent#destroy
             *
             * */
            destroy: function() {
            }
        };
    })(params));

    rf.BaseNonVisualComponent = function(componentId) {
        this.id = componentId;
        this.options = this.options || {};
    };

    rf.BaseNonVisualComponent.extend = function(child, h) {
        return extend(rf.BaseNonVisualComponent, child, h);
    };

    rf.BaseNonVisualComponent.extendClass = function (methods) {
        var DerivedClass = methods.init || rf.blankFunction;
        var SupperClass = this;

        SupperClass.extend(DerivedClass);

        DerivedClass.extendClass = SupperClass.extendClass;

        $.extend(DerivedClass.prototype, methods);

        return DerivedClass;
    };

    $.extend(rf.BaseNonVisualComponent.prototype, (function (params) {
        return {
            name: "BaseNonVisualComponent",

            toString: function() {
                var result = [];
                if (this.constructor.$super) {
                    result[result.length] = this.constructor.$super.toString();
                }
                result[result.length] = this.name;
                return result.join(', ');
            },

            getValue: function() {
                return;
            },
            /**
             * Attach component object to DOM element by component id, DOM element or jQuery object and returns the element
             * Its required for the client side API calls and to clean up after ajax request or document unload by
             * calling destroy method
             *
             * @method
             * @name RichFaces.BaseNonVisualComponent#attachToDom
             * @param {string|DOMElement|jQuery} source - component id, DOM element or DOM elements wrapped by jQuery
             *
             * @return {DOMElement}
             * */
            attachToDom: function(source) {
                source = source || this.id;
                var element = rf.getDomElement(source);
                if (element) {
                    var container = element[rf.RICH_CONTAINER] = element[rf.RICH_CONTAINER] || {};
                    container.attachedComponents = container.attachedComponents || {};
                    container.attachedComponents[this.name] = container.attachedComponents[this.name] || [];
                    container.attachedComponents[this.name].push(this);
                }
                return element;
            },

            /**
             * Detach component object from DOM element by component id, DOM element or jQuery object
             *
             * @method
             * @name RichFaces.BaseNonVisualComponent#detach
             * @param {string|DOMElement|jQuery} source - component id, DOM element or DOM elements wrapped by jQuery
             *
             * */
            detach: function(source) {
                source = source || this.id;
                var element = rf.getDomElement(source);
                element && element[rf.RICH_CONTAINER] && (element[rf.RICH_CONTAINER].attachedComponents[this.name] = null);
            },

            /**
             * Destroy method. Will be called before remove component from the page
             *
             * @method
             * @name RichFaces.BaseNonVisualComponent#destroy
             *
             * */
            destroy: function() {
            }
        };
    })(params));


})(jQuery, window.RichFaces || (window.RichFaces = {}));

// RichFaces Base class for ui components
(function($, rf) {

    /**
     * @namespace RichFaces.ui
     * @memberOf! RichFaces
     */
    rf.ui = rf.ui || {};

    // Constructor definition
    rf.ui.Base = function(componentId, options, defaultOptions) {
        this.namespace = "." + rf.Event.createNamespace(this.name, componentId);
        // call constructor of parent class
        $super.constructor.call(this, componentId);
        this.options = $.extend(this.options, defaultOptions, options);
        this.attachToDom();
        this.__bindEventHandlers();
    };

    // Extend component class and add protected methods from parent class to our container
    rf.BaseComponent.extend(rf.ui.Base);

    // define super class link
    var $super = rf.ui.Base.$super;

    $.extend(rf.ui.Base.prototype, {
            __bindEventHandlers: function () {
            },
            destroy: function () {
                rf.Event.unbindById(this.id, this.namespace);
                $super.destroy.call(this);
            }
        });

})(RichFaces.jQuery, RichFaces);
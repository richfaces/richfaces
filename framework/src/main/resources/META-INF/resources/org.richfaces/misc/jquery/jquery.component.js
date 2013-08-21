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

window.RichFaces = window.RichFaces || {};
RichFaces.jQuery = RichFaces.jQuery || window.jQuery;

(function($, rf) {

    rf.ui = rf.ui || {};

    var evaluate = function(selector) {
        var result = selector;
        try {
            result = eval(selector);
        } catch (e) {
            //do nothing
        }
        return result;
    };

    var evaluateJQuery = function(element, selector) {
        var result = element || evaluate(selector);
        if (!(result instanceof $)) {
            result = $(result || "");
        }

        return result;
    };

    var createEventHandlerFunction = function(opts) {
        return function() {
            var selector = evaluateJQuery(null, opts.selector);
            selector[opts.attachType || "bind"](opts.event, null, new Function("event", opts.query));
        };
    };

    var createDirectQueryFunction = function(opts) {
        var queryFunction = new Function("options", "arguments[1]." + opts.query);

        return function() {
            var element;
            var options;

            if (arguments.length == 1) {
                //function(options) { ...query()... }
                options = arguments[0];
            } else {
                //function(element, options) { ...query()... }
                element = arguments[0];
                options = arguments[1];
            }

            var selector = evaluateJQuery(element, opts.selector);
            queryFunction.call(this, options, selector);
        };
    };

    var createQueryFunction = function(options) {
        if (options.event) {
            return createEventHandlerFunction(options);
        } else {
            return createDirectQueryFunction(options);
        }
    };

    var query = function(options) {
        if (options.timing == 'immediate') {
            createQueryFunction(options).call(this);
        } else {
            $(document).ready(createQueryFunction(options));
        }
    };

    rf.ui.jQueryComponent = {

        createFunction: createQueryFunction,

        query: query

    };

}(RichFaces.jQuery, RichFaces));

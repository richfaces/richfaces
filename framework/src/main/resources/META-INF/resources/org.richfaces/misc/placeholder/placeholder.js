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

(function ($, rf) {

    rf.ui = rf.ui || {};

    var defaultOptions = {
        useNative: false
    };

    rf.ui.Placeholder = rf.BaseComponent.extendClass({

        name:"Placeholder",

        init: function (componentId, options) {
            $super.constructor.call(this, componentId);
            options = $.extend({}, defaultOptions, options);
            this.attachToDom(this.id);
            $(function() {
                options.className = 'rf-plhdr ' + ((options.styleClass) ? options.styleClass : '');
                var elements = (options.selector) ? $(options.selector) : $(document.getElementById(options.targetId));
                // finds all inputs within the subtree of target elements
                var inputs = elements.find(':editable').andSelf().filter(':editable');
                inputs.watermark(options.text, options);
            });
        },
        // destructor definition
        destroy: function () {
            // define destructor if additional cleaning is needed but
            // in most cases its not nessesary.
            // call parent’s destructor
            $super.destroy.call(this);
        }
    });
    
    // once per all placeholders on a page
    $(function() {
        $(document).on('ajaxsubmit', 'form', $.watermark.hideAll);
        $(document).on('ajaxbegin', 'form', $.watermark.showAll);
            // need to use setTimeout to allow client's native reset to happen
        $(document).on('reset', 'form', function() {setTimeout( $.watermark.showAll, 0); });
    });
    
    // define super class reference - reference to the parent prototype
    var $super = rf.ui.Placeholder.$super;
})(RichFaces.jQuery, RichFaces);
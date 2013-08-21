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

(function($, rf) {
    
    rf.ui = rf.ui || {};
    
    var defaultOptions = {
        styleClass: '',
        nonblocking: false,
        nonblockingOpacity: 0.2,
        showHistory: false,
        animationSpeed: 'slow',
        opacity: '1',
        showShadow: false,
        showCloseButton: true,
        appearAnimation: 'fade',
        hideAnimation: 'fade',
        sticky: false,
        stayTime: 8000,
        delay: 0
    };
    
    var defaultStackId = "org.richfaces.notifyStack.default";
    
    var events = "click dblclick  keydown keypress keyup mousedown mousemove mouseout mouseover mouseup";
    
    var propertyTranslation = {
        'summary':'pnotify_title',
        'detail': 'pnotify_text',
        'styleClass': 'pnotify_addclass',
        'nonblocking': 'pnotify_nonblock',
        'nonblockingOpacity': 'pnotify_nonblock_opacity',
        'showHistory': 'pnotify_history',
        'animation': 'pnotify_animation',
        'appearAnimation': 'effect_in',
        'hideAnimation': 'effect_out',
        'animationSpeed': 'pnotify_animate_speed',
        'opacity': 'pnotify_opacity',
        'showShadow': 'pnotify_shadow',
        'showCloseButton': 'pnotify_closer',
        'sticky': 'pnotify_hide',
        'stayTime': 'pnotify_delay'
    };
    
    var severityClasses = ["rf-ntf-inf", "rf-ntf-wrn", "rf-ntf-err", "rf-ntf-ftl"];
    
    var translateProperties = function(target, source, translation) {
        for (var attr in source) {
            var targetAttr = translation[attr] != null ? translation[attr] : attr;
            target[targetAttr] = source[attr];
            if (target[targetAttr] instanceof Object) {
                target[targetAttr] = $.extend({}, target[targetAttr], translation);
            }
        }
        return target;
    };
    
    var getDefaultStack = function() {
        if (!document.getElementById(defaultStackId)) {
            var stackElement = $('<span id="' + defaultStackId + '" class="rf-ntf-stck" />');
            $('body').append(stackElement);
            new RichFaces.ui.NotifyStack(defaultStackId);
        }
        return getStack(defaultStackId);
    };
    
    var getStack = function(stackId) {
        if (!stackId) {
            return getDefaultStack();
        }
        return rf.component(stackId).getStack();
    };
    

    var array_remove = function(array, from, to) {
        var rest = array.slice((to || from) + 1 || array.length);
        array.length = from < 0 ? array.length + from : from;
        return array.push.apply(array, rest);
    };
    
    rf.ui.Notify = function(options) {
        var options = $.extend({}, defaultOptions, options);
        
        if (typeof options.severity == "number") {
            var severity = severityClasses[options.severity];
            options.styleClass = options.styleClass ? severity + " " + options.styleClass : severity;
        }
        
        var pnotifyOptions = translateProperties({}, options, propertyTranslation);

        var display = function() {
            var stack = getStack(options.stackId);
            pnotifyOptions.pnotify_stack = stack;
            pnotifyOptions.pnotify_addclass += ' rf-ntf-pos-' + stack.position;
            pnotifyOptions.pnotify_after_close = function(pnotify) {
                var index = $.inArray(pnotify, stack.notifications);
                if (index >= 0) {
                    array_remove(stack.notifications, index);
                }
            }
            var pnotify = $.pnotify(pnotifyOptions);
            pnotify.on(events, function(e) {
                if (options['on' + e.type]) {
                    options['on' + e.type].call(this, e);
                }
            });
            stack.addNotification(pnotify);
        }
        
        if (options.sticky !== null) {
            pnotifyOptions.pnotify_hide = !options.sticky;
        }
        
        $(document).ready(function() {
            if (options.delay) {
                setTimeout(function() {
                    display();
                }, options.delay);
            } else {
                display();
            }
        });
    };

})(jQuery, RichFaces);
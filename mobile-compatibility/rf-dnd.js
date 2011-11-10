/*!
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
 *
 *  Author: Dave Furfero
 *  Description: Adapted from https://github.com/furf/jquery-ui-touch-punch
 *  For use with the RichFaces Drag and Drop component on iOS
  */

(function (jQuery) {

    jQuery.support.touch = typeof Touch === 'object';

    if (!jQuery.support.touch) {
        return;
    }

    var mouseProto = jQuery.ui.mouse.prototype,
            _mouseInit = mouseProto._mouseInit,
            _mouseDown = mouseProto._mouseDown,
            _mouseUp = mouseProto._mouseUp,

            mouseEvents = {
                touchstart: 'mousedown',
                touchmove:  'mousemove',
                touchend:   'mouseup'
            };

    function makeMouseEvent(event) {

        var touch = event.originalEvent.changedTouches[0];

        return jQuery.extend(event, {
            type:    mouseEvents[event.type],
            which:   1,
            pageX:   touch.pageX,
            pageY:   touch.pageY,
            screenX: touch.screenX,
            screenY: touch.screenY,
            clientX: touch.clientX,
            clientY: touch.clientY
        });
    }

    mouseProto._mouseInit = function () {

        var self = this;

        self.element.bind('touchstart.' + self.widgetName, function (event) {
            return self._mouseDown(makeMouseEvent(event));
        });

        _mouseInit.call(self);
    };

    mouseProto._mouseDown = function (event) {

        var self = this,
                ret = _mouseDown.call(self, event);

        self._touchMoveDelegate = function (event) {
            return self._mouseMove(makeMouseEvent(event));
        };

        self._touchEndDelegate = function(event) {
            return self._mouseUp(makeMouseEvent(event));
        };

        jQuery(document)
                .bind('touchmove.' + self.widgetName, self._touchMoveDelegate)
                .bind('touchend.' + self.widgetName, self._touchEndDelegate);

        return ret;
    };

    mouseProto._mouseUp = function (event) {

        var self = this;

        jQuery(document)
                .unbind('touchmove.' + self.widgetName, self._touchMoveDelegate)
                .unbind('touchend.' + self.widgetName, self._touchEndDelegate);

        return _mouseUp.call(self, event);
    };

})(jQuery);
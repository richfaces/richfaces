/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
// @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
window.RichFaces = window.RichFaces || {};
window.RichFaces.Selenium = window.RichFaces.Selenium || {};
window.RichFaces.Selenium.Firefox = window.RichFaces.Selenium.Firefox || {};
window.RichFaces.Selenium.Firefox.Keyboard = window.RichFaces.Selenium.Firefox.Keyboard || {};
window.RichFaces.Selenium.Firefox.Keyboard.Workaround = window.RichFaces.Selenium.Firefox.Keyboard.Workaround || (function () {

    var invoked = false;
    var inserted = false;

    var body = document.getElementsByTagName('body')[0];

    var button = document.createElement('button');
    button.innerHTML = 'inserted button';
    button.onclick = function () {
        body.removeChild(wrapper);
        invoked = true;
        inserted = false;
    };

    var wrapper = wrapper || document.createElement('div');
    wrapper.style = 'z-index: 10000; position: fixed; padding: 10px;';
    wrapper.appendChild(button);

    return {
        isInvoked: function () {
            return invoked;
        },
        getActiveElement: function () {
            return document.activeElement;
        },
        getActiveElementTagName: function () {
            return this.getActiveElement().tagName;
        },
        insertButtonWorkaround: function () {
            if (!inserted) {
                body.insertBefore(wrapper, body.firstChild);
                inserted = true;
                invoked = false;
            }
        },
        getButtonElement: function () {
            return button;
        }
    };
})();
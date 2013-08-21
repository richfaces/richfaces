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

  rf.ui.toolbarHandlers = function(options) {
    if (options.id && options.events) {
      $('.rf-tb-itm', document.getElementById(options.id)).bind(
        options.events);
    }
    var groups = options.groups;
    if (groups && groups.length > 0) {
      var group;
      var i;
      for (i in groups) {
        group = groups[i];
        if (group) {
          var groupIds = group.ids;
          var y;
          var groupElements = [];
          for (y in groupIds) {
            groupElements.push(document.getElementById(groupIds[y]));
          }
          $(groupElements).bind(group.events);
        }
      }
    }
  }

})(RichFaces.jQuery, RichFaces);


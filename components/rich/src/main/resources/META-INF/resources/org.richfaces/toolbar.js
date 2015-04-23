(function ($, rf) {

    rf.ui = rf.ui || {};

  rf.ui.toolbarHandlers = function(options) {
      if (options.id && options.events) {
          $('.rf-tb-itm', document.getElementById(options.id)).bind(
              options.events);
      }
      var groups = options.groups;
      if (groups && groups.length > 0) {
          var group;
          for (var i = 0; i < groups.length; i++) {
              group = groups[i];
              if (group) {
                  var groupIds = group.ids;
                  var y;
                  var groupElements = [];
                  for (var y = 0; y < groupIds.length; y++) {
                      groupElements.push(document.getElementById(groupIds[y]));
                  }
                  $(groupElements).bind(group.events);
              }
          }
      }
  }

})(RichFaces.jQuery, RichFaces);
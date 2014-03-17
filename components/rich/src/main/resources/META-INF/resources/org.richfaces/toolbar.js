(function ($, rf) {

  rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

  rf.rf4.ui.toolbarHandlers = function(options) {
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
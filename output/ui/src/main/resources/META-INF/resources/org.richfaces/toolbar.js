function toolbarHandlers(options) {
    if (options.id && options.events) {
        jQuery('.rf-tb-itm', document.getElementById(options.id)).bind(
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
                jQuery(groupElements).bind(group.events);
            }
        }
    }
}

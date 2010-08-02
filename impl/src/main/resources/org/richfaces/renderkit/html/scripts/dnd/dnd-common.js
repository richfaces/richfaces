DnD.getDnDDefaultParams = function(elt) {
	var attr = Richfaces.getNSAttribute("defaultdndparams", elt);

	if (attr) {
		var params = attr.parseJSON(EventHandlersWalk);
		if (params) {
			return params;
		}
	}

	return {};
}

DnD.getDnDMergedParams = function(elt, name) {
	var params = DnD.getDnDDefaultParams(elt);

	var attr = Richfaces.getNSAttribute(name, elt);

	if (attr) {
		var dndParams = attr.parseJSON(EventHandlersWalk);
		if (params) {
			if (dndParams) {
				Object.extend(params, dndParams);
			}
		} else {
			params = dndParams;
		}
	}

	return params;
};

DnD.getDnDDragParams = function(elt) {
	return DnD.getDnDMergedParams(elt, "dragdndparams");
};

DnD.getDnDDropParams = function(elt) {
	return DnD.getDnDMergedParams(elt, "dropdndparams");
};

DnD.setDefaultDnDParams = function(params) {
	if (params) {
		if (drag && drag.source && drag.source.getDraggableItems && drag.source.getDraggableItems() > 1) {
	            
            var itemsCount = drag.source.getDraggableItems();
            params["count"] = itemsCount;
			if (!params["label"]) {
				params["label"] = params["count"] + " " + (params["units"] ? 
					params["units"] : "items");
			}
		}
	}
};

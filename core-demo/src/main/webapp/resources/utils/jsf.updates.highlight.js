jsf.ajax.addOnEvent(function(event) {
	if (event.status == 'success') {
		if (event.responseXML && jQuery.effects && jQuery.effects.highlight) {
			var partialUpdates = jQuery("partial-response > changes > update", event.responseXML);
			partialUpdates.each(function () {
				var id = jQuery(this).attr('id');
				if (id) {
					var updatedElt = document.getElementById(id);
					if (updatedElt) {
						var jue = jQuery(updatedElt);
						jue.effect("highlight", {}, 8000);
					}
				}
			});
		}
	}
});


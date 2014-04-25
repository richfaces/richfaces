/**
 * http://plugins.jquery.com/project/focus
 * JQUERY FOCUS 1.0.0
 * Submitted by erikkallen on November 9, 2009 - 5:31pm
 * http://plugins.jquery.com/node/11272/release
 **/
(function($) {

function sameOrChild(n1, n2) {
	// http://www.quirksmode.org/blog/archives/2006/01/contains_for_mo.html
	return n1 === n2 || (typeof(n1.contains) !== 'undefined' ? n1.contains(n2) : !!(n1.compareDocumentPosition(n2) & 16));
}

function focusHandler(event) {
	event = $.event.fix(event || window.event), $this = $(this), isFocused = $this.data('focus.isFocused');
	if (!isFocused) {
		$this.data('focus.isFocused', true);
		event.type = 'focusin';
		return $.event.handle.apply(this, [event]);
	}	
}

function blurHandler(event) {
	var args = [].slice.call(arguments, 1), $this = $(this);
	event = $.event.fix(event || window.event);

	window.setTimeout(function() {
		if (!sameOrChild($this.get(0), document.activeElement)) {
			$this.data('focus.isFocused', false);
			event.type = 'focusout';
			return $.event.handle.apply($this.get(0), [event]);
		}
	}, 0);
}

function setupEvents(elem) {
	var $elem = $(elem), ref = $elem.data('focus.handlerReferences') || 0;
	if (ref == 0) {
		if (elem.addEventListener) {
			elem.addEventListener('focus', focusHandler, true);
			elem.addEventListener('blur', blurHandler, true);
		}
		else {
			elem.onfocusin  = focusHandler;
			elem.onfocusout = blurHandler;
		}
	}
	$elem.data('focus.handlerReferences', ref + 1)
	$elem.data('focus.isFocused', sameOrChild(elem, document.activeElement));
}

function teardownEvents(elem) {
	var $elem = $(elem), ref = $elem.data('focus.handlerReferences') || 0;
	if (ref == 1) {
		if (elem.removeEventListener) {
			elem.removeEventListener('focus', focusHandler, true);
			elem.removeEventListener('blur', blurHandler, true);
		}
		else {
			elem.onfocusin  = null;
			elem.onfocusout = null;
		}
		$elem.removeData('focus.handlerReferences')
		$elem.removeData('focus.isFocused');
	}
	else {
		$elem.data('focus.handlerReferences', ref - 1);
	}
}

$.each(['focusin', 'focusout'], function(i, x) {
	$.event.special[x] = {
		setup: function() { setupEvents(this); },
		teardown: function() { teardownEvents(this); }
	};
});

$.fn.extend({
	focusin: function(fn) {
		return fn ? this.bind('focusin', fn) : this.trigger('focusin');
	},
	focusout: function(fn) {
		return fn ? this.bind('focusout', fn) : this.trigger('focusout');
	}
});

})(jQuery);

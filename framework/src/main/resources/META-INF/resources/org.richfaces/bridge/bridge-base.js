(function($) {

  /**
   * Base for JSF components built on top of Widget Factory pattern.
   *
   * The widget is destroyed by jQuery UI when called $.cleanData(e) on an element that widget is bound to.
   */
  $.widget('rf.bridgeBase', {

    _create : function() {
      this.element.data('rf.bridge', this);
    },

    _addUnitsIfRequired: function(options, property) {
      if (options[property] !== null) {
        if (parseInt(options[property]) == options[property]) {
          options[property] = options[property] + 'px';
        }
      }
    }

  });

}(RichFaces.jQuery));
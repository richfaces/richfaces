(function($) {

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
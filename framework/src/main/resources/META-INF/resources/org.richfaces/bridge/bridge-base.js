(function($) {

  $.widget('rf.bridgeBase', {

    options : {
      pluginNames : null
    },

    _create : function() {
      this.element.data('rf.bridge', this);
      this._registerCleanDomListener(this.element, this.options.pluginNames);
    },

    _destroy : function() {
      this._unRegisterCleanDomListener();
    },

    _registerCleanDomListener : function(element, pluginNames) {
      $('body').on("cleanDom.RICH", function(event, ui) {
        if ($.contains(ui.target, element)) {
          $.each(pluginNames, function() {
            var pluginName = this;
            $(element).data(pluginName).destroy();
          })

        }
      });
    },

    _unRegisterCleanDomListener : function() {
      $('body').off("cleanDom.RICH");
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
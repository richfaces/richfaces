(function($, rf) {

  $.widget('rf.richAutocompleteBridge', $.rf.bridgeBase, {

    options : {
      mode : 'cachedAjax',
      showButton : false,
      layout : 'list',
      minChars : 0,
      autoFocus : true,
      autoFill: true
    },

    _create : function() {
      this._super();

      var clientId = this.element.attr('id');
      var bridge = this;

      var autocompleteOptions = $.extend({}, this.options, {

        source : '[id="' + clientId + 'Suggestions"]',
        cached: (this.options.mode === 'cachedAjax'),
        update : function(request, done) {
          if (bridge.options.mode.match(/client/i)) {
            done();
            return;
          }
          var params = {};
          params[clientId + 'SearchTerm'] = request.term;
          rf.ajax(clientId, null, {
            parameters : params,
            error : done,
            complete : done
          });
        }
      });

      $(document.getElementById(clientId + 'Input')).richAutocomplete(autocompleteOptions);
    }
  });

}(RichFaces.jQuery, RichFaces));
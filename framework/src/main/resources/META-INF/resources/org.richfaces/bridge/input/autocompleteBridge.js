(function($, rf) {

  $.widget('rf.richAutocompleteBridge', $.rf.bridgeBase, {

    options : {
      mode : 'cachedAjax',
      showButton : false,
      minLength : 1,
      autoFocus : true,
      autoFill: true,
      filter: function(term, value) {
        if (term.length === 0) return true;
        if (value.indexOf(term) === 0) return true;
        return false;
      }
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
        },
        filter: function(array, term) {
          return $.grep(array, function(item) {
            var value = item.label || item.value || item;
            return bridge.options.filter(term.toLowerCase(), value.toLowerCase());
          });
        }
      });

      $(document.getElementById(clientId + 'Input')).richAutocomplete(autocompleteOptions);
    }
  });

}(RichFaces.jQuery, RichFaces));
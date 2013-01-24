(function() {
    var SourceRetriever = {

        getElementSource : function(element) {
            return element.outerHTML;
        }
    };
    
    window.SourceRetriever = SourceRetriever;
})();
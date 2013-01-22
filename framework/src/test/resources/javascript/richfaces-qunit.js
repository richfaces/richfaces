window.RichFaces = window.RichFaces || {};
RichFaces.QUnit = (function() {
    var setTestHeader = function (header) {
        var e = document.getElementById("qunit-header");
        e.innerHTML = header;
    };

    return {
        appendDomElements: function (parent, html) {
            var element = document.createElement("div");
            element.innerHTML = html;
            var elements = [], e;
            while (e = element.firstChild) {
                elements.push(e);
                parent.appendChild(e);
            }
            return elements;
        },
        removeDomElements: function (elements) {
            var element;
            while (elements.length > 0) {
                element = elements.pop();
                element.parentNode.removeChild(element);
            }
        },
        run: function (f) {
            jQuery(document).ready(f);
        }
    };
}());
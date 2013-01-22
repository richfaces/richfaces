RichFaces.QUnit
    .run(function() {
        module("jquery-position");

        var body = document.getElementsByTagName("body")[0];

        function testPositioning(element, left, top, comment) {
            equal(element.style.left, left + "px", comment);
            equal(element.style.top, top + "px", comment);
            equal(jQuery(element).offset().left, left, comment);
            equal(jQuery(element).offset().top, top, comment);
        }

        function testPositioning1(element, left, top, left1, top1, comment) {
            equal(element.style.left, left + "px", comment);
            equal(element.style.top, top + "px", comment);
            equal(jQuery(element).offset().left, left1, comment);
            equal(jQuery(element).offset().top, top1, comment);
        }

        // position general tests
        // Test temporary disabled because it falls after upgrade to HtmlUnit 2.8
        // TODO: Investigate why horisontal position set to wrong value.
        /*
         * test("source parameter test", function() { expect(24);
         * 
         * try {
         * 
         * var elements = RichFaces.QUnit.appendDomElements(body, '<div style="position:absolute;left:100px;top:100px
         * width:200px; height:200px; background-color:red" id="testElement">some text</div><div style="width:300px;
         * height:300px; background-color:blue" id="testElement1">some text</div>');
         * 
         * var e = document.getElementById("testElement"); var e1 = jQuery("#testElement1");
         * 
         * jQuery(e).setPosition("#testElement1", {from:"RB", to:"RB"}); testPositioning(e, e1.offset().left+e1.width(),
         * e1.offset().top+e1.height(), "jQuery selector"); e.style.left=e.style.top="0px";
         * 
         * jQuery(e).setPosition({id:"testElement1"}, {from:"RB", to:"RB"}); testPositioning(e, e1.offset().left+e1.width(),
         * e1.offset().top+e1.height(), "object with id"); e.style.left=e.style.top="0px";
         * 
         * jQuery(e).setPosition({left:300,top:300}, {from:"RB", to:"RB"}); testPositioning(e, 300, 300, "object with
         * left,top"); e.style.left=e.style.top="0px";
         * 
         * jQuery(e).setPosition(jQuery("#testElement1"), {from:"RB", to:"RB"}); testPositioning(e, e1.offset().left+e1.width(),
         * e1.offset().top+e1.height(), "jQuery object"); e.style.left=e.style.top="0px";
         * 
         * jQuery(e).setPosition(document.getElementById("testElement1"), {from:"RB", to:"RB"}); testPositioning(e,
         * e1.offset().left+e1.width(), e1.offset().top+e1.height(), "DOM element"); e.style.left=e.style.top="0px";
         * 
         * jQuery(e).setPosition({type:"customEvent", pageX:300, pageY:300}, {from:"RB", to:"RB"}); testPositioning(e, 300, 300,
         * "Event"); e.style.left=e.style.top="0px";
         *  } finally { RichFaces.QUnit.removeDomElements(elements); } });
         */
        // position html markup's tests
        /*
         * https://jira.jboss.org/jira/browse/RF-645 Calendar: incorrectly positioned popup in richfaces-demo IE7
         */
        test(
            "html markup 1",
            function() {
                expect(4);

                try {

                    var elements = RichFaces.QUnit
                        .appendDomElements(
                            body,
                            '<form>'
                                + '<table id="parent" width="100%" cellspacing="0" cellpadding="10" border="1" style="position: relative;">'
                                + '<tbody>' + '<tr>' + '<td>'
                                + '<table style="position:absolute; background-color:red" id="testElement">' + '<tr>'
                                + '<td style="width:200px;height:200px">some text</td>' + '</tr>' + '</table>' + '</td>'
                                + '</tr>' + '</tbody>' + '</table>' + '</form>');

                    var e = document.getElementById("testElement");
                    var jqe = jQuery(e);
                    jqe.css('left', '0px');
                    jqe.css('top', '0px');
                    var offset = jqe.offset();
                    jQuery(e).setPosition({
                        left : 300,
                        top : 300
                    });
                    testPositioning1(e, 300 - Math.floor(offset.left), 300 - Math.floor(offset.top), 300, 300);

                } finally {
                    RichFaces.QUnit.removeDomElements(elements);
                }
            });

        /*
         * https://jira.jboss.org/jira/browse/RF-1314 Calendar positioning bug.
         */
        test(
            "html markup 2",
            function() {
                expect(8);

                try {

                    var elements = RichFaces.QUnit
                        .appendDomElements(
                            body,
                            '<div style="height:350px;overflow-x:hidden;overflow-y:auto;width:300px">'
                                + '<div style="height:200px"></div>'
                                + '<table style="position:absolute; background-color:red" id="testElement">'
                                + '<tr>'
                                + '<td style="width:200px;height:200px">some text</td>'
                                + '</tr>'
                                + '</table>'
                                + '<div style="position:absolute; width:200px; height:200px; background-color:blue" id="testElement1">some text</div>'
                                + '<div style="height:200px"></div> ' + '</div>');

                    var e = document.getElementById("testElement");
                    jQuery(e).setPosition({
                        left : 300,
                        top : 300
                    });
                    testPositioning(e, 300, 300);

                    var e = document.getElementById("testElement1");
                    jQuery(e).setPosition({
                        left : 500,
                        top : 500
                    });
                    testPositioning(e, 500, 500);

                } finally {
                    RichFaces.QUnit.removeDomElements(elements);
                }
            });

        // ---
        test("html markup 3", function() {
            expect(4);

            try {
                var elements = RichFaces.QUnit.appendDomElements(body, '<table width="400" align="center">' + '<tr>' + '<td>'
                    + '<table style="position:absolute; background-color:red" id="testElement">' + '<tr>'
                    + '<td style="width:200px;height:200px">some text</td>' + '</tr>' + '</table>' + '</td>' + '</tr>'
                    + '</table>');

                var e = document.getElementById("testElement");
                jQuery(e).setPosition({
                    left : 300,
                    top : 300
                });
                testPositioning(e, 300, 300);

            } finally {
                RichFaces.QUnit.removeDomElements(elements);
            }
        });

        // ---
        test(
            "html markup 4",
            function() {
                expect(8);

                try {
                    var elements = RichFaces.QUnit
                        .appendDomElements(
                            body,
                            '<div style="position:absolute; top: 0px; bottom: 0px; left: 0px; right: 0px; overflow: auto;">'
                                + '<table style="position:absolute; background-color:red" id="testElement">'
                                + '<tr>'
                                + '<td style="width:200px;height:200px">some text</td>'
                                + '</tr>'
                                + '</table>'
                                + '<div style="position:absolute; width:200px; height:200px; background-color:blue" id="testElement1">some text</div>'
                                + '</div>');

                    var e = document.getElementById("testElement");
                    jQuery(e).setPosition({
                        left : 300,
                        top : 300
                    });
                    testPositioning(e, 300, 300);

                    var e = document.getElementById("testElement1");
                    jQuery(e).setPosition({
                        left : 500,
                        top : 500
                    });
                    testPositioning(e, 500, 500);

                } finally {
                    RichFaces.QUnit.removeDomElements(elements);
                }
            });

        // ---
        test(
            "html markup 5",
            function() {
                expect(8);

                try {
                    var elements = RichFaces.QUnit
                        .appendDomElements(
                            body,
                            '<div style="position:absolute; top: 0px; bottom: 0px; left: 0px; right: 0px; overflow: auto;">'
                                + '<div>'
                                + '<table style="position:absolute; background-color:red" id="testElement">'
                                + '<tr>'
                                + '<td style="width:200px;height:200px">some text</td>'
                                + '</tr>'
                                + '</table>'
                                + '<div style="position:absolute; width:200px; height:200px; background-color:blue" id="testElement1">some text</div>'
                                + '</div>' + '</div>');

                    var e = document.getElementById("testElement");
                    jQuery(e).setPosition({
                        left : 300,
                        top : 300
                    });
                    testPositioning(e, 300, 300);

                    var e = document.getElementById("testElement1");
                    jQuery(e).setPosition({
                        left : 500,
                        top : 500
                    });
                    testPositioning(e, 500, 500);

                } finally {
                    RichFaces.QUnit.removeDomElements(elements);
                }
            });

        // ---
        test(
            "html markup 6",
            function() {
                expect(8);

                try {

                    var elements = RichFaces.QUnit
                        .appendDomElements(
                            body,
                            '<div style="position:absolute; width:400px; top: 200px; left: 200px;">'
                                + '<table style="position:absolute; background-color:red" id="testElement">'
                                + '<tr>'
                                + '<td style="width:200px;height:200px">some text</td>'
                                + '</tr>'
                                + '</table>'
                                + '<div style="position:absolute; width:200px; height:200px; background-color:blue" id="testElement1">some text</div>'
                                + '</div>');

                    var e = document.getElementById("testElement");
                    jQuery(e).setPosition({
                        left : 300,
                        top : 300
                    });
                    testPositioning1(e, 100, 100, 300, 300);

                    var e = document.getElementById("testElement1");
                    jQuery(e).setPosition({
                        left : 500,
                        top : 500
                    });
                    testPositioning1(e, 300, 300, 500, 500);

                } finally {
                    RichFaces.QUnit.removeDomElements(elements);
                }
            });

        // ---
        test(
            "html markup 7",
            function() {
                expect(8);

                try {

                    var elements = RichFaces.QUnit
                        .appendDomElements(
                            body,
                            '<div style="position:absolute; width:400px; top: 200px; left: 200px;">'
                                + '<div>'
                                + '<table style="position:absolute; background-color:red" id="testElement">'
                                + '<tr>'
                                + '<td style="width:200px;height:200px">some text</td>'
                                + '</tr>'
                                + '</table>'
                                + '<div style="position:absolute; width:200px; height:200px; background-color:blue" id="testElement1">some text</div>'
                                + '</div>' + '</div>');

                    var e = document.getElementById("testElement");
                    jQuery(e).setPosition({
                        left : 300,
                        top : 300
                    });
                    testPositioning1(e, 100, 100, 300, 300);

                    var e = document.getElementById("testElement1");
                    jQuery(e).setPosition({
                        left : 500,
                        top : 500
                    });
                    testPositioning1(e, 300, 300, 500, 500);

                } finally {
                    RichFaces.QUnit.removeDomElements(elements);
                }
            });

        test(
            "set the same position",
            function() {
                expect(4);

                try {

                    var elements = RichFaces.QUnit
                        .appendDomElements(body,
                            '<div id="testElement" style="position:absolute; width:400px; height: 400px; top: 200px; left: 200px;">some text</div>');

                    var e = document.getElementById("testElement");
                    jQuery(e).setPosition({
                        left : 200,
                        top : 200
                    });
                    testPositioning(e, 200, 200);

                } finally {
                    RichFaces.QUnit.removeDomElements(elements);
                }
            });

        test(
            "hidden element position",
            function() {
                expect(6);

                try {

                    var elements = RichFaces.QUnit
                        .appendDomElements(
                            body,
                            '<div id="testElement" style="display:none; position:absolute; width:400px; height: 400px; top: 200px; left: 200px;">some text</div>');

                    var e = document.getElementById("testElement");
                    jQuery(e).setPosition({
                        left : 300,
                        top : 300
                    });
                    ok(e.style.display == "none", "display=='none'");
                    ok(e.style.visibility == "", "visibility==''");
                    e.style.display = '';
                    testPositioning(e, 300, 300);

                } finally {
                    RichFaces.QUnit.removeDomElements(elements);
                }
            });

        // TODO: add auto position tests // depends on some refactoring and optimization (not done yet)

    });
RichFaces.QUnit.run(function() {
    module("richfaces-toggle-panel");

    test("TogglePanelItem test public methods", function () {
        var item = RichFaces.$('f:name1');

        ok(item, "item");
        equals(item.getName(), "name1", "item.getName()");

        var togglePanel = item.getTogglePanel();
        ok(togglePanel, item.getTogglePanel());
        equals(togglePanel.id, "f:panel", "togglePanel.id"); // see xhtml page definition

        ok(item.isSelected(), "item.isSelected()");
    });

    test("TogglePanelItem test events order", function () {
        var componentId = 'f:name1';
        var item = RichFaces.$(componentId);

        expect(2);
        var state = 1;
        var leave = RichFaces.Event.bindById(componentId, "__leave", function () {
            ok(state == 1, "leave handler invouked (state == 1)");
            state++;
        });

        item.__leave();

        var enter = RichFaces.Event.bindById(componentId, "__enter", function () {
            ok(state == 2, "enter handler invouked (state == 2)");
            state++;
        });

        item.__enter();

        RichFaces.Event.unbindById(componentId, "__leave", leave);
        RichFaces.Event.unbindById(componentId, "__enter", enter);
    });

    test("TogglePanelItem test cacelable of leave", function () {
        var componentId = 'f:name1';
        var item = RichFaces.$(componentId);

        expect(2);
        var state = 1;
        var leave = RichFaces.Event.bindById(componentId, "__leave", function () {
            ok(true, "leave handler invouked");

            return false;
        });

        ok(!item.__leave(), "!item.leave()");

        RichFaces.Event.bindById(componentId, "__leave", leave);
    });
});
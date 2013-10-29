package org.richfaces.tests.page.fragments.impl.tree;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

public class TreeShowcase {

    @FindBy
    private RichFacesTree tree;

    public void showcase_tree() {
        tree.expandNode(ChoicePickerHelper.byVisibleText().match("src"))
            .expandNode(ChoicePickerHelper.byIndex().index(2))
            .selectNode(1);

        tree.selectNode(4).collapseNode(1);
    }
}

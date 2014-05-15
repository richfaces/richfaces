package org.richfaces.fragment.tree;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

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

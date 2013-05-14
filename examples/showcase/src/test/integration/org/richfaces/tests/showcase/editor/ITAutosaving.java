/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.showcase.editor;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.condition.element.ElementTextContains;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.editor.page.AutosavingPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITAutosaving extends AbstractWebDriverTest {

    @Page
    private AutosavingPage page;

    private static final int TIMEOUT_FOR_AUTOSAVING = 1600;

    private final String[] EXPECTED_PARAGRAPHS = {
            "\"Little Red Riding Hood\" is a famous fairy tale about a young girl's encounter with a wolf. "
                + "The story has been changed considerably in its history and subject to numerous modern adaptations and readings.",

            "The version most widely known today is based on the Brothers Grimm variant. "
                + "It is about a girl called Little Red Riding Hood, after the red hooded cape or cloak she wears. "
                + "The girl walks through the woods to deliver food to her sick grandmother.",

            "A wolf wants to eat the girl but is afraid to do so in public. "
                + "He approaches the girl, and she naïvely tells him where she is going. "
                + "He suggests the girl pick some flowers, which she does. In the meantime, he goes to "
                + "the grandmother's house and gains entry by pretending to be the girl. He swallows the "
                + "grandmother whole, and waits for the girl, disguised as the grandmother.",

            "When the girl arrives, she notices he looks very strange to be her grandma. In most retellings",

            "A hunter, however, comes to the rescue and cuts the wolf open. Little "
                + "Red Riding Hood and her grandmother emerge unharmed. They fill the wolf's body with "
                + "heavy stones, which drown him when he falls into a well. Other versions of the story have "
                + "had the grandmother shut in the closet instead of eaten, and some have Little Red Riding "
                + "Hood saved by the hunter as the wolf advances on her rather than after she is eaten.",

            "The tale makes the clearest contrast between the safe world of the village and the "
                + "dangers of the forest, conventional antitheses that are essentially medieval, though no written versions are as old as that." };

    @Test
    public void testContentOfEditor() {

        String contentOfEditorInput = page.outputFromEditor.getText();

        int j = 1;
        for (String i : EXPECTED_PARAGRAPHS) {
            assertTrue("The " + j + ". paragraph of initial content is corrupted!", contentOfEditorInput.contains(i));
            j++;
        }
    }

    @Test
    public void testAutoSavingAfterBlur() {
        String expected = "Added and possibly saved text!";
        page.editor.typeTextToEditor(expected);

        guardAjax(page.outputFromEditor).click();
        String actual = page.outputFromEditor.getText();
        assertTrue("The change in the editor was not saved automatically!", actual.contains(expected));
    }

    @Test
    public void testAutoSavingAfter1Sec() {
        String expected = "Added and possibly saved text!";
        page.editor.typeTextToEditor(expected);

        waitAjax().withTimeout(TIMEOUT_FOR_AUTOSAVING, TimeUnit.MILLISECONDS).until(
            new ElementTextContains(page.outputFromEditor, expected));
    }

}

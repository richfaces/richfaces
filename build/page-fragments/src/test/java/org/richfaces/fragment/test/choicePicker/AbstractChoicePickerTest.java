/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
 */

package org.richfaces.fragment.test.choicePicker;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Lists;

public class AbstractChoicePickerTest {

    @Mock
    protected MyPageFragment myFragment;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);

      List<WebElement> divs = new ArrayList<WebElement>();
      for(int i = 1; i <= 6; i++) {
          WebElement elem = mock(WebElement.class);
          when(elem.getText()).thenReturn("" + i);
          divs.add(elem);
      }
      when(myFragment.getDivs()).thenReturn(divs);
    }

    protected List<String> getStringsFromElements(List<WebElement> list) {
        List<String> result = Lists.newArrayList();
        for (WebElement webElement : list) {
            result.add(webElement.getText());
        }
        return result;
    }
}
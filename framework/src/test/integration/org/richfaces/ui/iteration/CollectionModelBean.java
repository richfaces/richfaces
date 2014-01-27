package org.richfaces.ui.iteration;

import javax.enterprise.inject.Model;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

@Model
public class CollectionModelBean {

    private Collection collection = new LinkedHashSet(Arrays.asList("1", "2", "3"));

    public Collection getCollection() {
        return collection;
    }
}

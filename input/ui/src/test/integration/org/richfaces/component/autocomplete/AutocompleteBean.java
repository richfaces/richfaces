package org.richfaces.component.autocomplete;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class AutocompleteBean {

    private boolean listenerInvoked = false;

    public List<String> getSuggestions() {
        return Arrays.asList("a", "b", "c");
    }

    public void actionListener() {
        listenerInvoked = true;
    }

    public boolean isListenerInvoked() {
        return listenerInvoked;
    }
}

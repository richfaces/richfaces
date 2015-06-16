package org.richfaces.component.autocomplete;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class AutocompleteBean {

    private static final List<String> LIST = Arrays.asList("Toronto", "New York", "San Francisco", "Tampa Bay");
    private static final long serialVersionUID = 1L;
    private boolean listenerInvoked = false;

    public void actionListener() {
        listenerInvoked = true;
    }

    public List<String> getSuggestions() {
        return LIST;
    }

    public boolean isListenerInvoked() {
        return listenerInvoked;
    }
}

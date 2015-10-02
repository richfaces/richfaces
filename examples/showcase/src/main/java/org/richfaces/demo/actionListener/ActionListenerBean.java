package org.richfaces.demo.actionListener;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/**
 * @author Nick Belaevski
 *
 */
@ManagedBean
@NoneScoped
public class ActionListenerBean {
    public static final class ActionListenerImpl implements ActionListener {
        public void processAction(ActionEvent event) throws AbortProcessingException {
            addFacesMessage("Implementation of ActionListener created and called: " + this);
        }
    }

    private static final class BoundActionListener implements ActionListener {
        public void processAction(ActionEvent event) throws AbortProcessingException {
            addFacesMessage("Bound listener called");
        }
    }

    private ActionListener actionListener = new BoundActionListener();

    private static void addFacesMessage(String messageText) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(messageText));
    }

    public void handleActionMethod(ActionEvent event) throws AbortProcessingException {
        addFacesMessage("Method expression listener called");
    }

    public void handleActionMethodComposite(ActionEvent event) throws AbortProcessingException {
        addFacesMessage("Method expression listener called from composite component");
    }

    public ActionListener getActionListener() {
        return actionListener;
    }
}

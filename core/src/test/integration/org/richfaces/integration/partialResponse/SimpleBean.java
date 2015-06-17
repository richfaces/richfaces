package org.richfaces.integration.partialResponse;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class SimpleBean {

    public static final String NOT_RUN = "did not run";
    public static final String PASSED = "passed";

    private String checkClientWindowIdIsNotEmptyResult = NOT_RUN;
    private String test;

    public void checkClientWindowIdIsNotEmpty() {
        HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
        String jfwid = request.getParameter("jfwid").trim();
        if (jfwid.isEmpty()) {
            checkClientWindowIdIsNotEmptyResult = "Client window id should not be empty.";
            return;
        }
        checkClientWindowIdIsNotEmptyResult = PASSED;
    }

    public void doAction() {
        System.out.println("123");
    }

    public String getCheckClientWindowIdIsNotEmptyResult() {
        return checkClientWindowIdIsNotEmptyResult;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}

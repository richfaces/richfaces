package org.richfaces.demo.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class FairyTailBean {

    private String fairyTail;

    @PostConstruct
    public void loadFairyTail() {
        InputStream inputStream = EditorBean.class.getResourceAsStream("fairy-tail.html");
        try {
            StringBuffer buffer = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = in.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }
            fairyTail = buffer.toString();
            in.close();
        } catch (IOException e) {
            fairyTail = "";
        }
    }

    public String getFairyTail() {
        return fairyTail;
    }
}

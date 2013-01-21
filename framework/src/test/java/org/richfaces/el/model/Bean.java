package org.richfaces.el.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Size;

public class Bean {
    private List<String> list;
    private Map<String, String> map = new HashMap<String, String>();
    private String string;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Size(max = 2)
    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}

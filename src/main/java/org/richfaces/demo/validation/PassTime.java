package org.richfaces.demo.validation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class PassTime {

    @NotEmpty
    @Length(max = 15, min = 3)
    private String title;
    @NotNull
    @Min(0)
    @Max(12)
    private Integer time;

    public PassTime(String title, Integer time) {
        setTitle(title);
        setTime(time);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

}

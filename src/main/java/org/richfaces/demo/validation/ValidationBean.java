/**
 * 
 */
package org.richfaces.demo.validation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Ilya Shaikovsky
 * 
 */
@ManagedBean
@RequestScoped
public class ValidationBean {
    private static final Class<?>[] DEFAULT_GROUP = { Default.class };
    private String progressString = "Fill the form please";

    // @NotEmpty
    // @Pattern(regexp=".*[^\\s].*", message="This string contain only spaces")
    @Length(min = 3, max = 12)
    private String name;
    @Email
    @NotEmpty
    private String email;

    @NotNull
    @Min(18)
    @Max(100)
    private Integer age;

    public List<String> getRender() {
        List<String> res = new ArrayList<String>();
        res.add("msg");
        return res;
    }

    public Class[] getGroups() {
        return DEFAULT_GROUP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void success() {
        setProgressString(getProgressString() + "(Strored successfully)");
    }

    public String getProgressString() {
        return progressString;
    }

    public void setProgressString(String progressString) {
        this.progressString = progressString;
    }
}

/**
 *
 */
package org.richfaces.example;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.validation.Valid;
import javax.validation.constraints.Max;

/**
 * @author asmirnov
 *
 */
@ManagedBean
@SessionScoped
public class DataBean {
    private final List<Validable<?>> beans;

    /**
     * @return the beans
     */
    @Valid
    public List<Validable<?>> getBeans() {
        return beans;
    }

    public DataBean() {
        beans = new ArrayList<Validable<?>>(7);
        beans.add(new NotNullBean());
        beans.add(new NotEmptyBean());
        beans.add(new SizeBean());
        beans.add(new MinBean());
        beans.add(new MaxBean());
        beans.add(new MinMaxBean());
        beans.add(new PatternBean());
    }

    @Max(value = 20, message = "Total value should be less then 20")
    public int getTotal() {
        int total = 0;
        for (Validable<?> bean : beans) {
            Object value = bean.getValue();
            if (value instanceof Integer) {
                Integer intValue = (Integer) value;
                total += intValue;
            }
        }
        return total;
    }
}

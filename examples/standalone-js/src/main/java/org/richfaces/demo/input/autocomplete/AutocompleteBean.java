package org.richfaces.demo.input.autocomplete;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

@ManagedBean
@RequestScoped
public class AutocompleteBean implements Serializable {

    private List<Person> people = Arrays.asList(
            new Person("Aaron", 27),
            new Person("Adam", 27),
            new Person("Beatrice", 27),
            new Person("Benny", 27),
            new Person("Bert", 27),
            new Person("Belveder", 27),
            new Person("Chris", 27),
            new Person("Charles", 27),
            new Person("Curtis", 27),
            new Person("Daniel", 27),
            new Person("David", 32),
            new Person("Ernie", 32),
            new Person("Francis", 32),
            new Person("Gill", 32),
            new Person("Henry", 32),
            new Person("Humpfrey", 68),
            new Person("Ivan", 32),
            new Person("Jacob", 32),
            new Person("Jack", 27),
            new Person("James", 27),
            new Person("John", 26),
            new Person("Kate", 32),
            new Person("Katrina", 32),
            new Person("Lance", 32),
            new Person("Lisa", 27),
            new Person("Luke", 32),
            new Person("Mike", 32),
            new Person("Mark", 27),
            new Person("Mathhew", 27),
            new Person("Martin", 27),
            new Person("Neil", 27),
            new Person("Nathan", 27),
            new Person("Neil", 27),
            new Person("Orlando", 27),
            new Person("Peter", 27),
            new Person("Quik", 27),
            new Person("Richard", 27),
            new Person("Steven", 27),
            new Person("Timothy", 27),
            new Person("Thomas", 27),
            new Person("Uncle", 27),
            new Person("Victor", 27),
            new Person("William", 27),
            new Person("Xena", 27),
            new Person("Yankee", 27),
            new Person("Zod", 27)
    );

    public List<Person> getSuggestions() {
        return people;
    }

    public Collection<Person> suggest(FacesContext facesContext, UIComponent component, final String prefix) {
        return Collections2.filter(people, new Predicate<Person>() {
            @Override
            public boolean apply(Person input) {
                if (prefix == null) {
                    return true;
                }
                return input.name.toLowerCase().startsWith(prefix.toLowerCase());
            }
        });
    }

    public static class Person {

        private static volatile int SEQUENCE = 0;

        private final int id = SEQUENCE++;
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person [name=" + name + ", age=" + age + "]";
        }
    }
}

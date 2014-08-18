package org.richfaces.demo.model.person;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* Created by bleathem on 15/08/14.
*/
public class Person {

    public static List<Person> people = Arrays.asList(
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
    private static volatile int SEQUENCE = 0;

    public static Map<String, Person> peopleMap = Maps.uniqueIndex(people, new Function<Person, String>() {
        public String apply(Person from) {
            return String.valueOf(from.getId()); // or something else
        }
    });

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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Person [id=" + String.valueOf(id) + ", name=" + name + ", age=" + age + "]";
    }
}

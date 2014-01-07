/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.fragment.common.picker;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.picker.ChoicePickerHelper.WebElementPicking.WebElementPicker;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public final class ChoicePickerHelper {

    private ChoicePickerHelper() {
    }

    /**
     * @return Returns ChoicePicker for picking choices by index.
     */
    public static ByIndexChoicePicker byIndex() {
        return new ByIndexChoicePicker();
    }

    /**
     * @return Returns ChoicePicker for picking choices by visible text. The picker can have multiple conditions/filters (e.g. startsWith('xy').endsWith('z')).
     */
    public static ByVisibleTextChoicePicker byVisibleText() {
        return new ByVisibleTextChoicePicker();
    }

    /**
     * @return Returns ChoicePicker for picking choices by WebElement methods/properties.
     */
    public static WebElementPicker byWebElement() {
        return new WebElementPickerImpl();
    }

    public static class ByIndexChoicePicker implements ChoicePicker, MultipleChoicePicker {

        private final Set<Integer> reachableIndexes = new LinkedHashSet<Integer>();
        private final List<PreparationCommand> preparationCommands = Lists.newArrayList();

        private ByIndexChoicePicker() {
        }

        private void addReachableIndex(int index) {
            reachableIndexes.add(index);
        }

        public ByIndexChoicePicker beforeLast(final int positionsBeforeLast) {
            preparationCommands.add(new PreparationCommand() {
                @Override
                public void prepare(List<WebElement> list) {
                    int countedIndex = list.size() - 1 - positionsBeforeLast;
                    if (countedIndex >= 0 && countedIndex < list.size()) {
                        addReachableIndex(countedIndex);
                    }
                }
            });
            return this;
        }

        /**
         * Picks every nth index from 0 (including).
         *
         * @param  nth has to be greater than 1
         * @return     same instance
         */
        public ByIndexChoicePicker everyNth(final int nth) {
            return everyNth(nth, 0);
        }

        /**
         * Picks every nth index from @from (including).
         *
         * @param nth  has to be greater than 1 (the iteration step)
         * @param from has to be greater or equals to 0
         * @return     same instance
         */
        public ByIndexChoicePicker everyNth(final int nth, final int from) {
            Preconditions.checkArgument(nth > 1);
            Preconditions.checkArgument(from >= 0);
            preparationCommands.add(new PreparationCommand() {
                @Override
                public void prepare(List<WebElement> list) {
                    for (int i = from; i < list.size(); i += nth) {
                        addReachableIndex(i);
                    }
                }
            });
            return this;
        }

        public ByIndexChoicePicker first() {
            return index(0);
        }

        /**
         * Picks every index from given range.
         *
         * @param range range from which will be the indexes picked
         * @return same instance
         */
        public ByIndexChoicePicker fromRange(final Range<Integer> range) {
            Preconditions.checkNotNull(range);
            preparationCommands.add(new PreparationCommand() {
                @Override
                public void prepare(List<WebElement> list) {
                    for (int i = 0; i < list.size(); i++) {
                        if (range.contains(i)) {
                            addReachableIndex(i);
                        }
                    }
                }
            });
            return this;
        }

        public ByIndexChoicePicker index(final int index) {
            preparationCommands.add(new PreparationCommand() {
                @Override
                public void prepare(List<WebElement> list) {
                    if (list.size() > index && index >= 0) {
                        addReachableIndex(index);
                    }
                }
            });
            return this;
        }

        public ByIndexChoicePicker indexes(Integer... indexes) {
            for (Integer integer : indexes) {
                index(integer);
            }
            return this;
        }

        public ByIndexChoicePicker last() {
            return beforeLast(0);
        }

        @Override
        public WebElement pick(List<WebElement> options) {
            List<WebElement> elements = pickInner(options, TRUE);
            return (elements.isEmpty() ? null : elements.get(0));
        }

        private List<WebElement> pickInner(List<WebElement> options, boolean pickFirst) {
            Preconditions.checkNotNull(options, "Options cannot be null.");
            Preconditions.checkArgument(!reachableIndexes.isEmpty() || !preparationCommands.isEmpty(), "No filter specified.");
            if (options.isEmpty()) {
                return Collections.emptyList();
            }
            for (PreparationCommand command : preparationCommands) {
                command.prepare(options);
            }
            if (reachableIndexes.isEmpty()) {
                return Collections.emptyList();
            }

            List<WebElement> result = Lists.newArrayList();
            if (pickFirst) {
                result.add(options.get(reachableIndexes.iterator().next()));
            } else {
                for (Integer i : reachableIndexes) {
                    result.add(options.get(i));
                }

            }
            return result;
        }

        @Override
        public List<WebElement> pickMultiple(List<WebElement> options) {
            return pickInner(options, FALSE);
        }

        @Override
        public String toString() {
            return (reachableIndexes.isEmpty() ? "unknown index picking" : reachableIndexes.toString());
        }

        private interface PreparationCommand {

            void prepare(List<WebElement> list);
        }
    };

    public static class ByVisibleTextChoicePicker implements ChoicePicker, MultipleChoicePicker {

        private final List<Predicate> filters = Lists.newArrayList();
        private boolean allRulesMustPass = Boolean.TRUE;
        private Function<WebElement, WebElement> transformationFunction;

        private ByVisibleTextChoicePicker() {
        }

        public ByVisibleTextChoicePicker addFilter(Predicate<WebElement> filter) {
            filters.add(filter);
            return this;
        }

        /**
         * If true, then all rules/filters must pass to pick an element.
         * If false, then if at least one rule/filter passes, element will be picked.
         * Default value is true.
         * @param allRulesMustPass
         */
        public ByVisibleTextChoicePicker allRulesMustPass(boolean allRulesMustPass) {
            this.allRulesMustPass = allRulesMustPass;
            return this;
        }

        public ByVisibleTextChoicePicker contains(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().contains(str);
                }

                @Override
                public String toString() {
                    return "contains('" + str + "')";
                }

            });
        }

        public ByVisibleTextChoicePicker endsWith(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().endsWith(str);
                }

                @Override
                public String toString() {
                    return "endsWith('" + str + "')";
                }
            });
        }

        public ByVisibleTextChoicePicker match(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().matches(str);
                }

                @Override
                public String toString() {
                    return "matches('" + str + "')";
                }
            });
        }

        @Override
        public WebElement pick(List<WebElement> options) {
            List<WebElement> elements = pickInner(options, TRUE);
            return (elements == null || (elements.isEmpty()) ? null : elements.get(0));
        }

        private List<WebElement> pickInner(List<WebElement> options, boolean pickFirst) {
            Preconditions.checkNotNull(options, "Options cannot be null.");
            Preconditions.checkArgument(!filters.isEmpty(), "No filters specified.");
            if (options.isEmpty()) {
                return Collections.EMPTY_LIST;
            }

            if (pickFirst) {
                try {
                    return Lists.newArrayList(Iterables.find(options, new PickPredicate()));
                } catch (NoSuchElementException e) {
                    return Collections.EMPTY_LIST;
                }

//                Less performant:
//
//                Optional<WebElement> tryFind = Iterables.tryFind(options, new PickPredicate());
//                return (tryFind.isPresent() ? Lists.newArrayList(tryFind.get()) : Collections.EMPTY_LIST);
            } else {
                return Lists.newArrayList(Sets.newLinkedHashSet(Iterables.filter(options, new PickPredicate())));
            }
        }

        @Override
        public List<WebElement> pickMultiple(List<WebElement> options) {
            return pickInner(options, FALSE);
        }

        /**
         * Sets a transformation function, that will be used to transform each WebElement from list of possible choices
         * to another WebElement.
         *
         * Example:
         * This picker will be picking and comparing text from such divs:
         * <code>
         *  <div>
         *      <span>text1</span>
         *      <span>text2</span>
         *  </div>
         * </code> ,
         * but you want to compare the text only with the second span.
         * The only thing you need to do is to add this function:
         * <code>
         * new Function<WebElement, WebElement>() {
         *      @Override
         *      public WebElement apply(WebElement input) {
         *          return input.findElements(By.tagName("span")).get(1);
         *      }
         *  }
         * </code>
         *
         * @param transformationFunction
         */
        public void setTransformationFunction(Function<WebElement, WebElement> transformationFunction) {
            this.transformationFunction = transformationFunction;
        }

        public ByVisibleTextChoicePicker startsWith(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().startsWith(str);
                }

                @Override
                public String toString() {
                    return "startsWith('" + str + "')";
                }
            });
        }

        @Override
        public String toString() {
            return (filters.isEmpty() ? "no filters specified" : filters.toString());
        }

        private WebElement transFormIfNeeded(WebElement input) {
            return (transformationFunction == null ? input : transformationFunction.apply(input));
        }

        private class PickPredicate implements Predicate<WebElement> {

            @Override
            public boolean apply(WebElement input) {
                WebElement element = transFormIfNeeded(input);
                if (allRulesMustPass) {
                    for (Predicate predicate : filters) {
                        if (!predicate.apply(element)) {
                            return FALSE;
                        }
                    }
                    return TRUE;
                } else {
                    for (Predicate predicate : filters) {
                        if (predicate.apply(element)) {
                            return TRUE;
                        }
                    }
                    return FALSE;
                }
            }
        }
    };

    public interface WebElementPicking {

        ComparationBy text();

        ComparationBy attribute(String attributeName);

        public interface ComparationBy {

            CanBeNegated endsWith(String str);

            CanBeNegated equalTo(String str);

            CanBeNegated contains(String str);

            CanBeNegated matches(String str);

            CanBeNegated starstWith(String str);
        }

        public interface CanBeNegated extends LogicalOperation {

            LogicalOperation not();
        }

        public interface LogicalOperation extends ChoicePicker, MultipleChoicePicker {

            WebElementPicking and();

            WebElementPicking or();
        }

        public interface WebElementPicker extends ChoicePicker, MultipleChoicePicker, WebElementPicking {
        }
    }

    public static class WebElementPickerImpl implements WebElementPicker {

        private final LinkedList<MergingPredicate> predicates = new LinkedList<MergingPredicate>();
        private final LinkedList<LogicalFunctions> logicalFunctions = new LinkedList<LogicalFunctions>();

        private final ComparationBy comparation = new ComparationByImpl();
        private final LogicalOperation operation = new LogicalOperationImpl();
        private final CanBeNegated canBeNegated = new CanBeNegatedImpl();

        private Function<WebElement, WebElement> transformationFunction;
        private Function<WebElement, String> webElementFunction;

        private enum LogicalFunctions {

            AND {
                @Override
                boolean apply(boolean b1, Predicate<WebElement> b2, WebElement e) {
                    return b1 && b2.apply(e);
                }
            },
            OR {
                @Override
                boolean apply(boolean b1, Predicate<WebElement> b2, WebElement e) {
                    return b1 || b2.apply(e);
                }
            };

            abstract boolean apply(boolean b1, Predicate<WebElement> b2, WebElement e);
        }

        @Override
        public ComparationBy attribute(String attributeName) {
            webElementFunction = new GetAttributeFunction(attributeName);
            return comparation;
        }

        @Override
        public WebElement pick(List<WebElement> options) {
            List<WebElement> elements = pickInner(options, TRUE);
            return (elements.isEmpty() ? null : elements.get(0));
        }

        private List<WebElement> pickInner(List<WebElement> options, boolean pickFirst) {
            Preconditions.checkNotNull(options, "Options cannot be null.");
            Preconditions.checkArgument(!predicates.isEmpty());
            Preconditions.checkArgument(predicates.size() - 1 == logicalFunctions.size());
            if (options.isEmpty()) {
                return Collections.EMPTY_LIST;
            }
            LinkedHashSet<WebElement> result;
            try {
                if (pickFirst) {
                    result = Sets.newLinkedHashSet();
                    result.add(Iterables.find(options, new FinalPredicate()));
                } else {
                    result = Sets.newLinkedHashSet(Iterables.filter(options, new FinalPredicate()));
                }
                return Lists.newArrayList(result);
            } catch (NoSuchElementException ex) {
                return Collections.EMPTY_LIST;
            }
        }

        @Override
        public List<WebElement> pickMultiple(List<WebElement> options) {
            return pickInner(options, FALSE);
        }

        @Override
        public ComparationBy text() {
            webElementFunction = new GetTextFunction();
            return comparation;
        }

        private WebElement transFormIfNeeded(WebElement input) {
            return (transformationFunction == null ? input : transformationFunction.apply(input));
        }

        private class FinalPredicate implements Predicate<WebElement> {

            @Override
            public boolean apply(WebElement input) {
                WebElement transformed = transFormIfNeeded(input);
                if (predicates.size() == 1) {
                    return predicates.peekFirst().apply(input);
                }
                LinkedList<Predicate<WebElement>> predicatesCopy = new LinkedList<Predicate<WebElement>>(predicates);
                LinkedList<LogicalFunctions> logicalFunctionsCopy = new LinkedList<LogicalFunctions>(logicalFunctions);
                boolean previousResult = predicatesCopy.removeFirst().apply(transformed);
                LogicalFunctions logicalFunction;
                while (!logicalFunctionsCopy.isEmpty()) {
                    logicalFunction = logicalFunctionsCopy.removeFirst();
                    if (!previousResult && logicalFunction.equals(LogicalFunctions.AND)) {
                        return previousResult;// return false if previous result was false and the function is AND
                    }
                    previousResult = logicalFunction.apply(previousResult, predicatesCopy.removeFirst(), transformed);
                }
                return previousResult;
            }
        }

        private class ComparationByImpl implements ComparationBy {

            @Override
            public CanBeNegated contains(String str) {
                predicates.add(new MergingPredicate(webElementFunction, new ContainsFunction(str)));
                return canBeNegated;
            }

            @Override
            public CanBeNegated endsWith(String str) {
                predicates.add(new MergingPredicate(webElementFunction, new EndsWithFunction(str)));
                return canBeNegated;
            }

            @Override
            public CanBeNegated equalTo(String str) {
                predicates.add(new MergingPredicate(webElementFunction, new EqualsToFunction(str)));
                return canBeNegated;
            }

            @Override
            public CanBeNegated matches(String str) {
                predicates.add(new MergingPredicate(webElementFunction, new MatchesFunction(str)));
                return canBeNegated;
            }

            @Override
            public CanBeNegated starstWith(String str) {
                predicates.add(new MergingPredicate(webElementFunction, new StartsWithFunction(str)));
                return canBeNegated;
            }
        }

        private class LogicalOperationImpl implements LogicalOperation {

            @Override
            public WebElementPicking and() {
                logicalFunctions.add(LogicalFunctions.AND);
                return WebElementPickerImpl.this;
            }

            @Override
            public WebElementPicking or() {
                logicalFunctions.add(LogicalFunctions.OR);
                return WebElementPickerImpl.this;
            }

            @Override
            public WebElement pick(List<WebElement> options) {
                return WebElementPickerImpl.this.pick(options);
            }

            @Override
            public List<WebElement> pickMultiple(List<WebElement> options) {
                return WebElementPickerImpl.this.pickMultiple(options);
            }
        }

        private class CanBeNegatedImpl extends LogicalOperationImpl implements CanBeNegated {

            @Override
            public LogicalOperation not() {
                predicates.peekLast().negate();
                return operation;
            }
        }

        private static class MergingPredicate implements Predicate<WebElement> {

            private final Function<WebElement, String> elementToString;
            private final Function<String, Boolean> stringToBoolean;
            private boolean negate = Boolean.FALSE;

            public MergingPredicate(Function<WebElement, String> elementToString, Function<String, Boolean> stringToBoolean) {
                this.elementToString = elementToString;
                this.stringToBoolean = stringToBoolean;
            }

            public void negate() {
                this.negate = Boolean.TRUE;
            }

            @Override
            public boolean apply(WebElement input) {
                boolean result = stringToBoolean.apply(elementToString.apply(input));
                return negate ? !result : result;
            }

            @Override
            public String toString() {
                return "MergingPredicate{" + "elementToString=" + elementToString + ", stringToBoolean=" + stringToBoolean + ", negate=" + negate + '}';
            }
        }

        private class GetTextFunction implements Function<WebElement, String> {

            @Override
            public String apply(WebElement input) {
                return input.getText();
            }

            @Override
            public String toString() {
                return "GetTextFunction()";
            }
        }

        private static class GetAttributeFunction implements Function<WebElement, String> {

            private final String attName;

            public GetAttributeFunction(String attName) {
                this.attName = attName;
            }

            @Override
            public String apply(WebElement input) {
                return input.getAttribute(attName);
            }

            @Override
            public String toString() {
                return "GetAttributeFunction{" + "attName=" + attName + '}';
            }

        }

        private static class ContainsFunction extends CompareToFunction {

            public ContainsFunction(String compareTo) {
                super(compareTo);
            }

            @Override
            public Boolean apply(String input) {
                return input.contains(getCompareTo());
            }
        }

        private static class EndsWithFunction extends CompareToFunction {

            public EndsWithFunction(String compareTo) {
                super(compareTo);
            }

            @Override
            public Boolean apply(String input) {
                return input.endsWith(getCompareTo());
            }
        }

        private static class EqualsToFunction extends CompareToFunction {

            public EqualsToFunction(String compareTo) {
                super(compareTo);
            }

            @Override
            public Boolean apply(String input) {
                return input.equals(getCompareTo());
            }
        }

        private static class MatchesFunction extends CompareToFunction {

            public MatchesFunction(String compareTo) {
                super(compareTo);
            }

            @Override
            public Boolean apply(String input) {
                return input.matches(getCompareTo());
            }
        }

        private static class StartsWithFunction extends CompareToFunction {

            public StartsWithFunction(String compareTo) {
                super(compareTo);
            }

            @Override
            public Boolean apply(String input) {
                return input.startsWith(getCompareTo());
            }
        }

        private abstract static class CompareToFunction implements Function<String, Boolean> {

            private final String compareTo;

            public CompareToFunction(String compareTo) {
                this.compareTo = compareTo;
            }

            public String getCompareTo() {
                return compareTo;
            }

            @Override
            public String toString() {
                return getClass().getSimpleName() + '{' + compareTo + '}';
            }
        }
    }
}

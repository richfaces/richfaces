/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.showcase.repeat;

import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class AbstractDataIterationWithStates extends AbstractWebDriverTest {

    // Inner class for State infomation, which are used in data iteration components
    public class StateWithCapitalAndTimeZone {

        private String state = null;
        private String capital = null;
        private String timeZone = null;

        public StateWithCapitalAndTimeZone() {
        }

        public StateWithCapitalAndTimeZone(String state, String capital, String timeZone) {
            this.state = state;
            this.capital = capital;
            this.timeZone = timeZone;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        @Override
        public String toString() {

            return this.state + " " + this.capital + " " + this.timeZone;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((capital == null) ? 0 : capital.hashCode());
            result = prime * result + ((state == null) ? 0 : state.hashCode());
            result = prime * result + ((timeZone == null) ? 0 : timeZone.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            StateWithCapitalAndTimeZone other = (StateWithCapitalAndTimeZone) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (capital == null) {
                if (other.capital != null) {
                    return false;
                }
            } else if (!capital.equals(other.capital)) {
                return false;
            }
            if (state == null) {
                if (other.state != null) {
                    return false;
                }
            } else if (!state.equals(other.state)) {
                return false;
            }
            if (timeZone == null) {
                if (other.timeZone != null) {
                    return false;
                }
            } else if (!timeZone.equals(other.timeZone)) {
                return false;
            }
            return true;
        }

        private AbstractDataIterationWithStates getOuterType() {
            return AbstractDataIterationWithStates.this;
        }
    }

}

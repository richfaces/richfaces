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
package org.richfaces.demo.misc;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * @author Nick Belaevski
 *
 */
public class TestIdentityFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper((HttpServletRequest) request) {
            private UserBean getUserBean() {
                HttpSession session = getSession(false);
                if (session != null) {
                    return (UserBean) session.getAttribute("userBean");
                }

                return null;
            }

            @Override
            public boolean isUserInRole(String role) {
                UserBean userBean = getUserBean();
                if (userBean != null) {
                    return userBean.isUserInRole(role);
                }

                return false;
            }

            @Override
            public Principal getUserPrincipal() {
                UserBean userBean = getUserBean();
                if (userBean != null) {
                    return userBean.getPrincipal();
                }

                return null;
            }

            @Override
            public String getRemoteUser() {
                UserBean userBean = getUserBean();
                if (userBean != null) {
                    return userBean.getRolename();
                }
                return null;
            }
        };

        chain.doFilter(wrapper, response);
    }
}

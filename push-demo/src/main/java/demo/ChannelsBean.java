/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @author Nick Belaevski
 *
 */
@SessionScoped
@ManagedBean
public class ChannelsBean implements Serializable {
    private static final long serialVersionUID = 2916670958555985564L;
    private List<Channel> channels = new ArrayList<Channel>();

    public ChannelsBean() {
        channels.add(new Channel("upgrade"));
        channels.add(new Channel("programming"));
        channels.add(new Channel("hardware"));
        channels.add(new Channel("os"));
    }

    public List<Channel> getChannels() {
        return channels;
    }
}

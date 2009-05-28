/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.photoalbum.ui;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.photoalbum.util.Environment;
/**
 * Convenience UI class for application help system
 *
 * @author Andrey Markhel
 */
@Name("help")
@Scope(ScopeType.EVENT)
@AutoCreate
public class Help {

	private String page = "/includes/help/stuff.xhtml";

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
	
	/**
	 * Convenience method to show specified page with help info in modal panel
	 *
	 * @param src - page to show
	 */
	public void navigateTo(String src){
		this.setPage(src);
	}
	
	/**
	 * Convenience method to determine is there need to render application help system.
	 *
	 * @param src - page to show
	 */
	public boolean isShowHelp(){
		return Environment.isShowHelp();
	}
}

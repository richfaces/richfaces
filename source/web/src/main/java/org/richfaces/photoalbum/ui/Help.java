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

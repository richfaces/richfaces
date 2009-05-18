package org.richfaces.photoalbum.util;
/**
 * This class is session listener that observes <code>"org.jboss.seam.sessionExpired"</code> event to delete in production systems users when it's session is expired
 * to prevent flood. Used only on livedemo server. If you don't want this functionality simply delete this class from distributive.
 * 
 * @author Andrey Markhel
 */
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.richfaces.photoalbum.domain.Comment;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IImageAction;

@Scope(ScopeType.SESSION)
@Name("sessionListener")
@Startup
public class SessionListener {
	
	@In(required=false)
	private User user;

	@In
	private IImageAction imageAction;
	@In(value="entityManager")
	private EntityManager em;

	@Destroy
	@Transactional
	@Observer("org.jboss.seam.sessionExpired")
	public void onDestroy(){
		if (!Environment.isInProduction()) {
			return;
		}

		if(user.getId() != null && !user.isPreDefined()){
			user = em.merge(user);
			final List<Comment> userComments = imageAction.findAllUserComments(user);
			for (Comment c : userComments) {
				em.remove(c);
			}
			em.remove(user);
			em.flush();
			
			Events.instance().raiseEvent(Constants.USER_DELETED_EVENT, user);
		}
	}

	
}

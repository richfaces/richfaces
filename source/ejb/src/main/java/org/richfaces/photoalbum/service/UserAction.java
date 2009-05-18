/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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
package org.richfaces.photoalbum.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.richfaces.photoalbum.domain.User;

/**
 * Class for manipulating with user entity. Analogous to DAO pattern.
 *  EJB3 Bean
 *
 * @author Andrey Markhel
 */

@Name("userAction")
@Stateless
@AutoCreate
public class UserAction implements IUserAction {

	@In(value="entityManager")
	EntityManager em;
	
	@In private User user;

	/**
     * Login user. If succes return logged user, otherwise return null
     * @param username - username
     * @param password - password
     * @return user if success
     */
	public User login(String username, String password) {
		return (User)em.createNamedQuery(Constants.USER_LOGIN_QUERY)
		.setParameter(Constants.USERNAME_PARAMETER, username)
		.setParameter(Constants.PASSWORD_PARAMETER, password)
		.getSingleResult();
	}

	/**
     * Persist user entity to database
     * @param user - user to register
	 * @throws PhotoAlbumException
     */
	public void register(User user) throws PhotoAlbumException {
		try{
		em.persist(user);
		em.flush();
		}
        catch(Exception e){
        	throw new PhotoAlbumException(e.getMessage());
        }
	}
	
	/**
     * Synchronize state of user entity with database
     * @return user if success
	 * @throws PhotoAlbumException
     */
	public User updateUser() throws PhotoAlbumException {
		try{
		em.flush();
		}
        catch(Exception e){
        	throw new PhotoAlbumException(e.getMessage());
        }
		return user;
	}
	
	/**
     * Check if user with specified login already exist
     * @return is user with specified login already exist
     */
	public boolean isUserExist(String login) {
		return em.createNamedQuery(Constants.USER_EXIST_QUERY)
		.setParameter(Constants.LOGIN_PARAMETER, login)
		.getResultList().size() != 0;
	}
	
	/**
     * Check if user with specified email already exist
     * @return is user with specified email already exist
     */
	public boolean isEmailExist(String email){
		return em.createNamedQuery(Constants.EMAIL_EXIST_QUERY)
		.setParameter(Constants.EMAIL_PARAMETER, email)
		.getResultList().size() != 0;
	}
}
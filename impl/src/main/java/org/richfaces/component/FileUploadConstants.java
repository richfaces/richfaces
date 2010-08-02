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

package org.richfaces.component;

import org.ajax4jsf.request.MultipartRequest;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public final class FileUploadConstants {
    public static final String FILE_UPLOAD_ACTION = "_richfaces_file_upload_action";
    public static final String FILE_UPLOAD_ACTION_PROGRESS = "progress";
    public static final String FILE_UPLOAD_ACTION_STOP = "richfaces_file_upload_action_stop";

    /**
     * Request parameter name indicated that file was uploaded by RF component
     */
    public static final String FILE_UPLOAD_INDICATOR = "_richfaces_upload_file_indicator";

    /**
     * Session bean name where progress bar's percent map will be stored
     */
    public static final String PERCENT_BEAN_NAME = "_richfaces_upload_percents";

    /**
     * Session bean name where stop keys will be stored
     */
    public static final String REQUEST_KEYS_BEAN_NAME = "_richfaces_request_keys";

    /**
     * Session bean name where request size will be stored
     */
    public static final String REQUEST_SIZE_BEAN_NAME = "_richfaces_request_size";

    /**
     * Session bean name to store max files count allowed to upload
     */
    public static final String UPLOADED_COUNTER = "_richfaces_uploaded_file_counter";

    /**
     * Request parameter that indicates if multipart request forced by rich file
     * upload component
     */
    public static final String UPLOAD_FILES_ID = "_richfaces_upload_uid";
    public static final String FILE_UPLOAD_REQUEST_ATTRIBUTE_NAME = MultipartRequest.class.getName();

    private FileUploadConstants() {

        // private constructor
    }
}

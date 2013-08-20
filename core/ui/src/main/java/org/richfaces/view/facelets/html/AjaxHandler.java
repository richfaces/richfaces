/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.view.facelets.html;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.view.AttachedObjectTarget;
import javax.faces.view.BehaviorHolderAttachedObjectTarget;
import javax.faces.view.facelets.BehaviorConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.CompositeFaceletHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletHandler;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

import org.ajax4jsf.component.behavior.AjaxBehavior;
import org.richfaces.component.AbstractAttachQueue;
import org.richfaces.ui.behavior.BehaviorStack;
import org.richfaces.ui.behavior.BehaviorStack.BehaviorInfo;
import org.richfaces.ui.common.CustomBehaviorHandler;
import org.richfaces.view.facelets.TagHandlerUtils;
import org.richfaces.view.facelets.tag.AjaxBehaviorRule;

/**
 * @author Anton Belevich
 *
 */
public class AjaxHandler extends CustomBehaviorHandler {
    public AjaxHandler(BehaviorConfig config) {
        super(config);
    }

    public boolean isWrapping() {
        if (this.nextHandler instanceof TagHandler) {
            return !(this.nextHandler instanceof AttachQueueHandler);
        }

        if (this.nextHandler instanceof CompositeFaceletHandler) {
            FaceletHandler[] handlers = ((CompositeFaceletHandler) this.nextHandler).getHandlers();
            for (FaceletHandler handler : handlers) {
                if (handler instanceof TagHandler) {
                    if (handler instanceof AttachQueueHandler) {
                        continue;
                    }

                    return true;
                }

                if (handler instanceof CompositeFaceletHandler) {
                    return true;
                }
            }
        }

        return false;
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {

        Application application = ctx.getFacesContext().getApplication();
        Behavior behavior = application.createBehavior(getBehaviorId());
        if (behavior instanceof AjaxBehavior) {
            setAttributes(ctx, behavior);
            AjaxBehavior ajaxBehavior = (AjaxBehavior) behavior;

            FacesContext context = ctx.getFacesContext();

            BehaviorStack ajaxBehaviors = BehaviorStack.getBehaviorStack(context, true);
            String eventName = getEventName();
            ajaxBehaviors.pushBehavior(context, ajaxBehavior, getBehaviorId(), eventName);

            AttachQueueStack attachQueueStack = AttachQueueStack.getStack(context, true);
            AttachQueueInfo attachQueueInfo = new AttachQueueInfo(parent);
            attachQueueStack.push(attachQueueInfo);

            nextHandler.apply(ctx, parent);

            attachQueueStack.pop();

            BehaviorInfo behaviorInfo = ajaxBehaviors.popBehavior();
            if (behaviorInfo != null) {
                if (isWrapping()) {
                    AbstractAttachQueue attachQueue = attachQueueInfo.getAttachQueue();
                    if (attachQueue != null) {
                        List<ClientBehavior> behaviors = behaviorInfo.getBehaviors();
                        if (behaviors != null) {
                            for (ClientBehavior processBehavior : behaviors) {
                                if (processBehavior instanceof AjaxBehavior) {
                                    AjaxBehavior createdAjaxBehavior = (AjaxBehavior) processBehavior;
                                    attachQueue.associateWith(createdAjaxBehavior);
                                }
                            }
                        }
                    }
                } else {
                    AbstractAttachQueue attachQueue = attachQueueInfo.getAttachQueue();
                    if (attachQueue != null) {
                        attachQueue.associateWith(ajaxBehavior);
                    }

                    applyNested(ctx, parent, ajaxBehavior);
                }
            } else {
                // TODO nick - is this possible?
            }
        }
    }

    private void applyNested(FaceletContext ctx, UIComponent parent, AjaxBehavior behavior) {
        if (!ComponentHandler.isNew(parent)) {
            return;
        }

        String eventName = getEventName();
        if (UIComponent.isCompositeComponent(parent)) {
            BeanInfo componentBeanInfo = (BeanInfo) parent.getAttributes().get(UIComponent.BEANINFO_KEY);

            if (null == componentBeanInfo) {
                throw new TagException(tag, "Error: enclosing composite component does not have BeanInfo attribute");
            }

            BeanDescriptor componentDescriptor = componentBeanInfo.getBeanDescriptor();

            if (null == componentDescriptor) {
                throw new TagException(tag, "Error: enclosing composite component BeanInfo does not have BeanDescriptor");
            }

            @SuppressWarnings("unchecked")
            List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>) componentDescriptor
                .getValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);

            if (null == targetList) {
                throw new TagException(tag, "Error: enclosing composite component does not support behavior events");
            }

            boolean supportedEvent = false;

            for (AttachedObjectTarget target : targetList) {
                if (target instanceof BehaviorHolderAttachedObjectTarget) {
                    BehaviorHolderAttachedObjectTarget behaviorTarget = (BehaviorHolderAttachedObjectTarget) target;

                    if ((null != eventName && eventName.equals(behaviorTarget.getName()))
                        || (null == eventName && behaviorTarget.isDefaultEvent())) {
                        supportedEvent = true;

                        break;
                    }
                }
            }

            if (supportedEvent) {
                TagHandlerUtils.getOrCreateRetargetableHandlersList(parent).add(this);
            } else {
                throw new TagException(tag, "Error: enclosing composite component does not support event " + eventName);
            }
        } else if (parent instanceof ClientBehaviorHolder) {
            ClientBehaviorHolder bHolder = (ClientBehaviorHolder) parent;
            eventName = resolveEventName(bHolder);
            bHolder.addClientBehavior(eventName, behavior);
        } else {
            throw new TagException(this.tag, "Unable to attach <a4j:ajax> to non-ClientBehaviorHolder parent");
        }
    }

    private String resolveEventName(ClientBehaviorHolder bHolder) {
        String eventName = getEventName();
        if (null == eventName) {
            eventName = bHolder.getDefaultEventName();

            if (null == eventName) {
                throw new TagException(this.tag, "Event attribute could not be determined: " + eventName);
            }
        } else {
            Collection<String> eventNames = bHolder.getEventNames();

            if (!eventNames.contains(eventName)) {
                throw new TagException(this.tag, eventName + "event is not supported for the "
                    + bHolder.getClass().getSimpleName());
            }
        }

        return eventName;
    }

    @Override
    public MetaRule[] getMetaRules() {
        return new MetaRule[] { AjaxBehaviorRule.INSTANCE };
    }
}

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
package org.richfaces.resource;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.Node;

/**
 * @author Nick Belaevski
 *
 */
public class Java2DAnimatedUserResourceWrapperImpl extends Java2DUserResourceWrapperImpl {
    public Java2DAnimatedUserResourceWrapperImpl(Java2DAnimatedUserResource resourceObject, boolean cacheable, boolean versioned) {
        super(resourceObject, cacheable, versioned);
    }

    private static ImageWriter getSequenceCapableImageWriter(ImageType imageType) {
        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName(imageType.getFormatName());

        while (imageWriters.hasNext()) {
            ImageWriter imageWriter = imageWriters.next();

            if (imageWriter.canWriteSequence()) {
                return imageWriter;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Cannot find sequence-capable image writer for {0} format",
            imageType.getFormatName()));
    }

    private static Node getOrCreateChild(Node root, String name) {
        Node result = null;

        for (Node node = root.getFirstChild(); (node != null) && (result == null); node = node.getNextSibling()) {
            if (name.equals(node.getNodeName())) {
                result = node;
            }
        }

        if (result == null) {
            result = new IIOMetadataNode(name);
            root.appendChild(result);
        }

        return result;
    }

    private static void checkSupportedFormat(ImageType imageType) {
        if (imageType != ImageType.GIF) {
            throw new IllegalArgumentException(MessageFormat.format("Image format {0} is not supported",
                imageType.getFormatName()));
        }
    }

    @Override
    public String getRequestPath() {
        // detect unsupported types early
        checkSupportedFormat(getWrapped().getImageType());
        return super.getRequestPath();
    }

    @Override
    protected void paintAndWrite(ImageOutputStream outputStream) throws IOException {
        Java2DAnimatedUserResource userResource = (Java2DAnimatedUserResource) getWrapped();

        ImageType imageType = userResource.getImageType();
        checkSupportedFormat(imageType);
        ImageWriter imageWriter = getSequenceCapableImageWriter(imageType);
        Dimension dimension = userResource.getDimension();
        BufferedImage image = imageType.createImage(dimension);

        try {
            imageWriter.setOutput(outputStream);

            ImageWriteParam defaultImageWriteParam = imageWriter.getDefaultWriteParam();
            IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(
                ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB), defaultImageWriteParam);
            String metaFormatName = imageMetaData.getNativeMetadataFormatName();
            Node root = imageMetaData.getAsTree(metaFormatName);
            IIOMetadataNode graphicsControlExtensionNode = (IIOMetadataNode) getOrCreateChild(root, "GraphicControlExtension");

            // http://java.sun.com/javase/6/docs/api/javax/imageio/metadata/doc-files/gif_metadata.html
            graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
            graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
            graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
            graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(userResource.getFrameDelay() / 100));
            graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

            Node applicationExtensionsNode = getOrCreateChild(root, "ApplicationExtensions");
            IIOMetadataNode netscapeExtension = new IIOMetadataNode("ApplicationExtension");

            netscapeExtension.setAttribute("applicationID", "NETSCAPE");
            netscapeExtension.setAttribute("authenticationCode", "2.0");

            byte numLoops = (byte) (userResource.isLooped() ? 0x0 : 0x1);

            netscapeExtension.setUserObject(new byte[] { 0x1, numLoops, 0x0 });
            applicationExtensionsNode.appendChild(netscapeExtension);
            imageMetaData.setFromTree(metaFormatName, root);

            imageWriter.prepareWriteSequence(null);

            userResource.startFramesSequence();

            while (userResource.hasNextFrame()) {
                Graphics2D g2d = null;
                try {
                    g2d = createGraphics(image);
                    userResource.paint(g2d);
                    imageWriter.writeToSequence(new IIOImage(image, null, imageMetaData), defaultImageWriteParam);
                } finally {
                    if (g2d != null) {
                        g2d.dispose();
                    }
                }
            }

            imageWriter.endWriteSequence();
        } finally {
            imageWriter.dispose();
        }
    }
}

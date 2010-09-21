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
package org.ajax4jsf.resource;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.ajax4jsf.resource.image.animatedgif.AnimatedGifEncoder;
import org.richfaces.resource.ImageType;

public abstract class AnimationResource extends Java2Dresource {

    private int[] delays;
    private int currFrameIndex = 0;
    
    public AnimationResource() {
        super(ImageType.GIF);
    }

    protected abstract Dimension getFrameSize();
    
    protected abstract int getNumberOfFrames();

    protected int getRepeat() {
        return -1;
    }

    protected int[] getFrameDelays() {
        if (delays == null) {
            delays = new int[getNumberOfFrames()];
            Arrays.fill(delays, 0);
        }

        return delays;
    }

    
    
    @Override
    public InputStream getInputStream() throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(baos);
            Dimension frameSize = getFrameSize();
            int numberOfFrames = getNumberOfFrames();
            BufferedImage frame = null;
            currFrameIndex = 0;
            if (frameSize.getHeight() > 0.0 && frameSize.getWidth() > 0.0
                    && numberOfFrames > 0) {
                AnimatedGifEncoder encoder = new AnimatedGifEncoder();
                encoder.start(output);
                encoder.setRepeat(getRepeat());
                int[] delays = getFrameDelays();
                while (currFrameIndex < numberOfFrames) {
                    frame = getImageType().createImage(frameSize.width,
                            frameSize.height);
                    Graphics2D graphics = frame.createGraphics();
                    paint(graphics, currFrameIndex++);
                    graphics.dispose();
                    encoder.addFrame(frame);
                    if (delays != null && delays.length > currFrameIndex) {
                        encoder.setDelay(delays[currFrameIndex]);
                    }
                }
                encoder.finish();
            }
            output.flush();
            output.close();
            return new ByteArrayInputStream(baos.toByteArray());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    protected abstract void paint(Graphics2D graphics2D, int frameIndex);

}

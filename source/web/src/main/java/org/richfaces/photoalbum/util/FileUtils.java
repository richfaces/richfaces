package org.richfaces.photoalbum.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;

/**
 * Utility class for operations with file-system
 *
 */

public class FileUtils {

    private static final String JPEG = "jpeg";
	private static final String JPG = "jpg";
	private static final int BUFFER_SIZE = 4 * 1024;
    private static final boolean CLOCK = true;
    private static final boolean VERIFY = true;

    /**
     * Utility method for copying file
     * @param srcFile - source file
     * @param destFile - destination file
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
        if (!srcFile.getPath().toLowerCase().endsWith(JPG) && !srcFile.getPath().toLowerCase().endsWith(JPEG)) {
            return;
        }
        final InputStream in = new FileInputStream(srcFile);
        final OutputStream out = new FileOutputStream(destFile);
        try{
            long millis = System.currentTimeMillis();
            CRC32 checksum;
            if (VERIFY) {
                checksum = new CRC32();
                checksum.reset();
            }
            final byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = in.read(buffer);
            while (bytesRead >= 0) {
                if (VERIFY) {
                    checksum.update(buffer, 0, bytesRead);
                }
                out.write(buffer, 0, bytesRead);
                bytesRead = in.read(buffer);
            }
            if (CLOCK) {
                millis = System.currentTimeMillis() - millis;
                System.out.println("Copy file '" + srcFile.getPath() + "' on " + millis / 1000L + " second(s)");
            }
        }catch(IOException e){
        	throw e;
        }finally{
        	out.close();
            in.close();
        }
    }

    /**
     * Utility method for copying directory
     * @param srcDir - source directory
     * @param dstDir - destination directory
     */
    public static void copyDirectory(File srcDir, File dstDir)
            throws IOException {

        if (".svn".equals(srcDir.getName())) {
            return;
        }

        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }

            for (String aChildren : srcDir.list()) {
                copyDirectory(new File(srcDir, aChildren), new File(dstDir, aChildren));
            }
        } else {
            copyFile(srcDir, dstDir);
        }
    }

    /**
     * Utility method for delete directory
     * @param dir - directory to delete
     * @param isInitialDelete - determine if the deleting process running at startup or on destroy of application
     * @return true if directory succesfully deleted
     */
    public static boolean deleteDirectory(File dir , boolean isInitialDelete){
        if (dir.isDirectory()) {
            if (dir.exists()) {
                for (File child : dir.listFiles()) {
                	try{
                		deleteDirectory(child, isInitialDelete);
                	}catch(Exception e){
                		if(isInitialDelete){
                			continue;
                		}
                		else return false;
                	}
                }
            }

        } else {
            if (dir.exists()) {
                final boolean isFileDeleted = dir.delete();
                System.out.println((isFileDeleted ? "OK     " : "ERROR ") +
                        "Delete file '" + dir.getPath() + '\'');
            }
        }
        dir.delete();
        return true;
    }
	
    /**
     * Utility method for concatenation names of collection of files
     * @param files - array of strings to concatenate
     * @return concatenated string
     */
	public static String joinFiles(String... files) {
		final StringBuilder res = new StringBuilder();
		for (String file : files) {
			res.append(file).append(File.separatorChar);
		}

		return res.substring(0, res.length() - 1);
	}

	 /**
     * Utility method for delete file
     * @param file - file to delete
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
    
    /**
     * Utility method to read image from disk and transform image to BufferedImage object
     * @param data - relative path to the image
     * @param format - file prefix of the image
     * @return BufferedImage representation of the image
     *
     */
    public static BufferedImage bitmapToImage(String data, String format) throws IOException {
        final InputStream inb = new FileInputStream(data);
        final ImageReader rdr = ImageIO.getImageReadersByFormatName(format).next();
        final ImageInputStream imageInput = ImageIO.createImageInputStream(inb);
        rdr.setInput(imageInput);
        final BufferedImage image = rdr.read(0);
        inb.close();
        return image;
    }

    /**
     * Utility method to write BufferedImage object to disk
     * @param image - BufferedImage object to save.
     * @param data - relative path to the image
     * @param format - file prefix of the image
     * @return BufferedImage representation of the image
     *
     */
    public static void imageToBitmap(BufferedImage image, String data, String format) throws IOException {
        final OutputStream inb = new FileOutputStream(data);
        final ImageWriter wrt = ImageIO.getImageWritersByFormatName(format).next();
        final ImageInputStream imageInput = ImageIO.createImageOutputStream(inb);
        wrt.setOutput(imageInput);
        wrt.write(image);
        inb.close();
    }

    /**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * @param img           the original image to be scaled
     * @param targetWidth   the desired width of the scaled instance,
     *                      in pixels
     * @param targetHeight  the desired height of the scaled instance,
     *                      in pixels
     * @param hint          one of the rendering hints that corresponds to
     *                      {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *                      {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *                      {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *                      {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *                      scaling technique that provides higher quality than the usual
     *                      one-step technique (only useful in downscaling cases, where
     *                      {@code targetWidth} or {@code targetHeight} is
     *                      smaller than the original dimensions, and generally only when
     *                      the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage}
     */
    public static BufferedImage getScaledInstance(BufferedImage img,
                                                  int targetWidth,
                                                  int targetHeight,
                                                  Object hint,
                                                  boolean higherQuality) {
        final int type = img.getTransparency() == Transparency.OPAQUE ?
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage) img;
        int w;
        int h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            final BufferedImage tmp = new BufferedImage(w, h, type);
            final Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }

    /**
     * Utility method for creation of directory
     * @param directory - directory to create
     *
     */
	public static void addDirectory(File directory) {
		if (directory.exists()) {
            deleteDirectory(directory, false);
        }
		directory.mkdirs();
	}
}
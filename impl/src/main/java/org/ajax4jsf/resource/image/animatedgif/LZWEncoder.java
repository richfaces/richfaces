package org.ajax4jsf.resource.image.animatedgif;

import java.io.OutputStream;
import java.io.IOException;

//==============================================================================
//  Adapted from Jef Poskanzer's Java port by way of J. M. G. Elliott.
//  K Weiner 12/00

class LZWEncoder {
    static final int BITS = 12;
    static final int HSIZE = 5003;
    private static final int EOF = -1;
    private int imgW;
    private int imgH;
    private byte[] pixAry;
    private int initCodeSize;
    private int remaining;
    private int curPixel;
    
    private int nBits; // number of bits/code
    private int maxbits = BITS; // user settable max # bits/code
    private int maxcode; // maximum code, given n_bits
    private int maxmaxcode = 1 << BITS; // should NEVER generate this code

    private int[] htab = new int[HSIZE];
    private int[] codetab = new int[HSIZE];

    private int hsize = HSIZE; // for dynamic table sizing

    private int freeEnt = 0; // first unused entry

    // block compression parameters -- after all codes are used up,
    // and compression rate changes, start over.
    private boolean clearFlg = false;

    // Algorithm:  use open addressing double hashing (no chaining) on the
    // prefix code / next character combination.  We do a variant of Knuth's
    // algorithm D (vol. 3, sec. 6.4) along with G. Knott's relatively-prime
    // secondary probe.  Here, the modular division first probe is gives way
    // to a faster exclusive-or manipulation.  Also do block compression with
    // an adaptive reset, whereby the code table is cleared when the compression
    // ratio decreases, but after the table fills.  The variable-length output
    // codes are re-sized at this point, and a special CLEAR code is generated
    // for the decompressor.  Late addition:  construct the table according to
    // file size for noticeable speed improvement on small files.  Please direct
    // questions about this implementation to ames!jaw.

    private int gInitBits;

    private int clearCode;
    private int eOFCode;

    // output
    //
    // Output the given code.
    // Inputs:
    //      code:   A n_bits-bit integer.  If == -1, then EOF.  This assumes
    //              that n_bits =< wordsize - 1.
    // Outputs:
    //      Outputs code to the file.
    // Assumptions:
    //      Chars are 8 bits long.
    // Algorithm:
    //      Maintain a BITS character long buffer (so that 8 codes will
    // fit in it exactly).  Use the VAX insv instruction to insert each
    // code in turn.  When the buffer fills up empty it and start over.

    private int curAccum = 0;
    private int curBits = 0;
    private int aCount;

    // Define the storage for the packet accumulator
    private byte[] accum = new byte[256];
    
    private int[] masks = 
    {
        0x0000,
        0x0001,
        0x0003,
        0x0007,
        0x000F,
        0x001F,
        0x003F,
        0x007F,
        0x00FF,
        0x01FF,
        0x03FF,
        0x07FF,
        0x0FFF,
        0x1FFF,
        0x3FFF,
        0x7FFF,
        0xFFFF 
    };
    
    LZWEncoder(int width, int height, byte[] pixels, int colorDepth) {
        imgW = width;
        imgH = height;
        pixAry = pixels;
        initCodeSize = Math.max(2, colorDepth);
    }
    
    // Add a character to the end of the current packet, and if it is 254
    // characters, flush the packet to disk.
    void charOut(byte c, OutputStream outs) throws IOException {
        accum[aCount++] = c;
        if (aCount >= 254) {
            flushChar(outs);
        }
    }
    
    // Clear out the hash table

    // table clear for block compress
    void clBlock(OutputStream outs) throws IOException {
        clHash(hsize);
        freeEnt = clearCode + 2;
        clearFlg = true;

        output(clearCode, outs);
    }
    
    // reset code table
    void clHash(int hsize) {
        for (int i = 0; i < hsize; ++i) {
            htab[i] = -1;
        }
    }
    
    void compress(int initBits, OutputStream outs) throws IOException {
        int fcode;
        int i /* = 0 */;
        int c;
        int ent;
        int disp;
        int hsizeReg;
        int hshift;

        // Set up the globals:  g_init_bits - initial number of bits
        gInitBits = initBits;

        // Set up the necessary values
        clearFlg = false;
        nBits = gInitBits;
        maxcode = maxcode(nBits);

        clearCode = 1 << (initBits - 1);
        eOFCode = clearCode + 1;
        freeEnt = clearCode + 2;

        aCount = 0; // clear packet

        ent = nextPixel();

        hshift = 0;
        for (fcode = hsize; fcode < 65536; fcode *= 2) {
            ++hshift;
        }
        hshift = 8 - hshift; // set hash code range bound

        hsizeReg = hsize;
        clHash(hsizeReg); // clear hash table

        output(clearCode, outs);

    outer_loop : 
    	while ((c = nextPixel()) != EOF) {
            fcode = (c << maxbits) + ent;
            i = (c << hshift) ^ ent; // xor hashing

            if (htab[i] == fcode) {
                ent = codetab[i];
                continue;
            } else if (htab[i] >= 0) {
                disp = hsizeReg - i; // secondary hash (after G. Knott)
                if (i == 0) {
                    disp = 1;
                }
                do {
                    i -= disp;
                    if (i < 0) {
                        i += hsizeReg;
                    }

                    if (htab[i] == fcode) {
                        ent = codetab[i];
                        continue outer_loop;
                    }
                } while (htab[i] >= 0);
            }
            output(ent, outs);
            ent = c;
            if (freeEnt < maxmaxcode) {
                codetab[i] = freeEnt++; // code -> hashtable
                htab[i] = fcode;
            } else {
                clBlock(outs);
            }
        }
        // Put out the final code.
        output(ent, outs);
        output(eOFCode, outs);
    }
    
    //----------------------------------------------------------------------------
    void encode(OutputStream os) throws IOException {
        os.write(initCodeSize); // write "initial code size" byte

        remaining = imgW * imgH; // reset navigation variables
        curPixel = 0;

        compress(initCodeSize + 1, os); // compress and write the pixel data

        os.write(0); // write block terminator
    }
    
    // Flush the packet to disk, and reset the accumulator
    void flushChar(OutputStream outs) throws IOException {
        if (aCount > 0) {
            outs.write(aCount);
            outs.write(accum, 0, aCount);
            aCount = 0;
        }
    }
    
    final int maxcode(int nBits) {
        return (1 << nBits) - 1;
    }
    
    //----------------------------------------------------------------------------
    // Return the next pixel from the image
    //----------------------------------------------------------------------------
    private int nextPixel() {
        if (remaining == 0) {
            return EOF;
        }

        --remaining;

        byte pix = pixAry[curPixel++];

        return pix & 0xff;
    }
    
    void output(int code, OutputStream outs) throws IOException {
        curAccum &= masks[curBits];

        if (curBits > 0) {
            curAccum |= (code << curBits);
        } else {
            curAccum = code;
        }

        curBits += nBits;

        while (curBits >= 8) {
            charOut((byte) (curAccum & 0xff), outs);
            curAccum >>= 8;
            curBits -= 8;
        }

        // If the next entry is going to be too big for the code size,
        // then increase it, if possible.
        if (freeEnt > maxcode || clearFlg) {
            if (clearFlg) {
            	nBits = gInitBits;
                maxcode = maxcode(nBits);
                clearFlg = false;
            } else {
                ++nBits;
                if (nBits == maxbits) {
                    maxcode = maxmaxcode;
                } else {
                    maxcode = maxcode(nBits);
                }
            }
        }

        if (code == eOFCode) {
            // At EOF, write the rest of the buffer.
            while (curBits > 0) {
                charOut((byte) (curAccum & 0xff), outs);
                curAccum >>= 8;
                curBits -= 8;
            }

            flushChar(outs);
        }
    }
}

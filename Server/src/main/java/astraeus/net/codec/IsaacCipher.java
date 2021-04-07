/* Copyright (c) 2009 Graham Edgecombe, Blake Beaupain and Brett Russell
*
* More information about Hyperion may be found on this website:
*    http://hyperion.grahamedgecombe.com/
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package astraeus.net.codec;

/**
* An implementation of an ISAAC cipher. See <a href="http://en.wikipedia.org/wiki/ISAAC_(cipher)">
* http://en.wikipedia.org/wiki/ISAAC_(cipher)</a> for more information.
* <p>
* <p>
* This implementation is based on the one written by Bob Jenkins, which is available at <a
* href="http://www.burtleburtle.net/bob/java/rand/Rand.java"> http://www.burtleburtle.net/bob/java/rand/Rand.java</a>.
*
* @author Graham Edgecombe
*/
public final class IsaacCipher {

   /**
    * The golden ratio.
    */
   private static final int RATIO = 0x9e3779b9;

   /**
    * The log of the size of the results and memory arrays.
    */
   private static final int SIZE_LOG = 8;

   /**
    * The size of the results and memory arrays.
    */
   private static final int SIZE = 1 << SIZE_LOG;

   /**
    * For pseudorandom lookup.
    */
   private static final int MASK = (SIZE - 1) << 2;

   /**
    * The count through the results.
    */
   private int count = 0;

   /**
    * The results.
    */
   private int results[] = new int[SIZE];

   /**
    * The internal memory state.
    */
   private int memory[] = new int[SIZE];

   /**
    * The accumulator.
    */
   private int a;

   /**
    * The last result.
    */
   private int b;

   /**
    * The counter.
    */
   private int c;

   /**
    * Creates the ISAAC cipher.
    *
    * @param seed The seed.
    */
   public IsaacCipher(int[] seed) {
       for (int i = 0; i < seed.length; i++) {
           results[i] = seed[i];
       }
       init(true);
   }

   /**
    * Gets the next value.
    *
    * @return The next value.
    */
   public int getKey() {
       if (count-- == 0) {
           isaac();
           count = SIZE - 1;
       }
       return results[count];
   }

   /**
    * Generates 256 results.
    */
   public void isaac() {
       int i, j, x, y;
       b += ++c;
       for (i = 0, j = SIZE / 2; i < SIZE / 2; ) {
           x = memory[i];
           a ^= a << 13;
           a += memory[j++];
           memory[i] = y = memory[(x & MASK) >> 2] + a + b;
           results[i++] = b = memory[((y >> SIZE_LOG) & MASK) >> 2] + x;

           x = memory[i];
           a ^= a >>> 6;
           a += memory[j++];
           memory[i] = y = memory[(x & MASK) >> 2] + a + b;
           results[i++] = b = memory[((y >> SIZE_LOG) & MASK) >> 2] + x;

           x = memory[i];
           a ^= a << 2;
           a += memory[j++];
           memory[i] = y = memory[(x & MASK) >> 2] + a + b;
           results[i++] = b = memory[((y >> SIZE_LOG) & MASK) >> 2] + x;

           x = memory[i];
           a ^= a >>> 16;
           a += memory[j++];
           memory[i] = y = memory[(x & MASK) >> 2] + a + b;
           results[i++] = b = memory[((y >> SIZE_LOG) & MASK) >> 2] + x;
       }
       for (j = 0; j < SIZE / 2; ) {
           x = memory[i];
           a ^= a << 13;
           a += memory[j++];
           memory[i] = y = memory[(x & MASK) >> 2] + a + b;
           results[i++] = b = memory[((y >> SIZE_LOG) & MASK) >> 2] + x;

           x = memory[i];
           a ^= a >>> 6;
           a += memory[j++];
           memory[i] = y = memory[(x & MASK) >> 2] + a + b;
           results[i++] = b = memory[((y >> SIZE_LOG) & MASK) >> 2] + x;

           x = memory[i];
           a ^= a << 2;
           a += memory[j++];
           memory[i] = y = memory[(x & MASK) >> 2] + a + b;
           results[i++] = b = memory[((y >> SIZE_LOG) & MASK) >> 2] + x;

           x = memory[i];
           a ^= a >>> 16;
           a += memory[j++];
           memory[i] = y = memory[(x & MASK) >> 2] + a + b;
           results[i++] = b = memory[((y >> SIZE_LOG) & MASK) >> 2] + x;
       }
   }

   /**
    * Initializes the ISAAC.
    *
    * @param flag Flag indicating if we should perform a second pass.
    */
   public void init(boolean flag) {
       int i;
       int a, b, c, d, e, f, g, h;
       a = b = c = d = e = f = g = h = RATIO;
       for (i = 0; i < 4; ++i) {
           a ^= b << 11;
           d += a;
           b += c;
           b ^= c >>> 2;
           e += b;
           c += d;
           c ^= d << 8;
           f += c;
           d += e;
           d ^= e >>> 16;
           g += d;
           e += f;
           e ^= f << 10;
           h += e;
           f += g;
           f ^= g >>> 4;
           a += f;
           g += h;
           g ^= h << 8;
           b += g;
           h += a;
           h ^= a >>> 9;
           c += h;
           a += b;
       }
       for (i = 0; i < SIZE; i += 8) {
           if (flag) {
               a += results[i];
               b += results[i + 1];
               c += results[i + 2];
               d += results[i + 3];
               e += results[i + 4];
               f += results[i + 5];
               g += results[i + 6];
               h += results[i + 7];
           }
           a ^= b << 11;
           d += a;
           b += c;
           b ^= c >>> 2;
           e += b;
           c += d;
           c ^= d << 8;
           f += c;
           d += e;
           d ^= e >>> 16;
           g += d;
           e += f;
           e ^= f << 10;
           h += e;
           f += g;
           f ^= g >>> 4;
           a += f;
           g += h;
           g ^= h << 8;
           b += g;
           h += a;
           h ^= a >>> 9;
           c += h;
           a += b;
           memory[i] = a;
           memory[i + 1] = b;
           memory[i + 2] = c;
           memory[i + 3] = d;
           memory[i + 4] = e;
           memory[i + 5] = f;
           memory[i + 6] = g;
           memory[i + 7] = h;
       }
       if (flag) {
           for (i = 0; i < SIZE; i += 8) {
               a += memory[i];
               b += memory[i + 1];
               c += memory[i + 2];
               d += memory[i + 3];
               e += memory[i + 4];
               f += memory[i + 5];
               g += memory[i + 6];
               h += memory[i + 7];
               a ^= b << 11;
               d += a;
               b += c;
               b ^= c >>> 2;
               e += b;
               c += d;
               c ^= d << 8;
               f += c;
               d += e;
               d ^= e >>> 16;
               g += d;
               e += f;
               e ^= f << 10;
               h += e;
               f += g;
               f ^= g >>> 4;
               a += f;
               g += h;
               g ^= h << 8;
               b += g;
               h += a;
               h ^= a >>> 9;
               c += h;
               a += b;
               memory[i] = a;
               memory[i + 1] = b;
               memory[i + 2] = c;
               memory[i + 3] = d;
               memory[i + 4] = e;
               memory[i + 5] = f;
               memory[i + 6] = g;
               memory[i + 7] = h;
           }
       }
       isaac();
       count = SIZE;
   }
}

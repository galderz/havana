/*
 * Copyright (c) 2017, Red Hat Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package lang;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

@JCStressTest
@Outcome(id = "-1", expect = ACCEPTABLE, desc = "Object is not seen yet.")
@Outcome(id = "8", expect = ACCEPTABLE,  desc = "Seen the complete object.")
@Outcome(expect = FORBIDDEN, desc = "Seeing partially constructed object.")
@State
public class FinalInitExtendedNoFinalTest
{
    int v = 1;

    MyObjectExtended o;

    @Actor
    public void actor1(II_Result r)
    {
        o = new MyObjectExtended(v);
    }

    @Actor
    public void actor2(II_Result r)
    {
        MyObjectExtended o = this.o;
        if (o != null) {
            r.r1 = o.x8 + o.x7 + o.x6 + o.x5 + o.x4 + o.x3 + o.x2 + o.x1;
        } else {
            r.r1 = -1;
        }
    }

    class MyObject
    {
        final int x4;
        public MyObject(int v) {
            x4 = v;
        }
    }

    class MyObjectExtended extends MyObject
    {
        int x1, x2, x3;
        int x5, x6, x7;
        int x8;
        public MyObjectExtended(int v) {
            super(v);
            x1 = v;
            x2 = v;
            x3 = v;
            x5 = v;
            x6 = v;
            x7 = v;
            x8 = v;
        }
    }
}

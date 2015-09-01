/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.ctrnn.layout;

import java.util.ArrayList;
import java.util.Random;


/**
 *
 * @author Miles
 */
public class Range {
    public float low,high;
        public Range(float low2, float high2) {
            this.low = low2; this.high = high2;
        }
        public float getDiff() {
            return high - low;
        }
        public float map(float x) {
            //maps a val of -1 to 1 to this range
            return getDiff() * (x + 1) / 2 + low;
        }
        public float[] map(float[] x) {
            //same with an array
            float[] r = new float[x.length];
            for(int i = 0; i< x.length; i++) {
                r[i] = map(x[i]);
            }
            return r;
        }
        public ArrayList<Float> map(ArrayList<Float> x) {
            ArrayList<Float> r = new ArrayList<>(x.size());
            for(float d : x)
                r.add(map(d));
            return r;
        }
        
        public float getRandDouble() {
            Random rand = new Random();
            return (float)rand.nextDouble() * getDiff() - low;
        }
        public float clip(float x) {
            if(x < low)
                return low;
            if(x > high)
                return high;
            return x;
        }
        @Override
        public boolean equals(Object obj) {
        	if ( !(obj instanceof Range) )
				return false;
			Range other = (Range) obj;
			return (low == other.low) && (high == other.high);
        }
        
        @Override
        public String toString() {
        	return "[Range " + low + ":" + high + "]";
        }
        
        
}

package is.idega.idegaweb.pheidippides.util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.codec.binary.Base32;

public class GiftCardUtil {

    private AtomicLong sequence;
    private Random random;
    private Random seed;

    public GiftCardUtil() {
    	seed = new SecureRandom();
        sequence = new AtomicLong(System.currentTimeMillis() - seed.nextLong());
        random = new SecureRandom();
    }

    public String generateCode() {
        byte[] id = new byte[10];
        longTo5ByteArray(sequence.incrementAndGet(), id);
        byte[] rnd = new byte[5];
        random.nextBytes(rnd);
        System.arraycopy(rnd, 0, id, 5, 5);
        return new Base32().encodeAsString(id);
    }

    private void longTo5ByteArray(long l, byte[] b) {
        b[0] = (byte) (l >>> 32);
        b[1] = (byte) (l >>> 24);
        b[2] = (byte) (l >>> 16);
        b[3] = (byte) (l >>> 8);
        b[4] = (byte) (l >>> 0);
    }
    
    public static void main(String args[]) {
    	GiftCardUtil util = new GiftCardUtil();
    	System.out.println(util.generateCode());
    }
    
}
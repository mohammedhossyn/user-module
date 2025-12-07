package com.usermodule.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class PasswordEncoderService {
    private static byte[] encrypKey;
    private int _key[];  // The 128 bit key.
    private byte _keyBytes[];  // original key as found
    private int _padding;      // amount of padding added in byte --> integer conversion.

    /**
     * /**
     * Accepts key for enciphering/deciphering.
     *
     * @param key 128 bit (16 byte) key.
     * @throws ArrayIndexOutOfBoundsException if the key isn't the correct length.
     */
    public PasswordEncoderService(byte key[]) {
        int klen = key.length;
        _key = new int[4];

        // Incorrect key length throws exception.
        if (klen != 16)
            throw new ArrayIndexOutOfBoundsException(this.getClass().getName() + ": Key is not 16 bytes");

        int j, i;
        for (i = 0, j = 0; j < klen; j += 4, i++)
            _key[i] = (key[j] << 24) | (((key[j + 1]) & 0xff) << 16) | (((key[j + 2]) & 0xff) << 8) | ((key[j + 3]) & 0xff);

        _keyBytes = key;  // save for toString.
    }


    /**
     * Representation of TEA class
     */
    public String toString() {
        String tea = this.getClass().getName();
        tea += ": Tiny Encryption Algorithm (TEA)  key: " + dumpBytes(_keyBytes);
        return tea;
    }


    /**
     * Encipher two <code>int</code>s.
     * Replaces the original contents of the parameters with the results.
     * The integers are usually created from 8 bytes.
     * The usual way to collect bytes to the int array is:
     * <PRE>
     * byte ba[] = { .... };
     * int v[] = new int[2];
     * v[0] = (ba[j] << 24 ) | (((ba[j+1])&0xff) << 16) | (((ba[j+2])&0xff) << 8) | ((ba[j+3])&0xff);
     * v[1] = (ba[j+4] << 24 ) | (((ba[j+5])&0xff) << 16) | (((ba[j+6])&0xff) << 8) | ((ba[j+7])&0xff);
     * v = encipher(v);
     * </PRE>
     *
     * @param v two <code>int</code> array as input.
     * @return array of two <code>int</code>s, enciphered.
     */
    public int[] encipher(int v[]) {
        int y = v[0];
        int z = v[1];
        int sum = 0;
        int delta = 0x9E3779B9;
        int a = _key[0];
        int b = _key[1];
        int c = _key[2];
        int d = _key[3];
        int n = 32;

        while (n-- > 0) {
            sum += delta;
            y += (z << 4) + a ^ z + sum ^ (z >> 5) + b;
            z += (y << 4) + c ^ y + sum ^ (y >> 5) + d;
        }

        v[0] = y;
        v[1] = z;

        return v;
    }


    /**
     * Decipher two <code>int</code>s.
     * Replaces the original contents of the parameters with the results.
     * The integers are usually decocted to 8 bytes.
     * The decoction of the <code>int</code>s to bytes can be done
     * this way.
     * <PRE>
     * int x[] = decipher(ins);
     * outb[j]   = (byte)(x[0] >>> 24);
     * outb[j+1] = (byte)(x[0] >>> 16);
     * outb[j+2] = (byte)(x[0] >>> 8);
     * outb[j+3] = (byte)(x[0]);
     * outb[j+4] = (byte)(x[1] >>> 24);
     * outb[j+5] = (byte)(x[1] >>> 16);
     * outb[j+6] = (byte)(x[1] >>> 8);
     * outb[j+7] = (byte)(x[1]);
     * </PRE>
     *
     * @param v <code>int</code> array of 2
     * @return deciphered <code>int</code> array of 2
     */
    public int[] decipher(int v[]) {
        int y = v[0];
        int z = v[1];
        int sum = 0xC6EF3720;
        int delta = 0x9E3779B9;
        int a = _key[0];
        int b = _key[1];
        int c = _key[2];
        int d = _key[3];
        int n = 32;

        // sum = delta<<5, in general sum = delta * n

        while (n-- > 0) {
            z -= (y << 4) + c ^ y + sum ^ (y >> 5) + d;
            y -= (z << 4) + a ^ z + sum ^ (z >> 5) + b;
            sum -= delta;
        }

        v[0] = y;
        v[1] = z;

        return v;
    }

    /**
     * Encipher two <code>bytes</code>s.
     *
     * @param v <code>byte</code> array of 2
     * @return enciphered <code>byte</code> array of 2
     */
    public byte[] encipher(byte v[]) {
        byte y = v[0];
        byte z = v[1];
        int sum = 0;
        int delta = 0x9E3779B9;
        int a = _key[0];
        int b = _key[1];
        int c = _key[2];
        int d = _key[3];
        int n = 32;

        while (n-- > 0) {
            sum += delta;
            y += (z << 4) + a ^ z + sum ^ (z >> 5) + b;
            z += (y << 4) + c ^ y + sum ^ (y >> 5) + d;
        }

        v[0] = y;
        v[1] = z;

        return v;
    }

    /**
     * Decipher two <code>bytes</code>s.
     *
     * @param v <code>byte</code> array of 2
     * @return decipherd <code>byte</code> array of 2
     */
    public byte[] decipher(byte v[]) {
        byte y = v[0];
        byte z = v[1];
        int sum = 0xC6EF3720;
        int delta = 0x9E3779B9;
        int a = _key[0];
        int b = _key[1];
        int c = _key[2];
        int d = _key[3];
        int n = 32;

        // sum = delta<<5, in general sum = delta * n

        while (n-- > 0) {
            z -= (y << 4) + c ^ y + sum ^ (y >> 5) + d;
            y -= (z << 4) + a ^ z + sum ^ (z >> 5) + b;
            sum -= delta;
        }

        v[0] = y;
        v[1] = z;

        return v;
    }

    /**
     * Byte wrapper for encoding.
     * Converts bytes to ints.
     * Padding will be added if required.
     *
     * @param b incoming <code>byte</code> array
     * @param b count
     * @return integer conversion array, possibly with padding.
     * @see #padding
     */
    int[] encode(byte b[], int count) {
        int j, i;
        int bLen = count;
        byte bp[] = b;

        _padding = bLen % 8;
        if (_padding != 0)   // Add some padding, if necessary.
        {
            _padding = 8 - (bLen % 8);
            bp = new byte[bLen + _padding];
            System.arraycopy(b, 0, bp, 0, bLen);
            bLen = bp.length;
        }

        int intCount = bLen / 4;
        int r[] = new int[2];
        int out[] = new int[intCount];

        for (i = 0, j = 0; j < bLen; j += 8, i += 2) {
            // Java's unforgivable lack of unsigneds causes more bit
            // twiddling than this language really needs.
            r[0] = (bp[j] << 24) | (((bp[j + 1]) & 0xff) << 16) | (((bp[j + 2]) & 0xff) << 8) | ((bp[j + 3]) & 0xff);
            r[1] = (bp[j + 4] << 24) | (((bp[j + 5]) & 0xff) << 16) | (((bp[j + 6]) & 0xff) << 8) | ((bp[j + 7]) & 0xff);
            encipher(r);
            out[i] = r[0];
            out[i + 1] = r[1];
        }

        return out;
    }

    /**
     * Report how much padding was done in the last encode.
     *
     * @return bytes of padding added
     * @see #encode
     */
    public int padding() {
        return _padding;
    }

    /**
     * Convert a byte array to ints and then decode.
     * There may be some padding at the end of the byte array from
     * the previous encode operation.
     *
     * @param b     bytes to decode
     * @param count number of bytes in the array to decode
     * @return <code>byte</code> array of decoded bytes.
     */
    public byte[] decode(byte b[], int count) {
        int i, j;

        int intCount = count / 4;
        int ini[] = new int[intCount];
        for (i = 0, j = 0; i < intCount; i += 2, j += 8) {
            ini[i] = (b[j] << 24) | (((b[j + 1]) & 0xff) << 16) | (((b[j + 2]) & 0xff) << 8) | ((b[j + 3]) & 0xff);
            ini[i + 1] = (b[j + 4] << 24) | (((b[j + 5]) & 0xff) << 16) | (((b[j + 6]) & 0xff) << 8) | ((b[j + 7]) & 0xff);
        }
        return decode(ini);
    }

    /**
     * Decode an integer array.
     * There may be some padding at the end of the byte array from
     * the previous encode operation.
     *
     * @param b bytes to decode
     * @return <code>byte</code> array of decoded bytes.
     */
    public byte[] decode(int b[]) {
        // create the large number and start stripping ints out, two at a time.
        int intCount = b.length;

        byte outb[] = new byte[intCount * 4];
        int tmp[] = new int[2];

        // decipher all the ints.
        int i, j;
        for (j = 0, i = 0; i < intCount; i += 2, j += 8) {
            tmp[0] = b[i];
            tmp[1] = b[i + 1];
            decipher(tmp);
            outb[j] = (byte) (tmp[0] >>> 24);
            outb[j + 1] = (byte) (tmp[0] >>> 16);
            outb[j + 2] = (byte) (tmp[0] >>> 8);
            outb[j + 3] = (byte) (tmp[0]);
            outb[j + 4] = (byte) (tmp[1] >>> 24);
            outb[j + 5] = (byte) (tmp[1] >>> 16);
            outb[j + 6] = (byte) (tmp[1] >>> 8);
            outb[j + 7] = (byte) (tmp[1]);
        }

        return outb;
    }


    // Display some bytes in HEX.
    //
    private String dumpBytes(byte b[]) {
        StringBuffer r = new StringBuffer();
        final String hex = "0123456789ABCDEF";

        for (int i = 0; i < b.length; i++) {
            int c = ((b[i]) >>> 4) & 0xf;
            r.append(hex.charAt(c));
            c = ((int) b[i] & 0xf);
            r.append(hex.charAt(c));
        }

        return r.toString();
    }

    public String encode(String value, byte[] encryptionKey) {
        encrypKey = encryptionKey;
        PasswordEncoderService tea = new PasswordEncoderService(encrypKey);

        byte plainSource[];
        try {
            plainSource = value.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            plainSource = value.getBytes();
        }
        int enc[] = tea.encode(plainSource, plainSource.length);

        String strEncode = new String();
        for (int i = 0; i < enc.length; i++) {
            if (i != 0) {
                strEncode = strEncode.concat("#");
            }
            strEncode = strEncode.concat(Integer.toString(enc[i]));
        }
        strEncode = strEncode.concat("!");
        strEncode = strEncode.concat(Integer.toString(tea.padding()));
        return strEncode;
    }

    public String decode(String value, byte[] encryptionKey) {
        encrypKey = encryptionKey;
        PasswordEncoderService tea = new PasswordEncoderService(encrypKey);
        String[] parseEncode = value.split("!");

        int pandding = Integer.parseInt(parseEncode[1]);
        String[] args = parseEncode[0].split("#");
        int enc[] = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            enc[i] = Integer.parseInt(args[i]);
        }
        byte dec[] = tea.decode(enc);
        String strDecode = new String(dec);
        return strDecode.substring(0, (strDecode.length() - pandding));
    }
}
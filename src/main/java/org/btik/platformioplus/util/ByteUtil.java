package org.btik.platformioplus.util;

import java.security.SecureRandom;

/**
 * @author lustre
 * @since 2022/10/23 12:02
 */
public class ByteUtil {
    private static final byte[] HEX_BYTE_DIGEST = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F'
    };

    public static String toHexString(byte[] bytes) {

        byte[] hexBytes = toHexBytes(bytes);
        if (hexBytes == null) {
            return null;
        }
        return new String(hexBytes);
    }

    public static byte[] toHexBytes(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return null;
        }
        byte[] result = new byte[bytes.length << 1];
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            result[2 * i] = HEX_BYTE_DIGEST[(b & 0xf0) >> 4];
            result[2 * i + 1] = HEX_BYTE_DIGEST[b & 0xf];
        }
        return result;
    }

    public static byte[] uuid(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] uuid = new byte[length];
        secureRandom.nextBytes(uuid);
        return uuid;
    }

    public static String uuidStr(int length) {
        return toHexString(uuid(length));
    }

}

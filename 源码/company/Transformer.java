package com.company;

public class Transformer {
    // 把int型整数值转换位0-1串：
    public static String intToBits(int iValue) {
        char[] cArray = new char[32];
        int single = 1;
        for (int i=0; i<32; i++) {
            int andResult = iValue & single;
            if (andResult == 0)
                cArray[31-i] = '0';
            else
                cArray[31-i] = '1';
            single = single << 1;
        }
        String bits = new String(cArray);
        return bits;
    }
    // 把0-1串转换成int型整数值：
    public static int bitsToInt(String bits) {
        int iValue = 0;
        int single = 1;
        for (int i=0; i<32; i++) {
            if (bits.charAt(31-i) == '1')
                iValue = iValue | single;
            single = single << 1;
        }
        return iValue;
    }
    // 把long型整数值转换成0-1串：
    public static String longToBits(long lValue) {
        char[] cArray = new char[64];
        long single = 1;
        for (int i=0; i<64; i++) {
            long andResult = lValue & single;
            if (andResult == 0)
                cArray[63-i] = '0';
            else
                cArray[63-i] = '1';
            single = single << 1;
        }
        String bits = new String(cArray);
        return bits;
    }
    //把0-1串转换成long型长整型值：
    public static long bitsToLong(String bits) {
        long lValue = 0;
        long single = 1;
        for (int i=0; i<64; i++) {
            if (bits.charAt(63-i) == '1')
                lValue = lValue | single;
            single = single << 1;
        }
        return lValue;
    }
    //把byte型整数值转换成0-1串：
    public static String byteToBits(byte bValue) {
        int iValue = bValue;
        char[] cArray = new char[8];
        int single = 1;
        for (int i=0; i<8; i++) {
            int andResult = iValue & single;
            if (andResult == 0)
                cArray[7-i] = '0';
            else
                cArray[7-i] = '1';
            single = single << 1;
        }
        String bits = new String(cArray);
        return bits;
    }
    //把0-1串转换成byte型的整数值：
    public static byte bitsToByte(String bits) {
        int iValue = 0;
        int single = 1;
        for (int i=0; i<8; i++) {
            if (bits.charAt(7-i) == '1')
                iValue = iValue | single;
            single = single << 1;
        }
        if (bits.charAt(0) == '1')
            iValue = iValue | 0xFFFFFF00;
        byte b = (byte)iValue;
        return b;
    }
    //把byte型整数值转换成无符号的整数值：
    public static int byteToUnsignedInt(byte bValue) {
        if (bValue >= 0)
            return bValue;
        else
            return 256 + bValue;
    }
    //把int型无符号整数值转换成byte型整数值：
    public static byte unsignedIntToByte(int uiValue) {
        if (uiValue < 128)
            return (byte)uiValue;
        else
            return (byte)(uiValue - 256);
    }
    //把int型整数值转换成4个字节构成的数组：
    public static byte[] intToBytes(int iValue) {
        byte[] bytes = new byte[4];
        int iSingleByte = 0xFF000000;
        for (int i=0; i<4; i++) {
            int iByteValue = iValue & iSingleByte;
            iByteValue = iByteValue >>> ( (3-i) * 8);
            bytes[i] = unsignedIntToByte(iByteValue);
            iSingleByte = iSingleByte >>> 8;
        }
        return bytes;
    }
    //把4个字节保存的整数值复原：
    public static int bytesToInt(byte[] bytes) {
        int iValue = 0;
        for (int i=0; i<4; i++) {
            int iByteValue = byteToUnsignedInt(bytes[i]);
            iByteValue = iByteValue << ( (3-i) * 8 );
            iValue = iValue | iByteValue;
        }
        return iValue;
    }
    //把long型的整数值转换成8个字节构成的数组：
    public static byte[] longToBytes(long lValue) {
        byte[] bytes = new byte[8];
        long lSingleByte = 0XFF00000000000000L;
        for (int i=0; i<8; i++) {
            long lByteValue = lValue & lSingleByte;
            lByteValue = lByteValue >>> ( (7-i) * 8 );
            bytes[i] = unsignedIntToByte((int)lByteValue);
            lSingleByte = lSingleByte >>> 8;
        }
        return bytes;
    }
    //把由8个字节构成的数组复原为它们表示的long型整数值：
    public static long bytesToLong(byte[] bytes) {
        long lValue = 0;
        for (int i=0; i<8; i++) {
            long lByteValue = byteToUnsignedInt(bytes[i]);
            lByteValue = lByteValue << ( (7-i) * 8 );
            lValue = lValue | lByteValue;
        }
        return lValue;
    }
    //把double型数值转换成8个字节构成的数组表示：
    public static byte[] doubleToBytes(double dValue) {
        long lValue = Double.doubleToLongBits(dValue);
        byte[] bytes = longToBytes(lValue);
        return bytes;
    }
    //把8个字节构成的数组复原成它们表示的双精度型数值：
    public static double bytesToDouble(byte[] bytes) {
        long lValue = bytesToLong(bytes);
        double dValue = Double.longBitsToDouble(lValue);
        return dValue;
    }
}
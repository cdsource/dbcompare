package org.jpf.utils;

/***************************
 *字符串 DESede(3DES) 加密
 *
 *	2005-07-13
 *
 ***************************/

import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ThreeDes
{

   private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish
   private static final byte[] keyBytes =
      {
      0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x10, 0x40, 0x38
      , 0x28, 0x25, 0x79, 0x51, (byte) 0xCB, (byte) 0xDD, 0x55, 0x66
      , 0x77, 0x29, 0x74, (byte) 0x98, 0x30, 0x40, 0x36, (byte) 0xE2}; //24字节的密钥

   /**
    * 将byte数组转换为表示16进制值的字符串，
    * 如：byte[]{8,18}转换为：0813，
    * 和public static byte[] hexStr2ByteArr(String strIn)
    * 互为可逆的转换过程
    * @param arrB 需要转换的byte数组
    * @return 转换后的字符串
    * @throws Exception 本方法不处理任何异常，所有异常全部抛出
    */
   public static String byteArr2HexStr(byte[] arrB)
   {
      int iLen = arrB.length;
      //每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
      StringBuffer sb = new StringBuffer(iLen * 2);
      for (int i = 0; i < iLen; i++)
      {
         int intTmp = arrB[i];
         //把负数转换为正数
         while (intTmp < 0)
         {
            intTmp = intTmp + 256;
         }
         //小于0F的数需要在前面补0
         if (intTmp < 16)
         {
            sb.append("0");
         }
         sb.append(Integer.toString(intTmp, 16));
      }
      return sb.toString();
   }

   /**
    * 将表示16进制值的字符串转换为byte数组，
    * 和public static String byteArr2HexStr(byte[] arrB)
    * 互为可逆的转换过程
    * @param strIn 需要转换的字符串
    * @return 转换后的byte数组
    * @throws Exception 本方法不处理任何异常，所有异常全部抛出
    * @author <a href="mailto:zhangji@aspire-tech.com">ZhangJi</a>
    */
   public static byte[] hexStr2ByteArr(String strIn)
   {
      byte[] arrB = strIn.getBytes();
      int iLen = arrB.length;

      //两个字符表示一个字节，所以字节数组长度是字符串长度除以2
      byte[] arrOut = new byte[iLen / 2];
      for (int i = 0; i < iLen; i = i + 2)
      {
         String strTmp = new String(arrB, i, 2);
         arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
      }
      return arrOut;
   }

   //加密
   //keybyte为加密密钥，长度为24字节
   //src为被加密的数据缓冲区（源）
   public static byte[] encryptMode(byte[] src)
   {
      try
      {

         //生成密钥
         SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);

         //加密
         Cipher c1 = Cipher.getInstance(Algorithm);
         c1.init(Cipher.ENCRYPT_MODE, deskey);
         return c1.doFinal(src);
      } catch (java.security.NoSuchAlgorithmException e1)
      {
         e1.printStackTrace();
      } catch (javax.crypto.NoSuchPaddingException e2)
      {
         e2.printStackTrace();
      } catch (java.lang.Exception e3)
      {
         e3.printStackTrace();
      }
      return null;
   }

   //解密
   //keybyte为加密密钥，长度为24字节
   //src为加密后的缓冲区
   public static byte[] decryptMode(byte[] src)
   {
      try
      {

         //生成密钥
         SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);

         //解密
         Cipher c1 = Cipher.getInstance(Algorithm);
         c1.init(Cipher.DECRYPT_MODE, deskey);
         return c1.doFinal(src);
      } catch (java.security.NoSuchAlgorithmException e1)
      {
         e1.printStackTrace();
      } catch (javax.crypto.NoSuchPaddingException e2)
      {
         e2.printStackTrace();
      } catch (java.lang.Exception e3)
      {
         e3.printStackTrace();
      }
      return null;
   }

   //转换成十六进制字符串
   public static String byte2hex(byte[] b)
   {
      String hs = "";
      String stmp = "";

      for (int n = 0; n < b.length; n++)
      {
         stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
         if (stmp.length() == 1)
         {
            hs = hs + "0" + stmp;
         } else
         {
            hs = hs + stmp;
         }
         //if (n<b.length-1)  hs=hs+":";
         //System.out.println(b[n]+"==="+hs);
      }
      return hs.toUpperCase();
   }



   //构造函数
   public ThreeDes()
   {
      Security.addProvider(new com.sun.crypto.provider.SunJCE());
   }

   public static String getS()
   {
      return null;
   }

   public static String DoDecrypt(String str)
   {
      String deStr = str.toLowerCase();
      StringBuffer deStrBuff = new StringBuffer();
      for (int i = 0; i < deStr.length(); i += 2)
      {
         String subStr = deStr.substring(i, i + 2);
         int tmpch = Integer.parseInt(subStr, 16);
         tmpch ^= 1;
         tmpch ^= 0xa;
         deStrBuff.append( (char) tmpch);
      }

      return deStrBuff.toString();
   }

   public static void main(String[] args)
   {
      /*
   	if (args.length == 2)
      {
         if (args[0].equalsIgnoreCase("1"))
         {
            System.out.println(new String(ThreeDes.decryptMode(ThreeDes.
               hexStr2ByteArr(args[1]))));

         }
         if (args[0].equalsIgnoreCase("2"))
         {
            System.out.println(DoDecrypt(args[1]));
         }
      }
      */
      System.out.println(DoDecrypt("js"));
      //Security.addProvider(new com.sun.crypto.provider.SunJCE());
      //System.out.println("key:"+new String(byte2hex(keyBytes)));
      /*
//    	添加新安全算法,如果用JCE就要把它添加进去
                   Security.addProvider(new com.sun.crypto.provider.SunJCE());
                   String Algorithm="DES"; //定义 加密算法,可用 DES,DESede,Blowfish
                   String myinfo="要加密的信息";
                   try {
//    	生成密钥
                   KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
                   SecretKey deskey = keygen.generateKey();



//    	加密
                   System.out.println("加密前的二进串:"+byte2hex(myinfo.getBytes()));
                   System.out.println("加密前的信息:"+myinfo);
                   Cipher c1 = Cipher.getInstance(Algorithm);
                   c1.init(Cipher.ENCRYPT_MODE,deskey);
                   byte[] cipherByte=c1.doFinal(myinfo.getBytes());
                   System.out.println("加密后的二进串:"+byte2hex(cipherByte));
//    	解密
                   c1 = Cipher.getInstance(Algorithm);
                   c1.init(Cipher.DECRYPT_MODE,deskey);
                   byte[] clearByte=c1.doFinal(cipherByte);
                   System.out.println("解密后的二进串:"+byte2hex(clearByte));
                   System.out.println("解密后的信息:"+(new String(clearByte)));

                   }
       catch (java.security.NoSuchAlgorithmException e1) {e1.printStackTrace();}
       catch (javax.crypto.NoSuchPaddingException e2) {e2.printStackTrace();}
                   catch (java.lang.Exception e3) {e3.printStackTrace();}

       */

      //添加新安全算法,如果用JCE就要把它添加进去

      /*
           System.out.println(System.getProperty("user.dir"));

           String szSrc = "123abc";

           byte[] aaaaaa = ThreeDes.encryptMode(szSrc.getBytes());

           System.out.println("加密前的字符串:" + szSrc);

           String ttt = ThreeDes.byteArr2HexStr(aaaaaa);

           System.out.println("加密后的字符串:" + ttt);

           System.out.println("解密后的字符串：" +
       new String(ThreeDes.decryptMode(ThreeDes.hexStr2ByteArr(ttt))));

           System.out.println("=======================");
       */

//        byte[] encoded = ThreeDes.encryptMode( szSrc.getBytes());
//        byte [] encoded = {-1,1,1,1};
//        char [] encchar = {128,1,1,1};
//
//        for(int i =0; i<encchar.length; i++){
//        	System.out.print((int)encchar[i]);
//        	System.out.print("t ");
//        }
//
//        for(int i =0; i<encoded.length; i++){
//        	System.out.print((int)(char)encoded[i]);
//        	System.out.print(" ");
//        }
//        System.out.println("");
//        [-124, 49, -92, 55, 85, 34, -35, -38, -76, 83, 41, -42, -69, 80, -20, -63,
//         -124, 49, -92, 55, 9, -84, -43, 97, -21, -116, -13, -20, -124, 49, -92, 55, 29]
//        [-1, 85, 34, -35, -38, -76, 83, 41, -42, -69, 80, -20, -63, -94, 9, -84, -43,
//         97, -21, -116, -13, -20, -125, 29]


//        String ddd= new String(encoded);
//        System.out.println((int)ddd.charAt(0));
//
//        byte [] oo = ddd.getBytes();
//
//        for(int i=0; i<oo.length; ++i){
//        	System.out.print(oo[i]);
//        	System.out.print(" ");
//        }

//
//        System.out.println(encoded);
//        String end = byte2hex(encoded);
//        System.out.println(end);
//        System.out.println(string2byte(end));
//
//        System.out.println(encoded + "---------------" + ddd);
//
//        System.out.println(encoded + " " + new String(encoded).getBytes());
//
//        System.out.println("加密后的字符串:" +ddd );
//        System.out.println(encoded);
//        System.out.println(new String(encoded));
//        System.out.println(ddd.getBytes());
//        System.out.println(byte2hex(encoded).getBytes());
      //byte [] enc = ttt.getBytes();
      //byte[] srcBytes = ThreeDes.decryptMode(enc);
      // System.out.println("解密后的字符串:" + (new String(srcBytes)));

   }

}

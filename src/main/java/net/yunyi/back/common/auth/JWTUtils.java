package net.yunyi.back.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.yunyi.back.common.BizException;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.HashMap;

/**
 * @author 19028
 * @date 2019/12/21 22:55
 */

public class JWTUtils {

	private static RSAPrivateKey priKey;
	private static RSAPublicKey pubKey;

	static {
		getInstance();
	}

	public synchronized static JWTUtils getInstance(String modulus, String privateExponent, String publicExponent) {
		if (priKey == null && pubKey == null) {
			priKey = RSAUtils.getPrivateKey(modulus, privateExponent);
			pubKey = RSAUtils.getPublicKey(modulus, publicExponent);
		}
		return SingletonHolder.INSTANCE;
	}

	public synchronized static void reload(String modulus, String privateExponent, String publicExponent) {
		priKey = RSAUtils.getPrivateKey(modulus, privateExponent);
		pubKey = RSAUtils.getPublicKey(modulus, publicExponent);
	}

	public synchronized static JWTUtils getInstance() {
		if (priKey == null && pubKey == null) {
			priKey = RSAUtils.getPrivateKey(RSAUtils.modulus, RSAUtils.private_exponent);
			pubKey = RSAUtils.getPublicKey(RSAUtils.modulus, RSAUtils.public_exponent);
		}
		return SingletonHolder.INSTANCE;
	}

	/**
	 * 获取Token
	 *
	 * @param uid 用户ID
	 * @param exp 失效时间，单位分钟
	 * @return
	 */
	public static String getToken(String uid, int exp) {
		long endTime = System.currentTimeMillis() + 1000 * 60 * exp;
		return Jwts.builder().setSubject(uid).setExpiration(new Date(endTime)).signWith(SignatureAlgorithm.RS512, priKey).compact();
	}

	/**
	 * 获取Token
	 *
	 * @param uid 用户ID
	 * @return
	 */
	public static String getToken(long uid) {
		String uidStr = String.valueOf(uid);
		long endTime = System.currentTimeMillis() + 1000 * 60 * 1440 * 7;
		return Jwts.builder().setSubject(uidStr).setExpiration(new Date(endTime)).signWith(SignatureAlgorithm.RS512, priKey).compact();
	}

	/**
	 * 检查Token是否合法
	 *
	 * @param token
	 * @return JWTResult
	 */
	public long checkToken(String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token).getBody();
			String sub = claims.get("sub", String.class);
			return Long.parseLong(sub);
		} catch (ExpiredJwtException e) {
			// 在解析JWT字符串时，如果‘过期时间字段’已经早于当前时间，将会抛出ExpiredJwtException异常，说明本次请求已经失效
			throw new BizException(401, "token已过期", e);
		} catch (Exception e) {
			// 在解析JWT字符串时，如果密钥不正确，将会解析失败，抛出SignatureException异常，说明该JWT字符串是伪造的
			throw new BizException(401, "非法请求", e);
		}
	}

	private static class SingletonHolder {

		private static final JWTUtils INSTANCE = new JWTUtils();
	}

	public static class RSAUtils {

		public static String modulus = "120749774428185480467622030722535804071445078993623309709775427878906293937338059423076695854937532244466465395164541641368876529295825453848760144835049363522545908104302024165873971414491110512342791594610742544380402908598585190494003507524195754273822268813447403290817343077571516216147839402414940310061";

		public static String public_exponent = "65537";

		public static String private_exponent = "73923469942286919561803730971048133587965873619209827001168953680477872428610977313161774128992838682156392947263251899461404460204267953359475632559803617319478756560848229397545070273747796303141458040475889804016062973476403760709402857872540300632704514872361476749953797952016730690123983122643596231873";

		/**
		 * 公钥加密
		 *
		 * @param data
		 * @return
		 * @throws Exception
		 */
		public static String encryptByPublicKey(String data) throws Exception {
			RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, public_exponent);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 模长
			int key_len = publicKey.getModulus().bitLength() / 8;
			// 加密数据长度 <= 模长-11
			String[] datas = splitString(data, key_len - 11);
			String mi = "";
			// 如果明文长度大于模长-11则要分组加密
			for (String s : datas) {
				mi += bcd2Str(cipher.doFinal(s.getBytes()));
			}
			return mi;
		}

		/**
		 * 私钥解密
		 *
		 * @param data
		 * @return
		 * @throws Exception
		 */
		public static String decryptByPrivateKey(String data) throws Exception {
			RSAPrivateKey privateKey = RSAUtils.getPrivateKey(modulus, private_exponent);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// 模长
			int key_len = privateKey.getModulus().bitLength() / 8;
			byte[] bytes = data.getBytes();
			byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
			// 如果密文长度大于模长则要分组解密
			String ming = "";
			byte[][] arrays = splitArray(bcd, key_len);
			for (byte[] arr : arrays) {
				ming += new String(cipher.doFinal(arr));
			}
			return ming;
		}

		/**
		 * 生成公钥和私钥
		 *
		 * @throws NoSuchAlgorithmException
		 */
		public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException {
			HashMap<String, Object> map = new HashMap<String, Object>();
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(1024);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			map.put("public", publicKey);
			map.put("private", privateKey);
			return map;
		}

		/**
		 * 使用模和指数生成RSA公钥 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
		 * /None/NoPadding】
		 *
		 * @param modulus  模
		 * @param exponent 指数
		 * @return
		 */
		public static RSAPublicKey getPublicKey(String modulus, String exponent) {
			try {
				BigInteger b1 = new BigInteger(modulus);
				BigInteger b2 = new BigInteger(exponent);
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
				return (RSAPublicKey) keyFactory.generatePublic(keySpec);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 使用模和指数生成RSA私钥 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
		 * /None/NoPadding】
		 *
		 * @param modulus  模
		 * @param exponent 指数
		 * @return
		 */
		public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
			try {
				BigInteger b1 = new BigInteger(modulus);
				BigInteger b2 = new BigInteger(exponent);
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
				return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 公钥加密
		 *
		 * @param data
		 * @param publicKey
		 * @return
		 * @throws Exception
		 */
		public static String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 模长
			int key_len = publicKey.getModulus().bitLength() / 8;
			// 加密数据长度 <= 模长-11
			String[] datas = splitString(data, key_len - 11);
			String mi = "";
			// 如果明文长度大于模长-11则要分组加密
			for (String s : datas) {
				mi += bcd2Str(cipher.doFinal(s.getBytes()));
			}
			return mi;
		}

		/**
		 * 私钥解密
		 *
		 * @param data
		 * @param privateKey
		 * @return
		 * @throws Exception
		 */
		public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// 模长
			int key_len = privateKey.getModulus().bitLength() / 8;
			byte[] bytes = data.getBytes();
			byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
			// 如果密文长度大于模长则要分组解密
			String ming = "";
			byte[][] arrays = splitArray(bcd, key_len);
			for (byte[] arr : arrays) {
				ming += new String(cipher.doFinal(arr));
			}
			return ming;
		}

		/**
		 * ASCII码转BCD码
		 */
		public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
			byte[] bcd = new byte[asc_len / 2];
			int j = 0;
			for (int i = 0; i < (asc_len + 1) / 2; i++) {
				bcd[i] = asc_to_bcd(ascii[j++]);
				bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
			}
			return bcd;
		}

		public static byte asc_to_bcd(byte asc) {
			byte bcd;

			if ((asc >= '0') && (asc <= '9')) {
				bcd = (byte) (asc - '0');
			} else if ((asc >= 'A') && (asc <= 'F')) {
				bcd = (byte) (asc - 'A' + 10);
			} else if ((asc >= 'a') && (asc <= 'f')) {
				bcd = (byte) (asc - 'a' + 10);
			} else {
				bcd = (byte) (asc - 48);
			}
			return bcd;
		}

		/**
		 * BCD转字符串
		 */
		public static String bcd2Str(byte[] bytes) {
			char temp[] = new char[bytes.length * 2], val;

			for (int i = 0; i < bytes.length; i++) {
				val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
				temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

				val = (char) (bytes[i] & 0x0f);
				temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
			}
			return new String(temp);
		}

		/**
		 * 拆分字符串
		 */
		public static String[] splitString(String string, int len) {
			int x = string.length() / len;
			int y = string.length() % len;
			int z = 0;
			if (y != 0) {
				z = 1;
			}
			String[] strings = new String[x + z];
			String str = "";
			for (int i = 0; i < x + z; i++) {
				if (i == x + z - 1 && y != 0) {
					str = string.substring(i * len, i * len + y);
				} else {
					str = string.substring(i * len, i * len + len);
				}
				strings[i] = str;
			}
			return strings;
		}

		/**
		 * 拆分数组
		 */
		public static byte[][] splitArray(byte[] data, int len) {
			int x = data.length / len;
			int y = data.length % len;
			int z = 0;
			if (y != 0) {
				z = 1;
			}
			byte[][] arrays = new byte[x + z][];
			byte[] arr;
			for (int i = 0; i < x + z; i++) {
				arr = new byte[len];
				if (i == x + z - 1 && y != 0) {
					System.arraycopy(data, i * len, arr, 0, y);
				} else {
					System.arraycopy(data, i * len, arr, 0, len);
				}
				arrays[i] = arr;
			}
			return arrays;
		}
	}
}

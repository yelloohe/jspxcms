package com.jspxcms.common.util;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.StringUtils;

public abstract class IpUtils {
	public static byte[] ip2Bytes(String ip) {
		// Make a first pass to categorize the characters in this string.
		boolean hasColon = false;
		boolean hasDot = false;
		for (int i = 0; i < ip.length(); i++) {
			char c = ip.charAt(i);
			if (c == '.') {
				hasDot = true;
			} else if (c == ':') {
				if (hasDot) {
					return null; // Colons must not appear after dots.
				}
				hasColon = true;
			} else if (Character.digit(c, 16) == -1) {
				return null; // Everything else must be a decimal or hex digit.
			}
		}

		// Now decide which address family to parse.
		if (hasColon) {
			if (hasDot) {
				ip = convertDottedQuadToHex(ip);
				if (ip == null) {
					return null;
				}
			}
			return textToNumericFormatV6(ip);
		} else if (hasDot) {
			return textToNumericFormatV4(ip);
		}
		return null;
	}

	private static String convertDottedQuadToHex(String ipString) {
		int lastColon = ipString.lastIndexOf(':');
		String initialPart = ipString.substring(0, lastColon + 1);
		String dottedQuad = ipString.substring(lastColon + 1);
		byte[] quad = textToNumericFormatV4(dottedQuad);
		if (quad == null) {
			return null;
		}
		String penultimate = Integer.toHexString(((quad[0] & 0xff) << 8)
				| (quad[1] & 0xff));
		String ultimate = Integer.toHexString(((quad[2] & 0xff) << 8)
				| (quad[3] & 0xff));
		return initialPart + penultimate + ":" + ultimate;
	}

	private static byte[] textToNumericFormatV4(String ipString) {
		String[] address = ipString.split("\\.", IPV4_PART_COUNT + 1);
		if (address.length != IPV4_PART_COUNT) {
			return null;
		}

		byte[] bytes = new byte[IPV4_PART_COUNT];
		try {
			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = parseOctet(address[i]);
			}
		} catch (NumberFormatException ex) {
			return null;
		}

		return bytes;
	}

	private static byte[] textToNumericFormatV6(String ipString) {
		String[] parts = ipString.split(":", IPV6_PART_COUNT + 2);
		if (parts.length < 3 || parts.length > IPV6_PART_COUNT + 1) {
			return null;
		}

		int skipIndex = -1;
		for (int i = 1; i < parts.length - 1; i++) {
			if (parts[i].length() == 0) {
				if (skipIndex >= 0) {
					return null;
				}
				skipIndex = i;
			}
		}

		int partsHi;
		int partsLo;
		if (skipIndex >= 0) {
			partsHi = skipIndex;
			partsLo = parts.length - skipIndex - 1;
			if (parts[0].length() == 0 && --partsHi != 0) {
				return null;
			}
			if (parts[parts.length - 1].length() == 0 && --partsLo != 0) {
				return null;
			}
		} else {
			partsHi = parts.length;
			partsLo = 0;
		}

		int partsSkipped = IPV6_PART_COUNT - (partsHi + partsLo);
		if (!(skipIndex >= 0 ? partsSkipped >= 1 : partsSkipped == 0)) {
			return null;
		}

		ByteBuffer rawBytes = ByteBuffer.allocate(2 * IPV6_PART_COUNT);
		try {
			for (int i = 0; i < partsHi; i++) {
				rawBytes.putShort(parseHextet(parts[i]));
			}
			for (int i = 0; i < partsSkipped; i++) {
				rawBytes.putShort((short) 0);
			}
			for (int i = partsLo; i > 0; i--) {
				rawBytes.putShort(parseHextet(parts[parts.length - i]));
			}
		} catch (NumberFormatException ex) {
			return null;
		}
		return rawBytes.array();
	}

	private static short parseHextet(String ipPart) {
		int hextet = Integer.parseInt(ipPart, 16);
		if (hextet > 0xffff) {
			throw new NumberFormatException();
		}
		return (short) hextet;
	}

	private static byte parseOctet(String ipPart) {
		int octet = Integer.parseInt(ipPart);
		if (octet > 255 || (ipPart.startsWith("0") && ipPart.length() > 1)) {
			throw new NumberFormatException();
		}
		return (byte) octet;
	}

	private static final int IPV4_PART_COUNT = 4;
	private static final int IPV6_PART_COUNT = 8;

	public static int bytesCompare(byte[] bytes1, byte[] bytes2) {
		if (bytes1 == bytes2) {
			return 0;
		}
		int len1 = bytes1.length;
		int len2 = bytes2.length;
		int len = len1 < len2 ? len1 : len2;
		for (int i = 0; i < len; i++) {
			int a = (bytes1[i] & 0xff);
			int b = (bytes2[i] & 0xff);
			if (a != b) {
				return a - b;
			}
		}
		return 0;
	}

	public static boolean inRange(String range, String ip) {
		if (StringUtils.isBlank(range)) {
			return true;
		}
		byte[] ipBytes = ip2Bytes(ip);
		if (ipBytes == null) {
			return false;
		}
		range = StringUtils.remove(range, ' ');
		range = StringUtils.remove(range, '\r');
		int index;
		byte[] lineBytes, beginBytes, endBytes;
		for (String line : StringUtils.split(range, '\n')) {
			if (StringUtils.isNotBlank(line)) {
				index = line.indexOf('-');
				if (index != -1) {
					beginBytes = ip2Bytes(line.substring(0, index));
					endBytes = ip2Bytes(line.substring(index + 1));
					if (beginBytes == null || endBytes == null
							|| beginBytes.length != ipBytes.length
							|| endBytes.length != ipBytes.length) {
						continue;
					}
					if (bytesCompare(beginBytes, ipBytes) <= 0
							&& bytesCompare(endBytes, ipBytes) >= 0) {
						return true;
					}
				} else {
					lineBytes = ip2Bytes(line);
					if (lineBytes == null || lineBytes.length != ipBytes.length) {
						continue;
					}
					if (bytesCompare(lineBytes, ipBytes) == 0) {
						return true;
					}
				}
			}
		}
		return false;
	}
}

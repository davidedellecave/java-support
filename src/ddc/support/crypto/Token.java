package ddc.support.crypto;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import ddc.support.util.Timespan;

public class Token {
	public static Timespan validity = new Timespan(5, TimeUnit.DAYS);
	
	public static String encodeToken(Crypto crypto, String... toks) throws NoSuchAlgorithmException, TokenException {
		String token = "";
		for (String tok : toks) {
			token += tok.trim() + " ";
		}
		token += System.currentTimeMillis();
		CryptoResult result = crypto.encryptBase32(token);
		if (result.isFailed())
			throw new TokenException("Token - Cannot encode token - " + result.getException());
		return result.data;
	}

	public static String[] decodeToken(Crypto crypto, String token) throws NoSuchAlgorithmException, TokenException {
		CryptoResult result = crypto.decryptBase32(token);
		if (result.isFailed())
			throw new TokenException("Token - Cannot decode token - " + result.getException() + " token:[" + token + "]");
//		System.out.println(result.data);
		String[] toks = result.data.split(" ");
		long millis = Long.valueOf(toks[toks.length-1]);		  
//		DateTime dt = new DateTime(millis);
//		ZonedDateTime dt = toZonedDateTime(millis);
		Instant i = Instant.ofEpochMilli(millis);
		if (i.plus(validity.getMillis(), ChronoUnit.MILLIS).isBefore(Instant.now()))		
			throw new TokenException("Token - token expired");		
		return toks;
	}
}

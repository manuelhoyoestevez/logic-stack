package lotto.proxy.auth;

import java.security.PublicKey;
import java.util.Map;

public interface JWTDecoderInterface {
	public PublicKey getPublicKey() ;
	public Map<String, Object> decode (String jwt);
}

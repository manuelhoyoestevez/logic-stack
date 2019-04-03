package mhe.proxy.auth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import io.jsonwebtoken.Jwts;

public class JWTDecoder implements JWTDecoderInterface {
	private PublicKey publicKey = null;

	public JWTDecoder(String publicKeyString) throws IOException, CertificateException {
		InputStream fin = new ByteArrayInputStream(publicKeyString.getBytes("UTF-8"));
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		X509Certificate certificateX509 = (X509Certificate) certificateFactory.generateCertificate(fin);
		this.publicKey = certificateX509.getPublicKey();
		fin.close();
	}
	
	public PublicKey getPublicKey() {
		return this.publicKey;
	}
	
	public Map<String, Object> decode (String jwt){
		return Jwts.parser().setSigningKey(this.publicKey).parseClaimsJws(jwt).getBody();
	}
}

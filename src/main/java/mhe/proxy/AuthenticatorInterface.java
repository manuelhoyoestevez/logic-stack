package mhe.proxy;

import io.vertx.core.Future;
import mhe.logger.LoggerInterface;
import mhe.model.AuthenticatedUserInterface;

public interface AuthenticatorInterface {
	public Future<String> getToken(String tokenId, LoggerInterface requestFactory);
	public Future<AuthenticatedUserInterface> decodeToken(String jwt, LoggerInterface requestFactory);
	public Future<AuthenticatedUserInterface> getDecodedToken(String tokenId, LoggerInterface requestFactory);
}
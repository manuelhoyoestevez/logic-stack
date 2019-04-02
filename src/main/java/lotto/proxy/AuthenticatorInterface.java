package lotto.proxy;

import io.vertx.core.Future;
import lotto.logger.LoggerInterface;
import lotto.model.AuthenticatedUserInterface;

public interface AuthenticatorInterface {
	public Future<String> getToken(String tokenId, LoggerInterface requestFactory);
	public Future<AuthenticatedUserInterface> decodeToken(String jwt, LoggerInterface requestFactory);
	public Future<AuthenticatedUserInterface> getDecodedToken(String tokenId, LoggerInterface requestFactory);
}
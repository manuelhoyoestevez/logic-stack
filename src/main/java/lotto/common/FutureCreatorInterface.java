package lotto.common;

import java.util.List;
import io.vertx.core.Future;

public interface FutureCreatorInterface {
	public Future<? extends Object> createFuture(List<Object> data);
}

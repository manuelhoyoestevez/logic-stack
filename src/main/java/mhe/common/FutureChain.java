package mhe.common;

import java.util.List;
import java.util.ArrayList;
import io.vertx.core.Future;

public class FutureChain extends ArrayList<FutureCreatorInterface> {

	private static final long serialVersionUID = 7051935959293830720L;

	public boolean addFuture(Future<? extends Object> future) {
		return this.add(data -> future);
	}
	
	public Future<List<? extends Object>> composite() {
		return composite(this);
	}
	
	public Future<? extends Object> coalesce() {
		return coalesce(this);
	}

	public static Future<List<? extends Object>> composite(List<FutureCreatorInterface> futures) {
		return compositeRecursive(futures, new ArrayList<Object>(), 0);
	}

	private static Future<List<? extends Object>> compositeRecursive(List<FutureCreatorInterface> futures, List<Object> results, int i) {
		Future<List<? extends Object>> ret = Future.future();
		try {
			if(i > futures.size()) {
				ret.fail(new MheException(500, "TOO_LONG_INDEX", "Too long index: " + i + ", when there is only " + futures.size() + " futures"));
			}
			else if(i == futures.size()) {
				ret.complete(results);
			}
			else {
				futures.get(i)
				.createFuture(results)
				.setHandler(oneRes -> {
					if(oneRes.failed()) {
						ret.fail(oneRes.cause());
					}
					else {
						results.add(oneRes.result());
						compositeRecursive(futures, results, i + 1).setHandler(ret.completer());
					}
				});
			}
		}
		catch(Throwable ex) {
			ret.fail(ex);
		}

		return ret;
	}
	
	
	
	/**
	 * Va ejecutando la lista de futures hasta que uno devuelve un resultado no nulo
	 * @param futures Lista de futures
	 * @return Primer resultado distinto de null, o null si no existe ninguno
	 */
	public static Future<? extends Object> coalesce(List<FutureCreatorInterface> futures) {
		return coalesceRecursive(futures, 0);
	}
	
	private static Future<Object> coalesceRecursive(List<FutureCreatorInterface> futures, int i) {
		Future<Object> ret = Future.future();
		try {
			if(i > futures.size()) {
				ret.fail(new MheException(500, "TOO_LONG_INDEX", "Too long index: " + i + ", when there is only " + futures.size() + " futures"));
			}
			else if(i == futures.size()) {
				ret.complete(null);
			}
			else {
				futures.get(i)
				.createFuture(null)
				.setHandler(oneRes -> {
					if(oneRes.failed()) {
						ret.fail(oneRes.cause());
					}
					else if(oneRes.result() == null){
						coalesceRecursive(futures, i + 1).setHandler(ret.completer());
					}
					else {
						ret.complete(oneRes.result());
					}
				});
			}
		}
		catch(Throwable ex) {
			ret.fail(ex);
		}
		return ret;
	}
}

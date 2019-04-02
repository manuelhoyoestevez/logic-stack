package lotto.consul;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import lotto.common.FutureChain;
import lotto.common.ParametersSetInterface;
import lotto.logger.LoggerInterface;

public abstract class ConsulConfigManagerHelper {
	/**
	 * 
	 * @param paramObject
	 * @param serviceCatalog
	 * @return
	 */
	public static Future<? extends Object> getConfigParameterFromJSON(JsonObject paramObject, ParametersSetInterface parametersSource, LoggerInterface loggerFactory) {
		FutureChain futures = new FutureChain();
		Map<String, String> env = System.getenv();
		LoggerInterface logger = loggerFactory.createLocalLogger("ConsulConfigManagerHelper", "getConfigParameterFromJSON", "none");
		
		logger.log(Level.INFO, "Getting parameter value form JSON: " + paramObject);
		
		for(String type : paramObject.fieldNames()) {
			Object value = paramObject.getValue(type);
			
			logger.log(Level.INFO, "type = " + type + ", value = " + value + ": ");
			
			if(value != null) {
				switch(type) {
					case "def":
						logger.log(Level.INFO, "Checking...");
						futures.add(d -> Future.succeededFuture(value));
						break;
						
					case "env":
						logger.log(Level.INFO, "Checking...");
						futures.add(d -> Future.succeededFuture(env.get(value)));
						break;
						
					case "key":
						if(parametersSource != null) {
							logger.log(Level.INFO, "Checking...");
							futures.add(d -> parametersSource.getParameterValueByKey(value.toString(), null, loggerFactory));
						}
						else {
							logger.log(Level.INFO, "Without service catalog!");
						}
						break;
					default:
						logger.log(Level.WARNING, "Unknown type!");
				}
			}
		}
		
		logger.log(Level.INFO, "Getting parameters futures set!");

		return futures.coalesce();
	}
	
	public static Future<Map<String, ? extends Object>> getConfigParametersFromJsonObject(JsonObject parametersJson, ParametersSetInterface parametersSource, LoggerInterface loggerFactory) {
		Future<Map<String, ? extends Object>> fut = Future.future();
		LoggerInterface logger = loggerFactory.createLocalLogger("ConsulConfigManagerHelper", "getConfigParametersFromJsonObject", "none");
		
		logger.log(Level.INFO, "Getting parameters values form JSON: " + parametersJson);
		
		try {
			int i = 0;
			Map<String, Integer> index = new HashMap<String, Integer>();
			
			FutureChain futures = new FutureChain();
			
			for(String field : parametersJson.fieldNames()) {
				logger.log(Level.INFO, "Parameter " + field + " (" + i + ")");
				JsonObject parameter = parametersJson.getJsonObject(field);
				futures.addFuture(getConfigParameterFromJSON(parameter, parametersSource, loggerFactory));
				index.put(field, i++);
			}
			
			logger.log(Level.INFO, "" + i + " future parameters!");
			
			futures.composite().setHandler(res -> {
				try {
					if(res.failed()) {
						fut.fail(res.cause());
					}
					else {
						List<? extends Object> result = res.result();
						Map<String, Object> ret = new HashMap<String, Object>();
						
						logger.log(Level.INFO, result.size() + " parameters!");
						
						for(String key : index.keySet()) {
							int j = index.get(key);
							Object value = result.get(j);
							logger.log(Level.INFO, "Parameter " + key + " (" + j + "): " + value);
							ret.put(key, value);
						}

						fut.complete(ret);
					}
				}
				catch(Throwable ex) {
					fut.fail(ex);
				}
			});
		}
		catch(Throwable ex) {
			fut.fail(ex);
		}
		return fut;
	}
	
	public static Future<ConsulServiceCatalog> getConsulServiceCatalog(JsonObject consulParams, JsonObject services, LoggerInterface loggerFactory) {
		Future<ConsulServiceCatalog> fut = Future.future();
		LoggerInterface logger = loggerFactory.createLocalLogger("ConsulConfigManagerHelper", "getConsulServiceCatalog");

		logger.log(Level.INFO, "Getting consul values from JSON: " + consulParams);

		try {
			if(consulParams == null) {
				logger.log(Level.SEVERE, "Missing JSON!");
				fut.complete(null);
			}
			else if(!consulParams.containsKey("host")){
				logger.log(Level.SEVERE, "Missing host!");
				fut.complete(null);
			}
			else if(!consulParams.containsKey("port")){
				logger.log(Level.SEVERE, "Missing port!");
				fut.complete(null);
			}
			else {
				FutureChain futures = new FutureChain();
				futures.addFuture(getConfigParameterFromJSON(consulParams.getJsonObject("host"), null, loggerFactory));
				futures.addFuture(getConfigParameterFromJSON(consulParams.getJsonObject("port"), null, loggerFactory));
				
				futures.composite().setHandler(res -> {
					try {
						if(res.failed()) {
							fut.fail(res.cause());
						}
						else {
							List<? extends Object> result = res.result();
							Object host = result.get(0);
							Object port = result.get(1);
							
							logger.log(Level.INFO, "host = " + host + ", port = " + port);
							
							if(host != null && port != null) {
								ConsulServiceCatalog ret = new ConsulServiceCatalog(host.toString(), Integer.parseInt(port.toString()));
								
								for(String serviceId : services.fieldNames()) {
									ret.putExtraInfo(new ConsulServiceExtraInfo().fromJsonObject(serviceId, services.getJsonObject(serviceId)));
								}
								
								fut.complete(ret);
							}
							else {
								fut.complete(null);
							}
						}
					}
					catch(com.ecwid.consul.transport.TransportException ex) {
						logger.log(Level.SEVERE, "Consul unreacheable: " + ex.toString());
						fut.complete(null);
					}
					catch(Throwable ex) {
						fut.fail(ex);
					}
				});
			}
		}
		catch(Throwable ex) {
			fut.fail(ex);
		}
		return fut;
	}
}

package lotto.proxy.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lotto.common.FutureChain;
import lotto.common.LottoException;
import lotto.consul.ConsulConfigManagerHelper;
import lotto.consul.ConsulServiceCatalog;
import lotto.logger.LoggerInterface;
import lotto.proxy.AuthenticatorInterface;
import lotto.proxy.AuthorizatorInterface;
import lotto.proxy.ConfigManagerInterface;
import lotto.proxy.ConfigParamsInterface;
import lotto.proxy.auth.Authenticator;
import lotto.proxy.auth.Authorizator;
import lotto.proxy.auth.JWTDecoder;
import lotto.proxy.auth.JWTDecoderInterface;
import lotto.proxy.impl.ConfigParams;

public class ConfigManager implements ConfigManagerInterface {
	private Vertx vertx;
	private JsonObject config;
	LoggerInterface loggerFactory;

	public ConfigManager(Vertx vertx, LoggerInterface loggerFactory) {
		super();
		this.vertx = vertx;
		this.loggerFactory = loggerFactory;
	}
	
	@Override
	public String toString() {
		return "config: " + config;
	}

	@Override
	public Future<ConfigParamsInterface> getConfigParams(List<String> args) {
		// Se van a ejecutar 4 futures secuencialmente
		// Se devuelve el último future
		
		Future<ConfigParamsInterface> ret = Future.future();
		
		FutureChain futures = new FutureChain();
		
		LoggerInterface logger = loggerFactory.createLocalLogger("ConfigManager", "getConfigParams");
		
		// 0: config
		futures.add(results -> {
			Future<JsonObject> fut = Future.future();
			try {
				vertx.fileSystem().readFile("config.json", configResult -> {
					try {
						config = configResult.result().toJsonObject();
						
						if (configResult.failed()) {
							ret.fail(configResult.cause());
						}
						else if(config == null) {
							ret.fail(new Exception("File config.json not found"));
						}
						else {
							logger.log(Level.INFO, "Config file content: " + config.toString());
							fut.complete(config);
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
		});
	
		// 1: catalogService
		futures.add(results -> {
			JsonObject consul = config.getJsonObject("consul", null);
			JsonObject services = config.getJsonObject("services", null);
			logger.log(Level.INFO, "consul: " + consul);
			logger.log(Level.INFO, "services: " + services);
			return ConsulConfigManagerHelper.getConsulServiceCatalog(consul, services, this.loggerFactory);
		});
		
		// 2: paramsMap
		futures.add(results -> {
			JsonObject parameters = config.getJsonObject("parameters", null);
			logger.log(Level.INFO, "parameters: " + parameters);
			ConsulServiceCatalog catalogService = (ConsulServiceCatalog) results.get(1);
			return ConsulConfigManagerHelper.getConfigParametersFromJsonObject(parameters, catalogService, this.loggerFactory);
		});
		
		// 3: Main future
		futures.add(results -> {
			try {
				ConsulServiceCatalog catalogService = (ConsulServiceCatalog) results.get(1);
				logger.log(Level.INFO, "Config got!");
				
				@SuppressWarnings("unchecked")
				Map<String, ? extends Object> paramsMap = (Map<String, ? extends Object>) results.get(2);
				
				List<String> requiredParameters = new ArrayList<String>();
				requiredParameters.add("listenPort");
				requiredParameters.add("timeout");
				requiredParameters.add("serviceListColor");
				requiredParameters.add("authServiceId");
				requiredParameters.add("dispatchMode");
				requiredParameters.add("chunk");
				requiredParameters.add("publicKey");
				requiredParameters.add("reportPermissionUse");
				
				for(String requiredParameterKey : requiredParameters){
					Object requiredParameterValue = paramsMap.get(requiredParameterKey);
					
					if(requiredParameterValue == null) {
						throw new LottoException(500, "MISSING_PARAMETER", "Missing parameter '" + requiredParameterKey + "'");
					}
					else {
						logger.log(Level.INFO, requiredParameterKey + ": " + requiredParameterValue);
					}
				}

				catalogService.setListColor(!paramsMap.get("serviceListColor").toString().equalsIgnoreCase("black"));

				JWTDecoderInterface jwtDecoder = new JWTDecoder(paramsMap.get("publicKey").toString());
				AuthorizatorInterface authorizator  = new Authorizator(vertx, catalogService, paramsMap.get("authServiceId").toString(), Boolean.parseBoolean(paramsMap.get("reportPermissionUse").toString()));
				AuthenticatorInterface authenticator = new Authenticator(vertx, catalogService, jwtDecoder, paramsMap.get("authServiceId").toString());
				HashMap<String, String> loggerArgs = new HashMap<String, String>();
				
				loggerArgs.put("service-name", "Proxy");
				
				logger.log(Level.INFO, "Completed!");
				
				ret.complete(
					new ConfigParams(
						Integer.parseInt(paramsMap.get("listenPort").toString()),
						Integer.parseInt(paramsMap.get("timeout").toString()),
						new Firewall(authenticator, authorizator), 
						new CustomDispatcher(vertx, catalogService, paramsMap.get("dispatchMode").toString(), Boolean.parseBoolean(paramsMap.get("chunk").toString())),
						catalogService,
						loggerFactory.createLocalLogger(loggerArgs)
					)
				);
			}
			catch(Throwable ex) {
				ret.fail(ex);
			}
			return ret;
		});
		
		futures.composite();
		
		return ret;
	}
}

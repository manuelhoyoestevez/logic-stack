package mhe.consul;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import io.vertx.core.Future;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import mhe.logger.LoggerInterface;
import mhe.common.MheException;
import mhe.common.ParametersSetInterface;
import mhe.common.ServiceCatalogInterface;
import mhe.common.ServiceExtraInfoInterface;
import mhe.common.ServiceParamsInterface;

public class ConsulServiceCatalog implements ServiceCatalogInterface, ParametersSetInterface {
	private int i = 0;
	private QueryParams queryParams;
	private ConsulClient consulClient;
	private Boolean listColor = true;
	private Map<String, ServiceExtraInfoInterface> extraInfo;

	public ConsulServiceCatalog(String consulHost, Integer consulPort) {
		this.queryParams = new QueryParams("");
		this.consulClient = new ConsulClient(consulHost, consulPort);
		this.extraInfo = new HashMap<String, ServiceExtraInfoInterface>();
	}

	public ConsulServiceCatalog putExtraInfo(ServiceExtraInfoInterface extra) {
		this.extraInfo.put(extra.getServiceId(), extra);
		return this;
	}

	public ServiceExtraInfoInterface getExtraInfo(String serviceId) {
		ServiceExtraInfoInterface extra = this.extraInfo.get(serviceId);
		
		if(extra == null) {
			extra = new ConsulServiceExtraInfo().setValues(serviceId, "", !this.listColor, null);
			this.putExtraInfo(extra);
		}
		
		return extra;
	}

	@Override
	public void setListColor(Boolean listColor) {
		this.listColor = listColor;
	}
	
	@Override
	public Future<ServiceParamsInterface> getNextServiceParams(String serviceId, LoggerInterface requestFactory) {
		Future<ServiceParamsInterface> ret = Future.future();
		LoggerInterface logger = requestFactory.createLocalLogger("ConsulServiceCatalog", "getNextServiceParams");
		try {
			logger.log(Level.INFO, "Getting instances of service with ID '" + serviceId + "'");
			Response<List<CatalogService>> serviceResponse = this.consulClient.getCatalogService(serviceId, this.queryParams);
			
			if(serviceResponse == null){
				ret.fail(new MheException(500, "SERVICE_NOT_FOUND", "Service with ID '" + serviceId + "' not found"));
			}
			else {
				List<CatalogService> serviceList = serviceResponse.getValue();
				
				if(serviceList.isEmpty()) {
					ret.fail(new MheException(500, "SERVICE_NOT_INSTACIATED", "Service with ID '" + serviceId + "' not instanciated"));
				}
				else {
					int n = i++ % serviceList.size();
					logger.log(Level.INFO, "Service with ID '" + serviceId + "' has " + serviceList.size() + " instances! Getting instance " + (n + 1) + "...");
					ret.complete(new ConsulServiceParams(serviceList.get(n), this.getExtraInfo(serviceId)));
				}
			}
		}
		catch(Throwable ex){
			ret.fail(ex);
		}
		return ret;
	}

	@Override
	public Future<String> getParameterValueByKey(String key, LoggerInterface requestFactory) {
		return this.getParameterValueByKey(key, null, requestFactory);
	}

	@Override
	public Future<String> getParameterValueByKey(String key, String def, LoggerInterface requestFactory) {
		Future<String> ret = Future.future();
		LoggerInterface logger = requestFactory.createLocalLogger("ConsulServiceCatalog", "getParameterValueByKey", "none");
		
		try {
			logger.log(Level.INFO, "key = " + key + ", def = " + def);
			
			GetValue resValue;
			Response<GetValue> res = this.consulClient.getKVValue(key);
			
			if(res == null || (resValue = res.getValue()) == null) {
				logger.log(Level.INFO, "key = " + key + ": not found! returning " + def);
				ret.complete(def);
			}
			else {
				String result = new String(Base64.getDecoder().decode(resValue.getValue()));
				logger.log(Level.INFO, "key = " + key + ": found! returning " + result);
				ret.complete(result);
			}
			//ret.fail(new ProxyException(404, "OBJECT_NOT_FOUND", "Value with key '" + key + "' not found"));
		}
		catch(com.ecwid.consul.transport.TransportException ex) {
			logger.log(Level.SEVERE, "Cant connect: " + ex.toString());
			ret.complete(def);
		}
		catch(Throwable ex){
			ret.fail(ex);
		}
		return ret;
	}

	@Override
	public Future<List<ServiceParamsInterface>> getServiceParams(String serviceId, LoggerInterface requestFactory) {
		Future<List<ServiceParamsInterface>> fut = Future.future();
		LoggerInterface logger = requestFactory.createLocalLogger("ConsulServiceCatalog", "getServiceParams");
		try {
			logger.log(Level.INFO, "Getting instances of service with ID '" + serviceId + "'");
			Response<List<CatalogService>> serviceResponse = this.consulClient.getCatalogService(serviceId, this.queryParams);
			
			if(serviceResponse == null){
				fut.fail(new MheException(500, "SERVICE_NOT_FOUND", "Service with ID '" + serviceId + "' not found"));
			}
			else {
				logger.log(Level.INFO, "Instances of service with ID '" + serviceId + "' got!");
				List<ServiceParamsInterface> ret = new ArrayList<ServiceParamsInterface>();
				for(CatalogService service : serviceResponse.getValue()) {
					ret.add(new ConsulServiceParams(service, this.getExtraInfo(serviceId)));
				}
				fut.complete(ret);
			}
		}
		catch(Throwable ex){
			fut.fail(ex);
		}
		return fut;
	}
}

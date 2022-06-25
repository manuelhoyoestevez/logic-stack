function fn() {    
    var env = karate.env; // get system property 'karate.env'
    karate.log('karate.env system property was:', env);

    if (!env) {
        env = 'test';
    }

    var config = {
        env: env,
	    urlApi: 'http://localhost:8583'
    }

    return config;
}

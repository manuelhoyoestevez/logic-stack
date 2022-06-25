package com.mhe.dev.logic.stack.infrastructure.rest.spring.controller;

import com.mhe.dev.logic.stack.infrastructure.rest.spring.SystemProperties;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.api.SystemApi;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemController.
 */
@RestController
public class SystemController implements SystemApi
{
    @Override
    public ResponseEntity<Object> getSystemProperties()
    {
        Map<String, String> systemProperties = SystemProperties.getProperties();
        return new ResponseEntity<>(systemProperties, HttpStatus.OK);
    }
}

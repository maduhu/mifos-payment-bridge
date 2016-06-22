package com.omexit.mifos;

import com.omexit.payment.PaymentService;
import com.omexit.util.BaseController;
import com.omexit.util.exceptions.ValidationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Antony on 2/9/2016.
 */
@RestController
public class MifosController extends BaseController {
    @Autowired
    PaymentService paymentService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = PAYMENT_HOOK, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> paymentHook(@RequestHeader("Fineract-Platform-TenantId") final String tenantId,
                                            @RequestHeader("X-Fineract-Entity") final String entity,
                                            @RequestHeader("X-Fineract-Action") final String action, @RequestBody final String payload)
            throws ValidationException {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        System.err.println(payload);
        logger.info(String.format("paymentHook(tenantId=%s,entity=%s,action=%s,payload=%s)", tenantId, entity, action, payload));
        //Add missing fields from the headers to the payload
        JSONObject jsonObject = new JSONObject(payload);
        jsonObject.put("entity", entity);
        jsonObject.put("action", action);
        jsonObject.put("tenantId", tenantId);
        String request = jsonObject.toString();

        paymentService.handleHook(entity, request);

        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }
}

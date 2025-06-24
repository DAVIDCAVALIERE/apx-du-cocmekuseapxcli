package com.bbva.cmek.lib.rme2.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.apx.exception.io.network.TimeoutException;
import com.bbva.cmek.dto.accounts.AccountDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;

/**
 * The CMEKRME2Impl class...
 */
public class CMEKRME2Impl extends CMEKRME2Abstract {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMEKRME2Impl.class);

    //soap
    @Override
    public AccountDTO executeGetAccount(String numberAccount) {
        AccountDTO response = new AccountDTO();
        HashMap<String, String> params = new HashMap<>();
        params.put("account", numberAccount); //URI parameter
        try {
            ResponseEntity<String> responseService =
                    this.internalApiConnector.getForEntity("aso.getAccount", String.class, params);
            if (responseService != null) {
                String responseExecute = responseService.getBody();
                Gson gson = getGsonObject();
                response = gson.fromJson(responseExecute, AccountDTO.class);
            } else {
                // Si responseService es null, asignar código de error y mensaje
                response.setErrorCode("EXC_VAR");
                response.setErrorMessage("Error de comunicación con el servidor");
            }
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            response = new AccountDTO();
            response.setErrorCode(ex.getStatusCode().toString());
            response.setErrorMessage(ex.getResponseBodyAsString());
            this.addAdvice("CMEK12006001"); //cuenta no existe
        } catch (RestClientException ex) {
            response = new AccountDTO();
            response.setErrorCode("EXC_VAR");
            response.setErrorMessage(ex.getMessage());
            this.addAdvice("CMEK12006001"); //cuenta no existe
        } catch (BusinessException | TimeoutException ex) {
            response = new AccountDTO();
            response.setErrorCode("EXC_VAR1");
            response.setErrorMessage(ex.getMessage());
        }
        return response;
    }

    private Gson getGsonObject() {
        return new GsonBuilder().create();
    }
}

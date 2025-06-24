package com.bbva.cmek.lib.rme1.impl;

import com.bbva.cmek.dto.accounts.AccountDTO;
import com.bbva.cmek.dto.payments.PaymentDTO;
import com.datiobd.daas.Parameters;
import com.datiobd.daas.conf.EnumOperation;
import com.datiobd.daas.conf.MongoConnectorException;
import com.datiobd.daas.model.DocumentWrapper;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The CMEKRME1Impl class...
 */
public class CMEKRME1Impl extends CMEKRME1Abstract {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMEKRME1Impl.class);

    private static final String BD1 = "BMG_CMEK_CO_BILL";
    private static final String COLLECTION_TRANSACTION = "c_cmek_bill_payment";
    private static final String TRANSACTION_ID = "transaction_id";

    @Override
    public PaymentDTO executeDoBillPayment(PaymentDTO payment) {
        PaymentDTO response = new PaymentDTO();

        AccountDTO serviceResponse = cmekRME2.executeGetAccount(payment.getAccount().getId());

        if (serviceResponse != null) {
            boolean isValidPayment = false;
            response.setId(payment.getId());
            response.setOperationDateTime(new Date());
            response.setAccount(serviceResponse);
            response.setBill(payment.getBill());
            isValidPayment = validateBill(payment.getBill().getNumber(), payment.getBill().getValue(),
                    serviceResponse.getBalance());
            if (isValidPayment) {
                registryPayment(payment);
            }
        }
        return response;
    }

    //oracle
    public boolean validateBill(String billId, long billAmount, long balanceAccount) {
        boolean isValidBill = false;
        List<Map<String, Object>> map = jdbcUtils.queryForList("sql.select.bill", billId);
        isValidBill = verifyBill(map, billId, billAmount, balanceAccount);
        return isValidBill;
    }

    public boolean verifyBill(List<Map<String, Object>> bills, String idBill,
                              long expectedAmount, long balanceAccount) {
        for (Map<String, Object> factura : bills) {
            String billID = (String) factura.get("BILL_ID");
            Date expiration = (Date) factura.get("EXPIRATION_DATE");
            BigDecimal billAmount = (BigDecimal) factura.get("BILL_AMOUNT");
            double amountBill = billAmount.doubleValue();

            String estado = (String) factura.get("BILL_STATUS");
            //verificar que la factura existe y esta activa
            if (billID.equals(idBill) && estado.trim().equals("VIGENTE")) {
                //verificar que la factura no este vencida
                if (expiration.after(new Date()) || expiration.equals(new Date())) {
                    //verificar que el monto coincida con el valor de la base de datos y sea ,ayor al balance de la cuenta
                    if (amountBill == (double) expectedAmount && balanceAccount >= amountBill) {
                        return true; //La factura es valida
                    } else {
                        this.addAdvice("CMEK12006002");
                        return false; //El monto no coincide
                    }
                } else {
                    return false; //la factura esta vencida
                }
            }
        }
        this.addAdvice("CMEK12006003");
        return false; //la factura no existe o esta inactiva
    }

    //mongo
    public String registryPayment(PaymentDTO payment) {
        LOGGER.info("insertDataOperation");
        String response = null;
        Map<String, Object> baseParameters = getParameters();
        baseParameters.put(Parameters.COLLECTION, COLLECTION_TRANSACTION);
        try {
            LOGGER.info("Se ejecuta la insercion de la operacion");
            String json = dtoToJsonPayment(payment);
            DocumentWrapper documentWrapper = DocumentWrapper.parse(json);
            baseParameters.put(Parameters.DOCUMENT, documentWrapper);
            this.daasMongoConnector.executeWithNoReturn(EnumOperation.INSERT_ONE, baseParameters);
            response = documentWrapper.getString(TRANSACTION_ID);
        } catch (MongoConnectorException e) {
            LOGGER.info("error al ejecutar la inserccion {}", e);
        }
        LOGGER.info("final insertDataOperation");
        return response;
    }

    //obtiene parametros de bd y de api
    private Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put(Parameters.DATABASE_PROPERTY_NAME, BD1);
        params.put(Parameters.API_OR_REST, Parameters.API);
        return params;
    }

    //convierte PaymentDTO en un JSON
    public String dtoToJsonPayment(PaymentDTO payment) {

        Document document = new Document();
        document.put("PaymentID", payment.getId());
        document.put("PaymentAmount", payment.getBill().getValue());
        document.put("PaymentDate", payment.getOperationDateTime());
        return document.toJson();
    }

}

package com.bbva.cmek;

import com.bbva.cmek.dto.accounts.AccountDTO;
import com.bbva.cmek.dto.payments.PaymentDTO;
import com.bbva.cmek.lib.rme1.CMEKRME1;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Advice;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.Severity;
import com.bbva.elara.domain.transaction.request.header.CommonRequestHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CMEKTME101COTransactionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMEKTME101COTransactionTest.class);

    private Map<String, Object> parameters;

    private Map<Class<?>, Object> serviceLibraries;

    @Mock
    private ApplicationConfigurationService applicationConfigurationService;

    @Mock
    private CommonRequestHeader commonRequestHeader;

    @Mock
    private CMEKRME1 cmekrme1;

    private final CMEKTME101COTransaction transaction = spy(new CMEKTME101COTransaction() {
        @Override
        protected void addParameter(String field, Object obj) {
            if (parameters != null) {
                parameters.put(field, obj);
            }
        }

        @Override
        protected Object getParameter(String field) {
            return parameters.get(field);
        }

        @Override
        protected <T> T getServiceLibrary(Class<T> serviceInterface) {
            return (T) serviceLibraries.get(serviceInterface);
        }

        @Override
        public String getProperty(String keyProperty) {
            return applicationConfigurationService.getProperty(keyProperty);
        }

        @Override
        protected CommonRequestHeader getRequestHeader() {
            return commonRequestHeader;
        }
    });

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        initializeTransaction();
        // TODO - Set the mocks created from the libraries to the transaction, e.g.:
        setServiceLibrary(CMEKRME1.class, cmekrme1);

    }

    private void initializeTransaction() {
        transaction.setContext(new Context());
        parameters = new HashMap<>();
        serviceLibraries = new HashMap<>();
    }

    private void setServiceLibrary(Class<?> clasz, Object mockObject) {
        serviceLibraries.put(clasz, mockObject);
    }

    private void setParameterToTransaction(String parameter, Object value) {
        parameters.put(parameter, value);
    }

    private Object getParameterFromTransaction(String parameter) {
        return parameters.get(parameter);
    }

    @Test
    public void executeTest() {

        PaymentDTO mockPaymentDTO = new PaymentDTO();  // DTO de prueba
        PaymentDTO responsePaymentDTO = new PaymentDTO(); // Respuesta simulada

        // Configurar el estado inicial de `transaction`
        transaction.setPayment(mockPaymentDTO);

        // Mock del metodo externo
        when(cmekrme1.executeDoBillPayment(mockPaymentDTO)).thenReturn(responsePaymentDTO);

        // Act
        transaction.execute(); // Ejecutamos el metodo bajo prueba

        // Assert
        verify(cmekrme1).executeDoBillPayment(mockPaymentDTO); // Verifica la interacción con el mock
        assertEquals(responsePaymentDTO, transaction.getPayment());
        assertEquals(0, transaction.getAdviceList().size());
    }

    @Test
    public void executeTest_withAdviceList_notEmpty() {
        PaymentDTO mockPaymentDTO = new PaymentDTO();  // DTO de prueba
        PaymentDTO responsePaymentDTO = new PaymentDTO(); // Respuesta simulada

        // Configurar el estado inicial de la transacción
        transaction.setPayment(mockPaymentDTO);

        // Mock del método externo
        when(cmekrme1.executeDoBillPayment(mockPaymentDTO)).thenReturn(responsePaymentDTO);

        // Mockear getAdviceList() para que devuelva una lista con un elemento
        List<Advice> mockedAdviceList = new ArrayList<>();
        mockedAdviceList.add(new Advice()); // Añadimos un advice a la lista

        // Usamos doReturn para mockear el comportamiento de getAdviceList()
        doReturn(mockedAdviceList).when(transaction).getAdviceList();

        // Actuar  (Ejecutar el método bajo prueba)
        transaction.execute(); // Ejecutamos el método bajo prueba

        // Verifica la interacción con el mock
        verify(cmekrme1).executeDoBillPayment(mockPaymentDTO);

        // Verifica que se haya actualizado la cuenta correctamente
        Assert.assertEquals(responsePaymentDTO, transaction.getPayment());

        // Verifica que la lista de advice no esté vacía
        Assert.assertFalse(transaction.getAdviceList().isEmpty());

        // Verifica que se haya establecido el nivel de severidad ENR
        Assert.assertEquals(Severity.ENR, transaction.getSeverity());
    }

}

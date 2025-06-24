package com.bbva.cmek.lib.rme2.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.apx.exception.io.network.TimeoutException;
import com.bbva.cmek.dto.accounts.AccountDTO;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.utility.api.connector.APIConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class CMEKRME2ImplTest {

    @Mock
    protected APIConnector internalApiConnector;
    @Mock
    protected APIConnector externalApiConnector;

    @InjectMocks
    private CMEKRME2Impl cmekrme2 = new CMEKRME2Impl() {
        protected APIConnector getInternalApiConnector() {
            return internalApiConnector;
        }

        protected APIConnector getExternalApiConnector() {
            return externalApiConnector;
        }

        @Override
        protected void addAdvice(String code) {
            // Simular comportamiento o ignorarlo en los tests
        }
    };

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteGetAccount_Success() {
        // Setup
        String accountId = "1234";
        String jsonResponse = "{\"id\":\"1234\", \"balance\":1000}"; // Ejemplo de respuesta JSON
        AccountDTO expectedAccount = new AccountDTO();
        expectedAccount.setId("1234");
        expectedAccount.setBalance(1000);

        // Mock del ResponseEntity
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(internalApiConnector.getForEntity(anyString(), eq(String.class), anyMap()))
                .thenReturn(responseEntity);

        // Test: ejecución del método
        AccountDTO result = cmekrme2.executeGetAccount(accountId);

        // Verificaciones
        assertNotNull(result);
        assertEquals(expectedAccount.getId(), result.getId());
        assertEquals(expectedAccount.getBalance(), result.getBalance(), 0.01);
    }

    @Test
    public void testExecuteGetAccount_HttpStatusCodeException() {
        // Setup
        String accountId = "123456";
        String errorMessage = "Error en la API";  // Mensaje de error esperado
        String errorCode = "500";  // Código de error esperado

        // Simulamos que ocurre una HttpStatusCodeException
        HttpStatusCodeException exception = Mockito.mock(HttpStatusCodeException.class);
        when(exception.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(exception.getResponseBodyAsString()).thenReturn(errorMessage);

        // Simulamos que el internalApiConnector lanza la excepción
        when(internalApiConnector.getForEntity(anyString(), eq(String.class), anyMap()))
                .thenThrow(exception);

        // Test: ejecución del método
        AccountDTO result = cmekrme2.executeGetAccount(accountId);

        // Verificaciones
        assertNotNull(result);  // Verifica que el resultado no sea null
        assertEquals(errorCode, result.getErrorCode());  // Verifica el código de error
        assertEquals(errorMessage, result.getErrorMessage());  // Verifica el mensaje de error
    }

    @Test
    public void testExecuteGetAccount_RestClientException() {
        // Setup
        String accountId = "123456";
        String errorMessage = "Error de comunicación con el servidor";
        AccountDTO expectedAccount = new AccountDTO();
        expectedAccount.setErrorCode("EXC_VAR");
        expectedAccount.setErrorMessage(errorMessage);

        // Mock de la excepción RestClientException
        when(internalApiConnector.getForEntity(anyString(), eq(String.class), anyMap()))
                .thenThrow(new RestClientException(errorMessage) {
                });

        // Test: ejecución del método
        AccountDTO result = cmekrme2.executeGetAccount(accountId);

        // Verificaciones
        assertNotNull(result);
        assertEquals(expectedAccount.getErrorCode(), result.getErrorCode());
        assertEquals(expectedAccount.getErrorMessage(), result.getErrorMessage());
    }

    @Test
    public void testExecuteGetAccount_BusinessException() {
        // Crear la BusinessException
        BusinessException mockException = mock(BusinessException.class);
        Mockito.when(mockException.getMessage()).thenReturn("Business Logic Error");

        String numberAccount = "1234567890";
        // Configuramos la excepción BusinessException
        Mockito.when(internalApiConnector.getForEntity(eq("aso.getAccount"), eq(String.class), anyMap()))
                .thenThrow(mockException);

        //Llamar al metodo
        AccountDTO result = cmekrme2.executeGetAccount(numberAccount);

        //Verificar que el resultado tenga el código de error y el mensaje esperado
        assertNotNull(result);
        assertEquals("EXC_VAR1", result.getErrorCode());  // Código de error asignado en el catch
        assertEquals("Business Logic Error", result.getErrorMessage());  // Mensaje de error
    }

    @Test
    public void testExecuteGetAccount_TimeoutException() {
        // Crear la excepción TimeoutException
        TimeoutException mockException = mock(TimeoutException.class);
        Mockito.when(mockException.getMessage()).thenReturn("Timeout occurred during API call");

        String numberAccount = "1234567890";

        // Configuramos la excepción TimeoutException
        Mockito.when(internalApiConnector.getForEntity(eq("aso.getAccount"), eq(String.class), anyMap()))
                .thenThrow(mockException);

        // Llamar al metodo
        AccountDTO result = cmekrme2.executeGetAccount(numberAccount);

        // Verificar que el resultado tenga el código de error y el mensaje esperado
        assertNotNull(result);
        assertEquals("EXC_VAR1", result.getErrorCode());  // Código de error asignado en el catch
        assertEquals("Timeout occurred during API call", result.getErrorMessage());  // Mensaje de error
    }

    @Test
    public void testExecuteGetAccount_ResponseServiceNull() {
        // Setup
        String accountId = "1234";

        // Simulamos que el internalApiConnector retorna null
        when(internalApiConnector.getForEntity(anyString(), eq(String.class), anyMap()))
                .thenReturn(null);

        // Test: ejecución del método
        AccountDTO result = cmekrme2.executeGetAccount(accountId);

        // Verificaciones
        assertNotNull(result);  // Verifica que el resultado no sea null
        assertEquals("EXC_VAR", result.getErrorCode());  // Verifica el código de error
        assertEquals("Error de comunicación con el servidor", result.getErrorMessage());  // Verifica el mensaje de error
    }

    @Test
    public void testSetApplicationConfigurationService() {
        // Setup
        ApplicationConfigurationService mockConfigService = Mockito.mock(ApplicationConfigurationService.class);

        // Invocar el setter para asignar el servicio de configuración
        cmekrme2.setApplicationConfigurationService(mockConfigService);

        // Verificar que la configuración se haya asignado correctamente
        assertNotNull(cmekrme2.applicationConfigurationService);
        assertEquals(mockConfigService, cmekrme2.applicationConfigurationService);
    }

}

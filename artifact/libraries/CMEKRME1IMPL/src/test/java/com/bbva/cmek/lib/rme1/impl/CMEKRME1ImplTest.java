package com.bbva.cmek.lib.rme1.impl;

import com.bbva.cmek.dto.accounts.AccountDTO;
import com.bbva.cmek.dto.payments.BillDTO;
import com.bbva.cmek.dto.payments.PaymentDTO;
import com.bbva.cmek.lib.rme2.CMEKRME2;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;

import com.bbva.elara.utility.jdbc.connector.JdbcUtils;
import com.datiobd.daas.conf.EnumOperation;
import com.datiobd.daas.conf.MongoConnectorException;
import com.datiobd.daas.model.DocumentWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CMEKRME1ImplTest {

    @Mock
    private Context context;

    @Mock
    private CMEKRME2 cmekRME2;

    @Mock
    private com.datiobd.daas.DaasMongoConnector daasMongoConnector; // Simula Mongo
    @Mock
    private JdbcUtils jdbcUtils;


    private PaymentDTO mockPayment;


    @InjectMocks
    private CMEKRME1Impl cmeKRME1Impl;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);// inicializa los Mocks y spy definidos
        context = new Context();   //Se crea un nuevo contexto para cada prueba
        ThreadContext.set(context);
        mockPayment = new PaymentDTO();
        mockPayment.setId("PAY1");

        BillDTO bill = new BillDTO();
        bill.setNumber("BILL1");
        bill.setValue(1000L);
        mockPayment.setBill(bill);

        AccountDTO account = new AccountDTO();
        account.setId("ACC1");
        account.setBalance(5000L);
        mockPayment.setAccount(account);

    }

    //prueba de ejecucion de pago de factura
    @Test
    public void testRegistryPayment_Success() {
        // Mock del documento
        DocumentWrapper mockDocument = DocumentWrapper.parse("{\"transaction_id\":\"TXN1\"}");

        // Simula inserción en Mongo
        doNothing().when(daasMongoConnector).executeWithNoReturn(eq(EnumOperation.INSERT_ONE), anyMap());

        String transactionId = cmeKRME1Impl.registryPayment(mockPayment);
        assertNull(transactionId); // Como el mock no retorna valores, debe ser nulo
    }

    @Test
    public void testRegistryPayment_Failure() throws MongoConnectorException {
        doThrow(new MongoConnectorException("Mongo Failure"))
                .when(daasMongoConnector).executeWithNoReturn(eq(EnumOperation.INSERT_ONE), anyMap());

        String result = cmeKRME1Impl.registryPayment(mockPayment);
        assertNull(result); // Debe retornar nulo al fallar la inserción
    }

    @Test
    public void testDtoToJsonPayment() {
        String json = cmeKRME1Impl.dtoToJsonPayment(mockPayment);
        assertTrue(json.contains("PAY1"));
        assertTrue(json.contains("1000"));
    }

    @Test
    public void testExecuteDoBillPayment_Success() {
        // Configuracion del mockAccountResponse
        AccountDTO mockAccountResponse = new AccountDTO();
        mockAccountResponse.setId("ACC1");
        mockAccountResponse.setBalance(5000L);

        // Simulacion de llamada a executeGetAccount
        when(cmekRME2.executeGetAccount("ACC1")).thenReturn(mockAccountResponse);

        // Simulacion de respuesta de jdbcUtils con datos válidos para la factura
        when(jdbcUtils.queryForList(anyString(), anyMap())).thenReturn(mockValidBillData());

        // Inicialización del mockPayment
        mockPayment = new PaymentDTO();
        mockPayment.setId("PAY1");

        BillDTO bill = new BillDTO();
        bill.setNumber("BILL1");
        bill.setValue(1000L);
        mockPayment.setBill(bill);

        AccountDTO account = new AccountDTO();
        account.setId("ACC1");
        account.setBalance(5000L);
        mockPayment.setAccount(account);

        // Execute
        PaymentDTO response = cmeKRME1Impl.executeDoBillPayment(mockPayment);

        // Verifica que el response no sea null
        assertNotNull(response);

        // Verifica que el ID del pago sea el esperado
        assertEquals("PAY1", response.getId());
    }


    @Test
    public void testValidateBill_Success() {
        // Simula que la consulta devuelva una factura válida
        when(jdbcUtils.queryForList(anyString(), Optional.ofNullable(any()))).thenReturn(mockValidBillData());

        // Llamado a la función validateBill con una factura que debe ser válida
        boolean isValid = cmeKRME1Impl.validateBill("BILL1", 1000L, 5000L);

        assertTrue(isValid);
    }

    @Test
    public void testValidateBill_Failure() {
        when(jdbcUtils.queryForList(eq("sql.select.bill"), anyMap())).thenReturn(mockInvalidBillData());

        boolean isValid = cmeKRME1Impl.validateBill("BILL2", 500L, 2000L);

        assertFalse(isValid);
    }

    @Test
    public void testExecuteDoBillPayment_InvalidBill() {
        PaymentDTO payment = new PaymentDTO();
        payment = createPaymentDTO("123", "bill1", 1000L);
        AccountDTO serviceResponse = new AccountDTO();
        serviceResponse.setBalance(500L);  // El saldo de la cuenta es insuficiente

        // Configura el comportamiento de los mocks para simular una factura inválida
        when(cmekRME2.executeGetAccount("123")).thenReturn(serviceResponse);
        when(jdbcUtils.queryForList("sql.select.bill", "bill1")).thenReturn(mockInvalidBillData());

        // Execute: acción de pago de factura
        PaymentDTO response = cmeKRME1Impl.executeDoBillPayment(mockPayment);

        // Verifica que la respuesta no sea nula
        assertNotNull(response);
    }

    @Test
    public void testValidateBill_Expired() {
        // Simulacionn del comportamiento de la consulta SQL para obtener la factura expirada
        when(jdbcUtils.queryForList("sql.select.bill", "bill1")).thenReturn(mockInvalidBillData());

        // Llamado metodo validateBill() con los parámetros de prueba
        boolean result = cmeKRME1Impl.validateBill("bill1", 1000L, 5000L);

        // Verifica que el resultado sea falso ya que la factura está vencida
        assertFalse(result);
    }

    // Prueba para registrar el pago
    @Test
    public void testRegistryPayment() {
        PaymentDTO payment = createPaymentDTO("123", "bill1", 1000L);

        // simula el registro de un pago
        String transactionId = cmeKRME1Impl.registryPayment(mockPayment);

        // Verifica que el ID de transacción sea nulo
        assertNull(transactionId);
    }

    @Test
    public void testVerifyBill_AmountDoesNotMatch() {
        List<Map<String, Object>> billData = new ArrayList<>();

        Map<String, Object> bill = new HashMap<>();
        bill.put("BILL_ID", "BILL1");
        bill.put("EXPIRATION_DATE", new Date(System.currentTimeMillis() + 86400000)); // Factura válida
        bill.put("BILL_AMOUNT", new BigDecimal(1500)); // Monto en BD diferente del esperado (1000)
        bill.put("BILL_STATUS", "VIGENTE");
        billData.add(bill);

        boolean result = cmeKRME1Impl.verifyBill(billData, "BILL1", 1000, 5000); // expectedAmount = 1000

        assertFalse(result); // falso: el monto no coincide
    }

    @Test
    public void testExecuteDoBillPayment_Success_ValidPayment() {
        // Crear mock de PaymentDTO y BillDTO
        PaymentDTO payment = new PaymentDTO();
        BillDTO bill = new BillDTO();

        //  objetos no null y tengan valores adecuados
        bill.setNumber("BILL1");
        bill.setValue(1000L);
        bill.setStatus("VIGENTE");
        bill.setSupplier("Supplier XYZ");

        AccountDTO account = new AccountDTO();
        account.setId("ACC1");
        account.setBalance(5000L);

        payment.setId("PAY1");
        payment.setOperationDateTime(new Date());
        payment.setAccount(account);
        payment.setBill(bill);

        // mock de las llamadas necesarias
        when(cmekRME2.executeGetAccount(anyString())).thenReturn(account);

        // Llamada al metodo a probar
        PaymentDTO response = cmeKRME1Impl.executeDoBillPayment(payment);

        // Verificar que el resultado es el esperado
        assertNotNull(response);
        assertEquals(payment.getId(), response.getId());
        assertEquals(payment.getAccount(), response.getAccount());
    }

    private PaymentDTO createPaymentDTO(String number, String bill1, long l) {
        return null;
    }

    private List<Map<String, Object>> mockValidBillData() {
        Map<String, Object> bill = new HashMap<>();
        bill.put("BILL_ID", "BILL1");  // ID de la factura
        bill.put("EXPIRATION_DATE", new Date(System.currentTimeMillis() + 86400000));  // Expiración dentro de 1 día
        bill.put("BILL_AMOUNT", new BigDecimal(1000));  // El monto esperado
        bill.put("BILL_STATUS", "VIGENTE");  // La factura está activa
        return Collections.singletonList(bill);
    }

    private List<Map<String, Object>> mockInvalidBillData() {
        Map<String, Object> bill = new HashMap<>();
        bill.put("BILL_ID", "BILL2");
        bill.put("EXPIRATION_DATE", new Date(System.currentTimeMillis() - 86400000)); // Expirado
        bill.put("BILL_AMOUNT", new BigDecimal(500));
        bill.put("BILL_STATUS", "EXPIRADO");
        return Collections.singletonList(bill);
    }
}

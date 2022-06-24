package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

@ActiveProfiles("test")

@Sql(scripts = { "classpath:flyway/migrations/V1.0_Jeep_Schema.sql",
                 "classpath:flyway/migrations/V1.1_Jeep_Data.sql"},
                   config = @SqlConfig(encoding = "utf-8"))

class CreateOrderTest {
  
  @Autowired
    private TestRestTemplate restTemplate;
  
  @LocalServerPort
    private int serverPort;

  @Test
  void testCreateOrderReturnsSuccess201() {
    // Given: an order as JSON
    String body = createOrderBody();
    String uri = String.format("http://localhost:%d/orders", serverPort);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);
    
    // When: the order is sent
    ResponseEntity<Order> response = restTemplate.exchange(uri, HttpMethod.POST, bodyEntity, Order.class);
    
    // Then: a 201 status is returned 
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    
    // And: the returned order is correct
    assertThat(response.getBody()).isNotNull();
    
    Order order = response.getBody();
    assertThat(order.getCustomer().getCustomerId()).isEqualTo("STERN_TORO");
    assertThat(order.getModel().getModelId()).isEqualTo(JeepModel.WRANGLER_4XE);
    assertThat(order.getModel().getTrimLevel()).isEqualTo("High Altitude 4xe");
    assertThat(order.getModel().getNumDoors()).isEqualTo(4);
    assertThat(order.getColor().getColorId()).isEqualTo("INT_BLACK_STEEL_GRAY");
    assertThat(order.getEngine().getEngineId()).isEqualTo("3_0_DIESEL");
    assertThat(order.getTire().getTireId()).isEqualTo("37_YOKOHAMA");
    assertThat(order.getOptions()).hasSize(6);

  }

  /**
   * 
   */
  private String createOrderBody() {
    
    //@formatter:off
    
                return "{\n"
                        + "   \"customer\":\"STERN_TORO\",\n"
                        + "   \"model\":\"WRANGLER_4XE\",\n"
                        + "   \"trim\":\"High Altitude 4xe\",\n"
                        + "   \"doors\":4,\n"
                        + "   \"color\":\"INT_BLACK_STEEL_GRAY\",\n"
                        + "   \"engine\":\"3_0_DIESEL\",\n"
                        + "   \"tire\":\"37_YOKOHAMA\",\n"
                        + "   \"options\":[\n"
                        + "     \"DOOR_ROUGH_4\",\n"
                        + "     \"EXT_MOPAR_TRAILER\",\n"
                        + "     \"INT_MOPAR_RADIO\",\n"
                        + "     \"STOR_TUFFY_ENCLOSURE\",\n"
                        + "     \"TOP_MOPAR_HARD_3_COLOR\",\n"
                        + "     \"WHEEL_TERA_NOMAD\"\n"
                        + " ]\n"
                        + "}";
    //@formatter:on
    
    }

    
    
  }



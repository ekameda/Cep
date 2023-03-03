package br.com.wipro.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.wipro.dto.AddressDTO;
import br.com.wipro.dto.ZipCodeRequestDTO;
import br.com.wipro.service.ThirdZipCodeResponse;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ZipCodeControllerTest {
	
	private static final String SPECTED_SIAFI = "7107";
	private static final String SPECTED_DDD = "11";
	private static final String SPECTED_GIA = "1004";
	private static final String SPECTED_IBGE = "3550308";
	private static final String ZIPE_CODE = "01001000";
	private static final String SPECTED_ZIP_CODE = "01001-000";
	private static final String SPECTED_ROAD = "Praça da Sé";
	private static final String SPECTED_COMPLEMENT = "lado ímpar";
	private static final String SPECTED_NEIGHBORHOOD_ = "Sé";
	private static final String SPECTED_CITY = "São Paulo";
	private static final String SPECTED_STATE = "SP";
	private static final BigDecimal SPECTED_SHIPPING = new BigDecimal("7.85");
	
	@Autowired
    private MockMvc mockMvc;	
	private ObjectMapper objectMapper;
	private ThirdZipCodeResponse response;
	private RestTemplate restTemplate;
	private ResponseEntity<ThirdZipCodeResponse> responseEntity;
	
	@BeforeEach
	void setup() {
		restTemplate = mock(RestTemplate.class);
		objectMapper = new ObjectMapper();
		response = ThirdZipCodeResponse.builder()
				.cep(SPECTED_ZIP_CODE)
				.logradouro(SPECTED_ROAD)
				.complemento(SPECTED_COMPLEMENT)
				.bairro(SPECTED_NEIGHBORHOOD_)
				.localidade(SPECTED_CITY)
				.uf(SPECTED_STATE)
				.ibge(SPECTED_IBGE)
				.gia(SPECTED_GIA)
				.ddd(SPECTED_DDD)
				.siafi(SPECTED_SIAFI)
				.erro(false)
				.build();
		
		responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		
		when(restTemplate.getForEntity(URI.create(ZIPE_CODE), ThirdZipCodeResponse.class))
		.thenReturn(responseEntity);
	}
	
	@Test
	void shouldReturnAddressDTOSuccessfully() throws UnsupportedEncodingException, Exception {

		ZipCodeRequestDTO request = ZipCodeRequestDTO.builder()
				.cep(ZIPE_CODE)
				.build();
		
		String response = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/consulta-endereco")
                        .content(this.objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
		
		AddressDTO addressDTO = getResponse(response);
		
		assertNotNull(addressDTO);
		assertEquals(SPECTED_ZIP_CODE, convertToUTF8(addressDTO.getCep()));
		assertEquals(SPECTED_ROAD, convertToUTF8(addressDTO.getRua()));
		assertEquals(SPECTED_COMPLEMENT, convertToUTF8(addressDTO.getComplemento()));
		assertEquals(SPECTED_NEIGHBORHOOD_, convertToUTF8(addressDTO.getBairro()));
		assertEquals(SPECTED_CITY, convertToUTF8(addressDTO.getCidade()));
		assertEquals(SPECTED_STATE, convertToUTF8(addressDTO.getEstado()));
		assertEquals(SPECTED_SHIPPING, addressDTO.getFrete());
		
	}
	
	private AddressDTO getResponse(String response) throws JsonMappingException, JsonProcessingException {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JavaType type = objectMapper.getTypeFactory().
                constructType(AddressDTO.class);

        return objectMapper.readValue(response, type);
    }
	
	public static String convertToUTF8(String value) {
        return new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

}

package br.com.wipro.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.com.wipro.dto.AddressDTO;
import br.com.wipro.exception.ExternalAPIException;
import br.com.wipro.exception.InvalidZipCodeException;
import br.com.wipro.service.ThirdZipCodeResponse;
import br.com.wipro.service.ZipCodeService;

class ZipCodeServiceTest {
	
	private static final String HTTPS_VIACEP_COM_BR_WS_S_JSON = "https://viacep.com.br/ws/55814190/json/";
	private static final String ZIP_CODE = "55814190";
	
	private ZipCodeService zipCodeService;
	private RestTemplate restTemplate;
	private ResponseEntity<ThirdZipCodeResponse> responseEntity;
	private ThirdZipCodeResponse response;
	
	@BeforeEach
	void setup () {
		restTemplate = mock(RestTemplate.class);
		zipCodeService = new ZipCodeService(restTemplate);
		ReflectionTestUtils.setField(zipCodeService, "uri", HTTPS_VIACEP_COM_BR_WS_S_JSON);
		
		response = ThirdZipCodeResponse.builder()
				.cep("55814-190")
				.logradouro("Rua E")
				.complemento("(Cohab I)")
				.bairro("Santo Antônio")
				.localidade("Carpina")
				.uf("PE")
				.ibge("2604007")
				.gia("")
				.ddd("81")
				.siafi("2379")
				.erro(false)
				.build();
		
		responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		
		when(restTemplate.getForEntity(URI.create(HTTPS_VIACEP_COM_BR_WS_S_JSON), ThirdZipCodeResponse.class))
			.thenReturn(responseEntity);
	}
	
	@Test
	void shouldReturnAddressDTOSuccessfully() {
		AddressDTO address = assertDoesNotThrow(() -> zipCodeService.getAddressByCep(ZIP_CODE));
		
		String spectedZipCode = "55814-190";
		
		assertNotNull(address);
		assertEquals(spectedZipCode, address.getCep());
	}
	
	@Test
	void shouldThrowsInvalidZipCodeException() {
		response.setErro(true);
		
		String spectedMessage = "Invalid zip code!";
		
		InvalidZipCodeException exception = assertThrows(InvalidZipCodeException.class, 
				() -> zipCodeService.getAddressByCep(ZIP_CODE));
		
		assertEquals(spectedMessage, exception.getMessage());
		
	}
	
	@Test
	void shouldThrowsExternalAPIException() {
		when(restTemplate.getForEntity(URI.create(HTTPS_VIACEP_COM_BR_WS_S_JSON), ThirdZipCodeResponse.class))
			.thenThrow(new RestClientException("Error"));
		
		String spectedMessage = "Unexpected error";
		
		ExternalAPIException exception = assertThrows(ExternalAPIException.class, 
				() -> zipCodeService.getAddressByCep(ZIP_CODE));
		
		assertEquals(spectedMessage, exception.getMessage());
	}

}

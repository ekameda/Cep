package br.com.wipro.service;

import java.math.BigDecimal;
import java.net.URI;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.com.wipro.dto.AddressDTO;
import br.com.wipro.exception.ExternalAPIException;
import br.com.wipro.exception.InvalidZipCodeException;
import br.com.wipro.repository.RegionRepository;

@Service
public class ZipCodeService {
	
	@Value("${viaCep.uri}")
	private String uri;
	
	private RestTemplate restTemplate;

	public ZipCodeService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public AddressDTO getAddressByCep(String zipCode) {
		AddressDTO addressDTOResponse = null;
		
		zipCode = Pattern.compile("\\D").matcher(zipCode).replaceAll("");
		
		if(zipCode.length() != 8) {
			throw new ExternalAPIException("CEP não for encontrado!");
		}
		
		System.out.println(zipCode);
		
        try {
        	ResponseEntity<ThirdZipCodeResponse> responseEntity = restTemplate
                    .getForEntity(URI.create(String.format(uri, zipCode)), ThirdZipCodeResponse.class);
        	
        	if (responseEntity.getBody().isErro()) {
        		throw new InvalidZipCodeException("CEP não for encontrado!");
        	}
        	
        	addressDTOResponse = AddressDTO.builder()
        			.cep(responseEntity.getBody().getCep())
        			.rua(responseEntity.getBody().getLogradouro())
        			.complemento(responseEntity.getBody().getComplemento())
        			.bairro(responseEntity.getBody().getBairro())
        			.cidade(responseEntity.getBody().getLocalidade())
        			.estado(responseEntity.getBody().getUf())
        			.frete(getShippingByUf(responseEntity.getBody().getUf()))
        			.build();
        	
        } catch (RestClientException ex) {
            throw new ExternalAPIException("CEP não for encontrado!");
        }
        
        return addressDTOResponse;		
	}

	private BigDecimal getShippingByUf(String uf) {
		RegionRepository regionRepository = new RegionRepository();		
		return regionRepository.getshippingByUf(uf);
	}

}

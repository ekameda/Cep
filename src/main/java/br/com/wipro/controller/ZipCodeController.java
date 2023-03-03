package br.com.wipro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wipro.dto.AddressDTO;
import br.com.wipro.dto.ZipCodeRequestDTO;
import br.com.wipro.service.ZipCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Zip Code Search")
@RequestMapping("/v1/consulta-endereco")
public class ZipCodeController {
	
	private ZipCodeService zipcodeService;
	
	public ZipCodeController(ZipCodeService zupcodeService) {
		this.zipcodeService = zupcodeService;
	}

	@PostMapping
	@Operation(summary = "Get shipping by zip code.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Created"), 
		@ApiResponse(responseCode = "400", description = "Bad Request"),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error")})
	public ResponseEntity<AddressDTO> inserirPosicao(@RequestBody ZipCodeRequestDTO zipCodeRequestDTO) {
		return new ResponseEntity<>(zipcodeService.getAddressByCep(zipCodeRequestDTO.getCep()), HttpStatus.OK);
	}

}

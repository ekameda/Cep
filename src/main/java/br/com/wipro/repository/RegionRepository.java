package br.com.wipro.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class RegionRepository {
	
	Map<String,BigDecimal> states = new HashMap<>();

	public RegionRepository() {		
		states.put( "AM", BigDecimal.valueOf(20.83));
		states.put( "RR", BigDecimal.valueOf(20.83));
		states.put( "AP", BigDecimal.valueOf(20.83));
		states.put( "PA", BigDecimal.valueOf(20.83));
		states.put( "TO", BigDecimal.valueOf(20.83));
		states.put( "RO", BigDecimal.valueOf(20.83));
		states.put( "AC", BigDecimal.valueOf(20.83));		
		states.put( "MA", BigDecimal.valueOf(15.98));
		states.put( "PI", BigDecimal.valueOf(15.98));
		states.put( "CE", BigDecimal.valueOf(15.98));
		states.put( "RN", BigDecimal.valueOf(15.98));
		states.put( "PE", BigDecimal.valueOf(15.98));
		states.put( "PB", BigDecimal.valueOf(15.98));
		states.put( "SE", BigDecimal.valueOf(15.98));
		states.put( "AL", BigDecimal.valueOf(15.98));
		states.put( "BA", BigDecimal.valueOf(15.98));		 
		states.put( "MT", BigDecimal.valueOf(12.50));
		states.put( "MS", BigDecimal.valueOf(12.50));
		states.put( "GO", BigDecimal.valueOf(12.50));		 
		states.put( "SP", BigDecimal.valueOf(7.85));
		states.put( "RJ", BigDecimal.valueOf(7.85));
		states.put( "ES", BigDecimal.valueOf(7.85));
		states.put( "MG", BigDecimal.valueOf(7.85));		 
		states.put( "PR", BigDecimal.valueOf(17.30));
		states.put( "RS", BigDecimal.valueOf(17.30));
		states.put( "SC", BigDecimal.valueOf(17.30));	

	}
	
	public BigDecimal getshippingByUf(String uf) {
		BigDecimal shipping = BigDecimal.ZERO;
		if (states.containsKey(uf)) {
			shipping = states.get(uf);
		}
		return shipping;
	} 	

}

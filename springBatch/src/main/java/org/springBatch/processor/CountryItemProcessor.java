package org.springBatch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springBatch.entity.Country;
import org.springframework.batch.item.ItemProcessor;

public class CountryItemProcessor implements ItemProcessor<Country, Country> {

	private static final Logger log = LoggerFactory
			.getLogger(CountryItemProcessor.class);

	@Override
	public Country process(final Country country) throws Exception {
		final String name = country.getName().toUpperCase();

		final Country transformedCountry = new Country(country.getId(), name,
				country.getCurrency());

		log.info("Converting : {" + country + "} into {" + transformedCountry
				+ "}");

		return transformedCountry;
	}

}

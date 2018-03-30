package pl.piomin.services.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import pl.piomin.services.ignite.model.Contact;
import pl.piomin.services.ignite.model.Person;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableIgniteRepositories
@EnableSwagger2
public class IgniteRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgniteRestApplication.class, args);
	}

	@Bean
	public Ignite igniteInstance() {
		
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setIgniteInstanceName("ignite-cluster-node");
		
		CacheConfiguration<Long, Person> ccfg1 = new CacheConfiguration<>("PersonCache");
		ccfg1.setIndexedTypes(Long.class, Person.class);
		CacheConfiguration<Long, Contact> ccfg2 = new CacheConfiguration<>("ContactCache");
		ccfg2.setIndexedTypes(Long.class, Contact.class);
		cfg.setCacheConfiguration(ccfg1, ccfg2);
		
//		DataStorageConfiguration storageCfg = new DataStorageConfiguration();
//		storageCfg.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
//		cfg.setDataStorageConfiguration(storageCfg);
		
		return Ignition.start(cfg);
	}
	
	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
				.apis(RequestHandlerSelectors.basePackage("pl.piomin.services.ignite.controller"))
				.paths(PathSelectors.any())
			.build()
			.apiInfo(new ApiInfoBuilder().version("1.0").title("Ignite API").description("Documentation Ignite API v1.0").build());
	}
	

}

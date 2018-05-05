package pl.piomin.services.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.kubernetes.TcpDiscoveryKubernetesIpFinder;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import pl.piomin.services.ignite.model.Contact;
import pl.piomin.services.ignite.model.Person;

@SpringBootApplication
@EnableIgniteRepositories
public class IgniteRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgniteRestApplication.class, args);
	}

	@Bean
	public Ignite igniteInstance() {
		
		TcpDiscoverySpi spi = new TcpDiscoverySpi(); 
		TcpDiscoveryKubernetesIpFinder ipFinder = new TcpDiscoveryKubernetesIpFinder();
		spi.setIpFinder(ipFinder);
		
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setClientMode(true);
		cfg.setDiscoverySpi(spi);
//		cfg.setIgniteInstanceName("kubernetes-cluster");
		
		CacheConfiguration<Long, Person> ccfg1 = new CacheConfiguration<>("PersonCache");
		ccfg1.setIndexedTypes(Long.class, Person.class);
		CacheConfiguration<Long, Contact> ccfg2 = new CacheConfiguration<>("ContactCache");
		ccfg2.setIndexedTypes(Long.class, Contact.class);
		cfg.setCacheConfiguration(ccfg1, ccfg2);
		
		return Ignition.start(cfg);
	}

}

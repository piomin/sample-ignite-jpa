package pl.piomin.services.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.kubernetes.TcpDiscoveryKubernetesIpFinder;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
		cfg.setIgniteInstanceName("kubernetes-cluster");
		
		return Ignition.start(cfg);
	}

}

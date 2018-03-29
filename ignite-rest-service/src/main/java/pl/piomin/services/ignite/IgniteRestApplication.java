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
		ipFinder.setMasterUrl("https://192.168.99.100:8443");
		ipFinder.setAccountToken("C:\\Users\\minkowp\\Kubernetes\\token");
		spi.setIpFinder(ipFinder);
		
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setClientMode(true);
//		cfg.setIgniteInstanceName("Kubernetes-Demo");
		cfg.setDiscoverySpi(spi);
		cfg.setIgniteInstanceName("kubernetes-cluster");
		
//		ClientConnectorConfiguration cliConnCfg = new ClientConnectorConfiguration();
//		cliConnCfg.setHost("ignite");
//		cliConnCfg.setPort(9042);
//		cliConnCfg.setTcpNoDelay(true);
//		cfg.setClientConnectorConfiguration(cliConnCfg);
		
//        CacheConfiguration ccfg = new CacheConfiguration();
//        ccfg.setName("PersonCache");
//        ccfg.setCacheMode(CacheMode.PARTITIONED);
//        ccfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
//        ccfg.setCopyOnRead(true);
//        CacheConfiguration ccfg2 = new CacheConfiguration();
//        ccfg.setName("ContactCache");
//        ccfg.setCacheMode(CacheMode.PARTITIONED);
//        ccfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
//        ccfg.setCopyOnRead(true);
        
//        cfg.setCacheConfiguration(ccfg, ccfg2);
		return Ignition.start(cfg);
	}

}

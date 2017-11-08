package pl.piomin.services.ignite;

import javax.sql.DataSource;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.cache.store.jdbc.dialect.MySQLDialect;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.internal.processors.odbc.jdbc.JdbcMetaPrimaryKeysRequest;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mysql.jdbc.MysqlDefs;

import pl.piomin.services.ignite.model.Person;

@SpringBootApplication
@EnableIgniteRepositories
public class IgniteRestApplication {
	
	@Autowired
	DataSource datasource;
	
    public static void main(String[] args) {
        SpringApplication.run(IgniteRestApplication.class, args);
    }
    
    @Bean
    public Ignite igniteInstance() {
    	System.out.println("datasource: " + datasource.toString());
    	IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setIgniteInstanceName("ignite-1");
        cfg.setPeerClassLoadingEnabled(true);
        CacheConfiguration<Long, Person> ccfg = new CacheConfiguration<Long, Person>("PersonCache");
        ccfg.setIndexedTypes(Long.class, Person.class);
        ccfg.setWriteBehindEnabled(true);
        ccfg.setReadThrough(true);
        ccfg.setWriteThrough(true);
        
        CacheJdbcPojoStoreFactory f = new CacheJdbcPojoStoreFactory();
        f.setDataSource(datasource);
        f.setDialect(new MySQLDialect());
        JdbcType jdbcType = new JdbcType();
        jdbcType.setCacheName("PersonCache");
        jdbcType.setKeyType(Long.class);
        jdbcType.setValueType(Person.class);
        jdbcType.setDatabaseTable("person");
        jdbcType.setDatabaseSchema("ignite");
        jdbcType.setKeyFields(new JdbcTypeField(1, "id", Long.class, "id"));
        f.setTypes(jdbcType);
        ccfg.setCacheStoreFactory(f);
        
        cfg.setCacheConfiguration(ccfg);
    	return Ignition.start(cfg);
    }
    
}

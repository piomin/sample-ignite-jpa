package pl.piomin.services.ignite;

import java.sql.JDBCType;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheKeyConfiguration;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.cache.store.jdbc.dialect.MySQLDialect;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import pl.piomin.services.ignite.model.Contact;
import pl.piomin.services.ignite.model.ContactType;
import pl.piomin.services.ignite.model.Gender;
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
    	IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setIgniteInstanceName("ignite-1");
        cfg.setPeerClassLoadingEnabled(true);
        
        CacheConfiguration<Long, Contact> ccfg2 = new CacheConfiguration<>("ContactCache");
//        ccfg2.setCacheMode(CacheMode.REPLICATED);
        ccfg2.setIndexedTypes(Long.class, Contact.class);
        ccfg2.setWriteBehindEnabled(true);
        ccfg2.setReadThrough(true);
        ccfg2.setWriteThrough(true);
        CacheJdbcPojoStoreFactory<Long, Contact> f2 = new CacheJdbcPojoStoreFactory<>();
        f2.setDataSource(datasource);
        f2.setDialect(new MySQLDialect());
        JdbcType jdbcContactType = new JdbcType();
        jdbcContactType.setCacheName("ContactCache");
        jdbcContactType.setKeyType(Long.class);
        jdbcContactType.setValueType(Contact.class);
        jdbcContactType.setDatabaseTable("contact");
        jdbcContactType.setDatabaseSchema("ignite");
        jdbcContactType.setKeyFields(new JdbcTypeField(JDBCType.INTEGER.getVendorTypeNumber(), "id", Long.class, "id"));
        jdbcContactType.setValueFields(new JdbcTypeField(JDBCType.VARCHAR.getVendorTypeNumber(), "contact_type", ContactType.class, "type"), new JdbcTypeField(JDBCType.VARCHAR.getVendorTypeNumber(), "location", String.class, "location"), new JdbcTypeField(JDBCType.INTEGER.getVendorTypeNumber(), "person_id", Long.class, "personId"));
        f2.setTypes(jdbcContactType);
        ccfg2.setCacheStoreFactory(f2);
        
        CacheConfiguration<Long, Person> ccfg = new CacheConfiguration<>("PersonCache");
//        ccfg.setCacheMode(CacheMode.REPLICATED);
        ccfg.setIndexedTypes(Long.class, Person.class);
        ccfg.setWriteBehindEnabled(true);
        ccfg.setReadThrough(true);
        ccfg.setWriteThrough(true);
        CacheJdbcPojoStoreFactory<Long, Person> f = new CacheJdbcPojoStoreFactory<>();
        f.setDataSource(datasource);
        f.setDialect(new MySQLDialect());
        JdbcType jdbcType = new JdbcType();
        jdbcType.setCacheName("PersonCache");
        jdbcType.setKeyType(Long.class);
        jdbcType.setValueType(Person.class);
        jdbcType.setDatabaseTable("person");
        jdbcType.setDatabaseSchema("ignite");
        jdbcType.setKeyFields(new JdbcTypeField(JDBCType.INTEGER.getVendorTypeNumber(), "id", Long.class, "id"));
        jdbcType.setValueFields(new JdbcTypeField(JDBCType.VARCHAR.getVendorTypeNumber(), "first_name", String.class, "firstName"), new JdbcTypeField(JDBCType.VARCHAR.getVendorTypeNumber(), "last_name", String.class, "lastName"), new JdbcTypeField(JDBCType.VARCHAR.getVendorTypeNumber(), "gender", Gender.class, "gender"), new JdbcTypeField(JDBCType.VARCHAR.getVendorTypeNumber(), "country", String.class, "country"), new JdbcTypeField(6, "city", String.class, "city"), new JdbcTypeField(JDBCType.VARCHAR.getVendorTypeNumber(), "address", String.class, "address"), new JdbcTypeField(JDBCType.DATE.getVendorTypeNumber(), "birth_date", Date.class, "birthDate"));
//        JdbcType jdbcType2 = new JdbcType();
//        jdbcType2.setCacheName("PersonCache");
//        jdbcType2.setKeyType(Long.class);
//        jdbcType2.setValueType(Contact.class);
//        jdbcType2.setDatabaseTable("contact");
//        jdbcType2.setDatabaseSchema("ignite");
//        jdbcType2.setKeyFields(new JdbcTypeField(1, "id", Long.class, "id"));
//        jdbcType2.setValueFields(new JdbcTypeField(2, "contact_type", ContactType.class, "type"), new JdbcTypeField(3, "location", String.class, "location"), new JdbcTypeField(4, "person_id", Integer.class, "personId"));
        f.setTypes(jdbcType);
        ccfg.setCacheStoreFactory(f);
        
         
        cfg.setCacheConfiguration(ccfg, ccfg2);
    	return Ignition.start(cfg);
    }
    
    @Bean(name = "personSequence")
    public IgniteAtomicSequence personSequence() {
    	Ignite i = igniteInstance();
    	return i.atomicSequence("personSequence", 0, true);
    }
    
    @Bean(name = "contactSequence")
    public IgniteAtomicSequence contactSequence() {
    	Ignite i = igniteInstance();
    	return i.atomicSequence("contactSequence", 0, true);
    }
    
}

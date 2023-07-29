package pl.piomin.services.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
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

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Date;

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
        jdbcContactType.setKeyFields(new JdbcTypeField(Types.INTEGER, "id", Long.class, "id"));
        jdbcContactType.setValueFields(new JdbcTypeField(Types.VARCHAR, "contact_type", ContactType.class, "type"),
                new JdbcTypeField(Types.VARCHAR, "location", String.class, "location"),
                new JdbcTypeField(Types.INTEGER, "person_id", Long.class, "personId"));
        f2.setTypes(jdbcContactType);
        ccfg2.setCacheStoreFactory(f2);

        CacheConfiguration<Long, Person> ccfg = new CacheConfiguration<>("PersonCache");
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
        jdbcType.setKeyFields(new JdbcTypeField(Types.INTEGER, "id", Long.class, "id"));
        jdbcType.setValueFields(new JdbcTypeField(Types.VARCHAR, "first_name", String.class, "firstName"),
                new JdbcTypeField(Types.VARCHAR, "last_name", String.class, "lastName"),
                new JdbcTypeField(Types.VARCHAR, "gender", Gender.class, "gender"),
                new JdbcTypeField(Types.VARCHAR, "country", String.class, "country"),
                new JdbcTypeField(Types.VARCHAR, "city", String.class, "city"),
                new JdbcTypeField(Types.VARCHAR, "address", String.class, "address"),
                new JdbcTypeField(Types.DATE, "birth_date", Date.class, "birthDate"));
        f.setTypes(jdbcType);
        ccfg.setCacheStoreFactory(f);

        cfg.setCacheConfiguration(ccfg, ccfg2);
        return Ignition.start(cfg);
    }

}

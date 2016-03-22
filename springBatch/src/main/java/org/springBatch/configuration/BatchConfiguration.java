package org.springBatch.configuration;


import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springBatch.entity.Country;
import org.springBatch.listener.JobCompletionNotificationListener;
import org.springBatch.processor.CountryItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Bean
    public FlatFileItemReader<Country> reader() {
        FlatFileItemReader<Country> reader = new FlatFileItemReader<Country>();
        reader.setResource(new ClassPathResource("countries.csv"));
        reader.setLineMapper(new DefaultLineMapper<Country>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "id", "name", "currency" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Country>() {{
                setTargetType(Country.class);
            }});
        }});
        return reader;
    }

    @Bean
    public CountryItemProcessor processor() {
        return new CountryItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Country> writer(DataSource dataSource) {
        JdbcBatchItemWriter<Country> writer = new JdbcBatchItemWriter<Country>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Country>());
        writer.setSql("INSERT INTO countries (id, name, currency) VALUES (:id, :name, :currency)");
        writer.setDataSource(dataSource);
        return writer;
    }
    
    @Bean
    public Job importUserJob(DataSource dataSource) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener(dataSource))
                .flow(step1(dataSource))
                .end()
                .build();
    }

    @Bean
    public Step step1(DataSource dataSource) {
        return stepBuilderFactory.get("step1")
                .<Country, Country> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer(dataSource))
                .build();
    }
    
    @Bean
    public JobExecutionListener listener(DataSource dataSource) {
        return new JobCompletionNotificationListener(new JdbcTemplate(dataSource));
    }
    
    @Bean
  	public DataSource getDataSource() {
  	    BasicDataSource dataSource = new BasicDataSource();
  	    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
  	    dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
  	    dataSource.setUsername("root");
  	    dataSource.setPassword("");
  	    return dataSource;
  	}

}

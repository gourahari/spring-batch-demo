package com.goura.java.springbatch.config;

import com.goura.java.springbatch.entiry.Employee;
import com.goura.java.springbatch.repository.EmployeeRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    @Autowired
    private PlatformTransactionManager txnManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Bean
    public ItemReader<Employee> reader() {
        FlatFileItemReader<Employee> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("data/people-10.csv"));
        reader.setName("my-reader");
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());
        return reader;
    }

    @Bean
    public EmployeeProcessor processor() {
        return new EmployeeProcessor();
    }

    @Bean
    public RepositoryItemWriter<Employee> writer() {
        RepositoryItemWriter<Employee> writer = new RepositoryItemWriter<>();
        writer.setRepository(employeeRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step(ItemReader reader, JobRepository jobRepository) {
        return new StepBuilder("step1", jobRepository)
                .<Employee, Employee>chunk(2, txnManager)
                .reader(reader)
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job runJob(Step step, JobRepository jobRepository) {
        return new JobBuilder("batch-job", jobRepository)
                .flow(step)
                .end()
                .build();
    }

    private LineMapper<Employee> lineMapper() {
        DefaultLineMapper<Employee> mapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames("id", "firstName", "lastName", "gender", "email", "phone", "dob", "jobTitle");

        BeanWrapperFieldSetMapper<Employee> fieldMapper = new BeanWrapperFieldSetMapper<>();
        fieldMapper.setTargetType(Employee.class);

        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(fieldMapper);
        return mapper;
    }
}

package com.goura.java.springbatch.repository;

import com.goura.java.springbatch.entiry.Employee;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CassandraRepository<Employee, String> {
}

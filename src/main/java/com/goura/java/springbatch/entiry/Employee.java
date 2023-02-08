package com.goura.java.springbatch.entiry;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Date;

@Setter
@Getter
@NoArgsConstructor
@Table("employee")
public class Employee {
    @PrimaryKey
    private String id;
    @Column
    private Date dob;
    @Column
    private String email;
    @Column("firstName")
    private String firstName;
    @Column("lastName")
    private String lastName;
    @Column
    private String gender;
    @Column("jobTitle")
    private String jobTitle;
    @Column
    private String phone;
}

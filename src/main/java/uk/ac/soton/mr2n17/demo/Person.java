package uk.ac.soton.mr2n17.demo;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Person {

    @Id
    private String id;

    @Field
    @NotBlank
    @Size(max = 30)
    private String firstName;

    @Field
    @NotBlank
    @Size(max = 30)
    private String lastName;
}

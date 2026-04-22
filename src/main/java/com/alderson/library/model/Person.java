package com.alderson.library.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    @NotEmpty(message = "Name should not be empty")
    private String fullName;
    @Min(value = 0, message = "Enter a correct year")
    private int yearOfBirth;
    @OneToMany(mappedBy = "owner")
    List<Book> books;

    @PreRemove
    public void releaseBooks() {
        if (!books.isEmpty()) {
            books.stream()
                    .forEach(book -> book.setOwner(null));
        }
    }
}

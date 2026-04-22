package com.alderson.library.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {

    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty(message = "Title should not be empty")
    private String title;
    @NotEmpty(message = "Author should not be empty")
    private String author;
    @Min(value = 0, message = "Enter correct year")
    private int year;
    private LocalDate dateOfTaking;
    @Transient
    private boolean overdue;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person owner;

    public boolean isOverdue() {
        if (dateOfTaking == null) {
            return false;
        }
        LocalDate dateOfExpiring = dateOfTaking.plusDays(10);
        overdue = LocalDate.now().isAfter(dateOfExpiring);
        return overdue;
    }
}

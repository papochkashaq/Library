package com.alderson.library.controller;

import com.alderson.library.model.Person;
import com.alderson.library.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("people", personService.findAll());
        return "people/all";
    }

    @GetMapping("/new")
    public String newPersonPage(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping("/new")
    public String savePerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        try {
            personService.savePerson(person);
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("fullName", "error.person", "This person already exists");
            return "people/new";
        }
        personService.savePerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String personPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("person", personService.findPersonById(id));
        return "people/person";
    }

    @GetMapping("/edit/{id}")
    public String editPersonPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("person", personService.findPersonById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String editPerson(@PathVariable("id") Long id, @ModelAttribute("person") @Valid Person person,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        person.setId(id);
        try {
            personService.updatePerson(id, person);
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("fullName", "error.person", "This person already exists");
            return "people/edit";
        }
        return "redirect:/people";
    }

    @DeleteMapping("{id}")
    public String deletePerson(@PathVariable("id") Long id) {
        personService.deletePerson(id);
        return "redirect:/people";
    }
}

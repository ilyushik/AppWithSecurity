package com.example.secondapp.Services;

import com.example.secondapp.Model.Person;
import com.example.secondapp.Repositories.PersonRepo;
import com.example.secondapp.Security.PersonDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepo personRepo;

    public PersonDetailsService(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepo.findPersonByUsername(username);
        if (person.isEmpty()) {
            throw new  UsernameNotFoundException("Person with such name was not found");
        }
        return new PersonDetails(person.get());
    }
}

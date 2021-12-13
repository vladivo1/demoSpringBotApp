package com.example.demospringbotapp.pro;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
    public class Controller {

        private final Repository repository;

        public Controller(Repository repository) {
            this.repository = repository;
        }

        //Операция сохранения юзера в базу данных
        @PostMapping("/users")
        @ResponseStatus(HttpStatus.CREATED)
        public Employee saveEmployee(@RequestBody Employee employee) {
            return repository.save(employee);
        }

        //Получение списка юзеров
        @GetMapping("/users")
        @ResponseStatus(HttpStatus.OK)
        public List<Employee> getAllUsers() {
            return repository.findAll();
        }

        //Получения юзера по id
        @GetMapping("/users/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Employee getEmployeeById(@PathVariable Integer id) {

            Employee employee = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));

        /*if (employee.getIsDeleted()) {
            throw new EntityNotFoundException("Employee was deleted with id = " + id);
        }*/

            return employee;
        }

        //Обновление юзера
        @PutMapping("/users/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Employee refreshEmployee(@PathVariable("id") Integer id, @RequestBody Employee employee) {

            return repository.findById(id)
                    .map(entity -> {
                        entity.setName(employee.getName());
                        entity.setEmail(employee.getEmail());
                        entity.setCountry(employee.getCountry());
                        return repository.save(entity);
                    })
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));
        }

        //Удаление по id
        @DeleteMapping("/users/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void removeEmployeeById(@PathVariable Integer id) {
            //repository.deleteById(id);
            Employee employee = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));
            //employee.setIsDeleted(true);
            repository.delete(employee);//save(employee);
        }

        //Удаление всех юзеров
        @DeleteMapping("/users")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void removeAllUsers() {
            repository.deleteAll();
        }
    }

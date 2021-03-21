package com.grupo03.controller;


import com.grupo03.model.Person;
import com.grupo03.model.dao.PersonDao;

/**
 * Realiza as operações da entidade Person aplicando as regras de negócio.
 */
public class PersonController {

    /**
     * Instância da classe Dao para realizar as operações no banco.
     */
    private static final PersonDao personDao = new PersonDao();

    /**
     * Construtor sem argumento.
     */
    public PersonController() {}

    /**
     * Cadastra uma nova pessoa no banco de dados.
     *
     * @param name      o nome da pessoa.
     * @param lastname  o sobre nome da pessoa.
     * @return          true se a operação for bem sucedida.
     */
    public boolean create(String name, String lastname) {

        var result = true;

        if (name.length() < 3 || name.length() > 255) {
            result = false;
        } else if (lastname.length() < 3 || lastname.length() > 255) {
            result = false;
        }

        if (result) {
            var person = new Person(name, lastname);
            personDao.save(person);
        }

        return result;
    }

    /**
     * Consulta o total de pessoas cadastradas.
     * @return o total de pessoas cadastradas no banco de dados.
     */
    public static int getTotalPersons() {
         return personDao.getAll().size();
    }


}

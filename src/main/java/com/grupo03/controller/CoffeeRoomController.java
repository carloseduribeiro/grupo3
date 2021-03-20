package com.grupo03.controller;


import com.grupo03.model.CoffeeRoom;
import com.grupo03.model.dao.CoffeeRoomDao;

/**
 * Realiza as operações da entidade CoffeeRoom aplicando as regras de negócio.
 * @see com.grupo03.model.dao.CoffeeRoomDao
 * @see com.grupo03.model.CoffeeRoom
 *
 * @author  Carlos Eduardo Ribeiro
 * @version 1.0
 */
public class CoffeeRoomController {

    /**
     * Instancia do Dao para realizar as operações no banco.
     */
    private final CoffeeRoomDao coffeeRoomDao;

    /**
     * Construtor sem argumento.</br>
     * Instancia um objeto da classe CoffeeRoomDao para realizar as operações
     * no banco de dados.
     */
    public CoffeeRoomController() {
        this.coffeeRoomDao = new CoffeeRoomDao();
    }

    /**
     * Cadastra um novo espaço de café no banco de dados.
     *
     * @param name      o nome do espaço de café.
     * @return          true se a operação foi realizada com sucesso.
     */
    public boolean create(String name) {

        /*
         * Verifica se o nome tem pelo menos 3 caracteres ou se não passa de 255
         * que é o limite do banco:
         */
        if (name.length() < 3 || name.length() > 255) {
            return false;
        } else {
            var coffeeRoom = new CoffeeRoom(name);
            this.coffeeRoomDao.save(coffeeRoom);
            return true;
        }
    }
}

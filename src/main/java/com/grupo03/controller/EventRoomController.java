package com.grupo03.controller;

import com.grupo03.model.EventRoom;
import com.grupo03.model.dao.EventRoomDao;

/**
 * Realiza as operações da entidade EventRoom aplicando as regras de negócio.
 * @see com.grupo03.model.dao.EventRoomDao
 * @see com.grupo03.model.EventRoom
 *
 * @author  Carlos Eduardo Ribeiro
 * @version 1.0
 */
public class EventRoomController {

    /**
     * Instancia do Dao para realizar as operações no banco.
     */
    private final EventRoomDao eventRoomDao;

    /**
     * Construtor sem argumento.<br>
     * Instancia um objeto da classe EventRoomDao.
     */
    public EventRoomController() {
        this.eventRoomDao = new EventRoomDao();
    }

    /**
     * Cadastra um novo espaço de evendo no banco de dados.
     *
     * @param name      o nome do espaço de evento.
     * @param capacity  a capacidade máxima do espaço.
     * @return          true se a operação foi realizada com sucesso.
     */
    public boolean create(String name, int capacity) {

        var result = true; // Armazena o resultado da operação.

        /* Verifica se o nome tem pelo menos 3 caracteres ou se não passa de 255
         * que é o limite do banco: */
        if (name.length() < 3 || name.length() > 255)
            result = false;

        /* Verifica se a capacidade do espaço de pelo menos 2 lugares:
         * 1 aluno + 1 professor por exemplo.*/
        if (capacity < 2)
            result = false;

        // Verifica se nenhum erro foi encontrado:
        if (result) {
            var eventRoom = new EventRoom(name, capacity);
            this.eventRoomDao.save(eventRoom);
        }

        return result;
    }

}

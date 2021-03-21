package com.grupo03.controller;

import com.grupo03.model.EventRoom;
import com.grupo03.model.dao.EventRoomDao;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


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
    private static final EventRoomDao eventRoomDao = new EventRoomDao();

    /**
     * Construtor sem argumento.
     */
    public EventRoomController() {}

    /**
     * Cadastra um novo espaço de evento no banco de dados.
     *
     * @param name      o nome do espaço de evento.
     * @param capacity  a capacidade máxima do espaço.
     * @return          true se a operação foi realizada com sucesso.
     */
    public boolean create(String name, int capacity) {

        var result = true;      // Armazena o resultado da operação.

        if (name.length() < 3 || name.length() > 255) {
            result = false;
        } else if (capacity < 2) {
            result = false;
        }

        if (result) {
            var eventRoom = new EventRoom(name, capacity);
            eventRoomDao.save(eventRoom);
        }

        return result;
    }

    /**
     * Consulta e retorna a capacidade máxima de pessoas que podem ser
     * cadastradas no evento.
     * @return  a capacidade máxima do evento.
     */
    public static int getEventCapacity() {

        int lessCapacity;
        int amountRooms;

        List<EventRoom> eventRoomList = eventRoomDao.getAll();
        amountRooms = eventRoomList.size();

        Optional<EventRoom> lessCapacityRoom =
                            eventRoomList.stream().min(Comparator.comparing(EventRoom::getCapacity));
        lessCapacity = lessCapacityRoom.map(EventRoom::getCapacity).orElse(0);

         /* Menor capacidade * quantidade de salas = capacidade máxima do evento: */
        return amountRooms * lessCapacity;
    }
}
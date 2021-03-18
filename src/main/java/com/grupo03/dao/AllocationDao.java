package com.grupo03.dao;

import com.grupo03.model.CoffeeRoom;
import com.grupo03.model.EventRoom;
import com.grupo03.model.Person;
import com.grupo03.model.joins.CoffeeRoomPerson;
import com.grupo03.model.joins.EventRoomPerson;
import com.grupo03.persistence.EntityManagerProvider;

import javax.persistence.EntityManager;
import java.util.List;

/**
 *  Classe responsável por fazer a alocação de uma lista de pessoas em salas de
 *  eventos e em ambientes de café seguindo a Regra de Negócio:
 *  </br>
 *  "A diferença de pessoas em cada sala deverá ser de no máximo 1 pessoa. Para
 *  estimular a troca de conhecimentos, metade das pessoas precisam trocar de sala
 *  entre as duas etapas do treinamento."
 *  @see com.grupo03.model.CoffeeRoom
 *  @see com.grupo03.model.EventRoom;
 *  @see com.grupo03.model.Person;
 *  @see com.grupo03.model.joins.CoffeeRoomPerson;
 *  @see com.grupo03.model.joins.EventRoomPerson;
 *  @see com.grupo03.persistence.EntityManagerProvider;
 *  @see javax.persistence.EntityManager;
 *
 * {@link #allocate()} Percorre a lista de pessoas e aloca-as nas salas por etapas
 *
 * @author Carlos Eduardo Ribeiro (carloseduribeiro)
 * @author Guilherme Peyerl Florêncio (GuilhermePeyflo)
 * @author Tarcísio Nunes (tarcnux)
 * @version 2.0
 */
public class AllocationDao {

    private static final EntityManager em = EntityManagerProvider.getEntityManager();

    /**
     * Consulta a lista de pessoas cadastradas no banco de dados e faz distribuição
     * delas nas salas de evento e ambiente de café.
     */
    public static void allocate() {

        // Instancia as classes que fazem as operações no banco de dados:
        var personDao = new PersonDao();
        var eventRoomDao = new EventRoomDao();
        var coffeeRoomDao = new CoffeeRoomDao();

        // Busca e armazena a lista de pessoas cadastradas no banco:
        List<Person> personList = personDao.getAll();

        // Busca e armazena a lista de salas/espaços de café cadastradas no banco:
        List<EventRoom> eventRoomList = eventRoomDao.getAll();
        List<CoffeeRoom> coffeeRoomList = coffeeRoomDao.getAll();

        // Armazena o índice da última sala/espaço de café cadastrada:
        int lastEventRoomIndex = eventRoomList.size() - 1;
        int lastCoffeeEventRoomIndex = coffeeRoomList.size() - 1;

        // Armazena o índice das salas/espaçoa de café:
        int eventRoomIndex = 0;
        int coffeeRoomIndex = 0;

        /* Armazena uma insância das classes que representam
         a join table para fazer a associação: */
        CoffeeRoomPerson coffeeRoomPerson;
        EventRoomPerson eventRoomPerson;

        // Armazena o espaço atual na lista para fazer a associação:
        CoffeeRoom coffeeRoom;
        EventRoom eventRoom;

        // Define o assento da primeira pessoa da lista:
        // Armazena o lugar/assento da pessoa na sala:
        int seat = 1;

        // Iniciar a trasação com o banco para realizar as operações:
        em.getTransaction().begin();

        // Percorre a lista de pessoas para alocá-las nas salas:
        for (Person person : personList) {

            person.setSeat(seat);   // Atribui o lugar à pessoa.

            // As pessoas que serão alocadas nos assentos ímpares não mudarão de sala na mudança de etapas:
            if (seat % 2 == 1) {
                // Instancia a sala atal da lista para associar à pessoa:
                eventRoom = eventRoomList.get(eventRoomIndex);

                // Faz a associação da sala:
                eventRoomPerson = new EventRoomPerson(person, eventRoom, 1);
                em.merge(eventRoomPerson);
                eventRoomPerson = new EventRoomPerson(person, eventRoom, 2);
                em.merge(eventRoomPerson);

                // Instancia a sala atal da lista para associar à pessoa:
                coffeeRoom = coffeeRoomList.get(coffeeRoomIndex);

                // Faz a associação do espaço de fafé:
                coffeeRoomPerson = new CoffeeRoomPerson(person, coffeeRoom, 1);
                em.merge(coffeeRoomPerson);
                coffeeRoomPerson = new CoffeeRoomPerson(person, coffeeRoom, 2);
                em.merge(coffeeRoomPerson);

            } else {    // Pessoas no assento par troca de sala na segunda etapa:

                // Aloca a pessoa na sala na primeira etapa:
                eventRoom = eventRoomList.get(eventRoomIndex);
                eventRoomPerson = new EventRoomPerson(person, eventRoom, 1);
                em.merge(eventRoomPerson);

                // Caso for a última sala:
                EventRoom nextEventRoom;
                if (eventRoomIndex == lastEventRoomIndex) {
                    // Aloca a pessoa na segunda etapa:
                    nextEventRoom = eventRoomList.get(0);   // Instancia a próxima sala;
                    eventRoomPerson = new EventRoomPerson(person, nextEventRoom, 2);
                    em.merge(eventRoomPerson);
                } else {
                    // Aloca a pessoa na segunda etapa:
                    nextEventRoom = eventRoomList.get(eventRoomIndex + 1);  // Instancia a pŕoxima sala;
                    eventRoomPerson = new EventRoomPerson(person, nextEventRoom, 2);
                    em.merge(eventRoomPerson);
                }

                // Aloca a pessoa no espaço de café na primeira etapa:
                coffeeRoom = coffeeRoomList.get(coffeeRoomIndex);
                coffeeRoomPerson = new CoffeeRoomPerson(person, coffeeRoom, 1);
                em.merge(coffeeRoomPerson);

                // Caso for o último espaço de café:
                CoffeeRoom nextCoffeeRoom;
                if (coffeeRoomIndex == lastCoffeeEventRoomIndex) {
                    // Aloca a pessoa na segunda etapa:
                    nextCoffeeRoom = coffeeRoomList.get(0); // Instancia o próximo espaço de café;
                    coffeeRoomPerson = new CoffeeRoomPerson(person, nextCoffeeRoom, 2);
                    em.merge(coffeeRoomPerson);
                } else {
                    // Aloca a pessoa na segunda etapa:
                    nextCoffeeRoom = coffeeRoomList.get(coffeeRoomIndex + 1);   // Instancia o próximo espaço de café;
                    coffeeRoomPerson = new CoffeeRoomPerson(person, nextCoffeeRoom, 2);
                    em.merge(coffeeRoomPerson);
                }

            }   // fim else;

            // Verifica se alcançou o índice máximo da lista de espaço de café ou salas para resetar o índice:
            eventRoomIndex = eventRoomIndex < lastEventRoomIndex ? ++eventRoomIndex : 0;
            coffeeRoomIndex = coffeeRoomIndex < lastCoffeeEventRoomIndex ? ++coffeeRoomIndex : 0;

            // Seja 0, incrementa 1 para o próxima assento:
            seat = eventRoomIndex == 0 ? ++seat : seat;
        }

        // Faz o commit das operações e fecha o EntityManager:
        em.getTransaction().commit();
        em.close();

    }   // fim allocate();

}

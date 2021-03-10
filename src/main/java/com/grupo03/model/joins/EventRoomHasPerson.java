package com.grupo03.model.joins;

import com.grupo03.model.EventRoom;
import com.grupo03.model.Person;

import javax.persistence.*;

@Entity
@Table(name = "tbEventRoom_Has_Person")
public class EventRoomHasPerson {

    // identificador da relação entre a tabela Person e EventRoom
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRoomHasPerson")
    private int id;

    // Id da tabela Person
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPerson")
    private Person person;

    // Id da tabela EventRoom
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idRoom")
    private EventRoom eventRoom;

    @Column
    private int stage;


// Métodos getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public EventRoom getEventRoom() {
        return eventRoom;
    }

    public void setEventRoom(EventRoom eventRoom) {
        this.eventRoom = eventRoom;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}

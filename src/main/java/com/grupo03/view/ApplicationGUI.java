package com.grupo03.view;


import com.grupo03.controller.CoffeeRoomController;
import com.grupo03.controller.EventRoomController;
import com.grupo03.model.CoffeeRoom;
import com.grupo03.model.EventRoom;
import com.grupo03.model.Person;
import com.grupo03.model.dao.AllocationDao;
import com.grupo03.model.dao.CoffeeRoomDao;
import com.grupo03.model.dao.EventRoomDao;
import com.grupo03.model.dao.PersonDao;
import com.grupo03.persistence.EntityManagerProvider;

import java.util.*;


/**
 * Contém os métodos da interfáce do usuário.
 * @see com.grupo03.controller.EventRoomController
 *
 * {@link #start()} Método que exibe o menu de funcionalidades do sistema.
 * {@link #createPerson()} Cria uma nova pessoa e a cadastra no banco de dados.
 * {@link #createEventRoom()} Imprime o menu e obtém as informações necessárias para cadastrar um espaço de evento.
 * {@link #createCoffeeRoom()} Cria uma nova sala de café e salva no banco de dados.
 * {@link #getPersonList()} Exibe as salas de evento e café em que a pessoa selecionada foi cadastrada.
 * {@link #getEventRoomList()} Exibe todas as pessoas que estão cadastradas na sala de evento selecionada,
 * durante as etapas 1 e 2.
 * {@link #getCoffeeRoomList()} Exibe todas as pessoas que estão cadastradas na sala de café selecionada,
 * durante as etapas 1 e 2.
 * {@link #setPersonRoom()} Distribui as pessoas cadastradas no sistema nas salas de evento e ambientes de café.
 * {@link #limpar()} Mantém a exibição dos menus mais organizada,
 * para facilitar a compreensão do que está sendo exibido.
 *
 * @author Jorge Davi Navarro
 * @author Tarcísio Nunes
 * @author Carlos Eduardo Ribeiro
 * @author Guilherme Peyerl Florêncio
 */
public class ApplicationGUI {

    // Instancia um objeto da classe Scanner para entrada de dados:
    private static final Scanner ler = new Scanner(System.in);

    /**
     * Imprime o menu e obtém as informações necessárias para cadastrar um espaço de evento.
     */
    private static void createEventRoom(){
        // Instancia o controller:
        var eventRoomControler = new EventRoomController();

        // Armazenam os dados que o usuaŕio digitar:
        var input = "";

        var name = "";
        var capacity = 0;

        var opcaoMenu = "";     // Armazena a opção que o usuário digitar.
        var cancelar = false;   // falso se o usuário quiser cancelar.

        System.out.println("\n=== CADASTRO DE ESPAÇO DE EVENTO ===");
        do {
            System.out.println("Aperte ENTER para cancelar.");

            // Pede ao usuário para inserir o nome do espaço:
            do {
                System.out.print("Nome: ");
                input = ler.nextLine();

                // Verifica se o valor digitado está vazio para cancelar:
                if (input.isEmpty()) {
                    cancelar = true;
                    break;
                } else if (input.length() < 3) {    // Valor inserido muito pequeno:
                    System.out.println("\nErro: Insira um nome válido!");
                }

                name = input;
            } while (name.length() < 3);

            // Testa se o usuário quer cancelar o cadastro:
            if (cancelar) {
                System.out.println("Operação cancelada!\n");
                break;
            }

            // Pede ao usuário para inserir a capacidade máxima do espaço:
            do {
                System.out.print("Capacidade máxima: ");
                input = ler.nextLine();

                // Verifica se o valor digitado está vazio para cancelar:
                if (input.isEmpty()) {
                    cancelar = true;
                    break;
                }

                // Verifica se o valor inserido é válido:
                try {
                    // Tenta converter para int:
                    capacity = Integer.parseInt(input);

                    if (capacity <= 1) {    // Valor inserido menor que 2:
                        System.out.println("\nErro: Insira um número maior que 1!");
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Erro: Insira apenas números inteiros! Ex.: 5");
                    capacity = 0;
                }
            } while (capacity <= 1);

            // Testa se o usuário quer cancelar o cadastro:
            if (cancelar) {
                System.out.println("Operação cancelada!");
                break;
            }

            // Faz o cadastro no sistema:
            boolean eventRoomCadastrada =
                    eventRoomControler.create(name, capacity);

            if (eventRoomCadastrada) {
                System.out.println("Espaço de evento cadastrado!\n");
            }

            System.out.print("Deseja cadastrar outra espaço? (S ou N): ");
            opcaoMenu = ler.next();
            ler.nextLine();
        }while(opcaoMenu.equalsIgnoreCase("S"));
    }

    /**
     * Imprime o menu e obtém as informações necessárias para cadastrar um espaço de café.
     */
    private static void createCoffeeRoom() {

        var coffeeRoomControler = new CoffeeRoomController();

        // Armazenam os dados que o usuaŕio digitar:
        var input = "";

        var name = "";

        var opcaoMenu = "";     // Armazena a opção que o usuário digitar.
        var cancelar = false;   // falso se o usuário quiser cancelar.

        System.out.println("\n==== CADASTRO DE ESPAÇO DE CAFÉ ====");
        do {
            System.out.println("Aperte ENTER para cancelar.");

            // Pede ao usuário para inserir o nome do espaço:
            do {
                System.out.print("Nome: ");
                input = ler.nextLine();

                // Verifica se o valor digitado está vazio para cancelar:
                if (input.isEmpty()) {
                    cancelar = true;
                    break;
                } else if (input.length() < 3) {
                    System.out.println("\nErro: Insira um nome válido!");
                }

                name = input;
            } while (name.length() < 3);

            // Verifica se o usuário quer cancelar o cadastro:
            if (cancelar) {
                System.out.println("Operação cancelada!\n");
                break;
            }

            // Faz o cadastro no sistema:
            boolean coffeeRoomCadastrada =
                    coffeeRoomControler.create(name);

            if (coffeeRoomCadastrada) {
                System.out.println("Espaço de evento cadastrado!\n");
            }

            System.out.print("Deseja cadastrar outro expaço? (S ou N): ");
            opcaoMenu = ler.next();
            ler.nextLine();
        }while(opcaoMenu.equalsIgnoreCase("S"));
    }

    /**
     * Método que cria as pessoas e salva no banco
     */
    public static void createPerson(){
        Person person;
        Scanner teclado = new Scanner(System.in);


        String name,lastName,opcao;
        int capAtual;

        List<EventRoom> rooms;
        List<CoffeeRoom> coffes;
        List<Person> pessoas;


        var personController = new PersonDao();
        var roomController = new EventRoomDao();
        var coffeeController = new CoffeeRoomDao();

        pessoas = personController.getAll();
        capAtual = pessoas.size();

        rooms = roomController.getAll();
        coffes = coffeeController.getAll();

        if(rooms.size() >= 2 && coffes.size() >= 2){

            // Busca a menor capacidade de sala:
            Optional<Integer> capMax =
                    rooms.stream().map(EventRoom::getCapacity).min(Comparator.naturalOrder());

            int capacidadeMaxima = capMax.get();

            capacidadeMaxima = capacidadeMaxima * rooms.size();

            do {
                if(capAtual<capacidadeMaxima){

                    System.out.println("Capacidade disponível: " + (capacidadeMaxima - capAtual));
                    System.out.println("Insira o nome:");
                    name = teclado.nextLine();

                    System.out.println("Insira o sobrenome:");
                    lastName = teclado.nextLine();

                    System.out.println("Nome: " + name + " " + lastName);
                    person = new Person (name, lastName);
                    person.setName(name);
                    person.setLastname(lastName);
                    personController.save(person);

                    capAtual++;
                    System.out.println("Deseja inserir outra Pessoa?? S ou N");
                    opcao = teclado.next();
                    teclado.nextLine();

                    limpar();
                }

                else {
                    System.out.println("Capacidade Máxima atingida. Retornando ao menu inicial.");
                    opcao="N";
                }
            }while(opcao.equalsIgnoreCase("S"));
        }else{
            System.out.println("Cadastre pelo menos 2 salas de Evento e 2 salas de Café antes de cadastrar pessoas");
        }
        }


    /**
     * Método que retorna as salas de evento e café em que uma pessoa esta alocada
     * @throws InputMismatchException
     * @throws IndexOutOfBoundsException
     */
    public static void getPersonList(){

        Scanner teclado = new Scanner(System.in);
        var em = EntityManagerProvider.getEntityManager();
        int opcao = 0;

        var pController = new PersonDao();

        List<Person> persons;
        persons = pController.getAll();

        Person person;

        do{
            limpar();
            System.out.println("Selecione a pessoa na lista abaixo:\n\n");
            int aux=1;
            for (Person p:persons) {
                System.out.println(aux+") "+ p.getName()+" "+p.getLastname());
                aux++;
            }
            System.out.println("Digite: ");
            try {
                opcao = teclado.nextInt();
                int id;
                id = persons.get(opcao-1).getId();
                person = em.find(Person.class,id);
                System.out.println("Etapa 1"
                        +"\nSala de Evento: "+ person.getEventRoomPersonList().get(0).getEventRoom().getName()
                        +"\nSala de Café: "+ person.getCoffeeRoomPersonList().get(0).getCoffeeRoom().getName()
                        +"\nAssento: "+ person.getSeat()
                        +"\n\nEtapa 2"
                        +"\nSala de Evento: "+ person.getEventRoomPersonList().get(1).getEventRoom().getName()
                        +"\nSala de Café: "  + person.getCoffeeRoomPersonList().get(1).getCoffeeRoom().getName()
                        +"\nAssento: "+ person.getSeat()
                );

                System.out.println("\n\n\nDeseja buscar outra Pessoa?\n1)Sim\n2)Não\nDigite:");
                opcao = teclado.nextInt();
            }catch (InputMismatchException inputError){
                System.out.println("A opção selecionada não é válida! Retornando ao Menu Principal");
                opcao = 2;
            }catch (IndexOutOfBoundsException indexBound){
                if (opcao > persons.size() || opcao < 1){
                    System.out.println("O valor digitado não corresponde a uma pessoa da lista");
                }else {
                    System.out.println("A função de alocar pessoas nas salas não foi executada! Selecione a opção 7 no menu Principal");
                    opcao = 2;
                }
            }
        }while(opcao!=2);
   }

    /**
     * Método que retorna uma lista de pessoas em uma sala de evento durante cada etapa
     * @throws InputMismatchException
     * @throws IndexOutOfBoundsException
     */
    public static void getEventRoomList(){
        var em = EntityManagerProvider.getEntityManager();
        var teclado = new Scanner(System.in);
        int opcao = 0;

        var ercontroller = new EventRoomDao();

        List<EventRoom> eventrooms;
        eventrooms = ercontroller.getAll();

        EventRoom ev1;
        do{

        System.out.println("Qual sala você deseja consultar?\nDigite:");

        int aux=1;
        //imprime todas as salas na tela
        for (EventRoom ev:eventrooms
             ) {
            System.out.println(aux+")" +ev.getName());
            aux++;
        }

        System.out.println("Digite: ");

        try{
            opcao=teclado.nextInt();

            int id;

            id = eventrooms.get(opcao-1).getId();

            ev1 = em.find(EventRoom.class,id);
            //imprime as pessoas na sala
            if(ev1.getPersonList(1).size() == 0 && ev1.getPersonList(2).size() == 0 ){
                System.out.println("Selecione a opção 7 do menu Principal para executar a alocação de pessoas nas salas antes de fazer as consultas");
            }else {
                System.out.println("Etapa 1 \n\t| Nome \t\t\t\t| Assento |");
                ev1.getPersonList(1).forEach(e ->
                        System.out.println("\t| " + e.getName() + " " + e.getLastname() + " \t\t\t|" + e.getSeat() + " |"));
                System.out.println("");
                System.out.println("Etapa 2 \n\t| Nome \t\t\t\t| Assento |");
                ev1.getPersonList(2).forEach(e ->
                        System.out.println("\t| " + e.getName() + " " + e.getLastname() + " \t\t\t| " + e.getSeat() + " |"));
            }
            System.out.println("\n\n\nDeseja buscar outra sala?\n1)Sim\n2)Não\nDigite:");
            opcao= teclado.nextInt();
        // Dispara a exceção caso o valor digitado pelo usuário não seja uma das opções exibidas
        }catch (InputMismatchException inputError){
            System.out.println("A opção selecionada não é válida! Retornando ao Menu Principal");
            opcao = 2;
        // Dispara a exceção caso a opção digitada não esteja na lista de opções ou caso a lista de pessoas
        // esteja vazia por não ter sido executado o método de alocação
        }catch (IndexOutOfBoundsException indexBound){
            if (opcao > eventrooms.size() || opcao < 1){
                System.out.println("O valor digitado não corresponde a uma sala de eventos da lista");
            }else {
                System.out.println("A função de alocar pessoas nas salas não foi executada! Selecione a opção 7 no menu Principal");
                opcao = 2;
            }
        }

        }while(opcao!=2);
}


    /**
     * Método que retorna uma lista de pessoas em uma sala de café durante cada etapa
     * @throws InputMismatchException
     * @throws IndexOutOfBoundsException
     */
    public static void getCoffeeRoomList(){

        var em = EntityManagerProvider.getEntityManager();
        var teclado = new Scanner(System.in);
        int opcao = 0;

        var cfcontroller = new CoffeeRoomDao();

        List<CoffeeRoom> coffeerooms;
        coffeerooms = cfcontroller.getAll();

        CoffeeRoom ev1;
        do{

            System.out.println("Qual sala do café você deseja consultar?");

            int aux=1;
            //imprime todas as salas na tela
            for (CoffeeRoom cr:coffeerooms
            ) {
                System.out.println(aux+")" +cr.getName());
                aux++;
            }
            System.out.println("Digite: ");
            try{
                opcao=teclado.nextInt();

                int id;
                id = coffeerooms.get(opcao-1).getId();
                ev1 = em.find(CoffeeRoom.class,id);

                //imprime as pessoas na sala
                if(ev1.getPersonList(1).size() == 0 && ev1.getPersonList(2).size() == 0 ){
                    System.out.println("Selecione a opção 7 do menu Principal para executar a alocação de pessoas nas salas antes de fazer as consultas");
                }else {
                    System.out.println("Etapa 1 \n\t| Nome \t\t\t\t| Assento |");
                    ev1.getPersonList(1).forEach(e ->
                            System.out.println("\t| " + e.getName() + " " + e.getLastname() + " \t\t\t|" + e.getSeat() + " |"));
                    System.out.println("");
                    System.out.println("Etapa 2 \n\t| Nome \t\t\t\t| Assento |");
                    ev1.getPersonList(2).forEach(e ->
                            System.out.println("\t| " + e.getName() + " " + e.getLastname() + " \t\t\t| " + e.getSeat() + " |"));
                }
                System.out.println("\n\n\nDeseja buscar outra sala de café?\n1)Sim\n2)Não\nDigite:");
                opcao= teclado.nextInt();
            // Dispara a exceção caso o valor digitado pelo usuário não seja uma das opções exibidas
            }catch (InputMismatchException inputError){
                System.out.println("A opção selecionada não é válida! Retornando ao Menu Principal");
                opcao = 2;
            // Dispara a exceção caso a opção digitada não esteja na lista de opções ou caso a lista de pessoas
            // esteja vazia por não ter sido executado o método de alocação
            }catch (IndexOutOfBoundsException indexBound){
                if (opcao > coffeerooms.size() || opcao < 1){
                    System.out.println("O valor digitado não corresponde a uma sala de eventos da lista");
                }else {
                    System.out.println("A função de alocar pessoas nas salas não foi executada! Selecione a opção 7 no menu Principal");
                    opcao = 2;
                }
            }

        }while(opcao!=2);


    }

    /**
     * Método de alocação de pessoas nas salas e ambientes de café
     */
    public static void setPersonRoom(){
        /*
            Para rodar este método uma segunda vez, é necessário limpar as tabelas
            associativas antes.
            delete from tbCoffeeRoomPerson where idCoffeeRoomPerson > 0;
            SELECT * FROM prowayeventsmanager_db.tbCoffeeRoomPerson;

            delete from tbEventRoomPerson where idEventRoomPerson > 0;
            SELECT * FROM prowayeventsmanager_db.tbEventRoomPerson;
         */

        PersonDao listPerson = new PersonDao();
        EventRoomDao eventDao = new EventRoomDao();
        CoffeeRoomDao coffeeDao = new CoffeeRoomDao();
        List<CoffeeRoom> coffeeRooms = coffeeDao.getAll();
        List<EventRoom> rooms = eventDao.getAll();
        List<Person> people = listPerson.getAll();

        if (people.size() < 2 || rooms.size() < 2 || coffeeRooms.size() < 2){
            System.out.println("Cadastre pelo menos, 2 pessoas, 2 salas de Evento e 2 salas de café");
        }else{
            AllocationDao.allocate();

            System.out.println("Usuários alocados com sucesso!!");
        }
    }

    /**
     * Método que "limpa" a tela para deixar os dados em exibição mais organizados
     */
    public static void limpar(){
        for(int i=0;i<20;i++) {System.out.println("");}
    }

    /**
     * Método que inicia a aplicação exibindo o menu de funcinalidades
     */
    public static void start() {
        String op;
        Scanner teclado = new Scanner(System.in);
        System.out.println("Seja Bem vindo!");
        do {
            System.out.println("Escolha uma opção: ");
            System.out.print("" +
                    "\t1)Cadastrar Salas\n" +
                    "\t2)Cadastrar Salas de Café\n" +
                    "\t3)Cadastrar Pessoas\n" +
                    "\t4)Consultar Pessoas\n" +
                    "\t5)Consultar Salas\n" +
                    "\t6)Consultar Salas de Café\n" +
                    "\t7)Alocar pessoas as salas\n"+
                    "\t0)Sair\n\n" +
                    "Digite: ");
            op = teclado.nextLine();

            switch(op) {
                case "1":
                        createEventRoom();
                        break;

                case "2":
                        createCoffeeRoom();
                        break;

                case "3":
                        createPerson();
                        break;

                case "4":
                        getPersonList();
                        break;

                case "5":
                        getEventRoomList();
                        break;

                case "6":
                        getCoffeeRoomList();
                        break;

                case "7":
                        setPersonRoom();
                        break;

                case "0":
                        break;
                default:
                        System.out.println("Digite uma opção válida!\n");
                        break;
           }
        } while(!op.equals("0"));
        System.out.println("Obrigado por usar o sistema!");
    }

}

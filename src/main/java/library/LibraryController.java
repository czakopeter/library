package library;

import com.mysql.cj.jdbc.MysqlDataSource;
import library.domain.pojo.MembershipCard;
import library.domain.pojo.Publisher;
import library.domain.save.BookForSave;
import library.domain.save.MembershipCarForSave;
import library.domain.save.PublisherForSave;
import library.domain.save.RentForSave;
import library.domain.view.BookForView;
import library.domain.view.RentForView;
import library.repository.BookRepository;
import library.repository.MembershipCardRepository;
import library.repository.PublisherRepository;
import library.repository.RentRepository;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class LibraryController {

    private boolean run = true;
    private Reader reader;
    private PublisherRepository publisherRepository;
    private BookRepository bookRepository;
    private MembershipCardRepository membershipCardRepository;
    private RentRepository rentRepository;
    private Map<String, String> menu;

    public static void main(String[] args) {
        new LibraryController().run();
    }

    private void run() {
        init();

        while (run) {
            showMenu();
            String command = reader.getString("Command: ");
            commandProcess(command);
        }
    }

    private void commandProcess(String command) {
        switch (command) {
            case "0":
                exit();
                break;
            case "1":
                Publisher publisher = publisherRepository.save(getPublisherFromUser());
                System.out.println("ADDED PUBLISHER");
                System.out.println(publisher);
                break;
            case "2":
                List<Publisher> publishers = publisherRepository.findAll();
                System.out.println("ALL PUBLISHER");
                publishers.forEach(System.out::println);
                break;
            case "3":
                BookForView book = bookRepository.save(getBookFromUser());
                System.out.println("ADDED BOOK");
                System.out.println(book);
                break;
            case "4":
                List<BookForView> books = bookRepository.findAll();
                System.out.println("ALL BOOKS");
                books.forEach(System.out::println);
                break;
            case "5":
                int publisherId = reader.getInt("Publisher id: ", "Can not parse integer");
                List<String> titles = bookRepository.findBookTitlesByPublisherId(publisherId);
                System.out.println("TITLE OF BOOKS OF PUBLISHER WITH ID: " + publisherId);
                titles.forEach(System.out::println);
                break;
            case "6":
                MembershipCard card = membershipCardRepository.save(getCardFromUser());
                System.out.println("ADDED MEMBERSHIP CARD");
                System.out.println(card);
                break;
            case "7":
                List<MembershipCard> cards = membershipCardRepository.findAll();
                System.out.println("ALL MEMBERSHIP CARD");
                cards.forEach(System.out::println);
                break;
            case "8":
                RentForView rent = rentRepository.save(getRentFromUser());
                System.out.println("ADDED RENT");
                System.out.println(rent);
                break;
            case "9":
                List<RentForView> rents = rentRepository.findAll();
                System.out.println("ALL RENTS");
                rents.forEach(System.out::println);
                break;
            default:
                System.out.println("Invalid command!");
                break;
        }
    }

    private void showMenu() {
        menu.entrySet().forEach(entry -> System.out.println("\t" + entry.getKey() + "\t" + entry.getValue()));
    }

    private void init() {
        reader = new Reader(new Scanner(System.in));
        initRepositories();
        initMenu();
    }

    private void exit() {
        run = false;
    }

    private PublisherForSave getPublisherFromUser() {
        PublisherForSave publisher = new PublisherForSave();
        publisher.setName(reader.getString("Publisher name: "));
        publisher.setCity(reader.getString("Publisher city: "));
        return publisher;
    }

    private BookForSave getBookFromUser() {
        BookForSave book = new BookForSave();
        book.setPublisherId(reader.getInt("Publisher id: ", "Can not parse integer"));
        book.setTitle(reader.getString("Book title: "));
        return book;
    }

    private MembershipCarForSave getCardFromUser() {
        MembershipCarForSave membershipCard = new MembershipCarForSave();
        membershipCard.setName(reader.getString("Name: "));
        membershipCard.setBirthdate(reader.getDate("Birthdate: "));
        return membershipCard;
    }

    private RentForSave getRentFromUser() {
        RentForSave rent = new RentForSave();
        rent.setBookId(reader.getInt("Book id: ", "Can not parse integer"));
        rent.setMembershipCardId(reader.getInt("Membership card id: ", "Can not parse integer"));
        rent.setRentDate(reader.getDate("Rent date: "));
        return rent;
    }

    private void initMenu() {
        menu = new TreeMap<>();
        menu.put("0", "Exit");
        menu.put("1", "Create publisher");
        menu.put("2", "Print all publisher");
        menu.put("3", "Create book");
        menu.put("4", "Print all book");
        menu.put("5", "Print title of book of publisher");
        menu.put("6", "Create membership card");
        menu.put("7", "Print all membership card");
    }

    private void initRepositories() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost/library2?useSSL=false&serverTimezone=UTC");
        dataSource.setUser("root");
        dataSource.setPassword("test1234");

        bookRepository = new BookRepository(dataSource);
        publisherRepository = new PublisherRepository(dataSource);
        membershipCardRepository = new MembershipCardRepository(dataSource);
        rentRepository = new RentRepository(dataSource);
    }
}
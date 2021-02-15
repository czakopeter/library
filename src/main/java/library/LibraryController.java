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
import library.repository.jdbcTemplate.BookRepositoryWithJDBCTemplate;
import library.repository.jdbcTemplate.MembershipCardRepositoryWithJDBCTemplate;
import library.repository.jdbcTemplate.PublisherRepositoryWithJDBCTemplate;
import library.repository.jdbcTemplate.RentRepositoryWithJDBCTemplate;
import library.userInterface.MenuItem;
import library.userInterface.UserInterface;

import java.util.*;

public class LibraryController {

    private PublisherRepository publisherRepository;
    private BookRepository bookRepository;
    private MembershipCardRepository membershipCardRepository;
    private RentRepository rentRepository;
    private UserInterface userInterface;

    public static void main(String[] args) {
        new LibraryController().run();
    }

    private void run() {
        init();
        userInterface.run();
    }

    private void createPublisher() {
        Publisher publisher = publisherRepository.save(getPublisherFromUser());
        System.out.println("ADDED PUBLISHER");
        System.out.println(publisher);
    }

    private void printAllPublisher() {
        List<Publisher> publishers = publisherRepository.findAll();
        System.out.println("ALL PUBLISHER");
        publishers.forEach(System.out::println);
    }

    private void createBook() {
        BookForView book = bookRepository.save(getBookFromUser());
        System.out.println("ADDED BOOK");
        System.out.println(book);
    }

    private void printAllBook() {
        List<BookForView> books = bookRepository.findAll();
        System.out.println("ALL BOOKS");
        books.forEach(System.out::println);
    }

    private void printBookByPublisher() {
        List<String> titles = bookRepository.findBookTitlesByPublisherId(
                userInterface.getInt("Publisher id: ", "Can not parse integer"));
        System.out.println("TITLE OF BOOKS");
        titles.forEach(System.out::println);
    }

    private void createMembershipCard() {
        MembershipCard card = membershipCardRepository.save(getCardFromUser());
        System.out.println("ADDED MEMBERSHIP CARD");
        System.out.println(card);
    }

    private void printAllMembershipCard() {
        List<MembershipCard> cards = membershipCardRepository.findAll();
        System.out.println("ALL MEMBERSHIP CARD");
        cards.forEach(System.out::println);
    }

    private void createRent() {
        RentForView rent = rentRepository.save(getRentFromUser());
        System.out.println("ADDED RENT");
        System.out.println(rent);
    }

    private void printAllRent() {
        List<RentForView> rents = rentRepository.findAll();
        System.out.println("ALL RENTS");
        rents.forEach(System.out::println);
    }

    private void exit() {
        userInterface.close();
    }

    private PublisherForSave getPublisherFromUser() {
        PublisherForSave publisher = new PublisherForSave();
        publisher.setName(userInterface.getString("Publisher name: "));
        publisher.setCity(userInterface.getString("Publisher city: "));
        return publisher;
    }

    private BookForSave getBookFromUser() {
        BookForSave book = new BookForSave();
        book.setPublisherId(userInterface.getInt("Publisher id: ", "Can not parse integer"));
        book.setTitle(userInterface.getString("Book title: "));
        return book;
    }

    private MembershipCarForSave getCardFromUser() {
        MembershipCarForSave membershipCard = new MembershipCarForSave();
        membershipCard.setName(userInterface.getString("Name: "));
        membershipCard.setBirthdate(userInterface.getDate("Birthdate: "));
        return membershipCard;
    }

    private RentForSave getRentFromUser() {
        RentForSave rent = new RentForSave();
        rent.setBookId(userInterface.getInt("Book id: ", "Can not parse integer"));
        rent.setMembershipCardId(userInterface.getInt("Membership card id: ", "Can not parse integer"));
        rent.setRentDate(userInterface.getDate("Rent date: "));
        return rent;
    }

    private void init() {
        initRepositories();
        initUserInterface();
    }

    private void initUserInterface() {
        userInterface = new UserInterface(initMenu());
    }

    private Map<String, MenuItem> initMenu() {
        Map<String, MenuItem> menu = new TreeMap<>();
        menu.put("0", new MenuItem("Exit", this::exit));
        menu.put("1", new MenuItem("Create publisher", this::createPublisher));
        menu.put("2", new MenuItem("Print all publisher", this::printAllPublisher));
        menu.put("3", new MenuItem("Create book", this::createBook));
        menu.put("4", new MenuItem("Print all book", this::printAllBook));
        menu.put("5", new MenuItem("Print title of book of publisher", this::printBookByPublisher));
        menu.put("6", new MenuItem("Create membership card", this::createMembershipCard));
        menu.put("7", new MenuItem("Print all membership card", this::printAllMembershipCard));
        menu.put("8", new MenuItem("Create rent", this::createRent));
        menu.put("9", new MenuItem("Print all rent", this::printAllRent));
        return menu;
    }

    private void initRepositories() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost/library2?useSSL=false&serverTimezone=UTC");
        dataSource.setUser("root");
        dataSource.setPassword("test1234");

        bookRepository = new BookRepositoryWithJDBCTemplate(dataSource);
        publisherRepository = new PublisherRepositoryWithJDBCTemplate(dataSource);
        membershipCardRepository = new MembershipCardRepositoryWithJDBCTemplate(dataSource);
        rentRepository = new RentRepositoryWithJDBCTemplate(dataSource);

//        bookRepository = new BookRepository(dataSource);
//        publisherRepository = new PublisherRepository(dataSource);
//        membershipCardRepository = new MembershipCardRepository(dataSource);
//        rentRepository = new RentRepository(dataSource);
    }
}
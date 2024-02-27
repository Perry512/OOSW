
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

import model.Book;
import model.BookCollection;
import model.Patron;
import model.PatronCollection;


public class TestAssgn1 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String bName, author, pubYear, bStatus;

        String pName, address, city, zip, stateCode, email, pStatus, dob;

        Properties prop;
        Book book;
        BookCollection bc;
        Patron patron;
        PatronCollection pc;

        Vector<Book> bookList;
        Vector<Patron> patronList;

        boolean mach = true;
        while (mach) {
            System.out.println("1: Add a Book");
            System.out.println("2: Add a Patron");
            System.out.println("3: Search for a Book based on Title");
            System.out.println("4: Search for a Book based on Published Year");
            System.out.println("5: Search for a Patron younger than certain date");
            System.out.println("6: Search for a Patron based on Zip");

            int input = scan.nextInt();
            scan.nextLine();

            switch (input) {
                case 1:
                    System.out.println("Please enter Book name");
                    bName = scan.nextLine();
                    System.out.println("Please enter Book author");
                    author = scan.nextLine();
                    System.out.println("Please enter Book published year");
                    pubYear = scan.nextLine();

                    prop = new Properties();
                    prop.setProperty("bookTitle", bName);
                    prop.setProperty("author", author);
                    prop.setProperty("pubYear", pubYear);
                    prop.setProperty("status", "Active");

                    book = new Book(prop);
                    book.update();

                    break;
                case 2:
                    System.out.println("Please enter Patron name");
                    pName = scan.nextLine();
                    System.out.println("Please enter Patron address");
                    address = scan.nextLine();
                    System.out.println("Please enter Patron city");
                    city = scan.nextLine();
                    System.out.println("Please enter Patron zip");
                    zip = scan.nextLine();
                    System.out.println("Please enter Patron state code");
                    stateCode = scan.nextLine();
                    System.out.println("Please enter Patron email");
                    email = scan.nextLine();
                    System.out.println("Please enter Patron date of birth");
                    dob = scan.nextLine();

                    prop = new Properties();
                    prop.setProperty("name", pName);
                    prop.setProperty("address", address);
                    prop.setProperty("city", city);
                    prop.setProperty("zip", zip);
                    prop.setProperty("stateCode", stateCode);
                    prop.setProperty("email", email);
                    prop.setProperty("dateOfBirth", dob);
                    prop.setProperty("status", "Active");

                    patron = new Patron(prop);
                    patron.update();
                    break;
                case 3:
                    System.out.println("Please enter book title (don't need full title)");
                    bName = scan.nextLine();

                    bc = new BookCollection();
                    bookList = bc.findBookWithTitleLike(bName);
                    printBooks(bookList);
                    break;
                case 4:
                    System.out.println("Please enter year to find books that were published before that year");
                    pubYear = scan.nextLine();

                    bc = new BookCollection();
                    bookList = bc.findBooksOlderThan(pubYear);
                    printBooks(bookList);
                    break;
                case 5:
                    System.out.println("Please enter date yyyy-mm-dd to find patron younger than this date");
                    pubYear = scan.nextLine();

                    pc = new PatronCollection();
                    patronList = pc.findPatronsYoungerThan(pubYear);
                    printPatrons(patronList);
                    break;
                case 6:
                    System.out.println("Please enter zipcode");
                    zip = scan.nextLine();

                    pc = new PatronCollection();
                    patronList = pc.findPatronsAtZipCode(zip);
                    printPatrons(patronList);
                    break;
                case 7:
                    mach = false;
                    break;
            }

        }
    }

    private static void printBooks(Vector<Book> bookList){
        for (Book book : bookList) {
            System.out.println(book);
        }
    }

    private static void printPatrons(Vector<Patron> patronList){
        for (Patron patron : patronList) {
            System.out.println(patron);
        }
    }
}

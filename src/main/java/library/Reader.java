package library;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

public class Reader {
    Scanner sc;

    public Reader(Scanner sc) {
        this.sc = sc;
    }

    public String getString(String question) {
        System.out.print(question);
        return sc.nextLine();
    }

    public int getInt(String question, String invalid) {
        int result = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(question);
            String line = sc.nextLine();
            try {
                result = Integer.parseInt(line);
                valid = true;
            } catch (NumberFormatException ex) {
                System.out.println(invalid);
            }
        }
        return result;
    }

    public LocalDate getDate(String question) {
        LocalDate result = LocalDate.now();
        boolean valid = false;
        while (!valid) {
            System.out.println(question);
            String year = getString("Year: ");
            String month = getString("Month: ");
            String day = getString("Day: ");
            try {
                result = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                valid = true;
            } catch (NumberFormatException ex) {
                System.out.printf(
                        "Some of given data can not parse to integer. Year: %s, month: %s, day: %s",
                        year, month, day);
            } catch (DateTimeException ex) {
                System.out.printf(
                        "Can not parse date. Year: %s, month: %s, day: %s",
                        year, month, day);
            }
        }
        return result;
    }
}

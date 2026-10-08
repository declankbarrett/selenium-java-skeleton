package support;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** Seed data and matching rules shared by the users search/filter UI and API steps. */
public final class UserSearchData {

    /** Users created by create-db.sql. Other runs may add rows, so tests never assert totals. */
    public static final List<String> SEED_EMAILS =
            List.of("manager@test.com", "tester@test.com", "developer@test.com", "business@test.com");

    public static final String ALL_POSITIONS = "All positions";
    public static final String NO_RESULTS_MESSAGE = "No users match your search";
    public static final String CREATED_EMAIL_PREFIX = "qa.search.";

    private UserSearchData() {}

    /** AC1/AC2: case-insensitive partial match on name, surname, email or "name surname". */
    public static boolean matchesSearch(String name, String surname, String email, String search) {
        String needle = search.toLowerCase(Locale.ROOT);
        String fullName = name + " " + surname;
        return contains(name, needle) || contains(surname, needle) || contains(email, needle)
                || contains(fullName, needle);
    }

    /** UI rows render "name surname" in one cell, which covers name, surname and full name. */
    public static boolean rowMatchesSearch(String fullName, String email, String search) {
        String needle = search.toLowerCase(Locale.ROOT);
        return contains(fullName, needle) || contains(email, needle);
    }

    public static boolean userMatchesSearch(Map<String, Object> user, String search) {
        return matchesSearch(field(user, "name"), field(user, "surname"), field(user, "email"), search);
    }

    public static String field(Map<String, Object> user, String key) {
        return Objects.toString(user.get(key), "");
    }

    private static boolean contains(String value, String lowerNeedle) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(lowerNeedle);
    }
}

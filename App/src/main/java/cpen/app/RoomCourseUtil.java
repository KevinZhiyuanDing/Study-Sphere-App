package cpen.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * A utility class that provides lists of default room locations and its corresponding abbreviation.

 */
public class RoomCourseUtil {
    public static List<String> locationList = new ArrayList<>(
        Arrays.asList("Ivring K Barber Library", "MAA", "Walter C Koerner Library", "Woodward Library", "Research Commons", "Any Location"));
    public static Map<String, String> locationToAbbrev = new HashMap<>();
    static {
        locationToAbbrev.put("Ivring K Barber Library", "IKB");
        locationToAbbrev.put("MAA", "MAA");
        locationToAbbrev.put("Walter C Koerner Library", "Koerner");
        locationToAbbrev.put("Woodward Library", "Woodward");
        locationToAbbrev.put("Research Commons", "Research");
        locationToAbbrev.put("Any Location", "");
    }
    public static List<String> courseList = new ArrayList<>(
        Arrays.asList("APSC", "MATH", "PHYS", "CHEM", "MECH", "ELEC", "CPEN"));

}

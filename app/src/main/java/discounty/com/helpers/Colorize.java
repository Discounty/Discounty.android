package discounty.com.helpers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Yakovenko Denis
 * @version 0.0.1
 *
 *
 * Provides one static method for getting a hex representation of a color,
 * and a helper method for checking if the color of the specified name is
 * actually available in the class.
 *  <p>
 * Usage:
 *      1. {@code Colorize.get()} - returns a random hex color
 *      2. {@code Colorize.get(String)} - returns a hex color of a spacified name.
 *         If the color doesn't exist in the class - returns {@code null}.
 *      3. {@code Colorize.checkColorAvailability(String)} - returns a {@code boolean} value that
 *         represents color's availability: <tt>true</tt> - if available, <tt>false</tt> - if not.
 * </p>
 */
public class Colorize {

    /**
     * Contains all the colors of the class (with the names of each color).
     */
    private static final Map<String, String> colorsMap = new LinkedHashMap<String, String>() {
        {
            put("almostblack", "#161616");
            put("blue", "#0000FF");
            put("blueviolet", "#8A2BE2");
            put("brown", "#A52A2A");
            put("burlywood", "#DEB887");
            put("cadetblue", "#5F9EA0");
            put("chartreuse", "#7FFF00");
            put("chocolate", "#D2691E");
            put("coral", "#FF7F50");
            put("cornflowerblue", "#6495ED");
            put("crimson", "#DC143C");
            put("darkblue", "#00008B");
            put("darkcyan", "#008B8B");
            put("darkgoldenrod", "#B8860B");
            put("darkgrey", "#A9A9A9");
            put("darkgreen", "#006400");
            put("darkkhaki", "#BDB76B");
            put("darkmagenta", "#8B008B");
            put("darkolivegreen", "#556B2F");
            put("darkorange", "#FF8C00");
            put("darkorchid", "#9932CC");
            put("darkred", "#8B0000");
            put("darksalmon", "#E9967A");
            put("darkseagreen", "#8FBC8F");
            put("darkslateblue", "#483D8B");
            put("darkslategray", "#2F4F4F");
            put("darkslategrey", "#2F4F4F");
            put("darkturquoise", "#00CED1");
            put("darkviolet", "#9400D3");
            put("deeppink", "#FF1493");
            put("deepskyblue", "#00BFFF");
            put("dimgray", "#696969");
            put("dimgrey", "#696969");
            put("dodgerblue", "#1E90FF");
            put("firebrick", "#B22222");
            put("forestgreen", "#228B22");
            put("fuchsia", "#FF00FF");
            put("gold", "#FFD700");
            put("goldenrod", "#DAA520");
            put("gray", "#808080");
            put("grey", "#808080");
            put("green", "#008000");
            put("greenyellow", "#ADFF2F");
            put("hotpink", "#FF69B4");
            put("indianred", "#CD5C5C");
            put("indigo", "#4B0082");
            put("khaki", "#F0E68C");
            put("lavender", "#E6E6FA");
            put("lawngreen", "#7CFC00");
            put("lightblue", "#ADD8E6");
            put("lightcoral", "#F08080");
            put("lightgrey", "#D3D3D3");
            put("lightgreen", "#90EE90");
            put("lightpink", "#FFB6C1");
            put("lightsalmon", "#FFA07A");
            put("lightseagreen", "#20B2AA");
            put("lightskyblue", "#87CEFA");
            put("lightslategray", "#778899");
            put("lightslategrey", "#778899");
            put("lightsteelblue", "#B0C4DE");
            put("lime", "#00FF00");
            put("limegreen", "#32CD32");
            put("magenta", "#FF00FF");
            put("maroon", "#800000");
            put("mediumaquamarine", "#66CDAA");
            put("mediumblue", "#0000CD");
            put("mediumorchid", "#BA55D3");
            put("mediumpurple", "#9370D8");
            put("mediumseagreen", "#3CB371");
            put("mediumslateblue", "#7B68EE");
            put("mediumspringgreen", "#00FA9A");
            put("mediumturquoise", "#48D1CC");
            put("mediumvioletred", "#C71585");
            put("midnightblue", "#191970");
            put("navy", "#000080");
            put("olive", "#808000");
            put("olivedrab", "#6B8E23");
            put("orange", "#FFA500");
            put("orangered", "#FF4500");
            put("orchid", "#DA70D6");
            put("palegreen", "#98FB98");
            put("palevioletred", "#D87093");
            put("peru", "#CD853F");
            put("pink", "#FFC0CB");
            put("plum", "#DDA0DD");
            put("powderblue", "#B0E0E6");
            put("purple", "#800080");
            put("rebeccapurple", "#663399");
            put("red", "#FF0000");
            put("rosybrown", "#BC8F8F");
            put("royalblue", "#4169E1");
            put("saddlebrown", "#8B4513");
            put("salmon", "#FA8072");
            put("sandybrown", "#F4A460");
            put("seagreen", "#2E8B57");
            put("sienna", "#A0522D");
            put("skyblue", "#87CEEB");
            put("slateblue", "#6A5ACD");
            put("slategray", "#708090");
            put("slategrey", "#708090");
            put("springgreen", "#00FF7F");
            put("steelblue", "#4682B4");
            put("tan", "#D2B48C");
            put("teal", "#008080");
            put("thistle", "#D8BFD8");
            put("tomato", "#FF6347");
            put("turquoise", "#40E0D0");
            put("violet", "#EE82EE");
            put("yellow", "#FFFF00");
            put("yellowgreen", "#9ACD32");
        }
    };

    /**
     * Contains all the colors of the class (without their names, just the hex values).
     */
    private static final Object[] colorsArray = colorsMap.values().toArray();

    /**
     * Generates random indexes to get from the @colorsArray
     */
    private static final Random generator = new Random();

    /**
     * Returns a random hex color string.
     *
     * @return Random hex <tt>String</tt> color representation.
     */
    public static String get() {
        return (String) colorsArray[generator.nextInt(colorsArray.length)];
    }

    /**
     * Returns a hex color string
     *
     * @param colorName the name of the needed color.
     * @return Hex <tt>String</tt> color representation if it exists,
     *         and {@code null} if the color doesn't exist in the class.
     */
    public static String get(String colorName) {
        return colorsMap.get(colorName);
    }

    /**
     * Allows to check if the color by the specified name is
     * available.
     *
     * @param colorName the name of the color that needs to be checked.
     * @return <tt>true</tt> if color exists, <tt>false</tt> if color doesn't exist.
     */
    public static boolean checkColorAvailability(String colorName) {
        return colorsMap.get(colorName) != null;
    }
}

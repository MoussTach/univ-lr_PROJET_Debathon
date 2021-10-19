package fr.univlr.debathon.custom.fixer;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * {@link TooltipDefaultFixer}
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a>
 */
public class TooltipDefaultFixer {
    /**
     * Returns true if successful.
     * Current defaults are 1000, 5000, 200;
     *
     * @param openDelay - long - opening delay of the tooltip
     * @param visibleDuration - long - visibility delay of the tooltip
     * @param  closeDelay - long - closing delay of the tooltip
     */
    @SuppressWarnings({"SameReturnValue"})
    public static void setTooltipTimers(long openDelay, long visibleDuration, long closeDelay) {
        try {
            Field f = Tooltip.class.getDeclaredField("BEHAVIOR");
            f.setAccessible(true);


            Class[] classes = Tooltip.class.getDeclaredClasses();
            for (Class clazz : classes)
                if (clazz.getName().equals("javafx.scene.control.Tooltip$TooltipBehavior")) {
                    Constructor ctor = clazz.getDeclaredConstructor(Duration.class, Duration.class, Duration.class, boolean.class);
                    ctor.setAccessible(true);
                    Object tooltipBehavior = ctor.newInstance(new Duration(openDelay), new Duration(visibleDuration), new Duration(closeDelay), false);
                    f.set(null, tooltipBehavior);
                    break;
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

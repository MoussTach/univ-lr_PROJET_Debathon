package com.sun.javafx.scene.control.skin;

import javafx.scene.control.Pagination;
import javafx.scene.control.skin.PaginationSkin;
import javafx.util.Duration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Custom class, used to replace the default {@link PaginationSkin} to change some variable.
 *
 * @author Gaetan Brenckle
 */
public class CustomPaginationSkin extends PaginationSkin {

    /**
     * Constructor changed.
     * Create this constructor to override the {@link PaginationSkin} DURATION field with reflection.
     * The target is to remove the animation of the transition between two pagination, but you can only change the duration,
     * and it doesn't work with the changes before printing.
     *
     * @author Gaetan Brenckle
     *
     * @param pagination - {@link Pagination} - default field used for the heritage.
     */
    public CustomPaginationSkin(final Pagination pagination) {
        super(pagination);

        try {
            Field f = PaginationSkin.class.getDeclaredField("DURATION");
            f.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);

            f.set(null, Duration.millis(0.09));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}


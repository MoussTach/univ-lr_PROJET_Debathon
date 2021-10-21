package fr.univlr.debathon.language;

import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * Class used to have a dynamic reloading of ui text with the usage of property.
 * This class stock a list of {@literal ObjectProperty<ResourceBundle>} that the ressourceBundle associed will be update
 * when the current language is changed.
 * This class is a singleton-class, use LanguageBundle#getInstance() to create and use a instance.
 *
 * To use this class, you need to create a local variable of a {@literal ObjectProperty<ResourceBundle>} to stock the
 * result of the LanguageBundle#bindResourceBundle(String) method.
 *
 *</pre>
 * example:
 * <pre>
 *{@code private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.lg_basic");}
 * </pre>
 * <pre>
 * To bind a property and allow a dynamic update when the language change, you must create a listener of the local
 * variable created before in the constructor.
 *
 * </pre>
 * example:
 * <pre>
 *{@code
 *      YourClass() {
 *          this.resBundle_.addListener(observable -> {
 *
 *          myLabel_property.set(this.resBundle_.get().getString("key_of_myLabel"));
 *          myText_property.set(this.resBundle_.get().getString("key_of_myText"));
 *          ...
 *          });
 *      }
 *}
 * </pre>
 * <pre>
 * Now, when you call LanguageBundle#setCurrLocale(languages) with a new language, your property will be updated.
 * </pre>
 *
 * <h1>Warning</h1>
 * <pre>
 * With only the example, your property will be only updated with the next language change and not at their default state.
 * you need to define your property to get the string returned with the actual language to avoid a empty value when you
 * create your property.
 *
 * </pre>
 * example:
 * <pre>
 * {@code
 *      private final StringProperty myLabel_property = new SimpleStringProperty(this.resBundle_.get().getString("key_of_myLabel"));
 *      private final StringProperty myText_property = new SimpleStringProperty(this.resBundle_.get().getString("key_of_myText"));
 *      ...
 *}
 *</pre>
 *
 * @author Gaetan Brenckle
 */
public class LanguageBundle {

    /**
     * Enum for the language supported with your program
     *
     * @author Gaetan Brenckle
     */
    public enum languages {
        FR(Locale.FRANCE, "Français"),
        US(Locale.US, "English");

        private final Locale locale_;
        private final String langName_;

        languages(Locale locale, String langName) {
            this.locale_ = locale;
            this.langName_ = langName;
        }

        Locale getLocale() {
            return this.locale_;
        }

        @Override
        public String toString() {
            return this.langName_;
        }
    }

    private static final CustomLogger LOGGER = CustomLogger.create(LanguageBundle.class.getName());
    private static LanguageBundle INSTANCE;
    private languages currLanguage_ = languages.FR;
    private final ObjectProperty<Locale> currentLocale_ = new SimpleObjectProperty<>(currLanguage_.getLocale());
    private final Map<String, ObjectProperty<ResourceBundle>> mapRessourceBundle_ = new ConcurrentHashMap<>();

    /**
     * Getter of the current instance of the class {@link LanguageBundle}.
     * If the current instance is null, a new instance is create.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link LanguageBundle} - return the current instance of LanguageBundle.
     */
    public static LanguageBundle getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LanguageBundle();
        }
        return INSTANCE;
    }

    /**
     * Default constructor.
     * Create a listener to update each RessourceBundle of the map when a new language is set.
     *
     * @author Gaetan Brenckle
     */
    private LanguageBundle() {
        this.currentLocale_.addListener((observable, oldValue, newValue) -> {
            for (Map.Entry<String, ObjectProperty<ResourceBundle>> entry : this.mapRessourceBundle_.entrySet()) {
                String nameBundle = entry.getKey();
                ObjectProperty<ResourceBundle> forBundle = entry.getValue();
                forBundle.set(ResourceBundle.getBundle(nameBundle, this.currentLocale_.get()));
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("change the language. %s -> %s", oldValue.getCountry(), newValue.getCountry()));
            }
        });
    }

    /**
     * This function is used to create a new property of a RessourceBundle.
     * If a bundle is already created with the given parameter, the method doesn't create a newer but return the property
     * stocked on the map with the same key as the parameter.
     *
     * @author Gaetan Brenckle
     *
     * @param baseName - {@link String} - base name of the properties file, same parameter as the {@link ResourceBundle#getBundle(String)} method.
     * @return {@link ObjectProperty}{@literal <}{@link ResourceBundle}{@literal >}- Return a new property of the ressourceBundle.
     */
    public ObjectProperty<ResourceBundle> bindResourceBundle(String baseName) {
        ObjectProperty<ResourceBundle> resBundle;

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("[public][method] usage of LanguageBundle.bindResourceBundle(%s).", baseName));
        }
        if (!this.mapRessourceBundle_.containsKey(baseName)) {
            resBundle = new SimpleObjectProperty<>(ResourceBundle.getBundle(baseName, this.currentLocale_.get()));
            this.mapRessourceBundle_.putIfAbsent(baseName, resBundle);
        } else {
            resBundle = this.mapRessourceBundle_.get(baseName);
        }
        return resBundle;
    }

    /**
     * Method used to unbind a ressourceBundle property.
     *
     * @author Gaetan Brenckle
     *
     * @param propertyValue - the property ressourceBundle that you want to unbind.
     */
    public void unbindRessourceBundle(ObjectProperty<ResourceBundle> propertyValue) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("[public][method] usage of LanguageBundle.unbindRessourceBundle(%s).", propertyValue.toString()));
        }
        this.mapRessourceBundle_.remove((propertyValue.get()).getBaseBundleName(), propertyValue);
    }

    /**
     * Getter on the current language.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link languages} - return the current language defined on your {@link LanguageBundle}
     */
    public languages getCurrLanguage() {
        return this.currLanguage_;
    }

    /**
     * Getter of the current Locale class.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link Locale} - return the current Locale class.
     */
    public Locale getCurrLocale() {
        return this.currentLocale_.get();
    }

    /**
     * Method used to change the current language.
     *
     * @author Gaetan Brenckle
     *
     * @param lang {@link languages} - the newer language you want to applied.
     */
    public void setCurrLocale(languages lang) {
        this.currLanguage_ = lang;
        this.currentLocale_.set(lang.getLocale());
    }

    /**
     * Return the size of the map of ressourceBundle updated when the language is changed.
     * Mostly used on test.
     *
     * @author Gaetan Brenckle
     *
     * @return int - the size of the map
     */
    public int mapSize() {
        return this.mapRessourceBundle_.size();
    }
}
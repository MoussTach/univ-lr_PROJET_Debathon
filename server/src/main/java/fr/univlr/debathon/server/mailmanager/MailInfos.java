package fr.univlr.debathon.server.mailmanager;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

public class MailInfos {

    private final BooleanProperty isSend = new SimpleBooleanProperty(false);

    private final StringProperty subject = new SimpleStringProperty("none");
    private final StringProperty body = new SimpleStringProperty("Send automatically.");

    private final ListProperty<String> to = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<String> bcc = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty from = new SimpleStringProperty("");

    private final MailData data;

    public MailInfos(MailData data) {
        this.data = data;

        this.from.set(MailManager.MAILDATA.get("From_User"));
    }


    /**
     * getter of the variable subject
     *
     * @author Gaetan Brenckle
     *
     * @return {@link String} - return the variable subject
     */
    public String getSubject() {
        return subject.get();
    }

    /**
     * getter of the variable subject
     *
     * @author Gaetan Brenckle
     *
     * @return {@link String} - return the variable subject
     */
    public String getBody() {
        return body.get();
    }

    /**
     * getter of the variable from
     *
     * @author Gaetan Brenckle
     *
     * @return {@link String} - return the variable from
     */
    public String getFrom() {
        return from.get();
    }

    /**
     * getter of the variable data
     *
     * @author Gaetan Brenckle
     *
     * @return {@link MailData} - return the variable data
     */
    public MailData getData() {
        return data;
    }


    /**
     * Setter for the variable subject.
     *
     * @param subject - {@link String} - subject of the job class.
     * @return {@link MailInfos} - builder pattern
     */
    public MailInfos setSubject(String subject) {
        this.subject.set(subject);
        return this;
    }

    /**
     * Setter for the variable body.
     *
     * @param body - {@link String} - body of the job class.
     * @return {@link MailInfos} - builder pattern
     */
    public MailInfos setBody(String body) {
        this.body.set(body);
        return this;
    }


    /**
     * Setter for the variable from.
     *
     * @param from - {@link String} - from of the job class.
     * @return {@link MailInfos} - builder pattern
     */
    public MailInfos setFrom(String from) {
        this.from.set(from);
        return this;
    }


    /**
     * Property of the variable isSend.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link BooleanProperty} - return the property of the variable isSend.
     */
    public BooleanProperty isSendProperty() {
        return isSend;
    }

    /**
     * Property of the variable subject.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable subject.
     */
    public StringProperty subjectProperty() {
        return subject;
    }

    /**
     * Property of the variable subject.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable subject.
     */
    public StringProperty bodyProperty() {
        return body;
    }

    /**
     * Property of the variable to.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable to.
     */
    public ListProperty<String> toProperty() {
        return to;
    }

    /**
     * Property of the variable bcc.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable bcc.
     */
    public ListProperty<String> bccProperty() {
        return bcc;
    }

    /**
     * Property of the variable from.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable from.
     */
    public StringProperty fromProperty() {
        return from;
    }
}
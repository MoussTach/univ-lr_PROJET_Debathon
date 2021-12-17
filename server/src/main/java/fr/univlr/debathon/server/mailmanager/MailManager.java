package fr.univlr.debathon.server.mailmanager;

import fr.univlr.debathon.log.generate.CustomLogger;
import fr.univlr.debathon.taskmanager.TaskArray;
import fr.univlr.debathon.taskmanager.TaskManager;
import fr.univlr.debathon.taskmanager.Task_Custom;
import fr.univlr.debathon.taskmanager.ThreadArray;
import jakarta.mail.internet.InternetAddress;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.util.Pair;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailManager extends TaskManager {

    public static final Map<String, String> MAILDATA = new HashMap<>() {{
        put("From_User", "debathonCDIJ-norepl@outlook.com");
        put("From_Password", "92wBz&Z??b&1GrtY");
        put("AInfos_Host", "smtp-mail.outlook.com");
        put("AInfos_Port", "587");
    }};

    private static final CustomLogger LOGGER = CustomLogger.create(MailManager.class.getName());

    private final List<MailData> mailWaitConnecting = new ArrayList<>();
    private final ListProperty<MailData> mailListing = new SimpleListProperty<>(FXCollections.observableArrayList(
            mail -> new Observable[] {mail.getInfos().isSendProperty()}
    ));
    private final BooleanProperty isConnected = new SimpleBooleanProperty(true);


    public MailManager() {

        ChangeListener<Boolean> listener_isConnected = (observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                sendMails(mailWaitConnecting);
            }
        };
        isConnected.addListener(listener_isConnected);
    }


    /**
     * test if the internet is reachable.
     *
     * @author Gaetan Brenckle
     *
     * @param retry - boolean - a indicator if the task should be retry after a fail
     * @return {@link Task_Custom} return a task that can execute the test
     */
    public Task_Custom<?> test_Internet(boolean retry) {
        return new Task_Custom<Boolean>("ping internet", retry) {
            @Override
            protected Boolean call_Task() throws IOException {

                try {
                    //ping google.com
                    InetAddress ip = InetAddress.getByAddress(new byte[] { (byte)92, (byte)184, (byte)110, (byte)89 });

                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("[Task][ping internet]");
                    }

                    if (!ip.isReachable(5000)) {
                        throw new IOException("Adresse not reachable");
                    }

                    isConnected.set(true);
                } catch (IOException e) {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn(String.format("[Task][ping internet] - [%s]", e.getMessage()));
                    }

                    isConnected.set(false);
                    throw e;
                }
                return true;
            }
        };
    }

    /**
     * test if the sended address is valid or not.
     *
     * @author Gaetan Brenckle
     *
     * @param mailToTest - {@link String} - test this adr mail
     * @return {@link Task_Custom} return a task that can execute the test
     */
    public Task_Custom<?> test_AdrMail(String mailToTest) {
        return new Task_Custom<Void>("test the validity of the email destination") {
            @Override
            protected Void call_Task() throws Exception {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("[Task][test the validity of the email destination]");
                }
                InternetAddress emailAddr = new InternetAddress(mailToTest);
                emailAddr.validate();
                return null;
            }
        };
    }

    /**
     * Add a task used to send a mail on the last TaskArray.
     *
     * @author Gaetan Brenckle
     *
     * @param mails - {@link List} - list of mail
     * @return - {@link ThreadArray} - return the current array of task
     */
    public ThreadArray<?> sendMails(List<MailData> mails) {
        boolean canBeSended = false;
        ThreadArray<?> arr = currentTask.get();

        if (mails != null) {

            if (arr == null) {
                arr = new TaskArray<>(ThreadArray.ExecutionType.SEQUENTIAL);

                Task_Custom<?> task_pingInternet = test_Internet(true)
                        .setStepDurationOnFail(Duration.ofSeconds(5))
                        .setMaximumDurationOnFail(Duration.ofSeconds(60));

                arr.addTask(new Pair(task_pingInternet, new TaskArray<>(ThreadArray.ExecutionType.END)));
                canBeSended = true;
            }

            if (isConnected.get() || canBeSended) {
                for (MailData mail : mails) {
                    if (mail != null) {
                        arr.addTask(new Pair(mail.buildMail(), new TaskArray<>(ThreadArray.ExecutionType.END)));
                        if (mailListing.stream().noneMatch(mailData -> mailData.equals(mail))) {
                            mailListing.add(mail);
                        }

                        mailWaitConnecting.remove(mail);
                    }
                }

            } else {
                mailWaitConnecting.addAll(mails);
                mails.forEach(mail -> {
                    if (mail != null) {
                        if (mailListing.stream().noneMatch(mailData -> mailData.equals(mail))) {
                            mailListing.add(mail);
                        }
                    }
                });
            }

            this.addArray(arr);
        }
        return arr;
    }


    /**
     * getter of the list of waiting mails
     *
     * @author Gaetan Brenckle
     *
     * @return {@link List} - return list of waiting mail
     */
    public List<MailData> getMailWaitConnecting() {
        return mailWaitConnecting;
    }

    /**
     * getter of the list of mail sended
     *
     * @author Gaetan Brenckle
     *
     * @return {@link List} - return list of mail sended
     */
    public List<MailData> getMailListing() {
        return mailListing;
    }


    /**
     * Property of the variable mailListing.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable mailListing.
     */
    public ListProperty<MailData> mailListingProperty() {
        return mailListing;
    }

    /**
     * Property of the variable isConnected.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link BooleanProperty} - return the property of the variable isConnected.
     */
    public BooleanProperty isConnectedProperty() {
        return isConnected;
    }
}

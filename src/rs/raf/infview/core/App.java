package rs.raf.infview.core;

import rs.raf.infview.util.io.hasher.Hasher;
import rs.raf.infview.util.io.hasher.SimpleHasher;
import rs.raf.infview.util.io.orm.AbstractORM;
import rs.raf.infview.util.io.orm.microsoft.MicrosoftORM;
import rs.raf.infview.view.AbstractUI;
import rs.raf.infview.view.swing.SwingUI;
import static rs.raf.infview.core.App.Extension.*;

public class App {

    public enum Extension {
        LOG("InfViewLog", ".ivl"),
        CONFIG("InfViewConfiguration", ".ivc"),
        RESOURCE("InfViewResource", ".ivr");

        private final String name, suffix;

        Extension(String name, String suffix) {
            this.name = name;
            this.suffix = suffix;
        }

        public String getName() {
            return name;
        }

        public String getSuffix() {
            return suffix;
        }
    }

    public static final boolean DEBUG = true;

    public static final String VERSION = "0.0.4";

    public static final RuntimeConfig RUNTIME = new RuntimeConfig();

    public static final Hasher HASHER = new SimpleHasher();

    public static final AbstractUI UI = new SwingUI();
    public static final AbstractORM ORM = new MicrosoftORM();

    public static final int COMMAND_QUEUE_SIZE = 10;

    public static final String LOG_PATH = "log" + LOG.getSuffix();
    public static final String CONFIG_PATH = "config" + CONFIG.getSuffix();
    public static final String RESOURCE_PATH = "schema" + RESOURCE.getSuffix();

    public static final String DEFAULT_HOME_PATH = ".";
    public static final String DEFAULT_LANGUAGE = "EN";
    public static final int DEFAULT_BLOCK_SIZE = 10;

    // See all reported errors at https://lazarjelic.com/ecloga/projects/software_admin/report_show.php
    public static final String REPORT_API_URL = "https://lazarjelic.com/ecloga/projects/software_admin/report_add.php";
    public static final String REPORT_API_KEY = "INFVIEW2019RAF";

    public static final String ACTIVATION_URL = "https://lazarjelic.com/ecloga/projects/software_admin/license_activate.php";
    public static final String LOGIN_URL = "https://lazarjelic.com/ecloga/projects/software_admin/user_login.php";
    public static final String SESSION_URL = "https://lazarjelic.com/ecloga/projects/software_admin/user_session.php";
    public static final String HELP_URL = "https://lazarjelic.com/ecloga/projects/software_admin/help.php";
}
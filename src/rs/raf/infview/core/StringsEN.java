package rs.raf.infview.core;

class StringsEN extends Strings {

    StringsEN() {
        super.APP_NAME = "InfView";

        super.FRAME_MAIN = "InfView";
        super.FRAME_ABOUT = "About";
        super.FRAME_SETTINGS = "Settings";
        super.FRAME_LOGIN = "Login";
        super.FRAME_EDITOR = "Editor";
        super.FRAME_RELATION = "Relation";
        super.FRAME_RECORD = "Record";
        super.FRAME_TRASH = "Trash";
        super.FRAME_RESULT = "Result";

        super.MENU_FILE = "File";
        super.MENU_EDIT = "Edit";
        super.MENU_SETTINGS = "Settings";
        super.MENU_HELP = "Help";
        super.MENU_NEW = "New";
        super.MENU_OPEN = "Open";
        super.MENU_SAVE = "Save";
        super.MENU_SAVE_AS = "Save As...";
        super.MENU_EXIT = "Exit";
        super.MENU_UNDO = "Undo";
        super.MENU_REDO = "Redo";
        super.MENU_CUT = "Cut";
        super.MENU_COPY = "Copy";
        super.MENU_PASTE = "Paste";
        super.MENU_CREATE = "Create";
        super.MENU_FETCH = "Fetch";
        super.MENU_UPDATE = "Update";
        super.MENU_DELETE = "Delete";
        super.MENU_SORT = "Sort";
        super.MENU_SEARCH = "Search";
        super.MENU_TRASH = "Trash";
        super.MENU_CONVERT = "Convert";
        super.MENU_EDITOR = "Editor";
        super.MENU_RELATION = "Relation";
        super.MENU_OFFLINE = "Offline";
        super.MENU_ONLINE = "Online";
        super.MENU_ABOUT = "About";
        super.MENU_ADD = "Add";
        super.MENU_REMOVE = "Remove";
        super.MENU_DETAILS = "Details";
        super.MENU_ACCOUNT = "Account";
        super.MENU_LOGOUT = "Logout";
        super.MENU_CLOSE = "Close";
        super.MENU_APPLY = "Apply";
        super.MENU_COUNT = "Count";
        super.MENU_AVERAGE = "Average";

        super.DIALOG_ERROR = "Error";
        super.DIALOG_INFO = "Information";
        super.DIALOG_QUESTION = "Question";
        
        super.FILE_SORTED = "File finished sorting!";

        super.ERROR_NO_DEFAULT_IMAGE = "Default image cannot be found.";
        super.ERROR_CANNOT_WRITE = "Cannot write to specified destination";
        super.ERROR_CANNOT_CONVERT = "Cannot convert to specified destination";
        super.ERROR_INITIALIZE_FAILED = "Cannot initialize paths";
        super.ERROR_ACTIVATION_FAILED = "License activation failed";
        super.ERROR_CREDENTIALS_REQUIRED = "Credentials are required";
        super.ERROR_LOGIN_FAILED = "Login failed";
        super.ERROR_CANNOT_UNDO = "Action cannot be undone";
        super.ERROR_CANNOT_REDO = "Action cannot be redone";
        super.ERROR_ENTITY_NOT_SELECTED = "Entity is not selected";
        super.ERROR_RECORD_NOT_SELECTED = "Record is not selected";
        super.ERROR_ENTITY_NOT_SERIALIZED = "Entity is not serialized";
        super.ERROR_RESOURCE_NOT_SELECTED = "Resource is not selected";
        super.ERROR_NO_PARAMETERS = "Entity has no parameters";
        super.ERROR_RESTRICTED_ACCESS = "Restricted access";
        super.ERROR_NOT_DIRECT_PARENT = "Destination cannot be direct parent of node";
        super.ERROR_CANNOT_COPY = "Cannot copy specified file";
        super.ERROR_CANNOT_LOAD_SCHEMA = "Cannot load schema";
        super.ERROR_CANNOT_VALIDATE_ROOT = "Cannot validate root";
        super.ERROR_ENTITY_OPENED = "Resource has opened entity. Please close it and try again.";
        super.ERROR_FILE_ALREADY_OPENED = "File is already opened by another process.";
        super.ERROR_NOT_DATABASE = "Not a database";

        super.INFO_ONLINE_HELP = "Please, ask a question at ";
        super.INFO_ADD_NODE = "Right click to add node";
        super.INFO_CHOOSE_PATH = "Choose path";
        super.INFO_APP_FILES = "InfView files";
        super.INFO_TOTAL_RECORDS = "Total records: ";
        super.INFO_RECORD_SIZE = "Record size: ";
        super.INFO_BLOCK_SIZE = "Block size: ";
        super.INFO_CURRENT_BLOCK = "Current block: ";
        super.INFO_FILE_CONVERTED = "File converted to: ";

        super.QUESTION_UNSAVED = "Unsaved changes will be discarded. Do you want to continue?";
        super.QUESTION_LOAD = "Data will be loaded from file. Do you want to continue?";
        super.QUESTION_SAVE = "Data will be saved to file. Do you want to continue?";
        super.QUESTION_REMOVE = "Node and it's <<CHILD_COUNT>> children and <<LEAF_COUNT>> leafs will be removed. Do you want to continue?";
        super.QUESTION_SEARCH = "Pick a type of search";
        super.QUESTION_SINGLE = "Do you want to stop after first found record?";
        super.QUESTION_FILE = "Do you want search results to be saved in separate file?";

        super.OPTION_YES = "Yes";
        super.OPTION_NO = "No";
        super.OPTION_OK = "OK";
        super.OPTION_CANCEL = "Cancel";
        super.OPTION_BACK = "Back";
        super.OPTION_NEXT = "Next";
        super.OPTION_FINISH = "Finish";
        super.OPTION_LOGIN = "Login";
        super.OPTION_SAVE_EXIT = "Save and exit";
        super.OPTION_BROWSE = "Browse";
        super.OPTION_RESTORE = "Restore";

        super.INPUT_LICENSE_TITLE = "License Activation";
        super.INPUT_LICENSE_MESSAGE = "Enter license code";
        super.INPUT_USERNAME = "Username";
        super.INPUT_PASSWORD = "Password";
        super.INPUT_OPTIONS = "ResourceOptions";
        
        super.RECORD_NOT_FOUND = "Record not found!";

        super.STATUS_SAVE = "Saved";
        super.STATUS_ADD = "Added";
        super.STATUS_REMOVE = "Removed";
        super.STATUS_CUT = "Cut";
        super.STATUS_COPY = "Copied";
        super.STATUS_PASTE = "Pasted";
        super.STATUS_UNDO = "Undone";
        super.STATUS_REDO = "Redone";
        super.STATUS_WRITE_SETTINGS = "Settings changes written";
        super.STATUS_DISCARD_SETTINGS = "Settings changes discarded";

        super.ABOUT_VERSION = "Version: ";
        super.ABOUT_DEBUG = "Debug: ";
        super.ABOUT_AUTHOR_1 = "Lazar Jelic";
        super.ABOUT_AUTHOR_2 = "Stefan Zivkovic";
        super.ABOUT_AUTHOR_3 = "Marko Stojanovic";
        super.ABOUT_DESCRIPTION = "InfView is an information resource manager.";
        
        super.VALIDATOR_NO_NAME = "Name must have a value!";
        super.VALIDATOR_CANNOT_BE_EMPTY = "Username or password can't be empty!";
        super.VALIDATOR_NOT_INTEGER = "Default value must be integer!";
        super.VALIDATOR_NOT_IN_RANGE = "Default value must be in [minimum, maximum] range!";
        super.VALIDATOR_NOT_IN_LENGHT_RANGE = "Default value must be in [minLength, maxLength] range!";
        super.VALIDATOR_RESOURCE_SAME_NAME = "Two resources with same name!";
        super.VALIDATOR_RESOURCE_SAME_ID = "Two resources with same ID!";
        super.VALIDATOR_ATTRIBUTES_SAME_NAME = "Two attributes with same name!";
        super.VALIDATOR_ATTRIBUTES_SAME_ID = "Two attributes with same ID!";
        super.VALIDATOR_NO_PRIMARY_ATTRIBUTES = "No primary attributes in given entity!";
        super.VALIDATOR_NO_ATTRIBUTES = "No attributes in given entity!";
        super.VALIDATOR_NO_PARENT_ENTITY = "Relation does not have parent entity!";
        super.VALIDATOR_NO_CONNECT_ENTITIES = "Relation does not connect existing entities!";
        super.VALIDATOR_NO_TYPE = "Resource must have a type!";
        super.VALIDATOR_ATTRIBUTE_NO_VALUE = "Required attribute has no value!";
        super.VALIDATOR_ATTRIBUTES_NOT_UNIQUE = "Record primary attributes not unique!";
        super.VALIDATOR_PATTERN_NOT_VALID = "Pattern not valid!";
        super.VALIDATOR_PATTERN_NOT_MATCHED = "Default value not in given pattern!";
        super.VALIDATOR_DEFAULT_NOT_FLOAT = "Default value not float!";
        super.VALIDATOR_DEFAULT_NOT_RANGE = "Default float not in [min, max] range!";
        super.VALIDATOR_FORMAT_NOT_VALID = "Format not valid!";
        super.VALIDATOR_DATES_NOT_MATCHED = "Dates not in given format!";
        super.VALIDATOR_DEFAULT_DATE_NOT_RANGE = "Default date not in [min, max] range!";
        super.VALIDATOR_TIME_NOT_MATCHED = "Time not in given format!";
        super.VALIDATOR_DEFAULT_TIME_NOT_RANGE = "Default time not in [min, max] range!";
        super.VALIDATOR_DATESTIMES_NOT_MATCHED = "DateTimes not in given format!";
        super.VALIDATOR_DATESTIMES_NOT_RANGE = "Default dateTimes not in [min, max] range!";
        super.VALIDATOR_DEFAULT_VALUE_NOT_BOOLEAN = "Default value not boolean!";
    }
}
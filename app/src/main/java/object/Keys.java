package object;

public class Keys {

    public static final String MODE_PROFILE = "MODE_PROFILE",
            TITLE_TANGGAL = "TITLE_TANGGAL" , DATE_CHOSEN = "DAY_CHOSEN",
            USER_REGISTER_STATUS = "USER_REGISTER_STATUS", WHATSAPP = "WHATSAPP",
    USERNAME = "USERNAME", HOUR_SELECTED = "HOUR_SELECTED",
    LEGEND = "LEGEND", NOT_FIRST_TIME = "NOT_FIRST_TIME",
            USER_GENDER = "USER_GENDER", USER_ID = "USER_ID",
            USER_USAGE = "USER_USAGE",
            USER_FORM_STATUS_COMPLETED = "USER_FORM_STATUS_COMPLETED",
            USER_PROFESSION = "USER_PROFESSION", CALENDAR_OPENED = "CALENDAR_OPENED",
            RATE_APP_PREVIOUS = "RATE_APP_PREVIOUS", DAY_PASSED = "DAY_PASSED",
            DATE_INSTALLED = "DATE_INSTALLED",
            INFO_TEXT_PILIH_TANGGAL = "INFO_TEXT_PILIH_TANGGAL",
            // data member 1 until 5 will be containing csv
            // gender [male/female];name;add-date[dd-mm-yyyy]
            // contoh: FamilyMember
            // nama;kelamin;hubungan;tanggal (yyyy-MM-dd)
            DATA_MEMBER_1 = "DATA_MEMBER_1",
            DATA_MEMBER_2 = "DATA_MEMBER_2",
            DATA_MEMBER_3 = "DATA_MEMBER_3",
            DATA_MEMBER_4 = "DATA_MEMBER_4",
            DATA_MEMBER_5 = "DATA_MEMBER_5",

            // if ordered together so this will be filled in
            DATA_MEMBER_BOOKING = "DATA_MEMBER_BOOKING",

            // either single or multiorder
            BOOKING_MODE = "BOOKING_MODE",
            MODE_BOOKING_SINGLE_ORDER = "MODE_BOOKING_SINGLE_ORDER",
            MODE_BOOKING_MULTI_ORDER = "MODE_BOOKING_MULTI_ORDER",

            // whether we want to include the person as well?
            // or just the family only?
            BOOKING_MODE_BERSAMA = "BOOKING_MODE_BERSAMA",
            BOOKING_BERSAMA = "BOOKING_BERSAMA",
            BOOKING_MEREKA_SAJA = "BOOKING_MEREKA_SAJA";

    public static final int MODE_IKHWAN = 1, MODE_AKHWAT = 0;
    public static final int ACCESS_MANAGEMENT = 1, ACCESS_CLIENT = 2;
    public static final int TOGGLE_OFF = 0, TOGGLE_ON = 1;

    public static final int LANGUAGE_ID = 1, LANGUAGE_EN = 2;

    public static final int PROFESI_PELAJAR = 1, PROFESI_UMUM = 2,
            PROFESI_EMPTY = -1;

    public static final int LAYOUT_CHOOSE_BOOKING_MODE =4,
            LAYOUT_CHOOSE_PROFESSION = 3,
            LAYOUT_CHOOSE_PROFILE = 1,
            LAYOUT_DATA_FORM = 2,
            LAYOUT_DETECT_REGISTER = 0;

    public static final String LEGEND_GREEN = "green",
            LEGEND_ORANGE = "orange", LEGEND_RED = "red",
            LEGEND_WHITE = "white", LEGEND_PINK = "pink";

    // used for toggling the hour or desc text shown
    public static final String MODE_DESCRIPTION = "DESC", MODE_HOUR = "HOUR";

}

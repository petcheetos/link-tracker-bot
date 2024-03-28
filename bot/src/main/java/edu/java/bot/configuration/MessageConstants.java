package edu.java.bot.configuration;

public class MessageConstants {
    private MessageConstants() {
    }

    public static final String HELP_NAME = "/help";
    public static final String HELP_DESCRIPTION = "Show available commands";
    public static final String HELP_TITLE = "Commands:\n";

    public static final String LIST_NAME = "/list";
    public static final String LIST_DESCRIPTION = "Send a list of tracking sites";
    public static final String LIST_TITLE = "\uD83D\uDCBB List of tracking sites:";
    public static final String LIST_EMPTY_MSG = "List of tracking sites is empty \uD83D\uDE1E";

    public static final String START_NAME = "/start";
    public static final String START_DESCRIPTION = "Register a user";
    public static final String START_MSG =
        "Hi! \uD83D\uDC4B I am a link tracking bot! Type /help to see the list of available commands!";

    public static final String TRACK_NAME = "/track";
    public static final String TRACK_DESCRIPTION = "Add link for tracking";
    public static final String TRACK_SUCCEED = "\uD83C\uDF89 Site is now tracked: ";
    public static final String TRACK_EMPTY_LINK = "Empty link! ✏️ Type /track link";

    public static final String UNTRACK_NAME = "/untrack";
    public static final String UNTRACK_DESCRIPTION = "Remove a site from the tracking list";
    public static final String UNTRACK_SUCCEED = "\uD83D\uDE34 Site is no longer tracked: ";
    public static final String UNTRACK_EMPTY_LINK = "Empty link! ✏️ Type /untrack link";

    public static final String UNKNOWN_COMMAND = "I do not know this command \uD83D\uDE32";
}

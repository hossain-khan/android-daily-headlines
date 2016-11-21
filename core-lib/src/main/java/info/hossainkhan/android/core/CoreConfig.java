package info.hossainkhan.android.core;

public class CoreConfig {
    /**
     * Number of items needed for a RSS feed to be valid.
     */
    public static final int MINIMUM_FEED_ITEM_REQUIRED = 5;

    /**
     * Background image update delay in milliseconds.
     */
    public static final int BACKGROUND_UPDATE_DELAY = 500;

    /**
     * Search query delay before making network request.
     */
    public static final int SEARCH_DELAY_MS = 500;

    /**
     * Minimum character required before searching for feeds.
     */
    public static final int SEARCH_TEXT_MIN_LENGTH = 3;

    /**
     * Maximum number of search result per query.
     */
    public static final int SEARCH_RESULT_LIMIT = 10;
}

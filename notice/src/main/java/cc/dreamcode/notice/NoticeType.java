package cc.dreamcode.notice;

import lombok.NonNull;

public interface NoticeType {

    String getName();

    NoticeType CHAT = () -> "CHAT";
    NoticeType ACTION_BAR = () -> "ACTION_BAR";
    NoticeType TITLE = () -> "TITLE";
    NoticeType SUBTITLE = () -> "SUBTITLE";
    NoticeType TITLE_SUBTITLE = () -> "TITLE_SUBTITLE";
    NoticeType SIDEBAR = () -> "SIDEBAR";
    NoticeType BOSS_BAR = () -> "BOSS_BAR";
    NoticeType BOOK = () -> "BOOK";
    NoticeType PLAYER_LIST = () -> "PLAYER_LIST";
    NoticeType SOUND = () -> "SOUND";
    NoticeType NONE = () -> "NONE";

    static NoticeType fromName(@NonNull String name) {
        String upperName = name.trim().toUpperCase();
        return switch (upperName) {
            case "CHAT", "MESSAGE" -> CHAT;
            case "ACTION_BAR", "ACTIONBAR" -> ACTION_BAR;
            case "TITLE" -> TITLE;
            case "SUBTITLE" -> SUBTITLE;
            case "TITLE_SUBTITLE", "TITLE_AND_SUBTITLE" -> TITLE_SUBTITLE;
            case "SIDEBAR", "BOARD", "SCOREBOARD" -> SIDEBAR;
            case "BOSS_BAR", "BOSSBAR" -> BOSS_BAR;
            case "BOOK" -> BOOK;
            case "PLAYER_LIST", "TABLIST", "HEADER_FOOTER" -> PLAYER_LIST;
            case "SOUND" -> SOUND;
            case "NONE" -> NONE;
            default -> () -> upperName;
        };
    }
}

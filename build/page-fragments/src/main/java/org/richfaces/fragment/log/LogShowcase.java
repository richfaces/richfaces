package org.richfaces.fragment.log;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.log.Log.LogEntryLevel;

public class LogShowcase {

    @FindBy
    private RichFacesLog log;

    public void showcase_log() {
        log.getLogEntries().getItem(0).getLevel();

        log.getLogEntries().getItem(0).getTimeStamp();

        log.changeLevel(LogEntryLevel.WARN);
        log.clear();
    }
}

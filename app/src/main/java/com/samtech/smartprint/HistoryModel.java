package com.samtech.smartprint;

/**
 * Created by CyberTech on 7/22/2017.
 */
public class HistoryModel {
    public String historyType;
    public String historyAmount;
    public String historyDate;

    public HistoryModel(String historyType, String historyAmount, String historyDate) {
        this.historyType = historyType;
        this.historyAmount = historyAmount;
        this.historyDate = historyDate;
    }

    public HistoryModel() {
    }

    public String getHistoryType() {
        return historyType;
    }

    public void setHistoryType(String historyType) {
        this.historyType = historyType;
    }

    public String getHistoryAmount() {
        return historyAmount;
    }

    public void setHistoryAmount(String historyAmount) {
        this.historyAmount = historyAmount;
    }

    public String getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(String historyDate) {
        this.historyDate = historyDate;
    }
}

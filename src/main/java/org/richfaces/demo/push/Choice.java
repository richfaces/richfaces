package org.richfaces.demo.push;

public class Choice {
    private String label;
    private int votesCount;

    public Choice(String label) {
        this.label = label;
        this.votesCount = 0;
    }

    public void increment(int i) {
        this.votesCount += i;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(int votesCount) {
        this.votesCount = votesCount;
    }
}

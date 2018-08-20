package sample.model;


import java.util.List;

public class Question
{
    private Flag correctFlag;
    private List<Flag> flags;

    public Question(Flag correctFlag, List<Flag> flags) {
        this.correctFlag = correctFlag;
        this.flags = flags;
    }

    public Flag getCorrectFlag() {
        return correctFlag;
    }

    public List<Flag> getFlags() {
        return flags;
    }
}

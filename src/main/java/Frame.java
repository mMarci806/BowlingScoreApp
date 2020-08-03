public class Frame {

    private Integer firstThrow;
    private Integer secondThrow;
    private Integer score;
    private FrameType frameType;

    public Frame(){
        firstThrow = 0;
        secondThrow = 0;
        score = 0;
    }
    public Integer getFirstThrow() {
        return firstThrow;
    }

    public void setFirstThrow(Integer firstThrow) {
        this.firstThrow += firstThrow;
    }

    public Integer getSecondThrow() {
        return secondThrow;
    }

    public void setSecondThrow(Integer secondThrow) {
        this.secondThrow += secondThrow;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score += score;
    }


    public FrameType getFrameType() {
        return frameType;
    }

    public void setFrameType(FrameType frameType) {
        this.frameType = frameType;
    }

    public void calculateScore(){
        this.score = this.firstThrow + this.secondThrow;
    }
}

package engine.quiz;

public class Answer {

    private int[] answer;

    public Answer() {
        this.answer = new int[0];
    }

    public Answer(int[] answer) {
        this.answer = answer;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }
}

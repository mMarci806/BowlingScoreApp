import java.util.List;

public interface IReadInput {
    void readInput(String input);
    Integer calculateTotalScore(List<Frame> frameList);
    Frame mappingFromStringToFrame(String str, Frame frame);
}

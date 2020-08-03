import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main implements IReadInput {

    public static int lineNumber = 1;
    List<String> inputs;
    List<Integer> bonusThrows;
    List<Frame> frameList;

    @Override
    public void readInput(String input) throws IllegalStateException {
        frameList = new ArrayList<>();
        bonusThrows = new ArrayList<>();
        inputs = Arrays.asList(input.split("\\s+"));
        if (inputs.size() >= 10 && inputs.size() <= 12) {
            inputs.stream().forEach(str -> {
                if (frameList.size() < 10) {
                    Frame frame = new Frame();
                    try {
                        frameList.add(mappingFromStringToFrame(str, frame));
                    }catch (IllegalStateException e){
                        e.getMessage();
                    }
                } else {
                    if (str.matches("[1-9]")) {
                        bonusThrows.add(Integer.parseInt(str));

                    } else if (str.equals("x")) {
                        bonusThrows.add(10);
                    } else {
                        throw new IllegalStateException("Incorrect input : " + str);
                    }
                }
            });
        } else {
            throw new IllegalStateException("Wrong input string at line : " + lineNumber);
        }

    }

    @Override
    public Integer calculateTotalScore(List<Frame> frameList) throws IndexOutOfBoundsException{
        for (int ind = 0; ind < 10; ind++) {
            Frame currentFrame = frameList.get(ind);
            FrameType type = currentFrame.getFrameType();
            switch (type) {
                case MISS:
                    currentFrame.setScore(0);
                    break;
                case OPEN:
                    currentFrame.calculateScore();
                    break;
                case SPAR:
                    currentFrame.calculateScore();
                    if(frameList.indexOf(currentFrame) == 9){
                            currentFrame.setScore(bonusThrows.get(0));
                    }else {
                        currentFrame.setScore(frameList.get(ind + 1).getFirstThrow());
                    }
                    break;
                case STRIKE:
                    currentFrame.calculateScore();
                    if(frameList.indexOf(currentFrame) == 8){
                        if(frameList.get(9).getFrameType().equals(FrameType.STRIKE)){
                            currentFrame.setScore(10 + bonusThrows.get(0));
                        }else{
                            currentFrame.setScore(frameList.get(9).getFirstThrow() + frameList.get(9).getSecondThrow());
                        }
                    }else if(frameList.indexOf(currentFrame) == 9){
                        currentFrame.setScore(bonusThrows.get(0) + bonusThrows.get(1));
                    }else{
                        Frame next1 = frameList.get(ind + 1);
                        if(next1.getFrameType().equals(FrameType.STRIKE)){
                            Frame next2 = frameList.get(ind + 2);
                            currentFrame.setScore(next1.getFirstThrow() + next2.getFirstThrow());
                        }else{
                            currentFrame.setScore(next1.getFirstThrow() + next1.getSecondThrow());
                        }
                    }
            }
            frameList.set(ind, currentFrame);
        }
        return frameList.stream().mapToInt(fr -> fr.getScore()).sum();
    }

    @Override
    public Frame mappingFromStringToFrame(String str, Frame frame) throws IllegalStateException {

        if (str.equals("x")) {
            frame.setFirstThrow(10);
            frame.setFrameType(FrameType.STRIKE);
        } else if (str.equals("-")) {
            frame.setFrameType(FrameType.MISS);
        } else if (str.equals("-/")) {
            frame.setFirstThrow(0);
            frame.setSecondThrow(10);
            frame.setFrameType(FrameType.SPAR);
        } else if (str.matches("[1-8]{2}")) {
            frame.setFirstThrow(Integer.parseInt(str.substring(0, 1)));
            frame.setSecondThrow(Integer.parseInt(str.substring(1, 2)));
            frame.setFrameType(FrameType.OPEN);
        } else if (str.matches("-[1-9]")) {
            frame.setFirstThrow(0);
            frame.setSecondThrow(Integer.parseInt(str.substring(1, 2)));
            frame.setFrameType(FrameType.OPEN);
        } else if (str.matches("[1-9]/")) {
            frame.setFirstThrow(Integer.parseInt(str.substring(0, 1)));
            frame.setSecondThrow(10 - frame.getFirstThrow());
            frame.setFrameType(FrameType.SPAR);
        } else if (str.matches("[1-9]-")){
            frame.setFirstThrow(Integer.parseInt(str.substring(0,1)));
            frame.setSecondThrow(0);
            frame.setFrameType(FrameType.OPEN);
        }else {
            throw new IllegalStateException("Incorrect input !");
        }
        return frame;
    }

    public static void main(String args[]) {
        Main main = new Main();
        try (Scanner sc = new Scanner(new File("src/main/resources/InputFile"))) {
            while (sc.hasNextLine()) {
                try {
                main.readInput(sc.nextLine());
                System.out.println("Player" + lineNumber + " : " +main.calculateTotalScore(main.frameList));
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }catch (IndexOutOfBoundsException e) {
                    System.err.println("Wrong input, line " + lineNumber);
                } finally{
                    lineNumber++;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File doesn't exist");
        }catch (Exception e ){
            e.printStackTrace();
        }
    }
}

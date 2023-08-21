import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

public class BefungeInterpreter {


    public static void main(String[] args) {
        BefungeInterpreter interpreter = new BefungeInterpreter();
        System.out.println("Most classic code: \n>              v\nv\"Hello World!\"<\n>:v\n^,_@\nOutput:");
        System.out.println(interpreter.interpret(">              v\nv\"Hello World!\"<\n>:v\n^,_@"));
        System.out.println();
        System.out.println("Print '123456789': \n>987v>.v\nv456<  :\n>321 ^ _@\nOutput:");
        System.out.println(interpreter.interpret(">987v>.v\nv456<  :\n>321 ^ _@"));
        System.out.println();
        System.out.println("Code for calculating the factorial of 8:\n08>:1-:v v *_$.@ \n  ^    _$>\\:^\nOutput:");
        System.out.println(interpreter.interpret("08>:1-:v v *_$.@ \n  ^    _$>\\:^"));
    }

    enum Direction {
        RIGHT, LEFT, UP, DOWN
    }

    public String interpret(String code) {
        Stack<Integer> stack = new Stack<>();
        StringBuilder output = new StringBuilder();
        Direction direction = Direction.RIGHT;

        String[] codeLines = code.split("\n");
        int codeLineCount = codeLines.length;
        int codeLineLength = Arrays.stream(codeLines).map(el ->  el.length()).max((firstInt, secondInt) -> firstInt-secondInt).get();

        Character[][] operators = new Character[codeLineCount][codeLineLength];
        boolean stringMode = false;
        int i = 0, j = 0;

        // Init operators
        for (int codeLineIndex = 0; codeLineIndex < codeLineCount; codeLineIndex++) {
            for (int charIndex = 0; charIndex < codeLineLength; charIndex++) {
                if (codeLines[codeLineIndex].length() - 1 < charIndex) operators[codeLineIndex][charIndex] = ' ';
                else operators[codeLineIndex][charIndex] = codeLines[codeLineIndex].charAt(charIndex);
            }
        }

        Character pointer = operators[i][j];

        // Operators handling
        while (pointer != '@') {
            if (stringMode) {
                if (pointer == '"') {
                    stringMode = false;
                }
                else {
                    stack.push((int) pointer);
                }
            }
            else {
                if (pointer.toString().matches("[0-9]")) stack.push(Integer.parseInt(pointer.toString()));
                else {
                    switch (pointer) {
                        case ' ':
                            break;
                        case '+':
                            stack.push(stack.pop() + stack.pop());
                            break;
                        case '-': {
                            int a = stack.pop();
                            int b = stack.pop();
                            stack.push(b - a);
                        }
                        break;
                        case '*':
                            stack.push(stack.pop() * stack.pop());
                            break;
                        case '/': {
                            int a = stack.pop();
                            int b = stack.pop();
                            if (a != 0) stack.push(b / a);
                            else stack.push(0);
                        }
                        break;
                        case '%': {
                            int a = stack.pop();
                            int b = stack.pop();
                            if (a != 0) stack.push(b % a);
                            else stack.push(0);
                        }
                        break;
                        case '!':
                            if (stack.pop() == 0) stack.push(1);
                            else stack.push(0);
                            break;
                        case '`': {
                            int a = stack.pop();
                            int b = stack.pop();
                            if (b > a) stack.push(1);
                            else stack.push(0);
                        }
                        break;
                        case '>':
                            direction = Direction.RIGHT;
                            break;
                        case '<':
                            direction = Direction.LEFT;
                            break;
                        case '^':
                            direction = Direction.UP;
                            break;
                        case 'v':
                            direction = Direction.DOWN;
                            break;
                        case '?':
                            // We need to find direction that doesn't move us to bounds of array
                            boolean foundGoodDirection = false;
                            Random rand = new Random();
                            Direction newDirection;
                            while (!foundGoodDirection) {
                                newDirection = Direction.values()[rand.nextInt(4)];
                                if (direction == Direction.RIGHT && j + 1 < operators[0].length)
                                    foundGoodDirection = true;
                                else if (direction == Direction.LEFT && j - 1 >= 0) foundGoodDirection = true;
                                else if (direction == Direction.UP && i - 1 >= 0) foundGoodDirection = true;
                                else if (direction == Direction.DOWN && i + 1 < operators.length)
                                    foundGoodDirection = true;

                                if (foundGoodDirection) {
                                    direction = newDirection;
                                }
                            }
                            break;
                        case '"':
                            stringMode = true;
                            break;
                        case ':':
                            if (stack.isEmpty()) stack.push(0);
                            else {
                                int dupe = stack.pop();
                                stack.push(dupe);
                                stack.push(dupe);
                            }
                            break;
                        case '\\':
                            if (stack.size() == 1) {
                                int valueOnTop = stack.pop();
                                int valueOnBottom = 0;
                                stack.push(valueOnTop);
                                stack.push(valueOnBottom);
                            } else {
                                int valueOnTop = stack.pop();
                                int valueOnBottom = stack.pop();
                                stack.push(valueOnTop);
                                stack.push(valueOnBottom);
                            }
                            break;
                        case '$':
                            stack.pop();
                            break;
                        case '.':
                            output.append(Integer.toString(stack.pop()));
                            break;
                        case ',':
                            output.append((char) ( (int) stack.pop()));
                            break;
                        case '#':
                            if (direction == Direction.RIGHT) j++;
                            else if (direction == Direction.LEFT) j--;
                            else if (direction == Direction.UP) i--;
                            else if (direction == Direction.DOWN) i++;
                            break;
                        case 'p': {
                            int y = stack.pop();
                            int x = stack.pop();
                            int value = stack.pop();
                            operators[y][x] = Character.valueOf((char) value);
                        }
                        break;
                        case 'g': {
                            int x = stack.pop();
                            int y = stack.pop();
                            stack.push( (int) operators[x][y]);
                        }
                        break;
                        case '_':
                            if (stack.pop() == 0) direction = Direction.RIGHT;
                            else direction = Direction.LEFT;
                            break;
                        case '|':
                            if (stack.pop() == 0) direction = Direction.DOWN;
                            else direction = Direction.UP;
                            break;
                    }
                }
            }
            // After operator handle, moving to next operator
            if (direction == Direction.RIGHT) {
                if (j + 1 > codeLineLength - 1) j = 0;
                else j++;
                pointer = operators[i][j];
            } else if (direction == Direction.LEFT) {
                if (j - 1 < 0) j = codeLineLength - 1;
                else j--;
                pointer = operators[i][j];
            } else if (direction == Direction.UP) {
                if (i - 1 < 0) i = codeLineCount - 1;
                else i--;
                pointer = operators[i][j];
            } else if (direction == Direction.DOWN) {
                if (i + 1 > codeLineCount - 1) i = 0;
                else i++;
                pointer = operators[i][j];
            }
        }

        return output.toString();
    }
}


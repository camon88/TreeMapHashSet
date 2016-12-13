
import java.io.BufferedReader;
import java.io.IOException;

/**
 * StateMachine class: helper class for JavaIdentifier program returns words in
 * a file one at a time, by calling the hasToken(), getToken(), and
 * getLineNumber() methods.
 *
 *
 */
public class StateMachine {

    private enum State {

        LONG_COMMENT, STRING, IDENTIFIER, START_STATE,
        INLINE_COMMENT, SINGLE_CHARACTER
    }

    private State state;
    private BufferedReader input;
    private char[] currentWord;
    private int index;
    private int lineNumber;
    private char lastChar;
    private boolean endOfFile;
    private String token;

    /**
     * Constructor for StateMachine
     *
     * @param in : the BufferedReader - Java program file, already open
     */
    public StateMachine(BufferedReader in) {
        state = State.START_STATE;
        input = in;
        index = 0;
        currentWord = new char[80];
        lineNumber = 1;
        lastChar = ' ';
        endOfFile = false;
        token = null;
    }

    /**
     * Accessor method for the line number of the last token returned
     *
     * @return returns the line number of the last token - or 1 if nothing has
     * been read
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     *
     * @return returns the next token (word) in the file, or null if at the end
     * of the file. The next word could be a Java keyword or a program
     * identifier.
     */
    public String getToken() {
        String temp = token;
        if (temp == null) {
            temp = getNextToken();
        }
        token = null;
        return temp;
    }

    /**
     *
     * @return returns true if the program contains another keyword false
     * otherwise
     */
    public boolean hasToken() {
        if (endOfFile) {
            return false;
        }
        if (token == null) {
            token = getNextToken();
            return token != null;
        }
        return true;
    }

      //  PRIVATE METHODS
    private String getNextToken() {
        char ch = ' ';

        while (!endOfFile) {
            if (lastChar == '\n') {
                lineNumber++;
            }
            lastChar = ch;
            try {
                ch = (char) input.read();
            } catch (IOException e) {
                System.out.println("Trouble reading input in getNextToken()");
                System.exit(0);
            }
            if (ch == (char) -1) {
                endOfFile = true;
                return null;
            }
            switch (state) {
                case START_STATE:
                    index = 0;
                    if (isLetter(ch) || ch == '_') {
                        currentWord[index] = ch;
                        index++;
                        state = State.IDENTIFIER;
                    } else if (ch == '/') {
                        if (lastChar == '/') {
                            state = State.INLINE_COMMENT;
                        }
                    } else if (ch == '*') {
                        if (lastChar == '/') {
                            state = State.LONG_COMMENT;
                        }
                    } else if (ch == '\"') {
                        state = State.STRING;
                    } else if (ch == '\'') {
                        state = State.SINGLE_CHARACTER;
                    }
                    break;
                case IDENTIFIER:
                    if (isLetter(ch) || isDigit(ch) || ch == '_') {
                        currentWord[index] = ch;
                        index++;
                    } else {
                        state = State.START_STATE;
                        lastChar = ch;
                        return new String(currentWord, 0, index);
                    }
                    break;
                case INLINE_COMMENT:
                    if (ch == '\n') // end-of-comment
                    {
                        state = State.START_STATE;
                    }
                    break;
                case LONG_COMMENT:
                    if (ch == '/' && lastChar == '*') // end-of-comment
                    {
                        state = State.START_STATE;
                    }
                    break;
                case STRING:
                    if (ch == '\"' && lastChar != '\\') // end-of-string
                    {
                        state = State.START_STATE;
                    }
                    break;
                case SINGLE_CHARACTER:
                    if (ch == '\'' && lastChar != '\\') // end-of-character
                    {
                        state = State.START_STATE;
                    } else if (ch == '\\' && lastChar == '\\') {
                        ch = ' ';                         // backslash char 
                    }
                    break;
            }
        }
        return null;
    }

    private boolean isDigit(char ch) {
        return (ch >= '0' && ch <= '9');
    }

    private boolean isLetter(char ch) {
        return ch >= 'A' && ch <= 'Z'
                || ch >= 'a' && ch <= 'z';
    }

}

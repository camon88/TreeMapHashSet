
    import java.io.BufferedReader;
    import java.io.FileReader;
    import java.io.IOException;
    import java.util.HashSet;
    import java.util.Iterator;
    import java.util.LinkedList;
    import java.util.Scanner;
    import java.util.Set;
    import java.util.TreeMap;

    /*
     a program that will read a java source code file and output a list of 
     all identifiers(variables, method names, class names, fields..) 
     but NOT KEYWORDS and NOT WORDS FOUND IN STRING CONSTANTS.
     */
    public class JavaIdentifier {

        public static void main(String[] args) {
            // place all the Java keywords from the keywords.dat file in a HashSet of Strings
            BufferedReader fileIn = null;
            StateMachine reader;
            System.out.println("Building keywords list from file keywords.dat ...");
            HashSet<String> keywordsSet = new HashSet<String>();
            try {
                fileIn = new BufferedReader(new FileReader("keywords.dat"));
                reader = new StateMachine(fileIn);
                //placing keywords from the keywords.dat file in a HashSet of Strings
                while (reader.hasToken()) {
                    keywordsSet.add(reader.getToken());
                }
                fileIn.close();

            } catch (IOException ioe) {
                System.out.println("File keywords.dat not found.  Exiting");
                System.exit(1);
            }
            System.out.println("Done!");
            System.out.print("Enter java file name to read: ");
            String fileName = getString();
            String token;
            int lineNumber;
            LinkedList<String> ll;
            TreeMap<String, LinkedList<String>> tm = new TreeMap<String, LinkedList<String>>();
            //reading file
            try {
                fileIn = new BufferedReader(new FileReader(fileName));
                reader = new StateMachine(fileIn);
                while (reader.hasToken()) {
                    token = reader.getToken();
                    lineNumber = reader.getLineNumber();
                    //if current word is not keyword
                    if (!keywordsSet.contains(token)) {
                        //if token already added to treemap
                        if (tm.get(token)!=null) {
                            //get the linklist and add line number
                            tm.get(token).add(Integer.toString(lineNumber));
                        } else {
                            //create new linked list and add to tree map
                            ll = new LinkedList<String>();
                            ll.add(Integer.toString(lineNumber));
                            tm.put(token, ll);
                        }
                    }
                }

                fileIn.close();

            } catch (IOException ioe) {
                System.out.println("File not found.  Exiting");
            }

            //iterating over treemap
            Set<String> s = tm.keySet();
            Iterator<String> itr = s.iterator();
            while (itr.hasNext()) {
                token = itr.next();
                System.out.print(token + " ");
                ll = tm.get(token);
                System.out.println(ll.toString());
                
            }
        }

        /**
         * Helper method to get String from user
         *
         * @return
         */
        public static String getString() {
            Scanner input = new Scanner(System.in);
            return input.nextLine();
        }
    }

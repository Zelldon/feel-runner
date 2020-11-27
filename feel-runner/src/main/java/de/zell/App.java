package de.zell;


import io.zeebe.el.impl.FeelExpressionLanguage;
import io.zeebe.protocol.impl.encoding.MsgPackConverter;
import io.zeebe.util.sched.clock.ActorClock;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args ) throws Exception
    {
        final var expressionLanguage = new FeelExpressionLanguage(new Clock());
        try (final var reader =
            new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("$");
                final var inputLine = reader.readLine();
                if (inputLine == null || inputLine.equalsIgnoreCase("exit"))
                {
                    break;
                }

                try {

                    final var expressionStr = String.format("=%s", inputLine);
                    System.out.println();
                    System.out.println("Try expression: " + expressionStr);
                    final var expression = expressionLanguage.parseExpression(expressionStr);
                    final var evaluationResult = expressionLanguage.evaluateExpression(expression, x -> null);

                    System.out.println("EvaluationResult: " + evaluationResult);
                    System.out.println("Result type: " + evaluationResult.getType());
                    System.out.println("Result failure: " + evaluationResult.getFailureMessage());
                    System.out.println("Result date time: " + evaluationResult.getDateTime());
                    System.out.println("Result duration: " + evaluationResult.getDuration());
                    final var directBuffer = evaluationResult.toBuffer();
                    if (directBuffer != null) {
                        System.out.println("As json: " + MsgPackConverter.convertToJson(directBuffer));
                    }
                } catch (Exception e) {
                    System.out.println("Unexpected exception happend based on last input: " + inputLine);
                    e.printStackTrace();
                }

            }
        }

        System.out.println("Bye..");
    }

    private static final class Clock implements ActorClock {

        @Override
        public boolean update() {
            return true;
        }

        @Override
        public long getTimeMillis() {
            return System.currentTimeMillis();
        }

        @Override
        public long getNanosSinceLastMillisecond() {
            return 0;
        }

        @Override
        public long getNanoTime() {
            return System.nanoTime();
        }
    }
}

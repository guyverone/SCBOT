
import java.util.Scanner;

/**
 * Created by suhd on 2016-04-07.
 */
public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()) {
            int firstNum = 0, secondNum = 0;
            int[] firstNumArray = new int[10], secondNumArray = new int[10], maxArray = new int[10], minArray = new int[10];
            int firstNumArrayLength = 0, secondNumArrayLength = 0, maxArrayLength = 0, minArrayLength = 0, winner = 0;

            //ignored type comparison
            firstNum = Integer.valueOf(sc.next());
            secondNum = Integer.valueOf(sc.next());
            //there's no way to get score higher than 9900
            if(firstNum <= 9900 && secondNum <= 9900 && firstNum >= 1 && secondNum >= 1) {
                //fill the firstNumArray with base element of firstNum
                firstNumArrayLength = fillArrayWithElementWithLengthReturn(firstNumArray, firstNum, firstNumArrayLength);
                //fill the secondNumArray with base element of secondNum
                secondNumArrayLength = fillArrayWithElementWithLengthReturn(secondNumArray, secondNum, secondNumArrayLength);

                if (firstNumArrayLength > secondNumArrayLength) {
                    maxArray = firstNumArray;
                    maxArrayLength = firstNumArrayLength;
                    minArray = secondNumArray;
                    minArrayLength = secondNumArrayLength;
                } else {
                    maxArray = secondNumArray;
                    maxArrayLength = secondNumArrayLength;
                    minArray = firstNumArray;
                    minArrayLength = firstNumArrayLength;
                }
                boolean flag = isIntersectionBetweenTwoArray(maxArray, minArray, maxArrayLength, minArrayLength);
                if (flag) {
                    winner = firstNum < secondNum ? firstNum : secondNum;
                } else {
                    winner = firstNum > secondNum ? firstNum : secondNum;
                }
                System.out.println(winner);
            }
        }
    }

    private static int fillArrayWithElementWithLengthReturn(int[] numArray, int num, int numArrayLength) {
        for(int i=2; i<=100; i++) {
            if(num%i == 0 && num!=i && num/i!=i) {
                numArray[numArrayLength] = num/i;
                numArrayLength++;
            }
            if(num == i) {
                numArray[numArrayLength] = i;
                numArrayLength++;
            }
        }
        return numArrayLength;
    }

    private static boolean isIntersectionBetweenTwoArray(int[] maxArray,int[] minArray,int maxArrayLength,int minArrayLength) {
        for(int i=0; i<maxArrayLength; i++) {
            for(int j=0; j<minArrayLength; j++) {
                if(maxArray[i] == minArray[j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
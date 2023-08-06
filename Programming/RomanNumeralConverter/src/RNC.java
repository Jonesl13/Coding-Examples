import java.util.Scanner;

public class RNC {
    public static void main(String[]args){
       Scanner scan = new Scanner(System.in);
        System.out.println("Please enter whole number or Roman numerals (or both!):");

        int numberOut = toInteger(scan.nextLine());
        System.out.println("Integer Total: "+ numberOut);
        System.out.println("Roman Numerals: "+ toRomanNumerals(numberOut));

    }

    //Takes a given int n and returns the Roman numeral equivalent
    public static String toRomanNumerals (int n){

        if (n > 3999) {
            return "This number is too large for Roman numerals, it must be less than 4,000.";
        }

        String rN = "";

            while (n - 1000 >= 0) {
                rN += "M";
                n -= 1000;
            }
            if (n - 900 >= 0) {
                rN += "CM";
                n -= 900;
            }
            if (n - 500 >= 0) {
                rN += "D";
                n -= 500;
            }
            if (n - 400 >= 0) {
                rN += "CD";
                n -= 400;
            }
            while (n - 100 >= 0) {
                rN += "C";
                n -= 100;
            }
            while (n - 90 >= 0) {
                rN += "XC";
                n -= 90;
            }
            if (n - 50 >= 0) {
                rN += "L";
                n -= 50;
            }
            if (n - 40 >= 0) {
                rN += "XL";
                n -= 40;
            }
            while (n - 10 >= 0) {
                rN += "X";
                n -= 10;
            }
            while (n - 9 >= 0) {
                rN += "IX";
                n -= 9;
            }
            if (n - 5 >= 0) {
                rN += "V";
                n -= 5;
            }
            if (n - 4 >= 0) {
                rN += "IV";
                n -= 4;
            }
            while (n - 1 >= 0) {
                rN += "I";
                n -= 1;
            }
        return rN;
    }

    //Takes a given String romanNumerals and returns the int equivalent
    public static int toInteger (String romanNumerals){
        romanNumerals = romanNumerals +" ";   //Adding a blank character to the end of rN to avoid array out of bounds below (a cheeky workaround I know, but it works B^) )
        char[] ch = romanNumerals.toCharArray();
        String spareDigits = "0";
        int n = 0;

        for (int count = 0; count < ch.length; count++) {

            //If the character is a number, keep it to the side (spareDigits) so we can add it to the final int at the end
            if(Character.isDigit(ch[count])){
                spareDigits = spareDigits + ch[count];
                continue;
            }

           if (ch[count] =='M') {
                n += 1000;
               continue;
            }
            if (ch[count] =='C' && ch[count+1] == 'M') {
                n += 900;
                count++;
                continue;
            }
            if (ch[count] =='D') {
                n += 500;
                continue;
            }
            if (ch[count] =='C' && ch[count+1] == 'D') {
                n += 400;
                count++;
                continue;
            }
            if (ch[count] =='C') {
                n += 100;
                continue;
            }
            if (ch[count] =='X' && ch[count+1] == 'C') {
                n += 90;
                count++;
                continue;
            }
            if (ch[count] =='L') {
                n += 50;
                continue;
            }
            if (ch[count] =='X' && ch[count+1] == 'L') {
                n += 40;
                count++;
                continue;
            }
            if (ch[count] =='X') {
                n += 10;
                continue;
            }
            if (ch[count] =='I' && ch[count+1] == 'X') {
                n += 9;
                count++;
                continue;
            }
            if (ch[count] =='V') {
                n += 5;
                continue;
            }
            if (ch[count] =='I' && ch[count+1] == 'V') {
                n += 4;
                count++;
                continue;
            }
            if (ch[count] =='I') {
                n += 1;
                continue;
            }

        }

        //Checking to make sure the given number doesn't exceed the integer limit
        try {
            n = n + Integer.parseInt(spareDigits);
        }catch (NumberFormatException nfe) {
            throw new NumberFormatException("Input number exceeds 2,147,483,647 (32 bit limit)");
        }
        return n;
    }
}

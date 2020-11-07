package com.example.SudoKey;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Triangle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triangle);
    }

    public void clearAll(View v){
        TextView input;
        int [] inArr = {R.id.sideA, R.id.sideB, R.id.sideC, R.id.angleA, R.id.angleB, R.id.angleC};
        for(int i=0;i<6;i++){
             input = findViewById(inArr[i]);
             input.setText("");
        }
        clearAns();
    }

    public void clearAns(){
        TextView input;
        int [] inArr = {R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4, R.id.answer5, R.id.answer6, R.id.areaAns};
        for(int i=0;i<6;i++){
            input = findViewById(inArr[i]);
            input.setText("");
        }
    }

    public void solve(View v){
        int [] inArr = {R.id.sideA, R.id.sideB, R.id.sideC, R.id.angleA, R.id.angleB, R.id.angleC};
        int [] answers = {R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4, R.id.answer5, R.id.answer6, R.id.areaAns};
        double [] arr = new double[6];
        TextView input, output;
        for(int i=0;i<6;i++){
            try {
                input = findViewById(inArr[i]);
                arr[i] = Double.parseDouble(String.valueOf(input.getText()));
                if(arr[i]<0){
                    arr[i] = 0;
                }
            }
            catch (Exception e){
                arr[i] =0;
            }
        }

        double a = arr[0];
        double b =arr[1];
        double c =arr[2];
        double A =arr[3];
        double B =arr[4];
        double C =arr[5];

        int sides = ((a != 0) ? 1 : 0) + ((b != 0) ? 1 : 0) + ((c != 0) ? 1 : 0);
        int angles = ((A != 0) ? 1 : 0) + ((B != 0) ? 1 : 0) + ((C != 0) ? 1 : 0);
        if(sides+angles != 3){
            Toast.makeText(this,"please give exactly 3 peices of information", Toast.LENGTH_LONG).show();
            a = 0;
            b =0;
            c =0;
            A =0;
            B =0;
            C =0;
        }
        if( a != 0 && b != 0 && c !=0){
            A = function_acos((-function_square(a,2)+function_square(b,2)+function_square(c,2))/(2*b*c));
            C = function_acos((function_square(a,2)+ function_square(b,2)-function_square(c,2))/(2*b*a));
            B = function_acos((function_square(a,2)-function_square(b,2)+function_square(c,2))/(2*c*a));
        }
        else if( a!=0 && b!=0 && A!=0){
            B = function_asin((b*function_sin(A))/(a));
            C = 180-B-A;
            c = (function_sin(C)*b)/(function_sin(B));
        }
        else if( a != 0 && c != 0 && A != 0){
            C = function_asin((c*function_sin(A))/(a));
            B = 180-C-A;
            b = (function_sin(B) * a)/(function_sin(A));
        }
        else if( c!=0 && b!=0 && B!=0){
            C = function_asin((c*function_sin(B))/(b));
            A = 180-B-C;
            a = (function_sin(C)*b)/(function_sin(B));
        }
        else if( a!=0 && c!=0 && C!=0){
            A = function_asin((a*function_sin(C))/(c));
            B = 180-C-A;
            b= (function_sin(B)*c)/(function_sin(C));
        }
        else if( a!=0 && c!=0 && B!=0){
            b = function_root(a*a+c*c-2*a*c*function_cos(B),2);
            A = function_asin((a*function_sin(B))/(b));
            C = 180-B-A;
        }
        else if( a!=0 && b!=0 && C!=0){
            c = function_root(a*a+b*b-2*a*b*function_cos(C),2);
            A = function_asin((a*function_sin(C))/(c));
            B = 180-C-A;
        }
        else if( b!=0 && c!=0 && A!=0){
            a = function_root(b*b+c*c-2*b*c*function_cos(A),2);
            B = function_asin((a*function_sin(B))/(b));
            C = 180-B-A;
        }
        else if( c!=0 && b!=0 && C!=0){
            B = function_asin((c*function_sin(C))/(c));
            A = 180-B-C;
            a = (function_sin(A)*b)/(function_sin(B));
        }
        else if( a!=0 && b!=0 && B!=0){
            A = function_asin((a*function_sin(B))/(b));
            C = 180-B-A;
            c = (function_sin(C)*b)/(function_sin(B));
        }
        else if(c!= 0 && A!=0 && B!=0){
            C = 180-A-B;
            a = (c*function_sin(A))/function_sin(C);
            b = (c*function_sin(B))/function_sin(C);
        }
        else if(b!= 0 && A!=0 && C!=0){
            B = 180-A-C;
            a = (b*function_sin(A))/function_sin(B);
            c = (b*function_sin(C))/function_sin(B);
        }
        else if(a!= 0 && B!=0 && C!=0){
            A = 180-B-C;
            b = (a*function_sin(B))/function_sin(A);
            c = (a*function_sin(C))/function_sin(A);
        }
        else if(a!= 0 && A!=0 && B!=0){
            C = 180-A-B;
            b = (a*function_sin(A))/function_sin(A);
            c = (a*function_sin(C))/function_sin(A);
        }
        else if(b!= 0 && A!=0 && B!=0){
            C = 180-A-B;
            a = (b*function_sin(A))/function_sin(B);
            c = (b*function_sin(C))/function_sin(B);
        }
        else if(c!= 0 && A!=0 && C!=0){
            B = 180-C-A;
            a = (c*function_sin(A))/function_sin(C);
            b = (c*function_sin(C))/function_sin(C);
        }
        else if(b!= 0 && B!=0 && C!=0){
            A = 180-C-B;
            a = (b*function_sin(A))/function_sin(B);
            c = (b*function_sin(C))/function_sin(B);
        }
        else{
            Toast.makeText(this, "no solution please try again", Toast.LENGTH_SHORT).show();
            clearAns();

        }

        double area = (a*function_sin(C)*b)/2;
        double [] answer = {a, b, c, A, B, C, area};
        for(int j =0;j<7;j++){
            output = findViewById(answers[j]);
            output.setText(String.valueOf(answer[j]));
        }
    }

    public static double function_square(double x,double y){
        return Math.pow(x,y);
    }

    public static double function_sin(double x){
        return Math.sin(x*Math.PI/180);
    }

    public static double function_cos(double x) {
        return Math.cos(x*Math.PI/180);
    }

    public static double function_tan(double x) {
        return Math.tan(x*Math.PI/180);
    }

    public static double function_acos(double x) {
        return Math.toDegrees(Math.acos(x));
    }

    public static double function_asin(double x) {
        return Math.toDegrees(Math.asin(x));
    }

    public double solveSide(double a, double b, double C) {
        C = Math.toRadians(C);
        if (C > 0.001)
            return Math.sqrt(a * a + b * b - 2 * a * b * Math.cos(C));
        else
            return Math.sqrt((a - b) * (a - b) + a * b * C * C * (1 - C * C / 12));
    }

    public static double function_root(double x,double y){
        return Math.pow(x,(1/y));
    }

    public double solveAngle(double a, double b, double c) {
        double temp = (a * a + b * b - c * c) / (2 * a * b);
        if (-1 <= temp && temp <= 0.9999999)
            return Math.toDegrees(Math.acos(temp));
        else if (temp <= 1)
            return Math.toDegrees(Math.sqrt((c * c - (a - b) * (a - b)) / (a * b)));
        else
            return 0;
    }



/*    public double [] check() {
        int[] inArr = {R.id.sideA, R.id.sideB, R.id.sideC, R.id.angleA, R.id.angleB, R.id.angleC};
        int[] answers = {R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4, R.id.answer5, R.id.answer6, R.id.areanAns};
        double[] arr = new double[6];
        TextView input, output;
        for (int i = 0; i < 6; i++) {
            try {
                input = findViewById(inArr[i]);
                arr[i] = Double.parseDouble(String.valueOf(input.getText()));
                if (arr[i] < 0) {
                    arr[i] = 0;
                }
            } catch (Exception e) {
                arr[i] = 0;
            }
        }
        double a = arr[0];
        double b = arr[1];
        double c = arr[2];
        double A = arr[3];
        double B = arr[4];
        double C = arr[5];
        int sides = ((a != 0) ? 1 : 0) + ((b != 0) ? 1 : 0) + ((c != 0) ? 1 : 0);
        int angles = ((A != 0) ? 1 : 0) + ((B != 0) ? 1 : 0) + ((C != 0) ? 1 : 0);
        if (sides + angles != 3){
            //throw "Give exactly 3 pieces of information";
            System.out.println("eror");
    }
        else if (sides == 0) {
            //throw "Give at least one side length";
            System.out.println("eror");
        }

        else if (sides == 3) {
             String status = "Side side side (SSS) case";
            if (a + b <= c || b + c <= a || c + a <= b) {
                //throw status + " - No solution";
                System.out.println("eror");
            }
            A = solveAngle(b, c, a);
            B = solveAngle(c, a, b);
            C = solveAngle(a, b, c);
            // Heron's formula
            double s = (a + b + c) / 2;
            double area = Math.sqrt(s * (s - a) * (s - b) * (s - c));

        } else if (angles == 2) {
            String status = "Angle side angle (ASA) case";
            // Find missing angle
            if (A == 0) A = 180 - B - C;
            if (B == 0) B = 180 - C - A;
            if (C == 0) C = 180 - A - B;
            if (A <= 0 || B <= 0 || C <= 0) {
                //throw status + " - No solution";
                System.out.println("eror");
            }
            double sinA = Math.sin(Math.toRadians(A));
            double sinB = Math.sin(Math.toRadians(B));
            double sinC = Math.sin(Math.toRadians(C));
            // Use law of sines to find sides
            double ratio, area;  // side / sin(angle)
            if (a != 0) { ratio = a / sinA; area = a * ratio * sinB * sinC / 2; }
            if (b != 0) { ratio = b / sinB; area = b * ratio * sinC * sinA / 2; }
            if (c != 0) { ratio = c / sinC; area = c * ratio * sinA * sinB / 2; }
            if (a == 0) a = ratio * sinA;
            if (b == 0) b = ratio * sinB;
            if (c == 0) c = ratio * sinC;

        } else if (A != 0 && a == 0 || B != 0 && b == 0 || C != 0 && c == 0) {
            String status = "Side angle side (SAS) case";
            if (A != 0 && A >= 180 || B != 0 && B >= 180 || C != 0 && C >= 180) {
                //throw status + " - No solution";
                System.out.println("eror");
            }
            double area;
            if (a == 0) a = solveSide(b, c, A);
            if (b == 0) b = solveSide(c, a, B);
            if (c == 0) c = solveSide(a, b, C);
            if (A == 0) A = solveAngle(b, c, a);
            if (B == 0) B = solveAngle(c, a, b);
            if (C == 0) C = solveAngle(a, b, c);
            if (A != 0) area = b * c * Math.sin(Math.toRadians(A)) / 2;
            if (B != 0) area = c * a * Math.sin(Math.toRadians(B)) / 2;
            if (C != 0) area = a * b * Math.sin(Math.toRadians(C)) / 2;

        } else {
            String status = "Side side angle (SSA) case - ";
            double knownSide, knownAngle, partialSide;
            if (a != 0 && A != 0) { knownSide = a; knownAngle = A; }
            if (b != 0 && B != 0) { knownSide = b; knownAngle = B; }
            if (c != 0 && C != 0) { knownSide = c; knownAngle = C; }
            if (a != 0 && A == 0) partialSide = a;
            if (b != 0 && B == 0) partialSide = b;
            if (c != 0 && C == 0) partialSide = c;
            if (knownAngle >= 180) {
                //throw status + "No solution";
                System.out.println("eror");
            }

            double ratio = knownSide / Math.sin(Math.toRadians(knownAngle));
            double temp = partialSide / ratio;  // sin(partialAngle)
            double partialAngle, unknownSide, unknownAngle;
            if (temp > 1 || knownAngle >= 90 && knownSide <= partialSide) {
                //throw status + "No solution";
                System.out.println("eror");
            }
            else if (temp == 1 || knownSide >= partialSide) {
                status += "Unique solution";
                partialAngle = Math.toDegrees(Math.asin(temp));
                unknownAngle = 180 - knownAngle - partialAngle;
                unknownSide = ratio * Math.sin(Math.toDegrees(unknownAngle));  // Law of sines
                double area = knownSide * partialSide * Math.sin(Math.toRadians(unknownAngle)) / 2;
            } else {
                status += "Two solutions";
                double partialAngle0 = Math.toDegrees(Math.asin(temp));
                double partialAngle1 = 180 - partialAngle0;
                double unknownAngle0 = 180 - knownAngle - partialAngle0;
                double unknownAngle1 = 180 - knownAngle - partialAngle1;
                double unknownSide0 = ratio * Math.sin(Math.toRadians(unknownAngle0));  // Law of sines
                double unknownSide1 = ratio * Math.sin(Math.toRadians(unknownAngle1));  // Law of sines
                partialAngle = partialAngle0;
                unknownAngle = unknownAngle0;
                unknownSide = unknownSide0;
                double area = knownSide * partialSide * Math.sin(Math.toRadians(unknownAngle0)) / 2;

            }
            if (a != 0 && A == 0) A = partialAngle;
            if (b != 0 && B == 0) B = partialAngle;
            if (c != 0 && C == 0) C = partialAngle;
            if (a == 0 && A == 0) { a = unknownSide; A = unknownAngle; }
            if (b == 0 && B == 0) { b = unknownSide; B = unknownAngle; }
            if (c == 0 && C == 0) { c = unknownSide; C = unknownAngle; }
            double [] array = {a, b, c, A, B, C, area};
            return array;
        }
    }
    */


}
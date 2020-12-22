package com.example.SudoKey;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class Nonogram {

    private  String[] p1 = {"C BA CB BB F AE F A B", "AB CA AE GA E C D C"};

    private  String[] p2 = {"F CAC ACAC CN AAA AABB EBB EAA ECCC HCCC", "D D AE "
            + "CD AE A DA BBB CC AAB BAA AAB DA AAB AAA BAB AAA CD BBA DA"};

    private  String[] p3 = {"CA BDA ACC BD CCAC CBBAC BBBBB BAABAA ABAD AABB BBH "
            + "BBBD ABBAAA CCEA AACAAB BCACC ACBH DCH ADBE ADBB DBE ECE DAA DB CC",
            "BC CAC CBAB BDD CDBDE BEBDF ADCDFA DCCFB DBCFC ABDBA BBF AAF BADB DBF "
                    + "AAAAD BDG CEF CBDB BBB FC"};

    private  String[] p4 = {"E BCB BEA BH BEK AABAF ABAC BAA BFB OD JH BADCF Q Q "
            + "R AN AAN EI H G", "E CB BAB AAA AAA AC BB ACC ACCA AGB AIA AJ AJ "
            + "ACE AH BAF CAG DAG FAH FJ GJ ADK ABK BL CM"};
    public String [][] solution =new String[20][20];
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Nonogram(int [][] hr, int [][] vr){
        String [] puzzleData = new String[2];
        String word="0ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder x= new StringBuilder();
        StringBuilder y= new StringBuilder();
        for(int i=0;i<hr.length;i++){
            for(int j=0;j<hr[i].length;j++) {
                x.append(word.charAt(hr[i][j]));
            }
            x.append(" ");
        }
        for(int i=0;i<vr.length;i++){
            for(int j=0;j<vr[i].length;j++) {
                y.append(word.charAt(vr[i][j]));
            }
            y.append(" ");
        }
        puzzleData[0]=x.toString();
        puzzleData[1]=y.toString();
        newPuzzle(puzzleData);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
     private void newPuzzle(String[] data) {
        String[] rowData = data[0].split("\\s");
        String[] colData = data[1].split("\\s");

        List<List<BitSet>> cols, rows;
        rows = getCandidates(rowData, colData.length);
        cols = getCandidates(colData, rowData.length);

        int numChanged;
        do {
            numChanged = reduceMutual(cols, rows);
            if (numChanged == -1) {
                return;
            }
        } while (numChanged > 0);
        StringBuilder str = new StringBuilder();
        int counter=0;
        for (List<BitSet> row : rows) {
            str=new StringBuilder("");
            for (int i = 0; i < cols.size(); i++) {
                solution[counter][i]=(String)(row.get(0).get(i) ? "# " : ". ");
            }
            counter++;
        }
    }

    // collect all possible solutions for the given clues
    @RequiresApi(api = Build.VERSION_CODES.N)
     List<List<BitSet>> getCandidates(String[] data, int len) {
        List<List<BitSet>> result = new ArrayList<>();

        for (String s : data) {
            List<BitSet> lst = new LinkedList<>();

            int sumChars = s.chars().map(c -> c - 'A' + 1).sum();
            List<String> prep = stream(s.split(""))
                    .map(x -> repeat(x.charAt(0) - 'A' + 1, "1")).collect(toList());

            for (String r : genSequence(prep, len - sumChars + 1)) {
                char[] bits = r.substring(1).toCharArray();
                BitSet bitset = new BitSet(bits.length);
                for (int i = 0; i < bits.length; i++)
                    bitset.set(i, bits[i] == '1');
                lst.add(bitset);
            }
            result.add(lst);
        }
        return result;
    }

    // permutation generator, translated from Python via D
    @RequiresApi(api = Build.VERSION_CODES.N)
     List<String> genSequence(List<String> ones, int numZeros) {
        if (ones.isEmpty())
            return asList(repeat(numZeros, "0"));

        List<String> result = new ArrayList<>();
        for (int x = 1; x < numZeros - ones.size() + 2; x++) {
            List<String> skipOne = ones.stream().skip(1).collect(toList());
            for (String tail : genSequence(skipOne, numZeros - x))
                result.add(repeat(x, "0") + ones.get(0) + tail);
        }
        return result;
    }

     String repeat(int n, String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++)
            sb.append(s);
        return sb.toString();
    }

    /* If all the candidates for a row have a value in common for a certain cell,
    then it's the only possible outcome, and all the candidates from the
    corresponding column need to have that value for that cell too. The ones
    that don't, are removed. The same for all columns. It goes back and forth,
    until no more candidates can be removed or a list is empty (failure). */

    @RequiresApi(api = Build.VERSION_CODES.N)
     int reduceMutual(List<List<BitSet>> cols, List<List<BitSet>> rows) {
        int countRemoved1 = reduce(cols, rows);
        if (countRemoved1 == -1)
            return -1;

        int countRemoved2 = reduce(rows, cols);
        if (countRemoved2 == -1)
            return -1;

        return countRemoved1 + countRemoved2;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
     int reduce(List<List<BitSet>> a, List<List<BitSet>> b) {
        int countRemoved = 0;

        for (int i = 0; i < a.size(); i++) {

            BitSet commonOn = new BitSet();
            commonOn.set(0, b.size());
            BitSet commonOff = new BitSet();

            // determine which values all candidates of ai have in common
            for (BitSet candidate : a.get(i)) {
                commonOn.and(candidate);
                commonOff.or(candidate);
            }

            // remove from bj all candidates that don't share the forced values
            for (int j = 0; j < b.size(); j++) {
                final int fi = i, fj = j;

                if (b.get(j).removeIf(cnd -> (commonOn.get(fj) && !cnd.get(fi))
                        || (!commonOff.get(fj) && cnd.get(fi))))
                    countRemoved++;

                if (b.get(j).isEmpty())
                    return -1;
            }
        }
        return countRemoved;
    }
}
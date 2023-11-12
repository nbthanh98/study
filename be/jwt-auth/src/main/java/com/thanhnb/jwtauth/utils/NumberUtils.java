package com.thanhnb.jwtauth.utils;

import java.util.ArrayList;
import java.util.List;

public class NumberUtils {

        /**
         * Split number into subset small number with max length.
         * @param numSplit: number split
         * @param maxSubLength: max length list sub-number
         * @return list<integer> sub-number
         */
        public static List<Integer> splitNum(int numSplit, int maxSubLength) {
                List<Integer> subsetNum = new ArrayList<>();

                int subNumber = numSplit / maxSubLength;
                int remainder = numSplit % maxSubLength;

                int sum = 0;
                for (int i = 0; i < remainder; i++) {
                        int num = subNumber + 1;
                        subsetNum.add(num);
                        sum = sum + num;
                }
                for (int i = 0; i < (maxSubLength - remainder); i ++) {
                        subsetNum.add(subNumber);
                        sum = sum + subNumber;
                }
                System.out.printf("numSplit=%s/sum=%s%n", numSplit, sum);
                return subsetNum;
        }

        public static void main(String[] args) {
                System.out.println(splitNum(497, 5));
                System.out.println(splitNum(411, 5));
                System.out.println(splitNum(425, 5));
                System.out.println(splitNum(359, 5));
                System.out.println(splitNum(497, 5));
        }
}

package com.thanhnb.jwtauth.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NumberUtils {

        /**
         * Split number into subset small number with max length.
         * @param numSplit: number split
         * @param maxSubLength: max length list sub-number
         * @return list<integer> sub-number
         */
        public static List<Long> splitNum(long numSplit, long maxSubLength) {
                List<Long> subsetNum = new ArrayList<>();

                long subNumber = numSplit / maxSubLength;
                long remainder = numSplit % maxSubLength;

                long sum = 0;
                for (int i = 0; i < remainder; i++) {
                        long num = subNumber + 1;
                        subsetNum.add(num);
                        sum = sum + num;
                }
                for (int i = 0; i < (maxSubLength - remainder); i++) {
                        subsetNum.add(subNumber);
                        sum = sum + subNumber;
                }
                System.out.printf("numSplit=%s/sum=%s%n", numSplit, sum);
                return subsetNum;
        }

        public static List<Long> splitNumWithMaxNum(long numSplit, long maxNum) {
                List<Long> subsetNum = new ArrayList<>();
                int defaultSubLength = 2;
                while ((numSplit / defaultSubLength) > maxNum) {
                        defaultSubLength++;
                }
                return splitNum(numSplit, defaultSubLength);
        }

        public static void main(String[] args) {
                for (int i = 0; i < 100; i++) {
                        long maxRandom = 599_999_999L;
                        long minRandom = 500_000_000L;
                        long splitNumRandom = (int) ((Math.random() * (maxRandom - minRandom)) + minRandom);
//                        System.out.println(splitNumWithMaxNum(splitNumRandom, 200_000_000L));
                        System.out.println(splitNum(splitNumRandom, 5)
                                .stream()
                                .map(v -> CurrencyUtils.toVNCurrency(BigDecimal.valueOf(v)))
                                .collect(Collectors.toList()));
                }
        }
}

package com.andon.springbootutil;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author Andon
 * 2024/3/5
 */
@Slf4j
public class Poker {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        String next = scanner.next();
        System.out.println("您输入了：" + next);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Card {
        String flowerColor;
        String number;
    }
}

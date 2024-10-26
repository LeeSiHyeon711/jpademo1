package org.lsh.jpademo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JpademoApplicationTests {

    class Solution {
        public int[] solution(int[] numbers) {
            int[] answer = {};
            int[] result = {};
            for(int i=0; i<answer.length; i++){
                result = new int[]{answer[i] * 2};
            }

            return result;
        }
    }
}

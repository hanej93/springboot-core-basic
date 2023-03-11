package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA : A사용자 10000원 주문
        statefulService1.order("userA", 10000);

        // ThreadB : B사용자 20000원 주문
        statefulService1.order("userA", 20000);
        
        // ThreadA: 사용자A 주문 금액 조회
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    @Test
    void statelessServiceSingleton() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatelessService statelessService1 = ac.getBean(StatelessService.class);
        StatelessService statelessService2 = ac.getBean(StatelessService.class);

        // ThreadA : A사용자 10000원 주문
        int price1 = statelessService1.order("userA", 10000);

        // ThreadB : B사용자 20000원 주문
        int price2 = statelessService2.order("userA", 20000);

        // ThreadA: 사용자A 주문 금액 조회
        System.out.println("price1 = " + price1);

        assertThat(price1).isEqualTo(10000);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }

        @Bean
        public StatelessService statelessService() {
            return new StatelessService();
        }
    }


}
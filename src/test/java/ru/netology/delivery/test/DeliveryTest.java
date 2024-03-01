package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

}

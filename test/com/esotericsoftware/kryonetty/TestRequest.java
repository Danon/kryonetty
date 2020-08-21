package com.esotericsoftware.kryonetty;

public class TestRequest implements Request
{
    public String someText;

    public TestRequest() {
    }

    public TestRequest(String someText) {
        this.someText = someText;
    }
}

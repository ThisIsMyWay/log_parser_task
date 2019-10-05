package com.test.recordparser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class LogItemParserTest {

    @Test
    public void convertShouldReturnFullyFilledObject() {
        //given
        String stringToConvert = "{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\",\n" +
                "\"host\":\"12345\", \"timestamp\":1491377495212}" + "{\"id\":\"scsmbstgraa\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\",\n" +
                "\"host\":\"12345\", \"timestamp\":1491377495212}";

        //when
        List<LogItem> convertedList = LogItemParser.convert(stringToConvert);

        //then
        LogItem expected = new LogItem("scsmbstgra", "STARTED", "APPLICATION_LOG", "12345", 1491377495212L);
        LogItem expected2 = new LogItem("scsmbstgraa", "STARTED", "APPLICATION_LOG", "12345", 1491377495212L);
        Assert.assertEquals(Arrays.asList(expected, expected2), convertedList);
    }
}
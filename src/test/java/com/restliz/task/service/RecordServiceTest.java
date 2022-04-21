package com.restliz.task.service;

import com.restliz.task.entity.Call;
import com.restliz.task.entity.Message;
import com.restliz.task.exception.ApiException;
import com.restliz.task.repository.CallRepository;
import com.restliz.task.repository.MessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {
    @Mock
    private CallRepository callRepository;
    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    RecordService recordService;

    @Test
    public void getCountryCodeOneChar(){
        Long code = 187634L;
        Integer result = recordService.getCountryCode(code);
        assertEquals(1, result);
    }

    @Test
    public void getCountryCodeTwoChars(){
        Long code = 34786L;
        Integer result = recordService.getCountryCode(code);
        assertEquals(34, result);
    }

    @Test
    public void getCountryCodeThreeChars(){
        Long code = 29123L;
        Integer result = recordService.getCountryCode(code);
        assertEquals(291, result);
    }

    @Test
    public void getCountryCodeCount(){
        Set<Long> set = new HashSet<>();
        set.add(1234L);
        set.add(5678L);
        set.add(5790L);
        Long result = recordService.getCountryCodeCount(set);
        assertEquals(3, result);
    }

    @Test
    public void getCountryCodeCountWithDuplications(){
        Set<Long> set = new HashSet<>();
        set.add(1234L);
        set.add(1678L);
        set.add(29786L);
        set.add(29721L);
        Long result = recordService.getCountryCodeCount(set);
        assertEquals(2, result);
    }

    @Test
    public void addCall(){
        Call call = new Call();
        recordService.addRecord(call);
        verify(callRepository).save(call);
    }

    @Test
    public void addMessage(){
        Message message = new Message();
        recordService.addRecord(message);
        verify(messageRepository).save(message);
    }

    @Test
    public void getGlobalMetrics(){
        List<Call> calls = new ArrayList<>();

        Call call1 = new Call();
        call1.setOrigin(12345L);
        call1.setDestination(52525L);
        calls.add(call1);

        Call call2 = new Call();
        call2.setOrigin(5673L);
        call2.setDestination(52333L);
        calls.add(call2);

        when(callRepository.findAll()).thenReturn(calls);
        recordService.getMetrics();

        Map<String, Long> result = recordService.getMetrics();
        Map<String, Long> expectedResult = new LinkedHashMap<>();
        expectedResult.put("row_count", 2L);
        expectedResult.put("call_count", 2L);
        expectedResult.put("message_count", 0L);
        expectedResult.put("unique_origin_cc_count", 2L);
        expectedResult.put("unique_destination_cc_count", 1L);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void getGlobalMetricsByDate(){
        List<Call> calls = new ArrayList<>();

        Call call1 = new Call();
        call1.setOrigin(12345L);
        call1.setDestination(52525L);
        call1.setTimestamp(1650570557000L);
        calls.add(call1);

        Call call2 = new Call();
        call2.setOrigin(5673L);
        call2.setDestination(52333L);
        call2.setTimestamp(1238762637000L);
        calls.add(call2);

        when(callRepository.findAll()).thenReturn(calls);

        Map<String, Long> result = recordService.getMetrics("220421");
        Map<String, Long> expectedResult = new LinkedHashMap<>();
        expectedResult.put("row_count", 1L);
        expectedResult.put("call_count", 1L);
        expectedResult.put("message_count", 0L);
        expectedResult.put("unique_origin_cc_count", 1L);
        expectedResult.put("unique_destination_cc_count", 1L);

        assertEquals(expectedResult, result);
    }

    @Test
    public void getGlobalMetricsByDateFailOnBadDateFormat(){
        ApiException thrownException = Assertions.assertThrows(ApiException.class, () -> {
            recordService.getMetrics("24.01.2023");
        });
        assertEquals("Bad date format", thrownException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrownException.getHttpStatus());
    }
}
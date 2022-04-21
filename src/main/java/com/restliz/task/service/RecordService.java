package com.restliz.task.service;

import com.restliz.task.entity.Call;
import com.restliz.task.entity.Message;
import com.restliz.task.entity.Record;
import com.restliz.task.exception.ApiException;
import com.restliz.task.repository.CallRepository;
import com.restliz.task.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecordService {
    private final CallRepository callRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public RecordService(CallRepository callRepository, MessageRepository messageRepository) {
        this.callRepository = callRepository;
        this.messageRepository = messageRepository;
    }

    public Integer getCountryCode(Long code){
        String codeStr = code.toString();
        if(codeStr.matches("^([17].*)")){
            return codeStr.charAt(0) - '0';
        }
        else if(codeStr.matches("^((27)|(3([0-469]))|(4[^2])|(5[^[09]])|(6[0-6])|(8([1-4]|6))|(9[0-5])).*")){
            return Integer.parseInt(codeStr.substring(0, 2));
        }
        else{
            return Integer.parseInt(codeStr.substring(0, 3));
        }
    }

    public Long getCountryCodeCount(Set<Long> codes){
        Set<Integer> countryCodes = new HashSet<>();
        codes.forEach((code) -> countryCodes.add(getCountryCode(code)));
        return (long) countryCodes.size();
    }

    public void addRecord(Call call){
        callRepository.save(call);
    }

    public void addRecord(Message message){
        messageRepository.save(message);
    }

    private Map<String, Long> getMetricsMap(List<Call> calls, long messageCount){
        long callCount = calls.size();
        Long rowsCount = callCount + messageCount;

        Set<Long> originCountryCodes = calls.stream().map(Record::getOrigin).collect(Collectors.toSet());
        Set<Long> destinationCountryCodes = calls.stream().map(Record::getDestination).collect(Collectors.toSet());

        Map<String, Long> result = new LinkedHashMap<>();
        result.put("row_count", rowsCount);
        result.put("call_count", callCount);
        result.put("message_count", messageCount);
        result.put("unique_origin_cc_count", getCountryCodeCount(originCountryCodes));
        result.put("unique_destination_cc_count", getCountryCodeCount(destinationCountryCodes));

        return result;
    }

    public Map<String, Long> getMetrics(){
        List<Call> calls = callRepository.findAll();
        long messageCount = messageRepository.count();
        return getMetricsMap(calls, messageCount);
    }

    public Map<String, Long> getMetrics(String dateString){
        DateFormat df = new SimpleDateFormat("yyMMdd");
        Date date;
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            throw new ApiException("Bad date format", HttpStatus.BAD_REQUEST);
        }

        List<Call> calls = callRepository.findAll().stream()
                .filter(call -> df.format(call.getTimestamp()).equals(df.format(date)))
                .collect(Collectors.toList());


        long messageCount = messageRepository.findAll().stream()
                .filter(message -> df.format(message.getTimestamp()).equals(df.format(date)))
                .count();

        return getMetricsMap(calls, messageCount);
    }
}

package com.poc.twilio.controller;

import com.poc.twilio.model.CallRequest;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OutboundCallController {
    @Value("${twilio.ph}")
    private String twilioPh;
    @Value("${twilio.username}")
    private String username;
    @Value("${twilio.password}")
    private String password;
    @Value("${manufacturer.ph}")
    private String manufacturerPh;

    @PostMapping("/call")
    public ResponseEntity<String> makeCall(@RequestBody CallRequest callRequest) {
        Twilio.init(username, password);

        Call call = Call
                .creator(
                        new com.twilio.type.PhoneNumber(manufacturerPh),
                        new com.twilio.type.PhoneNumber(twilioPh),
                        new com.twilio.type.Twiml("<Response><Say>"+callRequest.getMessage()+"</Say>\n" +
                                "    <Dial>"+callRequest.getPhoneNumber()+"</Dial>\n" +
                                "</Response>"))
                .create();

        return ResponseEntity.ok(call.getSid());

    }
}

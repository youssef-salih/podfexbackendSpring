package com.cosmomedia.location.controllers;

import com.cosmomedia.location.enums.StatusAdmin;
import com.cosmomedia.location.enums.StatusUser;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StatusUserControllers {

    @QueryMapping
    public List<String> statusUser(){
        return Arrays.stream(StatusUser.values())
                .map(StatusUser::name)
                .collect(Collectors.toList());
    }
}

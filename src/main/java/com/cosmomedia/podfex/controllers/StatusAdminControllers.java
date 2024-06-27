package com.cosmomedia.podfex.controllers;

import com.cosmomedia.podfex.enums.StatusAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StatusAdminControllers {

    @QueryMapping
    public List<String> statusAdmin(){
        return Arrays.stream(StatusAdmin.values())
                .map(StatusAdmin::name)
                .collect(Collectors.toList());
    }
}

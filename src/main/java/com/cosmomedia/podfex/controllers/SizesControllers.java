package com.cosmomedia.podfex.controllers;

import com.cosmomedia.podfex.enums.Sizes;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SizesControllers {

    @QueryMapping
    public List<String> sizes(){
        return Arrays.stream(Sizes.values())
                .map(Sizes::name)
                .collect(Collectors.toList());
    }
}

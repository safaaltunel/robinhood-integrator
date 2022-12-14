package com.safaaltunel.robinhoodintegrator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstrumentResponse {

    private String name;
    private String simple_name;
    private String market;

}

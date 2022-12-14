package com.safaaltunel.robinhoodintegrator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MarketResponse {

    private String mic;
    private String acronym;
    private String name;
    private String country;
    private String website;
}

package com.safaaltunel.robinhoodintegrator.model.response;

import com.safaaltunel.robinhoodintegrator.model.MarketResponse;
import lombok.Data;

import java.util.List;

@Data
public class Markets {

    private String next;
    private String previous;
    private List<MarketResponse> results;
}

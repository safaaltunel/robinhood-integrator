package com.safaaltunel.robinhoodintegrator.model.response;

import com.safaaltunel.robinhoodintegrator.model.InstrumentResponse;
import lombok.Data;

import java.util.List;

@Data
public class Instruments {

    private String next;
    private String previous;
    private List<InstrumentResponse> results;
}

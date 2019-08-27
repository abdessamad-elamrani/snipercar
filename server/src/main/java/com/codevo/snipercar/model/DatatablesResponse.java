package com.codevo.snipercar.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DatatablesResponse implements Serializable {
    private List<?> data;
    private int draw;//the NO.of requests
    private int length;
    private long recordsTotal;
    private long recordsFiltered;
}

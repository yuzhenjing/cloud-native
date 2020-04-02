package com.cloud.kxlist.brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KxPages<T> {

    private int totalPages;
    private int totalElements;
    private boolean last;
    private int numberOfElements;
    private boolean first;
    private String sort;
    private int size;
    private int number;
    private List<T> content;
}

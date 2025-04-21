package com.ecom.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatogeryResponse {
    private List<CatogeryDTO> content;

    private long pageNumber;
    private long pageSize;
    private long totalElements;
    private long totalPages;
    private boolean lastpage;
}

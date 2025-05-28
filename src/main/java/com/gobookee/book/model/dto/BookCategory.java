package com.gobookee.book.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCategory {
    private Integer bcCid;
    private Integer bcDept;
    private String bcCidName;
    private String bcCountry;
    private Integer bcDept1Cid;
    private String bcDept1Name;
    private Integer bcDept2Cid;
    private String bcDept2Name;
    private Integer bcDept3Cid;
    private String bcDept3Name;
    private Integer bcDept4Cid;
    private String bcDept4Name;
    private Integer bcDept5Cid;
    private String bcDept5Name;
}

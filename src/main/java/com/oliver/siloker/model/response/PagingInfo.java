package com.oliver.siloker.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PagingInfo<T> {

    public Integer currentPage;

    public Integer nextPage;

    public Integer prevPage;

    public Integer totalPage;

    public Long totalItem;

    public List<T> content;

    public static <T> PagingInfo<T> convertFromPage(Page<T> page) {
        PagingInfo<T> pagingInfo = new PagingInfo<>();
        pagingInfo.currentPage = page.getNumber() + 1;
        pagingInfo.totalPage = page.getTotalPages();
        pagingInfo.totalItem = page.getTotalElements();

        if (page.hasNext()) {
            pagingInfo.nextPage = (page.getNumber() + 1) + 1;
        }

        if (page.hasPrevious()) {
            if (pagingInfo.currentPage > pagingInfo.totalPage)
                pagingInfo.prevPage = pagingInfo.totalPage;
            else pagingInfo.prevPage = (page.getNumber() + 1) - 1;
        }

        pagingInfo.content = page.getContent();

        return pagingInfo;
    }
}


/*
 * @ (#) RecommendedJobPageDTO.java        1.0     12/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.dto;

import lombok.Data;

import java.util.List;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/13/2024
 */
@Data
public class RecommendedJobPageDTO {
    private List<JobRecommendation> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;

    // Thêm các helper methods
    public long getStartCount() {
        return (currentPage - 1) * pageSize + 1;
    }

    public long getEndCount() {
        return Math.min(currentPage * pageSize, totalElements);
    }

    public RecommendedJobPageDTO(List<JobRecommendation> content, int currentPage,
                                 int totalPages, long totalElements, int pageSize) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
        this.hasNext = currentPage < totalPages;
        this.hasPrevious = currentPage > 1;
    }
}
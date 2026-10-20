package com.example.post.dto;
/** 1페이지 10개, 페이지 번호는 5개씩 표시한다. */
public class Page {
    private final int page;
    private final int size = 10;
    private final long totalCount;
    private final int totalPages;
    private final int startPage;
    private final int endPage;
    public Page(int requestedPage, long totalCount) {
        this.totalCount = Math.max(0, totalCount);
        this.totalPages = (int) Math.max(1, (this.totalCount + size - 1) / size);
        this.page = Math.max(1, Math.min(requestedPage, totalPages));
        this.startPage = ((page - 1) / 5) * 5 + 1;
        this.endPage = Math.min(startPage + 4, totalPages);
    }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getOffset() { return (long) (page - 1) * size; }
    public long getTotalCount() { return totalCount; }
    public int getTotalPages() { return totalPages; }
    public int getStartPage() { return startPage; }
    public int getEndPage() { return endPage; }
    public boolean isHasPrevious() { return page > 1; }
    public boolean isHasNext() { return page < totalPages; }
}

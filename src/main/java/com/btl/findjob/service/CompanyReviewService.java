package com.btl.findjob.service;


import com.btl.findjob.model.CompanyReview;
import com.btl.findjob.model.CompanyReviewCriteria;
import com.btl.findjob.model.CompanyReviewPageDTO;

import java.util.List;
import java.util.Map;

public interface CompanyReviewService {

    //댓글 추가
    int insertCompanyReview(CompanyReview companyReview) throws Exception;

    //리뷰코펜트에서 페이징처리 위해
    CompanyReviewPageDTO getListWithPaging(CompanyReviewCriteria companyReviewCriteria, int ci_id, int cr_category) throws Exception;

    //총 평점
    double totalStarRtAve(String ci_companyName);

    //카테고리별 평점
    double categoryStarRtAve(String ci_companyName,int cr_category);

    //카테고리 이름 출력위해
    String categoryName(int cr_category);

    int getCountByCategory(String ci_companyName, int cr_category);

}
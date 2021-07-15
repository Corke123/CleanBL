package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.unibl.etf.ps.cleanbl.model.Report;
import org.unibl.etf.ps.cleanbl.model.ReportPage;
import org.unibl.etf.ps.cleanbl.model.ReportSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ReportCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public ReportCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Report> findAllWithFilters(ReportPage reportPage, ReportSearchCriteria reportSearchCriteria) {
        CriteriaQuery<Report> criteriaQuery = criteriaBuilder.createQuery(Report.class);
        Root<Report> reportRoot = criteriaQuery.from(Report.class);
        Predicate predicate = getPredicate(reportSearchCriteria, reportRoot);
        criteriaQuery.where(predicate);
        setOrder(reportPage,criteriaQuery,reportRoot);
        TypedQuery<Report> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(reportPage.getPageNumber() * reportPage.getPageSize());
        typedQuery.setMaxResults(reportPage.getPageSize());
        Pageable pageable =getPageable(reportPage);
        Long reportCount = getReportsCount(predicate);
        return new PageImpl<>(typedQuery.getResultList(),pageable,reportCount);

    }

    private Predicate getPredicate(ReportSearchCriteria reportSearchCriteria, Root<Report> reportRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(reportSearchCriteria.getStatus())){
            predicates.add(
                    criteriaBuilder.like(reportRoot.get("status"),"%" + reportSearchCriteria.getStatus() + "%")
            );
        }
        if(Objects.nonNull(reportSearchCriteria.getUserName())){
            predicates.add(
                    criteriaBuilder.like(reportRoot.get("userName"), "%" + reportSearchCriteria.getUserName() + "%")
            );
        }
        if(Objects.nonNull(reportSearchCriteria.getTitle())){
            predicates.add(
                    criteriaBuilder.like(reportRoot.get("title"), "%" + reportSearchCriteria.getTitle() + "%")
            );
        }
        if(Objects.nonNull(reportSearchCriteria.getCreatedAt())){
            predicates.add(
                    criteriaBuilder.like(reportRoot.get("createdAt"), "%" + reportSearchCriteria.getCreatedAt() + "%")
            );
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }


    private void setOrder(ReportPage reportPage, CriteriaQuery<Report> criteriaQuery, Root<Report> reportRoot) {
        if(reportPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(reportRoot.get(reportPage.getSortBy())));
        }else {
            criteriaQuery.orderBy(criteriaBuilder.desc(reportRoot.get(reportPage.getSortBy())));
        }
    }

    private Pageable getPageable(ReportPage reportPage) {
        Sort sort = Sort.by(reportPage.getSortDirection(), reportPage.getSortBy());
        return PageRequest.of(reportPage.getPageNumber(), reportPage.getPageSize(),sort);
    }


    private Long getReportsCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Report> countRoot = countQuery.from(Report.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}

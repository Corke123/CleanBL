package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.unibl.etf.ps.cleanbl.model.Department;
import org.unibl.etf.ps.cleanbl.model.Report;
import org.unibl.etf.ps.cleanbl.dto.ReportPage;
import org.unibl.etf.ps.cleanbl.dto.ReportSearchCriteria;

@Repository
public class ReportCriteriaRepository {
    private static final String ACTIVE = "active";
    private static final String INACTIVE = "inactive";
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
        setOrder(reportPage, criteriaQuery, reportRoot);
        TypedQuery<Report> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(reportPage.getPageNumber() * reportPage.getPageSize());
        typedQuery.setMaxResults(reportPage.getPageSize());
        Pageable pageable = getPageable(reportPage);
        Long reportCount = getReportsCount(predicate);
        return new PageImpl<>(typedQuery.getResultList(), pageable, reportCount);
    }

    public Page<Report> findAllWithFiltersForDepartmentOfficersReports(ReportPage reportPage,
                                                                       ReportSearchCriteria reportSearchCriteria,
                                                                       Department department) {
        CriteriaQuery<Report> criteriaQuery = criteriaBuilder.createQuery(Report.class);
        Root<Report> reportRoot = criteriaQuery.from(Report.class);
        Predicate predicate = getPredicateForDepartmentOfficersReports(reportSearchCriteria, reportRoot, department);
        criteriaQuery.where(predicate);
        setOrder(reportPage, criteriaQuery, reportRoot);
        TypedQuery<Report> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(reportPage.getPageNumber() * reportPage.getPageSize());
        typedQuery.setMaxResults(reportPage.getPageSize());
        Pageable pageable = getPageable(reportPage);
        Long reportCount = getReportsCount(predicate);
        return new PageImpl<>(typedQuery.getResultList(), pageable, reportCount);
    }

    private Predicate getPredicateForDepartmentOfficersReports(ReportSearchCriteria reportSearchCriteria,
                                                               Root<Report> reportRoot,
                                                               Department department) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(
                    criteriaBuilder.like(reportRoot.get("department").get("name"), "%" + department.getName() + "%")
        );

        if (Objects.nonNull(reportSearchCriteria.getStatus())) {
            if(reportSearchCriteria.getStatus().equals(ACTIVE))
                predicates.add(
                        criteriaBuilder.notLike(reportRoot.get("reportStatus").get("name"), "%" + "završen" + "%")
                );
            else if(reportSearchCriteria.getStatus().equals(INACTIVE)) {
            predicates.add(
                    criteriaBuilder.like(reportRoot.get("reportStatus").get("name"), "%" + "završen" + "%")
            );
            }
        }
        if (Objects.nonNull(reportSearchCriteria.getUsername())) {
            predicates.add(
                    criteriaBuilder.like(reportRoot.get("user").get("username"), "%" + reportSearchCriteria.getUsername() + "%")
            );
        }
        if (Objects.nonNull(reportSearchCriteria.getTitle())) {
            predicates.add(
                    criteriaBuilder.like(reportRoot.get("title"), "%" + reportSearchCriteria.getTitle() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate getPredicate(ReportSearchCriteria reportSearchCriteria, Root<Report> reportRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(reportSearchCriteria.getStatus())) {
            predicates.add(
                    criteriaBuilder.like(reportRoot.get("reportStatus").get("name"), "%" + reportSearchCriteria.getStatus() + "%")
            );
        }
        if (Objects.nonNull(reportSearchCriteria.getUsername())) {
            predicates.add(
                    criteriaBuilder.like(reportRoot.get("user").get("username"), "%" + reportSearchCriteria.getUsername() + "%")
            );
        }
        if (Objects.nonNull(reportSearchCriteria.getTitle())) {
            predicates.add(
                    criteriaBuilder.like(reportRoot.get("title"), "%" + reportSearchCriteria.getTitle() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(ReportPage reportPage, CriteriaQuery<Report> criteriaQuery, Root<Report> reportRoot) {
        if (reportPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(reportRoot.get(reportPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(reportRoot.get(reportPage.getSortBy())));
        }
    }

    private Pageable getPageable(ReportPage reportPage) {
        Sort sort = Sort.by(reportPage.getSortDirection(), reportPage.getSortBy());
        return PageRequest.of(reportPage.getPageNumber(), reportPage.getPageSize(), sort);
    }

    private Long getReportsCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Report> countRoot = countQuery.from(Report.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}

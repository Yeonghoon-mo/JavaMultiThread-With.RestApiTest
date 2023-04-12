package com.attoresearchtest.domain.host;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.attoresearchtest.domain.host.QHost.host;

public class HostRepositoryImpl implements HostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public HostRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // Host 개수 Count
    @Override
    public Long hostCount() {
        return queryFactory
                .select(Wildcard.count)
                .from(host)
                .where(host.deleteYn.eq("N"))
                .fetch().get(0);
    }

    // Host List
    @Override
    public List<Host> findByHostList() {
        return queryFactory
                .select(host)
                .from(host)
                .where(host.deleteYn.eq("N"))
                .orderBy(host.id.desc())
                .fetch();

    }
}

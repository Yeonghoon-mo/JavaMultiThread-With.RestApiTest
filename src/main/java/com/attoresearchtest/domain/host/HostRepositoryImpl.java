package com.attoresearchtest.domain.host;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import java.util.List;

import static com.attoresearchtest.domain.host.QHost.host;

public class HostRepositoryImpl implements HostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public HostRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // Host 개수 Count
    @Override
    public Long hostCount() {
        return queryFactory
                .selectFrom(host)
                .where(host.deleteYn.eq("N"))
                .fetchCount();
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

package com.attoresearchtest.domain.host;

import java.util.List;

public interface HostRepositoryCustom {

    // Host Count Check
    Long hostCount();

    // Host List
    List<Host> findByHostList();

}

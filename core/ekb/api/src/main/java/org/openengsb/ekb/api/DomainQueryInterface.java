package org.openengsb.ekb.api;

import java.util.List;

public interface DomainQueryInterface {

    <T> List<T> getAll(Class<T> type);

}

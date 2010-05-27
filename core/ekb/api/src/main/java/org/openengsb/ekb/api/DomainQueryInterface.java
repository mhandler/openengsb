package org.openengsb.ekb.api;

import java.util.List;

public interface DomainQueryInterface {

    <T> List<T> getAll(Class<T> type);

    <T> T getByKey(Class<T> type, String key);

}

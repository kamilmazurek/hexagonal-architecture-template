package template.application.port;

import template.application.usecase.Create;
import template.application.usecase.Delete;
import template.application.usecase.Read;
import template.application.usecase.Upsert;

public interface ItemRepositoryPort extends Create, Read, Upsert, Delete {

}

package template.application.port;

import template.application.usecase.Create;
import template.application.usecase.Delete;
import template.application.usecase.Insert;
import template.application.usecase.Read;

public interface ItemsRepositoryPort extends Create, Read, Insert, Delete {
}

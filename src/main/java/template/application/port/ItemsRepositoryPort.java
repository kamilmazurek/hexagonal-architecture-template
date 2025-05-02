package template.application.port;

import template.application.usecase.GetItem;
import template.application.usecase.GetItems;
import template.application.usecase.PutItem;

public interface ItemsRepositoryPort extends GetItem, GetItems, PutItem {
}
